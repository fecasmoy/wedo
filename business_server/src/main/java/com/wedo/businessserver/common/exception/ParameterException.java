package com.wedo.businessserver.common.exception;

/**
 * 参数不合法：继承于BusinessException的具体异常。适用于传入参数为空，参数样式不正确等。
 * 
 * 可根据工程实际情况，扩展具体的业务异常。
 * 
 * @author l00100468
 */
public class ParameterException
    extends BusinessException
{
    
    // 进行序列化的时候用到的序列码
    private static final long serialVersionUID = -9105383303983980464L;
    
    /**
     * 默认构造器
     */
    public ParameterException()
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
    public ParameterException(String errorCode, String[] errorArgs, Exception e)
    {
        super(errorCode, errorArgs, e);
    }
    
    /**
     * 代参构造器
     * 
     * @param errorCode errorCode
     */
    public ParameterException(String errorCode)
    {
        super(errorCode);
    }
    
}
