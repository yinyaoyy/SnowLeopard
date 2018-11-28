/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 区域DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {

    /**
     * 根据地区名称查找地区
     * @param name
     * @return
     */
    List<Area> findByName(String name);
    
    /**
	 * 根据parent_ids查询锡林格勒盟及旗下区县
	 * @param aparentId
	 * @param bparentId
	 * @return
	 */
    List<Area> findQxList(@Param("aparentId")String aparentId,@Param("bparentId")String bparentId);
}
