package com.thinkgem.jeesite.api.dto.vo.info;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.info.entity.InfoLegalServiceOffice;

import java.text.SimpleDateFormat;

/**
 * 基层法律服务所
 *
 * @author kakasun
 * @create 2018-04-26 上午10:24
 */
public class LegalServiceOfficeExcelExportVo {

    String name;        // 名称
    String approvalDate;        // 批准日期
    String personInCharge;        // 负责人
    String practiceLicenseNum;        // 执业许可证号
    String practiceLicenseExpiryTime;        // 执业许可证过期时间
//    String occupationalState;        // 执业状态
    String licenseNumber;        // 组织机构代码证编号
    String approvalNumber;        // 批准文号
   
    String competentAuthoritiesName;        // 主管机关名称
    String address;        // 地址
    String phone;        // 联系电话
    String fax;        // 传真
    String officialWebAddress;        // 官网地址
    String introduction;        // 机构简介
    String area;        // 所在地区
    String coordinate;        // 经纬度(经度在前)

    public LegalServiceOfficeExcelExportVo() {
    }

    public LegalServiceOfficeExcelExportVo(InfoLegalServiceOffice legalServiceOffice, SimpleDateFormat sdf) {
        name = legalServiceOffice.getName();
        personInCharge = legalServiceOffice.getPersonInCharge();
        practiceLicenseNum = legalServiceOffice.getPracticeLicenseNum();
        if(legalServiceOffice.getPracticeLicenseExpiryTime()!=null){
        	practiceLicenseExpiryTime = sdf.format(legalServiceOffice.getPracticeLicenseExpiryTime());
        }
        licenseNumber = legalServiceOffice.getLicenseNumber();
        approvalNumber = legalServiceOffice.getApprovalNumber();
        if(legalServiceOffice.getApprovalDate()!=null){
        	   approvalDate = sdf.format(legalServiceOffice.getApprovalDate());
        }
        competentAuthoritiesName = legalServiceOffice.getCompetentAuthoritiesName();
        address = legalServiceOffice.getAddress();
        phone = legalServiceOffice.getPhone();
        fax = legalServiceOffice.getFax();
        officialWebAddress = legalServiceOffice.getOfficialWebAddress();
        introduction = legalServiceOffice.getIntroduction();
        if(legalServiceOffice.getArea()!=null){
        	area = legalServiceOffice.getArea().getName();
        }
        coordinate = legalServiceOffice.getCoordinate();
    }

    @ExcelField(title = "名称", align = 2, sort = 5)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title = "负责人", align = 2, sort = 10)
    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    @ExcelField(title = "执业许可证号", align = 2, sort = 20)
    public String getPracticeLicenseNum() {
        return practiceLicenseNum;
    }

    public void setPracticeLicenseNum(String practiceLicenseNum) {
        this.practiceLicenseNum = practiceLicenseNum;
    }

    @ExcelField(title = "执业许可证过期时间", align = 2, sort = 30)
    public String getPracticeLicenseExpiryTime() {
        return practiceLicenseExpiryTime;
    }

    public void setPracticeLicenseExpiryTime(String practiceLicenseExpiryTime) {
        this.practiceLicenseExpiryTime = practiceLicenseExpiryTime;
    }
/*
    @ExcelField(title = "执业状态", align = 2, sort = 40)
    public String getOccupationalState() {
        return occupationalState;
    }

    public void setOccupationalState(String occupationalState) {
        this.occupationalState = occupationalState;
    }*/

    @ExcelField(title = "组织机构代码证编号", align = 2, sort = 50)
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @ExcelField(title = "批准文号", align = 2, sort = 60)
    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }

    @ExcelField(title = "批准日期", align = 2, sort = 70)
    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    @ExcelField(title = "主管机关名称", align = 2, sort = 80)
    public String getCompetentAuthoritiesName() {
        return competentAuthoritiesName;
    }

    public void setCompetentAuthoritiesName(String competentAuthoritiesName) {
        this.competentAuthoritiesName = competentAuthoritiesName;
    }

    @ExcelField(title = "地址", align = 2, sort = 90)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ExcelField(title = "联系电话", align = 2, sort = 100)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ExcelField(title = "传真", align = 2, sort = 110)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @ExcelField(title = "官网地址", align = 2, sort = 120)
    public String getOfficialWebAddress() {
        return officialWebAddress;
    }

    public void setOfficialWebAddress(String officialWebAddress) {
        this.officialWebAddress = officialWebAddress;
    }

    @ExcelField(title = "机构简介", align = 2, sort = 130)
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @ExcelField(title = "所在地区", align = 2, sort = 140)
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @ExcelField(title = "经纬度(经度在前)", align = 2, sort = 150)
    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "LegalServiceOfficeExcelExportVo{" +
                "name='" + name + '\'' +
                ", personInCharge='" + personInCharge + '\'' +
                ", practiceLicenseNum='" + practiceLicenseNum + '\'' +
                ", practiceLicenseExpiryTime='" + practiceLicenseExpiryTime + '\'' +
//                ", occupationalState='" + occupationalState + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", approvalNumber='" + approvalNumber + '\'' +
                ", approvalDate='" + approvalDate + '\'' +
                ", competentAuthoritiesName='" + competentAuthoritiesName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", officialWebAddress='" + officialWebAddress + '\'' +
                ", introduction='" + introduction + '\'' +
                ", area='" + area + '\'' +
                ", coordinate='" + coordinate + '\'' +
                '}';
    }
}
