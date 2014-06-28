package com.wedo.businessserver.css3.ws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.cache.CacheManageFactory;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.service.WSupportNodeService;
import com.wedo.businessserver.css3.ws.model.Result;
import com.wedo.businessserver.css3.ws.model.SupportNode;
import com.wedo.businessserver.css3.ws.model.SystemState;
import com.wedo.businessserver.loadbalance.MonitorInfoBean;

/**
 * 计算节点对外webservice，cossp使用
 * 
 * @author c90003207
 * 
 */
@WebService
public class CspService
{
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(CspService.class);
    
    /**
     * 本地化处理
     */
    private ResourceBundle msg = LanguageUtil.getMessage();
    
    /**
     * WSupportNodeService
     */
    private WSupportNodeService wSupportNodeService =
        (WSupportNodeService) BaseStaticContextLoader.getApplicationContext()
            .getBean("wSupportNodeService");
    
    /**
     * 批量创建计算节点并挂载存储资源
     * 
     * @param listSupportNode listSupportNode
     * @return return value
     */
    public Result batchCreateCsp(List<SupportNode> listSupportNode)
    {
        Result result = new Result();
        try
        {
            logger.debug(msg.getObject("msg.csp.create"));
            wSupportNodeService.batchCreateCsp(listSupportNode);// 批量创建计算节点
            wSupportNodeService.updateCspSym(listSupportNode);// 建立软件连
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00220"), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    /**
     * 单个更新计算节点的IP
     * 
     * @param oldIp oldIp
     * @param newIp newIp
     * @return return value
     */
    public Result updateIp(String oldIp, String newIp)
    {
        Result result = new Result();
        try
        {
            logger.debug(msg.getString("msg.csp.update"));
            wSupportNodeService.updateCsp(oldIp, newIp);// 更新节点ip
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00221"), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    /**
     * 批量更新计算节点的状态
     * 
     * @param listSupportNode listSupportNode
     * @return return value
     */
    public Result batchUpdateCsp(List<SupportNode> listSupportNode)
    {
        Result result = new Result();
        try
        {
            logger.debug(msg.getString("msg.csp.update_2"));
            wSupportNodeService.batchUpdateCsp(listSupportNode);// 批量更新状态
            wSupportNodeService.updateCspSym(listSupportNode);// 批量更新软连接
            result.setResult(true);
            result.setMessage(Constants.SUCCESS);
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00221"), e);
            result.setResult(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    /**
     * 返回各个可用节点的状态
     * 
     * @param ips ips
     * @return return value
     */
    @SuppressWarnings("unchecked")
    public List<SystemState> listSystemState(List<String> ips)
    {
        try
        {
            logger.debug(msg.getString("msg.csp.status"));
            List<SystemState> list = new ArrayList<SystemState>();
            List<MonitorInfoBean> listMonitorInfoBean =
                (List<MonitorInfoBean>) CacheManageFactory.getInstance(
                    "com.wedo.businessserver.cache.SystemCacheManage").getCaches();// 从缓存中获得各个节点状态
            for (int i = 0; i < listMonitorInfoBean.size(); i++)
            {
                if (ips.contains(listMonitorInfoBean.get(i).getLocalIp()))
                {
                    SystemState systemState = new SystemState();
                    systemState.setIp(listMonitorInfoBean.get(i).getLocalIp());
                    systemState.setCpuRatio(listMonitorInfoBean.get(i)
                        .getCpuRatio());
                    systemState.setMemRatio(listMonitorInfoBean.get(i)
                        .getMemRatio());
                    list.add(systemState);// 增加节点列表准备返回
                }
            }
            return list;
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00223"), e);
            return null;
        }
    }
    
    /**
     * 判断是否存活
     */
    public void livability()
    {
        logger.debug(com.wedo.businessserver.storage.jcr.Constants.LOCALIP + "存活");
    }
    
}
