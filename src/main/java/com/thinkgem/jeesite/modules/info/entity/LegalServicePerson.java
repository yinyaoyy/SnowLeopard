/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 基层法律服务工作者Entity
 * @author 王鹏
 * @version 2018-05-09
 */
public class LegalServicePerson extends DataEntity<LegalServicePerson> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private Office legalOffice;		// 所属基层法律服务所
	private Date birthday;		// 出生年月
	private String age ;   //年龄
	private String phone;		// 手机号码
	private String sex;		// 性别
	private String idCard;		// 身份证号
	private String licenseNumber;		// 执业证号
	private String qualificationNo;		// 资格证号
	private String weixinNo;		// 微信号
	private String qqNo;		// QQ号
	private String licenseType;		// 专职/兼职
	private Date certificateDate;		// 领证时间（YYYY-MM-DD）
	private Date practisingDate;		// 执业时间（YYYY-MM-DD）
	private String industryRewards;		// 行业奖励（多个用&ldquo;@&rdquo;隔开）
	private String imageUrl;		// 照片名称（身份证号.jpg）
	private Office office;		// 主营机关
	private Area area;// 地区
	private String practisingYear;//执业年限(根据执业时间自动计算)
	private String ethnic;		// 民族
	private String education;		// 学历
	private String politicalFace;		// 政治面貌
	private String businessExpertise; //业务专长
	private	String introduction; //个人简介
	private	String administrativeDivision; //执业行政区划
	private String isMongolian;//精通蒙汉双语 0是1否
	private String role = ""; //角色
	
	public LegalServicePerson() {
		super();
	}

	public LegalServicePerson(String id){
		super(id);
	}

	public LegalServicePerson(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}

	@ExcelField(title="姓名", type=0, align=1, sort=10)
	@Length(min=0, max=300, message="姓名长度必须介于 0 和 300 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@ExcelField(title="起始年度", type=0, align=1, sort=80) 
	public String getPractisingYear() {
		return practisingYear;
	}

	public void setPractisingYear(String practisingYear) {
		this.practisingYear = practisingYear;
	}

	@ExcelField(value="legalOffice.name", title="工作单位",  type=0, align=1, sort=70)
	public Office getLegalOffice() {
		return legalOffice;
	}

	public void setLegalOffice(Office legalOffice) {
		this.legalOffice = legalOffice;
	}
	
	public String getAge() {
		if(StringUtils.isNotBlank(idCard)) {
			Map<String,String> map = DateUtils.getBirAgeSex(idCard);
			this.age =  map.get("age");
		}
	    return age;

	}

	public void setAge(String age) {
		this.age = age;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
		this.age = String.valueOf(DateUtils.pastYear(birthday));
	}

	@Length(min=0, max=20, message="手机号码长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ExcelField(title="性别", type=0, align=1, sort=20, dictType="sex")
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		if(StringUtils.isNotBlank(idCard)) {
			Map<String,String> map = DateUtils.getBirAgeSex(idCard);
			this.sex =  map.get("sexCode");
		}
	    return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@ExcelField(title="身份证号", type=0, align=1, sort=50)
	@Length(min=0, max=20, message="身份证号长度必须介于 0 和 20 之间")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@ExcelField(title="执业证号", type=0, align=1, sort=90)
	@Length(min=0, max=50, message="执业证号长度必须介于 0 和 50 之间")
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	@Length(min=0, max=50, message="资格证号长度必须介于 0 和 50 之间")
	public String getQualificationNo() {
		return qualificationNo;
	}

	public void setQualificationNo(String qualificationNo) {
		this.qualificationNo = qualificationNo;
	}

	@Length(min=0, max=20, message="微信号长度必须介于 0 和 20 之间")
	public String getWeixinNo() {
		return weixinNo;
	}

	public void setWeixinNo(String weixinNo) {
		this.weixinNo = weixinNo;
	}

	@Length(min=0, max=20, message="QQ号长度必须介于 0 和 20 之间")
	public String getQqNo() {
		return qqNo;
	}

	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}

	@Length(min=0, max=1, message="专职/兼职长度必须介于 0 和 1 之间")
	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getCertificateDate() {
		return certificateDate;
	}

	public void setCertificateDate(Date certificateDate) {
		this.certificateDate = certificateDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getPractisingDate() {
		return practisingDate;
	}

	public void setPractisingDate(Date practisingDate) {
		this.practisingDate = practisingDate;
	}

	@Length(min=0, max=2000, message="行业奖励（多个用&ldquo;@&rdquo;隔开）长度必须介于 0 和 2000 之间")
	public String getIndustryRewards() {
		return industryRewards;
	}

	public void setIndustryRewards(String industryRewards) {
		this.industryRewards = industryRewards;
	}

	@ExcelField(title="照片", type=0, align=1, sort=130)
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="民族", type=0, align=1, sort=30, dictType="ethnic")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}

	@ExcelField(title="学历", type=0, align=1, sort=40)
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	
	@ExcelField(title="政治面貌", type=0, align=1, sort=60)
	public String getPoliticalFace() {
		return politicalFace;
	}

	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}

	@ExcelField(title="业务专长", type=0, align=1, sort=100)
	public String getBusinessExpertise() {
		return businessExpertise;
	}

	public void setBusinessExpertise(String businessExpertise) {
		this.businessExpertise = businessExpertise;
	}

	@ExcelField(title="个人简介", type=0, align=1, sort=110)
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@ExcelField(title="执业行政区划", type=0, align=1, sort=120)
	public String getAdministrativeDivision() {
		return administrativeDivision;
	}

	public void setAdministrativeDivision(String administrativeDivision) {
		this.administrativeDivision = administrativeDivision;
	}

	public String getIsMongolian() {
		return isMongolian;
	}

	public void setIsMongolian(String isMongolian) {
		this.isMongolian = isMongolian;
	}
	
	@ExcelField(title="角色", align=2, sort=140, dictType="info_role_type")
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}