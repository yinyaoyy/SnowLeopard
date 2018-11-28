/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.ServerRole;

/**
 * 服务角色DAO接口
 * @author 王鹏
 * @version 2018-05-08
 */
@MyBatisDao
public interface ServerRoleDao extends CrudDao<ServerRole> {
	public int delete(String siteId);
}