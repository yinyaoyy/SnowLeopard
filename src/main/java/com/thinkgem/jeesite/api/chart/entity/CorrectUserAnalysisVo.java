package com.thinkgem.jeesite.api.chart.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.modules.info.entity.CorrectUserAnalysis;

public class CorrectUserAnalysisVo {
	private String idCard;//身份证号
	private Date analysisDate;		// 分析日期
	private String errorRate;		// 误差率
	private String type;		// 参数类型（字典info_correct_analysis_type）
	private String minValue;		// 最小值
	private String averageValue;		// 平均值
	private String maxValue;		// 最大值
	private String imageValue;		// 影像值
	private Date beginDate;
	private Date endDate;
	
	public CorrectUserAnalysisVo(CorrectUserAnalysis correctUserAnalysis) {
		this.idCard = correctUserAnalysis.getIdCard();
		this.analysisDate = correctUserAnalysis.getAnalysisDate();
		this.errorRate = correctUserAnalysis.getErrorRate();
		this.type = correctUserAnalysis.getType();
		this.minValue = correctUserAnalysis.getMinValue();
		this.averageValue = correctUserAnalysis.getAverageValue();
		this.maxValue = correctUserAnalysis.getMaxValue();
		this.imageValue = correctUserAnalysis.getImageValue();
		this.beginDate = correctUserAnalysis.getBeginDate();
		this.endDate = correctUserAnalysis.getBeginDate();
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getAnalysisDate() {
		return analysisDate;
	}
	public void setAnalysisDate(Date analysisDate) {
		this.analysisDate = analysisDate;
	}
	public String getErrorRate() {
		return errorRate;
	}
	public void setErrorRate(String errorRate) {
		this.errorRate = errorRate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMinValue() {
		return minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	public String getAverageValue() {
		return averageValue;
	}
	public void setAverageValue(String averageValue) {
		this.averageValue = averageValue;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public String getImageValue() {
		return imageValue;
	}
	public void setImageValue(String imageValue) {
		this.imageValue = imageValue;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
