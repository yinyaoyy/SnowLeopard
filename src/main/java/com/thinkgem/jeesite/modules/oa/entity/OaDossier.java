/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.ActEntity;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAgreement;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;

/**
 * 卷宗说明Entity
 * @author zhangqiang
 * @version 2018-05-28
 */
@JsonInclude
public class OaDossier extends ActEntity<OaDossier> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private String dossierTitle;		// 卷宗标题
	private String dossierContent;		// 卷宗内容
	private OaPeopleMediationApply oaPeopleMediationApply; //申请表对象
	private OaPeopleMediationAgreement oaPeopleMediationAgreement; //协议书对象
	private String caseFile; //案件相关文件
	private String status;    //卷宗状态

	
	public String getStatus(){
		if(status==null){
			return "0";
		}
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	
	public OaPeopleMediationApply getOaPeopleMediationApply() {
		return oaPeopleMediationApply;
	}

	public void setOaPeopleMediationApply(OaPeopleMediationApply oaPeopleMediationApply) {
		this.oaPeopleMediationApply = oaPeopleMediationApply;
	}

	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}

	public OaDossier() {
		super();
	}

	public OaDossier(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@Length(min=0, max=64, message="卷宗标题长度必须介于 0 和 64 之间")
	public String getDossierTitle() {
		return dossierTitle;
	}

	public void setDossierTitle(String dossierTitle) {
		this.dossierTitle = dossierTitle;
	}
	
	public String getDossierContent() {
		return dossierContent;
	}

	public void setDossierContent(String dossierContent) {
		this.dossierContent = dossierContent;
	}

	public OaPeopleMediationAgreement getOaPeopleMediationAgreement() {
		return oaPeopleMediationAgreement;
	}

	public void setOaPeopleMediationAgreement(OaPeopleMediationAgreement oaPeopleMediationAgreement) {
		this.oaPeopleMediationAgreement = oaPeopleMediationAgreement;
	}
	
}