package com.thinkgem.jeesite.api.chart;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thinkgem.jeesite.api.chart.services.FastLegalCountService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;

/**
 * 大屏接口:全盟法律服务平台(快速直通车受理)
 * @author 王鹏
 * @version 2018-6-7 10:59:31
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/710", "/api/200/710"})
public class ApiFastLegalChartController {

    private static final Logger log = LoggerFactory.getLogger(ApiFastLegalChartController.class);
    
    @Autowired
    private FastLegalCountService fastLegalCountService;
    
    /**
     * 全盟法律服务平台接待数量走势图
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse countPeopleByArea(BaseForm<Map> form){
    	ServerResponse result = null;
    	try {
    		Map paramMap = form.initQueryObj(Map.class);
    		String startDate = String.valueOf(paramMap.get("startDate"));
    		String endDate = String.valueOf(paramMap.get("endDate"));
    		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		else {
    			Map<String, Object> map = fastLegalCountService.countFastLegalByAreaDate(startDate, endDate);
    			result = ServerResponse.createBySuccess(map);
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
}
