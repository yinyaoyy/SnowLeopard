/**
 * 
 */
package com.thinkgem.jeesite.tripartite.entity;

import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 第三方调度任务信息实体
 * @author 王鹏
 * @version 2018-06-29 19:55:18
 */
public class TripartiteTaskEntity {

	private String id = "";//主键
	private String systemUrl = "";//系统地址
	private String systemDesc = "";//系统名称
	private String apiUrl = "";//接口地址
	private String apiName = "";//接口名称
	private String isPause = "";//是否暂停执行
	private String method = "";//请求方式
	private String beanName = "";//请求结果处理类
	//请求头
	private Map<String, String> hearders = Maps.newHashMap();
	//请求参数集合(list>1会触发循环调用)
	private List<Map<String, Object>> paramList = Lists.newArrayList();
	//当前请求参数信息
	private Map<String, Object> currParam = Maps.newHashMap();
	
	
	@Override
	public String toString() {
		return "TripartiteTaskEntity [id=" + id + ", isPause=" + isPause + ", systemDesc=" + systemDesc + ", apiName=" + apiName + ", systemUrl="
				+ systemUrl + ", apiUrl=" + apiUrl + ", method=" + method + ", beanName=" + beanName + ", hearders="
				+ hearders + ", paramList=" + paramList + ", currParam=" + currParam + "]";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsPause() {
		return isPause;
	}

	public void setIsPause(String isPause) {
		this.isPause = isPause;
	}

	public String getSystemDesc() {
		return systemDesc;
	}
	public void setSystemDesc(String systemDesc) {
		this.systemDesc = systemDesc;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public Map<String, Object> getCurrParam() {
		return currParam;
	}
	public void setCurrParam(Map<String, Object> currParam) {
		this.currParam = currParam;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getSystemUrl() {
		return systemUrl;
	}
	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}
	public String getApiUrl() {
		return apiUrl;
	}
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	public Map<String, String> getHearders() {
		return hearders;
	}
	public void setHearders(Map<String, String> hearders) {
		this.hearders = hearders;
	}
	public List<Map<String, Object>> getParamList() {
		return paramList;
	}
	public void setParamList(List<Map<String, Object>> paramList) {
		this.paramList = paramList;
	}
	
}
