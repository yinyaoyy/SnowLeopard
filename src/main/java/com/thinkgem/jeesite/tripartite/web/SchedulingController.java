/**
 * 
 */
package com.thinkgem.jeesite.tripartite.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.tripartite.entity.TripartiteTaskEntity;
import com.thinkgem.jeesite.tripartite.service.SchedulingImplService;

/**
 * @author 王鹏
 * @version 2018-07-01 17:18:59
 */
@Controller
@RequestMapping(value = "${adminPath}/tripartite/scheduling")
public class SchedulingController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SchedulingController.class);
    
	@Autowired
	private SchedulingImplService schedulingImplService;
	
	@RequiresPermissions("tripartite:scheduling:run")
	@RequestMapping(value = "runTask")
	public @ResponseBody String runTask(String systemId, String taskId) {
		//获取调度详细信息
		TripartiteTaskEntity tte = schedulingImplService.getTaskById(systemId, taskId);
		//发送请求
		String msg = schedulingImplService.sendRequest(tte);
		logger.debug("任务处理完毕systemId=["+systemId+"]taskId=["+taskId+"]详细："+JSON.toJSONString(tte));
		return msg;
	}
}
