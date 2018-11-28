/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.SysUserPush;

/**
 * 用户未在线时的的消息保存DAO接口
 * @author 尹垚
 * @version 2018-07-12
 */
@MyBatisDao
public interface SysUserPushDao extends CrudDao<SysUserPush> {

	void deleteById(@Param("id")String id);
	void insertList(List<SysUserPush> pushs);
	List<SysUserPush> getByReceivedId(@Param("id")String id);
	void updateByReceiveId(SysUserPush push);
	void updateStatusById(@Param("id")String id);
	int findNoReadList(@Param("userId")String userId);
	List<SysUserPush> findMesList(SysUserPush sysUserPush);
	void changeReadToAll(@Param("userId")String userId);
	void changeReadStatusById(String id);
	
}