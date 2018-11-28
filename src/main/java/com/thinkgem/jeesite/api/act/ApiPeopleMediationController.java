package com.thinkgem.jeesite.api.act;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.ActTaskBusiEnum;
import com.thinkgem.jeesite.api.dto.form.ActTaskForm;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.act.ActVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.entity.OaDossier;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidInform;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAcceptRegister;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAgreement;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationExamine;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationRecord;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationVisit;
import com.thinkgem.jeesite.modules.oa.service.OaDossierService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationAcceptRegisterService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationAgreementService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationApplyService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationExamineService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationRecordService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationVisitService;
import com.thinkgem.jeesite.modules.sys.entity.NodeManager;
import com.thinkgem.jeesite.modules.sys.service.NodeManagerService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 人民调解申请API接口 此接口必须要登录
 * 
 * @author 张强
 * @version
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api")
public class ApiPeopleMediationController {

	private static final Logger log = LoggerFactory.getLogger(ApiPeopleMediationController.class);

	@Autowired
	private OaPeopleMediationApplyService oaPeopleMediationApplyService;
	@Autowired
	private ActTaskService actTaskService;

	private CrudService crudService;

	@Autowired
	private OaPeopleMediationAcceptRegisterService oaPeopleMediationAcceptRegisterService;

	@Autowired
	private OaPeopleMediationExamineService oaPeopleMediationExamineService;
	@Autowired
	private OaPeopleMediationRecordService oaPeopleMediationRecordService;
	@Autowired
	private OaPeopleMediationAgreementService oaPeopleMediationAgreementService;
	@Autowired
	private OaPeopleMediationVisitService oaPeopleMediationVisitService;
	@Autowired
	private OaDossierService oaDossierService;
	@Autowired
	private NodeManagerService nodeManagerService;
	@Autowired
	private SystemService systemService;
	 
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 保存人民调解申请表
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/10","/100/540/10"})
	public ServerResponse saveApply(HttpServletRequest request, BaseForm<OaPeopleMediationApply> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationApply.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationApplyService.save(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 保存人民调解登记表
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/11","/100/540/11"})
	public ServerResponse saveAccept(HttpServletRequest request, BaseForm<OaPeopleMediationAcceptRegister> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationAcceptRegister.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationAcceptRegisterService.save(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 保存人民调解调查表
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/12","/100/540/12"})
	public ServerResponse saveExamine(HttpServletRequest request, BaseForm<OaPeopleMediationExamine> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationExamine.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationExamineService.save(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 保存人民调解调解记录表
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/13","/100/540/13"})
	public ServerResponse saveRecord(HttpServletRequest request, BaseForm<OaPeopleMediationRecord> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationRecord.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationRecordService.save(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 保存人民调解协议表
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/14","/100/540/14"})
	public ServerResponse saveAgreement(HttpServletRequest request, BaseForm<OaPeopleMediationAgreement> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationAgreement.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationAgreementService.save(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 保存人民调解回访表
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/15","/100/540/15"})
	public ServerResponse saveVisit(HttpServletRequest request, BaseForm<OaPeopleMediationVisit> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationVisit.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationVisitService.save(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 保存人民调解卷宗
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/16","/100/540/16"})
	public ServerResponse saveDossier(HttpServletRequest request, BaseForm<OaDossier> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaDossier.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaDossierService.save(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	/**
	 * 提交人民调解申请
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/20","/100/540/20"})
	public ServerResponse submitApply(HttpServletRequest request, BaseForm<OaPeopleMediationApply> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationApply.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				List<Map<String, Object>> list = systemService.getOfficeUser("10", "0", form.getQueryObj().getCaseCounty().getId(), form.getQueryObj().getCaseTown().getId(), "");
				if(list==null || list.size()==0){
					return ServerResponse.createByErrorMessage("该地区没有对应的人民调解委员会!!!");
				}
				result = oaPeopleMediationApplyService.submit(form.getQueryObj());
				if (result != null) {
					return result;
				} else {
					result = ServerResponse.createBySuccess(null);
				}
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	/**
	 * 提交人民调解受理登记
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/21","/100/540/21"})
	public ServerResponse submitAccept(HttpServletRequest request, BaseForm<OaPeopleMediationAcceptRegister> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationAcceptRegister.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationAcceptRegisterService.submit(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 提交人民调解调查记录
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/22","/100/540/22"})
	public ServerResponse submitExamine(HttpServletRequest request, BaseForm<OaPeopleMediationExamine> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationExamine.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationExamineService.submit(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 提交人民调解记录
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/23","/100/540/23"})
	public ServerResponse submitRecord(HttpServletRequest request, BaseForm<OaPeopleMediationRecord> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationRecord.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationRecordService.submit(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	/**
	 * 提交人民调解协议
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/24","/100/540/24"})
	public ServerResponse submitAgreement(HttpServletRequest request, BaseForm<OaPeopleMediationAgreement> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationAgreement.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationAgreementService.submit(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	/**
	 * 提交人民调解回访
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/25","/100/540/25"})
	public ServerResponse submitVisit(HttpServletRequest request, BaseForm<OaPeopleMediationVisit> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaPeopleMediationVisit.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaPeopleMediationVisitService.submit(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 提交人民调解卷宗
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/26","/100/540/26"})
	public ServerResponse submitDossier(HttpServletRequest request, BaseForm<OaDossier> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaDossier.class);
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				oaDossierService.submit(form.getQueryObj());
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 校验参数是否正确
	 * 
	 * @author 王鹏
	 * @version 2018-05-14 17:09:13
	 * @return
	 */
	public String checkParam(Object oaPeopleMediationApply) {
		StringBuffer sb = new StringBuffer();
		try {
			BeanValidators.validateWithException(validator, oaPeopleMediationApply);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 办理人民调解申请的审批
	 * 
	 * @author 张强
	 * @version 2018-05-24 10:56:58
	 * @param form
	 * @return
	 */
	@RequestMapping(value={"/200/540/30","/100/540/30"})
	public ServerResponse toDo(BaseForm<OaPeopleMediationApply> form) {
		ServerResponse result = null;
		String checkMsg = "";
		try {
			OaPeopleMediationApply oaPeopleMediationApply = form.initQueryObj(OaPeopleMediationApply.class);
			oaPeopleMediationApply.setUpdateBy(UserUtils.getUser());
			oaPeopleMediationApply.setUpdateDate(new Date());

			if (StringUtils.isBlank(oaPeopleMediationApply.getId())) {
				// 业务主键为空
				return ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ID.getCode(),
						ResponseCode.BUSINESS_ID.getDesc());
			}
			checkMsg = ActTaskForm.checkTaskParam(oaPeopleMediationApply.getAct());
			if (StringUtils.isNotBlank(checkMsg)) {
				// 如果校验参数时返回了错误信息，则要返回相应提示且不再继续处理
				return ServerResponse.createByErrorCodeMessage(ResponseCode.ACT_PARAM_ERROR.getCode(), checkMsg);
			} else {
//				String taskDefKey = oaPeopleMediationApply.getAct().getTaskDefKey();
				
				checkMsg = oaPeopleMediationApplyService.toDo(oaPeopleMediationApply);

				if (StringUtils.isNotBlank(checkMsg)) {
					result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(),
							checkMsg);
				} else {
					result = ServerResponse.createBySuccess(null);
				}
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

	/**
	 * 获取人民调解表单信息
	 * 
	 * @author 张强
	 * @version
	 * @param form
	 * @return
	 */
	@RequestMapping(value={"/200/540/40","/100/540/40"})
	public ServerResponse getData(BaseForm<ActTaskForm> form) {
		ServerResponse result = null;
		try {
			form.initQueryObj(ActTaskForm.class);
			Act act = form.getQueryObj().toAct();
			if (StringUtils.isNotBlank(ActTaskForm.checkTaskParam(act))) {
				// 如果校验参数时返回了错误信息，则要返回相应提示且不再继续处理
				return ServerResponse.createByErrorCodeMessage(ResponseCode.ACT_PARAM_ERROR.getCode(),
						ResponseCode.ACT_PARAM_ERROR.getDesc());
			}
			// 获取流程XML上的表单KEY
//			String formKey = actTaskService.getFormKey(act.getProcDefId(), act.getTaskDefKey());
			// 获取流程xml上的备注信息，备注信息表示的是该节点对应的数据表名称
			/*String tableName = actTaskService.getDocumentByProDefId(act.getProcInsId(), act.getTaskDefKey());

			// 获取流程实例对象
			 if (act.getProcInsId() != null) {
			 	act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
			 	if (!"".equals(tableName)) {
			 		actTaskService.getDataId(act, actTaskService.getProcIns(act.getProcInsId()), tableName);
			 	}
			 	if (act.getProcIns() == null) {
			 		act.setHisProcIns(actTaskService.getHisProcIns(act.getProcInsId()));

			 		String proInsId = actTaskService.getHisProcIns(act.getProcInsId()).getId();
			 		tableName = actTaskService.getDocumentByProDefId(proInsId, act.getTaskDefKey());
			 		if (!"".equals(tableName)) {
			 			actTaskService.getDataId(act, actTaskService.getHisProcIns(act.getProcInsId()), tableName);
			 		}
			 	}
			 }*/
			if (act.getProcInsId() != null){
    			act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
    			if(act.getProcIns()==null){
    				act.setHisProcIns(actTaskService.getHisProcIns(act.getProcInsId()));
    			}
    		}
			// 获取业务服务对象名称
			String serviceName = ActTaskBusiEnum.getBusiByKey(act.getTaskDefKey());
			crudService = SpringContextHolder.getBean(serviceName);
			ActVo actVo = new ActVo(act);
			actVo.setBusinessData(crudService.get(act.getBusinessId()));
			result = ServerResponse.createBySuccess(actVo);
			log.debug("返回参数：{}", result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	
	/**
	 * 获取流程节点信息
	 * 
	 * @author 张强
	 * @version
	 * @param form
	 * @return
	 */
	@RequestMapping(value={"/200/540/50","/100/540/50"})
	public ServerResponse<List<Act>> histoicFlow(BaseForm<OaPeopleMediationApply> form){
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		List<Act> histoicFlowList = actTaskService.histoicList(bject.getString("procInsId"));
		ServerResponse<List<Act>> result = ServerResponse.createBySuccess(histoicFlowList);
		/*try {
			System.out.println(JsonMapper.getInstance().toJson(result));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
		return result;
	}
	
	/**
	 * 获取流程节点信息
	 * 
	 * @author 张强
	 * @version
	 * @param form
	 * @return
	 */
	@RequestMapping(value={"/200/540/51","/100/540/51"})
	public ServerResponse<List<Act>> histoicFlowCompleteNote(BaseForm<OaPeopleMediationApply> form){
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		List<Act> histoicFlowList = actTaskService.histoicListCompleteNote(bject.getString("procInsId"));
		ServerResponse<List<Act>> result = ServerResponse.createBySuccess(histoicFlowList);
		/*try {
			System.out.println(JsonMapper.getInstance().toJson(result));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
		return result;
	}
	
	/**
	 * 获取人民调解的草稿
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/60","/100/540/60"})
	public ServerResponse getDrafts(HttpServletRequest request, BaseForm<OaPeopleMediationApply> form){
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
    			PageVo<OaPeopleMediationApply> page = oaPeopleMediationApplyService.findDraftPageforApi(pageNo, pageSize, caseTitle);
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
	@RequestMapping(value={"/200/540/70","/100/540/70"})
    public ServerResponse deleteDraft(HttpServletRequest request, BaseForm<OaPeopleMediationApply> form){
    	ServerResponse result = null;
    	try {
    		OaPeopleMediationApply oaPeopleMediationApply = form.initQueryObj(OaPeopleMediationApply.class);
    		if(StringUtils.isBlank(oaPeopleMediationApply.getId())) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		else {//通过校验
    			//删除草稿内容
    			oaPeopleMediationApplyService.deleteDraft(oaPeopleMediationApply.getId().split(","));
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
	
	/**
	 * 获取人民调解和法律援助的草稿文件(临时使用)
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/80","/100/540/80"})
	public ServerResponse getDraft(HttpServletRequest request, BaseForm<Act> form){
		ServerResponse result = null;
    	try {
    		JSONObject jo = JSON.parseObject(form.getQuery());
    		Integer pageNo = null;
    		Integer pageSize = null;
    		String beginDate = "";
    		String endDate = "";
    		String caseTitle = "";
    		try {
    			pageNo = Integer.parseInt(jo.getString("pageNo"));
    			pageSize = Integer.parseInt(jo.getString("pageSize"));
    			beginDate=jo.getString("beginDate");
    			endDate=jo.getString("endDate");
    			caseTitle = jo.getString("caseTitle");
    		}catch(Exception e) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		if(result == null) {
    			PageVo<Act> page = oaPeopleMediationApplyService.findDraft(pageNo,pageSize,beginDate,endDate,caseTitle);
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
	 * 获取人民调解和法律援助的草稿文件(临时使用)
	 * 
	 * @return
	 */
	@RequestMapping(value={"/200/540/90","/100/540/90"})
	public ServerResponse getDraftById(HttpServletRequest request, BaseForm<Act> form){
		ServerResponse result = null;
    	try {
    		JSONObject jo = JSON.parseObject(form.getQuery());
    		String id = "";
    		String procDefKey = "";
    		try {
    			id = jo.getString("id");
    			procDefKey = jo.getString("procDefKey");
    		}catch(Exception e) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		if(result == null) {
    			
    			if("mediation".equals(procDefKey)){
    				PageVo<OaPeopleMediationApply> page= oaPeopleMediationApplyService.findDraftMediationById(id);
    				result = ServerResponse.createBySuccess(page);
    			}else if("legal".equals(procDefKey)){
    				PageVo<OaLegalAid> page = oaPeopleMediationApplyService.findDraftLegalById(id);
    				result = ServerResponse.createBySuccess(page);
    			}else if("defense".equals(procDefKey)){
    				PageVo<OaLegalAidInform> page = oaPeopleMediationApplyService.findDraftdefenseById(id);
    				result = ServerResponse.createBySuccess(page);
    			}
    			
    			if(result ==null){
    				result = ServerResponse.createBySuccess(new PageVo() );
    			}
    	
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
	 * 获取流程节点信息
	 * 
	 * @author 张强
	 * @version
	 * @param form
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/200/540/100","/100/540/100"})
	public ServerResponse<Map> getFlowList(BaseForm<OaPeopleMediationApply> form){
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		String  procDefKey=bject.getString("procDefKey");
		String  version=bject.getString("version");
		String procInsId=bject.getString("procInsId");
		List<Act> histoicFlowList = Lists.newArrayList();
		List<NodeManager> nodeList= Lists.newArrayList();
		if(StringUtils.isNoneBlank(procInsId)){
			histoicFlowList = actTaskService.histoicList(bject.getString("procInsId"));	
		}
		if(StringUtils.isNoneBlank(procDefKey)){
			NodeManager nodeManager=new NodeManager();
	    	nodeManager.setDelFlag("0");
	    	nodeManager.setProcDefKey(procDefKey);
	    	nodeManager.setVersion(version);
	    	if(StringUtils.isBlank(version)){
	    		nodeManager.setVersion(nodeManagerService.findNewVersion(nodeManager));
	    	}
	    	nodeList=nodeManagerService.findList(nodeManager);
		}
		Map map=new HashMap();
		map.put("histoicFlowList", histoicFlowList);
		map.put("nodeList", nodeList);
		return ServerResponse.createBySuccess(map);
	}
}
