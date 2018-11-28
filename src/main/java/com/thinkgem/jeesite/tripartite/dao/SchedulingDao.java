/**
 * 
 */
package com.thinkgem.jeesite.tripartite.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.tripartite.entity.TripartiteTaskEntity;

/**
 * 与第三方系统对接调度服务
 * 获取调度实体信息
 * @author 王鹏
 * @version 2018-07-01 16:55:05
 */
@MyBatisDao
public interface SchedulingDao {

	/**
	 * 根据id获取调度信息
	 * @author 王鹏
	 * @version 2018-07-01 16:56:47
	 * @param id
	 * @return
	 */
	public TripartiteTaskEntity get(String id);
	
	/**
	 * 默认的插入数据方法
	 * 自动生成id和importDate，其他需通过配置或其他方式处理
	 * @author 王鹏
	 * @version 2018-07-02 18:10:14
	 * @param tableName
	 * @param columns
	 * @param list
	 * @return
	 */
	public int insert(@Param("table")String tableName, @Param("columns")String[] columns,
			@Param("list")List<Map<String, Object>> list);
}
