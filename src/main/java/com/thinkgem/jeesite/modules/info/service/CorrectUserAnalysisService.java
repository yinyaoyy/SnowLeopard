/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.info.entity.CorrectUserAnalysis;
import com.thinkgem.jeesite.modules.info.entity.CorrectUserAnalysisVo;
import com.thinkgem.jeesite.modules.info.dao.CorrectUserAnalysisDao;

/**
 * 社区矫正心理生理分析Service
 * @author wanglin
 * @version 2018-07-28
 */
@Service
@Transactional(readOnly = true)
public class CorrectUserAnalysisService extends CrudService<CorrectUserAnalysisDao, CorrectUserAnalysis> {

	public CorrectUserAnalysis get(String id) {
		return super.get(id);
	}
	
	public List<CorrectUserAnalysis> findList(CorrectUserAnalysis correctUserAnalysis) {
		return super.findList(correctUserAnalysis);
	}
	
	public Page<CorrectUserAnalysisVo> findPageVo(Page<CorrectUserAnalysis> page, CorrectUserAnalysis correctUserAnalysis) {
		page=super.findPage(page, correctUserAnalysis);
		List<CorrectUserAnalysisVo> listVo=Lists.newArrayList();
		List<CorrectUserAnalysis> list=page.getList();
		for (CorrectUserAnalysis correctUserAnalysis2 : list) {
			boolean flag=false;
			for (CorrectUserAnalysisVo correctUserAnalysisVo : listVo) {
				if(correctUserAnalysisVo.getAnalysisDate().getTime()==correctUserAnalysis2.getAnalysisDate().getTime()) {
					correctUserAnalysisVo.getList().add(correctUserAnalysis2);
					flag=true;
					break;
				}
			}
			if(!flag){
				CorrectUserAnalysisVo correctUserAnalysisVo=new CorrectUserAnalysisVo();	
				correctUserAnalysisVo.setAnalysisDate(correctUserAnalysis2.getAnalysisDate());
				correctUserAnalysisVo.setErrorRate(correctUserAnalysis2.getErrorRate());
				correctUserAnalysisVo.getList().add(correctUserAnalysis2);
				listVo.add(correctUserAnalysisVo);
			}
		}
		Page<CorrectUserAnalysisVo> pageVo=new Page<CorrectUserAnalysisVo>(page.getPageNo(),page.getPageSize(),page.getCount(),listVo);
		return pageVo;
	}
	/*public Page<CorrectUserAnalysisVo> findPageVo(Page<CorrectUserAnalysisVo> page, CorrectUserAnalysisVo correctUserAnalysis) {
		correctUserAnalysis.setPage(page);
		list.
		page.setList(dao.findList(entity));
		
	}*/
	@Transactional(readOnly = false)
	public void save(CorrectUserAnalysis correctUserAnalysis) {
		super.save(correctUserAnalysis);
	}
	
	@Transactional(readOnly = false)
	public void delete(CorrectUserAnalysis correctUserAnalysis) {
		super.delete(correctUserAnalysis);
	}
	
}