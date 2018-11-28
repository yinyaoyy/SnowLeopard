/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.act.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.api.act.entity.ProcessStateVo;
import com.thinkgem.jeesite.api.dto.vo.act.ActVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.PageHelper;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.act.dao.ActDao;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.cmd.CreateAndTakeTransitionCmd;
import com.thinkgem.jeesite.modules.act.service.cmd.JumpTaskCmd;
import com.thinkgem.jeesite.modules.act.service.creator.ChainedActivitiesCreator;
import com.thinkgem.jeesite.modules.act.service.creator.MultiInstanceActivityCreator;
import com.thinkgem.jeesite.modules.act.service.creator.RuntimeActivityDefinitionEntityIntepreter;
import com.thinkgem.jeesite.modules.act.service.creator.SimpleRuntimeActivityDefinitionEntity;
import com.thinkgem.jeesite.modules.act.utils.ActUtils;
import com.thinkgem.jeesite.modules.act.utils.ProcessDefCache;
import com.thinkgem.jeesite.modules.act.utils.ProcessDefUtils;
import com.thinkgem.jeesite.modules.info.dao.StarLevelDao;
import com.thinkgem.jeesite.modules.info.entity.StarLevel;
import com.thinkgem.jeesite.modules.oa.dao.OaDataLinkDao;
import com.thinkgem.jeesite.modules.oa.dao.OaProcessStateDao;
import com.thinkgem.jeesite.modules.oa.dao.act.OaFastLegalDao;
import com.thinkgem.jeesite.modules.oa.dao.act.OaLegalAidDao;
import com.thinkgem.jeesite.modules.oa.dao.act.OaPeopleMediationApplyDao;
import com.thinkgem.jeesite.modules.oa.entity.OaDataLink;
import com.thinkgem.jeesite.modules.oa.entity.OaProcessState;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.NodeManager;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.NodeManagerService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 流程定义相关Service
 * @author ThinkGem
 * @version 2013-11-03
 */
@Service
@Transactional(readOnly = true)
public class ActTaskService extends BaseService {
	
	@Autowired
	private ActDao actDao;
	
	@Autowired
	private OaDataLinkDao oaDataLinkDao;

	@Autowired
	private ProcessEngineFactoryBean processEngineFactory;
	
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SystemService systemService;
	@Autowired
	private NodeManagerService nodeManagerService;
	@Autowired
	private OaPeopleMediationApplyDao oaPeopleMediationApplyDao;
	@Autowired
	private OaLegalAidDao oaLegalAidDao;
	@Autowired
	private OaProcessStateDao oaProcessStateDao;
	@Autowired
	private OaFastLegalDao oaFastLegalDao;
	@Autowired
	private StarLevelDao starLevelDao;
	/**
	 * 获取待办列表
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Page<Act> todoList(Page<Act> page,Act act){
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		
		List<Act> result = new ArrayList<Act>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
		setTaskQueryParam(todoTaskQuery, act);
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			//判断状态是否为挂起
			e.setStatus(processIsSuspended(task.getProcessInstanceId())?"suspended":"todo");
			//根据申请人登录账号查询申请人信息
			e.setApplyUser(systemService.getUserByLoginName(String.valueOf(e.getVars().getMap().get("apply"))));
			result.add(e);
		}
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId).includeProcessVariables()
				.orderByTaskCreateTime().desc();
		
		setTaskQueryParam(toClaimQuery, act);
		
		//和result中的进行比较
		Act current = null;
		int size = result.size();
		// 查询列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			 //获取result的长度
			 int len = result.size();
			 if(size==0){
				 	Act e = new Act();
					e.setTask(task);
					e.setVars(task.getProcessVariables());
					e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
					//判断状态是否为挂起
					if(!"".equals(task.getProcessInstanceId())){
						e.setStatus(processIsSuspended(task.getProcessInstanceId())?"suspended":"claim");
					}
					//根据申请人登录账号查询申请人信息
					e.setApplyUser(systemService.getUserByLoginName(String.valueOf(e.getVars().getMap().get("apply"))));
					result.add(e);
			 }else{
				 for(int i = 0 ; i<len ; i++){
					 boolean flag = true;
					 current = result.get(i);
					 if(task.getCreateTime().getTime() - current.getTask().getCreateTime().getTime()>=0){
						 	Act e = new Act();
							e.setTask(task);
							e.setVars(task.getProcessVariables());
							e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
							//判断状态是否为挂起
							if(!"".equals(task.getProcessInstanceId())){
								e.setStatus(processIsSuspended(task.getProcessInstanceId())?"suspended":"claim");
							}
							//根据申请人登录账号查询申请人信息
							e.setApplyUser(systemService.getUserByLoginName(String.valueOf(e.getVars().getMap().get("apply"))));
							result.add(i,e);
							flag = false;
							break;
					 }else if(i == len-1 && flag == true){
						 	Act e = new Act();
							e.setTask(task);
							e.setVars(task.getProcessVariables());
							e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
							//判断状态是否为挂起
							if(!"".equals(task.getProcessInstanceId())){
								e.setStatus(processIsSuspended(task.getProcessInstanceId())?"suspended":"claim");
							}
							//根据申请人登录账号查询申请人信息
							e.setApplyUser(systemService.getUserByLoginName(String.valueOf(e.getVars().getMap().get("apply"))));
							result.add(e);
					 }
				 }
			 }
		}
		page.setCount(result.size());
		PageHelper<Act> newPage=new	PageHelper<Act>(page.getPageNo(),page.getPageSize(),result);
		page.setList(newPage.getPageRecordList());
		return page;
	}
	/**
	 * 获取待办列表
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Page<Act> getUserAllList(Page<Act> page,Act act){
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		List<Act> result = new ArrayList<Act>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
		setTaskQueryParam(todoTaskQuery, act);
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			//判断状态是否为挂起
			e.setStatus(processIsSuspended(task.getProcessInstanceId())?"suspended":"todo");
			//根据申请人登录账号查询申请人信息
			e.setApplyUser(systemService.getUserByLoginName(String.valueOf(e.getVars().getMap().get("apply"))));
			result.add(e);
		}
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId).includeProcessVariables()
				.orderByTaskCreateTime().desc();
		setTaskQueryParam(toClaimQuery, act);
		// 查询列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			//判断状态是否为挂起
			e.setStatus(processIsSuspended(task.getProcessInstanceId())?"suspended":"claim");
			//根据申请人登录账号查询申请人信息
			e.setApplyUser(systemService.getUserByLoginName(String.valueOf(e.getVars().getMap().get("apply"))));
			result.add(e);
		}
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished()
				.includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			histTaskQuery.taskCompletedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			histTaskQuery.taskCompletedBefore(act.getEndDate());
		}
		// 查询列表
		List<HistoricTaskInstance> histList = histTaskQuery.listPage(page.getFirstResult(), page.getMaxResults());
		//处理分页问题
		//List<Act> actList=Lists.newArrayList();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setHistTask(histTask);
			e.setVars(histTask.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			e.setStatus("finish");
			//根据申请人登录账号查询申请人信息
			e.setApplyUser(systemService.getUserByLoginName(String.valueOf(e.getVars().getMap().get("apply"))));
			result.add(e);
		}
		page.setCount(result.size());
		PageHelper<Act> newPage=new	PageHelper<Act>(page.getPageNo(),page.getPageSize(),result);
		page.setList(newPage.getPageRecordList());
		return page;
	}
	/**
	 * @author 王鹏
	 * @version 2018-05-28 19:33:34
	 * @param todoTaskQuery
	 * @param act
	 */
	private void setTaskQueryParam(TaskQuery todoTaskQuery, Act act) {
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			todoTaskQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			todoTaskQuery.taskCreatedBefore(act.getEndDate());
		}
	}
	/**
	 * 获取待办数量
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Long todoListCount(Page<Act> page,Act act){
		Long assigneeCount = 0l;//已签收数量
		Long claimCount = 0l;//代签收数量
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
		setTaskQueryParam(todoTaskQuery, act);
		//查询数量
		assigneeCount = todoTaskQuery.count();
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId).includeProcessVariables()
				.orderByTaskCreateTime().desc();
		setTaskQueryParam(toClaimQuery, act);
		// 查询数量
		claimCount = toClaimQuery.count();
		return assigneeCount+claimCount;
	}
	/**
	 * 获取全部待办列表
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Page<Act> todoLists(Page<Act> page, Act act){
		List<Act> result = new ArrayList<Act>();
		TaskQuery toClaimQuery = taskService.createTaskQuery()
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		setTaskQueryParam(toClaimQuery, act);
		page.setCount(toClaimQuery.count());
		// 查询列表
		List<Task> toClaimList = toClaimQuery.listPage(page.getFirstResult(), page.getMaxResults());
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("claim");
			result.add(e);
			String procInsId = e.getTask().getExecutionId();
			User user=userDao.getName(procInsId);
			if(user!=null){
				e.getTask().setAssignee(user.getName());
			}else{
				e.getTask().setAssignee("");
			}
		}
		page.setList(result);
		return page;
	}
	/**
	 * 获取全部待办数量
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Long todoListsCount(Page<Act> page, Act act){
		Long count = 0l;
		TaskQuery toClaimQuery = taskService.createTaskQuery()
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		setTaskQueryParam(toClaimQuery, act);
		count = toClaimQuery.count();
		return count;
	}
	/**
	 * 获取已办任务
	 * @param page
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Page<Act> historicList(Page<Act> page, Act act){
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished()
				.includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			histTaskQuery.taskCompletedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			histTaskQuery.taskCompletedBefore(act.getEndDate());
		}
		
		// 查询总数
		page.setCount(histTaskQuery.count());
		
		// 查询列表
		List<HistoricTaskInstance> histList = histTaskQuery.listPage(page.getFirstResult(), page.getMaxResults());
		//处理分页问题
		List<Act> actList=Lists.newArrayList();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setHistTask(histTask);
			e.setVars(histTask.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			e.setStatus("finish");
			//根据申请人登录账号查询申请人信息
			e.setApplyUser(systemService.getUserByLoginName(String.valueOf(e.getVars().getMap().get("apply"))));
			actList.add(e);
		}
		page.setList(actList);
		return page;
	}

	/**
	 * 获取已办数量
	 * @param page
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Long historicListCount(Page<Act> page, Act act){
		Long count = 0l;
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished()
				.includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			histTaskQuery.taskCompletedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			histTaskQuery.taskCompletedBefore(act.getEndDate());
		}
		// 查询总数
		count = histTaskQuery.count();
		return count;
	}
	/**
	 * 获取创建人创建的任务
	 * @param page
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Page<Act> historicCreaterUserList(Page<Act> page, Act act){
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		HistoricProcessInstanceQuery histTaskQuery = historyService.createHistoricProcessInstanceQuery().startedBy(userId).includeProcessVariables().orderByProcessInstanceStartTime().desc();
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			histTaskQuery.startedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			histTaskQuery.startedBefore(act.getEndDate());
		}
		if(StringUtils.isNotBlank(act.getStatus())){
			if("1".equals(act.getStatus())){
				histTaskQuery.unfinished();		//未完成，还在流程中的
			}else if("2".equals(act.getStatus())){
				histTaskQuery.finished();			//已完成归档的
			}
		}
		if (StringUtils.isNotBlank(act.getTitle())){
			histTaskQuery.variableValueLike("title", "%"+act.getTitle()+"%");
		}
		// 查询总数
		page.setCount(histTaskQuery.count());
		
		// 查询列表
		List<HistoricProcessInstance> histList = histTaskQuery.listPage(page.getFirstResult(), page.getMaxResults());
		//处理分页问题
		List<Act> actList=Lists.newArrayList();
		Task task = null;
		for (HistoricProcessInstance histTask : histList) {
			Act e = new Act();
			//e.setHistTask(histTask);
			e.setHisProcIns(histTask);
			e.setVars(histTask.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			e.setStatus("finish");
			task = getCurrentTaskByProcInsId(histTask.getId());
			if(task!=null) {
				e.setTaskId(task.getId());
				e.setTaskDefKey(task.getTaskDefinitionKey());
				e.setTaskName(task.getName());
			}
			else {
				HistoricTaskInstance temp = getCompleteCurrentTaskByProcInsId(histTask.getId());
				e.setTaskId(temp.getId());
				e.setTaskDefKey(temp.getTaskDefinitionKey());
				e.setTaskName(temp.getName());
				String loginName = temp.getAssignee();
				User u= UserUtils.getByLoginName(loginName);
				e.setCurrentUser(u);
			}
			// e.setTaskName(this.getCurrentTask(histTask.getId(), "", "").getHistIns().getActivityName());;
			actList.add(e);
		}
		page.setList(actList);
		return page;
	}
	public  Act getCurrentTask(String procInsId, String startAct, String endAct){
		List<Act> actList=this.histoicFlowList(procInsId, startAct, endAct);
		return actList.get(actList.size()-1);
	}
	/**
	 * 获取流转历史列表
	 * @param procInsId 流程实例
	 * @param startAct 开始活动节点名称
	 * @param endAct 结束活动节点名称
	 */
	public List<Act> histoicFlowList(String procInsId, String startAct, String endAct){
		List<Act> actList = Lists.newArrayList();
		List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInsId)
				.orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().list();
		
		boolean start = false;
		Map<String, Integer> actMap = Maps.newHashMap();
		
		for (int i=0; i<list.size(); i++){
			
			HistoricActivityInstance histIns = list.get(i);
			
			// 过滤开始节点前的节点
			if (StringUtils.isNotBlank(startAct) && startAct.equals(histIns.getActivityId())){
				start = true;
			}
			if (StringUtils.isNotBlank(startAct) && !start){
				continue;
			}
			
			// 只显示开始节点和结束节点，并且执行人不为空的任务
			if (StringUtils.isNotBlank(histIns.getAssignee())
					 || "startEvent".equals(histIns.getActivityType())
					 || "endEvent".equals(histIns.getActivityType())){
				
				// 给节点增加一个序号
				Integer actNum = actMap.get(histIns.getActivityId());
				if (actNum == null){
					actMap.put(histIns.getActivityId(), actMap.size());
				}
				
				Act e = new Act();
				e.setHistIns(histIns);
				// 获取流程发起人名称
				if ("startEvent".equals(histIns.getActivityType())){
					List<HistoricProcessInstance> il = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).orderByProcessInstanceStartTime().asc().list();
					if (il.size() > 0){
						if (StringUtils.isNotBlank(il.get(0).getStartUserId())){
							User user = UserUtils.getByLoginName(il.get(0).getStartUserId());
							if (user != null){
								e.setAssignee(histIns.getAssignee());
								e.setAssigneeName(user.getName());
							}
						}
					}
				}
				// 获取任务执行人名称
				if (StringUtils.isNotEmpty(histIns.getAssignee())){
					User user = UserUtils.getByLoginName(histIns.getAssignee());
					if (user != null){
						e.setAssignee(histIns.getAssignee());
						e.setAssigneeName(user.getName());
					}
				}
				// 获取意见评论内容
				if (StringUtils.isNotBlank(histIns.getTaskId())){
					//mysql longblob没有字符集设置，为避免乱码需要进行编码转换
					/*List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
					if (commentList.size()>0){
						e.setComment(commentList.get(0).getFullMessage());
					}*/
					List<String> commentList = actDao.getCommentsByTaskId(histIns.getTaskId());
					if(commentList.size()>0) {
						e.setComment(commentList.get(0));
					}
				}
				actList.add(e);
			}
			
			// 过滤结束节点后的节点
			if (StringUtils.isNotBlank(endAct) && endAct.equals(histIns.getActivityId())){
				boolean bl = false;
				Integer actNum = actMap.get(histIns.getActivityId());
				// 该活动节点，后续节点是否在结束节点之前，在后续节点中是否存在
				for (int j=i+1; j<list.size(); j++){
					HistoricActivityInstance hi = list.get(j);
					Integer actNumA = actMap.get(hi.getActivityId());
					if ((actNumA != null && actNumA < actNum) || StringUtils.equals(hi.getActivityId(), histIns.getActivityId())){
						bl = true;
					}
				}
				if (!bl){
					break;
				}
			}
		}
		return actList;
	}

	/**
	 * 获取流程列表
	 * @param category 流程分类
	 */
	public Page<Object[]> processList(Page<Object[]> page, String category) {
		/*
		 * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
		 */
	    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
	    		.latestVersion().active().orderByProcessDefinitionKey().asc();
	    
	    if (StringUtils.isNotEmpty(category)){
	    	processDefinitionQuery.processDefinitionCategory(category);
		}
	    List list = new ArrayList();
	    list.add("notification_defense");
	    list.add("oa_agreement");
	    long a = 0;
	    List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(page.getFirstResult(), page.getMaxResults());
	    for (ProcessDefinition processDefinition : processDefinitionList) {
	      String deploymentId = processDefinition.getDeploymentId();
	      Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
	      if(list.contains(processDefinition.getKey())){
	    	  page.getList().add(new Object[]{processDefinition, deployment});
	    	  a ++;
	      }
	    }
	    page.setList(page.getList());
	    page.setCount(a);
		return page;
	}
	
	/**
	 * 获取流程表单（首先获取任务节点表单KEY，如果没有则取流程开始节点表单KEY）
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String getFormKey(String procDefId, String taskDefKey){
		String formKey = "";
		if (StringUtils.isNotBlank(procDefId)){
			if (StringUtils.isNotBlank(taskDefKey)){
				try{
					formKey = formService.getTaskFormKey(procDefId, taskDefKey);
				}catch (Exception e) {
					formKey = "";
					//手动的回滚，不然要报错
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
			if (StringUtils.isBlank(formKey)){
				try{
					formKey = formService.getStartFormKey(procDefId);
				}catch (Exception e) {
					formKey = "";
				}
				
			}
			if (StringUtils.isBlank(formKey)){
				formKey = "/404";
			}
		}
		
		logger.debug("getFormKey: {}", formKey);
		return formKey;
	}
	
	/**
	 * 获取流程实例对象
	 * @param procInsId
	 * @return
	 */
	@Transactional(readOnly = false)
	public ProcessInstance getProcIns(String procInsId) {
		return runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
	}
	/**
	 * 获取历史流程实例对象
	 * @param procInsId
	 * @return
	 */
	@Transactional(readOnly = false)
	public HistoricProcessInstance getHisProcIns(String procInsId) {
		 return processEngine.getHistoryService()/**与历史数据（历史表）相关的Service*/  
                 .createHistoricProcessInstanceQuery()/**创建历史流程实例查询*/  
                 .processInstanceId(procInsId)/**使用流程实例ID查询*/  
                 .singleResult();
		//return runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
	}

	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId) {
		return startProcess(procDefKey, businessTable, businessId, "");
	}
	
	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @param title			流程标题，显示在待办任务标题
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId, String title) {
		Map<String, Object> vars = Maps.newHashMap();
		return startProcess(procDefKey, businessTable, businessId, title, vars);
	}
	
	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @param title			流程标题，显示在待办任务标题
	 * @param vars			流程变量
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId, String title, Map<String, Object> vars) {
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId())
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(userId);
		
		// 设置流程变量
		if (vars == null){
			vars = Maps.newHashMap();
		}
		
		// 设置流程标题
		if (StringUtils.isNotBlank(title)){
			vars.put("title", title);
		}
		
		// 启动流程
		ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessTable+":"+businessId, vars);
		
		
		// 更新业务表流程实例ID
		Act act = new Act();
		act.setBusinessTable(businessTable);// 业务表名
		act.setBusinessId(businessId);	// 业务表ID
		act.setProcInsId(procIns.getId());
		actDao.updateProcInsIdByBusinessId(act);
		
		Map<String, Object> param = Maps.newHashMap();
		Date date = historyService.createHistoricProcessInstanceQuery().processInstanceId(procIns.getId()).singleResult().getStartTime();
		String taskDefKey = "";
		String name  = "";
		
		if("legal_aid".equals(procDefKey)){
			taskDefKey = "aid_start";
			name = "申请法律援助";
		}else if("notification_defense".equals(procDefKey)) {
			taskDefKey = "defense_start";
			name = "填写通知辩护";
		}else if("people_mediation".equals(procDefKey)){
			taskDefKey = "mediation_start";
			name = "提交申请";
		}else if("oa_agreement".equals(procDefKey)){
			taskDefKey = "agreement_start";
			name = "申请三定方案";
		}else if("fast_legal".equals(procDefKey)){
			taskDefKey = "fast_start";
			name = "申请办理直通车";
		}
		//act_hi_taskinst必要字段
		param.put("id", IdGen.uuid());
		param.put("procDefId", procIns.getProcessDefinitionId());
		param.put("taskDefKey",taskDefKey );
		param.put("procInstId", procIns.getId());
		param.put("executionId", procIns.getId());
		param.put("name", name);
		param.put("assignee", userId);
		param.put("startTime", date);
		param.put("claimTime", date);
		param.put("endTime", date);
		param.put("duration", 0);
		param.put("deleteReason", "completed");
		param.put("priority", 50);
		actDao.insertApplyToHiTask(param);
		
		return act.getProcInsId();
	}
	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @param title			流程标题，显示在待办任务标题
	 * @param vars			流程变量
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId, String title, Map<String, Object> vars,String userIds) {
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId())
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(userId);
		
		// 设置流程变量
		if (vars == null){
			vars = Maps.newHashMap();
		}
		
		// 设置流程标题
		if (StringUtils.isNotBlank(title)){
			vars.put("title", title);
		}
		
		// 启动流程
		//ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessTable+":"+businessId, vars);
		ProcessInstance procIns =runtimeService.startProcessInstanceByKeyAndTenantId(procDefKey, businessTable+":"+businessId, vars,userIds);
		// 更新业务表流程实例ID
		Act act = new Act();
		act.setBusinessTable(businessTable);// 业务表名
		act.setBusinessId(businessId);	// 业务表ID
		act.setProcInsId(procIns.getId());
		actDao.updateProcInsIdByBusinessId(act);
		return act.getProcInsId();
	}

	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable	表名
	 * @param dataId		业务表Id
	 * @param vars		参数
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey,String businessTable,String dataId, Map<String, Object> vars) {
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId())
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(userId);
		
		// 设置流程变量
		if (vars == null){
			vars = Maps.newHashMap();
		}
		
		// // 设置流程标题
		// if (StringUtils.isNotBlank(title)){
		// 	vars.put("title", title);
		// }
		
		// 启动流程
		ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, vars);
		
//		// 更新业务表流程实例ID
		Act act = new Act();
		act.setBusinessTable(businessTable);// 业务表名
		act.setBusinessId(dataId);	// 业务表ID
		act.setProcInsId(procIns.getId());
		actDao.updateProcInsIdByBusinessId(act);
		//更新中间表的数据
		OaDataLink oaDataLink = new OaDataLink();
		oaDataLink.preInsert();
		oaDataLink.setProcInsId(procIns.getId());
		oaDataLink.setTableName(businessTable);
		oaDataLink.setDataId(dataId);
		oaDataLinkDao.insert(oaDataLink);
		
		return act.getProcInsId();
	}

	/**
	 * 获取任务
	 * @param taskId 任务ID
	 */
	public Task getCurrentTaskByProcInsId(String procInsId){
		List<Task> list = taskService.createTaskQuery().processInstanceId(procInsId).active().list();
		
		if(list==null || list.size()==0) {
			return null;
		}
		else {
			return list.get(0);
		}
	}
	
	/**
	 * 获取流程已经完成的最后一个任务节点信息
	 * @param taskId 任务ID
	 */
	public HistoricTaskInstance getCompleteCurrentTaskByProcInsId(String procInsId){
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().desc().list();
		
		if(list==null || list.size()==0) {
			return null;
		}
		else {
			return list.get(0);
		}
	}
	/**
	 * 获取任务
	 * @param taskId 任务ID
	 */
	public Task getTask(String taskId){
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}
	
	/**
	 * 删除任务
	 * @param taskId 任务ID
	 * @param deleteReason 删除原因
	 */
	@Transactional(readOnly = false)
	public void deleteTask(String taskId, String deleteReason){
		taskService.deleteTask(taskId, deleteReason);
	}
	
	/**
	 * 签收任务
	 * @param taskId 任务ID
	 * @param userId 签收用户ID（用户登录名）
	 */
	@Transactional(readOnly = false)
	public void claim(String taskId, String userId){
		taskService.claim(taskId, userId);
	}
	
	/**
	 * 提交任务, 并保存意见
	 * @param taskId 任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment 任务提交意见的内容
	 * @param vars 任务变量
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, String procInsId, String comment, Map<String, Object> vars){
		complete(taskId, procInsId, comment, "", vars);
	}
	
	/**
	 * 提交任务, 并保存意见
	 * @param taskId 任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment 任务提交意见的内容
	 * @param title			流程标题，显示在待办任务标题
	 * @param vars 任务变量
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars){
		// 添加意见
		if (StringUtils.isNotBlank(procInsId) && StringUtils.isNotBlank(comment)){
			taskService.addComment(taskId, procInsId, comment);
		}
		
		// 设置流程变量
		if (vars == null){
			vars = Maps.newHashMap();
		}
		
		// 设置流程标题
		if (StringUtils.isNotBlank(title)){
			vars.put("title", title);
		}
		
		// 提交任务
		taskService.complete(taskId, vars);
	}

	/**
	 * 完成第一个任务
	 * @param procInsId
	 */
	@Transactional(readOnly = false)
	public void completeFirstTask(String procInsId){
		completeFirstTask(procInsId, null, null, null);
	}
	
	/**
	 * 完成第一个任务
	 * @param procInsId
	 * @param comment
	 * @param title
	 * @param vars
	 */
	@Transactional(readOnly = false)
	public void completeFirstTask(String procInsId, String comment, String title, Map<String, Object> vars){
		String userId = UserUtils.getUser().getLoginName();
		Task task = taskService.createTaskQuery().taskAssignee(userId).processInstanceId(procInsId).active().singleResult();
		if (task != null){
			complete(task.getId(), procInsId, comment, title, vars);
		}
	}
	
	/**
	 * 添加任务意见
	 */
	public void addTaskComment(String taskId, String procInsId, String comment){
		taskService.addComment(taskId, procInsId, comment);
	}
	
	//////////////////  回退、前进、跳转、前加签、后加签、分裂 移植  https://github.com/bluejoe2008/openwebflow  ////////////////////////////////////////////////// 

	/**
	 * 任务后退一步
	 */
	public void taskBack(String procInsId, Map<String, Object> variables) {
		taskBack(getCurrentTask(procInsId), variables);
	}

	/**
	 * 任务后退至指定活动
	 */
	public void taskBack(TaskEntity currentTaskEntity, Map<String, Object> variables) {
		ActivityImpl activity = (ActivityImpl) ProcessDefUtils
				.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), currentTaskEntity.getTaskDefinitionKey())
				.getIncomingTransitions().get(0).getSource();
		jumpTask(currentTaskEntity, activity, variables);
	}

	/**
	 * 任务前进一步
	 */
	public void taskForward(String procInsId, Map<String, Object> variables) {
		taskForward(getCurrentTask(procInsId), variables);
	}

	/**
	 * 任务前进至指定活动
	 */
	public void taskForward(TaskEntity currentTaskEntity, Map<String, Object> variables) {
		ActivityImpl activity = (ActivityImpl) ProcessDefUtils
				.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), currentTaskEntity.getTaskDefinitionKey())
				.getOutgoingTransitions().get(0).getDestination();

		jumpTask(currentTaskEntity, activity, variables);
	}
	
	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	@Transactional(readOnly = false)
	public void jumpTask(String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables) {
		jumpTask(getCurrentTask(procInsId), targetTaskDefinitionKey, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	public void jumpTask(String procInsId, String currentTaskId, String targetTaskDefinitionKey, Map<String, Object> variables) {
		jumpTask(getTaskEntity(currentTaskId), targetTaskDefinitionKey, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * @param currentTaskEntity 当前任务节点
	 * @param targetTaskDefinitionKey 目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	public void jumpTask(TaskEntity currentTaskEntity, String targetTaskDefinitionKey, Map<String, Object> variables) {
		ActivityImpl activity = ProcessDefUtils.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(),
				targetTaskDefinitionKey);
		jumpTask(currentTaskEntity, activity, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * @param currentTaskEntity 当前任务节点
	 * @param targetActivity 目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	private void jumpTask(TaskEntity currentTaskEntity, ActivityImpl targetActivity, Map<String, Object> variables) {
		CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
		commandExecutor.execute(new JumpTaskCmd(currentTaskEntity, targetActivity, variables));
	}
	
	/**
	 * 后加签
	 */
	@SuppressWarnings("unchecked")
	public ActivityImpl[] insertTasksAfter(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, String... assignees) {
		List<String> assigneeList = new ArrayList<String>();
		assigneeList.add(Authentication.getAuthenticatedUserId());
		assigneeList.addAll(CollectionUtils.arrayToList(assignees));
		String[] newAssignees = assigneeList.toArray(new String[0]);
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(procDefId);
		ActivityImpl prototypeActivity = ProcessDefUtils.getActivity(processEngine, processDefinition.getId(), targetTaskDefinitionKey);
		return cloneAndMakeChain(processDefinition, procInsId, targetTaskDefinitionKey, prototypeActivity.getOutgoingTransitions().get(0).getDestination().getId(), variables, newAssignees);
	}

	/**
	 * 前加签
	 */
	public ActivityImpl[] insertTasksBefore(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, String... assignees) {
		ProcessDefinitionEntity procDef = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(procDefId);
		return cloneAndMakeChain(procDef, procInsId, targetTaskDefinitionKey, targetTaskDefinitionKey, variables, assignees);
	}

	/**
	 * 分裂某节点为多实例节点
	 */
	public ActivityImpl splitTask(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, String... assignee) {
		return splitTask(procDefId, procInsId, targetTaskDefinitionKey, variables, true, assignee);
	}
	
	/**
	 * 分裂某节点为多实例节点
	 */
	@SuppressWarnings("unchecked")
	public ActivityImpl splitTask(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, boolean isSequential, String... assignees) {
		SimpleRuntimeActivityDefinitionEntity info = new SimpleRuntimeActivityDefinitionEntity();
		info.setProcessDefinitionId(procDefId);
		info.setProcessInstanceId(procInsId);

		RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);

		radei.setPrototypeActivityId(targetTaskDefinitionKey);
		radei.setAssignees(CollectionUtils.arrayToList(assignees));
		radei.setSequential(isSequential);
		
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(procDefId);
		ActivityImpl clone = new MultiInstanceActivityCreator().createActivities(processEngine, processDefinition, info)[0];

		TaskEntity currentTaskEntity = this.getCurrentTask(procInsId);
		
		CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
		commandExecutor.execute(new CreateAndTakeTransitionCmd(currentTaskEntity, clone, variables));

		return clone;
	}

	private TaskEntity getCurrentTask(String procInsId) {
		return (TaskEntity) taskService.createTaskQuery().processInstanceId(procInsId).active().singleResult();
	}

	private TaskEntity getTaskEntity(String taskId) {
		return (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	@SuppressWarnings("unchecked")
	private ActivityImpl[] cloneAndMakeChain(ProcessDefinitionEntity procDef, String procInsId, String prototypeActivityId, String nextActivityId, Map<String, Object> variables, String... assignees) {
		SimpleRuntimeActivityDefinitionEntity info = new SimpleRuntimeActivityDefinitionEntity();
		info.setProcessDefinitionId(procDef.getId());
		info.setProcessInstanceId(procInsId);

		RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);
		radei.setPrototypeActivityId(prototypeActivityId);
		radei.setAssignees(CollectionUtils.arrayToList(assignees));
		radei.setNextActivityId(nextActivityId);

		ActivityImpl[] activities = new ChainedActivitiesCreator().createActivities(processEngine, procDef, info);

		jumpTask(procInsId, activities[0].getId(), variables);

		return activities;
	}
	
	

	
	/**
	 * 读取带跟踪的图片
	 * @param executionId	环节ID
	 * @return	封装了各种节点信息
	 */
	public InputStream tracePhoto(String processDefinitionId, String executionId) {
//		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		
		List<String> activeActivityIds = Lists.newArrayList();
		if (runtimeService.createExecutionQuery().executionId(executionId).count() > 0){
			activeActivityIds = runtimeService.getActiveActivityIds(executionId);
		}
		

		// 使用spring注入引擎请使用下面的这行代码
		Context.setProcessEngineConfiguration(processEngineFactory.getProcessEngineConfiguration());
		return processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
				.generateDiagram(bpmnModel, "png", activeActivityIds);
	}
	
	/**
	 * 流程跟踪图信息
	 * @param processInstanceId		流程实例ID
	 * @return	封装了各种节点信息
	 */
	public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
		Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//执行实例
		Object property = PropertyUtils.getProperty(execution, "activityId");
		String activityId = "";
		if (property != null) {
			activityId = property.toString();
		}
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
		List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

		List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
		for (ActivityImpl activity : activitiList) {

			boolean currentActiviti = false;
			String id = activity.getId();

			// 当前节点
			if (id.equals(activityId)) {
				currentActiviti = true;
			}

			Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);

			activityInfos.add(activityImageInfo);
		}

		return activityInfos;
	}
	

	/**
	 * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
	 * @param activity
	 * @param processInstance
	 * @param currentActiviti
	 * @return
	 */
	private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, ProcessInstance processInstance,
			boolean currentActiviti) throws Exception {
		Map<String, Object> vars = new HashMap<String, Object>();
		Map<String, Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("currentActiviti", currentActiviti);
		setPosition(activity, activityInfo);
		setWidthAndHeight(activity, activityInfo);

		Map<String, Object> properties = activity.getProperties();
		vars.put("节点名称", properties.get("name"));
		vars.put("任务类型", ActUtils.parseToZhType(properties.get("type").toString()));

		ActivityBehavior activityBehavior = activity.getActivityBehavior();
		logger.debug("activityBehavior={}", activityBehavior);
		if (activityBehavior instanceof UserTaskActivityBehavior) {

			Task currentTask = null;

			// 当前节点的task
			if (currentActiviti) {
				currentTask = getCurrentTaskInfo(processInstance);
			}

			// 当前任务的分配角色
			UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
			TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
			Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
			if (!candidateGroupIdExpressions.isEmpty()) {

				// 任务的处理角色
				setTaskGroup(vars, candidateGroupIdExpressions);

				// 当前处理人
				if (currentTask != null) {
					setCurrentTaskAssignee(vars, currentTask);
				}
			}
		}

		vars.put("节点说明", properties.get("documentation"));

		String description = activity.getProcessDefinition().getDescription();
		vars.put("描述", description);

		logger.debug("trace variables: {}", vars);
		activityInfo.put("vars", vars);
		return activityInfo;
	}

	/**
	 * 设置任务组
	 * @param vars
	 * @param candidateGroupIdExpressions
	 */
	private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
		String roles = "";
		for (Expression expression : candidateGroupIdExpressions) {
			String expressionText = expression.getExpressionText();
			String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
			roles += roleName;
		}
		vars.put("任务所属角色", roles);
	}

	/**
	 * 设置当前处理人信息
	 * @param vars
	 * @param currentTask
	 */
	private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
		String assignee = currentTask.getAssignee();
		if (assignee != null) {
			org.activiti.engine.identity.User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
			String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
			vars.put("当前处理人", userInfo);
		}
	}

	/**
	 * 获取当前节点信息
	 * @param processInstance
	 * @return
	 */
	public  Task getCurrentTaskInfo(ProcessInstance processInstance) {
		Task currentTask = null;
		try {
			String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");
			logger.debug("current activity id: {}", activitiId);

			currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId)
					.singleResult();
			logger.debug("current task for processInstance: {}", ToStringBuilder.reflectionToString(currentTask));

		} catch (Exception e) {
			logger.error("can not get property activityId from processInstance: {}", processInstance);
		}
		return currentTask;
	}
	/**
	 * 获取当前节点信息
	 * @param processInstance
	 * @return
	 */
	public  Task getCurrentTaskInfo1(String  id) {
		Task currentTask = null;
		try {
			//String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");
			//logger.debug("current activity id: {}", activitiId);

			currentTask = taskService.createTaskQuery().processInstanceId(id).taskDefinitionKey("gongchu2")
					.singleResult();
			logger.debug("current task for processInstance: {}", ToStringBuilder.reflectionToString(currentTask));

		} catch (Exception e) {
			//logger.error("can not get property activityId from processInstance: {}", processInstance);
		}
		return currentTask;
	}
	/**
	 * 设置宽度、高度属性
	 * @param activity
	 * @param activityInfo
	 */
	private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("width", activity.getWidth());
		activityInfo.put("height", activity.getHeight());
	}

	/**
	 * 设置坐标位置
	 * @param activity
	 * @param activityInfo
	 */
	private void setPosition(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("x", activity.getX());
		activityInfo.put("y", activity.getY());
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}
	
	/**
	 * 校验流程实例是否被挂起
	 * @author 王鹏
	 * @version 2018-05-28 17:41:20
	 * @param procInsId
	 * @return
	 */
	public boolean processIsSuspended(String procInsId) {
		return runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult().isSuspended();
	}
	
	/**
	 * 挂起一个流程实例
	 * @author 王鹏
	 * @version 2018-05-27 17:00:49
	 * @param procInsId 流程实例id
	 * @param comment 挂起理由
	 */
	@Transactional(readOnly = false)
    public void suspendProcessInstance(String procInsId, String comment){
		/*String taskId = "suspend_"+procInsId+"_"+DateUtils.getDate("yyyyMMddHHmmss");
		String userLoginName = UserUtils.getUser().getLoginName();
		//加入一个新任务
		addTask(procInsId, taskId);
		//指定受理人
		taskService.setAssignee(taskId, userLoginName);
		//自动签收此任务
		taskService.claim(taskId, userLoginName);
		//添加挂起备注
		taskService.addComment(taskId, procInsId, comment);*/
        //根据一个流程实例的id挂起该流程实例
        runtimeService.suspendProcessInstanceById(procInsId);
    }
    
    /**
     * 激活一个流程实例
     * @author 王鹏
     * @version 2018-05-27 17:01:07
	 * @param procInsId 流程实例id
	 * @param comment 挂起理由
     */
	@Transactional(readOnly = false)
    public void activateProcessInstance(String procInsId, String comment){
    	//根据一个流程实例的id激活该流程实例
        runtimeService.activateProcessInstanceById(procInsId);
    }
    
     /**
     * 获取一个流程的某一个节点对应的表名称
     * @author 王鹏
     * @version 2018-05-27 17:01:07
     */
    public String getDocumentByProDefId(String procInsId, String taskDefKey){
    	
		String tableName = "";
		Map<String,Object> map = new HashMap<String,Object>();
		String firstTableName = "";
		if (StringUtils.isNotBlank(procInsId)){
//			if (StringUtils.isNotBlank(taskDefKey)){
				try{
					List<HistoricTaskInstance> list = processEngine.getHistoryService()//与历史数据（历史表）相关的Service
							.createHistoricTaskInstanceQuery()//创建历史任务实例查询
							.processInstanceId(procInsId)//
							.orderByHistoricTaskInstanceStartTime().asc()
							.list();
					if(list!=null && list.size()>0){
						for(HistoricTaskInstance hti:list){
							if(hti.getTaskDefinitionKey()!=null){
								if(firstTableName==""){
									firstTableName = hti.getDescription();
								}
								map.put(hti.getTaskDefinitionKey(),hti.getDescription());
							}
						}
					}
					tableName = (String) map.get(taskDefKey);
					
				}catch (Exception e) {
					tableName = "";
				}
//			}
			if(StringUtils.isBlank(tableName)){
				
				tableName = firstTableName;
			}
			if (StringUtils.isBlank(tableName)){
				tableName = "";
			}
		}
		logger.debug("getDocumentByProDefId: {}", tableName);
		return tableName;
    }
    /**
     * 从中间表中获取业务表的id
     * @param act       [description]
     * @param procIns   [流程未走完在实例中查找]
     * @param tableName [description]
     */
    public void getDataId(Act act,ProcessInstance procIns,String tableName){
    	if (procIns != null && tableName!=null){
			//通过tableName 和 BusinessKey 来获取
			String dataId = "";
			OaDataLink oaDataLink = oaDataLinkDao.getDataId(procIns.getProcessInstanceId(),tableName);
			if(oaDataLink!=null){
				act.setBusinessTable(tableName);
				dataId = oaDataLink.getDataId();
				act.setBusinessId(dataId);
			}
		}
    }
    /**
     * 从中间表中获取业务表的id
     * @param act       [description]
     * @param procIns   [流程走完后从历史中查找记录]
     * @param tableName [description]
     */
	public void getDataId(Act act, HistoricProcessInstance hisProcIns, String tableName) {
		if (hisProcIns != null && tableName!=null){
			//通过tableName 和 BusinessKey 来获取
			String dataId = "";
			OaDataLink oaDataLink = oaDataLinkDao.getDataId(hisProcIns.getId(),tableName);
			if(oaDataLink!=null){
				act.setBusinessTable(tableName);
				dataId = oaDataLink.getDataId();
				act.setBusinessId(dataId);
			}
		}
	}
	/**
	 * 获取流转历史列表
	 * @param procInsId 流程实例
	 * @param 
	 * @param 
	 */
	public List<Act> histoicList(String procInsId){
		List<Act> actList = Lists.newArrayList();
		List<HistoricTaskInstance> list1 = processEngine.getHistoryService()//与历史数据（历史表）相关的Service
				.createHistoricTaskInstanceQuery()//创建历史任务实例查询
				.processInstanceId(procInsId)//
				.orderByHistoricTaskInstanceStartTime().asc()
				.list();
		if(list1!=null && list1.size()>0){
			for(HistoricTaskInstance hti:list1){
				Act act = new Act();
				act.setHisProcIns(processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(procInsId).singleResult());
				act.setTaskId(hti.getId());
				act.setTaskName(hti.getName());
				act.setProcDefId(hti.getProcessDefinitionId());
				act.setTaskDefKey(hti.getTaskDefinitionKey());
				if (hti.getEndTime()!=null) {
					act.setStatus("2");//代表节点任务完成
				}else if(hti.getStartTime()!=null & hti.getEndTime()==null){
					act.setStatus("1");//代表节点进行中
				}
				act.setProcInsId(procInsId);
				actList.add(act);
			}
		}
		return actList;
	}
	
	/**
	 * 获取流转历史列表
	 * @param procInsId 流程实例
	 * @param 
	 * @param 
	 */
	public List<Act> histoicListCompleteNote(String procInsId){
		List<Act> actList = Lists.newArrayList();
		
		if("".equals(procInsId) || procInsId == null){
			List<ProcessDefinition> plist= repositoryService.createProcessDefinitionQuery().processDefinitionKey("people_mediation").orderByProcessDefinitionVersion().desc().list();
			String  procDefId = plist.get(0).getId();
			BpmnModel model = repositoryService.getBpmnModel(procDefId);  
			if(model != null) {  
			    Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();  
			    boolean flag = false;
			    for(FlowElement e : flowElements) {  
			    	String taskName = e.getId();
			    	if("mediation_start".equals(taskName)||"mediation_zhiding".equals(taskName)||"mediation_xiugai".equals(taskName)||"mediation_shenhe".equals(taskName)||"mediation_dengji".equals(taskName)||"mediation_diaocha".equals(taskName)||"mediation_tiaojie".equals(taskName)||"mediation_xieyi".equals(taskName)||"mediation_huifang".equals(taskName)||"mediation_juanzong".equals(taskName)){
			    		if(flag &&("mediation_start".equals(taskName)||"mediation_zhiding".equals(taskName)||"mediation_xiugai".equals(taskName)||"mediation_shenhe".equals(taskName))){
							continue;
						}
			    		Act act  = new Act();
			    		if("mediation_start".equals(taskName)){
			    			act.setStatus("1");//代表节点进行中
			    			act.setComment("mediation_start");//代表节点进行中
			    			flag = true;
			    		}else{
			    			act.setStatus("0");
			    		}
			    		act.setTaskName(e.getName());
			    		act.setTaskDefKey(taskName);
			    		act.setProcDefId(procDefId);
			    		if("mediation_dengji".equals(taskName)){
		    				actList.add(1, act);
		    			}else{
		    				actList.add(act);
		    			}
			    	}
			    }
			}
		}else{
			List<HistoricTaskInstance> list1 = processEngine.getHistoryService()//与历史数据（历史表）相关的Service
					.createHistoricTaskInstanceQuery()//创建历史任务实例查询
					.processInstanceId(procInsId)//
					.orderByHistoricTaskInstanceStartTime().asc()
					.list();
			
			if(list1!=null && list1.size()>0){
				String  procDefId = list1.get(0).getProcessDefinitionId();
				BpmnModel model = repositoryService.getBpmnModel(procDefId);  
				if(model != null) {  
				    Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();  
				    boolean flag = false;//用来合并mediation_start,mediation_zhiding,mediation_shenhe,mediation_xiugai这四个节点的信息
				    for(FlowElement e : flowElements) {  
				    	String taskName = e.getId();
				    	
				    	Act act  = new Act();
				    	for(HistoricTaskInstance hti:list1){
							if(taskName.equals(hti.getTaskDefinitionKey())){
								
								if(flag &&("mediation_start".equals(taskName)||"mediation_zhiding".equals(taskName)||"mediation_xiugai".equals(taskName)||"mediation_shenhe".equals(taskName)||"mediation_shenqing".equals(taskName))){
									continue;
								}
								
								act.setHisProcIns(processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(procInsId).singleResult());
								act.setTaskId(hti.getId());
								act.setTaskName(hti.getName());
								act.setProcDefId(hti.getProcessDefinitionId());
								act.setTaskDefKey(hti.getTaskDefinitionKey());
								if (hti.getEndTime()!=null) {
									act.setStatus("2");//代表节点任务完成
								}else if(hti.getStartTime()!=null & hti.getEndTime()==null){
									act.setStatus("1");//代表节点进行中
								}
								act.setProcInsId(procInsId);
								
								if("mediation_start".equals(taskName)||"mediation_zhiding".equals(taskName)||"mediation_xiugai".equals(taskName)||"mediation_shenhe".equals(taskName)||"mediation_shenqing".equals(taskName)){
									flag = true;
					    		}
								if("mediation_dengji".equals(taskName)){
				    				actList.add(1, act);
				    			}else{
				    				actList.add(act);
				    			}
								break;
							}
						}
				    	if("".equals(act.getStatus()) || act.getStatus()== null){
				    		
				    		if(flag &&("mediation_start".equals(taskName)||"mediation_zhiding".equals(taskName)||"mediation_xiugai".equals(taskName)||"mediation_shenhe".equals(taskName)||"mediation_shenqing".equals(taskName))){
								continue;
							}
				    		if("mediation_dengji".equals(taskName)||"mediation_diaocha".equals(taskName)||"mediation_tiaojie".equals(taskName)||"mediation_xieyi".equals(taskName)||"mediation_huifang".equals(taskName)||"mediation_juanzong".equals(taskName)){
				    			
				    			act.setStatus("0");//代表节点任务完成
				    			act.setProcInsId(procInsId);
				    			act.setTaskName(e.getName());
				    			act.setProcDefId(procDefId);
				    			act.setTaskDefKey(e.getId());
				    			
				    			if("mediation_dengji".equals(taskName)){
				    				actList.add(1, act);
				    				//登记节点还没开始，就将开始节点设置为进行中
			    					Act newAct = actList.get(0);
			    					newAct.setStatus("1");
			    					newAct.setComment(taskService.createTaskQuery().processInstanceId(procInsId).singleResult().getTaskDefinitionKey());
			    					actList.set(0,newAct);
			    				
				    			}else{
				    				actList.add(act);
				    			}
				    		}
				    	}
				    }  
				} 
			}
		}
		
		return actList;
	}
	
	/**
	 * 获取流转历史列表(法律援助使用，法律援助不需要流程信息)
	 * @param procInsId 流程实例
	 * @param 
	 * @param 
	 */
	public List<Act> histoicCompleteNote(String procInsId){
		//思路
		//首先获取该流程实例对应的版本和流程key值
		//得到配置中的nodeList
		//设置流程节点中的状态：
		//查看流程是否完结，完结则所有节点都是2
		//未完结，那么查找当前节点所在的位置，将该节点设置1，节点之前的为2，节点之后的为0
		
		
		List<Act> actList = Lists.newArrayList();
		HistoricProcessInstance h = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).singleResult();
		
		int version = h.getProcessDefinitionVersion();
		String procDefKey = h.getProcessDefinitionKey();
		List<NodeManager> nodeList= Lists.newArrayList();
		
		NodeManager nodeManager=new NodeManager();
    	nodeManager.setDelFlag("0");
    	nodeManager.setProcDefKey(procDefKey);
    	nodeManager.setVersion(String.valueOf(version));
		nodeList=nodeManagerService.findList(nodeManager);
		
		Task task = getCurrentTaskByProcInsId(procInsId);
		if(task==null){
			for(int i = 0 ; i< nodeList.size() ; i++){
				Act act = new Act();
				act.setTaskName(nodeList.get(i).getName());
				act.setStatus("2"); //代表已经完成了
				actList.add(act);
			}
		}else{
			String taskName = task.getTaskDefinitionKey();
			int index = 0;
			for(int i = 0 ; i< nodeList.size() ; i++){
				if(nodeList.get(i).getTaskDefKey().contains(taskName)){
					index = i;
					break;
				}
			}	
			for(int i = 0 ; i< nodeList.size() ; i++){
				Act act = new Act();
				act.setTaskName(nodeList.get(i).getName());
				
				if( i < index){
					act.setStatus("2"); //代表已经完成了
				}else if(i == index){
					act.setStatus("1"); //代表进行中
				}else if(i > index){
					act.setStatus("0"); //代表未开始
				}
				actList.add(act);
			}
		}
		return actList;
	}
	
	
	/**
	 * 给接口使用个，获取创建人创建的任务
	 * @param page
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Page<ActVo> ApihistoricCreaterUserList(Page<ActVo> page, Act act){
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		HistoricProcessInstanceQuery histTaskQuery = historyService.createHistoricProcessInstanceQuery().startedBy(userId).includeProcessVariables().orderByProcessInstanceStartTime().desc();
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			histTaskQuery.startedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			histTaskQuery.startedBefore(act.getEndDate());
		}
		if(StringUtils.isNotBlank(act.getStatus())){
			if("1".equals(act.getStatus())){
				histTaskQuery.unfinished();		//未完成，还在流程中的
			}else if("2".equals(act.getStatus())){
				histTaskQuery.finished();			//已完成归档的
			}
		}
		if (StringUtils.isNotBlank(act.getTitle())){
			histTaskQuery.variableValueLike("title", "%"+act.getTitle()+"%");
		}
		// 查询总数
		page.setCount(histTaskQuery.count());
		
		// 查询列表
		List<HistoricProcessInstance> histList = histTaskQuery.listPage(page.getFirstResult(), page.getMaxResults());
		//处理分页问题
		List<ActVo> actList=Lists.newArrayList();
		Task task = null;
		for (HistoricProcessInstance histTask : histList) {
			ActVo e = new ActVo();
			e.setTask(histTask);
			String[] ss = histTask.getBusinessKey().split(":");
			e.setBusinessTable(ss[0]);
			e.setBusinessId(ss[1]);
			e.setVars(histTask.getProcessVariables());
			e.setProcDefId(histTask.getProcessDefinitionId());
			e.setProcDefKey(histTask.getProcessDefinitionKey());
			e.setProcInsId(histTask.getId());
			e.setVersion(histTask.getProcessDefinitionVersion().toString());
			e.setProcDefName(ProcessDefCache.get(histTask.getProcessDefinitionId()).getName());
			task = getCurrentTaskByProcInsId(histTask.getId());
			if(task!=null) {
				e.setTaskId(task.getId());
				e.setTaskDefKey(task.getTaskDefinitionKey());
				e.setTaskName(task.getName());
			}
			else {
				HistoricTaskInstance temp = getCompleteCurrentTaskByProcInsId(histTask.getId());
				
				List<Map<String,Object>> evaluatedList = Lists.newArrayList();
				Map<String,Object> map= null;
				
				
				e.setTaskId(temp.getId());
				e.setTaskDefKey(temp.getTaskDefinitionKey());
				e.setTaskName(temp.getName());
				
				String loginName = temp.getAssignee();
				User u= UserUtils.getByLoginName(loginName);
				//当是法律援助的时候，会有两个人需要评价
				if("legal_aid".equals(e.getProcDefKey())){
					if(u!=null && u.getId()!=""){
						map = Maps.newHashMap();
						map.put("beEvaluatedUserId", u.getId());  //被评论人的id
						map.put("avatar", u.getPhoto());	  //被评论人的照片
						map.put("roleName", getRoleById(u.getId()));				  //被评论人的角色
						map.put("name",u.getName());		  //被评论人的姓名
						evaluatedList.add(map);
					}
					
					//获取法援人员审批节点的任务办理人
					loginName = historyService.createHistoricTaskInstanceQuery().processInstanceId(histTask.getId()).taskDefinitionKey("adi_approve").list().get(0).getAssignee();
					u= UserUtils.getByLoginName(loginName);
					if(u!=null && u.getId()!=""){
						map = Maps.newHashMap();
						map.put("beEvaluatedUserId", u.getId());  //被评论人的id
						map.put("avatar", u.getPhoto());	  //被评论人的照片
						map.put("roleName", getRoleById(u.getId()));				  //被评论人的角色
						map.put("name",u.getName());		  //被评论人的姓名
						evaluatedList.add(map);
					}
					
					e.setIsEvaluate(oaLegalAidDao.get(e.getBusinessId()).getIsEvaluate());
				}else if("people_mediation".equals(histTask.getProcessDefinitionKey())){
					if(u!=null && u.getId()!=""){
						map =  Maps.newHashMap();
						map.put("beEvaluatedUserId", u.getId());  //被评论人的id
						map.put("avatar", u.getPhoto());	  //被评论人的照片
						map.put("roleName", "人民调解员");				  //被评论人的角色
						map.put("name",u.getName());		  //被评论人的姓名
						evaluatedList.add(map);
					}else{
					}
					e.setIsEvaluate(oaPeopleMediationApplyDao.get(e.getBusinessId()).getIsEvaluate());
				}else if("fast_legal".equals(histTask.getProcessDefinitionKey())){
					if(u!=null && u.getId()!=""){
						map =  Maps.newHashMap();
						map.put("beEvaluatedUserId", u.getId());  //被评论人的id
						map.put("avatar", u.getPhoto());	  //被评论人的照片
						map.put("roleName", getRoleById(u.getId()));				  //被评论人的角色
						map.put("name",u.getName());		  //被评论人的姓名
						evaluatedList.add(map);
					}else{
					}
					e.setIsEvaluate(oaFastLegalDao.get(e.getBusinessId()).getIsEvaluate());
				}
				e.setEvaluatedList(evaluatedList);
			}
			
			actList.add(e);
		}
		page.setList(actList);
		return page;
	}
	/**
	 * 综合查询
	 * @param page
	 * @param act
	 * @return
	 */
	public Page<Act> getArchives(Page<Act> page, Act act) {
		act.setPage(page);
		List<Act> list = actDao.getArchives(act);
		page.setList(list);
		return page;
	}
	/**
     * 根据提交的信息初始化form内容
     * 并反射出人员分类相应的apiAgencySearch服务
     * @author 
     * @version
     * @param form
     */
    private String getRoleById(String id) {
    	
		//获取字典人员分类键值
    	List<String> userTypeValues = systemService.userTypeid(id);
    	String userTypeValue = "";
    	if(userTypeValues !=null && userTypeValues.size()>0){
    		userTypeValue = userTypeValues.get(0);
    	}
    	String userType = "";
    	if(StringUtils.isNotBlank(userTypeValue)){
	    	if(userTypeValue.equals("lawyer")){//律师
	    		userType = "律师";
	    	}else if(userTypeValue.equals("legal_service_person")){//基层法律服务者
	    		userType = "基层法律服务者";
	    	}else if(userTypeValue.equals("judiciaryUser")){//司法所工作人员
	    		userType = "司法所工作人员";
	    	}else if(userTypeValue.equals("lawAssitanceUser")){//法援
	    		userType = "法律援助工作人员";
	    	}else if(userTypeValue.equals("peopleMediation")){//调解员
	    		userType = "人民调解员";
	    	}else if(userTypeValue.equals("supervisor")){//人民监督员
	    		userType = "人民监督员";
	    	}else if(userTypeValue.equals("notaryMember")){//公证员
	    		userType = "公证员";
	    	}else if(userTypeValue.equals("infoForensicPersonnel")){//司法鉴定人员
	    		userType = "司法鉴定人员";
	    	}
    	}
    	
		return userType;
    }
    /**
     * 判断该任务是否已经被领取，true代表领取了，false代表未领取
     * @param act
     * @return
     */
    public boolean isClaim(Act act){
    	boolean flag = false;
    	Task task = taskService.createTaskQuery().taskId(act.getTaskId()).singleResult();
    	if(task.getAssignee() != null){
    		flag = true;
    	}
    	return flag;
    } 

    /** 
     * 根据任务ID获取流程定义 
     *  
     * @param taskId 
     *            任务ID 
     * @return 
     * @throws Exception 
     */  
    private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception {  
        // 取得流程定义  
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)  
                .getDeployedProcessDefinition(getTaskEntity(taskId)  
                        .getProcessDefinitionId());  
        if (processDefinition == null) {  
            throw new Exception("流程定义未找到!");  
        }  
  
        return processDefinition;  
    }  
    /** 
     * 中止流程(特权人直接审批通过等) 
     *  
     * @param taskId 
     */  
    public void endProcess(String taskId) throws Exception {  
        ActivityImpl endActivity = findActivitiImpl(taskId, "defense_end");  
        commitProcess(taskId, null, endActivity.getId());  
    }  
    /** 
     * @param taskId 
     *            当前任务ID 
     * @param variables 
     *            流程变量 
     * @param activityId 
     *            流程转向执行任务节点ID<br> 
     *            此参数为空，默认为提交操作 
     * @throws Exception 
     */  
    private void commitProcess(String taskId, Map<String, Object> variables,  
            String activityId) throws Exception {  
        if (variables == null) {  
            variables = new HashMap<String, Object>();  
        }  
        // 跳转节点为空，默认提交操作  
        if (StringUtils.isNotBlank(activityId)) {  
            taskService.complete(taskId, variables);  
        } else {// 流程转向操作  
            turnTransition(taskId, activityId, variables);  
        }  
    } 
    /** 
     * 流程转向操作 
     *  
     * @param taskId 
     *            当前任务ID 
     * @param activityId 
     *            目标节点任务ID 
     * @param variables 
     *            流程变量 
     * @throws Exception 
     */  
    private void turnTransition(String taskId, String activityId,  
            Map<String, Object> variables) throws Exception {  
        // 当前节点  
        ActivityImpl currActivity = findActivitiImpl(taskId, null);  
        // 清空当前流向  
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);  
  
        // 创建新流向  
        TransitionImpl newTransition = currActivity.createOutgoingTransition();  
        // 目标节点  
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);  
        // 设置新流向的目标节点  
        newTransition.setDestination(pointActivity);  
  
        // 执行转向任务  
        taskService.complete(taskId, variables);  
        // 删除目标节点新流入  
        pointActivity.getIncomingTransitions().remove(newTransition);  
  
        // 还原以前流向  
        restoreTransition(currActivity, oriPvmTransitionList);  
    }  
    /** 
     * 根据任务ID和节点ID获取活动节点 <br> 
     *  
     * @param taskId 
     *            任务ID 
     * @param activityId 
     *            活动节点ID <br> 
     *            如果为null或""，则默认查询当前活动节点 <br> 
     *            如果为"end"，则查询结束节点 <br> 
     *  
     * @return 
     * @throws Exception 
     */  
    private ActivityImpl findActivitiImpl(String taskId, String activityId)  
            throws Exception {  
        // 取得流程定义  
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);  
  
        // 获取当前活动节点ID  
        if (StringUtils.isNotBlank(activityId)) {  
            activityId = getTaskEntity(taskId).getTaskDefinitionKey();  
        }  
  
        // 根据流程定义，获取该流程实例的结束节点  
        if (activityId.toUpperCase().equals("DEFENSE_FYZHUREN")) {  
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {  
                List<PvmTransition> pvmTransitionList = activityImpl  
                        .getOutgoingTransitions();  
                if (pvmTransitionList.isEmpty()) {  
                    return activityImpl;  
                }  
            }  
        }  
        // 根据节点ID，获取对应的活动节点  
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition)  
                .findActivity(activityId);  

        return activityImpl;  
    }  

    /** 
     * 清空指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @return 节点流向集合 
     */  
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {  
        // 存储当前节点所有流向临时变量  
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();  
        // 获取当前节点所有流向，存储到临时变量，然后清空  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        for (PvmTransition pvmTransition : pvmTransitionList) {  
            oriPvmTransitionList.add(pvmTransition);  
        }  
        pvmTransitionList.clear();  
  
        return oriPvmTransitionList;  
    } 
    /** 
     * 还原指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @param oriPvmTransitionList 
     *            原有节点流向集合 
     */  
    private void restoreTransition(ActivityImpl activityImpl,  
            List<PvmTransition> oriPvmTransitionList) {  
        // 清空现有流向  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        pvmTransitionList.clear();  
        // 还原以前流向  
        for (PvmTransition pvmTransition : oriPvmTransitionList) {  
            pvmTransitionList.add(pvmTransition);  
        }  
    }  
    
	  /**
	   * 
     * 作废插入流程数据
     * @param act
     */
    public void taskinst(Act act){
	    String userId = UserUtils.getUser().getLoginName();
		Date date = new Date();
		Map<String, Object> params = Maps.newHashMap();
		params.put("procInstId", act.getProcInsId());
		params.put("assignee", userId);
		params.put("taskId", act.getTaskId());
		params.put("claimTime", date);
		params.put("endTime", date);
		params.put("taskDefKey", act.getTaskDefKey());
		params.put("actId", act.getTaskDefKey());
		actDao.updateTask(params);
		actDao.updateActinst(params);
    }
    
    /**
     * 综合查询，从流程状态表中查询
     * @param page
     * @param oaProcessState
     * @return
     */
	public Page<ProcessStateVo> getComprehensiveQuery(Page<ProcessStateVo> page, ProcessStateVo processStateVo) {
		// TODO Auto-generated method stub
		processStateVo.setPage(page);
		List<ProcessStateVo> list = oaProcessStateDao.comprehensiveQueryForApi(processStateVo);
		page.setList(list);
		return page;
	}
	/**
	 * 给接口使用,获取我申请的已完成的评论列表
	 * @param page
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	public Page<ActVo> ApiGetCommentList(Page<ActVo> page, Act act){
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		HistoricProcessInstanceQuery histTaskQuery = historyService.createHistoricProcessInstanceQuery().startedBy(userId).includeProcessVariables().orderByProcessInstanceStartTime().desc();
		
		// 设置查询条件
		if (StringUtils.isNotBlank(act.getProcDefKey())){
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null){
			histTaskQuery.startedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null){
			histTaskQuery.startedBefore(act.getEndDate());
		}
		if(StringUtils.isNotBlank(act.getStatus())){
			if("1".equals(act.getStatus())){
				histTaskQuery.unfinished();		//未完成，还在流程中的
			}else if("2".equals(act.getStatus())){
				histTaskQuery.finished();			//已完成归档的
			}
		}
		if (StringUtils.isNotBlank(act.getTitle())){
			histTaskQuery.variableValueLike("title", "%"+act.getTitle()+"%");
		}
		List<String> list = Lists.newArrayList();
		list.add("legal_aid");
		list.add("people_mediation");
		list.add("fast_legal");
		histTaskQuery.processDefinitionKeyIn(list);
		// 查询总数
		page.setCount(histTaskQuery.count());
		
		// 查询列表
		List<HistoricProcessInstance> histList = histTaskQuery.listPage(page.getFirstResult(), page.getMaxResults());
		//处理分页问题
		List<ActVo> actList=Lists.newArrayList();
		Task task = null;
		for (HistoricProcessInstance histTask : histList) {
			
				ActVo e = new ActVo();
				e.setTask(histTask);
				String[] ss = histTask.getBusinessKey().split(":");
				e.setBusinessTable(ss[0]);
				e.setBusinessId(ss[1]);
				e.setVars(histTask.getProcessVariables());
				e.setProcDefId(histTask.getProcessDefinitionId());
				e.setProcDefKey(histTask.getProcessDefinitionKey());
				e.setProcInsId(histTask.getId());
				e.setVersion(histTask.getProcessDefinitionVersion().toString());
				e.setProcDefName(ProcessDefCache.get(histTask.getProcessDefinitionId()).getName());
				task = getCurrentTaskByProcInsId(histTask.getId());
				if(task!=null) {
					e.setTaskId(task.getId());
					e.setTaskDefKey(task.getTaskDefinitionKey());
					e.setTaskName(task.getName());
				}
				else {
					HistoricTaskInstance temp = getCompleteCurrentTaskByProcInsId(histTask.getId());
					
					List<Map<String,Object>> evaluatedList = Lists.newArrayList();
					Map<String,Object> map= null;
					
					
					e.setTaskId(temp.getId());
					e.setTaskDefKey(temp.getTaskDefinitionKey());
					e.setTaskName(temp.getName());
					
					String loginName = temp.getAssignee();
					User u= UserUtils.getByLoginName(loginName);
					//当是法律援助的时候，会有两个人需要评价
					if("legal_aid".equals(e.getProcDefKey())){
						if(u!=null && u.getId()!=""){
							map = Maps.newHashMap();
							map.put("beEvaluatedUserId", u.getId());  //被评论人的id
							map.put("avatar", u.getPhoto());	  //被评论人的照片
							map.put("roleName", getRoleById(u.getId()));				  //被评论人的角色
							map.put("name",u.getName());		  //被评论人的姓名
							evaluatedList.add(map);
						}
						
						//获取法援人员审批节点的任务办理人
						loginName = historyService.createHistoricTaskInstanceQuery().processInstanceId(histTask.getId()).taskDefinitionKey("adi_approve").list().get(0).getAssignee();
						u= UserUtils.getByLoginName(loginName);
						if(u!=null && u.getId()!=""){
							map = Maps.newHashMap();
							map.put("beEvaluatedUserId", u.getId());  //被评论人的id
							map.put("avatar", u.getPhoto());	  //被评论人的照片
							map.put("roleName", getRoleById(u.getId()));				  //被评论人的角色
							map.put("name",u.getName());		  //被评论人的姓名
							evaluatedList.add(map);
						}
						
						e.setIsEvaluate(oaLegalAidDao.get(e.getBusinessId()).getIsEvaluate());
					}else if("people_mediation".equals(histTask.getProcessDefinitionKey())){
						if(u!=null && u.getId()!=""){
							map =  Maps.newHashMap();
							map.put("beEvaluatedUserId", u.getId());  //被评论人的id
							map.put("avatar", u.getPhoto());	  //被评论人的照片
							map.put("roleName", "人民调解员");				  //被评论人的角色
							map.put("name",u.getName());		  //被评论人的姓名
							evaluatedList.add(map);
						}else{
						}
						e.setIsEvaluate(oaPeopleMediationApplyDao.get(e.getBusinessId()).getIsEvaluate());
					}else if("fast_legal".equals(histTask.getProcessDefinitionKey())){
						if(u!=null && u.getId()!=""){
							map =  Maps.newHashMap();
							map.put("beEvaluatedUserId", u.getId());  //被评论人的id
							map.put("avatar", u.getPhoto());	  //被评论人的照片
							map.put("roleName", getRoleById(u.getId()));				  //被评论人的角色
							map.put("name",u.getName());		  //被评论人的姓名
							evaluatedList.add(map);
						}else{
						}
						e.setIsEvaluate(oaFastLegalDao.get(e.getBusinessId()).getIsEvaluate());
					}
					e.setEvaluatedList(evaluatedList);
				}
				
				actList.add(e);
			}
		page.setList(actList);
		return page;
	}
	
	/**
	 * 通过二维码来获取节点信息
	 * @param string
	 * @return
	 */
	public Map<String, Object> getInfoByQRCode(String procInsId) {
		// TODO Auto-generated method stub
		//节点信息中需要有各个节点的状态,各个几点的名称就可以了
		//详细信息中需要有当前任务的状态,和当前任务的节点名称,开始时间,标题,还有当前办理人
		Map<String, Object> resultMap = Maps.newHashMap();			//存放所有的信息,用来返回
		List<Act> nodeList = Lists.newArrayList();  //存放节点信息
		Map<String, Object> infoMap = Maps.newHashMap();  //存放业务信息
		
		List<HistoricTaskInstance> list = processEngine.getHistoryService()//与历史数据（历史表）相关的Service
				.createHistoricTaskInstanceQuery()//创建历史任务实例查询
				.processInstanceId(procInsId)//
				.orderByHistoricTaskInstanceStartTime().asc()
				.list();
		
		if(list!=null && list.size()>0){
			for(int i = 0 ; i < list.size() ; i ++){
				HistoricTaskInstance hti = list.get(i);
				Act act  = new Act();
				act.setHisProcIns(processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(procInsId).singleResult());
				act.setTaskId(hti.getId());
				act.setTaskName(hti.getName());
				act.setProcDefId(hti.getProcessDefinitionId());
				act.setTaskDefKey(hti.getTaskDefinitionKey());
				if (hti.getEndTime()!=null) {
					act.setStatus("2");//代表节点任务完成
				}else if(hti.getStartTime()!=null & hti.getEndTime()==null){
					act.setStatus("1");//代表节点进行中
				}
				act.setProcInsId(procInsId);
				nodeList.add(act);
			}
			
			/*Task task = getCurrentTaskByProcInsId(procInsId);
			if(task == null){
				HistoricTaskInstance temp = getCompleteCurrentTaskByProcInsId(procInsId);
				historyService.createHistoricVariableInstanceQuery().processInstanceId(procInsId).list();
				infoMap.put("status", "已完成");
				infoMap.put("applyUser", UserUtils.getByLoginName((String) list.get(0).getProcessVariables().get("apply"))); //申请人
				infoMap.put("applyDate", list.get(0).getCreateTime());
				System.out.println(list.get(0).getProcessVariables().get("var"));
			}else{
				infoMap.put("status", "进行中");
				infoMap.put("applyUser", UserUtils.getByLoginName((String) list.get(0).getProcessVariables().get("apply"))); //申请人
				infoMap.put("assignee", UserUtils.getByLoginName(task.getAssignee())); //任务办理人
				infoMap.put("applyDate", list.get(0).getCreateTime());
			}*/
			
			OaProcessState oaProcessState = oaProcessStateDao.getByProcInsId(procInsId);
			infoMap.put("applyUser",UserUtils.get(oaProcessState.getCreateBy().getId()).getName());
			infoMap.put("applyDate",DateUtils.formatDate(oaProcessState.getCreateDate(), "yyyy-MM-dd"));
			infoMap.put("title", oaProcessState.getTitle());
			infoMap.put("status", oaProcessState.getStateDesc());
			infoMap.put("caseType", oaProcessState.getCaseTypeDesc());
			///暂时有问题
			if("1".equals(oaProcessState.getState())|| "4".equals(oaProcessState.getState())){
				Task task = getCurrentTaskByProcInsId(procInsId);
				String assignee = task.getAssignee();
				if(assignee!="" && assignee!=null){
					infoMap.put("contractor", UserUtils.getByLoginName(assignee).getName());
				}
			}
		}
		
		resultMap.put("nodeList", nodeList);
		resultMap.put("infoMap", infoMap);
		
		return resultMap;
	}
	
	/**
     *满意度查询
     * @param page
     * @param oaProcessState
     * @return
     */
	public Page<StarLevel> getStarLevel(Page<StarLevel> page, StarLevel starLevel) {
		// TODO Auto-generated method stub
		starLevel.setPage(page);
		List<StarLevel> list = page.getList();
		if(StringUtils.isNotBlank(starLevel.getGroupType())){
			if(starLevel.getGroupType().equals("0")){
				list = page.getList();
			}else if(starLevel.getGroupType().equals("1")){//地区
				list = starLevelDao.findListByArea(starLevel);
			}else if(starLevel.getGroupType().equals("2")){//机构
				list = starLevelDao.findListByOffice(starLevel);
			}else if(starLevel.getGroupType().equals("3")){//查询
				if(starLevel.getType().equals("1")){//律师
					list = starLevelDao.findListByLawyer(starLevel);
				}else if(starLevel.getType().equals("6")) {//公证员
					list = starLevelDao.findListByNotaryMeber(starLevel);
				}else if(starLevel.getType().equals("9")) {//基层法律服务者
					list = starLevelDao.findListByLegalServicePerson(starLevel);
				}else if(starLevel.getType().equals("11")) {//司法所工作人员
					list = starLevelDao.findListByJudiciaryUser(starLevel);
				}else if(starLevel.getType().equals("13")) {//法援
					list = starLevelDao.findListByLawAssitanceUser(starLevel);
				}else if(starLevel.getType().equals("14")) {//调解员
					list = starLevelDao.findListByPeopleMediation(starLevel);
				}
			}
		}
			
		page.setList(list);
		return page;
	}
	
	/**
	 * 地区
	 * @param id
	 * @return
	 */
	public String area(String id) {
		return starLevelDao.area(id);
	}
	
	/**
	 * 机构
	 * @param id
	 * @return
	 */
	public String office(String id) {
		return starLevelDao.office(id);
	}
}
