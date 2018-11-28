package com.thinkgem.jeesite.api.chart;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thinkgem.jeesite.api.chart.services.CountByAreaService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.utils.enums.CountPeopleByAreaEnum;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;

/**
 * 通用图表接口
 * @author 王鹏
 * @version 2018-6-7 10:59:31
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/660", "/api/200/660"})
public class ApiUniversalCountChartController {

    private static final Logger log = LoggerFactory.getLogger(ApiUniversalCountChartController.class);
    
    @Autowired
    private CountByAreaService countByAreaService;
    
    /**
     * 大屏统计: 全盟概况
     * 1.目前各旗县社区矫正人员数量占比
     * 2.目前各旗县律师人员数量占比
     * 3.目前各旗县公证人员数量占比
     * 4.目前各旗县安置帮教人员数量占比
     * 5.目前三个旗县司法鉴定所人员数量占比
     * 6.目前各旗县人民调解员人员数量占比
     * 7.目前各旗县人民监督员人员数量占比
     * 8.目前各旗县基层法律服务者数量占比
     * 9.各旗县法律援助中心工作人员数量
     * 10.各旗县司法所工作人员人员数量占比
     * 11.全盟各旗县在监服刑人员数量对比
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse countPeopleByArea(BaseForm<Map> form){
    	ServerResponse result = null;
    	try {
    		Map paramMap = form.initQueryObj(Map.class);
    		String key = String.valueOf(paramMap.get("key"));
    		String sort = String.valueOf(paramMap.get("sort"));
    		if(StringUtils.isBlank(key) || StringUtils.isBlank(CountPeopleByAreaEnum.getLabel(key))) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		else {
    			List<List<Object>> list = countByAreaService.countPeopleByArea_EnumKey(key, sort);
    			result = ServerResponse.createBySuccess(list);
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
	 * 统计全部地区的机构数量，并按规定格式返回数据
	 * 示例:
	 * [{
	 * 		"name": "锡林浩特市",
	 * 		"coord": [116.093659, 43.932312],
	 * 		"value": {
	 * 			"areaid": "sdadsad",
	 * 			"setupList": [{
	 * 					"name": "律师事务所",
	 * 					"categoryid": 1231231,
	 * 					"count": 22
	 * 				}, {
	 * 					"name": "律师事务所",
	 * 					"count": 22
	 * 				}
	 * 			]
	 * 		}
	 * 	}
	 * ]
	 * @author 王鹏
	 * @version 2018-06-11 18:24:39
	 * @return
	 */
	@RequestMapping("/20")
    public ServerResponse countAgencyByArea(){
    	ServerResponse result = null;
    	try {
    		List<Map<String, Object>> list = countByAreaService.countAgencyByAllArea();
    		result = ServerResponse.createBySuccess(list);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),"无",e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),"无",e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	

	/**
	 * 统计机构和人员的综合信息
	 * jg=机构：司法所、人民调解委员会、基层法律服务所
	 * ry=人员：司法所工作人员、人民调解员、基层法律服务工作者、人民监督员
	 * 示例:
	 * {
	 * 	xData: ['锡林郭勒盟', '二连浩特市', '锡林浩特市', '阿巴嘎旗',
	 * 		'苏尼特左旗', '苏尼特右旗', '东乌珠穆沁旗', '西乌珠穆沁旗', '太仆寺旗',
	 * 		'镶黄旗', '正镶白旗', '正蓝旗', '多伦县', '乌拉盖管理区'
	 * 	],
	 * 	series: [{
	 * 		name: '司法所',
	 * 		data: [120, 200, 150, 120, 200, 150, 120, 200, 150, 120, 200, 150, 120, 200],
	 * 		type: 'bar'
	 *    }
	 * 	]
	 * }
	 * @author 王鹏
	 * @version 2018-7-15 11:42:31
	 * @return
	 */
	@RequestMapping("/30")
    public ServerResponse countAgencyAndPeopleByArea(BaseForm<Map> form){
    	ServerResponse result = null;
    	try {
    		Map paramMap = form.initQueryObj(Map.class);
    		String type = String.valueOf(paramMap.get("type"));
    		Map<String, Object> list = countByAreaService.countAgencyAndPeopleByArea(type);
    		result = ServerResponse.createBySuccess(list);
    		log.debug("返回参数：{}",result);
		} catch (BusinessException e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),"无",e);
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数:\n{},\n详细信息:\n{}",e.getMessage(),"无",e);
			result = ServerResponse.createByError();
		}
        return result;
    }

    /**
     * 大屏统计: 根据时间段按日期进行统计
     * 1.目前各旗县社区矫正人员数量占比
     * 2.目前各旗县律师人员数量占比
     * 3.目前各旗县公证人员数量占比
     * 4.目前各旗县安置帮教人员数量占比
     * 5.目前三个旗县司法鉴定所人员数量占比
     * 6.目前各旗县人民调解员人员数量占比
     * 7.目前各旗县人民监督员人员数量占比
     * 8.目前各旗县基层法律服务者数量占比
     * 9.各旗县法律援助中心工作人员数量
     * 10.各旗县司法所工作人员人员数量占比
     * 11.全盟各旗县在监服刑人员数量对比
     * @return
     */
	@RequestMapping("/40")
    public ServerResponse countPeopleByDate(BaseForm<Map> form){
    	ServerResponse result = null;
    	try {
    		Map paramMap = form.initQueryObj(Map.class);
    		String key = String.valueOf(paramMap.get("key"));
    		String startDate = String.valueOf(paramMap.get("startDate"));
    		String endDate = String.valueOf(paramMap.get("endDate"));
    		/*
    		 * key必须有值且有效
    		 * startDate和endDate必须有值且格式一致
    		 */
    		if(StringUtils.isBlank(key) || StringUtils.isBlank(CountPeopleByAreaEnum.getLabel(key))
    				|| StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)
    				|| startDate.length() != endDate.length()) {
    			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), ResponseCode.INVALID_PARAMETER.getDesc());
    		}
    		else {
    			Map<String, Object> map = countByAreaService.countPeopleByAreaDate_EnumKey(key, startDate, endDate);
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
