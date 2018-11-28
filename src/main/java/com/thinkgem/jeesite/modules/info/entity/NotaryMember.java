/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import java.util.Date;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 公证员Entity
 * @author 王鹏
 * @version 2018-04-23
 */
public class NotaryMember extends DataEntity<NotaryMember> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String sex;		// 性别
	private Date birthday;   // 生日(用于计算年龄)
	private String ethnic;		// 民族
	private String politicalFace;		// 政治面貌
	private String education;		// 学历
	private Date practisingTime;		// 执业时间
	private String expertise;		// 业务专长
	private Office notaryAgency;		// 执业机构
	private String licenseNumber;		// 执业证号
	private Office office;		// 主管机关
	private String idCard;		// 身份证号
	private String imageUrl;		// 照片
	private Area area;// 地区
	private String practisingYear;//执业年限(根据执业时间自动计算)
	private String isMongolian;   //蒙汉
	private String phone;		// 电话
	private String age;		// 年龄
	private String introduction;//个人简介
	private String role;
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}



	public String getIsMongolian() {
		return isMongolian;
	}

	public void setIsMongolian(String isMongolian) {
		this.isMongolian = isMongolian;
	}

	public NotaryMember() {
		super();
	}

	public NotaryMember(String id){
		super(id);
	}
	public NotaryMember(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}
	@ExcelField(title="姓名", align=1, sort=10)
	@Length(min=1, max=200, message="姓名长度必须介于 1 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	@ExcelField(title="性别", align=1, sort=20,dictType="sex")
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@ExcelField(title="出生日期", align=1, sort=30)
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
	@ExcelField(title="职务", align=1, sort=35,dictType="info_role_type")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@ExcelField(title="民族", align=1, sort=40,dictType="ethnic")
	@Length(min=0, max=30, message="民族长度必须介于 0 和 30 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		
		if(ethnic==null&&"".equals(ethnic)){
			this.ethnic="暂无";
		}else{
			this.ethnic = ethnic;
		}
	}
	@ExcelField(title="政治面貌", align=1, sort=50)
	@Length(min=0, max=30, message="政治面貌长度必须介于 0 和 30 之间")
	public String getPoliticalFace() {
		return politicalFace;
	}

	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}
	@ExcelField(title="学历", align=1, sort=60)
	@Length(min=0, max=30, message="学历长度必须介于 0 和 30 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	@ExcelField(title="执业时间", align=1, sort=70)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPractisingTime() {
		return practisingTime;
	}

	public void setPractisingTime(Date practisingTime) {
		this.practisingTime = practisingTime;
	}
	@ExcelField(title="业务专长", align=1, sort=80)
	@Length(min=0, max=600, message="业务专长长度必须介于 0 和 600 之间")
	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
	@ExcelField(value="notaryAgency.name",title="执业机构",align=1, sort=90)
	public Office getNotaryAgency() {
		return notaryAgency;
	}

	public void setNotaryAgency(Office notaryAgency) {
		this.notaryAgency = notaryAgency;
	}
	@ExcelField(title="执业证号", align=1, sort=100)
	@Length(min=1, max=30, message="执业证号长度必须介于 1 和 30 之间")
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		if(licenseNumber==null&&"".equals(licenseNumber)){
			this.licenseNumber="暂无";
		}else{
			this.licenseNumber = licenseNumber;
		}
	}
	@ExcelField(value="office.name",title="主管机关", align=1, sort=110)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	@ExcelField(value="idCard",title="身份证号", align=1, sort=120)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		Map<String,String> map = DateUtils.getBirAgeSex(idCard);
		this.age = map.get("age");
		Date birthday = DateUtils.getBirthday(idCard);
		this.birthday = birthday; 
		this.sex = map.get("sexCode");
		this.idCard = idCard;
	}
	@ExcelField(value="phone",title="电话", align=1, sort=130)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(value="imageUrl",title="照片路径", align=1, sort=140)
	@Length(min=0, max=300, message="照片长度必须介于 0 和 300 之间")
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getPractisingYear() {
		return practisingYear;
	}

	public void setPractisingYear(String practisingYear) {
		this.practisingYear = practisingYear;
	}
}