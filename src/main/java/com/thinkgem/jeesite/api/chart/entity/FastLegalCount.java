/**
 * 
 */
package com.thinkgem.jeesite.api.chart.entity;

import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 统计全盟法律服务平台(快速直通车-受理)
 * @author 王鹏
 * @version 2018-07-12 14:53:24
 */
@JsonInclude
public class FastLegalCount extends DataEntity<FastLegalCount>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6410273347720982928L;
	
	private String startDate;  //查询开始时间
	private String endDate;  //查询结束时间
	private String areaIds;  //选择统计的旗县id集合(英文都好分割)
	private String datePattern;  //查询日期的格式化字符
	private String[] areaArr = null;  //查询时的地区集合
	private List<String> dateList = Lists.newArrayList();  //查询统计的日期集合
	private String forTable = "false";  //是否是表格展示
	
	private Area area = new Area();  //地区
	private Integer count;  //数量
	private String countDate;  //统计的时间段(年yyyy、月yyyy-MM、日yyyy-MM-dd)
	
	@Override
	public String toString() {
		return "ArticleCount [startDate=" + startDate + ", endDate=" + endDate + ", areaIds=" + areaIds
				+ ", datePattern=" + datePattern + ", areaArr=" + Arrays.toString(areaArr) + ", dateList=" + dateList
				+ ", forTable=" + forTable + ", area=" + area + ", count=" + count + ", countDate=" + countDate + "]";
	}
	
	public String getForTable() {
		return forTable;
	}
	public void setForTable(String forTable) {
		this.forTable = forTable;
	}
	public String getCountDate() {
		return countDate;
	}
	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}
	public String[] getAreaArr() {
		return areaArr;
	}
	public void setAreaArr(String[] areaArr) {
		this.areaArr = areaArr;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getAreaIds() {
		return areaIds;
	}
	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}
	public String getDatePattern() {
		return datePattern;
	}
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}
	public List<String> getDateList() {
		return dateList;
	}
	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
