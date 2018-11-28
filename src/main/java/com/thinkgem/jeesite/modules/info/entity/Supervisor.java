/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 人民监督员Entity
 * @author suzz
 * @version 2018-06-08
 */
public class Supervisor extends DataEntity<Supervisor> {
	
	private static final long serialVersionUID = 1L;
	private Office name;		// 选任单位
	private String xrName;		// 人民监督员姓名
	private String sex;		// sex
	private String appllication;		// 报名方式
	private Date birthday;		// 监督员生日
	private String nation;		// 民族
	private String politicsStatus;		// 政治面貌
	private String educationBackground;		// 监督员学历
	private String graduateInstitutions;		// 毕业院校
	private String major;		// 学历专业
	private String idno;		// 监督员身份证号
	private String phone;		// 手机号
	private String qqNo;		// qq号
	private String wechat;//微信号
	private String law_work;//法学相关背景
	private String telephone;		// 监督员座机
	private String zipcode;		// 地址邮编
	private String mailbox;		// 邮箱
	private Area nativeProvince;		// 省或直辖市
	private Area nativeCity;		// native_city
	private Area nativeCounty;		// native_county
	private String nativeTowns;		// native_towns
	private String company;		// 工作地点
	private String companyNature;		// 工作单位性质(个人，国资。。)
	private Area oftenProvince;		// 经常居住省
	private Area oftenCity;		// 经常居住市
	private Area oftenCounty;		// 经常居住县区
	private String oftenTowns;		// 经常居住详细
	private String job;		// 职业类别
	private String position;		// 职位
	private String duty;		// 职务（如文员、内勤）
	private String technicalPost;		// 职称
	private String postTech;		// 职称级别
	private String representative;		// 是否是人大代表0是1非
	private String cppcc;		// 是否是政协委员0是1非
	private String civilServant;		// 是否是公职
	private String resume;		// 个人简介
	private String honor;		// 个人荣誉
	private String member;		// 家庭成员
	private String memberJj;		// 成员简介
	private String memberPost;		// 成员级别
	private String photograph;		// 照片
	private String jobNo;		// 工作证号
	private String isMongolian;//是否蒙汉精通 0是1否
	public Supervisor() {
		super();
	}

	public Supervisor(String id){
		super(id);
	}
	@ExcelField(value="name.name",title="选任单位", type=0, align=1, sort=10)
	public Office getName() {
		return name;
	}

	public void setName(Office name) {
		this.name = name;
	}
	@ExcelField(title="姓名", type=0, align=1, sort=20)
	@Length(min=0, max=20, message="人民监督员姓名长度必须介于 0 和 20 之间")
	public String getXrName() {
		return xrName;
	}

	public void setXrName(String xrName) {
		this.xrName = xrName;
	}
	@ExcelField(title="性别", type=0, align=1, sort=30,dictType="sex")
	@Length(min=0, max=1, message="sex长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@ExcelField(title="报名方式", type=0, align=1, sort=40)
	@Length(min=0, max=10, message="报名方式长度必须介于 0 和 10 之间")
	public String getAppllication() {
		return appllication;
	}

	public void setAppllication(String appllication) {
		this.appllication = appllication;
	}
	@ExcelField(title="出生日期", type=0, align=1, sort=50)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@ExcelField(title="民族", type=0, align=1, sort=60)
	@Length(min=0, max=10, message="民族长度必须介于 0 和 10 之间")
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	@ExcelField(title="政治面貌", type=0, align=1, sort=70)
	@Length(min=0, max=10, message="政治面貌长度必须介于 0 和 10 之间")
	public String getPoliticsStatus() {
		return politicsStatus;
	}

	public void setPoliticsStatus(String politicsStatus) {
		this.politicsStatus = politicsStatus;
	}
	@ExcelField(title="文化程度", type=0, align=1, sort=80)
	@Length(min=0, max=20, message="监督员学历长度必须介于 0 和 20 之间")
	public String getEducationBackground() {
		return educationBackground;
	}

	public void setEducationBackground(String educationBackground) {
		this.educationBackground = educationBackground;
	}
	@ExcelField(title="毕业院校", type=0, align=1, sort=90)
	@Length(min=0, max=20, message="毕业院校长度必须介于 0 和 20 之间")
	public String getGraduateInstitutions() {
		return graduateInstitutions;
	}

	public void setGraduateInstitutions(String graduateInstitutions) {
		this.graduateInstitutions = graduateInstitutions;
	}
	@ExcelField(title="专业", type=0, align=1, sort=100)
	@Length(min=0, max=32, message="学历专业长度必须介于 0 和 32 之间")
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
	@ExcelField(title="身份证号码", type=0, align=1, sort=105)
	@Length(min=0, max=20, message="监督员身份证号长度必须介于 0 和 20 之间")
	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}
	@ExcelField(title="手机", type=0, align=1, sort=110)
	@Length(min=0, max=20, message="手机号长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ExcelField(title="QQ号", type=0, align=1, sort=120)
	@Length(min=0, max=20, message="qq号长度必须介于 0 和 20 之间")
	public String getQqNo() {
		return qqNo;
	}

	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}
	@ExcelField(title="微信号", type=0, align=1, sort=130)
	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	@ExcelField(title="办公电话", type=0, align=1, sort=140)
	@Length(min=0, max=32, message="监督员座机长度必须介于 0 和 10 之间")
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	@ExcelField(title="邮编", type=0, align=1, sort=150)
	@Length(min=0, max=32, message="地址邮编长度必须介于 0 和 10 之间")
	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	@ExcelField(title="电子邮箱", type=0, align=1, sort=160)
	@Length(min=0, max=32, message="邮箱长度必须介于 0 和 16 之间")
	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}
	@ExcelField(value="nativeProvince.name",title="户籍地（省、直辖市、自治区）", type=0, align=1, sort=170)
	public Area getNativeProvince() {
		return nativeProvince;
	}

	public void setNativeProvince(Area nativeProvince) {
		this.nativeProvince = nativeProvince;
	}
	@ExcelField(value="nativeCity.name",title="户籍地（市）", type=0, align=1, sort=180)
	public Area getNativeCity() {
		return nativeCity;
	}

	public void setNativeCity(Area nativeCity) {
		this.nativeCity = nativeCity;
	}
	@ExcelField(value="nativeCounty.name",title="户籍地（区）", type=0, align=1, sort=190)
	public Area getNativeCounty() {
		return nativeCounty;
	}

	public void setNativeCounty(Area nativeCounty) {
		this.nativeCounty = nativeCounty;
	}
	@ExcelField(title="户籍地详细地址", type=0, align=1, sort=200)
	@Length(min=0, max=64, message="户籍地详细地址必须介于 0 和 32 之间")
	public String getNativeTowns() {
		return nativeTowns;
	}

	public void setNativeTowns(String nativeTowns) {
		this.nativeTowns = nativeTowns;
	}
	@ExcelField(value="oftenProvince.name",title="经常居住地（省、直辖市、自治区）", type=0, align=1, sort=210)
	public Area getOftenProvince() {
		return oftenProvince;
	}
	
	public void setOftenProvince(Area oftenProvince) {
		this.oftenProvince = oftenProvince;
	}
	@ExcelField(value="oftenCity.name",title="经常居住地（市）", type=0, align=1, sort=220)
	public Area getOftenCity() {
		return oftenCity;
	}

	public void setOftenCity(Area oftenCity) {
		this.oftenCity = oftenCity;
	}
	@ExcelField(value="oftenCounty.name",title="经常居住地（区）", type=0, align=1, sort=230)
	public Area getOftenCounty() {
		return oftenCounty;
	}

	public void setOftenCounty(Area oftenCounty) {
		this.oftenCounty = oftenCounty;
	}
	@ExcelField(title="经常居住地详细地址", type=0, align=1, sort=240)
	public String getOftenTowns() {
		return oftenTowns;
	}

	public void setOftenTowns(String oftenTowns) {
		this.oftenTowns = oftenTowns;
	}

	
	@ExcelField(title="工作单位", type=0, align=1, sort=250)
	@Length(min=0, max=32, message="工作地点长度必须介于 0 和 32 之间")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	@ExcelField(title="单位性质", type=0, align=1, sort=260)
	@Length(min=0, max=32, message="工作单位性质(个人，国资。。)长度必须介于 0 和 10 之间")
	public String getCompanyNature() {
		return companyNature;
	}

	public void setCompanyNature(String companyNature) {
		this.companyNature = companyNature;
	}
	
	@ExcelField(title="职业类别", type=0, align=1, sort=270)
	@Length(min=0, max=64, message="职业类别长度必须介于 0 和 20 之间")
	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
	@ExcelField(title="职务", type=0, align=1, sort=280)
	@Length(min=0, max=32, message="职务（如文员、内勤）长度必须介于 0 和 10 之间")
	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}
	
	@ExcelField(title="职务级别", type=0, align=1, sort=290)
	@Length(min=0, max=16, message="职位长度必须介于 0 和 16 之间")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	
	public String getTechnicalPost() {
		return technicalPost;
	}
	@ExcelField(title="职称", type=0, align=1, sort=300)
	public void setTechnicalPost(String technicalPost) {
		this.technicalPost = technicalPost;
	}
	@ExcelField(title="职称级别", type=0, align=1, sort=310)
	@Length(min=0, max=32, message="职称级别长度必须介于 0 和 6 之间")
	public String getPostTech() {
		return postTech;
	}

	public void setPostTech(String postTech) {
		this.postTech = postTech;
	}
	@ExcelField(title="人大代表", type=0, align=1, sort=320,dictType="yes_no")
	@Length(min=0, max=12, message="是否是人大代表0是1非长度必须介于 0 和 1 之间")
	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}
	@ExcelField(title="政协委员", type=0, align=1, sort=330,dictType="yes_no")
	@Length(min=0, max=10, message="是否是政协委员0是1非长度必须介于 0 和 1 之间")
	public String getCppcc() {
		return cppcc;
	}

	public void setCppcc(String cppcc) {
		this.cppcc = cppcc;
	}
	@ExcelField(title="是否有法学或法律工作背景", type=0, align=1, sort=340)
	public String getLaw_work() {
		return law_work;
	}

	public void setLaw_work(String law_work) {
		this.law_work = law_work;
	}
	@ExcelField(title="是否公职人员", type=0, align=1, sort=350,dictType="yes_no")
	@Length(min=0, max=10, message="是否是公职长度必须介于 0 和 1 之间")
	public String getCivilServant() {
		return civilServant;
	}

	public void setCivilServant(String civilServant) {
		this.civilServant = civilServant;
	}
	@ExcelField(title="个人简历", type=0, align=1, sort=360)
	@Length(min=0, max=300, message="个人简介长度必须介于 0 和 200 之间")
	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}
	@ExcelField(title="何时何地受到何种奖励", type=0, align=1, sort=370)
	@Length(min=0, max=200, message="个人荣誉长度必须介于 0 和 200 之间")
	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}
	@ExcelField(title="家庭及主要社会关系", type=0, align=1, sort=380)
	@Length(min=0, max=600, message="家庭成员长度必须介于 0 和 64 之间")
	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}
	@ExcelField(title="人员状态", type=0, align=1, sort=390)
	@Length(min=0, max=64, message="成员简介长度必须介于 0 和 64 之间")
	public String getMemberJj() {
		return memberJj;
	}

	public void setMemberJj(String memberJj) {
		this.memberJj = memberJj;
	}
	
	private String  workingExperience;
	
	@ExcelField(title="任职经历", type=0, align=1, sort=400)
	public String getWorkingExperience() {
		return workingExperience;
	}

	public void setWorkingExperience(String workingExperience) {
		this.workingExperience = workingExperience;
	}
	@ExcelField(title="任职级别", type=0, align=1, sort=410)
	@Length(min=0, max=64, message="成员级别长度必须介于 0 和 64 之间")
	public String getMemberPost() {
		return memberPost;
	}

	public void setMemberPost(String memberPost) {
		this.memberPost = memberPost;
	}
	@ExcelField(title="照片上传名称", type=0, align=1, sort=420)
	@Length(min=0, max=200, message="照片长度必须介于 0 和 64 之间")
	public String getPhotograph() {
		return photograph;
	}

	public void setPhotograph(String photograph) {
		this.photograph = photograph;
	}
	@ExcelField(title="工作证编号", type=0, align=1, sort=430)
	@Length(min=0, max=64, message="工作证号长度必须介于 0 和 16 之间")
	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	
	public String getIsMongolian() {
		return isMongolian;
	}

	public void setIsMongolian(String isMongolian) {
		this.isMongolian = isMongolian;
	}

	public Supervisor(String name, String areaId) {
		super();
		this.xrName = name;
		this.oftenCounty = new Area();
		this.oftenCounty.setId(areaId);
	}

}