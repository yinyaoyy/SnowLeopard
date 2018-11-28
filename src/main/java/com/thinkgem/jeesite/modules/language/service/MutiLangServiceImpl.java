package com.thinkgem.jeesite.modules.language.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.modules.language.dao.SysMunlLangDao;
import com.thinkgem.jeesite.modules.language.entity.SysMunlLang;
import com.thinkgem.jeesite.modules.sys.dao.DictDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

@Service("mutiLangService")
@Transactional
public class MutiLangServiceImpl implements MutiLangServiceI  {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SysMunlLangDao sysMunlLangDao;
	@Autowired
	private DictDao dictDao;
	@Override
	public void initAllMutiLang(SysMunlLang sysMunlLang) {
		List<Dict> typeList= dictDao.findLanguageList();
		if(sysMunlLang==null){
			sysMunlLang=new SysMunlLang();
		}
		List<SysMunlLang>  lanA=sysMunlLangDao.getLanguageAscriptionList();
		for (int i = 0; i < typeList.size(); i++) {//根据语言进行遍历
			for (SysMunlLang language : lanA) {
				if(language!=null&&language.getLanguageAscription()!=null){
					Map<String, String> mutiLangMapen = new HashMap<String, String>();
					Dict  dict=typeList.get(i);
					sysMunlLang.setLangCode(dict.getValue());
					sysMunlLang.setLanguageAscription(language.getLanguageAscription());
					//获取所有语言归属
					List<SysMunlLang> mutiLang=sysMunlLangDao.findAllLanguageList(sysMunlLang);
					for(SysMunlLang ssMunlLang : mutiLang){
						//根据语言归属存在在不同的map中
						mutiLangMapen.put(ssMunlLang.getLangKey(), ssMunlLang.getLangContext());
					}
					CacheUtils.put(dict.getValue()+"_"+language.getLanguageAscription(), mutiLangMapen);
				 }
			  }
		}
	}

}
