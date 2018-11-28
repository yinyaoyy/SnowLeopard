/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.LegalServicePerson;

/**
 * 基层法律服务工作者DAO接口
 * @author 王鹏
 * @version 2018-05-09
 */
@MyBatisDao
public interface LegalServicePersonDao extends CrudDao<LegalServicePerson> {

	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-5-9 11:47:41
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计律师数量
	 * @author 王鹏
	 * @version 2018-5-9 11:47:45
	 * @return
	 */
	int count(LegalServicePerson lo);

    /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除
    AgencyVo getById(String id);
    
    /**
	 * 根据身份证人员验证
	 * @author 刘江陵
	 * @version 2018-06-10 09:52:11
	 * @param infoDataId
	 * @return
	 */
	public String personIdCard(String IdCard);
	
	/**
	 * 更新评价值
	 * @param evaluation
	 * @param id
	 */
	public void evaluationUpdate(@Param("evaluation")String evaluation,@Param("id")String id,@Param("remark")String remark);
}