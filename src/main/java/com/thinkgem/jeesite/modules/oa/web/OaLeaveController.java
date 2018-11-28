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
import com.thinkgem.jeesite.modules.oa.entity.OaLeave;
import com.thinkgem.jeesite.modules.oa.service.OaLeaveService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 请假流程Controller
 * @author lin
 * @version 2018-02-07
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaLeave")
public class OaLeaveController extends BaseController {

	@Autowired
	private OaLeaveService oaLeaveService;
	
	@ModelAttribute
	public OaLeave get(@RequestParam(required=false) String id) {
		OaLeave entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaLeaveService.get(id);
		}
		if (entity == null){
			entity = new OaLeave();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaLeave:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaLeave oaLeave, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaLeave> page = oaLeaveService.findPage(new Page<OaLeave>(request, response), oaLeave); 
		model.addAttribute("page", page);
		return "modules/oa/oaLeaveList";
	}

	@RequiresPermissions("oa:oaLeave:view")
	@RequestMapping(value = "form")
	public String form(OaLeave oaLeave, Model model,HttpServletRequest request) {
		model.addAttribute("oaLeave", oaLeave);
		if(oaLeave!=null&&oaLeave.getId()!=null&&!"".equals(oaLeave.getId())){
			if("view".equals(oaLeave.getAct().getStatus())){
				oaLeave.setCreateBy(UserUtils.get(oaLeave.getCreateBy().getId()));
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/oaLeaveTodo";
			}else{
				String taskDefKey = oaLeave.getAct().getTaskDefKey();
				if("leave3".equals(taskDefKey)){
					return "modules/oa/oaLeaveUpdate";	
				}else{
					return "modules/oa/oaLeaveTodo";
				}
			}
		}else{
			return "modules/oa/oaLeaveForm";
		}
	}

	@RequiresPermissions("oa:oaLeave:edit")
	@RequestMapping(value = "save")
	public String save(OaLeave oaLeave, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, oaLeave)){
			return form(oaLeave, model,request);
		}
		oaLeaveService.save(oaLeave);
		addMessage(redirectAttributes, "申请请假成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	@RequiresPermissions("oa:oaLeave:edit")
	@RequestMapping(value = "toDo")
	public String toDo(OaLeave oaLeave, Model model, RedirectAttributes redirectAttributes,HttpServletRequest reques) {
		if (!beanValidator(model, oaLeave)){
			return form(oaLeave, model,reques);
		}
		oaLeaveService.toDo(oaLeave);
		addMessage(redirectAttributes, "处理请假单成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}

	@RequiresPermissions("oa:oaLeave:edit")
	@RequestMapping(value = "delete")
	public String delete(OaLeave oaLeave, RedirectAttributes redirectAttributes) {
		oaLeaveService.delete(oaLeave);
		addMessage(redirectAttributes, "删除请假单成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaLeave/?repage";
	}

}