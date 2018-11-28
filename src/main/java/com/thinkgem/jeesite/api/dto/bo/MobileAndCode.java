package com.thinkgem.jeesite.api.dto.bo;

/**
 * 手机号及其对应验证码
 * @author kakasun
 * @create 2018-04-18 上午9:36
 */
public class MobileAndCode {
    String mobile;
    String code;

    public static MobileAndCode build(String mobile,String code){
        MobileAndCode mac = new MobileAndCode();
        mac.setCode(code);
        mac.setMobile(mobile);
        return mac;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MobileAndCode{" +
                "mobile='" + mobile + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
