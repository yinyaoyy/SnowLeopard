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
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.info.entity.Lawyer;
import com.thinkgem.jeesite.modules.info.entity.LowOffice;
import com.thinkgem.jeesite.modules.info.service.LawyerService;
import com.thinkgem.jeesite.modules.info.service.LowOfficeService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 律师信息管理Controller
 * @author 王鹏
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/info/lawyer")
public class LawyerController extends BaseController {

	@Autowired
	private LawyerService lawyerService;
	@Autowired
	private LowOfficeService lawOfficeService;
	
	@ModelAttribute
	public Lawyer get(@RequestParam(required=false) String id) {
		Lawyer entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = lawyerService.get(id);
		}
		if (entity == null){
			entity = new Lawyer();
		}
		return entity;
	}
	
	@RequiresPermissions("info:lawyer:view")
	@RequestMapping(value = {"list", ""})
	public String list(Lawyer lawyer, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u =UserUtils.getUser();
		Page<Lawyer> page = lawyerService.findPage(new Page<Lawyer>(request, response), lawyer,u); 
		model.addAttribute("page", page);
		return "modules/info/lawyerList";
	}

	@RequiresPermissions("info:lawyer:view")
	@RequestMapping(value = "form")
	public String form(Lawyer lawyer, Model model) {
		List<LowOffice> lawOfficeList = lawOfficeService.findList(new LowOffice());
		model.addAttribute("lawOfficeList", lawOfficeList);
		model.addAttribute("lawyer", lawyer);
		return "modules/info/lawyerForm";
	}

	@RequiresPermissions("info:lawyer:edit")
	@RequestMapping(value = "save")
	public String save(Lawyer lawyer, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, lawyer)){
			return form(lawyer, model);
		}
		lawyerService.save(lawyer);
		addMessage(redirectAttributes, "保存律师信息成功");
		return "redirect:"+Global.getAdminPath()+"/info/lawyer/?repage";
	}
	
	@RequiresPermissions("info:lawyer:edit")
	@RequestMapping(value = "delete")
	public String delete(Lawyer lawyer, RedirectAttributes redirectAttributes) {
		try {
			lawyerService.delete(lawyer);
			addMessage(redirectAttributes, "删除律师信息成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/lawyer/?repage";
	}

	/**
	 * 导出律师数据
	 * @param lawyer
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:lawyer:view")
    @RequestMapping(value = "export")
    public String exportFile(Lawyer lawyer, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<Lawyer> list = null;
		try {
			if(lawyer==null){
				lawyer=new Lawyer();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="律师数据模板.xlsx";
            	list = new ArrayList<Lawyer>();
            }else{
            	fileName = "律师数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<Lawyer> page = lawyerService.findPage(new Page<Lawyer>(request, response, -1), lawyer);
        		list = page.getList();
            }
    		new ExportExcel("律师数据", Lawyer.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/lawyer/list?repage";
    }

	/**
	 * 导入律师数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:lawyer:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Lawyer> list = ei.getDataList(Lawyer.class);
			Lawyer lawyer = null;
			for (int i=0;i<list.size();i++){
				lawyer = list.get(i);
				try{
					BeanValidators.validateWithException(validator, lawyer);
					if(lawyer.getArea()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 律师所在区域不存在; ");
						failureNum++;
					}else if(lawyer.getLawOffice()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 律师所在律师事务所不存在; ");
						failureNum++;
					}else if(lawyerService.getInfoByIdCard(lawyer.getIdCard())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 律师已存在; ");
						failureNum++;
					}
					else{
						lawyerService.save(lawyer);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("律师数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("律师数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入律师失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/lawyer/list?repage";
    }

	@RequiresPermissions("info:lawyer:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
	try {
		String batchid = request.getParameter("batchid");
		lawyerService.batchDelete(batchid);
		addMessage(redirectAttributes, "删除律师成功");	
	} catch (BusinessException e) {
		addMessage(redirectAttributes, e.getMessage());	
	}catch (Exception e) {
		addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
	}
		
		return "redirect:" + adminPath + "/info/lawyer/list?repage";
	}
}