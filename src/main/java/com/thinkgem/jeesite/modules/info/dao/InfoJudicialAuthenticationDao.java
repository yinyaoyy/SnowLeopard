/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.InfoJudicialAuthentication;

import java.util.List;

/**
 * 司法鉴定所管理DAO接口
 * @author hejia
 * @version 2018-04-23
 */
@MyBatisDao
public interface InfoJudicialAuthenticationDao extends CrudDao<InfoJudicialAuthentication> {

    /**
     * 根据区域id统计司法鉴定所数量
     * @param judicialAuthentication
     * @return
     */
    Integer countByAreaId(InfoJudicialAuthentication judicialAuthentication);
    /**
     * 根据司法鉴定所名称查找鉴定所
     */
    List<InfoJudicialAuthentication> findByName(String name);
	/**
	 * 接口集合查询查询集合
	 * @author 王鹏
	 * @version 2018-4-25 17:58:33
	 * @param af
	 * @return
	 */
	List<AgencyVo> findListForApi(AgencyVo av);
	AgencyVo getInfo(String id);
}