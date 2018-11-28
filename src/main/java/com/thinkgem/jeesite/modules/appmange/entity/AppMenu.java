/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * app应用Entity
 * @author hejia
 * @version 2018-04-19
 */
public class AppMenu extends DataEntity<AppMenu> {
	
	private static final long serialVersionUID = 1L;
	private String appType;		// 应用类型
	private String title;		// 名称
	private String icon;		// 图标链接
	private String sort;		// 排序
	private String href;		// 应用的请求链接
	private String target;		// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String isShow;		// 显示
	
	public AppMenu() {
		super();
	}

	public AppMenu(String id){
		super(id);
	}

	@Length(min=1, max=225, message="应用类型长度必须介于 1 和 225 之间")
	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}
	
	@Length(min=1, max=225, message="名称长度必须介于 1 和 225 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=225, message="图标链接长度必须介于 0 和 225 之间")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Length(min=0, max=11, message="排序长度必须介于 0 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	@Length(min=1, max=225, message="应用的请求链接长度必须介于 1 和 225 之间")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	@Length(min=0, max=225, message="目标（ mainFrame、_blank、_self、_parent、_top）长度必须介于 0 和 225 之间")
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	@Length(min=0, max=1, message="显示长度必须介于 0 和 1 之间")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
}