package com.thinkgem.jeesite.common.utils;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.config.Global;

public class MicroUtil {
	 static final String REQUEST_TYPE_POST = "post";
	public static <K,V> Map getMicroService(String url,String type,Map map) throws MicroException{
		String REMOTE_URL = Global.getConfig("jeecms_port");
    	String exchange = null;
    	try {
	    	if(REQUEST_TYPE_POST.equalsIgnoreCase(type)){
	    		HttpHeaders headers = new HttpHeaders();
	    		MediaType types = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
	    		headers.setContentType(types);
	    		HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(map),  headers);
	    		exchange = new RestTemplate().postForObject(REMOTE_URL+url,requestEntity,String.class);
	    	}else{
	    		exchange = new RestTemplate().getForObject(REMOTE_URL+url,String.class);
	    	}
    	}catch (Exception e) {
    		e.printStackTrace();
    		throw new MicroException("调取接口操作失败！");
		}
    	return JSON.parseObject(exchange,Map.class);
    }
	public static <K,V> Map getmessageService(String url,String type,Map map) throws MicroException{
		String REMOTE_URL = Global.getConfig("ext_message");
		
    	String exchange = null;
    	try {
	    	if(REQUEST_TYPE_POST.equalsIgnoreCase(type)){
	    		HttpHeaders headers = new HttpHeaders();
	    		MediaType types = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
	    		headers.setContentType(types);
	    		HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(map),  headers);
	    		
	    		exchange = new RestTemplate().postForObject(REMOTE_URL+url,requestEntity,String.class);
	    	}else{
	    		exchange = new RestTemplate().getForObject(REMOTE_URL+url,String.class);
	    	}
	    	
    	}catch (Exception e) {
    		e.printStackTrace();
    		throw new MicroException("调取接口操作失败！");
		}
    	return JSON.parseObject(exchange,Map.class);
    }
	public static <K,V> Map setmessageService(String url,String type,Map map) throws MicroException{
		String REMOTE_URL = Global.getConfig("ext_disk");
		
    	String exchange = null;
    	try {
	    	if(REQUEST_TYPE_POST.equalsIgnoreCase(type)){
	    		HttpHeaders headers = new HttpHeaders();
	    		MediaType types = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
	    		headers.setContentType(types);
	    		HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(map),  headers);
	    		
	    		exchange = new RestTemplate().postForObject(REMOTE_URL+url,requestEntity,String.class);
	    	}else{
	    		exchange = new RestTemplate().getForObject(REMOTE_URL+url,String.class);
	    	}
	    	
    	}catch (Exception e) {
    		e.printStackTrace();
    		throw new MicroException("调取接口操作失败！");
		}
    	return JSON.parseObject(exchange,Map.class);
    }
	public static void loginoutService(String url,String type,Map map) throws MicroException{
		String REMOTE_URL = Global.getConfig("loginout");
		String exchange = null;
		exchange = new RestTemplate().getForObject(REMOTE_URL+url,String.class);
	}
	public static void getMicroServiceNoReturn(String url,String type,Map map) throws MicroException{
		String REMOTE_URL = Global.getConfig("jeecms_port");
    	String exchange = null;
    	try {
	    	if(REQUEST_TYPE_POST.equalsIgnoreCase(type)){
	    		HttpHeaders headers = new HttpHeaders();
	    		MediaType types = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
	    		headers.setContentType(types);
	    		HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(map),  headers);
	    		exchange = new RestTemplate().postForObject(REMOTE_URL+url,requestEntity,String.class);
	    	}else{
	    		exchange = new RestTemplate().getForObject(REMOTE_URL+url,String.class);
	    	}
    	}catch (Exception e) {
    		e.printStackTrace();
    		throw new MicroException("调取接口操作失败！");
		}
    	//return JSON.parseObject(exchange,Map.class);
    }
		
}
