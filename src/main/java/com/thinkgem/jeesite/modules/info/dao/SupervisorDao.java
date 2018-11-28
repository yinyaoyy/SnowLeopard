/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.chart.entity.SupervisorVo;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.Supervisor;

/**
 * 人民监督科DAO接口
 * @author suzz
 * @version 2018-06-08
 */
@MyBatisDao
public interface SupervisorDao extends CrudDao<Supervisor> {
	/**
	 * 接口集合查询查询集合
	 * @author suzz
	 * @version 2018-4-22 16:26:47
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计监督员数量
	 * @author suzz
	 * @version 2018-4-22 16:26:47
	 * @return
	 */
	int count(Supervisor lo);

    /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除
    /**
     * 通过id获取信息详情
     * @param id
     * @return
     */
    AgencyVo getById(String id);
    /**
     * 大屏查询数据
     * @param supervisorVo
     * @return
     */
	List<SupervisorVo> findListForApiBigScreen(SupervisorVo supervisorVo);
	
	/**
	 * 更新评价值
	 * @param evaluation
	 * @param id
	 */
	public void evaluationUpdate(@Param("evaluation")String evaluation,@Param("id")String id,@Param("remark")String remark);
}