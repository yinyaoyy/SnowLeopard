/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

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
import com.thinkgem.jeesite.modules.sys.entity.VersionManager;
import com.thinkgem.jeesite.modules.sys.service.VersionManagerService;

/**
 * 系统版本管理Controller
 * @author huangtao
 * @version 2018-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/versionManager")
public class VersionManagerController extends BaseController {

	@Autowired
	private VersionManagerService versionManagerService;
	
	@ModelAttribute
	public VersionManager get(@RequestParam(required=false) String id) {
		VersionManager entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = versionManagerService.get(id);
		}
		if (entity == null){
			entity = new VersionManager();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:versionManager:view")
	@RequestMapping(value = {"list", ""})
	public String list(VersionManager versionManager, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VersionManager> page = versionManagerService.findPage(new Page<VersionManager>(request, response), versionManager); 
		model.addAttribute("page", page);
		return "modules/sys/versionManagerList";
	}

	@RequiresPermissions("sys:versionManager:view")
	@RequestMapping(value = "form")
	public String form(VersionManager versionManager, Model model) {
		model.addAttribute("versionManager", versionManager);
		return "modules/sys/versionManagerForm";
	}

	@RequiresPermissions("sys:versionManager:edit")
	@RequestMapping(value = "save")
	public String save(VersionManager versionManager, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, versionManager)){
			return form(versionManager, model);
		}
		versionManagerService.save(versionManager);
		addMessage(redirectAttributes, "保存系统版本管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/versionManager/?repage";
	}
	
	@RequiresPermissions("sys:versionManager:edit")
	@RequestMapping(value = "delete")
	public String delete(VersionManager versionManager, RedirectAttributes redirectAttributes) {
		versionManagerService.delete(versionManager);
		addMessage(redirectAttributes, "删除系统版本管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/versionManager/?repage";
	}

}