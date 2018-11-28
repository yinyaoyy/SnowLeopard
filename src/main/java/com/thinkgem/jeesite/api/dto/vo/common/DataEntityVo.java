package com.thinkgem.jeesite.api.dto.vo.common;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

public abstract class DataEntityVo<T> {
	protected String createByName;	// 创建者
	protected Date createDate;	// 创建日期
	protected String updateByName;	// 更新者
	protected Date updateDate;	// 更新日期
	


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}

}
