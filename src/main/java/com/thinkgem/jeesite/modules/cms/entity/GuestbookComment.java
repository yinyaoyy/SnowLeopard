/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 留言回复Entity
 * @author wanglin
 * @version 2018-04-25
 */
public class GuestbookComment extends DataEntity<GuestbookComment> {
	


	private static final long serialVersionUID = 1L;
	private String guestbookId="";		// 回复的留言id
	private String content="";		// 回复内容
	private User createUser;	// 创建者
	private String commentType="";//回复类型0追问1追答
	private List<GuestbookCommentRe> guestbookCommentReList=Lists.newArrayList();
	private int thumbsUpTrue;//点赞
	private int thumbsUpFalse;//反赞
	private String isEvaluate;//是否评价
	private GuestbookEvaluate guestbookEvaluate;
	public GuestbookComment() {
		super();
	}

	public GuestbookComment(String id){
		super(id);
	}

	@Length(min=0, max=64, message="回复的留言id长度必须介于 0 和 64 之间")
	public String getGuestbookId() {
		return guestbookId;
	}

	public void setGuestbookId(String guestbookId) {
		this.guestbookId = guestbookId;
	}
	
	@Length(min=0, max=64, message="回复内容长度必须介于 0 和 64 之间")
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

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public List<GuestbookCommentRe> getGuestbookCommentReList() {
		return guestbookCommentReList;
	}

	public void setGuestbookCommentReList(
			List<GuestbookCommentRe> guestbookCommentReList) {
		this.guestbookCommentReList = guestbookCommentReList;
	}

	public int getThumbsUpTrue() {
		return thumbsUpTrue;
	}

	public void setThumbsUpTrue(int thumbsUpTrue) {
		this.thumbsUpTrue = thumbsUpTrue;
	}

	public int getThumbsUpFalse() {
		return thumbsUpFalse;
	}

	public void setThumbsUpFalse(int thumbsUpFalse) {
		this.thumbsUpFalse = thumbsUpFalse;
	}

	public String getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(String isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	public GuestbookEvaluate getGuestbookEvaluate() {
		return guestbookEvaluate;
	}

	public void setGuestbookEvaluate(GuestbookEvaluate guestbookEvaluate) {
		this.guestbookEvaluate = guestbookEvaluate;
	}
	@Override
	public String toString() {
		return "GuestbookComment [guestbookId=" + guestbookId + ", content=" + content + ", createUser=" + createUser
				+ ", commentType=" + commentType + ", guestbookCommentReList=" + guestbookCommentReList
				+ ", thumbsUpTrue=" + thumbsUpTrue + ", thumbsUpFalse=" + thumbsUpFalse + ", isEvaluate=" + isEvaluate
				+ ", guestbookEvaluate=" + guestbookEvaluate + "]";
	}
}