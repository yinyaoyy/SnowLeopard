/**
 * 
 */
package com.thinkgem.jeesite.api.dto.vo;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * @author 王鹏
 * @version 2018-04-18 10:10:16
 */
@JsonInclude
public class AgencyVo extends DataEntity<AgencyVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4086562589992198765L;
	private String categoryId;//机构分类sys_office_category
	private Area area=new Area();//区域id/名称 
	private Area town=new Area();//乡镇id/名称 
	private String agencyId="暂无";//机构或人员的id
	private String agencyName;//机构名称
	private String agencyAddress="暂无";//机构地址
	private String agencyPhone="暂无";//机构电话
	private String personName;//人员名称（或者负责人名称）
	private String imageUrl;//链接地址
	private String coordinate="暂无";//坐标地址
	private Integer hasPerson = -1;//包含人数统计(为-1就是下面没有人员)
	private int  evaluate;//人员评价值
	private String id="暂无";//在机构表或者人员表中的id
	private String no="暂无";//人员工号或者机构证号
	private String type="暂无";//类型,职业类型
	private String zipCode="暂无";		// 邮编
	private String email;//电子邮箱
	private String introduction="暂无";		// 机构简介
	private String sex="暂无";//性别
	private String ethnic="暂无";		// 民族
	private String education="暂无";		// 学历
	private String age;		// 年龄
	private String politicalFace="暂无";		// 政治面貌
	private String practisingYear="暂无";//执业年限(根据执业时间自动计算)
	private String roleId="暂无";		// 职务
	private String worktime="暂无";		// 办公时间
	private String teamSize="暂无";		// 团队规模（人员数量）
	private Date birthday;  // 生日
	private String isMongolian="暂无";//精通蒙汉双语 0是1否
	private String licenseForm="暂无";//组织形式
	private String businessExpertise="暂无";//业务专长
	private String mediatorType;//金牌银牌
	private String idCard;  //身份证号
	private String fax;//传真
	private String scopeOfBussess;//业务范围
	private Date practisingTime;//开始执业时间
	private String mainOrgans;//主营机关
	private String status;//执业状态   1正常2停止
	private String isAidLawyer="暂无";//是否是法援律师
	private String evaluation="";
	private String officeId;//机构di
	public String getIsAidLawyer() {
		if(StringUtils.isBlank(isAidLawyer)){
			isAidLawyer="暂无";
		}
		return isAidLawyer;
	}
	public void setIsAidLawyer(String isAidLawyer) {
		this.isAidLawyer = isAidLawyer;
	}
	public String getEmail() {
		if(StringUtils.isBlank(email)){
			email="暂无";
		}
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		if(StringUtils.isBlank(status)){
			status="暂无";
		}
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMainOrgans() {
		if(StringUtils.isBlank(isAidLawyer)){
			isAidLawyer="暂无";
		}
		return mainOrgans;
	}
	public void setMainOrgans(String mainOrgans) {
		this.mainOrgans = mainOrgans;
	}
	public Date getPractisingTime() {
		return practisingTime;
	}
	public void setPractisingTime(Date practisingTime) {
		this.practisingTime = practisingTime;
		 String time= String.valueOf(DateUtils.getSubtraction(practisingTime));
		 if(!"0".equals(time)){
	    this.practisingYear = time;
		 }
	}
	public String getScopeOfBussess() {
		if(StringUtils.isBlank(scopeOfBussess)){
			scopeOfBussess="暂无";
		}
		return scopeOfBussess;
	}
	public void setScopeOfBussess(String scopeOfBussess) {
		this.scopeOfBussess = scopeOfBussess;
	}
	public String getFax() {
		if(StringUtils.isBlank(fax)){
			fax="暂无";
		}
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		Map<String,String> map = DateUtils.getBirAgeSex(idCard);
		this.age = map.get("age");
		Date birthday = DateUtils.getBirthday(idCard);
		this.birthday = birthday; 
		this.idCard = idCard;
	}
	public AgencyVo() {
		
	}
	//为接口使用方便
	public AgencyVo(String name, String areaId) {
		super();
		this.agencyName = name;
		this.area = new Area();
		this.area.setId(areaId);
	}
	//为接口使用方便
		public AgencyVo(String name, String areaId,String townId) {
			super();
			this.agencyName = name;
			this.evaluation = townId;
			this.isAidLawyer = townId;
			this.area = new Area();
			this.area.setId(areaId);
			this.town = new Area();
			this.town.setId(townId);
		}
	//为接口使用方便
	public AgencyVo(String name, String areaId,String townId,String isMongolian) {
		super();
		this.agencyName = name;
		this.isMongolian = isMongolian;
		this.evaluation = isMongolian;
		this.isAidLawyer = townId;
		this.area = new Area();
		this.area.setId(areaId);
		this.town = new Area();
		this.town.setId(townId);
	}
	//为接口使用方便
	public AgencyVo(String name, String areaId,String townId,String isMongolian,String evaluation) {
		super();
		this.agencyName = name;
		this.isMongolian = isMongolian;
		this.evaluation = evaluation;
		this.isAidLawyer = townId;
		this.area = new Area();
		this.area.setId(areaId);
		this.town = new Area();
		this.town.setId(townId);
	}
	//为接口使用方便
		public AgencyVo(String name, String areaId,String townId,String isMongolian,String evaluation,String officeId) {
			this.agencyName = name;
			this.isMongolian = isMongolian;
			this.evaluation = evaluation;
			this.isAidLawyer = townId;
			this.area = new Area();
			this.area.setId(areaId);
			this.town = new Area();
			this.town.setId(townId);
			this.officeId = officeId;
		}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("categoryId ="+categoryId +"//机构分类id\n");
		sb.append("area   ="+(area!=null?area.toString():"") +"//区域 \n");
		sb.append("agencyId ="+agencyId +"//机构/人员Id\n");
		sb.append("agencyName ="+agencyName +"//机构名称\n");
		sb.append("agencyAddress ="+agencyAddress +"//机构地址\n");
		sb.append("agencyPhone="+agencyPhone+"//机构电话\n");
		sb.append("personName ="+personName +"//人员名称\n");
		sb.append("imageUrl   ="+imageUrl   +"//链接地址\n");
		sb.append("coordinate ="+coordinate +"//坐标地址\n");
		sb.append("hasPerson ="+hasPerson +"//包含人数统计(为-1就是下面没有人员)\n");
		sb.append("zipCode ="+zipCode +"//邮编\n");
		sb.append("introduction ="+introduction +"//机构人员简介\n");
		return sb.toString();
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getAgencyAddress() {
		if(StringUtils.isBlank(agencyAddress)){
			agencyAddress="暂无";
		}
		return agencyAddress;
	}
	public void setAgencyAddress(String agencyAddress) {
		this.agencyAddress = agencyAddress;
	}
	public String getAgencyPhone() {
		if(StringUtils.isBlank(agencyPhone)){
			agencyPhone="暂无";
		}
		return agencyPhone;
	}
	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}
	public String getPersonName() {
		if(StringUtils.isBlank(personName)){
			personName="暂无";
		}
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	public Integer getHasPerson() {
		return hasPerson;
	}
	public void setHasPerson(Integer hasPerson) {
		this.hasPerson = hasPerson;
	}
	public int getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(int evaluate) {
		this.evaluate = evaluate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNo() {
		if(StringUtils.isBlank(no)){
			no="暂无";
		}
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getType() {
		if(StringUtils.isBlank(type)){
			type="暂无";
		}
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getZipCode() {
		if(StringUtils.isBlank(zipCode)){
			zipCode="暂无";
		}
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getIntroduction() {
		if(StringUtils.isBlank(introduction)){
			introduction="暂无";
		}
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getSex() {
		if(StringUtils.isBlank(sex)){
			sex="暂无";
		}
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEthnic() {
		if(StringUtils.isBlank(ethnic)){
			ethnic="暂无";
		}
		return ethnic;
	}
	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	public String getEducation() {
		if(StringUtils.isBlank(education)){
			education="暂无";
		}
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getAge() {
		
		return age;
	}
	public void setAge(String age) {
		if(StringUtils.isBlank(age)||Integer.parseInt(age)<18||Integer.parseInt(age)>80){
			age="暂无";
		}
		this.age = age;
		
	}
	public String getPoliticalFace() {
		if(StringUtils.isBlank(politicalFace)){
			politicalFace="暂无";
		}
		return politicalFace;
	}
	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}
	public String getPractisingYear() {
		if(StringUtils.isBlank(practisingYear)){
			practisingYear="暂无";
		}
		return practisingYear;
	}
	public void setPractisingYear(String practisingYear) {
		this.practisingYear = practisingYear;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getWorktime() {
		if(StringUtils.isBlank(worktime)){
			worktime="暂无";
		}
		return worktime;
	}
	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}
	public String getTeamSize() {
		if(StringUtils.isBlank(teamSize)){
			teamSize="暂无";
		}
		return teamSize;
	}
	public void setTeamSize(String teamSize) {
		this.teamSize = teamSize;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		 this.birthday = birthday;
		/* String birth=DateUtils.formatDateTime(birthday);
		 int age = DateUtils.getCarAge(birth);
		 this.age=String.valueOf(age);*/
		
	}
	public Area getTown() {
		return town;
	}
	public void setTown(Area town) {
		this.town = town;
	}
	public String getIsMongolian() {
		return isMongolian;
	}
	public void setIsMongolian(String isMongolian) {
		this.isMongolian = isMongolian;
	}
	public String getLicenseForm() {
		if(StringUtils.isBlank(licenseForm)){
			licenseForm="暂无";
		}
		return licenseForm;
	}
	public void setLicenseForm(String licenseForm) {
		this.licenseForm = licenseForm;
	}
	public String getBusinessExpertise() {
		if(StringUtils.isBlank(businessExpertise)){
			businessExpertise="暂无";
		}
		return businessExpertise;
	}
	public void setBusinessExpertise(String businessExpertise) {
		this.businessExpertise = businessExpertise;
	}
	public String getMediatorType() {
		if(StringUtils.isBlank(mediatorType)){
			mediatorType="暂无";
		}
		return mediatorType;
	}
	public void setMediatorType(String mediatorType) {
		this.mediatorType = mediatorType;
	}
	public String getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
}
