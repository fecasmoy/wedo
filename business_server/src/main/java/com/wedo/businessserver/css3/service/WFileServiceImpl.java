package com.wedo.businessserver.css3.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.wedo.businessserver.attemper.threadpool.SearchTask;
import com.wedo.businessserver.attemper.threadpool.SecurityTask;
import com.wedo.businessserver.attemper.threadpool.ThreadPoolManager;
import com.wedo.businessserver.cache.SupportNode;
import com.wedo.businessserver.common.dao.support.PageInfo;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.ParameterException;
import com.wedo.businessserver.common.util.DateUtils;
import com.wedo.businessserver.common.util.RandomGUID;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WFileHistory;
import com.wedo.businessserver.css3.domain.WFolder;
import com.wedo.businessserver.css3.domain.WRepository;
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.domain.Wapp;
import com.wedo.businessserver.css3.service.util.DataServiceUtil;
import com.wedo.businessserver.css3.service.util.PictureUtils;
import com.wedo.businessserver.css3.ws.model.FileBuffer;
import com.wedo.businessserver.css3.ws.model.Keyassignments;
import com.wedo.businessserver.heartbeat.ClientFactory;
import com.wedo.businessserver.heartbeat.Constants;
import com.wedo.businessserver.searchengine.IkIndexWriter;
import com.wedo.businessserver.storage.jcr.domain.MetaData;

/**
 * 文件操作接口实现类
 * 
 * @author c90003207
 * 
 */
@Service("wFileService")
public class WFileServiceImpl
    extends WOnlineAbstractServiceImpl
    implements WFileService
{
    private static HashMap<String, String> cacheMap =
        new HashMap<String, String>();

    /**
     * INDEX 0
     */
    private static final int INDEX_0 = 0;

    /**
     * INDEX 1
     */
    private static final int INDEX_1 = 1;

    /**
     * INDEX 2
     */
    private static final int INDEX_2 = 2;

    /**
     * INDEX 3
     */
    private static final int INDEX_3 = 3;

    /**
     * VALUE 100
     */
    private static final long VALUE_100 = 100L;

    /**
     * 需要生成缩略图的文件后缀名列表
     */
    private static final String IMAGE_FILE_PSTFIX =
        "|.bmp|.dip|.jpg|.jpeg|.jpe|.jfif|.gif|.tif|.tiff|.png|";

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
     * Thread Pool Manager
     */
    private ThreadPoolManager threadPoolManager = ThreadPoolManager
        .getInstance();

    /**
     * 重命名文件
     * 
     * @author c90003210
     * @param appId 应用guid
     * @param fileUid 文件uuid
     * @param newfilename 新文件名
     * @throws BusinessException 业务异常
     */
    public void renameFile(String appId, String fileUid, String newfilename)
        throws BusinessException
    {
        try
        {
            // logger.info("重命名文件夹");
            logger
                .debug("fileUid       =" + fileUid);
            logger
                .debug("newfilename   =" + newfilename);
            if (StringUtils
                .isBlank(appId) || StringUtils
                .isBlank(fileUid))
            {
                throw new ParameterException("ERROR.00500");
            }
            Map<String, String> fileMap = new HashMap<String, String>();
            fileMap
                .put("appguid", appId);
            fileMap
                .put("fileuuid", fileUid);
            fileMap
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            WFile wfile = getWFile(fileMap);
            if (wfile
                .getFName().trim().equals(newfilename
                    .trim()))
            {
                throw new ParameterException("ERROR.00501");
            }
            validFileName(wfile
                .getFolderguid(), newfilename);
            MetaData metaData = this
                .getJCRService().getMetaData(wfile
                    .getFileuuid(), null);
            metaData
                .setName(newfilename);
            metaData
                .setCheckin(false);
            this
                .getJCRService().updateMetaData(metaData);
            wfile
                .setFName(newfilename);
            wfile
                .setFileCreateTime(Calendar
                    .getInstance());
            getFileEntityDao()
                .update(wfile);
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00521"));
            throw new BusinessException("ERROR.00521", e);
        }
    }

    /**
     * copy File
     * 
     * @param appId app Id
     * @param cabinetuuid cabinet uuid
     * @param folderuuid folder uuid
     * @param fileuuid file uuid
     * @return {@link FileBuffer}
     * @throws BusinessException Business Exception
     */
    public FileBuffer copyFile(String appId, String cabinetuuid,
        String folderuuid, String fileuuid)
        throws BusinessException
    {
        File oldfile = null;
        File newfile = null;
        try
        {
            // logger.info("复制文件");
            if (StringUtils
                .isBlank(appId) || StringUtils
                .isBlank(cabinetuuid))
            {
                throw new ParameterException("ERROR.00500");
            }
            String writeRoot = SupportNode
                .getInstance().getHangsCarriesPath();
            Calendar calendar = Calendar
                .getInstance();
            String time = DateUtils
                .format(calendar, "yyyy/MM/dd");
            File folderFile = new File(writeRoot + "/repository/date/" + time);
            createPaFolder(folderFile);
            FileBuffer fBuffer = new FileBuffer();
            Map<String, String> fileMap = new HashMap<String, String>();
            fileMap
                .put("appguid", appId);
            fileMap
                .put("fileuuid", fileuuid);
            fileMap
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            WFile wfile = getWFile(fileMap);
            Map<String, String> cabinetMap = new HashMap<String, String>();
            cabinetMap
                .put("appId", appId);
            cabinetMap
                .put("repuuid", cabinetuuid);
            WRepository wRepository = getCabinet(cabinetMap);
            String targetFolderuuid = null;
            String targetFolderguid = null;
            if (StringUtils
                .isBlank(folderuuid))
            {
                targetFolderuuid = cabinetuuid;
                targetFolderguid = wRepository
                    .getGuid();
            }
            else
            {
                Map<String, String> folderMap = new HashMap<String, String>();
                folderMap
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                folderMap
                    .put("appguid", appId);
                folderMap
                    .put("folderuuid", folderuuid);
                WFolder wFolder = getFolder(folderMap);
                targetFolderuuid = folderuuid;
                targetFolderguid = wFolder
                    .getGuid();
            }
            if (wfile
                .getFolderguid().equals(targetFolderguid))
            {
                throw new ParameterException("ERROR.00501");
            }
            validFileName(targetFolderguid, wfile
                .getFName());
            WFile newWFile = new WFile();
            MetaData metaData = this
                .getJCRService().getMetaData(fileuuid, null);
            MetaData newMetaData = new MetaData();
            // JFile jFile = this.getJCRService().getFile(fileuuid);
            // JFile newJfile = new JFile();
            newMetaData
                .setName(metaData
                    .getName());
            newMetaData
                .setParentUuid(targetFolderuuid);
            newMetaData
                .setCheckin(true);
            newMetaData
                .setAppGuid(appId);
            newMetaData
                .setSize(metaData
                    .getSize());
            newMetaData
                .setCreateDate(DateUtils
                    .format(Calendar
                        .getInstance(), "yyyy-MM-dd HH:mm:ss"));
            getJCRService()
                .createMetaData(newMetaData);
            String newName = newMetaData
                .getUuid() + "-" + newMetaData
                .getVersion();
            List<WSupportNode> list =
                getWSupportByIp(com.wedo.businessserver.storage.jcr.Constants.LOCALIP);
            for (int i = 0; i < list
                .size(); i++)
            {
                String readroot = list
                    .get(i).getMountPath();
                oldfile = new File(readroot + wfile
                    .getFpath());
                if (oldfile
                    .exists() && oldfile
                    .isFile())
                {
                    break;
                }
            }
            if (oldfile == null)
            {
                throw new BusinessException("ERROR.00520");
            }
            String realPath =
                writeRoot + "/repository/date/" + time + "/" + newName;
            newfile = new File(realPath);
            FileUtils
                .copyFile(oldfile, newfile);
            // TODO 拷贝文件产生新的数据后，需要对图片文件产生缩略图
            String name = metaData
                .getName();
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
            // 缩略图生成完毕

            RandomGUID randomGUID = new RandomGUID();
            fBuffer
                .setFileuuid(newMetaData
                    .getUuid());
            fBuffer
                .setVersion(newMetaData
                    .getVersion());
            newWFile
                .setAppGuid(appId);
            newWFile
                .setDownNumber(BigDecimal
                    .valueOf(0));
            newWFile
                .setFileCreateTime(Calendar
                    .getInstance());
            newWFile
                .setFName(wfile
                    .getFName());
            newWFile
                .setFileSize(wfile
                    .getFileSize());
            newWFile
                .setFileuuid(newMetaData
                    .getUuid());
            newWFile
                .setVersion(newMetaData
                    .getVersion());
            newWFile
                .setVisitNumber(BigDecimal
                    .valueOf(0));
            newWFile
                .setRepguid(wRepository
                    .getGuid());
            newWFile
                .setCsnguid(null);
            newWFile
                .setGuid(randomGUID
                    .getValueAfterMD5());
            newWFile
                .setFolderguid(targetFolderguid);
            newWFile
                .setAvaible(SystemParameter.FILE_FOLDER_AVIABLES);
            newWFile
                .setFpath("/repository/date/" + time + "/" + newName);
            newWFile
                .setMergeFlag(wfile
                    .getMergeFlag());
            newWFile
                .setEncryptFlag(wfile
                    .getEncryptFlag());
            newWFile
                .setCompressFlag(wfile
                    .getCompressFlag());
            newWFile
                .setAvaibleService(wfile
                    .getAvaibleService());
            newWFile
                .setMd5(wfile
                    .getMd5());
            getFileEntityDao()
                .save(newWFile);
            copyFileTask(appId, newWFile, targetFolderguid);
            return fBuffer;
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00522"));
            throw new BusinessException("ERROR.00522", e);
        }

    }

    /**
     * copy Files
     * 
     * @param appId app Id
     * @param cabinetuuid cabinet uuid
     * @param folderuuid folder uuid
     * @param fileuuids file uuids
     * @return {@link List}
     * @throws BusinessException Business Exception
     */
    public List<FileBuffer> copyFiles(String appId, String cabinetuuid,
        String folderuuid, List<String> fileuuids)
        throws BusinessException
    {
        List<FileBuffer> list = new ArrayList<FileBuffer>();
        try
        {
            // logger.info("批量拷贝文件");
            if (StringUtils
                .isBlank(appId) || StringUtils
                .isBlank(cabinetuuid))
            {
                throw new ParameterException("ERROR.00500");
            }
            if (fileuuids == null || fileuuids
                .size() <= 0 || StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00500");
            }
            for (int i = 0; i < fileuuids
                .size(); i++)
            {
                try
                {
                    list
                        .add(copyFile(appId, cabinetuuid, folderuuid, fileuuids
                            .get(i)));
                }
                catch (Exception e)
                {
                    // 捕获异常,不处理.原因:批量操作不因个别失败中止.
                    logger
                        .info(MSG
                            .getString("msg.file") + fileuuids
                            .get(i) + MSG
                            .getString("msg.file.copyerror"));
                }
            }
            return list;
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00523"));
            throw new BusinessException("ERROR.00523", e);
        }
    }

    /**
     * move File
     * 
     * @param appId app Id
     * @param cabinetuuid cabinet uuid
     * @param fileuid file uid
     * @param folderuid folder uid
     * @return {@link FileBuffer}
     * @throws BusinessException Business Exception
     */
    public FileBuffer moveFile(String appId, String cabinetuuid,
        String fileuid, String folderuid)
        throws BusinessException
    {
        try
        {
            if (StringUtils
                .isBlank(appId) || StringUtils
                .isBlank(fileuid) || StringUtils
                .isBlank(folderuid))
            {
                throw new ParameterException("ERROR.00500");
            }
            Map<String, String> fileMap = new HashMap<String, String>();
            fileMap
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            fileMap
                .put("fileuuid", fileuid);
            fileMap
                .put("appguid", appId);
            WFile wfile = getWFile(fileMap);
            String targetFolderguid = null;
            if (StringUtils
                .isBlank(folderuid))
            {
                Map<String, String> cabinetMap = new HashMap<String, String>();
                cabinetMap
                    .put("appId", appId);
                cabinetMap
                    .put("repuuid", folderuid);
                WRepository wRepository = getCabinet(cabinetMap);
                targetFolderguid = wRepository
                    .getGuid();
            }
            else
            {
                Map<String, String> folderMap = new HashMap<String, String>();
                folderMap
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                folderMap
                    .put("folderuuid", folderuid);
                folderMap
                    .put("appguid", appId);
                WFolder wFolder = getFolder(folderMap);
                targetFolderguid = wFolder
                    .getGuid();
            }
            if (wfile
                .getFolderguid().equals(targetFolderguid))
            {
                throw new ParameterException("ERROR.00501");
            }
            MetaData metaData = getJCRService()
                .getMetaData(wfile
                    .getFileuuid(), null);
            metaData
                .setParentUuid(folderuid);
            metaData
                .setCheckin(false);
            metaData
                .setCreateDate(DateUtils
                    .format(Calendar
                        .getInstance(), "yyyy-MM-dd HH:mm:ss"));
            this
                .getJCRService().updateMetaData(metaData);
            validFileName(targetFolderguid, wfile
                .getFName());
            FileBuffer filebuffer = new FileBuffer();
            filebuffer
                .setFileuuid(fileuid);
            wfile
                .setFolderguid(targetFolderguid);
            wfile
                .setFileCreateTime(Calendar
                    .getInstance());
            wfile
                .setMd5(null);
            getFileEntityDao()
                .update(wfile);
            return filebuffer;
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00526"));
            throw new BusinessException("ERROR.00526", e);
        }
    }

    /**
     * move Files
     * 
     * @param appId app Id
     * @param cabinetuuid cabinet uuid
     * @param fileuid file uid
     * @param folderuid folder uid
     * @return {@link List}
     * @throws BusinessException Business Exception
     */
    public List<FileBuffer> moveFiles(String appId, String cabinetuuid,
        List<String> fileuid, String folderuid)
        throws BusinessException
    {
        List<FileBuffer> list = new ArrayList<FileBuffer>();
        try
        {
            if (StringUtils
                .isBlank(appId) || StringUtils
                .isBlank(cabinetuuid))
            {
                throw new ParameterException("ERROR.00400");
            }
            if (fileuid == null || fileuid
                .size() <= 0 || StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00401");
            }
            for (int i = 0; i < fileuid
                .size(); i++)
            {
                try
                {
                    list
                        .add(moveFile(appId, cabinetuuid, fileuid
                            .get(i), folderuid));
                }
                catch (Exception e)
                {
                    logger
                        .info("file" + fileuid
                            .get(i) + "copy error!");
                }
            }
            return list;
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00527"));
            throw new BusinessException("ERROR.00527", e);
        }
    }

    /**
     * remove File
     * 
     * @param appId app Id
     * @param version version
     * @param fileuid file uid
     * @param flag flag
     * @throws BusinessException Business Exception
     */
    public void removeFile(String appId, String version, String fileuid,
        String flag)
        throws BusinessException
    {
        try
        {
            // logger.info("开始删除文件");
            Map<String, String> map = new HashMap<String, String>();
            map
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            map
                .put("fileuuid", fileuid);
            map
                .put("appguid", appId);
            String nowversion = null;

            // 当无具体版本时,全版本删除
            if (StringUtils
                .isBlank(version))
            {
                WFile file = getWFile(map);
                nowversion = file
                    .getVersion();
            }

            // 全版本删除
            if (version
                .equalsIgnoreCase(nowversion) || StringUtils
                .isBlank(version))
            {
                getWFile(map);
                String sqlfile =
                    "update ci_file c set c.avaible=? where c.avaible=? and c.fileuuid=? and c.appguid=?";
                Object[] sqlfilevalues =
                    new Object[] {flag, SystemParameter.FILE_FOLDER_AVIABLES,
                        fileuid, appId};
                // 修改文件表
                getJdbcTemplate()
                    .update(sqlfile, sqlfilevalues);
                String sqlfilehistory =
                    "update ci_file_history c set c.avaible=? where c.avaible=? and c.fileuuid=? and c.appguid=?";
                Object[] sqlfilehistoryvalues =
                    new Object[] {flag, SystemParameter.FILE_FOLDER_AVIABLES,
                        fileuid, appId};
                // 修改文件历史表
                getJdbcTemplate()
                    .update(sqlfilehistory, sqlfilehistoryvalues);
            }
            // 指定版本删除
            else
            {
                map
                    .put("version", version);
                // 执行指定版本删除操作
                delVersionFile(map, flag);
            }
            WFile wfile = new WFile();
            wfile
                .setFileuuid(fileuid);
            wfile
                .setVersion(version);
            if (DataServiceUtil
                .getAuthorization(appId).getFunction().getSearchEngine()
                .getAvailable())
            {
                threadPoolManager
                    .addTask(new SearchTask(new IkIndexWriter(), wfile,
                        com.wedo.businessserver.searchengine.Constants.DELETE_MARK));
                String message = "fileuuid=" + wfile
                    .getFileuuid() + ";version=" + wfile
                    .getVersion() + ";";
                ClientFactory
                    .run(Constants.INDEX_DEL, message);
            }
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            logger
                .info("appId:   " + appId);
            logger
                .info("version: " + version);
            logger
                .info("fileuid: " + fileuid);
            logger
                .info("flag:    " + flag);
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00529"));
            throw new BusinessException("ERROR.00529", e);
        }
    }

    /**
     * remove Files
     * 
     * @param appId app Id
     * @param fileuuids file uuids
     * @param flag fiag
     * @return {@link List}
     * @throws BusinessException Business Exception
     */
    public List<String> removeFiles(String appId, List<String> fileuuids,
        String flag)
        throws BusinessException
    {
        try
        {
            // logger.info("开始文件批量删除");
            if (StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00500");
            }
            List<String> list = new ArrayList<String>();
            if (fileuuids == null || fileuuids
                .size() <= 0)
            {
                throw new ParameterException("ERROR.00500");
            }
            for (int i = 0; i < fileuuids
                .size(); i++)
            {
                String fileuuid = fileuuids
                    .get(i);
                try
                {
                    removeFile(appId, "", fileuuid, flag);
                    list
                        .add(fileuuid);
                }
                catch (Exception e)
                {
                    logger
                        .info(MSG
                            .getString("msg.file") + fileuuid + MSG
                            .getString("ERROR.00529"));
                }
            }
            return list;
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00530"));
            throw new BusinessException("ERROR.00530", e);
        }
    }

    /**
     * save WFile History
     * 
     * @param wfileHistory wfile History
     * @throws BusinessException Business Exception
     */
    public void saveWFileHistory(WFileHistory wfileHistory)
        throws BusinessException
    {
        try
        {
            getFileHistoryEntityDao()
                .save(wfileHistory);
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }

    }

    /**
     * update WFile
     * 
     * @param wfile wfile
     * @throws BusinessException Business Exception
     */
    public void updateWFile(WFile wfile)
        throws BusinessException
    {
        try
        {
            getFileEntityDao()
                .update(wfile);
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
    }

    /**
     * save WFile
     * 
     * @param wfile wfile
     * @throws BusinessException Business Exception
     */
    public void saveWFile(WFile wfile)
        throws BusinessException
    {
        try
        {
            getFileEntityDao()
                .save(wfile);
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
    }

    /**
     * download File
     * 
     * @param appId app Id
     * @param buffer buffer
     * @return {@link FileBuffer}
     * @throws BusinessException Business Exception
     * @throws IOException IO Exception
     */
    public FileBuffer downloadFile(String appId, FileBuffer buffer)
        throws BusinessException, IOException
    {
        // logger.info("开始启动文件分片下载");
        RandomAccessFile rf = null;
        File file = null;
        byte[] data;
        try
        {
            FileBuffer realFileBuffer = new FileBuffer();
            // 单次传输大小
            Long size = buffer
                .getDownSize();
            // 默认大小
            int thissize =
                Integer
                    .valueOf(com.wedo.businessserver.css3.service.Constants.DOWNLOAD_BUFFER_SIZE);
            if (thissize < size)
            {
                throw new ParameterException("ERROR.00531");
            }
            String fileuuid = buffer
                .getFileuuid();
            String version = buffer
                .getVersion();
            if (buffer
                .getOffset() == 0)
            {
                putCache(appId, fileuuid, version);
            }
            else
            {
                if (StringUtils
                    .isBlank(cacheMap
                        .get(fileuuid + version)))
                {
                    putCache(appId, fileuuid, version);
                }
            }
            rf = new RandomAccessFile(cacheMap
                .get(fileuuid + version), "rw");
            file = new File(cacheMap
                .get(fileuuid + version));
            // 文件大小
            Long fileSize = file
                .length();
            if (buffer
                .getOffset() + Long
                .valueOf(size) >= fileSize)
            {
                size = fileSize - buffer
                    .getOffset();
                // 文件已经完成传输
                if (size <= 0)
                {
                    cacheMap
                        .remove(fileuuid + version);
                    buffer
                        .setFinish(true);
                }
                else
                {
                    buffer
                        .setFinish(false);
                }
            }
            data = new byte[Integer
                .parseInt(String
                    .valueOf(size))];
            rf
                .seek(buffer
                    .getOffset());
            @SuppressWarnings("unused")
            int point = rf
                .read(data);
            realFileBuffer
                .setOffset(buffer
                    .getOffset() + size);
            realFileBuffer
                .setData(data);
            realFileBuffer
                .setFullLength(file
                    .length());
            rf
                .close();
            realFileBuffer
                .setMessage("OPERATION_SUCCESS");
            return realFileBuffer;
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00524"));
            throw new BusinessException("ERROR.00524", e);
        }
        finally
        {
            if (rf != null)
            {
                rf
                    .close();
            }
        }
    }

    /**
     * roll back
     * 
     * @param fileuuid file uuid
     * @param version version
     * @throws BusinessException Business Exception
     */
    public void rollback(String fileuuid, String version)
        throws BusinessException
    {
        String temp = null;
        // 记录最高有效历史版本
        try
        {
            MetaData metaData = getJCRService()
                .getMetaData(fileuuid, null);
            logger
                .debug("fileuuid : " + fileuuid);
            logger
                .debug("the highest version ：  " + metaData
                    .getVersion());
            logger
                .debug("the scan version is ： " + version);
            // 不是最新版本
            if (!metaData
                .getVersion().equals(version
                    .trim()))
            {
                String sqlfilehistory =
                    "update ci_file_history c set c.avaible=? where c.avaible=? and c.fileuuid=? and c.edition=?";
                Object[] sqlfilehistoryvalues =
                    new Object[] {SystemParameter.FILE_FOLDER_UNAVIABLES,
                        SystemParameter.FILE_FOLDER_AVIABLES, fileuuid,
                        version,};
                // 修改文件历史表,置记录无效
                getJdbcTemplate()
                    .update(sqlfilehistory, sqlfilehistoryvalues);
            }
            else
            {
                // 最新版本
                if (version
                    .trim().equals("1.0"))
                {
                    String sqlFile =
                        "update ci_file c set c.avaible=? where c.avaible=? and c.fileuuid=? and c.edition=?";
                    Object[] sqlfilevalues =
                        new Object[] {SystemParameter.FILE_FOLDER_UNAVIABLES,
                            SystemParameter.FILE_FOLDER_AVIABLES, fileuuid,
                            version};
                    // 修改文件表
                    getJdbcTemplate()
                        .update(sqlFile, sqlfilevalues);
                }
                if (!version
                    .trim().equals("1.0"))
                {
                    temp = rollbackOldVersion(fileuuid, version);
                }
            }

            Wapp wapp = getApp(metaData
                .getAppGuid());
            String domainName = wapp
                .getDomainName();
            String ip = "";
            try
            {
                ip = domainName
                    .substring(0, domainName
                        .indexOf("/"));
            }
            catch (StringIndexOutOfBoundsException e)
            {
                logger
                    .error(e
                        .getMessage(), e);
                throw new Exception(e
                    .getMessage());
            }
            catch (Exception e)
            {
                logger
                    .error(e
                        .getMessage(), e);
                throw new Exception(e
                    .getMessage());
            }
            if (StringUtils
                .isBlank(temp))
            {
                temp = "";
            }
            rollbackToClient(ip, fileuuid, version, temp, metaData);
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00702");
        }
    }

    /**
     * 上传文件，但不提交，不产生版本
     * 
     * @param appId app Id
     * @param cabinetuuid cabinet uuid
     * @param folderuuid folderuuid
     * @param buffer buffer
     * @return {@link FileBuffer}
     * @throws BusinessException Business Exception
     * @throws IOException IO Exception
     */
    public FileBuffer uploadFile(String appId, String cabinetuuid,
        String folderuuid, FileBuffer buffer)
        throws BusinessException, IOException
    {
        RandomAccessFile rf = null;
        FileBuffer fileBuffer = new FileBuffer();
        try
        {
            validityOfFile(buffer);
            String root = SupportNode
                .getInstance().getHangsCarriesPath() + "/temp";
            String tempFilePath = null;
            if (buffer
                .getOffset() == 0)
            {
                HashMap<String, String> map =
                    uploadvlidateName(appId, cabinetuuid, folderuuid, buffer
                        .getName());
                if (map
                    .get("flag").equals("false"))
                { // 文件不重名，直接生成JCR
                    // logger.info(msg.getString("msg.file.nonexistent"));
                    MetaData metaData = new MetaData();
                    metaData
                        .setName(buffer
                            .getName());
                    metaData
                        .setCheckin(false);
                    metaData
                        .setSize(buffer
                            .getFullLength());
                    metaData
                        .setParentUuid(folderuuid);
                    metaData
                        .setRepuuid(cabinetuuid);
                    metaData
                        .setAppGuid(appId);
                    /**
                     * 接口变动，上传的时候暂不处理 if
                     * (StringUtils.isNotBlank(buffer.getMd5())) {
                     * jfile.setMd5(buffer.getMd5()); } if
                     * (buffer.getCompressFlag() != null) {
                     * jfile.setCompressFlag(String.valueOf(buffer
                     * .getCompressFlag())); } if (buffer.getEncryptFlag() !=
                     * null) { jfile.setEncryptFlag(String.valueOf(buffer
                     * .getEncryptFlag())); } if (buffer.getMergeFlag() != null)
                     * { jfile.setMergeFlag(String
                     * .valueOf(buffer.getMergeFlag())); }
                     */
                    getJCRService()
                        .createMetaData(metaData);
                    fileBuffer
                        .setFileuuid(metaData
                            .getUuid());
                }
                // 文件重名
                else
                {
                    // logger.info(msg.getString("msg.file.existed"));
                    fileBuffer
                        .setFileuuid(map
                            .get("fileuuid"));
                }
                // 文件一次传完情况
                if (buffer
                    .isFinish())
                {
                    tempFilePath = root + "/" + fileBuffer
                        .getFileuuid();
                    rf = new RandomAccessFile(tempFilePath, "rw");
                    rf
                        .seek(buffer
                            .getOffset());
                    rf
                        .write(buffer
                            .getData());
                    rf
                        .close();
                    return fileBuffer;
                }
            }
            // 分片上传处理
            if (tempFilePath == null)
            {
                tempFilePath = root + "/" + buffer
                    .getFileuuid();
            }
            if (buffer
                .getOffset() == 0)
            {
                tempFilePath = root + "/" + fileBuffer
                    .getFileuuid();
                rf = new RandomAccessFile(tempFilePath, "rw");
                rf
                    .seek(buffer
                        .getOffset());
                rf
                    .write(buffer
                        .getData());
                rf
                    .close();
            }
            else
            {
                tempFilePath = root + "/" + buffer
                    .getFileuuid();
                File tempFile = new File(tempFilePath);
                if (tempFile
                    .isDirectory() || !tempFile
                    .exists())
                {
                    logger
                        .error("Uploaded by slice,the tempfile notexist!");
                    logger
                        .info("Upload Error Fileuuid  =" + buffer
                            .getFileuuid());
                    throw new BusinessException("ERROR.00536");
                }
                rf = new RandomAccessFile(tempFilePath, "rw");
                rf
                    .seek(buffer
                        .getOffset());
                rf
                    .write(buffer
                        .getData());
                rf
                    .close();
            }
            fileBuffer = uploadFileReturn(fileBuffer, buffer);
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00525"));
            throw new BusinessException("ERROR.00525", e);
        }
        finally
        {
            if (rf != null)
            {
                rf
                    .close();
            }
        }
        return fileBuffer;
    }

    /**
     * 提交文件，产生版本 2009-11-25G更改:新增4个参数， by l00100468 mergeFlag, encryptFlag,
     * compressFlag, md5
     * 
     * @param appId app Id
     * @param fileuuid file uuid
     * @param mergeFlag merge Flag
     * @param encryptFlag encrypt Flag
     * @param compressFlag compress Flag
     * @param md5 md5
     * @return {@link FileBuffer}
     * @throws BusinessException Business Exception
     */
    public FileBuffer tocommit(String appId, String fileuuid, String mergeFlag,
        String encryptFlag, String compressFlag, String md5)
        throws BusinessException
    {
        // logger.info("开始文件提交");
        logger
            .info("Tocommit  Fileuuid :" + fileuuid);
        File file = null;
        try
        {
            String writeRoot = SupportNode
                .getInstance().getHangsCarriesPath();// 获得文件写入路径
            Calendar calendar = Calendar
                .getInstance();
            String time = DateUtils
                .format(calendar, "yyyy/MM/dd");
            File folderFile = new File(writeRoot + "/repository/date/" + time);
            createPaFolder(folderFile);
            file = new File(writeRoot + "/temp/" + fileuuid);

            // 文件分得上传处理情况
            if (!file
                .exists())
            {
                logger
                    .error("File commit Error,Tempfile not exist!");
                logger
                    .info("Commit Error Fileuuid  =" + fileuuid);
                throw new BusinessException("ERROR.00536");
            }
            MetaData metaData = getJCRService()
                .getMetaData(fileuuid, null);
            // JFile jfile = getJCRService().getFile(fileuuid);
            metaData
                .setCheckin(true);
            metaData
                .setCreateDate(DateUtils
                    .format(Calendar
                        .getInstance(), "yyyy-MM-dd HH:mm:ss"));
            // getJCRService().updateFile(jfile);// jcr生成元数据,返回版本
            getJCRService()
                .updateMetaData(metaData);
            String version = metaData
                .getVersion();
            String cabinetuuid = metaData
                .getRepuuid();
            String folderuuid = metaData
                .getParentUuid();
            String realName = fileuuid + "-" + version;
            String realPath =
                writeRoot + "/repository/date/" + time + "/" + realName;
            File realFile = new File(realPath);
            Wapp wapp = getApp(appId);// 校验应用
            HashMap<String, String> map =
                uploadvlidateName(appId, cabinetuuid, folderuuid, metaData
                    .getName());// 判断文件是否重名
            if (map
                .get("flag").equals("false"))
            {
                fileNoName(appId, metaData, realFile, fileuuid, version, map,
                    time, realName, mergeFlag, encryptFlag, compressFlag, md5,
                    realPath);
            }
            // 文件重名
            else
            {
                fileHasName(appId, metaData, realFile, fileuuid, version, map,
                    time, realName, mergeFlag, encryptFlag, compressFlag, md5,
                    realPath);
            }

            // 组装回调时文件LINK地址
            // 组装软链接路径-START
            String linkurl = "/reso/";

            Properties pros = System
                .getProperties();
            String osName = pros
                .getProperty("os.name");
            try
            {
                if (osName
                    .indexOf("Windows") != -1)
                {
                    // Windows操作系统处理
                    linkurl =
                        linkurl + "repository/date/" + time + "/" + realName;
                }
                else
                {
                    linkurl =
                        linkurl + SupportNode
                            .getInstance().getSymName() + "/repository/date/"
                            + time + "/" + realName;
                }
            }
            catch (StringIndexOutOfBoundsException e)
            {
                logger
                    .error(e
                        .getMessage(), e);
                throw new Exception(e
                    .getMessage());
            }
            catch (Exception e)
            {
                logger
                    .error(e
                        .getMessage(), e);
                throw new Exception(e
                    .getMessage());
            }
            /* 组装软链接路径-END */

            String appIp = wapp
                .getAppUploadCallBack();// 获得应用IP,准备回调
            postMessageToClient(appIp, metaData, file
                .length(), metaData
                .getParentUuid(), linkurl, mergeFlag, encryptFlag,
                compressFlag, md5);// 通知应用端进行处理
            FileBuffer fileBuffer = new FileBuffer();
            fileBuffer
                .setFileuuid(fileuuid);
            fileBuffer
                .setVersion(version);
            fileBuffer
                .setMessage("OPERATION_SUCCESS");
            Boolean flag = file
                .renameTo(realFile);
            if (!flag)
            {
                logger
                    .info("ReName the tmpfile Failed in commit. tmpfile is =  "
                        + file
                            .getPath());
            }
            logger
                .debug(MSG
                    .getString("OPERATION_SUCCESS"));

            // 图片文件产生缩略图
            String name = metaData
                .getName();
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
            // System.out
            // .println("-----------" + realPath);
            // System.out
            // .println("------- " + new File(realPath)
            // .exists());
            if (IMAGE_FILE_PSTFIX
                .lastIndexOf(filePstFix) != -1)
            {
                PictureUtils
                    .resize(realPath, realPath + IMAGE_FILE_TARGET_PST, TARGET_WIDTH,
                        TARGET_HEIGHTH);
            }

            return fileBuffer;
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00532"));
            throw new BusinessException("ERROR.00532", e);
        }
    }

    /**
     * 分页查询WFILE
     * 
     * @param map map
     * @param pageInfo page Info
     * @throws BusinessException Business Exception
     */
    public void pageWFle(HashMap<String, String> map, PageInfo<WFile> pageInfo)
        throws BusinessException
    {
        try
        {
            Criteria criteria = getFileEntityDao()
                .getEntityCriteria();
            String avaible = map
                .get("avaible");
            // 是否可用
            if (StringUtils
                .isNotBlank(avaible))
            {
                criteria
                    .add(Restrictions
                        .eq("avaible", avaible));
            }
            String mergeFlag = map
                .get("mergeFlag");
            if (StringUtils
                .isNotBlank(mergeFlag))
            {
                // 是否增量
                criteria
                    .add(Restrictions
                        .eq("mergeFlag", mergeFlag));
            }
            String encryptFlag = map
                .get("encryptFlag");
            if (StringUtils
                .isNotBlank(encryptFlag))
            {
                // 是否加密
                criteria
                    .add(Restrictions
                        .eq("encryptFlag", encryptFlag));
            }
            String compressFlag = map
                .get("compressFlag");
            if (StringUtils
                .isNotBlank(encryptFlag))
            {
                // 是否压缩
                criteria
                    .add(Restrictions
                        .eq("compressFlag", compressFlag));
            }
            getFileEntityDao()
                .pagedQueryByQbc(pageInfo, criteria);
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00528_1"));
            throw new BusinessException("ERROR.00528", e);
        }

    }

    /**
     * 分页查询WFileHistory
     * 
     * @param map map
     * @param pageInfo page Info
     * @throws BusinessException Business Exception
     */
    public void pageWFleHistory(HashMap<String, String> map,
        PageInfo<WFileHistory> pageInfo)
        throws BusinessException
    {
        try
        {
            Criteria criteria = getFileHistoryEntityDao()
                .getEntityCriteria();
            String avaible = map
                .get("avaible");
            if (StringUtils
                .isNotBlank(avaible))
            {
                // 是否可用
                criteria
                    .add(Restrictions
                        .eq("avaible", avaible));
            }
            String mergeFlag = map
                .get("mergeFlag");
            if (StringUtils
                .isNotBlank(mergeFlag))
            {
                // 是否增量
                criteria
                    .add(Restrictions
                        .eq("mergeFlag", mergeFlag));
            }
            String encryptFlag = map
                .get("encryptFlag");
            if (StringUtils
                .isNotBlank(encryptFlag))
            {
                // 是否加密
                criteria
                    .add(Restrictions
                        .eq("encryptFlag", encryptFlag));
            }
            String compressFlag = map
                .get("compressFlag");
            if (StringUtils
                .isNotBlank(encryptFlag))
            {
                // 是否压缩
                criteria
                    .add(Restrictions
                        .eq("compressFlag", compressFlag));
            }
            getFileHistoryEntityDao()
                .pagedQueryByQbc(pageInfo, criteria);
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00528_1"));
            throw new BusinessException("ERROR.00528", e);
        }

    }

    /**
     * 设置对象元数据
     * 
     * @param appId appId
     * @param fileuuid file uuid
     * @param version version
     * @param metadatas metadatas
     * @throws BusinessException Business Exception
     */
    public void setFileMetadata(String appId, String fileuuid, String version,
        List<Keyassignments> metadatas)
        throws BusinessException
    {
        try
        {
            HashMap<String, String> fileMap = new HashMap<String, String>();
            HashMap<String, String> map = new HashMap<String, String>();
            try
            {
                fileMap
                    .put("appguid", appId);
                fileMap
                    .put("fileuuid", fileuuid);
                fileMap
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                getWFile(fileMap);
            }
            catch (BusinessException e)
            {
                if (StringUtils
                    .isNotBlank(version))
                {
                    fileMap
                        .put("version", version);
                    getWFileHistory(fileMap);
                }
                else
                {
                    throw e;
                }
            }
            for (Keyassignments keyassignments : metadatas)
            {
                map
                    .put(keyassignments
                        .getKey(), keyassignments
                        .getValue());
            }
            MetaData metaData = getJCRService()
                .getMetaData(fileuuid, version);
            metaData
                .setMap(map);
            metaData
                .setCheckin(false);
            getJCRService()
                .updateMetaData(metaData);
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00594"));
            throw new BusinessException("ERROR.00594", e);
        }
    }

    /**
     * 获取对象元数据
     * 
     * @param appId app Id
     * @param fileuuid file uuid
     * @param version version
     * @return list
     * @throws BusinessException Business Exception
     */
    public List<Keyassignments> getFileMetadata(String appId, String fileuuid,
        String version)
        throws BusinessException
    {
        try
        {
            List<Keyassignments> list = new ArrayList<Keyassignments>();
            Map<String, String> fileMap = new HashMap<String, String>();
            try
            {
                fileMap
                    .put("appguid", appId);
                fileMap
                    .put("fileuuid", fileuuid);
                fileMap
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                getWFile(fileMap);
            }
            catch (BusinessException e)
            {
                if (StringUtils
                    .isNotBlank(version))
                {
                    fileMap
                        .put("version", version);
                    getWFileHistory(fileMap);
                }
                else
                {
                    throw e;
                }
            }
            MetaData metaData = getJCRService()
                .getMetaData(fileuuid, version);
            HashMap<String, String> map = metaData
                .getMap();
            if (map != null)
            {
                Iterator<Entry<String, String>> it = map
                    .entrySet().iterator();
                while (it
                    .hasNext())
                {
                    Entry<String, String> entry = it
                        .next();
                    Keyassignments keyassignments = new Keyassignments();
                    keyassignments
                        .setKey(entry
                            .getKey());
                    keyassignments
                        .setValue(map
                            .get(entry
                                .getValue()));
                    list
                        .add(keyassignments);
                }
            }
            return list;
        }
        catch (DataAccessException e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00050"));
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00595"));
            throw new BusinessException("ERROR.00595", e);
        }
    }

    /**
     * 下载所用cache
     * 
     * @param appId app Id
     * @param fileuuid file uuid
     * @param version version
     * @throws Exception 异常
     */
    private void putCache(String appId, String fileuuid, String version)
        throws Exception
    {
        File writeFile = null;
        String readroot = null;
        try
        {
            String path = null;
            Map<String, String> fileMap = new HashMap<String, String>();
            if (StringUtils
                .isBlank(appId) || StringUtils
                .isBlank(fileuuid))
            {
                throw new ParameterException("ERROR.00500");
            }
            fileMap
                .put("appguid", appId);
            fileMap
                .put("fileuuid", fileuuid);
            fileMap
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            logger
                .debug("appId=    " + appId);
            logger
                .debug("fileuuid= " + fileuuid);
            logger
                .debug("version=  " + version);
            if (StringUtils
                .isBlank(version))
            {
                WFile wfile = getWFile(fileMap);
                path = wfile
                    .getFpath();
            }
            else
            {
                fileMap
                    .put("version", version);
                WFileHistory fileHistory = getWFileHistory(fileMap);
                path = fileHistory
                    .getFpath();
            }
            List<WSupportNode> list =
                getWSupportByIp(com.wedo.businessserver.storage.jcr.Constants.LOCALIP);
            for (int i = 0; i < list
                .size(); i++)
            {
                readroot = list
                    .get(i).getMountPath();
                writeFile = new File(readroot + path);
                if (writeFile
                    .exists() && writeFile
                    .isFile())
                {
                    break;
                }
            }
            if (writeFile == null)
            {
                throw new BusinessException("ERROR.00524");
            }
            cacheMap
                .put(fileuuid + version, readroot + path);
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00524_1"));
            throw new BusinessException("ERROR.00524", e);
        }

    }

    private FileBuffer uploadFileReturn(FileBuffer fileBuffer, FileBuffer buffer)
        throws Exception
    {
        if (buffer
            .getOffset() != 0 && !buffer
            .isFinish())
        {
            fileBuffer
                .setFileuuid(buffer
                    .getFileuuid());
        }
        if (buffer
            .isFinish())
        {
            fileBuffer
                .setFileuuid(buffer
                    .getFileuuid());
        }
        return fileBuffer;
    }

    /**
     * 检验文件上传偏移量
     * 
     * @param buffer
     * @throws BusinessException Exception
     */
    private void validityOfFile(FileBuffer buffer)
        throws BusinessException
    {
        if (buffer == null)
        {
            throw new ParameterException("ERROR.00500");
        }
        if (buffer
            .getOffset() < 0)
        {
            throw new ParameterException("ERROR.00502");
        }
        if (buffer
            .getData().length > Long
            .valueOf(com.wedo.businessserver.css3.service.Constants.UPLOAD_BUFFER_SIZE))
        {
            throw new ParameterException("ERROR.00503");// 文件操作单次文件过大
        }
    }

    /**
     * 上传完成通知客户端 20091125:增加回弹函数参数加密、压缩、增量、MD5
     * 
     * @param resIp res Ip
     * @param metaData meta Data
     * @param length length
     * @param folderuuid folder uuid
     * @param linkurl link url
     * @param mergeFlag merge Flag
     * @param encryptFlag encrypt Flag
     * @param compressFlag compress Flag
     * @param md5 md5
     * @throws Exception Exception
     */
    private void postMessageToClient(String resIp, MetaData metaData,
        Long length, String folderuuid, String linkurl, String mergeFlag,
        String encryptFlag, String compressFlag, String md5)
        throws Exception
    {
        PostMethod postMethod = null;
        try
        {
            logger
                .debug("callback :ip     =" + resIp);// 客户端回调地址
            logger
                .debug("length =" + length);// 回调函数文件大小
            // 通知客户端处理
            HttpClient httpClient = new HttpClient();
            postMethod = new UTF8PostMethod(resIp);
            NameValuePair[] data =
            {
                new NameValuePair("fileuuid", metaData
                    .getUuid()),
                new NameValuePair("version", metaData
                    .getVersion()),
                new NameValuePair("filesize", Long
                    .toString(length)),
                new NameValuePair("folderuuid", folderuuid),
                new NameValuePair("filename", java.net.URLEncoder
                        .encode(metaData
                            .getName(), "UTF-8")),
                new NameValuePair("linkurl", linkurl),
                new NameValuePair("mergeFlag", mergeFlag),
                new NameValuePair("encryptFlag", encryptFlag),
                new NameValuePair("compressFlag", compressFlag),
                new NameValuePair("md5", md5)};
            postMethod
                .setRequestBody(data);
            postMethod
                .setRequestHeader("Connection", "close");
            int statusCode = httpClient
                .executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK)
            {
                logger
                    .info(MSG
                        .getString("ERROR.00542") + postMethod
                        .getStatusLine());
                throw new BusinessException("ERROR.00542");
            }
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger
                .info(MSG
                    .getString("ERROR.00539"));
            throw new BusinessException("ERROR.00539", e);
        }
        finally
        {
            postMethod
                .releaseConnection();// 关闭http连接
        }
    }

    private void setWfilMergeFlag(WFile wfile, String mergeFlag)
        throws Exception
    {
        if (StringUtils
            .isNotBlank(mergeFlag))
        {
            if (mergeFlag
                .equals("0") || mergeFlag
                .equals("1"))
            {
                wfile
                    .setMergeFlag(mergeFlag);
            }
            else
            {
                throw new ParameterException("ERROR.00505");
            }
        }
        else
        {
            wfile
                .setMergeFlag(SystemParameter.UN_MERGE);
        }
    }

    private void fileNoNameProperty(WFile wfile, String mergeFlag,
        String encryptFlag, String compressFlag, String md5)
        throws Exception
    {
        setWfilMergeFlag(wfile, mergeFlag);
        if (StringUtils
            .isNotBlank(encryptFlag))
        {
            if (encryptFlag
                .equals("0") || encryptFlag
                .equals("1"))
            {
                wfile
                    .setEncryptFlag(encryptFlag);
            }
            else
            {
                throw new ParameterException("ERROR.00506");
            }
        }
        else
        {
            wfile
                .setEncryptFlag(SystemParameter.UNENCRYPT_FLAG);
        }
        if (StringUtils
            .isNotBlank(compressFlag))
        {
            if (compressFlag
                .equals("0") || compressFlag
                .equals("1"))
            {
                wfile
                    .setCompressFlag(compressFlag);
            }
            else
            {
                throw new ParameterException("ERROR.00507");
            }
        }
        else
        {
            wfile
                .setCompressFlag(SystemParameter.UNCOMPRESS_FLAG);
        }
        if (StringUtils
            .isNotBlank(md5))
        {
            wfile
                .setMd5(md5);
        }
    }

    private void fileNoName(String appId, MetaData metaData, File file,
        String fileuuid, String version, HashMap<String, String> map,
        String time, String realName, String mergeFlag, String encryptFlag,
        String compressFlag, String md5, String realPath)
        throws Exception
    {
        // 文件不重名
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
            .setFName(metaData
                .getName());
        wfile
            .setFileSize(file
                .length());
        wfile
            .setFileuuid(fileuuid);
        wfile
            .setVersion(version);
        wfile
            .setVisitNumber(new BigDecimal(0));
        wfile
            .setRepguid(map
                .get("wrepositoryguid"));
        wfile
            .setGuid(randomGUID
                .getValueAfterMD5());
        wfile
            .setFolderguid(map
                .get("folderGuid"));
        wfile
            .setAvaible(SystemParameter.FILE_FOLDER_AVIABLES);
        wfile
            .setFpath("/repository/date/" + time + "/" + realName);
        fileNoNameProperty(wfile, mergeFlag, encryptFlag, compressFlag, md5);
        wfile
            .setAvaibleService(SystemParameter.UNAVAIBLE_SERVICE);
        saveWFile(wfile);
        if (DataServiceUtil
            .getAuthorization(appId).getFunction().getSearchEngine()
            .getAvailable())
        {
            if (wfile
                .getMergeFlag().equals(SystemParameter.UN_MERGE) && wfile
                .getEncryptFlag().equals(SystemParameter.UNENCRYPT_FLAG)
                && wfile
                    .getCompressFlag().equals(SystemParameter.UNCOMPRESS_FLAG))
            {
                HashMap<String, String> folderMap =
                    new HashMap<String, String>();
                folderMap
                    .put("guid", wfile
                        .getFolderguid());
                wfile
                    .setFoldertreepath(getFolder(folderMap)
                        .getTreepath());
                threadPoolManager
                    .addTask(new SearchTask(
                        new IkIndexWriter(),
                        wfile,
                        com.wedo.businessserver.searchengine.Constants.INCREMENT_FLAG));
            }
        }

        // 需要杀毒
        if (DataServiceUtil
            .getAuthorization(appId).getFunction().getScanVirus()
            .getAvailable())
        {
            threadPoolManager
                .addTask(new SecurityTask(realPath));
        }
    }

    private void fileHasName(String appId, MetaData metaData, File file,
        String fileuuid, String version, HashMap<String, String> map,
        String time, String realName, String mergeFlag, String encryptFlag,
        String compressFlag, String md5, String realPath)
        throws Exception
    {
        HashMap<String, String> fileMap = new HashMap<String, String>();
        fileMap
            .put("fileuuid", fileuuid);
        fileMap
            .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
        WFile wfile = getWFile(fileMap);
        WFileHistory fileHistory = new WFileHistory();
        BeanUtils
            .copyProperties(fileHistory, wfile);
        fileHistory
            .setId(null);
        RandomGUID historyrandomGUID = new RandomGUID();
        fileHistory
            .setGuid(historyrandomGUID
                .getValueAfterMD5());
        saveWFileHistory(fileHistory);
        wfile
            .setVersion(version);
        if (StringUtils
            .isNotBlank(mergeFlag))
        {
            wfile
                .setMergeFlag(mergeFlag);
        }
        if (StringUtils
            .isNotBlank(encryptFlag))
        {
            wfile
                .setEncryptFlag(encryptFlag);
        }
        if (StringUtils
            .isNotBlank(compressFlag))
        {
            wfile
                .setCompressFlag(compressFlag);
        }
        if (StringUtils
            .isNotBlank(md5))
        {
            wfile
                .setMd5(md5);
        }
        wfile
            .setFileSize(file
                .length());
        wfile
            .setFpath("/repository/date/" + time + "/" + realName);
        updateWFile(wfile);
        if (DataServiceUtil
            .getAuthorization(appId).getFunction().getSearchEngine()
            .getAvailable())
        {
            if (wfile
                .getMergeFlag().equals(SystemParameter.UN_MERGE) && wfile
                .getEncryptFlag().equals(SystemParameter.UNENCRYPT_FLAG)
                && wfile
                    .getCompressFlag().equals(SystemParameter.UNCOMPRESS_FLAG))
            {
                HashMap<String, String> folderMap =
                    new HashMap<String, String>();
                folderMap
                    .put("guid", wfile
                        .getFolderguid());
                wfile
                    .setFoldertreepath(getFolder(folderMap)
                        .getTreepath());
                fileHistory
                    .setFoldertreepath(getFolder(folderMap)
                        .getTreepath());
                wfile
                    .setWFileHistory(fileHistory);
                threadPoolManager
                    .addTask(new SearchTask(
                        new IkIndexWriter(),
                        wfile,
                        com.wedo.businessserver.searchengine.Constants.INCREMENT_FLAG));
            }
        }

        // 需要杀毒
        if (DataServiceUtil
            .getAuthorization(appId).getFunction().getScanVirus()
            .getAvailable())
        {
            threadPoolManager
                .addTask(new SecurityTask(realPath));
        }
    }

    private void createPaFolder(File folderFile)
        throws Exception
    {
        if (!folderFile
            .exists())
        {
            Boolean flag = folderFile
                .mkdirs();
            if (!flag)
            {
                logger
                    .info("Mkdir failed . folder is =  " + folderFile
                        .getPath());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private String rollbackOldVersion(String fileuuid, String version)
        throws Exception
    {
        String temp = null;
        /*
         * 多版本情况下处理逻辑
         * 
         * 1、当所有历史版本皆为无效版本时，处理方式：直接将最新版本置为无效
         * 2、当最高历史版本为有效版本时，处理方式：将最新版本置为无效，将最高历史版本置为可用
         * 3、当最高历史版本为无效版本时，处理方式：最新版本置为无效，将有效的最高历史版本置为可用
         */
        String sqlfilehistory =
            "select c.edition from ci_file_history c where c.avaible=? and c.fileuuid=? ";
        Object[] sqlfilehistoryvalues =
            new Object[] {SystemParameter.FILE_FOLDER_AVIABLES, fileuuid};
        // 查出所有有效历史文件
        List<WFileHistory> list = getJdbcTemplate()
            .queryForList(sqlfilehistory, sqlfilehistoryvalues);

        // 如果无有效历史文件，则按最新版本为1.0处理
        if (list == null || list
            .size() <= 0)
        {
            String sqlFile =
                "update ci_file c set c.avaible=? where c.avaible=? and c.fileuuid=? and c.edition=?";
            Object[] sqlfilevalues =
                new Object[] {SystemParameter.FILE_FOLDER_UNAVIABLES,
                    SystemParameter.FILE_FOLDER_AVIABLES, fileuuid, version};
            // 修改文件表
            getJdbcTemplate()
                .update(sqlFile, sqlfilevalues);
        }

        // 如果存在有效历史文件的版本处理
        // 1、取出最高有效版本
        if (list != null && list
            .size() > 0)
        {
            temp = highestHisVersion(list);
            logger
                .info("the highest available history version is : " + temp);
            // 2、当前文件表：版本处理
            if (temp == null)
            {
                logger
                    .error(MSG
                        .getString("ERROR.00576"));
                // 获取最高有效历史版本有误
                throw new BusinessException("ERROR.00576");
            }
            String sqlFile =
                "delete from ci_file  where avaible=? and fileuuid=? and edition=?";
            Object[] sqlfilevalues =
                new Object[] {SystemParameter.FILE_FOLDER_AVIABLES, fileuuid,
                    version};
            // 删除当前表病毒文件记录
            getJdbcTemplate()
                .update(sqlFile, sqlfilevalues);
            HashMap<String, String> map = new HashMap<String, String>();
            map
                .put("fileuuid", fileuuid);
            map
                .put("version", temp);
            map
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            // 取出合适回滚版本
            WFileHistory fileHistory = getWFileHistory(map);
            WFile newfile = new WFile();
            BeanUtils
                .copyProperties(newfile, fileHistory);
            newfile
                .setId(null);
            RandomGUID historyrandomGUID = new RandomGUID();
            newfile
                .setGuid(historyrandomGUID
                    .getValueAfterMD5());
            // 当前表增加该记录
            this
                .saveWFile(newfile);

            String delsql =
                "delete from  ci_file_history  where avaible=? and fileuuid=? and edition=?";
            Object[] delsqlvalues =
                new Object[] {SystemParameter.FILE_FOLDER_AVIABLES, fileuuid,
                    temp};
            // 历史表删除该记录
            getJdbcTemplate()
                .update(delsql, delsqlvalues);
        }
        return temp;
    }

    private void rollbackToClient(String ip, String fileuuid, String version,
        String temp, MetaData metaData)
        throws Exception
    {
        String url = "http" + ":" + "/" + "/" + ip + "/live/VirusFileServlet";
        logger
            .info("APP Secruity Callback URL  : " + url);

        PostMethod post = new UTF8PostMethod(url);
        HttpClient httpClient = new HttpClient();
        HttpClientParams params = new HttpClientParams();
        params
            .setParameter(HttpClientParams.USE_EXPECT_CONTINUE, Boolean.FALSE);
        params
            .setParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, Long
                .valueOf(VALUE_100));
        httpClient
            .setParams(params);
        NameValuePair[] data = new NameValuePair[INDEX_3];
        // 文件UUID
        data[INDEX_0] = new NameValuePair("fileUuid", fileuuid);
        // 病毒文件版本
        data[INDEX_1] = new NameValuePair("fileVersion", version);
        // 回滚版本
        data[INDEX_2] = new NameValuePair("rollbackVersion", temp);
        post
            .setRequestBody(data);
        post
            .setRequestHeader("Connection", "close");
        httpClient
            .executeMethod(post);
        int status = post
            .getStatusCode();

        if (status != HttpStatus.SC_OK)
        {
            logger
                .info(MSG
                    .getString("ERROR.00701") + "  APP Return Status:" + status);
            // 杀毒回调失败
            throw new BusinessException("ERROR.00701");
        }
        if (StringUtils
            .isNotBlank(temp))
        {
            logger
                .info("Start update the metadata! " + " version :" + temp);
            // 删除病毒源文件
            getJCRService()
                .delMetaData(metaData);
            // 更改源数据目录最新版本文件
            getJCRService()
                .updateMetaData(fileuuid, temp);
        }
    }

    private void copyFileTask(String appId, WFile newWFile,
        String targetFolderguid)
        throws Exception
    {
        if (DataServiceUtil
            .getAuthorization(appId).getFunction().getSearchEngine()
            .getAvailable())
        {
            if (newWFile
                .getMergeFlag().equals(SystemParameter.UN_MERGE) && newWFile
                .getEncryptFlag().equals(SystemParameter.UNENCRYPT_FLAG)
                && newWFile
                    .getCompressFlag().equals(SystemParameter.UNCOMPRESS_FLAG))
            {
                HashMap<String, String> folderMap =
                    new HashMap<String, String>();
                folderMap
                    .put("guid", targetFolderguid);
                newWFile
                    .setFoldertreepath(getFolder(folderMap)
                        .getTreepath());
                threadPoolManager
                    .addTask(new SearchTask(
                        new IkIndexWriter(),
                        newWFile,
                        com.wedo.businessserver.searchengine.Constants.INCREMENT_FLAG));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private String highestHisVersion(List<WFileHistory> list)
    {
        String temp = null;
        Iterator it = list
            .iterator();
        while (it
            .hasNext())
        {
            Map hfileMap = (Map) it
                .next();
            String hversion = ((String) hfileMap
                .get("edition"))
                .trim();
            if (temp == null)
            {
                temp = hversion;
                continue;
            }
            // 取位数更长时
            if (hversion
                .length() > temp
                .length())
            {
                temp = hversion;
            }
            // 长度一致时按ASCII比较，取较大
            else if (hversion
                .length() == temp
                .length())
            {
                if (temp
                    .compareToIgnoreCase(hversion) < 0)
                {
                    temp = hversion;
                }
            }
        }
        return temp;
    }

    /**
     * 执行指定版本文件的删除
     * 
     * @param map 文件信息map，含：version、appguid、fileuuid
     * @param flag 文件有效标记：0为有效，1为逻辑删除，2为物理删除
     * @throws BusinessException Business Exception
     */
    private void delVersionFile(Map<String, String> map, String flag)
        throws BusinessException
    {
        String version = map
            .get("version");
        String appId = map
            .get("appguid");
        String fileuid = map
            .get("fileuuid");
        try
        {
            // 查找历史版本列表
            getWFileHistory(map);
            String sqlfilehistory =
                "update ci_file_history c set c.avaible=? "
                    + "where c.avaible=? and c.fileuuid=? and c.edition=? and c.appguid=?";
            Object[] sqlfilehistoryvalues =
                new Object[] {flag, SystemParameter.FILE_FOLDER_AVIABLES,
                    fileuid, version, appId};
            // 修改文件历史表
            getJdbcTemplate()
                .update(sqlfilehistory, sqlfilehistoryvalues);
        }
        catch (BusinessException e2)
        {
            throw e2;
        }
    }

    /**
     * UTF8 Post Method
     * 
     * @author c90003207
     * 
     */
    public static class UTF8PostMethod
        extends PostMethod
    {
        /**
         * UTF8 Post Method
         * 
         * @param url url
         */
        public UTF8PostMethod(String url)
        {
            super(url);
        }

        /**
         * get Request CharSet
         * 
         * @return String
         */
        @Override
        public String getRequestCharSet()
        {
            // return super.getRequestCharSet();
            return "UTF-8";
        }
    }

}
