/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
	/**
	 * 根据指定条件查询相应机构
	 * @author 王鹏
	 * @version 2018-05-17 10:34:05
	 * @param office
	 * @return
	 */
	List<Office> findListByCondition(Office office);
	Office getByParentAndName(Office office);
	List<Office> findIdName (Office office);
	Office findInfoByNameAndArea(@Param("name")String name, @Param("id")String id);
	List<Office>  selectParent(Office office);
	
}
