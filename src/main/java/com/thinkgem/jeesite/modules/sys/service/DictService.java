/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.modules.sys.dao.DictDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 字典Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class DictService extends CrudService<DictDao, Dict> {
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}

	@Transactional(readOnly = false)
	public void save(Dict dict) {
		super.save(dict);
		removeCache(dict.getParentId());
	}

	@Transactional(readOnly = false)
	public void delete(Dict dict) {
		super.delete(dict);
		removeCache(dict.getParentId());
	}
   private void removeCache(String parentId){
	   List<Dict> typeList= dao.findLanguageList(); 
	   for (Dict dict : typeList) {
		   CacheUtils.remove(DictUtils.CACHE_DICT_MAP+"_"+dict.getValue());
		   CacheUtils.remove(DictUtils.CACHE_DICT_MAP+"ONE_"+dict.getValue());
		   if(parentId!=""){
			   CacheUtils.remove(DictUtils.CACHE_DICT_MAP+"TWO_"+parentId+dict.getValue());
		   }
	 }
   }
   public Page<Dict> findListByLanguage(Page<Dict> page, Dict dict){
	         dict.setPage(page);
			 page.setList(dao.findListByLanguage(dict));
			return page;
	}
  
   public List<Dict> selectFindList(Dict dict) {
		return dao.selectFindList(dict);
	}
   @Transactional(readOnly = false)
   public int deleteFindList(String type,String value,String languageType) {
	    removeCache("");
		return dao.deleteFindList(type,value,languageType);
	}
   public List<Dict> getDictByLanAndTypeAndVal(Dict dict){
	   return dao.getDictByLanAndTypeAndVal(dict);
   }
   public Dict getDictByLanAndLabel(Dict dict){
	   return dao.getDictByLanAndLabel( dict);
   }
   @Transactional(readOnly = false)
	public int batchDelete(List<String>  batchid) {
	     removeCache("");
		 return dao.batchDelete(batchid);
	}
   @Transactional(readOnly = false)
   public int batchSave(List<Dict> list){
	   return dao.batchSave(list);
   }
}
