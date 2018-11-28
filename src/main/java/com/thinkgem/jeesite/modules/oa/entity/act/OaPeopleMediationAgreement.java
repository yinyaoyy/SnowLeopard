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
 * 人民调解协议书Entity
 * @author zhangqiang
 * @version 2018-05-28
 */
@JsonInclude
public class OaPeopleMediationAgreement extends ActEntity<OaPeopleMediationAgreement> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private String agreementCode;		// 协议书编号
	private String disputeFact;		// 纠纷事实
	private String disputeMatter;		// 争议事项
	private String agreementContent;		// 达成协议内容
	private String performMode;		// 履行方式
	private Date timeLimit;		// 时限
	private String caseFile; //案件相关文件
	private String recorder; //协议记录人
	private OaPeopleMediationApply oaPeopleMediationApply; //申请表对象

	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Date timeLimit) {
		this.timeLimit = timeLimit;
	}

	public OaPeopleMediationApply getOaPeopleMediationApply() {
		return oaPeopleMediationApply;
	}

	public void setOaPeopleMediationApply(OaPeopleMediationApply oaPeopleMediationApply) {
		this.oaPeopleMediationApply = oaPeopleMediationApply;
	}

		
	public OaPeopleMediationAgreement() {
		super();
	}

	public OaPeopleMediationAgreement(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@Length(min=0, max=255, message="协议书编号长度必须介于 0 和 255 之间")
	public String getAgreementCode() {
		return agreementCode;
	}

	public void setAgreementCode(String agreementCode) {
		this.agreementCode = agreementCode;
	}
	
	
	@Length(min=0, max=400, message="纠纷事实长度必须介于 0 和 400 之间")
	public String getDisputeFact() {
		return disputeFact;
	}

	public void setDisputeFact(String disputeFact) {
		this.disputeFact = disputeFact;
	}
	
	@Length(min=0, max=400, message="争议事项长度必须介于 0 和 400 之间")
	public String getDisputeMatter() {
		return disputeMatter;
	}

	public void setDisputeMatter(String disputeMatter) {
		this.disputeMatter = disputeMatter;
	}
	
	public String getAgreementContent() {
		return agreementContent;
	}

	public void setAgreementContent(String agreementContent) {
		this.agreementContent = agreementContent;
	}
	
	@Length(min=0, max=32, message="履行方式长度必须介于 0 和 32 之间")
	public String getPerformMode() {
		return performMode;
	}

	public void setPerformMode(String performMode) {
		this.performMode = performMode;
	}

	public String getRecorder() {
		return recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}
	
	
}