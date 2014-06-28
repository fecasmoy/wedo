package com.wedo.businessserver.heartbeat;

/**
 * 分布式环境下,相关常量定方式
 * 
 */
public class Constants
{
    
    /** 系统信息组播端口 */
    public static final int SYSTEM_INFO = 12345;
    
    /** CSP写入路径更新组播端口 */
    public static final int CSP_UPDATE = 12346;
    
    /** 删除文件索引组播端口 */
    public static final int INDEX_DEL = 12347;
    
    /** 重置索引文件组播端口 */
    public static final int INDEX_SEARCHER_INITAL = 12348;
    
    /** 应用配置更新组播端口 */
    public static final int APP_PROPERTIES = 12349;
    
}
