/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.SiteRole;

/**
 * 站点角色DAO接口
 * @author lin
 * @version 2018-04-15
 */
@MyBatisDao
public interface SiteRoleDao extends CrudDao<SiteRole> {
	public int delete(String siteId);
}