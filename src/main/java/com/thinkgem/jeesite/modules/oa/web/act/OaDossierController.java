/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.oa.entity.OaDossier;
import com.thinkgem.jeesite.modules.oa.service.OaDossierService;

/**
 * 卷宗说明Controller
 * @author zhangqiang
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaDossier")
public class OaDossierController extends BaseController {

	@Autowired
	private OaDossierService oaDossierService;
	
	public OaDossier get(@RequestParam(required=false) String id) {
		OaDossier entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaDossierService.get(id);
		}
		if (entity == null){
			entity = new OaDossier();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaDossier:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaDossier oaDossier, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaDossier> page = oaDossierService.findPage(new Page<OaDossier>(request, response), oaDossier); 
		model.addAttribute("page", page);
		return "modules/oa/oaDossierList";
	}

	@RequiresPermissions("oa:oaDossier:view")
	@RequestMapping(value = "form")
	public String form(OaDossier oaDossier, Model model, HttpServletRequest request) {
		Act act = oaDossier.getAct();
		oaDossier = get(request.getParameter("id"));
		oaDossier.setAct(act);
		model.addAttribute("oaDossier", oaDossier);
		if(oaDossier!=null&&StringUtils.isNotBlank(oaDossier.getProcInsId())){
			//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
			if("view".equals(oaDossier.getAct().getStatus())){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/act/oaDossierView";
			}else{
				return "modules/oa/act/oaDossierForm";
			}
		}
		else {
			return "modules/oa/act/oaDossierForm";
		}
	}

	@RequiresPermissions("oa:oaDossier:edit")
	@RequestMapping(value = "save")
	public String save(OaDossier oaDossier, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaDossier)){
			return form(oaDossier, model, request);
		}
		oaDossierService.save(oaDossier);
		addMessage(redirectAttributes, "保存卷宗成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}

	@RequiresPermissions("oa:oaDossier:edit")
	@RequestMapping(value = "submit")
	public String submit(OaDossier oaDossier, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaDossier)){
			return form(oaDossier, model, request);
		}
		oaDossierService.submit(oaDossier);
		addMessage(redirectAttributes, "保存卷宗成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:oaDossier:edit")
	@RequestMapping(value = "delete")
	public String delete(OaDossier oaDossier, RedirectAttributes redirectAttributes) {
		oaDossierService.delete(oaDossier);
		addMessage(redirectAttributes, "删除人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaDossier/?repage";
	}

}