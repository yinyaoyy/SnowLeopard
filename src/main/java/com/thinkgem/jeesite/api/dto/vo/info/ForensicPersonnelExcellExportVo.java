/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.api.dto.vo.info;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 司法鉴定员excell导出vo
 *
 * @author hejia
 * @version 2018-04-23
 */
public class ForensicPersonnelExcellExportVo {

    private String name;        // 姓名
    private String sex;        // 性别
    private String birthday;        // 出生日期
    private String ethnic;        // 名族
    private String politicalFace;        // 政治面貌
    private String education;        // 学历
    private String practisingTime;        // 入行年月，从业时间
    private String expertise;        // 业务专长
    private Office judicialAuthentication;        // 司法鉴定机构
    private String area;        // 所在地区
    private String licenseNumber;        // 执业证号
    private String licenseExpiryTime;        // 证书过期时间

    @ExcelField(title = "姓名", align = 2, sort = 5)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title = "性别:男-1；女-2", align = 2, sort = 10)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @ExcelField(title = "出生日期", align = 2, sort = 15)
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @ExcelField(title = "民族", align = 2, sort = 20)
    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    @ExcelField(title = "政治面貌", align = 2, sort = 25)
    public String getPoliticalFace() {
        return politicalFace;
    }

    public void setPoliticalFace(String politicalFace) {
        this.politicalFace = politicalFace;
    }

    @ExcelField(title = "学历", align = 2, sort = 30)
    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @ExcelField(title = "哪一年入行", align = 2, sort = 35)
    public String getPractisingTime() {
        return practisingTime;
    }

    public void setPractisingTime(String practisingTime) {
        this.practisingTime = practisingTime;
    }

    @ExcelField(title = "业务专长", align = 2, sort = 40)
    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    @ExcelField(value="judicialAuthentication.name",title = "所属鉴定机构", align = 2, sort = 45)
    public Office getJudicialAuthentication() {
        return judicialAuthentication;
    }

    public void setJudicialAuthentication(Office judicialAuthentication) {
        this.judicialAuthentication = judicialAuthentication;
    }

    @ExcelField(title = "所在区域", align = 2, sort = 50)
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @ExcelField(title = "执业证书号", align = 2, sort = 55)
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @ExcelField(title = "证书过期时间", align = 2, sort = 60)
    public String getLicenseExpiryTime() {
        return licenseExpiryTime;
    }

    public void setLicenseExpiryTime(String licenseExpiryTime) {
        this.licenseExpiryTime = licenseExpiryTime;
    }

    @Override
    public String toString() {
        return "ForensicPersonnelExcellExportVo{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", ethnic='" + ethnic + '\'' +
                ", politicalFace='" + politicalFace + '\'' +
                ", education='" + education + '\'' +
                ", practisingTime='" + practisingTime + '\'' +
                ", expertise='" + expertise + '\'' +
                ", judicialAuthentication='" + judicialAuthentication + '\'' +
                ", area='" + area + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", licenseExpiryTime='" + licenseExpiryTime + '\'' +
                '}';
    }
}