/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.language.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.language.entity.SysMunlLang;

/**
 * languageDAO接口
 * @author language
 * @version 2017-09-01
 */
@MyBatisDao
public interface SysMunlLangDao extends CrudDao<SysMunlLang> {
	public List<SysMunlLang> getLanguageAscriptionList();//获取全部语言归属
	 public List<SysMunlLang> findAllLanguageList(SysMunlLang sysMunlLang);//根据语言和语言归属查询
	 public List<SysMunlLang> getLanguageAscription();//查询全部的语言归属
	 public int batchDelete(List<String> batchid);//批量删除
	 public List<SysMunlLang> getListByLanguageAndAscrip(SysMunlLang sysMunlLang);
	 public int getCountByLanguage(SysMunlLang sysMunlLang);//获取某种语言，某种语言归属下的个数
     public int deleteBylangKey(SysMunlLang sysMunlLang);      
     public void batchAdd(List<SysMunlLang> list);
     public void updateLangKeyByLangKey(@Param("oldLangKey") String oldLangKey,@Param("newLangKey")String newLangKey);
     //通过key和语言类型查询语言内容
     public String findLanguageContent(@Param("langKey") String langKey,@Param("langCode")String langCode);
}