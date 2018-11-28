/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.ActEntity;

/**
 * 公出单Entity
 * @author lin
 * @version 2018-02-28
 */
public class OaGongchu extends ActEntity<OaGongchu> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String gongchuType;		// 公出类型
	private String reason;		// 公出理由
	private String file;		// 附件
	private String allTime;		// 公出总时间
	private String oaGongchuRecordIds;
	private String oaGongchuRecordNames;
	private int agreeCount;		// 同意数量
	private int allCount;		// 总任务数

	public OaGongchu() {
		super();
	}

	public OaGongchu(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Length(min=0, max=20, message="公出类型长度必须介于 0 和 20 之间")
	public String getGongchuType() {
		return gongchuType;
	}

	public void setGongchuType(String gongchuType) {
		this.gongchuType = gongchuType;
	}
	
	@Length(min=0, max=255, message="公出理由长度必须介于 0 和 255 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Length(min=0, max=255, message="附件长度必须介于 0 和 255 之间")
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	@Length(min=0, max=10, message="公出总时间长度必须介于 0 和 10 之间")
	public String getAllTime() {
		return allTime;
	}

	public void setAllTime(String allTime) {
		this.allTime = allTime;
	}

	public String getOaGongchuRecordIds() {
		return oaGongchuRecordIds;
	}

	public void setOaGongchuRecordIds(String oaGongchuRecordIds) {
		this.oaGongchuRecordIds = oaGongchuRecordIds;
	}

	public String getOaGongchuRecordNames() {
		return oaGongchuRecordNames;
	}

	public void setOaGongchuRecordNames(String oaGongchuRecordNames) {
		this.oaGongchuRecordNames = oaGongchuRecordNames;
	}

	public int getAgreeCount() {
		return agreeCount;
	}

	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
	
}