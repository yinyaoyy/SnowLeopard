package com.thinkgem.jeesite.api.cms.services;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.dto.vo.cms.ArticleVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.dao.ArticleDao;
import com.thinkgem.jeesite.modules.cms.dao.CategoryDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.ArticleData;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentThumbsUp;
import com.thinkgem.jeesite.modules.cms.service.ArticleDataService;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentThumbsUpService;
import com.thinkgem.jeesite.modules.cms.utils.SensitiveCache;
import com.thinkgem.jeesite.modules.cms.utils.SensitiveWord;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * api文章service层
 * @author wanglin
 * @create 2018-04-18 上午10:00
 */
@Service
public class ApiArticleService {
	
	//获取jeesite文件服务器ip
	public static final String server_url = Global.getConfig("server_url");
	
	public static final String url = "src=\"/userfiles";
	
	public static final String aUrl = "src=&quot;/userfiles";
	
	public static final String urls = "src="+"\""+server_url+"/userfiles";
	
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private ArticleDataService articleDataService;
	@Autowired
	private GuestbookCommentThumbsUpService guestbookCommentThumbsUpService;

	public PageVo<ArticleVo> findListByCategoryType(Page<Article> page,Article article){
		article.setPage(page);
		List<Article> articleList=articleDao.findListByCategoryType(article);
		return changeList(articleList,page);
	}
	@Transactional(readOnly = false)
	public PageVo<ArticleVo> articleList(Page<Article> page, Article article) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate =  (Date)CacheUtils.get("updateExpiredWeightDateByArticle");
		if (updateExpiredWeightDate == null || (updateExpiredWeightDate != null 
				&& updateExpiredWeightDate.getTime() < new Date().getTime())){
			articleDao.updateExpiredWeight(article);
			CacheUtils.put("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
		}
		if (article.getCategory()!=null && StringUtils.isNotBlank(article.getCategory().getId()) && !Category.isRoot(article.getCategory().getId())){
			Category category = categoryDao.get(article.getCategory().getId());
			if (category==null){
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			article.setCategory(category);
		}
		else{
			article.setCategory(new Category());
		}
		article.setPage(page);
		return  changeList(articleDao.findList(article),page);
	}
	
	@Transactional(readOnly = false)
	public ArticleVo get(Article article) {
		article=articleDao.get(article);
		//文章点击数+1
		articleService.updateHitsAddOne(article.getId());
//		article.setHits(article.getHits()+1);
//		articleDao.update(article);
		
		//修改文章文件路径
		ArticleData articleData = article.getArticleData();
		//获取评论列表
		if("1".equals(articleData.getAllowComment())){
			article.setCommentList(articleDao.getCommentListByArticleId(article.getId()));
		}
		//获取点赞数
		GuestbookCommentThumbsUp guestbookCommentThumbsUp = new GuestbookCommentThumbsUp();
		guestbookCommentThumbsUp.setCommentId(article.getId());
		guestbookCommentThumbsUp.setDelFlag("0");
		int thumbsUpCount = guestbookCommentThumbsUpService.findThumbsUpByCommentId(guestbookCommentThumbsUp);
		article.setThumbsUpCount(thumbsUpCount);	
		//判断当前用户是否点赞了该文章
		guestbookCommentThumbsUp.setUserId(UserUtils.getUser().getId());
		guestbookCommentThumbsUp = guestbookCommentThumbsUpService.get(guestbookCommentThumbsUp);
		if(guestbookCommentThumbsUp==null){
			article.setIsThumbsUp("false");
		}else{
			article.setIsThumbsUp("true");
		}
		
		if(articleData != null&&StringUtils.isNotBlank(articleData.getContent())){
    		if(articleData.getContent().indexOf(url) != -1){
    			articleData.setContent(articleData.getContent().replaceAll(url, urls));
    			article.setArticleData(articleData);
    		}else if(articleData.getContent().indexOf(aUrl) != -1){
    			articleData.setContent(articleData.getContent().replaceAll(aUrl, urls));
    			article.setArticleData(articleData);
    		}else{
    			article.setArticleData(articleDataService.get(article.getId()));
    		}
    		//文章增加敏感词过滤
    		SensitiveWord sensitiveWord=SensitiveCache.get();
			String string =sensitiveWord.replaceSensitiveWord(article.getArticleData().getContent(), 2, "*");
			article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(
					string));
    	}else{
    		article.setArticleData(articleDataService.get(article.getId()));
    	}
		return  new ArticleVo(article);
	}
	@Transactional(readOnly = false)
	public PageVo<ArticleVo> getArticleByServiceId(Page<Article> page,Article article){
		/*Category category=article.;
		article.setCategory(category);*/
		//修改文章文件路径
		return this.articleList(page, article);
	}
	private PageVo<ArticleVo> changeList(List<Article> articleList,Page<Article> page){
		List<ArticleVo>articleVoList=Lists.newArrayList();
        if(null != articleList && articleList.size() > 0){
        	ArticleVo articlevo = null;
        	ArticleData articleData = null;
            for (Article articles : articleList){
            	articlevo = new ArticleVo(articles);
            	articleData = articlevo.getArticleData();
            	//修改文章文件路径
            	if(articleData != null&&StringUtils.isNotBlank(articleData.getContent())){
            		if(articleData.getContent().indexOf(url) != -1){
            			articleData.setContent(articleData.getContent().replaceAll(url, urls));
            			articlevo.setArticleData(articleData);
            		}else if(articleData.getContent().indexOf(aUrl) != -1){
            			articleData.setContent(articleData.getContent().replaceAll(aUrl, urls));
            			articlevo.setArticleData(articleData);
            		}
            	}
            	articleVoList.add(articlevo);
            }
        }
		PageVo<ArticleVo> pageVo=new PageVo<ArticleVo>(page);
		pageVo.setList(articleVoList);
		return pageVo;
	}
	
	/**
	 * 文章点赞和取消点赞
	 * @param ct
	 * @throws Exception
	 */
	@Transactional
	public void addGustbookThumbsUp(GuestbookCommentThumbsUp ct) throws Exception{
		GuestbookCommentThumbsUp gct=guestbookCommentThumbsUpService.get(ct);
		if(gct==null && ct.isThumbsUp()){
			guestbookCommentThumbsUpService.save(ct);
		}else if(gct!=null && !ct.isThumbsUp()){
			ct.setUserId(UserUtils.getUser().getId());
			guestbookCommentThumbsUpService.deleteByCommentIdAndUserId(ct);
		}else if(gct!=null && ct.isThumbsUp()){
			throw new BusinessException("文章已经点赞");
		}else if(gct==null && !ct.isThumbsUp()){
			throw new BusinessException("文章已经取消点赞");
		}else{
			throw new Exception();
		}
	}
}
