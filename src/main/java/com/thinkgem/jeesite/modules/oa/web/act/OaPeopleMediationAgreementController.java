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
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAgreement;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationAgreementService;

/**
 * 人民调解协议书Controller
 * @author zhangqiang
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/act/oaPeopleMediationAgreement")
public class OaPeopleMediationAgreementController extends BaseController {

	@Autowired
	private OaPeopleMediationAgreementService oaPeopleMediationAgreementService;
	
	public OaPeopleMediationAgreement get(@RequestParam(required=false) String id) {
		OaPeopleMediationAgreement entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaPeopleMediationAgreementService.get(id);
		}
		if (entity == null){
			entity = new OaPeopleMediationAgreement();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationAgreement:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaPeopleMediationAgreement oaPeopleMediationAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaPeopleMediationAgreement> page = oaPeopleMediationAgreementService.findPage(new Page<OaPeopleMediationAgreement>(request, response), oaPeopleMediationAgreement); 
		model.addAttribute("page", page);
		return "modules/oa/act/oaPeopleMediationAgreementList";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationAgreement:view")
	@RequestMapping(value = "form")
	public String form(OaPeopleMediationAgreement oaPeopleMediationAgreement, Model model,HttpServletRequest request) {
		Act act = oaPeopleMediationAgreement.getAct();
		oaPeopleMediationAgreement = get(request.getParameter("id"));
		oaPeopleMediationAgreement.setAct(act);
		model.addAttribute("oaPeopleMediationAgreement", oaPeopleMediationAgreement);
		if(oaPeopleMediationAgreement!=null&&StringUtils.isNotBlank(oaPeopleMediationAgreement.getProcInsId())){
			//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
			if("view".equals(oaPeopleMediationAgreement.getAct().getStatus())){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/act/oaPeopleMediationAgreementView";
			}else{
				return "modules/oa/act/oaPeopleMediationAgreementForm";
			}
		}
		else {
			return "modules/oa/act/oaPeopleMediationAgreementForm";
		}
	}

	@RequiresPermissions("oa:act:oaPeopleMediationAgreement:edit")
	@RequestMapping(value = "save")
	public String save(OaPeopleMediationAgreement oaPeopleMediationAgreement, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationAgreement)){
			return form(oaPeopleMediationAgreement, model,request);
		}
		oaPeopleMediationAgreementService.save(oaPeopleMediationAgreement);
		addMessage(redirectAttributes, "保存人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationAgreement:edit")
	@RequestMapping(value = "submit")
	public String submit(OaPeopleMediationAgreement oaPeopleMediationAgreement, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationAgreement)){
			return form(oaPeopleMediationAgreement, model, request);
		}
		oaPeopleMediationAgreementService.submit(oaPeopleMediationAgreement);
		addMessage(redirectAttributes, "人民调解协议书保存成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";	
		// 跳转到人民调解回访记录
		// return "modules/oa/act/oaPeopleMediationAgreementForm";
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationAgreement:edit")
	@RequestMapping(value = "delete")
	public String delete(OaPeopleMediationAgreement oaPeopleMediationAgreement, RedirectAttributes redirectAttributes) {
		oaPeopleMediationAgreementService.delete(oaPeopleMediationAgreement);
		addMessage(redirectAttributes, "删除人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/oa/act/oaPeopleMediationAgreement/?repage";
	}

}