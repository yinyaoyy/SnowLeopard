package com.thinkgem.jeesite.common.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.thinkgem.jeesite.api.dto.bo.MobileAndCode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证码和对应手机号缓存
 * @author kakasun
 * @create 2018-04-18 上午9:31
 */
public class SMSCodeCacheUtils {

    private static final LoadingCache<String,MobileAndCode> smsCodeCache;
    static {
        smsCodeCache = CacheBuilder.newBuilder()
                .initialCapacity(1000)
                .maximumSize(10000)
                .expireAfterAccess(90, TimeUnit.SECONDS)
                .build(new CacheLoader<String, MobileAndCode>() {
                    @Override
                    public MobileAndCode load(String s) throws Exception {
                        return new MobileAndCode();
                    }
                });
    }

    public static void cacheKeyValue(String key,MobileAndCode value){
        smsCodeCache.put(key,value);
    }

    public static MobileAndCode getValueByKey(String key) throws ExecutionException {
        MobileAndCode value = smsCodeCache.get(key);
        if(null == value.getMobile()){
            return null;
        }
        return value;
    }
}
