/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 鉴定所管理Entity
 * @author hejia
 * @version 2018-04-23
 */
public class InfoJudicialAuthentication extends DataEntity<InfoJudicialAuthentication> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String address;		// 地址
	private String phone;		// 联系电话
	private String worktime;		// 工作时间
	private String sort;		// 排序
	private String licenseNumber;		// 组织机构代码证编号
	private String licenseHerf;		// 组织机构代码证
	private String introduction;		// 机构简介
	private Area area;		// 所在地区
	private String isShow;		// 显示
	private String log;		// logo
	private String coordinate;		// 经纬度(经度在前)
	private Date foundingTime;		// 成立时间
	private String businessExpertise;		// 业务专长
	private String personInCharge;		// 法人
	private String personInChargeHerf;		// 负责人照片
	private String teamSize;		// 团队规模
	private String practiceLicense;		// 执业许可证
	private String practiceLicenseNum;		// 执业许可证号
	private Long practiceLicenseExpiryTime;		// 职业许可证过期时间
	private String occupationalState;		// 职业状态
	private String scopeOfBusiness;		// 业务范围
	private String officialWebAddress;		// 官网地址
	private String unifiedCode;		// 统一社会信用代码
	private String fax;		// 传真
	private String zipCode;		// 邮编
	private Date effectiveDate;		// 机构有效开始日期
	private Date deadline;		// 机构有限截止日期
	private Date firstRegistration;		// 首次获准登记日期
	private String certificateAuthority;		// 颁证机关
	private Date certificateDate;		// 颁证日期
	private String entrustManagement;		// 委托管理部门
	private String registeredAuthority;		// 登记管理部门名称
	private String institutionalNature;		// 机构性质
	private String legalPersonSex;		// 法人性别
	private String legalPersonMobile;		// 法人手机号码
	private String chargePerson;		// 机构负责人姓名
	private String chargePersonSex;		// 机构负责人性别
	private String chargePersonMobile;		// 负责人手机号码
	
	public InfoJudicialAuthentication() {
		super();
	}

	public InfoJudicialAuthentication(String id){
		super(id);
	}
	@ExcelField(title = "名称", align = 2, sort = 15)
	@Length(min=0, max=225, message="名称长度必须介于 0 和 225 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title = "地址", align = 2, sort = 10)
	@Length(min=0, max=225, message="地址长度必须介于 0 和 225 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@ExcelField(title = "联系电话", align = 2, sort = 15)
	@Length(min=0, max=22, message="联系电话长度必须介于 0 和 22 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=50, message="工作时间长度必须介于 0 和 50 之间")
	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}
	
	@Length(min=0, max=11, message="排序长度必须介于 0 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	@Length(min=0, max=100, message="组织机构代码证编号长度必须介于 0 和 100 之间")
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	@ExcelField(title = "组织机构代码证", align = 2, sort = 80)
	@Length(min=0, max=100, message="组织机构代码证长度必须介于 0 和 100 之间")
	public String getLicenseHerf() {
		return licenseHerf;
	}

	public void setLicenseHerf(String licenseHerf) {
		this.licenseHerf = licenseHerf;
	}
	
	@Length(min=0, max=2250, message="机构简介长度必须介于 0 和 2250 之间")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	@ExcelField(title = "所在地区", align = 2, sort = 20)
	@NotNull(message="所在地区不能为空")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@Length(min=0, max=1, message="显示长度必须介于 0 和 1 之间")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	@Length(min=0, max=225, message="logo长度必须介于 0 和 225 之间")
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	 @ExcelField(title = "经纬度，经度在前", align = 2, sort = 25)
	@Length(min=1, max=50, message="经纬度(经度在前)长度必须介于 1 和 50 之间")
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	@ExcelField(title = "成立时间", align = 2, sort = 30)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="成立时间不能为空")
	public Date getFoundingTime() {
		return foundingTime;
	}

	public void setFoundingTime(Date foundingTime) {
		this.foundingTime = foundingTime;
	}
	@ExcelField(title = "业务专长", align = 2, sort = 35)
	@Length(min=0, max=512, message="业务专长长度必须介于 0 和 512 之间")
	public String getBusinessExpertise() {
		return businessExpertise;
	}

	public void setBusinessExpertise(String businessExpertise) {
		this.businessExpertise = businessExpertise;
	}
	@ExcelField(title = "法人", align = 2, sort = 85)
	@Length(min=0, max=50, message="法人长度必须介于 0 和 50 之间")
	public String getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	
	@Length(min=0, max=225, message="负责人照片长度必须介于 0 和 225 之间")
	public String getPersonInChargeHerf() {
		return personInChargeHerf;
	}

	public void setPersonInChargeHerf(String personInChargeHerf) {
		this.personInChargeHerf = personInChargeHerf;
	}
	@ExcelField(title = "团队规模", align = 2, sort = 45)
	@Length(min=0, max=11, message="团队规模长度必须介于 0 和 11 之间")
	public String getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(String teamSize) {
		this.teamSize = teamSize;
	}
	@ExcelField(title = "执业许可证编号", align = 2, sort = 50)
	@Length(min=1, max=225, message="执业许可证长度必须介于 1 和 225 之间")
	public String getPracticeLicense() {
		return practiceLicense;
	}

	public void setPracticeLicense(String practiceLicense) {
		this.practiceLicense = practiceLicense;
	}
	
	@Length(min=0, max=225, message="执业许可证号长度必须介于 0 和 225 之间")
	public String getPracticeLicenseNum() {
		return practiceLicenseNum;
	}

	public void setPracticeLicenseNum(String practiceLicenseNum) {
		this.practiceLicenseNum = practiceLicenseNum;
	}
	
	public Long getPracticeLicenseExpiryTime() {
		return practiceLicenseExpiryTime;
	}

	public void setPracticeLicenseExpiryTime(Long practiceLicenseExpiryTime) {
		this.practiceLicenseExpiryTime = practiceLicenseExpiryTime;
	}
	
	@Length(min=0, max=10, message="职业状态长度必须介于 0 和 10 之间")
	public String getOccupationalState() {
		return occupationalState;
	}

	public void setOccupationalState(String occupationalState) {
		this.occupationalState = occupationalState;
	}
	@ExcelField(title = "业务范围", align = 2, sort = 55)
	@Length(min=0, max=1024, message="业务范围长度必须介于 0 和 1024 之间")
	public String getScopeOfBusiness() {
		return scopeOfBusiness;
	}

	public void setScopeOfBusiness(String scopeOfBusiness) {
		this.scopeOfBusiness = scopeOfBusiness;
	}
	@ExcelField(title = "官网 网址", align = 2, sort = 60)
	@Length(min=0, max=225, message="官网地址长度必须介于 0 和 225 之间")
	public String getOfficialWebAddress() {
		return officialWebAddress;
	}

	public void setOfficialWebAddress(String officialWebAddress) {
		this.officialWebAddress = officialWebAddress;
	}

	@Length(min=0, max=50, message="统一社会信用代码长度必须介于 0 和 50 之间")
	public String getUnifiedCode() {
		return unifiedCode;
	}

	public void setUnifiedCode(String unifiedCode) {
		this.unifiedCode = unifiedCode;
	}
	@ExcelField(title = "官网 网址", align = 2, sort = 100)
	@Length(min=0, max=50, message="传真长度必须介于 0 和 50 之间")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Length(min=0, max=10, message="邮编长度必须介于 0 和 10 之间")
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	@ExcelField(title = "机构有效截止日期", align = 2, sort = 70)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFirstRegistration() {
		return firstRegistration;
	}

	public void setFirstRegistration(Date firstRegistration) {
		this.firstRegistration = firstRegistration;
	}
	
	@Length(min=0, max=200, message="颁证机关长度必须介于 0 和 200 之间")
	public String getCertificateAuthority() {
		return certificateAuthority;
	}

	public void setCertificateAuthority(String certificateAuthority) {
		this.certificateAuthority = certificateAuthority;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCertificateDate() {
		return certificateDate;
	}

	public void setCertificateDate(Date certificateDate) {
		this.certificateDate = certificateDate;
	}
	
	@Length(min=0, max=200, message="委托管理部门长度必须介于 0 和 200 之间")
	public String getEntrustManagement() {
		return entrustManagement;
	}

	public void setEntrustManagement(String entrustManagement) {
		this.entrustManagement = entrustManagement;
	}
	
	@Length(min=0, max=200, message="登记管理部门名称长度必须介于 0 和 200 之间")
	public String getRegisteredAuthority() {
		return registeredAuthority;
	}

	public void setRegisteredAuthority(String registeredAuthority) {
		this.registeredAuthority = registeredAuthority;
	}
	
	@Length(min=0, max=10, message="机构性质长度必须介于 0 和 10 之间")
	public String getInstitutionalNature() {
		return institutionalNature;
	}

	public void setInstitutionalNature(String institutionalNature) {
		this.institutionalNature = institutionalNature;
	}
	
	@Length(min=0, max=200, message="法人性别长度必须介于 0 和 200 之间")
	public String getLegalPersonSex() {
		return legalPersonSex;
	}

	public void setLegalPersonSex(String legalPersonSex) {
		this.legalPersonSex = legalPersonSex;
	}
	@ExcelField(title = "法人手机号码", align = 2, sort = 90)
	@Length(min=0, max=20, message="法人手机号码长度必须介于 0 和 20 之间")
	public String getLegalPersonMobile() {
		return legalPersonMobile;
	}

	public void setLegalPersonMobile(String legalPersonMobile) {
		this.legalPersonMobile = legalPersonMobile;
	}
	@ExcelField(title = "机构负责人", align = 2, sort = 40)
	@Length(min=0, max=200, message="机构负责人姓名长度必须介于 0 和 200 之间")
	public String getChargePerson() {
		return chargePerson;
	}

	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}
	
	@Length(min=0, max=1, message="机构负责人性别长度必须介于 0 和 1 之间")
	public String getChargePersonSex() {
		return chargePersonSex;
	}

	public void setChargePersonSex(String chargePersonSex) {
		this.chargePersonSex = chargePersonSex;
	}
	@ExcelField(title = "负责人手机号码", align = 2, sort = 75)
	@Length(min=0, max=20, message="负责人手机号码长度必须介于 0 和 20 之间")
	public String getChargePersonMobile() {
		return chargePersonMobile;
	}

	public void setChargePersonMobile(String chargePersonMobile) {
		this.chargePersonMobile = chargePersonMobile;
	}
}