package com.thinkgem.jeesite.common.utils;

/**
 * 常量
 * @author kakasun
 * @create 2018-04-15 下午8:10
 */
public interface Constants {
    /**
     * 短信验证码，在session中的保存key
     */
    String MOBLE_CODE_KEY = "checkcode_key";
    /**
     * 保存手机号的key
     */
    String MOBLE_KEY = "mobile_key";
    /**
     * 保存重置密码认证的token的key
     */
    String CHANGE_PWD_FORGET_TOKEN = "changePwdForgetToken";
    /**
     * 在session中存登录设备类型的key
     */
    String SOURCE_DEVICE = "source_device_key";
    /**
     * 在header获取sessionＩｄ的key
     */
    String HEADER_SESSION_ID = "token";

    /**
     * 普通用户id
     */
    String PUBLIC_NOMAL_PEOPLE = "7c0a590cbaef4b4a949301b430a68a3c";
}
