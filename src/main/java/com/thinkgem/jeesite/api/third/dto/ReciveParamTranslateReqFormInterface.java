package com.thinkgem.jeesite.api.third.dto;

import org.springframework.util.MultiValueMap;


/**
 * ${DESCRIPTION}
 *
 * @author kakasun
 * @create 2018-04-22 上午8:36
 */
public interface ReciveParamTranslateReqFormInterface {

    /**
     * 根据提交的query参数适配请求第三方接口的参数
     * @param query
     * @return
     */
    MultiValueMap<String, String> createVariables(String query);
}
