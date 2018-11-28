/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import java.util.Date;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;

/**
 * 用户的添加Entity
 * @author 尹垚
 * @version 2018-06-25
 */
public class UserExpand extends DataEntity<UserExpand> {
	
	private static final long serialVersionUID = 1L;
	private String qq;		// qq
	private String weChat;		// we_chat
	private String otherLinks;		// other_links
	private String schoolInfo;		// 毕业院校
	private String major;		// 主修专业
	private String education;		// 学历
	private String sex;		// 性别
	private String identityType;		// 身份类别（公务员，参照公务员、事业单位工作人员、工勤人员、混岗、聘用人员、其他）
	private String prepareType;		// 编制类型(司法行政专项编制、地方行政编制、事业编制、工勤编制、不占编)
	private String ethnic;		// 民族
	private String nativePlace;		// 籍贯
	private String degree;		// 学位
	private String politicalFace;		// 政治面貌
	private String birthPlace;		// 出生地
	private Date joinTime;		// 入党（团）时间
	private String usedName;		// 曾用名
	private Date startworkTime;		// 开始工作时间
	private String isSeries;		// 在编情况
	private Date publicOfficialsRegistrationTime;		// 公职人员登记时间
	private Date graduationTime;		// 毕业时间
	private String professionCategory;		// 专业类别
	private String isHigh;		// 是否高配
	private String contribution;		// 记功情况
	private String jobCategory;		// 职务类别(领导职务、非领导职务)
	private String jobLevel;		// 职务级别(县处级正职、县处级副职、乡科级正职、乡科级副职、科员、试用期人员、工勤人员、事业管理岗、事业技术岗)
	private Date nowWorktime;		// 现任职时间
	private String nowDocNumber;		// 现任职文号
	private String serveSamejobTime;		// 任同职级时间
	private String serveSamedocNumberTime;		// 任同职级文号
	private String salarySource;		// 工资来源
	private String personStatus;		// 人员状态(在岗、不在岗)
	private String columnMarking;		// 列编标志
	private Date columnDate;		// 列编日期
	private String columnDocNumber;		// 列编文号
	private String columnChannel;		// 列编渠道
	private String layoffsSign;		// 减员标志
	private String columnSource;		// 列编来源
	private Date layoffsTime;		// 减员日期
	private String layoffsChannel;		// 减员渠道
	private String layoffsDocNumber;		// 减员文号
	private String layoffsGo;		// 减员去向
	private String introduction;		// 个人简介
	private String rewardPunishment;		// 奖惩情况
	private String learnTraing;		// 学习培训情况
	private String loginName;// 登录名
	private String industryCategory;//行业类别
	private String userImg;//用户头像，不是登陆头像
	public String getIndustryCategory() {
		return industryCategory;
	}

	public void setIndustryCategory(String industryCategory) {
		this.industryCategory = industryCategory;
	}

	public UserExpand() {
		super();
	}

	public UserExpand(String id){
		super(id);
	}
	
	public void setIdCard(String idCard) {
		Map<String,String> map = DateUtils.getBirAgeSex(idCard);
		this.sex = map.get("sexCode"); 
	}

	@Length(min=0, max=20, message="qq长度必须介于 0 和 20 之间")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	
	@Length(min=0, max=255, message="we_chat长度必须介于 0 和 255 之间")
	public String getWeChat() {
		return weChat;
	}

	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}
	
	@Length(min=0, max=255, message="other_links长度必须介于 0 和 255 之间")
	public String getOtherLinks() {
		return otherLinks;
	}

	public void setOtherLinks(String otherLinks) {
		this.otherLinks = otherLinks;
	}
	
	@Length(min=0, max=255, message="毕业院校长度必须介于 0 和 255 之间")
	public String getSchoolInfo() {
		return schoolInfo;
	}

	public void setSchoolInfo(String schoolInfo) {
		this.schoolInfo = schoolInfo;
	}
	
	@Length(min=0, max=255, message="主修专业长度必须介于 0 和 255 之间")
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
	
	@Length(min=0, max=255, message="学历长度必须介于 0 和 255 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	
	@Length(min=0, max=2, message="性别长度必须介于 0 和 2 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=100, message="身份类别（公务员，参照公务员、事业单位工作人员、工勤人员、混岗、聘用人员、其他）长度必须介于 0 和 100 之间")
	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	
	@Length(min=0, max=100, message="编制类型(司法行政专项编制、地方行政编制、事业编制、工勤编制、不占编)长度必须介于 0 和 100 之间")
	public String getPrepareType() {
		return prepareType;
	}

	public void setPrepareType(String prepareType) {
		this.prepareType = prepareType;
	}
	
	@Length(min=0, max=50, message="民族长度必须介于 0 和 50 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	
	@Length(min=0, max=100, message="籍贯长度必须介于 0 和 100 之间")
	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	
	@Length(min=0, max=30, message="学位长度必须介于 0 和 30 之间")
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}
	
	@Length(min=0, max=30, message="政治面貌长度必须介于 0 和 30 之间")
	public String getPoliticalFace() {
		return politicalFace;
	}

	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}
	
	@Length(min=0, max=100, message="出生地长度必须介于 0 和 100 之间")
	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	
	@Length(min=0, max=100, message="曾用名长度必须介于 0 和 100 之间")
	public String getUsedName() {
		return usedName;
	}

	public void setUsedName(String usedName) {
		this.usedName = usedName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartworkTime() {
		return startworkTime;
	}

	public void setStartworkTime(Date startworkTime) {
		this.startworkTime = startworkTime;
	}
	
	@Length(min=0, max=2, message="在编情况长度必须介于 0 和 2 之间")
	public String getIsSeries() {
		return isSeries;
	}

	public void setIsSeries(String isSeries) {
		this.isSeries = isSeries;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPublicOfficialsRegistrationTime() {
		return publicOfficialsRegistrationTime;
	}

	public void setPublicOfficialsRegistrationTime(Date publicOfficialsRegistrationTime) {
		this.publicOfficialsRegistrationTime = publicOfficialsRegistrationTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getGraduationTime() {
		return graduationTime;
	}

	public void setGraduationTime(Date graduationTime) {
		this.graduationTime = graduationTime;
	}
	
	@Length(min=0, max=50, message="专业类别长度必须介于 0 和 50 之间")
	public String getProfessionCategory() {
		return professionCategory;
	}

	public void setProfessionCategory(String professionCategory) {
		this.professionCategory = professionCategory;
	}
	
	@Length(min=0, max=2, message="是否高配长度必须介于 0 和 2 之间")
	public String getIsHigh() {
		return isHigh;
	}

	public void setIsHigh(String isHigh) {
		this.isHigh = isHigh;
	}
	
	@Length(min=0, max=300, message="记功情况长度必须介于 0 和 300 之间")
	public String getContribution() {
		return contribution;
	}

	public void setContribution(String contribution) {
		this.contribution = contribution;
	}
	
	@Length(min=0, max=50, message="职务类别(领导职务、非领导职务)长度必须介于 0 和 50 之间")
	public String getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}
	
	@Length(min=0, max=50, message="职务级别(县处级正职、县处级副职、乡科级正职、乡科级副职、科员、试用期人员、工勤人员、事业管理岗、事业技术岗)长度必须介于 0 和 50 之间")
	public String getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getNowWorktime() {
		return nowWorktime;
	}

	public void setNowWorktime(Date nowWorktime) {
		this.nowWorktime = nowWorktime;
	}
	
	
	@Length(min=0, max=100, message="现任职文号长度必须介于 0 和 100 之间")
	public String getNowDocNumber() {
		return nowDocNumber;
	}

	public void setNowDocNumber(String nowDocNumber) {
		this.nowDocNumber = nowDocNumber;
	}
	
	@Length(min=0, max=10, message="任同职级时间长度必须介于 0 和 10 之间")
	public String getServeSamejobTime() {
		return serveSamejobTime;
	}

	public void setServeSamejobTime(String serveSamejobTime) {
		this.serveSamejobTime = serveSamejobTime;
	}
	
	@Length(min=0, max=10, message="任同职级文号长度必须介于 0 和 10 之间")
	public String getServeSamedocNumberTime() {
		return serveSamedocNumberTime;
	}

	public void setServeSamedocNumberTime(String serveSamedocNumberTime) {
		this.serveSamedocNumberTime = serveSamedocNumberTime;
	}
	
	@Length(min=0, max=100, message="工资来源长度必须介于 0 和 100 之间")
	public String getSalarySource() {
		return salarySource;
	}

	public void setSalarySource(String salarySource) {
		this.salarySource = salarySource;
	}
	
	@Length(min=0, max=10, message="人员状态(在岗、不在岗)长度必须介于 0 和 10 之间")
	public String getPersonStatus() {
		return personStatus;
	}

	public void setPersonStatus(String personStatus) {
		this.personStatus = personStatus;
	}
	
	@Length(min=0, max=50, message="列编标志长度必须介于 1 和 50 之间")
	public String getColumnMarking() {
		return columnMarking;
	}

	public void setColumnMarking(String columnMarking) {
		this.columnMarking = columnMarking;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getColumnDate() {
		return columnDate;
	}

	public void setColumnDate(Date columnDate) {
		this.columnDate = columnDate;
	}
	
	@Length(min=0, max=100, message="列编文号长度必须介于 1 和 100 之间")
	public String getColumnDocNumber() {
		return columnDocNumber;
	}

	public void setColumnDocNumber(String columnDocNumber) {
		this.columnDocNumber = columnDocNumber;
	}
	
	@Length(min=0, max=100, message="列编渠道长度必须介于 1 和 100 之间")
	public String getColumnChannel() {
		return columnChannel;
	}

	public void setColumnChannel(String columnChannel) {
		this.columnChannel = columnChannel;
	}
	
	@Length(min=0, max=50, message="减员标志长度必须介于 0 和 50 之间")
	public String getLayoffsSign() {
		return layoffsSign;
	}

	public void setLayoffsSign(String layoffsSign) {
		this.layoffsSign = layoffsSign;
	}
	
	@Length(min=0, max=100, message="列编来源长度必须介于 1 和 100 之间")
	public String getColumnSource() {
		return columnSource;
	}

	public void setColumnSource(String columnSource) {
		this.columnSource = columnSource;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLayoffsTime() {
		return layoffsTime;
	}

	public void setLayoffsTime(Date layoffsTime) {
		this.layoffsTime = layoffsTime;
	}
	
	@Length(min=0, max=100, message="减员渠道长度必须介于 0 和 100 之间")
	public String getLayoffsChannel() {
		return layoffsChannel;
	}

	public void setLayoffsChannel(String layoffsChannel) {
		this.layoffsChannel = layoffsChannel;
	}
	
	@Length(min=0, max=100, message="减员文号长度必须介于 0 和 100 之间")
	public String getLayoffsDocNumber() {
		return layoffsDocNumber;
	}

	public void setLayoffsDocNumber(String layoffsDocNumber) {
		this.layoffsDocNumber = layoffsDocNumber;
	}
	
	@Length(min=0, max=100, message="减员去向长度必须介于 0 和 100 之间")
	public String getLayoffsGo() {
		return layoffsGo;
	}

	public void setLayoffsGo(String layoffsGo) {
		this.layoffsGo = layoffsGo;
	}
	
	@Length(min=0, max=1000, message="个人简介长度必须介于 0 和 1000 之间")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	@Length(min=0, max=500, message="奖惩情况长度必须介于 0 和 500 之间")
	public String getRewardPunishment() {
		return rewardPunishment;
	}

	public void setRewardPunishment(String rewardPunishment) {
		this.rewardPunishment = rewardPunishment;
	}
	
	@Length(min=0, max=500, message="学习培训情况长度必须介于 0 和 500 之间")
	public String getLearnTraing() {
		return learnTraing;
	}

	public void setLearnTraing(String learnTraing) {
		this.learnTraing = learnTraing;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	
}