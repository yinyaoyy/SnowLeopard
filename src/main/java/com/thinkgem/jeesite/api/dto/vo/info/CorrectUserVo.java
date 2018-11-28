/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.api.dto.vo.info;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.modules.info.entity.CorrectUser;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 社区矫正人员Entity
 * @version 2018-06-25
 */
public class CorrectUserVo {
	
	private String name;		// 姓名
	private Office office;		// office_id
	private String accusation;		// 罪名
	private String mainPenalty;		// 主刑
	private String mainPenaltyDesc;		// 主刑描述
	private String correctType;		// 矫正类别
	private String correctTypeDesc;		// 矫正类别描述
	private String responsibilityName;		// 责任人
	private String correctStatus;		// 矫正状态
	private String correctStatusDesc;		// 矫正状态描述
	private String correctLevel;		// 矫正等级
	private String correctLevelDesc;		// 矫正等级
	private String idCard;//身份证号
	private String sex;//性别
	private String phone;//电话
	private String age;//年龄
	public CorrectUserVo() {
		super();
	}
	public CorrectUserVo(CorrectUser  correctUser) {
		this.name=correctUser.getName();
		this.office=correctUser.getOffice();
		this.accusation=correctUser.getAccusation();
		this.mainPenalty=correctUser.getMainPenalty();
		Dict dict=null;
		dict=DictUtils.getDictByTypeValue("info_correct_user_penalty",correctUser.getMainPenalty());
		if(dict!=null) {
			this.mainPenaltyDesc=dict.getLabel();
		}
		this.correctType=correctUser.getCorrectType();
		dict=DictUtils.getDictByTypeValue("info_correct_user_type",correctUser.getCorrectType());
		if(dict!=null) {
			this.correctTypeDesc=dict.getLabel();
		}
		this.responsibilityName=correctUser.getResponsibilityName();
		this.correctStatus=correctUser.getCorrectStatus();
		dict=DictUtils.getDictByTypeValue("info_correct_user_state",correctUser.getCorrectStatus());
		if(dict!=null) {
			this.correctStatusDesc=dict.getLabel();
		}
		this.correctLevel=correctUser.getCorrectLevel();
		dict=DictUtils.getDictByTypeValue("info_correct_user_level",correctUser.getCorrectLevel());
		if(dict!=null) {
			this.correctStatusDesc=dict.getLabel();
		}
		this.idCard=correctUser.getIdCard();
		this.sex=correctUser.getSex();
		this.phone=correctUser.getPhone();
		this.age=correctUser.getAge();
	}
	@Length(min=0, max=20, message="姓名长度必须介于 0 和 20 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=200, message="罪名长度必须介于 0 和 200 之间")
	public String getAccusation() {
		return accusation;
	}

	public void setAccusation(String accusation) {
		this.accusation = accusation;
	}
	
	@Length(min=0, max=1, message="主刑长度必须介于 0 和 1 之间")
	public String getMainPenalty() {
		return mainPenalty;
	}

	public void setMainPenalty(String mainPenalty) {
		this.mainPenalty = mainPenalty;
	}
	
	@Length(min=0, max=1, message="矫正类别长度必须介于 0 和 1 之间")
	public String getCorrectType() {
		return correctType;
	}

	public void setCorrectType(String correctType) {
		this.correctType = correctType;
	}
	
	@Length(min=0, max=200, message="责任人长度必须介于 0 和 200 之间")
	public String getResponsibilityName() {
		return responsibilityName;
	}

	public void setResponsibilityName(String responsibilityName) {
		this.responsibilityName = responsibilityName;
	}
	
	@Length(min=0, max=1, message="矫正状态长度必须介于 0 和 1 之间")
	public String getCorrectStatus() {
		return correctStatus;
	}

	public void setCorrectStatus(String correctStatus) {
		this.correctStatus = correctStatus;
	}
	
	@Length(min=0, max=1, message="矫正等级长度必须介于 0 和 1 之间")
	public String getCorrectLevel() {
		return correctLevel;
	}

	public void setCorrectLevel(String correctLevel) {
		this.correctLevel = correctLevel;
	}
	public String getIdCard() {
		return idCard;
	}
	
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAge() {
	    return age;
	}
	public void setAge(String age) {
		this.age = age;
	}

	public String getMainPenaltyDesc() {
		return mainPenaltyDesc;
	}

	public void setMainPenaltyDesc(String mainPenaltyDesc) {
		this.mainPenaltyDesc = mainPenaltyDesc;
	}

	public String getCorrectTypeDesc() {
		return correctTypeDesc;
	}

	public void setCorrectTypeDesc(String correctTypeDesc) {
		this.correctTypeDesc = correctTypeDesc;
	}

	public String getCorrectStatusDesc() {
		return correctStatusDesc;
	}

	public void setCorrectStatusDesc(String correctStatusDesc) {
		this.correctStatusDesc = correctStatusDesc;
	}
	public String getCorrectLevelDesc() {
		return correctLevelDesc;
	}
	public void setCorrectLevelDesc(String correctLevelDesc) {
		this.correctLevelDesc = correctLevelDesc;
	}
	
}