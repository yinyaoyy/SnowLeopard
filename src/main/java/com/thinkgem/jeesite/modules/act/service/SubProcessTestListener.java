package com.thinkgem.jeesite.modules.act.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

public class SubProcessTestListener implements ExecutionListener,TaskListener{
	private static final long serialVersionUID = 1L;  
	  
    @Autowired  
    private RuntimeService runtimeService;  
    @Override
	public void notify(DelegateExecution execution) throws Exception {
		String eventName = execution.getEventName();  
		//start  
		if ("start".equals(eventName)) {  
		System.out.println("start=========");  
		}else if ("end".equals(eventName)) {  
		System.out.println("end=========");  
		}  
		else if ("take".equals(eventName)) {  
		System.out.println("take=========");  
		}  
		
	}  
    @Override 
    public void notify(DelegateTask delegateTask){  
        System.err.println("1.子流程任务创建======delegateTask{}:" + delegateTask.getId());  
        //获取子流程变量  
        DelegateExecution execution =  delegateTask.getExecution();  
        Long businessId = (Long)execution.getVariable("businessId");  
        Integer businessType = (Integer)execution.getVariable("businessType");  
        Long businessKey = (Long)execution.getVariable("businessKey");  
        System.err.println(  
            "2.获取子流程参数======businessId:" + businessId + ";businessType:" + businessType  
                + ";businessKey:" + businessKey);  
  
        //子流程实例set业务单号和主流程保持一致  
        runtimeService.updateBusinessKey(delegateTask.getProcessInstanceId(), businessKey.toString());  
    }

	
}
