/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 服务角色Entity
 * @author 王鹏
 * @version 2018-05-08
 */
public class ServerRole extends DataEntity<ServerRole> {
	
	private static final long serialVersionUID = 1L;
	private String serverId;		// 服务id
	private String roleId;		// 角色id
	
	public ServerRole() {
		super();
	}

	public ServerRole(String id){
		super(id);
	}

	@Length(min=0, max=64, message="服务id长度必须介于 0 和 64 之间")
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	
	@Length(min=0, max=64, message="角色id长度必须介于 0 和 64 之间")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
}