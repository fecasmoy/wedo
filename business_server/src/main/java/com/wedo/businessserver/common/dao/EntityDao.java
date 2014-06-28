package com.wedo.businessserver.common.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.dao.DataAccessException;

import com.wedo.businessserver.common.dao.support.PageInfo;

/**
 * 针对单个Entity对象的操作,不依赖于具体ORM实现方案.
 * 
 * @param <T> type
 * @author calvin
 */
public interface EntityDao<T>
{
    /**
     * setter
     * 
     * @param t type
     */
    void setEntityClass(Class<T> t);
    
    /**
     * 主键获得ID
     * 
     * @param id id
     * @return t
     * @throws DataAccessException 数据库访问异常
     */
    T get(Serializable id)
        throws DataAccessException;
    
    /**
     * 不带条件获得所有对象
     * 
     * @return t
     * @throws DataAccessException 数据库访问异常
     */
    List<T> getAll()
        throws DataAccessException;
    
    /**
     * 保存对象
     * 
     * @param o o
     * @throws DataAccessException 数据库访问异常
     */
    void save(Object o)
        throws DataAccessException;
    
    /**
     * 更新对象
     * 
     * @param o o
     * @throws DataAccessException 数据库访问异常
     */
    void update(Object o)
        throws DataAccessException;
    
    /**
     * 删除对象，根据主键
     * 
     * @param id id
     * @throws DataAccessException 数据库访问异常
     */
    void removeById(Serializable id)
        throws DataAccessException;
    
    /**
     * 删除对象，根据对象
     * 
     * @param o o
     * @throws DataAccessException 数据库访问异常
     */
    void remove(Object o)
        throws DataAccessException;
    
    /**
     * hql删除
     * 
     * @param hql hql
     * @throws DataAccessException 数据库访问异常
     */
    void deleteByHql(String hql)
        throws DataAccessException;
    
    /**
     * 帶条件hql查询
     * 
     * @param hsql hql
     * @param values values
     * @return list
     * @throws DataAccessException 数据库访问异常
     */
    List<T> find(String hsql, Object... values)
        throws DataAccessException;
    
    /**
     * QBC查询
     * 
     * @param criteria criteria
     * @return list
     * @throws DataAccessException 数据库访问异常
     */
    List<T> findByQbc(Criteria criteria)
        throws DataAccessException;
    
    /**
     * 主键获得ID 带缓存
     * 
     * @param id id
     * @return T
     * @throws DataAccessException 数据库访问异常
     */
    T getForCache(Serializable id)
        throws DataAccessException;
    
    /**
     * 不带条件获得所有对象 带缓存
     * 
     * @return list
     * @throws DataAccessException 数据库访问异常
     */
    List<T> getAllForCache()
        throws DataAccessException;
    
    /**
     * 帶条件hql查询 带缓存
     * 
     * @param hsql hql
     * @param values values
     * @return list
     * @throws DataAccessException 数据库访问异常
     */
    List<T> findForCache(String hsql, Object... values)
        throws DataAccessException;
    
    /**
     * QBC查询 带缓存
     * 
     * @param criteria criteria
     * @return list
     * @throws DataAccessException 数据库访问异常
     */
    List<T> findByQbcForCache(Criteria criteria)
        throws DataAccessException;
    
    /**
     * HQL分页查询 带缓存
     * 
     * @param hql hql
     * @param pageInfo pageInfo
     * @param args args
     * @return values
     * @throws DataAccessException 数据库访问异常
     */
    PageInfo<T> pagedQueryForCache(String hql, PageInfo<T> pageInfo,
        Object... args)
        throws DataAccessException;
    
    /**
     * HQL分页查询
     * 
     * @param pageInfo pageInfo
     * @param hql hql
     * @param args args
     * @return values
     * @throws DataAccessException 数据库访问异常
     */
    PageInfo<T> pagedQuery(PageInfo<T> pageInfo, String hql, Object... args)
        throws DataAccessException;
    
    /**
     * QBC分页查询 带缓存
     * 
     * @param pageInfo pageInfo
     * @param criteria criteria
     * @return values
     * @throws DataAccessException 数据库访问异常
     */
    PageInfo<T> pagedQueryByQbcForCache(PageInfo<T> pageInfo, Criteria criteria)
        throws DataAccessException;
    
    /**
     * QBC分页查询
     * 
     * @param pageInfo pageInfo
     * @param criteria criteria
     * @return T
     * @throws DataAccessException 数据库访问异常
     */
    PageInfo<T> pagedQueryByQbc(PageInfo<T> pageInfo, Criteria criteria)
        throws DataAccessException;
    
    /**
     * 带条件查询单个对象(hql方式)
     * 
     * @param hql hql
     * @param values values
     * @return T
     * @throws DataAccessException 数据库访问异常
     */
    T getByhql(String hql, Object... values)
        throws DataAccessException;
    
    /**
     * 带条件查询单个对象缓存(hql方式)
     * 
     * @param hql hql
     * @param values values
     * @return T
     * @throws DataAccessException 数据库访问异常
     */
    T getByhqlForCache(String hql, Object... values)
        throws DataAccessException;
    
    /**
     * 带条件查询单个对象缓存(qbc方式)
     * 
     * @param criteria criteria
     * @return T
     * @throws DataAccessException 数据库访问异常
     */
    T getByQbc(Criteria criteria)
        throws DataAccessException;
    
    /**
     * 带条件查询单个对象(qbc方式)
     * 
     * @param criteria criteria
     * @return T
     * @throws DataAccessException 数据库访问异常
     */
    T getByQbcForCache(Criteria criteria)
        throws DataAccessException;
    
    /**
     * 获取Criteria对象
     * 
     * @return Criteria
     * @throws DataAccessException 数据库访问异常
     */
    Criteria getEntityCriteria()
        throws DataAccessException;
    
    /**
     * 原生sql查询
     * 
     * @param sql sql
     * @return list
     * @throws DataAccessException 数据库访问异常
     */
    List<T> getByNativeSql(String sql)
        throws DataAccessException;
    
}
