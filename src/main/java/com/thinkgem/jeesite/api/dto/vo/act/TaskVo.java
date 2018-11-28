package com.thinkgem.jeesite.api.dto.vo.act;

import java.util.Date;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TaskVo {
	 private String id;
	 private String name;
	 private String taskDefinitionKey;
	 private String processInstanceId;
	 private String processDefinitionId;
	 private Date createTime;
	 private String assignee;
	 private Date endTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public TaskVo(Task task) {
		this.assignee=task.getAssignee();
        this.createTime=task.getCreateTime();
        this.id=task.getId();
        this.name=task.getName();
        this.processDefinitionId=task.getProcessDefinitionId();
        this.processInstanceId=task.getProcessInstanceId();
        this.taskDefinitionKey=task.getTaskDefinitionKey();
	}
	public TaskVo(HistoricTaskInstance task) {
		this.assignee=task.getAssignee();
        this.createTime=task.getCreateTime();
        this.id=task.getId();
        this.name=task.getName();
        this.processDefinitionId=task.getProcessDefinitionId();
        this.processInstanceId=task.getProcessInstanceId();
        this.taskDefinitionKey=task.getTaskDefinitionKey();
	}
	public TaskVo(HistoricProcessInstance task) {
		//this.assignee=task.getAssignee();
        this.createTime=task.getStartTime();
        this.endTime=task.getEndTime();
        this.name=task.getName();
        this.processDefinitionId=task.getProcessDefinitionId();
        this.processInstanceId=task.getId();
	}
}
