/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.entity.Suggestion;
import com.thinkgem.jeesite.modules.cms.dao.SuggestionDao;

/**
 * 意见反馈Service
 * @author wanglin
 * @version 2018-05-12
 */
@Service
@Transactional(readOnly = true)
public class SuggestionService extends CrudService<SuggestionDao, Suggestion> {

	public Suggestion get(String id) {
		return super.get(id);
	}
	
	public List<Suggestion> findList(Suggestion suggestion) {
		return super.findList(suggestion);
	}
	
	public Page<Suggestion> findPage(Page<Suggestion> page, Suggestion suggestion) {
		return super.findPage(page, suggestion);
	}
	
	@Transactional(readOnly = false)
	public void save(Suggestion suggestion) {
		super.save(suggestion);
	}
	
	@Transactional(readOnly = false)
	public void delete(Suggestion suggestion) {
		super.delete(suggestion);
	}
	public Page<Suggestion> findUserSuggestionList(Page<Suggestion> page, Suggestion suggestion) {
		suggestion.setPage(page);
		page.setList(dao.findUserSuggestionList(suggestion));
		return page;
	}
	public Page<Suggestion> findUserIsInquiriesSuggestionList(Page<Suggestion> page, Suggestion suggestion) {
		suggestion.setPage(page);
		page.setList(dao.findUserIsInquiriesSuggestionList(suggestion));
		return page;
	}

}