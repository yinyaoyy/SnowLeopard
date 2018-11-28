/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao;

import java.util.List;


import com.thinkgem.jeesite.api.act.entity.ProcessStateVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.OaProcessState;

/**
 * 流程状态DAO接口
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OaProcessStateDao extends CrudDao<OaProcessState> {

	public String getById(String id);
	
	/**
	 * 更新是否允许作废
	 * @param procInsId
	 */
	public void end(String procInsId);
	/**
	 * 查询是否允许作废
	 * @param procInsId
	 * @return
	 */
	public String getByIsDestroy(String procInsId);
	
	/**
	 * 综合查询
	 * @param processStateVo
	 * @return
	 */
	public List<ProcessStateVo> comprehensiveQueryForApi(ProcessStateVo processStateVo);
	
	/**
	 * 综合查询的数量
	 * @param processStateVo
	 * @return
	 */
	public int comprehensiveQueryCountForApi(ProcessStateVo processStateVo);
	
	/**
	 * 统计分析
	 * @param processStateVo
	 * @return
	 */
	public List<ProcessStateVo> statisticsAnalysis(ProcessStateVo processStateVo);
	/**
	 * 承办人一共办理了多少案件的统计
	 * @param processStateVo
	 * @return
	 */
	public List<ProcessStateVo> contractorQuery(ProcessStateVo processStateVo);
	
	/**
	 * 根据procInsId获取信息
	 * @param procInsId
	 * @return
	 */
	public OaProcessState getByProcInsId(String procInsId);
	
}
