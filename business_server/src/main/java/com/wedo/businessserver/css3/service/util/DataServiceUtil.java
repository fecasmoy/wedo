package com.wedo.businessserver.css3.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.exception.GeneralException;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.ws.model.WappProperties;
import com.wedo.businessserver.storage.jcr.Constants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * data service utils
 * 
 * @author c90003207
 */
public class DataServiceUtil
{
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory.getLog(DataServiceUtil.class);
    
    /**
     * message
     */
    private static ResourceBundle msg = LanguageUtil.getMessage();
    
    /**
     * 应用数据服务权限判断
     * 
     * @author l00100468
     * @param appguid 应用guid
     * @return {@link WappProperties}
     * @throws GeneralException General Exception
     */
    public static WappProperties getAuthorization(String appguid)
        throws GeneralException
    {
        logger.info(msg.getString("msg.app.properties") + "  appguid: "
            + appguid);// 获取应用数据服务权限
        try
        {
            if (StringUtils.isNotBlank(appguid))
            {
                appguid = appguid.trim();
                WappProperties prop = Constants.getAppPropMap().get(appguid);
                if (prop == null)
                { // 应用配置文件未取出，则将其取出并置入map列表
                    XStream sm = new XStream(new DomDriver());
                    InputStream in =
                        new FileInputStream(Constants.APP_PROPERTIES_ROOT + "/"
                            + appguid);
                    prop = (WappProperties) sm.fromXML(in);
                    Constants.setAppPropMap(appguid, prop);
                }
                return prop;
            }
            else
            {
                throw new GeneralException("ERROR.00596");// 应用ID为空错误
            }
        }
        catch (FileNotFoundException e)
        {
            logger.error(msg.getString("ERROR.00325"), e);// 应用配置文件不存在
            throw new GeneralException("ERROR.00325", e);
        }
        catch (Exception e)
        {
            logger.error(msg.getString("ERROR.00326"), e);// 获取应用服务权限错误
            throw new GeneralException("ERROR.00326", e);
        }
    }
    
    /**
     * default property
     * 
     * @param appguid appguid
     * @throws FileNotFoundException File Not Found Exception
     */
    public static void defaultProperties(String appguid)
        throws FileNotFoundException
    {
        WappProperties prop = new WappProperties();
        prop.setAppId(appguid);
        prop.getFunction().getOnlineEdit().setAvailable(false);
        prop.getFunction().getSearchEngine().setAvailable(false);
        prop.getFunction().getSearchEngine().setNeedSummary("false");
        prop.getFunction().getScanVirus().setAvailable(false);
        XStream sm = new XStream();
        File folder = new File(Constants.APP_PROPERTIES_ROOT);
        if (!folder.isDirectory())
        {
            Boolean flag = folder.mkdirs();
            if (!flag)
            {
                logger
                    .error("Create APP properties faile parentfolder Failed! parentfolder is: "
                        + folder.getPath());
            }
        }
        OutputStream out = new FileOutputStream(folder + "/" + appguid);
        sm.toXML(prop, out);
        
    }
}
