/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 留言回复Entity
 * @author wanglin
 * @version 2018-05-07
 */
public class GuestbookCommentRe extends DataEntity<GuestbookCommentRe> {
	
	private static final long serialVersionUID = 1L;
	private String commentId;		// 回复的评论id
	private String commentType;		// 回复类型0追问1追答
	private String reUser;		// 被回复人id
	private String guestbookId;		// 留言id
	private String content;		// content
	private User createUser;	// 创建者
	private List<GuestbookCommentRe> commentList;//追答的列表
	public GuestbookCommentRe() {
		super();
	}

	public GuestbookCommentRe(String id){
		super(id);
	}

	@Length(min=0, max=64, message="回复的评论id长度必须介于 0 和 64 之间")
	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	@Length(min=0, max=1, message="回复类型0追问1追答长度必须介于 0 和 1 之间")
	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}
	
	@Length(min=0, max=64, message="被回复人id长度必须介于 0 和 64 之间")
	public String getReUser() {
		return reUser;
	}

	public void setReUser(String reUser) {
		this.reUser = reUser;
	}
	
	@Length(min=0, max=64, message="留言id长度必须介于 0 和 64 之间")
	public String getGuestbookId() {
		return guestbookId;
	}

	public void setGuestbookId(String guestbookId) {
		this.guestbookId = guestbookId;
	}
	
	@Length(min=0, max=1000, message="content长度必须介于 0 和 1000 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	
}