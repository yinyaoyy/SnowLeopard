/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 投诉建议Entity
 * @author wanglin
 * @version 2018-05-11
 */
public class Complaint extends DataEntity<Complaint> {
	
	private static final long serialVersionUID = 1L;
	private String content="";		// 投诉内容
	private String name="";		// 姓名
	private String title="";		// 标题
	private String phone="";		// 电话
	private String ip="";		// IP
	private Area area=new Area();		// 地区
	private Area townarea =new Area();  //镇
	private Area noarea =new Area();  //被投诉人所属旗县
	private Area notown =new Area();//所在乡镇
	private String isInquiries ="";		// 是否有追问
	private int inquiriesCount;		// 追问次数
	private String isComment="";		// 是否有回复
	private String sex;		// 性别
	private String no ="";		// 被投诉人工号
	private boolean anonymous; //是否匿名
	 private Office organization=new Office();//被投诉机构机构
	private String remarks ="";
	//为了支持时间段查询数据
	private Date beginDate;
	private Date endDate;
	private String suchComplaints="";//投诉事项 	cms_complaint_shixiang
	private String classWorker="";//工作人员类别 	cms_complaint_worker
	private List<GuestbookComment> commentList=Lists.newArrayList();//回复列表
	private String reContent;//用来保存回复内容
	private Date createdate;
	private String nonumber="";//被投诉人执业证号
	private String statisticNum;//接口访问统计值
	


	

	public String getSuchComplaints() {
		return suchComplaints;
	}


	public void setSuchComplaints(String suchComplaints) {
		this.suchComplaints = suchComplaints;
	}


	public String getClassWorker() {
		return classWorker;
	}


	public void setClassWorker(String classWorker) {
		this.classWorker = classWorker;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBeginDate() {
		return beginDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
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
	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Office getOrganization() {
		return organization;
	}


	public void setOrganization(Office organization) {
		this.organization = organization;
	}


	
	public Date getCreatedate() {
		return createdate;
	}
	

	public String getNonumber() {
		return nonumber;
	}
	public void setNonumber(String nonumber) {
		this.nonumber = nonumber;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getReContent() {
		return reContent;
	}

	public void setReContent(String reContent) {
		this.reContent = reContent;
	}

	public boolean getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	
	public Complaint() {
		super();
	}

	public Complaint(String id){
		super(id);
	}

	@Length(min=0, max=255, message="投诉内容长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Area getTownarea() {
		return townarea;
	}

	public void setTownarea(Area townarea) {
		this.townarea = townarea;
	}

	public Area getNoarea() {
		return noarea;
	}

	public void setNoarea(Area noarea) {
		this.noarea = noarea;
	}

	public Area getNotown() {
		return notown;
	}

	public void setNotown(Area notown) {
		this.notown = notown;
	}

	@Length(min=0, max=100, message="姓名长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=100, message="标题长度必须介于 0 和 100 之间")
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
	
	public int getInquiriesCount() {
		return inquiriesCount;
	}

	public void setInquiriesCount(int inquiriesCount) {
		this.inquiriesCount = inquiriesCount;
	}
	
	@Length(min=0, max=1, message="是否有回复长度必须介于 0 和 1 之间")
	public String getIsComment() {
		return isComment;
	}

	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=100, message="被投诉人工号长度必须介于 0 和 100 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public List<GuestbookComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<GuestbookComment> commentList) {
		this.commentList = commentList;
	}

	public String getStatisticNum() {
		return statisticNum;
	}


	public void setStatisticNum(String statisticNum) {
		this.statisticNum = statisticNum;
	}


	@Override
	public String toString() {
		return "Complaint [content=" + content + ", name=" + name + ", title=" + title + ", phone=" + phone + ", ip="
				+ ip + ", area=" + area + ", townarea=" + townarea + ", noarea=" + noarea + ", notown=" + notown
				+ ", isInquiries=" + isInquiries + ", inquiriesCount=" + inquiriesCount + ", isComment=" + isComment
				+ ", sex=" + sex + ", no=" + no + ", anonymous=" + anonymous + ", organization=" + organization
				+ ", remarks=" + remarks + ", beginDate=" + beginDate + ", endDate=" + endDate + ", suchComplaints="
				+ suchComplaints + ", classWorker=" + classWorker + ", commentList=" + commentList + ", reContent="
				+ reContent + ", createdate=" + createdate + ", nonumber=" + nonumber + "]";
	}
	
	
}