/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.JudiciaryUser;

/**
 * 司法所工作人员DAO接口
 * @author wanglin
 * @version 2018-06-10
 */
@MyBatisDao
public interface JudiciaryUserDao extends CrudDao<JudiciaryUser> {
	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-4-22 16:26:47
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计司法所工作人员数量
	 * @author 王鹏
	 * @version 2018-4-22 16:26:47
	 * @return
	 */
	int count(JudiciaryUser judiciaryUser);

    /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除
    AgencyVo getById(String id);
    
    /**
	 * 更新评价值
	 * @param evaluation
	 * @param id
	 */
    public void evaluationUpdate(@Param("evaluation")String evaluation,@Param("id")String id,@Param("remark")String remark);
}