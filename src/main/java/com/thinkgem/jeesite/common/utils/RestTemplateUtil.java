/**
 * 
 */
package com.thinkgem.jeesite.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;

/**
 * RestTemplate风格请求处理类
 * @author 王鹏
 * @version 2018-06-29 16:02:08
 */
public class RestTemplateUtil {

	/**
	 * 日志输出
	 */
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);
	/**
	 * RestTemplate请求实现类
	 */
	private static RestTemplate restRequest = new RestTemplate();
	
	/**
	 * 发送请求
	 * @author 王鹏
	 * @version 2018-06-29 17:21:22
	 * @param remoteName 配置文件中的域名端口信息的key
	 * @param url 接口对应的地址
	 * @param type 请求类型post/get
	 * @param headerMap 请求头集合
	 * @param paramMap 请求参数集合
	 * @return
	 */
	public static String sendRequest(String remoteName, String url, String type,
			Map<String, String> headerMap, Map<String, Object> paramMap) {
		String result = "";
		String requestUrl = Global.getConfig(remoteName)+url;
		HttpHeaders headers = new HttpHeaders();
		if(headerMap != null && !headerMap.isEmpty()) {
			//设置请求头参数
			headers.setAll(headerMap);
		}
		if(headers.getContentType()==null) {
			//如果没有指定Content-Type，则默认指定为application/x-www-form-urlencoded
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		}
		HttpMethod method = null;//请求方式
		HttpEntity<JSONObject> requestEntity = null;//请求参数
		Map<String, Object> uriVariables = null;//请求地址参数
		if(HttpMethod.POST.name().equalsIgnoreCase(type)) {
			//选择为post请求方式
			method = HttpMethod.POST;
			JSONObject param = new JSONObject();
			param.putAll(paramMap);
			requestEntity = new HttpEntity<JSONObject>(param, headers);
		}
		else if(HttpMethod.GET.name().equalsIgnoreCase(type)) {
			//选择为get请求方式
			method = HttpMethod.GET;
			uriVariables = paramMap;
		}
		else {
			logger.debug("暂不支持的请求方式。");
			return result;
		}
		//请求结果
		ResponseEntity<String> response = restRequest.exchange(requestUrl, method, requestEntity, String.class, uriVariables);
		//得到内容
		result = response.toString();
		return result;
	}
	
	/**
	 * 测试《02.内蒙古自治厅数据交换平台》接口
	 * @author 王鹏
	 * @version 2018-06-29 16:58:51
	 * @return
	 */
	private static String test_nmgzztsjjhpt() {
		//测试《02.内蒙古自治厅数据交换平台》获取律师信息
		String url = "/law/firms?admOrgCode=152501";//获取律师接口
		Map<String, String> headerMap = Maps.newHashMap();
		headerMap.put("appId", "7f16dbea190848feb3e344aed198e33c");
		headerMap.put("mac", "");
		Map<String, Object> paramMap = Maps.newHashMap();
//		paramMap.put("admOrgCode", "152501");
//		paramMap.put("name", "");
		String result = sendRequest("nmgzztsjjhpt", url, "get", headerMap, paramMap);
		try {
			result = new String(result.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @author 王鹏
	 * @version 2018-06-29 16:02:08
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * 测试《02.内蒙古自治厅数据交换平台》接口
		 */
		System.out.println(test_nmgzztsjjhpt());
	}

}
