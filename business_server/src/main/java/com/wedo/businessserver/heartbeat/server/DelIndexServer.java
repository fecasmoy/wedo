package com.wedo.businessserver.heartbeat.server;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.attemper.threadpool.SearchTask;
import com.wedo.businessserver.attemper.threadpool.ThreadPoolManager;
import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.searchengine.IkIndexWriter;

/**
 *删除索引事件组播，输出获取信息
 * 
 * Date:2009-12-28
 * 
 * @author l00100468
 */
public class DelIndexServer
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
     * length 2
     */
    private static final int LENGTH_2 = 2;
    
    /**
     *监听线程主方法：用户获取删除文件索引的文件信息，
     * 
     * @param date date
     * @throws Exception exception
     */
    public void listener(String date)
        throws Exception
    {
        logger.info(msg.getString("msg.search.del_index"));// 开始删除索引事件组播监听
        try
        {
            if (StringUtils.isNotBlank(date))
            {
                dellIndex(date);
            }
        }
        catch (Exception e)
        {
            logger.info(msg.getString("ERROR.00041") + "  port: " + getPort());// 删除文件索引组播错误
            throw e;
        }
        
    }
    
    /**
     * 删除索引
     * 
     * @param date
     * @throws Exception
     */
    private void dellIndex(String date)
        throws Exception
    {
        WFile file = new WFile();
        String[] temp = date.split(";");
        for (int i = 0; i < temp.length; i++)
        {
            String pro = temp[i];
            String[] s = pro.split("=");
            if (s[0].equals("version"))
            {
                if (s.length == LENGTH_2)
                {
                    file.setVersion(s[1]);
                }
            }
            if (s[0].equals("fileuuid"))
            {
                file.setFileuuid(s[1]);
            }
        }
        ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance();
        threadPoolManager.addTask(new SearchTask(new IkIndexWriter(), file,
            com.wedo.businessserver.searchengine.Constants.DELETE_MARK));
    }
}
