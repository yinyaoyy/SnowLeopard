/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 人民调解委员会Entity
 * @author wanglin
 * @version 2018-05-23
 */
public class PeopleMediationCommittee extends DataEntity<PeopleMediationCommittee> {
	
	private static final long serialVersionUID = 1L;
	
	private String name;		// 机构名称
	private String organizationForm;		// 管理体制
	private String chargeUser;		// 负责人
	private String chargeUserIdCard;
	private String abbreviation;		// 简称
	private String address="暂无";		// 机构地址
	private String licenseNumber="暂无";//执业证号

	private Office office;		// 主营机关
	private String zipCode="暂无";		// 邮编
	private String phone="暂无";		// 联系电话
	private String imageUrl;		// 图片地址
	private String faxNumber="暂无";		// 传真号码
	private String establishArea;		// 设立地点(市县)
	private Date establishTime;		// 成立时间
	private Area area;		// 所属地区
	private String introduction;		// 机构简介
	private String coordinate;		// 坐标(经纬度)
	private String administrativeDivision;		// 行政区划
//	private Area belongqixian;//新加
	private Area belongtowns;//新加
	
/*	@ExcelField(value="licenseNumber", title="机构证号", align=2, sort=5)
*/	
	@ExcelField(value="chargeUserIdCard", title="负责人身份证号", align=2, sort=160)
	public String getChargeUserIdCard() {
		return chargeUserIdCard;
	}
	public void setChargeUserIdCard(String chargeUserIdCard) {
		this.chargeUserIdCard = chargeUserIdCard;
	}
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	
	@ExcelField(value="chargeUser", title="机构负责人", align=2, sort=10)
	public void setChargeUser(String chargeUser) {
		this.chargeUser = chargeUser;
	}

	public String getChargeUser() {
		return chargeUser;
	}
	
	@ExcelField(value="name", title="机构名称", align=2, sort=20)
	@Length(min=1, max=400, message="机构名称长度必须介于 1 和 200 之间")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(value="address", title="机构地址", align=2, sort=30)
	@Length(min=0, max=500, message="机构地址长度必须介于 0 和 500 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@ExcelField(value="area.name", title="所属旗县", align=2, sort=40)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
//	@ExcelField(value="belongqixian", title="所属旗县", align=2, sort=40)
//	
//	public Area getBelongqixian() {
//		return belongqixian;
//	}
//
//	public void setBelongqixian(Area belongqixian) {
//		this.belongqixian = belongqixian;
//	}
	@ExcelField(value="belongtowns.name", title="所属乡镇（旗县以下的调解委员会需要表名所在乡镇或苏木）", align=2, sort=50)
	
	public Area getBelongtowns() {
		return belongtowns;
	}

	public void setBelongtowns(Area belongtowns) {
		this.belongtowns = belongtowns;
	}
	@ExcelField(value="office.name", title="主管机关", align=2, sort=60)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	@ExcelField(value="zipCode", title="邮编", align=2, sort=70)
	@Length(min=0, max=20, message="邮编长度必须介于 0 和 20 之间")
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@ExcelField(value="phone", title="电话", align=2, sort=80)
	@Length(min=0, max=20, message="联系电话长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ExcelField(value="faxNumber", title="传真", align=2, sort=90)
	@Length(min=0, max=20, message="传真号码长度必须介于 0 和 20 之间")
	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	
	@ExcelField(value="introduction", title="简介", align=2, sort=100)
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	@ExcelField(value="establishArea", title="设立地点", align=2, sort=110)
	@Length(min=1, max=64, message="设立地点长度必须介于 1 和 64 之间")
	public String getEstablishArea() {
		return establishArea;
	}

	public void setEstablishArea(String establishArea) {
		this.establishArea = establishArea;
	}
	@ExcelField(value="establishTime", title="成立时间", align=2, sort=120)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEstablishTime() {
		return establishTime;
	}

	public void setEstablishTime(Date establishTime) {
		this.establishTime = establishTime;
	}
	
	@ExcelField(value="administrativeDivision", title="行政区划", align=2, sort=130)
	@Length(min=0, max=6, message="行政区划长度必须介于 0 和 6 之间")
	public String getAdministrativeDivision() {
		return administrativeDivision;
	}

	public void setAdministrativeDivision(String administrativeDivision) {
		this.administrativeDivision = administrativeDivision;
	}

	
	public PeopleMediationCommittee() {
		super();
	}

	public PeopleMediationCommittee(String id){
		super(id);
	}
	@ExcelField(value="coordinate", title="经纬度（经度在前，维度在后，保留小数点后六位）", align=2, sort=135)
	@Length(min=0, max=30, message="坐标(经纬度)长度必须介于 0 和 30 之间")
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	/*@ExcelField(title="管理体制", align=1, sort=140)*/
	@Length(min=0, max=30, message="管理体制长度必须介于 0 和 30 之间")
	public String getOrganizationForm() {
		return organizationForm;
	}

	public void setOrganizationForm(String organizationForm) {
		this.organizationForm = organizationForm;
	}
	

	/*@ExcelField(value="chargeUser.name", title="机构负责人", align=2, sort=20)
	public User getChargeUser() {
		return chargeUser;
	}

	public void setChargeUser(User chargeUser) {
		this.chargeUser = chargeUser;
	}*/
	/*@ExcelField(value="abbreviation", title="机构简称", align=2, sort=150)*/
	@Length(min=0, max=20, message="简称长度必须介于 0 和 20 之间")
	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	
	
	
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	
	
	public PeopleMediationCommittee(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}
}