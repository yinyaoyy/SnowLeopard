/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tripartite.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.tripartite.entity.TripartiteParamConfig;
import com.thinkgem.jeesite.tripartite.util.TripartiteTypeEnum;
import com.thinkgem.jeesite.modules.tripartite.dao.TripartiteParamConfigDao;

/**
 * 与第三方系统对接请求头、参数配置表Service
 * @author 王鹏
 * @version 2018-06-30
 */
@Service
@Transactional(readOnly = true)
public class TripartiteParamConfigService extends CrudService<TripartiteParamConfigDao, TripartiteParamConfig> {

	public TripartiteParamConfig get(String id) {
		return super.get(id);
	}
	
	public List<TripartiteParamConfig> findList(TripartiteParamConfig tripartiteParamConfig) {
		return super.findList(tripartiteParamConfig);
	}

	public List<TripartiteParamConfig> findAllParamList(TripartiteParamConfig tripartiteParamConfig) {
		List<TripartiteParamConfig> list = Lists.newArrayList();
		if(TripartiteTypeEnum.SYSTEM_URL.getType().equals(tripartiteParamConfig.getParent().getType())) {
			//如果是系统，还需获取请求头设置
			tripartiteParamConfig.setType(TripartiteTypeEnum.SYSTEM_HEADER.getType());
			list.addAll(findList(tripartiteParamConfig));
		}
		//获取通用参数和接口参数
		tripartiteParamConfig.setType(TripartiteTypeEnum.SYSTEM_PARAM.getType());
		list.addAll(findList(tripartiteParamConfig)); 
		tripartiteParamConfig.setType(TripartiteTypeEnum.API_PARAM.getType());
		list.addAll(findList(tripartiteParamConfig)); 
		return list;
	}
	
	public Page<TripartiteParamConfig> findPage(Page<TripartiteParamConfig> page, TripartiteParamConfig tripartiteParamConfig) {
		return super.findPage(page, tripartiteParamConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(TripartiteParamConfig tripartiteParamConfig) {
		super.save(tripartiteParamConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(TripartiteParamConfig tripartiteParamConfig) {
		super.delete(tripartiteParamConfig);
	}
	
}