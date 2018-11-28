/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 社区矫正心理生理分析Entity
 * @author wanglin
 * @version 2018-07-28
 */
public class CorrectUserAnalysis extends DataEntity<CorrectUserAnalysis> {
	
	private static final long serialVersionUID = 1L;
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
	public CorrectUserAnalysis() {
		super();
	}

	public CorrectUserAnalysis(String id){
		super(id);
	}
   
	@ExcelField(title="身份证", type=0, align=1, sort=10)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	@ExcelField(title="分析日期", type=0, align=1, sort=20)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getAnalysisDate() {
		return analysisDate;
	}

	public void setAnalysisDate(Date analysisDate) {
		this.analysisDate = analysisDate;
	}
	@ExcelField(title="误差率", type=0, align=1, sort=30)
	@Length(min=0, max=255, message="误差率长度必须介于 0 和 255 之间")
	public String getErrorRate() {
		return errorRate;
	}

	public void setErrorRate(String errorRate) {
		this.errorRate = errorRate;
	}
	@ExcelField(title="参数类型", type=0, align=1, sort=40,dictType="info_correct_analysis_type")
	@Length(min=0, max=2, message="参数类型（字典info_correct_analysis_type）长度必须介于 0 和 2 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@ExcelField(title="最小值", type=0, align=1, sort=50)
	@Length(min=0, max=255, message="最小值长度必须介于 0 和 255 之间")
	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	@ExcelField(title="平均值", type=0, align=1, sort=60)
	@Length(min=0, max=255, message="平均值长度必须介于 0 和 255 之间")
	public String getAverageValue() {
		return averageValue;
	}

	public void setAverageValue(String averageValue) {
		this.averageValue = averageValue;
	}
	@ExcelField(title="最大值", type=0, align=1, sort=70)
	@Length(min=0, max=255, message="最大值长度必须介于 0 和 255 之间")
	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	@ExcelField(title="影像值", type=0, align=1, sort=80)
	@Length(min=0, max=255, message="影像值长度必须介于 0 和 255 之间")
	public String getImageValue() {
		return imageValue;
	}

	public void setImageValue(String imageValue) {
		this.imageValue = imageValue;
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
	
}