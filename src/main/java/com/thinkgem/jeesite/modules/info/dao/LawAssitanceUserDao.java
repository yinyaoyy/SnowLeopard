/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.LawAssitanceUser;

/**
 * 法援中心工作人员DAO接口
 * @author wanglin
 * @version 2018-04-22
 */
@MyBatisDao
public interface LawAssitanceUserDao extends CrudDao<LawAssitanceUser> {
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
	int count(LawAssitanceUser lo);	
	 /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除

    AgencyVo getById(String id);
    /**
     * 
     * 添加法援中心工作者
     * @author 黄涛
	 * @version 2018-6-12 
	 * @param card
	 * @return
     * */
	String personIdCard(String card);
	
	/**
	 * 更新评价值
	 * @param evaluation
	 * @param id
	 */
	public void evaluationUpdate(@Param("evaluation")String evaluation,@Param("id")String id,@Param("remark")String remark);
}