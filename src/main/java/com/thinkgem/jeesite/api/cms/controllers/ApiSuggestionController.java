package com.thinkgem.jeesite.api.cms.controllers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinkgem.jeesite.api.cms.services.ApiSuggestionService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;
import com.thinkgem.jeesite.modules.cms.entity.Suggestion;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 意见反馈API接口
 * 此类接口介不需要登录
 * @author wanglin
 * @create 2018-04-18 上午9:03
 */
@RestController
@RequestMapping("/api")
public class ApiSuggestionController {
	@Autowired
	private ApiSuggestionService apiSuggestionService;
	private static final Logger log = LoggerFactory.getLogger(ApiSuggestionController.class);
	 /**
	    * 分页获取已回复、未回复意见反馈列表
	    * @param form
	    * @return
	    */
		@RequestMapping(value= {"/200/620/10","/100/620/10"})
		public ServerResponse<PageVo<Suggestion>> userSuggestionList(BaseForm<Suggestion> form) {
			JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
			int pageNo=1;
			try {
				 pageNo=StringUtils.toInteger(bject.getByte("pageNo"));
			} catch (Exception e) {
				log.equals(e);
			}
			int pageSize=Integer.valueOf(Global.getConfig("page.pageSize"));
			try {
				pageSize=StringUtils.toInteger(bject.getByte("pageSize"));
			} catch (Exception e) {
				log.equals(e);
			}
			Page<Suggestion> page=new Page<Suggestion>(pageNo,pageSize);
			Suggestion suggestion=new Suggestion();
			suggestion.setCreateBy(UserUtils.getUser());
			suggestion.setIsComment(bject.getString("isComment"));
	        //page = apiSuggestionService.findUserSuggestionList(page, Suggestion); 
	        PageVo<Suggestion> pageVo=new PageVo<Suggestion>(page);
			pageVo.setList(page.getList());
	        ServerResponse<PageVo<Suggestion>> result = ServerResponse.createBySuccess(pageVo);
			return result;
		}
	 /**
	    * 分页获取我的问答意见反馈
	    * @param form
	    * @return
	    */
		@RequestMapping(value= {"/200/620/20","/100/620/20"})
		public ServerResponse<PageVo<Suggestion>> findUserIsInquiriesSuggestionList(BaseForm<Suggestion> form) {
			JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
			int pageNo=1;
			try {
				 pageNo=StringUtils.toInteger(bject.getByte("pageNo"));
			} catch (Exception e) {
				log.equals(e);
			}
			int pageSize=Integer.valueOf(Global.getConfig("page.pageSize"));
			try {
				pageSize=StringUtils.toInteger(bject.getByte("pageSize"));
			} catch (Exception e) {
				log.equals(e);
			}
			Page<Suggestion> page=new Page<Suggestion>(pageNo,pageSize);
			Suggestion suggestion=new Suggestion();
			suggestion.setCreateBy(UserUtils.getUser());
	        //page = apiSuggestionService.findUserIsInquiriesSuggestionList(page, suggestion); 
	        PageVo<Suggestion> pageVo=new PageVo<Suggestion>(page);
			pageVo.setList(page.getList());
	        ServerResponse<PageVo<Suggestion>> result = ServerResponse.createBySuccess(pageVo);
			return result;
		}
	  /**
	    * 添加意见反馈
	    * @param form
	    * @return
	    */
		@RequestMapping(value= {"/200/620/30","/100/620/30"})
		public ServerResponse<?> saveSuggestion(BaseForm<Suggestion> form) {
			Suggestion suggestion=JSONObject.parseObject(form.getQuery(), Suggestion.class);
			User user=UserUtils.getUser();
			suggestion.setCreateDate(new Date());
			suggestion.setName(user.getName());
			apiSuggestionService.addSuggestion(suggestion);
			ServerResponse<?> result = ServerResponse.createBySuccess();
			return result;
		}
	  /**
	    * 添加意见反馈追问
	    * @param form
	    * @return
	    */
		@RequestMapping(value= {"/200/620/40","/100/620/40"})
		public ServerResponse<String> addGustbookComment(BaseForm<GuestbookCommentRe> form) {
			form.initQueryObj(new TypeReference<GuestbookCommentRe>(){});
			GuestbookCommentRe guestbookCommentre=form.getQueryObj();
			guestbookCommentre.setCommentType("0");
			guestbookCommentre.setCreateBy(UserUtils.getUser());
			apiSuggestionService.addSuggestionComment(guestbookCommentre);
			ServerResponse<String> result = ServerResponse.createBySuccess();
			return result;
		}
		
}
