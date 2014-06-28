package com.wedo.businessserver.css3.service;

import com.wedo.businessserver.common.util.ConfigurableConstants;

/**
 * 文件上传下载常量
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
    
    /**
     * DOWNLOAD BUFFER SIZE
     */
    public static final String DOWNLOAD_BUFFER_SIZE =
        getProperty("file.downbuffer.maxsize", "1048576");
    
    /**
     * UPLOAD BUFFER SIZE
     */
    public static final String UPLOAD_BUFFER_SIZE =
        getProperty("file.uploadbuffer.maxsize", "1572864");
}
