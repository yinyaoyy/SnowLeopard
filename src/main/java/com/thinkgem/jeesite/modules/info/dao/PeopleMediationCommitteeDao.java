/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.PeopleMediationCommittee;

/**
 * 人民调解委员会DAO接口
 * @author wanglin
 * @version 2018-05-23
 */
@MyBatisDao
public interface PeopleMediationCommitteeDao extends CrudDao<PeopleMediationCommittee> {
	   /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除
    public List<PeopleMediationCommittee> getByNameAdnArea(PeopleMediationCommittee peopleMediationCommittee);
	
	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-04-19 14:59:48
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计调委会数量
	 * @author 王鹏
	 * @version 2018-04-19 10:44:41
	 * @return
	 */
	int count(PeopleMediationCommittee lo);
	
	int counte(String tt);
	
	AgencyVo getById(String id);
	public List<PeopleMediationCommittee> getMediationInfo(@Param("name")String name,
			@Param("areaId")String areaId, @Param("belongTowns")String belongTowns);
	public List<PeopleMediationCommittee> findListInfo(
			PeopleMediationCommittee lo);
}