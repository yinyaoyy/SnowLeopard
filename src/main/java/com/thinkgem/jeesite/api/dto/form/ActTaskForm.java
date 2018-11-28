/**
 * 
 */
package com.thinkgem.jeesite.api.dto.form;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;

/**
 * @author 王鹏
 * @version 2018-05-24 08:47:48
 */
public class ActTaskForm extends BaseForm<ActTaskForm> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3477701256429420760L;

	private String businessId ; // 业务主键(法援申请表id)
	private String procInsId  ; // 流程实例ID
	private String procDefId  ; // 流程定义ID
	private String procDefKey ; // 流程定义Key
	private String taskId     ; // 任务编号
	private String taskName   ; // 任务名称
	private String taskDefKey ; // 任务定义Key
	private String flag       ; // 意见状态
	private String comment    ; // 意见备注

	public Act toAct() {
		Act act = new Act();
		act.setBusinessId(businessId);
		act.setProcInsId(procInsId);
		act.setProcDefId(procDefId);
		act.setProcDefKey(procDefKey);
		act.setTaskId(taskId);
		act.setTaskName(taskName);
		act.setTaskDefKey(taskDefKey);
		act.setFlag(flag);
		act.setComment(comment);
		return act;
	}

	/**
	 * 校验act参数是否完整
	 * @author 王鹏
	 * @version 2018-05-23 21:14:47
	 * @param act
	 * @return
	 */
	public static String checkTaskParam(Act act) {
		StringBuffer msg = new StringBuffer();
		if(StringUtils.isBlank(act.getProcInsId())) {
			msg.append("流程实例ID不能为空。");
		}
		if(StringUtils.isBlank(act.getProcDefId())) {
			msg.append("流程定义ID不能为空。");
		}
		if(StringUtils.isBlank(act.getProcDefKey())) {
			msg.append("流程定义Key不能为空。");
		}
		if(StringUtils.isBlank(act.getTaskId())) {
			msg.append("任务编号不能为空。");
		}
		if(StringUtils.isBlank(act.getTaskName())) {
			msg.append("任务名称不能为空。");
		}
		if(StringUtils.isBlank(act.getTaskDefKey())) {
			msg.append("任务定义Key不能为空。");
		}
		return msg.toString();
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getProcInsId() {
		return procInsId;
	}
	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	public String getProcDefId() {
		return procDefId;
	}
	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}
	public String getProcDefKey() {
		return procDefKey;
	}
	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
