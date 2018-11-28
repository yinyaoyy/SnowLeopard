package com.thinkgem.jeesite.common.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.thinkgem.jeesite.api.dto.vo.third.CurrentWeatherVo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证码和对应手机号缓存
 * @author kakasun
 * @create 2018-04-18 上午9:31
 */
public class WeatherCacheUtils {

    private static final LoadingCache<String,CurrentWeatherVo> weatherCacher;
    static {
        weatherCacher = CacheBuilder.newBuilder()
                .initialCapacity(100)
                .maximumSize(10000)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(new CacheLoader<String, CurrentWeatherVo>() {
                    @Override
                    public CurrentWeatherVo load(String s) throws Exception {
                        return new CurrentWeatherVo();
                    }
                });
    }

    public static void cacheKeyValue(String key,CurrentWeatherVo value){
        weatherCacher.put(key,value);
    }

    public static CurrentWeatherVo getValueByKey(String key) throws ExecutionException {
        CurrentWeatherVo value = weatherCacher.get(key);
        if(null == value.getCid()){
            return null;
        }
        return value;
    }
}
