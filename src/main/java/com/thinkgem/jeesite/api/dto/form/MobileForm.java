package com.thinkgem.jeesite.api.dto.form;

/**
 * 手机号表单
 * @author kakasun
 * @create 2018-04-18 上午9:16
 */
public class MobileForm {
    /**
     * 手机号
     */
    String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MobileForm{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}
