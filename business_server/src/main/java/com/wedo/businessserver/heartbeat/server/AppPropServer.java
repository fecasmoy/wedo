package com.wedo.businessserver.heartbeat.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.ws.model.WappProperties;
import com.wedo.businessserver.storage.jcr.Constants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 应用配置更新组播接收
 * 
 * @author l00100468
 */
public class AppPropServer
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
     * 接收应用配置xml串,并将取转为POJO,更新Map关系列表
     * 
     * @author l00100468
     * @param date date
     * @throws Exception exception
     */
    public void listener(String date)
        throws Exception
    {
        logger.info(msg.getString("msg.app.mulicast"));// 应用配置更新组播处理
        try
        {
            date = date.trim();
            logger.info(msg.getString("msg.app.cruappguid") + date);
            if (StringUtils.isNotBlank(date))
            {
                XStream sm = new XStream(new DomDriver());
                date = Constants.APP_PROPERTIES_ROOT + "/" + date;
                InputStream in = new FileInputStream(date);
                WappProperties newprop = (WappProperties) sm.fromXML(in);// 修改生成的配置文件
                Constants.setAppPropMap(newprop.getAppId(), newprop);// 更新内存状态
            }
        }
        catch (Exception e)
        {
            logger.error(msg.getString("ERROR.00046") + "  port: " + getPort(),
                e);// 应用配置文件更新组播错误
            throw e;
        }
        
    }
    
}
