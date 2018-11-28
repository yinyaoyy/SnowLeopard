package com.thinkgem.jeesite.api.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkgem.jeesite.api.act.entity.ProcessStateVo;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.oa.service.OaProcessStateService;

@RestController
@SuppressWarnings("rawtypes")
@RequestMapping(value={"/api/200/640","/api/100/640"})
public class ApiComprehensiveQueryController {
	
private static final Logger log = LoggerFactory.getLogger(ApiComprehensiveQueryController.class);
	
	@Autowired
	private OaProcessStateService oaProcessStateService;
	
	/**
	 * 综合查询
	 * @param form
	 * @return
	 */
	@RequestMapping("/10")
	public ServerResponse getData(BaseForm<ProcessStateVo> form) {
		ServerResponse result = null;
		try {
			form.initQueryObj(ProcessStateVo.class);
			PageVo<ProcessStateVo> list = oaProcessStateService.comprehensiveQueryForApi(form.getQueryObj());
			result = ServerResponse.createBySuccess(list);
			log.debug("返回参数：{}", result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	
	/**
	 * 统计分析
	 * @param form
	 * @return
	 */
	@RequestMapping("/20")
	public ServerResponse getAnalysis(BaseForm<ProcessStateVo> form) {
		ServerResponse result = null;
		try {
			form.initQueryObj(ProcessStateVo.class);
			List<Map<String, Object>> list = oaProcessStateService.statisticsAnalysis(form.getQueryObj());
			
			result = ServerResponse.createBySuccess(list);
			log.debug("返回参数：{}", result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}

}
