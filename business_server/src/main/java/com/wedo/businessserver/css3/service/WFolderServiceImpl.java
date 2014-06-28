package com.wedo.businessserver.css3.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.wedo.businessserver.attemper.threadpool.SearchTask;
import com.wedo.businessserver.attemper.threadpool.ThreadPoolManager;
import com.wedo.businessserver.cache.SupportNode;
import com.wedo.businessserver.common.dao.EntityDao;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.exception.DataNotFoundException;
import com.wedo.businessserver.common.exception.OperationNotPermitException;
import com.wedo.businessserver.common.exception.ParameterException;
import com.wedo.businessserver.common.util.DateUtils;
import com.wedo.businessserver.common.util.RandomGUID;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WFolder;
import com.wedo.businessserver.css3.domain.WRepository;
import com.wedo.businessserver.css3.domain.WSupportNode;
import com.wedo.businessserver.css3.service.util.ChangePathValueSetterImpl;
import com.wedo.businessserver.css3.service.util.DataServiceUtil;
import com.wedo.businessserver.css3.service.util.DelWfileValueSetterImpl;
import com.wedo.businessserver.css3.service.util.InsertWfileValueSetterImpl;
import com.wedo.businessserver.css3.ws.model.FileBuffer;
import com.wedo.businessserver.css3.ws.model.FilePageInfo;
import com.wedo.businessserver.css3.ws.model.ResultBuffer;
import com.wedo.businessserver.searchengine.IkIndexWriter;
import com.wedo.businessserver.searchengine.SearchContext;
import com.wedo.businessserver.storage.jcr.domain.MetaData;

/**
 * 文件夹操作接口实现类
 * 
 * @author c90003207
 * 
 */
@Service("wFolderService")
public class WFolderServiceImpl
    extends WOnlineAbstractServiceImpl
    implements WFolderService

{

    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory
        .getLog(WFolderServiceImpl.class);

    private ThreadPoolManager threadPoolManager = ThreadPoolManager
        .getInstance();

    /**
     * 拷贝文件夹
     * 
     * @param appId 应用GUID
     * @param cabinetuuid 用户仓库UUID
     * @param folderuuid 源文件夹UUID
     * @param targetfolderuid 目标文件夹UUID
     * @return ResultBuffer
     * @throws BusinessException Business Exception
     */
    public ResultBuffer copyFolder(String appId, String cabinetuuid,
        String folderuuid, String targetfolderuid)
        throws BusinessException
    {
        ResultBuffer resultBuffer = new ResultBuffer();
        try
        {
            // 基本参数非空判断
            if (StringUtils
                .isBlank(cabinetuuid) || StringUtils
                .isBlank(folderuuid) || StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00401");
            }
            // 拷贝源与目标判断：不能相等（本身）
            if (folderuuid
                .equals(targetfolderuid))
            {
                throw new ParameterException("ERROR.00402");
            }
            // 校验文件夹
            Map<String, String> folderMap = new HashMap<String, String>();
            folderMap
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            folderMap
                .put("folderuuid", folderuuid);
            folderMap
                .put("appguid", appId);
            WFolder oldfolder = getFolder(folderMap);
            String parentGuid = null;
            String parentPath = null;
            if (StringUtils
                .isNotBlank(targetfolderuid))
            {
                // 检验目标文件夹不是根目录的时候的合法性
                Map<String, String> targetfolderMap =
                    new HashMap<String, String>();
                targetfolderMap
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                targetfolderMap
                    .put("folderuuid", targetfolderuid);
                targetfolderMap
                    .put("appguid", appId);
                WFolder targetFolder = getFolder(targetfolderMap);
                parentGuid = targetFolder
                    .getGuid();
                parentPath = targetFolder
                    .getTreepath();
                if (!validateName(oldfolder
                    .getRepguid(), oldfolder
                    .getName(), parentGuid,
                    SystemParameter.FILE_FOLDER_AVIABLES))
                {
                    throw new DataNotFoundException("ERROR.00410");
                }

            }
            else
            {
                // 检验目标文件夹是根目录的时候的合法性
                parentGuid = oldfolder
                    .getRepguid();
                Map<String, String> cabinetMap = new HashMap<String, String>();
                cabinetMap
                    .put("appId", appId);
                cabinetMap
                    .put("repuuid", cabinetuuid);
                WRepository repository = getCabinet(cabinetMap);
                targetfolderuid = repository
                    .getRepuuid();
                if (!validateName(oldfolder
                    .getRepguid(), oldfolder
                    .getName(), repository
                    .getGuid(), SystemParameter.FILE_FOLDER_AVIABLES))
                {
                    throw new DataNotFoundException("ERROR.00410");
                }
            }
            // 拷贝源与目标合法性判断：子或本身
            if (checkChildFolder(oldfolder
                .getTreepath(), targetfolderuid))
            {
                // 检验文件夹是否拷贝到自己或者子文件夹下
                throw new OperationNotPermitException("ERROR.00403");
            }
            // 递归寻找文件夹并处理文件夹下子文件夹
            searchFolders(oldfolder, parentGuid, parentPath, targetfolderuid,
                resultBuffer);
            return resultBuffer;
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00424", e);
        }
    }

    /**
     * 搜索关键字
     * 
     * @param appId app Id
     * @param keyWord key Word
     * @param folderuuid folder uuid
     * @param filePageInfo file Page Info
     * @param map map
     * @throws BusinessException Business Exception
     */
    public void search(String appId, String keyWord, String folderuuid,
        FilePageInfo filePageInfo, HashMap<String, String> map)
        throws BusinessException
    {
        try
        {
            HashMap<String, String> folderMap = new HashMap<String, String>();
            folderMap
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            folderMap
                .put("folderuuid", folderuuid);
            folderMap
                .put("appguid", appId);
            WFolder folder = getFolder(folderMap);
            String folderPath = folder
                .getTreepath().replaceAll(FOLDERSPLIT, "");
            SearchContext searchContext =
                new SearchContext(new IkIndexWriter());
            searchContext
                .search(new String[] {keyWord}, folderPath, filePageInfo, map);
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00580", e);
        }
    }

    /**
     * 批量复制文件夹
     * 
     * @param appId appId
     * @param cabinetuuid cabinet uuid
     * @param folderuuids folder uuids
     * @param targetfolderuid target folder uid
     * @return ResultBuffer 文件夹\文件的拷贝结果
     * @throws BusinessException Business Exception
     */
    public ResultBuffer copyFolders(String appId, String cabinetuuid,
        List<String> folderuuids, String targetfolderuid)
        throws BusinessException
    {
        ResultBuffer resultBuffer = new ResultBuffer();
        try
        {
            // 基本操作参数合法性判断
            if (StringUtils
                .isBlank(cabinetuuid) || StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00401");
            }
            if (folderuuids == null || folderuuids
                .size() <= 0)
            {
                throw new ParameterException("ERROR.00401");
            }
            // 依次进行文件夹源拷贝操作
            resultBuffer =
                copyFoldersEx(appId, cabinetuuid, folderuuids, targetfolderuid);
            return resultBuffer;
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00427", e);
        }
    }

    /**
     * 移动文件夹
     * 
     * @param appId 应用ID
     * @param cabinetuuid cabinetuuid
     * @param folderuid folderuid
     * @param targetfolderuid targetfolderuid
     * @throws BusinessException Exception
     */
    public void moveFolder(String appId, String cabinetuuid, String folderuid,
        String targetfolderuid)
        throws BusinessException
    {
        try
        {
            if (StringUtils
                .isBlank(cabinetuuid) || StringUtils
                .isBlank(folderuid) || StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00401");
            }
            if (folderuid
                .equals(targetfolderuid))
            {
                throw new ParameterException("ERROR.00402");
            }
            Map<String, String> folderMap = new HashMap<String, String>();
            folderMap
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            folderMap
                .put("folderuuid", folderuid);
            folderMap
                .put("appguid", appId);
            WFolder oldfolder = getFolder(folderMap);// 获得文件夹
            String parentGuid = null;
            // 如果目标文件夹不为空,判断目标文件夹是否有效，且文件夹不重名
            if (StringUtils
                .isNotBlank(targetfolderuid))
            {
                Map<String, String> targetfolderMap =
                    new HashMap<String, String>();
                targetfolderMap
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                targetfolderMap
                    .put("folderuuid", targetfolderuid);
                targetfolderMap
                    .put("appguid", appId);
                parentGuid = getFolder(targetfolderMap)
                    .getGuid();
                if (!validateName(oldfolder
                    .getRepguid(), oldfolder
                    .getName(), parentGuid,
                    SystemParameter.FILE_FOLDER_AVIABLES))
                {
                    throw new ParameterException("ERROR.00404");
                }
            }
            // 如果目标文件夹为空，则取根为目标文件夹
            else
            {
                parentGuid = oldfolder
                    .getRepguid();
                Map<String, String> cabinetMap = new HashMap<String, String>();
                cabinetMap
                    .put("appId", appId);
                cabinetMap
                    .put("repuuid", cabinetuuid);
                WRepository repository = getCabinet(cabinetMap);
                targetfolderuid = repository
                    .getRepuuid();
                if (!validateName(oldfolder
                    .getRepguid(), oldfolder
                    .getName(), repository
                    .getGuid(), SystemParameter.FILE_FOLDER_AVIABLES))
                {
                    throw new ParameterException("ERROR.00404");
                }
            }
            if (checkChildFolder(oldfolder
                .getTreepath(), targetfolderuid))
            {
                // 文件夹不能移动到本身或子目录下
                throw new ParameterException("ERROR.00403");
            }
            WFolder tempFolder = new WFolder();
            BeanUtils
                .copyProperties(tempFolder, oldfolder);
            tempFolder
                .setPaguid(parentGuid);
            changePath(tempFolder);
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00428", e);
        }
    }

    /**
     * 批量移动文件夹
     * 
     * @param appId app Id
     * @param cabinetuuid cabinet uuid
     * @param folderuids folder uids
     * @param targetfolderuid target folder uid
     * @return list
     * @throws BusinessException Business Exception
     */
    public List<String> moveFolders(String appId, String cabinetuuid,
        List<String> folderuids, String targetfolderuid)
        throws BusinessException
    {
        try
        {
            if (StringUtils
                .isBlank(cabinetuuid) || StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00401");
            }
            List<String> list = new ArrayList<String>();
            if (folderuids == null || folderuids
                .size() <= 0)
            {
                throw new ParameterException("ERROR.00401");
            }
            for (int i = 0; i < folderuids
                .size(); i++)
            {
                String folderuuid = folderuids
                    .get(i);
                try
                {
                    moveFolder(appId, cabinetuuid, folderuuid, targetfolderuid);// 调用移动文件夹方法
                    list
                        .add(folderuuid);
                }
                catch (Exception e)
                {
                    // 捕获异常不做处理，原因：批量操作，不因单个操作失败中止
                    logger
                        .info("delete forld " + folderuuid + " failed.");
                }
            }
            return list;
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00429", e);
        }
    }

    /**
     * 批量删除文件夹
     * 
     * @param appId appId
     * @param folderuids folderuids
     * @param flag flag
     * @return list
     * @throws BusinessException Business Exception
     */
    public List<String> removeFolders(String appId, List<String> folderuids,
        String flag)
        throws BusinessException
    {
        try
        {
            if (StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00401");
            }
            List<String> list = new ArrayList<String>();
            if (folderuids == null || folderuids
                .size() <= 0)
            {
                throw new ParameterException("ERROR.00401");
            }
            for (int i = 0; i < folderuids
                .size(); i++)
            {
                String folderuuid = folderuids
                    .get(i);
                try
                {
                    // 调用删除文件夹
                    removeFolder(appId, folderuuid, flag);
                    list
                        .add(folderuuid);
                }
                catch (Exception e)
                {
                    logger
                        .error(e
                            .getMessage(), e);
                }
            }
            return list;
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00430", e);
        }
    }

    /**
     * 重命名文件夹
     * 
     * @param appId app Id
     * @param cabinetuuid cabinet uuid
     * @param folderuid folder uid
     * @param newfoldername new folder name
     * @throws BusinessException Business Exception
     */
    public void renameFolder(String appId, String cabinetuuid,
        String folderuid, String newfoldername)
        throws BusinessException
    {
        try
        {
            logger
                .debug("folderuuid :  " + folderuid);
            logger
                .debug("New File Name：  " + newfoldername);
            newfoldername = newfoldername
                .trim();
            if (StringUtils
                .isBlank(newfoldername
                    .trim()))
            {
                throw new ParameterException("ERROR.00405");
            }
            if (StringUtils
                .isBlank(cabinetuuid) || StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00401");
            }
            if (StringUtils
                .isBlank(folderuid
                    .trim()))
            {
                throw new ParameterException("ERROR.00401");
            }
            Map<String, String> cabinetMap = new HashMap<String, String>();
            cabinetMap
                .put("appId", appId);
            cabinetMap
                .put("repuuid", cabinetuuid);
            WRepository repository = getCabinet(cabinetMap);// 检测仓库有效性
            Map<String, String> folderMap = new HashMap<String, String>();
            folderMap
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            folderMap
                .put("folderuuid", folderuid);
            folderMap
                .put("appguid", appId);
            WFolder wfolder = getFolder(folderMap);// 检测文件夹有效性
            if (newfoldername
                .equals(wfolder
                    .getName()))
            {
                throw new BusinessException("ERROR.00402");
            }
            if (!repository
                .getGuid().equals(wfolder
                    .getRepguid()))
            {
                if (!validateName(cabinetuuid, newfoldername, wfolder
                    .getPaguid(), SystemParameter.FILE_FOLDER_AVIABLES))
                {
                    // 验证文件夹是否重名,根目录情况
                    throw new ParameterException("ERROR.00404");
                }
            }
            else
            {
                if (!validateName(cabinetuuid, newfoldername, repository
                    .getGuid(), SystemParameter.FILE_FOLDER_AVIABLES))
                {
                    // 验证文件夹是否重名，非根目录情况
                    throw new ParameterException("ERROR.00404");
                }
            }
            wfolder
                .setName(newfoldername);
            changePath(wfolder);// 修改文件夹下子文件夹路径
            getFolderEntityDao()
                .update(wfolder);
            MetaData metaData = this
                .getJCRService().getMetaData(folderuid, null);
            metaData
                .setName(newfoldername);
            this
                .getJCRService().updateMetaData(metaData);
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00431", e);
        }

    }

    /**
     * 检测名称 根据仓库guid 文件夹name 父文件夹uuid检测同路径下是否有同名的folder
     * 
     * @param repguid 仓库guid
     * @param name 文件夹name
     * @param parentfolderguid 父文件夹guid
     *@param avaible 文件可用标识
     * @return Boolean false- 重名，true-未重名
     * @throws Exception Exception
     */
    private Boolean validateName(String repguid, String name,
        String parentfolderguid, String avaible)
        throws Exception
    {
        try
        {
            logger
                .debug("repguid=" + repguid);
            logger
                .debug("name=" + name);
            logger
                .debug("parentfolderguid=" + parentfolderguid);
            EntityDao<WFolder> entityDao = getFolderEntityDao();
            Criteria criteria = entityDao
                .getEntityCriteria();
            criteria
                .add(Restrictions
                    .eq("name", name));// 文件夹名称
            criteria
                .add(Restrictions
                    .eq("repguid", repguid));// 仓库guid
            if (StringUtils
                .isNotBlank(avaible))
            {
                criteria
                    .add(Restrictions
                        .eq("avaible", avaible));
            }
            /* 如果父文件夹存在 */
            if (StringUtils
                .isNotBlank(parentfolderguid))
            {
                criteria
                    .add(Restrictions
                        .eq("paguid", parentfolderguid));// 父文件夹guid
            }
            entityDao
                .getByQbc(criteria);
            return false;
        }
        catch (Exception e)
        {
            return true;
        }
    }

    /**
     * 文件夹不能移动到本身或子目录下
     * 
     * @param path path
     * @param targetFolderuuid targetFolderuuid
     * @return boolean
     * @throws BusinessException Exception
     */
    private Boolean checkChildFolder(String path, String targetFolderuuid)
        throws BusinessException
    {
        try
        {
            String hql =
                "select c.id from ci_folder c where c.folderuuid=? and c.treepath like ?";
            List<?> list =
                this
                    .getJdbcTemplate().queryForList(hql,
                        new Object[] {targetFolderuuid, path + "%"});
            if (list != null && list
                .size() > 0)
            {
                return true;
            }
            return false;
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00423", e);
        }
    }

    /**
     * 递归寻找文件夹
     * 
     * @param wfolder 文件夹对象
     * @param targetFolderGuid 目标文件夹uuid
     * @param targetPath 目标treepath
     * @param targetFolderuuid 目标文件夹uuid
     * @param resultBuffer 需要填充的返回值
     * @throws Exception Exception
     */
    private void searchFolders(WFolder wfolder, String targetFolderGuid,
        String targetPath, String targetFolderuuid, ResultBuffer resultBuffer)
        throws Exception
    {
        String sql = null;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String tempPath = null;
        try
        {
            // 调用JCR Service创建Folder MetaData.
            MetaData metaData = new MetaData();
            metaData
                .setName(wfolder
                    .getName());
            metaData
                .setSize(0L);
            metaData
                .setParentUuid(targetFolderuuid);
            metaData
                .setCheckin(false);
            metaData
                .setCreateDate(DateUtils
                    .format(Calendar
                        .getInstance(), "yyyy-MM-dd HH:mm:ss"));
            metaData
                .setAppGuid(wfolder
                    .getAppGuid());
            getJCRService()
                .createMetaData(metaData);
            // 文件夹path生成处理
            RandomGUID folderrandomGUID = new RandomGUID();
            if (targetPath == null)
            {
                tempPath = "/" + wfolder
                    .getName() + FOLDERSPLIT;
            }
            else
            {
                tempPath = targetPath + "/" + wfolder
                    .getName() + FOLDERSPLIT;
            }
            // 插入文件夹表:拷贝后文件夹DB记录
            sql =
                "insert into ci_folder(edition,guid,repguid,appguid,name,paguid,createTime,totalUpFlux,totalDownFlux,"
                    + "totalDownFileNum,cost_Memory,treepath,avaible,folderuuid,tag,statistics) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            getJdbcTemplate()
                .update(
                    sql,
                    new Object[] {null, folderrandomGUID
                        .getValueAfterMD5(), wfolder
                        .getRepguid(), wfolder
                        .getAppGuid(), wfolder
                        .getName(), targetFolderGuid, DateUtils
                        .format(Calendar
                            .getInstance(), pattern), "0", "0", "0", "0",
                        tempPath, wfolder
                            .getAvaible(), metaData
                            .getUuid(), null, wfolder
                            .getStatistics()});
            // 将拷贝文件夹对像,加入结果集
            HashMap<String, String> map = resultBuffer
                .getFolderuuids();
            map
                .put(wfolder
                    .getFolderUuid(), metaData
                    .getUuid());
            resultBuffer
                .setFolderuuids(map);
            // 执行文件夹下文件拷贝
            copyFile(wfolder
                .getAppGuid(), wfolder
                .getGuid(), folderrandomGUID
                .getValueAfterMD5(), metaData
                .getUuid(), resultBuffer);
            // 获取拷贝源下子文件夹列表
            List<WFolder> tempList = getFolderEntityDao()
                .find("from WFolder where paguid=?", new Object[] {wfolder
                    .getGuid()});
            if (tempList
                .size() != 0)
            {
                // 依次进行子文件夹的拷贝
                for (int i = 0; i < tempList
                    .size(); i++)
                {
                    searchFolders(tempList
                        .get(i), folderrandomGUID
                        .getValueAfterMD5(), tempPath, metaData
                        .getUuid(), resultBuffer);// 递归寻找
                }
            }
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00424", e);
        }
    }

    private ResultBuffer copyFoldersEx(String appId, String cabinetuuid,
        List<String> folderuuids, String targetfolderuid)
    {
        ResultBuffer resultBuffer = new ResultBuffer();
        HashMap<String, String> resultBufferFolderMap = resultBuffer
            .getFolderuuids();
        HashMap<String, FileBuffer> resultBufferFileMap = resultBuffer
            .getFileBuffers();
        // 依次进行文件夹源拷贝操作
        for (int i = 0; i < folderuuids
            .size(); i++)
        {
            String folderuuid = folderuuids
                .get(i);
            try
            {
                // 调用复制文件夹方法
                ResultBuffer buffer =
                    copyFolder(appId, cabinetuuid, folderuuid, targetfolderuid);
                // 文件夹复制返回值对象组装
                HashMap<String, String> foldermap = buffer
                    .getFolderuuids();
                Iterator<Entry<String, String>> itfolder = foldermap
                    .entrySet().iterator();
                while (itfolder
                    .hasNext())
                {
                    Entry<String, String> entry = itfolder
                        .next();
                    resultBufferFolderMap
                        .put(entry
                            .getKey(), entry
                            .getValue());
                }
                // 文件复制返回值对象组装
                HashMap<String, FileBuffer> filemap = buffer
                    .getFileBuffers();
                Iterator<Entry<String, FileBuffer>> itfile = filemap
                    .entrySet().iterator();
                while (itfile
                    .hasNext())
                {
                    Entry<String, FileBuffer> entry = itfile
                        .next();
                    resultBufferFileMap
                        .put(entry
                            .getKey(), entry
                            .getValue());
                }
            }
            catch (Exception e)
            {
                // 异常只接收而不做处理，考虑批量复制执行不因某个失败而终止。
                logger
                    .info("Folder: " + folderuuid + "Copy Failed.");
            }
        }
        resultBuffer
            .setFolderuuids(resultBufferFolderMap);
        resultBuffer
            .setFileBuffers(resultBufferFileMap);

        return resultBuffer;
    }

    /**
     * 拷贝指定文件夹文件
     * 
     * @param appId appId
     * @param folderGuid 源文件夹guid
     * @param targetFolderGuid 目标文件夹guid
     * @param targetFolderuuid 目标文件夹uuid
     * @param resultBuffer 需填充的结果集
     * @throws BusinessException Business Exception
     */
    private void copyFile(String appId, String folderGuid,
        String targetFolderGuid, String targetFolderuuid,
        ResultBuffer resultBuffer)
        throws BusinessException
    {
        File oldfile = null;
        try
        {
            String writeRoot = SupportNode
                .getInstance().getHangsCarriesPath();
            HashMap<String, FileBuffer> filemap = resultBuffer
                .getFileBuffers();
            // 获取本机挂载路径列表
            List<WSupportNode> listWSupportNode =
                getWSupportByIp(com.wedo.businessserver.storage.jcr.Constants.LOCALIP);
            Calendar calendar = Calendar
                .getInstance();
            String time = DateUtils
                .format(calendar, "yyyy/MM/dd");
            // 文件实际存放位置
            File folderFile = new File(writeRoot + "/repository/date/" + time);
            if (!folderFile
                .exists())
            {
                @SuppressWarnings("unused")
                Boolean flag = folderFile
                    .mkdirs();
            }
            // 获取拷贝源文件夹下文件列�?
            String hql = "from WFile c where c.folderguid=?";
            List<WFile> list = getFileEntityDao()
                .find(hql, new Object[] {folderGuid});
            // 依次进行文件拷贝,并添加执行结果对像列�?
            final List<WFile> insertWfileList = new ArrayList<WFile>();
            for (int i = 0; i < list
                .size(); i++)
            {
                RandomGUID filerandomGUID = new RandomGUID();
                WFile wfile = list
                    .get(i);
                // 调用JCRService创建File MetaData
                MetaData metaData = this
                    .getJCRService().getMetaData(wfile
                        .getFileuuid(), null);
                MetaData newmetaData = new MetaData();
                newmetaData
                    .setName(metaData
                        .getName());
                newmetaData
                    .setParentUuid(targetFolderuuid);
                newmetaData
                    .setCheckin(true);
                newmetaData
                    .setAppGuid(wfile
                        .getAppGuid());
                newmetaData
                    .setSize(metaData
                        .getSize());
                newmetaData
                    .setCreateDate(DateUtils
                        .format(Calendar
                            .getInstance(), "yyyy-MM-dd HH:mm:ss"));
                newmetaData
                    .setAppGuid(appId);
                getJCRService()
                    .createMetaData(newmetaData);
                // 从本机挂载资源中查找源文件数�?,若遍列所有挂载资源均未获得源文件,则抛出异�?
                oldfile = findSourceFile(listWSupportNode, wfile);

                // 根据源文件拷贝生成新文件
                String newName = newmetaData
                    .getUuid() + "-" + newmetaData
                    .getVersion();
                File newfile =
                    new File(writeRoot + "/repository/date/" + time + "/"
                        + newName);
                FileUtils
                    .copyFile(oldfile, newfile);
                // 设置拷贝文件;并添加结果对像列�?,以便于DB记录的插�?
                WFile insertWFile = new WFile();
                insertWFile
                    .setGuid(filerandomGUID
                        .getValueAfterMD5());
                insertWFile
                    .setVersion(newmetaData
                        .getVersion());
                insertWFile
                    .setRepguid(wfile
                        .getRepguid());
                insertWFile
                    .setFpath("/repository/date/" + time + "/" + newName);
                insertWFile
                    .setAppGuid(wfile
                        .getAppGuid());
                insertWFile
                    .setVisitNumber(new BigDecimal("0"));
                insertWFile
                    .setDownNumber(new BigDecimal("0"));
                insertWFile
                    .setFileSize(metaData
                        .getSize());
                insertWFile
                    .setFName(wfile
                        .getFName());
                insertWFile
                    .setFileuuid(newmetaData
                        .getUuid());
                insertWFile
                    .setFolderguid(targetFolderGuid);
                insertWFile
                    .setAvaible(wfile
                        .getAvaible());
                insertWFile
                    .setMergeFlag(wfile
                        .getMergeFlag());
                insertWFile
                    .setEncryptFlag(wfile
                        .getEncryptFlag());
                insertWFile
                    .setCompressFlag(wfile
                        .getCompressFlag());
                insertWFile
                    .setAvaibleService(wfile
                        .getAvaibleService());
                insertWFile
                    .setMd5(wfile
                        .getMd5());
                insertWfileList
                    .add(insertWFile);
                // 设置拷贝执行结果列表
                FileBuffer fileBuffer = new FileBuffer();
                fileBuffer
                    .setFileuuid(newmetaData
                        .getUuid());
                fileBuffer
                    .setVersion(newmetaData
                        .getVersion());
                fileBuffer
                    .setCompressFlag(Integer
                        .valueOf(wfile
                            .getCompressFlag()));
                fileBuffer
                    .setEncryptFlag(Integer
                        .valueOf(wfile
                            .getEncryptFlag()));
                fileBuffer
                    .setMergeFlag(Integer
                        .valueOf(wfile
                            .getMergeFlag()));
                fileBuffer
                    .setMd5(wfile
                        .getMd5());
                filemap
                    .put(wfile
                        .getFileuuid(), fileBuffer);
            }
            // 批量添加文件记录
            // batchInsertWfile(insertWfileList);
            if (insertWfileList
                .size() > 0)
            {
                getJdbcTemplate()
                    .batchUpdate(
                        "insert into ci_file(guid,edition,repguid,fpath,appguid, visitNumber,downNumber,fileSize,"
                            + "fName, fileCreateTime,csnGuid,fileuuid,folderguid,avaible,mergeFlag,encryptFlag,"
                            + "compressFlag,tag,avaibleService,md5)  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new InsertWfileValueSetterImpl(insertWfileList));
            }
            // 添加拷贝后文件的索引处理
            if (DataServiceUtil
                .getAuthorization(appId).getFunction().getSearchEngine()
                .getAvailable())
            {
                addSearchTask(insertWfileList);
            }
            resultBuffer
                .setFileBuffers(filemap);
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00426", e);
        }
    }

    /**
     * 批量添加文件搜索任务
     * 
     * @param insertWfileList 文件列表
     * @throws BusinessException Business Exception
     */
    private void addSearchTask(List<WFile> insertWfileList)
        throws BusinessException
    {
        for (WFile tempFile : insertWfileList)
        {
            if (tempFile
                .getMergeFlag().equalsIgnoreCase(SystemParameter.UN_MERGE)
                && tempFile
                    .getEncryptFlag().equalsIgnoreCase(
                        SystemParameter.UNENCRYPT_FLAG)
                && tempFile
                    .getCompressFlag().equalsIgnoreCase(
                        SystemParameter.UNCOMPRESS_FLAG))
            {
                HashMap<String, String> folderMap =
                    new HashMap<String, String>();
                folderMap
                    .put("guid", tempFile
                        .getFolderguid());
                tempFile
                    .setFoldertreepath(getFolder(folderMap)
                        .getTreepath());
                threadPoolManager
                    .addTask(new SearchTask(
                        new IkIndexWriter(),
                        tempFile,
                        com.wedo.businessserver.searchengine.Constants.INCREMENT_FLAG));
            }
        }
    }

    /**
     * 获取云存储中实体文件数据
     * 
     * @param listWSupportNode listWSupportNode
     * @param wfile wfile
     * @return File file
     * @throws BusinessException Business Exception
     */
    private File findSourceFile(List<WSupportNode> listWSupportNode, WFile wfile)
        throws BusinessException
    {
        File oldfile = null;
        for (int m = 0; m < listWSupportNode
            .size(); m++)
        {
            String readroot = listWSupportNode
                .get(m).getMountPath();
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
            throw new BusinessException("ERROR.00425");
        }
        return oldfile;
    }

    /**
     * 删除文件夹
     * 
     * @param appId app Id
     * @param folderuid folder uid
     * @param flag flag
     * @throws BusinessException Business Exception
     */
    @SuppressWarnings("unchecked")
    public void removeFolder(String appId, String folderuid, String flag)
        throws BusinessException
    {
        try
        {
            if (StringUtils
                .isBlank(folderuid) || StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00401");
            }
            Map<String, String> folderMap = new HashMap<String, String>();
            logger
                .debug("appId=" + appId);
            logger
                .debug("folderuid=" + folderuid);
            logger
                .debug("flag=" + flag);
            folderMap
                .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
            folderMap
                .put("folderuuid", folderuid);
            folderMap
                .put("appguid", appId);
            WFolder wfolder = getFolder(folderMap);
            // 获得文件夹下面的文件夹guid列表
            List guidList =
                this
                    .getJdbcTemplate()
                    .queryForList(
                        "select c.guid from ci_folder c where c.avaible=? and c.appguid=? and c.treepath like ?",
                        new Object[] {SystemParameter.FILE_FOLDER_AVIABLES,
                            appId, wfolder
                                .getTreepath() + "%"});
            // 前数据返回列表集转成guid列表
            final List<Map<String, String>> sqlList =
                new ArrayList<Map<String, String>>();
            for (int i = 0; i < guidList
                .size(); i++)
            {
                String guid = (String) ((Map) guidList
                    .get(i))
                    .get("guid");
                Map<String, String> map = new HashMap<String, String>();
                map
                    .put(guid, flag);
                sqlList
                    .add(map);
                WFile indexFile = new WFile();
                indexFile
                    .setFolderguid(guid);
            }
            // 删除文件夹
            this
                .getJdbcTemplate()
                .update(
                    "update ci_folder c set c.avaible=? where c.avaible=? and c.appguid=? and c.treepath like ?",
                    new Object[] {flag, SystemParameter.FILE_FOLDER_AVIABLES,
                        appId, wfolder
                            .getTreepath() + "%"});
            // 删除文件,批量处理子文件夹下的文件删除
            if (sqlList != null && sqlList
                .size() > 0)
            {
                this
                    .getJdbcTemplate()
                    .batchUpdate(
                        "update ci_file c set c.avaible=? where c.avaible=? and c.folderguid=?",
                        new DelWfileValueSetterImpl(sqlList));
            }
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00421", e);
        }

    }

    /**
     * 修改文件夹下子文件夹路径
     * 
     * @param folder folder
     * @throws BusinessException Business Exception
     */
    private void changePath(WFolder folder)
        throws BusinessException
    {
        try
        {
            String treepath = folder
                .getTreepath();
            String hql =
                "from WFolder c where c.avaible=? and c.appGuid=? and c.treepath like ?";
            List<WFolder> list =
                getFolderEntityDao()
                    .find(
                        hql,
                        new Object[] {SystemParameter.FILE_FOLDER_AVIABLES,
                            folder
                                .getAppGuid(), treepath + "%"});
            if (list != null && list
                .size() > 0)
            {
                final List<Map<Long, String>> sqlList;
                String parentpath = null;
                Map<String, String> parentfolderMap =
                    new HashMap<String, String>();
                parentfolderMap
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                parentfolderMap
                    .put("guid", folder
                        .getPaguid());
                parentfolderMap
                    .put("appguid", folder
                        .getAppGuid());
                WFolder parentFolder = getFolder(parentfolderMap);
                if (StringUtils
                    .isBlank(parentFolder
                        .getTreepath()))
                {
                    parentpath = "/";
                }
                else
                {
                    parentpath = parentFolder
                        .getTreepath() + "/";
                }
                sqlList = changeFolderList(list, parentpath, folder);
                if (sqlList != null && sqlList
                    .size() > 0)
                {
                    /** 修改批量更新文件夹sql为传入参数方式 */
                    getJdbcTemplate()
                        .batchUpdate(
                            "update ci_folder c set c.treepath=? where c.id=?",
                            new ChangePathValueSetterImpl(sqlList));
                }
            }
            /** 修改update文件夹sql为传出参数形式 */
            Object[] values = new Object[] {folder
                .getPaguid(), 0, folder
                .getFolderUuid()};
            getJdbcTemplate()
                .update(
                    "update ci_folder c set c.paguid=? where c.avaible=? and c.folderuuid=?",
                    values);
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00422", e);
        }
    }

    private List<Map<Long, String>> changeFolderList(List<WFolder> list,
        String parentpath, WFolder folder)
    {

        List<Map<Long, String>> sqlList = new ArrayList<Map<Long, String>>();
        int depth = folder
            .getTreepath().split("/").length;

        for (int i = 0; i < list
            .size(); i++)
        {
            String[] tempPath = list
                .get(i).getTreepath().split("/");

            String ownerpath = parentpath + folder
                .getName() + FOLDERSPLIT;
            StringBuffer tempbuffer = new StringBuffer();
            tempbuffer
                .append(ownerpath);
            for (int j = depth; j < tempPath.length; j++)
            {
                if (StringUtils
                    .isNotBlank(tempPath[j]))
                {
                    tempbuffer
                        .append("/" + tempPath[j]);
                }
            }
            Map<Long, String> sqlHashMap = new HashMap<Long, String>();
            sqlHashMap
                .put(list
                    .get(i).getId(), tempbuffer
                    .toString());
            sqlList
                .add(sqlHashMap);
        }
        return sqlList;
    }

    /**
     * 创建文件夹
     * 
     * @param appId 应用guid
     * @param cabinetuid 仓库uuid
     * @param folderuid 父文件夹uuid
     * @param foldername 要创建的文件夹name
     * @param statistics 是否统计标识
     * @return string
     * @throws BusinessException Business Exception
     */
    public String createFolder(String appId, String cabinetuid,
        String folderuid, String foldername, String statistics)
        throws BusinessException
    {
        try
        {
            logger
                .debug("appId=" + appId);
            logger
                .debug("cabinetuid=" + cabinetuid);
            logger
                .debug("folderuid=" + folderuid);
            logger
                .debug("foldername=" + foldername);
            logger
                .debug("statistics=" + statistics);
            foldername = foldername
                .trim();
            if (StringUtils
                .isBlank(foldername
                    .trim()))
            {
                throw new ParameterException("ERROR.00400");
            }
            if (StringUtils
                .isBlank(cabinetuid) || StringUtils
                .isBlank(appId))
            {
                throw new ParameterException("ERROR.00400");
            }
            if (StringUtils
                .isBlank(statistics))
            {
                statistics = SystemParameter.UNAVAIBLE_SERVICE;// 如果为空，默认为不统计
            }
            Map<String, String> cabinetMap = new HashMap<String, String>();
            cabinetMap
                .put("appId", appId);
            cabinetMap
                .put("repuuid", cabinetuid);
            String treepath = null;
            WRepository wrepository = getCabinet(cabinetMap);// 检测仓库有效性
            WFolder parentfolder = null;
            String parentuuid = null;
            String parentGuid = null;
            if (StringUtils
                .isNotBlank(folderuid))
            {
                Map<String, String> folderMap = new HashMap<String, String>();
                folderMap
                    .put("avaible", SystemParameter.FILE_FOLDER_AVIABLES);
                folderMap
                    .put("folderuuid", folderuid);
                folderMap
                    .put("appguid", appId);
                parentfolder = getFolder(folderMap);
                parentuuid = folderuid;
                parentGuid = parentfolder
                    .getGuid();
                // 父文件的treepath加上自己的文件夹名称作为treepath
                treepath = parentfolder
                    .getTreepath() + "/" + foldername + FOLDERSPLIT;
            }
            else
            {
                parentuuid = cabinetuid;
                parentGuid = wrepository
                    .getGuid();
                // 父文件夹不存在，从根路径开始构建
                treepath = "/" + foldername + FOLDERSPLIT;
            }
            // 判断文件夹是否重名
            if (!validateName(wrepository
                .getGuid(), foldername, parentGuid,
                SystemParameter.FILE_FOLDER_AVIABLES))
            {
                throw new ParameterException("ERROR.00404");
            }
            WFolder wFolder = new WFolder();
            RandomGUID randomGUID = new RandomGUID();
            wFolder
                .setGuid(randomGUID
                    .getValueAfterMD5());
            wFolder
                .setRepguid(wrepository
                    .getGuid());
            wFolder
                .setAppGuid(wrepository
                    .getAppGuid());
            wFolder
                .setName(foldername);
            wFolder
                .setCreateTime(Calendar
                    .getInstance()); // 创建时间
            wFolder
                .setTotalDownFlux(new BigDecimal("0"));// 下载流量
            wFolder
                .setTotalUpFlux(new BigDecimal("0")); // 上传流量
            wFolder
                .setTotalDownFileNum(new BigDecimal("0"));// 下载次数
            wFolder
                .setCostMemory(new BigDecimal("0"));// 占用空间
            wFolder
                .setAvaible(SystemParameter.FILE_FOLDER_AVIABLES); // 有效性
            wFolder
                .setStatistics(statistics);
            wFolder
                .setTreepath(treepath);
            wFolder
                .setPaguid(parentGuid);
            /** JFolder为面向底层的文件夹对象 wFolder为面向上层的文件夹对象 */
            // JFolder folder = new JFolder();
            // folder.setName(foldername);
            // folder.setSize(0L);
            // folder.setParentUuid(parentuuid);
            // folder.setCreateDate(Calendar.getInstance());
            // getJCRService().createFolder(folder);// 调用JCR Service创建Floder
            MetaData metaData = new MetaData();
            metaData
                .setCheckin(false);
            metaData
                .setName(foldername);
            metaData
                .setSize(0L);
            metaData
                .setParentUuid(parentuuid);
            metaData
                .setCreateDate(DateUtils
                    .format(Calendar
                        .getInstance(), "yyyy-MM-dd HH:mm:ss"));
            metaData
                .setAppGuid(appId);
            getJCRService()
                .createMetaData(metaData);
            wFolder
                .setFolderUuid(metaData
                    .getUuid());// JCR创建floder后获得的uuid
            getFolderEntityDao()
                .save(wFolder);// 保存到数据库
            return metaData
                .getUuid();
        }
        catch (DataAccessException e)
        {
            throw new BusinessException("ERROR.00050", e);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("ERROR.00420", e);
        }
    }

}
