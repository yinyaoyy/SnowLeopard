package com.thinkgem.jeesite.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkgem.jeesite.api.dto.form.SiteIdForm;

import java.text.SimpleDateFormat;

/**
 * json 工具
 *
 * @author kakasun
 * @create 2018-04-17 下午3:17
 */
public class JsonUtils {

    private static ObjectMapper mapper = null;
    private static final Object lock = new Object();

    static {
        //创建json对象
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    //  将对象转换为json字符串
    public static String toStr(Object object) {
        String json = null;
        try {
            synchronized (lock) {
                json = mapper.writeValueAsString(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    //  将对象转换为json字符串
    public String toStr(Object object, SimpleDateFormat dateFormat) {
        String json = null;
        try {
            synchronized (lock) {
                mapper.setDateFormat(dateFormat);
                json = mapper.writeValueAsString(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    //  将json字符串转换为对象
    public static  <T> T toObject(String json, TypeReference<T> ref) {
        T t = null;
        try {
            t = mapper.readValue(json, ref);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    //  将json字符串转换为对象
//    public static  Object toObject(String json, Object o) {
//        if(null == o){
//            return null;
//        }
//        try {
//            o = mapper.
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return t;
//    }

    public static void main(String[] arg0){
        SiteIdForm form = toObject("{\"siteId\":\"2\"}",new TypeReference<SiteIdForm>(){});
        System.out.println(form);
    }
}
