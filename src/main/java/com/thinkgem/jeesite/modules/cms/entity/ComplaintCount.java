/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import com.thinkgem.jeesite.modules.sys.entity.Area;
//import java.util.Date;
//import com.alibaba.fastjson.annotation.JSONField;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;

/**
 * 投诉建议统计属性Entity
 * @author 王鹏
 * @version 2018-6-8 15:08:07
 */
public class ComplaintCount extends DataEntity<ComplaintCount> {
	
	private static final long serialVersionUID = 1L;
	public static String OFFICE_LAWYER_OFFICE = OfficeRoleConstant.OFFICE_LAWYER_OFFICE;
	
	private String beginDate; //查询开始日期
	private String endDate;  // 查询结束日期
	
	private String year; //年份
	private Area area = new Area();  //地区
	private String isComment;		// 是否有回复
	private Integer count ; // 数量

	private String datePattern;//日期格式(mysql格式)
	
	private String type; 	// 统计机构类型，目前4=调委会;5=法援中心
	private String[] countNames; //投诉统计名称和受理统计名称
	


	/**
	 * 根据开始日期自动判定，请确保开始日期数据完整正确
	 * @author 王鹏
	 * @version 2018-06-26 10:17:34
	 * @return
	 */
	public String getDatePattern() {
		datePattern = "";
		if(StringUtils.isNotBlank(beginDate)) {
			if(DateUtils.match("\\d{4}", beginDate)) {
				datePattern = "%Y";
			}
			else if(DateUtils.match("\\d{4}-\\d{2}", beginDate)) {
				datePattern = "%Y-%m";
			}
			else if(DateUtils.match("\\d{4}-\\d{2}-\\d{2}", beginDate)) {
				datePattern = "%Y-%m-%d";
			}
		}
		return datePattern;
	}
	public String[] getCountNames() {
		return countNames;
	}
	public void setCountNames(String[] countNames) {
		this.countNames = countNames;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
//	@JSONField(format="yyyy-MM-dd")
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
//	@JSONField(format="yyyy-MM-dd")
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getIsComment() {
		return isComment;
	}
	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}