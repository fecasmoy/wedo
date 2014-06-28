package com.wedo.businessserver.event.listener;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.slide.lock.TempFile;
import org.apache.slide.webdav.util.WebdavStatus;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.wedo.businessserver.attemper.threadpool.SearchTask;
import com.wedo.businessserver.attemper.threadpool.SecurityTask;
import com.wedo.businessserver.attemper.threadpool.ThreadPoolManager;
import com.wedo.businessserver.cache.SupportNode;
import com.wedo.businessserver.common.util.BaseStaticContextLoader;
import com.wedo.businessserver.common.util.DateUtils;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.common.util.RandomGUID;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.domain.WFileHistory;
import com.wedo.businessserver.css3.domain.Wapp;
import com.wedo.businessserver.css3.service.WAppService;
import com.wedo.businessserver.css3.service.WFileService;
import com.wedo.businessserver.css3.service.WFolderService;
import com.wedo.businessserver.css3.service.util.DataServiceUtil;
import com.wedo.businessserver.event.MessageEvent;
import com.wedo.businessserver.searchengine.IkIndexWriter;
import com.wedo.businessserver.storage.jcr.domain.MetaData;
import com.wedo.businessserver.storage.jcr.service.JCRService;

/**
 * 在线编辑锁同步监听类
 * 
 * @author c90003207
 * 
 */
public class WebdavLockListener
    implements ApplicationListener
{

    /**
     * 记录日志的时候用到的日志工具类
     */
    private static final Log logger = LogFactory
        .getLog(WebdavLockListener.class);

    /**
     * message
     */
    private ResourceBundle msg = LanguageUtil
        .getMessage();

    /**
     * pool thread
     */
    private ThreadPoolManager threadPoolManager = ThreadPoolManager
        .getInstance();

    /**
     * 处理在线编辑锁同步事件
     * 
     * @param event event
     */
    public void onApplicationEvent(ApplicationEvent event)
    {
        if (event instanceof MessageEvent)
        {
            MessageEvent msEvent = (MessageEvent) event;
            if (msEvent
                .getMessage() instanceof TempFile)
            {
                editLock(msEvent);
            }
        }
    }

    /**
     * edit locking
     * 
     * @param msEvent event
     */
    private void editLock(MessageEvent msEvent)
    {
        TempFile tmpfileInfo = (TempFile) msEvent
            .getMessage();
        String fileUuid = tmpfileInfo
            .getFilename();
        String filepath = tmpfileInfo
            .getPath() + "/" + fileUuid;
        File tempfile = new File(filepath);
        if (!tempfile
            .exists())
        {
            // 临时文件不存在
            logger
                .error(msg
                    .getString("ERROR.00540"));
        }
        else
        {
            lockFile(fileUuid, filepath, tmpfileInfo, tempfile);
        }
        // 移除锁列表中对应对像
        WebdavStatus
            .getMapTempFiles().remove(fileUuid);
    }

    /**
     * lock files
     * 
     * @param fileUuid fileUuid
     * @param filepath filepath
     * @param tmpfileInfo tmpfileInfo
     * @param tempfile tempfile
     */
    private void lockFile(String fileUuid, String filepath,
        TempFile tmpfileInfo, File tempfile)
    {
        logger
            .info(msg
                .getString("msg.webdav.dealfile"));
        String root;
        try
        {
            root = SupportNode
                .getInstance().getHangsCarriesPath();
            Map<String, String> fileMap = new HashMap<String, String>();
            fileMap
                .put("avaible", "0");
            fileMap
                .put("fileuuid", fileUuid);
            // 获得最新文件
            WFile wFile = getWFileService()
                .getWFile(fileMap);
            Calendar calendar = Calendar
                .getInstance();
            String time = DateUtils
                .format(calendar, "yyyy/MM/dd");
            MetaData metaData = getJCRService()
                .getMetaData(tmpfileInfo
                    .getFilename(), wFile
                    .getVersion());
            metaData
                .setCheckin(true);
            metaData
                .setCreateDate(DateUtils
                    .format(calendar, "yyyy-MM-dd HH:mm:ss"));
            metaData
                .setSize(tempfile
                    .length());
            this
                .getJCRService().updateMetaData(metaData);
            File folderFile = new File(root + "/repository/date/" + time);
            if (!folderFile
                .exists())
            {
                Boolean flag = folderFile
                    .mkdirs();
                if (!flag)
                {
                    logger
                        .error("Create folder failed in webdav. folder is : "
                            + folderFile
                                .getPath());
                }
            }
            String realName = metaData
                .getUuid() + "-" + metaData
                .getVersion();
            String realPath =
                root + "/repository/date/" + time + "/" + realName;
            File realFile = new File(realPath);
            Boolean reflag = tempfile
                .renameTo(realFile);
            if (!reflag)
            {
                logger
                    .error("Failed Rename the webdav tmpfile. tmpfile is: "
                        + tempfile
                            .getPath());
            }

            WFileHistory fileHistory = new WFileHistory();
            BeanUtils
                .copyProperties(fileHistory, wFile);
            fileHistory
                .setId(null);
            RandomGUID randomGUID = new RandomGUID();
            fileHistory
                .setGuid(randomGUID
                    .getValueAfterMD5());
            getWFileService()
                .saveWFileHistory(fileHistory);
            wFile
                .setVersion(metaData
                    .getVersion());
            wFile
                .setFileSize(realFile
                    .length());
            wFile
                .setFpath("/repository/date/" + time + "/" + realName);
            wFile
                .setMd5(null);
            getWFileService()
                .updateWFile(wFile);
            Wapp app = getWAppService()
                .getApp(wFile
                    .getAppGuid());
            String domainName = app
                .getDomainName();
            String ip = domainName
                .substring(0, domainName
                    .indexOf("/"));
//            WOffice woffice = new WOffice();
//            // 通知客户端回调方法执行业务逻辑
//            woffice
//                .office(ip, tmpfileInfo
//                    .getFilename(), wFile
//                    .getVersion(), String
//                    .valueOf(realFile
//                        .length()));
            File lockfile =
                new File(WebdavStatus.LOCKFILE_ROOT + "/" + fileUuid);
            if (lockfile
                .exists())
            {
                Boolean flag = lockfile
                    .delete();
                if (!flag)
                {
                    logger
                        .error("Delete lockfile failed in Webdav. file is:  "
                            + lockfile
                                .getPath());
                }
            }
            else
            {
                // 在线编辑件锁不存在
                logger
                    .error(msg
                        .getString("ERROR.00575") + "  fileuuid: " + fileUuid);
            }
            if (DataServiceUtil
                .getAuthorization(wFile
                    .getAppGuid()).getFunction().getSearchEngine()
                .getAvailable())
            {
                HashMap<String, String> folderMap =
                    new HashMap<String, String>();
                folderMap
                    .put("guid", wFile
                        .getFolderguid());
                wFile
                    .setFoldertreepath(getWFolderService()
                        .getFolder(folderMap).getTreepath());
                fileHistory
                    .setFoldertreepath(getWFolderService()
                        .getFolder(folderMap).getTreepath());
                // 全文检索
                wFile
                    .setWFileHistory(fileHistory);
                threadPoolManager
                    .addTask(new SearchTask(
                        new IkIndexWriter(),
                        wFile,
                        com.wedo.businessserver.searchengine.Constants.INCREMENT_FLAG));
            }
            // 需要杀毒
            if (DataServiceUtil
                .getAuthorization(wFile
                    .getAppGuid()).getFunction().getScanVirus().getAvailable())
            {
                threadPoolManager
                    .addTask(new SecurityTask(realPath));
            }
        }
        catch (Exception e)
        {
            // 临时文件生成版本错误
            logger
                .error(msg
                    .getString("ERROR.00541"), e);
        }
    }

    /**
     * getter
     * 
     * @return value
     */
    public WFileService getWFileService()
    {
        return (WFileService) BaseStaticContextLoader
            .getApplicationContext().getBean("wFileService");
    }

    /**
     * getter
     * 
     * @return value
     */
    public JCRService getJCRService()
    {
        return (JCRService) BaseStaticContextLoader
            .getApplicationContext().getBean("jCRService");
    }

    /**
     * getter
     * 
     * @return value
     */
    public WFolderService getWFolderService()
    {
        return (WFolderService) BaseStaticContextLoader
            .getApplicationContext().getBean("wFolderService");
    }

    /**
     * getter
     * 
     * @return value
     */
    public WAppService getWAppService()
    {
        return (WAppService) BaseStaticContextLoader
            .getApplicationContext().getBean("wAppService");
    }
}
