package com.wedo.businessserver.attemper.job;

import java.io.Serializable;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.cache.CacheManageFactory;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.heartbeat.ClientFactory;
import com.wedo.businessserver.loadbalance.MonitorInfoBean;
import com.wedo.businessserver.loadbalance.SystemState;

/**
 * 定时同步各个节点状态
 * 
 * @author c90003207
 * 
 */
public class SystemStateDoJobImpl
    implements Serializable
{

    // 进行序列化的时候用到的序列码
    private static final long serialVersionUID = -3242848943779600330L;

    // 记录日志的时候用到的日志工具类
    private static final Log logger = LogFactory
        .getLog(SystemStateDoJobImpl.class);

    // 本地化处理
    private static final ResourceBundle MSG_I18N = LanguageUtil
        .getMessage();

    /**
     * 系统状态同步
     * 
     * @throws BusinessException Business Exception
     */
    public void systemState()
        throws BusinessException
    {
        MonitorInfoBean monitorInfoBean = null;
        try
        {
            SystemState systemState = new SystemState();
            // 初始化
            monitorInfoBean = new MonitorInfoBean();
            // 设置本机ip
            monitorInfoBean
                .setLocalIp(com.wedo.businessserver.storage.jcr.Constants.LOCALIP);
            // 设置cpu使用率
            monitorInfoBean
                .setCpuRatio(systemState
                    .getCpuUsage());
            // 设置内存可使用
            monitorInfoBean
                .setMemRatio(systemState
                    .getMemUsage());

            if (monitorInfoBean != null)
            {
                logger
                    .debug(MSG_I18N
                        .getString("msg.system.multicast"));
                // 监控信息为空的时候，需要更新缓存状态
                CacheManageFactory
                    .getInstance("com.wedo.businessserver.cache.SystemCacheManage")
                    .putCache(monitorInfoBean);// 更新缓存状态
                // 组播发送状态要求其他节点处理
                ClientFactory
                    .run(com.wedo.businessserver.heartbeat.Constants.SYSTEM_INFO,
                        monitorInfoBean
                            .toString());
            }

        }
        catch (Exception e)
        {
            logger
                .error(MSG_I18N
                    .getString("ERROR.00040"), e);
            // ERROR.00040=获取系统信息或组播失败
            throw new BusinessException("ERROR.00040", e);
        }
    }
}
