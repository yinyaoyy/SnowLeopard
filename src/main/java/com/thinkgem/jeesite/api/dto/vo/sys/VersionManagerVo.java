/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.api.dto.vo.sys;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.modules.sys.entity.VersionManager;

/**
 * 系统版本管理Entity
 * @author huangtao
 * @version 2018-06-27
 */
public class VersionManagerVo  {
	
	private String version;		// 版本
	private String represent;		// 描述
	private String url;		// ios页面
	
	public VersionManagerVo() {
		super();
	}

	public VersionManagerVo(VersionManager versionManager,String tag){
		this.version=versionManager.getVersion();
		this.represent=versionManager.getRepresent();
		if("200".equals(tag)){
			this.url=versionManager.getAndroidUrl();
		}else if("300".equals(tag)){
			this.url=versionManager.getIosUrl();
		}
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "VersionManagerVo [version=" + version + ", represent="
				+ represent + ", url=" + url + "]";
	}
	
	
	
}