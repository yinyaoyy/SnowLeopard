/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 人民调解员Entity
 * @author wanglin
 * @version 2018-05-25
 */
public class PeopleMediation extends DataEntity<PeopleMediation> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String sex;		// 性别
	private String phone;		// 电话
	private String ethnic;		// 民族
	private String idCard;		// 身份证号
	private String education;   // 学历
	private String mediatorType; //调解员类型
	private String roleId;		// 职务
	private Date startTime;		// 开始执业时间
	private String isPluralistic;		// 是否兼职
	private Office office;		// 所属调委会
	private String agencyNo;    //所在机构编号
	private String politicalFace;		// 政治面貌
	private Area area;		// area_id
	private String introduction;		// introduction
	private String imageUrl;		// 图片地址
	private String age;		// 年龄
	private Date birthday;		// 生日
	private String no;//工号
	private Area town;
	private String isMongolian;//精通蒙汉双语 0是1否

	public PeopleMediation() {
		super();
	}

	public PeopleMediation(String id){
		super(id);
	}
	@ExcelField(title="姓名", align=1, sort=10)
	@Length(min=1, max=200, message="姓名长度必须介于 1 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="性别",align=1, sort=20,dictType="sex")
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@ExcelField(title="手机",align=1, sort=30)
	@Length(min=0, max=20, message="手机长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ExcelField(title="民族",align=1, sort=40, dictType="ethnic")
	@Length(min=0, max=30, message="民族长度必须介于 0 和 30 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	@ExcelField(title="身份证号",align=1, sort=50)
	@Length(min=0, max=20, message="身份证号长度必须介于 0 和 20 之间")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		Map<String,String> map = DateUtils.getBirAgeSex(idCard);
		//this.age = map.get("age");
		Date birthday = DateUtils.getBirthday(idCard);
		this.birthday = birthday; 
		this.sex = map.get("sexCode");
		this.idCard = idCard;
	}
	@ExcelField(title="学历",align=1, sort=60)
	@Length(min=0, max=30, message="学历长度必须介于 0 和 30 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	/*@ExcelField(title="调解员类型",type=0, align=2, sort=65,dictType="mediator_type")*/
	@Length(min=0, max=30, message="调解员类型必须介于 0 和 30 之间")
	public String getMediatorType() {
		return mediatorType;
	}

	public void setMediatorType(String mediatorType) {
		this.mediatorType = mediatorType;
	}
	@ExcelField(title="调委会职务", align=1, sort=70)
	@Length(min=0, max=64, message="职务长度必须介于 0 和 64 之间")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	@ExcelField(title="开始执业时间", align=1, sort=80)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@ExcelField(title="是否兼职",type=0, align=1, sort=90 ,dictType="yes_no")
	@Length(min=0, max=4, message="是否兼职长度必须介于 0 和 4 之间")
	public String getIsPluralistic() {
		return isPluralistic;
	}

	public void setIsPluralistic(String isPluralistic) {
		this.isPluralistic = isPluralistic;
	}
	@ExcelField(value="office.name", title="所属人民调解委员会", align=2, sort=100)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	/*@ExcelField(title="机构编号",type=0, align=2, sort=105)*/
	@Length(min=0, max=30, message="机构编号必须介于 0 和 30 之间")
	public String getAgencyNo() {
		return agencyNo;
	}

	public void setAgencyNo(String agencyNo) {
		this.agencyNo = agencyNo;
	}

	@ExcelField(title="政治面貌",align=1, sort=110)
	@Length(min=0, max=30, message="政治面貌长度必须介于 0 和 30 之间")
	public String getPoliticalFace() {
		return politicalFace;
	}

	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}
	@ExcelField(value="area.name", title="所属行政区域", align=1, sort=120)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	@ExcelField(title="个人简介", align=1, sort=130)
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	@ExcelField(title="图片",align=1, sort=140)
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@Length(min=0, max=3, message="年龄长度必须介于 0 和 3 之间")
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
	}
	
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getIsMongolian() {
		return isMongolian;
	}

	public void setIsMongolian(String isMongolian) {
		this.isMongolian = isMongolian;
	}

	public PeopleMediation(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}

	public Area getTown() {
		return town;
	}

	public void setTown(Area town) {
		this.town = town;
	}
	
}