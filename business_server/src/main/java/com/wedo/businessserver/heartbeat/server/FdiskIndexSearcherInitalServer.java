package com.wedo.businessserver.heartbeat.server;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.searchengine.IkIndexWriter;
import com.wedo.businessserver.searchengine.SearchContext;

/**
 * Fdisk Index Searcher Inital
 * 
 * @author c90003207
 * 
 */
public class FdiskIndexSearcherInitalServer
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
     * 监听线程主方法：重置索引的文件信息，
     * 
     * @param date date
     * @throws Exception exception
     */
    public void listener(String date)
        throws Exception
    {
        logger.debug(msg.getString("msg.search.multicast"));// 重置索引组播接收处理;
        try
        {
            if (StringUtils.isNotBlank(date))
            {
                SearchContext searchContext =
                    new SearchContext(new IkIndexWriter());
                searchContext.fdiskIndexSearcherInital();
            }
        }
        catch (Exception e)
        {
            logger.error(msg.getString("ERROR.00573") + "  port: " + getPort(),
                e);// 重置索引组播错误,
            throw e;
        }
        
    }
}
