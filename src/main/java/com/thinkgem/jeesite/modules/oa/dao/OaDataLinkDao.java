/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.OaDataLink;

/**
 * 记录流程数据DAO接口
 * @author zhangqiang
 * @version 2018-05-28
 */
@MyBatisDao
public interface OaDataLinkDao extends CrudDao<OaDataLink> {
	/**
	 * 获取数据Id
	 * @param businessKey 流程实例id
	 * @param tableName 表名
	 * @return 对应的表的数据Id
	 */
	public OaDataLink getDataId(String businessKey,String tableName);

	public int getCountByDataId(String dataId);
	
}