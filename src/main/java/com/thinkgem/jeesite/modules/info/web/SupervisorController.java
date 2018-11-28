/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.web;

import java.util.ArrayList;
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

import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.info.entity.Supervisor;
import com.thinkgem.jeesite.modules.info.service.SupervisorService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;

/**
 * 人民监督员Controller
 * @author suzz
 * @version 2018-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/info/supervisor")
public class SupervisorController extends BaseController {
	@Autowired
	private SupervisorService supervisorService;
	
	private OfficeService officeService;
	
	@ModelAttribute
	public Supervisor get(@RequestParam(required=false) String id) {
		Supervisor entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supervisorService.get(id);
		}
		if (entity == null){
			entity = new Supervisor();
		}
		return entity;
	}
	
	@RequiresPermissions("info:supervisor:view")
	@RequestMapping(value = {"list", ""})
	public String list(Supervisor supervisor, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Supervisor> page = supervisorService.findPage(new Page<Supervisor>(request, response), supervisor); 
		
		model.addAttribute("page", page);
		return "modules/info/supervisorList";
	}

	@RequiresPermissions("info:supervisor:view")
	@RequestMapping(value = "form")
	public String form(Supervisor supervisor, Model model) {

		model.addAttribute("supervisor", supervisor);
		//model.addAttribute("officelist",officelist);
		return "modules/info/supervisorForm";
	}

	@RequiresPermissions("info:supervisor:edit")
	@RequestMapping(value = "save")
	public String save(Supervisor supervisor, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, supervisor)){
			return form(supervisor, model);
		}
		supervisorService.save(supervisor);
		addMessage(redirectAttributes, "保存人民监督员成功");
		return "redirect:"+Global.getAdminPath()+"/info/supervisor/?repage";
	}
	
	@RequiresPermissions("info:supervisor:edit")
	@RequestMapping(value = "delete")
	public String delete(Supervisor supervisor, RedirectAttributes redirectAttributes) {
		supervisorService.delete(supervisor);
		addMessage(redirectAttributes, "删除人民监督员成功");
		return "redirect:"+Global.getAdminPath()+"/info/supervisor/?repage";
	}

	
	/**
	 * 导出人民监督员数据
	 * @param lawyer
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:supervisor:view")
    @RequestMapping(value = "export")
    public String exportFile(Supervisor supervisor, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<Supervisor> list = null;
		try {
			if(supervisor==null){
				supervisor=new Supervisor();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="人民监督员模板.xlsx";
            	list = new ArrayList<Supervisor>();
            }else{
            	fileName = "人民监督员"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<Supervisor> page = supervisorService.findPage(new Page<Supervisor>(request, response, -1), supervisor);
        		list = page.getList();
            }
    		new ExportExcel("人民监督员", Supervisor.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/supervisor/list?repage";
    }

	
	/**
	 * 导入人民监督员数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:supervisor:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Supervisor> list = ei.getDataList(Supervisor.class);
			Supervisor supervisor = null;
			for (int i=0;i<list.size();i++){
				supervisor = list.get(i);
				try{
					 if(supervisor.getXrName()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 人民监督员姓名不能为空; ");
						failureNum++;
					} else if(supervisor.getName()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 选任单位不能为空; ");
						failureNum++;
					}else{
						supervisorService.save(supervisor);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("人民监督数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("人民监督数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "人民监督数据失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/supervisor/list?repage";
    }

}