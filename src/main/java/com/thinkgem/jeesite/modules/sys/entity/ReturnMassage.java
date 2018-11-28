package com.thinkgem.jeesite.modules.sys.entity;

import java.io.Serializable;


public class ReturnMassage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String massage;
    private boolean isValidateCodeLogin=false;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMassage() {
		return massage;
	}

	public void setMassage(String massage) {
		this.massage = massage;
	}

	public boolean isValidateCodeLogin() {
		return isValidateCodeLogin;
	}

	public void setValidateCodeLogin(boolean isValidateCodeLogin) {
		this.isValidateCodeLogin = isValidateCodeLogin;
	}

	public ReturnMassage(String code, String massage) {
		super();
		this.code = code;
		this.massage = massage;
	}

	public ReturnMassage() {
		super();
	}

}
