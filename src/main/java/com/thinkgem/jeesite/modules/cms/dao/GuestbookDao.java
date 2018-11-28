/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.Guestbook;
import com.thinkgem.jeesite.modules.cms.entity.Site;

/**
 * 留言DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface GuestbookDao extends CrudDao<Guestbook> {
	public List<Guestbook>findList(Guestbook guestbook,List<Site> siteList);
	public List<Guestbook> findUserList(Guestbook guestbook);
	public List<Guestbook> findUserIsInquiriesList(Guestbook guestbook);
	public List<T> getApi(String id);
	/**
	 * 接口访问统计量
	 * @param id
	 */
	public void statisticNum(String id);
}
 