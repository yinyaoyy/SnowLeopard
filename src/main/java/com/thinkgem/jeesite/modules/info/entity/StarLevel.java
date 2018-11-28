package com.thinkgem.jeesite.modules.info.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.api.dto.vo.common.DataEntityVo;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
public class StarLevel  extends DataEntity<StarLevel> {
	
	private String id;		// 业务id
	private String type;		//类型
	private String name;	// 姓名
	private Office office; //机构
	private Area area;  //地区
	private String evaluation; //评价值
	private int evaluationNum; //评价数量
	private String[] areaId;
	private String[] officeId;
	private String areaName;
	private String areaIds;
	private String officeName;
	private String officeIds;
	private String groupType; //分组类型

	public StarLevel() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="人员名称", type=0, align=1, sort=30)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(value="office.name", title="机构", type=0, align=1, sort=20)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(value="area.name", title="地区", type=0, align=1, sort=10)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@ExcelField(title="评价星级", type=0, align=1, sort=40)
	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	
	@ExcelField(title="评价数量", type=0, align=1, sort=50)
	public int getEvaluationNum() {
		return evaluationNum;
	}

	public void setEvaluationNum(int evaluationNum) {
		this.evaluationNum = evaluationNum;
	}
	
	public String[] getAreaId() {
		return areaId;
	}

	public void setAreaId(String[] areaId) {
		this.areaId = areaId;
	}

	public String[] getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String[] officeId) {
		this.officeId = officeId;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(String officeIds) {
		this.officeIds = officeIds;
	}
	
}
