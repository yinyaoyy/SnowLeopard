/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.act.dao;


import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.act.entity.Act;

/**
 * 审批DAO接口
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface ActDao extends CrudDao<Act> {

	public int updateProcInsIdByBusinessId(Act act);
	

	/**
	 * 流程启动之后手动添加申请人的节点信息，是申请人可以在申请后，在已完成列表中找到相应的信息
	 * @param  param [description]
	 * @return       [description]
	 */
	public int insertApplyToHiTask(Map<String, Object> param);


	/**
	 * 获取当前登录人受理中/已归档的项目列表信息
	 * @param  page      [description]
	 * @param  beginDate [description]
	 * @param  endDate   [description]
	 * @param  caseTitle [description]
	 * @param  user      [description]
	 * @return           [description]
	 */
	public List<Act> getArchives(Act act);
	
	/**
	 * 根据任务id查询评论内容
	 * @author 王鹏
	 * @version 2018-07-08 17:55:48
	 * @param taskId
	 * @return
	 */
	public List<String> getCommentsByTaskId(String taskId);
	
	
	/**
	 * 流程作废时修改当前环节数据
	 * @param param
	 */
	public void updateTask(Map<String, Object> param);
	
	/**
	 * 流程作废时修改act_hi_actinst数据
	 * @param param
	 */
	public void updateActinst(Map<String, Object> param);
	
	/**
	 * 流程作废时插入act_hi_comment数据
	 * @param param
	 */
	public void insertActhicomment(Map<String, Object> param);

}
