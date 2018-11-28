/**
 * 
 */
package com.thinkgem.jeesite.tripartite.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.HttpUtil;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.tripartite.dao.TripartiteParamConfigDao;
import com.thinkgem.jeesite.modules.tripartite.entity.TripartiteParamConfig;
import com.thinkgem.jeesite.modules.tripartite.entity.TripartiteSystemConfig;
import com.thinkgem.jeesite.tripartite.dao.SchedulingDao;
import com.thinkgem.jeesite.tripartite.entity.TripartiteTaskEntity;
import com.thinkgem.jeesite.tripartite.util.TripartiteTypeEnum;
import com.thinkgem.jeesite.tripartite.util.TripartiteValueTypeEnum;

/**
 * 与第三方系统对接调度服务
 * @author 王鹏
 * @version 2018-06-29 19:36:10
 */
public abstract class SchedulingService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulingService.class);
    
	@Autowired
	private SchedulingDao schedulingDao;
	@Autowired
	private TripartiteParamConfigDao paramConfigDao;
	
	/**
	 * 获得所有参数
	 * @author 王鹏
	 * @version 2018-06-30 10:46:45
	 * @return
	 */
	public abstract List<Map<String, Object>> getParam();
	
	/**
	 * 通用的处理任务方法
	 * @author 王鹏
	 * @version 2018-06-29 19:42:42
	 * @param currTte 当前调度信息
	 * @param jsonObject
	 * <br>header存放为key-value
	 * <br>具体返回内容存放为key(responseBody)中
	 * @return
	 */
	public abstract String handleTask(TripartiteTaskEntity currTte, JSONObject jsonObject);

	/**
	 * 获得所有请求头
	 * @author 王鹏
	 * @version 2018-06-30 10:46:45
	 * @param systemId 系统配置id
	 * @return
	 */
	public Map<String, String> getHearder(String systemId){
		Map<String, String> hearders = Maps.newHashMap();
		TripartiteParamConfig tpc = new TripartiteParamConfig();
		tpc.setParent(new TripartiteSystemConfig(systemId));
		//查询header参数
		tpc.setType(TripartiteTypeEnum.SYSTEM_HEADER.getType());
		List<TripartiteParamConfig> list = paramConfigDao.findList(tpc);
		for (int i = 0; i < list.size(); i++) {
			hearders.put(list.get(i).getName(), list.get(i).getDefaultValue());
		}
		return hearders;
	}
	
	/**
	 * 获得所有参数
	 * @author 王鹏
	 * @version 2018-06-30 10:46:45
	 * @param systemId 系统配置id
	 * @param taskId 任务配置id
	 * @return
	 */
	public List<Map<String, Object>> getParam(String systemId, String taskId){
		List<Map<String, Object>> paramList = Lists.newArrayList();
		List<TripartiteParamConfig> tpcParamList = Lists.newArrayList();
		//获取通用参数
		List<TripartiteParamConfig> sysParamList = getSystemParam(systemId);
		//获取接口参数
		List<TripartiteParamConfig> taskParamList = getApiParam(taskId);
		//获取接口参数
		tpcParamList.addAll(sysParamList);
		tpcParamList.addAll(taskParamList);
		TripartiteParamConfig tpcParam = null;//处理参数
		boolean hasMethod = false;//判断是否含有使用method设置的参数，如果有则直接调用此方法，不在进行其他设置
		String beanName = null;//调用方法的服务类名
		String name = null;//参数名称
		String valueType = null;//参数值类型
		String defaultValue = null;//默认参数值
		for (int i = 0; i < tpcParamList.size(); i++) {
			//首先处理是否包含method的，如果有则其他均不处理
			tpcParam = tpcParamList.get(i);
			name = tpcParam.getName();
			valueType = tpcParam.getValueType();
			defaultValue = tpcParam.getDefaultValue();
			if(TripartiteValueTypeEnum.METHOD.getType().equals(valueType)) {
				hasMethod = true;
				beanName = tpcParam.getDefaultValue();
				break;
			}
		}
		if(hasMethod) {//如果设置了获取参数的方法，则直接调用返回
			SchedulingService schedulingService = SpringContextHolder.getBean(beanName);
			return schedulingService.getParam();
		}
		for (int i = 0; i < tpcParamList.size(); i++) {
			tpcParam = tpcParamList.get(i);
			name = tpcParam.getName();
			valueType = tpcParam.getValueType();
			defaultValue = tpcParam.getDefaultValue();
			//处理设置为数组的参数,多个数组必须保持参数一致
			if(TripartiteValueTypeEnum.ARRAY.getType().equals(valueType)) {
				/*
				 * 出现此设置时，如果有多个数组类型，则必须保持长度一致否则将导致请求参数不匹配
				 */
				String[] valueArr = defaultValue.split(",");//获取参数数组
				for (int j = 0; j < valueArr.length; j++) {
					if(paramList.size()==0) {//如果是第一个参数，直接赋值
						Map<String, Object> param = Maps.newHashMap();//参数
						param.put(name, valueArr[j]);
						paramList.add(param);
					}
					else {//如果已经有多个了，则按照顺序放入值
						if(j < paramList.size()) {
							paramList.get(j).put(name, valueArr[j]);
						}//如果数组超出则不再进行匹配
					}
				}// end valueArr
			}
		}
		for (int i = 0; i < tpcParamList.size(); i++) {
			tpcParam = tpcParamList.get(i);
			name = tpcParam.getName();
			valueType = tpcParam.getValueType();
			defaultValue = tpcParam.getDefaultValue();
			//处理只是字符串的参数
			if(TripartiteValueTypeEnum.STRING.getType().equals(valueType)) {
				if(paramList.size()==0) {//如果是第一个参数，直接赋值
					Map<String, Object> param = Maps.newHashMap();//参数
					param.put(name, defaultValue);
					paramList.add(param);
				}
				else {//如果已经有多个了，则每一组中都放入此值
					for (int j = 0; j < paramList.size(); j++) {
						paramList.get(j).put(name, defaultValue);
					}
				}
			}
		}
		return paramList;
	}

	/**
	 * 获取通用参数
	 * @author 王鹏
	 * @version 2018-07-02 15:44:52
	 * @param systemId
	 * @return
	 */
	protected List<TripartiteParamConfig> getSystemParam(String systemId) {
		TripartiteParamConfig tpc = new TripartiteParamConfig();
		tpc.setParent(new TripartiteSystemConfig(systemId));
		tpc.setType(TripartiteTypeEnum.SYSTEM_PARAM.getType());
		List<TripartiteParamConfig> sysParamList = paramConfigDao.findList(tpc);
		return sysParamList;
	}
	/**
	 * 接口参数
	 * @author 王鹏
	 * @version 2018-07-02 15:44:52
	 * @param systemId
	 * @return
	 */
	protected List<TripartiteParamConfig> getApiParam(String taskId) {
		TripartiteParamConfig tpc = new TripartiteParamConfig();
		tpc.setParent(new TripartiteSystemConfig(taskId));
		tpc.setType(TripartiteTypeEnum.API_PARAM.getType());
		List<TripartiteParamConfig> taskParamList = paramConfigDao.findList(tpc);
		return taskParamList;
	}
	
	/**
	 * 根据调度任务信息处理相应请求
	 * @author 王鹏
	 * @version 2018-06-29 20:08:33
	 * @param tte
	 * @return
	 */
	public String sendRequest(TripartiteTaskEntity tte) {
		StringBuffer sb = new StringBuffer();
		//获取所有参数
		List<Map<String, Object>> paramList = tte.getParamList();
		//获取结果处理类
		SchedulingService schedulingService = SpringContextHolder.getBean(tte.getBeanName());
		TripartiteTaskEntity currTte = null;//当前调度信息
		String msg = null;
		for (int i = 0; i < paramList.size(); i++) {
			try {
				currTte = tte;
				//当前请求参数信息
				currTte.setCurrParam(paramList.get(i));
				//如果出现多个参数集合，则做循环请求处理
				JSONObject jsonObject = HttpUtil.sendRequest(tte.getSystemUrl(),
						tte.getApiUrl(), tte.getMethod(), tte.getHearders(), paramList.get(i));
				//记录处理日志
				msg = schedulingService.handleTask(currTte, jsonObject);
				sb.append(msg+"\n");
			} catch (Exception e) {
				// TODO: handle exception
				//打印错误信息，继续处理，不影响其他操作
				logger.debug("出现异常["+e.getMessage()+"]，参数：["+JSON.toJSONString(tte)+"]，详细信息：\n"+e);
				sb.append(e.getMessage()+"\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 适合以同样的参数做重复请求(一次未能获取全部数据的情况)
	 * @author 王鹏
	 * @version 2018-06-29 20:08:33
	 * @param tte
	 * @return
	 */
	public JSONObject repeatRequest(TripartiteTaskEntity currTte) {
		//如果出现多个参数集合，则做循环请求处理
		JSONObject result = HttpUtil.sendRequest(currTte.getSystemUrl(),
				currTte.getApiUrl(), currTte.getMethod(), currTte.getHearders(), currTte.getCurrParam());
		//记录处理日志
		return result;
	}
	
	/**
	 * 将jsonArray转换为List<Map<String, Object>>
	 * @author 王鹏
	 * @version 2018-07-01 16:14:55
	 * @param jsonArray
	 * @return
	 */
	public List<Map<String, Object>> toList(JSONArray jsonArray){
		List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Object> map = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			map = jsonArray.getJSONObject(i);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取字段集合
	 * @author 王鹏
	 * @version 2018-07-02 17:40:05
	 * @param propertiesName
	 * @param key
	 * @return
	 */
	public String[] getColumns(String propertiesName, String key) {
		return new PropertiesLoader(propertiesName).getProperty(key+".columns").split(",");
	}

	/**
	 * 获取表名
	 * 表名的配置文件key为接口key.tableName
	 * @author 王鹏
	 * @version 2018-07-02 17:40:05
	 * @param propertiesName
	 * @param key
	 * @return
	 */
	public String getTableName(String propertiesName, String key) {
		return new PropertiesLoader(propertiesName).getProperty(key+".table");
	}
	
	/**
	 * 将jsonArray转换为List<Map<String, Object>>
	 * 手动处理json数组，校验字段，防止json的null过滤
	 * @author 王鹏
	 * @version 2018-07-01 16:14:55
	 * @param jsonArray
	 * @return
	 */
	public List<Map<String, Object>> toList(String[] columns, JSONArray jsonArray){
		//手动处理json数组，校验字段，防止json的null过滤
		List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Object> map = null;
		JSONObject jsonObject = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			//实例map并按照字段赋值
			map = new LinkedHashMap<String, Object>();
			jsonObject = jsonArray.getJSONObject(i);
			for (int j = 0; j < columns.length; j++) {
				map.put(columns[j], jsonObject.getString(columns[j]));
			}
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取任务调度信息
	 * @author 王鹏
	 * @version 2018-07-01 17:21:10
	 * @param taskId
	 * @return
	 */
	public TripartiteTaskEntity getTaskById(String systemId, String taskId) {
		TripartiteTaskEntity tte = schedulingDao.get(taskId);
		//设置请求头信息
		tte.setHearders(getHearder(systemId));
		//设置请求体信息
		tte.setParamList(getParam(systemId, taskId));
		return tte;
	}

	/**
	 * 保存数据(通用方法，如果不适用请手动处理)
	 * @author 王鹏
	 * @version 2018-07-01 15:27:45
	 * @param jsonArray
	 * @return
	 */
	public String saveData(String propertiesName, String key, String tableName, JSONArray jsonArray) {
		String msg = "";
		//获取字段集合
		String[] columns = getColumns(propertiesName, key);
		//转换为集合插入数据
		schedulingDao.insert(tableName, columns, toList(columns, jsonArray));
		return msg;
	}
}
