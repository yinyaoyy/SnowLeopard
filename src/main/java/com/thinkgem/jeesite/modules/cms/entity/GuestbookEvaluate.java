/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.Date;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.sun.tools.javac.util.List;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 留言评价Entity
 * @author wanglin
 * @version 2018-05-09
 */
public class GuestbookEvaluate extends DataEntity<GuestbookEvaluate> {
	
	private static final long serialVersionUID = 1L;
	private String commentId;		// 评价id
	private String prescription;		// 解答时效
	//private String effectiveness;		// 有效性
	//private String dissatisfaction;		// 不满意原因
	private String proposal;		// 意见
	private User beEvaluatedUser;//被评价人
	private String type;
	private Date beginDate;	// 开始时间
	private Date endDate;	// 结束时间
	private String userTypeId;	// 字典人员分类id
	

	public GuestbookEvaluate() {
		super();
	}

	public GuestbookEvaluate(String id){
		super(id);
	}

	@Length(min=0, max=64, message="评价id长度必须介于 0 和 64 之间")
	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	@Length(min=0, max=10, message="解答时效长度必须介于 0 和 10 之间")
	public String getPrescription() {
		return prescription;
	}

	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}
	
	/**
	@Length(min=0, max=10, message="有效性长度必须介于 0 和 10 之间")
	public String getEffectiveness() {
		return effectiveness;
	}

	public void setEffectiveness(String effectiveness) {
		this.effectiveness = effectiveness;
	}
	
	@Length(min=0, max=10, message="不满意原因长度必须介于 0 和 10 之间")
	public String getDissatisfaction() {
		return dissatisfaction;
	}

	public void setDissatisfaction(String dissatisfaction) {
		this.dissatisfaction = dissatisfaction;
	}
	*/
	@Length(min=0, max=500, message="意见长度必须介于 0 和 500 之间")
	public String getProposal() {
		return proposal;
	}

	public void setProposal(String proposal) {
		this.proposal = proposal;
	}

	public User getBeEvaluatedUser() {
		return beEvaluatedUser;
	}

	public void setBeEvaluatedUser(User beEvaluatedUser) {
		this.beEvaluatedUser = beEvaluatedUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(String userTypeId) {
		this.userTypeId = userTypeId;
	}
	
	
}