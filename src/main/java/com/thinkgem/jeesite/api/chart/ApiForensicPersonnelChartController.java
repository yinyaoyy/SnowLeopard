package com.thinkgem.jeesite.api.chart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkgem.jeesite.api.chart.entity.ForensicPersonnelVo;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.info.service.InfoForensicPersonnelService;


/***
 * 司法鉴定员大屏接口
 * @author del
 *
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/590", "/api/200/590"})
public class ApiForensicPersonnelChartController {
	
	 private static final Logger log = LoggerFactory.getLogger(ApiForensicPersonnelChartController.class);
	 
	 	@Autowired
		private InfoForensicPersonnelService infoForensicPersonnelService;
	    
	    /**
	     * 大屏查询:根据条件查询司法鉴定员信息
	     * @return
	     */
		@RequestMapping("/10")
	    public ServerResponse searchForensicForBigScreen(BaseForm<ForensicPersonnelVo> form){
	    	ServerResponse result = null;
	    	try {
	    		ForensicPersonnelVo forensicPersonnelVo = form.initQueryObj(ForensicPersonnelVo.class);
	    		//查询数据并返回
	    		PageVo<ForensicPersonnelVo> resultPage = infoForensicPersonnelService.findListForApiBigScreen(forensicPersonnelVo);
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
