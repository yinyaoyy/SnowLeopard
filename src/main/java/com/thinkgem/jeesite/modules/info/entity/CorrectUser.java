/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.entity;

import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 社区矫正人员Entity
 * @author liujiangling
 * @version 2018-06-25
 */
public class CorrectUser extends DataEntity<CorrectUser> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private Office office;		// office_id
	private String accusation;		// 罪名
	private String mainPenalty;		// 主刑
	private String correctType;		// 矫正类别
	private String responsibilityName;		// 责任人
	private String correctStatus;		// 矫正状态
	private String correctLevel;		// 矫正等级
	private String idCard;//身份证号
	private String sex;//性别
	private String phone;//电话
	private String age;//年龄
	public CorrectUser() {
		super();
	}

	public CorrectUser(String id){
		super(id);
	}
	
	@ExcelField(title="姓名", type=0, align=1, sort=10)
	@Length(min=0, max=20, message="姓名长度必须介于 0 和 20 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(value="office.name",title="所在司法所", type=0, align=1, sort=20)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="罪名", type=0, align=1, sort=30)
	@Length(min=0, max=200, message="罪名长度必须介于 0 和 200 之间")
	public String getAccusation() {
		return accusation;
	}

	public void setAccusation(String accusation) {
		this.accusation = accusation;
	}
	
	@ExcelField(title="主刑", type=0, align=1, sort=40 ,dictType="info_correct_user_penalty")
	@Length(min=0, max=1, message="主刑长度必须介于 0 和 1 之间")
	public String getMainPenalty() {
		return mainPenalty;
	}

	public void setMainPenalty(String mainPenalty) {
		this.mainPenalty = mainPenalty;
	}
	
	@ExcelField(title="矫正类别", type=0, align=1, sort=50 ,dictType="info_correct_user_type")
	@Length(min=0, max=1, message="矫正类别长度必须介于 0 和 1 之间")
	public String getCorrectType() {
		return correctType;
	}

	public void setCorrectType(String correctType) {
		this.correctType = correctType;
	}
	
	@ExcelField(title="责任人", type=0, align=1, sort=60)
	@Length(min=0, max=200, message="责任人长度必须介于 0 和 200 之间")
	public String getResponsibilityName() {
		return responsibilityName;
	}

	public void setResponsibilityName(String responsibilityName) {
		this.responsibilityName = responsibilityName;
	}
	
	@ExcelField(title="矫正状态", type=0, align=1, sort=70 ,dictType="info_correct_user_state")
	@Length(min=0, max=1, message="矫正状态长度必须介于 0 和 1 之间")
	public String getCorrectStatus() {
		return correctStatus;
	}

	public void setCorrectStatus(String correctStatus) {
		this.correctStatus = correctStatus;
	}
	
	@ExcelField(title="矫正等级", type=0, align=1, sort=80 ,dictType="info_correct_user_level")
	@Length(min=0, max=1, message="矫正等级长度必须介于 0 和 1 之间")
	public String getCorrectLevel() {
		return correctLevel;
	}

	public void setCorrectLevel(String correctLevel) {
		this.correctLevel = correctLevel;
	}
	@ExcelField(title="身份证号", type=0, align=1, sort=15)
	public String getIdCard() {
		return idCard;
	}
	
	public void setIdCard(String idCard) {
		Map<String,String> map = DateUtils.getBirAgeSex(idCard);
		this.sex = map.get("sexCode");
		this.idCard = idCard;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@ExcelField(title="手机号", type=0, align=1, sort=17)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAge() {
		if(StringUtils.isNotBlank(idCard)) {
			Map<String,String> map = DateUtils.getBirAgeSex(idCard);
			this.age =  map.get("age");
		}
	    return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
}