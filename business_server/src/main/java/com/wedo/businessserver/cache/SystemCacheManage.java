package com.wedo.businessserver.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.wedo.businessserver.loadbalance.MonitorInfoBean;
import com.wedo.businessserver.storage.jcr.Constants;

/**
 * 分布式系统获得各节点cpu，内存的缓存
 * 
 * @author c90003207
 * 
 */
public class SystemCacheManage extends CacheManageFactory {
	// 缓存管理器
	private static CacheManager manager = null;

	// 默认的缓存名称
	private static final String CACHENAME = "systemstate";

	// CPU 运行峰值
	private static final double CPU_HIGHEST_RATE = 99.0;

	/**
	 * 初始化缓存
	 * 
	 * @return return value
	 * @throws Exception
	 *             Exception
	 */
	public static synchronized CacheManager getCacheManager() throws Exception {
		if (manager == null) {
			String ehcache = SystemCacheManage.class.getClassLoader()
					.getResource("").toURI().getPath()
					+ "ehcache.xml";// 加载缓存配置文件
			manager = new CacheManager(ehcache);
		}
		return manager;
	}

	/**
	 * 放入缓存
	 * 
	 * @param obj
	 *            obj
	 * @throws Exception
	 *             Exception
	 */
	public void putCache(Object obj) throws Exception {
		try {
			Cache cache = getCacheManager().getCache(CACHENAME); // 获取配置文件缓存名称
			Element element = null;
			MonitorInfoBean monitorInfoBean = (MonitorInfoBean) obj;
			element = new Element(monitorInfoBean.getLocalIp(), monitorInfoBean);
			if (element != null) {
				// 把对象放入缓存
				cache.put(element);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 读取缓存
	 * 
	 * @return return value
	 * @throws Exception
	 *             Exception
	 */
	public List<MonitorInfoBean> getCaches() throws Exception {
		List<MonitorInfoBean> list = new ArrayList<MonitorInfoBean>();
		try {
			Cache cache = getCacheManager().getCache(CACHENAME);// 获取配置文件缓存名称
			List<?> listcache = cache.getKeys();
			for (int i = 0; i < listcache.size(); i++) { // 循环读取缓存，如果是本计算组的则加入列表
				Object obj = cache.get(listcache.get(i)).getObjectValue();
				if (obj != null && obj instanceof MonitorInfoBean) {
					list.add((MonitorInfoBean) cache.get(listcache.get(i))
							.getObjectValue());
				}
			}
			sortCollection(list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list.size() == 0) {
			MonitorInfoBean monitorInfoBean = new MonitorInfoBean();
			monitorInfoBean.setLocalIp(Constants.LOCALIP);
			monitorInfoBean.setCpuRatio(Double.valueOf(CPU_HIGHEST_RATE));
			monitorInfoBean.setMemRatio(Double.valueOf(CPU_HIGHEST_RATE));
			list.add(monitorInfoBean);
		}
		return list;
	}

	private void sortCollection(List<MonitorInfoBean> list) throws Exception {
		// 集合排序
		Collections.sort(list, new Comparator<MonitorInfoBean>() {
			public int compare(MonitorInfoBean arg0, MonitorInfoBean arg1) {
				if (arg0.getCpuRatio() > arg1.getCpuRatio())// cpu是异地排序要素
				{
					return 0;
				}
				if (Double.compare(arg0.getCpuRatio(), arg1.getCpuRatio()) == 0) {
					if (arg0.getMemRatio() >= arg1.getMemRatio())// 内存第二排序要素
					{
						return 0;
					} else {
						return 1;
					}
				}
				return 1;
			}
		});
	}
}
