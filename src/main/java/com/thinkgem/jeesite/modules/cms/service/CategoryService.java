/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.modules.cms.dao.CategoryDao;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 栏目Service
 * @author ThinkGem
 * @version 2013-5-31
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends TreeService<CategoryDao, Category> {
	@Autowired
	private SiteService siteService;

	public static final String CACHE_CATEGORY_LIST = "categoryList";
	public static final String CACHE_CATEGORY_OLD_LIST = "categoryOldList";
	private Category entity = new Category();
	
	@SuppressWarnings("unchecked")
	public List<Category> findByUser(boolean isCurrentSite, String module){
		
		List<Category> list = (List<Category>)UserUtils.getCache(CACHE_CATEGORY_LIST);
		if (list == null){
//			User user = UserUtils.getUser();
			Category category = new Category();
			category.setOffice(new Office());
			//category.getSqlMap().put("dsf", dataScopeFilter(user, "o", "u"));
			category.setSite(new Site());
			category.setParent(new Category());
			list = dao.findList(category);
			/*
			 * delete by 王鹏 2018-7-10 10:32:59
			 * 莫名其妙，完全没有意义
			 * // 将没有父节点的节点，找到父节点
			Set<String> parentIdSet = Sets.newHashSet();
			for (Category e : list){
				if (e.getParent()!=null && StringUtils.isNotBlank(e.getParent().getId())){
					boolean isExistParent = false;
					for (Category e2 : list){
						if (e.getParent().getId().equals(e2.getId())){
							isExistParent = true;
							break;
						}
					}
					if (!isExistParent){
						parentIdSet.add(e.getParent().getId());
					}
				}
			}
			if (parentIdSet.size() > 0){
			}*/
			UserUtils.putCache(CACHE_CATEGORY_LIST, list);
		}
		List<Site> siteList= siteService.findPage(new Page<Site>(),new Site()).getList();
		List<Category> categoryList = Lists.newArrayList(); 
		Category c = null;//符合条件的分类
		for (Site site : siteList) {
			//循环栏目，将栏目作为一个顶级分类
			Category category = new Category();
			category.setName(site.getName());
			category.setId(site.getId());
			category.setParentIds("0,");
			//循环所有分类
			for (Category e : list){
				/* 处理分类，将其添加到栏目下，形成树形结构
				 * 1.如果分类是根分类，则每个栏目都要有
				 * 2.如果分类的栏目id不为空且与当前循环的栏目id相同
				 */
				if (Category.isRoot(e.getId()) || (e.getSite()!=null && e.getSite().getId() !=null 
						&& e.getSite().getId().equals(site.getId()))){
					//将当前根分类(栏目)设为此分类的父级
					e.setParent(category);
					//如果模型不为空
					if (StringUtils.isNotEmpty(module)){
						//模型与当前分类中的模型相符，或者当前分类没有设置模型
						if (module.equals(e.getModule()) || "".equals(e.getModule())){
							c = new Category();//重新封装对象，方式同地址覆盖
							c.setId(e.getId());
							c.setParent(new Category(e.getParent().getId()));
							c.setName(e.getName());
							c.setParentIds(e.getParentIds());
							c.setModule(e.getModule());
							categoryList.add(c);
						}
					}else{//或者对模型没有要求
						c = new Category();//重新封装对象，方式同地址覆盖
						c.setId(e.getId());
						c.setParent(new Category(e.getParent().getId()));
						c.setName(e.getName());
						c.setParentIds(e.getParentIds());
						c.setModule(e.getModule());
						categoryList.add(c);
					}
				}
			}
			categoryList.add(category);
		}
		return categoryList;
	}
	@SuppressWarnings("unchecked")
	public List<Category> findByUserOld(boolean isCurrentSite, String module){
		List<Category> list = (List<Category>)UserUtils.getCache(CACHE_CATEGORY_OLD_LIST);
		if (list == null){
//			User user = UserUtils.getUser();
			Category category = new Category();
			category.setOffice(new Office());
			//category.getSqlMap().put("dsf", dataScopeFilter(user, "o", "u"));
			category.setSite(new Site());
			category.setParent(new Category());
			list = dao.findList(category);
			// 将没有父节点的节点，找到父节点
			Set<String> parentIdSet = Sets.newHashSet();
			for (Category e : list){
				if (e.getParent()!=null && StringUtils.isNotBlank(e.getParent().getId())){
					boolean isExistParent = false;
					for (Category e2 : list){
						if (e.getParent().getId().equals(e2.getId())){
							isExistParent = true;
							break;
						}
					}
					if (!isExistParent){
						parentIdSet.add(e.getParent().getId());
					}
				}
			}
			if (parentIdSet.size() > 0){
			}
			UserUtils.putCache(CACHE_CATEGORY_OLD_LIST, list);
		}
		
		if (isCurrentSite){
			List<Category> categoryList = Lists.newArrayList(); 
			for (Category e : list){
				if (Category.isRoot(e.getId()) || (e.getSite()!=null && e.getSite().getId() !=null 
						&& e.getSite().getId().equals(Site.getCurrentSiteId()))){
					if (StringUtils.isNotEmpty(module)){
						if (module.equals(e.getModule()) || "".equals(e.getModule())){
							categoryList.add(e);
						}
					}else{
						categoryList.add(e);
					}
				}
			}
			return categoryList;
		}
		return list;
	}
	public List<Category> findByParentId(String parentId, String siteId){
		Category parent = new Category();
		parent.setId(parentId);
		entity.setParent(parent);
		Site site = new Site();
		site.setId(siteId);
		entity.setSite(site);
		return dao.findByParentIdAndSiteId(entity);
	}
	
	public Page<Category> find(Page<Category> page, Category category) {
		category.setPage(page);
		category.setInMenu(Global.SHOW);
		page.setList(dao.findModule(category));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Category category) {
		category.setSite(new Site(Site.getCurrentSiteId()));
		if (StringUtils.isNotBlank(category.getViewConfig())){
            category.setViewConfig(StringEscapeUtils.unescapeHtml4(category.getViewConfig()));
        }
		super.save(category);
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
		UserUtils.removeCache(CACHE_CATEGORY_OLD_LIST);
		CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
	}
	
	@Transactional(readOnly = false)
	public void delete(Category category) {
		super.delete(category);
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
		UserUtils.removeCache(CACHE_CATEGORY_OLD_LIST);
		CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
	}
	
	/**
	 * 通过编号获取栏目列表
	 */
	public List<Category> findByIds(String ids) {
		List<Category> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids,",");
		if (idss.length>0){
			for(String id : idss){
				Category e = dao.get(id);
				if(null != e){
					list.add(e);
				}
				
			}
		}
		return list;
	}
	
}
