/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 司法所Entity
 * @author wanglin
 * @version 2018-06-08
 */
public class Judiciary extends DataEntity<Judiciary> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 司法所名称
	private Area area;		// 所属旗县
	private Area town;		// 所属乡镇
	private String coordinate;		// 坐标(经纬度)
	private String address;		// 地址
	private String chargeUser;		// 司法所所长
	private String phone;		// 联系电话
	private String teamSize;		// 工作人员数
	private String introduction;		// 司法所简介
	private String imgUrl;           //机构图片
	private String zipCode;  //司法所邮编

	public Judiciary() {
		super();
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Judiciary(String id){
		super(id);
	}
	//为接口使用方便
	public Judiciary(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}
	@ExcelField(title="名称", type=0, align=1, sort=10)
	@Length(min=0, max=50, message="司法所名称长度必须介于 0 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(value="area.name",title="归属旗县", type=0, align=1, sort=20)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	@ExcelField(value="town.name",title="归属乡镇", type=0, align=1, sort=30)
	public Area getTown() {
		return town;
	}

	public void setTown(Area town) {
		this.town = town;
	}
	@ExcelField(title="经纬度", type=0, align=1, sort=40)
	@Length(min=0, max=30, message="坐标(经纬度)长度必须介于 0 和 30 之间")
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	@ExcelField(title="地址", type=0, align=1, sort=50)
	@Length(min=0, max=200, message="地址长度必须介于 0 和 200 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@ExcelField(title="司法所所长", type=0, align=1, sort=60)
	@Length(min=0, max=20, message="司法所所长长度必须介于 0 和 20 之间")
	public String getChargeUser() {
		return chargeUser;
	}

	public void setChargeUser(String chargeUser) {
		this.chargeUser = chargeUser;
	}
	@ExcelField(title="联系电话", type=0, align=1, sort=70)
	@Length(min=0, max=20, message="联系电话长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ExcelField(title="工作人员数", type=0, align=1, sort=80)
	@Length(min=0, max=10, message="工作人员数长度必须介于 0 和 10 之间")
	public String getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(String teamSize) {
		this.teamSize = teamSize;
	}
	@ExcelField(title="司法所简介", type=0, align=1, sort=90)
	@Length(min=0, max=500, message="司法所简介长度必须介于 0 和 255 之间")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
}