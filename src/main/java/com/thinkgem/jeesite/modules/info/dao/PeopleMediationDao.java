/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.chart.entity.PeopleMediatorVo;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.PeopleMediation;

/**
 * 人民调解员DAO接口
 * @author wanglin
 * @version 2018-05-25
 */
@MyBatisDao
public interface PeopleMediationDao extends CrudDao<PeopleMediation> {
	public int batchDelete(String[] batchid);// 批量删除

	/**
	 * 接口集合查询查询集合
	 * 
	 * @author 王鹏
	 * @version 2018-4-22 16:26:47
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);

	/**
	 * 统计调解员数量
	 * 
	 * @author 王鹏
	 * @version 2018-4-22 16:26:47
	 * @return
	 */
	int count(PeopleMediation lo);

	String findUserLoginNameById(String id);

	/**
	 * 根据身份证好查找调解员的信息，如果重复就不插入
	 * 
	 * @author admin 尹垚
	 * @version 2018-6-10 16:43:25
	 * @param cardNo
	 */
	public String findInfoByCardNo(String idCard);

	/**
	 * 法律人员详细信息
	 * 
	 * @author 尹垚
	 * @param id
	 * @return
	 */
	public AgencyVo getInfo(String id);

	/**
	 * 接口:大屏查询人民调解员信息
	 * 
	 * @author 王鹏
	 * @version 2018-6-13 21:06:09
	 * @param lv
	 * @return
	 */
	List<PeopleMediatorVo> findListForBigScreen(PeopleMediatorVo pmv);

	public List<AgencyVo> findList(AgencyVo peopleMediation);
	
	/**
	 * 更新评价值
	 * @param evaluation
	 * @param id
	 */
	public void evaluationUpdate(@Param("evaluation")String evaluation,@Param("id")String id,@Param("remark")String remark);

	public List<PeopleMediation> getPeopleMediationInfo(
			PeopleMediation peopleMediation);
}