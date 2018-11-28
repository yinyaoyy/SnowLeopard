package com.thinkgem.jeesite.api.chart;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thinkgem.jeesite.api.chart.entity.LegalAidVo;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.cms.entity.ComplaintCount;
import com.thinkgem.jeesite.modules.cms.service.ComplaintService;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidCount;
import com.thinkgem.jeesite.modules.oa.service.act.OaLegalAidService;

/**
 * 法援图表接口
 * @author 王鹏
 * @version 2018-6-7 10:59:31
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/550", "/api/200/550"})
public class ApiLegalAidChartController {

    private static final Logger log = LoggerFactory.getLogger(ApiLegalAidChartController.class);
    
    @Autowired
    private OaLegalAidService oaLegalAidService;
    @Autowired
    private ComplaintService complaintService;
    
    /**
     * 大屏统计: 按区域统计时间段内法援申请数量
     * 全盟法律援助案件申请数量占比图、各旗县法律援助案件占比图
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse countArea(BaseForm<OaLegalAidCount> form){
    	ServerResponse result = null;
    	try {
    		OaLegalAidCount oac = form.initQueryObj(OaLegalAidCount.class);
    		Map<String, Object> resultMap = oaLegalAidService.countAreaByYearForBigScreen(oac);
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
     * 大屏统计: 按年度区域统计时间段内法援申请/受理数量对比
     * 全盟法律援助案件申请数量与受理数量走势对比图
     * @return
     */
	@RequestMapping("/20")
    public ServerResponse countYearCompared(BaseForm<OaLegalAidCount> form){
    	ServerResponse result = null;
    	try {
    		OaLegalAidCount oac = form.initQueryObj(OaLegalAidCount.class);
    		Map<String, Object> resultList = oaLegalAidService.countYearByAreaComparedForBigScreen(oac);
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
     * 大屏统计: 全盟法律援助案件数量折线图
     * @return
     */
	@RequestMapping("/30")
    public ServerResponse countLegalAidAcceptForBigScreen(BaseForm<OaLegalAidCount> form){
    	ServerResponse result = null;
    	try {
    		OaLegalAidCount oac = form.initQueryObj(OaLegalAidCount.class);
    		Map<String, Object> resultList = oaLegalAidService.countLegalAidAcceptForBigScreen1(oac);
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
     * 大屏统计: 全盟各旗县法律援助中心接收投诉数量与处理投诉数量对比图
     * @return
     */
	@RequestMapping("/40")
    public ServerResponse countComplaintCommentComparedForBigScreen(BaseForm<ComplaintCount> form){
    	ServerResponse result = null;
    	try {
    		ComplaintCount cc = form.initQueryObj(ComplaintCount.class);
    		//设定为只查法援中心的
    		cc.setType("5");
    		cc.setCountNames(new String[]{"法律援助中心接收投诉数量", "法律援助中心受理投诉数量"});
    		Map<String, Object> resultList = complaintService.countComplaintCommentCompared1(cc);
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
     * 大屏查询:根据条件查询法援申请信息
     * @return
     */
	@RequestMapping("/50")
    public ServerResponse searchLegalAidForBigScreen(BaseForm<LegalAidVo> form){
    	ServerResponse result = null;
    	try {
    		LegalAidVo lav = form.initQueryObj(LegalAidVo.class);
    		if(lav.getBeginDate()==null || lav.getEndDate()==null || lav.getPage()==null) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		else {//查询数据并返回
    			PageVo<LegalAidVo> resultPage = oaLegalAidService.findPageforApi(lav);
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
     * 大屏统计(折线图): 各旗县律师收到投诉数量
     * @return
     */
	@RequestMapping("/60")
    public ServerResponse countComplaintLawOfficeForBigScreen(BaseForm<ComplaintCount> form){
    	ServerResponse result = null;
    	try {
    		ComplaintCount cc = form.initQueryObj(ComplaintCount.class);
    		cc.setType("LawOffice");//限定只查律师事务所的
    		cc.setIsComment("1");//限定只查受理的
    		Map<String, Object> resultList = complaintService.countComplaintLawOffice(cc);
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
}
