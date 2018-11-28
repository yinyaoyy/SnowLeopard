package com.thinkgem.jeesite.api.third.dto.reciveimpl;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.api.third.dto.ReciveParamTranslateReqFormInterface;
import com.thinkgem.jeesite.common.utils.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


/**
 * 根据经纬度搜索城市的请求参数适配
 *
 * @author kakasun
 * @create 2018-04-22 上午8:39
 */
public class HeWeatherCitySerachReqFormTranslate implements ReciveParamTranslateReqFormInterface {

    @Override
    public MultiValueMap<String, String> createVariables(String query) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        if(StringUtils.isEmpty(query)){
            return param;
        }
        JSONObject jsonObject = JSONObject.parseObject(query);
        param.add("location",jsonObject.getString("location"));
        return param;
    }
}
