/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.dao.DictDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class DictUtils {
	
	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);

	public static final String CACHE_DICT_MAP = "dictMap";

	public static String getDictLabel(String value, String type){
		return getDictLabel(value, type, "");
	}
	public static String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}
	
	public static String getDictLabels(String languageType, String type,String value, String defaultLabel){
		if(languageType==null||"".equals(languageType)){
			languageType=getCurrentLanguage();
		}
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictListByLanguage(type,languageType)){
				if (type.equals(dict.getType()) && languageType.equals(dict.getLanguageType())&& value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultLabel;
	}

	public static String getDictValue(String languageType, String type,String label, String defaultLabel){
		if(languageType==null||"".equals(languageType)){
			languageType=getCurrentLanguage();
		}
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictListByLanguage(type,languageType)){
				if (type.equals(dict.getType()) && languageType.equals(dict.getLanguageType())&&label.equals(dict.getLabel())){
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}
	
	public static List<Dict> getDictList(String type){
		String language = getCurrentLanguage();
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP+"_"+language);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			Dict d=new Dict();
			d.setLanguageType(language);
			for (Dict dict : dictDao.findList(d)){
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP+"_"+language, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}

	/**
	 * 根据字典类型及具体值查询单个字典信息
	 * 附加一个根据语言查询的重载方法
	 * @author 王鹏
	 * @version 2018-04-18 10:49:07
	 * @param type
	 * @return
	 */
	public static Dict getDictByTypeValue(String type, String value){
		return getDictByTypeValue(type, value, null);
	}
	public static Dict getDictByTypeValue(String type, String value, String language){
		if(StringUtils.isBlank(value)) {
			return null;
		}
		language = StringUtils.isBlank(language)?getCurrentLanguage():language;
		List<Dict> list = getDictListByLanguage(type, language);
		Dict dict = null;
		for (int i = 0; i < list.size(); i++) {
			if(value.equals(list.get(i).getValue())) {
				dict = list.get(i);
				break;
			}
		}
		return dict;
	}
	
	public static List<Dict> getDictListByLanguage(String type,String language){
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP+"_"+language);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			Dict d=new Dict();
			d.setLanguageType(language);
			for (Dict dict : dictDao.findList(d)){
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP+"_"+language, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	/**
	 * 返回字典列表（JSON）
	 * @param type
	 * @return
	 */
	public static String getDictListJson(String type){
		return JsonMapper.toJsonString(getDictList(type));
	}
	public static String getCurrentLanguage(){
		String language = (String)UserUtils.getSession().getAttribute("lang");
		if(language==null||"".equals(language)){
			language=Global.getConfig("langeageType");
		}
       return language;
	}
	/**
	 * 获取一级字典
	 * @param type
	 * @return
	 */
	public static List<Dict> getOneDictList(String type){
		String language = getCurrentLanguage();
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP+"ONE_"+language);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			Dict d=new Dict();
			d.setParentId("0");
			d.setLanguageType(language);
			for (Dict dict : dictDao.findList(d)){
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP+"ONE_"+language, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	/**
	 * 获取二级字典
	 * @param type
	 * @return
	 */
	public static List<Dict> getChildrenDictList(String type,String parentId){
		if(StringUtils.isBlank(parentId)) {
			return Lists.newArrayList();
		}else {
			String language = getCurrentLanguage();
			@SuppressWarnings("unchecked")
			Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP+"TWO_"+parentId+language);
			if (dictMap==null){
				dictMap = Maps.newHashMap();
				Dict d=new Dict();
				d.setParentId(parentId);
				d.setLanguageType(language);
				for (Dict dict : dictDao.findList(d)){
					List<Dict> dictList = dictMap.get(dict.getType());
					if (dictList != null){
						dictList.add(dict);
					}else{
						dictMap.put(dict.getType(), Lists.newArrayList(dict));
					}
				}
				CacheUtils.put(CACHE_DICT_MAP+"TWO_"+parentId+language, dictMap);
			}
			List<Dict> dictList = dictMap.get(type);
			if (dictList == null){
				dictList = Lists.newArrayList();
			}
			return dictList;
		}
	}
}
