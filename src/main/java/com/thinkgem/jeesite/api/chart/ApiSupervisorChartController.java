package com.thinkgem.jeesite.api.chart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkgem.jeesite.api.chart.entity.SupervisorVo;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.info.service.SupervisorService;

/***
 * 大屏 人民监督员接口
 * @author 张强
 *
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/580", "/api/200/580"})
public class ApiSupervisorChartController {
	
	 private static final Logger log = LoggerFactory.getLogger(ApiSupervisorChartController.class);
	    
	 	@Autowired
		private SupervisorService supervisorService;
	    
	    /**
	     * 大屏查询:根据条件查询人民监督员信息
	     * @return
	     */
		@RequestMapping("/10")
	    public ServerResponse searchSupervisorForBigScreen(BaseForm<SupervisorVo> form){
	    	ServerResponse result = null;
	    	try {
	    		SupervisorVo supervisorVo = form.initQueryObj(SupervisorVo.class);
	    		//查询数据并返回
	    		PageVo<SupervisorVo> resultPage = supervisorService.findListForApiBigScreen(supervisorVo);
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
