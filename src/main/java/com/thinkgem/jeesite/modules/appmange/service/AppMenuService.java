/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.service;

import java.util.ArrayList;
import java.util.List;

import com.thinkgem.jeesite.api.dto.vo.AppMenuVo;
import com.thinkgem.jeesite.common.utils.Constants;
import com.thinkgem.jeesite.modules.appmange.dao.SysRoleAppMenuDao;
import com.thinkgem.jeesite.modules.appmange.entity.SysRoleAppMenu;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.appmange.entity.AppMenu;
import com.thinkgem.jeesite.modules.appmange.dao.AppMenuDao;

/**
 * app应用Service
 * @author hejia
 * @version 2018-04-19
 */
@Service
@Transactional(readOnly = true)
public class AppMenuService extends CrudService<AppMenuDao, AppMenu> {


	@Autowired
	SysRoleAppMenuDao sysRoleAppMenuDao;
	@Autowired
	AppMenuDao appMenuDao;

	public List<AppMenuVo> loadMenu(){
		List<Role> roles = UserUtils.getRoleList();
		//未登录视为普通用户
		if(null == roles || roles.size() < 1){
		    Role defRole = new Role();
		    defRole.setId(Constants.PUBLIC_NOMAL_PEOPLE);
		    roles = new ArrayList<>();
            roles.add(defRole);
        }
		List<SysRoleAppMenu> sram = sysRoleAppMenuDao.findByRoleIdS(roles);
        List<AppMenuVo> appMenuVos = new ArrayList<>();
        if(null == sram || sram.size() < 1){
            return appMenuVos;
        }
		List<AppMenu> appMenus = appMenuDao.findByIdsAndShow(sram);
		for(AppMenu menu:appMenus){
		    appMenuVos.add(new AppMenuVo(menu));
        }
        return appMenuVos;
	}


	public AppMenu get(String id) {
		return super.get(id);
	}
	
	public List<AppMenu> findList(AppMenu appMenu) {
		return super.findList(appMenu);
	}
	
	public Page<AppMenu> findPage(Page<AppMenu> page, AppMenu appMenu) {
		return super.findPage(page, appMenu);
	}
	
	@Transactional(readOnly = false)
	public void save(AppMenu appMenu) {
		super.save(appMenu);
	}
	
	@Transactional(readOnly = false)
	public void delete(AppMenu appMenu) {
		super.delete(appMenu);
	}
	
}