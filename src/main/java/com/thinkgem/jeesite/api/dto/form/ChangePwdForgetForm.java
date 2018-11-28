package com.thinkgem.jeesite.api.dto.form;

/**
 * 忘记密码重置密码
 *
 * @author kakasun
 * @create 2018-04-18 上午11:36
 */
public class ChangePwdForgetForm {
    /**
     * 短信验证码token
     */
    String smsToken;
    /**
     * 密码
     */
    String pwd;
    /**
     * 验证码
     */
    String code;

    public String getSmsToken() {
        return smsToken;
    }

    public void setSmsToken(String smsToken) {
        this.smsToken = smsToken;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ChangePwdForgetForm{" +
                "smsToken='" + smsToken + '\'' +
                ", pwd='" + pwd + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
