package com.wedo.businessserver.attemper.job;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.searchengine.IkIndexWriter;
import com.wedo.businessserver.searchengine.SearchContext;

/**
 * 索引合并定时处理
 * 
 * @author c90003207
 */
public class IncorporateIndexJobImpl
    implements Serializable
{
    
    //进行序列化的时候用到的序列码
    private static final long serialVersionUID = 3147011451878723546L;
    
    // 记录日志的时候用到的日志工具类
    private static final Log logger =
        LogFactory.getLog(IncorporateIndexJobImpl.class);
    
    /**
     * 合并索引
     */
    public void incorporateIndex()
    {
        try
        {
            logger.debug("Start Merge Index");
            //生成查询上下文
            SearchContext searchContext =
                new SearchContext(new IkIndexWriter());
            //开始合并索引
            searchContext.incorporateIndex();
        }
        catch (Exception e)
        {
            logger.info("Merge Index Error", e);
        }
    }
    
}
