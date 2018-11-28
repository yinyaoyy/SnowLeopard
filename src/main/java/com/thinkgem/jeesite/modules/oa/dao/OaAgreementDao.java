/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.OaAgreement;

/**
 * 三定方案DAO接口
 * @author liujiangling
 * @version 2018-06-22
 */
@MyBatisDao
public interface OaAgreementDao extends CrudDao<OaAgreement> {
	/**
	 * 删除三定方案意见
	 */
	public int deletes(@Param("id")String id);
}