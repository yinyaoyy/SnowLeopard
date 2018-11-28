/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.InfoLegalServiceOffice;

/**
 * 基层法律服务所DAO接口
 * @author hejia
 * @version 2018-04-24
 */
@MyBatisDao
public interface InfoLegalServiceOfficeDao extends CrudDao<InfoLegalServiceOffice> {

	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-4-26 10:02:11
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 基层法律服务所数量
	 * @author 王鹏
	 * @version 2018-4-26 10:02:11
	 * @return
	 */
	int count(InfoLegalServiceOffice lo);

    /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除
    
    AgencyVo getById(String id);
    
    /**
	 * 根据机构证号、地区验证机构
	 * @author 刘江陵
	 * @version 2018-06-10 09:52:11
	 * @param infoDataId
	 * @return
	 */
	public String personInstitution(@Param("no")String no,@Param("areaId")String areaId,@Param("townId")String townId);
}