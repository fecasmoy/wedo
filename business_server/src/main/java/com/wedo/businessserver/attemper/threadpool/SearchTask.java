package com.wedo.businessserver.attemper.threadpool;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wedo.businessserver.common.util.LanguageUtil;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.searchengine.IndexWriterFactory;

/**
 * 搜索任务实现
 * 
 * @author c90003207
 * 
 */
public class SearchTask
    implements Task
{

    // 记录日志的时候用到的日志工具类
    private static final Log logger = LogFactory
        .getLog(SearchTask.class);

    // 本地化处理
    private static final ResourceBundle MSG_I18N = LanguageUtil
        .getMessage();

    // 搜索引擎抽象类
    private IndexWriterFactory indexWriter;

    // 执行依据
    private Integer flag;

    // 执行对象
    private WFile file;

    /**
     * 构造函数
     * 
     * @param indexWriter indexWriter
     * @param file file
     * @param flag flag
     */
    public SearchTask(IndexWriterFactory indexWriter, WFile file, int flag)
    {
        this.file = file;
        this.indexWriter = indexWriter;
        this.flag = flag;
    }

    /**
     * 获得执行对象
     * 
     * @return return value
     */
    public WFile getFile()
    {
        return file;
    }

    /**
     * 设置执行对象
     * 
     * @param file file
     */
    public void setFile(WFile file)
    {
        this.file = file;
    }

    /**
     * get Index Writer
     * 
     * @return IndexWriterFactory
     */
    public IndexWriterFactory getIndexWriter()
    {
        return indexWriter;
    }

    /**
     * set Index Writer
     * 
     * @param indexWriter Index Writer
     */
    public void setIndexWriter(IndexWriterFactory indexWriter)
    {
        this.indexWriter = indexWriter;
    }

    /**
     * 执行
     */
    public void execute()
    {
        switch (flag)
        { 
            case com.wedo.businessserver.searchengine.Constants.INCREMENT_FLAG: // 增量索引
                try
                {
                    indexWriter
                        .incrementIndex(file);
                }
                catch (Exception e)
                {
                    logger
                        .info(MSG_I18N
                            .getString("msg.increment.index.error"), e);
                }
                break;
            case com.wedo.businessserver.searchengine.Constants.DELETE_MARK: // 删除索引
                try
                {
                    indexWriter
                        .deleteIndex(file);
                }
                catch (Exception e)
                {
                    logger
                        .info(MSG_I18N
                            .getString("msg.delete.index.error"), e);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 获得执行依据
     * 
     * @return return value
     */
    public Integer getFlag()
    {
        return flag;
    }

    /**
     * 设置执行依据
     * 
     * @param flag flag
     */
    public void setFlag(Integer flag)
    {
        this.flag = flag;
    }
}
