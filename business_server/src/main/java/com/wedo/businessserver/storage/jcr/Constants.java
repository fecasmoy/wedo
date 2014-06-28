package com.wedo.businessserver.storage.jcr;

import java.util.HashMap;

import com.wedo.businessserver.common.util.ConfigurableConstants;
import com.wedo.businessserver.css3.ws.model.WappProperties;

/**
 * 系统产量设置
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
    
    /** 临时文件 */
    // public static final String ROOT = getProperty("tempfile.root", "F");
    /** 指定jcr元數據路径的根 */
    public static final String JCRROOT =
        getProperty("jcr.root", "") + "/repository";
    
    /** 密钥文件位置 */
    public static final String SECURITYKEY =
        getProperty("file.securityKey", "");
    
    /** 分布式ip設置 */
    public static final String DISTRIBUTEDIP = getProperty("distributedIP", "");
    
    /** 本地ip設置 */
    public static final String LOCALIP = getProperty("localIp", "");
    
    /** 本地内网ip設置 */
    public static final String INNERIP = getProperty("innerIp", "");
    
    /** 外链根目录配置 */
    public static final String RESCROOT = getProperty("rescroot", "");
    
    /** 应用配置文件存放路径 */
    public static final String APP_PROPERTIES_ROOT = JCRROOT + "/AppProperties";
    
    /** 存放应用配置文件 */
    private static HashMap<String, WappProperties> appPropMap =
        new HashMap<String, WappProperties>();
    
    public static HashMap<String, WappProperties> getAppPropMap()
    {
        return appPropMap;
    }
    
    /**
     * 新增应用配置文件至MAP
     * 
     * @param appguid appguid
     * @param properties properties
     */
    public static void setAppPropMap(String appguid, WappProperties properties)
    {
        Constants.appPropMap.put(appguid, properties);
    }
    
}
