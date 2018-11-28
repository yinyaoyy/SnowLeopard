/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.CorrectUser;
import com.thinkgem.jeesite.modules.info.entity.PrisonUser;

/**
 * 社区矫正人员DAO接口
 * @author liujiangling
 * @version 2018-06-25
 */
@MyBatisDao
public interface CorrectUserDao extends CrudDao<CorrectUser> {
	/**
	 * 接口集合查询查询集合
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计社区矫正人员数量
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
    CorrectUser getByIdCard(String idCard);//通过身份证号获取详情
}