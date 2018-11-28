/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.OaGongchu;

/**
 * 公出单DAO接口
 * @author lin
 * @version 2018-02-28
 */
@MyBatisDao
public interface OaGongchuDao extends CrudDao<OaGongchu> {
	
}