package com.thinkgem.jeesite.common.utils;

/**
 * 
 * <b>类名称：</b>MicroException
 * <b>类描述：</b>调用远程接口异常类
 * <b>创建人：</b>ChenSong
 * <b>修改人：</b>ChenSong
 * <b>修改时间：</b>2016-5-26 下午3:54:57
 * <b>修改备注：</b>
 * @version v1.0<br/>
 */
public class MicroException extends Exception {
	private static final long serialVersionUID = -3330292413711550945L;

	public MicroException() {
		super();
	}

	public MicroException(String message) {
		super(message);
	}
}
