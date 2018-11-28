/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 评论Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Comment extends DataEntity<Comment> {

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Category category;// 分类编号
	private String contentId;	// 归属分类内容的编号（Article.id、Photo.id、Download.id）
	private String title;	// 归属分类内容的标题（Article.title、Photo.title、Download.title）
	private String content; // 评论内容
	@JsonIgnore
	private String ip; 		// 评论IP
	@JsonIgnore
	private User auditUser; // 审核人
	@JsonIgnore
	private Date auditDate;	// 审核时间
	@JsonIgnore
	private String delFlag;	// 删除标记删除标记（0：正常；1：删除；2：审核）
	@JsonIgnore
    private Office company;//归属机构
	@JsonIgnore
    private Office office;//归属科室
	@JsonIgnore
    private User commentator;  //评论人
    private String name;  //用户登录名
    private String photo; //用户头像

	//为了支持时间段查询数据
	private Date beginDate;
	private Date endDate;

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Comment() {
		super();
		this.delFlag = DEL_FLAG_AUDIT;
	}
	
	public Comment(String id){
		this();
		this.id = id;
	}
	
	public Comment(Category category){
		this();
		this.category = category;
	}

	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@NotNull
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	@Length(min=1, max=255)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Length(min=1, max=1)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	public User getCommentator() {
		return commentator;
	}

	public void setCommentator(User commentator) {
		this.commentator = commentator;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}