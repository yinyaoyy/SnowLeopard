/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 站点角色Entity
 * @author lin
 * @version 2018-04-15
 */
public class SiteRole extends DataEntity<SiteRole> {
	
	private static final long serialVersionUID = 1L;
	private String siteId;		// site_id
	private String roleId;		// role_id
	
	public SiteRole() {
		super();
	}

	public SiteRole(String id){
		super(id);
	}

	@Length(min=1, max=64, message="site_id长度必须介于 1 和 64 之间")
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	@Length(min=1, max=64, message="role_id长度必须介于 1 和 64 之间")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
}