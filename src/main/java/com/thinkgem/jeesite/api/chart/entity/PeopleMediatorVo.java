/**
 * 
 */
package com.thinkgem.jeesite.api.chart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.api.dto.vo.common.DataEntityVo;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * @author 王鹏
 * @version 2018-06-13 15:01:05
 */
@JsonInclude
public class PeopleMediatorVo extends DataEntityVo<PeopleMediatorVo> {

	public static final String DEL_FLAG_NORMAL = BaseEntity.DEL_FLAG_NORMAL;
	
	private Page<PeopleMediatorVo> page;//分页数据
	private Area area;//地区数据
	private String name;//调解员姓名
	private String imageUrl;//照片
	private String mediatorType;//调解员类型,字典为mediator_type
	private Integer acceptCount;//处理案件数量
	
	private String mediatorTypeDesc;//调解员类型说明
	
	@JsonIgnore
	public Page<PeopleMediatorVo> getPage() {
		return page;
	}
	public void setPage(Page<PeopleMediatorVo> page) {
		this.page = page;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getMediatorType() {
		return mediatorType;
	}
	public void setMediatorType(String mediatorType) {
		this.mediatorType = mediatorType;
		this.mediatorTypeDesc = DictUtils.getDictLabel(mediatorType, "mediator_type");
	}
	public Integer getAcceptCount() {
		return acceptCount;
	}
	public void setAcceptCount(Integer acceptCount) {
		this.acceptCount = acceptCount;
	}
	public String getMediatorTypeDesc() {
		return mediatorTypeDesc;
	}
	public void setMediatorTypeDesc(String mediatorTypeDesc) {
		this.mediatorTypeDesc = mediatorTypeDesc;
	}
}
