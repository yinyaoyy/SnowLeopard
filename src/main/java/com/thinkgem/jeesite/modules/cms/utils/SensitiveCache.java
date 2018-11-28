package com.thinkgem.jeesite.modules.cms.utils;

import com.thinkgem.jeesite.common.utils.CacheUtils;


/**
 * @author 如初
 * @Date 2018-06-25
 * 敏感词模型汇缓存处理
 */

public class SensitiveCache {
	
	/**
	 * @author 如初
	 * @Date 2018-06-25
	 * 敏感词缓存的常量
	 */
	public static final String CACHE_SENSITIVE = "CACHE_SENSITIVE";
	//优先从缓存中去取
	public static SensitiveWord get(){
		SensitiveWord sensitiveWord = (SensitiveWord)CacheUtils.get(CACHE_SENSITIVE);
		if (sensitiveWord == null){
			sensitiveWord = new SensitiveWord();
			CacheUtils.put(CACHE_SENSITIVE, sensitiveWord);
				
		}
		return sensitiveWord;
	}
	//移除缓存，还没定义时机后期可修改，自己感觉没必要，每次更新字库重启服务就是了
	public static void remove(){
		CacheUtils.remove(CACHE_SENSITIVE);	
		}	
}
