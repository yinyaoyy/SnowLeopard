/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.common.persistence.ActEntity;

/**
 * 法律服务直通车Entity
 * @author zq
 * @version 2018-07-07
 */
@JsonInclude
public class OaFastLegal extends ActEntity<OaFastLegal> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	@JsonIgnore
	private User acceptMan = new User();	// 受理人
	private String acceptManName;   //受理人姓名
	private String acceptManCode;		// 受理人工号
	private String caseAcceptCode;		// 案件受理编号/案件登记编号
	private String accuserName;		// 申请人姓名
	private String accuserSex;		// 申请人性别
	private String accuserEthnic;		// 申请人民族
	private Date accuserBirthday;		// 申请人出生日期
	private String accuserPhone;		// 申请人手机号
	private String accuserIdCard;		// 申请人身份证号
	private Area accuserCounty = new Area();		// 申请人所在地区
	private Area accuserTown = new Area();		// 申请人所在乡镇
	private String caseClassify;		// 案件类别（人民调解或法律援助）
	private String caseType;		// 案件类型
	private Area caseCounty = new Area();		// 案发地区
	private Area caseTown = new Area();		// 案件发生乡镇
	private String caseTitle;		// 案件标题
	private String caseReason;		// 案发简要说明
	private Date caseTime;		// 案件发生时间
	private String caseInvolvecount;		// 涉及人数
	private String caseSource;		// 案件来源/受理方式
	private String caseInvolveMoney;		// 涉及金额
	private String caseRank;		// 案件严重等级
	private Office office = new Office();		// 机构id
	private User transactor = new User();		// 承办人
	
	private String handleResult;		// 处理结果
	@JsonIgnore
	private String isEvaluate;		// 是否评价0代表未评价，1代表评论了
	@JsonIgnore
	private String overthreshold;		// 代表参与人数是否超过阈值，0代表未超过，1代表超过
	@JsonIgnore
	private String status;		// 案件状态0代表工作人员还未受理，1代表工作人员已经受理，2代表已分发给业务部门领导3，代表
	private String caseFile;		// 附件
	
	private String caseClassifyDesc; //案件类别的描述
	private String caseTypeDesc;	//案件类型的描述
	private String caseSourceDesc;   //受理方式的描述
	private String caseRankDesc;     //案件严重等级的描述
	private String accuserSexDesc;   //性别的描述
	private String accuserEthnicDesc;  //民族的描述
	private String caseInvolveMoneyDesc;  //金额的描述
	private String isSubmit;          //"true"是提交，"false"是保存

	private String qrcode;   			//二维码地址
	
	public OaFastLegal() {
		super();
	}

	public OaFastLegal(String id){
		super(id);
	}

//	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
//	@Length(min=0, max=64, message="受理人长度必须介于 0 和 64 之间")
	public User getAcceptMan() {
		return acceptMan;
	}

	public void setAcceptMan(User acceptMan) {
		this.acceptMan = acceptMan;
	}
	
//	@Length(min=0, max=64, message="受理人工号长度必须介于 0 和 64 之间")
	public String getAcceptManCode() {
		if(acceptMan!=null&&!"".equals(acceptMan.getId())){
			return acceptMan.getNo();
		}
		return acceptManCode;
	}

	public void setAcceptManCode(String acceptManCode) {
		this.acceptManCode = acceptManCode;
	}
	
//	@Length(min=0, max=64, message="案件受理编号/案件登记编号长度必须介于 0 和 64 之间")
	public String getCaseAcceptCode() {
		return caseAcceptCode;
	}

	public void setCaseAcceptCode(String caseAcceptCode) {
		this.caseAcceptCode = caseAcceptCode;
	}
	
//	@Length(min=0, max=64, message="申请人姓名长度必须介于 0 和 64 之间")
	public String getAccuserName() {
		return accuserName;
	}

	public void setAccuserName(String accuserName) {
		this.accuserName = accuserName;
	}
	
//	@Length(min=0, max=1, message="申请人性别长度必须介于 0 和 1 之间")
	public String getAccuserSex() {
		return accuserSex;
	}

	public void setAccuserSex(String accuserSex) {
		this.accuserSex = accuserSex;
		this.accuserSexDesc = DictUtils.getDictLabel(accuserSex, "sex");
	}
	
//	@Length(min=0, max=8, message="申请人民族长度必须介于 0 和 8 之间")
	public String getAccuserEthnic() {
		return accuserEthnic;
	}

	public void setAccuserEthnic(String accuserEthnic) {
		this.accuserEthnic = accuserEthnic;
		this.accuserEthnicDesc = DictUtils.getDictLabel(accuserEthnic, "ethnic");
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getAccuserBirthday() {
		return accuserBirthday;
	}

	public void setAccuserBirthday(Date accuserBirthday) {
		this.accuserBirthday = accuserBirthday;
	}
	
//	@Length(min=1, max=64, message="申请人手机号长度必须介于 1 和 64 之间")
	public String getAccuserPhone() {
		return accuserPhone;
	}

	public void setAccuserPhone(String accuserPhone) {
		this.accuserPhone = accuserPhone;
	}
	
//	@Length(min=0, max=64, message="申请人身份证号长度必须介于 0 和 64 之间")
	public String getAccuserIdCard() {
		return accuserIdCard;
	}

	public void setAccuserIdCard(String accuserIdCard) {
		this.accuserIdCard = accuserIdCard;
	}
	
//	@Length(min=0, max=64, message="申请人所在地区长度必须介于 0 和 64 之间")
	public Area getAccuserCounty() {
		return accuserCounty;
	}

	public void setAccuserCounty(Area accuserCounty) {
		this.accuserCounty = accuserCounty;
	}
	
//	@Length(min=0, max=64, message="申请人所在乡镇长度必须介于 0 和 64 之间")
	public Area getAccuserTown() {
		return accuserTown;
	}

	public void setAccuserTown(Area accuserTown) {
		this.accuserTown = accuserTown;
	}
	
//	@Length(min=1, max=64, message="案件类别（人民调解或法律援助）长度必须介于 1 和 64之间")
	public String getCaseClassify() {
		return caseClassify;
	}

	public void setCaseClassify(String caseClassify) {
		this.caseClassify = caseClassify;
		this.caseClassifyDesc = DictUtils.getDictLabel(caseClassify, "oa_case_classify");
	}
	
//	@Length(min=1, max=8, message="案件类型长度必须介于 1 和 8 之间")
	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
		this.caseTypeDesc = DictUtils.getDictLabel(caseType, "oa_case_classify");
	}
	
//	@Length(min=1, max=64, message="案发地区长度必须介于 1 和 64 之间")
	public Area getCaseCounty() {
		return caseCounty;
	}

	public void setCaseCounty(Area caseCounty) {
		this.caseCounty = caseCounty;
	}
	
//	@Length(min=0, max=64, message="案件发生乡镇长度必须介于 0 和 64 之间")
	public Area getCaseTown() {
		return caseTown;
	}

	public void setCaseTown(Area caseTown) {
		this.caseTown = caseTown;
	}
	
//	@Length(min=0, max=64, message="案件标题长度必须介于 0 和 64 之间")
	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	
	public String getCaseReason() {
		return caseReason;
	}

	public void setCaseReason(String caseReason) {
		this.caseReason = caseReason;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getCaseTime() {
		return caseTime;
	}

	public void setCaseTime(Date caseTime) {
		this.caseTime = caseTime;
	}
	
//	@Length(min=0, max=8, message="涉及人数长度必须介于 0 和 8 之间")
	public String getCaseInvolvecount() {
		return caseInvolvecount;
	}

	public void setCaseInvolvecount(String caseInvolvecount) {
		this.caseInvolvecount = caseInvolvecount;
	}
	
//	@Length(min=0, max=8, message="案件来源/受理方式长度必须介于 0 和 8 之间")
	public String getCaseSource() {
		return caseSource;
	}

	public void setCaseSource(String caseSource) {
		this.caseSource = caseSource;
		this.caseSourceDesc = DictUtils.getDictLabel(caseSource, "fast_case_source");
	}
	
//	@Length(min=0, max=64, message="涉及金额长度必须介于 0 和 64 之间")
	public String getCaseInvolveMoney() {
		return caseInvolveMoney;
	}

	public void setCaseInvolveMoney(String caseInvolveMoney) {
		this.caseInvolveMoney = caseInvolveMoney;
		this.caseInvolveMoneyDesc = DictUtils.getDictLabel(caseInvolveMoney, "oa_case_money");
	}
	
//	@Length(min=0, max=8, message="案件严重等级长度必须介于 0 和 8 之间")
	public String getCaseRank() {
		return caseRank;
	}

	public void setCaseRank(String caseRank) {
		this.caseRank = caseRank;
		this.caseRankDesc = DictUtils.getDictLabel(caseRank, "case_rank");
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	public User getTransactor() {
		return transactor;
	}

	public void setTransactor(User transactor) {
		this.transactor = transactor;
	}
	
	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}
	
//	@Length(min=0, max=1, message="是否评价0代表未评价，1代表评论了长度必须介于 0 和 1 之间")
	public String getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(String isEvaluate) {
		this.isEvaluate = isEvaluate;
	}
	
//	@Length(min=0, max=1, message="代表参与人数是否超过阈值，0代表未超过，1代表超过长度必须介于 0 和 1 之间")
	public String getOverthreshold() {
		return overthreshold;
	}

	public void setOverthreshold(String overthreshold) {
		this.overthreshold = overthreshold;
	}
	
//	@Length(min=0, max=8, message="案件状态长度必须介于 0 和 8 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
//	@Length(min=0, max=255, message="附件长度必须介于 0 和 255 之间")
	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}
	
	public String getCaseClassifyDesc() {
		return caseClassifyDesc;
	}

	public void setCaseClassifyDesc(String caseClassifyDesc) {
		this.caseClassifyDesc = caseClassifyDesc;
	}

	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}

	public void setCaseTypeDesc(String caseTypeDesc) {
		this.caseTypeDesc = caseTypeDesc;
	}

	public String getCaseSourceDesc() {
		return caseSourceDesc;
	}

	public void setCaseSourceDesc(String caseSourceDesc) {
		this.caseSourceDesc = caseSourceDesc;
	}

	public String getCaseRankDesc() {
		return caseRankDesc;
	}

	public void setCaseRankDesc(String caseRankDesc) {
		this.caseRankDesc = caseRankDesc;
	}

	public String getAccuserSexDesc() {
		return accuserSexDesc;
	}

	public void setAccuserSexDesc(String accuserSexDesc) {
		this.accuserSexDesc = accuserSexDesc;
	}

	public String getAccuserEthnicDesc() {
		return accuserEthnicDesc;
	}

	public void setAccuserEthnicDesc(String accuserEthnicDesc) {
		this.accuserEthnicDesc = accuserEthnicDesc;
	}
	
	public String getAcceptManName() {
		if(acceptMan!=null&&!"".equals(acceptMan.getId())){
			return acceptMan.getName();
		}
		return acceptManName;
	}

	public void setAcceptManName(String acceptManName) {
		this.acceptManName = acceptManName;
	}

	public String getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getCaseInvolveMoneyDesc() {
		return caseInvolveMoneyDesc;
	}

	public void setCaseInvolveMoneyDesc(String caseInvolveMoneyDesc) {
		this.caseInvolveMoneyDesc = caseInvolveMoneyDesc;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

}