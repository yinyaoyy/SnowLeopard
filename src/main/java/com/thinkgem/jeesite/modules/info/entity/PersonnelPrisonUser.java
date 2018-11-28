/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 在册安置帮教人员Entity
 * @author huangtao
 * @version 2018-06-22
 */
public class PersonnelPrisonUser extends DataEntity<PersonnelPrisonUser> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 安置人员姓名
	private String usedName;		// 曾用名
	private String sex;		// 性别
	private String idCard;		// 身份证号
	private Date birthday;		// 出生日期
	private String nation;		// 民族
	private String education;		// 文化水平
	private String isCountry;		// 是否农村
	private String residenceAddress;		// 户籍地址
	private Date releaseTime;		// 释放时间
	private String isRecidivism;		// 是否累犯
	private String drugUse;		// 吸毒史
	private String istrain;		// 是否参加职业技能培训
	private String isSkill;		// 是否获得职业技能证书
	private String isMentalHealth;		// 是否心理健康
	private String familyTies;		// 家庭联系情况
	private String isThreePerson;		// 是否三无人员
	private String riskAssessment;		// 危险性评估
	private String reconstruction;		// 改造表现
	private String attitude;		// 认罪态度
	private String circumstances;		// 服刑期间特殊情况备注及帮教建议
	private String isConnect;		// 是否衔接
	private Area area;		// 所属旗县
	private Date entryTime;		// 录入时间
	
	public PersonnelPrisonUser() {
		super();
	}

	public PersonnelPrisonUser(String id){
		super(id);
	}
	@ExcelField(title="姓名", type=0, align=1, sort=10)
	@Length(min=0, max=50, message="安置人员姓名长度必须介于 0 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="曾用名", type=0, align=1, sort=20)
	@Length(min=0, max=50, message="曾用名长度必须介于 0 和 50 之间")
	public String getUsedName() {
		return usedName;
	}

	public void setUsedName(String usedName) {
		this.usedName = usedName;
	}
	
	@ExcelField(title="性别", type=0, align=1, sort=30)
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@ExcelField(title="身份证号", type=0, align=1, sort=40)
	@Length(min=0, max=18, message="身份证号长度必须介于 0 和 18 之间")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@ExcelField(title="出生日期", type=0, align=1, sort=50)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@ExcelField(title="民族", type=0, align=1, sort=60, dictType="ethnic")
	@Length(min=0, max=2, message="民族长度必须介于 0 和 2 之间")
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	@ExcelField(title="文化水平", type=0, align=1, sort=70)
	@Length(min=0, max=20, message="文化水平长度必须介于 0 和 20 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	
	@ExcelField(title="是否农村户籍", type=0, align=1, sort=80)
	@Length(min=0, max=1, message="是否农村长度必须介于 0 和 1 之间")
	public String getIsCountry() {
		return isCountry;
	}

	public void setIsCountry(String isCountry) {
		this.isCountry = isCountry;
	}
	@ExcelField(title="户籍地址", type=0, align=1, sort=90)
	@Length(min=0, max=200, message="户籍地址长度必须介于 0 和 200 之间")
	public String getResidenceAddress() {
		return residenceAddress;
	}

	public void setResidenceAddress(String residenceAddress) {
		this.residenceAddress = residenceAddress;
	}
	
	@ExcelField(title="释放时间", type=0, align=1, sort=100)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
	@ExcelField(title="是否累犯(是,否)", type=0, align=1, sort=110)
	@Length(min=0, max=1, message="是否累犯长度必须介于 0 和 1 之间")
	public String getIsRecidivism() {
		return isRecidivism;
	}

	public void setIsRecidivism(String isRecidivism) {
		this.isRecidivism = isRecidivism;
	}
	@ExcelField(title="吸毒史(是,否)", type=0, align=1, sort=120)
	@Length(min=0, max=1, message="吸毒史长度必须介于 0 和 1 之间")
	public String getDrugUse() {
		return drugUse;
	}

	public void setDrugUse(String drugUse) {
		this.drugUse = drugUse;
	}
	
	@ExcelField(title="是否参加职业技能培训(是,否)", type=0, align=1, sort=130)
	@Length(min=0, max=1, message="是否参加职业技能培训长度必须介于 0 和 1 之间")
	public String getIstrain() {
		return istrain;
	}

	public void setIstrain(String istrain) {
		this.istrain = istrain;
	}
	@ExcelField(title="是否获得职业技能证书(是,否)", type=0, align=1, sort=140)
	@Length(min=0, max=1, message="是否获得职业技能证书长度必须介于 0 和 1 之间")
	public String getIsSkill() {
		return isSkill;
	}

	public void setIsSkill(String isSkill) {
		this.isSkill = isSkill;
	}
	@ExcelField(title="是否心理健康(是,否)", type=0, align=1, sort=150)
	@Length(min=0, max=1, message="是否心理健康长度必须介于 0 和 1 之间")
	public String getIsMentalHealth() {
		return isMentalHealth;
	}

	public void setIsMentalHealth(String isMentalHealth) {
		this.isMentalHealth = isMentalHealth;
	}
	@ExcelField(title="家庭联系情况", type=0, align=1, sort=160)
	@Length(min=0, max=100, message="家庭联系情况长度必须介于 0 和 100 之间")
	public String getFamilyTies() {
		return familyTies;
	}

	public void setFamilyTies(String familyTies) {
		this.familyTies = familyTies;
	}
	@ExcelField(title="是否三无人员(是,否)", type=0, align=1, sort=170)
	@Length(min=0, max=1, message="是否三无人员长度必须介于 0 和 1 之间")
	public String getIsThreePerson() {
		return isThreePerson;
	}

	public void setIsThreePerson(String isThreePerson) {
		this.isThreePerson = isThreePerson;
	}
	@ExcelField(title="危险性评估", type=0, align=1, sort=180)
	@Length(min=0, max=100, message="危险性评估长度必须介于 0 和 100 之间")
	public String getRiskAssessment() {
		return riskAssessment;
	}

	public void setRiskAssessment(String riskAssessment) {
		this.riskAssessment = riskAssessment;
	}
	@ExcelField(title="改造表现", type=0, align=1, sort=190)
	@Length(min=0, max=100, message="改造表现长度必须介于 0 和 100 之间")
	public String getReconstruction() {
		return reconstruction;
	}

	public void setReconstruction(String reconstruction) {
		this.reconstruction = reconstruction;
	}
	@ExcelField(title="认罪态度", type=0, align=1, sort=200)
	@Length(min=0, max=10, message="认罪态度长度必须介于 0 和 10 之间")
	public String getAttitude() {
		return attitude;
	}

	public void setAttitude(String attitude) {
		this.attitude = attitude;
	}
	@ExcelField(title="服刑期间特殊情况备注", type=0, align=1, sort=210)
	@Length(min=0, max=200, message="服刑期间特殊情况备注及帮教建议长度必须介于 0 和 200 之间")
	public String getCircumstances() {
		return circumstances;
	}

	public void setCircumstances(String circumstances) {
		this.circumstances = circumstances;
	}
	@ExcelField(title="是否衔接(是,否)", type=0, align=1, sort=220)
	@Length(min=0, max=1, message="是否衔接长度必须介于 0 和 1 之间")
	public String getIsConnect() {
		return isConnect;
	}

	public void setIsConnect(String isConnect) {
		this.isConnect = isConnect;
	}
	@ExcelField(title="所属旗县", type=0, align=1, sort=230)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	@ExcelField(title="录入时间", type=0, align=1, sort=240)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}
	
}