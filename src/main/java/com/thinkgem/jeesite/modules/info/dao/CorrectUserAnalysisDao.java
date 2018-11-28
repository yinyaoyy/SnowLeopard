/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.info.entity.CorrectUserAnalysis;
import com.thinkgem.jeesite.modules.info.entity.CorrectUserAnalysisVo;

/**
 * 社区矫正心理生理分析DAO接口
 * @author wanglin
 * @version 2018-07-28
 */
@MyBatisDao
public interface CorrectUserAnalysisDao extends CrudDao<CorrectUserAnalysis> {
}