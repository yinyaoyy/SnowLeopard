/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import com.thinkgem.jeesite.common.persistence.ActEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 人民调解受理登记Entity
 * @author zhangqiang
 * @version 2018-05-25
 */
public class OaPeopleMediationAcceptRegisterCount extends ActEntity<OaPeopleMediationAcceptRegisterCount> {
	
	private static final long serialVersionUID = 1L;
	private String year;  //申请年份
	private String caseRank;		//案件严重等级
	private Area area = new Area();	// 案件所在旗县
	private Integer count; //案件数量
	
	private String beginDate; //查询开始时间
	private String endDate;   //查询结束时间
	private String datePattern;//日期格式(mysql格式)
	
	private String countDate;//统计日期
	private String groupByDate = "false";//是否根据日期统计

	
	public String getCountDate() {
		return countDate;
	}
	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}
	public String getGroupByDate() {
		return groupByDate;
	}
	public void setGroupByDate(String groupByDate) {
		this.groupByDate = groupByDate;
	}
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
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCaseRank() {
		return caseRank;
	}
	public void setCaseRank(String caseRank) {
		this.caseRank = caseRank;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

}