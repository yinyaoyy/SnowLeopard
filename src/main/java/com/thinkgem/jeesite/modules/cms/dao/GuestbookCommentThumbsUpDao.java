/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentThumbsUp;

/**
 * 留言点赞DAO接口
 * @author wanglin
 * @version 2018-05-07
 */
@MyBatisDao
public interface GuestbookCommentThumbsUpDao extends CrudDao<GuestbookCommentThumbsUp> {
	
	int findThumbsUpByCommentId(GuestbookCommentThumbsUp guestbookCommentThumbsUp);

	public void updateDelFlag(GuestbookCommentThumbsUp guestbookCommentThumbsUp);

	void deleteByCommentIdAndUserId(GuestbookCommentThumbsUp guestbookCommentThumbsUp);
}