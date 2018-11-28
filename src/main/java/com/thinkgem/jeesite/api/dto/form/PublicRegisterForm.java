package com.thinkgem.jeesite.api.dto.form;

/**
 * 公众注册提交表单
 * @author kakasun
 * @create 2018-04-18 上午10:14
 */
public class PublicRegisterForm {

    /**
     * 短信验证码token
     */
    String smsToken;
    /**
     * 真实姓名
     */
    String name;
    /**
     * 身份证号
     */
    String papernum;
    /**
     * 密码
     */
    String pwd;
    /**
     * 验证码
     */
    String code;
    /**
     * 密保问题
     */
    String question;
    /**
     * 密保问题答案
     */
    String answer;

    public String getSmsToken() {
        return smsToken;
    }

    public void setSmsToken(String smsToken) {
        this.smsToken = smsToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPapernum() {
        return papernum;
    }

    public void setPapernum(String papernum) {
        this.papernum = papernum;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "PublicRegisterForm{" +
                "smsToken='" + smsToken + '\'' +
                ", name='" + name + '\'' +
                ", papernum='" + papernum + '\'' +
                ", pwd='" + pwd + '\'' +
                ", code='" + code + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
