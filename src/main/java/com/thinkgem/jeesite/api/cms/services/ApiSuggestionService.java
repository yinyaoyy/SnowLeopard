package com.thinkgem.jeesite.api.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.cms.dao.SuggestionDao;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;
import com.thinkgem.jeesite.modules.cms.entity.Suggestion;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentReService;
import com.thinkgem.jeesite.modules.cms.service.SuggestionService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentService;
/**
 * api站点service层
 * @author wanglin
 * @create 2018-04-18 上午10:00
 */
@Service
@Transactional(readOnly = true)
public class ApiSuggestionService {
	@Autowired
	private SuggestionService suggestionService;
	@Autowired
	private SuggestionDao suggestionDao;
	@Autowired
    private GuestbookCommentService guestbookCommentService;
	@Autowired
	private GuestbookCommentReService  guestbookCommentReService;


	public Page<Suggestion> findUserSuggestionList(Page<Suggestion> page, Suggestion suggestion) {
		return suggestionService.findUserSuggestionList(page,suggestion);
	}
	public Page<Suggestion> findUserIsInquiriesSuggestionList(Page<Suggestion> page, Suggestion suggestion) {
		return suggestionService.findUserIsInquiriesSuggestionList(page, suggestion);
	}
	@Transactional(readOnly = false)
    public void addSuggestion(Suggestion Suggestion){
		suggestionService.save(Suggestion);
    }
	public void addSuggestionComment(GuestbookCommentRe guestbookCommentRe){
		guestbookCommentReService.save(guestbookCommentRe);
		Suggestion suggestion=new Suggestion();
		suggestion.setId(guestbookCommentRe.getGuestbookId());
		suggestion.setIsInquiries("1");
		suggestionDao.update(suggestion); 
	}

}
