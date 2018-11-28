/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.act.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.api.act.entity.ProcessStateVo;
import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.act.utils.ActUtils;
import com.thinkgem.jeesite.modules.info.entity.LegalServicePerson;
import com.thinkgem.jeesite.modules.info.entity.StarLevel;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 流程个人任务相关Controller
 * @author ThinkGem
 * @version 2013-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/act/task")
public class ActTaskController extends BaseController {

	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private AreaService areaService;
	
	/**
	 * 获取待办列表
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = {"todo", ""})
	public String todoList(Act act,HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Page<Act> page = new Page<Act>(request, response);
		page = actTaskService.todoList(page,act);
		model.addAttribute("page", page);
		return "modules/act/actTaskTodoList";
	}
	
	/**
	 * 获取已办任务
	 * @param page
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = "historic")
	public String historicList(Act act, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Page<Act> page = new Page<Act>(request, response);
		page = actTaskService.historicList(page, act);
		model.addAttribute("page", page);
		return "modules/act/actTaskHistoricList";
	}

	/**
	 * 获取流转历史列表
	 * @param procInsId 流程实例
	 * @param startAct 开始活动节点名称
	 * @param endAct 结束活动节点名称
	 */
	@RequestMapping(value = "histoicFlow")
	public String histoicFlow(Act act, String startAct, String endAct, Model model){
		if (StringUtils.isNotBlank(act.getProcInsId())){
			List<Act> histoicFlowList = actTaskService.histoicFlowList(act.getProcInsId(), startAct, endAct);
			model.addAttribute("histoicFlowList", histoicFlowList);
		}
		return "modules/act/actTaskHistoricFlow";
	}
	
	/**
	 * 获取流程列表
	 * @param category 流程分类
	 */
	@RequestMapping(value = "process")
	public String processList(String category, HttpServletRequest request, HttpServletResponse response, Model model) {
	    Page<Object[]> page = new Page<Object[]>(request, response);
	    page = actTaskService.processList(page, category);
		model.addAttribute("page", page);
		model.addAttribute("category", category);
		return "modules/act/actTaskProcessList";
	}
	
	/**
	 * 获取流程表单
	 * @param taskId	任务ID
	 * @param taskName	任务名称
	 * @param taskDefKey 任务环节标识
	 * @param procInsId 流程实例ID
	 * @param procDefId 流程定义ID
	 */
	@RequestMapping(value = "form")
	public String form(Act act, HttpServletRequest request, Model model){
		String zt = request.getParameter("zt"); 
		// 获取流程XML上的表单KEY
		String formKey = actTaskService.getFormKey(act.getProcDefId(), act.getTaskDefKey());
		// 获取流程xml上的备注信息，备注信息表示的是该节点对应的数据表名称
		// String tableName = actTaskService.getDocumentByProDefId(act.getProcInsId(), act.getTaskDefKey());
		// tableName = "";

		// // 获取流程实例对象
		// if (act.getProcInsId() != null){
		// 	act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
		// 	if(!"".equals(tableName)){
		// 		actTaskService.getDataId(act,actTaskService.getProcIns(act.getProcInsId()),tableName);
		// 	}
		// 	if(act.getProcIns()==null){
		// 		act.setHisProcIns(actTaskService.getHisProcIns(act.getProcInsId()));
				
		// 		String proInsId = actTaskService.getHisProcIns(act.getProcInsId()).getId();
		// 		tableName = actTaskService.getDocumentByProDefId(proInsId, act.getTaskDefKey());
		// 		if(!"".equals(tableName)){
		// 			actTaskService.getDataId(act,actTaskService.getHisProcIns(act.getProcInsId()),tableName);
		// 		}
		// 	}
		// }
		// 获取流程实例对象
    		if (act.getProcInsId() != null){
    			act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
    			if(act.getProcIns()==null){
    				act.setHisProcIns(actTaskService.getHisProcIns(act.getProcInsId()));
    			}
    		}
		return "redirect:" + ActUtils.getFormUrl(formKey, act , zt);
	}
	
	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 */
	@RequestMapping(value = "start")
	@ResponseBody
	public String start(Act act, String table, String id, Model model) throws Exception {
		actTaskService.startProcess(act.getProcDefKey(), act.getBusinessId(), act.getBusinessTable(), act.getTitle());
		return "true";//adminPath + "/act/task";
	}

	/**
	 * 签收任务
	 * @param taskId 任务ID
	 */
	@RequestMapping(value = "claim")
	@ResponseBody
	public String claim(Act act) {
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		actTaskService.claim(act.getTaskId(), userId);
		return "true";//adminPath + "/act/task";
	}
	
	/**
	 * 完成任务
	 * @param taskId 任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment 任务提交意见的内容
	 * @param vars 任务流程变量，如下
	 * 		vars.keys=flag,pass
	 * 		vars.values=1,true
	 * 		vars.types=S,B  @see com.thinkgem.jeesite.modules.act.utils.PropertyType
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	public String complete(Act act) {
		actTaskService.complete(act.getTaskId(), act.getProcInsId(), act.getComment(), act.getVars().getVariableMap());
		return "true";//adminPath + "/act/task";
	}
	
	/**
	 * 读取带跟踪的图片
	 */
	@RequestMapping(value = "trace/photo/{procDefId}/{execId}")
	public void tracePhoto(@PathVariable("procDefId") String procDefId, @PathVariable("execId") String execId, HttpServletResponse response) throws Exception {
		InputStream imageStream = actTaskService.tracePhoto(procDefId, execId);
		
		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}
	
	/**
	 * 输出跟踪流程信息
	 * 
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "trace/info/{proInsId}")
	public List<Map<String, Object>> traceInfo(@PathVariable("proInsId") String proInsId) throws Exception {
		List<Map<String, Object>> activityInfos = actTaskService.traceProcess(proInsId);
		return activityInfos;
	}

	/**
	 * 显示流程图
	 
	@RequestMapping(value = "processPic")
	public void processPic(String procDefId, HttpServletResponse response) throws Exception {
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
		String diagramResourceName = procDef.getDiagramResourceName();
		InputStream imageStream = repositoryService.getResourceAsStream(procDef.getDeploymentId(), diagramResourceName);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}*/
	
	/**
	 * 获取跟踪信息
	 
	@RequestMapping(value = "processMap")
	public String processMap(String procDefId, String proInstId, Model model)
			throws Exception {
		List<ActivityImpl> actImpls = new ArrayList<ActivityImpl>();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().processDefinitionId(procDefId)
				.singleResult();
		ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
		String processDefinitionId = pdImpl.getId();// 流程标识
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
		List<String> activeActivityIds = runtimeService.getActiveActivityIds(proInstId);
		for (String activeId : activeActivityIds) {
			for (ActivityImpl activityImpl : activitiList) {
				String id = activityImpl.getId();
				if (activityImpl.isScope()) {
					if (activityImpl.getActivities().size() > 1) {
						List<ActivityImpl> subAcList = activityImpl
								.getActivities();
						for (ActivityImpl subActImpl : subAcList) {
							String subid = subActImpl.getId();
							System.out.println("subImpl:" + subid);
							if (activeId.equals(subid)) {// 获得执行到那个节点
								actImpls.add(subActImpl);
								break;
							}
						}
					}
				}
				if (activeId.equals(id)) {// 获得执行到那个节点
					actImpls.add(activityImpl);
					System.out.println(id);
				}
			}
		}
		model.addAttribute("procDefId", procDefId);
		model.addAttribute("proInstId", proInstId);
		model.addAttribute("actImpls", actImpls);
		return "modules/act/actTaskMap";
	}*/
	
	/**
	 * 删除任务
	 * @param taskId 流程实例ID
	 * @param reason 删除原因
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "deleteTask")
	public String deleteTask(String taskId, String reason, RedirectAttributes redirectAttributes) {
		if (StringUtils.isBlank(reason)){
			addMessage(redirectAttributes, "请填写删除原因");
		}else{
			actTaskService.deleteTask(taskId, reason);
			addMessage(redirectAttributes, "删除任务成功，任务ID=" + taskId);
		}
		return "redirect:" + adminPath + "/act/task";
	}
	/**
	 * 获取全部待办列表
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = {"all", ""})
	public String all(Act act,HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Page<Act> page = new Page<Act>(request, response);
		//page = actTaskService.todoLists(page,act);
		page = actTaskService.getUserAllList(page,act);
		model.addAttribute("page", page);
		return "modules/act/actTaskTodoListAll";
	}
	/**
	 * 获取获取创建人创建的任务
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = "allCreater")
	public String allCreater(Act act,HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Page<Act> page = new Page<Act>(request, response);
		page = actTaskService.historicCreaterUserList(page,act);
		model.addAttribute("page", page);
		return "modules/act/actAllCreateList";
	}

	/**
	 *综合查询（查询各个表然后汇总）
	 * 
	 * @return
	 */
	@RequestMapping(value = {"archives"})
	public String getArchives(Act act,HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		Page<Act> page = new Page<Act>(request, response);
		page = actTaskService.getArchives(page,act);
		model.addAttribute("page", page);
		return "modules/act/actTaskArchivesList";
	}
	
	/**
	 *
	 * 综合查询（查询流程状态表）
	 * @return
	 */
	@RequestMapping(value = {"comprehensiveQuery"})
	public String getComprehensiveQuery(ProcessStateVo processStateVo,HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		Page<ProcessStateVo> page = new Page<ProcessStateVo>(request, response);
		page = actTaskService.getComprehensiveQuery(page,processStateVo);
		model.addAttribute("page", page);
		return "modules/act/actTaskComprehensiveList";
	}
	
	/**
	 *
	  * 满意度查询
	 * @return
	 */
	@RequestMapping(value = {"starLevel"})
	public String getStarLevel(StarLevel starLevel,HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		Page<StarLevel> page = new Page<StarLevel>(request, response);
		String areaName = "";
		String officeName = ""; 
		if(StringUtils.isNotBlank(starLevel.getAreaIds())){
			starLevel.setAreaId(starLevel.getAreaIds().split(","));
			String[] area = starLevel.getAreaId();
			for(int i=0;i<area.length;i++) {
				if(i == area.length-1) {
					areaName += actTaskService.area(area[i]);
				}else {
					areaName += actTaskService.area(area[i])+",";
				}
			}
		}
		if(StringUtils.isNotBlank(starLevel.getOfficeIds())){
			starLevel.setOfficeId(starLevel.getOfficeIds().split(","));
			String[] office = starLevel.getOfficeId();
			for(int i=0;i<office.length;i++) {
				if(i == office.length-1) {
					officeName += actTaskService.office(office[i]);
				}else {
					officeName += actTaskService.office(office[i])+",";
				}
			}
		}
		starLevel.setAreaName(areaName);
		starLevel.setOfficeName(officeName);
		page = actTaskService.getStarLevel(page,starLevel);
		model.addAttribute("page", page);
		return "modules/act/actTaskStarLevelList";
	}
	
	/**
	 * 导出满意度查询
	 * @param legalServicePerson
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
    @RequestMapping(value = "starLevelExport")
    public String exportFile(StarLevel starLevel, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		if(StringUtils.isNotBlank(starLevel.getAreaIds())){
			starLevel.setAreaId(starLevel.getAreaIds().split(","));
		}
		if(StringUtils.isNotBlank(starLevel.getOfficeIds())){
			starLevel.setOfficeId(starLevel.getOfficeIds().split(","));
		}
		boolean isTemplate = type!=null&&"1".equals(type);
		List<StarLevel> list = null;
		try {
			if(starLevel==null){
				starLevel=new StarLevel();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="满意度.xlsx";
            	list = new ArrayList<StarLevel>();
            }else{
            	fileName = "满意度-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<StarLevel> page = actTaskService.getStarLevel(new Page<StarLevel>(request, response, -1), starLevel);
        		list = page.getList();
            }
    		new ExportExcel("满意度", StarLevel.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/act/actTaskStarLevel/list?repage";
    }
	
}
