package com.wedo.bserver.common.exceptions;

/**
 * 非业务异常：所有接口、接口实现过程中遇到的非预期异常，需转化为该异常
 * 
 * @author isaac
 */
public class GeneralException extends AbstractRuntimeException {

	// 进行序列化的时候用到的序列码
	private static final long serialVersionUID = -5571428359728907655L;

	/**
	 * 默认构造器
	 */
	public GeneralException() {
		super();
	}

	/**
	 * 代参构造器
	 * 
	 * @param errorCode
	 *            errorCode
	 * @param errorArgs
	 *            errorArgs
	 * @param e
	 *            e
	 */
	public GeneralException(String errorCode, String[] errorArgs, Exception e) {
		super(errorCode, errorArgs, e);
	}

	/**
	 * 代参构造器
	 * 
	 * @param errorMessage
	 *            errorMessage
	 * @param e
	 *            e
	 */
	public GeneralException(String errorMessage, Exception e) {
		super(errorMessage, e);
	}

	/**
	 * 代参构造器
	 * 
	 * @param errorMessage
	 *            errorMessage
	 */
	public GeneralException(String errorMessage) {
		super(errorMessage);
	}
}
