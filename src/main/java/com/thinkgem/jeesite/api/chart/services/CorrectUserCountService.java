/**
 * 
 */
package com.thinkgem.jeesite.api.chart.services;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.api.chart.dao.CountByAreaDao;
import com.thinkgem.jeesite.api.chart.entity.CountByArea;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.AreaService;

/**
 * @author 王鹏
 * @version 2018-06-10 16:24:12
 */
@Service
@Transactional(readOnly = true)
public class CorrectUserCountService extends CrudService<CountByAreaDao, CountByArea> {

    //锡林郭勒盟  sys_area 中对应的id(为了只获取锡盟下属的地区)
    private static final String AREA_PARENT_ID = "5";
    
	@Autowired
	private AreaService areaService;
	@Autowired
	private CountByAreaService countByAreaService;
	
	/**
     * 社区矫正人员统计接口
     * 双柱状图:矫正中、已解矫(所有非矫正中的)
	 * @author 王鹏
	 * @version 2018-7-16 15:32:24
	 * @return
	 */
	public Map<String, Object> countCorrectUser(){
		Map<String, Object> resultMap = Maps.newHashMap();
		//获取全部旗县(不要父节点)
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		//设置xData对象
		List<String> xDataList = Lists.newArrayList();
		for (int i = 0; i < areaList.size(); i++) {
			xDataList.add(areaList.get(i).getName());
		}
		resultMap.put("xData", xDataList);
		//拼接series
		List<Map<String, Object>> seriesList = Lists.newArrayList();
		//获取矫正中的数据
		List<CountByArea> inList = countByAreaService.countPeopleByArea("1", null, " AND c.correct_status = 0 ");
		//矫正中的Map
		Map<String, Object> inMap = Maps.newHashMap();
		inMap.put("name", "矫正中");
		inMap.put("type", "bar");
		List<Integer> inCountList = Lists.newArrayList();
		for (int i = 0; i < inList.size(); i++) {
			inCountList.add(inList.get(i).getCount());
		}
		inMap.put("data", inCountList);
		seriesList.add(inMap);
		//获取已解矫(所有非矫正中的)数据
		List<CountByArea> notList = countByAreaService.countPeopleByArea("1", null, " AND c.correct_status != 0 ");
		//已解矫(所有非矫正中的)的Map
		Map<String, Object> notMap = Maps.newHashMap();
		notMap.put("name", "已解矫");
		notMap.put("type", "bar");
		List<Integer> notCountList = Lists.newArrayList();
		for (int i = 0; i < notList.size(); i++) {
			notCountList.add(notList.get(i).getCount());
		}
		notMap.put("data", notCountList);
		seriesList.add(notMap);
		//封装数据完毕
		resultMap.put("series", seriesList);
		return resultMap;
	}
}
