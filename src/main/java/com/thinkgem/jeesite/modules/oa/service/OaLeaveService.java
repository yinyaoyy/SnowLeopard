/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.act.utils.ActUtils;
import com.thinkgem.jeesite.modules.oa.dao.OaLeaveDao;
import com.thinkgem.jeesite.modules.oa.entity.OaLeave;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 请假流程Service
 * @author lin
 * @version 2018-02-07
 */
@Service
@Transactional(readOnly = true)
public class OaLeaveService extends CrudService<OaLeaveDao, OaLeave> {
	@Autowired
	private ActTaskService actTaskService;

	public OaLeave get(String id) {
		return super.get(id);
	}
	
	public List<OaLeave> findList(OaLeave oaLeave) {
		return super.findList(oaLeave);
	}
	
	public Page<OaLeave> findPage(Page<OaLeave> page, OaLeave oaLeave) {
		return super.findPage(page, oaLeave);
	}
	
	@Transactional(readOnly = false)
	public void save(OaLeave oaLeave) {
		User user=UserUtils.getUser();
		oaLeave.setCreateBy(user);
		// 申请发起
		if (StringUtils.isBlank(oaLeave.getId())){
			oaLeave.preInsert();
			dao.insert(oaLeave);
			// 启动流程
			Map<String, Object> vars = Maps.newHashMap();
			String []userIds=StringUtils.split(oaLeave.getOaLeaveRecordIds(), ",");
			List<String> users=Lists.newArrayList();
			for (int i = 0; i <userIds.length; i++) {
				users.add(UserUtils.get(userIds[i]).getLoginName());
			}
			vars.put("userId",users);
			actTaskService.startProcess(ActUtils.PD_LEAVE[0], ActUtils.PD_LEAVE[1], oaLeave.getId(), "请假申请流程("+user.getName()+")",vars);
		}
		
		// 重新编辑申请		
		else{
			oaLeave.preUpdate();
			dao.update(oaLeave);

			oaLeave.getAct().setComment(("yes".equals(oaLeave.getAct().getFlag())?"[重申] ":"[销毁] ")+oaLeave.getAct().getComment());
			
			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(oaLeave.getAct().getFlag())? "1" : "0");
			actTaskService.complete(oaLeave.getAct().getTaskId(), oaLeave.getAct().getProcInsId(), oaLeave.getAct().getComment(),"请假申请流程("+user.getName()+")", vars);
		}
	}
	/**
	 * 审核审批保存
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void toDo(OaLeave oaLeave) {
		Map<String, Object> vars = Maps.newHashMap();
		// 设置意见
		oaLeave.getAct().setComment(("yes".equals(oaLeave.getAct().getFlag())?"[同意] ":"[驳回] ")+oaLeave.getAct().getComment());
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaLeave.getAct().getTaskDefKey();
		// 审核环节
		if ("leave2".equals(taskDefKey)){
			claim(oaLeave.getAct());
		}
		else if ("leave5".equals(taskDefKey)){
			claim(oaLeave.getAct());
		}
		else if ("leave4".equals(taskDefKey)){
			claim(oaLeave.getAct());
		}else if ("leave3".equals(taskDefKey)){
			claim(oaLeave.getAct());
			oaLeave.preUpdate();
			dao.update(oaLeave);
			String []userIds=StringUtils.split(oaLeave.getOaLeaveRecordIds(), ",");
			List<String> users=Lists.newArrayList();
			for (int i = 0; i <userIds.length; i++) {
				users.add(UserUtils.get(userIds[i]).getLoginName());
			}
			vars.put("userId",users);
			oaLeave.getAct().setComment("");
		}
		// 未知环节，直接返回
		else{
			return;
		}
		// 提交流程任务
		vars.put("pass", "yes".equals(oaLeave.getAct().getFlag())? "1" : "0");
		actTaskService.complete(oaLeave.getAct().getTaskId(), oaLeave.getAct().getProcInsId(), oaLeave.getAct().getComment(), vars);
	}
	@Transactional(readOnly = false)
	public void delete(OaLeave oaLeave) {
		super.delete(oaLeave);
	}
	private void claim(Act act){
		String userId = UserUtils.getUser().getLoginName();
		actTaskService.claim(act.getTaskId(), userId);
	}

}