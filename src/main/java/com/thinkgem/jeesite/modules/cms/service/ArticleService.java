/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.dao.ArticleDao;
import com.thinkgem.jeesite.modules.cms.dao.ArticleDataDao;
import com.thinkgem.jeesite.modules.cms.dao.CategoryDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.ArticleCount;
import com.thinkgem.jeesite.modules.cms.entity.ArticleData;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.utils.FormatStyle;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 文章Service
 * @author ThinkGem
 * @version 2013-05-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends CrudService<ArticleDao, Article> {

	@Autowired
	private ArticleDataDao articleDataDao;
	@Autowired
	private CategoryDao categoryDao;
	
	@Transactional(readOnly = false)
	public Page<Article> findPage(Page<Article> page, Article article, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate =  (Date)CacheUtils.get("updateExpiredWeightDateByArticle");
		if (updateExpiredWeightDate == null || (updateExpiredWeightDate != null 
				&& updateExpiredWeightDate.getTime() < new Date().getTime())){
			dao.updateExpiredWeight(article);
			CacheUtils.put("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
		}
		if (article.getCategory()!=null && StringUtils.isNotBlank(article.getCategory().getId()) && !Category.isRoot(article.getCategory().getId())){
			Category category = categoryDao.get(article.getCategory().getId());
			if (category==null){
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			article.setCategory(category);
		}
		else{
			article.setCategory(new Category());
		}
		article.getSqlMap().put("dsf", dataScopeFilter(article.getCurrentUser(), "of", "a"));
		return super.findPage(page, article);
		
	}

	@Transactional(readOnly = false)
	public void save(Article article) {
		
		 if(article.getArticleData().getContent()!=null && article.getArticleData().getContent().length()>=0) {
			ArticleData articleData= article.getArticleData();
			 String str=articleData.getContent();
			 //替换匹配固定值
			 str =FormatStyle.reaplance(str);
			 //替换匹配正则
			 str =FormatStyle.reaplance1(str);
			 articleData.setContent(StringEscapeUtils.unescapeHtml4(str));
			 article.setArticleData(articleData);
		 }
		
		// 如果没有审核权限，则将当前内容改为待审核状态
		if (!UserUtils.getSubject().isPermitted("cms:article:audit")){
			article.setDelFlag(Article.DEL_FLAG_AUDIT);
		}
		// 如果栏目不需要审核，则将该内容设为发布状态
		if (article.getCategory()!=null&&StringUtils.isNotBlank(article.getCategory().getId())){
			Category category = categoryDao.get(article.getCategory().getId());
			article.setSiteId(category.getSite().getId());
			if (!Global.YES.equals(category.getIsAudit())){
				article.setDelFlag(Article.DEL_FLAG_NORMAL);
			}
		}
		article.setUpdateBy(UserUtils.getUser());
		article.setUpdateDate(new Date());
        if (StringUtils.isNotBlank(article.getViewConfig())){
            article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
        }
        if(dao.articleHotCount(article)>3){
        	Article  oldArticle=dao.getOldHost(article);
        	oldArticle.setPosid(oldArticle.getPosid().replace("1", ""));
        	dao.update(oldArticle);
        }
        ArticleData articleData = new ArticleData();
		if (StringUtils.isBlank(article.getId())){
			article.preInsert();
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			User user=UserUtils.getUser();
			article.setCompany(user.getCompany());
			article.setOffice(user.getOffice());
			dao.insert(article);
			articleDataDao.insert(articleData);
		}else{
			article.preUpdate();
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			dao.update(article);
			articleDataDao.update(article.getArticleData());
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Article article, Boolean isRe) {
		if(isRe!=null&&isRe){
			article.setDelFlag("0");
		}else{
			article.setDelFlag("1");	
		}
		super.delete(article);
	}
	
	/**
	 * 通过编号获取内容标题
	 * @return new Object[]{栏目Id,文章Id,文章标题}
	 */
	public List<Object[]> findByIds(String ids) {
		if(ids == null){
			return new ArrayList<Object[]>();
		}
		List<Object[]> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids,",");
		Article e = null;
		for(int i=0;(idss.length-i)>0;i++){
			e = dao.get(idss[i]);
			list.add(new Object[]{e.getCategory().getId(),e.getId(),StringUtils.abbr(e.getTitle(),50)});
		}
		return list;
	}
	
	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}
	
	/**
	 * 更新索引
	 */
	public void createIndex(){
	}
	
	/**
	 * 全文检索
	 */
	// 暂不提供检索功能
	public Page<Article> search(Page<Article> page, String q, String categoryId, String beginDate, String endDate){
		
		return page;
	}
	
	/**
	 * 各旗县普法宣传文章统计图
	 * @author 王鹏
	 * @version 2018-07-12 15:44:12
	 * @param ac
	 * @return
	 */
	public Map<String, List<String>> countArticle(ArticleCount ac){
		Map<String, List<String>> map = Maps.newHashMap();
		if(StringUtils.isNotBlank(ac.getDatePattern())) {
			//将日期格式改为mysql的日期格式
			ac.setDatePattern(ac.getDatePattern().replace("yyyy", "%Y").replace("MM", "%m").replace("dd", "%d"));
		}
		if(StringUtils.isNotBlank(ac.getAreaIds())) {
			//处理选择地区(改为符合mysql查询条件的格式)
			ac.setAreaArr(ac.getAreaIds().split(","));
		}
		//统计结果
		List<ArticleCount> list = dao.countArticle(ac);
		//将结果改为符合报表使用的格式
		List<String> countList = Lists.newArrayList();
		List<String> nameList = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			countList.add(String.valueOf(list.get(i).getCount()));
			nameList.add(list.get(i).getArea().getName());
		}
		map.put("counts", countList);
		map.put("names", nameList);
		return map;
	}

	/**
	 * 各旗县普法宣传文章统计图(表格形式展示)
	 * @author 王鹏
	 * @version 2018-07-12 15:44:12
	 * @param ac
	 * @return
	 */
	public List<List<String>> countArticleForTable(ArticleCount ac, List<Area> areaList) {
		List<List<String>> list = Lists.newArrayList();
		if(StringUtils.isNotBlank(ac.getDatePattern())) {
			//将日期格式改为mysql的日期格式
			ac.setDatePattern(ac.getDatePattern().replace("yyyy", "%Y").replace("MM", "%m").replace("dd", "%d"));
		}
		if(StringUtils.isNotBlank(ac.getAreaIds())) {
			//处理选择地区(改为符合mysql查询条件的格式)
			ac.setAreaArr(ac.getAreaIds().split(","));
		}
		//设置按表格统计
		ac.setForTable("true");
		//统计结果
		List<ArticleCount> countList = dao.countArticle(ac);
		//拿到两个日期之间的全部结果
		List<String> dateList = DateUtils.getAllDayBetweenDate(ac.getStartDate(), ac.getEndDate());
		//初始化表头
		List<String> tr = Lists.newArrayList();
		tr.add("日期");
		String[] areaArr = ac.getAreaArr();
		if(ac.getAreaArr()==null) {//如果没有选择地区，那默认统计全部的
			areaArr = new String[areaList.size()];
			for (int i = 0; i < areaList.size(); i++) {//全部地区id
				areaArr[i] = areaList.get(i).getId();
				tr.add(areaList.get(i).getName());//放入地区名称
			}
		}
		else {//安装选择的地区统计
			for (int i = 0; i < areaArr.length; i++) {//选中的地区id
				for (int j = 0; j < areaList.size(); j++) {//全部地区id
					if(areaArr[i].equals(areaList.get(j).getId())) {
						tr.add(areaList.get(j).getName());//放入地区名称
						break;
					}
				}
			}
		}
		list.add(tr);//添加表头
		//循环数据
		List<String> td = null;//数据集合
		ArticleCount countAc = null;//统计数据
		boolean isArea = false;//比对是否与当前日期和地区匹配,默认没有匹配
		for (int i = 0; i < dateList.size(); i++) {
			td = Lists.newArrayList();
			td.add(dateList.get(i));//放入时间
			//保存地区对应的数据
			for (int j = 0; j < areaArr.length; j++) {
				//是否匹配到了地区
				//比对是否与当前日期和地区匹配
				isArea = false;//默认没有匹配
				for (int k = 0; k < countList.size(); k++) {
					countAc = countList.get(k);//拿到统计数据
					if(dateList.get(i).equals(countAc.getCountDate())
							&& areaArr[j].equals(countAc.getArea().getId())) {
						td.add(String.valueOf(countAc.getCount()));
						isArea = true;//匹配到了，就退出此次循环
						break;
					}
				}
				if(!isArea) {//如果没有匹配到，则补0
					td.add("0");
				}
			}
			list.add(td);//一条数据添加完成
		}
		return list;
	}
}
