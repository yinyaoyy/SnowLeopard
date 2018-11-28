/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.Suggestion;

/**
 * 意见反馈DAO接口
 * @author wanglin
 * @version 2018-05-12
 */
@MyBatisDao
public interface SuggestionDao extends CrudDao<Suggestion> {
	List<Suggestion> findUserSuggestionList(Suggestion suggestion);
	List<Suggestion> findUserIsInquiriesSuggestionList(Suggestion suggestion);

}