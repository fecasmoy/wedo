package com.wedo.businessserver.css3.http;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wedo.businessserver.attemper.threadpool.SearchTask;
import com.wedo.businessserver.attemper.threadpool.SecurityTask;
import com.wedo.businessserver.attemper.threadpool.ThreadPoolManager;
import com.wedo.businessserver.cache.SupportNode;
import com.wedo.businessserver.common.dao.EntityDao;
import com.wedo.businessserver.common.dao.support.DAOUtil;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.DateUtils;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.common.util.RandomGUID;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WFileHistory;
import com.wedo.businessserver.css3.domain.Wapp;
import com.wedo.businessserver.css3.service.SystemParameter;
import com.wedo.businessserver.css3.service.WFileService;
import com.wedo.businessserver.css3.service.WFolderService;
import com.wedo.businessserver.css3.service.WRepositoryService;
import com.wedo.businessserver.css3.service.WSupportNodeService;
import com.wedo.businessserver.css3.service.util.DataServiceUtil;
import com.wedo.businessserver.css3.service.util.PictureUtils;
import com.wedo.businessserver.searchengine.IkIndexWriter;
import com.wedo.businessserver.storage.jcr.domain.MetaData;
import com.wedo.businessserver.storage.jcr.service.JCRService;

/**
 * 上传文件servlet
 * 
 * changed log: 20091205 by liuguoxia:增加linux下软链接操作
 * 1)将jFile,realname提出变为方法有效域，2)判断操作系统windows/linux，拼装上传文件后:result；3)注释原result
 * 
 * @author c90003207
 * 
 */
public class UploadServlet
    extends HttpServlet
{

    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -3636492744172226238L;

    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory
        .getLog(UploadServlet.class);

    /**
     * SIZE 1024
     */
    private static final int SIZE_1024 = 1024;

    /**
     * SIZE 2
     */
    private static final int SIZE_2 = 2;

    /**
     * SIZE 4
     */
    private static final int SIZE_4 = 4;

    /**
     * 需要生成缩略图的文件后缀名列表
     */
    private static final String IMAGE_FILE_PSTFIX = "|.bmp|.dip|.jpg|.jpeg|.jpe|.jfif|.gif|.tif|.tiff|.png|";
    
    /**
     * 生成缩略图的后缀
     */
    private static final String IMAGE_FILE_TARGET_PST = "-MINI";
    
    /**
     * 缩略图的宽度
     */
    private static final int TARGET_WIDTH = 74;
    
    /**
     * 缩略图的高度
     */
    private static final int TARGET_HEIGHTH = 58;
    /**
     * jcr service
     */
    private JCRService service;

    /**
     * File Service
     */
    private WFileService wFileService;

    /**
     * Folder Service
     */
    private WFolderService wFolderService;

    /**
     * Jdbc Template
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Repository Service
     */
    private WRepositoryService wRepositoryService;

    /**
     * Support Node Service
     */
    private WSupportNodeService wSupportNodeService;

    /**
     * i18n
     */
    private ResourceBundle msg = LanguageUtil
        .getMessage();

    /**
     * get folder service
     * 
     * @return return value
     */
    public WFolderService getWFolderService()
    {
        wFolderService = (WFolderService) BaseStaticContextLoader
            .getApplicationContext().getBean("wFolderService");
        return wFolderService;
    }

    /**
     * WSupportNodeService
     * 
     * @return WSupportNodeService
     */
    public WSupportNodeService getWSupportNodeService()
    {
        wSupportNodeService = (WSupportNodeService) BaseStaticContextLoader
            .getApplicationContext().getBean("wSupportNodeService");
        return wSupportNodeService;
    }

    /**
     * supportNodeService
     * 
     * @param supportNodeService supportNodeService
     */
    public void setWSupportNodeService(WSupportNodeService supportNodeService)
    {
        wSupportNodeService = supportNodeService;
    }

    /**
     * WRepositoryService
     * 
     * @return WRepositoryService
     */
    public WRepositoryService getWRepositoryService()
    {
        if (wRepositoryService == null)
        {
            wRepositoryService = (WRepositoryService) BaseStaticContextLoader
                .getApplicationContext().getBean("wRepositoryService");
        }
        return wRepositoryService;
    }

    /**
     * JdbcTemplate
     * 
     * @return JdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate()
    {
        if (jdbcTemplate == null)
        {
            jdbcTemplate = (JdbcTemplate) BaseStaticContextLoader
                .getApplicationContext().getBean("jdbcTemplate");
        }
        return jdbcTemplate;
    }

    /**
     * WFileService
     * 
     * @return WFileService
     */
    public WFileService getWFileService()
    {
        if (wFileService == null)
        {
            wFileService = (WFileService) BaseStaticContextLoader
                .getApplicationContext().getBean("wFileService");
        }
        return wFileService;
    }

    /**
     * WFileService
     * 
     * @param fileService WFileService
     */
    public void setWFileService(WFileService fileService)
    {
        wFileService = fileService;
    }

    /**
     * EntityDao
     * 
     * @return EntityDao
     */
    private static EntityDao<Wapp> getEntityDao()
    {
        EntityDao<Wapp> entityDao = DAOUtil
            .getEntityDao(Wapp.class);
        return entityDao;
    }

    /**
     * JCRService
     * 
     * @return JCRService
     */
    public JCRService getService()
    {
        if (service == null)
        {
            service = (JCRService) BaseStaticContextLoader
                .getApplicationContext().getBean("jCRService");
        }
        return service;
    }

    /**
     * service
     * 
     * @param service service
     */
    public void setService(JCRService service)
    {
        this.service = service;
    }

    /**
     * get method
     * 
     * @param req request
     * @param resp response
     * @throws ServletException exceptions
     * @throws IOException exceptions
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        logger
            .debug(msg
                .getString("msg.file.upload_start"));
        String accountGUID = null;
        String fileCreator = null;
        String backUrl = null;
        String appId = null;
        String rep = null;
        String folder = null;
        StringBuffer result = new StringBuffer();
        List<String> scanFileList = new ArrayList<String>();
        List<WFile> searchFileList = new ArrayList<WFile>();
        ThreadPoolManager threadPoolManager = ThreadPoolManager
            .getInstance();
        try
        {
            String root = SupportNode
                .getInstance().getHangsCarriesPath();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory
                .setSizeThreshold((SIZE_1024 * SIZE_1024) * SIZE_4);// 内存缓冲
            String tempFilePath = root + "/temp";
            if (!new File(tempFilePath)
                .isDirectory())
            {
                @SuppressWarnings("unused")
                Boolean flag = new File(tempFilePath)
                    .mkdirs();
            }
            // 临时文件目录
            factory
                .setRepository(new File(tempFilePath));
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 最大大小
            upload
                .setSizeMax((SIZE_1024 * SIZE_1024 * SIZE_1024) * SIZE_2);
            List items = upload
                .parseRequest(req);
            // 获取上传的参数
            Map<String, String> fileinfo = getFileinfo(items);
            accountGUID = fileinfo
                .get("accountGUID");
            fileCreator = fileinfo
                .get("fileCreator");
            backUrl = fileinfo
                .get("backUrl");
            appId = fileinfo
                .get("appId");
            rep = fileinfo
                .get("rep");
            folder = fileinfo
                .get("folder");
            // 检查应用是否存在
            Criteria criteria = getEntityDao()
                .getEntityCriteria();
            criteria
                .add(Restrictions
                    .eq("guid", appId));
            logger
                .debug("appId=" + appId);
            try
            {
                getEntityDao()
                    .getByQbc(criteria);
            }
            catch (Exception e)
            {
                logger
                    .info(msg
                        .getString("ERROR.00504"));
                throw new Exception("ERROR.00504", e);
            }
            // 依次处理上传文件
            Iterator iter = items
                .iterator();
            while (iter
                .hasNext())
            {
                FileItem item = (FileItem) iter
                    .next();
                String name = item
                    .getName();
                Map<String, String> cabinetMap = new HashMap<String, String>();
                cabinetMap
                    .put("appId", appId);
                cabinetMap
                    .put("repuuid", rep);
                // 检验仓库的合法性
                getWRepositoryService()
                    .getCabinet(cabinetMap);

                // 设置文件处理方法所需参数
                cabinetMap
                    .put("folder", folder);
                cabinetMap
                    .put("name", name);

                // 如果传入文件名不为空，执行文件上传处理
                if (StringUtils
                    .isNotBlank(name))
                {
                    Map<String, Object> reslutMap =
                        uploadFileDeal(root, item, cabinetMap);
                    String realPath = (String) reslutMap
                        .get("realPath");
                    StringBuffer filelink = (StringBuffer) reslutMap
                        .get("filelink");
                    WFile seachFile = (WFile) reslutMap
                        .get("wfileojbect");
                    scanFileList
                        .add(realPath);
                    searchFileList
                        .add(seachFile);
                    result
                        .append(filelink);
                }
            }
            // 文件处理完成,准备返回请求结果
            resp
                .setCharacterEncoding("UTF-8");
            logger
                .info(msg
                    .getString("msg.file.backurl") + backUrl + "?result="
                    + result + "&accountGUID=" + accountGUID + "&fileCreator="
                    + fileCreator);
            // CSSI处理完成,转像应用端回调处理
            resp
                .sendRedirect(backUrl + "?result=" + result + "&accountGUID="
                    + accountGUID + "&fileCreator=" + fileCreator);
            // 全文检索任务添加
            if (DataServiceUtil
                .getAuthorization(appId).getFunction().getSearchEngine()
                .getAvailable())
            {
                for (int i = 0; i < searchFileList
                    .size(); i++)
                {
                    threadPoolManager
                        .addTask(new SearchTask(
                            new IkIndexWriter(),
                            searchFileList
                                .get(i),
                            com.wedo.businessserver.searchengine.Constants.INCREMENT_FLAG));
                }
            }

            // 上传文件杀毒任务添加
            if (DataServiceUtil
                .getAuthorization(appId).getFunction().getScanVirus()
                .getAvailable())
            {
                for (int i = 0; i < scanFileList
                    .size(); i++)
                {
                    threadPoolManager
                        .addTask(new SecurityTask(scanFileList
                            .get(i)));
                }
            }
        }
        catch (Exception e)
        {
            logger
                .info(msg
                    .getString("msg.file.backurl") + backUrl + "?errormessage="
                    + e
                        .getMessage(), e);
            resp
                .sendRedirect(backUrl + "?errormessage=" + e
                    .getMessage());
        }
    }

    /**
     * post method
     * 
     * @param req request
     * @param resp response
     * @throws ServletException exceptions
     * @throws IOException exceptions
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        // TODO Auto-generated method stub
        doGet(req, resp);
    }

    /**
     * 上传文件处理执行言法
     * 
     * @param root S3数据写入根路径
     * @param item 上传对像
     * @param infoMap 上传文件MAP信息，包括：appId、repuuid、父folder、文件name
     * @throws Exception Exception
     */
    private Map<String, Object> uploadFileDeal(String root, FileItem item,
        Map<String, String> infoMap)
        throws Exception
    {

        Map<String, Object> reslutMap = new HashMap<String, Object>();

        File saveFile = null;
        File realFile = null;
        MetaData metaData = new MetaData();
        String realName = null;
        StringBuffer result = new StringBuffer();

        String appId = infoMap
            .get("appId");
        String rep = infoMap
            .get("repuuid");
        String folder = infoMap
            .get("folder");
        String name = infoMap
            .get("name");
        if (StringUtils
            .isNotBlank(name))
        {
            Calendar calendar = Calendar
                .getInstance();
            String time = DateUtils
                .format(calendar, "yyyy/MM/dd");
            File folderFile = new File(root + "/repository/date/" + time);
            if (!folderFile
                .exists())
            {
                @SuppressWarnings("unused")
                Boolean flag = folderFile
                    .mkdirs();
            }
            // 校验上传文件是否重名
            Map<String, String> map = getWFileService()
                .uploadvlidateName(appId, rep, folder, name
                    .substring(name
                        .lastIndexOf("\\") + 1, name
                        .length()));
            String folderGuid = map
                .get("folderGuid");
            RandomGUID filerandomGUID = new RandomGUID();
            String saveFileName = root + "/repository/date/" + filerandomGUID
            .getValueAfterMD5();
            saveFile = new File(saveFileName);
            item
                .write(saveFile);
            
            String realPath = "";
            if (map
                .get("flag").equals("true"))
            {
                // 上传重名文件时,更新生成元数据文件
                String fileuuid = map
                    .get("fileuuid");
                metaData = this
                    .getService().getMetaData(fileuuid, null);
                metaData
                    .setCreateDate(DateUtils
                        .format(Calendar
                            .getInstance(), "yyyy-MM-dd HH:mm:ss"));
                metaData
                    .setName(name
                        .substring(name
                            .lastIndexOf("\\") + 1, name
                            .length()));
                metaData
                    .setCheckin(true);
                metaData
                    .setSize(item
                        .getSize());
                this
                    .getService().updateMetaData(metaData);
                // 文件实体数据生成S3文件实体
                realName = metaData
                    .getUuid() + "-" + metaData
                    .getVersion();
                realPath =
                    root + "/repository/date/" + time + "/" + realName;
                realFile = new File(realPath);
                @SuppressWarnings("unused")
                Boolean flag = saveFile
                    .renameTo(realFile);
                // 添加上传结果:文件实体路径供杀毒使用
                reslutMap
                    .put("realPath", realPath);
                // 新增文件历史表
                String oldfileuuid = map
                    .get("fileuuid");
                HashMap<String, String> fileMap = new HashMap<String, String>();
                fileMap
                    .put("fileuuid", oldfileuuid);
                fileMap
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                WFile oldFile = getWFileService()
                    .getWFile(fileMap);
                WFileHistory fileHistory = new WFileHistory();
                BeanUtils
                    .copyProperties(fileHistory, oldFile);
                fileHistory
                    .setId(null);
                RandomGUID historyrandomGUID = new RandomGUID();
                fileHistory
                    .setGuid(historyrandomGUID
                        .getValueAfterMD5());
                getWFileService()
                    .saveWFileHistory(fileHistory);

                // 修改文件表中最新文件版本
                oldFile
                    .setVersion(metaData
                        .getVersion());
                oldFile
                    .setFileSize(item
                        .getSize());
                oldFile
                    .setFpath("/repository/date/" + time + "/" + realName);
                oldFile
                    .setFileCreateTime(Calendar
                        .getInstance());
                getWFileService()
                    .updateWFile(oldFile);
                // 设置文件的路径值，供搜索时使用
                HashMap<String, String> folderMap =
                    new HashMap<String, String>();
                folderMap
                    .put("guid", folderGuid);
                oldFile
                    .setFoldertreepath(getWFolderService()
                        .getFolder(folderMap).getTreepath());
                fileHistory
                    .setFoldertreepath(getWFolderService()
                        .getFolder(folderMap).getTreepath());
                oldFile
                    .setWFileHistory(fileHistory);
                // 全文检索任务添加
                reslutMap
                    .put("wfileojbect", oldFile);
            }
            else
            {
                // 不重名文件,新建元数据文件
                metaData
                    .setCheckin(true);
                metaData
                    .setCreateDate(DateUtils
                        .format(Calendar
                            .getInstance(), "yyyy-MM-dd HH:mm:ss"));
                metaData
                    .setName(name
                        .substring(name
                            .lastIndexOf("\\") + 1, name
                            .length()));
                if (StringUtils
                    .isNotBlank(folder))
                {
                    metaData
                        .setParentUuid(folder);
                }
                else
                {
                    metaData
                        .setParentUuid(rep);
                }
                metaData
                    .setSize(item
                        .getSize());
                metaData
                    .setAppGuid(appId);
                getService()
                    .createMetaData(metaData);
                // 文件实体数据生成S3文件实体
                realName = metaData
                    .getUuid() + "-" + metaData
                    .getVersion();
                realPath =
                    root + "/repository/date/" + time + "/" + realName;
                realFile = new File(realPath);
                @SuppressWarnings("unused")
                Boolean flag = saveFile
                    .renameTo(realFile);
                // 添加上传结果:文件实体路径供杀毒使用
                reslutMap
                    .put("realPath", realPath);
                // 插入文件表记录
                RandomGUID randomGUID = new RandomGUID();
                WFile wfile = new WFile();
                wfile
                    .setAppGuid(appId);
                wfile
                    .setDownNumber(new BigDecimal(0));
                wfile
                    .setFileCreateTime(Calendar
                        .getInstance());
                wfile
                    .setFName(name
                        .substring(name
                            .lastIndexOf("\\") + 1, name
                            .length()));
                wfile
                    .setFileSize(item
                        .getSize());
                wfile
                    .setFileuuid(metaData
                        .getUuid());
                wfile
                    .setVersion(metaData
                        .getVersion());
                wfile
                    .setVisitNumber(new BigDecimal(0));
                wfile
                    .setRepguid(appId);
                wfile
                    .setGuid(randomGUID
                        .getValueAfterMD5());
                wfile
                    .setFolderguid(folderGuid);
                wfile
                    .setAvaible(SystemParameter.FILE_FOLDER_AVIABLES);
                wfile
                    .setFpath("/repository/date/" + time + "/" + realName);
                wfile
                    .setMergeFlag(SystemParameter.UN_MERGE); // 设置文件不增量
                wfile
                    .setEncryptFlag(SystemParameter.UNENCRYPT_FLAG); // 设置文件不加密
                wfile
                    .setCompressFlag(SystemParameter.UNCOMPRESS_FLAG); // 设置文件不压缩
                wfile
                    .setAvaibleService(SystemParameter.UNAVAIBLE_SERVICE); // 设置文件不统计
                getWFileService()
                    .saveWFile(wfile);
                // 设置文件的路径值，供搜索时使用
                HashMap<String, String> folderMap =
                    new HashMap<String, String>();
                folderMap
                    .put("guid", folderGuid);
                wfile
                    .setFoldertreepath(getWFolderService()
                        .getFolder(folderMap).getTreepath());
                reslutMap
                    .put("wfileojbect", wfile);
            }
            // TODO
            String filePstFix = "|null|";
            if (name
                .lastIndexOf(".") != -1)
            {
                filePstFix = "|" + name
                    .substring(name
                        .lastIndexOf("."), name
                        .length()).toLowerCase(Locale
                            .getDefault()) + "|";
            }
            logger
                .info(filePstFix + " -- " + IMAGE_FILE_PSTFIX
                    .lastIndexOf(filePstFix));
            // 产生图片文件的缩略图
            if (IMAGE_FILE_PSTFIX
                .lastIndexOf(filePstFix) != -1)
            {
                PictureUtils
                    .resize(realPath, realPath + IMAGE_FILE_TARGET_PST, TARGET_WIDTH,
                        TARGET_HEIGHTH);
            }
            // 拼装上传后返回LINK信息
            result
                .append(metaData
                    .getUuid() + ":" + metaData
                    .getVersion() + ":" + metaData
                    .getSize() + ":" + java.net.URLEncoder
                    .encode(metaData
                        .getName(), "UTF-8") + ":" + metaData
                    .getParentUuid());

            Properties pros = System
                .getProperties();
            String osName = pros
                .getProperty("os.name");
            // 多文件链接地址以"|"为分隔符)
            if (osName
                .indexOf("Windows") != -1)
            {
                result =
                    result
                        .append(":" + "/reso/" + "repository/date/" + time
                            + "/" + realName + "|");
            }
            else
            {
                result =
                    result
                        .append(":" + "/reso/" + SupportNode
                            .getInstance().getSymName() + "/"
                            + "repository/date/" + time + "/" + realName + "|");
            }
            reslutMap
                .put("filelink", result);

        }
        return reslutMap;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getFileinfo(List items)
    {
        Map<String, String> fileinfo = new HashMap<String, String>();
        for (int m = 0; m < items
            .size(); m++)
        {
            FileItem item = (FileItem) items
                .get(m);
            if (item
                .getFieldName().equals("accountGUID"))
            {
                String accountGUID = item
                    .getString();
                fileinfo
                    .put("accountGUID", accountGUID);
                logger
                    .debug("accountGUID=" + accountGUID);
            }
            if (item
                .getFieldName().equals("fileCreator"))
            {
                String fileCreator = item
                    .getString();
                fileinfo
                    .put("fileCreator", fileCreator);

                logger
                    .debug("fileCreator=" + fileCreator);
            }
            if (item
                .getFieldName().equals("backUrl"))
            {
                String backUrl = item
                    .getString();
                fileinfo
                    .put("backUrl", backUrl);
                logger
                    .debug("backUrl=" + backUrl);
            }
            if (item
                .getFieldName().equals("appId"))
            {
                String appId = item
                    .getString();
                fileinfo
                    .put("appId", appId);
                logger
                    .debug("appId=" + appId);
            }
            if (item
                .getFieldName().equals("rep"))
            {
                String rep = item
                    .getString();
                fileinfo
                    .put("rep", rep);
                logger
                    .debug("rep=" + rep);
            }
            if (item
                .getFieldName().equals("folder"))
            {
                String folder = item
                    .getString();
                fileinfo
                    .put("folder", folder);
                logger
                    .debug("folder=" + folder);
            }

        }
        return fileinfo;
    }
}
