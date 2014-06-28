package com.wedo.businessserver.css3.ws.model;

import java.util.List;

/**
 * 搜索引擎返回的查询结果集
 * 
 * @author c90003207
 * 
 */
public class FilePageInfo
{
    /**
     * default page count
     */
    private static final int DEFAULT_PAGE_COUNT = 10;
    
    /** 
     * 查询结果集
     */
    private List<SFile> pageResults;
    
    /** 
     * 每页大小
     */
    private Integer countOfCurrentPage = DEFAULT_PAGE_COUNT;
    
    /** 
     * 当前页号
     */
    private Integer currentPage;
    
    /** 
     * 总共记录数
     */
    private Integer totalCount;
    
    /** 
     *错误编码
     */
    private String message;
    
    /**
     * getter
     * @return list
     */
    public List<SFile> getPageResults()
    {
        return pageResults;
    }
    
    /**
     * setter
     * @param pageResults pageResults
     */
    public void setPageResults(List<SFile> pageResults)
    {
        this.pageResults = pageResults;
    }
    
    /**
     * getter
     * @return int
     */
    public Integer getCountOfCurrentPage()
    {
        return countOfCurrentPage;
    }
    
    /**
     * setter
     * @param countOfCurrentPage countOfCurrentPage
     */
    public void setCountOfCurrentPage(Integer countOfCurrentPage)
    {
        this.countOfCurrentPage = countOfCurrentPage;
    }
    
    /**
     * getter
     * @return int
     */
    public Integer getCurrentPage()
    {
        return currentPage;
    }
    
    /**
     * setter
     * @param currentPage currentPage
     */
    public void setCurrentPage(Integer currentPage)
    {
        this.currentPage = currentPage;
    }
    
    /**
     * getter
     * @return int
     */
    public Integer getTotalCount()
    {
        return totalCount;
    }
    
    /**
     * setter
     * @param totalCount totalCount
     */
    public void setTotalCount(Integer totalCount)
    {
        this.totalCount = totalCount;
    }
    
    /**
     * getter
     * @return string
     */
    public String getMessage()
    {
        return message;
    }
    
    /**
     * setter
     * @param message message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
}
