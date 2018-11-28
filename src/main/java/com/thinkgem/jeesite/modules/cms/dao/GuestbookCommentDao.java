/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookComment;

/**
 * 留言回复DAO接口
 * @author wanglin
 * @version 2018-04-25
 */
@MyBatisDao
public interface GuestbookCommentDao extends CrudDao<GuestbookComment> {
	public void updateThumbsUp(GuestbookComment guestbookComment);
	public GuestbookComment findget(String id);
	
	
	/**
	 * 修改回复
	 * @param id
	 */
	public void updateContent(GuestbookComment guestbookComment);
	/**
	 * 接口更新是否评价
	 * @param id
	 */
	void isEvaluate(String id);
	
	
	/**
	 * 根据id删除回复
	 * @param id
	 */
	public void deleteGuestbookComment(String id);
	
	/**
	 * 查看此消息下还有没有回复
	 * @param id
	 */
	public Integer countselect(String id);
}