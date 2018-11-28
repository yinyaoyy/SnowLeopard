/**
 * 
 */
package com.thinkgem.jeesite.api.chart.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.dto.vo.common.DataEntityVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 接口:大屏查询法援申请
 * @author 王鹏
 * @version 2018-06-10 20:27:27
 */
@JsonInclude
public class LegalAidVo extends DataEntityVo<LegalAidVo> {

	private String id;
	private Page<LegalAidVo> page;
	private String beginDate;
	private String endDate;
	private Area area = new Area();
	private String caseType;
	private String caseTitle;
	private Date applyDate;
	private String name;
	private String caseTypeDesc;
	private String caseNature;
	private String caseNatureDesc;
	private List<String> areaList = Lists.newArrayList();
	private int count;   //查询下得到的总条数
	private String archiving;   //归档:0=未;1=已
	
	private String datePattern;//日期格式(mysql格式)

	
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
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JsonIgnore
	public Page<LegalAidVo> getPage() {
		return page;
	}
	public void setPage(Page<LegalAidVo> page) {
		this.page = page;
	}
	@JsonIgnore
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	@JsonIgnore
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
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
		this.caseTypeDesc = DictUtils.getDictLabel(caseType, "oa_case_classify");
	}
	public String getCaseTitle() {
		return caseTitle;
	}
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	@JsonFormat(pattern="yyyy-MM-dd")
	public Date getApplyDate() {
		return createDate==null?applyDate:createDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}
	public void setCaseTypeDesc(String caseTypeDesc) {
		this.caseTypeDesc = caseTypeDesc;
	}
	public String getCaseNature() {
		return caseNature;
	}
	public void setCaseNature(String caseNature) {
		this.caseNature = caseNature;
		this.caseNatureDesc = DictUtils.getDictLabel(caseNature, "case_nature");
	}
	public String getCaseNatureDesc() {
		return caseNatureDesc;
	}
	public void setCaseNatureDesc(String caseNatureDesc) {
		this.caseNatureDesc = caseNatureDesc;
	}
	public List<String> getAreaList() {
		return areaList;
	}
	public void setAreaList(List<String> areaList) {
		this.areaList = areaList;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getArchiving() {
		return archiving;
	}
	public void setArchiving(String archiving) {
		this.archiving = archiving;
	}
}
