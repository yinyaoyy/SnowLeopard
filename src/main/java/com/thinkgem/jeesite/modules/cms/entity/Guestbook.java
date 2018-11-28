/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 留言Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Guestbook extends DataEntity<Guestbook> {
	
	private static final long serialVersionUID = 1L;
	private String type=""; 	// 业务分类（咨询、建议、投诉、其它）
	private String typeDesc=""; 	// 业务分类（咨询、建议、投诉、其它）
	private String problemType =""; 	// 问题分类（咨询、建议、投诉、其它）
	private String problemTypeDesc =""; 	// 问题分类（咨询、建议、投诉、其它）
	private String title=""; // 留言内容
	private String content=""; // 留言内容
	private String name=""; 	// 姓名
	private String phone=""; 	// 电话
	private Date createDate;// 留言时间
	private String delFlag="";	// 删除标记删除标记（0：正常；1：删除；2：审核）
	private Area area=new Area();
	private List<GuestbookComment> commentList=Lists.newArrayList();
	private String isComment="";//是否有回复（0无1有）
	private String isInquiries="";//是否有追问（0无1有）
	private int inquiriesCount;
	private String reContent="";
	//为了支持时间段查询数据
	private Date beginDate;
	private Date endDate;
	//指定留言人
	private User user;
	private String statisticNum; //接口访问统计次数

	public User getUser() {
		return user;
	}


	public void setUser(User user) {	
		this.user = user;
	}


	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBeginDate() {
		return beginDate;
	}


	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getEndDate() {
		return endDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Guestbook() {
		this.delFlag = DEL_FLAG_NORMAL;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProblemType() {
		return problemType;
	}
	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<GuestbookComment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<GuestbookComment> commentList) {
		this.commentList = commentList;
	}
	public String getIsInquiries() {
		return isInquiries;
	}
	public void setIsInquiries(String isInquiries) {
		this.isInquiries = isInquiries;
	}
	public String getReContent() {
		return reContent;
	}
	public void setReContent(String reContent) {
		this.reContent = reContent;
	}
	public int getInquiriesCount() {
		return inquiriesCount;
	}
	public void setInquiriesCount(int inquiriesCount) {
		this.inquiriesCount = inquiriesCount;
	}
	public String getIsComment() {
		return isComment;
	}
	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}

	public String getTypeDesc() {
		return typeDesc;
	}


	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}


	public String getProblemTypeDesc() {
		return problemTypeDesc;
	}

	public void setProblemTypeDesc(String problemTypeDesc) {
		this.problemTypeDesc = problemTypeDesc;
	}


	public String getStatisticNum() {
		return statisticNum;
	}


	public void setStatisticNum(String statisticNum) {
		this.statisticNum = statisticNum;
	}
	
}