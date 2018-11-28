/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.OaDossier;

/**
 * 卷宗说明DAO接口
 * @author zhangqiang
 * @version 2018-05-28
 */
@MyBatisDao
public interface OaDossierDao extends CrudDao<OaDossier> {
	
}