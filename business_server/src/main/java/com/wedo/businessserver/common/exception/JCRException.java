package com.wedo.businessserver.common.exception;

/**
 * 元数据引擎异常类
 * 
 * @author c90003207
 * 
 */
public class JCRException
    extends BusinessException
{
    
    // 进行序列化的时候用到的序列码
    private static final long serialVersionUID = -2027539328655872801L;
    
    /**
     * 默认构造器
     */
    public JCRException()
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
    public JCRException(String errorCode, String[] errorArgs, Exception e)
    {
        super(errorCode, errorArgs, e);
    }
    
    /**
     * 代参构造器
     * 
     * @param errorCode errorCode
     * @param tx tx
     */
    public JCRException(String errorCode, Throwable tx)
    {
        super(errorCode, tx);
    }
    
    /**
     * 代参构造器
     * 
     * @param errorCode errorCode
     */
    public JCRException(String errorCode)
    {
        super(errorCode);
    }
}
