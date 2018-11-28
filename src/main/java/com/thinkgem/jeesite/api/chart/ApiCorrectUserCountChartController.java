package com.thinkgem.jeesite.api.chart;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.chart.entity.CorrectUserAnalysisVo;
import com.thinkgem.jeesite.api.chart.services.CorrectUserCountService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.info.entity.CorrectUserAnalysis;
import com.thinkgem.jeesite.modules.info.service.CorrectUserAnalysisService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 社区矫正人员统计接口
 * @author 王鹏
 * @version 2018年7月16日11:48:43
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/680", "/api/200/680"})
public class ApiCorrectUserCountChartController {

    private static final Logger logger = LoggerFactory.getLogger(ApiCorrectUserCountChartController.class);
    
    @Autowired
    private CorrectUserCountService correctUserCountService;
    @Autowired
	private CorrectUserAnalysisService correctUserAnalysisService;
	
    /**
     * 社区矫正人员统计接口
     * 双柱状图:矫正中、已解矫(所有非矫正中的)
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse countCorrectUserByDate(){
    	ServerResponse result = null;
    	try {
    		Map<String, Object> map = correctUserCountService.countCorrectUser();
    		result = ServerResponse.createBySuccess(map);
    		logger.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			logger.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),"无",e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),"无",e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	/**
	 * 社区矫正人员心理生理分析 折线图
	 * @param correctUserAnalysis
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/20")
	public ServerResponse analysisList(BaseForm<CorrectUserAnalysis> form) {
		ServerResponse result = null;
    	try {
    		CorrectUserAnalysis correctUserAnalysis = form.initQueryObj(CorrectUserAnalysis.class);
    		//查询数据并返回
    		if(correctUserAnalysis.getIdCard()==null) {
    			correctUserAnalysis.setIdCard("");	
    		}
    		List<Dict> dictList=DictUtils.getDictList("info_correct_analysis_type");
    		Map<String,List<CorrectUserAnalysisVo>> map=new HashMap();
    		for (Dict dict : dictList) {
    			correctUserAnalysis.setType(dict.getValue());
    			map.put('"'+dict.getLabel()+'"', changeData(correctUserAnalysisService.findList(correctUserAnalysis)));
    		}
    		result = ServerResponse.createBySuccess(map);
    		logger.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			logger.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
	}
	/**
	 * 社区矫正人员心理生理分析
	 * @param correctUserAnalysis
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/30")
	public ServerResponse analysisOneDay(BaseForm<CorrectUserAnalysis> form) {
		ServerResponse result = null;
    	try {
    		CorrectUserAnalysis correctUserAnalysis = form.initQueryObj(CorrectUserAnalysis.class);
    		//查询数据并返回
    		if(correctUserAnalysis.getIdCard()==null) {
    			correctUserAnalysis.setIdCard("");	
    		}
    		Date begin=correctUserAnalysis.getAnalysisDate();
    		Calendar c = Calendar.getInstance();  
            c.setTime(begin);  
            c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天 
            correctUserAnalysis.setBeginDate(begin);
            correctUserAnalysis.setEndDate(c.getTime());
    		List<CorrectUserAnalysisVo> list=changeData(correctUserAnalysisService.findList(correctUserAnalysis));
    		result = ServerResponse.createBySuccess(list);
    		logger.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			logger.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
	}
	private List<CorrectUserAnalysisVo> changeData(List<CorrectUserAnalysis> list){
		List<CorrectUserAnalysisVo> returnData=Lists.newArrayList();
		for (CorrectUserAnalysis correctUserAnalysis : list) {
			returnData.add(new CorrectUserAnalysisVo(correctUserAnalysis));
		}
		return returnData;
	}
}
