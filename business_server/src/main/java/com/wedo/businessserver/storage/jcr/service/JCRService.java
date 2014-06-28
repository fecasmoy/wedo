package com.wedo.businessserver.storage.jcr.service;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.storage.jcr.domain.MetaData;

/**
 * 元数据操作接口类
 * 
 * @author c90003207
 * 
 */
public interface JCRService
{
    
    /**
     * 创建一个应用仓库
     * 
     * @param appGuid 应用的唯一标识
     * @return 应用的UUID
     * @throws BusinessException Exception
     */
    public String createAppRep(String appGuid)
        throws BusinessException;
    
    /**
     * 新建文件
     * 
     * @param metaData 文件基本信息（不包含文件系统信息）
     * @return 文件信息
     * @throws BusinessException Exception
     */
    public MetaData createMetaData(MetaData metaData)
        throws BusinessException;
    
    /**
     * 更新文件
     * 
     * @param metaData 文件基本信息（不包含文件系统信息）
     * @return 文件信息
     * @throws BusinessException Exception
     */
    public MetaData updateMetaData(MetaData metaData)
        throws BusinessException;
    
    /**
     * 取得文件
     * 
     * @param fileUuid 文件UUID
     * @param version 指定回退的最新版本
     * @return 文件信息
     * @throws BusinessException Exception
     */
    public MetaData getMetaData(String fileUuid, String version)
        throws BusinessException;
    
    /**
     * 特殊情况下版本回退时，更新源数据最高版本信息
     * 
     * @param fileUuid 文件基本信息
     * @param version 指定回退的最新版本
     * 
     *            //return 文件信息
     * @throws BusinessException Exception
     */
    public void updateMetaData(String fileUuid, String version)
        throws BusinessException;
    
    /**
     * 删除源数据
     * 
     * @param metaData 文件基本信息
     * @throws BusinessException Exception
     */
    public void delMetaData(MetaData metaData)
        throws BusinessException;
    
}
