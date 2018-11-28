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
 * 接口:大屏查询律师信息
 * @author 王鹏
 * @version 2018-06-10 20:27:27
 */
@JsonInclude
public class LawyerVo extends DataEntityVo<LawyerVo> {

	public static final String DEL_FLAG_NORMAL = BaseEntity.DEL_FLAG_NORMAL;
	
	private String id;
	private Page<LawyerVo> page;
	private Area area = new Area();
	private String licenseType;//执业类别，字典lawyer_license_type
	private String name;//律师姓名
	private String imageUrl;//照片
	private String ethnic;//民族
	private Integer acceptCount;//受理案件数量
	
	private String licenseTypeDesc;
	private String ethnicDesc;

	
	public Integer getAcceptCount() {
		return acceptCount;
	}
	public void setAcceptCount(Integer acceptCount) {
		this.acceptCount = acceptCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JsonIgnore
	public Page<LawyerVo> getPage() {
		return page;
	}
	public void setPage(Page<LawyerVo> page) {
		this.page = page;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
		this.licenseTypeDesc = DictUtils.getDictLabel(licenseType, "lawyer_license_type");
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
	public String getEthnic() {
		return ethnic;
	}
	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
		this.ethnicDesc = DictUtils.getDictLabel(ethnic, "ethnic");
	}
	public String getLicenseTypeDesc() {
		return licenseTypeDesc;
	}
	public void setLicenseTypeDesc(String licenseTypeDesc) {
		this.licenseTypeDesc = licenseTypeDesc;
	}
	public String getEthnicDesc() {
		return ethnicDesc;
	}
	public void setEthnicDesc(String ethnicDesc) {
		this.ethnicDesc = ethnicDesc;
	}
	
}
