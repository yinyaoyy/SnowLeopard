/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookEvaluate;
import com.thinkgem.jeesite.modules.cms.dao.GuestbookEvaluateDao;

/**
 * 留言评价Service
 * @author wanglin
 * @version 2018-05-09
 */
@Service
@Transactional(readOnly = true)
public class GuestbookEvaluateService extends CrudService<GuestbookEvaluateDao, GuestbookEvaluate> {

	public GuestbookEvaluate get(String id) {
		return super.get(id);
	}
	
	public List<GuestbookEvaluate> findList(GuestbookEvaluate guestbookEvaluate) {
		return super.findList(guestbookEvaluate);
	}
	
	public Page<GuestbookEvaluate> findPage(Page<GuestbookEvaluate> page, GuestbookEvaluate guestbookEvaluate) {
		return super.findPage(page, guestbookEvaluate);
	}
	
	@Transactional(readOnly = false)
	public void save(GuestbookEvaluate guestbookEvaluate) {
		super.save(guestbookEvaluate);
	}
	
	@Transactional(readOnly = false)
	public void delete(GuestbookEvaluate guestbookEvaluate) {
		super.delete(guestbookEvaluate);
	}
	public int getEvaluatedByUser(GuestbookEvaluate guestbookEvaluate){
		return dao.getEvaluatedByUser(guestbookEvaluate);
	}
}