package com.wedo.businessserver.heartbeat.server;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.domain.WSupportNode;

/**
 * CSP组播接收处理
 * 
 * @author c90003207
 * 
 */
public class CspServer
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
     * 接收收组播，完成CSP的节点更新挂载事件
     * 
     * @param date 组播接收数据
     * @throws Exception Exception
     */
    public void listener(String date)
        throws Exception
    {
        logger.info(msg.getString("msg.csp.mulicast"));// csp更新组播，接收处理
        try
        {
            WSupportNode wSupportNode = new WSupportNode();
            String[] tmp = date.split("\\|");
            wSupportNode.setHangsCarriesPath(tmp[0]);
            wSupportNode.setCsnguid(tmp[1]);
            getPublisher().publish(wSupportNode);// 发布事件
        }
        catch (Exception e)
        {
            logger.error(msg.getString("ERROR.00045") + "  port: " + getPort(),
                e);// CSP组播，CSP节点更新败
        }
    }
}
