package com.wedo.businessserver.css3.ws.service;

import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.service.WFolderService;
import com.wedo.businessserver.css3.ws.model.FilePageInfo;
import com.wedo.businessserver.css3.ws.model.FolderBuffer;
import com.wedo.businessserver.css3.ws.model.Keyassignments;
import com.wedo.businessserver.css3.ws.model.Result;
import com.wedo.businessserver.css3.ws.model.ResultBuffer;

/**
 * 文件夹操作的Service
 * 
 * @author c90003207
 */
@WebService
public class FolderService
{
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(FolderService.class);
    
    /**
     * 国际化用到的工具类
     */
    private static ResourceBundle msg = LanguageUtil.getMessage();
    
    private WFolderService wFolderService =
        (WFolderService) BaseStaticContextLoader.getApplicationContext()
            .getBean("wFolderService");
    
    /**
     * 创建文件夹
     * 
     * @param appId 应用appid
     * @param cabinet 倉庫名稱
     * @param folderuid 父文件夹uid
     * @param foldername 文件夹名称
     * @param statistics 是否统计
     * @return 文件夹uid
     */
    public FolderBuffer createFolder(String appId, String cabinet,
        String folderuid, String foldername, String statistics)
    {
        FolderBuffer folderBuffer = new FolderBuffer();
        try
        {
            String folderuuid =
                wFolderService.createFolder(appId, cabinet, folderuid,
                    foldername, statistics);
            folderBuffer.setFolderUuid(folderuuid);
            folderBuffer.setMessage(Constants.SUCCESS);
            return folderBuffer;
        }
        catch (BusinessException e)
        {
            logger.info(msg.getString("ERROR.00420"), e);
            folderBuffer.setMessage(e.getMessage());
        }
        return folderBuffer;
    }
    
    /**
     * 重命名文件夹
     * 
     * @param appid 应用appid
     * @param cabinet 仓库uuid
     * @param folderuid 文件夹uuid
     * @param newfoldername 新文件夹名称
     * @return return value
     */
    public Result renameFolder(String appid, String cabinet, String folderuid,
        String newfoldername)
    {
        Result result = new Result();
        try
        {
            wFolderService.renameFolder(appid, cabinet, folderuid,
                newfoldername);
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (BusinessException e)
        {
            logger.info(msg.getString("ERROR.00431"), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除文件夹
     * 
     * @param appId 应用appid
     * @param folderuid 文件夹uuid
     * @param flag 删除标志位
     * @return return value
     */
    public Result removeFolder(String appId, String folderuid, String flag)
    {
        Long start = System.currentTimeMillis();
        Result result = new Result();
        try
        {
            wFolderService.removeFolder(appId, folderuid, flag);
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (BusinessException e)
        {
            logger.info(msg.getString("ERROR.00421"), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        Long end = System.currentTimeMillis();
        logger.info(msg.getString("msg.folder.remove") + (end - start) + "ms");
        return result;
    }
    
    /**
     * 批量删除文件夹
     * 
     * @param appId 应用appid
     * @param folderuids 文件夹uuid列表
     * @param flag 删除标志
     * @return return value
     */
    public List<String> removeFolders(String appId, List<String> folderuids,
        String flag)
    {
        Long start = System.currentTimeMillis();
        try
        {
            List<String> list =
                wFolderService.removeFolders(appId, folderuids, flag);
            Long end = System.currentTimeMillis();
            logger.info(msg.getString("msg.folders.remove") + (end - start)
                + "ms");
            return list;
        }
        catch (BusinessException e)
        {
            logger.info(msg.getString("ERROR.00430"), e);
            return null;
        }
    }
    
    /**
     * 移动文件夹
     * 
     * @param appId 应用appid
     * @param cabinetuuid 仓库uuid
     * @param folderuid 文件夹uuid
     * @param targetfolderuid 目标文件夹uuid
     * @return return value
     */
    public Result moveFolder(String appId, String cabinetuuid,
        String folderuid, String targetfolderuid)
    {
        Long start = System.currentTimeMillis();
        Result result = new Result();
        try
        {
            wFolderService.moveFolder(appId, cabinetuuid, folderuid,
                targetfolderuid);
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00428"), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        Long end = System.currentTimeMillis();
        logger.info(msg.getString("msg.folder.move") + (end - start) + "ms");
        return result;
    }
    
    /**
     * 批量移动文件夹
     * 
     * @param appId 应用appid
     * @param cabinetuuid 仓库uuid
     * @param folderuids 文件夹uuid列表
     * @param targetfolderuid 目标文件夹uuid
     * @return return value
     */
    public List<String> moveFolders(String appId, String cabinetuuid,
        List<String> folderuids, String targetfolderuid)
    {
        try
        {
            Long start = System.currentTimeMillis();
            List<String> list =
                wFolderService.moveFolders(appId, cabinetuuid, folderuids,
                    targetfolderuid);
            Long end = System.currentTimeMillis();
            logger.info(msg.getString("msg.folders.move") + (end - start)
                + "ms");
            return list;
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00429"), e);
            return null;
        }
    }
    
    /**
     * 设置文件夹元数据
     * 
     * @param appId 应用appguid
     * @param folderuuid 文件夹uuid
     * @param version 文件版本
     * @param metadatas 元数据
     * @return return value
     */
    public Result setFolderMetadata(String appId, String folderuuid,
        String version, List<Keyassignments> metadatas)
    {
        return null;
    }
    
    /**
     * 获得文件夹元数据
     * 
     * @param appId 应用appguid
     * @param folderuuid 文件夹uuid
     * @param version 文件夹版本
     * @return 元数据
     */
    public List<Keyassignments> getFolderMetadata(String appId,
        String folderuuid, String version)
    {
        return null;
    }
    
    /**
     * 拷贝文件夹
     * 
     * @param appId 应用id
     * @param cabinetuuid 仓库uuid
     * @param folderuuid 文件夹uuid
     * @param targetfolderuid 目标文件夹uuid
     * @return return value
     */
    public ResultBuffer copyFolder(String appId, String cabinetuuid,
        String folderuuid, String targetfolderuid)
    {
        Long start = System.currentTimeMillis();
        ResultBuffer resultBuffer = new ResultBuffer();
        try
        {
            logger.info(msg.getString("msg.folder.copy"));
            resultBuffer =
                wFolderService.copyFolder(appId, cabinetuuid, folderuuid,
                    targetfolderuid);
            resultBuffer.setMessage(Constants.SUCCESS);
            resultBuffer.setFlag(true);
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00424"), e);
            resultBuffer.setFlag(false);
            resultBuffer.setMessage(e.getMessage());
        }
        Long end = System.currentTimeMillis();
        logger.info(msg.getString("msg.folder.copy") + (end - start) + "ms");
        return resultBuffer;
    }
    
    /**
     * 拷贝文件夹
     * 
     * @param appId 应用id
     * @param cabinetuuid 仓库uuid
     * @param folderuuids 文件夹uuid
     * @param targetfolderuid 目标文件夹uuid
     * @return return value
     */
    public ResultBuffer copyFolders(String appId, String cabinetuuid,
        List<String> folderuuids, String targetfolderuid)
    {
        Long start = System.currentTimeMillis();
        ResultBuffer resultBuffer = new ResultBuffer();
        try
        {
            logger.info(msg.getString("msg.folder.copy_2"));
            resultBuffer =
                wFolderService.copyFolders(appId, cabinetuuid, folderuuids,
                    targetfolderuid);
            resultBuffer.setMessage(Constants.SUCCESS);
            resultBuffer.setFlag(true);
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00427"), e);
            resultBuffer.setFlag(false);
            resultBuffer.setMessage(e.getMessage());
        }
        Long end = System.currentTimeMillis();
        logger.info(msg.getString("msg.folders.copy") + (end - start) + "ms");
        return resultBuffer;
    }
    
    /**
     * 搜索关键字
     * 
     * @param appId 应用id
     * @param keyWord 关键字
     * @param folderuuid 文件夹uuid
     * @param filePageInfo 分页对象
     * @return return value
     * @throws BusinessException
     */
    public FilePageInfo search(String appId, String keyWord, String folderuuid,
        FilePageInfo filePageInfo)
    {
        try
        {
            Long start = System.currentTimeMillis();
            wFolderService.search(appId, keyWord, folderuuid, filePageInfo,
                null);
            filePageInfo.setMessage(Constants.SUCCESS);
            Long end = System.currentTimeMillis();
            logger.info(msg.getString("OPERATION_SUCCESS"));
            logger.info("It take " + (end - start) + "ms");
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00580"), e);
            filePageInfo.setMessage(e.getMessage());
        }
        return filePageInfo;
        
    }
}
