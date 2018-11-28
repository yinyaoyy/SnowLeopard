/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.ActEntity;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 法援通知辩护Entity
 * @author suzz
 * @version 2018-07-11
 */
@JsonInclude
public class OaLegalAidInform extends ActEntity<OaLegalAidInform> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private String year;		// 年份
	private String yearNo;		// 年份编号
	private String name;		// 姓名
	private String sex;		// 性别
	private Date birthday;		// 出生日期
	private String ethnic;		// 民族
	private String hasmongol;		// 是否涉及蒙语0=否;1=是
	private String idCard;		// 身份证号
	private String domicile;		// 户籍所在地
	private String address;		// 住所地
	private String phone;		// 联系电话
	private String proxyName;		// 法援申请联系人
	private String caseTitle;		// 案情标题
	private String caseReason;		// 案情及申请理由概述
	private String caseFile;		// 案件相关文件
	private Office lawOffice = new Office();	// 律师事务所
	private User lawyer = new User();		// 律师
	private Office legalOffice = new Office();;		// 法援机构
	private User legalPerson= new User();		// 法援机构处理人
	private String thirdPartyScore;		// 第三方评估人员评分
	private String thirdPartyEvaluation;		// 第三方评估人员评价
	private String receiveSubsidy;		// 承办案件领取补贴信息
	private String legalAidType;		// 类型:1=申请;2=指定
	private String archiving;		// 归档:0=未;1=已
	private String source;		// 来源_source
	private String aidCategory;		// 法援通知申请所属类型
	private String caseFile2;		// 案件相关文件承办人提交
	private String isEvaluate;		// 是否评价
	private String caseHandleProcess;		// 案件办理过程
	private String education;		// 申请人学历
	private String renyuanType;		// 申请人类型
	private String officeType;		// 申请机构类别
	private String jgphone;		// 申请机构联系人电话
	private String caseTelevancy;		// 案件涉及
	private String caseGuilt;		// 案件涉及罪名
	private String caseInform;		// 通知函号
	private String informReson;		// 通知原因
	private String cumName;		// 附带民诉原告人(暂定可存多个以逗号分开）
	private Area dom = new Area(); ; 	// 申请人户籍地到省
	private String internation;		// 申请人国籍
	private String officeNamee;		// 通知机关名称
	private String jgPerson;		// 机关联系人
	private Date sldate;		// 受理日期
	private String casesStage;		// 诉讼阶段
	private String casesum;		// 同案人数
	private Office lawOfficeId;

	private String isSubmit;
	private String proxyType;
	private String modality;        //援助方式\
	private Date scdate;	
	private String casesStagedesc;
	private String sexdesc;	
	private String ethnicdesc;
	private String educationdesc;
	private String caseTelevancydesc;	
	private String informResondesc;
	private String renyuanTypedesc;
	private String internationdesc;	
	private String caseGuiltdesc;
	private String modalitydesc;
	
	//讲不过移动端加2个字段
	//审查意见
	private String approveCom;	
	//审批意见
	private String fyzhurenCom;

	


	public String getApproveCom() {
		return approveCom;
	}

	public void setApproveCom(String approveCom) {
		this.approveCom = approveCom;
	}

	public String getFyzhurenCom() {
		return fyzhurenCom;
	}

	public void setFyzhurenCom(String fyzhurenCom) {
		this.fyzhurenCom = fyzhurenCom;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getScdate() {
		return scdate;
	}

	public void setScdate(Date scdate) {
		this.scdate = scdate;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
		this.modalitydesc=DictUtils.getDictLabel(modality, "oa_modalities");
	}

	public String getModalitydesc() {
		return modalitydesc;
	}

	public void setModalitydesc(String modalitydesc) {
		this.modalitydesc = modalitydesc;
	}

	public String getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}

	public OaLegalAidInform() {
		super();
	}

	public OaLegalAidInform(String id){
		super(id);
	}

	//@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	//@Length(min=0, max=24, message="年份长度必须介于 0 和 4 之间")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	//@Length(min=0, max=24, message="年份编号长度必须介于 0 和 6 之间")
	public String getYearNo() {
		return yearNo;
	}

	public void setYearNo(String yearNo) {
		this.yearNo = yearNo;
	}
	
	//@Length(min=0, max=200, message="姓名长度必须介于 1 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
		this.sexdesc = DictUtils.getDictLabel(sex, "sex");
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	//@Length(min=0, max=100, message="民族长度必须介于 0 和 100 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
		this.ethnicdesc = DictUtils.getDictLabel(ethnic, "ethnic");
	}
	
	//@Length(min=0, max=1, message="是否涉及蒙语0=否;1=是长度必须介于 0 和 1 之间")
	public String getHasmongol() {
		return hasmongol;
	}

	public void setHasmongol(String hasmongol) {
		this.hasmongol = hasmongol;
	}
	
	//@Length(min=0, max=20, message="身份证号长度必须介于 0 和 20 之间")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	//@Length(min=0, max=600, message="户籍所在地长度必须介于 0 和 600 之间")
	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}
	
	//@Length(min=0, max=600, message="住所地长度必须介于 0 和 600 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	//@Length(min=0, max=20, message="联系电话长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	//@Length(min=0, max=200, message="法援申请联系人长度必须介于 0 和 200 之间")
	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}
	
	//@Length(min=0, max=200, message="案情标题长度必须介于 0 和 200 之间")
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
	
	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}
	
	//@Length(min=0, max=64, message="律师事务所长度必须介于 0 和 64 之间")
	public Office getLawOfficeId() {
		return lawOfficeId;
	}

	public void setLawOfficeId(Office lawOfficeId) {
		this.lawOfficeId = lawOfficeId;
	}
	

	
	public Office getLawOffice() {
		return lawOffice;
	}

	public void setLawOffice(Office lawOffice) {
		this.lawOffice = lawOffice;
	}

	public User getLawyer() {
		return lawyer;
	}

	public void setLawyer(User lawyer) {
		this.lawyer = lawyer;
	}

	public void setLegalOffice(Office legalOffice) {
		this.legalOffice = legalOffice;
	}

	//@Length(min=0, max=64, message="法援机构长度必须介于 0 和 64 之间")
	public Office getLegalOffice() {
		return legalOffice;
	}

	public void setLegalOfficeId(Office legalOffice) {
		this.legalOffice = legalOffice;
	}
	
	//@Length(min=0, max=64, message="法援机构处理人长度必须介于 0 和 64 之间")
	public User getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(User legalPerson) {
		this.legalPerson = legalPerson;
	}
	
	//@Length(min=0, max=3, message="第三方评估人员评分长度必须介于 0 和 3 之间")
	public String getThirdPartyScore() {
		return thirdPartyScore;
	}

	public void setThirdPartyScore(String thirdPartyScore) {
		this.thirdPartyScore = thirdPartyScore;
	}
	
	//@Length(min=0, max=2000, message="第三方评估人员评价长度必须介于 0 和 2000 之间")
	public String getThirdPartyEvaluation() {
		return thirdPartyEvaluation;
	}

	public void setThirdPartyEvaluation(String thirdPartyEvaluation) {
		this.thirdPartyEvaluation = thirdPartyEvaluation;
	}
	
	//@Length(min=0, max=200, message="承办案件领取补贴信息长度必须介于 0 和 200 之间")
	public String getReceiveSubsidy() {
		return receiveSubsidy;
	}

	public void setReceiveSubsidy(String receiveSubsidy) {
		this.receiveSubsidy = receiveSubsidy;
	}
	
	//@Length(min=0, max=1, message="类型:1=申请;2=指定长度必须介于 1 和 1 之间")
	public String getLegalAidType() {
		return legalAidType;
	}

	public void setLegalAidType(String legalAidType) {
		this.legalAidType = legalAidType;
	}
	
	//@Length(min=0, max=1, message="归档:0=未;1=已长度必须介于 1 和 1 之间")
	public String getArchiving() {
		return archiving;
	}

	public void setArchiving(String archiving) {
		this.archiving = archiving;
	}
	
	//@Length(min=0, max=4, message="来源_source长度必须介于 0 和 3 之间")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	//@Length(min=0, max=2, message="法援申请所属类型()长度必须介于 0 和 2 之间")
	public String getAidCategory() {
		return aidCategory;
	}

	public void setAidCategory(String aidCategory) {
		this.aidCategory = aidCategory;
	}
	
	public String getCaseFile2() {
		return caseFile2;
	}

	public void setCaseFile2(String caseFile2) {
		this.caseFile2 = caseFile2;
	}
	
	//@Length(min=0, max=1, message="是否评价长度必须介于 0 和 1 之间")
	public String getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(String isEvaluate) {
		this.isEvaluate = isEvaluate;
	}
	
	public String getCaseHandleProcess() {
		return caseHandleProcess;
	}

	public void setCaseHandleProcess(String caseHandleProcess) {
		this.caseHandleProcess = caseHandleProcess;
	}
	
	//@Length(min=0, max=12, message="申请人学历长度必须介于 0 和 12 之间")
	public String getEducation() {
		return education;
		
	}

	public void setEducation(String education) {
		this.education = education;
		this.educationdesc = DictUtils.getDictLabel(education, "oa_education");
	}
	
	//@Length(min=0, max=12, message="申请人类型长度必须介于 0 和 12 之间")
	public String getRenyuanType() {
		return renyuanType;
	}

	public void setRenyuanType(String renyuanType) {
		this.renyuanType = renyuanType;
		this.renyuanTypedesc = DictUtils.getDictLabel(renyuanType, "oa_personnel_category");
	}
	
	@Length(min=0, max=12, message="申请机构类别长度必须介于 0 和 12 之间")
	public String getOfficeType() {
		return officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}
	
	@Length(min=0, max=12, message="申请机构联系人电话长度必须介于 0 和 12 之间")
	public String getJgphone() {
		return jgphone;
	}

	public void setJgphone(String jgphone) {
		this.jgphone = jgphone;
	}
	
	//@Length(min=0, max=12, message="案件涉及长度必须介于 0 和 12 之间")
	public String getCaseTelevancy() {
		return caseTelevancy;
	}

	public void setCaseTelevancy(String caseTelevancy) {
		this.caseTelevancy = caseTelevancy;
		this.caseTelevancydesc = DictUtils.getDictLabel(caseTelevancy, "oa_case_involving");
	}
	
	//@Length(min=0, max=12, message="案件涉及罪名长度必须介于 0 和 12 之间")
	public String getCaseGuilt() {
		return caseGuilt;
	}

	public void setCaseGuilt(String caseGuilt) {
		this.caseGuilt = caseGuilt;
		String caseGuiltdescee="";
		String arr []=caseGuilt.split(",");
		for(int i=0;i<arr.length;i++) {
			String j=arr [i];
			caseGuiltdescee+= DictUtils.getDictLabel(j, "oa_caseGuilt")+";";
		}
		this.caseGuiltdesc=caseGuiltdescee;
	}
	
	//@Length(min=0, max=12, message="通知函号长度必须介于 0 和 12 之间")
	public String getCaseInform() {
		return caseInform;
	}

	public void setCaseInform(String caseInform) {
		this.caseInform = caseInform;
	}
	
	//@Length(min=0, max=12, message="通知原因长度必须介于 0 和 12 之间")
	public String getInformReson() {
		return informReson;
	}

	public void setInformReson(String informReson) {
		this.informReson = informReson;
		this.informResondesc=DictUtils.getDictLabel(informReson, "oa_informReson");
	}
	
	//@Length(min=0, max=64, message="附带民诉原告人(暂定可存多个以逗号分开）长度必须介于 0 和 64 之间")
	public String getCumName() {
		return cumName;
	}

	public void setCumName(String cumName) {
		this.cumName = cumName;
	}
	
	//@Length(min=0, max=64, message="申请人户籍地到省长度必须介于 0 和 64 之间")
	public Area getDom() {
		return dom;
	}

	public void setDom(Area dom) {
		this.dom = dom;
	}
	
	//@Length(min=0, max=12, message="申请人国籍长度必须介于 0 和 12 之间")
	public String getInternation() {
		return internation;
	}

	public void setInternation(String internation) {
		this.internation = internation;
		this.internationdesc=DictUtils.getDictLabel(internation, "oa_internation");
	}
	
	//@Length(min=0, max=64, message="通知机关名称长度必须介于 0 和 64 之间")
	public String getOfficeNamee() {
		return officeNamee;
	}

	public void setOfficeNamee(String officeNamee) {
		this.officeNamee = officeNamee;
	}
	
	//@Length(min=0, max=12, message="机关联系人长度必须介于 0 和 12 之间")
	public String getJgPerson() {
		return jgPerson;
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
	
	//@Length(min=0, max=12, message="诉讼阶段长度必须介于 0 和 12 之间")
	public String getCasesStage() {
		return casesStage;
	}

	public void setCasesStage(String casesStage) {
		this.casesStage = casesStage;
		this.casesStagedesc=DictUtils.getDictLabel(casesStage, "oa_litigation_phase");
	}
	
	//@Length(min=0, max=12, message="同案人数长度必须介于 0 和 12 之间")
	public String getCasesum() {
		return casesum;
	}

	public void setCasesum(String casesum) {
		this.casesum = casesum;
	}

	public String getCasesStagedesc() {
		return casesStagedesc;
	}

	public void setCasesStagedesc(String casesStagedesc) {
		this.casesStagedesc = casesStagedesc;
	}

	public String getSexdesc() {
		return sexdesc;
	}

	public void setSexdesc(String sexdesc) {
		this.sexdesc = sexdesc;
	}

	public String getEthnicdesc() {
		return ethnicdesc;
	}

	public void setEthnicdesc(String ethnicdesc) {
		this.ethnicdesc = ethnicdesc;
	}


	public String getEducationdesc() {
		return educationdesc;
	}

	public void setEducationdesc(String educationdesc) {
		this.educationdesc = educationdesc;
	}

	public String getCaseTelevancydesc() {
		return caseTelevancydesc;
	}

	public void setCaseTelevancydesc(String caseTelevancydesc) {
		this.caseTelevancydesc = caseTelevancydesc;
	}

	public String getInformResondesc() {
		return informResondesc;
	}

	public void setInformResondesc(String informResondesc) {
		this.informResondesc = informResondesc;
	}

	public String getRenyuanTypedesc() {
		return renyuanTypedesc;
	}

	public void setRenyuanTypedesc(String renyuanTypedesc) {
		this.renyuanTypedesc = renyuanTypedesc;
	}

	public String getInternationdesc() {
		return internationdesc;
	}

	public void setInternationdesc(String internationdesc) {
		this.internationdesc = internationdesc;
	}

	public String getCaseGuiltdesc() {
		return caseGuiltdesc;
	}

	public void setCaseGuiltdesc(String caseGuiltdesc) {
		this.caseGuiltdesc = caseGuiltdesc;
	}
	
}