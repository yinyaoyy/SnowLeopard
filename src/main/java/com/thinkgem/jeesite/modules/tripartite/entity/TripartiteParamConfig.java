/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tripartite.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 与第三方系统对接参数配置表Entity
 * @author 王鹏
 * @version 2018-06-23
 */
public class TripartiteParamConfig extends DataEntity<TripartiteParamConfig> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private TripartiteSystemConfig parent;		// 系统/接口
	private String name;		// 参数名称
	private String description;		// 参数说明
	private String valueType	;		//解析默认值的方式tripartite_config_value_type
	private String defaultValue;		//参数默认值
	
	public TripartiteParamConfig() {
		super();
	}

	public TripartiteParamConfig(String id){
		super(id);
	}

	@Override
	public String toString() {
		return "TripartiteParamConfig [type=" + type + ", parent=" + parent + ", name="
				+ name + ", description=" + description + ", valueType=" + valueType + ", defaultValue=" + defaultValue
				+ ", remarks=" + remarks + ", createBy=" + createBy + ", createDate=" + createDate + ", updateBy="
				+ updateBy + ", updateDate=" + updateDate + ", delFlag=" + delFlag + ", id=" + id + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TripartiteSystemConfig getParent() {
		return parent;
	}

	public void setParent(TripartiteSystemConfig parent) {
		this.parent = parent;
	}

	@Length(min=0, max=1, message="类型长度必须介于 0 和 300 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=1, message="类型长度必须介于 0 和 300 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Length(min=0, max=1, message="类型长度必须介于 0 和 1 之间")
	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	@Length(min=0, max=1, message="类型长度必须介于 0 和 1000 之间")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}