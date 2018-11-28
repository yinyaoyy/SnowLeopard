package com.thinkgem.jeesite.api.dto.form;

/**
 * 提交site id form表单
 * @author kakasun
 * @create 2018-04-17 下午2:31
 */
public class SiteIdForm {

    String siteId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    public String toString() {
        return "SiteIdForm{" +
                "siteId='" + siteId + '\'' +
                '}';
    }
}
