package com.thinkgem.jeesite.api.third.dto.respimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.api.dto.vo.third.CurrentWeatherVo;
import com.thinkgem.jeesite.api.third.dto.RespDataTranslateVoInterface;
import com.thinkgem.jeesite.api.third.exception.HeWeatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 和天气城市请求响应数据
 *
 * @author kakasun
 * @create 2018-04-20 下午3:35
 */
public class HeWeatherCurrentWeatherRespDataTranslateVo implements RespDataTranslateVoInterface<CurrentWeatherVo > {

    private static final String COND_ICON_PREFIX = "/static/cond_icon_heweather/";
    private static final Logger log = LoggerFactory.getLogger(HeWeatherCurrentWeatherRespDataTranslateVo.class);

    @Override
    public CurrentWeatherVo createVo(String body) throws HeWeatherException {
        log.debug("第三方接口返回json：{}",body);
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
        String nowStr = jsonObject.getString("now");
        JSONObject basic = JSONObject.parseObject(basicStr);
        CurrentWeatherVo currentWeather = new CurrentWeatherVo();
        currentWeather.setCid(basic.getString("cid"));
        currentWeather.setLocation(basic.getString("location"));
        currentWeather.setParent_city(basic.getString("parent_city"));
        currentWeather.setAdmin_area(basic.getString("admin_area"));
        currentWeather.setCnty(basic.getString("cnty"));
        currentWeather.setTz(basic.getString("tz"));

        JSONObject nowWeather = JSONObject.parseObject(nowStr);
        currentWeather.setFl(nowWeather.getString("fl"));
        currentWeather.setTmp(nowWeather.getString("tmp"));
        currentWeather.setCond_code(nowWeather.getString("cond_code"));
        currentWeather.setCond_txt(nowWeather.getString("cond_txt"));
        currentWeather.setWind_deg(nowWeather.getString("wind_deg"));
        currentWeather.setWind_dir(nowWeather.getString("wind_dir"));
        currentWeather.setWind_sc(nowWeather.getString("wind_sc"));
        currentWeather.setWind_spd(nowWeather.getString("wind_spd"));
        currentWeather.setHum(nowWeather.getString("hum"));
        currentWeather.setPcpn(nowWeather.getString("pcpn"));
        currentWeather.setPres(nowWeather.getString("pres"));
        currentWeather.setVis(nowWeather.getString("vis"));
        currentWeather.setCloud(nowWeather.getString("cloud"));

        currentWeather.setCond_icon(COND_ICON_PREFIX + currentWeather.getCond_code() + ".png");
        return currentWeather;
    }

}
