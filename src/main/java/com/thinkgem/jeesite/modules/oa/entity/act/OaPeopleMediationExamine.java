/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.ActEntity;

/**
 * 人民调解调查记录Entity
 * @author zhangqiang
 * @version 2018-05-28
 */
@JsonInclude
public class OaPeopleMediationExamine extends ActEntity<OaPeopleMediationExamine> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private Date examineDate;		// 调查时间
	private String examinePlace;		// 调查地点
	private String participants;		// 参加人
	private String inquirer;		// 调查人
	private String respondent;		// 被调查人
	private String recordContent;		// 记录内容
	private String recorder;		// 记录人
	private String caseFile; //案件相关文件

	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}

	public OaPeopleMediationApply getOaPeopleMediationApply() {
		return oaPeopleMediationApply;
	}

	public void setOaPeopleMediationApply(OaPeopleMediationApply oaPeopleMediationApply) {
		this.oaPeopleMediationApply = oaPeopleMediationApply;
	}

	private OaPeopleMediationApply oaPeopleMediationApply; //申请表对象
	
	public OaPeopleMediationExamine() {
		super();
	}

	public OaPeopleMediationExamine(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getExamineDate() {
		return examineDate;
	}

	public void setExamineDate(Date examineDate) {
		this.examineDate = examineDate;
	}
	
	@Length(min=0, max=255, message="地点长度必须介于 0 和 255 之间")
	public String getExaminePlace() {
		return examinePlace;
	}

	public void setExaminePlace(String examinePlace) {
		this.examinePlace = examinePlace;
	}
	
	@Length(min=0, max=400, message="参加人长度必须介于 0 和 400 之间")
	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}
	
	@Length(min=0, max=64, message="调查人长度必须介于 0 和 64 之间")
	public String getInquirer() {
		return inquirer;
	}

	public void setInquirer(String inquirer) {
		this.inquirer = inquirer;
	}
	
	@Length(min=0, max=400, message="被调查人长度必须介于 0 和 400 之间")
	public String getRespondent() {
		return respondent;
	}

	public void setRespondent(String respondent) {
		this.respondent = respondent;
	}
	
	public String getRecordContent() {
		return recordContent;
	}

	public void setRecordContent(String recordContent) {
		this.recordContent = recordContent;
	}
	
	@Length(min=0, max=64, message="记录人长度必须介于 0 和 64 之间")
	public String getRecorder() {
		return recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}
	
}