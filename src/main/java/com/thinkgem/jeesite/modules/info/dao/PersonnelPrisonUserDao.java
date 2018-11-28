/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.LawAssitanceUser;
import com.thinkgem.jeesite.modules.info.entity.PersonnelPrisonUser;

/**
 * 在册安置帮教人员DAO接口
 * @author huangtao
 * @version 2018-06-22
 */
@MyBatisDao
public interface PersonnelPrisonUserDao extends CrudDao<PersonnelPrisonUser> {
    /**
     * 添加安置人员工作者
     * @author 黄涛
	 * @version 2018-6-22 
     * @param idCard
     * @return
     */
	public String personIdCard(String idCard);
	/**
	 * 接口集合查询查询集合
	 * @author huangtao
	 * @version 2018-06-22
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	AgencyVo getById(String id);
	
	/**
	 * 统计在册安置帮教人员数量
	 * @author 黄涛
	 * @version 2018-04-19 10:44:41
	 * @return
	 */
	int count(PersonnelPrisonUser lo);	
	 /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除

	
}