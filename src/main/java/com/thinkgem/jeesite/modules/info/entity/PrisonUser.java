/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 在监服刑人员Entity
 * @author liujiangling
 * @version 2018-06-21
 */
public class PrisonUser extends DataEntity<PrisonUser> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String beforeName;		// 曾用名
	private String sex;		// 性别
	private String age ;   //年龄
	private String idCard;		// 身份证号
	private Date birthday;		// 出生年月
	private String ethnic;		// 民族
	private String education;		// 学历
	private Area area;		// area_id
	private String address;		// 地址
	private String ruralCadastre;		// 是否农村籍
	private String checkState;		// 核查状态
	private String receipt;		// 是否有回执
	private String prisonName;		// 所在监所
	private String image;		// 是否有照片
	private Date enteringDate;		// 录入时间
	private Date checkDate;		// 核查时间
	private String remarks;		// 是否有照片
	
	public PrisonUser() {
		super();
	}

	public PrisonUser(String id){
		super(id);
	}

	public PrisonUser(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}
	
	@ExcelField(title="姓名", type=0, align=1, sort=10)
	@Length(min=0, max=20, message="姓名长度必须介于 0 和 20 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="曾用名", type=0, align=1, sort=20)
	@Length(min=0, max=20, message="曾用名长度必须介于 0 和 20 之间")
	public String getBeforeName() {
		return beforeName;
	}

	public void setBeforeName(String beforeName) {
		this.beforeName = beforeName;
	}
	
	@ExcelField(title="性别", type=0, align=1, sort=30, dictType="sex")
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="身份证号", type=0, align=1, sort=40)
	@Length(min=0, max=20, message="身份证号长度必须介于 0 和 20 之间")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
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
	
	@ExcelField(title="民族", type=0, align=1, sort=60, dictType="ethnic")
	@Length(min=0, max=2, message="民族长度必须介于 0 和 2 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	
	@ExcelField(title="文化水平", type=0, align=1, sort=70)
	@Length(min=0, max=30, message="学历长度必须介于 0 和 30 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	
	@ExcelField(value="area.name",title="所在地区", type=0, align=1, sort=170)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@ExcelField(title="户籍地址", type=0, align=1, sort=90)
	@Length(min=0, max=200, message="地址长度必须介于 0 和 200 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="是否农村籍", type=0, align=1, sort=80, dictType="yes_no")
	@Length(min=0, max=1, message="是否农村籍长度必须介于 0 和 1 之间")
	public String getRuralCadastre() {
		return ruralCadastre;
	}

	public void setRuralCadastre(String ruralCadastre) {
		this.ruralCadastre = ruralCadastre;
	}
	
	@ExcelField(title="核查状态", type=0, align=1, sort=100, dictType="check_state")
	@Length(min=0, max=1, message="核查状态长度必须介于 0 和 1 之间")
	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	
	@ExcelField(title="是否有回执", type=0, align=1, sort=120, dictType="cms_guestbook_type_inquiries")
	@Length(min=0, max=1, message="是否有回执长度必须介于 0 和 1 之间")
	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	
	@ExcelField(title="所在监所", type=0, align=1, sort=130)
	@Length(min=0, max=50, message="所在监所长度必须介于 0 和 50 之间")
	public String getPrisonName() {
		return prisonName;
	}

	public void setPrisonName(String prisonName) {
		this.prisonName = prisonName;
	}
	
	@ExcelField(title="是否有照片", type=0, align=1, sort=140, dictType="cms_guestbook_type_inquiries")
	@Length(min=0, max=1, message="是否有照片长度必须介于 0 和 1 之间")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@ExcelField(title="录入时间", type=0, align=1, sort=150)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getEnteringDate() {
		return enteringDate;
	}

	public void setEnteringDate(Date enteringDate) {
		this.enteringDate = enteringDate;
	}
	
	@ExcelField(title="核查时间", type=0, align=1, sort=160)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="备注", type=0, align=1, sort=110)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

}