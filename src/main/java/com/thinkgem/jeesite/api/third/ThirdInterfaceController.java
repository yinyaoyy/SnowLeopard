package com.thinkgem.jeesite.api.third;

import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.third.CityNameVo;
import com.thinkgem.jeesite.api.dto.vo.third.CurrentWeatherVo;
import com.thinkgem.jeesite.api.third.dto.ReciveParamTranslateReqFormInterface;
import com.thinkgem.jeesite.api.third.dto.RespDataTranslateVoInterface;
import com.thinkgem.jeesite.api.third.exception.ThirdInterfaceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.WeatherCacheUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 第三方接口入口
 *
 * @author kakasun
 * @create 2018-04-20 下午2:31
 */
@RestController
@RequestMapping("thirdapi/100")
public class ThirdInterfaceController {

    private static final String FORM_PACKAGE_NAME = "com.thinkgem.jeesite.api.third.dto.reciveimpl.";
    private static final String DATA_PACKAGE_NAME = "com.thinkgem.jeesite.api.third.dto.respimpl.";
    private static final Logger log = LoggerFactory.getLogger(ThirdInterfaceController.class);

    @Resource(name = "restTemplate")
    RestTemplate restTemplate;
    @Resource(name = "thirdInterfaceProp")
    ReloadableResourceBundleMessageSource thirdInterfaceProp;

    /**
     * 转接第三方的请求
     *
     * @param third
     * @param no
     * @param form
     * @return
     */
    @RequestMapping("/{third}/{no}")
    public ServerResponse<Object> transfer(@PathVariable("third") String third,
                                   @PathVariable("no") String no, BaseForm form)
            throws UnsupportedEncodingException, ClassNotFoundException,
            IllegalAccessException, InstantiationException, ThirdInterfaceException, ExecutionException {

        String query = form.getQuery();
        //获取此接口配置key的前缀
        String keyPrefix = third + '.' + no + '.';
        Object data = null;
        switch (keyPrefix) {
            case "8010.20.":
                data = getServerResponseHeWeatherNow(query, keyPrefix);
                break;
            default:
                data = getServerResponseDefault(query, keyPrefix);

        }
        return ServerResponse.createBySuccess(data);
    }

    /**
     * 获取当前天气
     * @param query
     * @param keyPrefix
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws ThirdInterfaceException
     * @throws ExecutionException
     */
    private CurrentWeatherVo getServerResponseHeWeatherNow(String query, String keyPrefix) throws InstantiationException, IllegalAccessException, ClassNotFoundException, ThirdInterfaceException, ExecutionException {
        CurrentWeatherVo weather = null;
        try {
            weather = getCurrentWeatherVoFromCache(query);
            if ( null != weather) return weather;
        }catch (Exception e){
            e.printStackTrace();
        }
        weather = (CurrentWeatherVo)getServerResponseDefault(query, keyPrefix);
        WeatherCacheUtils.cacheKeyValue(weather.getCid(),weather);
        return weather;
    }

    /**
     * 从缓存中获取天气
     * @param query
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws ThirdInterfaceException
     * @throws ExecutionException
     */
    private CurrentWeatherVo getCurrentWeatherVoFromCache(String query) throws InstantiationException, IllegalAccessException, ClassNotFoundException, ThirdInterfaceException, ExecutionException {
        List<CityNameVo> cityNameList = (List<CityNameVo>) getServerResponseDefault(query, "8010.10.");
        CurrentWeatherVo weather = null;
        for (CityNameVo name : cityNameList) {
            weather = WeatherCacheUtils.getValueByKey(name.getCid());
            if (null != weather) {
                log.debug("从缓存中获取天气：{}",weather);
                return weather;
            }
        }
        return null;
    }

    /**
     * 默认的请求转发
     *
     * @param query
     * @param keyPrefix
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws ThirdInterfaceException
     */
    private Object getServerResponseDefault(String query, String keyPrefix) throws InstantiationException, IllegalAccessException, ClassNotFoundException, ThirdInterfaceException {
        MultiValueMap<String, String> param = getParamByQuery(keyPrefix,query);
        String appKeyStr = getByKey(keyPrefix + "appKey");
        //拼接请求数据
        try {
            if(StringUtils.isNotEmpty(appKeyStr)){
                String[] appKV = appKeyStr.split("=");
                param.add(appKV[0],appKV[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String urlKey = keyPrefix + "url";
        String urlStr = getByKey(urlKey);
        String method = getByKey(keyPrefix + "method");
        //获取此接口的请求方法
        ResponseEntity<String> responseEntity = null;
        if ("get".equalsIgnoreCase(method)) {//get请求
            StringBuffer sb = new StringBuffer();
//            Map<String,String> map = param.toSingleValueMap();
            Set<String> set = param.keySet();
            List<String> values = null;
            for(String s:set){
                values = param.get(s);
                for(String v:values) {
                    sb.append(s);
                    sb.append('=');
                    sb.append(v);
                    sb.append('&');
                }
            }
            urlStr = urlStr + '?' + sb.toString();
            responseEntity = restTemplate.getForEntity(urlStr, String.class);
        } else {//post请求
            HttpHeaders headers = new HttpHeaders();
            //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(param, headers);
            responseEntity = restTemplate.postForEntity(urlStr, requestEntity, String.class);
        }
        //获取响应体
        String body = responseEntity.getBody();
        //后去响应体的解析类
        String className = DATA_PACKAGE_NAME + getByKey(keyPrefix + "respimpl");
        RespDataTranslateVoInterface rdtvi = (RespDataTranslateVoInterface) Class.forName(className).newInstance();
        //创建数据展示对象
        Object vo = rdtvi.createVo(body);
        return vo;
    }

    /**
     * 解析query中参数为map
     * @param keyPrefix
     * @param query
     * @return
     * @throws ThirdInterfaceException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private MultiValueMap<String, String> getParamByQuery(String keyPrefix,String query) throws ThirdInterfaceException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = FORM_PACKAGE_NAME + getByKey(keyPrefix + "reciveimpl");
        ReciveParamTranslateReqFormInterface rptrfi = (ReciveParamTranslateReqFormInterface) Class.forName(className).newInstance();
        MultiValueMap<String, String> param = rptrfi.createVariables(query);
        return param;
    }

    /**
     * 根据key获取值
     *
     * @param urlKey
     * @return
     */
    private String getByKey(String urlKey) throws ThirdInterfaceException {
        String value = thirdInterfaceProp.getMessage(urlKey, null, null, null);
        if (null == value) {
            throw new ThirdInterfaceException("该第三方接口不存在");
        }
        return value;
    }


}
