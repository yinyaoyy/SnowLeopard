package com.thinkgem.jeesite.api.chart.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.api.dto.vo.common.DataEntityVo;
import com.thinkgem.jeesite.modules.sys.entity.Area;
/**
 * 大屏使用
 * @author del
 *
 */
@JsonInclude
public class LegalORMediationVo  extends DataEntityVo<LegalORMediationVo> {
	
	private String procDefKey;    //案件类型
	private String hasMongolDesc;//是否涉及蒙语说明
	private Date beginDate;        //开始时间
	private Date endDate;			//结束时间
	private Area area = new Area();	//所属旗县
	private String caseType;		//纠纷类型
	private String caseRank;		//严重等级
	private String caseTypeDesc;	//纠纷类型描述
	private String caseRankDesc;	//严重等级描述
	
	
	public String getProcDefKey() {
		return procDefKey;
	}
	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}
	public String getHasMongolDesc() {
		return hasMongolDesc;
	}
	public void setHasMongolDesc(String hasMongolDesc) {
		this.hasMongolDesc = hasMongolDesc;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
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
	}
	public String getCaseRank() {
		return caseRank;
	}
	public void setCaseRank(String caseRank) {
		this.caseRank = caseRank;
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
}
