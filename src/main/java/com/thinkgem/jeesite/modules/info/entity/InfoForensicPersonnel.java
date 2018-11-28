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

import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 鉴定人员信息管理Entity
 * @author hejia
 * @version 2018-04-24
 */
public class InfoForensicPersonnel extends DataEntity<InfoForensicPersonnel> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String sex;		// 性别
	private Date birthday;		// 出生日期
	private String ethnic;		// 民族
	private String politicalFace;		// 政治面貌
	private String education;		// 学历
	private Date practisingTime;		// 入行年月，从业时间
	private String expertise;		// 业务专长
	private Office judicialAuthentication;		// 司法鉴定机构id
	private Area area;		// 所在地区
	private String licenseNumber;		// 执业证号
	private Date licenseExpiryTime;		// 证书过期时间
	private String phone;		// 联系电话
	private String address;		// 联系地址
	private String idCard;//身份证号
	private String imageUrl;		// 头像
	private String competentAuthoritiesName;		// 主管机关名称
	
	
	private String licenseHerf;		// 执业证书
	private String competentAuthoritiesId;		// 主管机关id
	private String isShow;		// 显示
	private String scopeOfBussess;//业务范围
	
    private String isMongolian;//蒙汉精通
    private String sysOfficeCategory;		// 机构人员分类
	private String siteId;		// 所属站点id
	private String sysServiceId;		// 所属pc端服务id
	private String appMenuId;		// 所属移动端服务id
	private String role = ""; //角色
    
    @ExcelField(title="姓名", type=0, align=1, sort=10)
    @Length(min=1, max=225, message="姓名长度必须介于 1 和 225 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="性别", type=0, align=1, sort=20,dictType="sex")
	@Length(min=1, max=1, message="性别长度必须介于 1 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@ExcelField(title="出生日期", type=0, align=1, sort=30)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@ExcelField(title="民族", type=0, align=1, sort=40,dictType="ethnic")
	@Length(min=0, max=10, message="名族长度必须介于 0 和 10 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	@ExcelField(title="政治面貌", type=0, align=1, sort=50)
	@Length(min=0, max=20, message="政治面貌长度必须介于 0 和 10 之间")
	public String getPoliticalFace() {
		return politicalFace;
	}
	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}
	@ExcelField(title="学历", type=0, align=1, sort=60)
	@Length(min=0, max=10, message="学历长度必须介于 0 和 10 之间")
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	
	@ExcelField(title="从业时间", type=0, align=1, sort=70)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPractisingTime() {
		return practisingTime;
	}
	public void setPractisingTime(Date practisingTime) {
		this.practisingTime = practisingTime;
	}
	
	@ExcelField(title="业务专长", type=0, align=1, sort=80)
	@Length(min=0, max=1024, message="业务专长长度必须介于 0 和 1024 之间")
	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
	
	@ExcelField(value="judicialAuthentication.name",title="所属鉴定机构", type=0, align=1, sort=90)
	public Office getJudicialAuthentication() {
		return judicialAuthentication;
	}
	public void setJudicialAuthentication(Office judicialAuthentication) {
		this.judicialAuthentication = judicialAuthentication;
	}

	@ExcelField(value="area.name",title="所在地区", type=0, align=1, sort=100)
	@NotNull(message="所在地区不能为空")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@ExcelField(title="执业证号", type=0, align=1, sort=110)
	@Length(min=1, max=225, message="执业证号长度必须介于 1 和 225 之间")
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	
	@ExcelField(title="证书过期时间", type=0, align=1, sort=120)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLicenseExpiryTime() {
		return licenseExpiryTime;
	}

	public void setLicenseExpiryTime(Date licenseExpiryTime) {
		
			this.licenseExpiryTime = licenseExpiryTime;
	}
	
	@ExcelField(title="电话", type=0, align=1, sort=130)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="机构地址", type=0, align=1, sort=140)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="身份证号", type=0, align=1, sort=150)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		if(StringUtils.isNoneBlank(idCard)){
			Map<String,String> map = DateUtils.getBirAgeSex(idCard);
			Date birthday = DateUtils.getBirthday(idCard);
			this.birthday = birthday; 
			this.sex = map.get("sexCode");
		}
		this.idCard = idCard;
	}
	
	//@ExcelField(title="图片路径", type=0, align=1, sort=160)
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	//@ExcelField(title="主管机关名称", type=0, align=1, sort=170)
	@Length(min=0, max=64, message="主管机关名称长度必须介于 0 和 64 之间")
	public String getCompetentAuthoritiesName() {
		return competentAuthoritiesName;
	}

	public void setCompetentAuthoritiesName(String competentAuthoritiesName) {
		this.competentAuthoritiesName = competentAuthoritiesName;
	}
	
	
	
	
	public String getIsMongolian() {
		return isMongolian;
	}

	public void setIsMongolian(String isMongolian) {
		this.isMongolian = isMongolian;
	}

	public String getScopeOfBussess() {
		return scopeOfBussess;
	}

	public void setScopeOfBussess(String scopeOfBussess) {
		this.scopeOfBussess = scopeOfBussess;
	}

	public InfoForensicPersonnel() {
		super();
	}

	public InfoForensicPersonnel(String id){
		super(id);
	}

	
	@Length(min=1, max=2, message="机构人员分类长度必须介于 1 和 2 之间")
	public String getSysOfficeCategory() {
		return sysOfficeCategory;
	}

	public void setSysOfficeCategory(String sysOfficeCategory) {
		this.sysOfficeCategory = sysOfficeCategory;
	}
	
	@Length(min=0, max=64, message="所属站点id长度必须介于 0 和 64 之间")
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	@Length(min=0, max=64, message="所属pc端服务id长度必须介于 0 和 64 之间")
	public String getSysServiceId() {
		return sysServiceId;
	}

	public void setSysServiceId(String sysServiceId) {
		this.sysServiceId = sysServiceId;
	}
	
	@Length(min=0, max=64, message="所属移动端服务id长度必须介于 0 和 64 之间")
	public String getAppMenuId() {
		return appMenuId;
	}

	public void setAppMenuId(String appMenuId) {
		this.appMenuId = appMenuId;
	}
	
	
	
	@Length(min=0, max=225, message="执业证书长度必须介于 0 和 225 之间")
	public String getLicenseHerf() {
		return licenseHerf;
	}

	public void setLicenseHerf(String licenseHerf) {
		this.licenseHerf = licenseHerf;
	}
	
	
	
	@Length(min=0, max=64, message="主管机关id长度必须介于 0 和 64 之间")
	public String getCompetentAuthoritiesId() {
		return competentAuthoritiesId;
	}

	public void setCompetentAuthoritiesId(String competentAuthoritiesId) {
		this.competentAuthoritiesId = competentAuthoritiesId;
	}
	
	
	
	@Length(min=0, max=1, message="显示长度必须介于 0 和 1 之间")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	@ExcelField(title="角色", align=2, sort=180, dictType="info_role_type")
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}