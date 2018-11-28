/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.chart.entity.LawyerVo;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.Lawyer;

/**
 * 律师信息管理DAO接口
 * @author 王鹏
 * @version 2018-04-22
 */
@MyBatisDao
public interface LawyerDao extends CrudDao<Lawyer> {

	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-4-22 16:26:47
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计律师数量
	 * @author 王鹏
	 * @version 2018-4-22 16:26:47
	 * @return
	 */
	int count(Lawyer lo);

    /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除
    
    /**
     * 接口:大屏查询律师信息
     * @author 王鹏
     * @version 2018-06-11 20:59:12
     * @param lv
     * @return
     */
    List<LawyerVo> findListForApiBigScreen(LawyerVo lawyerVo);

	AgencyVo getInfo(String id);
	
	/**
	 * 更新评价值
	 * @param evaluation
	 * @param id
	 */
	public void evaluationUpdate(@Param("evaluation")String evaluation,@Param("id")String id,@Param("remark")String remark);

	Lawyer getInfoByIdCard(String idCard);
}