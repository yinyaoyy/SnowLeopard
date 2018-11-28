/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.ActEntity;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 人民调解受理登记Entity
 * @author zhangqiang
 * @version 2018-05-25
 */
@JsonInclude
public class OaPeopleMediationAcceptRegister extends ActEntity<OaPeopleMediationAcceptRegister> {
	
	private static final long serialVersionUID = 1L;
	private String id;
	

	private String procInsId;		// 流程实例编号
	private String caseSource;		// 案件来源
	private String hasMongol; 		//是否涉及蒙语
	private String caseRank;		//案件严重等级
	private Area caseCounty = new Area();	// 案件所在旗县
	private Area caseTown = new Area();		// 案件所在乡镇

	private Date caseTime;			// 案件发生时间
	private String caseArea;		// 案件发生详细地址
	private String caseInvolveCount; //案件涉及人员数量
	private String caseType;		// 纠纷类型
	private String disputeSituation;		// 纠纷简要情况
	private String caseFile; 		//案件相关文件
	
	private String caseSourceDesc; //案件来源描述
	private String hasMongolDesc;   //是否涉及蒙语描述
	private String caseRankDesc;    //案件严重等级描述
	private String caseTypeDesc;	//纠纷类型

	private String overThreshold;   //案件涉及人数是否超过阈值，0代表没有超过，1代表超过了

	public String getOverThreshold(){
		if(overThreshold == null){
			return "0";
		}
		return overThreshold;
	}

	public void setOverThreshold(String overThreshold){
		this.overThreshold = overThreshold;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getCaseSourceDesc() {
		return caseSourceDesc;
	}

	public void setCaseSourceDesc(String caseSourceDesc) {
		this.caseSourceDesc = caseSourceDesc;
	}

	public String getHasMongolDesc() {
		return hasMongolDesc;
	}

	public void setHasMongolDesc(String hasMongolDesc) {
		this.hasMongolDesc = hasMongolDesc;
	}

	public String getCaseRankDesc() {
		return caseRankDesc;
	}

	public void setCaseRankDesc(String caseRankDesc) {
		this.caseRankDesc = caseRankDesc;
	}

	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}

	public void setCaseTypeDesc(String caseTypeDesc) {
		this.caseTypeDesc = caseTypeDesc;
	}

	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}

	private OaPeopleMediationApply oaPeopleMediationApply; //申请表对象
	
	public String getHasMongol() {
		return hasMongol;
	}

	public void setHasMongol(String hasMongol) {
		this.hasMongol = hasMongol;
		this.hasMongolDesc = DictUtils.getDictLabel(hasMongol, "yes_no");
	}

	public String getCaseRank() {
		return caseRank;
	}

	public void setCaseRank(String caseRank) {
		this.caseRank = caseRank;
		this.caseRankDesc = DictUtils.getDictLabel(caseRank, "case_rank");
	}

	public Area getCaseCounty() {
		return caseCounty;
	}

	public void setCaseCounty(Area caseCounty) {
		this.caseCounty = caseCounty;
	}

	public Area getCaseTown() {
		return caseTown;
	}

	public void setCaseTown(Area caseTown) {
		this.caseTown = caseTown;
	}

	public String getCaseInvolveCount() {
		return caseInvolveCount;
	}

	public void setCaseInvolveCount(String caseInvolveCount) {
		this.caseInvolveCount = caseInvolveCount;
	}

	public String getDisputeSituation() {
		return disputeSituation;
	}

	public void setDisputeSituation(String disputeSituation) {
		this.disputeSituation = disputeSituation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	public OaPeopleMediationAcceptRegister() {
		super();
	}

	public OaPeopleMediationAcceptRegister(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	
	@Length(min=0, max=64, message="案件发生地区长度必须介于 0 和 64 之间")
	public String getCaseArea() {
		return caseArea;
	}

	public void setCaseArea(String caseArea) {
		this.caseArea = caseArea;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getCaseTime() {
		return caseTime;
	}

	public void setCaseTime(Date caseTime) {
		this.caseTime = caseTime;
	}
	
	@Length(min=0, max=8, message="纠纷类型长度必须介于 0 和 8 之间")
	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
		this.caseTypeDesc = DictUtils.getDictLabel(caseType, "dispute_type");
	}
	
	@Length(min=0, max=64, message="案件来源长度必须介于 0 和 64 之间")
	public String getCaseSource() {
		return caseSource;
	}

	public void setCaseSource(String caseSource) {
		this.caseSource = caseSource;
		this.caseSourceDesc = DictUtils.getDictLabel(caseSource, "mediate_case_Source");
	}
	
	public void setOaPeopleMediationApply(OaPeopleMediationApply oaPeopleMediationApply) {
		this.oaPeopleMediationApply = oaPeopleMediationApply;
	}

	public OaPeopleMediationApply getOaPeopleMediationApply() {
		return oaPeopleMediationApply;
	}
}