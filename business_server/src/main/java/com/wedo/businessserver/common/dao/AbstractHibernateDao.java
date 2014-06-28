package com.wedo.businessserver.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.wedo.businessserver.common.dao.support.PageInfo;
import com.wedo.businessserver.common.util.BeanUtils;

/**
 * 纯Hibernate Entity DAO基类.不带SpringSide扩展的分页函数. 通过泛型，子类无需扩展任何函数即拥有完整的CRUD操作.
 * 
 * @author calvin
 * @see HibernateDaoSupport
 * @see BaseHibernateDao
 * @param <T>
 *            type
 */
@Service("entityDao")
public class AbstractHibernateDao<T> extends HibernateDaoSupport implements
		EntityDao<T> {

	// 记录日志的时候用到的日志工具类
	private static final Log logger = LogFactory
			.getLog(AbstractHibernateDao.class);

	// Dao所管理的Entity类型.
	private Class<T> entityClass;

	/**
	 * 取得entityClass的函数. JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重新实现此函数达到相同效果。
	 * 
	 * @return 返回值
	 */
	@SuppressWarnings("unchecked")
	public Class getEntityClass() {
		return entityClass;
	}

	/**
	 * 取得entityClass的函数. JDK1.4不支持泛型的子类可以抛开Class<T> entityClass, 重新实现此函数达到相同效果。
	 * 
	 * @param t
	 *            type
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setEntityClass(Class t) {
		entityClass = t;
	}

	/**
	 * 获取所管理的对象名，如"User"
	 * 
	 * @return 返回值
	 */
	protected String getEntityName() {
		return ClassUtils.getShortName(getEntityClass());
	}

	/**
	 * 取得Entity的Criteria.
	 * 
	 * @return 返回值
	 */
	public Criteria getEntityCriteria() {
		return getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createCriteria(getEntityClass());
	}

	/**
	 * 获取对象
	 * 
	 * @param id
	 *            id
	 * @return 返回值
	 */
	@SuppressWarnings("unchecked")
	public T get(Serializable id) {
		T o = (T) getHibernateTemplate().get(getEntityClass(), id);
		if (o == null) {
			throw new ObjectRetrievalFailureException(getEntityClass(), id);
		}
		return o;
	}

	/**
	 * 获取所有的dao
	 * 
	 * @return 返回值
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return getHibernateTemplate().loadAll(getEntityClass());
	}

	/**
	 * 持久化对象到数据库
	 * 
	 * @param o
	 *            o
	 */
	public void save(Object o) {
		getHibernateTemplate().save(o);
		getHibernateTemplate().flush();
	}

	/**
	 * 更新对象到数据库
	 * 
	 * @param o
	 *            o
	 */
	public void update(Object o) {
		getHibernateTemplate().update(o);
		getHibernateTemplate().flush();
	}

	/**
	 * 删除所有对象
	 * 
	 * @param id
	 *            id
	 */
	public void removeById(Serializable id) {
		remove(get(id));
	}

	/**
	 * 删除指定对象
	 * 
	 * @param o
	 *            o
	 */
	public void remove(Object o) {
		getHibernateTemplate().delete(o);
	}

	/**
	 * 用hql的方式查找所有对象
	 * 
	 * @param hsql
	 *            hsql
	 * @param values
	 *            values
	 * @return 返回值
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(String hsql, Object... values) {
		if (values.length == 0) {
			return getHibernateTemplate().find(hsql);
		} else {
			return getHibernateTemplate().find(hsql, values);
		}
	}

	/**
	 * 获得单个记录(带缓存)
	 * 
	 * @param id
	 *            id
	 * @return 返回
	 */
	@SuppressWarnings("unchecked")
	public T getForCache(Serializable id) {
		logger.info("get the cache");
		String hql = "from " + getEntityName() + " t where t.id=" + id;
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(hql).setCacheable(true);
		T o = (T) (query.list().get(0));
		if (o == null) {
			logger.info("Object Retrieval Failure");
			throw new ObjectRetrievalFailureException(getEntityClass(), id);
		}
		return o;
	}

	/**
	 * 加载所有记录(带缓存)
	 * 
	 * @return 返回值
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAllForCache() {
		String hql = "from " + getEntityName() + " c order by c.id asc";
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(hql).setCacheable(true);
		return query.list();
	}

	/**
	 * hql 删除
	 * 
	 * @param hql
	 *            hql
	 */
	public void deleteByHql(String hql) {
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(hql);
		query.executeUpdate();
	}

	/**
	 * 条件查询(带缓存)
	 * 
	 * @param hsql
	 *            hsql
	 * @param values
	 *            values
	 * @return 数据
	 */
	@SuppressWarnings("unchecked")
	public List<T> findForCache(String hsql, Object... values) {
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(hsql).setCacheable(true);
		if (values == null || values.length == 0) {
			return query.list();
		} else {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
			return query.list();
		}
	}

	/**
	 * 条件查询QBC(带缓存)
	 * 
	 * @param criterions
	 *            criterions
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByQbcForCache(Criterion... criterions) {
		Criteria criteria = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createCriteria(getEntityClass());
		criteria.setCacheable(true);
		if (criterions != null && criterions.length != 0) {
			for (Criterion c : criterions) {
				criteria.add(c);
			}
		}
		return criteria.list();
	}

	/**
	 * 分页查询
	 * 
	 * @param hql
	 *            hql
	 * @param pageInfo
	 *            pageInfo
	 * @param args
	 *            args
	 * @return 返回值
	 */
	@SuppressWarnings("unchecked")
	public PageInfo<T> pagedQueryForCache(String hql, PageInfo<T> pageInfo,
			Object... args) {
		Assert.hasText(hql);
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(hql).setCacheable(true);
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		String countQueryString = "select count (*) "
				+ removeSelect(removeOrders(hql)); // ？？removeSelect(),removeOrders()作用？
		int totalCount = Integer.valueOf(getHibernateTemplate()
				.find(countQueryString, args).get(0).toString());
		pageInfo.setTotalCount(totalCount); // 设置最大记录数
		query.setMaxResults(pageInfo.getCountOfCurrentPage()); // （）内参数值为当前页记录数
		query.setFirstResult(pageInfo.getCountOfCurrentPage() // （）内参数值为上一页日志最后一条记录
				* (pageInfo.getCurrentPage() - 1));
		pageInfo.setPageResults(query.list()); // ？？
		return pageInfo;
	}

	/**
	 * 分页查询
	 * 
	 * @param pageInfo
	 *            pageInfo
	 * @param hql
	 *            hql
	 * @param args
	 *            args
	 * @return 返回值
	 * @throws DataAccessException
	 *             Data Access Exception
	 */
	@SuppressWarnings("unchecked")
	public PageInfo<T> pagedQuery(PageInfo<T> pageInfo, String hql,
			Object... args)
	// 定长2个参数的pageQuery()
			throws DataAccessException {
		Assert.hasText(hql);
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(hql);
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		String countQueryString = "select count (*) "
				+ removeSelect(removeOrders(hql)); // ？？removeSelect(),removeOrders()作用？
		int totalCount = Integer.valueOf(getHibernateTemplate()
				.find(countQueryString, args).get(0).toString());
		pageInfo.setTotalCount(totalCount); // 设置最大记录数
		query.setMaxResults(pageInfo.getCountOfCurrentPage()); // （）内参数值为当前页记录数
		query.setFirstResult(pageInfo.getCountOfCurrentPage() // （）内参数值为上一页日志最后一条记录
				* (pageInfo.getCurrentPage() - 1));
		pageInfo.setPageResults(query.list()); // ？？
		return pageInfo;
	}

	/**
	 * QBC分页查询
	 * 
	 * @param criteria
	 *            criteria
	 * @param pageInfo
	 *            pageInfo
	 * @return 返回值
	 * @throws DataAccessException
	 *             数据库访问异常
	 */
	@SuppressWarnings("unchecked")
	public PageInfo<T> pagedQueryByQbcForCache(PageInfo<T> pageInfo,
			Criteria criteria) throws DataAccessException {
		CriteriaImpl impl = (CriteriaImpl) criteria; // ？？一种返回值的列表容器
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) BeanUtils.forceGetProperty(impl,
					"orderEntries");
			BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		// 执行查询
		int totalCount = (Integer) criteria.setProjection(
				Projections.rowCount()).uniqueResult();
		pageInfo.setTotalCount(totalCount);
		// 将之前的Projection和OrderBy条件重新设回去
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		}

		try {
			BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}
		criteria.setCacheable(true);
		List list = criteria
				.setFirstResult(
						pageInfo.getCountOfCurrentPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getCountOfCurrentPage()).list();
		pageInfo.setPageResults(list);
		return pageInfo;
	}

	/**
	 * 使用QBC的方式进行分页查询
	 * 
	 * @param pageInfo
	 *            pageInfo
	 * @param criteria
	 *            criteria
	 * @return 返回值
	 * @throws DataAccessException
	 *             数据库访问异常
	 */
	@SuppressWarnings("unchecked")
	public PageInfo<T> pagedQueryByQbc(PageInfo<T> pageInfo, Criteria criteria)
			throws DataAccessException {
		CriteriaImpl impl = (CriteriaImpl) criteria; // ？？一种返回值的列表容器
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) BeanUtils.forceGetProperty(impl,
					"orderEntries");
			BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		// 执行查询
		int totalCount = (Integer) criteria.setProjection(
				Projections.rowCount()).uniqueResult();
		pageInfo.setTotalCount(totalCount);
		// 将之前的Projection和OrderBy条件重新设回去
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		}

		try {
			BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}
		List list = criteria
				.setFirstResult(
						pageInfo.getCountOfCurrentPage()
								* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getCountOfCurrentPage()).list();
		pageInfo.setPageResults(list);
		return pageInfo;
	}

	/**
	 * sql查询单挑记录
	 * 
	 * @param hsql
	 *            hql
	 * @param values
	 *            values
	 * @return 返回值
	 * @throws DataAccessException
	 *             数据库访问 异常
	 */
	@SuppressWarnings("unchecked")
	public T getByhql(String hsql, Object... values) throws DataAccessException {
		List list = getHibernateTemplate().find(hsql, values);
		if (!list.isEmpty() && list.size() != 0) {
			return (T) list.get(0);
		} else {
			throw new ObjectRetrievalFailureException(getEntityClass(),
					"object not found");
		}
	}

	/**
	 * sql查询单挑记录（缓存）
	 * 
	 * @param hql
	 *            hql
	 * @param values
	 *            values
	 * @return 返回值
	 * @throws DataAccessException
	 *             数据库访问异常
	 */
	@SuppressWarnings("unchecked")
	public T getByhqlForCache(String hql, Object... values)
			throws DataAccessException {
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(hql).setCacheable(true);
		if (values != null && values.length != 0) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		List list = query.list();
		if (!list.isEmpty() && list.size() != 0) {
			return (T) list.get(0);
		} else {
			throw new ObjectRetrievalFailureException(getEntityClass(),
					"object not found");
		}
	}

	/**
	 * 使用QBC的方式查询对象
	 * 
	 * @param criteria
	 *            criteria
	 * @return 返回值
	 * @throws DataAccessException
	 *             数据库访问异常
	 */
	@SuppressWarnings("unchecked")
	public T getByQbc(Criteria criteria) throws DataAccessException {
		List list = criteria.list();
		if (!list.isEmpty() && list.size() != 0) {
			return (T) list.get(0);
		} else {
			throw new ObjectRetrievalFailureException(getEntityClass(),
					"object not found");
		}
	}

	/**
	 * 获取内容后填入缓冲
	 * 
	 * @param criteria
	 *            criteria
	 * @return type
	 * @throws DataAccessException
	 *             数据库访问异常
	 */
	@SuppressWarnings("unchecked")
	public T getByQbcForCache(Criteria criteria) throws DataAccessException {
		criteria.setCacheable(true);
		List list = criteria.list();
		if (!list.isEmpty() && list.size() != 0) {
			return (T) list.get(0);
		} else {
			throw new ObjectRetrievalFailureException(getEntityClass(),
					"object not found");
		}
	}

	/**
	 * 查询总记录数
	 * 
	 * @param countString
	 *            countString
	 * @return int
	 * @throws DataAccessException
	 *             数据库访问异常
	 */
	protected int count(final String countString) throws DataAccessException {
		return Integer.valueOf(String.valueOf(getHibernateTemplate().find(
				countString).get(0)));
	}

	/**
	 * 去除select 子句，未考虑union的情况
	 * 
	 * @param hql
	 *            hql
	 * @return string
	 */
	protected static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase(Locale.getDefault()).indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql
				+ " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除orderby 子句
	 * 
	 * @param hql
	 *            hql
	 * @return string
	 */
	protected static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 去除orderby 子句
	 * 
	 * @param criteria
	 *            criteria
	 * @return 返回值
	 * @throws DataAccessException
	 *             数据库访问异常
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByQbc(Criteria criteria) throws DataAccessException {
		return criteria.list();
	}

	/**
	 * 填入到Cache中
	 * 
	 * @param criteria
	 *            criteria
	 * @return list
	 * @throws DataAccessException
	 *             数据库访问异常
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByQbcForCache(Criteria criteria)
			throws DataAccessException {
		criteria.setCacheable(true);
		return criteria.list();
	}

	/**
	 * 使用SQL进行查询
	 * 
	 * @param sql
	 *            sql
	 * @return list
	 * @throws DataAccessException
	 *             数据库访问异常
	 */
	@SuppressWarnings("unchecked")
	public List<T> getByNativeSql(String sql) throws DataAccessException {
		List<T> list = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql)
				.addEntity(getEntityClass()).list();
		return list;
	}
}
