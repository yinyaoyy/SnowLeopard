/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 社区矫正心理生理分析Entity
 * @author wanglin
 * @version 2018-07-28
 */
public class CorrectUserAnalysisVo extends DataEntity<CorrectUserAnalysisVo> {
	
	private static final long serialVersionUID = 1L;
	private String idCard;//身份证号
	private Date analysisDate;		// 分析日期
	private String errorRate;		// 误差率
	private List<CorrectUserAnalysis> list=Lists.newArrayList();
	
	public CorrectUserAnalysisVo() {
		super();
	}

	public CorrectUserAnalysisVo(String id){
		super(id);
	}
   
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
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

	public List<CorrectUserAnalysis> getList() {
		return list;
	}

	public void setList(List<CorrectUserAnalysis> list) {
		this.list = list;
	}

}