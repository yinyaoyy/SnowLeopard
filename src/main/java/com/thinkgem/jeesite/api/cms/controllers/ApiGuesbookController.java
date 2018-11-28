package com.thinkgem.jeesite.api.cms.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.cms.services.ApiGuesbookService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.cms.entity.Guestbook;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentThumbsUp;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookEvaluate;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookEvaluateService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookService;
import com.thinkgem.jeesite.modules.cms.utils.SensitiveCache;
import com.thinkgem.jeesite.modules.cms.utils.SensitiveWord;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.service.act.OaFastLegalService;
import com.thinkgem.jeesite.modules.oa.service.act.OaLegalAidService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationApplyService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import groovyjarjarasm.asm.tree.IntInsnNode;

/**
 * 留言API接口
 * 此类接口介不需要登录
 * @author wanglin
 * @create 2018-04-18 上午9:03
 */
@RestController
@RequestMapping("/api")
public class ApiGuesbookController {
	@Autowired
	private ApiGuesbookService apiGuesbookService;
	private ApiAgencySearch apiAgencySearch;
	@Autowired
	GuestbookEvaluateService guestbookEvaluateService;
	@Autowired
	private GuestbookService guestbookService;
	@Autowired
    private SystemService systemService;
	@Autowired
	private OaLegalAidService oaLegalAidService;
	@Autowired
	private OaPeopleMediationApplyService oaPeopleMediationApplyService;
	@Autowired
	private GuestbookCommentService guestbookCommentService;
	@Autowired
	private OaFastLegalService oaFastLegalService;
	private static final Logger log = LoggerFactory.getLogger(ApiGuesbookController.class);
	//机构分类字典识别码
    private static final String DICT_AGENCY_CAGETORY = "sys_office_category";
	
    /**
     * 根据提交的信息初始化form内容
     * 并反射出人员分类相应的apiAgencySearch服务
     * @author 
     * @version
     * @param form
     */
    private String getApiAgencySearchByAgencyForm(BaseForm<GuestbookEvaluate> form) {
		form.initQueryObj(GuestbookEvaluate.class);
		//获取字典人员分类键值
    	List<String> userTypeValues = systemService.userTypeid(form.getQueryObj().getBeEvaluatedUser().getId());
    	String userTypeValue = "";
    	if(userTypeValues!=null && userTypeValues.size()>0){
    		userTypeValue = userTypeValues.get(0);
    	}
    	String userType = "";
    	if(StringUtils.isNotBlank(userTypeValue)){
	    	if(userTypeValue.equals("lawyer")){//律师
	    		userType = "1";
	    	}else if(userTypeValue.equals("legal_service_person")){//基层法律服务者
	    		userType = "13";
	    	}else if(userTypeValue.equals("judiciaryUser")){//司法所工作人员
	    		userType = "15";
	    	}else if(userTypeValue.equals("lawAssitanceUser")){//法援
	    		userType = "9";
	    	}else if(userTypeValue.equals("peopleMediation")){//调解员
	    		userType = "11";
	    	}else if(userTypeValue.equals("supervisor")){//人民监督员
	    		userType = "14";
	    	}else if(userTypeValue.equals("notaryMember")){//公证员
	    		userType = "6";
	    	}/*else if(userTypeValue.equals("peopleMediation")){//司法鉴定人员
	    		userType = "7";
	    	}*/
    	}
    	if(StringUtils.isNotBlank(userType)){
	    	form.getQueryObj().setUserTypeId(userType);
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY, form.getQueryObj().getUserTypeId());
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		return userType;
    }
    
   /**
    * 分页获取留言列表
    * @param form
    * @return
    */
	@RequestMapping("/100/600/10")
	public ServerResponse<PageVo<Guestbook>> list(BaseForm<Guestbook> form) {
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
		Page<Guestbook> page=new Page<Guestbook>(pageNo,pageSize);
		Guestbook guestbook=new Guestbook();
		guestbook.setTitle(bject.getString("title"));
		guestbook.setDelFlag("0");
		guestbook.setType(bject.getString("type"));
		guestbook.setProblemType(bject.getString("problemType"));
		guestbook.setIsComment("1");
        page = apiGuesbookService.findPage(page, guestbook); 
        PageVo<Guestbook> pageVo=new PageVo<Guestbook>(page);
		pageVo.setList(page.getList());
        ServerResponse<PageVo<Guestbook>> result = ServerResponse.createBySuccess(pageVo);
		return result;
	}
	  /**
	    * 添加留言
	    * @param form
	    * @return
	    */
	@RequestMapping(value={"/200/600/20","/100/600/20"})
	public ServerResponse<Object> save(BaseForm<Guestbook> form) {
		ServerResponse<Object> result;
		Guestbook guestbook=JSONObject.parseObject(form.getQuery(), Guestbook.class);
		SensitiveWord sensitiveWord=(SensitiveWord)SensitiveCache.get();
		if(sensitiveWord.isContaintSensitiveWord(guestbook.getContent(),1)||sensitiveWord.isContaintSensitiveWord(guestbook.getTitle(),1)) 
		{
			result=ServerResponse.createByErrorCodeMessage(1190, "您的咨询的内容或标题中包含不当言语，请重新编辑提交");
			}
		   else
		{
		 User user=UserUtils.getUser();
		guestbook.setCreateDate(new Date());
		guestbook.setName(user.getName());
		guestbookService.save(guestbook);
		 result = ServerResponse.createBySuccess();
		 }
		
		return result;
	}

	  /**
	    * 获取留言
	    * @param form
	    * @return
	    */
	@RequestMapping("/100/600/30")
	public ServerResponse<Guestbook> getGustbook(BaseForm<Guestbook> form) {
		form.initQueryObj(new TypeReference<Guestbook>(){});
		guestbookService.statisticNum(form.getQueryObj().getId());
		Guestbook guestbook=guestbookService.get(form.getQueryObj().getId());
		ServerResponse<Guestbook> result = ServerResponse.createBySuccess(guestbook);
		return result;
	}
	  /**
	    * 添加留言追问
	    * @param form
	    * @return
	    */
	@RequestMapping(value= {"/200/600/40","/100/600/40"})
	public ServerResponse<String> addGustbookComment(BaseForm<GuestbookCommentRe> form) {
		form.initQueryObj(new TypeReference<GuestbookCommentRe>(){});
		ServerResponse<String> result=null;
		GuestbookCommentRe guestbookCommentre=form.getQueryObj();
		SensitiveWord sensitiveWord=(SensitiveWord)SensitiveCache.get();
		if(sensitiveWord.isContaintSensitiveWord(guestbookCommentre.getContent(), 1)) {
			result=ServerResponse.createByErrorCodeMessage(1190, "您的追问内容中包含不当言语，请重新编辑提交");
		}
		else 
		{
		guestbookCommentre.setCommentType("0");
		guestbookCommentre.setCreateBy(UserUtils.getUser());
		apiGuesbookService.addGustbookComment(guestbookCommentre);
		 result = ServerResponse.createBySuccess();
		}
		return result;
	}
	
	  /**
	    * 点赞
	    * @param form
	    * @return
	    */
	@RequestMapping(value= {"/200/600/50","/100/600/50"})
	public ServerResponse<Integer> addGustbookThumbsUp(BaseForm<GuestbookCommentThumbsUp> form) {
		form.initQueryObj(new TypeReference<GuestbookCommentThumbsUp>(){});
		GuestbookCommentThumbsUp ct=form.getQueryObj();
		ct.setUserId(UserUtils.getUser().getId());
		ServerResponse<Integer> result = ServerResponse.createBySuccess(apiGuesbookService.addGustbookThumbsUp(ct));
		return result;
	}
	  /**
	    * 评价
	    * @param form
	    * @return
	    */
	@RequestMapping(value= {"/200/600/60","/100/600/60"})
	public ServerResponse<List<T>> addGustbookEvaluate(BaseForm<GuestbookEvaluate> form) {
		//获取字典表对应的机构信息(备注中包含表名)
		String userType = getApiAgencySearchByAgencyForm(form);
		//form.initQueryObj(new TypeReference<GuestbookEvaluate>(){});
		GuestbookEvaluate ct=form.getQueryObj();
		ct.setCreateBy(UserUtils.getUser());
		apiGuesbookService.addGustbookEvaluate(ct);
		if(StringUtils.isNotBlank(userType)){
			//查询评价值
			int ev = guestbookEvaluateService.getEvaluatedByUser(ct);
			String evaluation = ev+"";
			//更新相应人员评价值
			apiAgencySearch.evaluationUpdate(evaluation,ct.getBeEvaluatedUser().getId(),"");
		}
		List<T> list = Lists.newArrayList();
		//更新是否评价
		if(StringUtils.isNotBlank(ct.getType())){
			if(ct.getType().equals("1")||ct.getType().equals("2")){
				guestbookCommentService.isEvaluate(ct.getCommentId());
				list = guestbookService.getApi(ct.getCommentId());
			}else if(ct.getType().equals("3")){
				oaLegalAidService.isEvaluate(ct.getCommentId());
				list = oaLegalAidService.getApi(ct.getCommentId());
			}else if(ct.getType().equals("4")){
				oaPeopleMediationApplyService.isEvaluate(ct.getCommentId());
				list = oaPeopleMediationApplyService.getApi(ct.getCommentId());
			}
		}
		ServerResponse<List<T>> result = ServerResponse.createBySuccess(list);
		return result;
	}
	 /**
	    * 分页获取已回复、未回复、留言列表
	    * @param form
	    * @return
	    */
		@RequestMapping(value= {"/200/600/70","/100/600/70"})
		public ServerResponse<PageVo<Guestbook>> userList(BaseForm<Guestbook> form) {
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
			Page<Guestbook> page=new Page<Guestbook>(pageNo,pageSize);
			Guestbook guestbook=new Guestbook();
			String beginDate=bject.getString("beginDate");
			String endDate=bject.getString("endDate");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNoneBlank(beginDate)){
				try {
					guestbook.setBeginDate(sdf.parse(beginDate));
				} catch (ParseException e) {
					log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
				}
			}
			if(StringUtils.isNoneBlank(endDate)){
				try {
					Calendar c = Calendar.getInstance();  
			        c.setTime(sdf.parse(endDate));  
			        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天 
			        guestbook.setEndDate(c.getTime());
				} catch (ParseException e) {
					log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
				}
			}
			guestbook.setCreateBy(UserUtils.getUser());
			guestbook.setIsComment(bject.getString("isComment"));
			guestbook.setTitle(bject.getString("title"));
	        page = apiGuesbookService.findUserList(page, guestbook); 
	        PageVo<Guestbook> pageVo=new PageVo<Guestbook>(page);
			pageVo.setList(page.getList());
	        ServerResponse<PageVo<Guestbook>> result = ServerResponse.createBySuccess(pageVo);
			return result;
		}
	 /**
	    * 分页获取我的问答留言
	    * @param form
	    * @return
	    */
		@RequestMapping(value= {"/200/600/80","/100/600/80"})
		public ServerResponse<PageVo<Guestbook>> findUserIsInquiriesList(BaseForm<Guestbook> form) {
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
			Page<Guestbook> page=new Page<Guestbook>(pageNo,pageSize);
			Guestbook guestbook=new Guestbook();
			guestbook.setCreateBy(UserUtils.getUser());
			//guestbook.setIsComment(bject.getString("isComment"));
	        page = apiGuesbookService.findUserIsInquiriesList(page, guestbook); 
	        PageVo<Guestbook> pageVo=new PageVo<Guestbook>(page);
			pageVo.setList(page.getList());
	        ServerResponse<PageVo<Guestbook>> result = ServerResponse.createBySuccess(pageVo);
			return result;
		}
   /**
    * 分页获取某个人的所有评价
    * @param form
    * @return
    */
	@RequestMapping("/100/600/90")
	public ServerResponse<PageVo<GuestbookEvaluate>> getUserEvaluate(BaseForm<GuestbookEvaluate> form) {
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
		Page<GuestbookEvaluate> page=new Page<GuestbookEvaluate>(pageNo,pageSize);
		page.setOrderBy("a.prescription DESC");
		GuestbookEvaluate  guestbookEvaluate=new GuestbookEvaluate();
		String beginDate=bject.getString("beginDate");
		String endDate=bject.getString("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNoneBlank(beginDate)){
			try {
				guestbookEvaluate.setBeginDate(sdf.parse(beginDate));
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		if(StringUtils.isNoneBlank(endDate)){
			try {
				Calendar c = Calendar.getInstance();  
		        c.setTime(sdf.parse(endDate));  
		        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天 
		        guestbookEvaluate.setEndDate(c.getTime());
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		guestbookEvaluate.setBeEvaluatedUser(new User(bject.getString("id")));
		guestbookEvaluate.setType(bject.getString("type"));
		guestbookEvaluate.setPrescription(bject.getString("prescription"));
        page = apiGuesbookService.getUserEvaluate(page, guestbookEvaluate); 
        PageVo<GuestbookEvaluate> pageVo=new PageVo<GuestbookEvaluate>(page);
		pageVo.setList(page.getList());
		ServerResponse<PageVo<GuestbookEvaluate>> result = ServerResponse.createBySuccess(pageVo);
		return result;
	}	
	
	
	  /**
	    * 满意度评价
	    * 给一件业务数据添加多条评价
	    * @param form
	    * @return
	    */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= {"/200/600/100","/100/600/100"})
	public ServerResponse<List<T>> addGustbookEvaluateGroup(BaseForm<GuestbookEvaluate> form) {
		
		List<T> list = null;
		try {
			String query = form.getQuery();
			JSONObject queryMap = (JSONObject) JSONObject.parse(query);
			String commentId = queryMap.getString("commentId");
			String type = queryMap.getString("type");
			if("".equals(type) || type==null ){
				return ServerResponse.createByErrorMessage("类型不能为空");
			}
			
			List<Map<String, Object>>  evaluatedList = (List<Map<String, Object>>) queryMap.get("evaluatedList");
			Map<String, Object> map = null; 
			GuestbookEvaluate guestbookEvaluate = null;
//			String userType ="";
			if(evaluatedList!=null && evaluatedList.size()>0){
				for (int i = 0; i < evaluatedList.size(); i++) {
					guestbookEvaluate = new GuestbookEvaluate();
					map = evaluatedList.get(i);
					guestbookEvaluate.setBeEvaluatedUser(UserUtils.get((String) map.get("beEvaluatedUserId")));
					guestbookEvaluate.setCommentId(commentId);
					guestbookEvaluate.setCreateBy(UserUtils.getUser());
					guestbookEvaluate.setPrescription((String) map.get("prescription"));
					guestbookEvaluate.setProposal((String) map.get("proposal"));
					guestbookEvaluate.setType(type);
					String userTypeValue = "";
					userTypeValue = getApiAgencySearchByAgencyForm1(guestbookEvaluate,userTypeValue);
					apiGuesbookService.addGustbookEvaluate(guestbookEvaluate);
					if(StringUtils.isNotBlank(userTypeValue)){
						//查询评价值
						int ev = guestbookEvaluateService.getEvaluatedByUser(guestbookEvaluate);
						String evaluation = ev+"";
						//更新相应人员评价值
						apiAgencySearch.evaluationUpdate(evaluation,guestbookEvaluate.getBeEvaluatedUser().getId(),userTypeValue);
					}
				}
			}else{
				return ServerResponse.createByErrorMessage("被评论人不能为空");
			}
			list = Lists.newArrayList();
			//更新是否评价
			if(StringUtils.isNotBlank(guestbookEvaluate.getType())){
				if("1".equals(type)||"2".equals(type)){
					guestbookCommentService.isEvaluate(commentId);
					list = guestbookService.getApi(commentId);
				}else if("3".equals(type)){
					oaLegalAidService.isEvaluate(commentId);
					list = oaLegalAidService.getApi(commentId);
				}else if("4".equals(type)){
					oaPeopleMediationApplyService.isEvaluate(commentId);
					list = oaPeopleMediationApplyService.getApi(commentId);
				}else if("5".equals(type)){
					oaFastLegalService.isEvaluate(commentId);
					list = oaFastLegalService.getApi(commentId);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServerResponse.createByError();
		}
		ServerResponse<List<T>> result = ServerResponse.createBySuccess(list);
		return result;
	}
	
	/**
     * 根据提交的信息初始化form内容
     * 并反射出人员分类相应的apiAgencySearch服务
     * @author 
     * @version
     * @param form
     */
    private String getApiAgencySearchByAgencyForm1(GuestbookEvaluate guestbookEvaluate,String userTypeValue) {
    	
		//获取字典人员分类键值
    	List<String> userTypeValues = systemService.userTypeid(guestbookEvaluate.getBeEvaluatedUser().getId());
    	String type = guestbookEvaluate.getType();
    	String userType = "";
    	String caseClassify = "";
    	if ("5".equals(type)) {
    		caseClassify = oaFastLegalService.get(guestbookEvaluate.getCommentId()).getCaseClassify();
		}
    	for(int i = 0; i<userTypeValues.size() ; i++){
    		
    		if (userTypeValues !=null && userTypeValues.size()>0) {
    			userTypeValue = userTypeValues.get(i);
    		}
    		if(StringUtils.isNotBlank(userTypeValue)){
    			if("1".equals(type)||"2".equals(type)||"3".equals(type)||"4".equals(type)){
    				if(userTypeValue.equals("lawyer")){//律师
        	    		userType = "1";
        	    	}else if(userTypeValue.equals("legal_service_person")){//基层法律服务者
        	    		userType = "13";
        	    	}else if(userTypeValue.equals("judiciaryUser")){//司法所工作人员
        	    		userType = "15";
        	    	}else if(userTypeValue.equals("lawAssitanceUser")){//法援
        	    		userType = "9";
        	    	}else if(userTypeValue.equals("peopleMediation")){//调解员
        	    		userType = "11";
        	    	}
    			}
    			if("5".equals(type)){
    				if("apply_lawyer".equals(caseClassify)&&userTypeValue.equals("lawyer")){//律师
        	    		userType = "1";
        	    	}else if("legal_aid".equals(caseClassify)&&userTypeValue.equals("legal_service_person")){//基层法律服务者
        	    		userType = "13";
        	    	}else if("legal_aid".equals(caseClassify)&&userTypeValue.equals("lawAssitanceUser")){//法援
        	    		userType = "9";
        	    	}else if("people_mediation".equals(caseClassify)&&userTypeValue.equals("peopleMediation")){//调解员
        	    		userType = "11";
        	    	}else if("apply_notarization".equals(caseClassify)&&userTypeValue.equals("notaryMember")){//公证员
        	    		userType = "6";
        	    	}else if("apply_appraise".equals(caseClassify)&&userTypeValue.equals("infoForensicPersonnel")){//司法鉴定员
        	    		userType = "7";
        	    	}
    			}
    			if(!"".equals(userType)){
    				break;
    			}
    			
    		}
    	}
    	
    	if(StringUtils.isNotBlank(userType)){
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY, userType);
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		return userTypeValue;
    }
}
