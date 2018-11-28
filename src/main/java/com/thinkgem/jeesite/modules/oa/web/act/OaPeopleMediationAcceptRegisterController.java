/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web.act;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAcceptRegister;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAcceptRegisterCount;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationAcceptRegisterService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 人民调解受理登记Controller
 * @author zhangqiang
 * @version 2018-05-24
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/act/oaPeopleMediationAcceptRegister")
public class OaPeopleMediationAcceptRegisterController extends BaseController {

	@Autowired
	private OaPeopleMediationAcceptRegisterService oaPeopleMediationAcceptRegisterService;
	
	
	public OaPeopleMediationAcceptRegister get(@RequestParam(required=false) String id) {
		OaPeopleMediationAcceptRegister entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaPeopleMediationAcceptRegisterService.get(id);
		}
		if (entity == null){
			entity = new OaPeopleMediationAcceptRegister();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationAcceptRegister:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaPeopleMediationAcceptRegister> page = oaPeopleMediationAcceptRegisterService.findPage(new Page<OaPeopleMediationAcceptRegister>(request, response), oaPeopleMediationAcceptRegister); 
		model.addAttribute("page", page);
		return "modules/oa/act/oaPeopleMediationAcceptRegisterList";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationAcceptRegister:view")
	@RequestMapping(value = "form")
	public String form(OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister, Model model,HttpServletRequest request) {
		Act act = oaPeopleMediationAcceptRegister.getAct();
		oaPeopleMediationAcceptRegister = get(request.getParameter("id"));
		oaPeopleMediationAcceptRegister.setAct(act);
		String status = request.getParameter("status");
		model.addAttribute("oaPeopleMediationAcceptRegister", oaPeopleMediationAcceptRegister);
		if(oaPeopleMediationAcceptRegister!=null&&StringUtils.isNotBlank(oaPeopleMediationAcceptRegister.getOaPeopleMediationApply().getProcInsId())){
			//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
			if("view".equals(oaPeopleMediationAcceptRegister.getAct().getStatus())||"view".equals(status)){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/act/oaPeopleMediationAcceptRegisterView";
			}else{
				return "modules/oa/act/oaPeopleMediationAcceptRegisterForm";
			}
		}
		else {
			return "modules/oa/act/oaPeopleMediationAcceptRegisterForm";
		}
	}

	@RequiresPermissions("oa:act:oaPeopleMediationAcceptRegister:edit")
	@RequestMapping(value = "save")
	public String save(OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationAcceptRegister)){
			return form(oaPeopleMediationAcceptRegister, model, request);
		}
		oaPeopleMediationAcceptRegisterService.save(oaPeopleMediationAcceptRegister);
		addMessage(redirectAttributes, "保存人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationAcceptRegister:edit")
	@RequestMapping(value = "submit")
	public String submit(OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationAcceptRegister)){
			return form(oaPeopleMediationAcceptRegister, model, request);
		}
		oaPeopleMediationAcceptRegisterService.submit(oaPeopleMediationAcceptRegister);
		addMessage(redirectAttributes, "人民调解受理登记表保存成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	@RequiresPermissions("oa:act:oaPeopleMediationAcceptRegister:edit")
	@RequestMapping(value = "delete")
	public String delete(OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister, RedirectAttributes redirectAttributes) {
		oaPeopleMediationAcceptRegisterService.delete(oaPeopleMediationAcceptRegister);
		addMessage(redirectAttributes, "删除人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/oa/act/oaPeopleMediationAcceptRegister/?repage";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationAcceptRegister:view:countByYearCaseRank")
	@RequestMapping(value="toCountByYearCaseRank")
	public String toCountByYearCaseRank(OaPeopleMediationAcceptRegisterCount opmarc, HttpServletRequest request, HttpServletResponse response, Model model) {
		//设置默认年度
		if(StringUtils.isBlank(opmarc.getYear())) {
			opmarc.setYear(DateUtils.getYear());
		}
		return "modules/chart/peopleMediation/countYearCaseRank";
	}
	@RequiresPermissions("oa:act:oaPeopleMediationApply:view:countByYearCaseRank")
	@RequestMapping(value="countByYearCaseRank")
	@ResponseBody
	public List<OaPeopleMediationAcceptRegisterCount> countByYearArea(OaPeopleMediationAcceptRegisterCount opmarc) {
		//获取相应数据
		List<OaPeopleMediationAcceptRegisterCount> list = oaPeopleMediationAcceptRegisterService.countByYearCaseRank(opmarc); 
		return list;
	}
}