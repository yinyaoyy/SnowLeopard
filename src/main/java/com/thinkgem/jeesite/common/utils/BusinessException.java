/**
 * 
 */
package com.thinkgem.jeesite.common.utils;

/**
 * @author 王鹏
 * @version 2018-05-10 12:01:33
 */
public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5520957125215849866L;
	
	private int code ;
	
	public int getCode() {
		return code;
	}
	public BusinessException() {
		super();
	}
	public BusinessException(String msg) {
		super(msg);
	}
	public BusinessException(int code,String msg) {
		super(msg);
		this.code = code;
	}
}
