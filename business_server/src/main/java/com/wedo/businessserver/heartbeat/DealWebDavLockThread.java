package com.wedo.businessserver.heartbeat;

import java.util.Date;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.slide.lock.TempFile;
import org.apache.slide.webdav.util.WebdavStatus;

import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.event.Publisher;

/**
 * 在线编辑锁临时文件轮巡处理
 * 
 */
public class DealWebDavLockThread
    extends Thread
{
    
    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger =
        LogFactory.getLog(DealWebDavLockThread.class);
    
    /**
     * sleep time 6000
     */
    private static final int SLEEP_TIME_6000 = 6000;
    
    /**
     * message
     */
    private ResourceBundle msg = LanguageUtil.getMessage();
    
    /**
     * RUN METHOD
     */
    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                lisenter();
                sleep(SLEEP_TIME_6000);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    /**
     * 轮巡处理临时文件方法
     * 
     */
    private void lisenter()
    {
        try
        {
            
            Enumeration<String> enumfiles =
                WebdavStatus.getMapTempFiles().keys();
            logger.debug(msg.getString("msg.webdav.start")
                + WebdavStatus.getMapTempFiles().size());
            
            // 对map关系表进行轮巡判断
            while (enumfiles.hasMoreElements())
            {
                String fileUuid = enumfiles.nextElement();
                TempFile tmpfileInfo =
                    (TempFile) WebdavStatus.getMapTempFiles().get(fileUuid);
                
                if (tmpfileInfo.getExpirationDate().before(new Date()))
                {
                    logger.info(msg.getString("msg.file")
                        + tmpfileInfo.getFilename()
                        + msg.getString("msg.webdav.expire"));// 锁过期处理
                    Publisher pub =
                        (Publisher) BaseStaticContextLoader
                            .getApplicationContext().getBean("publisher");
                    pub.publish(tmpfileInfo);
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            // TODO
        }
    }
}
