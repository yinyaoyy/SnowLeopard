/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.ServerSourceManage;

/**
 * 服务数据资源的对应管理DAO接口
 * @author hejia
 * @version 2018-04-25
 */
@MyBatisDao
public interface ServerSourceManageDao extends CrudDao<ServerSourceManage> {
	
	public int insertServerRole(ServerSourceManage server);
	
	public List<ServerSourceManage> findByServiceId(List<String> list);

	/**
	 * 查询所有子集服务/资源
	 * @author 王鹏
	 * @version 2018-05-07 17:16:12
	 * @param serverSourceManage
	 * @return
	 */
	public List<ServerSourceManage> findByPids(ServerSourceManage serverSourceManage);
	
	/**
	 * 修改上级服务
	 * @author 王鹏
	 * @version 2018-05-07 17:17:00
	 * @param serverSourceManage
	 * @return
	 */
	public int updatePids(ServerSourceManage serverSourceManage);
}