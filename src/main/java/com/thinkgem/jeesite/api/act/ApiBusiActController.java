package com.thinkgem.jeesite.api.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thinkgem.jeesite.api.act.entity.BusiActDetailVo;
import com.thinkgem.jeesite.api.act.service.BusiActService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;

/**
 * 大屏业务流程相关接口
 * @author 王鹏
 * @version 2018-7-14 14:34:48
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/670", "/api/200/670"})
public class ApiBusiActController {

    private static final Logger logger = LoggerFactory.getLogger(ApiBusiActController.class);
    
    @Autowired
    private BusiActService busiActService;
    
    /**
     * 办理法律援助
     * @author 王鹏
     * @version 2018-05-24 10:56:58
     * @param form
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse getDetail(BaseForm<BusiActDetailVo> form){
    	ServerResponse result = null;
    	try {
    		BusiActDetailVo badv = form.initQueryObj(BusiActDetailVo.class);
    		if(StringUtils.isBlank(badv.getBusiId()) || StringUtils.isBlank(badv.getBusiType())) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER);
    		}
    		else {//查询相应的业务数据
    			badv = busiActService.getDetailByBusiId(badv);
    			result = ServerResponse.createBySuccess(badv);
    		}
		} catch (BusinessException e) {
			// TODO: handle exception
			logger.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
}
