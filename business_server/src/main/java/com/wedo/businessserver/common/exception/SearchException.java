package com.wedo.businessserver.common.exception;

/**
 * 搜索引擎异常类
 * 
 * @author c90003207
 * 
 */
public class SearchException
    extends BusinessException
{
    
    // 进行序列化的时候用到的序列码
    private static final long serialVersionUID = -2027539328655872801L;
    
    /**
     * 默认构造器
     */
    public SearchException()
    {
        super();
    }
    
    /**
     * 代参构造器
     * 
     * @param errorCode errorCode
     * @param errorArgs errorArgs
     * @param e e
     */
    public SearchException(String errorCode, String[] errorArgs, Exception e)
    {
        super(errorCode, errorArgs, e);
    }
    
    /**
     * 代参构造器
     * 
     * @param errorCode errorCode
     * @param tx tx
     */
    public SearchException(String errorCode, Throwable tx)
    {
        super(errorCode, tx);
    }
}
