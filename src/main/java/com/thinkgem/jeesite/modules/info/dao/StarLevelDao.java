/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.CorrectUser;
import com.thinkgem.jeesite.modules.info.entity.StarLevel;

/**
 * 社区矫正人员DAO接口
 * @author liujiangling
 * @version 2018-06-25
 */
@MyBatisDao
public interface StarLevelDao extends CrudDao<CorrectUser> {
	/**
	 * 律师
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<StarLevel> findListByLawyer(StarLevel av);
	
	/**
	 * 公证员
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<StarLevel> findListByNotaryMeber(StarLevel av);
	
	/**
	 * 基层法律服务者
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<StarLevel> findListByLegalServicePerson(StarLevel av);
	
	/**
	 * 司法所工作人员
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<StarLevel> findListByJudiciaryUser(StarLevel av);
	
	/**
	 * 法援
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<StarLevel> findListByLawAssitanceUser(StarLevel av);
	
	/**
	 * 调解员
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<StarLevel> findListByPeopleMediation(StarLevel av);
	
	/**
	 * 地区查询
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<StarLevel> findListByArea(StarLevel av);
	
	/**
	 * 机构查询
	 * @author 
	 * @version 
	 * @param af
	 * @return
	 */
	List<StarLevel> findListByOffice(StarLevel av);
	
	String area(String id);
	
	String office(String id);
}