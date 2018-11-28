/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 意见反馈Entity
 * @author wanglin
 * @version 2018-05-12
 */
public class Suggestion extends DataEntity<Suggestion> {
	
	private static final long serialVersionUID = 1L;
	private String content;		// 反馈内容
	private String name;		// 姓名
	private String title;		// 反馈标题
	private String phone;		// 电话
	private String ip;		// IP
	private Area area;		// 地区
	private String isInquiries;		// 是否有追问
	private String inquiriesCount;		// 追问次数
	private String isComment;		// 是否有回复
	private String proposedOffice;		// 被反馈部门
	private String proposedUser;		// 被反馈人员
	private List<GuestbookComment> commentList;//回复列表
	//为了支持时间段查询数据
	private Date beginDate;
	private Date endDate;
	private String reContent;//用来保存回复内容
	public String getReContent() {
		return reContent;
	}


	public void setReContent(String reContent) {
		this.reContent = reContent;
	}


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
	public Suggestion() {
		super();
	}

	public Suggestion(String id){
		super(id);
	}

	@Length(min=0, max=255, message="反馈内容长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=100, message="姓名长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=100, message="反馈标题长度必须介于 0 和 100 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=100, message="电话长度必须介于 0 和 100 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=100, message="IP长度必须介于 0 和 100 之间")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@Length(min=0, max=1, message="是否有追问长度必须介于 0 和 1 之间")
	public String getIsInquiries() {
		return isInquiries;
	}

	public void setIsInquiries(String isInquiries) {
		this.isInquiries = isInquiries;
	}
	
	@Length(min=0, max=4, message="追问次数长度必须介于 0 和 4 之间")
	public String getInquiriesCount() {
		return inquiriesCount;
	}

	public void setInquiriesCount(String inquiriesCount) {
		this.inquiriesCount = inquiriesCount;
	}
	
	@Length(min=0, max=1, message="是否有回复长度必须介于 0 和 1 之间")
	public String getIsComment() {
		return isComment;
	}

	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}
	
	@Length(min=0, max=64, message="被反馈部门长度必须介于 0 和 64 之间")
	public String getProposedOffice() {
		return proposedOffice;
	}

	public void setProposedOffice(String proposedOffice) {
		this.proposedOffice = proposedOffice;
	}
	
	@Length(min=0, max=64, message="被反馈人员长度必须介于 0 和 64 之间")
	public String getProposedUser() {
		return proposedUser;
	}

	public void setProposedUser(String proposedUser) {
		this.proposedUser = proposedUser;
	}

	public List<GuestbookComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<GuestbookComment> commentList) {
		this.commentList = commentList;
	}
	
}