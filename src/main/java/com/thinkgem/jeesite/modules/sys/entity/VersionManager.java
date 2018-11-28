/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 系统版本管理Entity
 * @author huangtao
 * @version 2018-06-27
 */
public class VersionManager extends DataEntity<VersionManager> {
	
	private static final long serialVersionUID = 1L;
	private String version;		// 版本
	private String represent;		// 描述
	private String iosUrl;		// ios页面
	private String androidUrl;		// 安卓页面
	
	public VersionManager() {
		super();
	}

	public VersionManager(String id){
		super(id);
	}

	@Length(min=0, max=64, message="版本长度必须介于 0 和 64 之间")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Length(min=0, max=200, message="描述长度必须介于 0 和 200 之间")
	public String getRepresent() {
		return represent;
	}

	public void setRepresent(String represent) {
		this.represent = represent;
	}
	
	@Length(min=0, max=500, message="ios页面长度必须介于 0 和 500之间")
	public String getIosUrl() {
		return iosUrl;
	}

	public void setIosUrl(String iosUrl) {
		this.iosUrl = iosUrl;
	}
	
	@Length(min=0, max=500, message="安卓页面长度必须介于 0 和 500之间")
	public String getAndroidUrl() {
		return androidUrl;
	}

	public void setAndroidUrl(String androidUrl) {
		this.androidUrl = androidUrl;
	}
	
}