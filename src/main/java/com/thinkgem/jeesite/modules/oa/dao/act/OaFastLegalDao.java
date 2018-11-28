/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao.act;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.thinkgem.jeesite.api.chart.entity.FastLegalCount;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.act.OaFastLegal;

/**
 * 法律服务直通车DAO接口
 * @author zq
 * @version 2018-07-07
 */
@MyBatisDao
public interface OaFastLegalDao extends CrudDao<OaFastLegal> {

	/**
	 * 全盟法律服务平台接待数量走势图
	 * @author 王鹏
	 * @version 2018-07-12 15:23:10
	 * @param flc
	 * @return
	 */
	public List<FastLegalCount> countFastLegal(FastLegalCount flc);
	
	/**
	 * 是否评论
	 * @param id
	 */
	public void isEvaluate(String id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<T> getApi(String id);
	
}