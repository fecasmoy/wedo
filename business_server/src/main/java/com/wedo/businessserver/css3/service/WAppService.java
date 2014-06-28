package com.wedo.businessserver.css3.service;

import java.util.HashMap;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.css3.domain.Wapp;
import com.wedo.businessserver.css3.ws.model.App;

/**
 * 应用处理接口
 * 
 * @author c90003207
 * 
 */
public interface WAppService
{
    /**
     * 获得应用
     * 
     * @param appId appId
     * @return value
     * @throws BusinessException Business Exception
     */
    public Wapp getApp(String appId)
        throws BusinessException;
    
    /**
     * 创建应用
     * 
     * @param app 应用guid
     * @return value
     * @throws BusinessException Business Exception
     */
    public App createApp(App app)
        throws BusinessException;
    
    /**
     * 更新应用
     * 
     * @param app 应用对象
     * @throws BusinessException Business Exception
     */
    public void updateApp(App app)
        throws BusinessException;
    
    /**
     * 创建仓库
     * 
     * @param appguid 应用guid
     * @param name 应用名称
     * @return value
     * @throws BusinessException Business Exception
     */
    public String createRepository(String appguid, String name)
        throws BusinessException;
    
    /**
     * 得到应用使用空间
     * 
     * @param appguid 应用guid
     * @return value
     * @throws BusinessException Business Exception
     */
    public HashMap<String, String> getAppSpace(String appguid)
        throws BusinessException;
}
