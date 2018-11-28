/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.info.entity.LawAssistance;
import com.thinkgem.jeesite.modules.info.service.LawAssistanceService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
/**
 * 法援中心Controller
 * @author wanglin
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/info/lawAssistance")
public class LawAssistanceController extends BaseController {

	@Autowired
	private LawAssistanceService lawAssistanceService;
	
	@ModelAttribute
	public LawAssistance get(@RequestParam(required=false) String id) {
		LawAssistance entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = lawAssistanceService.get(id);
		}
		if (entity == null){
			entity = new LawAssistance();
		}
		return entity;
	}
	
	@RequiresPermissions("info:lawAssistance:view")
	@RequestMapping(value = {"list", ""})
	public String list(LawAssistance lawAssistance, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u = UserUtils.getUser();
		Page<LawAssistance> page = lawAssistanceService.findPage(new Page<LawAssistance>(request, response), lawAssistance,u); 
		model.addAttribute("page", page);
		return "modules/info/lawAssistanceList";
	}

	@RequiresPermissions("info:lawAssistance:view")
	@RequestMapping(value = "form")
	public String form(LawAssistance lawAssistance, Model model) {
		model.addAttribute("lawAssistance", lawAssistance);
		return "modules/info/lawAssistanceForm";
	}

	@RequiresPermissions("info:lawAssistance:edit")
	@RequestMapping(value = "save")
	public String save(LawAssistance lawAssistance, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, lawAssistance)){
			return form(lawAssistance, model);
		}
		if(StringUtils.isBlank(lawAssistance.getId())){
			if(StringUtils.isNotBlank(lawAssistanceService.areaName(lawAssistance.getName()))){
				addMessage(redirectAttributes, "法援中心已存在,保存失败");
			}else{
				lawAssistanceService.save(lawAssistance);
				addMessage(redirectAttributes, "保存法援中心成功");
			}
		}else{
			lawAssistanceService.save(lawAssistance);
			addMessage(redirectAttributes, "保存法援中心成功");
		}
		return "redirect:"+Global.getAdminPath()+"/info/lawAssistance/?repage";
	}
	
	@RequiresPermissions("info:lawAssistance:edit")
	@RequestMapping(value = "delete")
	public String delete(LawAssistance lawAssistance, RedirectAttributes redirectAttributes) {
		try {
			lawAssistanceService.delete(lawAssistance);
			addMessage(redirectAttributes, "删除法援中心成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/lawAssistance/?repage";
	}
	/**
	 * 导出法援中心数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:lawAssistance:edit")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(LawAssistance lawAssistance, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "法援中心数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<LawAssistance> page = lawAssistanceService.findPage(new Page<LawAssistance>(request, response, -1), lawAssistance);
    		List<LawAssistance> list=page.getList();
    		//List<UserExport> exList=Lists.newArrayList();
            new ExportExcel("法援中心数据", LawAssistance.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出法援中心失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/lawAssistance?repage";
    }
	/**
	 * 导入法援中心数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:lawAssistance:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/info/lawAssistance?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LawAssistance> list = ei.getDataList(LawAssistance.class);
			LawAssistance lolawAssistances = null;
			for (int i=0;i<list.size();i++){
				lolawAssistances=list.get(i);
				try{
					if(lolawAssistances.getArea()==null || StringUtils.isBlank(lolawAssistances.getArea().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 法援中心坐在区域不存在; ");
						failureNum++;
					}else{
						lawAssistanceService.save(lolawAssistances);
						successNum++;	
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>法援中心 "+lolawAssistances.getName()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
					
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条法援中心，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条法援中心"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入法援中心失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/lawAssistance/?repage";
    }
	/**
	 * 下载导入法援中心数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:lawAssistance:edit")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "法援中心数据导入模板.xlsx";
    		List<LawAssistance> list = Lists.newArrayList();
    		new ExportExcel("法援中心数据", LawAssistance.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/lawAssistance?repage";
    }

}