/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.NodeManager;

/**
 * 流程节点DAO接口
 * @author wanglin
 * @version 2018-06-06
 */
@MyBatisDao
public interface NodeManagerDao extends CrudDao<NodeManager> {
 public	String findNewVersion(NodeManager nodeManager);
}