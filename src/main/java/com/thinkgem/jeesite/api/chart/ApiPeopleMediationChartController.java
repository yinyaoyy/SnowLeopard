package com.thinkgem.jeesite.api.chart;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkgem.jeesite.api.chart.entity.LegalORMediationVo;
import com.thinkgem.jeesite.api.chart.entity.PeopleMediationVo;
import com.thinkgem.jeesite.api.chart.entity.PeopleMediatorVo;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.cms.entity.ComplaintCount;
import com.thinkgem.jeesite.modules.cms.service.ComplaintService;
import com.thinkgem.jeesite.modules.info.service.PeopleMediationService;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApplyCount;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationApplyService;

/**
 * 人民调解图表接口
 * @author 王鹏
 * @version 2018-6-7 10:59:31
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/560", "/api/200/560"})
public class ApiPeopleMediationChartController {

    private static final Logger log = LoggerFactory.getLogger(ApiPeopleMediationChartController.class);
    
    @Autowired
    private OaPeopleMediationApplyService oaPeopleMediationApplyService;
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private PeopleMediationService peopleMediationService;
    
    /**
     * 大屏统计
	 * 全盟各旗县人民调解案件申请数量占比图
	 * 各旗县人民调解案件数量占比
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse countArea(BaseForm<OaPeopleMediationApplyCount> form){
    	ServerResponse result = null;
    	try {
    		OaPeopleMediationApplyCount opmac = form.initQueryObj(OaPeopleMediationApplyCount.class);
    		Map<String, Object> resultMap = oaPeopleMediationApplyService.countAreaPeopleMediationForBigScreen(opmac);
    		result = ServerResponse.createBySuccess(resultMap);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }

    /**
     * 大屏统计: 全盟人民调解案件申请数量与受理数量走势对比图
     * @return
     */
	@RequestMapping("/20")
    public ServerResponse countAidAcceptCompared(BaseForm<OaPeopleMediationApplyCount> form){
    	ServerResponse result = null;
    	try {
    		OaPeopleMediationApplyCount opmac = form.initQueryObj(OaPeopleMediationApplyCount.class);
    		Map<String, Object> resultList = oaPeopleMediationApplyService.countPeopleMediationAidAcceptCompared(opmac);
    		result = ServerResponse.createBySuccess(resultList);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }

    /**
     * 大屏统计: 全盟人民调解案件数量折线图
     * @return
     */
	@RequestMapping("/30")
    public ServerResponse countPeopleMediationAidAcceptForBigScreen(BaseForm<OaPeopleMediationApplyCount> form){
    	ServerResponse result = null;
    	try {
    		OaPeopleMediationApplyCount opmac = form.initQueryObj(OaPeopleMediationApplyCount.class);
    		Map<String, Object> resultList = oaPeopleMediationApplyService.countPeopleMediationAccept1(opmac);
    		result = ServerResponse.createBySuccess(resultList);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }

    /**
     * 大屏统计: 全盟各旗县人民调解员接收投诉数量与处理投诉数量对比
     * @return
     */
	@RequestMapping("/40")
    public ServerResponse countComplaintCommentComparedForBigScreen(BaseForm<ComplaintCount> form){
    	ServerResponse result = null;
    	try {
    		ComplaintCount cc = form.initQueryObj(ComplaintCount.class);
    		//设定为只查调委会的
    		cc.setType("4");
    		cc.setCountNames(new String[]{"人民调解员接收投诉数量", "人民调解员处理投诉数量"});
    		Map<String, Object> resultList = complaintService.countComplaintCommentCompared(cc);
    		result = ServerResponse.createBySuccess(resultList);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }

    /**
     * 大屏查询:根据条件查询人民调解案件信息
     * @return
     */
	@RequestMapping("/50")
    public ServerResponse searchPeopleMediationForBigScreen(BaseForm<PeopleMediationVo> form){
    	ServerResponse result = null;
    	try {
    		PeopleMediationVo pmv = form.initQueryObj(PeopleMediationVo.class);
    		if(pmv.getBeginDate()==null || pmv.getEndDate()==null || pmv.getPage()==null) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		else {//查询数据并返回
    			PageVo<PeopleMediationVo> resultPage = oaPeopleMediationApplyService.findPageforApi(pmv);
    			result = ServerResponse.createBySuccess(resultPage);
    		}
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }

    /**
     * 接口:大屏查询人民调解员信息
     * @return
     */
	@RequestMapping("/60")
    public ServerResponse findListForBigScreen(BaseForm<PeopleMediatorVo> form){
    	ServerResponse result = null;
    	try {
    		PeopleMediatorVo pmv = form.initQueryObj(PeopleMediatorVo.class);
    		//查询数据并返回
    		PageVo<PeopleMediatorVo> resultPage = peopleMediationService.findListForBigScreen(pmv);
    		result = ServerResponse.createBySuccess(resultPage);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	
	/**
     * 大屏统计:人民调解和法援的数量
     * @return
     */
	@RequestMapping("/70")
    public ServerResponse countLegalAndMediationAcceptForBigScreen(){
		ServerResponse result = null;
    	try {
    		Map<String, Object> resultMap = oaPeopleMediationApplyService.countLeaglAndMediation();
    		result = ServerResponse.createBySuccess(resultMap);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	/**
     * 大屏统计: 人民调解和法援的数量
     * @return
     */
	@RequestMapping("/80")
    public ServerResponse countLegalAndMediationAcceptForBigScreen1(){
		ServerResponse result = null;
    	try {
    		Map<String, Object> resultMap = oaPeopleMediationApplyService.countLeaglAndMediation1();
    		result = ServerResponse.createBySuccess(resultMap);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	
	/**
     * 大屏统计: 查询人民调解和法援的数量
     * @return
     */
	@RequestMapping("/90")
    public ServerResponse countLegalAndMediationByAreaId(BaseForm<PeopleMediatorVo> form){
		ServerResponse result = null;
    	try {
    		PeopleMediatorVo peopleMediatorVo = form.initQueryObj(PeopleMediatorVo.class);
    		
    		Map<String, Object> resultMap = oaPeopleMediationApplyService.countLegalAndMediationByAreaId(peopleMediatorVo);
    		result = ServerResponse.createBySuccess(resultMap);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
}
