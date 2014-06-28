package com.wedo.businessserver.common.dao.support;

import java.util.List;

/**
 * 分页对象
 * 
 * @param <T>
 * @author zhe.chen
 * 
 */
public class PageInfo<T>
{
    
    // 默认页数
    private static final int DEFAULT_PAGES = 10;
    
    // 查询分页结果
    private List<T> pageResults;
    
    // 当页记录数 默认为10条记录
    private int countOfCurrentPage = DEFAULT_PAGES;
    
    // 当前页号
    private int currentPage;
    
    // 总共记录数
    private long totalCount;
    
    // 总共页数
    private long totalPage;
    
    /**
     * 默认构造函数
     */
    public PageInfo()
    {
        this.currentPage = 1;
        this.countOfCurrentPage = DEFAULT_PAGES;
        this.totalCount = 0;
        this.totalPage = 0;
    }
    
    /**
     * 带参构造器
     * 
     * @param countOfCurrentPage countOfCurrentPage
     * @param currentPage currentPage
     */
    public PageInfo(int countOfCurrentPage, int currentPage)
    {
        super();
        this.countOfCurrentPage = countOfCurrentPage;
        this.currentPage = currentPage;
    }
    
    /**
     * 是否有下页
     * 
     * @return return value
     */
    public boolean hasNext()
    {
        return currentPage != totalPage;
    }
    
    /**
     * 是否有上页
     * 
     * @return return value
     */
    public boolean hasPrevious()
    {
        return currentPage != 1;
    }
    
    /**
     * 获取countOfCurrentPage
     * 
     * @return return value
     */
    public int getCountOfCurrentPage()
    {
        return countOfCurrentPage;
    }
    
    /**
     * countOfCurrentPage的set方法
     * 
     * @param countOfCurrentPage countOfCurrentPage
     */
    public void setCountOfCurrentPage(int countOfCurrentPage)
    {
        this.countOfCurrentPage = countOfCurrentPage;
    }
    
    /**
     * 获取currentPage
     * 
     * @return return value
     */
    public int getCurrentPage()
    {
        return currentPage;
    }
    
    /**
     * currentPage的set方法
     * 
     * @param currentPage currentPage
     */
    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }
    
    /**
     * 获取所有页面信息
     * 
     * @return return value
     */
    public List<T> getPageResults()
    {
        return pageResults;
    }
    
    /**
     * page Results
     * 
     * @param pageResults pageResults
     */
    public void setPageResults(List<T> pageResults)
    {
        this.pageResults = pageResults;
    }
    
    /**
     * 总页数
     * 
     * @return return value
     */
    public long getTotalCount()
    {
        return totalCount;
    }
    
    /**
     * total count
     * 
     * @param totalCount totalCount
     */
    public void setTotalCount(long totalCount)
    {
        this.totalCount = totalCount;
    }
    
    /**
     * total pages
     * 
     * @return return value
     */
    public long getTotalPage()
    {
        return ((totalCount + countOfCurrentPage) - 1) / countOfCurrentPage;
    }
    
    /**
     * total pages
     * 
     * @param totalPage totalPage
     */
    public void setTotalPage(long totalPage)
    {
        this.totalPage = totalPage;
    }
    
    /**
     * 装配pageResults
     * 
     * @param results results
     */
    @SuppressWarnings("unchecked")
    public void calPageResults(List results)
    {
        int iOffset;
        if (results == null)
        {
            throw new IllegalArgumentException("null argument!");
        }
        // 赋值为返回记录总条数
        int iDatasSize = results.size();
        // 如果返回记录条数，大于当前面最大记录序号则偏移量为当前页条数，
        if (iDatasSize >= countOfCurrentPage * currentPage)
        {
            // 如果返回记录数小于当前页包括当前页的总条数，则偏移为：
            iOffset = countOfCurrentPage;
        }
        else
        {
            iOffset = iDatasSize - countOfCurrentPage * (currentPage - 1);
        }
        // iStart值为，当前页条数*前一页页码
        int iStart = countOfCurrentPage * (currentPage - 1);
        
        pageResults = results.subList(iStart, iStart + iOffset);
    }
}
