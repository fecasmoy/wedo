package com.wedo.businessserver.cache;

import java.util.List;

/**
 * 抽象缓存工厂类
 * 
 * @author c90003207
 * 
 */
public abstract class CacheManageFactory
{
    /**
     * 工厂构造
     * 
     * @param className 工厂实例类
     * @return return value
     * @throws Exception exception
     */
    public static CacheManageFactory getInstance(String className)
        throws Exception
    {
        CacheManageFactory cacheManageFactory = null;
        try
        {
            Class<?> c = Class.forName(className);
            cacheManageFactory = (CacheManageFactory) c.newInstance();
            return cacheManageFactory;
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    /**
     * 对象放入缓存
     * 
     * @param obj obj
     * @throws Exception Exception
     */
    public abstract void putCache(Object obj)
        throws Exception;
    
    /**
     * 取得缓存列表
     * 
     * @return return value
     * @throws Exception Exception
     */
    public abstract List<?> getCaches()
        throws Exception;
}
