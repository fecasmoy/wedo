package com.wedo.businessserver.searchengine;

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
    /**
     * index size
     */
    public static final Long INDEX_SIZE = Long.valueOf(1024 * 1024 * 500);
    
    /**
     * lock time
     */
    public static final Long LOCK_TIME = Long.valueOf(1000 * 60 * 2);
    
    /**
     * is increment
     */
    public static final int INCREMENT_FLAG = 1;
    
    /**
     * is delete
     */
    public static final int DELETE_MARK = 2;
    
    /**
     * office tag
     */
    public static final String OFFICE_LABEL = "OFFICE";
    
    /**
     * image tag
     */
    public static final String IMAGE_LABEL = "IMAGE";
    
    /**
     * music tag
     */
    public static final String MUSIC_LABEL = "MUSIC";
    
    /**
     * vedio tag
     */
    public static final String VEDIO_LABEL = "VEDIO";
    
    /**
     * other tag
     */
    public static final String OTHER_LABEL = "OTHER";
    
    /** 搜索引擎索引位置 */
    private static String indexRoot = getProperty("indexroot", "");
    
    /**
     * GETTER
     * 
     * @return the iNDEX_ROOT
     */
    public static String getIndexRoot()
    {
        return indexRoot;
    }
    
    /**
     * SETTER
     * 
     * @param iNDEXROOT the iNDEX_ROOT to set
     */
    public static void setIndexRoot(String iNDEXROOT)
    {
        indexRoot = iNDEXROOT;
    }
    
    // 静态初始化读入portal.properties中的设置
    static
    {
        init("config.properties");
    }
    
}
