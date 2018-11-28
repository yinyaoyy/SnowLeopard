package com.thinkgem.jeesite.modules.sys.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class PartTimeJob extends DataEntity<UserRoleOffice> {
	private static final long serialVersionUID = 1L;
	private String userId;
	private String userName;
	private String roleId;
	private String roleName;
	private String officeId;
	private String officeName;
	private String isMain;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public PartTimeJob(String userId, String roleId, String officeId, String isMain) {
		super();
		this.userId = userId;
		this.roleId = roleId;
		this.officeId = officeId;
		this.isMain = isMain;
	}

	public PartTimeJob(String userId, String isMain) {
		super();
		this.userId = userId;
		this.isMain = isMain;
	}

	public PartTimeJob() {
		super();
	}

}
