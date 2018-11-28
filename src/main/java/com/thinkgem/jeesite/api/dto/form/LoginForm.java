package com.thinkgem.jeesite.api.dto.form;

/**
 * 登录表单
 *
 * @author kakasun
 * @create 2018-04-18 下午3:08
 */
public class LoginForm {
    /**
     * 用户名
     */
    String username;
    /**
     * 密码
     */
    String password;
    /**
     * 记住我
     */
    boolean rememberMe;
    /**
     * 验证码
     */
    String validateCode;
    /**
     * 当前国际化语言
     */
    String langCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }
    public boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    @Override
    public String toString() {
        return "LoginForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rememberMe=" + rememberMe +
                ", validateCode='" + validateCode + '\'' +
                ", langCode='" + langCode + '\'' +
                '}';
    }
}
