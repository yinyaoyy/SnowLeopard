/**
 * 
 */
package com.thinkgem.jeesite.api.dto.form;

import com.thinkgem.jeesite.common.persistence.BaseEntity;

/**
 * @author 王鹏
 * @version 2018-04-18 09:40:02
 */
public class AgencyForm extends BaseEntity<AgencyForm>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String categoryId;//机构分类id
	private String name;//机构名称
	private String areaId;//机构地区id
	private Integer pageNo;//分页页数
	private Integer pageSize;//分页条数
    private String isEvaluate;//是否查询评价值 false不，true查询
    private String townId;//乡镇id
    private String isMongolian;//精通蒙汉双语 0是1否
    private String isAidLawyer;//是否法援律师
    private String evaluation;//评价值
    private String officeId;
	public String getIsAidLawyer() {
		return isAidLawyer;
	}

	public void setIsAidLawyer(String isAidLawyer) {
		this.isAidLawyer = isAidLawyer;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("categoryId="+categoryId+"//机构分类id\n");
		sb.append("name="+name+"//机构名称\n");
		sb.append("areaId="+areaId+"//机构地区id\n");
		sb.append("id="+id+"//律所或是律师的id,查询详细信息使用\n");
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.common.persistence.BaseEntity#preInsert()
	 */
	@Override
	public void preInsert() {
		
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.common.persistence.BaseEntity#preUpdate()
	 */
	@Override
	public void preUpdate() {
		
	}
	
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(String isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	public String getTownId() {
		return townId;
	}

	public void setTownId(String townId) {
		this.townId = townId;
	}

	public String getIsMongolian() {
		return isMongolian;
	}

	public void setIsMongolian(String isMongolian) {
		this.isMongolian = isMongolian;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
}
