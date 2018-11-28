/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.api.dto.vo.info;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 司法鉴定所excell导出vo
 *
 * @author hejia
 * @version 2018-04-23
 */
public class JudicialAuthenticationExcellExportVo {

    private String name;        // 名称
    private String address;        // 地址
    private String phone;        // 联系电话
    private String introduction;        // 机构简介
    private String area;        // 所在地区
    private String coordinate;        // 经纬度(经度在前)
    private String foundingTime;        // 成立时间
    private String businessExpertise;        // 业务专长
    private String personInCharge;        // 负责人
    private String teamSize;        // 团队规模
    private String practiceLicenseNum;        // 执业许可证号
    private String scopeOfBusiness;        // 业务范围
    private String officialWebAddress;        // 官网地址

    public JudicialAuthenticationExcellExportVo() {
    }

    @ExcelField(title = "名称", align = 2, sort = 5)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title = "地址", align = 2, sort = 10)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ExcelField(title = "联系电话", align = 2, sort = 15)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ExcelField(title = "机构简介", align = 2, sort = 20)
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @ExcelField(title = "所在地区", align = 2, sort = 20)
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @ExcelField(title = "经纬度，经度在前", align = 2, sort = 25)
    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    @ExcelField(title = "成立时间", align = 2, sort = 30)
    public String getFoundingTime() {
        return foundingTime;
    }

    public void setFoundingTime(String foundingTime) {
        this.foundingTime = foundingTime;
    }

    @ExcelField(title = "业务专长", align = 2, sort = 35)
    public String getBusinessExpertise() {
        return businessExpertise;
    }

    public void setBusinessExpertise(String businessExpertise) {
        this.businessExpertise = businessExpertise;
    }

    @ExcelField(title = "负责人", align = 2, sort = 40)
    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    @ExcelField(title = "团队规模", align = 2, sort = 45)
    public String getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(String teamSize) {
        this.teamSize = teamSize;
    }

    @ExcelField(title = "执业许可证编号", align = 2, sort = 50)
    public String getPracticeLicenseNum() {
        return practiceLicenseNum;
    }

    public void setPracticeLicenseNum(String practiceLicenseNum) {
        this.practiceLicenseNum = practiceLicenseNum;
    }

    @ExcelField(title = "业务范围", align = 2, sort = 55)
    public String getScopeOfBusiness() {
        return scopeOfBusiness;
    }

    public void setScopeOfBusiness(String scopeOfBusiness) {
        this.scopeOfBusiness = scopeOfBusiness;
    }

    @ExcelField(title = "官网 网址", align = 2, sort = 60)
    public String getOfficialWebAddress() {
        return officialWebAddress;
    }

    public void setOfficialWebAddress(String officialWebAddress) {
        this.officialWebAddress = officialWebAddress;
    }

    @Override
    public String toString() {
        return "JudicialAuthenticationExcellExportVo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", introduction='" + introduction + '\'' +
                ", area='" + area + '\'' +
                ", coordinate='" + coordinate + '\'' +
                ", foundingTime='" + foundingTime + '\'' +
                ", businessExpertise='" + businessExpertise + '\'' +
                ", personInCharge='" + personInCharge + '\'' +
                ", teamSize='" + teamSize + '\'' +
                ", practiceLicenseNum='" + practiceLicenseNum + '\'' +
                ", scopeOfBusiness='" + scopeOfBusiness + '\'' +
                ", officialWebAddress='" + officialWebAddress + '\'' +
                '}';
    }
}