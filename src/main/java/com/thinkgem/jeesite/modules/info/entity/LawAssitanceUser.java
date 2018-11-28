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
 * 法援中心工作人员Entity
 * @author wanglin
 * @version 2018-04-22
 */
public class LawAssitanceUser extends DataEntity<LawAssitanceUser> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String sex;//性别
	private String age;		// 年龄
	private String phone;	// 联系电话
	private Date birthday;  // 出生日期
	private String idCard; //身份证号
	private String role;//职务
	private String nation;		// 民族
	private String isMongolian;//是否蒙语
	private Office office;//归属法援中心
	private String licenseNumber;	// 执业证号
	private Area area;//归属地区
	private String image;		// 图片路径
	private String introduction;// 个人简介
	
	
	private String address;		// 联系地址
	private String politicalFace;		// 政治面貌
	private String education;		// 学历
	private String manager;
	private String isAidLawyer;//是否是法援律师
	
	public LawAssitanceUser() {
		super();
	}

	public LawAssitanceUser(String id){
		super(id);
	}
	
	public LawAssitanceUser(String name, String areaId){
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}
	
	
	@ExcelField(title="名称", type=0, align=1, sort=10)
	@Length(min=0, max=200, message="名称长度必须介于 0 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="性别", type=0, align=1, sort=20,dictType="sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
		//String a=idCard.substring(16).substring(0, 1);
		/*if(idCard!=null && idCard.length()==18){
			if (Integer.parseInt(idCard.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
				sex = "2";
			} else {
				sex = "1";
			}
		}else{
			this.idCard="无";
		}*/
		/*if(sex!=null||!"".equals(sex)){
			
		}*/
		
		
	}
	@ExcelField(title="年龄", type=0, align=1, sort=30)
	@Length(min=0, max=4, message="年龄长度必须介于 0 和 4 之间")
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
	@ExcelField(title="电话", type=0, align=1, sort=40)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ExcelField(title="出生日期", type=0, align=1, sort=50)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
		this.age = String.valueOf(DateUtils.pastYear(birthday));
	}
	@ExcelField(title="身份证号", type=0, align=1, sort=60)
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		Map<String,String> map = DateUtils.getBirAgeSex(idCard);
		Date birthday = DateUtils.getBirthday(idCard);
		this.birthday = birthday; 
		//this.age = map.get("age");
		this.sex = map.get("sexCode");
		this.idCard = idCard;
		/*int age = DateUtils.getAge(idCard);
		if(age>=18&&age<=80){
		this.age = String.valueOf(age);
		}
		this.birthday=DateUtils.getBirthday(idCard);*/
	}
	@ExcelField(title="职务", align=1, sort=70,dictType="info_role_type")	
	public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
	
	@ExcelField(title="民族", type=0, align=1, sort=80, dictType="ethnic")
	@Length(min=0, max=50, message="民族长度必须介于 0 和 50 之间")
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	@ExcelField(title="是否蒙汉双通（是，否）", type=0, align=1, sort=90,dictType="yes_no")
	public String getIsMongolian() {
		return isMongolian;
	}
	public void setIsMongolian(String isMongolian) {
		this.isMongolian = isMongolian;
	}
	@ExcelField(value="office.name", title="归属法援中心(全称)", align=2, sort=100)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	@ExcelField(title="执业证号", type=0, align=1, sort=110)
	@Length(min=0, max=50, message="执业证号长度必须介于 0 和 50 之间")
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		if(licenseNumber==null || "".equals(licenseNumber)){
			this.licenseNumber = "暂无";
		   
		}else{
			this.licenseNumber = licenseNumber;
		}
	}
	@ExcelField(value="area.name", title="归属地区", align=2, sort=120)
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	@ExcelField(title="图片路径", type=0, align=1, sort=130)
	@Length(min=0, max=200, message="图片路径长度必须介于 0 和 200 之间")
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@ExcelField(title="个人简介", type=0, align=1, sort=140)
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	
	
	
	
	
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	

	

	

	public String getPoliticalFace() {
		return politicalFace;
	}

	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	@ExcelField(title="是否兼职法援律师", type=0, align=1, sort=20, dictType="yes_no")
	public String getIsAidLawyer() {
		return isAidLawyer;
	}

	public void setIsAidLawyer(String isAidLawyer) {
		this.isAidLawyer = isAidLawyer;
	}
	
}