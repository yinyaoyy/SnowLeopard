/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.info.service.NotaryAgencyService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 公证机构Entity
 * @author 王鹏
 * @version 2018-04-23
 */
public class NotaryAgency extends DataEntity<NotaryAgency> {
	private static NotaryAgencyService notaryAgencyService = SpringContextHolder.getBean(NotaryAgencyService.class);
	private static final long serialVersionUID = 1L;
	private String name;		// 公证处名称
	private Date practisingTime;		// 执业时间
	private String expertise;		// 业务专长
	private String licenseNumber;		// 执业证号
	private String address;		// 公证处地址
	private String telephone;		// 联系电话
	private String serviceTime;		// 服务时间
	private String manager;		// 负责人
	private String licenseStatus;		// 执业状态
	private String organizationCode;		// 组织机构代码证
	private String approvalNumber;		// 批准文号
	private Date approvalDate;		// 批准日期
	private Office office;		// 主营机关
	private String faxNumber;		// 传真号码
	private String website;		// 官网地址
	private String coordinate; //经纬度，经度在前
	private String imageUrl;		// 图片地址
	private Area area;		// 所在地区
	private String introduction;		// 公证处简介
	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public NotaryAgency() {
		super();
	}

	public NotaryAgency(String id){
		super(id);
	}
	//为接口使用方便
	public NotaryAgency(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area(areaId);
	}
	
	@ExcelField(title="公证处名称", type=0, align=1, sort=10)
	@Length(min=1, max=200, message="公证处名称长度必须介于 1 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="执业时间", type=0, align=1, sort=40)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPractisingTime() {
		return practisingTime;
	}

	public void setPractisingTime(Date practisingTime) {
		this.practisingTime = practisingTime;
	}
	
	@Length(min=0, max=600, message="业务专长长度必须介于 0 和 600 之间")
	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
	
	@ExcelField(title="执业证号", type=0, align=1, sort=30)
	@Length(min=1, max=30, message="执业证号长度必须介于 1 和 30 之间")
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	
	@ExcelField(title="公证处地址", type=0, align=1, sort=50)
	@Length(min=0, max=500, message="公证处地址长度必须介于 0 和 500 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="联系电话", type=0, align=1, sort=60)
	@Length(min=0, max=20, message="联系电话长度必须介于 0 和 20 之间")
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@ExcelField(title="服务时间", type=0, align=1, sort=120)
	@Length(min=0, max=20, message="服务时间长度必须介于 0 和 20 之间")
	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	@ExcelField(title="负责人", type=0, align=1, sort=20)
	@Length(min=1, max=200, message="负责人长度必须介于 1 和 200 之间")
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	@ExcelField(title="执业状态", type=0, align=1, sort=70 , dictType="lawyer_license_status")
	@Length(min=0, max=1, message="执业状态长度必须介于 0 和 1 之间")
	public String getLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
	}
	
	@ExcelField(title="组织机构代码", type=0, align=1, sort=80)
	@Length(min=1, max=30, message="组织机构代码证长度必须介于 1 和 30 之间")
	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	
	@ExcelField(title="批准文号", type=0, align=1, sort=90)
	@Length(min=0, max=30, message="批准文号长度必须介于 0 和 30 之间")
	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	
	@ExcelField(title="批准日期", type=0, align=1, sort=100)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	
	@ExcelField(value="office.name",title="主营机关", type=0, align=1, sort=110)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="传真号码", type=0, align=1, sort=150)
	@Length(min=0, max=20, message="传真号码长度必须介于 0 和 20 之间")
	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	
	@ExcelField(title="官网地址", type=0, align=1, sort=160)
	@Length(min=0, max=200, message="官网地址长度必须介于 0 和 200 之间")
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@ExcelField(value="area.name",title="所在地区", type=0, align=1, sort=130)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@ExcelField(title="坐标", type=0, align=1, sort=140)
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		for (NotaryAgency notaryAgency : notaryAgencyService.findList(new NotaryAgency(val, ""))) {
			if (StringUtils.trimToEmpty(val).equals(notaryAgency.getName())){
				return notaryAgency;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((NotaryAgency)val).getName() != null){
			return ((NotaryAgency)val).getName();
		}
		return "";
	}
	
}