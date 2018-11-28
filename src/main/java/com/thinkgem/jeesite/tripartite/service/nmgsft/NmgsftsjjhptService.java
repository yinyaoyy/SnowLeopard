/**
 * 
 */
package com.thinkgem.jeesite.tripartite.service.nmgsft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.tripartite.dao.nmgsft.NmgsftsjjhptDao;
import com.thinkgem.jeesite.tripartite.entity.TripartiteTaskEntity;
import com.thinkgem.jeesite.tripartite.service.SchedulingService;

/**
 * 内蒙古自治区司法厅数据交换平台
 * 接口调度服务
 * @author 王鹏
 * @version 2018-06-29 19:37:37
 */
public abstract class NmgsftsjjhptService extends SchedulingService {

    private static final Logger logger = LoggerFactory.getLogger(NmgsftsjjhptService.class);

	@Autowired
	NmgsftsjjhptDao nmgsftsjjhptDao;
	
	static final String PROPERTITES_NAME = "properties/nmgsftsjjhpt.properties";
	
	/**
	 * 获取接口返回信息
	 * 如果出现错误则记录日志
	 * @author 王鹏
	 * @version 2018-06-30 16:44:28
	 * @param jsonObject
	 * @return null为出现错误;0=不需要循环调用;>0为循环调用次数
	 */
	public Integer getResponse(JSONObject jsonObject) {
		JSONObject responseBody = getResponseBody(jsonObject);
		//获取响应码
		String respCode = responseBody.getString("respCode");
		//获取记录总数
		if(!"000000".equals(respCode)) {
			//记录错误日志
			logger.debug("接口调用出错: error:错误码=["+respCode+"];错误信息:["+responseBody.getString("respMsg")+"]");
			return null;
		}
		Integer total = getResponseTotal(jsonObject);
		JSONArray respBody = getBody(jsonObject);
		if(total>respBody.size()) {
			//需要循环请求处理,计算循环次数并返回
			//***注意已经请求过一次了
			return (total%respBody.size()==0)?(total/respBody.size()-1):(total/respBody.size());
		}
		else {
			return 0;
		}
	}

	/**
	 * 获取具体内容
	 * @author 王鹏
	 * @version 2018-07-02 09:43:11
	 * @param jsonObject
	 * @return
	 */
	public JSONObject getResponseBody(JSONObject jsonObject) {
		String responseStr = jsonObject.getString("responseBody");
		//去除转义、换行、html标签等无效内容
		responseStr = StringUtils.replaceSymbol(responseStr);
		responseStr = StringUtils.replaceHtml(responseStr);
		//将responseBody有不规范的字符串转换为jsonArray
		responseStr = responseStr.replace("\"[", "[").replace("]\"", "]");
		//将特殊的双引号屏蔽
		responseStr = responseStr.replaceAll("\"\"([\\u4e00-\\u9fa5|\\w|\\d]+)\"", "\"$1\"");
		System.out.println("responseStr=["+responseStr+"]");
		//将字符串转为对象
		JSONObject responseBody = JSON.parseObject(responseStr);
		return responseBody;
	}
	
	/**
	 * 获取总数
	 * @author 王鹏
	 * @version 2018-06-30 16:44:02
	 * @param jsonObject
	 * @return
	 */
	public Integer getResponseTotal(JSONObject jsonObject) {
		String totalStr = jsonObject.getJSONArray("X-Total-Count").getString(0);
		return StringUtils.isBlank(totalStr)?null:Integer.parseInt(totalStr);
	}
	
	/**
	 * 获取详细内容
	 * @author 王鹏
	 * @version 2018-06-30 16:44:02
	 * @param jsonObject
	 * @return
	 */
	public JSONArray getBody(JSONObject jsonObject) {
		JSONObject responseBody = getResponseBody(jsonObject);
		return responseBody.getJSONArray("respBody");
	}

	/**
	 * 获取字段集合
	 * @author 王鹏
	 * @version 2018-07-02 17:40:05
	 * @param key
	 * @return
	 */
	public String[] getColumns(String key) {
		return new PropertiesLoader(PROPERTITES_NAME).getProperty(key+".columns").split(",");
	}
	/**
	 * 获取表名
	 * 表名的配置文件key为接口key.tableName
	 * @author 王鹏
	 * @version 2018-07-02 17:40:05
	 * @param key
	 * @return
	 */
	public String getTableName(String key) {
		return new PropertiesLoader(PROPERTITES_NAME).getProperty(key+".table");
	}

	/**
	 * 保存数据(通用方法，如果不适用请手动处理)
	 * @author 王鹏
	 * @version 2018-07-01 15:27:45
	 * @param jsonArray
	 * @return
	 */
	public String saveData(String key, JSONArray jsonArray) {
		String msg = "";
		//获取表名
		String tableName = getTableName(key);
		//获取字段集合
		String[] columns = getColumns(key);
		//转换为集合插入数据
		nmgsftsjjhptDao.insert(tableName, columns, toList(columns, jsonArray));
		return msg;
	}

	/**
	 * 使用与内蒙古司法厅的通用处理程序
	 * @author 王鹏
	 * @version 2018-07-02 19:53:28
	 * @param propertiesKey 配置文件中的接口key
	 * @param currTte 当前调用信息
	 * @param jsonObject 接口返回的数据信息
	 * @return
	 */
	public String handleTask(String propertiesKey, TripartiteTaskEntity currTte, JSONObject jsonObject) {
		// TODO Auto-generated method stub
		String msg = "";//默认出错
		//获取具体内容
		Integer total = getResponse(jsonObject);
		if(total!=null) {
			//如果未出错则处理否则返回错误，错误信息已经记录在日志表
			JSONArray jsonArray = getBody(jsonObject);
			saveData(propertiesKey, jsonArray);//保存数据
			//如果数据没有处理完成，循环重复请求
			JSONObject repeatJSONObject = null;
			for (int i = 1; i <= total; i++) {
				//重复请求
				//需要调整分页参数
				currTte.getCurrParam().put("offset", (i));
				repeatJSONObject = repeatRequest(currTte);
				if(repeatJSONObject!=null) {
					//获取数据
					jsonArray = getBody(repeatJSONObject);
					saveData(propertiesKey, jsonArray);//保存数据
				}
				else {
					msg = "fail";//出现错误
					break;
				}
			}
			msg = StringUtils.isBlank(msg)?"success":msg;
		}
		return msg;
	}
}
