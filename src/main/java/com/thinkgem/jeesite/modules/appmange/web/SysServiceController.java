/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.appmange.entity.SysService;
import com.thinkgem.jeesite.modules.appmange.service.SysServiceService;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.SiteService;

/**
 * web服务Controller
 * @author wanglin
 * @version 2018-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/appmange/sysService")
public class SysServiceController extends BaseController {

	@Autowired
	private SysServiceService sysServiceService;
	@Autowired
	private SiteService siteService;

	@ModelAttribute
	public SysService get(@RequestParam(required=false) String id) {
		SysService entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysServiceService.get(id);
		}
		if (entity == null){
			entity = new SysService();
		}
		return entity;
	}
	
	@RequiresPermissions("appmange:sysService:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysService sysService, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysService> page = sysServiceService.findPage(new Page<SysService>(request, response), sysService); 
		model.addAttribute("page", page);
		return "modules/appmange/sysServiceList";
	}

	@RequiresPermissions("appmange:sysService:view")
	@RequestMapping(value = "form")
	public String form(SysService sysService, Model model) {
		model.addAttribute("allSite", siteService.findList(new Site()));
		model.addAttribute("sysService", sysService);
		return "modules/appmange/sysServiceForm";
	}

	@RequiresPermissions("appmange:sysService:edit")
	@RequestMapping(value = "save")
	public String save(SysService sysService, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysService)){
			return form(sysService, model);
		}
		sysServiceService.save(sysService);
		addMessage(redirectAttributes, "保存web服务成功");
		return "redirect:"+Global.getAdminPath()+"/appmange/sysService/?repage";
	}
	
	@RequiresPermissions("appmange:sysService:edit")
	@RequestMapping(value = "delete")
	public String delete(SysService sysService, RedirectAttributes redirectAttributes) {
		sysServiceService.delete(sysService);
		addMessage(redirectAttributes, "删除web服务成功");
		return "redirect:"+Global.getAdminPath()+"/appmange/sysService/?repage";
	}

}