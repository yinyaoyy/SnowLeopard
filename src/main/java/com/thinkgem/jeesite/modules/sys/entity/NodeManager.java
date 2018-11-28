/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 流程节点Entity
 * @author wanglin
 * @version 2018-06-06
 */
public class NodeManager extends DataEntity<NodeManager> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 操作名称
	private String procDefKey;		// 流程标识
	private String taskDefKey;		// 任务节点id
	private String pcUrl;		// pc页面
	private String iosUrl;		// ios页面
	private String androidUrl;		// 安卓页面
	private String version;		// 版本
	private String sort;		// 排序
	
	public NodeManager() {
		super();
	}

	public NodeManager(String id){
		super(id);
	}

	@Length(min=0, max=30, message="操作名称长度必须介于 0 和 30 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=100, message="流程标识长度必须介于 0 和 100 之间")
	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}
	
	@Length(min=0, max=300, message="任务节点id长度必须介于 0 和 30 之间")
	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}
	
	@Length(min=0, max=30, message="pc页面长度必须介于 0 和 30 之间")
	public String getPcUrl() {
		return pcUrl;
	}

	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
	}
	
	@Length(min=0, max=30, message="ios页面长度必须介于 0 和 30 之间")
	public String getIosUrl() {
		return iosUrl;
	}

	public void setIosUrl(String iosUrl) {
		this.iosUrl = iosUrl;
	}
	
	@Length(min=0, max=30, message="安卓页面长度必须介于 0 和 30 之间")
	public String getAndroidUrl() {
		return androidUrl;
	}

	public void setAndroidUrl(String androidUrl) {
		this.androidUrl = androidUrl;
	}
	
	@Length(min=0, max=20, message="版本长度必须介于 0 和 20 之间")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Length(min=0, max=8, message="排序长度必须介于 0 和 8 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
}