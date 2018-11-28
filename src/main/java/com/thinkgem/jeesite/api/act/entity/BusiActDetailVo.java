/**
 * 
 */
package com.thinkgem.jeesite.api.act.entity;

/**
 * 业务流程详细信息
 * @author 王鹏
 * @version 2018-07-14 14:49:24
 */
public class BusiActDetailVo {

	private String busiId;//业务表主键
	private String busiType;//业务类型(fy=法律援助;tj=人民调解)
	
	private String caseTitle;//案件标题
	private String caseContent;//案件内容
	private String applyTime;//申请时间(精确到秒)
	private String caseType;//案件类型
	private String areaName;//所属旗县
	private String caseRank;//严重等级
	private String applyUserName;//申请人姓名
	private String applyUserPhone;//申请人电话
	private String handleProgress;//办理进度
	
	
	@Override
	public String toString() {
		return "BusiActDetailVo [busiId=" + busiId + ", busiType=" + busiType + ", caseTitle=" + caseTitle
				+ ", caseContent=" + caseContent + ", applyTime=" + applyTime + ", caseType=" + caseType + ", areaName="
				+ areaName + ", caseRank=" + caseRank + ", applyUserName=" + applyUserName + ", applyUserPhone="
				+ applyUserPhone + ", handleProgress=" + handleProgress + "]";
	}
	
	public String getBusiId() {
		return busiId;
	}
	public void setBusiId(String busiId) {
		this.busiId = busiId;
	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public String getCaseTitle() {
		return caseTitle;
	}
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	public String getCaseContent() {
		return caseContent;
	}
	public void setCaseContent(String caseContent) {
		this.caseContent = caseContent;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getCaseRank() {
		return caseRank;
	}
	public void setCaseRank(String caseRank) {
		this.caseRank = caseRank;
	}
	public String getApplyUserName() {
		return applyUserName;
	}
	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}
	public String getApplyUserPhone() {
		return applyUserPhone;
	}
	public void setApplyUserPhone(String applyUserPhone) {
		this.applyUserPhone = applyUserPhone;
	}
	public String getHandleProgress() {
		return handleProgress;
	}
	public void setHandleProgress(String handleProgress) {
		this.handleProgress = handleProgress;
	}
	
}
