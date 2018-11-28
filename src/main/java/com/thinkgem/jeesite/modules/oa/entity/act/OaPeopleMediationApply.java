/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity.act;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thinkgem.jeesite.common.persistence.ActEntity;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookEvaluate;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 人民调解业务逻辑Entity
 * @author zhangqiang
 * @version 2018-05-24
 */
@JsonInclude
public class OaPeopleMediationApply extends ActEntity<OaPeopleMediationApply> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private String year;		// 年份
	private String yearNo;		// 年份编号
	private String accuserName;		// 申请人姓名
	private String accuserIdcard;		// 申请人身份证号
	private String accuserSex;		// 申请人性别
	private String accuserSexDesc;		// 申请人性别
	private Date accuserBirthday;		// 申请人出生日期
	private String accuserEthnic;		// 申请人民族
    private String accuserEthnicDesc;  // 申请人民族描述
	private Area accuserCounty = new Area();	// 申请人所在旗县
	private Area accuserTown = new Area();		// 申请人所在乡镇
	private String accuserOccupation;		// 申请人职业或职务
	private String accuserDomicile;		// 申请人户籍所在地
	private String accuserAddress;		// 申请人住所地
	private String accuserPostCode;		// 申请人邮政编码
	private String accuserPhone;		// 申请人联系电话
	private String defendantName;		// 被申请人姓名
	private String defendantIdcard;		// 被申请人身份证号
	private String defendantSex;		// 被申请人性别
	private String defendantSexDesc;		// 被申请人性别
	private Date defendantBirthday;		// 被申请人出生日期
	private String defendantEthnic;		// 被申请人民族
	private String defendantEthnicDesc;		// 被申请人民族
	private Area defendantCounty = new Area();	// 被申请人所在旗县
	private Area defendantTown = new Area();	// 被申请人所在乡镇
	private String defendantOccupation;		// 被申请人职业或职务
	private String defendantDomicile;		// 被申请人户籍所在地
	private String defendantAddress;		// 被申请人住所地
	private String defendantPostCode;		// 被申请人邮政编码
	private String defendantPhone;		// 被申请人联系电话
	private String caseTitle;		// 案件标题
	private String caseSituation;		// 案件及申请理由陈述
	private User mediator = new User();	// 指定调解员
	private Office peopleMediationCommittee = new Office(); //人民调解委员会
	private String caseFile; //案件相关文件
	
	private String status;	//申请表的状态，0默认，代表正在流程中，1同意了，2代表反对
	private String isEvaluate;//是否评价
	private GuestbookEvaluate guestbookEvaluate; //评价
	
	private Area caseCounty = new Area();	// 案件所在旗县
	private Area caseTown = new Area();		// 案件所在乡镇
	private String caseType;		// 纠纷类型
	private String caseTypeDesc;	//纠纷类型描述

	private String qrcode;			//二维码的地址
	
	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}

	public void setCaseTypeDesc(String caseTypeDesc) {
		this.caseTypeDesc = caseTypeDesc;
	}

	public Area getCaseCounty() {
		return caseCounty;
	}

	public void setCaseCounty(Area caseCounty) {
		this.caseCounty = caseCounty;
	}

	public Area getCaseTown() {
		return caseTown;
	}

	public void setCaseTown(Area caseTown) {
		this.caseTown = caseTown;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
		this.caseTypeDesc = DictUtils.getDictLabel(caseType, "oa_case_classify");
		
	}
	
	public String getStatus() {
		if(status==null){
			return "0";
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCaseFile() {
		return caseFile;
	}

	public void setCaseFile(String caseFile) {
		this.caseFile = caseFile;
	}

	public OaPeopleMediationApply() {
		super();
	}

	public OaPeopleMediationApply(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@Length(min=0, max=4, message="年份长度必须介于 0 和 4 之间")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	@Length(min=0, max=6, message="年份编号长度必须介于 0 和 6 之间")
	public String getYearNo() {
		return yearNo;
	}

	public void setYearNo(String yearNo) {
		this.yearNo = yearNo;
	}
	
//	@Length(min=1, max=200, message="申请人姓名长度必须介于 1 和 200 之间")
	public String getAccuserName() {
		return accuserName;
	}

	public void setAccuserName(String accuserName) {
		this.accuserName = accuserName;
	}
	
//	@Length(min=1, max=64, message="申请人身份证号长度必须介于 1 和 64 之间")
	public String getAccuserIdcard() {
		return accuserIdcard;
	}

	public void setAccuserIdcard(String accuserIdcard) {
		this.accuserIdcard = accuserIdcard;
	}
	
	@Length(min=0, max=1, message="申请人性别长度必须介于 0 和 1 之间")
	public String getAccuserSex() {
		return accuserSex;
	}

	public void setAccuserSex(String accuserSex) {
		this.accuserSex = accuserSex;
		this.accuserSexDesc = DictUtils.getDictLabel(accuserSex, "sex");
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getAccuserBirthday() {
		return accuserBirthday;
	}

	public void setAccuserBirthday(Date accuserBirthday) {
		this.accuserBirthday = accuserBirthday;
	}
	
	@Length(min=0, max=100, message="申请人民族长度必须介于 0 和 100 之间")
	public String getAccuserEthnic() {
		return accuserEthnic;
	}

	public void setAccuserEthnic(String accuserEthnic) {
		this.accuserEthnic = accuserEthnic;
		this.accuserEthnicDesc = DictUtils.getDictLabel(accuserEthnic, "ethnic");
	}
	
	
	public Area getAccuserCounty() {
		return accuserCounty;
	}

	public void setAccuserCounty(Area area) {
		this.accuserCounty = area;
	}
	
	
	public Area getAccuserTown() {
		return accuserTown;
	}

	public void setAccuserTown(Area area) {
		this.accuserTown = area;
	}
	
	@Length(min=0, max=64, message="申请人职业或职务长度必须介于 0 和 64 之间")
	public String getAccuserOccupation() {
		return accuserOccupation;
	}

	public void setAccuserOccupation(String accuserOccupation) {
		this.accuserOccupation = accuserOccupation;
	}
	
	@Length(min=0, max=64, message="申请人户籍所在地长度必须介于 0 和 64 之间")
	public String getAccuserDomicile() {
		return accuserDomicile;
	}

	public void setAccuserDomicile(String accuserDomicile) {
		this.accuserDomicile = accuserDomicile;
	}
	
	@Length(min=0, max=600, message="申请人住所地长度必须介于 0 和 600 之间")
	public String getAccuserAddress() {
		return accuserAddress;
	}

	public void setAccuserAddress(String accuserAddress) {
		this.accuserAddress = accuserAddress;
	}
	
	@Length(min=0, max=10, message="申请人邮政编码长度必须介于 0 和 10 之间")
	public String getAccuserPostCode() {
		return accuserPostCode;
	}

	public void setAccuserPostCode(String accuserPostCode) {
		this.accuserPostCode = accuserPostCode;
	}
	
	@Length(min=0, max=20, message="申请人联系电话长度必须介于 0 和 20 之间")
	public String getAccuserPhone() {
		return accuserPhone;
	}

	public void setAccuserPhone(String accuserPhone) {
		this.accuserPhone = accuserPhone;
	}
	
	@Length(min=0, max=200, message="被申请人姓名长度必须介于 0 和 200 之间")
	public String getDefendantName() {
		return defendantName;
	}

	public void setDefendantName(String defendantName) {
		this.defendantName = defendantName;
	}
	
	@Length(min=0, max=64, message="被申请人身份证号长度必须介于 0 和 64 之间")
	public String getDefendantIdcard() {
		return defendantIdcard;
	}

	public void setDefendantIdcard(String defendantIdcard) {
		this.defendantIdcard = defendantIdcard;
	}
	
	public String getDefendantSex() {
		return defendantSex;
	}

	public void setDefendantSex(String defendantSex) {
		this.defendantSex = defendantSex;
		this.defendantSexDesc = DictUtils.getDictLabel(defendantSex, "sex");
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDefendantBirthday() {
		return defendantBirthday;
	}

	public void setDefendantBirthday(Date defendantBirthday) {
		this.defendantBirthday = defendantBirthday;
	}
	
	@Length(min=0, max=100, message="被申请人民族长度必须介于 0 和 100 之间")
	public String getDefendantEthnic() {
		return defendantEthnic;
	}

	public void setDefendantEthnic(String defendantEthnic) {
		this.defendantEthnic = defendantEthnic;
		this.defendantEthnicDesc = DictUtils.getDictLabel(defendantEthnic, "ethnic");
	}
	
	public Area getDefendantCounty() {
		return defendantCounty;
	}

	public void setDefendantCounty(Area area) {
		this.defendantCounty = area;
	}
	
	
	public Area getDefendantTown() {
		return defendantTown;
	}

	public void setDefendantTown(Area area) {
		this.defendantTown = area;
	}
	
	@Length(min=0, max=64, message="被申请人职业或职务长度必须介于 0 和 64 之间")
	public String getDefendantOccupation() {
		return defendantOccupation;
	}

	public void setDefendantOccupation(String defendantOccupation) {
		this.defendantOccupation = defendantOccupation;
	}
	
	@Length(min=0, max=600, message="被申请人户籍所在地长度必须介于 0 和 600 之间")
	public String getDefendantDomicile() {
		return defendantDomicile;
	}

	public void setDefendantDomicile(String defendantDomicile) {
		this.defendantDomicile = defendantDomicile;
	}
	
	@Length(min=0, max=600, message="被申请人住所地长度必须介于 0 和 600 之间")
	public String getDefendantAddress() {
		return defendantAddress;
	}

	public void setDefendantAddress(String defendantAddress) {
		this.defendantAddress = defendantAddress;
	}
	
	@Length(min=0, max=10, message="被申请人邮政编码长度必须介于 0 和 10 之间")
	public String getDefendantPostCode() {
		return defendantPostCode;
	}

	public void setDefendantPostCode(String defendantPostCode) {
		this.defendantPostCode = defendantPostCode;
	}
	
	@Length(min=0, max=20, message="被申请人联系电话长度必须介于 0 和 20 之间")
	public String getDefendantPhone() {
		return defendantPhone;
	}

	public void setDefendantPhone(String defendantPhone) {
		this.defendantPhone = defendantPhone;
	}
	
	@Length(min=0, max=200, message="案件标题长度必须介于 0 和 200 之间")
	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	
	public String getCaseSituation() {
		return caseSituation;
	}

	public void setCaseSituation(String caseSituation) {
		this.caseSituation = caseSituation;
	}
	
	
	public User getMediator() {
		return mediator;
	}

	public void setMediator(User mediator) {
		this.mediator = mediator;
	}

	public Office getPeopleMediationCommittee() {
		return peopleMediationCommittee;
	}

	public void setPeopleMediationCommittee(Office peopleMediationCommittee) {
		this.peopleMediationCommittee = peopleMediationCommittee;
	}

	public String getAccuserEthnicDesc() {
		return accuserEthnicDesc;
	}

	public void setAccuserEthnicDesc(String accuserEthnicDesc) {
		this.accuserEthnicDesc = accuserEthnicDesc;
	}

	public String getDefendantSexDesc() {
		return defendantSexDesc;
	}

	public void setDefendantSexDesc(String defendantSexDesc) {
		this.defendantSexDesc = defendantSexDesc;
	}

	public String getDefendantEthnicDesc() {
		return defendantEthnicDesc;
	}

	public void setDefendantEthnicDesc(String defendantEthnicDesc) {
		this.defendantEthnicDesc = defendantEthnicDesc;
	}

	public String getAccuserSexDesc() {
		return accuserSexDesc;
	}

	public void setAccuserSexDesc(String accuserSexDesc) {
		this.accuserSexDesc = accuserSexDesc;
	}

	public String getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(String isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	public GuestbookEvaluate getGuestbookEvaluate() {
		return guestbookEvaluate;
	}

	public void setGuestbookEvaluate(GuestbookEvaluate guestbookEvaluate) {
		this.guestbookEvaluate = guestbookEvaluate;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
}