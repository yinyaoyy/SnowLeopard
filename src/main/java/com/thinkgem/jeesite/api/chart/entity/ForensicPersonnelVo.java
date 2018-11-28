package com.thinkgem.jeesite.api.chart.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.api.dto.vo.common.DataEntityVo;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;


@JsonInclude
public class ForensicPersonnelVo extends DataEntityVo<ForensicPersonnelVo> {
	
	public static final String DEL_FLAG_NORMAL = BaseEntity.DEL_FLAG_NORMAL;
	
	private String id;
	private String name;		// 姓名
	private String imageUrl;		// 头像
	private Date birthday;		// 出生日期
	private String politicalFace;		// 政治面貌
	private String education;		// 学历
	private long age;				//年龄
	private String siteId;		// 所属站点id
	private Area area;		// 所在地区
	private String expertise;		// 业务专长
	private Page<ForensicPersonnelVo> page;  //分页
	
	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@ExcelField(title="出生日期", type=0, align=1, sort=50)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
		if(birthday!=null){
			this.age = DateUtils.pastYear(birthday);
		}
	}

	public String getPoliticalFace() {
		return politicalFace;
	}

	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	@JsonIgnore
	public Page<ForensicPersonnelVo> getPage() {
		return page;
	}

	public void setPage(Page<ForensicPersonnelVo> page) {
		this.page = page;
	}

}
