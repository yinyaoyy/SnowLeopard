/**
 * 
 */
package com.thinkgem.jeesite.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

/**
 * 使用HttpClient发送http请求
 * @author 王鹏
 * @version 2018-06-29 17:20:38
 */
public class HttpUtil {


	/**
	 * 发送请求
	 * @author 王鹏
	 * @version 2018-06-29 17:21:22
	 * @param remoteId 请求的ip端口信息
	 * @param url 接口对应的地址
	 * @param type 请求类型post/get
	 * @param headerMap 请求头集合
	 * @param paramMap 请求参数集合
	 * @return
	 */
	public static JSONObject sendRequest(String remoteIp, String url, String type,
			Map<String, String> headerMap, Map<String, Object> paramMap) {
		JSONObject result = new JSONObject();
		StringBuffer responseBody = new StringBuffer();
		BufferedReader in = null;
		try {
			URLConnection connection = getConnection(remoteIp, url, headerMap, paramMap);
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
                result.put(key, map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            	responseBody.append(line);
            }
            result.put("responseBody", responseBody.toString());
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
		return result;
	}

	/**
	 * @author 王鹏
	 * @version 2018-07-02 20:50:12
	 * @param remoteIp
	 * @param url
	 * @param headerMap
	 * @param paramMap
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static URLConnection getConnection(String remoteIp, String url, Map<String, String> headerMap,
			Map<String, Object> paramMap) throws MalformedURLException, IOException {
		//请求地址
		StringBuffer requestUrl = new StringBuffer(remoteIp+url);
        	if(paramMap !=null && !paramMap.isEmpty()) {
        		requestUrl.append("?");
        		int size = paramMap.keySet().size();//参数个数
        		for (String key : paramMap.keySet()) {
        			requestUrl.append(key+"="+paramMap.get(key));
        			size--;
        			if(size!=0) {//后边还有参数，需要追加"&"
        				requestUrl.append("&");
        			}
				}
        	}
            URL realUrl = new URL(requestUrl.toString());
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性(header)
        	if(headerMap !=null && !headerMap.isEmpty()) {
        		for (String key : headerMap.keySet()) {
        			connection.setRequestProperty(key, headerMap.get(key));
				}
        	}
        	if(StringUtils.isBlank(connection.getRequestProperty("Content-Type"))) {
        		//补充默认的请求头属性
        		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        	}
            // 建立实际的连接
            connection.connect();
		return connection;
	}
	
	/**
	 * 发送请求，返回输出流
	 * 此方法无法返回文件头信息
	 * @author 王鹏
	 * @version 2018-06-29 17:21:22
	 * @param remoteId 请求的ip端口信息
	 * @param url 接口对应的地址
	 * @param type 请求类型post/get
	 * @param headerMap 请求头集合
	 * @param paramMap 请求参数集合
	 * @return
	 */
	public static InputStream sendRequestReturnStream(String remoteIp, String url, String type,
			Map<String, String> headerMap, Map<String, Object> paramMap) {
		URLConnection connection = null;
		InputStream is = null;
		try {
			connection = getConnection(remoteIp, url, headerMap, paramMap);
			is = connection.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}
	
	/**
	 * 测试《02.内蒙古自治厅数据交换平台》接口
	 * @author 王鹏
	 * @version 2018-06-29 16:58:51
	 * @return
	 */
	private static String test_nmgzztsjjhpt() {
		//测试《02.内蒙古自治厅数据交换平台》获取律师信息
		String url = "/law/firms";//获取律师接口
		Map<String, String> headerMap = Maps.newHashMap();
		headerMap.put("appId", "7f16dbea190848feb3e344aed198e33c");
		headerMap.put("mac", "");
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("admOrgCode", "152500");
		paramMap.put("name", "");
		JSONObject result = sendRequest("nmgzztsjjhpt", url, "get", headerMap, paramMap);
		System.out.println("请求总数:[X-Total-Count]=["+result.getJSONArray("X-Total-Count").getString(0)+"]");
		System.out.println("请求数据:\n"+result.getString("responseBody"));
		return result.toJSONString();
	}
	
	/**
	 * @author 王鹏
	 * @version 2018-06-29 17:20:38
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(test_nmgzztsjjhpt());
	}

}
