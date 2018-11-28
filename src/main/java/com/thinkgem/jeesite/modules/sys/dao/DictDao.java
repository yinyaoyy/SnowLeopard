/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface DictDao extends CrudDao<Dict> {

	public List<String> findTypeList(Dict dict);
	public List<Dict> findLanguageList();
	public List<Dict> findListByLanguage(Dict dict);//导出数据
	public List<Dict> getDictByLanAndTypeAndVal(Dict dict);
	public Dict getDictByLanAndLabel(Dict dict);
	public int batchDelete(List<String> batchid);//批量删除
	public List<Dict> selectFindList(Dict dict);
	public int deleteFindList(@Param("languageType")String languageType,@Param("type")String type,@Param("value")String value);
	public int batchSave(List<Dict> list);
}
