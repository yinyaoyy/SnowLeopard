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
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationRecord;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationRecordService;

/**
 * 人民调解调解记录Controller
 * @author zhangqiang
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/act/oaPeopleMediationRecord")
public class OaPeopleMediationRecordController extends BaseController {

	@Autowired
	private OaPeopleMediationRecordService oaPeopleMediationRecordService;
	
	public OaPeopleMediationRecord get(@RequestParam(required=false) String id) {
		OaPeopleMediationRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaPeopleMediationRecordService.get(id);
		}
		if (entity == null){
			entity = new OaPeopleMediationRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaPeopleMediationRecord oaPeopleMediationRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaPeopleMediationRecord> page = oaPeopleMediationRecordService.findPage(new Page<OaPeopleMediationRecord>(request, response), oaPeopleMediationRecord); 
		model.addAttribute("page", page);
		return "modules/oa/act/oaPeopleMediationRecordList";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationRecord:view")
	@RequestMapping(value = "form")
	public String form(OaPeopleMediationRecord oaPeopleMediationRecord, Model model, HttpServletRequest request) {
		Act act = oaPeopleMediationRecord.getAct();
		oaPeopleMediationRecord = get(request.getParameter("id"));
		oaPeopleMediationRecord.setAct(act);
		model.addAttribute("oaPeopleMediationRecord", oaPeopleMediationRecord);
		if(oaPeopleMediationRecord!=null&&StringUtils.isNotBlank(oaPeopleMediationRecord.getProcInsId())){
			//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
			if("view".equals(oaPeopleMediationRecord.getAct().getStatus())){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/act/oaPeopleMediationRecordView";
			}else{
				return "modules/oa/act/oaPeopleMediationRecordForm";
			}
		}
		else {
			return "modules/oa/act/oaPeopleMediationRecordForm";
		}
	}

	@RequiresPermissions("oa:act:oaPeopleMediationRecord:edit")
	@RequestMapping(value = "save")
	public String save(OaPeopleMediationRecord oaPeopleMediationRecord, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationRecord)){
			return form(oaPeopleMediationRecord, model, request);
		}
		oaPeopleMediationRecordService.save(oaPeopleMediationRecord);
		addMessage(redirectAttributes, "保存人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationRecord:edit")
	@RequestMapping(value = "submit")
	public String submit(OaPeopleMediationRecord oaPeopleMediationRecord, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationRecord)){
			return form(oaPeopleMediationRecord, model, request);
		}
		oaPeopleMediationRecordService.submit(oaPeopleMediationRecord);
		 addMessage(redirectAttributes, "人民调解调解记录保存成功");
		 return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
		// 跳转到人民调解协议书
//		return "modules/oa/act/oaPeopleMediationAgreementForm";
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(OaPeopleMediationRecord oaPeopleMediationRecord, RedirectAttributes redirectAttributes) {
		oaPeopleMediationRecordService.delete(oaPeopleMediationRecord);
		addMessage(redirectAttributes, "删除人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/oa/act/oaPeopleMediationRecord/?repage";
	}

}