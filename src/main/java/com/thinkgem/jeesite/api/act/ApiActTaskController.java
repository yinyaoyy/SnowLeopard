/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.api.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.api.ActTaskBusiEnum;
import com.thinkgem.jeesite.api.dto.form.ActTaskForm;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.act.ActVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 流程个人任务相关api
 * @author lin
 * @version 2013-11-03
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping(value =  {"/api/200/530"})
public class ApiActTaskController {

	@Autowired
	private ActTaskService actTaskService;
	private CrudService crudService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService;
	private static final Logger log = LoggerFactory.getLogger(ApiActTaskController.class);
	/**
	 * 获取待办列表
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = "10")
	public ServerResponse<PageVo<ActVo>> todoList(BaseForm<Act> form)  {
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		int pageNo=0;
		int pageSize=0;
		try {
			 pageNo=StringUtils.toInteger(bject.getByte("pageNo"));
			 if(pageNo==0){
				 pageNo=1;
			 }
			 pageSize=StringUtils.toInteger(bject.getByte("pageSize"));
				if(pageSize==0){
					pageSize=Integer.valueOf(Global.getConfig("page.pageSize"));
				}
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
		}
		Act act=new Act();
		act.setProcDefKey(bject.getString("procDefKey"));
		String beginDate=bject.getString("beginDate");
		String endDate=bject.getString("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNoneBlank(beginDate)){
			try {
				act.setBeginDate(sdf.parse(beginDate));
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		if(StringUtils.isNoneBlank(endDate)){
			try {
				act.setEndDate(sdf.parse(endDate));
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		Page<Act> page=actTaskService.todoList(new Page<Act>(pageNo,pageSize),act);
		PageVo<ActVo> pageVo=new PageVo<ActVo>(page);
		pageVo.setList(this.change(page.getList()));
		ServerResponse<PageVo<ActVo>> result = ServerResponse.createBySuccess(pageVo);
		return result;
	}
	
	/**
	 * 获取已办任务
	 * @param page
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = "20")
	public ServerResponse<PageVo<ActVo>> historicList(BaseForm<Act> form){
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		int pageNo=0;
		int pageSize=0;
		try {
			 pageNo=StringUtils.toInteger(bject.getByte("pageNo"));
			 if(pageNo==0){
				 pageNo=1;
			 }
			 pageSize=StringUtils.toInteger(bject.getByte("pageSize"));
				if(pageSize==0){
					pageSize=Integer.valueOf(Global.getConfig("page.pageSize"));
				}
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
		}
		Act act=new Act();
		act.setProcDefKey(bject.getString("procDefKey"));
		String beginDate=bject.getString("beginDate");
		String endDate=bject.getString("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNoneBlank(beginDate)){
			try {
				act.setBeginDate(sdf.parse(beginDate));
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		if(StringUtils.isNoneBlank(endDate)){
			try {
				Calendar c = Calendar.getInstance();  
		        c.setTime(sdf.parse(endDate));  
		        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天 
				act.setEndDate(c.getTime());
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		Page<Act> page=actTaskService.historicList(new Page<Act>(pageNo,pageSize),act);
		PageVo<ActVo> pageVo=new PageVo<ActVo>(page);
		pageVo.setList(this.change(page.getList()));
		ServerResponse<PageVo<ActVo>> result = ServerResponse.createBySuccess(pageVo);
		return result;	
	}

	/**
	 * 获取流转历史列表
	 * @param procInsId 流程实例
	 * @param startAct 开始活动节点名称
	 * @param endAct 结束活动节点名称
	 */
	@RequestMapping(value = "30")
	public ServerResponse<List<Act>> histoicFlow(BaseForm<Act> form){
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		List<Act> histoicFlowList = actTaskService.histoicFlowList(bject.getString("procInsId"), "", "");
		ServerResponse<List<Act>> result = ServerResponse.createBySuccess(histoicFlowList);
		/*try {
			System.out.println(JsonMapper.getInstance().toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return result;
	}
	private List<ActVo> change(List<Act> list){
		List<ActVo> listVo=Lists.newArrayList();
		for (Act act : list) {
			listVo.add(new ActVo(act));
		}
		return listVo;
	}
	

    /**
     * 获取法律援助表单信息
     * @author 王鹏
     * @version 2018-05-23 20:58:44
     * @param form
     * @return
     */
	@RequestMapping("/40")
    public ServerResponse getLegalAid(BaseForm<ActTaskForm> form){
    	ServerResponse result = null;
    	try {
    		form.initQueryObj(new TypeReference<ActTaskForm>(){});
    		Act act = form.getQueryObj().toAct();
    		if(StringUtils.isNotBlank(ActTaskForm.checkTaskParam(act))) {
    			//如果校验参数时返回了错误信息，则要返回相应提示且不再继续处理
    			return ServerResponse.createByErrorCodeMessage(ResponseCode.ACT_PARAM_ERROR.getCode(),ResponseCode.ACT_PARAM_ERROR.getDesc());
    		}
    		// 获取流程实例对象
    		if (act.getProcInsId() != null){
    			act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
    			if(act.getProcIns()==null){
    				act.setHisProcIns(actTaskService.getHisProcIns(act.getProcInsId()));
    			}
    		}
    		//获取业务服务对象名称
    		String serviceName = ActTaskBusiEnum.getBusiByKey(act.getProcDefKey());
    		crudService = SpringContextHolder.getBean(serviceName);
    		ActVo actVo = new ActVo(act);
    		actVo.setBusinessData(crudService.get(act.getBusinessId()));
			result = ServerResponse.createBySuccess(actVo);
			log.debug("返回参数：{}",result);
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	


    /**
     * 0.获取律师事务所集合
     * 1.获取律师和律师事务所
     * @author 王鹏
     * @version 2018-5-24 21:12:42
     * @param form
     * @return
     */
	@RequestMapping("/50")
    public ServerResponse getLawOfficeLawyer(BaseForm<Map> form){
    	ServerResponse result = null;
		List<Map<String, Object>> lawList = Lists.newArrayList();
		String type = "0";
		String id = null;
		String areaId = null;
    	try {
    		form.initQueryObj(Map.class);
    		type = String.valueOf(form.getQueryObj().get("type"));
    		id = String.valueOf(form.getQueryObj().get("id"));
    		areaId = String.valueOf(form.getQueryObj().get("areaId"));
    		if(StringUtils.isBlank(type)) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(),"类型为空");
    		}
    		if(result==null) {
    			lawList=systemService.getOfficeUserApi("2",type, areaId, "",id);
    			result = ServerResponse.createBySuccess(lawList);
    		}
			log.debug("返回参数：{}",result);
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	

    /**
     * 1.获取法律服务所集合
     * 2.根据法律服务所获取法律服务工作者
     * 3.获取法律服务所和法律服务工作者
     * Map{type, id} type为2,3时必须有id
     * @author 王鹏
     * @version 2018-5-24 21:12:38
     * @param form
     * @return
     */
	@RequestMapping("/60")
    public ServerResponse getLegalOfficePerson(BaseForm<Map> form){
    	ServerResponse result = null;
		List<Map<String, Object>> legalList = Lists.newArrayList();
		String type = "0";
		String id = null;
		String areaId = null;
    	try {
    		form.initQueryObj(Map.class);
    		type = String.valueOf(form.getQueryObj().get("type"));
    		id = String.valueOf(form.getQueryObj().get("id"));
    		areaId = String.valueOf(form.getQueryObj().get("areaId"));
    		if(StringUtils.isBlank(type)) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(),"类型为空");
    		}
    		if(result==null) {
    			legalList=systemService.getOfficeUserApi("8",type, areaId, "",id);
    			result = ServerResponse.createBySuccess(legalList);
    		}
			log.debug("返回参数：{}",result);
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	
	/**
	 * 获取机构人员账号数据
	 * @author 王鹏
	 * @version 2018-05-24 21:15:37
	 * @return
	 */
	public List<Map<String, Object>> getOfficeUser(String type, String companyId ,String areaId){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Boolean isAll = null;//默认为空即可
		String extId = null;
		Long grade = null;
		List<Office> list = new ArrayList<Office>();
		if(StringUtils.isNotBlank(companyId)){
			list = officeService.getOfficeByCompanyId(companyId);
		}else{
			list = officeService.findList(isAll);
		}
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			//若存在地区则根据地区过滤机构及人员数据
			if(StringUtils.isNotBlank(areaId)&&!"5".equals(areaId)&&!areaId.equals(e.getArea().getId())){
				continue;
			}
			if("0ce09269a49a4f1d936ad29d74bfa385".equals( e.getId())){
				continue;
			}
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				List<Map<String, Object>> users = Lists.newArrayList();
				List<User> liste = systemService.findUserByOfficeId(e.getId());
				if(liste.size()>0){
					for (int y = 0; y<liste.size();y++ ){
						Map<String, Object> map3 = Maps.newHashMap();
						map3.put("id", liste.get(y).getId());
						map3.put("name", liste.get(y).getName());
						map3.put("pId",e.getId());
						map3.put("pIds", e.getParentIds()+e.getId());
						map3.put("type","3");
						users.add(map3);
					}
				}
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				map.put("users", users);
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 获取我的申请列表
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = "70")
	public ServerResponse<PageVo<ActVo>> getApplyList(BaseForm<Act> form)  {
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		int pageNo=0;
		int pageSize=0;
		try {
			 pageNo=StringUtils.toInteger(bject.getByte("pageNo"));
			 if(pageNo==0){
				 pageNo=1;
			 }
			 pageSize=StringUtils.toInteger(bject.getByte("pageSize"));
				if(pageSize==0){
					pageSize=Integer.valueOf(Global.getConfig("page.pageSize"));
				}
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
		}
		Act act=new Act();
		act.setProcDefKey(bject.getString("procDefKey"));
		String beginDate=bject.getString("beginDate");
		String endDate=bject.getString("endDate");
		String status = bject.getString("status");
		String title = bject.getString("title");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNoneBlank(beginDate)){
			try {
				act.setBeginDate(sdf.parse(beginDate));
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		if(StringUtils.isNoneBlank(endDate)){
			try {
				Calendar c = Calendar.getInstance();  
		        c.setTime(sdf.parse(endDate));  
		        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天 
				act.setEndDate(c.getTime());
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		if(StringUtils.isNoneBlank(status)){
			act.setStatus(status);
		}
		if(StringUtils.isNoneBlank(title)){
			act.setTitle(title);
		}
//		Page<Act> page=actTaskService.historicCreaterUserList(new Page<Act>(pageNo,pageSize),act);
		/*PageVo<ActVo> pageVo=new PageVo<ActVo>(page);
		pageVo.setList(this.change(page.getList()));
		ServerResponse<PageVo<ActVo>> result = ServerResponse.createBySuccess(pageVo);*/
		Page<ActVo> page=actTaskService.ApihistoricCreaterUserList(new Page<ActVo>(pageNo,pageSize),act);
		PageVo<ActVo> pageVo=new PageVo<ActVo>(page);
		pageVo.setList(page.getList());
		ServerResponse<PageVo<ActVo>> result = ServerResponse.createBySuccess(pageVo);
		return result;
	}
	

    /**
     * 挂起或激活一个流程
     * @author 王鹏
     * @version 2018-5-27 17:15:53
     * @param form
     * @return
     */
	@RequestMapping("/90")
    public ServerResponse suspendActivate(BaseForm<ActTaskForm> form){
    	ServerResponse result = null;
    	try {
    		ActTaskForm atf = form.initQueryObj(ActTaskForm.class);
    		if(StringUtils.isBlank(atf.getProcInsId()) || StringUtils.isBlank(atf.getFlag())
    				|| StringUtils.isBlank(atf.getComment())) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		else {
    			if("yes".equals(atf.getFlag())) {//激活流程
    				actTaskService.activateProcessInstance(atf.getProcInsId(), atf.getComment());
    			}
    			if("no".equals(atf.getFlag())) {//挂起流程
    				actTaskService.suspendProcessInstance(atf.getProcInsId(), atf.getComment());
    			}
    			result = ServerResponse.createBySuccess(null);
    		}
			log.debug("返回参数：{}",result);
    	} catch (ActivitiException ae) {
    		if(StringUtils.isNotBlank(ae.getMessage()) && ae.getMessage().endsWith("already in state 'active'.")) {
    			result = ServerResponse.createBySuccess("流程已在激活状态");
    		}
    		else if(StringUtils.isNotBlank(ae.getMessage()) && ae.getMessage().endsWith("already in state 'suspended'.")) {
    			result = ServerResponse.createBySuccess("流程已在挂起状态");
    		}
    		else {
    			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",ae.getMessage(),form,ae);
    			result = ServerResponse.createByError();
    		}
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form,e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	  /**
	   * 获取机构人员列表
	   * legal_aid 法援中心  people_mediation	人民调解 apply_lawyer	申请律师 	apply_appraise	申请鉴定 	apply_notarization	申请公证
	   * isUser 是否查询机构下人员
	   * id 不为空是查询某个机构下的人员
     * @author 王召林
     * @version 2018-5-24 21:12:42
     * @param form
     * @return
     */
	@RequestMapping("/100")
    public ServerResponse getOfficeUserByType(BaseForm<Map> form){
    	ServerResponse result = null;
		List<Map<String, Object>> lawList = Lists.newArrayList();
		String type = "0";
		String isUser = null;
		String id = null;
		String areaId = null;
		String townId = null;
    	try {
    		form.initQueryObj(Map.class);
    		type = String.valueOf(form.getQueryObj().get("type"));
    		isUser = String.valueOf(form.getQueryObj().get("isUser"));
    		id = String.valueOf(form.getQueryObj().get("id"));
    		areaId = String.valueOf(form.getQueryObj().get("areaId"));
    		townId = String.valueOf(form.getQueryObj().get("townId"));
    		if(StringUtils.isBlank(type)) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(),"类型为空");
    		}
    		switch (type) {
			case "legal_aid":
				type="5";
				break;
			case "people_mediation":
				type="10";
				break;
			case "apply_lawyer":
				type="2";
				break;
			case "apply_appraise":
				type="4";
				break;
			case "apply_notarization":
				type="3";
				break;
			default:
				break;
			}
    		if(result==null) {
    			lawList=systemService.getOfficeUserApi(type,isUser, areaId, townId,id);
    			result = ServerResponse.createBySuccess(lawList);
    		}
			log.debug("返回参数：{}",result);
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	/**
	 * 手机端获取我申请的已完成评论
	 * @param procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = "110")
	public ServerResponse<PageVo<ActVo>> getCommentList(BaseForm<Act> form)  {
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		int pageNo=0;
		int pageSize=0;
		try {
			 pageNo=StringUtils.toInteger(bject.getByte("pageNo"));
			 if(pageNo==0){
				 pageNo=1;
			 }
			 pageSize=StringUtils.toInteger(bject.getByte("pageSize"));
				if(pageSize==0){
					pageSize=Integer.valueOf(Global.getConfig("page.pageSize"));
				}
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
		}
		Act act=new Act();
		act.setProcDefKey(bject.getString("procDefKey"));
		String beginDate=bject.getString("beginDate");
		String endDate=bject.getString("endDate");
		String status = bject.getString("status");
		String title = bject.getString("title");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNoneBlank(beginDate)){
			try {
				act.setBeginDate(sdf.parse(beginDate));
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		if(StringUtils.isNoneBlank(endDate)){
			try {
				Calendar c = Calendar.getInstance();  
		        c.setTime(sdf.parse(endDate));  
		        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天 
				act.setEndDate(c.getTime());
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		if(StringUtils.isNoneBlank(status)){
			act.setStatus(status);
		}
		if(StringUtils.isNoneBlank(title)){
			act.setTitle(title);
		}
//		Page<Act> page=actTaskService.historicCreaterUserList(new Page<Act>(pageNo,pageSize),act);
		/*PageVo<ActVo> pageVo=new PageVo<ActVo>(page);
		pageVo.setList(this.change(page.getList()));
		ServerResponse<PageVo<ActVo>> result = ServerResponse.createBySuccess(pageVo);*/
		Page<ActVo> page=actTaskService.ApiGetCommentList(new Page<ActVo>(pageNo,pageSize),act);
		PageVo<ActVo> pageVo=new PageVo<ActVo>(page);
		pageVo.setList(page.getList());
		ServerResponse<PageVo<ActVo>> result = ServerResponse.createBySuccess(pageVo);
		return result;
	}
	
	/**
     * 1.获取法援中心
     * 2.根据法援中心的兼职律师
     * @author 张强
     * @version 
     * @param form
     * @return
     */
	@RequestMapping("/130")
    public ServerResponse getlawAssistanceLawyer(BaseForm<Map> form){
    	ServerResponse result = null;
		List<Map<String, Object>> lawList = Lists.newArrayList();
		String type = "0";
		String id = null;
		String areaId = null;
    	try {
    		form.initQueryObj(Map.class);
    		type = String.valueOf(form.getQueryObj().get("type"));
    		id = String.valueOf(form.getQueryObj().get("id"));
    		areaId = String.valueOf(form.getQueryObj().get("areaId"));
    		if(StringUtils.isBlank(type)) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(),"类型为空");
    		}
    		if(result==null) {
    			lawList=systemService.getOfficeUserApi("5", type, areaId,"", id,"1");
    			//lawList = getOfficeUser("1".equals(type)?"2":"3", id ,areaId);
    			result = ServerResponse.createBySuccess(lawList);
    		}
			log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
}
