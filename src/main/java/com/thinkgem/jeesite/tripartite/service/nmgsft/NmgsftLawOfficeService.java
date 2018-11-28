/**
 * 
 */
package com.thinkgem.jeesite.tripartite.service.nmgsft;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.tripartite.entity.TripartiteTaskEntity;

/**
 * 内蒙古自治区司法厅数据交换平台
 * 律师事务所相关接口处理类
 * @author 王鹏
 * @version 2018-06-30 16:47:05
 */
@Service
public class NmgsftLawOfficeService extends NmgsftsjjhptService {

	/**
	 * 配置文件中对应的接口key
	 * 表名key.table
	 * 列名key.columns
	 */
	private static final String PROPERTIES_KEY = "law_office";
	
	/**
	 * 获取参数(此接口不需要)
	 * @see com.thinkgem.jeesite.tripartite.service.SchedulingService#getParam()
	 */
	@Override
	public List<Map<String, Object>> getParam() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @see com.thinkgem.jeesite.tripartite.service.SchedulingService#handleTask(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public String handleTask(TripartiteTaskEntity currTte, JSONObject jsonObject) {
		// TODO Auto-generated method stub
		return handleTask(PROPERTIES_KEY, currTte, jsonObject);
	}
	
}
