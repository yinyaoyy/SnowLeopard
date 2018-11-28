package com.thinkgem.jeesite.api.act;


import java.util.Date;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.thinkgem.jeesite.api.dto.form.ActTaskForm;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;

import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.entity.act.OaFastLegal;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidInform;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.service.act.OaFastLegalService;
import com.thinkgem.jeesite.modules.oa.service.act.OaLegalAidInformService;
import com.thinkgem.jeesite.modules.oa.service.act.OaLegalAidService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationApplyService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 法援通知辩护申请API接口
 * 此接口必须要登录
 * @author 如初
 * @version 2018-07-09 14:19:02
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/360", "/api/200/360"})
public class ApilegalAidInformController {

    private static final Logger log = LoggerFactory.getLogger(ApiLegalAidController.class);
    
    @Autowired
    private OaLegalAidInformService oaLegalAidInformService;
    
    @Autowired
    OaFastLegalService oaFastLegalService;
    
    @Autowired
    OaLegalAidService oaLegalAidService;
    
    @Autowired
    OaPeopleMediationApplyService oaPeopleMediationApplyService;
    @Autowired
	private ActTaskService actTaskService;
    
    @Autowired
	private SystemService systemService;
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	@RequestMapping("/10")
    public ServerResponse LegalAidInform(HttpServletRequest request, BaseForm<OaLegalAidInform> form){
		ServerResponse result = null;
    	try {
    		form.setTag(request.getHeader("tag"));
    		OaLegalAidInform oaLegalAidInform = form.initQueryObj(OaLegalAidInform.class);
    		if(!"0".equals(oaLegalAidInform.getIsSubmit())) {
    			//如果不为0就是正式提交申请，正式提交则进行数据校验
    			String checkResult = checkParam(oaLegalAidInform);
    			
    			if(StringUtils.isNotBlank(checkResult)) {
    				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
    			}
    			else {//通过校验
    				String draftId = oaLegalAidInform.getId();//草稿表主键
    				oaLegalAidInform.setId(null);//主键置空
    				oaLegalAidInform.setLegalAidType("2");//法援通知辩护
    				oaLegalAidInform.setSource(form.getTag());//指定接口来源
    				oaLegalAidInformService.save(form.getQueryObj());
    				//删除草稿内容
    				if(StringUtils.isNotBlank(draftId)) {
    					oaLegalAidInformService.deleteDraft(new String[]{draftId});
    				}
    				result = ServerResponse.createBySuccess(null);
    			}
    		}
    		else {//保存到草稿箱
    			oaLegalAidInform.setLegalAidType("2");
    			oaLegalAidInformService.saveDraft(oaLegalAidInform);
    			result = ServerResponse.createBySuccess(null);
    		}
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	
	/**
	 * 校验参数是否正确
	 * @author 王鹏
	 * @version 2018-05-14 17:09:13
	 * @return
	 */
	public String checkParam(OaLegalAidInform oaLegalAidInform) {
		StringBuffer sb = new StringBuffer();
		try{
			BeanValidators.validateWithException(validator, oaLegalAidInform);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
			}
		}
		return sb.toString();
	}

    /**
     * 办理法援通知辩护
     * @author 如初
     * @version 2018-07-09 14:33:58
     * @param form
     * @return
     */
	@RequestMapping("/20")
    public ServerResponse toDo(BaseForm<OaLegalAidInform> form){
    	ServerResponse result = null;
    	String checkMsg = ""; 
    	try {
    		OaLegalAidInform oaLegalAidInform = form.initQueryObj(OaLegalAidInform.class);
    		oaLegalAidInform.setUpdateBy(UserUtils.getUser());
    		oaLegalAidInform.setUpdateDate(new Date());
    		
    		if(StringUtils.isBlank(oaLegalAidInform.getId())) {
    			//业务主键为空
    			return ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ID.getCode(),ResponseCode.BUSINESS_ID.getDesc());
    		}
			checkMsg = ActTaskForm.checkTaskParam(oaLegalAidInform.getAct());
			if(StringUtils.isNotBlank(checkMsg)) {
    			//如果校验参数时返回了错误信息，则要返回相应提示且不再继续处理
				return ServerResponse.createByErrorCodeMessage(ResponseCode.ACT_PARAM_ERROR.getCode(), checkMsg);
    		} else {
    			String taskDefKey = oaLegalAidInform.getAct().getTaskDefKey();
    			
    				if("defense_update".equals(taskDefKey)){
    					checkMsg = checkParam(oaLegalAidInform);
    					if(StringUtils.isNotBlank(checkMsg)) {
    						return ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkMsg);
    					}
    					oaLegalAidInformService.save(oaLegalAidInform);//如果是申请人的操作则调用保存方法
    				}
    			else {//工作人员处理的调用toDo方法
    				checkMsg = oaLegalAidInformService.toDo(oaLegalAidInform);
    			}
    			if(StringUtils.isNotBlank(checkMsg)) {
    				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkMsg);
    			}else {
    				result = ServerResponse.createBySuccess(null);
    			}
    			log.debug("返回参数：{}",result);
    		}
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	

    /**
     * 获取草稿列表
     * @return
     */
	@RequestMapping("/30")
    public ServerResponse getDrafts(HttpServletRequest request, BaseForm<OaLegalAid> form){
    	ServerResponse result = null;
    	try {
    		JSONObject jo = JSON.parseObject(form.getQuery());
    		Integer pageNo = null;
    		Integer pageSize = null;
    		try {
    			pageNo = Integer.parseInt(jo.getString("pageNo"));
    			pageSize = Integer.parseInt(jo.getString("pageSize"));
    		}catch(Exception e) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		if(result == null) {
    			String caseTitle = jo.getString("caseTitle");
    			PageVo<OaLegalAidInform> page = oaLegalAidInformService.findDraftPageforApi(pageNo, pageSize, caseTitle,"2");
    			result = ServerResponse.createBySuccess(page);
    		}
    		log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	
    /**
     * 删除申请草稿(支持批量)
     * @return
     */
	@RequestMapping("/40")
    public ServerResponse deleteDraft(HttpServletRequest request, BaseForm<OaLegalAidInform> form){
    	ServerResponse result = null;
    	try {
    		OaLegalAidInform oaLegalAidInform = form.initQueryObj(OaLegalAidInform.class);
    		if(StringUtils.isBlank(oaLegalAidInform.getId())) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		else {//通过校验
    			
    			oaLegalAidInformService.deleteDraft(oaLegalAidInform.getId().split(","));
    		}
    		result = ServerResponse.createBySuccess(null);
    		log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	
/*	*//**
	 * 获取法援通知点信息（手机端使用）
	 * @param form
	 * @return
	 */
	@RequestMapping(value={"/50"})
	public ServerResponse<List<Act>> histoicCompleteNote(BaseForm<Act> form){
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		List<Act> histoicFlowList = actTaskService.histoicCompleteNote(bject.getString("procInsId"));
		ServerResponse<List<Act>> result = ServerResponse.createBySuccess(histoicFlowList);
		return result;
	}
	
	/*	*//**
	 * 获取法援机构
	 * @param form
	 * @return
	 */
	@RequestMapping(value={"/60"})
	public ServerResponse getOffice(BaseForm<Map> form) {
		ServerResponse result = null;
		try {
    		form.initQueryObj(Map.class);
    		String type= String.valueOf(form.getQueryObj().get("type"));
    		String isUser = String.valueOf(form.getQueryObj().get("isUser"));
    		String areaId  = String.valueOf(form.getQueryObj().get("areaId"));
    		String officeId = String.valueOf(form.getQueryObj().get("officeId"));
    			areaId="";
    		
    		if(StringUtils.isBlank(type)) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(),"类型为空");
    		}
    		if(result==null) {
    			List<Map<String, Object>>  lis =systemService.getOfficeUser(type, isUser, areaId,"",officeId);
    			result = ServerResponse.createBySuccess(lis);
    		}
			log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	
	/*	*//**
			 * 结束任务 此接口为公共就扣
			 * 
			 * @param form
			 * @return
			 */
	@RequestMapping(value = { "/70" })
	public ServerResponse endProcess(BaseForm<T> form) {
		String err = "";
		ServerResponse result = null;
		JSONObject bject = (JSONObject) JSONObject.parse(form.getQuery());
		JSONObject act = (JSONObject) bject.get("act");
		String procDefKey = act.getString("procDefKey");
		String flag = act.getString("flag");
		String id = (String) bject.get("id");
		String comment = act.getString("comment");
		String procInsId = act.getString("procInsId");
		String remarks = (String) bject.get("remarks");
		String taskId = act.getString("taskId");
		String taskDefKey = act.getString("taskDefKey");
		String taskName = act.getString("taskName");
		String procDefId = act.getString("procDefId");
		try {
			if ("notification_defense".equals(procDefKey)) {
				OaLegalAidInform oaLegalAidInform = new OaLegalAidInform();
				oaLegalAidInform.setProcInsId(procInsId);
				oaLegalAidInform.setId(id);
				oaLegalAidInform.setRemarks(remarks);
				Act act1 = new Act();
				act1.setProcInsId(procInsId);
				act1.setComment(comment);
				act1.setTaskId(taskId);
				act1.setProcDefKey(procDefKey);
				act1.setTaskDefKey(taskDefKey);
				act1.setTaskName(taskName);
				act1.setProcDefId(procDefId);
				oaLegalAidInform.setAct(act1);
				// 申请人发起
				if (!"end".equals(flag)) {
					err = oaLegalAidInformService.applyEndProcess(oaLegalAidInform);
				} else {
					// 当前处理人
					err = oaLegalAidInformService.endProcess(oaLegalAidInform);
				}
				if (StringUtils.isNotBlank(err)) {
					result = ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(), err);
				} else {
					result = ServerResponse.createBySuccess(null);
				}
				log.debug("返回参数：{}", result);
			} else if ("fast_legal".equals(procDefKey)) {
				OaFastLegal oaFastLegal = new OaFastLegal();
				oaFastLegal.setId(id);
				oaFastLegal.setRemarks(remarks);
				oaFastLegal.setProcInsId(procInsId);
				Act act1 = new Act();
				act1.setProcInsId(procInsId);
				act1.setComment(comment);
				act1.setTaskId(taskId);
				act1.setProcDefKey(procDefKey);
				act1.setTaskDefKey(taskDefKey);
				act1.setTaskName(taskName);
				act1.setProcDefId(procDefId);
				oaFastLegal.setAct(act1);
				if (!"end".equals(flag)) {
					oaFastLegalService.applyEndProcess(oaFastLegal);
				} else {
					err = oaFastLegalService.endProcess(oaFastLegal);
					result = ServerResponse.createBySuccess();
				}
				if (StringUtils.isNotBlank(err)) {
					result = ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(), err);
				} else {
					result = ServerResponse.createBySuccess(null);
				}
				log.debug("返回参数：{}", result);

			} else if ("people_mediation".equals(procDefKey)) {
				OaPeopleMediationApply oaPeopleMediationApply = new OaPeopleMediationApply();
				oaPeopleMediationApply.setId(id);
				oaPeopleMediationApply.setRemarks(remarks);
				oaPeopleMediationApply.setProcInsId(procInsId);
				Act act1 = new Act();
				act1.setProcInsId(procInsId);
				act1.setComment(comment);
				act1.setTaskId(taskId);
				act1.setProcDefKey(procDefKey);
				act1.setTaskDefKey(taskDefKey);
				act1.setTaskName(taskName);
				act1.setProcDefId(procDefId);
				oaPeopleMediationApply.setAct(act1);
				if (!"end".equals(flag)) {
					oaPeopleMediationApplyService.applyEndProcess(oaPeopleMediationApply);
				} else {
					err = oaPeopleMediationApplyService.endProcess(oaPeopleMediationApply);
				}
				if (StringUtils.isNotBlank(err)) {
					result = ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(), err);
				} else {
					result = ServerResponse.createBySuccess(null);
				}
				log.debug("返回参数：{}", result);

			} else if ("legal_aid".equals(procDefKey)) {

				OaLegalAid oaLegalAid = new OaLegalAid();
				oaLegalAid.setId(id);
				oaLegalAid.setRemarks(remarks);
				oaLegalAid.setProcInsId(procInsId);
				Act act1 = new Act();
				act1.setProcInsId(procInsId);
				act1.setComment(comment);
				act1.setTaskId(taskId);
				act1.setProcDefKey(procDefKey);
				act1.setTaskDefKey(taskDefKey);
				act1.setTaskName(taskName);
				act1.setProcDefId(procDefId);
				oaLegalAid.setAct(act1);
				if (!"end".equals(flag)) {
					oaLegalAidService.applyEndProcess(oaLegalAid);
					result = ServerResponse.createBySuccess();
				} else {
					err=oaLegalAidService.endProcess(oaLegalAid);

				}
				if (StringUtils.isNotBlank(err)) {
					result = ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(), err);
				} else {
					result = ServerResponse.createBySuccess(null);
				}
				log.debug("返回参数：{}", result);

			}
		} catch (BusinessException e) {
			// TODO: handle exception
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}", e.getMessage(), form.getQuery(), e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}

		return result;
	}
}
