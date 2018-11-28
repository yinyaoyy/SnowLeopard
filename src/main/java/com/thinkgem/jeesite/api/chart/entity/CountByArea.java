/**
 * 
 */
package com.thinkgem.jeesite.api.chart.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * @author 王鹏
 * @version 2018-06-10 16:31:20
 */
public class CountByArea extends DataEntity<CountByArea> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7647834459298640272L;
	private Area area; //地区
	private Integer count;//数量
	private String sort;//根据数量进行统计(h=从高到低;l从低到高)
	private String condition;//其他的查询条件，谨慎使用
	private String date;//统计的日期
	
	private String countSql = "SELECT '' count, '' area_id FROM DUAL";//统计语句
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	@JsonIgnore
	@JSONField(serialize=false)
	public String getCountSql() {
		return countSql;
	}
	public void setCountSql(String countSql) {
		this.countSql = countSql;
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
