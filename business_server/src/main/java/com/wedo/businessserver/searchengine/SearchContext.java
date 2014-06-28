package com.wedo.businessserver.searchengine;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.wedo.businessserver.common.exception.BusinessException;
import com.wedo.businessserver.css3.domain.WFile;
import com.wedo.businessserver.css3.ws.model.FilePageInfo;

/**
 * 搜索环境类
 * 
 * @author c90003207
 * 
 */
public class SearchContext
{
    private IndexWriterFactory indexWriter;
    
    /**
     * constructor
     * 
     * @param indexWriter index writer
     */
    public SearchContext(IndexWriterFactory indexWriter)
    {
        this.indexWriter = indexWriter;
    }
    
    /**
     * 创建索引
     * 
     * @param fileList fileList
     * @param tempFile tempFile
     * @param flag flag
     * @throws BusinessException Business Exception
     */
    public void createNewIndex(List<WFile> fileList, File tempFile, Boolean flag)
        throws BusinessException
    {
        indexWriter.createNewIndex(fileList, tempFile, flag);
    }
    
    /**
     * 增量索引
     * 
     * @param wfile wfile
     * @throws BusinessException Business Exception
     */
    public void incrementIndex(WFile wfile)
        throws BusinessException
    {
        indexWriter.incrementIndex(wfile);
    }
    
    /**
     * 删除索引
     * 
     * @param wfile wfile
     * @throws BusinessException Business Exception
     */
    public void deleteIndex(WFile wfile)
        throws BusinessException
    {
        indexWriter.deleteIndex(wfile);
    }
    
    /**
     * 查询内容
     * 
     * @param keyWord keyWord
     * @param folderPath folder Path
     * @param filePageInfo file PageInfo
     * @param map map
     * @throws BusinessException Business Exception
     */
    public void search(String[] keyWord, String folderPath,
        FilePageInfo filePageInfo, HashMap<String, String> map)
        throws BusinessException
    {
        indexWriter.search(keyWord, folderPath, filePageInfo, map);
    }
    
    /**
     * 合并索引
     * 
     * @throws BusinessException Business Exception
     */
    public void incorporateIndex()
        throws BusinessException
    {
        indexWriter.ramToDisk();
    }
    
    /**
     * 重置打开索引
     */
    public void fdiskIndexSearcherInital()
    {
        indexWriter.fdiskIndexSearcherInital();
    }
}
