/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.web;

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
import com.thinkgem.jeesite.modules.cms.entity.Suggestion;
import com.thinkgem.jeesite.modules.cms.service.SuggestionService;

/**
 * 意见反馈Controller
 * @author wanglin
 * @version 2018-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/suggestion")
public class SuggestionController extends BaseController {

	@Autowired
	private SuggestionService suggestionService;
	
	@ModelAttribute
	public Suggestion get(@RequestParam(required=false) String id) {
		Suggestion entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = suggestionService.get(id);
		}
		if (entity == null){
			entity = new Suggestion();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:suggestion:view")
	@RequestMapping(value = {"list", ""})
	public String list(Suggestion suggestion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Suggestion> page = suggestionService.findPage(new Page<Suggestion>(request, response), suggestion); 
		model.addAttribute("page", page);
		return "modules/cms/suggestionList";
	}

	@RequiresPermissions("cms:suggestion:view")
	@RequestMapping(value = "form")
	public String form(Suggestion suggestion, Model model) {
		model.addAttribute("suggestion", suggestion);
		return "modules/cms/suggestionForm";
	}

	@RequiresPermissions("cms:suggestion:edit")
	@RequestMapping(value = "save")
	public String save(Suggestion suggestion, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, suggestion)){
			return form(suggestion, model);
		}
		suggestionService.save(suggestion);
		addMessage(redirectAttributes, "保存意见反馈成功");
		return "redirect:"+Global.getAdminPath()+"/cms/suggestion/?repage";
	}
	
	@RequiresPermissions("cms:suggestion:edit")
	@RequestMapping(value = "delete")
	public String delete(Suggestion suggestion, RedirectAttributes redirectAttributes) {
		suggestionService.delete(suggestion);
		addMessage(redirectAttributes, "删除意见反馈成功");
		return "redirect:"+Global.getAdminPath()+"/cms/suggestion/?repage";
	}

}