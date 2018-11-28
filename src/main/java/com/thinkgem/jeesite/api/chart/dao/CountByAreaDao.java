/**
 * 
 */
package com.thinkgem.jeesite.api.chart.dao;

import java.util.List;
import com.thinkgem.jeesite.api.chart.entity.CountByArea;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * @author 王鹏
 * @version 2018-06-10 16:24:34
 */
@MyBatisDao
public interface CountByAreaDao extends CrudDao<CountByArea> {
	
	/**
	 * 通用的根据地区统计数量的查询方法
	 * @author 王鹏
	 * @version 2018-06-10 16:46:48
	 * @param cpba
	 * @return
	 */
	List<CountByArea> countByArea(CountByArea cba);

	/**
	 * 通用的根据地区、日期统计数量的查询方法
	 * @author 王鹏
	 * @version 2018-7-16 20:19:15
	 * @param cpba
	 * @return
	 */
	List<CountByArea> countByAreaDate(CountByArea cba);
}
