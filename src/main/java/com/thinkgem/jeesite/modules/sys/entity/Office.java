/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.common.persistence.TreeEntity;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * 机构Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Office extends TreeEntity<Office> {

	private static final long serialVersionUID = 1L;
	private Area area;		// 归属区域
	private String code; 	// 机构编码
	private String type; 	// 机构类型（1：部门；2：科室；3：小组）
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	private String useable;//是否可用
	private User primaryPerson;//主负责人
	private User deputyPerson;//副负责人
	private List<String> childDeptList;//快速添加子科室
	
	public Office(){
		super();
	}

	public Office(String id){
		super(id);
	}
	/**
	 * 用于自动注册机构使用
	 * @param name 必填
	 * @param pid 必填，上级id，使用常量指定
	 * @param areaId 选填，地区id
	 * @param code 选填，机构编码
	 * @throws BusinessException
	 */
	public Office(String name, String pid, Area area, String code) throws BusinessException{
		super();
		if(StringUtils.isBlank(name) || StringUtils.isBlank(pid)) {
			throw new BusinessException("机构名称或上级机构id不存在");
		}
		this.name = name;
		this.parent = new Office(pid);
		this.area = area;
		this.code = code;
		this.type = "2";//相当于科室
		this.grade = "3";//在司法局、相应机构下
		this.useable = "1";//可用
	}
	
	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public User getPrimaryPerson() {
		return primaryPerson;
	}

	public void setPrimaryPerson(User primaryPerson) {
		this.primaryPerson = primaryPerson;
	}

	public User getDeputyPerson() {
		return deputyPerson;
	}

	public void setDeputyPerson(User deputyPerson) {
		this.deputyPerson = deputyPerson;
	}

	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	@NotNull
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=1, max=1)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min=0, max=200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	@Override
	public String toString() {
		return name;
	}
}