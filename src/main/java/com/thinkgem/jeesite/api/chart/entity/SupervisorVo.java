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
public class SupervisorVo extends DataEntityVo<LawyerVo> {
	
	public static final String DEL_FLAG_NORMAL = BaseEntity.DEL_FLAG_NORMAL;
	
	private String id;
	private String xrName;		// 人民监督员姓名 
	private String photograph;		// 照片
	private long age;		//年龄
	private String educationBackground;		// 监督员学历
	private Area nativeCounty = new Area();		// native_county
	private String politicsStatus;		// 政治面貌
	private Date birthday;		// 监督员生日
	private Page<SupervisorVo> page;  //分页
	
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

	
	public String getPoliticsStatus() {
		return politicsStatus;
	}
	
	public void setPoliticsStatus(String politicsStatus) {
		this.politicsStatus = politicsStatus;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXrName() {
		return xrName;
	}

	public void setXrName(String xrName) {
		this.xrName = xrName;
	}

	public String getPhotograph() {
		return photograph;
	}

	public void setPhotograph(String photograph) {
		this.photograph = photograph;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public String getEducationBackground() {
		return educationBackground;
	}

	public void setEducationBackground(String educationBackground) {
		this.educationBackground = educationBackground;
	}

	public Area getNativeCounty() {
		return nativeCounty;
	}

	public void setNativeCounty(Area nativeCounty) {
		this.nativeCounty = nativeCounty;
	}

	@JsonIgnore
	public Page<SupervisorVo> getPage() {
		return page;
	}

	public void setPage(Page<SupervisorVo> page) {
		this.page = page;
	}
	
}
