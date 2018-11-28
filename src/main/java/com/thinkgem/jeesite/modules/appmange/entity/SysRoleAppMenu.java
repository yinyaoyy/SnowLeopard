/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 角色与移动端应用的对应关系Entity
 * @author hejia
 * @version 2018-04-19
 */
public class SysRoleAppMenu extends DataEntity<SysRoleAppMenu> {
	
	private static final long serialVersionUID = 1L;
	private String roleId;		// role_id
	private String appMenuId;		// app_menu_id
	
	public SysRoleAppMenu() {
		super();
	}

	public SysRoleAppMenu(String id){
		super(id);
	}

	@Length(min=1, max=64, message="role_id长度必须介于 1 和 64 之间")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@Length(min=1, max=64, message="app_menu_id长度必须介于 1 和 64 之间")
	public String getAppMenuId() {
		return appMenuId;
	}

	public void setAppMenuId(String appMenuId) {
		this.appMenuId = appMenuId;
	}
	
}