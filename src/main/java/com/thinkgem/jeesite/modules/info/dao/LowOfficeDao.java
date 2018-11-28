/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.LowOffice;

/**
 * 律师事务所DAO接口
 * @author 王鹏
 * @version 2018-04-18
 */
@MyBatisDao
public interface LowOfficeDao extends CrudDao<LowOffice> {
	
	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-04-19 14:59:48
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计律师事务所数量
	 * @author 王鹏
	 * @version 2018-04-19 10:44:41
	 * @return
	 */
	int count(LowOffice lo);

    /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除

	AgencyVo getInfo(String id);

	LowOffice getLawOfficeByName(String name);
}