/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.cms.dao.GuestbookCommentReDao;

/**
 * 留言回复Service
 * @author wanglin
 * @version 2018-05-07
 */
@Service
@Transactional(readOnly = true)
public class GuestbookCommentReService extends CrudService<GuestbookCommentReDao, GuestbookCommentRe> {

	public GuestbookCommentRe get(String id) {
		return super.get(id);
	}
	
	public List<GuestbookCommentRe> findList(GuestbookCommentRe guestbookCommentRe) {
		return super.findList(guestbookCommentRe);
	}
	
	public Page<GuestbookCommentRe> findPage(Page<GuestbookCommentRe> page, GuestbookCommentRe guestbookCommentRe) {
		return super.findPage(page, guestbookCommentRe);
	}
	
	@Transactional(readOnly = false)
	public void save(GuestbookCommentRe guestbookCommentRe) {
		User user=UserUtils.getUser();
		
		if (guestbookCommentRe.getIsNewRecord()){
			guestbookCommentRe.setCreateDate(new Date());
			guestbookCommentRe.setReUser(user.getName());
			guestbookCommentRe.preInsert();
			dao.insert(guestbookCommentRe);
		}
		else {
			guestbookCommentRe.setUpdateBy(user);
			guestbookCommentRe.setUpdateDate(new Date());
			dao.update(guestbookCommentRe);
		}
		
	}
	
	@Transactional(readOnly = false)
	public void delete(GuestbookCommentRe guestbookCommentRe) {
		super.delete(guestbookCommentRe);
	}
	public Integer count(String id) {
		Integer ff=dao.count(id);
		return ff;
	}
	@Transactional(readOnly = false)
	public void deletecommentre(String id) {
		dao.deletecommentre(id);
	}
}