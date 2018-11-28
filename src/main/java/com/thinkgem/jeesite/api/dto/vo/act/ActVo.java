/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.api.dto.vo.act;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.utils.Variable;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 工作流Entity
 * @author ThinkGem
 * @version 2013-11-03
 */
@JsonInclude
public class ActVo {
	
	private static final long serialVersionUID = 1L;

	private String procInsId; 	// 流程实例ID
	private String procDefId; 	// 流程定义ID
	private String procDefKey; 	// 流程定义Key（流程定义标识）
	private String taskId; 		// 任务编号
	private String taskName; 	// 任务名称
	private String taskDefKey; 	// 任务定义Key（任务环节标识）

	private String businessTable;	// 业务绑定Table
	private String businessId;		// 业务绑定ID
	private String title; 		// 任务标题
	private TaskVo task; 			// 任务对象
	private String assignee; // 任务执行人编号
	private String assigneeName; // 任务执行人名称
	private String  procDefName;//流程名称
	private Variable vars; 		// 流程变量
	private Object businessData; //业务数据
    private String version;
    private String commentId;      //任务完成后 ， 普通用户评论时，用来关联 业务表的字段， 法律援助用的是法律援助业务表的主键id，人民调解用的是流程实例id（procInsId）
//    private String currentUserId;  //任务完成后获取列表时将最后一个节点办理人的信息给返回回来 
//    private String avatar;         //被评论人的头像 
    private String isEvaluate;     //是否评价  0是未评论，1是已经评论
    

	private List<Map<String,Object>> evaluatedList = Lists.newArrayList();


	public ActVo() {
		super();
	}
	
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getTaskDefKey() {
		return taskDefKey;
	}


	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public Object getBusinessData() {
		return businessData;
	}

	public void setBusinessData(Object businessData) {
		this.businessData = businessData;
	}

	public String getProcDefId() {
		if (procDefId == null && task != null){
			procDefId = task.getProcessDefinitionId();
		}
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public String getProcInsId() {
		if (procInsId == null && task != null){
			procInsId = task.getProcessInstanceId();
		}
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessTable() {
		return businessTable;
	}

	public void setBusinessTable(String businessTable) {
		this.businessTable = businessTable;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getAssignee() {
		if (assignee == null && task != null){
			assignee = task.getAssignee();
		}
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}


	public Variable getVars() {
		return vars;
	}

	public void setVars(Variable vars) {
		this.vars = vars;
	}
	
	/**
	 * 通过Map设置流程变量值
	 * @param map
	 */
	public void setVars(Map<String, Object> map) {
		this.vars = new Variable(map);
	}

	/**
	 * 获取流程定义KEY
	 * @return
	 */
	public String getProcDefKey() {
		if (StringUtils.isBlank(procDefKey) && StringUtils.isNotBlank(procDefId)){
			procDefKey = StringUtils.split(procDefId, ":")[0];
		}
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}
	
	public TaskVo getTask() {
		return task;
	}

	public void setTask(Task tasks) {
		this.task = new TaskVo(tasks);
	}
	public void setTask(HistoricTaskInstance tasks) {
		this.task = new TaskVo(tasks);
	}
	public void setTask(HistoricProcessInstance tasks) {
		this.task = new TaskVo(tasks);
	}
	
	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public ActVo(Act act) {
		this.taskId = act.getTaskId();
		this.taskName = act.getTaskName();
		this.taskDefKey = act.getTaskDefKey();
		this.assignee=act.getAssignee();
		this.assigneeName=act.getAssigneeName();
		this.businessId=act.getBusinessId();
		this.businessTable=act.getBusinessTable();
		this.procDefId=act.getProcDefId();
		this.procDefKey=act.getProcDefKey();
		this.procInsId=act.getProcInsId();
		this.title=act.getTitle();
		this.vars=act.getVars();
		if(act.getProcDef()!=null) {
			this.procDefName=act.getProcDef().getName();
			this.version=act.getProcDef().getVersion()+"";
		}
		if(act.getTask()!=null){
			this.setTask(act.getTask());
		}else
		if(act.getHistTask()!=null){
			this.setTask(act.getHistTask());
		}else
        if(act.getHisProcIns()!=null){
        	this.setTask(act.getHisProcIns());
        }
	}

	public String getProcDefName() {
		return procDefName;
	}

	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	public void setTask(TaskVo task) {
		this.task = task;
	}
	


	public String getCommentId() {
		if(procDefKey!=null && !"".equals(procDefKey)){
			if("legal_aid".equals(procDefKey)){
				return businessId;
			}else if("people_mediation".equals(procDefKey)){
				return procInsId;
			}else if("fast_legal".equals(procDefKey)){
				return businessId;
			}
		}
		return commentId;
	}


	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	
	public List<Map<String,Object>> getEvaluatedList() {
		return evaluatedList;
	}


	public void setEvaluatedList(List<Map<String,Object>> evaluatedList) {
		this.evaluatedList = evaluatedList;
	}
	
	public String getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(String isEvaluate) {
		this.isEvaluate = isEvaluate;
	}
}
