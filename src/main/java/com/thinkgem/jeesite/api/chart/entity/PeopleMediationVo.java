/**
 * 
 */
package com.thinkgem.jeesite.api.chart.entity;

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
 * 接口:大屏查询人民调解案件
 * @author 王鹏
 * @version 2018-06-10 20:27:27
 */
@JsonInclude
public class PeopleMediationVo extends DataEntityVo<PeopleMediationVo> {

	/**分页数据 */
	private Page<PeopleMediationVo> page;
	private String beginDate;
	private String endDate;
	private Area area = new Area();
	//纠纷类型,字典为dispute_type
	private String caseType;
	private String caseRank;//严重等级,字典为case_rank
	
	private String caseTitle;//案件标题
	private String caseInvolveCount;//涉案人数
	private String accuserName;//申请人
	
	private String caseTypeDesc;
	private String caseRankDesc;
	private List<String> areaList = Lists.newArrayList();
	private int count;		//某查询下得到的总数
	private String id ;
	
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
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@JsonIgnore
	public Page<PeopleMediationVo> getPage() {
		return page;
	}
	public void setPage(Page<PeopleMediationVo> page) {
		this.page = page;
	}
	@JsonIgnore
	@JsonFormat(pattern="yyyy-MM-dd")
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	@JsonIgnore
	@JsonFormat(pattern="yyyy-MM-dd")
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
	public String getCaseRank() {
		return caseRank;
	}
	public void setCaseRank(String caseRank) {
		this.caseRank = caseRank;
		this.caseRankDesc = DictUtils.getDictLabel(caseRank, "case_rank");
	}
	public String getCaseTitle() {
		return caseTitle;
	}
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	public String getCaseInvolveCount() {
		return caseInvolveCount;
	}
	public void setCaseInvolveCount(String caseInvolveCount) {
		this.caseInvolveCount = caseInvolveCount;
	}
	public String getAccuserName() {
		return accuserName;
	}
	public void setAccuserName(String accuserName) {
		this.accuserName = accuserName;
	}
	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}
	public void setCaseTypeDesc(String caseTypeDesc) {
		this.caseTypeDesc = caseTypeDesc;
	}
	public String getCaseRankDesc() {
		return caseRankDesc;
	}
	public void setCaseRankDesc(String caseRankDesc) {
		this.caseRankDesc = caseRankDesc;
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
	
}
