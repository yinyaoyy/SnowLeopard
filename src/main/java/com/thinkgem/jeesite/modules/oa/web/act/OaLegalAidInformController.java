/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.info.entity.LowOffice;
import com.thinkgem.jeesite.modules.info.service.LowOfficeService;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidInform;
import com.thinkgem.jeesite.modules.oa.service.act.OaLegalAidInformService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 法援通知辩护Controller
 * @author suzz
 * @version 2018-07-11
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/act/oaLegalAidInform")
public class OaLegalAidInformController extends BaseController {

	@Autowired
	private OaLegalAidInformService oaLegalAidInformService;
	
	
	@Autowired
	private LowOfficeService lawOfficeService;

	
	@Autowired
	private ActTaskService actTaskService; 
	
	@ModelAttribute
	public OaLegalAidInform get(@RequestParam(required=false) String id) {
		OaLegalAidInform entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaLegalAidInformService.get(id);
		}
		if (entity == null){
			entity = new OaLegalAidInform();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:act:oaLegalAidInform:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaLegalAidInform oaLegalAidInform, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaLegalAidInform> page = oaLegalAidInformService.findPage(new Page<OaLegalAidInform>(request, response), oaLegalAidInform); 
		model.addAttribute("page", page);
		return "modules/oa/act/oaLegalAidInformList";
	}

	@RequiresPermissions("oa:act:oaLegalAidInform:view")
	@RequestMapping(value = "form")
	public String form(OaLegalAidInform oaLegalAidInform, Model model,HttpServletRequest request) {
		List<LowOffice> lawOfficeList = lawOfficeService.findList(new LowOffice());
		if(StringUtils.isBlank(oaLegalAidInform.getId())) {
			User user=UserUtils.getUser();
			oaLegalAidInform.setJgPerson(user.getName());
			oaLegalAidInform.setJgphone(user.getMobile());
			oaLegalAidInform.setOfficeNamee(user.getOffice().getName());
		     //机构类型  后期加上效验其他机构的不能发起此流程
			oaLegalAidInform.setOfficeType(user.getOffice().getType());
		}
		model.addAttribute("lawOfficeList", lawOfficeList);
		oaLegalAidInform.setLegalAidType("2");//从此方法进入的都是法援通知辩护
		model.addAttribute("oaLegalAidInform", oaLegalAidInform);
		model.addAttribute("officeLawyerOffice", OfficeRoleConstant.OFFICE_LAWYER_OFFICE);
		model.addAttribute("officeLegalService", OfficeRoleConstant.OFFICE_LEGAL_SERVICE);
		if(oaLegalAidInform!=null&&StringUtils.isNotBlank(oaLegalAidInform.getId())){
		//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
		if("view".equals(oaLegalAidInform.getAct().getStatus()) || "1".equals(oaLegalAidInform.getArchiving())){
			model.addAttribute("zt", request.getParameter("zt"));
			return "modules/oa/act/oaLegalAidinformView";
		}else if("defense_update".equals(oaLegalAidInform.getAct().getTaskDefKey())) {
			return "modules/oa/act/oaLegalAidinformForm";
		}else{
			return "modules/oa/act/oaLegalAidinformTodo";
		}
		}else {
	
		return "modules/oa/act/oaLegalAidinformForm";
	}
	}

	@RequiresPermissions("oa:act:oaLegalAidInform:edit")
	@RequestMapping(value = "save")
	public String save(OaLegalAidInform oaLegalAidInform, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, oaLegalAidInform)){
			return form(oaLegalAidInform, model, request);
		}
		oaLegalAidInformService.save(oaLegalAidInform);
		addMessage(redirectAttributes, "保存法援通知成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	@RequiresPermissions("oa:act:oaLegalAidInform:edit")
	@RequestMapping(value = "toDo")
	public String toDo(OaLegalAidInform oaLegalAidInform, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		
	
			oaLegalAidInformService.toDo(oaLegalAidInform);
		if("no".equals(oaLegalAidInform.getAct().getFlag())){
			if("defense_fyzhuren".equals(oaLegalAidInform.getAct().getTaskDefKey())||"defense_approve".equals(oaLegalAidInform.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援通知辩护申请已驳回");
			}else if("defense_chengbanren_banli".equals(oaLegalAidInform.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "保存法援通知辩护申请成功");
			}
		}else if("yes".equals(oaLegalAidInform.getAct().getFlag())){
			if("defense_confirm".equals(oaLegalAidInform.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援通知辩护申请已确认");
			}else if("defense_fyzhuren".equals(oaLegalAidInform.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援通知辩护申请审批成功");
			}else if("defense_lszhuren".equals(oaLegalAidInform.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援通知辩护申请承办人指定成功");
			}else if("defense_chengbanren_banli".equals(oaLegalAidInform.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援通知辩护申请已办结");
			}else if("defense_pingjia".equals(oaLegalAidInform.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援通知辩护申请已评价");
			}else if("defense_chengbanren_butie".equals(oaLegalAidInform.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援通知辩护申请承办人申领补贴成功");
			}
		}
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:act:oaLegalAidInform:edit")
	@RequestMapping(value = "delete")
	public String delete(OaLegalAidInform oaLegalAidInform, RedirectAttributes redirectAttributes) {
		oaLegalAidInformService.delete(oaLegalAidInform);
		addMessage(redirectAttributes, "删除法援通知辩护成功");
		return "redirect:\"+Global.getAdminPath()+\"/oa/act/oaLegalAid/?repage";
	}
	
	
	@RequiresPermissions("oa:act:oaLegalAidInform:edit")
	@RequestMapping(value = "endProcess")
	public String endProcess(OaLegalAidInform oaLegalAidInform,  Model model,RedirectAttributes redirectAttributes) {
		oaLegalAidInformService.applyEndProcess(oaLegalAidInform);
		addMessage(redirectAttributes, "申请人发起结束成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	

}