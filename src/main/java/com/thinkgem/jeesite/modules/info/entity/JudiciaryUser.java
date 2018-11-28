/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.common.utils.excel.fieldtype.RoleListType;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;

/**
 * 司法所工作人员Entity
 * @author wanglin
 * @version 2018-06-10
 */
public class JudiciaryUser extends DataEntity<JudiciaryUser> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String sex;		// 性别
	private String phone;		// 电话
	private String ethnic;		// 民族
	private String idcard;		// 身份证号
	private String roleId;		// 职务
	private Date practisingTime;		// 开始执业时间
	private String isPeopleMediation;		// 是否兼职人民调解员
	private String imageUrl;		// 头像路径
	private Office office;		// 归属司法所
	private String politicalFace;		// 政治面貌
	private Area area;		// 归属乡镇
	private Area town;		// 归属乡镇
	private String introduction;		// 个人简介
	private String isMongolian;//精通蒙汉双语 0是1否
	//private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	public JudiciaryUser() {
		super();
	}

	public JudiciaryUser(String id){
		super(id);
	}
	@ExcelField(title="姓名", type=0, align=1, sort=10)
	@Length(min=0, max=20, message="姓名长度必须介于 0 和 20 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="性别", type=0, align=1, sort=20,dictType="sex")
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@ExcelField(title="电话", type=0, align=1, sort=30)
	@Length(min=0, max=20, message="电话长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ExcelField(title="民族", type=0, align=1, sort=40,dictType="ethnic")
	@Length(min=0, max=2, message="民族长度必须介于 0 和 2 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	@ExcelField(title="身份证号", type=0, align=1, sort=50)
	@Length(min=0, max=20, message="身份证号长度必须介于 0 和 20 之间")
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		Map<String,String> map = DateUtils.getBirAgeSex(idcard);
		this.sex = map.get("sexCode");
		this.idcard = idcard;
	}
	@ExcelField(title="职务", type=0, align=1, sort=60,dictType="info_role_type")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@ExcelField(title="开始执业时间", type=0, align=1, sort=70)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPractisingTime() {
		return practisingTime;
	}

	public void setPractisingTime(Date practisingTime) {
		this.practisingTime = practisingTime;
	}
	@ExcelField(title="是否兼职人民调解员", type=0, align=1, sort=80,dictType="yes_no")
	@Length(min=0, max=1, message="是否兼职人民调解员长度必须介于 0 和 1 之间")
	public String getIsPeopleMediation() {
		return isPeopleMediation;
	}

	public void setIsPeopleMediation(String isPeopleMediation) {
		this.isPeopleMediation = isPeopleMediation;
	}
	@ExcelField(title="头像", type=0, align=1, sort=90)
	@Length(min=0, max=200, message="头像路径长度必须介于 0 和 200 之间")
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@ExcelField(value="office.name",title="所属司法所", type=0, align=1, sort=100)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	@ExcelField(title="政治面貌", type=0, align=1, sort=110)
	@Length(min=0, max=10, message="政治面貌长度必须介于 0 和 10 之间")
	public String getPoliticalFace() {
		return politicalFace;
	}

	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}
	@ExcelField(value="area.name",title="归属旗县", type=0, align=1, sort=120)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	@ExcelField(value="town.name",title="归属乡镇", type=0, align=1, sort=130)
	public Area getTown() {
		return town;
	}

	public void setTown(Area town) {
		this.town = town;
	}
	@ExcelField(title="个人简介", type=0, align=1, sort=140)
	@Length(min=0, max=500, message="个人简介长度必须介于 0 和 500 之间")
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

//	@JsonIgnore
//	public List<String> getRoleIdList() {
//		List<String> roleIdList = Lists.newArrayList();
//		for (Role role : roleList) {
//			roleIdList.add(role.getId());
//		}
//		return roleIdList;
//	}
//
//	public void setRoleIdList(List<String> roleIdList) {
//		roleList = Lists.newArrayList();
//		for (String roleId : roleIdList) {
//			Role role = new Role();
//			role.setId(roleId);
//			roleList.add(role);
//		}
//	}
	public JudiciaryUser(String name, String areaId) {
		super();
		this.name = name;
		this.area = new Area();
		this.area.setId(areaId);
	}
}