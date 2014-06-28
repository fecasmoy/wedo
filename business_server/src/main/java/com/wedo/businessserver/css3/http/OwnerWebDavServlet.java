package com.wedo.businessserver.css3.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.slide.authenticate.SecurityToken;
import org.apache.slide.common.Domain;
import org.apache.slide.webdav.WebdavException;
import org.apache.slide.webdav.WebdavMethod;
import org.apache.slide.webdav.WebdavServlet;
import org.apache.slide.webdav.WebdavServletConfig;
import org.apache.slide.webdav.util.WebdavStatus;
import org.apache.slide.webdav.util.WebdavUtils;

import com.wedo.businessserver.attemper.threadpool.SearchTask;
import com.wedo.businessserver.attemper.threadpool.SecurityTask;
import com.wedo.businessserver.attemper.threadpool.ThreadPoolManager;
import com.wedo.businessserver.cache.SupportNode;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.DateUtils;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.common.util.RandomGUID;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WFileHistory;
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.domain.Wapp;
import com.wedo.businessserver.css3.service.SystemParameter;
import com.wedo.businessserver.css3.service.WAppService;
import com.wedo.businessserver.css3.service.WFileService;
import com.wedo.businessserver.css3.service.WFolderService;
import com.wedo.businessserver.css3.service.WSupportNodeService;
import com.wedo.businessserver.css3.service.util.DataServiceUtil;
import com.wedo.businessserver.searchengine.IkIndexWriter;
import com.wedo.businessserver.storage.jcr.domain.MetaData;
import com.wedo.businessserver.storage.jcr.service.JCRService;

/**
 * 在线编辑servlet
 * 
 * @author c90003207
 * 
 */
public class OwnerWebDavServlet
    extends WebdavServlet
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = -5037425585858976286L;
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory
        .getLog(OwnerWebDavServlet.class);
    
    /**
     * INDEX 2
     */
    private static final int INDEX_2 = 2;
    
    /**
     * INDEX 3
     */
    private static final int INDEX_3 = 3;
    
    /**
     * INDEX 4
     */
    private static final int INDEX_4 = 4;
    
    /**
     * INDEX 5
     */
    private static final int INDEX_5 = 5;
    
    /**
     * INDEX 6
     */
    private static final int INDEX_6 = 6;
    
    /**
     * BYTE SIZE 8196
     */
    private static final int BYTE_SIZE_8196 = 8196;
    
    /**
     * 国际化用到的工具类
     */
    private ResourceBundle msg = LanguageUtil
        .getMessage();
    
    /**
     * 操作文件的Service
     */
    private WFileService wFileService;
    
    /**
     * 进行JCR操作的Service
     */
    private JCRService jCRService;
    
    /**
     * 云存储服务节点操作的Service
     */
    private WSupportNodeService wSupportNodeService;
    
    /**
     * 文件夹操作的Service
     */
    private WFolderService wFolderService;
    
    /**
     * 对应用进行操作的Service
     */
    private WAppService wAppService;
    
    /**
     * 线程池
     */
    private ThreadPoolManager threadPoolManager = ThreadPoolManager
        .getInstance();
    
    /**
     * 获取文件夹操作的Service
     * 
     * @return return value
     */
    public WFolderService getWFolderService()
    {
        // 从Spring容器中获取
        wFolderService = (WFolderService) BaseStaticContextLoader
            .getApplicationContext().getBean("wFolderService");
        return wFolderService;
    }
    
    /**
     * 获取应用操作的Service
     * 
     * @return return value
     */
    public WAppService getWAppService()
    {
        wAppService = (WAppService) BaseStaticContextLoader
            .getApplicationContext().getBean("wAppService");
        return wAppService;
    }
    
    /**
     * 获取云存储服务节点
     * 
     * @return return value
     */
    public WSupportNodeService getWSupportNodeService()
    {
        wSupportNodeService = (WSupportNodeService) BaseStaticContextLoader
            .getApplicationContext().getBean("wSupportNodeService");
        return wSupportNodeService;
    }
    
    /**
     * 设置云存储服务节点
     * 
     * @param supportNodeService support Node Service
     */
    public void setWSupportNodeService(WSupportNodeService supportNodeService)
    {
        wSupportNodeService = supportNodeService;
    }
    
    /**
     * 初始化方法
     * 
     * @throws ServletException Servlet Exception
     */
    @Override
    public void init()
        throws ServletException
    {
        super
            .init();
    }
    
    /**
     * 业务处理方法 实现在线编辑的业务逻辑
     * 
     * @param req req
     * @param resp resp
     * @throws ServletException Servlet Exception
     * @throws IOException IO Exception
     */
    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        // 输入输出流，记录被编辑文件的实体数据
        InputStream inputStream = null;
        OutputStream out = null;
        // 被编辑文件产生的临时文件
        File file = null;
        // 文件流
        FileOutputStream tempFile = null;
        // 实体文件和路径
        File realFile = null;
        String realPath = null;
        
        // 设置请求属性，供其它方法取用
        String result = WebdavUtils
            .getRelativePath(req, (WebdavServletConfig) getServletConfig());
        String root = null;
        String temppath = null;
        String s1[] = result
            .split("/");
        String s2[] = s1[INDEX_2]
            .split(";");
        String fileUuid = null;
        String appguid = null;
        // 读写
        if (s1.length == INDEX_3)
        {
            fileUuid = s2[INDEX_3];
            appguid = s2[INDEX_2];
        }
        // 只读
        else
        {
            fileUuid = s1[INDEX_4];
            appguid = s1[INDEX_3];
        }
        
        //过滤多余的空格
        fileUuid = fileUuid.trim();
        appguid = appguid.trim();
        
        // 判断应用增值服务权限
        if (!DataServiceUtil
            .getAuthorization(appguid).getFunction().getOnlineEdit()
            .getAvailable())
        {
            logger
                .error(msg
                    .getString("ERROR.00573"));// 该应用无在线编辑权限
            resp
                .sendError(WebdavStatus.SC_FORBIDDEN);
        }
        req
            .setAttribute("slide_uri", result);
        req
            .setAttribute("filename", fileUuid);
        logger
            .info("result=" + result);
        try
        {
            // 获取挂载路径
            root = SupportNode
                .getInstance().getHangsCarriesPath();
            // 提供临时文件存放路径
            if (root
                .endsWith("/"))
            {
                temppath = root + "temp";
            }
            else
            {
                temppath = root + "/temp";
            }
            req
                .setAttribute("temppath", temppath);
        }
        catch (BusinessException e1)
        {
            // 获取临时文件路径出现错误
            logger
                .error(e1
                    .getErrorMessage(), e1);
        }
        
        try
        {
            // 但令牌为空的时候进行初始化令牌
            if (token == null)
            {
                String namespaceName = req
                    .getContextPath();
                // 获取NameSpace
                if ((namespaceName == null) || (namespaceName
                    .equals("")))
                {
                    namespaceName = Domain
                        .getDefaultNamespace();
                }
                while (namespaceName
                    .startsWith("/"))
                {
                    namespaceName = namespaceName
                        .substring(1);
                }
                // 进行初始化令牌
                token = Domain
                    .accessNamespace(new SecurityToken(this), namespaceName);
            }
            resp
                .setStatus(WebdavStatus.SC_OK);// 设置http返回状态为200
            String methodName = req
                .getMethod();
            logger
                .info(msg
                    .getString("msg.system.action") + methodName);
            WebdavMethod method = methodFactory
                .createMethod(methodName);
            // 请求所用的方法如果不存在，在服务器端抛出异常
            if (method == null)
            {
                throw new WebdavException(WebdavStatus.SC_METHOD_NOT_ALLOWED);
            }
            else
            {
                // 处理PUT请求
                if (methodName
                    .equals("PUT"))
                {
                    inputStream = req
                        .getInputStream();// webdav上传文件 ：存放最新编辑内容
                    String filepath = null;
                    // 提供临时文件目录
                    if (!new File(temppath)
                        .isDirectory())
                    {
                        @SuppressWarnings("unused")
                        Boolean flag = new File(temppath)
                            .mkdirs();
                    }
                    // 写入临时文件
                    filepath = temppath + "/" + fileUuid;
                    file = new File(filepath);
                    if (file
                        .exists())
                    {
                        @SuppressWarnings("unused")
                        Boolean flag = file
                            .delete();
                    }
                    file = new File(filepath); // 如果已存在临时文件，则将期覆盖
                    int n = -1;
                    byte[] buffer = new byte[BYTE_SIZE_8196];
                    tempFile = new FileOutputStream(file);
                    while ((n = inputStream
                        .read(buffer, 0, BYTE_SIZE_8196)) > -1)
                    {
                        tempFile
                            .write(buffer, 0, n);
                    }
                    inputStream
                        .close();
                    tempFile
                        .close();
                }
                else if (methodName
                    .equals("GET"))
                { // 处理GET请求
                    // 获得文件流输出
                    /** 拆分字符串组装参数 */
                    String username = "";
                    String password = "";
                    String appId = "";
                    String version = "";
                    if (s1.length == INDEX_3)
                    {
                        username = s2[0];
                        password = s2[1];
                        appId = s2[INDEX_2];
                        fileUuid = s2[INDEX_3];
                        version = s2[INDEX_4];
                    }
                    else
                    {
                        username = s1[1];
                        password = s1[INDEX_2];
                        appId = s1[INDEX_3];
                        fileUuid = s1[INDEX_4];
                        version = s1[INDEX_5];
                    }
                    // 鉴权未通过
                    if (StringUtils
                        .isBlank(username) || StringUtils
                        .isBlank(password) || StringUtils
                        .isBlank(appId) || StringUtils
                        .isBlank(fileUuid) || StringUtils
                        .isBlank(version))
                    {
                        resp
                            .sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR);
                    }
                    logger
                        .info("fileUuid :" + fileUuid);
                    Map<String, String> fileMap = new HashMap<String, String>();
                    fileMap
                        .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                    fileMap
                        .put("fileuuid", fileUuid);
                    fileMap
                        .put("appguid", appId);
                    if (username
                        .equals("read"))
                    {
                        try
                        {
                            logger
                                .info(msg
                                    .getString("msg.file.search"));// 准备查询文件历史表
                            fileMap
                                .put("version", version);
                            WFileHistory fileHistory = getWFileService()
                                .getWFileHistory(fileMap);
                            realPath = fileHistory
                                .getFpath();// 查询文件历史表获得文件路径
                        }
                        catch (Exception e)
                        {
                            try
                            {
                                WFile wfile = getWFileService()
                                    .getWFile(fileMap);
                                realPath = wfile
                                    .getFpath();// 查询文件表获得文件路径
                            }
                            catch (Exception e2)
                            {
                                resp
                                    .sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR);
                            }
                        }
                    }
                    else
                    {
                        try
                        {
                            WFile wfile = getWFileService()
                                .getWFile(fileMap);
                            realPath = wfile
                                .getFpath();
                        }
                        catch (Exception e2)
                        {
                            resp
                                .sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR);
                        }
                    }
                    List<WSupportNode> list =
                        getWSupportNodeService()
                            .getWSupportByIp(
                            		com.wedo.businessserver.storage.jcr.Constants.LOCALIP);
                    for (int i = 0; i < list
                        .size(); i++)
                    {
                        String wroot = list
                            .get(i).getMountPath();
                        realFile = new File(wroot + realPath);
                        if (realFile
                            .exists() && realFile
                            .isFile())
                        {
                            break;
                        }
                    }
                    if (!realFile
                        .exists())
                    {
                        resp
                            .sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR);
                    }
                    else
                    {
                        inputStream = new FileInputStream(realFile);// 获取文件
                        int n = -1;
                        byte[] buffer = new byte[BYTE_SIZE_8196];
                        out = resp
                            .getOutputStream();
                        while ((n = inputStream
                            .read(buffer, 0, BYTE_SIZE_8196)) > -1)
                        {
                            out
                                .write(buffer, 0, n);
                        }
                        inputStream
                            .close();
                        out
                            .close();
                    }
                }
                else if (methodName
                    .equals("UNLOCK"))
                {
                    /** 拆分字符串组装参数 */
                    String username = s2[0];
                    String password = s2[1];
                    String appId = s2[INDEX_2];
                    fileUuid = s2[INDEX_3];
                    String version = s2[INDEX_4];
                    String backurl = s2[INDEX_5];
                    String path = s2[INDEX_6];
                    logger
                        .info("username=" + username);
                    logger
                        .info("password=" + password);
                    logger
                        .info("appId=" + appId);
                    logger
                        .info("fileUuid=" + fileUuid);
                    logger
                        .info("version=" + version);
                    logger
                        .info("backurl=" + backurl);
                    logger
                        .info("path=" + path);
                    
                    String filepath = null;
                    if (temppath
                        .endsWith("/"))
                    {
                        filepath = temppath + fileUuid;
                    }
                    else
                    {
                        filepath = temppath + "/" + fileUuid;
                    }
                    File tmpfile = new File(filepath);
                    if (tmpfile
                        .exists())
                    {
                        Map<String, String> fileMap =
                            new HashMap<String, String>();
                        fileMap
                            .put("avaible", "0");
                        fileMap
                            .put("fileuuid", fileUuid);
                        fileMap
                            .put("appguid", appId);
                        WFile wFile = null;
                        try
                        {
                            wFile = getWFileService()
                                .getWFile(fileMap);// 获得最新文件
                        }
                        catch (Exception e)
                        {
                            resp
                                .sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR);
                        }
                        logger
                            .info(msg
                                .getString("msg.webdav.dealfile") + "  : "
                                + fileUuid);
                        // JFile jFile = this.getJCRService().getFile(fileUuid);
                        // jFile.setCreateDate(Calendar.getInstance());
                        // jFile.setCheckin(true);
                        // this.getJCRService().updateFile(jFile);//
                        // jcr存储文件元数据，产生文件版本
                        MetaData metaData = this
                            .getJCRService().getMetaData(fileUuid, null);
                        metaData
                            .setCheckin(true);
                        metaData
                            .setCreateDate(DateUtils
                                .format(Calendar
                                    .getInstance(), "yyyy-MM-dd HH:mm:ss"));
                        metaData
                            .setSize(tmpfile
                                .length());
                        this
                            .getJCRService().updateMetaData(metaData);
                        String time = DateUtils
                            .format(Calendar
                                .getInstance(), "yyyy/MM/dd");
                        File folderFile =
                            new File(root + "/repository/date/" + time);
                        if (!folderFile
                            .exists())
                        {
                            @SuppressWarnings("unused")
                            Boolean flag = folderFile
                                .mkdirs();
                        }
                        String realName = metaData
                            .getUuid() + "-" + metaData
                            .getVersion();
                        realPath =
                            root + "/repository/date/" + time + "/" + realName;
                        realFile = new File(realPath);
                        @SuppressWarnings("unused")
                        Boolean flag = tmpfile
                            .renameTo(realFile);
                        WFileHistory fileHistory = new WFileHistory();
                        BeanUtils
                            .copyProperties(fileHistory, wFile);
                        fileHistory
                            .setId(null);
                        RandomGUID randomGUID = new RandomGUID();
                        fileHistory
                            .setGuid(randomGUID
                                .getValueAfterMD5());
                        getWFileService()
                            .saveWFileHistory(fileHistory);
                        
                        wFile
                            .setVersion(metaData
                                .getVersion());
                        wFile
                            .setFileSize(realFile
                                .length());
                        wFile
                            .setFpath("/repository/date/" + time + "/"
                                + realName);
                        wFile
                            .setMd5(null);
                        getWFileService()
                            .updateWFile(wFile);
                        WebdavStatus
                            .getMapTempFiles().remove(fileUuid);// 锁正常释放时,移除map列表中对应关系
                        
                        if (DataServiceUtil
                            .getAuthorization(appId).getFunction()
                            .getSearchEngine().getAvailable())
                        {
                            HashMap<String, String> folderMap =
                                new HashMap<String, String>();
                            folderMap
                                .put("guid", wFile
                                    .getFolderguid());
                            wFile
                                .setFoldertreepath(getWFolderService()
                                    .getFolder(folderMap).getTreepath());
                            fileHistory
                                .setFoldertreepath(getWFolderService()
                                    .getFolder(folderMap).getTreepath());
                            wFile
                                .setWFileHistory(fileHistory);// 设置之前的版本文件，全文检索实现
                            threadPoolManager
                                .addTask(new SearchTask(
                                    new IkIndexWriter(),
                                    wFile,
                                    com.wedo.businessserver.searchengine.Constants.INCREMENT_FLAG));// 线程池处理
                        }
                        // 需要杀毒
                        if (DataServiceUtil
                            .getAuthorization(appId).getFunction()
                            .getScanVirus().getAvailable())
                        {
                            threadPoolManager
                                .addTask(new SecurityTask(realPath));
                        }
                        Wapp app = getWAppService()
                            .getApp(appId);
                        String domainName = app
                            .getDomainName();
                        String ip = domainName
                            .substring(0, domainName
                                .indexOf("/"));
//                        WOffice woffice = new WOffice();
//                        woffice
//                            .office(ip, fileUuid, wFile
//                                .getVersion(), String
//                                .valueOf(realFile
//                                    .length()));// 通知客户端回调方法执行业务逻辑
                    }
                    File lockfile =
                        new File(WebdavStatus.LOCKFILE_ROOT + "/" + fileUuid);
                    if (lockfile
                        .exists())
                    {
                        @SuppressWarnings("unused")
                        Boolean flag = lockfile
                            .delete();
                    }
                    else
                    {
                        logger
                            .error(msg
                                .getString("ERROR.00575") + "  fileuuid: "
                                + fileUuid);// 在线编辑件锁不存在
                    }
                    method
                        .run(req, resp);
                }
                else
                {
                    method
                        .run(req, resp);
                }
            }
        }
        catch (WebdavException e)
        {
            try
            {
                resp
                    .sendError(e
                        .getStatusCode());
            }
            catch (Throwable ex)
            {
                logger
                    .info("send request error.");
            }
        }
        catch (Throwable e)
        {
            e
                .printStackTrace();
            try
            {
                resp
                    .sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR);
            }
            catch (Throwable ex)
            {
                logger
                    .info("send request error.");
            }
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream
                    .close();
            }
            if (out != null)
            {
                out
                    .close();
            }
            if (tempFile != null)
            {
                tempFile
                    .close();
            }
        }
    }
    
    /**
     * getter
     * 
     * @return WFileService
     */
    public WFileService getWFileService()
    {
        wFileService = (WFileService) BaseStaticContextLoader
            .getApplicationContext().getBean("wFileService");
        return wFileService;
    }
    
    /**
     * setter
     * 
     * @param fileService fileService
     */
    public void setWFileService(WFileService fileService)
    {
        wFileService = fileService;
    }
    
    /**
     * 获取云存储服务节点操作的Service
     * 
     * @return JCRService JCRService
     */
    public JCRService getJCRService()
    {
        jCRService = (JCRService) BaseStaticContextLoader
            .getApplicationContext().getBean("jCRService");
        return jCRService;
    }
    
    /**
     * 获取云存储服务节点操作的Service
     * 
     * @param service service
     */
    public void setJCRService(JCRService service)
    {
        jCRService = service;
    }
    
}
