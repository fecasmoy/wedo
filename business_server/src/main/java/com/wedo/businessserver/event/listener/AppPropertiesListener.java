package com.wedo.businessserver.event.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.ws.model.WappProperties;
import com.wedo.businessserver.event.MessageEvent;
import com.wedo.businessserver.storage.jcr.Constants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 应用配置文件变化的监听类
 * 
 * @author c90003207
 * 
 */
public class AppPropertiesListener
    implements ApplicationListener
{
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger =
        LogFactory.getLog(AppPropertiesListener.class);
    
    /**
     * i18n
     */
    private static final ResourceBundle MSG_I18N = LanguageUtil.getMessage();
    
    /**
     * 处理应用配置文件变化实现
     * 
     * @param event event
     */
    public void onApplicationEvent(ApplicationEvent event)
    {
        if (event instanceof MessageEvent)
        {
            MessageEvent msEvent = (MessageEvent) event;
            if (msEvent.getMessage() instanceof WappProperties)
            {
                appProperties(msEvent);
            }
        }
    }
    
    private void appProperties(MessageEvent msEvent)
    {
        InputStream in = null;
        try
        {
            WappProperties newprop = (WappProperties) msEvent.getMessage();
            XStream sm = new XStream(new DomDriver());
            in =
                new FileInputStream(new File(Constants.APP_PROPERTIES_ROOT
                    + "/" + newprop.getAppId()));
            newprop = (WappProperties) sm.fromXML(in);
            Constants.setAppPropMap(newprop.getAppId(), newprop);
            in.close();
        }
        catch (Exception e)
        {
            logger
                .info(MSG_I18N
                    .getString("ERROR.00327"), e);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
