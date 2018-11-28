/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 162599Entity
 * @author wanglin
 * @version 2018-07-23
 */
public class UserRoleOffice extends DataEntity<UserRoleOffice> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private Office office;		// 机构id
	private Role role;		// 角色id
	private String isMain;		// 是否为主职0主职1兼职
	
	public UserRoleOffice() {
		super();
	}

	public UserRoleOffice(String id){
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Length(min=0, max=1, message="是否为主职0主职1兼职长度必须介于 0 和 1 之间")
	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}
	
}