package com.wedo.bserver.common.exceptions;

/**
 * runtime exceptions
 * 
 * @author isaac
 * 
 */
public abstract class AbstractRuntimeException extends RuntimeException {

	// 进行序列化的时候用到的序列码
	private static final long serialVersionUID = -5571428359728907655L;

	// 错误代码,默认为未知错误
	private String errorCode = "UNKNOW_ERROR";

	// 原始异常
	private Throwable ex = null;

	// 错误信息中的参数
	private String[] errorArgs = null;

	// 兼容纯错误信息，不含error code,errorArgs的情况
	private String errorMessage = null;

	/**
	 * 构造函数
	 */
	public AbstractRuntimeException() {
		super();
	}

	/**
	 * 构造函数
	 * 
	 * @param msg
	 *            错误消息
	 */
	public AbstractRuntimeException(String msg) {
		super(msg);
		this.errorMessage = msg;
	}

	/**
	 * 构造函数
	 * 
	 * @param msg
	 *            错误消息
	 * @param cause
	 *            原因
	 */
	public AbstractRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
		this.errorMessage = msg;
		this.ex = cause;
	}

	/**
	 * 构造函数
	 * 
	 * @param errorCode
	 *            错误码
	 * @param errorArgs
	 *            原因
	 * @param e
	 *            e
	 */
	public AbstractRuntimeException(String errorCode, String[] errorArgs,
			Exception e) {
		this.errorCode = errorCode;
		this.errorArgs = errorArgs;
		this.ex = e;
	}

	/**
	 * errorCode的get方法
	 * 
	 * @return return value
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * errorCode的set方法
	 * 
	 * @param errorCode
	 *            errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * ex的get方法
	 * 
	 * @return return value
	 */
	public Throwable getEx() {
		return ex;
	}

	/**
	 * ex的set方法
	 * 
	 * @param ex
	 *            ex
	 */
	public void setEx(Throwable ex) {
		this.ex = ex;
	}

	/**
	 * errorArgs的get方法
	 * 
	 * @return return value
	 */
	public String[] getErrorArgs() {
		return errorArgs;
	}

	/**
	 * errorArgs的set方法
	 * 
	 * @param errorArgs
	 *            errorArgs
	 */
	public void setErrorArgs(String[] errorArgs) {
		this.errorArgs = errorArgs;
	}

	/**
	 * errorMessage的get方法
	 * 
	 * @return return value
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * errorMessage的set方法
	 * 
	 * @param errorMessage
	 *            errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
