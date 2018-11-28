package com.thinkgem.jeesite.modules.sys.utils;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.language.dao.SysMunlLangDao;

public class LanguageUtils {
	private static SysMunlLangDao sysMunlLangDao = SpringContextHolder.getBean(SysMunlLangDao.class);
	//通过key和语言类型查询语言内容
	public static String getLanguageContent(String langKey){
	   String langCode = DictUtils.getCurrentLanguage();
	   String content = sysMunlLangDao.findLanguageContent(langKey, langCode);
       return content;
	}
}
