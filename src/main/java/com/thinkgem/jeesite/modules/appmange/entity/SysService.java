/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * web服务Entity
 * @author wanglin
 * @version 2018-04-23
 */
public class SysService extends DataEntity<SysService> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 服务名
	private String siteId;		// 站点id
	private String officeId;		// 人员机构id
	private String oaId;		// oa办公id
	private String sort;//排序
	private String type;
	public SysService() {
		super();
	}

	public SysService(String id){
		super(id);
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
	
	@Length(min=0, max=255, message="人员机构id长度必须介于 0 和 255 之间")
	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
	@Length(min=0, max=255, message="oa办公id长度必须介于 0 和 255 之间")
	public String getOaId() {
		return oaId;
	}

	public void setOaId(String oaId) {
		this.oaId = oaId;
	}
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	public void setSiteIdList(List<String> siteIdList) {
		siteIdList = Lists.newArrayList();
		for (int i = 0; i <siteIdList.size(); i++) {
			if(i==0){
				siteId=siteIdList.get(i);	
			}else{
				siteId+=siteIdList.get(i);
			}
		}
	}
	@JsonIgnore
	public List<String> getOfficeIdList() {
		List<String> officeIdList = Lists.newArrayList();
		if(officeId!=null){
			for (String ids : officeId.split(",")) {
				officeIdList.add(ids);
			}
		}
		return officeIdList;
	}
	public void setOfficeIdList(List<String> officeIdList) {
		officeIdList = Lists.newArrayList();
		for (int i = 0; i <officeIdList.size(); i++) {
			if(i==0){
				officeId=officeIdList.get(i);	
			}else{
				officeId+=officeIdList.get(i);
			}
		}
	}
	@JsonIgnore
	public List<String> getOaIdList() {
		List<String> oaIdList = Lists.newArrayList();
		if(oaId!=null){
			for (String ids : oaId.split(",")) {
				oaIdList.add(ids);
			}
		}
		return oaIdList;
	}
	public void setOaIdList(List<String> oaIdList) {
		oaIdList = Lists.newArrayList();
		for (int i = 0; i <oaIdList.size(); i++) {
			if(i==0){
				oaId=oaIdList.get(i);	
			}else{
				oaId+=oaIdList.get(i);
			}
		}
	}
}