package com.wedo.businessserver.common.exception;

/**
 * 异常
 * 
 * @author c90003207
 * 
 */
public abstract class AbstractException
    extends Exception
{
    
    // 进行序列化的时候用到的序列码
    private static final long serialVersionUID = -1213814304328018204L;
    
    // 错误代码,默认为未知错误
    private String errorCode = "UNKNOW_ERROR";
    
    // 原始异常
    private Throwable ex = null;
    
    // 错误信息中的参数
    private String[] errorArgs = null;
    
    // 兼容纯错误信息，不含error code,errorArgs的情况
    private String errorMessage = null;
    
    /**
     * 默认构造函数
     */
    public AbstractException()
    {
        super();
    }
    
    /**
     * 带参构造函数
     * 
     * @param msg msg
     */
    public AbstractException(String msg)
    {
        super(msg);
        this.errorMessage = msg;
    }
    
    /**
     * 带参构造函数
     * 
     * @param msg msg
     * @param cause cause
     */
    public AbstractException(String msg, Throwable cause)
    {
        super(msg, cause);
        this.errorMessage = msg;
        this.ex = cause;
    }
    
    /**
     * 带参构造函数
     * 
     * @param errorCode errorCode
     * @param errorArgs errorArgs
     * @param e e
     */
    public AbstractException(String errorCode, String[] errorArgs, Exception e)
    {
        this.errorCode = errorCode;
        this.errorArgs = errorArgs;
        this.ex = e;
    }
    
    /**
     * errorCode的get方法
     * 
     * @return return value
     */
    public String getErrorCode()
    {
        return errorCode;
    }
    
    /**
     * errorCode的set方法
     * 
     * @param errorCode errorCode
     */
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }
    
    /**
     * ex的get方法
     * 
     * @return return value
     */
    public Throwable getEx()
    {
        return ex;
    }
    
    /**
     * ex的set方法
     * 
     * @param ex ex
     */
    public void setEx(Throwable ex)
    {
        this.ex = ex;
    }
    
    /**
     * errorArgs的get方法
     * 
     * @return return value
     */
    public String[] getErrorArgs()
    {
        return errorArgs;
    }
    
    /**
     * errorArgs的set方法
     * 
     * @param errorArgs errorArgs
     */
    public void setErrorArgs(String[] errorArgs)
    {
        this.errorArgs = errorArgs;
    }
    
    /**
     * errorMessage的get方法
     * 
     * @return return value
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }
    
    /**
     * errorMessage的set方法
     * 
     * @param errorMessage errorMessage
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    
}
