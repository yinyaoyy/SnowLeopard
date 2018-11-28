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
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.act.utils.ActUtils;
import com.thinkgem.jeesite.modules.oa.entity.OaGongchu;
import com.thinkgem.jeesite.modules.oa.dao.OaGongchuDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 公出单Service
 * @author lin
 * @version 2018-02-28
 */
@Service
@Transactional(readOnly = true)
public class OaGongchuService extends CrudService<OaGongchuDao, OaGongchu> {
	@Autowired
	private ActTaskService actTaskService;

	public OaGongchu get(String id) {
		return super.get(id);
	}
	
	public List<OaGongchu> findList(OaGongchu oaGongchu) {
		return super.findList(oaGongchu);
	}
	
	public Page<OaGongchu> findPage(Page<OaGongchu> page, OaGongchu oaGongchu) {
		return super.findPage(page, oaGongchu);
	}
	
	@Transactional(readOnly = false)
	public void save(OaGongchu oaGongchu) {
		User user=UserUtils.getUser();
		oaGongchu.setCreateBy(user);
		// 申请发起
		if (StringUtils.isBlank(oaGongchu.getId())){
			oaGongchu.preInsert();
			dao.insert(oaGongchu);
			// 启动流程
			Map<String, Object> vars = Maps.newHashMap();
			List<String> users=Lists.newArrayList();
			String []userIds=StringUtils.split(oaGongchu.getOaGongchuRecordIds(), ",");
			for (int i = 0; i <userIds.length; i++) {
				users.add(UserUtils.get(userIds[i]).getLoginName());
			}
			vars.put("assigneeList",users);
			actTaskService.startProcess(ActUtils.PD_GONGCHU[0], ActUtils.PD_GONGCHU[1], oaGongchu.getId(), "公出单流程("+user.getName()+")",vars);
		}
		
		// 重新编辑申请		
	  else{
		    oaGongchu.preUpdate();
			Map<String, Object> vars = Maps.newHashMap();
			if("yes".equals(oaGongchu.getAct().getFlag())){
				oaGongchu.getAct().setComment("[重申] "+oaGongchu.getAct().getComment());
				vars.put("pass","1");
				oaGongchu.setAgreeCount(0);
				oaGongchu.setAllCount(0);
				List<String> users=Lists.newArrayList();
				String []userIds=StringUtils.split(oaGongchu.getOaGongchuRecordIds(), ",");
				for (int i = 0; i <userIds.length; i++) {
					users.add(UserUtils.get(userIds[i]).getLoginName());
				}
				vars.put("assigneeList",users);
			}else{
				oaGongchu.getAct().setComment("[销毁] "+oaGongchu.getAct().getComment());
				vars.put("pass",  "0");
			}
			dao.update(oaGongchu);
			actTaskService.complete(oaGongchu.getAct().getTaskId(), oaGongchu.getAct().getProcInsId(), oaGongchu.getAct().getComment(),"公出单申请流程("+user.getName()+")", vars);
		}
	}
	/**
	 * 审核审批保存
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void toDo(OaGongchu oaGongchu) {
		//User user=UserUtils.getUser();
		Map<String, Object> vars = Maps.newHashMap();
		// 设置意见
		oaGongchu.getAct().setComment(("yes".equals(oaGongchu.getAct().getFlag())?"[同意] ":"[驳回] ")+oaGongchu.getAct().getComment());
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaGongchu.getAct().getTaskDefKey();
		if("gongchu2".equals(taskDefKey)){
			int allCount=oaGongchu.getAllCount()+1;
			int agreeCount=oaGongchu.getAgreeCount();
			oaGongchu.setAllCount(allCount);
			if("yes".equals(oaGongchu.getAct().getFlag())){
				oaGongchu.setAgreeCount(agreeCount+1);
			}
			if(allCount==4&&oaGongchu.getAgreeCount()==4){
				vars.put("renshi","thinkgem");
				vars.put("pass", "1");
			}else if(allCount==4&&oaGongchu.getAgreeCount()<4){
				vars.put("pass", "0");
			}
			dao.update(oaGongchu);
		}else if("gongchu4".equals(taskDefKey)){
			List<String> users=Lists.newArrayList();
			users.add("wang19");
			users.add("wang15");
			users.add("wang5");
			vars.put("usersList",users);
			//actTaskService.startProcess(ActUtils.PD_GONGCHU[0], ActUtils.PD_GONGCHU[1], oaGongchu.getId(), "公出单流程("+user.getName()+")",vars);
		} else if("gongchu8".equals(taskDefKey)){
			vars.put("manageUser","wang11");
		}  else{
			vars.put("pass", "yes".equals(oaGongchu.getAct().getFlag())? "1" : "0");
		}
		// 提交流程任务
		actTaskService.complete(oaGongchu.getAct().getTaskId(), oaGongchu.getAct().getProcInsId(), oaGongchu.getAct().getComment(), vars);
	}
	@Transactional(readOnly = false)
	public void delete(OaGongchu oaGongchu) {
		super.delete(oaGongchu);
	}
}