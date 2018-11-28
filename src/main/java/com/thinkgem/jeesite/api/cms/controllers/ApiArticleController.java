package com.thinkgem.jeesite.api.cms.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.api.cms.services.ApiArticleService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.cms.ArticleVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.ArticleData;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Comment;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentThumbsUp;
import com.thinkgem.jeesite.modules.cms.service.CommentService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 文章API接口
 * 此类接口介不需要登录
 * @author wanglin
 * @create 2018-04-18 上午9:03
 */
@RestController
@RequestMapping(value={"/api/100/601","/api/200/601"})
public class ApiArticleController {
	@Autowired
	private  ApiArticleService apiArticleService;
	@Autowired
	private  CommentService commnetService;
	private static final Logger log = LoggerFactory.getLogger(ApiArticleController.class);
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	/**
	 * 获取首页政策发布，服务动态文章列表 
	 * categoryType 1 政策发布,  2 服务动态
	 * @param form
	 * @return
	 */
	@RequestMapping("/10")
	public ServerResponse<PageVo<ArticleVo>> list(BaseForm<Article> form) {
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
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
		}
		Article article=new Article();
		Category category=new Category();
		String categoryType=bject.getByte("categoryType").toString();
		if("1".equals(categoryType)){
			category.setName("政策发布");
		}else if("2".equals(categoryType)){
			category.setName("服务动态");
		}
		article.setCategory(category);
		PageVo<ArticleVo> pageVo=apiArticleService.findListByCategoryType(new Page<Article>(pageNo,pageSize),article);
        ServerResponse<PageVo<ArticleVo>> result = ServerResponse.createBySuccess(pageVo);
		return result;
	}
	/**
	 * 分页获取某个栏目的所有文章
	 * categoryId  栏目id
	 * @param form
	 * @return
	 */
	@RequestMapping("20")
	public ServerResponse<PageVo<ArticleVo>> articleList(BaseForm<Article> form) {
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
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
		}
		Article article=new Article();
		Category category=new Category(bject.getString("categoryId"));
		article.setCategory(category);
		String  isAll=bject.getString("isAll");
		if(StringUtils.isNoneBlank(isAll)&&"false".equals(isAll)){
			article.setPosid("2");
		}
		article.setType(bject.getString("type"));
		String title=bject.getString("title");
		article.setTitle(title);
		String beginDate=bject.getString("beginDate");
		String endDate=bject.getString("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNoneBlank(beginDate)){
			try {
				article.setBeginDate(sdf.parse(beginDate));
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		if(StringUtils.isNoneBlank(endDate)){
			try {
				Calendar c = Calendar.getInstance();  
		        c.setTime(sdf.parse(endDate));  
		        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天 
				article.setEndDate(c.getTime());
			} catch (ParseException e) {
				log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			}
		}
		String content=bject.getString("content");
		if(StringUtils.isNoneBlank(content)){
			ArticleData articleData=new ArticleData();
			articleData.setContent(content);
			article.setArticleData(articleData);
		}
		PageVo<ArticleVo> pageVo=apiArticleService.articleList(new Page<Article>(pageNo,pageSize),article);
        ServerResponse<PageVo<ArticleVo>> result = ServerResponse.createBySuccess(pageVo);
		return result;
	}
	/**
	 * 通过id获取文章详情
	 * articleId  文章id
	 * @param form
	 * @return
	 */
	@RequestMapping("30")
	public ServerResponse<ArticleVo> getArticleById(BaseForm<Article> form) {
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		Article article=new Article((String)bject.get("articleId"));
        ServerResponse<ArticleVo> result = ServerResponse.createBySuccess(apiArticleService.get(article));
		return result;
	}
	/**
	 * 通过服务id获取服务下的政策发布或者服务动态栏目的文章
	 *id  服务id
	 * @param form
	 * @return
	 */
	@RequestMapping("40")
	public ServerResponse<PageVo<ArticleVo>> getArticleByServiceId(BaseForm<Map<String,String>> form) {
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
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
		}
		Article article=new Article();
		Category category=new Category((String)bject.get("sourceId"));
		/*String categoryType=bject.getString("categoryType");
		switch (categoryType) {
		case "1":
			category.setName("政策发布");
			break;
		case "2":
			category.setName("服务动态");
			break;
		case "3":
			category.setName("法律法规");
			break;
		default:
			break;
		}*/
		/*Site site=new Site(bject.getString("sourceId"));
		category.setSite(site);*/
		article.setCategory(category);
        log.debug("接收到参数：{}",form);
    	PageVo<ArticleVo> pageVo=apiArticleService.articleList(new Page<Article>(pageNo,pageSize),article);
        ServerResponse<PageVo<ArticleVo>> result = ServerResponse.createBySuccess(pageVo);
		return result;
	}
	
	/**
	 * 通过id添加文章评论
	 * articleId  
	 * @param form
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("50")
	public ServerResponse saveArticleCommentById(BaseForm<Comment> form) {
		ServerResponse result = null;
		try {
			form.initQueryObj(Comment.class);
			Comment comment = form.getQueryObj();
			String checkResult = checkParam(form.getQueryObj());
			if (StringUtils.isNotBlank(checkResult)) {
				result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER.getCode(), checkResult);
			} else {
				comment.setCommentator(UserUtils.getUser());
				commnetService.save(comment);
				result = ServerResponse.createBySuccess(null);
				log.debug("返回参数：{}", result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
	}
	/**
	 * 校验参数是否正确
	 * 
	 * @author 张强
	 * @version
	 * @return
	 */
	public String checkParam(Object object) {
		StringBuffer sb = new StringBuffer();
		try {
			BeanValidators.validateWithException(validator, object);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
			}
		}
		return sb.toString();
	}
	  /**
	    * 点赞
	    * @param form
	    * @return
	    */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/60")
	public ServerResponse addGustbookThumbsUp(BaseForm<GuestbookCommentThumbsUp> form) {
		form.initQueryObj(GuestbookCommentThumbsUp.class);
		GuestbookCommentThumbsUp ct=form.getQueryObj();
		ServerResponse result = ServerResponse.createBySuccess();
		
		try {
			ct.setUserId(UserUtils.getUser().getId());       //未登录报空指针异常
			apiArticleService.addGustbookThumbsUp(ct);
		} catch (BusinessException e) {
			result = ServerResponse.createByErrorMessage(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result = ServerResponse.createByError();
		}
		return result;
	}
}
