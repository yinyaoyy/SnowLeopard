package com.thinkgem.jeesite.api.chart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thinkgem.jeesite.api.chart.entity.LawyerVo;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.info.service.LawyerService;

/**
 * 律师接口大屏
 * @author 王鹏
 * @version 2018-6-11 21:27:09
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/570", "/api/200/570"})
public class ApiLawyerChartController {

    private static final Logger log = LoggerFactory.getLogger(ApiLawyerChartController.class);
    
    @Autowired
    private LawyerService lawyerService;
    
    /**
     * 大屏查询:根据条件查询律师信息
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse searchLawyerForBigScreen(BaseForm<LawyerVo> form){
    	ServerResponse result = null;
    	try {
    		LawyerVo lawyerVo = form.initQueryObj(LawyerVo.class);
    		//查询数据并返回
    		PageVo<LawyerVo> resultPage = lawyerService.findListForApiBigScreen(lawyerVo);
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

}
