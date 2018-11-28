/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.PrisonUser;

/**
 * 在监服刑人员DAO接口
 * @author liujiangling
 * @version 2018-06-21
 */
@MyBatisDao
public interface PrisonUserDao extends CrudDao<PrisonUser> {
	
	/**
	 * 接口集合查询查询集合
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计在监服刑人员数量
	 * @author
	 * @version 
	 * @return
	 */
	int count(PrisonUser prisonUser);

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
	 * @version
	 * @param infoDataId
	 * @return
	 */
	public String personIdCard(@Param("IdCard") String IdCard);
}