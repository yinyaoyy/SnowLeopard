/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentThumbsUp;
import com.thinkgem.jeesite.modules.cms.dao.GuestbookCommentThumbsUpDao;

/**
 * 留言点赞Service
 * @author wanglin
 * @version 2018-05-07
 */
@Service
@Transactional(readOnly = true)
public class GuestbookCommentThumbsUpService extends CrudService<GuestbookCommentThumbsUpDao, GuestbookCommentThumbsUp> {

	public GuestbookCommentThumbsUp get(String id) {
		return super.get(id);
	}
	
	public List<GuestbookCommentThumbsUp> findList(GuestbookCommentThumbsUp guestbookCommentThumbsUp) {
		return super.findList(guestbookCommentThumbsUp);
	}
	
	public Page<GuestbookCommentThumbsUp> findPage(Page<GuestbookCommentThumbsUp> page, GuestbookCommentThumbsUp guestbookCommentThumbsUp) {
		return super.findPage(page, guestbookCommentThumbsUp);
	}
	
	@Transactional(readOnly = false)
	public void save(GuestbookCommentThumbsUp guestbookCommentThumbsUp) {
		super.save(guestbookCommentThumbsUp);
	}
	
	@Transactional(readOnly = false)
	public void delete(GuestbookCommentThumbsUp guestbookCommentThumbsUp) {
		super.delete(guestbookCommentThumbsUp);
	}
	/**
	 * 根据commentId的评论数量
	 * @param commentId
	 * @return
	 */
	@Transactional(readOnly = true)
	public int findThumbsUpByCommentId(GuestbookCommentThumbsUp guestbookCommentThumbsUp) {
		return dao.findThumbsUpByCommentId(guestbookCommentThumbsUp);
	}
	
	public void updateThumbsUpDelFlag(GuestbookCommentThumbsUp guestbookCommentThumbsUp) {
		dao.updateDelFlag(guestbookCommentThumbsUp);
	}
	
	public void deleteByCommentIdAndUserId(GuestbookCommentThumbsUp guestbookCommentThumbsUp){
		dao.deleteByCommentIdAndUserId(guestbookCommentThumbsUp);
	}
	
}