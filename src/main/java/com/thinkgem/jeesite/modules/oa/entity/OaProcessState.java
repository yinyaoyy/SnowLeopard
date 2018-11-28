/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 用户未在线时的的消息保存Entity
 * @author 尹垚
 * @version 2018-07-12
 */
public class OaProcessState extends DataEntity<OaProcessState> {
	
	private static final long serialVersionUID = 1L;
	private String id;		// 业务id
	private String title;		// 标题
	private String type;		// 办理渠道
	private String state;		// 状态
	private String applicantName; //申请人账号
	private String procInsId; //流程编号
	private String comment; //意见
	private String caseType; //案件类别
	private String caseArea; //案件地区
	private Date handleDate; //受理时间
	private String contractor; //承办人
	private String severity; //严重等级
	private String stateDesc;
	private String caseTypeDesc;
	
	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}

	public void setCaseTypeDesc(String caseTypeDesc) {
		this.caseTypeDesc = caseTypeDesc;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public OaProcessState() {
		super();
	}

	public OaProcessState(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="流程编号长度必须介于 0 和 64 之间")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Length(min=0, max=200, message="流程标题长度必须介于 0 和 200 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=100, message="流程类型长度必须介于 0 和 100 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=100, message="流程状态长度必须介于 0 和 100 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		this.stateDesc = DictUtils.getDictLabel(state, "oa_act_progress");
	}
	
	@Length(min=0, max=100, message="申请人账号长度必须介于 0 和 100 之间")
	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	
	@Length(min=0, max=100, message="流程编号长度必须介于 0 和 100 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	@Length(min=0, max=4000, message="意见长度必须介于 0 和 4000之间")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
		this.caseTypeDesc = DictUtils.getDictLabel(caseType, "oa_handle_channels");
	}

	public String getCaseArea() {
		return caseArea;
	}

	public void setCaseArea(String caseArea) {
		this.caseArea = caseArea;
	}

	public Date getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}

	public String getContractor() {
		return contractor;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	
}