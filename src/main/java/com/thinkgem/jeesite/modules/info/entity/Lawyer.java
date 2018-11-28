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
 * 律师信息管理Entity
 * @author 王鹏
 * @version 2018-04-22
 */
public class Lawyer extends DataEntity<Lawyer> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 律师名称
	private String imageUrl;		// 图片地址
	private String sex;		// 性别
	private Date birthday;  // 生日
	private String age;		// 年龄
	private String ethnic;		// 民族
	private String politicalFace;		// 政治面貌
	private String education;		// 学历
	private Date practisingTime;		// 执业时间
	private String expertise;		// 业务专长
	private Office lawOffice;		// 执业机构id
	private String licenseNumber;		// 执业证号
	private String licenseType;		// 执业类别
	private String licenseStatus;		// 执业状态
	private Office office;		// 主营机关
	private Area area;// 地区
	private String practisingYear;//执业年限(根据执业时间自动计算)
	private String loginName;//登录账号
	private String idCard; //身份证号
	private String isMongolian;
	private String phone;
	private String introduction;		// 人员简介
	private String isAidLawyer="暂无";//是否是法援律师
	private String role = ""; //角色
	
	/**
	 * 不是存储的字典
	 * @return
	 */
	@ExcelField(title="是否法援律师", align=2, sort=85)
	public String getIsAidLawyer() {
		return isAidLawyer;
	}
	public void setIsAidLawyer(String isAidLawyer) {
		this.isAidLawyer = isAidLawyer;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	@ExcelField(title="电话", align=2, sort=75)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ExcelField(title="是否精通蒙汉双语", align=2, sort=70,dictType="yes_no")
	public String getIsMongolian() {
		return isMongolian;
	}
	public void setIsMongolian(String isMongolian) {
		this.isMongolian = isMongolian;
	}
	@ExcelField(title="身份证号", align=2, sort=80)
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
	public Lawyer() {
		super();
	}
	public Lawyer(String id){
		super(id);
	}
	public Lawyer(String name, String licenseType, String areaId) {
		super();
		this.name = name;
		this.licenseType = licenseType;
		this.area = new Area();
		this.area.setId(areaId);
	}


	//@ExcelField(title="登录账号", align=2, sort=70)
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@ExcelField(title="姓名", align=2, sort=5)
	@Length(min=1, max=200, message="律师名称长度必须介于 1 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="照片", align=2, sort=90)
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@ExcelField(title="性别", align=2, sort=10, dictType="sex")
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@ExcelField(title="生日", align=2, sort=15)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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

	@ExcelField(title="民族", align=2, sort=20,dictType="ethnic")
	@Length(min=0, max=30, message="民族长度必须介于 0 和 30 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}

	@ExcelField(title="政治面貌", align=2, sort=25)
	@Length(min=0, max=30, message="政治面貌长度必须介于 0 和 30 之间")
	public String getPoliticalFace() {
		return politicalFace;
	}

	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}

	@ExcelField(title="学历", align=2, sort=30)
	@Length(min=0, max=30, message="学历长度必须介于 0 和 30 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@ExcelField(title="执业时间",align=2, sort=35)
	@JsonFormat(pattern = "yyyy-MM-dd")
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
	//@ExcelField(title="执业年限", type=1, align=2, sort=36)
	public String getPractisingYear() {
		return practisingYear;
	}
	public void setPractisingYear(String practisingYear) {
		this.practisingYear = practisingYear;
	}

	@ExcelField(title="业务专长", align=1, sort=40)
	@Length(min=0, max=600, message="业务专长长度必须介于 0 和 600 之间")
	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	@ExcelField(value="lawOffice.name", title="执业机构",align=1, sort=45)
	public Office getLawOffice() {
		return lawOffice;
	}
	public void setLawOffice(Office lawOffice) {
		this.lawOffice = lawOffice;
	}

	@ExcelField(title="执业证号", align=2, sort=50)
	@Length(min=1, max=30, message="执业证号长度必须介于 1 和 30 之间")
	public String getLicenseNumber() {
		return licenseNumber;
	}

	
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	@ExcelField(title="执业类别", align=2, sort=55, dictType="lawyer_license_type")
	@Length(min=0, max=1, message="执业类别长度必须介于 0 和 1 之间")
	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	@ExcelField(title="执业状态", align=2, sort=60, dictType="lawyer_license_status")
	@Length(min=0, max=1, message="执业状态长度必须介于 0 和 1 之间")
	public String getLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
	}

	@ExcelField(value="office.name", title="主管机关", align=2, sort=65)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	@ExcelField(value="area.name", title="所属地区", align=2, sort=77)
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	
	@ExcelField(title="角色", align=2, sort=100, dictType="info_role_type")
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}