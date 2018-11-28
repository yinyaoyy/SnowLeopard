/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 留言点赞Entity
 * @author wanglin
 * @version 2018-05-08
 */
public class GuestbookCommentThumbsUp extends DataEntity<GuestbookCommentThumbsUp> {
	
	private static final long serialVersionUID = 1L;
	private String commentId;		// 追答id
	private String userId;		// 用户id
	private boolean thumbsUp;
	public GuestbookCommentThumbsUp() {
		super();
	}

	public GuestbookCommentThumbsUp(String id){
		super(id);
	}

	@Length(min=1, max=64, message="追答id长度必须介于 1 和 64 之间")
	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	@Length(min=0, max=64, message="用户id长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isThumbsUp() {
		return thumbsUp;
	}

	public void setThumbsUp(boolean thumbsUp) {
		this.thumbsUp = thumbsUp;
	}

	
}