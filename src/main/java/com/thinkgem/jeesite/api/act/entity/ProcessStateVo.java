package com.thinkgem.jeesite.api.act.entity;


import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.api.dto.vo.common.DataEntityVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
@JsonInclude
public class ProcessStateVo  extends DataEntityVo<ProcessStateVo> {
	
	@JsonIgnore
	private String id;		// 业务id
	@JsonIgnore
	private String type;		//办理渠道
	private String caseType;	// 案件类别
	private String state;		// 状态
	private Area caseArea = new Area();
	private String title;		// 标题
	@JsonIgnore
	private Date applyBeginDate;
	@JsonIgnore
	private Date applyEndDate;
	@JsonIgnore
	private Date acceptBeginDate;
	@JsonIgnore
	private Date acceptEndDate;
	@JsonIgnore
	private String severity; //严重等级
	@JsonIgnore
	private String contractor; //承办人
	
	private String applyUser;
	private String phone;
	
	private Date applyDate;
	@JsonIgnore
	private Date acceptDate;
	@JsonIgnore
	private Page<ProcessStateVo> page;
	@JsonIgnore
	private int count;
	
	private String stateDesc;
	private String caseTypeDesc;
	private String severityDesc;
	@JsonIgnore
	private String typeDesc;
	@JsonIgnore
	private String procInsId;

	public ProcessStateVo() {
		super();
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
		this.typeDesc = DictUtils.getDictLabel(type, "oa_handle_channels");
	}
	
	@Length(min=0, max=100, message="流程状态长度必须介于 0 和 100 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		this.stateDesc = DictUtils.getDictLabel(state, "oa_act_progress");
	}

	public Area getCaseArea() {
		return caseArea;
	}

	public void setCaseArea(Area caseArea) {
		this.caseArea = caseArea;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Date getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}

	public String getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}
	
	public Page<ProcessStateVo> getPage() {
		return page;
	}

	public void setPage(Page<ProcessStateVo> page) {
		this.page = page;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getAcceptBeginDate() {
		return acceptBeginDate;
	}

	public void setAcceptBeginDate(Date acceptBeginDate) {
		this.acceptBeginDate = acceptBeginDate;
	}

	public Date getAcceptEndDate() {
		return acceptEndDate;
	}

	public void setAcceptEndDate(Date acceptEndDate) {
		this.acceptEndDate = acceptEndDate;
	}
	public Date getApplyBeginDate() {
		return applyBeginDate;
	}

	public void setApplyBeginDate(Date applyBeginDate) {
		this.applyBeginDate = applyBeginDate;
	}

	public Date getApplyEndDate() {
		return applyEndDate;
	}

	public void setApplyEndDate(Date applyEndDate) {
		this.applyEndDate = applyEndDate;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
		this.caseTypeDesc = DictUtils.getDictLabel(caseType, "oa_handle_channels");
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
		this.severityDesc = DictUtils.getDictLabel(severity, "case_rank");
	}
	
	public String getContractor() {
		return contractor;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor;
	}
	
	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}

	public void setCaseTypeDesc(String caseTypeDesc) {
		this.caseTypeDesc = caseTypeDesc;
	}

	public String getSeverityDesc() {
		return severityDesc;
	}

	public void setSeverityDesc(String severityDesc) {
		this.severityDesc = severityDesc;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
}
