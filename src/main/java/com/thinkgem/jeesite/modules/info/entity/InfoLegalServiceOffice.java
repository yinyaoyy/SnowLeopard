/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.info.service.InfoLegalServiceOfficeService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 基层法律服务所Entity
 * @author hejia
 * @version 2018-04-24
 */
public class InfoLegalServiceOffice extends DataEntity<InfoLegalServiceOffice> {
	private static InfoLegalServiceOfficeService infoLegalServiceOfficeService = SpringContextHolder.getBean(InfoLegalServiceOfficeService.class);
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String personInCharge;		// 负责人
	private String personInChargeHerf;		// 负责人照片
	private String practiceLicense;		// 执业许可证
	private String practiceLicenseNum;		// 执业许可证号
	private Date practiceLicenseExpiryTime;		// 执业许可证过期时间
	private String occupationalState;		// 执业状态
	private String licenseNumber;		// 组织机构代码证编号
	private String licenseHerf;		// 组织机构代码证
	private String approvalNumber;		// 批准文号
	private Date approvalDate;		// 批准日期
	private String competentAuthoritiesId;		// 主管机关id
	private String competentAuthoritiesName;		// 主管机关名称
	private String address;		// 地址
	private String phone;		// 联系电话
	private String fax;		// 传真
	private String officialWebAddress;		// 官网地址
	private String worktime;		// 工作时间
	private String sort;		// 排序
	private String introduction;		// 机构简介
	private Area area;		// 所在地区
	private String logo;		// logo
	private String coordinate;		// 经纬度(经度在前)
	private String sysOfficeCategory;		// 机构人员分类
	private String siteId;		// 所属站点id
	private String sysServiceId;		// 所属pc端服务id
	private String appMenuId;		// 所属移动端服务id
	private String isShow;		// 显示
	private String legalPerson;//法人
	private String licenseForm; //组织形式
	private Area town; //乡镇
	private Date certificationTime; //颁证时间
	private	String administrativeDivision; //行政区划
	private String no;//机构证号
	private String businessExpertise; //业务专长
	private String zipCode; //邮编

	public InfoLegalServiceOffice() {
		super();
	}

	public InfoLegalServiceOffice(String id){
		super(id);
	}

	//为接口使用方便
	public InfoLegalServiceOffice(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		for (InfoLegalServiceOffice infoLegalServiceOffice : infoLegalServiceOfficeService.findList(new InfoLegalServiceOffice(val, ""))) {
			if (StringUtils.trimToEmpty(val).equals(infoLegalServiceOffice.getName())){
				return infoLegalServiceOffice;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((InfoLegalServiceOffice)val).getName() != null){
			return ((InfoLegalServiceOffice)val).getName();
		}
		return "";
	}
	@ExcelField(title="机构名称", type=0, align=1, sort=10)
	@Length(min=1, max=225, message="名称长度必须介于 1 和 225 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="负责人", type=0, align=1, sort=50)
	@Length(min=0, max=50, message="负责人长度必须介于 0 和 50 之间")
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
	
	@Length(min=0, max=225, message="执业许可证长度必须介于 0 和 225 之间")
	public String getPracticeLicense() {
		return practiceLicense;
	}

	public void setPracticeLicense(String practiceLicense) {
		this.practiceLicense = practiceLicense;
	}
	
	@Length(min=1, max=225, message="执业许可证号长度必须介于 1 和 225 之间")
	public String getPracticeLicenseNum() {
		return practiceLicenseNum;
	}

	public void setPracticeLicenseNum(String practiceLicenseNum) {
		this.practiceLicenseNum = practiceLicenseNum;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPracticeLicenseExpiryTime() {
		return practiceLicenseExpiryTime;
	}

	public void setPracticeLicenseExpiryTime(Date practiceLicenseExpiryTime) {
		this.practiceLicenseExpiryTime = practiceLicenseExpiryTime;
	}
	
	@Length(min=0, max=10, message="执业状态长度必须介于 0 和 10 之间")
	public String getOccupationalState() {
		return occupationalState;
	}

	public void setOccupationalState(String occupationalState) {
		this.occupationalState = occupationalState;
	}
	
	@Length(min=0, max=100, message="组织机构代码证编号长度必须介于 0 和 100 之间")
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	
	@Length(min=0, max=225, message="组织机构代码证长度必须介于 0 和 225 之间")
	public String getLicenseHerf() {
		return licenseHerf;
	}

	public void setLicenseHerf(String licenseHerf) {
		this.licenseHerf = licenseHerf;
	}
	
	@Length(min=0, max=100, message="批准文号长度必须介于 0 和 100 之间")
	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	
	@Length(min=0, max=64, message="主管机关id长度必须介于 0 和 64 之间")
	public String getCompetentAuthoritiesId() {
		return competentAuthoritiesId;
	}

	public void setCompetentAuthoritiesId(String competentAuthoritiesId) {
		this.competentAuthoritiesId = competentAuthoritiesId;
	}
	
	@Length(min=0, max=64, message="主管机关名称长度必须介于 0 和 64 之间")
	public String getCompetentAuthoritiesName() {
		return competentAuthoritiesName;
	}

	public void setCompetentAuthoritiesName(String competentAuthoritiesName) {
		this.competentAuthoritiesName = competentAuthoritiesName;
	}
	@ExcelField(title="地址", type=0, align=1, sort=70)
	@Length(min=0, max=225, message="地址长度必须介于 0 和 225 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@ExcelField(title="电话", type=0, align=1, sort=60)
	@Length(min=0, max=22, message="联系电话长度必须介于 0 和 22 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=22, message="传真长度必须介于 0 和 22 之间")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Length(min=0, max=225, message="官网地址长度必须介于 0 和 225 之间")
	public String getOfficialWebAddress() {
		return officialWebAddress;
	}

	public void setOfficialWebAddress(String officialWebAddress) {
		this.officialWebAddress = officialWebAddress;
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
	@ExcelField(title="机构简介", type=0, align=1, sort=100)
	@Length(min=0, max=2250, message="机构简介长度必须介于 0 和 2250 之间")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	@ExcelField(value="area.name",title="旗县", type=0, align=1, sort=30)
	@NotNull(message="所在地区不能为空")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@Length(min=0, max=225, message="logo长度必须介于 0 和 225 之间")
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@ExcelField(title="经纬度(经度在前)", type=0, align=1, sort=120)
	@Length(min=0, max=50, message="经纬度(经度在前)长度必须介于0 和 50 之间")
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	
	@Length(min=0, max=2, message="机构人员分类长度必须介于 0 和 2 之间")
	public String getSysOfficeCategory() {
		return sysOfficeCategory;
	}

	public void setSysOfficeCategory(String sysOfficeCategory) {
		this.sysOfficeCategory = sysOfficeCategory;
	}
	
	@Length(min=0, max=64, message="所属站点id长度必须介于 0 和 64 之间")
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	@Length(min=0, max=64, message="所属pc端服务id长度必须介于 0 和 64 之间")
	public String getSysServiceId() {
		return sysServiceId;
	}

	public void setSysServiceId(String sysServiceId) {
		this.sysServiceId = sysServiceId;
	}
	
	@Length(min=0, max=64, message="所属移动端服务id长度必须介于 0 和 64 之间")
	public String getAppMenuId() {
		return appMenuId;
	}

	public void setAppMenuId(String appMenuId) {
		this.appMenuId = appMenuId;
	}
	
	@Length(min=0, max=1, message="显示长度必须介于 0 和 1 之间")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	
	@ExcelField(title="组织形式", type=0, align=1, sort=20)
	public String getLicenseForm() {
		return licenseForm;
	}

	public void setLicenseForm(String licenseForm) {
		this.licenseForm = licenseForm;
	}
	
	@ExcelField(value="town.name",title="乡镇", type=0, align=1, sort=40)
	public Area getTown() {
		return town;
	}

	public void setTown(Area town) {
		this.town = town;
	}
	@ExcelField(title="颁发时间(xxxx年-xx月-xx日)", type=0, align=1, sort=40)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getCertificationTime() {
		return certificationTime;
	}

	public void setCertificationTime(Date certificationTime) {
		this.certificationTime = certificationTime;
	}
	@ExcelField(title="行政区划", type=0, align=1, sort=110)
	@Length(min=1, max=6, message="行政区划长度必须介于 1 和 6 之间")
	public String getAdministrativeDivision() {
		return administrativeDivision;
	}

	public void setAdministrativeDivision(String administrativeDivision) {
		this.administrativeDivision = administrativeDivision;
	}
	
	@ExcelField(title="机构证号", type=0, align=1, sort=80)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	
	@ExcelField(title="业务专长", type=0, align=1, sort=90)
	public String getBusinessExpertise() {
		return businessExpertise;
	}

	public void setBusinessExpertise(String businessExpertise) {
		this.businessExpertise = businessExpertise;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}