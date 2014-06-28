package com.wedo.businessserver.common.exception;

/**
 * 数据未找到：继承于BusinessException的具体业务异常。 适用于传入参数组合查询对像，对像不存在情况。
 * 
 * @author l00100468
 * 
 */
public class DataNotFoundException
    extends BusinessException
{
    
    // 进行序列化的时候用到的序列码
    private static final long serialVersionUID = 2614896299859408761L;
    
    /**
     * 默认构造器
     */
    public DataNotFoundException()
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
    public DataNotFoundException(String errorCode, String[] errorArgs,
        Exception e)
    {
        super(errorCode, errorArgs, e);
    }
    
    /**
     * 代参构造器
     * 
     * @param errorCode errorCode
     * @param tx tx
     */
    public DataNotFoundException(String errorCode, Throwable tx)
    {
        super(errorCode, tx);
    }
    
    /**
     * 代参构造器
     * 
     * @param errorCode errorCode
     */
    public DataNotFoundException(String errorCode)
    {
        super(errorCode);
    }
    
}
