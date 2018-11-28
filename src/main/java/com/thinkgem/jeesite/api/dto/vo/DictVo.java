/**
 * 
 */
package com.thinkgem.jeesite.api.dto.vo;

import com.thinkgem.jeesite.modules.sys.entity.Dict;

/**
 * 接口字典显示内容
 * @author 王鹏
 * @version 2018-04-18 15:36:56
 */
public class DictVo {

	private String value;	// 数据值
	private String label;	// 标签名
	private String type;	// 类型
	private String description;// 描述
	private Integer sort;	// 排序
	private String parentId;//父Id
	private String remarks;//备注
    private String languageType;//语言

    public DictVo() {
    }
    public DictVo(Dict d){
    	this.value = d.getValue();
    	this.label = d.getLabel();
    	this.type = d.getType();
    	this.description = d.getDescription();
    	this.sort = d.getSort();
    	this.parentId = d.getParentId();
    	this.remarks = d.getRemarks();
    	this.languageType = d.getLanguageType();
    }
    
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
	    sb.append("value       ="+value       +"// 数据值\n");
	    sb.append("label       ="+label       +"// 标签名\n");
	    sb.append("type        ="+type        +"// 类型  \n");
	    sb.append("description ="+description +"// 描述  \n");
	    sb.append("sort        ="+sort        +"// 排序  \n");
	    sb.append("parentId    ="+parentId    +"//父Id   \n");
	    sb.append("remarks     ="+remarks     +"//备注   \n");
	    sb.append("languageType="+languageType+"//语言   \n");
		return sb.toString();
    }
    
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getLanguageType() {
		return languageType;
	}
	public void setLanguageType(String languageType) {
		this.languageType = languageType;
	}
    
}
