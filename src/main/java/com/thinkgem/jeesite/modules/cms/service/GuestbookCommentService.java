/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookComment;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.cms.dao.GuestbookCommentDao;

/**
 * 留言回复Service
 * @author wanglin
 * @version 2018-04-25
 */
@Service
@Transactional(readOnly = true)
public class GuestbookCommentService extends CrudService<GuestbookCommentDao, GuestbookComment> {
  
	@Autowired
  public GuestbookCommentReService guestbookCommentReService;
	
	public GuestbookComment get(String id) {
		return super.get(id);
	}
	public GuestbookComment findget(String id) {
		return dao.findget(id);
	}
	
	public List<GuestbookComment> findList(GuestbookComment guestbookComment) {
		return super.findList(guestbookComment);
	}
	
	public Page<GuestbookComment> findPage(Page<GuestbookComment> page, GuestbookComment guestbookComment) {
		return super.findPage(page, guestbookComment);
	}
	
	@Transactional(readOnly = false)
	public void save(GuestbookComment guestbookComment) {
		super.save(guestbookComment);
	}
	
	@Transactional(readOnly = false)
	public void delete(GuestbookComment guestbookComment) {
		super.delete(guestbookComment);
	}
	

	@Transactional(readOnly = false)
	public String delete(String  id) {
		String massage="";
		if(guestbookCommentReService.count(id)>0) {
			massage="nn";
		}else {
			dao.deleteGuestbookComment(id);
		}
		
		return massage;
	}
	@Transactional(readOnly = false)
	public void updateThumbsUp(GuestbookComment guestbookComment){
		dao.updateThumbsUp(guestbookComment);
	}
	
	
	@Transactional(readOnly = false)
	public void updateContent(GuestbookComment guestbookComment){
		dao.updateContent(guestbookComment);
	}
	/**
	 * 更新是否评价
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void isEvaluate(String id){
		dao.isEvaluate(id);
	}
	
}