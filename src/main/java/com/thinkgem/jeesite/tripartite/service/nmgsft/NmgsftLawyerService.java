/**
 * 
 */
package com.thinkgem.jeesite.tripartite.service.nmgsft;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.tripartite.entity.TripartiteParamConfig;
import com.thinkgem.jeesite.tripartite.entity.TripartiteTaskEntity;

/**
 * 内蒙古自治区司法厅数据交换平台
 * 律师相关接口处理类
 * @author 王鹏
 * @version 2018年7月1日16:08:08
 */
@Service
public class NmgsftLawyerService extends NmgsftsjjhptService {
	

	/**
	 * 配置文件中的key
	 * 配置文件中对应的接口key
	 * 表名key.table
	 * 列名key.columns
	 * 系统id(为了获取默认分页参数) system_id
	 */
	private static final String PROPERTIES_KEY = "lawyer";
	
	/**
	 * 获取参数(需要根据律师事务所查询事务所下的律师)
	 * 事务所以
	 * @see com.thinkgem.jeesite.tripartite.service.SchedulingService#getParam()
	 */
	@Override
	public List<Map<String, Object>> getParam() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> paramList = Lists.newArrayList();
		List<String> creditNoList = nmgsftsjjhptDao.selectLawOfficeCreditNo(DateUtils.getDate());
		//获取默认分页设置
		//从配置文件中读取指定key
		String systemId = new PropertiesLoader(PROPERTITES_NAME).getProperty("system_id");
		List<TripartiteParamConfig> tpcList = getSystemParam(systemId);
		Map<String, Object> param = null;
		for (int i = 0; i < creditNoList.size(); i++) {
			param = Maps.newHashMap();
			param.put("creditNo", creditNoList.get(i));
			//补充通用参数
			for (int j = 0; j < tpcList.size(); j++) {
				param.put(tpcList.get(j).getName(), tpcList.get(j).getDefaultValue());
			}
			paramList.add(param);
		}
		return paramList;
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
