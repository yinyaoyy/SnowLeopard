/**
 * 
 */
package com.thinkgem.jeesite.api.chart.services;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.api.chart.dao.CountByAreaDao;
import com.thinkgem.jeesite.api.chart.entity.CountByArea;
import com.thinkgem.jeesite.api.utils.enums.CountAgencyByAreaEnum;
import com.thinkgem.jeesite.api.utils.enums.CountPeopleByAreaEnum;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * @author 王鹏
 * @version 2018-06-10 16:24:12
 */
@Service
@Transactional(readOnly = true)
public class CountByAreaService extends CrudService<CountByAreaDao, CountByArea> {

    //机构分类字典识别码
    private static final String DICT_AGENCY_CAGETORY = "sys_office_category";
    //锡林郭勒盟  sys_area 中对应的id(为了只获取锡盟下属的地区)
    private static final String AREA_PARENT_ID = "5";
    
    /*
	 * 统计机构和人员的综合信息
	 * 机构：司法所、人民调解委员会、基层法律服务所
	 * 人员：司法所工作人员、人民调解员、基层法律服务工作者、人民监督员
     */
    private static final String[][] COUNT_AGENCY_KEYS = {
		{"12","10","8"}, 
		{"司法所","人民调解委员会","基层法律服务所"}
    };
    private static final String[][] COUNT_PEOPLE_KEYS = {
		{"10","6","8","7"}, 
		{"司法所工作人员","人民调解员","基层法律服务工作者","人民监督员"}
    };
    
	@Autowired
	private AreaService areaService;
	
	/**
	 * 根据key统计相应的内容
	 * @author 王鹏
	 * @version 2018-06-10 16:54:36
	 * @param key
	 * @return
	 */
	public List<List<Object>> countPeopleByArea_EnumKey(String key, String sort) {
		List<List<Object>> resultList = Lists.newArrayList();
		//获取统计结果
		List<CountByArea> cpbaList = countPeopleByArea(key, sort, null);
		List<Object> list = null;
		for (int i = 0; i < cpbaList.size(); i++) {
			list = Lists.newArrayList();
			list.add(cpbaList.get(i).getArea().getName());
			list.add(cpbaList.get(i).getCount());
			resultList.add(list);
		}
		return resultList;
	}

	/**
	 * 统计多个区域的数量(目前的十三个旗县)
	 * 根据key统计相应的内容
	 * 示例:
	 * [{
	 * 		"name": "锡林浩特市",
	 * 		"coord": [116.093659, 43.932312],
	 * 		"value": {
	 * 			"areaid": "sdadsad",
	 * 			"setupList": [{
	 * 					"name": "律师事务所",
	 * 					"categoryid": 1231231,
	 * 					"count": 22
	 * 				}, {
	 * 					"name": "律师事务所",
	 * 					"count": 22
	 * 				}
	 * 			]
	 * 		}
	 * 	}
	 * ]
	 * @author 王鹏
	 * @version 2018-06-10 16:54:36
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> countAgencyByAllArea() {
		//最终数据集合
		List<Map<String, Object>> resultList = Lists.newArrayList();
		//获取全部旗县(不要父节点)
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		//获取字典表中的所有机构分类
		List<Dict> dicts = DictUtils.getDictList(DICT_AGENCY_CAGETORY);
		//设置统计集合
		List<Map<String, Object>> countList = Lists.newArrayList();
		//根据类型统计各地区数量
		Dict dict = null;
		Map<String, Object> countMap = null;//统计的map(对应字典和统计数据)
		for (int i = 0; i < dicts.size(); i++) {
			dict = dicts.get(i);
			if("人员分类".equals(dict.getDescription())) {
				continue;//目前只统计机构的
			}
			countMap = Maps.newHashMap();
			countMap.put("dict", dict);//字典
			//统计数据
			countMap.put("countList", countAgencyByArea(dict.getValue()));
			countList.add(countMap);
		}
		//循环地区集合并放置统计数据
		Map<String, Object> map = null;//单个地区集合
		Map<String, Object> mapValue = null;//统计信息集合
		List<Map<String, Object>> listSetup = null;//设置数量setupList
		Map<String, Object> mapSetup = null;//设置setupList.Map
		Area area = null;
		List<CountByArea> listCba = null;//里层集合1
		CountByArea cba = null;//具体数量
		for (int i = 0; i < areaList.size(); i++) {
			area = areaList.get(i);
			map = Maps.newHashMap();
			//地区名称
			map.put("name", area.getName());
			//设置经纬度
			map.put("coord", areaService.getLongitudeLatitudeByAreaId(area.getId()));
			mapValue = Maps.newHashMap();
			map.put("value", mapValue);
			mapValue.put("areaid", area.getId());
			listSetup = Lists.newArrayList();
			mapValue.put("setupList", listSetup);
			//循环设置统计数据
			for (int j = 0; j < countList.size(); j++) {
				mapSetup = Maps.newHashMap();
				countMap = countList.get(j);//获取统计Map(字典和统计数据)
				dict = (Dict) countMap.get("dict");//字典
				listCba = (List<CountByArea>)countMap.get("countList");//统计数据
				mapSetup.put("name", dict.getLabel());
				mapSetup.put("categoryid", dict.getValue());
				for (int k = 0; k < listCba.size(); k++) {
					cba = listCba.get(k);
					//判断地区是否相符
					if(area.getId().equals(cba.getArea().getId())) {
						//相符就放入数量
						mapSetup.put("count", cba.getCount());
						break;
					}
				}
				listSetup.add(mapSetup);//放入setupMap
			}
			resultList.add(map);//放入地区集合
		}
		return resultList;
	}

	/**
	 * 根据key获取统计数据
	 * @author 王鹏
	 * @version 2018-7-15 18:00:18
	 * @param key
	 * @return
	 */
	public List<CountByArea> countPeopleByArea(String key) {
		return countPeopleByArea(key, null, null);
	}
	/**
	 * 根据key统计相应的内容
	 * @author 王鹏
	 * @version 2018-06-10 16:54:36
	 * @param key
	 * @param sort 按数量排序h高到低(倒叙)；l低到高(正序)
	 * @param condition 其他的查询条件
	 * @return
	 */
	public List<CountByArea> countPeopleByArea(String key, String sort, String condition) {
		CountByArea cpba = new CountByArea();
		//设置统计语句
		if(StringUtils.isNotBlank(CountPeopleByAreaEnum.getTable(key))) {
			cpba.setCountSql(getPeopleCountSql(key, condition));
		}
		if("high".equals(sort) || "low".equals(sort)) {
			cpba.setSort(sort);
		}
		//获取统计结果
		return dao.countByArea(cpba);
	}
	/**
	 * 根据key统计相应的内容
	 * @author 王鹏
	 * @version 2018-06-10 16:54:36
	 * @param key
	 * @param sort 按数量排序h高到低(倒叙)；l低到高(正序)
	 * @param condition 其他的查询条件
	 * @return
	 */
	public List<CountByArea> countPeopleByAreaDate(String key, String startDate, String endDate, String condition) {
		CountByArea cpba = new CountByArea();
		//设置统计语句
		if(StringUtils.isNotBlank(CountPeopleByAreaEnum.getTable(key))) {
			cpba.setCountSql(getPeopleCountByAreaDateSql(key, startDate, endDate, condition));
		}
		//获取统计结果
		return dao.countByAreaDate(cpba);
	}
	
	/**
	 * 根据key统计相应的内容
	 * @author 王鹏
	 * @version 2018-06-10 16:54:36
	 * @param key
	 * @return
	 */
	public List<CountByArea> countAgencyByArea(String key) {
		CountByArea cpba = new CountByArea();
		//设置统计语句
		if(StringUtils.isNotBlank(CountAgencyByAreaEnum.getTable(key))) {
			cpba.setCountSql(getAgencyCountSql(key, null));
		}
		//获取统计结果
		return dao.countByArea(cpba);
	}

	
	/**
	 * 根据key生成相应的统计数据
	 * @author 王鹏
	 * @version 2018-06-10 16:55:28
	 * @param key
	 * @return
	 */
	String getPeopleCountSql(String key, String condition) {
		StringBuffer sb = new StringBuffer();
		//获取表名
		String tableName = CountPeopleByAreaEnum.getTable(key);
		//获取列名
		String columns = CountPeopleByAreaEnum.getAreaColumn(key);
		if(columns.indexOf("#,#") > 0) {
			//如果出现特殊分隔符，说明需要关联机构表进行统计
			//拆分字段,0是机构表明;1是外键;2是机构地区id
			String[] columnArr = columns.split("#,#");
			sb.append(" SELECT u."+columnArr[2]+" AS area_id, count(*) AS count ");
			//关联机构表
			sb.append("   FROM "+ tableName+" c, "+columnArr[0]+" u");
			sb.append("  WHERE c.del_flag = '0' ");
			//查询条件
			sb.append("    AND u.del_flag = '0' ");
			sb.append("    AND c."+columnArr[1]+" = u.id ");
			if(StringUtils.isNotBlank(condition)){//添加其他的查询条件
				sb.append("    "+condition+" ");
			}
			sb.append("  GROUP BY u."+columnArr[2]);
		}
		else {//没有特殊分隔符，正常的单表统计
			sb.append(" SELECT "+columns+" AS area_id, count(*) AS count ");
			sb.append("   FROM "+ tableName+" c ");
			sb.append("  WHERE c.del_flag = '0' ");
			if(StringUtils.isNotBlank(condition)){//添加其他的查询条件
				sb.append("    "+condition+" ");
			}
			sb.append("  GROUP BY c."+columns);
		}
		return sb.toString();
	}

	/**
	 * 根据key生成相应的统计数据
	 * @author 王鹏
	 * @version 2018-06-10 16:55:28
	 * @param key
	 * @return
	 */
	String getPeopleCountByAreaDateSql(String key, String startDate, String endDate, String condition) {
		StringBuffer sb = new StringBuffer();
		//获取表名
		String tableName = CountPeopleByAreaEnum.getTable(key);
		//获取列名
		String areaColumn = CountPeopleByAreaEnum.getAreaColumn(key);
		//获取日期列名
		String dateColumn = CountPeopleByAreaEnum.getDateColumn(key);
		//获取日期格式
		String datePattern = DateUtils.getDatePattern(startDate);
		if(areaColumn.indexOf("#,#") > 0) {
			//如果出现特殊分隔符，说明需要关联机构表进行统计
			//拆分字段,0是机构表明;1是外键;2是机构地区id
			String[] columnArr = areaColumn.split("#,#");
			sb.append(" SELECT u."+columnArr[2]+" AS area_id, count(*) AS count, ");
			sb.append("        date_format(c."+dateColumn+",'"+datePattern+"') date ");
			//关联机构表
			sb.append("   FROM "+ tableName+" c, "+columnArr[0]+" u");
			sb.append("  WHERE c.del_flag = '0' ");
			//查询条件
			sb.append("    AND u.del_flag = '0' ");
			sb.append("    AND c."+columnArr[1]+" = u.id ");
			sb.append("    AND date_format(c."+dateColumn+",'"+datePattern+"') >= '"+startDate+"' ");
			sb.append("    AND date_format(c."+dateColumn+",'"+datePattern+"') <= '"+endDate+"' ");
			if(StringUtils.isNotBlank(condition)){//添加其他的查询条件
				sb.append("    "+condition+" ");
			}
			sb.append("  GROUP BY u."+columnArr[2]+", date_format(c."+dateColumn+",'"+datePattern+"')");
		}
		else {//没有特殊分隔符，正常的单表统计
			sb.append(" SELECT "+areaColumn+" AS area_id, count(*) AS count, ");
			sb.append("        date_format(c."+dateColumn+",'"+datePattern+"') date ");
			sb.append("   FROM "+ tableName+" c ");
			sb.append("  WHERE c.del_flag = '0' ");
			sb.append("    AND date_format(c."+dateColumn+",'"+datePattern+"') >= '"+startDate+"' ");
			sb.append("    AND date_format(c."+dateColumn+",'"+datePattern+"') <= '"+endDate+"' ");
			if(StringUtils.isNotBlank(condition)){//添加其他的查询条件
				sb.append("    "+condition+" ");
			}
			sb.append("  GROUP BY c."+areaColumn+", date_format(c."+dateColumn+",'"+datePattern+"')");
		}
		return sb.toString();
	}

	/**
	 * 根据key生成相应的统计数据
	 * @author 王鹏
	 * @version 2018-06-10 16:55:28
	 * @param key
	 * @param condition 其他的查询条件
	 * @return
	 */
	String getAgencyCountSql(String key, String condition) {
		StringBuffer sb = new StringBuffer();
		//获取表名
		String tableName = CountAgencyByAreaEnum.getTable(key);
		//获取列名
		String columns = CountAgencyByAreaEnum.getAreaColumn(key);
		//按区域统计机构信息
		sb.append(" SELECT "+columns+" AS area_id, count(*) AS count ");
		sb.append("   FROM "+ tableName+" c ");
		sb.append("  WHERE c.del_flag = '0' ");
		if(StringUtils.isNotBlank(condition)){//添加其他的查询条件
			sb.append("    "+condition+" ");
		}
		sb.append("  GROUP BY c."+columns);
		return sb.toString();
	}

	/**
	 * 统计机构和人员的综合信息
	 * jg=机构：司法所、人民调解委员会、基层法律服务所
	 * ry=人员：司法所工作人员、人民调解员、基层法律服务工作者、人民监督员
	 * 示例:
	 * {
	 * 	xData: ['锡林郭勒盟', '二连浩特市', '锡林浩特市', '阿巴嘎旗',
	 * 		'苏尼特左旗', '苏尼特右旗', '东乌珠穆沁旗', '西乌珠穆沁旗', '太仆寺旗',
	 * 		'镶黄旗', '正镶白旗', '正蓝旗', '多伦县', '乌拉盖管理区'
	 * 	],
	 * 	series: [{
	 * 		name: '司法所',
	 * 		data: [120, 200, 150, 120, 200, 150, 120, 200, 150, 120, 200, 150, 120, 200],
	 * 		type: 'bar'
	 *    }
	 * 	]
	 * }
	 * @author 王鹏
	 * @param type jg=机构;ry=人员
	 * @version 2018-7-15 11:42:31
	 * @return
	 */
	@RequestMapping("/30")
    public Map<String, Object> countAgencyAndPeopleByArea(String type){
		//最终数据集合
		Map<String, Object> resultMap = Maps.newHashMap();
		//获取全部旗县(不要父节点)
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		//设置xData对象
		List<String> xDataList = Lists.newArrayList();
		for (int i = 0; i < areaList.size(); i++) {
			xDataList.add(areaList.get(i).getName());
		}
		resultMap.put("xData", xDataList);
		
		//设置series
		List<Map<String, Object>> seriesList = null;
		if("jg".equalsIgnoreCase(type)) {
			//司法所、人民调解委员会、基层法律服务所
			seriesList = countBarByParam("jg", COUNT_AGENCY_KEYS);
		}
		else if("ry".equalsIgnoreCase(type)) {
			//司法所工作人员、人民调解员、基层法律服务工作者、人民监督员
			seriesList = countBarByParam("ry", COUNT_PEOPLE_KEYS);
		}
		resultMap.put("series", seriesList);
		return resultMap;
	}
	/**
	 * 根据key和name进行统计，并返回饼状图需要的数据格式
	 * @author 王鹏
	 * @version 2018-07-15 17:08:09
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> countBarByParam(String type, String[][] param){
		//设置series
		List<Map<String, Object>> seriesList = Lists.newArrayList();
		List<CountByArea> countList = null;//统计结果
		List<Integer> seriesCountList = null;//统计数据
		Map<String, Object> seriesMap = null;
		for (int i = 0; i < param[0].length; i++) {
			seriesMap = Maps.newHashMap();
			//放置统计数据名字
			seriesMap.put("name", param[1][i]);
			//放置类型
			seriesMap.put("type", "bar");
			//获取统计数据
			if("jg".equals(type)) {//机构统计
				countList = countAgencyByArea(param[0][i]);
			}
			else if("ry".equals(type)) {//人员统计
				countList = countPeopleByArea(param[0][i]);
			}
			else {
				countList = Lists.newArrayList();
			}
			//循环向图表数据中赋值seriesCountList
			//因为地区和统计语句中的排序方式一致，地区数据数量一致，所以可以直接赋值，无需校验地区是否匹配
			seriesCountList = Lists.newArrayList();
			for (int j = 0; j < countList.size(); j++) {
				seriesCountList.add(countList.get(j).getCount());
			}
			//放置统计数据
			seriesMap.put("data", seriesCountList);
			//统计完一类的数据
			seriesList.add(seriesMap);
		}
		return seriesList;
	}

	/**
	 * 根据key按地区和日期统计相应的内容
	 * @author 王鹏
	 * @version 2018-06-10 16:54:36
	 * @param key
	 * @return
	 */
	public Map<String, Object> countPeopleByAreaDate_EnumKey(String key, String startDate, String endDate) {
		Map<String, Object> resultMap = Maps.newHashMap();
		//获取全部旗县(不要父节点)
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		//获取所有日期当做x轴坐标
		List<String> xData = DateUtils.getAllDayBetweenDate(startDate, endDate);
		resultMap.put("xData", xData);
		//获取统计结果
		List<CountByArea> cpbaList = countPeopleByAreaDate(key, startDate, endDate, null);
		//放入旗县的数据集合中
		List<Map<String, Object>> seriesList = Lists.newArrayList();
		Map<String, Object> areaMap = null;//旗县统计数据
		List<Integer> areaCountList = null;//旗县统计集合
		CountByArea areaCount = null;//具体统计数据
		boolean hasCount = false;//是否有数据
		for (int i = 0; i < areaList.size(); i++) {
			areaMap = Maps.newHashMap();
			areaMap.put("name", areaList.get(i).getName());//旗县名称
			areaMap.put("type", "line");//折线图
			areaCountList = Lists.newArrayList();
			for (int j = 0; j < xData.size(); j++) {
				hasCount = false;//该旗县在改日期是否有数据
				for (int k = 0; k < cpbaList.size(); k++) {
					areaCount = cpbaList.get(k);//获取统计数据
					//判断旗县、日期是否匹配
					if(areaList.get(i).getId().equals(areaCount.getArea().getId())
							&& xData.get(j).equals(areaCount.getDate())) {
						areaCountList.add(areaCount.getCount());
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
