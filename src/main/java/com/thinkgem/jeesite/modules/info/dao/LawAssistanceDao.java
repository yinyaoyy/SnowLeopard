/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.LawAssistance;

/**
 * 法援中心DAO接口
 * @author wanglin
 * @version 2018-04-22
 */
@MyBatisDao
public interface LawAssistanceDao extends CrudDao<LawAssistance> {
	/**
	 * 接口集合查询查询集合
	 * @author wanglin
	 * @version 2018-04-19 14:59:48
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计法援中心数量
	 * @author wanglin
	 * @version 2018-04-19 10:44:41
	 * @return
	 */
	int count(LawAssistance lo);	
	AgencyVo getById(String id);

	

	String areaName(String name);
}