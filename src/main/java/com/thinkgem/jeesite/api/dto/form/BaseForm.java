package com.thinkgem.jeesite.api.dto.form;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinkgem.jeesite.common.utils.JsonUtils;

/**
 * 请求表单基类
 * @author kakasun
 * @create 2018-04-17 上午9:48
 */
public class BaseForm <T>{
	
    /**
     * 发起请求的时间戳
     */
    Long timestamp;
    /**
     * 请求的设备类型；浏览器：100；安卓：200；ios：300；
     */
    String tag;
    /**
     * 请求提交的参数 json字符串
     */
    String query;
    /**
     * json字符串解析的对象
     */
    T queryObj;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public T getQueryObj() {
        return queryObj;
    }

    /**
     * 解析提交的表单数据为对象
     * 可以支持自动转换，不需要单独调用
     * @param ref
     * @deprecated 在处理多层次json时无法完成处理，建议使用重载方法initQueryObj(Class<T> c)
     */
    @Deprecated
    public void initQueryObj(TypeReference<T> ref) {
        queryObj = JsonUtils.toObject(query, ref);
    }
    /**
     * 解析提交的表单数据为对象
     * 可以支持自动转换，不需要单独调用
     * @param ref
     */
    public T initQueryObj(Class<T> c) {
        queryObj = JSON.parseObject(query, c);
        return queryObj;
    }

    public String getQuery() {
        return query;
    }

	public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "BaseForm{" +
                "timestamp=" + timestamp +
                ", tag='" + tag + '\'' +
                ", query=" + query +
                '}';
    }
}
