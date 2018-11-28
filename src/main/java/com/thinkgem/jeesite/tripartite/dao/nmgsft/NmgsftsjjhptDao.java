/**
 * 
 */
package com.thinkgem.jeesite.tripartite.dao.nmgsft;

import java.util.List;
import java.util.Map;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.tripartite.dao.SchedulingDao;

/**
 * 内蒙古自治区司法厅数据交换平台
 * 律师事务所相关接口处理类
 * @author 王鹏
 * @version 2018-06-30 17:55:23
 */
@MyBatisDao
public interface NmgsftsjjhptDao extends SchedulingDao {

	/**
	 * 获取当天律师事务所的“统一社会信用代码”
	 * 方便获取律师信息
	 * @author 王鹏
	 * @version 2018-07-01 14:48:35
	 * @param list
	 * @return
	 */
	public List<String> selectLawOfficeCreditNo(String date);
	
	/**
	 * 获取当天公证机构的“机构执业证号、机构代码”
	 * 方便获取公证员信息
	 * @author 王鹏
	 * @version 2018-07-01 14:48:35
	 * @param list
	 * @return
	 */
	public List<Map<String, String>> selectFairOrgs(String date);
	
	/**
	 * 获取当天公证机构的“执业机构许可证号、执业机构统一社会信用 代码”
	 * 方便获取鉴定人信息
	 * @author 王鹏
	 * @version 2018-07-01 14:48:35
	 * @param list
	 * @return
	 */
	public List<Map<String, String>> selectIdtOrgs(String date);
}
