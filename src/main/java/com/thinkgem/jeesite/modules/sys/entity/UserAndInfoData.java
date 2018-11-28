/**
 * 
 */
package com.thinkgem.jeesite.modules.sys.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * 根据业务信息初始化人员信息
 * @author 王鹏
 * @version 2018-06-05 16:11:37
 */
public class UserAndInfoData extends DataEntity<UserAndInfoData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8575426341528711754L;
	
	private String id; // 用户/机构账号机构id
	private String name;   // 用户真实姓名
	private String companyId; //用户账号机构节点id
	private String infoDataOfficeId; //用户业务机构id
	private String loginName;  //用户登录账号
	private String roleId;    //用户角色id
	private String infoDataId; // 用户业务数据id
	private String remark;   //备注
	private String idCard;  //用户身份证号
	private String phone;
	private String type;//机构还是人员1=机构;2=人员
	private String isMain;//是否是主要职位(只有一个主职，其他都是兼职)
	private String isMainDesc;//职位说明
	private String userSourceType="";//用户类型
	public UserAndInfoData() {
	}
	public UserAndInfoData(String name, String companyId, 
			String infoDataOfficeId, String loginName, 
			String roleId, String infoDataId, 
			String remark, String idCard) {
		this.name = name;
		this.companyId = companyId;
		this.infoDataOfficeId = infoDataOfficeId;
		this.loginName = loginName;
		this.roleId = roleId;
		this.infoDataId = infoDataId;
		this.remark = remark;
		this.idCard = idCard;
	}
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIsMainDesc() {
		return isMainDesc;
	}
	public void setIsMainDesc(String isMainDesc) {
		this.isMainDesc = isMainDesc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsMain() {
		return isMain;
	}
	public void setIsMain(String isMain) {
		this.isMain = isMain;
		this.isMainDesc = StringUtils.isBlank(isMain)?"":("1".equals(isMain)?"":"(兼职)");
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getInfoDataOfficeId() {
		return infoDataOfficeId;
	}
	public void setInfoDataOfficeId(String infoDataOfficeId) {
		this.infoDataOfficeId = infoDataOfficeId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getInfoDataId() {
		return infoDataId;
	}
	public void setInfoDataId(String infoDataId) {
		this.infoDataId = infoDataId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getUserSourceType() {
		return userSourceType;
	}
	public void setUserSourceType(String userSourceType) {
		this.userSourceType = userSourceType;
	}
	
}
