package com.thinkgem.jeesite.api.cms.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinkgem.jeesite.api.cms.services.ApiComplaintService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.cms.entity.Complaint;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;
import com.thinkgem.jeesite.modules.cms.service.ComplaintService;
import com.thinkgem.jeesite.modules.cms.utils.SensitiveCache;
import com.thinkgem.jeesite.modules.cms.utils.SensitiveWord;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 投诉建议API接口
 * 此类接口介不需要登录
 * @author wanglin
 * @create 2018-04-18 上午9:03
 */
@RestController
@RequestMapping("/api")
public class ApiComplaintController {
	@Autowired
	private ApiComplaintService apiComplaintService;
	@Autowired
	private ComplaintService complaintService;
	private static final Logger log = LoggerFactory.getLogger(ApiComplaintController.class);
	/**
	 * 验证Bean实例对象
	 */
	

	 /**
	    * 分页获取已回复、未回复投诉建议列表
	    * @param form
	    * @return
	    */
		@RequestMapping(value= {"/200/610/10","/100/610/10"})
		public ServerResponse<PageVo<Complaint>> userComplaintList(BaseForm<Complaint> form) {
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
			Page<Complaint> page=new Page<Complaint>(pageNo,pageSize);
			Complaint complaint=new Complaint();
			complaint.setCreateBy(UserUtils.getUser());
			complaint.setIsComment(bject.getString("isComment"));
	        page = apiComplaintService.findUserComplaintList(page, complaint); 
	        PageVo<Complaint> pageVo=new PageVo<Complaint>(page);
			pageVo.setList(page.getList());
	        ServerResponse<PageVo<Complaint>> result = ServerResponse.createBySuccess(pageVo);
			return result;
		}
	 /**
	    * 分页获取我的问答投诉建议
	    * @param form
	    * @return
	    */
		@RequestMapping(value= {"/200/610/20","/100/610/20"})
		public ServerResponse<PageVo<Complaint>> findUserIsInquiriesComplaintList(BaseForm<Complaint> form) {
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
			Page<Complaint> page=new Page<Complaint>(pageNo,pageSize);
			Complaint complaint=new Complaint();
			complaint.setCreateBy(UserUtils.getUser());
	        page = apiComplaintService.findUserIsInquiriesComplaintList(page, complaint); 
	        PageVo<Complaint> pageVo=new PageVo<Complaint>(page);
			pageVo.setList(page.getList());
	        ServerResponse<PageVo<Complaint>> result = ServerResponse.createBySuccess(pageVo);
			return result;
		}
	  /**
	    * 添加投诉建议
	    * @param form
	    * @return
	    */
		@RequestMapping(value= {"/200/610/30","/100/610/30"})
		public ServerResponse<?> saveComplaint(HttpServletRequest request,BaseForm<Complaint> form) {
			ServerResponse result = null;	
			Complaint complaint=JSONObject.parseObject(form.getQuery(), Complaint.class);
			//默认从缓存中去取模型
			SensitiveWord sensitiveWord=(SensitiveWord)SensitiveCache.get();
			if(sensitiveWord.isContaintSensitiveWord(complaint.getContent(), 1)||sensitiveWord.isContaintSensitiveWord(complaint.getTitle(), 1)) {
				result=ServerResponse.createByErrorCodeMessage(1190, "您的投诉内容或标题中包含不当言语，请重新编辑提交");
			}else {
				if(complaint.getContent() == null || complaint.getContent().length()<=0) {
					result=ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), "投诉内容为空");
					}
				else if(complaint.getTitle() == null ||complaint.getTitle().length()<=0) {
					result=ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), "标题不能为空");
				}
				else if(complaint.getOrganization().getId() == null ||complaint.getOrganization().getId().length()<=0) {
					result=ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), "找不到该机构");
				}
				else {
					User user=UserUtils.getUser();
					complaint.setCreateDate(new Date());
					complaint.setCreateBy(user);
					apiComplaintService.addComplaint(complaint);	
					 result = ServerResponse.createBySuccess();
				}}	
		
			return result;
		}
		
		@Autowired
		protected Validator validator;
		public String checkParam(Object complaint) {
			StringBuffer sb = new StringBuffer();
			try {
				BeanValidators.validateWithException(validator, complaint);
			} catch (ConstraintViolationException ex) {
				List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
				for (int i = 0; i < list.size(); i++) {
					sb.append(list.get(i));
				}
			}
			return sb.toString();
		}
		
	  /**
	    * 添加投诉建议追问
	    * @param form
	    * @return
	    */
		@RequestMapping( value= {"/200/610/40","/100/610/40"})
		public ServerResponse<String> addGustbookComment(BaseForm<GuestbookCommentRe> form) {
			form.initQueryObj(new TypeReference<GuestbookCommentRe>(){});
			ServerResponse<String> result;
			GuestbookCommentRe guestbookCommentRe=form.getQueryObj();
			SensitiveWord sensitiveWord=(SensitiveWord)SensitiveCache.get();
			if(sensitiveWord.isContaintSensitiveWord(guestbookCommentRe.getContent(), 1)) {
				result=ServerResponse.createByErrorCodeMessage(1190, "您的追问内容中包含不当言语，请重新编辑提交");
			}else {
			guestbookCommentRe.setCommentType("0");
			guestbookCommentRe.setCreateBy(UserUtils.getUser());
			apiComplaintService.addComplaintComment(guestbookCommentRe);
			result = ServerResponse.createBySuccess();
			}
			return result;
		}
		
		
		  /**
		    * 根据id获取投诉建议详情
		    * @param form
		    * @return
		    */
		@RequestMapping("/100/610/50")
		public ServerResponse<Complaint> getComplaint(BaseForm<Complaint> form) {
			form.initQueryObj(new TypeReference<Complaint>(){});
			 String id=form.getQueryObj().getId();
			complaintService.statisticNum(id);  
			Complaint	complaint= complaintService.get(id);
	      
			ServerResponse<Complaint> result = ServerResponse.createBySuccess(complaint);
			return result;
		}
		
	
}
