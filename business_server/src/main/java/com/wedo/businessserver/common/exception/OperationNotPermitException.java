package com.wedo.businessserver.common.exception;

/**
 *操作不合法：扩展与BusinessException的具体业务异常， 主要用于标识当前操作不合法，如移动文件夹时目标文件夹有重名文件；
 * 文件重命名时文件名重名、或与原文件名相同。
 * 
 * @author l00100468
 */
public class OperationNotPermitException
    extends BusinessException
{
    
    // 进行序列化的时候用到的序列码
    private static final long serialVersionUID = -2027539328655872801L;
    
    /**
     * 默认构造器
     */
    public OperationNotPermitException()
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
    public OperationNotPermitException(String errorCode, String[] errorArgs,
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
    public OperationNotPermitException(String errorCode, Throwable tx)
    {
        super(errorCode, tx);
    }
    
    /**
     * 代参构造器
     * 
     * @param errorCode errorCode
     */
    public OperationNotPermitException(String errorCode)
    {
        super(errorCode);
    }
    
}
