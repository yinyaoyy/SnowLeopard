/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tripartite.entity;

import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 与第三方系统对接配置表Entity
 * @author 王鹏
 * @version 2018-06-23
 */
public class TripartiteSystemConfig extends DataEntity<TripartiteSystemConfig> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String systemId;	//系统id
	private TripartiteSystemConfig parent;		// 上级id
	private String value;		// 系统、接口地址
	private String description;		// 中文说明
	private String method;		//请求方式get/post等
	private String serviceName;		//处理服务的名称
	private String requestRate;		// 请求频率(默认1天)
	private Date lastRequestDate;		// 上次请求时间
	private String isPause;		// 是否暂停执行
	private TripartiteSystemConfig beforeTask;	//前置任务
	
	public TripartiteSystemConfig() {
		super();
	}

	public TripartiteSystemConfig(String id){
		super(id);
	}

	@Override
	public String toString() {
		return "TripartiteSystemConfig [type=" + type + ", systemId=" + systemId + ", parent=" + parent + ", value="
				+ value + ", description=" + description + ", method=" + method + ", serviceName=" + serviceName
				+ ", requestRate=" + requestRate + ", lastRequestDate=" + lastRequestDate + ", isPause=" + isPause
				+ ", beforeTask=" + beforeTask + ", remarks=" + remarks + ", createBy=" + createBy + ", createDate="
				+ createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate + ", delFlag=" + delFlag
				+ ", id=" + id + "]";
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public TripartiteSystemConfig getBeforeTask() {
		return beforeTask;
	}

	public void setBeforeTask(TripartiteSystemConfig beforeTask) {
		this.beforeTask = beforeTask;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Length(min=0, max=1, message="类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@JsonBackReference
	public TripartiteSystemConfig getParent() {
		return parent;
	}

	public void setParent(TripartiteSystemConfig parent) {
		this.parent = parent;
	}
	
	@Length(min=0, max=300, message="值长度必须介于 0 和 300 之间")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Length(min=0, max=300, message="中文说明长度必须介于 0 和 300 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=3, message="请求频率(默认1天)长度必须介于 0 和 3 之间")
	public String getRequestRate() {
		return requestRate;
	}

	public void setRequestRate(String requestRate) {
		this.requestRate = requestRate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLastRequestDate() {
		return lastRequestDate;
	}

	public void setLastRequestDate(Date lastRequestDate) {
		this.lastRequestDate = lastRequestDate;
	}
	
	@Length(min=0, max=1, message="是否暂停执行长度必须介于 0 和 1 之间")
	public String getIsPause() {
		return isPause;
	}

	public void setIsPause(String isPause) {
		this.isPause = isPause;
	}
	
}