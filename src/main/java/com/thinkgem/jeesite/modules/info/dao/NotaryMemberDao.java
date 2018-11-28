/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.NotaryMember;

/**
 * 公证员DAO接口
 * @author 王鹏
 * @version 2018-04-23
 */
@MyBatisDao
public interface NotaryMemberDao extends CrudDao<NotaryMember> {

	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-4-22 16:26:47
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计公证员数量
	 * @author 王鹏
	 * @version 2018-4-22 16:26:47
	 * @return
	 */
	int count(NotaryMember lo);

    /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除
	
    /**
	 * 根据公证员人员验证
	 * @author 刘江陵
	 * @version 2018-06-10 09:52:11
	 * @param 
	 * @return
	 */
	public String personLicenseNumber(String licenseNumber);

	AgencyVo getInfo(String id);
	
	/**
	 * 更新评价值
	 * @param evaluation
	 * @param id
	 */
	public void evaluationUpdate(@Param("evaluation")String evaluation,@Param("id")String id,@Param("remark")String remark);
}