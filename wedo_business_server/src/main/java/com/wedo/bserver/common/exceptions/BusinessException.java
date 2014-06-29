package com.wedo.bserver.common.exceptions;

/**
 * 业务异常，所有接口、接口实现需抛出该异常
 * 
 * @author isaacxie
 */
public class BusinessException extends AbstractException {

	// 进行序列化的时候用到的序列码
	private static final long serialVersionUID = 289901618414689918L;

	/**
	 * 默认构造器
	 */
	public BusinessException() {
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
	public BusinessException(String errorCode, String[] errorArgs, Exception e) {
		super(errorCode, errorArgs, e);
	}

	/**
	 * 代参构造器
	 * 
	 * @param errorCode
	 *            errorCode
	 * @param tx
	 *            tx
	 */
	public BusinessException(String errorCode, Throwable tx) {
		super(errorCode, tx);
	}

	/**
	 * 代参构造器
	 * 
	 * @param errorCode
	 *            errorCode
	 */
	public BusinessException(String errorCode) {
		super(errorCode);
	}

}
