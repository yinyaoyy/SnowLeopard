/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tripartite.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.tripartite.entity.TripartiteSystemConfig;
import com.thinkgem.jeesite.modules.tripartite.dao.TripartiteSystemConfigDao;

/**
 * 与第三方系统对接配置表Service
 * @author 王鹏
 * @version 2018-06-23
 */
@Service
@Transactional(readOnly = true)
public class TripartiteSystemConfigService extends CrudService<TripartiteSystemConfigDao, TripartiteSystemConfig> {

	public TripartiteSystemConfig get(String id) {
		return super.get(id);
	}
	
	public List<TripartiteSystemConfig> findList(TripartiteSystemConfig tripartiteSystemConfig) {
		return super.findList(tripartiteSystemConfig);
	}
	
	/**
	 * 根据类型查询配置信息
	 * @author 王鹏
	 * @version 2018-06-23 20:17:30
	 * @param tripartiteSystemConfig
	 * @return
	 */
	public List<TripartiteSystemConfig> findListByType(String type) {
		TripartiteSystemConfig tripartiteSystemConfig = new TripartiteSystemConfig();
		tripartiteSystemConfig.setType(type);
		return super.findList(tripartiteSystemConfig);
	}
	/**
	 * 根据系统id查询下边的任务信息
	 * @author 王鹏
	 * @version 2018-6-24 16:35:27
	 * @param systemId 系统id
	 * @param hasSystem 是否包含系统信息(若包含则排在第一位)
	 * @return
	 */
	public List<TripartiteSystemConfig> findTaskBySystemId(String systemId, boolean hasSystem) {
		//根据系统id查询接口信息
		TripartiteSystemConfig tsc = new TripartiteSystemConfig();
		tsc.setParent(new TripartiteSystemConfig(systemId));
		tsc.setType("2");//查询接口
		List<TripartiteSystemConfig> list = super.findList(tsc);
		if(hasSystem) {//补充系统信息
			list.add(0, super.get(systemId));
		}
		return list;
	}
	
	public Page<TripartiteSystemConfig> findPage(Page<TripartiteSystemConfig> page, TripartiteSystemConfig tripartiteSystemConfig) {
		return super.findPage(page, tripartiteSystemConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(TripartiteSystemConfig tripartiteSystemConfig) {
		super.save(tripartiteSystemConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(TripartiteSystemConfig tripartiteSystemConfig) {
		super.delete(tripartiteSystemConfig);
	}
	
}