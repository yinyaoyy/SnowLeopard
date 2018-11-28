package com.thinkgem.jeesite.api.act;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkgem.jeesite.api.dto.form.ActTaskForm;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.act.ActVo;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.entity.act.OaFastLegal;
import com.thinkgem.jeesite.modules.oa.service.act.OaFastLegalService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 法律服务直通车接口
 * @author keytec
 *
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping(value={"/api/200/630","/api/100/630"})
public class ApiFastLegalController {
	
	private static final Logger log = LoggerFactory.getLogger(ApiFastLegalController.class);
	
	@Autowired
	private OaFastLegalService oaFastLegalService;
	@Autowired
	private ActTaskService actTaskService;
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	
	/**
	 * 提交申请
	 * 
	 * @return
	 */
	@RequestMapping("/10")
	public ServerResponse submit(HttpServletRequest request, BaseForm<OaFastLegal> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaFastLegal.class);
			oaFastLegalService.submit(form.getQueryObj());
			result = ServerResponse.createBySuccess(null);
			log.debug("返回参数：{}", result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("/20")
	public ServerResponse save(HttpServletRequest request, BaseForm<OaFastLegal> form) {
		ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(OaFastLegal.class);
			OaFastLegal oaFastLegal = form.getQueryObj();
			if("true".equals(oaFastLegal.getIsSubmit())) {
				String message = oaFastLegalService.toDo(oaFastLegal);
				if(!"".equals(message)){
					return ServerResponse.createByErrorMessage(message);
				}
			}else{
				oaFastLegalService.save(form.getQueryObj());
			}
			result = ServerResponse.createBySuccess(null);
			log.debug("返回参数：{}", result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	
	/**
	 * 审批
	 * 
	 * @author 张强
	 * @version 
	 * @param form
	 * @return
	 */
	@RequestMapping("/30")
	public ServerResponse toDo(BaseForm<OaFastLegal> form) {
		ServerResponse result = null;
		String checkMsg = "";
		try {
			OaFastLegal oaFastLegal = form.initQueryObj(OaFastLegal.class);
			oaFastLegal.setUpdateBy(UserUtils.getUser());
			oaFastLegal.setUpdateDate(new Date());

			if (StringUtils.isBlank(oaFastLegal.getId())) {
				// 业务主键为空
				return ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ID.getCode(),
						ResponseCode.BUSINESS_ID.getDesc());
			}
			checkMsg = ActTaskForm.checkTaskParam(oaFastLegal.getAct());
			if (StringUtils.isNotBlank(checkMsg)) {
				// 如果校验参数时返回了错误信息，则要返回相应提示且不再继续处理
				return ServerResponse.createByErrorCodeMessage(ResponseCode.ACT_PARAM_ERROR.getCode(), checkMsg);
			} else {
//				String taskDefKey = oaPeopleMediationApply.getAct().getTaskDefKey();
				
				checkMsg = oaFastLegalService.toDo(oaFastLegal);

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
	 * 获取直通车数据
	 * @param form
	 * @return
	 */
	@RequestMapping("/40")
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
			if (act.getProcInsId() != null){
    			act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
    			if(act.getProcIns()==null){
    				act.setHisProcIns(actTaskService.getHisProcIns(act.getProcInsId()));
    			}
    		}
			OaFastLegal oaFastLegal = oaFastLegalService.get(act.getBusinessId());
			ActVo actVo = new ActVo(act);
			actVo.setBusinessData(oaFastLegal);
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
	 * 通过
	 * @param form
	 * @return
	 */
	@RequestMapping("/50")
	public ServerResponse getDataByCode(BaseForm<ActTaskForm> form) {
		ServerResponse result = null;
		try {
			form.initQueryObj(ActTaskForm.class);
			Act act = form.getQueryObj().toAct();
			if (StringUtils.isNotBlank(ActTaskForm.checkTaskParam(act))) {
				// 如果校验参数时返回了错误信息，则要返回相应提示且不再继续处理
				return ServerResponse.createByErrorCodeMessage(ResponseCode.ACT_PARAM_ERROR.getCode(),
						ResponseCode.ACT_PARAM_ERROR.getDesc());
			}
			if (act.getProcInsId() != null){
    			act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
    			if(act.getProcIns()==null){
    				act.setHisProcIns(actTaskService.getHisProcIns(act.getProcInsId()));
    			}
    		}
			OaFastLegal oaFastLegal = oaFastLegalService.get(act.getBusinessId());
			ActVo actVo = new ActVo(act);
			actVo.setBusinessData(oaFastLegal);
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
	 * 校验参数是否正确
	 * 
	 * @author 
	 * @version 
	 * @return
	 */
	public String checkParam(Object oaFastLegal) {
		StringBuffer sb = new StringBuffer();
		try {
			BeanValidators.validateWithException(validator, oaFastLegal);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
			}
		}
		return sb.toString();
	}
	
}
