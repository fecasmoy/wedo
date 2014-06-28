package com.wedo.businessserver.css3.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.css3.domain.WFolder;
import com.wedo.businessserver.css3.ws.model.FilePageInfo;
import com.wedo.businessserver.css3.ws.model.ResultBuffer;

/**
 * 文件夹操作接口类
 * 
 * @author c90003207
 * 
 */
public interface WFolderService
{

    /**
     * 获得文件夹
     * 
     * @param map map
     * @return value
     * @throws BusinessException Business Exception
     */
    public WFolder getFolder(Map<String, String> map)
        throws BusinessException;

    /**
     * 创建文件夹
     * 
     * @param appId 应用guid
     * @param cabinetuuid 仓库uuid
     * @param folderuid 文件夹uuid
     * @param foldername 文件夹名称
     * @param statistics 是否统计
     * @return value
     * @throws BusinessException Business Exception
     */
    public String createFolder(String appId, String cabinetuuid,
        String folderuid, String foldername, String statistics)
        throws BusinessException;

    /**
     * 文件夹改名
     * 
     * @param appid 应用guid
     * @param cabinetuuid 仓库uuid
     * @param folderuid 文件夹uuid
     * @param newfoldername 新文件夹名称
     * @throws BusinessException Business Exception
     */
    public void renameFolder(String appid, String cabinetuuid,
        String folderuid, String newfoldername)
        throws BusinessException;

    /**
     * 删除文件夹
     * 
     * @param appId 应用guid
     * @param folderuid 文件夹uuid
     * @param flag 删除标示 1：逻辑删除 2：物理删除
     * @throws BusinessException Business Exception
     */
    public void removeFolder(String appId, String folderuid, String flag)
        throws BusinessException;

    /**
     * 批量删除文件夹
     * 
     * @param appId 应用guid
     * @param folderuids 文件夹uuid
     * @param flag 删除标示
     * @return value
     * @throws BusinessException Exception
     */
    public List<String> removeFolders(String appId, List<String> folderuids,
        String flag)
        throws BusinessException;

    /**
     * 移动文件夹
     * 
     * @param appId 应用guid
     * @param cabinetuuid cabinet uuid
     * @param folderuid 文件夹uid
     * @param targetfolderuid 目标文件夹uid
     * @throws BusinessException Business Exception
     */
    public void moveFolder(String appId, String cabinetuuid, String folderuid,
        String targetfolderuid)
        throws BusinessException;

    /**
     * 批量移动文件夹
     * 
     * @param appId 应用guid
     * @param cabinetuuid 文件夹uid
     * @param folderuids folder uids
     * @param targetfolderuid 目标文件夹uid
     * @return value
     * @throws BusinessException Business Exception
     */
    public List<String> moveFolders(String appId, String cabinetuuid,
        List<String> folderuids, String targetfolderuid)
        throws BusinessException;

    /**
     * 拷贝文件夹
     * 
     * @param appId appId
     * @param cabinetuuid cabinetuuid
     * @param folderuuid folderuuids
     * @param targetfolderuid targetfolderuid
     * @return value
     * @throws BusinessException Business Exception
     */
    public ResultBuffer copyFolder(String appId, String cabinetuuid,
        String folderuuid, String targetfolderuid)
        throws BusinessException;

    /**
     * 批量拷贝文件夹
     * 
     * @param appId appId
     * @param cabinetuuid cabinetuuid
     * @param folderuuids folderuuids
     * @param targetfolderuid targetfolderuid
     * @return value
     * @throws BusinessException Business Exception
     */
    public ResultBuffer copyFolders(String appId, String cabinetuuid,
        List<String> folderuuids, String targetfolderuid)
        throws BusinessException;

    /**
     * 搜索关键字
     * 
     * @param appId 应用id
     * @param keyWord 关键字
     * @param folderuuid 文件夹uuid
     * @param filePageInfo 分页对象
     * @param map map
     * @throws BusinessException Business Exception
     */
    public void search(String appId, String keyWord, String folderuuid,
        FilePageInfo filePageInfo, HashMap<String, String> map)
        throws BusinessException;
}
