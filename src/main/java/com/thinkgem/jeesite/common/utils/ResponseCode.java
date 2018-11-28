package com.thinkgem.jeesite.common.utils;

/**
 * 返回编码
 * @author kakasun
 * @create 2018-03-11 上午8:28
 */
public enum ResponseCode {

        SUCCESS(0,"SUCCESS"),
        ERROR(500,"服务器异常"),
        INVALID_PARAMETER(1000,"参数无效或缺失"),
        INVALID_API_KEY(1001,"API key无效"),
        INVALIDCALL_ID_PARAMETER(1003,"Call_id参数无效或已被使用"),
        INCORRECT_SIGNATURE(1004,"签名无效"),
        TOO_MANY_PARAMETERS(1005,"参数过多"),
        SERVICE_TEMPORARILY_UNAVAILABLE(1006,"后端服务暂时不可用"),
        UNSUPPORTED_OPENAPI_METHOD(1007,"Open API接口不被支持"),
        REQUEST_LIMIT_REACHED(1008,"应用对open api接口的调用请求数达到上限"),
        CODE_INVALID(1009,"验证码错误或过期"),
        NO_PERMISSION_TO_ACCESS_DATA(2000,"没有权限访问数据"),
        NEED_LOGIN(403000,"用户未登录"),
        PASSWORD_REEOR(403001,"账号密码不正确"),
        LOGINNAME_NOT_EXISTS(403002,"账号不存在，请注册"),
        PASSWORD_WRONG(403003,"密码不正确"),
        IDENTITY_ERROR_NORMAL(403004,"请使用“服务人员账号登录”"),
        IDENTITY_ERROR_WORKER(403005,"请使用“社会公众账号登录”"),
        ACT_PARAM_ERROR(9000,"流程参数不全"),
        BUSINESS_ID(9001,"业务主键为空"),
        BUSINESS_ERROR(9003,"业务操作错误"),
        MEDIATION_ID_PROVE(9004,"登录用户与人民调解申请无关！"),
        MEDIATION_NOT_LEADER(9005,"当前地区无司法所所长！"),
        ARTICLE_COMMENT_NOT_STAFF(9006,"该文章所属机构科室下无工作人员！");
        private final int code;
        private final String desc;

        ResponseCode(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode(){
            return code;
        }

        public String getDesc(){
            return desc;
        }

}
