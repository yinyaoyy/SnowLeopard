package com.thinkgem.jeesite.api.info;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.api.dto.vo.info.CorrectUserVo;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.info.entity.CorrectUser;
import com.thinkgem.jeesite.modules.info.service.CorrectUserAnalysisService;
import com.thinkgem.jeesite.modules.info.service.CorrectUserService;

/**
 * 社区矫正人员统计接口
 * @author 王鹏
 * @version 2018年7月16日11:48:43
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping({"/api/100/650", "/api/200/650"})
public class ApiCorrectUserController {

    private static final Logger logger = LoggerFactory.getLogger(ApiCorrectUserController.class);
    
    @Autowired
	private CorrectUserService correctUserService;
    /**
	 * 分页获取社区矫正信息
	 * categoryId  栏目id
	 * @param form
	 * @return
	 */
	@RequestMapping("10")
	public ServerResponse<PageVo<CorrectUserVo>> articleList(BaseForm<Article> form) {
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		int pageNo=0;
		int pageSize=0;
		try {
			 pageNo=StringUtils.toInteger(bject.getByte("pageNo"));
			 if(pageNo==0){
				 pageNo=1;
			 }
			 pageSize=StringUtils.toInteger(bject.getByte("pageSize"));
				if(pageSize==0){
					pageSize=Integer.valueOf(Global.getConfig("page.pageSize"));
				}
		} catch (Exception e) {
			logger.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
		}
		CorrectUser correctUser= new CorrectUser();
		Page<CorrectUser> page = correctUserService.findPage(new Page<CorrectUser>(pageNo, pageSize), correctUser);
		PageVo<CorrectUserVo> pageVo=new PageVo<CorrectUserVo>(page);
		pageVo.setList(changeData(page.getList()));
        ServerResponse<PageVo<CorrectUserVo>> result = ServerResponse.createBySuccess(pageVo);
		return result;
	}
    
	private List<CorrectUserVo> changeData(List<CorrectUser> list){
		List<CorrectUserVo> returnData=Lists.newArrayList();
		for (CorrectUser correctUser : list) {
			returnData.add(new CorrectUserVo(correctUser));
		}
		return returnData;
	}
}
