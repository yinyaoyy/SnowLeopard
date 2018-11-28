/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao.act;

import java.util.List;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAcceptRegister;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAcceptRegisterCount;

/**
 * 人民调解受理登记DAO接口
 * @author zhangqiang
 * @version 2018-05-24
 */
@MyBatisDao
public interface OaPeopleMediationAcceptRegisterDao extends CrudDao<OaPeopleMediationAcceptRegister> {

	/**
	 * 根据年度旗县严重等级统计
	 * 年度旗县严重等级数量(柱状)
	 * @author 王鹏
	 * @version 2018-06-04 10:22:59
	 * @param opmarc
	 * @return
	 */
	List<OaPeopleMediationAcceptRegisterCount> countByYearCaseRank(OaPeopleMediationAcceptRegisterCount opmarc);
}