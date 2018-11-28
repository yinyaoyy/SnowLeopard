/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.NotaryAgency;

/**
 * 公证机构DAO接口
 * @author 王鹏
 * @version 2018-04-23
 */
@MyBatisDao
public interface NotaryAgencyDao extends CrudDao<NotaryAgency> {

	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-5-3 17:56:29
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	
	/**
	 * 统计公证处数量
	 * @author 王鹏
	 * @version 2018-5-3 17:56:29
	 * @return
	 */
	int count(NotaryAgency lo);

    /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(String[] batchid);//批量删除
    
    /**
	 * 根据机构证号、地区验证机构
	 * @author 刘江陵
	 * @version 2018-06-10 09:52:11
	 * @param infoDataId
	 * @return
	 */
	public String personInstitution(@Param("licenseNumber")String licenseNumber,@Param("areaId")String areaId);

	AgencyVo getInfo(String id);
}