/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tripartite.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.tripartite.entity.TripartiteSystemConfig;

/**
 * 与第三方系统对接配置表DAO接口
 * @author 王鹏
 * @version 2018-06-23
 */
@MyBatisDao
public interface TripartiteSystemConfigDao extends CrudDao<TripartiteSystemConfig> {
	
}