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
import com.thinkgem.jeesite.modules.oa.entity.OaGongchu;
import com.thinkgem.jeesite.modules.oa.service.OaGongchuService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 公出单Controller
 * @author lin
 * @version 2018-02-28
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaGongchu")
public class OaGongchuController extends BaseController {

	@Autowired
	private OaGongchuService oaGongchuService;
	
	@ModelAttribute
	public OaGongchu get(@RequestParam(required=false) String id) {
		OaGongchu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaGongchuService.get(id);
		}
		if (entity == null){
			entity = new OaGongchu();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaGongchu:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaGongchu oaGongchu, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaGongchu> page = oaGongchuService.findPage(new Page<OaGongchu>(request, response), oaGongchu); 
		model.addAttribute("page", page);
		return "modules/oa/oaGongchuList";
	}

	@RequiresPermissions("oa:oaGongchu:view")
	@RequestMapping(value = "form")
	public String form(OaGongchu oaGongchu, Model model,HttpServletRequest request) {
		model.addAttribute("oaGongchu", oaGongchu);
		if(oaGongchu!=null&&oaGongchu.getId()!=null&&!"".equals(oaGongchu.getId())){
			if("view".equals(oaGongchu.getAct().getStatus())){
				oaGongchu.setCreateBy(UserUtils.get(oaGongchu.getCreateBy().getId()));
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/oaGongchuView";
			}else{
				String taskDefKey = oaGongchu.getAct().getTaskDefKey();
				if("gongchu6".equals(taskDefKey)){
					return "modules/oa/oaGongchuUpdate";	
				}else{
					return "modules/oa/oaGongchuTodo";
				}
			}
		}else{
			return "modules/oa/oaGongchuForm";
		}
		//return "modules/oa/oaGongchuForm";
	}

	@RequiresPermissions("oa:oaGongchu:edit")
	@RequestMapping(value = "save")
	public String save(OaGongchu oaGongchu, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, oaGongchu)){
			return form(oaGongchu, model,request);
		}
		oaGongchuService.save(oaGongchu);
		addMessage(redirectAttributes, "保存公出单成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:oaGongchu:edit")
	@RequestMapping(value = "delete")
	public String delete(OaGongchu oaGongchu, RedirectAttributes redirectAttributes) {
		oaGongchuService.delete(oaGongchu);
		addMessage(redirectAttributes, "删除公出单成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaGongchu/?repage";
	}
	@RequiresPermissions("oa:oaLeave:edit")
	@RequestMapping(value = "toDo")
	public String toDo(OaGongchu oaGongchu, Model model, RedirectAttributes redirectAttributes,HttpServletRequest reques) {
		if (!beanValidator(model, oaGongchu)){
			return form(oaGongchu, model,reques);
		}
		oaGongchuService.toDo(oaGongchu);
		addMessage(redirectAttributes, "处理请假单成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}

}