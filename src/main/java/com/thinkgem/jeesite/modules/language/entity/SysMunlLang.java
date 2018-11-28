/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.language.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * languageEntity
 * @author language
 * @version 2017-09-01
 */
public class SysMunlLang extends DataEntity<SysMunlLang> {
	
	private static final long serialVersionUID = 1L;
	private String langKey;		// 语言主键
	private String langContext;		// 内容
	private String langCode;		// 语言
	private String createName;		// 创建人姓名
	private String updateName;		// 更新人姓名
	private String description;     //描述
	private String operationType; //语言归属
	private String attributeType;//属性分类
	private String languageAscription;//归属页面 
	@ExcelField(title="语言描述", align=2, sort=60)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SysMunlLang() {
		super();
	}

	public SysMunlLang(String id){
		super(id);
	}

	@Length(min=0, max=255, message="语言主键长度必须介于 0 和 255 之间")
	@ExcelField(title="语言Key", align=2, sort=1)
	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}
	
	@Length(min=0, max=255, message="内容长度必须介于 0 和 255 之间")
	@ExcelField(title="内容", align=2, sort=40)
	public String getLangContext() {
		return langContext;
	}

	public void setLangContext(String langContext) {
		this.langContext = langContext;
	}
	
	@Length(min=0, max=255, message="语言长度必须介于 0 和 255 之间")
	@ExcelField(title="语言", align=2, sort=50)
	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	
	@Length(min=0, max=255, message="创建人姓名长度必须介于 0 和 255 之间")
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}
	
	@Length(min=0, max=255, message="更新人姓名长度必须介于 0 和 255 之间")
	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	@ExcelField(title="平台归属", align=2, sort=10)
	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	@ExcelField(title="属性分类", align=2, sort=20)
	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	@ExcelField(title="语言归属", align=2, sort=30)
	public String getLanguageAscription() {
		return languageAscription;
	}

	public void setLanguageAscription(String languageAscription) {
		this.languageAscription = languageAscription;
	}
	
}