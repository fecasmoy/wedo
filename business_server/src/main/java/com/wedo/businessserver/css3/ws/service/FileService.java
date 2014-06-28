package com.wedo.businessserver.css3.ws.service;

import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.service.WFileService;
import com.wedo.businessserver.css3.ws.model.FileBuffer;
import com.wedo.businessserver.css3.ws.model.Keyassignments;
import com.wedo.businessserver.css3.ws.model.Result;

/**
 * the file service object
 * 
 * @author c90003207
 * 
 */
@WebService
public class FileService
{
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(FileService.class);
    
    private ResourceBundle msg = LanguageUtil.getMessage();
    
    private WFileService wFileService =
        (WFileService) BaseStaticContextLoader.getApplicationContext().getBean(
            "wFileService");
    
    /**
     * 文件重命名
     * 
     * @param appId appId
     * @param fileuuid fileuuid
     * @param newfilename 文件新名称
     * @return return value
     */
    public Result renameFile(String appId, String fileuuid, String newfilename)
    {
        Result result = new Result();
        try
        {
            wFileService.renameFile(appId, fileuuid, newfilename);
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    /**
     * 大文件分片上传
     * 
     * @param appId 应用
     * @param cabinetuuid 仓库
     * @param folderuuid 文件夹
     * @param buffer 数据
     * 
     * @return 返回文件uid
     */
    public FileBuffer uploadFile(String appId, String cabinetuuid,
        String folderuuid, FileBuffer buffer)
    {
        try
        {
            FileBuffer fileBuffer =
                wFileService.uploadFile(appId, cabinetuuid, folderuuid, buffer);
            fileBuffer.setMessage(Constants.SUCCESS);
            return fileBuffer;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            FileBuffer fileBuffer = new FileBuffer();
            fileBuffer.setMessage(e.getMessage());
            return fileBuffer;
        }
    }
    
    /**
     * 大文件下載
     * 
     * @param appId 应用
     * @param buffer 文件数据
     * 
     * @return return value
     */
    public FileBuffer downloadFile(String appId, FileBuffer buffer)
    {
        try
        {
            return wFileService.downloadFile(appId, buffer);
        }
        catch (Exception e)
        {
            logger.error(msg.getString("ERROR.00524"), e);
            FileBuffer fileBuffer = new FileBuffer();
            fileBuffer.setMessage(e.getMessage());
            return fileBuffer;
        }
    }
    
    /**
     * 移动文件
     * 
     * @param appId 应用ID
     * @param cabinetuuid 仓库UUID
     * @param fileuuid 文件uid
     * @param folderuid 目標文件夾uid
     * @return return value
     */
    public Result moveFile(String appId, String cabinetuuid, String fileuuid,
        String folderuid)
    {
        Result result = new Result();
        try
        {
            wFileService.moveFile(appId, cabinetuuid, fileuuid, folderuid);
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (BusinessException e)
        {
            logger.error(msg.getString("ERROR.00526"), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除文件
     * 
     * @param appId 应用ID
     * @param version 版本
     * @param fileuuid 文件uid
     * @param flag 文件标示
     * @return return value
     */
    public Result removeFile(String appId, String version, String fileuuid,
        String flag)
    {
        Result result = new Result();
        try
        {
            wFileService.removeFile(appId, version, fileuuid, flag);
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (BusinessException e)
        {
            logger.error(msg.getString("ERROR.00529"), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    /**
     * 批量移动文件
     * 
     * @param appId 应用ID
     * @param cabinetuuid 仓库ID
     * @param fileuid 文件uid
     * @param folderuid 目標文件夾uid
     * @return return value
     */
    public List<FileBuffer> moveFiles(String appId, String cabinetuuid,
        List<String> fileuid, String folderuid)
    {
        try
        {
            return wFileService.moveFiles(appId, cabinetuuid, fileuid,
                folderuid);
        }
        catch (Exception e)
        {
            logger.error(msg.getString("ERROR.00527"), e);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 批量删除文件
     * 
     * @param appId 应用ID
     * @param fileuuids 文件uid列表
     * @param flag 文件标识
     * @return return value
     */
    public List<String> removeFiles(String appId, List<String> fileuuids,
        String flag)
    {
        try
        {
            return wFileService.removeFiles(appId, fileuuids, flag);
        }
        catch (BusinessException e)
        {
            logger.error(e.getMessage(), e);
            logger.info(msg.getString("ERROR.00530"));
            return null;
        }
    }
    
    /**
     * 复制文件
     * 
     * @param appId 應用ID
     * @param cabinetuuid 倉庫uuid
     * @param folderuuid 目標文件夾uuid
     * @param fileuuid fileuuid
     * @return return value
     */
    public FileBuffer copyFile(String appId, String cabinetuuid,
        String folderuuid, String fileuuid)
    {
        FileBuffer buffer = new FileBuffer();
        try
        {
            buffer =
                wFileService.copyFile(appId, cabinetuuid, folderuuid, fileuuid);
            buffer.setMessage(Constants.SUCCESS);
        }
        catch (BusinessException e)
        {
            logger.error(msg.getString("ERROR.00522"), e);
            buffer.setMessage("ERROR.00522");
        }
        return buffer;
    }
    
    /**
     * 批量复制文件
     * 
     * @param appId appId
     * @param cabinetuuid cabinetuuid
     * @param folderuuid folderuuid
     * @param fileuuids fileuuids
     * @return return value
     */
    public List<FileBuffer> copyFiles(String appId, String cabinetuuid,
        String folderuuid, List<String> fileuuids)
    {
        try
        {
            return wFileService.copyFiles(appId, cabinetuuid, folderuuid,
                fileuuids);
        }
        catch (BusinessException e)
        {
            logger.error(msg.getString("ERROR.00523"), e);
            return null;
        }
    }
    
    /**
     * 设置文件元数据
     * 
     * @param appId appId
     * @param fileuuid file uuid
     * @param version version
     * @param metadatas metadatas
     * @return return value
     */
    public Result setFileMetadata(String appId, String fileuuid,
        String version, List<Keyassignments> metadatas)
    {
        Result result = new Result();
        try
        {
            wFileService.setFileMetadata(appId, fileuuid, version, metadatas);
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (BusinessException e)
        {
            logger.error(msg.getString("ERROR.00594"), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    /**
     * 获得文件元数据
     * 
     * @param appId appId
     * @param fileuuid file uuid
     * @param version version
     * @return return value
     */
    public List<Keyassignments> getFileMetadata(String appId, String fileuuid,
        String version)
    {
        try
        {
            return wFileService.getFileMetadata(appId, fileuuid, version);
        }
        catch (BusinessException e)
        {
            logger.error(msg.getString("ERROR.00595"), e);
            return null;
        }
    }
    
    /**
     * 提交
     * 
     * @param appId appId
     * @param fileuuid fileuuid
     * @param mergeFlag mergeFlag
     * @param encryptFlag encryptFlag
     * @param compressFlag compressFlag
     * @param md5 md5
     * @return return value
     */
    public FileBuffer commit(String appId, String fileuuid, String mergeFlag,
        String encryptFlag, String compressFlag, String md5)
    {
        try
        {
            return wFileService.tocommit(appId, fileuuid, mergeFlag,
                encryptFlag, compressFlag, md5);
        }
        catch (Exception e)
        {
            logger.error(msg.getString("ERROR.00532"), e);
            FileBuffer fileBuffer = new FileBuffer();
            fileBuffer.setMessage(e.getMessage());
            return fileBuffer;
        }
    }
}
