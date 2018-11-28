package com.thinkgem.jeesite.api.cms.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.cms.ColumnVo;
import com.thinkgem.jeesite.api.dto.vo.cms.SiteVo;
import com.thinkgem.jeesite.api.dto.vo.cms.SysServiceVo;
import com.thinkgem.jeesite.api.dto.vo.sys.ServerAppVo;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.appmange.entity.SysService;
import com.thinkgem.jeesite.modules.appmange.service.SysServiceService;
import com.thinkgem.jeesite.modules.cms.dao.CategoryDao;
import com.thinkgem.jeesite.modules.cms.dao.SiteDao;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.sys.dao.ServerSourceManageDao;
import com.thinkgem.jeesite.modules.sys.entity.ServerSourceManage;

/**
 * api站点service层
 * @author kakasun
 * @create 2018-04-17 上午10:15
 */
@Service
public class ApiSiteService {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private ServerSourceManageDao serverSourceManageDao;
    public List<SiteVo> getAllSite() {
        List<Site> siteList = siteDao.findAll();
        List<SiteVo> allSite = new ArrayList<>();
        if(null != siteList && siteList.size() > 0){
            SiteVo siteVo = null;
            for (Site site:siteList){
                siteVo = new SiteVo(site);
                allSite.add(siteVo);
            }
        }
        return allSite;
    }

    /**
     * 根据站点id查询站点所有根栏目
     * @param siteId
     * @return
     */
    public List<ColumnVo> getAllColumnsBySiteId(String siteId) {
        List<Category> categories = categoryService.findByParentId(null,siteId);
        List<ColumnVo> columnVoList = new ArrayList<>();
        if(null != categories && categories.size() > 0){
            ColumnVo columnVo = null;
            for(Category c:categories){
            		columnVo = new ColumnVo(c);
                    columnVoList.add(columnVo);
            }
        }
        return columnVoList;
    }
    public   List<SysServiceVo> allAllService(){
        List<SysService> sysServiceList = sysServiceService.findList(new SysService());
        List<SysServiceVo> sysServiceVoList=Lists.newArrayList();
        for (SysService sysService : sysServiceList) {
        	sysServiceVoList.add(new SysServiceVo(sysService));
		}
       return sysServiceVoList;
    }
     /**
      * 根据人员机构获取服务信息及其包含的栏目信息，人员机构信息
      * @param officeId
      * @return
      */
	public SysServiceVo findByOfficeId(String officeId){
		SysServiceVo sysServiceVo=new SysServiceVo(sysServiceService.findByOfficeId(officeId));
		return this.findByService(sysServiceVo);
	}
    /**
     * 根据服务id获取服务信息及其包含的栏目信息，人员机构信息
     * @param serviceId
     * @return
     */
	public SysServiceVo findByServiceId(String serviceId){
		SysServiceVo sysServiceVo=new SysServiceVo(sysServiceService.get(serviceId));
		return this.findByService(sysServiceVo);
	}

	  /**
     * 丰富服务内容
     * @param sysServiceVo
     * @return
     */
	private SysServiceVo findByService(SysServiceVo sysServiceVo){
		List<Map<String,String>> siteList=Lists.newArrayList();
		List<Map<String,String>> officeList=Lists.newArrayList();
		for (String siteId: sysServiceVo.getSiteIdList()) {
			if(StringUtils.isNotEmpty(siteId)){
				Category category=new Category();
				category.setInMenu("1");
				Site site=new Site(siteId);
				category.setSite(site);
				List<Category> list=categoryDao.findModules(category);
				for (Category c : list) {
					Map map=new HashMap<>();
					map.put("categoryId", c.getId());
					map.put("categoryName", c.getName());
					siteList.add(map);
				}
			}
		}
		sysServiceVo.setSiteList(siteList);
		return sysServiceVo;
	}
	 /**
     * 通过栏目名获取栏目
     * @param category
     * @return
     */
	public  List<ServerAppVo> findByCategoryName(BaseForm<?> form,Category category){
		List<String> list=	categoryDao.getSiteByCategoryName(category);
		List<ServerSourceManage> sysServiceList=serverSourceManageDao.findByServiceId(list);
		List<ServerAppVo> sysServiceVoList=Lists.newArrayList();
		for (ServerSourceManage sysService : sysServiceList) {
			sysServiceVoList.add(new ServerAppVo(form,sysService));
		}
		return sysServiceVoList;
	}
}
