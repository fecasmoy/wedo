package com.wedo.businessserver.common.dao.support;

import com.wedo.businessserver.common.dao.EntityDao;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;

/**
 * dao获取工具类
 * 
 * @author c90003207
 * 
 */
public class DAOUtil
{
    
    /**
     * 获取操作的dao，泛型为制定的pojo
     * 
     * @param <T> <T>
     * @param t t
     * @return {@link EntityDao}
     */
    @SuppressWarnings("unchecked")
    public static <T> EntityDao<T> getEntityDao(Class<T> t)
    {
        // 从容器中获取EntityDao实例
        EntityDao entityDao =
            (EntityDao) BaseStaticContextLoader.getApplicationContext()
                .getBean("entityDao");
        entityDao.setEntityClass(t);
        return entityDao;
    }
    
}
