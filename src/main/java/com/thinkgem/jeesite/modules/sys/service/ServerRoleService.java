/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeeServer">JeeServer</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.dao.ServerRoleDao;
import com.thinkgem.jeesite.modules.sys.entity.ServerRole;

/**
 * 站点角色Service
 * @author lin
 * @version 2018-04-15
 */
@Service
@Transactional(readOnly = true)
public class ServerRoleService extends CrudService<ServerRoleDao, ServerRole> {

	public ServerRole get(String id) {
		return super.get(id);
	}
	
	public List<ServerRole> findList(ServerRole ServerRole) {
		return super.findList(ServerRole);
	}
	
	public Page<ServerRole> findPage(Page<ServerRole> page, ServerRole ServerRole) {
		return super.findPage(page, ServerRole);
	}
	
	@Transactional(readOnly = false)
	public void save(ServerRole ServerRole) {
		super.save(ServerRole);
	}
	
	@Transactional(readOnly = false)
	public void delete(String ServerId) {
		dao.delete(ServerId);
	}
	
}