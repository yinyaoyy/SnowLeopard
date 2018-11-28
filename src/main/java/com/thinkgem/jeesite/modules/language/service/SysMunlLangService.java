/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.language.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.language.entity.SysMunlLang;
import com.thinkgem.jeesite.modules.language.dao.SysMunlLangDao;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * languageService
 * @author language
 * @version 2017-09-01
 */
@Service
@Transactional(readOnly = true)
public class SysMunlLangService extends CrudService<SysMunlLangDao, SysMunlLang> {
	public SysMunlLang get(String id) {
		return super.get(id);
	}
	
	public List<SysMunlLang> findList(SysMunlLang sysMunlLang) {
		return super.findList(sysMunlLang);
	}
	
	public Page<SysMunlLang> findPage(Page<SysMunlLang> page, SysMunlLang sysMunlLang) {
		return super.findPage(page, sysMunlLang);
	}
	
	@Transactional(readOnly = false)
	public void save(SysMunlLang sysMunlLang) {
		super.save(sysMunlLang);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysMunlLang sysMunlLang) {
		super.delete(sysMunlLang);
	}

	public List<SysMunlLang> getLanguageAscription() {
		List<SysMunlLang> list = dao.getLanguageAscription();
		return list;
	}
	@Transactional(readOnly = false)
	public int batchDelete(List<String>  batchid) {
		 return dao.batchDelete(batchid);
	}
	/**
	 * 获取当前语言当前页的数据
	 * 
	 */
	public  Map<String,String> getCurrentPageList(String languageAscription){
		 SysMunlLang sysMunlLang =new SysMunlLang(); 
	     String lang =  DictUtils.getCurrentLanguage();
	     if(lang==null){
	    	 lang=Global.getConfig("langeageType");
	     }
	     sysMunlLang.setLangCode(lang);
	     sysMunlLang.setLanguageAscription(languageAscription);
	     List<SysMunlLang> list=dao.getListByLanguageAndAscrip(sysMunlLang);
	     Map<String,String> map=new HashMap<String,String>();
	     for (SysMunlLang sysMunlLang2 : list) {
	    	 map.put(sysMunlLang2.getLangKey(),sysMunlLang2.getLangContext());
		}
	     return map;
	}
	/**
	 * 根据语言和页获取相应页的数据
	 * 
	 */
	public  Map<String,String> getCurrentPageListByLan(SysMunlLang sysMunlLang){
	     List<SysMunlLang> list=dao.getListByLanguageAndAscrip(sysMunlLang);
	     Map<String,String> map=new HashMap<String,String>();
	     for (SysMunlLang sysMunlLang2 : list) {
	    	 map.put(sysMunlLang2.getLangKey(),sysMunlLang2.getLangContext());
		}
	     return map;
	}
	public int getCountByLanguage(SysMunlLang sysMunlLang){
		return  dao.getCountByLanguage(sysMunlLang);
	}
	public int deleteBylangKey(SysMunlLang sysMunlLang){
		return dao.deleteBylangKey(sysMunlLang);
	}
}