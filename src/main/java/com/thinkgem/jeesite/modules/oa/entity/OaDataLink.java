/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 记录流程数据Entity
 * @author zhangqiang
 * @version 2018-05-28
 */
public class OaDataLink extends DataEntity<OaDataLink> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程编号
	private String tableName;		// 表名
	private String dataId;		// 数据id
	
	public OaDataLink() {
		super();
	}

	public OaDataLink(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@Length(min=0, max=64, message="表名长度必须介于 0 和 64 之间")
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Length(min=0, max=64, message="数据id长度必须介于 0 和 64 之间")
	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
}