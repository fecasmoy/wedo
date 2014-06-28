package com.wedo.businessserver.heartbeat.server;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.cache.CacheManageFactory;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.loadbalance.MonitorInfoBean;

/**
 * 系统信息组播，输出获取的信息
 * 
 * @author c90003207
 * 
 */
public class SystemServer
    extends ServerFactory
{
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(ServerFactory.class);
    
    /**
     * 国际化处理
     */
    private static ResourceBundle msg = LanguageUtil.getMessage();
    
    /**
     * length 0
     */
    private static final int LENGTH_0 = 0;
    
    /**
     * length 1
     */
    private static final int LENGTH_1 = 1;
    
    /**
     * length 2
     */
    private static final int LENGTH_2 = 2;
    
    /**
     * length 4
     */
    private static final int LENGTH_4 = 4;
    
    /**
     * length 1024
     */
    private static final int LENGTH_1024 = 1024;
    
    /**
     * 监听主方法,获取cpu，内存等信息
     * 
     * @param date date
     * @throws Exception exception
     */
    public void listener(String date)
        throws Exception
    {
        logger.debug(msg.getString("msg.system.listener"));// cpu开始启动组播监听器;
        try
        {
            date = date.trim();
            if (StringUtils.isNotBlank(date))
            {
                systemState(date);
            }
        }
        catch (Exception ex)
        {
            logger.error(msg.getString("ERROR.00042") + getPort(), ex);// 系统信息组播错误.
            throw ex;
        }
    }
    
    /**
     * 处理系统状态
     * 
     * @param date date
     * @throws Exception exception
     */
    private void systemState(String date)
        throws Exception
    {
        MonitorInfoBean monitorInfoBean = new MonitorInfoBean();
        String[] temp = date.split(";");
        for (int i = 0; i < temp.length; i++)
        {
            String s = temp[i];
            String[] s1 = s.split("=");
            if (i == LENGTH_0)
            {
                logger.debug("IP :" + s1[1].trim());
                monitorInfoBean.setLocalIp(s1[1].trim());
            }
            if (i == LENGTH_1)
            {
                logger.debug("CPU :" + s1[1].trim());
                monitorInfoBean.setCpuRatio(new Double(s1[1].trim()));
            }
            if (i == LENGTH_2)
            {
                logger.debug(msg.getString("msg.system.mem") + s1[1].trim());
                if (s1[1].startsWith("0"))
                {
                    monitorInfoBean.setMemRatio(new Double("0"));
                }
                else
                {
                    monitorInfoBean.setMemRatio(new Double(s1[1].substring(0,
                        LENGTH_4))
                        * LENGTH_1024);
                }
            }
        }
        CacheManageFactory.getInstance(
            "com.wedo.businessserver.cache.SystemCacheManage").putCache(
                monitorInfoBean);
    }
}
