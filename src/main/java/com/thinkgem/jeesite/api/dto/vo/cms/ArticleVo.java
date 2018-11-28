package com.thinkgem.jeesite.api.dto.vo.cms;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.dto.vo.AgencyCategoryVo;
import com.thinkgem.jeesite.api.dto.vo.common.DataEntityVo;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.ArticleData;
import com.thinkgem.jeesite.modules.cms.entity.Comment;
@JsonInclude
public  class ArticleVo extends DataEntityVo<ArticleVo> {
	private String id;
	private String title;	// 标题
    private String link;	// 外部链接
	private String image;	// 文章图片
	private String keywords;// 关键字
	private String description;// 描述、摘要
	private Integer hits;	// 点击数
	private ArticleData articleData;	//文章副表
	private AgencyCategoryVo agencyCategoryVo;
	private String siteId;
	private String siteName;
    private String files;		// 附件
    private String type;//文章业务类型
    private Integer thumbsUpCount;	// 点赞数
    private List<Comment> commentList = Lists.newArrayList(); //评论列表
    private Integer commentCount = 0; //评论数量
    private String isThumbsUp;	//当前用户是否点赞了该文章
    
    public String getIsThumbsUp() {
		return isThumbsUp;
	}
	public void setIsThumbsUp(String isThumbsUp) {
		this.isThumbsUp = isThumbsUp;
	}
	public Integer getCommentCount() {
    	if(commentList != null && commentList.size() > 0 ){
    		return commentList.size();
    	}
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	public Integer getThumbsUpCount(){
    	return thumbsUpCount;
    }
    public void setThumbsUpCount(Integer thumbsUpCount){
    	this.thumbsUpCount = thumbsUpCount;
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getHits() {
		return hits;
	}
	public void setHits(Integer hits) {
		this.hits = hits;
	}
	public ArticleData getArticleData() {
		return articleData;
	}
	public void setArticleData(ArticleData articleData) {
		this.articleData = articleData;
	}
	
	public AgencyCategoryVo getAgencyCategoryVo() {
		return agencyCategoryVo;
	}
	public void setAgencyCategoryVo(AgencyCategoryVo agencyCategoryVo) {
		this.agencyCategoryVo = agencyCategoryVo;
	}
	
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArticleVo() {
		
	}
	public ArticleVo(Article article) {
		this.id = article.getId();
		this.title = article.getTitle();
		this.link = article.getLink();
		this.image = article.getImage();
		this.keywords = article.getKeywords();
		this.description = article.getDescription();
		this.hits = article.getHits();
		this.articleData=article.getArticleData();
		this.createByName=article.getCreateBy().getName();
	    this.createDate=article.getCreateDate();
        this.updateByName=article.getUpdateBy().getName();
		this.updateDate=article.getUpdateDate();
		this.agencyCategoryVo=new AgencyCategoryVo(article.getCategory().getId(),article.getCategory().getName());
	    this.siteName=article.getSiteName();
	    this.siteId=article.getSiteId();
	    this.files=article.getFiles();
	    this.type=article.getType();
	    this.commentList = article.getCommentList();
	    this.thumbsUpCount = article.getThumbsUpCount();
	    this.isThumbsUp = article.getIsThumbsUp();
	}
}
