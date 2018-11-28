/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;

/**
 * 留言回复DAO接口
 * @author wanglin
 * @version 2018-05-07
 */
@MyBatisDao
public interface GuestbookCommentReDao extends CrudDao<GuestbookCommentRe> {
	//是否有追问
	public Integer count(String id);
	//删除指定的追答
	public void deletecommentre(String id);
	
}