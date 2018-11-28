/**
 * 
 */
package com.thinkgem.jeesite.api.dto.vo;

/**
 * @author 王鹏
 * @version 2018-04-19 09:50:09
 */
public class AgencyCountVo {

	private String categoryId;//机构分类id
	private String categoryName;//机构分类名称
	private Integer count;//机构数量
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("categoryId ="+categoryId +"//机构分类id\n");
		sb.append("categoryName ="+categoryName +"//机构分类名称\n");
		sb.append("count ="+count +"//机构数量\n");
		return sb.toString();
	}
}
