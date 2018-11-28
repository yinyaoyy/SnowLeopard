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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.tripartite.entity.TripartiteSystemConfig;
import com.thinkgem.jeesite.modules.tripartite.service.TripartiteSystemConfigService;
import com.thinkgem.jeesite.tripartite.util.TripartiteTypeEnum;

/**
 * 与第三方系统对接配置表Controller
 * @author 王鹏
 * @version 2018-06-23
 */
@Controller
@RequestMapping(value = "${adminPath}/tripartite/tripartiteSystemConfig")
public class TripartiteSystemConfigController extends BaseController {

	@Autowired
	private TripartiteSystemConfigService tripartiteSystemConfigService;
	
	@ModelAttribute
	public TripartiteSystemConfig get(@RequestParam(required=false) String id) {
		TripartiteSystemConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tripartiteSystemConfigService.get(id);
		}
		if (entity == null){
			entity = new TripartiteSystemConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("tripartite:tripartiteSystemConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(TripartiteSystemConfig tripartiteSystemConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TripartiteSystemConfig> page = tripartiteSystemConfigService.findPage(new Page<TripartiteSystemConfig>(request, response), tripartiteSystemConfig); 
		model.addAttribute("page", page);
		return "modules/tripartite/tripartiteSystemConfigList";
	}

	@RequiresPermissions("tripartite:tripartiteSystemConfig:view")
	@RequestMapping(value = "form")
	public String form(TripartiteSystemConfig tsc, Model model) {
		//获取系统配置(type=1;ip端口类)作为一级配置
		List<TripartiteSystemConfig> systemList = tripartiteSystemConfigService.findListByType("1");
		model.addAttribute("systemList", systemList);
		if(StringUtils.isNotBlank(tsc.getId())) {
			if(TripartiteTypeEnum.SYSTEM_URL.getType().equals(tsc.getType())) {
				tsc.setSystemId(tsc.getId());
			}
			else {
				//补充系统的任务id，方便重置上级和前置任务
				List<TripartiteSystemConfig> taskList = tripartiteSystemConfigService.findTaskBySystemId(tsc.getSystemId(), true);
				model.addAttribute("taskList", taskList);
				List<TripartiteSystemConfig> beforeList = taskList;
				model.addAttribute("beforeList", beforeList);
			}
		}
		model.addAttribute("tripartiteSystemConfig", tsc);
		return "modules/tripartite/tripartiteSystemConfigForm";
	}

	@RequiresPermissions("tripartite:tripartiteSystemConfig:view")
	@RequestMapping(value = "param")
	public String param(TripartiteSystemConfig tripartiteSystemConfig, Model model) {
		//获取系统配置(type=1;ip端口类)作为一级配置
		model.addAttribute("systemList", tripartiteSystemConfigService.findListByType("1"));
		if(StringUtils.isNotBlank(tripartiteSystemConfig.getId())) {
			//补充系统的任务id，方便重置上级和前置任务
			List<TripartiteSystemConfig> taskList = tripartiteSystemConfigService.findTaskBySystemId(tripartiteSystemConfig.getSystemId(), true);
			model.addAttribute("taskList", taskList);
			List<TripartiteSystemConfig> beforeList = taskList;
			model.addAttribute("beforeList", beforeList);
		}
		model.addAttribute("tripartiteSystemConfig", tripartiteSystemConfig);
		return "modules/tripartite/tripartiteSystemConfigForm";
	}
	
	/**
	 * 根据系统查询接口信息
	 * @author 王鹏
	 * @version 2018-6-24 10:20:59
	 * @param tripartiteSystemConfig
	 * @return
	 */
	@RequiresPermissions("tripartite:tripartiteSystemConfig:edit")
	@RequestMapping(value = "getApiNameBySystem")
	public @ResponseBody List<TripartiteSystemConfig> getApiNameBySystem(String systemId) {
		//根据系统id查询接口信息
		return tripartiteSystemConfigService.findTaskBySystemId(systemId, true);
	}

	@RequiresPermissions("tripartite:tripartiteSystemConfig:edit")
	@RequestMapping(value = "save")
	public String save(TripartiteSystemConfig tripartiteSystemConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tripartiteSystemConfig)){
			return form(tripartiteSystemConfig, model);
		}
		tripartiteSystemConfigService.save(tripartiteSystemConfig);
		addMessage(redirectAttributes, "保存第三方系统对接配置成功");
		return "redirect:"+Global.getAdminPath()+"/tripartite/tripartiteSystemConfig/?repage";
	}
	
	@RequiresPermissions("tripartite:tripartiteSystemConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(TripartiteSystemConfig tripartiteSystemConfig, RedirectAttributes redirectAttributes) {
		tripartiteSystemConfigService.delete(tripartiteSystemConfig);
		addMessage(redirectAttributes, "删除第三方系统对接配置成功");
		return "redirect:"+Global.getAdminPath()+"/tripartite/tripartiteSystemConfig/?repage";
	}

}