/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.appmange.dao.SysRoleAppMenuDao;
import com.thinkgem.jeesite.modules.appmange.entity.AppMenu;
import com.thinkgem.jeesite.modules.appmange.entity.SysRoleAppMenu;
import com.thinkgem.jeesite.modules.appmange.service.AppMenuService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * app应用Controller
 * @author hejia
 * @version 2018-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/appmange/appMenu")
public class AppMenuController extends BaseController {

	@Autowired
	private AppMenuService appMenuService;
	@Autowired
	SysRoleAppMenuDao sysRoleAppMenuDao;

	@ModelAttribute
	public AppMenu get(@RequestParam(required=false) String id) {
		AppMenu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = appMenuService.get(id);
		}
		if (entity == null){
			entity = new AppMenu();
		}
		return entity;
	}
	
	@RequiresPermissions("appmange:appMenu:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppMenu appMenu, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AppMenu> page = appMenuService.findPage(new Page<AppMenu>(request, response), appMenu); 
		model.addAttribute("page", page);
		return "modules/appmange/appMenuList";
	}

	@RequiresPermissions("appmange:appMenu:view")
	@RequestMapping(value = "form")
	public String form(AppMenu appMenu, Model model) {
		model.addAttribute("appMenu", appMenu);
		model.addAttribute("allHaveRoles",sysRoleAppMenuDao.findByAppMenuId(appMenu.getId()));
		model.addAttribute("allRoles", UserUtils.getRoleList());
		return "modules/appmange/appMenuForm";
	}

	@RequiresPermissions("appmange:appMenu:edit")
	@RequestMapping(value = "save")
    @Transactional
	public String save(AppMenu appMenu,String[] roleIdList, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, appMenu)){
			return form(appMenu, model);
		}
		appMenuService.save(appMenu);
		if(null != roleIdList && roleIdList.length > 0) {
            SysRoleAppMenu sysRoleAppMenu = null;
            SysRoleAppMenu param = new SysRoleAppMenu();
            param.setAppMenuId(appMenu.getId());
            for (String roleId:roleIdList){
                param.setRoleId(roleId);
                sysRoleAppMenu = sysRoleAppMenuDao.findByAppMenuIdAndRoleId(param);
                if(null == sysRoleAppMenu){
                    param.preInsert();
                    sysRoleAppMenuDao.insert(param);
                }
            }
        }
		addMessage(redirectAttributes, "保存应用成功");
		return "redirect:"+Global.getAdminPath()+"/appmange/appMenu/?repage";
	}
	
	@RequiresPermissions("appmange:appMenu:edit")
	@RequestMapping(value = "delete")
	public String delete(AppMenu appMenu, RedirectAttributes redirectAttributes) {
		appMenuService.delete(appMenu);
		addMessage(redirectAttributes, "删除应用成功");
		return "redirect:"+Global.getAdminPath()+"/appmange/appMenu/?repage";
	}

}