package com.wedo.businessserver.css3.http;

import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.attemper.threadpool.ThreadPoolManager;
import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.heartbeat.ClientFactory;
import com.wedo.businessserver.heartbeat.Constants;
import com.wedo.businessserver.heartbeat.DealWebDavLockThread;
import com.wedo.businessserver.heartbeat.server.ServerFactory;
import com.wedo.businessserver.loadbalance.MonitorInfoBean;
import com.wedo.businessserver.loadbalance.SystemState;

/**
 * 系统初始化servlet,jcr 初始化和自动销毁
 * 
 * @author c90003207
 * 
 */
public class RepositoryServlet
    extends HttpServlet
{
    
    /**
     * 进行序列化的时候用到的序列码
     */
    private static final long serialVersionUID = 8729124029644747938L;
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger =
        LogFactory.getLog(RepositoryServlet.class);
    
    /**
     * i18n
     */
    private ResourceBundle msg = LanguageUtil.getMessage();
    
    /**
     * thread pool
     */
    private ThreadPoolManager threadPoolManager = null;
    
    /**
     * 容器销毁是jcr自动退出
     */
    @Override
    public void destroy()
    {
        try
        {
            // SessionUtils.outSession();
            if (threadPoolManager != null)
            {
                threadPoolManager.shutdown();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.destroy();
    }
    
    /**
     * 初始化启动，包括jcr初始化，以及组播监听启动
     * 
     * @throws ServletException Servlet Exception
     */
    @Override
    public void init()
        throws ServletException
    {
        try
        {
            ServerFactory.getInstance(
                "com.wedo.businessserver.heartbeat.server.SystemServer",
                Constants.SYSTEM_INFO).start(); // 内存，cpu监听线程启动
            // 软连接挂载监听线程启动
            ServerFactory.getInstance(
                "com.wedo.businessserver.heartbeat.server.CspServer",
                Constants.CSP_UPDATE).start();
            // 删除文件索引监听线程启动
            ServerFactory.getInstance(
                "com.wedo.businessserver.heartbeat.server.DelIndexServer",
                Constants.INDEX_DEL).start();
            // 同步搜索引擎索引
            ServerFactory
                .getInstance(
                    "com.wedo.businessserver.heartbeat.server.FdiskIndexSearcherInitalServer",
                    Constants.INDEX_SEARCHER_INITAL).start();
            // 应用配置更新
            ServerFactory.getInstance(
                "com.wedo.businessserver.heartbeat.server.AppPropServer",
                Constants.APP_PROPERTIES).start();
            DealWebDavLockThread thread = new DealWebDavLockThread();
            // 在线编辑锁超时与临时文件处理线程
            thread.start();
            MonitorInfoBean monitorInfoBean = null;
            try
            {
                SystemState systemState = new SystemState();
                monitorInfoBean = new MonitorInfoBean();
                monitorInfoBean
                    .setLocalIp(com.wedo.businessserver.storage.jcr.Constants.LOCALIP);
                monitorInfoBean.setCpuRatio(systemState.getCpuUsage());
                monitorInfoBean.setMemRatio(systemState.getMemUsage());
                // 初始化发送组播
                if (monitorInfoBean != null)
                {
                    // 初始化ip地址成功,并进行组播
                    logger.info(msg.getString("msg.init.repinit"));
                    ClientFactory.run(Constants.SYSTEM_INFO, monitorInfoBean
                        .toString());
                }
                threadPoolManager = ThreadPoolManager.getInstance();
            }
            catch (Exception e)
            {
                logger.info(msg.getString("ERROR.00040"));
                throw new BusinessException("ERROR.00040", e);
            }
            // SessionUtils.getSession();// 初始化启动jcr
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        super.init();
    }
}
