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
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationExamine;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationExamineService;

/**
 * 人民调解调查记录Controller
 * @author zhangqiang
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/act/oaPeopleMediationExamine")
public class OaPeopleMediationExamineController extends BaseController {

	@Autowired
	private OaPeopleMediationExamineService oaPeopleMediationExamineService;
	
	public OaPeopleMediationExamine get(@RequestParam(required=false) String id) {
		OaPeopleMediationExamine entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaPeopleMediationExamineService.get(id);
		}
		if (entity == null){
			entity = new OaPeopleMediationExamine();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationExamine:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaPeopleMediationExamine oaPeopleMediationExamine, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaPeopleMediationExamine> page = oaPeopleMediationExamineService.findPage(new Page<OaPeopleMediationExamine>(request, response), oaPeopleMediationExamine); 
		model.addAttribute("page", page);
		return "modules/oa/act/oaPeopleMediationExamineList";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationExamine:view")
	@RequestMapping(value = "form")
	public String form(OaPeopleMediationExamine oaPeopleMediationExamine, Model model, HttpServletRequest request) {
		
		Act act = oaPeopleMediationExamine.getAct();
		oaPeopleMediationExamine = get(request.getParameter("id"));
		oaPeopleMediationExamine.setAct(act);
		model.addAttribute("oaPeopleMediationExamine", oaPeopleMediationExamine);
		if(oaPeopleMediationExamine!=null&&StringUtils.isNotBlank(oaPeopleMediationExamine.getProcInsId())){
			//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
			if("view".equals(oaPeopleMediationExamine.getAct().getStatus())){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/act/oaPeopleMediationExamineView";
			}else{
				return "modules/oa/act/oaPeopleMediationExamineForm";
			}
		}
		else {
			return "modules/oa/act/oaPeopleMediationExamineForm";
		}
	}

	@RequiresPermissions("oa:act:oaPeopleMediationExamine:edit")
	@RequestMapping(value = "save")
	public String save(OaPeopleMediationExamine oaPeopleMediationExamine, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationExamine)){
			return form(oaPeopleMediationExamine, model, request);
		}
		oaPeopleMediationExamineService.save(oaPeopleMediationExamine);
		addMessage(redirectAttributes, "保存人民调解成功");
		 return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationExamine:edit")
	@RequestMapping(value = "submit")
	public String submit(OaPeopleMediationExamine oaPeopleMediationExamine, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationExamine)){
			return form(oaPeopleMediationExamine, model, request);
		}
		oaPeopleMediationExamineService.submit(oaPeopleMediationExamine);
		 addMessage(redirectAttributes, "人民调解调查记录保存成功");
		 return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
		// 跳转到人民调解记录表
//		return "modules/oa/act/oaPeopleMediationRecordForm";
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationExamine:edit")
	@RequestMapping(value = "delete")
	public String delete(OaPeopleMediationExamine oaPeopleMediationExamine, RedirectAttributes redirectAttributes) {
		oaPeopleMediationExamineService.delete(oaPeopleMediationExamine);
		addMessage(redirectAttributes, "删除人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/oa/act/oaPeopleMediationExamine/?repage";
	}

}