/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tripartite.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.tripartite.entity.TripartiteParamConfig;

/**
 * 与第三方系统对接请求头、参数配置表DAO接口
 * @author 王鹏
 * @version 2018-06-30
 */
@MyBatisDao
public interface TripartiteParamConfigDao extends CrudDao<TripartiteParamConfig> {
	
}