/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao.act;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationVisit;

/**
 * 人民调解回访记录DAO接口
 * @author zhangqiang
 * @version 2018-05-28
 */
@MyBatisDao
public interface OaPeopleMediationVisitDao extends CrudDao<OaPeopleMediationVisit> {
	
}