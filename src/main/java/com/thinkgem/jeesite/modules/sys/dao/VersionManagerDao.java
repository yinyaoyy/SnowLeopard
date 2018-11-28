/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.ServerSourceManage;
import com.thinkgem.jeesite.modules.sys.entity.VersionManager;

/**
 * 系统版本管理DAO接口
 * @author huangtao
 * @version 2018-06-27
 */
@MyBatisDao
public interface VersionManagerDao extends CrudDao<VersionManager> {
	 
	public List<VersionManager> findList(VersionManager versionManager);
}