/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.chart.entity.ForensicPersonnelVo;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.InfoForensicPersonnel;

/**
 * 鉴定人员信息管理DAO接口
 * @author hejia
 * @version 2018-04-24
 */
@MyBatisDao
public interface InfoForensicPersonnelDao extends CrudDao<InfoForensicPersonnel> {
	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-4-25 18:10:39
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);

    /**
     * 通过区域id统计
     * @param areaId
     * @return
     */
    Integer countByAreaId(String areaId);
    
    /***
     * 大屏查询鉴定人员信息
     * @param forensicPersonnelVo
     * @return
     */
	List<ForensicPersonnelVo> findListForApiBigScreen(ForensicPersonnelVo forensicPersonnelVo);

	AgencyVo getInfo(String id);
	
	/**
	 * 更新评价值
	 * @param evaluation
	 * @param id
	 */
	public void evaluationUpdate(@Param("evaluation")String evaluation,@Param("id")String id,@Param("remark")String remark);
	
}