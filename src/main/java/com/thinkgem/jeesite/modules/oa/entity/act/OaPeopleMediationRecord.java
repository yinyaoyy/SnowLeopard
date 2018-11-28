/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.ActEntity;

import net.sf.ehcache.writer.writebehind.CoalesceKeysFilter;

/**
 * 人民调解调解记录Entity
 * @author zhangqiang
 * @version 2018-05-28
 */
@JsonInclude
public class OaPeopleMediationRecord extends ActEntity<OaPeopleMediationRecord> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private Date mediateDate;		// 调解时间
	private String mediatePlace;		// 调解地点
	private String mediateRecord;		// 调解记录
	private String caseFile; //案件相关文件
	private String recorder;		// 记录人
	private String result;		// 调解结果
	private OaPeopleMediationApply oaPeopleMediationApply; //申请表对象
	private OaPeopleMediationExamine oaPeopleMediationExamine; //调查表对象
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getMediateDate() {
		return mediateDate;
	}

	public void setMediateDate(Date mediateDate) {
		this.mediateDate = mediateDate;
	}

	public String getMediatePlace() {
		return mediatePlace;
	}

	public void setMediatePlace(String mediatePlace) {
		this.mediatePlace = mediatePlace;
	}

	public OaPeopleMediationApply getOaPeopleMediationApply() {
		return oaPeopleMediationApply;
	}

	public void setOaPeopleMediationApply(OaPeopleMediationApply oaPeopleMediationApply) {
		this.oaPeopleMediationApply = oaPeopleMediationApply;
	}

	public OaPeopleMediationExamine getOaPeopleMediationExamine() {
		return oaPeopleMediationExamine;
	}

	public void setOaPeopleMediationExamine(OaPeopleMediationExamine oaPeopleMediationExamine) {
		this.oaPeopleMediationExamine = oaPeopleMediationExamine;
	}

	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}

	
	public OaPeopleMediationRecord() {
		super();
	}

	public OaPeopleMediationRecord(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	public String getMediateRecord() {
		return mediateRecord;
	}

	public void setMediateRecord(String mediateRecord) {
		this.mediateRecord = mediateRecord;
	}

	public String getRecorder() {
		return recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}