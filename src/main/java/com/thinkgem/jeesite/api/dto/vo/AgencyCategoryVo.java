/**
 * 
 */
package com.thinkgem.jeesite.api.dto.vo;

import com.thinkgem.jeesite.api.dto.vo.cms.SysServiceVo;

/**
 * @author 王鹏
 * @version 2018-04-18 10:10:16
 */
public class AgencyCategoryVo {

	private String categoryId;//分类id
	private String categoryName;//分类名称
	
	private SysServiceVo serviceVo;//业务服务内容
	
	
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

	public AgencyCategoryVo(String categoryId, String categoryName) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
	
	public SysServiceVo getServiceVo() {
		return serviceVo;
	}
	public void setServiceVo(SysServiceVo serviceVo) {
		this.serviceVo = serviceVo;
	}
	public AgencyCategoryVo() {
		super();
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("categoryId="+categoryId+"//机构分类id\n");
		sb.append("categoryName="+categoryName+"//分类名称\n");
		sb.append("serviceVo="+serviceVo+"//业务服务内容\n");
		return sb.toString();
	}
}
