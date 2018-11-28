/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.VersionManager;
import com.thinkgem.jeesite.modules.sys.dao.VersionManagerDao;

/**
 * 系统版本管理Service
 * @author huangtao
 * @version 2018-06-27
 */
@Service
@Transactional(readOnly = true)
public class VersionManagerService extends CrudService<VersionManagerDao, VersionManager> {
	@Autowired
    public VersionManagerDao versionManagerDao;

	public VersionManager get(String id) {
		return super.get(id);
	}
	
	public List<VersionManager> findList(VersionManager versionManager) {
		return versionManagerDao.findList(versionManager);
	}
	
	public Page<VersionManager> findPage(Page<VersionManager> page, VersionManager versionManager) {
		return super.findPage(page, versionManager);
	}
	
	@Transactional(readOnly = false)
	public void save(VersionManager versionManager) {
		if(StringUtils.isBlank(versionManager.getId())){
			String apk=versionManager.getAndroidUrl();
			if(StringUtils.isBlank(apk)){
				apk="/userfiles/default/user.apk";
			}
			versionManager.setAndroidUrl(apk);
	   }
		super.save(versionManager);
	}
	@Transactional(readOnly = false)
	public void delete(VersionManager versionManager) {
		super.delete(versionManager);
	}
	
}