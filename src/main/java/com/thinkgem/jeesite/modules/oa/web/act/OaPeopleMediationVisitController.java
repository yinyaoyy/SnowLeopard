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
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationVisit;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationVisitService;

/**
 * 人民调解回访记录Controller
 * @author zhangqiang
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/act/oaPeopleMediationVisit")
public class OaPeopleMediationVisitController extends BaseController {

	@Autowired
	private OaPeopleMediationVisitService oaPeopleMediationVisitService;
	
	public OaPeopleMediationVisit get(@RequestParam(required=false) String id) {
		OaPeopleMediationVisit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaPeopleMediationVisitService.get(id);
		}
		if (entity == null){
			entity = new OaPeopleMediationVisit();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationVisit:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaPeopleMediationVisit oaPeopleMediationVisit, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaPeopleMediationVisit> page = oaPeopleMediationVisitService.findPage(new Page<OaPeopleMediationVisit>(request, response), oaPeopleMediationVisit); 
		model.addAttribute("page", page);
		return "modules/oa/act/oaPeopleMediationVisitList";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationVisit:view")
	@RequestMapping(value = "form")
	public String form(OaPeopleMediationVisit oaPeopleMediationVisit, Model model, HttpServletRequest request) {
		Act act = oaPeopleMediationVisit.getAct();
		oaPeopleMediationVisit = get(request.getParameter("id"));
		oaPeopleMediationVisit.setAct(act);
		model.addAttribute("oaPeopleMediationVisit", oaPeopleMediationVisit);
		if(oaPeopleMediationVisit!=null&&StringUtils.isNotBlank(oaPeopleMediationVisit.getProcInsId())){
			//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
			if("view".equals(oaPeopleMediationVisit.getAct().getStatus())){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/act/oaPeopleMediationVisitView";
			}else{
				return "modules/oa/act/oaPeopleMediationVisitForm";
			}
		}
		else {
			return "modules/oa/act/oaPeopleMediationVisitForm";
		}
	}

	@RequiresPermissions("oa:act:oaPeopleMediationVisit:edit")
	@RequestMapping(value = "save")
	public String save(OaPeopleMediationVisit oaPeopleMediationVisit, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationVisit)){
			return form(oaPeopleMediationVisit, model, request);
		}
		oaPeopleMediationVisitService.save(oaPeopleMediationVisit);
		addMessage(redirectAttributes, "保存人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationVisit:edit")
	@RequestMapping(value = "submit")
	public String submit(OaPeopleMediationVisit oaPeopleMediationVisit, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationVisit)){
			return form(oaPeopleMediationVisit, model, request);
		}
		oaPeopleMediationVisitService.submit(oaPeopleMediationVisit);
		 addMessage(redirectAttributes, "人民调解回访记录保存成功");
		 return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
		// 跳转到填写卷宗
//		return "modules/oa/oaDossierForm";
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationVisit:edit")
	@RequestMapping(value = "delete")
	public String delete(OaPeopleMediationVisit oaPeopleMediationVisit, RedirectAttributes redirectAttributes) {
		oaPeopleMediationVisitService.delete(oaPeopleMediationVisit);
		addMessage(redirectAttributes, "删除人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/oa/act/oaPeopleMediationVisit/?repage";
	}

}