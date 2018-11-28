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
import com.thinkgem.jeesite.api.chart.entity.FastLegalCount;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.oa.dao.act.OaFastLegalDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.AreaService;

/**
 * 大屏接口:全盟法律服务平台(快速直通车受理)
 * @author 王鹏
 * @version 2018-7-17 11:00:24
 */
@Service
@Transactional(readOnly = true)
public class FastLegalCountService extends CrudService<CountByAreaDao, CountByArea> {

    //锡林郭勒盟  sys_area 中对应的id(为了只获取锡盟下属的地区)
    private static final String AREA_PARENT_ID = "5";
    
	@Autowired
	private AreaService areaService;
	@Autowired
	private OaFastLegalDao oaFastLegalDao;

	/**
	 * 各旗县普法宣传文章统计图
	 * @author 王鹏
	 * @version 2018-07-12 15:44:12
	 * @param ac
	 * @return
	 */
	public Map<String, Object> countFastLegalByAreaDate(String startDate, String endDate){
		Map<String, Object> resultMap = Maps.newHashMap();
		//获取全部旗县(不要父节点)
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		//设置xData对象
		List<String> xDataList = DateUtils.getAllDayBetweenDate(startDate, endDate);
		resultMap.put("xData", xDataList);
		FastLegalCount flc = new FastLegalCount();
		flc.setStartDate(startDate);
		flc.setEndDate(endDate);
		//将日期格式改为mysql的日期格式
		flc.setDatePattern(DateUtils.getDatePattern(startDate));
		//同时按日期进行统计
		flc.setForTable("true");
		//统计结果
		List<FastLegalCount> fastLegalList = oaFastLegalDao.countFastLegal(flc);
		//放入旗县的数据集合中
		List<Map<String, Object>> seriesList = Lists.newArrayList();
		Map<String, Object> areaMap = null;//旗县统计数据
		List<Integer> areaCountList = null;//旗县统计集合
		FastLegalCount fastLegalCount = null;//具体统计数据
		boolean hasCount = false;//是否有数据
		for (int i = 0; i < areaList.size(); i++) {
			areaMap = Maps.newHashMap();
			areaMap.put("name", areaList.get(i).getName());//旗县名称
			areaMap.put("type", "line");//折线图
			areaCountList = Lists.newArrayList();
			for (int j = 0; j < xDataList.size(); j++) {
				hasCount = false;//该旗县在改日期是否有数据
				for (int k = 0; k < fastLegalList.size(); k++) {
					fastLegalCount = fastLegalList.get(k);//获取统计数据
					//判断旗县、日期是否匹配
					if(areaList.get(i).getId().equals(fastLegalCount.getArea().getId())
							&& xDataList.get(j).equals(fastLegalCount.getCountDate())) {
						areaCountList.add(fastLegalCount.getCount());
						hasCount = true;
						break;
					}
				}
				if(!hasCount) {//如果旗县在此日期中没有数据，则默认补零
					areaCountList.add(0);
				}
			}
			//放入旗县的所有日期统计数量
			areaMap.put("data", areaCountList);
			seriesList.add(areaMap);//放入series集合中
		}
		resultMap.put("series", seriesList);
		return resultMap;
	}
}
