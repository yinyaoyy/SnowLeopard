/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import java.util.Date;

import org.h2.util.New;
import org.hibernate.validator.constraints.Length;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.ActEntity;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookEvaluate;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 法援申请Entity
 * @author 王鹏
 * @version 2018-05-11
 */
@JsonInclude
public class OaLegalAid extends ActEntity<OaLegalAid> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private String year;   //年份
	private String yearNo;  //年份编号
	private String name;		// 姓名
	private String sex;		// 性别
	private Date birthday;		// 出生日期
	private Area area = new Area();      // 所在区域    
	private String ethnic;		// 民族
	private String internation; //国籍
	private Area dom = new Area(); ; //户籍地到省
	private String hasMongol;   //是否涉及蒙语0=否;1=是
	private String education;  //申请人文化程度
	private String renyuanType;  //申请人类别
	private String idCard;		// 身份证号
	private String domicile;		// 户籍所在地  注：通知辩护时存的申请人户籍地详细
	private String address;		// 住所地  注：通知辩护时存的申请人羁押地
	private String postCode;		// 邮政编码
	private String phone;		// 联系电话
	private String employer;		// 工作单位
	private String proxyName;		// 代理人姓名  //法援申请时是申请联系人
	private String proxyType;		// 代理人类型
	private String proxyIdCard;		// 代理人身份证号
	private String caseTitle;		// 案情标题
	private String caseType;		// 案情类型
	private String caseReason;		// 案情及申请理由概述
	private String caseFile;		// 案件相关文件
	private Office lawOffice = new Office();		// 律师事务所
	private User lawyer = new User();		// 律师
	private String officeType; //通知机关类型;
	private String officeNamee; //通知机关名称
	private String casesum; //同案人数
	private Office legalOffice = new Office();  // 基层法律服务所   注：通知辩护时存的是具体的申请人指定的法援机构
	private User legalPerson = new User();		// 基层法律服务工作者 
	private String jgphone;                     //机关联系人电话，
	private String jgPerson;               //机关联系人
	private String thirdPartyScore;		// 第三方评估人员评分
	private String thirdPartyEvaluation;// 第三方评估人员评价
	private String receiveSubsidy;		// 承办案件领取补贴信息
	private String legalAidType;    //类型:1=申请;2=指定
	private String archiving;   //归档:0=未;1=已
	private String source;//来源(字典source)
	private String aidCategory;//法援申请所属类型(legal_aid_category)
	private String caseTelevancy;   //案件涉及
	private String caseGuilt;   //案件涉及罪名
	private String  caseInform; //通知函号
	private String  InformReson; //通知原因
	private String cumName;//附带民诉原告人
	private Date sldate;//受理日期
	private String casesStage;//诉讼阶段通知辩护的
	
	private Office lawAssistanceOffice = new Office(); //法援援助中心
	private User lawAssistUser = new User();     //法援中心兼任律师

	private String qrcode;					//二维码地址
	
	public String getJgPerson() {
		return jgPerson;
	}

	public String getCasesum() {
		return casesum;
	}

	public void setCasesum(String casesum) {
		this.casesum = casesum;
	}

	public String getCasesStage() {
		return casesStage;
	}

	public void setCasesStage(String casesStage) {
		this.casesStage = casesStage;
	}

	public void setJgPerson(String jgPerson) {
		this.jgPerson = jgPerson;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getSldate() {
		return sldate;
	}

	public void setSldate(Date sldate) {
		this.sldate = sldate;
	}
	private String caseNature;//案件性质(case_nature)
	private String caseFile2;//案件相关文件(承办人提交)
	
	private String isSubmit; //是否提交0否1是，默认为提交，为0就存到草稿表
	
	private String sexDesc;//性别说明
	private String ethnicDesc;//民族说明
	private String hasMongolDesc;//是否涉及蒙语说明
	private String proxyTypeDesc;//代理人类型说明
	private String caseTypeDesc;//案情说明
	private String aidCategoryDesc;//法援申请所属类型说明
	private String caseNatureDesc;//案件性质说明
	private String isEvaluate;//是否评价
	private GuestbookEvaluate guestbookEvaluate; //评价
	
	private String caseHandleProcess; //案件办理过程

	public OaLegalAid() {
		super();
	}

	public OaLegalAid(String id){
		super(id);
	}

	

	public String getOfficeNamee() {
		return officeNamee;
	}

	public void setOfficeNamee(String officeNamee) {
		this.officeNamee = officeNamee;
	}

	public String getInternation() {
		return internation;
	}

	public void setInternation(String internation) {
		this.internation = internation;
	}

	public Area getDom() {
		return dom;
	}

	public void setDom(Area dom) {
		this.dom = dom;
	}
	public String getIsSubmit() {
		return isSubmit;
	}

	
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getRenyuanType() {
		return renyuanType;
	}

	public void setRenyuanType(String renyuanType) {
		this.renyuanType = renyuanType;
	}

	public String getOfficeType() {
		return officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}

	public String getJgphone() {
		return jgphone;
	}

	public void setJgphone(String jgphone) {
		this.jgphone = jgphone;
	}

	public String getCaseTelevancy() {
		return caseTelevancy;
	}

	public void setCaseTelevancy(String caseTelevancy) {
		this.caseTelevancy = caseTelevancy;
	}

	public String getCaseGuilt() {
		return caseGuilt;
	}

	public void setCaseGuilt(String caseGuilt) {
		this.caseGuilt = caseGuilt;
	}

	public String getCaseInform() {
		return caseInform;
	}

	public void setCaseInform(String caseInform) {
		this.caseInform = caseInform;
	}

	public String getInformReson() {
		return InformReson;
	}

	public void setInformReson(String informReson) {
		InformReson = informReson;
	}

	public String getCumName() {
		return cumName;
	}

	public void setCumName(String cumName) {
		this.cumName = cumName;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getAidCategory() {
		return aidCategory;
	}

	public void setAidCategory(String aidCategory) {
		this.aidCategory = aidCategory;
		this.aidCategoryDesc = DictUtils.getDictLabel(aidCategory, "legal_aid_category");
	}

	public String getCaseNature() {
		return caseNature;
	}

	public void setCaseNature(String caseNature) {
		this.caseNature = caseNature;
		this.caseNatureDesc = DictUtils.getDictLabel(caseNature, "case_nature");
	}

	public String getCaseFile2() {
		return caseFile2;
	}

	public void setCaseFile2(String caseFile2) {
		this.caseFile2 = caseFile2;
	}

	public String getAidCategoryDesc() {
		return aidCategoryDesc;
	}

	public void setAidCategoryDesc(String aidCategoryDesc) {
		this.aidCategoryDesc = aidCategoryDesc;
	}

	public String getCaseNatureDesc() {
		return caseNatureDesc;
	}

	public void setCaseNatureDesc(String caseNatureDesc) {
		this.caseNatureDesc = caseNatureDesc;
	}

	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}

	public void setCaseTypeDesc(String caseTypeDesc) {
		this.caseTypeDesc = caseTypeDesc;
	}

	public String getSexDesc() {
		return sexDesc;
	}

	public void setSexDesc(String sexDesc) {
		this.sexDesc = sexDesc;
	}

	public String getEthnicDesc() {
		return ethnicDesc;
	}

	public void setEthnicDesc(String ethnicDesc) {
		this.ethnicDesc = ethnicDesc;
	}

	public String getHasMongolDesc() {
		return hasMongolDesc;
	}

	public void setHasMongolDesc(String hasMongolDesc) {
		this.hasMongolDesc = hasMongolDesc;
	}

	public String getProxyTypeDesc() {
		return proxyTypeDesc;
	}

	public void setProxyTypeDesc(String proxyTypeDesc) {
		this.proxyTypeDesc = proxyTypeDesc;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Office getLawOffice() {
		return lawOffice;
	}

	public String getHasMongol() {
		return hasMongol;
	}

	public void setHasMongol(String hasMongol) {
		this.hasMongol = hasMongol;
		this.hasMongolDesc = DictUtils.getDictLabel(hasMongol, "yes_no");
	}

	public void setLawOffice(Office lawOffice) {
		this.lawOffice = lawOffice;
	}

	public Office getLegalOffice() {
		return legalOffice;
	}

	public void setLegalOffice(Office legalOffice) {
		this.legalOffice = legalOffice;
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
//	@Length(min=1, max=200, message="姓名长度必须介于 1 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
//	@Length(min=1, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
		this.sexDesc = DictUtils.getDictLabel(sex, "sex");
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
//	@Length(min=1, max=100, message="民族长度必须介于 0 和 100 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
		this.ethnicDesc = DictUtils.getDictLabel(ethnic, "ethnic");
	}
	
//	@Length(min=1, max=20, message="身份证号长度必须介于 0 和 20 之间")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
//	@Length(min=1, max=600, message="户籍所在地长度必须介于 0 和 600 之间")
	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}
	
//	@Length(min=1, max=600, message="住所地长度必须介于 0 和 600 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=10, message="邮政编码长度必须介于 0 和 10 之间")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
//	@Length(min=1, max=20, message="联系电话长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=600, message="工作单位长度必须介于 0 和 600 之间")
	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}
	
	@Length(min=0, max=200, message="代理人姓名长度必须介于 0 和 200 之间")
	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}
	
	@Length(min=0, max=1, message="代理人类型长度必须介于 0 和 1 之间")
	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
		this.proxyTypeDesc = DictUtils.getDictLabel(proxyType, "legal_aid_proxy_type");
	}
	
	@Length(min=0, max=20, message="代理人身份证号长度必须介于 0 和 20 之间")
	public String getProxyIdCard() {
		return proxyIdCard;
	}

	public void setProxyIdCard(String proxyIdCard) {
		this.proxyIdCard = proxyIdCard;
	}
	
//	@Length(min=1, max=200, message="案情标题长度必须介于 0 和 200 之间")
	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	
//	@Length(min=1, max=1, message="案情类型长度必须介于 0 和 1 之间")
	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
		this.caseTypeDesc = DictUtils.getDictLabel(caseType, "oa_case_classify");
	}
	
	public String getCaseReason() {
		return caseReason;
	}

	public void setCaseReason(String caseReason) {
		this.caseReason = caseReason;
	}
	
	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}
	
	public User getLawyer() {
		return lawyer;
	}

	public void setLawyer(User lawyer) {
		this.lawyer = lawyer;
	}

	public User getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(User legalPerson) {
		this.legalPerson = legalPerson;
	}

	@Length(min=0, max=3, message="第三方评估人员评分长度必须介于 0 和 3 之间")
	public String getThirdPartyScore() {
		return thirdPartyScore;
	}

	public void setThirdPartyScore(String thirdPartyScore) {
		this.thirdPartyScore = thirdPartyScore;
	}

	@Length(min=0, max=2000, message="第三方评估人员评价长度必须介于 0 和 2000 之间")
	public String getThirdPartyEvaluation() {
		return thirdPartyEvaluation;
	}

	public void setThirdPartyEvaluation(String thirdPartyEvaluation) {
		this.thirdPartyEvaluation = thirdPartyEvaluation;
	}

	@Length(min=0, max=200, message="承办案件领取补贴信息长度必须介于 0 和 200 之间")
	public String getReceiveSubsidy() {
		return receiveSubsidy;
	}

	public void setReceiveSubsidy(String receiveSubsidy) {
		this.receiveSubsidy = receiveSubsidy;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getYearNo() {
		return yearNo;
	}

	public void setYearNo(String yearNo) {
		this.yearNo = yearNo;
	}

	public String getLegalAidType() {
		return legalAidType;
	}

	public void setLegalAidType(String legalAidType) {
		this.legalAidType = legalAidType;
	}

	public String getArchiving() {
		return archiving;
	}

	public void setArchiving(String archiving) {
		this.archiving = archiving;
	}

	public String getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(String isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	public GuestbookEvaluate getGuestbookEvaluate() {
		return guestbookEvaluate;
	}

	public void setGuestbookEvaluate(GuestbookEvaluate guestbookEvaluate) {
		this.guestbookEvaluate = guestbookEvaluate;
	}
	
	public Office getLawAssistanceOffice() {
		return lawAssistanceOffice;
	}

	public void setLawAssistanceOffice(Office lawAssistanceOffice) {
		this.lawAssistanceOffice = lawAssistanceOffice;
	}

	public User getLawAssistUser() {
		return lawAssistUser;
	}

	public void setLawAssistUser(User lawAssistUser) {
		this.lawAssistUser = lawAssistUser;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	@Override
	public String toString() {
		return "OaLegalAid [procInsId=" + procInsId + ", year=" + year + ", yearNo=" + yearNo + ", name=" + name
				+ ", sex=" + sex + ", birthday=" + birthday + ", area=" + area + ", ethnic=" + ethnic + ", hasMongol="
				+ hasMongol + ", education=" + education + ", renyuanType=" + renyuanType + ", idCard=" + idCard
				+ ", domicile=" + domicile + ", address=" + address + ", postCode=" + postCode + ", phone=" + phone
				+ ", employer=" + employer + ", proxyName=" + proxyName + ", proxyType=" + proxyType + ", proxyIdCard="
				+ proxyIdCard + ", caseTitle=" + caseTitle + ", caseType=" + caseType + ", caseReason=" + caseReason
				+ ", caseFile=" + caseFile + ", lawOffice=" + lawOffice + ", lawyer=" + lawyer + ", officeType="
				+ officeType + ", legalOffice=" + legalOffice + ", legalPerson=" + legalPerson + ", jgphone=" + jgphone
				+ ", thirdPartyScore=" + thirdPartyScore + ", thirdPartyEvaluation=" + thirdPartyEvaluation
				+ ", receiveSubsidy=" + receiveSubsidy + ", legalAidType=" + legalAidType + ", archiving=" + archiving
				+ ", source=" + source + ", aidCategory=" + aidCategory + ", caseTelevancy=" + caseTelevancy
				+ ", caseGuilt=" + caseGuilt + ", caseInform=" + caseInform + ", InformReson=" + InformReson
				+ ", cumName=" + cumName + ", caseNature=" + caseNature + ", caseFile2=" + caseFile2 + ", isSubmit="
				+ isSubmit + ", sexDesc=" + sexDesc + ", ethnicDesc=" + ethnicDesc + ", hasMongolDesc=" + hasMongolDesc
				+ ", proxyTypeDesc=" + proxyTypeDesc + ", caseTypeDesc=" + caseTypeDesc + ", aidCategoryDesc="
				+ aidCategoryDesc + ", caseNatureDesc=" + caseNatureDesc + ", isEvaluate=" + isEvaluate
				+ ", guestbookEvaluate=" + guestbookEvaluate + "]";
	}

	public String getCaseHandleProcess() {
		return caseHandleProcess;
	}

	public void setCaseHandleProcess(String caseHandleProcess) {
		this.caseHandleProcess = caseHandleProcess;
	}
	
}