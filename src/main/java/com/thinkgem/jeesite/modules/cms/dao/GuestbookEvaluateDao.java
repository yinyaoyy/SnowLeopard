/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookEvaluate;

/**
 * 留言评价DAO接口
 * @author wanglin
 * @version 2018-05-09
 */
@MyBatisDao
public interface GuestbookEvaluateDao extends CrudDao<GuestbookEvaluate> {
	public int getEvaluatedByUser(GuestbookEvaluate guestbookEvaluate);//获取某个人的评价平均值（已经四舍五入后）
}