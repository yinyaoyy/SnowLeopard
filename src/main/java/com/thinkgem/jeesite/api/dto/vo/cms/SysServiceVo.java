/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.api.dto.vo.cms;

import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.modules.appmange.entity.SysService;

/**
 * web服务Entity
 * @author wanglin
 * @version 2018-04-23
 */
public class SysServiceVo  {
	private String id;
	private String name;		// 服务名
	private String siteId;		// 站点id
	private String sort;//排序
	private List<Map<String,String>> siteList;
	
	
	public SysServiceVo() {
		super();
	}

	@Length(min=0, max=100, message="服务名长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="站点id长度必须介于 0 和 255 之间")
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Map<String, String>> getSiteList() {
		return siteList;
	}

	public void setSiteList(List<Map<String, String>> siteList) {
		this.siteList = siteList;
	}

	public SysServiceVo(SysService sysServiceVo) {
		this();
		if(sysServiceVo==null) {
			return;
		}
		this.id = sysServiceVo.getId();
		this.name = sysServiceVo.getName();
		this.siteId = sysServiceVo.getSiteId();
		this.sort = sysServiceVo.getSort();
	}

	@JsonIgnore
	public List<String> getSiteIdList() {
		List<String> siteIdList = Lists.newArrayList();
		if(siteId!=null){
			for (String ids : siteId.split(",")) {
				siteIdList.add(ids);
			}
		}
		return siteIdList;
	}
}