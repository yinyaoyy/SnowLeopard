/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.ActEntity;

/**
 * 请假流程Entity
 * @author lin
 * @version 2018-02-07
 */
public class OaLeave extends ActEntity<OaLeave> {
	
	private static final long serialVersionUID = 1L;
	private String processInstanceId;		// 流程实例编号
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String leaveType;		// 请假类型
	private String reason;		// 请假理由
	private String file;		// 附件
	private String allTime;		// 请假总时间
	private String oaLeaveRecordIds;
	private String oaLeaveRecordNames;
	public OaLeave() {
		super();
	}

	public OaLeave(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
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
	
	@Length(min=0, max=20, message="请假类型长度必须介于 0 和 20 之间")
	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	
	@Length(min=0, max=255, message="请假理由长度必须介于 0 和 255 之间")
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
	
	@Length(min=0, max=10, message="请假总时间长度必须介于 0 和 3 之间")
	public String getAllTime() {
		return allTime;
	}

	public void setAllTime(String allTime) {
		this.allTime = allTime;
	}

	public String getOaLeaveRecordIds() {
		return oaLeaveRecordIds;
	}

	public void setOaLeaveRecordIds(String oaLeaveRecordIds) {
		this.oaLeaveRecordIds = oaLeaveRecordIds;
	}

	public String getOaLeaveRecordNames() {
		return oaLeaveRecordNames;
	}

	public void setOaLeaveRecordNames(String oaLeaveRecordNames) {
		this.oaLeaveRecordNames = oaLeaveRecordNames;
	}

}