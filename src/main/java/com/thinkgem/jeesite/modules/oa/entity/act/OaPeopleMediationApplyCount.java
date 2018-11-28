/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import java.util.List;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 人民调解业务逻辑Entity
 * @author zhangqiang
 * @version 2018-05-24
 */
public class OaPeopleMediationApplyCount extends DataEntity<OaPeopleMediationApplyCount> {
	
	private static final long serialVersionUID = 1L;
	private String year;		// 年份
	private Area area = new Area();	// 申请人所在旗县
	private Integer count; //数量
	
	//查询开始结束时间
	private String beginDate;
	private String endDate;

	private String datePattern;//日期格式(mysql格式)
	
	private String isAccept; // 是否受理0=否；1=是
	private String dossierStatus; //卷宗的状态，0未归档；1代表为已归档
	private String caseType; // 纠纷类型，字典为oa_case_classify
	private String caseRank; // 严重等级，字典为case_rank
	private String overThreshold ; // 是否达到群体性事件阈值 0否；1是
	
	private String hasMongol; //是否涉及蒙语
	private String caseSource;//案件来源
	
	private List<String> areaList = Lists.newArrayList(); 
	private String procDefKey ; //流程定义
	
	public List<String> getAreaList() {
		return areaList;
	}
	public void setAreaList(List<String> areaList) {
		this.areaList = areaList;
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
	public String getHasMongol() {
		return hasMongol;
	}
	public void setHasMongol(String hasMongol) {
		this.hasMongol = hasMongol;
	}
	public String getCaseSource() {
		return caseSource;
	}
	public void setCaseSource(String caseSource) {
		this.caseSource = caseSource;
	}
	public String getIsAccept() {
		return isAccept;
	}
	public void setIsAccept(String isAccept) {
		this.isAccept = isAccept;
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
	public String getDossierStatus() {
		return dossierStatus;
	}
	public void setDossierStatus(String dossierStatus) {
		this.dossierStatus = dossierStatus;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getCaseRank() {
		return caseRank;
	}
	public void setCaseRank(String caseRank) {
		this.caseRank = caseRank;
	}
	public String getOverThreshold() {
		return overThreshold;
	}
	public void setOverThreshold(String overThreshold) {
		this.overThreshold = overThreshold;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
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
	
	public String getProcDefKey() {
		return procDefKey;
	}
	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}
}