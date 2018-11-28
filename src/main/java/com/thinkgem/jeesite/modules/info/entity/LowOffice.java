/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.info.service.LowOfficeService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 律师事务所Entity
 * @author 王鹏
 * @version 2018-04-18
 */
public class LowOffice extends DataEntity<LowOffice> {

	private static LowOfficeService lowOfficeService = SpringContextHolder.getBean(LowOfficeService.class);
	private static final long serialVersionUID = 1L;
	private String name;		// 律所名称
	private String address;		// 律所地址
	private String phone;		// 联系电话
	private String licenseNumber;		// 执业证号
	private String introduction;		// 机构简介
	private Area area;		// 所在地区
	private String imageUrl;		// 图片地址
	private String coordinate;		// 坐标(经纬度)
	private String organizationForm;		// 组织形式
	private String licenseStatus;		// 执业状态
	private String organizationCode;		// 组织机构代码证
	private Date approvalDate;		// 批准日期
	private String approvalNumber;		// 批准文号
	private Office office;		// 主营机关
	private String faxNumber;		// 传真号码
	private Date establishTime;		// 成立时间
	private String website;		// 律所网址
	private Date practisingTime;		// 执业时间
	private String practisingYear; //执业年限
	private String expertise;		// 业务专长
	private String manager;//负责人
	private String managerIdCard;
	private String zipCode;
	private String email;
	private String creditCode;
	
	
	public String getManagerIdCard() {
		return managerIdCard;
	}

	public void setManagerIdCard(String managerIdCard) {
		this.managerIdCard = managerIdCard;
	}

	@ExcelField(value="creditCode",title="统一社会信用代码", align=2, sort=105)
	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	@ExcelField(value="email",title="邮箱", align=2, sort=100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@ExcelField(value="zipCode",title="邮编", align=2, sort=95)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@ExcelField(value="manager",title="负责人", align=2, sort=90)
	@Length(min=0, max=50, message="负责人长度必须介于 0 和 50 之间")
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public LowOffice() {
		super();
	}

	public LowOffice(String id){
		super(id);
	}
	//为接口使用方便
	public LowOffice(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("name            ="+name             +" // 律所名称 \n");
		sb.append("address         ="+address          +" // 律所地址 \n");
		sb.append("phone           ="+phone            +" // 联系电话 \n");
		sb.append("licenseNumber   ="+licenseNumber    +" // 执业证号 \n");
		sb.append("introduction    ="+introduction     +" // 机构简介 \n");
		sb.append("area            ="+(area!=null?area.toString():"") +" // 所在地区 \n");
		sb.append("imageUrl        ="+imageUrl         +" // 图片地址 \n");
		sb.append("coordinate      ="+coordinate       +" // 坐标(经纬度) \n");
		sb.append("organizationForm="+organizationForm +" // 组织形式 \n");
		sb.append("licenseStatus   ="+licenseStatus    +" // 执业状态 \n");
		sb.append("organizationCode="+organizationCode +" // 组织机构代码证 \n");
		sb.append("approvalDate    ="+approvalDate     +" // 批准日期 \n");
		sb.append("approvalNumber  ="+approvalNumber   +" // 批准文号 \n");
		sb.append("office          ="+(office!=null?office.toString():"")+" // 主营机关 \n");
		sb.append("faxNumber       ="+faxNumber        +" // 传真号码 \n");
		sb.append("establishTime   ="+establishTime    +" // 成立时间 \n");
		sb.append("website         ="+website          +" // 律所网址 \n");
		sb.append("practisingTime  ="+practisingTime   +" // 执业时间 \n");
		sb.append("expertise       ="+expertise        +" // 业务专长 \n");
		return sb.toString();
	}

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		for (LowOffice lawOffice : lowOfficeService.findList(new LowOffice(val, ""))) {
			if (StringUtils.trimToEmpty(val).equals(lawOffice.getName())){
				return lawOffice;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((LowOffice)val).getName() != null){
			return ((LowOffice)val).getName();
		}
		return "";
	}
	@ExcelField(title="图片", align=2, sort=110)
	@Length(min=0, max=100, message="图片地址长度必须介于 0 和 100 之间")
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@ExcelField(title="律所名称", align=2, sort=5)
	@Length(min=1, max=200, message="律所名称长度必须介于 1 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="律所地址", align=2, sort=10)
	@Length(min=0, max=500, message="律所地址长度必须介于 0 和 500 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ExcelField(title="联系电话", align=2, sort=15)
	@Length(min=0, max=20, message="联系电话长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ExcelField(title="组织形式", align=2, sort=35)
	@Length(min=0, max=30, message="组织形式长度必须介于 0 和 30 之间")
	public String getOrganizationForm() {
		return organizationForm;
	}

	public void setOrganizationForm(String organizationForm) {
		this.organizationForm = organizationForm;
	}

	@ExcelField(title="执业状态", align=2, sort=40, dictType="lawyer_license_status")
	@Length(min=0, max=1, message="执业状态长度必须介于 0 和 1 之间")
	public String getLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
	}

	@ExcelField(title="执业证号", align=2, sort=20)
	@Length(min=1, max=30, message="执业证号长度必须介于 1 和 30 之间")
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	@ExcelField(title="机构简介", align=2, sort=25)
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@ExcelField(title="组织机构代码证", align=2, sort=45)
	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	@ExcelField(title="批准日期", align=2, sort=50)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	
	@ExcelField(title="批准文号", align=2, sort=55)
	@Length(min=0, max=30, message="批准文号长度必须介于 0 和 30 之间")
	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	@ExcelField(value="office.name", title="主管机关", align=2, sort=60)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=30, message="坐标(经纬度)长度必须介于 0 和 30 之间")
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	@ExcelField(title="传真号码", align=2, sort=65)
	@Length(min=0, max=20, message="传真号码长度必须介于 0 和 20 之间")
	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	@ExcelField(title="成立时间", align=2, sort=70)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEstablishTime() {
		return establishTime;
	}

	public void setEstablishTime(Date establishTime) {
		this.establishTime = establishTime;
	}

	@ExcelField(value="area.name", title="所在地区", align=2, sort=30)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@ExcelField(title="律所网址", align=2, sort=75)
	@Length(min=0, max=200, message="律所网址长度必须介于 0 和 200 之间")
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@ExcelField(title="执业时间", align=2, sort=80)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPractisingTime() {
		return practisingTime;
	}

	public void setPractisingTime(Date practisingTime) {
		this.practisingTime = practisingTime;
		if(practisingTime!=null) {
			long d1 = DateUtils.pastDays(practisingTime)/365;
			this.practisingYear = String.valueOf(d1+1);
		}
	}
	@ExcelField(title="执业年限",align=2, sort=80)
	public String getPractisingYear() {
		return practisingYear;
	}
	public void setPractisingYear(String practisingYear) {
		this.practisingYear = practisingYear;
	}

	@ExcelField(title="业务专长", align=2, sort=85)
	@Length(min=0, max=600, message="业务专长长度必须介于 0 和 600 之间")
	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
	
}