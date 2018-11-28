/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.appmange.entity.SysService;

/**
 * web服务DAO接口
 * @author wanglin
 * @version 2018-04-23
 */
@MyBatisDao
public interface SysServiceDao extends CrudDao<SysService> {
	public List<SysService> findListByType(@Param("id")String id,@Param("queryId")String queryId,@Param("type")String type);
	public int batchUpdate(List<SysService> list);
	/**
	 * 通过人员机构id获取服务
	 * @param officeId
	 * @return
	 */
	public SysService findByOfficeId(@Param("officeId")String officeId);
	public List<SysService> findBySiteIds(List<String> list);
}