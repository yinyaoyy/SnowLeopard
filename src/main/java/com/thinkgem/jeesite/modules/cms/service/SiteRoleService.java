/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.entity.SiteRole;
import com.thinkgem.jeesite.modules.cms.dao.SiteRoleDao;

/**
 * 站点角色Service
 * @author lin
 * @version 2018-04-15
 */
@Service
@Transactional(readOnly = true)
public class SiteRoleService extends CrudService<SiteRoleDao, SiteRole> {

	public SiteRole get(String id) {
		return super.get(id);
	}
	
	public List<SiteRole> findList(SiteRole siteRole) {
		return super.findList(siteRole);
	}
	
	public Page<SiteRole> findPage(Page<SiteRole> page, SiteRole siteRole) {
		return super.findPage(page, siteRole);
	}
	
	@Transactional(readOnly = false)
	public void save(SiteRole siteRole) {
		super.save(siteRole);
	}
	
	@Transactional(readOnly = false)
	public void delete(String siteId) {
		dao.delete(siteId);
	}
	
}