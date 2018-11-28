/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import java.util.List;

//import java.util.Date;
import org.hibernate.validator.constraints.Length;
//import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 法援申请统计Entity
 * @author 王鹏
 * @version 2018-5-29 17:08:10
 */
@JsonInclude
public class OaLegalAidCount extends DataEntity<OaLegalAidCount> {
	
	private static final long serialVersionUID = 1L;
	private String year;   //年份
	private String sex;		// 性别
	private Area area = new Area();      // 所在区域
	private String ethnic;		// 民族
	private String hasMongol;   //是否涉及蒙语0=否;1=是
	private String caseType;		// 案情类型
	private String legalAidType;    //类型:1=申请;2=指定
	private String archiving;   //归档:0=未;1=已
	private String source;//来源(字典source)
	private Integer count;//数量
	
	private String beginDate; //查询开始时间
	private String endDate;   //查询结束时间
	private String litigationType;//诉讼类型(litigation_type)
	private String isAccept;  //是否受理:1=是;非1就是全部;(以是否有yearNo为准，受理一定有，不受理一定没有)
	
	private String datePattern;//日期格式(mysql格式)
	private String forTable; //是否是为表格统计使用
	
	private String sexDesc;//性别说明
	private String ethnicDesc;//民族说明
	private String hasMongolDesc;//是否涉及蒙语说明
	private String caseTypeDesc;//案情说明
	private String legalAidTypeDesc;//类型说明
	private String archivingDesc;//归档说明
	private String sourceDesc;//来源说明
	private String litigationTypeDesc;//诉讼类型
	private String month;//月份
	
	private List<String> areaList = Lists.newArrayList();  //传入多个地区
	private List<String> dateList = Lists.newArrayList();  //传入多个时间点
	
	
	public String getForTable() {
		return forTable;
	}

	public void setForTable(String forTable) {
		this.forTable = forTable;
	}

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}

	public List<String> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<String> areaList) {
		this.areaList = areaList;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public OaLegalAidCount() {
		super();
	}

	public OaLegalAidCount(String id){
		super(id);
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

	public String getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(String isAccept) {
		this.isAccept = isAccept;
	}

	public String getLitigationType() {
		return litigationType;
	}

	public void setLitigationType(String litigationType) {
		this.litigationType = litigationType;
		this.litigationTypeDesc = DictUtils.getDictLabel(litigationType, "litigation_type");
	}

	public String getLitigationTypeDesc() {
		return litigationTypeDesc;
	}

	public void setLitigationTypeDesc(String litigationTypeDesc) {
		this.litigationTypeDesc = litigationTypeDesc;
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

	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}

	public void setCaseTypeDesc(String caseTypeDesc) {
		this.caseTypeDesc = caseTypeDesc;
	}

	public String getSexDesc() {
		return sexDesc;
	}

	public void setSexDesc(String sexDesc) {
		this.sexDesc = sexDesc;
	}

	public String getEthnicDesc() {
		return ethnicDesc;
	}

	public void setEthnicDesc(String ethnicDesc) {
		this.ethnicDesc = ethnicDesc;
	}

	public String getHasMongolDesc() {
		return hasMongolDesc;
	}

	public void setHasMongolDesc(String hasMongolDesc) {
		this.hasMongolDesc = hasMongolDesc;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
		this.sourceDesc = DictUtils.getDictLabel(source, "source");
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getHasMongol() {
		return hasMongol;
	}

	public void setHasMongol(String hasMongol) {
		this.hasMongol = hasMongol;
		this.hasMongolDesc = DictUtils.getDictLabel(hasMongol, "yes_no");
	}

	
	@Length(min=1, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
		this.sexDesc = DictUtils.getDictLabel(sex, "sex");
	}
	
	@Length(min=1, max=100, message="民族长度必须介于 0 和 100 之间")
	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
		this.ethnicDesc = DictUtils.getDictLabel(ethnic, "ethnic");
	}
	
	@Length(min=1, max=1, message="案情类型长度必须介于 0 和 1 之间")
	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
		this.caseTypeDesc = DictUtils.getDictLabel(caseType, "oa_case_classify");
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getLegalAidType() {
		return legalAidType;
	}

	public void setLegalAidType(String legalAidType) {
		this.legalAidType = legalAidType;
		this.legalAidTypeDesc = "2".equals(legalAidType)?"指定":"申请";
	}

	public String getArchiving() {
		return archiving;
	}

	public void setArchiving(String archiving) {
		this.archiving = archiving;
		this.archivingDesc = DictUtils.getDictLabel(archiving, "yes_no");
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getLegalAidTypeDesc() {
		return legalAidTypeDesc;
	}

	public void setLegalAidTypeDesc(String legalAidTypeDesc) {
		this.legalAidTypeDesc = legalAidTypeDesc;
	}

	public String getArchivingDesc() {
		return archivingDesc;
	}

	public void setArchivingDesc(String archivingDesc) {
		this.archivingDesc = archivingDesc;
	}

	public String getSourceDesc() {
		return sourceDesc;
	}

	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	
}