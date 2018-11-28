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
 * 人民调解回访记录Entity
 * @author zhangqiang
 * @version 2018-05-28
 */
@JsonInclude
public class OaPeopleMediationVisit extends ActEntity<OaPeopleMediationVisit> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private String visitCause;		// 回访事由
	private Date visitDate;		// 回访时间
	private String visitSituation;		// 回访内容
	private OaPeopleMediationApply oaPeopleMediationApply; //申请表对象
	private OaPeopleMediationAgreement oaPeopleMediationAgreement; //协议书对象
	private String caseFile; //案件相关文件
	public OaPeopleMediationVisit() {
		super();
	}

	public OaPeopleMediationVisit(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	
	@Length(min=0, max=255, message="回访事由长度必须介于 0 和 255 之间")
	public String getVisitCause() {
		return visitCause;
	}

	public void setVisitCause(String visitCause) {
		this.visitCause = visitCause;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	
	public String getVisitSituation() {
		return visitSituation;
	}

	public void setVisitSituation(String visitSituation) {
		this.visitSituation = visitSituation;
	}

	public OaPeopleMediationApply getOaPeopleMediationApply() {
		return oaPeopleMediationApply;
	}

	public void setOaPeopleMediationApply(OaPeopleMediationApply oaPeopleMediationApply) {
		this.oaPeopleMediationApply = oaPeopleMediationApply;
	}

	public OaPeopleMediationAgreement getOaPeopleMediationAgreement() {
		return oaPeopleMediationAgreement;
	}

	public void setOaPeopleMediationAgreement(OaPeopleMediationAgreement oaPeopleMediationAgreement) {
		this.oaPeopleMediationAgreement = oaPeopleMediationAgreement;
	}

	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}
}