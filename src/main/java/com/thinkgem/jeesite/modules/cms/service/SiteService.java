/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.dao.SiteDao;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 站点Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class SiteService extends CrudService<SiteDao, Site> {
	@Autowired
	private SystemService  systemService;
	@Autowired
	private SiteRoleService siteRoleService;
	public Page<Site> findPage(Page<Site> page, Site site) {
		site.getSqlMap().put("site", dataScopeFilter(site.getCurrentUser(), "o", "u"));
		if (!UserUtils.getUser().isAdmin()){
			site.setRoleIdList(site.getCurrentUser().getRoleIdList());
			//System.out.println(site.getRoleIdList().size());
		}
		return super.findPage(page, site);
	}
    public Site get(String id){
    	Site site=dao.get(id);
    	site.setRoleList(systemService.findRoleListBySiteId(id));
    	return site;
    }
	@Transactional(readOnly = false)
	public void save(Site site) {
		if (site.getCopyright()!=null){
			site.setCopyright(StringEscapeUtils.unescapeHtml4(site.getCopyright()));
		}
		super.save(site);
		siteRoleService.delete(site.getId());
		dao.insertSiteRole(site);
		CmsUtils.removeCache("site_"+site.getId());
		//CmsUtils.removeCache("siteList");
	}
	
	@Transactional(readOnly = false)
	public void delete(Site site, Boolean isRe) {
		site.setDelFlag(isRe!=null&&isRe?Site.DEL_FLAG_NORMAL:Site.DEL_FLAG_DELETE);
		super.delete(site);
		CmsUtils.removeCache("site_"+site.getId());
		//CmsUtils.removeCache("siteList");
	}
	
}
