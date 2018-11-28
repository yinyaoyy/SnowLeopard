/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.Judiciary;

/**
 * 司法所DAO接口
 * @author wanglin
 * @version 2018-06-08
 */
@MyBatisDao
public interface JudiciaryDao extends CrudDao<Judiciary> {

	/**
	 * 统计司法所数量
	 * @author 王鹏
	 * @version 2018-6-10 11:30:30
	 * @return
	 */
	int count(Judiciary judiciary);
	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-4-22 16:26:47
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	AgencyVo getById(String id);
}