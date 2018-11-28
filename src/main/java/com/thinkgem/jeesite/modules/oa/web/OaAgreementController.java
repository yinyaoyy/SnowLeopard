/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web;

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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.oa.entity.OaAgreement;
import com.thinkgem.jeesite.modules.oa.service.OaAgreementService;
import com.thinkgem.jeesite.modules.oa.service.OaProcessStateService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.AreaService;

/**
 * 三定方案Controller
 * @author liujiangling
 * @version 2018-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaAgreement")
public class OaAgreementController extends BaseController {

	@Autowired
	private OaAgreementService oaAgreementService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private OaProcessStateService oaProcessStateService;
	
	@ModelAttribute
	public OaAgreement get(@RequestParam(required=false) String id) {
		OaAgreement entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaAgreementService.get(id);
		}
		if (entity == null){
			entity = new OaAgreement();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaAgreement:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaAgreement oaAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaAgreement> apage = new Page<OaAgreement>(request, response);
		Page<OaAgreement> page = oaAgreementService.findPage(new Page<OaAgreement>(request, response), oaAgreement,apage); 
		List<Area> areaList = areaService.findQxList();
		model.addAttribute("page", page);
		model.addAttribute("areaList", areaList);
		return "modules/oa/oaAgreementList";
	}

	@RequiresPermissions("oa:oaAgreement:view")
	@RequestMapping(value = "form")
	public String form(OaAgreement oaAgreement, Model model,HttpServletRequest request) {
		model.addAttribute("oaAgreement", oaAgreement);
		if(oaAgreement!=null&&StringUtils.isNotBlank(oaAgreement.getId())){
			//如果流程是查看状态，那么只能查看，不能修改任何记录
			if("view".equals(oaAgreement.getAct().getStatus())){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/oaAgreementView";
			}else if("agreement_update".equals(oaAgreement.getAct().getTaskDefKey()) || "agreement_start".equals(oaAgreement.getAct().getTaskDefKey())) {
				return "modules/oa/oaAgreementForm";
			}else{
				return "modules/oa/oaAgreementTodo";
			}
		}
		else {
			return "modules/oa/oaAgreementForm";
		}
	}

	@RequiresPermissions("oa:oaAgreement:edit")
	@RequestMapping(value = "save")
	public String save(OaAgreement oaAgreement, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response) {
		if (!beanValidator(model, oaAgreement)){
			return form(oaAgreement, model, request);
		}
		//添加保存操作
		oaAgreementService.save(oaAgreement);
		if("yes".equals(oaAgreement.getAct().getFlag())){
			addMessage(redirectAttributes, "重申三定方案成功");
		}else if("no".equals(oaAgreement.getAct().getFlag())){
			addMessage(redirectAttributes, "删除三定方案成功");
		}else{
			addMessage(redirectAttributes, "保存三定方案成功");
		}
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:oaAgreement:edit")
	@RequestMapping(value = "toDo")
	public String toDo(OaAgreement oaAgreement, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		oaAgreementService.toDo(oaAgreement);
		if("no".equals(oaAgreement.getAct().getFlag())){
			addMessage(redirectAttributes, "三定方案已退回");
		}else{
			addMessage(redirectAttributes, "三定方案审批通过");
		}
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:oaAgreement:edit")
	@RequestMapping(value = "delete")
	public String delete(OaAgreement oaAgreement, RedirectAttributes redirectAttributes) {
		oaAgreementService.delete(oaAgreement);
		addMessage(redirectAttributes, "删除三定方案成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaAgreement/?repage";
	}

}