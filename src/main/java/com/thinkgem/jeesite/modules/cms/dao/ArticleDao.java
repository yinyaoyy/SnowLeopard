/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.ArticleCount;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Comment;

/**
 * 文章DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface ArticleDao extends CrudDao<Article> {
	
	public List<Article> findByIdIn(String[] ids);
	
	public int updateHitsAddOne(String id);
	
	public int updateExpiredWeight(Article article);
	
	public List<Category> findStats(Category category);
	public List<Article> findListByCategoryType(Article article);
	public int articleHotCount(Article article);
	public Article getOldHost(Article article);

	public List<Comment> getCommentListByArticleId(@Param("id")String id);
	
	/**
	 * 各旗县普法宣传文章统计图
	 * @author 王鹏
	 * @version 2018-07-12 15:23:10
	 * @param ac
	 * @return
	 */
	public List<ArticleCount> countArticle(ArticleCount ac);
}
