/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.modules.appmange.entity.SysService;
import com.thinkgem.jeesite.modules.appmange.dao.SysServiceDao;

/**
 * web服务Service
 * @author wanglin
 * @version 2018-04-23
 */
@Service
@Transactional(readOnly = true)
public class SysServiceService extends CrudService<SysServiceDao, SysService> {
	public static final String CACHE_SERVICE_LIST = "serviceList";
	public SysService get(String id) {
		return super.get(id);
	}
	
	public List<SysService> findList(SysService sysService) {
		List<SysService> list=(List<SysService>) CacheUtils.get(CACHE_SERVICE_LIST);
		if(list==null){
			list=super.findList(sysService);
			CacheUtils.put(CACHE_SERVICE_LIST, list);
		 }
		return list;
	}
	
	public Page<SysService> findPage(Page<SysService> page, SysService sysService) {
		return super.findPage(page, sysService);
	}
	
	@Transactional(readOnly = false)
	public void save(SysService sysService) {
		super.save(sysService);
		List<SysService> updateList=Lists.newArrayList();
		List<SysService> reList=Lists.newArrayList();
		String []idArray=null;
		String id=sysService.getId();
		//替换已有的站点
		if(sysService.getSiteId()!=null){
			idArray=sysService.getSiteId().split(",");
			for (int i = 0; i < idArray.length; i++) {
				reList=dao.findListByType(id,idArray[i],"1");
				for (SysService s : reList) {
					s.setSiteId(s.getSiteId().replace(idArray[i], ""));
					dao.update(s);
				}
			}
		}
		if(sysService.getOfficeId()!=null){
			idArray=sysService.getOfficeId().split(",");
			for (int i = 0; i < idArray.length; i++) {
				reList=dao.findListByType(id,idArray[i],"2");
				for (SysService s : reList) {
					s.setOfficeId(s.getOfficeId().replace(idArray[i], ""));
					dao.update(s);
				}
			}
		}
		if(sysService.getOaId()!=null){
			idArray=sysService.getOaId().split(",");
			for (int i = 0; i < idArray.length; i++) {
				reList=dao.findListByType(id,idArray[i],"3");
				for (SysService s : reList) {
					s.setOaId(s.getOaId().replace(idArray[i], ""));
					dao.update(s);
				}
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(SysService sysService) {
		super.delete(sysService);
	}
	public SysService findByOfficeId(String officeId){
		return dao.findByOfficeId(officeId);
	}
	public List<SysService> findBySiteIds(List<String> list){
		return dao.findBySiteIds(list);
	}
}