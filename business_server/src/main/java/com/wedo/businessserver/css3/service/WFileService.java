package com.wedo.businessserver.css3.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wedo.businessserver.common.dao.support.PageInfo;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WFileHistory;
import com.wedo.businessserver.css3.ws.model.FileBuffer;
import com.wedo.businessserver.css3.ws.model.Keyassignments;

/**
 * 文件操作接口类
 * 
 * @author c90003207
 * 
 */
public interface WFileService
{
    
    /**
     * 文件重命名
     * 
     * @param appId appid
     * @param fileUid 文件uid
     * @param newfilename 文件新名称
     * @throws BusinessException exception
     */
    public void renameFile(String appId, String fileUid, String newfilename)
        throws BusinessException;
    
    /**
     * 移动文件
     * 
     * @param appId appid
     * @param cabinetuuid cabinet uuid
     * @param fileuid 文件uid
     * @param folderuid 目標文件夾uid
     * @return FileBuffer {@link FileBuffer}
     * @throws BusinessException Business Exception
     */
    public FileBuffer moveFile(String appId, String cabinetuuid,
        String fileuid, String folderuid)
        throws BusinessException;
    
    /**
     * 删除文件
     * 
     * @param appId appId
     * @param version version
     * @param fileuid 文件uid
     * @param flag flag
     * @throws BusinessException Business Exception
     */
    public void removeFile(String appId, String version, String fileuid,
        String flag)
        throws BusinessException;
    
    /**
     * 批量移动文件
     * 
     * @param appId appid
     * @param cabinetuuid cabinetuuid
     * @param fileuid 文件uid
     * @param folderuid 目標文件夾uid
     * @return list
     * @throws BusinessException Business Exception
     */
    public List<FileBuffer> moveFiles(String appId, String cabinetuuid,
        List<String> fileuid, String folderuid)
        throws BusinessException;
    
    /**
     * 批量删除文件
     * 
     * @param appId appid
     * @param fileuuids 文件uid列表
     * @param flag flag
     * @return List {@link List}
     * @throws BusinessException Exception
     */
    public List<String> removeFiles(String appId, List<String> fileuuids,
        String flag)
        throws BusinessException;
    
    /**
     * 复制文件
     * 
     * @param appId 應用ID
     * @param cabinetuuid 倉庫uuid
     * @param folderuuid 目標文件夾uuid
     * @param fileuuid file uuid
     * @return FileBuffer {@link FileBuffer}
     * @throws BusinessException Exception
     */
    public FileBuffer copyFile(String appId, String cabinetuuid,
        String folderuuid, String fileuuid)
        throws BusinessException;
    
    /**
     * 批量复制文件
     * 
     * @param appId appid
     * @param cabinetuuid cabinet uuid
     * @param folderuuid folderuuid
     * @param fileuuids fileuuid
     * @return List {@link List}
     * @throws BusinessException Exception
     */
    public List<FileBuffer> copyFiles(String appId, String cabinetuuid,
        String folderuuid, List<String> fileuuids)
        throws BusinessException;
    
    /**
     * 得到文件
     * 
     * @param map map
     * @return WFile {@link WFile}
     * @throws BusinessException Exception
     */
    public WFile getWFile(Map<String, String> map)
        throws BusinessException;
    
    /**
     * 得到历史文件
     * 
     * @param map map
     * @return WFileHistory {@link WFileHistory}
     * @throws BusinessException Exception
     */
    public WFileHistory getWFileHistory(Map<String, String> map)
        throws BusinessException;
    
    /**
     * 插入历史文件
     * 
     * @param wfileHistory history
     * @throws BusinessException BusinessException
     */
    public void saveWFileHistory(WFileHistory wfileHistory)
        throws BusinessException;
    
    /**
     * 更新文件
     * 
     * @param wfile wfile
     * @throws BusinessException Business Exception
     */
    public void updateWFile(WFile wfile)
        throws BusinessException;
    
    /**
     * 新增文件
     * 
     * @param wfile files
     * @throws BusinessException Business Exception
     */
    public void saveWFile(WFile wfile)
        throws BusinessException;
    
    /**
     * 上传时检验重名
     * 
     * @param appId appid
     * @param cabinetuuid cabinet uuid
     * @param folderuuid folder uuid
     * @param filename file name
     * @return HashMap {@link HashMap}
     * @throws BusinessException BusinessException
     */
    public HashMap<String, String> uploadvlidateName(String appId,
        String cabinetuuid, String folderuuid, String filename)
        throws BusinessException;
    
    /**
     * 下载文件
     * 
     * @param appId appid
     * @param buffer buffer
     * @return {@link FileBuffer}
     * @throws BusinessException BusinessException
     * @throws IOException io exceptiom
     */
    public FileBuffer downloadFile(String appId, FileBuffer buffer)
        throws BusinessException, IOException;
    
    /**
     * 上传文件
     * 
     * @param appId appid
     * @param cabinetuuid cabinet uuid
     * @param folderuuid folder uuid
     * @param buffer buffer
     * @return FileBuffer {@link FileBuffer}
     * @throws BusinessException Business Exception
     * @throws IOException io exceptiom
     */
    public FileBuffer uploadFile(String appId, String cabinetuuid,
        String folderuuid, FileBuffer buffer)
        throws BusinessException, IOException;
    
    /**
     * 提交
     * 
     * @param appId appId
     * @param fileuuid fileuuid
     * @param mergeFlag mergeFlag
     * @param encryptFlag encryptFlag
     * @param compressFlag compressFlag
     * @param md5 md5
     * @return FileBuffer
     * @throws BusinessException BusinessException
     */
    public FileBuffer tocommit(String appId, String fileuuid, String mergeFlag,
        String encryptFlag, String compressFlag, String md5)
        throws BusinessException;
    
    /**
     * 分页查询WFILE
     * 
     * @param map map
     * @param pageInfo pageInfo
     * @throws BusinessException Business Exception
     */
    public void pageWFle(HashMap<String, String> map, PageInfo<WFile> pageInfo)
        throws BusinessException;
    
    /**
     * 分页查询WFILEHISTORY
     * 
     * @param map map
     * @param pageInfo pageInfo
     * @throws BusinessException Business Exception
     */
    public void pageWFleHistory(HashMap<String, String> map,
        PageInfo<WFileHistory> pageInfo)
        throws BusinessException;
    
    /**
     * 设置元数据
     * 
     * @param appId appId
     * @param fileuuid fileuuid
     * @param version version
     * @param metadatas metadatas
     * @throws BusinessException Business Exception
     */
    public void setFileMetadata(String appId, String fileuuid, String version,
        List<Keyassignments> metadatas)
        throws BusinessException;
    
    /**
     * 得到元数据
     * 
     * @param appId appId
     * @param fileuuid fileuuid
     * @param version version
     * @return list
     * @throws BusinessException Business Exception
     */
    public List<Keyassignments> getFileMetadata(String appId, String fileuuid,
        String version)
        throws BusinessException;
    
    /**
     * 杀毒后回滚
     * 
     * @param fileuuid fileuuid
     * @param version version
     * @throws BusinessException Business Exception
     */
    public void rollback(String fileuuid, String version)
        throws BusinessException;
}
