package com.wedo.businessserver.attemper.threadpool;

import com.wedo.businessserver.common.util.ConfigurableConstants;

/**
 * 搜索引擎产量设置
 * 
 * @author c90003207
 * 
 */
public class Constants
    extends ConfigurableConstants
{
    
    // 静态初始化读入portal.properties中的设置
    static
    {
        init("config.properties");
    }
    
    /** 杀毒服务器位置 */
    public static final String SECURITYIP = getProperty("securityIp", "");
    
}
