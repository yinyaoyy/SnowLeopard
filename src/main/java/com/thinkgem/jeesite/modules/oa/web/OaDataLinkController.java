/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web;

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
import com.thinkgem.jeesite.modules.oa.entity.OaDataLink;
import com.thinkgem.jeesite.modules.oa.service.OaDataLinkService;

/**
 * 记录流程数据Controller
 * @author zhangqiang
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaDataLink")
public class OaDataLinkController extends BaseController {

	@Autowired
	private OaDataLinkService oaDataLinkService;
	
	@ModelAttribute
	public OaDataLink get(@RequestParam(required=false) String id) {
		OaDataLink entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaDataLinkService.get(id);
		}
		if (entity == null){
			entity = new OaDataLink();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaDataLink:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaDataLink oaDataLink, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaDataLink> page = oaDataLinkService.findPage(new Page<OaDataLink>(request, response), oaDataLink); 
		model.addAttribute("page", page);
		return "modules/oa/oaDataLinkList";
	}

	@RequiresPermissions("oa:oaDataLink:view")
	@RequestMapping(value = "form")
	public String form(OaDataLink oaDataLink, Model model) {
		model.addAttribute("oaDataLink", oaDataLink);
		return "modules/oa/oaDataLinkForm";
	}

	@RequiresPermissions("oa:oaDataLink:edit")
	@RequestMapping(value = "save")
	public String save(OaDataLink oaDataLink, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaDataLink)){
			return form(oaDataLink, model);
		}
		oaDataLinkService.save(oaDataLink);
		addMessage(redirectAttributes, "保存流程成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaDataLink/?repage";
	}
	
	@RequiresPermissions("oa:oaDataLink:edit")
	@RequestMapping(value = "delete")
	public String delete(OaDataLink oaDataLink, RedirectAttributes redirectAttributes) {
		oaDataLinkService.delete(oaDataLink);
		addMessage(redirectAttributes, "删除流程成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaDataLink/?repage";
	}

}