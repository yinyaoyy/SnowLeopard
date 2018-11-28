/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 法援中心Entity
 * @author wanglin
 * @version 2018-04-22
 */
public class LawAssistance extends DataEntity<LawAssistance> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String manager;		// 负责人
	private String teamSize;		// 团队规模（人员数量）
	private String mobile;		// 电话
	private String worktime;		// 办公时间
	private String zipcode;		// 邮编
	
	private String address;		// 联系地址
	private Area area;//归属地区
	private String introduction;		// 机构简介
	private String img;		// 图片路径
	
	private String coordinate;		// 坐标(经纬度)
	private String phone;		// 联系电话
	
	
	public LawAssistance() {
		super();
	}

	public LawAssistance(String id){
		super(id);
	}	
	
	public LawAssistance(String name, String areaId){
		super();
		this.name = name;
		this.area = new Area(areaId);
	}
	@ExcelField(title="名称", type=0, align=1, sort=10)
	@Length(min=0, max=200, message="名称长度必须介于 0 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="负责人", type=0, align=1, sort=20)
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	@ExcelField(title="人员数量", type=0, align=1, sort=30)
	public String getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(String teamSize) {
		this.teamSize = teamSize;
	}
	@ExcelField(title="电话", type=0, align=1, sort=40)
	@Length(min=0, max=20, message="电话长度必须介于 0 和 20 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@ExcelField(title="办公时间", type=0, align=1, sort=50)
	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}
	
	@ExcelField(title="邮编", type=0, align=1, sort=60)
	@Length(min=0, max=10, message="邮编长度必须介于 0 和 10 之间")
	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	@ExcelField(title="联系地址", type=0, align=1, sort=70)
	@Length(min=0, max=100, message="联系地址长度必须介于 0 和 100 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
	@ExcelField(value="area.name", title="归属地区", align=2, sort=80)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	@ExcelField(title="机构简介", type=0, align=1, sort=90)
	@Length(min=0, max=500, message="机构简介长度必须介于 0 和 500 之间")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	@Length(min=0, max=200, message="图片路径长度必须介于 0 和 200 之间")
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	@Length(min=0, max=30, message="坐标(经纬度)长度必须介于 0 和 30 之间")
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	@Length(min=0, max=20, message="联系电话长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	
	
}