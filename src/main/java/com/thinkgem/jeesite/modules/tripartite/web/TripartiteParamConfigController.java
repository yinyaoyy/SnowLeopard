/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tripartite.web;

import java.util.List;
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
import com.thinkgem.jeesite.modules.tripartite.entity.TripartiteParamConfig;
import com.thinkgem.jeesite.modules.tripartite.service.TripartiteParamConfigService;

/**
 * 与第三方系统对接请求头、参数配置表Controller
 * @author 王鹏
 * @version 2018-06-30
 */
@Controller
@RequestMapping(value = "${adminPath}/tripartite/tripartiteParamConfig")
public class TripartiteParamConfigController extends BaseController {

	@Autowired
	private TripartiteParamConfigService tripartiteParamConfigService;
	
	@ModelAttribute
	public TripartiteParamConfig get(@RequestParam(required=false) String id) {
		TripartiteParamConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tripartiteParamConfigService.get(id);
		}
		if (entity == null){
			entity = new TripartiteParamConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("tripartite:tripartiteParamConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(TripartiteParamConfig tripartiteParamConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TripartiteParamConfig> page = tripartiteParamConfigService.findPage(new Page<TripartiteParamConfig>(request, response), tripartiteParamConfig); 
		model.addAttribute("page", page);
		return "modules/tripartite/tripartiteParamConfigList";
	}

	@RequiresPermissions("tripartite:tripartiteParamConfig:view")
	@RequestMapping(value = {"list1", ""})
	public String list1(TripartiteParamConfig tripartiteParamConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<TripartiteParamConfig> list = tripartiteParamConfigService.findAllParamList(tripartiteParamConfig);
		model.addAttribute("list", list);
		return "modules/tripartite/tripartiteParamConfigList";
	}

	@RequiresPermissions("tripartite:tripartiteParamConfig:view")
	@RequestMapping(value = "form")
	public String form(TripartiteParamConfig tripartiteParamConfig, Model model) {
		model.addAttribute("tripartiteParamConfig", tripartiteParamConfig);
		return "modules/tripartite/tripartiteParamConfigForm";
	}

	@RequiresPermissions("tripartite:tripartiteParamConfig:edit")
	@RequestMapping(value = "save")
	public String save(TripartiteParamConfig tripartiteParamConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tripartiteParamConfig)){
			return form(tripartiteParamConfig, model);
		}
		tripartiteParamConfigService.save(tripartiteParamConfig);
		addMessage(redirectAttributes, "保存与第三方系统对接请求头、参数配置表成功");
		return "redirect:"+Global.getAdminPath()+"/tripartite/tripartiteParamConfig/?repage";
	}
	
	@RequiresPermissions("tripartite:tripartiteParamConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(TripartiteParamConfig tripartiteParamConfig, RedirectAttributes redirectAttributes) {
		tripartiteParamConfigService.delete(tripartiteParamConfig);
		addMessage(redirectAttributes, "删除与第三方系统对接请求头、参数配置表成功");
		return "redirect:"+Global.getAdminPath()+"/tripartite/tripartiteParamConfig/?repage";
	}

}