package com.thinkgem.jeesite.api.third.dto.respimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.api.dto.vo.third.CityNameVo;
import com.thinkgem.jeesite.api.third.dto.RespDataTranslateVoInterface;
import com.thinkgem.jeesite.api.third.exception.HeWeatherException;

import java.util.ArrayList;
import java.util.List;

/**
 * 和天气城市请求响应数据
 *
 * @author kakasun
 * @create 2018-04-20 下午3:35
 */
public class HeWeatherCitySerachRespDataTranslateVo implements RespDataTranslateVoInterface<List<CityNameVo>> {

    @Override
    public List<CityNameVo> createVo(String body) throws HeWeatherException {
        JSONObject jsonObject = JSONObject.parseObject(body);
        String HeWeather6Str = jsonObject.getString("HeWeather6");
        JSONArray array = JSONArray.parseArray(HeWeather6Str);
        if(null == array || array.size() < 1){
            throw new HeWeatherException("第三方接口请求失败");
        }
        jsonObject = array.getJSONObject(0);
        if(!"ok".equalsIgnoreCase(jsonObject.getString("status"))){
            throw new HeWeatherException("第三方接口请求失败");
        }
        String basicStr = jsonObject.getString("basic");
        array = JSONArray.parseArray(basicStr);
        if(null == array || array.size() < 1){
            throw new HeWeatherException("第三方接口请求失败");
        }
        int size = array.size();
        List<CityNameVo> cityNameVoList = new ArrayList<>();
        CityNameVo cityName = null;
        for(int i = 0;i < size;i++) {
            jsonObject = array.getJSONObject(i);
            cityName = new CityNameVo();
            cityName.setCid(jsonObject.getString("cid"));
            cityName.setName(jsonObject.getString("location"));
            cityNameVoList.add(cityName);
        }
        return cityNameVoList;
    }

}
