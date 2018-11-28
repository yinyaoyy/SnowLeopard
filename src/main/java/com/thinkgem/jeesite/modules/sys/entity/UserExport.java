/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import java.util.Date;
import java.util.List;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.common.utils.excel.fieldtype.RoleListType;

/**
 * 用户Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserExport extends DataEntity<UserExport> {

	private static final long serialVersionUID = 1L;
	private String no;		// 工号
	private String name;	// 姓名
	private String sex;		// 性别
	private String mobile;	// 手机
	private String papernum;//身份证号
	private Office office;	// 归属科室
	private Office company;
	private String identityType;		// 身份类别（公务员，参照公务员、事业单位工作人员、工勤人员、混岗、聘用人员、其他）
	private String prepareType;		// 编制类型(司法行政专项编制、地方行政编制、事业编制、工勤编制、不占编)
	private Date birthday;	// 生日
	private String ethnic;		// 民族
	private String nativePlace;		// 籍贯
	private String education;		// 学历
	private String degree;		// 学位
	private String major;		// 专业
	private String politicalFace;		// 政治面貌
	private String birthPlace;		// 出生地
	private Date joinTime;		// 入党（团）时间
	private String usedName;		// 曾用名
	private Date startworkTime;		// 开始工作时间
	private String isSeries;		// 在编情况
	private Date publicOfficialsRegistrationTime;		// 公职人员登记时间
	private Date graduationTime;		// 毕业时间
	private String schoolInfo;		// 学校信息
	private String professionCategory;		// 专业类别
	private String isHigh;		// 是否高配
	private String contribution;		// 记功情况
	private String jobCategory;		// 职务类别(领导职务、非领导职务)
	private String jobLevel;		// 职务级别(县处级正职、县处级副职、乡科级正职、乡科级副职、科员、试用期人员、工勤人员、事业管理岗、事业技术岗)
	private String userType;// 职位
	private Date nowWorktime;		// 现任职时间
	private String nowDocNumber;		// 现任职文号
	private String serveSamejobTime;		// 任同职级时间
	private String serveSamedocNumberTime;		// 任同职级文号
	private String salarySource;		// 工资来源
	private String personStatus;		// 人员状态(在岗、不在岗)
	private String columnMarking;		// 列编标志
	private Date columnDate;		// 列编日期
	private String columnChannel;		// 列编渠道
	private String columnDocNumber;		// 列编文号
	private String columnSource;		// 列编来源
	private String layoffsSign;		// 减员标志
	private Date layoffsTime;		// 减员日期
	private String layoffsChannel;		// 减员渠道
	private String layoffsDocNumber;		// 减员文号
	private String layoffsGo;		// 减员去向
	private String introduction;		// 个人简介
	private String rewardPunishment;		// 奖惩情况
	private String learnTraing;		// 学习培训情况
	private String pwd;
	private User u;
	private UserExpand ue; 
	private String email;	// 邮箱
	private String phone;	// 电话
	private String photo;	// 头像
	private String oldLoginName;// 原登录名
	private String loginName;// 登录名
	private String loginFlag;
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	private String userSourceType;//用户类型
	public UserExport(){super();}
	public User getU() {
		u = new User();
		u.setId(id);
		u.setBirthday(birthday);
		u.setName(name);
		u.setMobile(mobile);
		u.setOffice(office);
		u.setCompany(company);
		u.setUserType(userType);
		u.setNo(no);
		u.setPapernum(papernum);
		u.setPassword(pwd);
		u.setPhoto(photo);
		u.setEmail(email);
		u.setLoginName(loginName);
	    u.setCreateBy(createBy);
	    u.setCreateDate(createDate);
	    u.setUpdateBy(updateBy);
	    u.setUpdateDate(updateDate);
	    u.setRoleList(roleList);
	    u.setLoginFlag(loginFlag);
		return u;
	}
	public void setU(User u) {
		this.u = u;
	}
	public UserExpand getUe() {
		ue = new UserExpand();
		ue.setId(id);
		ue.setBirthPlace(birthPlace);
		ue.setColumnChannel(columnChannel);
		ue.setColumnDate(columnDate);
		ue.setColumnDocNumber(columnDocNumber);
		ue.setColumnMarking(columnMarking);
		ue.setColumnSource(columnSource);
		ue.setContribution(contribution);
		ue.setDegree(degree);
		ue.setEducation(education);
		ue.setEthnic(ethnic);
		ue.setGraduationTime(graduationTime);
		ue.setIdentityType(identityType);
		ue.setIntroduction(introduction);
		ue.setIsHigh(isHigh);
		ue.setIsSeries(isSeries);
		ue.setJobCategory(jobCategory);
		ue.setJobLevel(jobLevel);
		ue.setJoinTime(joinTime);
		ue.setLayoffsChannel(layoffsChannel);
		ue.setLayoffsDocNumber(layoffsDocNumber);
		ue.setLayoffsGo(layoffsGo);
		ue.setLayoffsSign(layoffsSign);
		ue.setLayoffsTime(layoffsTime);
		ue.setLearnTraing(learnTraing);
		ue.setMajor(major);
		ue.setNativePlace(nativePlace);
		ue.setNowDocNumber(nowDocNumber);
		ue.setNowWorktime(nowWorktime);
		ue.setPersonStatus(personStatus);
		ue.setPoliticalFace(politicalFace);
		ue.setPrepareType(prepareType);
		ue.setProfessionCategory(professionCategory);
		ue.setPublicOfficialsRegistrationTime(publicOfficialsRegistrationTime);
		ue.setRewardPunishment(rewardPunishment);
		ue.setSalarySource(salarySource);
		ue.setSchoolInfo(schoolInfo);
		ue.setServeSamedocNumberTime(serveSamedocNumberTime);
		ue.setServeSamejobTime(serveSamejobTime);
		ue.setSex(sex);
		ue.setStartworkTime(startworkTime);
		ue.setUsedName(usedName);
		return ue;
	}
	public UserExport(User u,UserExpand e){
		this.no = u.getNo();
		this.name = u.getName();
		this.sex = e.getSex();
		this.mobile = u.getMobile();
		this.papernum = u.getPapernum();
		this.office = u.getOffice();
		this.company = u.getCompany();
		this.identityType = e.getIdentityType();
		this.prepareType = e.getPrepareType();
		this.birthday = u.getBirthday();
		this.ethnic = e.getEthnic();
		this.nativePlace = e.getNativePlace();
		this.education = e.getEducation();
		this.degree = e.getDegree();
		this.major = e.getMajor();
		this.politicalFace = e.getPoliticalFace();
		this.birthPlace = e.getBirthPlace();
		this.joinTime = e.getJoinTime();
		this.usedName = e.getUsedName();
		this.startworkTime = e.getStartworkTime();
		this.isSeries = e.getIsSeries();
		this.publicOfficialsRegistrationTime = e.getPublicOfficialsRegistrationTime();
		this.graduationTime = e.getGraduationTime();
		this.schoolInfo = e.getSchoolInfo();
		this.professionCategory = e.getProfessionCategory();
		this.isHigh = e.getIsHigh();
		this.contribution = e.getContribution();
		this.jobCategory = e.getJobCategory();
		this.jobLevel = e.getJobLevel();
		this.userType = u.getUserType();
		this.nowWorktime = e.getNowWorktime();
		this.nowDocNumber = e.getNowDocNumber();
		this.serveSamejobTime = e.getServeSamejobTime();
		this.serveSamedocNumberTime = e.getServeSamedocNumberTime();
		this.salarySource = e.getSalarySource();
		this.personStatus = e.getPersonStatus();
		this.columnMarking = e.getColumnMarking();
		this.columnDate = e.getColumnDate();
		this.columnChannel = e.getColumnChannel();
		this.columnDocNumber = e.getColumnDocNumber();
		this.columnSource = e.getColumnSource();
		this.layoffsSign = e.getLayoffsSign();
		this.layoffsTime = e.getLayoffsTime();
		this.layoffsChannel = e.getLayoffsChannel();
		this.layoffsDocNumber = e.getLayoffsDocNumber();
		this.layoffsGo = e.getLayoffsGo();
		this.introduction = e.getIntroduction();
		this.rewardPunishment = e.getRewardPunishment();
		this.learnTraing = e.getLearnTraing();
		this.pwd = u.getPassword();
		this.u = u;
		this.ue = e;
		this.email = u.getEmail();
		this.phone = u.getMobile();
		this.photo = u.getPhoto();
		this.oldLoginName = u.getOldLoginName();
		this.loginName = u.getLoginName();
		this.loginFlag = u.getLoginFlag();
		this.roleList = u.getRoleList();
	}
	public String getLoginFlag() {
		return loginFlag;
	}
	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}
	@ExcelField(title="登录名", type=1,align=2, sort=80)
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public void setUe(UserExpand ue) {
		
		this.ue = ue;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getOldLoginName() {
		return oldLoginName;
	}
	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}
	@Length(min=0, max=100, message="用户类型长度必须介于 0 和 100 之间")
	@ExcelField(value="userType",title="职位", align=2, sort=280, dictType="job_category")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	@ExcelField(value="birthday",title="生日", align=2, sort=80)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@ExcelField(value="company.name",title="归属机构", align=2, sort=45)
	public Office getCompany() {
		return company;
	}
	public void setCompany(Office company) {
		this.company = company;
	}
	@ExcelField(value="office.name",title="归属科室", align=2, sort=50)
	public Office getOffice() {
		return office;
	}

	
	public void setOffice(Office office) {
		this.office = office;
	}
	@ExcelField(value="no",title="档案编号",align=2, sort=10)
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	@ExcelField(title="姓名",align=2, sort=20)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="毕业院校长度必须介于 0 和 255 之间")
	@ExcelField(value="schoolInfo",title="毕业院校", align=2, sort=220)
	public String getSchoolInfo() {
		return schoolInfo;
	}

	public void setSchoolInfo(String schoolInfo) {
		this.schoolInfo = schoolInfo;
	}
	
	@Length(min=0, max=255, message="主修专业长度必须介于 0 和 255 之间")
	@ExcelField(value="major",title="主修专业", align=2, sort=130)
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
	
	@Length(min=0, max=255, message="学历长度必须介于 0 和 255 之间")
	@ExcelField(value="education",title="学历", align=2, sort=110)
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	@ExcelField(value="sex",title="性别", align=2, sort=30,dictType="sex")
	@Length(min=0, max=2, message="性别长度必须介于 0 和 2 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@Length(min=0, max=200, message="手机长度必须介于 0 和 200 之间")
	@ExcelField(value="mobile",title="手机", align=2, sort=35)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Length(min=0, max=20, message="身份证号必须介于 0 和 20 之间")
	@ExcelField(value="papernum",title="身份证号", align=2, sort=40)
	public String getPapernum() {
		return papernum;
	}

	public void setPapernum(String papernum) {
		this.papernum = papernum;
	}
	@Length(min=0, max=100, message="身份类别（公务员，参照公务员、事业单位工作人员、工勤人员、混岗、聘用人员、其他）长度必须介于 0 和 100 之间")
	@ExcelField(value="identityType",title="身份类别", align=2, sort=60,dictType="identity_type")
	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	
	@Length(min=0, max=100, message="编制类型(司法行政专项编制、地方行政编制、事业编制、工勤编制、不占编)长度必须介于 0 和 100 之间")
	@ExcelField(value="prepareType",title="编制类型", align=2, sort=70,dictType="prepare_type")
	public String getPrepareType() {
		return prepareType;
	}

	public void setPrepareType(String prepareType) {
		this.prepareType = prepareType;
	}
	
	@Length(min=0, max=50, message="民族长度必须介于 0 和 50 之间")
	@ExcelField(value="ethnic",title="民族", align=2, sort=90,dictType="ethnic")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	
	@Length(min=0, max=100, message="籍贯长度必须介于 0 和 100 之间")
	@ExcelField(value="nativePlace",title="籍贯", align=2, sort=100)
	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	
	@Length(min=0, max=30, message="学位长度必须介于 0 和 30 之间")
	@ExcelField(value="degree",title="学位", align=2, sort=120)
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}
	
	@Length(min=0, max=30, message="政治面貌长度必须介于 0 和 30 之间")
	@ExcelField(value="politicalFace",title="政治面貌", align=2, sort=140)
	public String getPoliticalFace() {
		return politicalFace;
	}

	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}
	
	@Length(min=0, max=100, message="出生地长度必须介于 0 和 100 之间")
	@ExcelField(value="birthPlace",title="出生地", align=2, sort=150)
	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(value="joinTime",title="入党（团）时间", align=2, sort=160)
	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	
	@Length(min=0, max=100, message="曾用名长度必须介于 0 和 100 之间")
	@ExcelField(value="usedName",title="曾用名", align=2, sort=170)
	public String getUsedName() {
		return usedName;
	}

	public void setUsedName(String usedName) {
		this.usedName = usedName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(value="startworkTime",title="参加工作时间", align=2, sort=180)
	public Date getStartworkTime() {
		return startworkTime;
	}

	public void setStartworkTime(Date startworkTime) {
		this.startworkTime = startworkTime;
	}
	
	@Length(min=0, max=10, message="在编情况长度必须介于 0 和 2 之间")
	@ExcelField(value="isSeries",title="在编情况", align=2, sort=190)
	public String getIsSeries() {
		return isSeries;
	}

	public void setIsSeries(String isSeries) {
		this.isSeries = isSeries;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(value="publicOfficialsRegistrationTime",title="公务员登记时间", align=2, sort=200)
	public Date getPublicOfficialsRegistrationTime() {
		return publicOfficialsRegistrationTime;
	}

	public void setPublicOfficialsRegistrationTime(Date publicOfficialsRegistrationTime) {
		this.publicOfficialsRegistrationTime = publicOfficialsRegistrationTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(value="graduationTime",title="毕业时间", align=2, sort=210)
	public Date getGraduationTime() {
		return graduationTime;
	}

	public void setGraduationTime(Date graduationTime) {
		this.graduationTime = graduationTime;
	}
	
	@Length(min=0, max=50, message="专业类别长度必须介于 0 和 50 之间")
	@ExcelField(value="professionCategory",title="专业类别", align=2, sort=230)
	public String getProfessionCategory() {
		return professionCategory;
	}

	public void setProfessionCategory(String professionCategory) {
		this.professionCategory = professionCategory;
	}
	
	@Length(min=0, max=2, message="是否高配长度必须介于 0 和 2 之间")
	@ExcelField(value="isHigh",title="是否高配", align=2, sort=240,dictType="yes_no")
	public String getIsHigh() {
		return isHigh;
	}

	public void setIsHigh(String isHigh) {
		this.isHigh = isHigh;
	}
	
	@Length(min=0, max=300, message="记功情况长度必须介于 0 和 300 之间")
	@ExcelField(value="contribution",title="记功情况", align=2, sort=250)
	public String getContribution() {
		return contribution;
	}

	public void setContribution(String contribution) {
		this.contribution = contribution;
	}
	
	@Length(min=0, max=50, message="职务类别(领导职务、非领导职务)长度必须介于 0 和 50 之间")
	@ExcelField(value="jobCategory",title="职务类别", align=2, sort=260,dictType="job_category")
	public String getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}
	
	@Length(min=0, max=50, message="职务级别(县处级正职、县处级副职、乡科级正职、乡科级副职、科员、试用期人员、工勤人员、事业管理岗、事业技术岗)长度必须介于 0 和 50 之间")
	@ExcelField(value="jobLevel",title="职务级别", align=2, sort=270,dictType="job_level")
	public String getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(value="nowWorktime",title="任职时间", align=2, sort=290)
	public Date getNowWorktime() {
		return nowWorktime;
	}

	public void setNowWorktime(Date nowWorktime) {
		this.nowWorktime = nowWorktime;
	}
	
	
	@Length(min=0, max=100, message="现任职文号长度必须介于 0 和 100 之间")
	@ExcelField(value="nowDocNumber",title="现任职文号", align=2, sort=300)
	public String getNowDocNumber() {
		return nowDocNumber;
	}

	public void setNowDocNumber(String nowDocNumber) {
		this.nowDocNumber = nowDocNumber;
	}
	
	@Length(min=0, max=10, message="任同职级时间长度必须介于 0 和 10 之间")
	@ExcelField(value="serveSamejobTime",title="任同职级时间", align=2, sort=310)
	public String getServeSamejobTime() {
		return serveSamejobTime;
	}

	public void setServeSamejobTime(String serveSamejobTime) {
		this.serveSamejobTime = serveSamejobTime;
	}
	
	@Length(min=0, max=50, message="任同职级文号长度必须介于 0 和 50 之间")
	@ExcelField(value="serveSamedocNumberTime",title="任同职级文号", align=2, sort=320)
	public String getServeSamedocNumberTime() {
		return serveSamedocNumberTime;
	}

	public void setServeSamedocNumberTime(String serveSamedocNumberTime) {
		this.serveSamedocNumberTime = serveSamedocNumberTime;
	}
	
	@Length(min=0, max=100, message="工资来源长度必须介于 0 和 100 之间")
	@ExcelField(value="salarySource",title="工资来源", align=2, sort=330)
	public String getSalarySource() {
		return salarySource;
	}

	public void setSalarySource(String salarySource) {
		this.salarySource = salarySource;
	}
	
	@Length(min=0, max=10, message="人员状态(在岗、不在岗)长度必须介于 0 和 10 之间")
	@ExcelField(value="personStatus",title="人员状态", align=2, sort=340,dictType="person_status")
	public String getPersonStatus() {
		return personStatus;
	}

	public void setPersonStatus(String personStatus) {
		this.personStatus = personStatus;
	}
	
	
	@ExcelField(value="columnMarking",title="列编标志", align=2, sort=350)
	public String getColumnMarking() {
		return columnMarking;
	}

	public void setColumnMarking(String columnMarking) {
		this.columnMarking = columnMarking;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(value="columnDate",title="列编日期", align=2, sort=360)
	//@NotNull(message="列编日期不能为空")
	public Date getColumnDate() {
		return columnDate;
	}

	public void setColumnDate(Date columnDate) {
		this.columnDate = columnDate;
	}
	
	@Length(min=0, max=100, message="列编文号长度必须介于 1 和 100 之间")
	@ExcelField(value="columnDocNumber",title="列编文号", align=2, sort=380)
	//@NotNull(message="列编文号不能为空")
	public String getColumnDocNumber() {
		return columnDocNumber;
	}

	public void setColumnDocNumber(String columnDocNumber) {
		this.columnDocNumber = columnDocNumber;
	}
	
	@Length(min=0, max=100, message="列编渠道长度必须介于 1 和 100 之间")
	@ExcelField(value="columnChannel",title="列编渠道", align=2, sort=370)
	//@NotNull(message="列编渠道不能为空")
	public String getColumnChannel() {
		return columnChannel;
	}

	public void setColumnChannel(String columnChannel) {
		this.columnChannel = columnChannel;
	}
	
	@Length(min=0, max=50, message="减员标志长度必须介于 0 和 50 之间")
	@ExcelField(value="layoffsSign",title="减员标志", align=2, sort=400)
	public String getLayoffsSign() {
		return layoffsSign;
	}

	public void setLayoffsSign(String layoffsSign) {
		this.layoffsSign = layoffsSign;
	}
	
	@Length(min=0, max=100, message="列编来源长度必须介于 1 和 100 之间")
	@ExcelField(value="columnSource",title="列编来源", align=2, sort=390)
	//@NotNull(message="列编来源不能为空")
	public String getColumnSource() {
		return columnSource;
	}

	public void setColumnSource(String columnSource) {
		this.columnSource = columnSource;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(value="layoffsTime",title="减员日期", align=2, sort=410)
	public Date getLayoffsTime() {
		return layoffsTime;
	}

	public void setLayoffsTime(Date layoffsTime) {
		this.layoffsTime = layoffsTime;
	}
	
	@Length(min=0, max=100, message="减员渠道长度必须介于 0 和 100 之间")
	@ExcelField(value="layoffsChannel",title="减员渠道", align=2, sort=420)
	public String getLayoffsChannel() {
		return layoffsChannel;
	}

	public void setLayoffsChannel(String layoffsChannel) {
		this.layoffsChannel = layoffsChannel;
	}
	
	@Length(min=0, max=100, message="减员文号长度必须介于 0 和 100 之间")
	@ExcelField(value="layoffsDocNumber",title="减员文号", align=2, sort=430)
	public String getLayoffsDocNumber() {
		return layoffsDocNumber;
	}

	public void setLayoffsDocNumber(String layoffsDocNumber) {
		this.layoffsDocNumber = layoffsDocNumber;
	}
	
	@Length(min=0, max=100, message="减员去向长度必须介于 0 和 100 之间")
	@ExcelField(value="layoffsGo",title="减员去向", align=2, sort=440)
	public String getLayoffsGo() {
		return layoffsGo;
	}

	public void setLayoffsGo(String layoffsGo) {
		this.layoffsGo = layoffsGo;
	}
	
	@Length(min=0, max=1000, message="个人简介长度必须介于 0 和 1000 之间")
	@ExcelField(value="introduction",title="个人简介", align=2, sort=450)
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	@Length(min=0, max=500, message="奖惩情况长度必须介于 0 和 500 之间")
	@ExcelField(value="rewardPunishment",title="奖惩情况", align=2, sort=460)
	public String getRewardPunishment() {
		return rewardPunishment;
	}

	public void setRewardPunishment(String rewardPunishment) {
		this.rewardPunishment = rewardPunishment;
	}
	
	@Length(min=0, max=500, message="学习培训情况长度必须介于 0 和 500 之间")
	@ExcelField(value="learnTraing",title="学习培训情况", align=2, sort=470)
	public String getLearnTraing() {
		return learnTraing;
	}

	public void setLearnTraing(String learnTraing) {
		this.learnTraing = learnTraing;
	}
	@ExcelField(title="拥有角色", align=2, sort=500, fieldType=RoleListType.class)
	
	public List<Role> getRoleList() {
		return roleList;
	}
	
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public String getUserSourceType() {
		return userSourceType;
	}
	public void setUserSourceType(String userSourceType) {
		this.userSourceType = userSourceType;
	}

}