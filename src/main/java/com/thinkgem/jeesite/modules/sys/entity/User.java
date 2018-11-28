/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.annotation.Phone;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.cols.SupCol;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.common.utils.excel.fieldtype.RoleListType;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 用户Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class User extends DataEntity<User> {

	private static final long serialVersionUID = 1L;
	private Office company;	// 归属部门
	private Office office;	// 归属科室
	private String papernum;//身份证号
	private String loginName;// 登录名
	private String password;// 密码
	private String no;		// 工号
	private String name = "";	// 姓名
	private String email;	// 邮箱
	private String phone;	// 电话
	private String mobile;	// 手机
	private String userType;// 用户职位
	private String userTypeDesc;// 用户职位
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	private String loginFlag;	// 是否允许登陆
	private String photo;	// 头像
	private String oldLoginName;// 原登录名
	private String newPassword;	// 新密码
	private String oldLoginIp;	// 上次登陆IP
	private Date oldLoginDate;	// 上次登陆日期
	private Role role;	// 根据角色查询用户条件
	private String xdbm;	
    private int loginCount;//登录次数
	private String address;	//地址
	private String isPerfect;//是否完善信息
	private String userSourceType;//用户类型
	private String userSourceTypeDesc;//用户类型
	private String isShow;
	private Date birthday;	// 生日
	private String bir;	// 生日
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	private UserExpand userExpand=new UserExpand();
	private Area area=new Area();
	private Area townarea=new Area();
	//private List<UserAndInfoData> careerList = Lists.newArrayList(); //兼职列表
	private String identityType;	//身份类型(0：社会大众:1：科员)
	private List<PartTimeJob> partTimeList=Lists.newArrayList(); //兼职
	public Area getTownarea() {
		return townarea;
	}


	public void setTownarea(Area townarea) {
		this.townarea = townarea;
	}

	

	/**
	 * 自动创建账号，所有参数必填
	 * @param name
	 * @param company 机构
	 * @param office 科室
	 * @param loginName
	 * @param roleId 多个角色可用英文逗号(,)分割
	 * @param idCard 身份证号
	 * @throws BusinessException
	 */
	public User(String name, Office company, Office office, String loginName, String roleId, String idCard) throws BusinessException {
		super();
		if(StringUtils.isBlank(name)||company==null||office==null||StringUtils.isBlank(loginName)
				||StringUtils.isBlank(roleId)||StringUtils.isBlank(idCard)) {
			throw new BusinessException("用户信息不全，无法创建用户账号");
		}
		this.name = name;
		this.company = company;
		//需要根据业务机构信息的id重新获取业务机构在机构表的id
		this.office = office;
		this.loginName = loginName;
		String[] roleIds = roleId.split(",");
		Role role = null;
		for (int i = 0; i < roleIds.length; i++) {
			role = new Role(roleIds[i]);
			this.roleList.add(role);
		}
		this.papernum = idCard;
		this.password = SystemService.entryptPassword(OfficeRoleConstant.DEFAULT_PASSWORD,loginName);
	}
	
/*	public List<UserAndInfoData> getCareerList() {
		return careerList;
	}


	public void setCareerList(List<UserAndInfoData> careerList) {
		this.careerList = careerList;
	}
*/

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getPapernum() {
		return papernum;
	}

	public void setPapernum(String papernum) {
		this.papernum = papernum;
	}

	public String getBir() {
		return bir;
	}

	public void setBir(String bir) {
		this.bir = bir;
	}

	public UserExpand getUserExpand() {
		return userExpand;
	}

	public void setUserExpand(UserExpand userExpand) {
		this.userExpand = userExpand;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@ExcelField(title="地区", align=2, sort=70)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsPerfect() {
		return isPerfect;
	}

	public void setIsPerfect(String isPerfect) {
		this.isPerfect = isPerfect;
	}

	public String getXdbm() {
		return xdbm;
	}

	public void setXdbm(String xdbm) {
		this.xdbm = xdbm;
	}


	
	public User() {
		super();
		this.loginFlag = Global.YES;
	}
	
	public User(String id){
		super(id);
	}

	public User(String id, String loginName){
		super(id);
		this.loginName = loginName;
	}

	public User(Role role){
		super();
		this.role = role;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	@SupCol(isUnique="true", isHide="true")
	@ExcelField(title="ID", type=1, align=2, sort=1)
	public String getId() {
		return id;
	}

	//@NotNull(message="归属部门不能为空")
	//@Length(min=0, max=100, message="归属部门长度必须介于 1 和 100 之间")
	@ExcelField(title="归属部门", align=2, sort=20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	
	//@NotNull(message="归属科室不能为空")
	//@Length(min=0, max=100, message="归属科室长度必须介于 1 和 100 之间")
	@ExcelField(title="归属科室", align=2, sort=25)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	@JsonIgnore
	@Length(min=0, max=100, message="登录名长度必须介于 0 和 100 之间")
	@ExcelField(title="登录名", align=2, sort=30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min=0, max=100, message="密码长度必须介于 0 和 100 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min=0, max=100, message="姓名长度必须介于 0 和 100 之间")
	@ExcelField(title="姓名", align=2, sort=40)
	public String getName() {
		return name;
	}
	
	@Length(min=0, max=100, message="工号长度必须介于 0 和 100 之间")
	@ExcelField(title="工号", align=2, sort=45)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email(message="邮箱格式不正确")
	@Length(min=0, max=200, message="邮箱长度必须介于 0 和 200 之间")
	@ExcelField(title="邮箱", align=1, sort=50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=200, message="电话长度必须介于 0 和 200 之间")
	//@ExcelField(title="电话", align=2, sort=60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Phone(message="手机格式不正确")
	@Length(min=0, max=200, message="手机长度必须介于 0 和 200 之间")
	@ExcelField(title="手机", align=2, sort=70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	//@ExcelField(title="备注", align=1, sort=900)
	public String getRemarks() {
		return remarks;
	}
	
	@Length(min=0, max=100, message="用户类型长度必须介于 0 和 100 之间")
	@ExcelField(title="职位", align=2, sort=80, dictType="sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	//@ExcelField(title="创建时间", type=0, align=1, sort=90)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title="最后登录IP", type=1, align=1, sort=100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录日期", type=1, align=1, sort=110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldLoginIp() {
		if (oldLoginIp == null){
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOldLoginDate() {
		if (oldLoginDate == null){
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}


	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			if("0".equals(role.getIsMain())) {//获取主职
				roleIdList.add(role.getId());
			}
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ",");
	}
	
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id){
		return id != null && "1".equals(id);
	}
	
	@Override
	public String toString() {
		return id;
	}


	public String getUserSourceType() {
		return userSourceType;
	}


	public void setUserSourceType(String userSourceType) {
		this.userSourceType = userSourceType;
	}


	public String getUserSourceTypeDesc() {
		return userSourceTypeDesc;
	}


	public void setUserSourceTypeDesc(String userSourceTypeDesc) {
		this.userSourceTypeDesc = userSourceTypeDesc;
	}


	public String getUserTypeDesc() {
		return userTypeDesc;
	}


	public void setUserTypeDesc(String userTypeDesc) {
		this.userTypeDesc = userTypeDesc;
	}


	public String getIsShow() {
		return isShow;
	}


	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	/*public static void main(String[] args) {
        String str = "wo";
        String[] s = str.split(",");
        System.out.println(s.length);
	}*/


	public String getIdentityType() {
		return identityType;
	}


	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public List<PartTimeJob> getPartTimeList() {
		return partTimeList;
	}

	public void setPartTimeList(List<PartTimeJob> partTimeList) {
		this.partTimeList = partTimeList;
	}
	public String getRoleOfficeIdList() {
		return "";
	}

	public void setRoleOfficeIdList(String roleOfficeId) {
		if(StringUtils.isNotBlank(roleOfficeId)) {
			roleOfficeId = StringEscapeUtils.unescapeHtml4(roleOfficeId);
			this.partTimeList=JSONObject.parseArray(roleOfficeId, PartTimeJob.class);
		}
	}
	
}