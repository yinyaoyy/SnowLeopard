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
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.service.act.OaLegalAidService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 法律援助申请API接口
 * 此接口必须要登录
 * @author 王鹏
 * @version 2018-04-18 09:40:02
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/520", "/api/200/520"})
public class ApiLegalAidController {

    private static final Logger log = LoggerFactory.getLogger(ApiLegalAidController.class);
    
    @Autowired
    private OaLegalAidService oaLegalAidService;
    @Autowired
	private ActTaskService actTaskService;
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
    
    /**
     * 申请法律援助
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse legalAid(HttpServletRequest request, BaseForm<OaLegalAid> form){
    	ServerResponse result = null;
    	try {
    		form.setTag(request.getHeader("tag"));
    		OaLegalAid oaLegalAid = form.initQueryObj(OaLegalAid.class);
    		if(!"0".equals(oaLegalAid.getIsSubmit())) {
    			//如果不为0就是正式提交申请，正式提交则进行数据校验
    			String checkResult = checkParam(oaLegalAid);
    			if(StringUtils.isNotBlank(checkResult)) {
    				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
    			}
    			else {//通过校验
    				String draftId = oaLegalAid.getId();//草稿表主键
    				oaLegalAid.setId(null);//主键置空
    				oaLegalAid.setLegalAidType("1");//此接口默认只为“申请”法律援助
    				oaLegalAid.setSource(form.getTag());//指定接口来源
    				oaLegalAidService.save(form.getQueryObj());
    				//删除草稿内容
    				if(StringUtils.isNotBlank(draftId)) {
    					oaLegalAidService.deleteDraft(new String[]{draftId});
    				}
    				result = ServerResponse.createBySuccess(null);
    			}
    		}
    		else {//保存到草稿箱
    			oaLegalAid.setLegalAidType("1");
    			oaLegalAidService.saveDraft(oaLegalAid);
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
	public String checkParam(OaLegalAid oaLegalAid) {
		StringBuffer sb = new StringBuffer();
		try{
			BeanValidators.validateWithException(validator, oaLegalAid);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
			}
		}
		return sb.toString();
	}

    /**
     * 办理法律援助
     * @author 王鹏
     * @version 2018-05-24 10:56:58
     * @param form
     * @return
     */
	@RequestMapping("/20")
    public ServerResponse toDo(BaseForm<OaLegalAid> form){
    	ServerResponse result = null;
    	String checkMsg = ""; 
    	try {
    		OaLegalAid oaLegalAid = form.initQueryObj(OaLegalAid.class);
    		oaLegalAid.setUpdateBy(UserUtils.getUser());
    		oaLegalAid.setUpdateDate(new Date());
    		
    		if(StringUtils.isBlank(oaLegalAid.getId())) {
    			//业务主键为空
    			return ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ID.getCode(),ResponseCode.BUSINESS_ID.getDesc());
    		}
			checkMsg = ActTaskForm.checkTaskParam(oaLegalAid.getAct());
			if(StringUtils.isNotBlank(checkMsg)) {
    			//如果校验参数时返回了错误信息，则要返回相应提示且不再继续处理
				return ServerResponse.createByErrorCodeMessage(ResponseCode.ACT_PARAM_ERROR.getCode(), checkMsg);
    		} else {
    			String taskDefKey = oaLegalAid.getAct().getTaskDefKey();
    			if("aid_apply_zhiding".equals(taskDefKey) || "aid_update".equals(taskDefKey)) {
    				if("aid_update".equals(taskDefKey)){
    					checkMsg = checkParam(oaLegalAid);
    					if(StringUtils.isNotBlank(checkMsg)) {
    						return ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkMsg);
    					}
    				}
    				oaLegalAidService.save(oaLegalAid);//如果是申请人的操作则调用保存方法
    			}
    			else {//工作人员处理的调用toDo方法
    				checkMsg = oaLegalAidService.toDo(oaLegalAid);
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
    			PageVo<OaLegalAid> page = oaLegalAidService.findDraftPageforApi(pageNo, pageSize, caseTitle,"1");
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
    public ServerResponse deleteDraft(HttpServletRequest request, BaseForm<OaLegalAid> form){
    	ServerResponse result = null;
    	try {
    		OaLegalAid oaLegalAid = form.initQueryObj(OaLegalAid.class);
    		if(StringUtils.isBlank(oaLegalAid.getId())) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		else {//通过校验
    			//删除草稿内容
    			oaLegalAidService.deleteDraft(oaLegalAid.getId().split(","));
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
	 * 获取法律援助流程节点信息（手机端使用）
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
	
}
