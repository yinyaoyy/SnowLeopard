/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.Category;

/**
 * 栏目DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface CategoryDao extends TreeDao<Category> {
	
	public List<Category> findModule(Category category);
	

	public List<Category> findByModule(String module);
	public List<Category> findByParentId(String parentId, String isMenu);

	public List<Category> findByParentIdAndSiteId(Category entity);
	
	public List<Map<String, Object>> findStats(String sql);
	public List<Category>  findModules(Category category);
	/**
	 * 通过栏目名获取所属的站点
	 * @param category
	 * @return
	 */
	public List<String>  getSiteByCategoryName(Category category);
	public Category getByCategoryNameAndsite(Category category);
}
