/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.web;

import java.text.SimpleDateFormat;
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
import com.thinkgem.jeesite.modules.info.entity.PersonnelPrisonUser;
import com.thinkgem.jeesite.modules.info.service.PersonnelPrisonUserService;

/**
 * 在册安置帮教人员Controller
 * @author huangtao
 * @version 2018-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/info/personnelPrisonUser")
public class PersonnelPrisonUserController extends BaseController {

	@Autowired
	private PersonnelPrisonUserService personnelPrisonUserService;
	
	@ModelAttribute
	public PersonnelPrisonUser get(@RequestParam(required=false) String id) {
		PersonnelPrisonUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = personnelPrisonUserService.get(id);
		}
		if (entity == null){
			entity = new PersonnelPrisonUser();
		}
		return entity;
	}
	
	@RequiresPermissions("info:personnelPrisonUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(PersonnelPrisonUser personnelPrisonUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PersonnelPrisonUser> page = personnelPrisonUserService.findPage(new Page<PersonnelPrisonUser>(request, response), personnelPrisonUser); 
		model.addAttribute("page", page);
		return "modules/info/personnelPrisonUserList";
	}

	@RequiresPermissions("info:personnelPrisonUser:view")
	@RequestMapping(value = "form")
	public String form(PersonnelPrisonUser personnelPrisonUser, Model model) {
		model.addAttribute("personnelPrisonUser", personnelPrisonUser);
		return "modules/info/personnelPrisonUserForm";
	}

	
	
	@RequiresPermissions("info:personnelPrisonUser:edit")
	@RequestMapping(value = "delete")
	public String delete(PersonnelPrisonUser personnelPrisonUser, RedirectAttributes redirectAttributes) {
		personnelPrisonUserService.delete(personnelPrisonUser);
		addMessage(redirectAttributes, "删除在册安置帮教人员成功");
		return "redirect:"+Global.getAdminPath()+"/info/personnelPrisonUser/?repage";
	}
	@RequiresPermissions("info:personnelPrisonUser:edit")
	@RequestMapping(value = "save")
	public String save(PersonnelPrisonUser personnelPrisonUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, personnelPrisonUser)){
			return form(personnelPrisonUser, model);
		}
		if(StringUtils.isBlank(personnelPrisonUser.getId())&&StringUtils.isNotBlank(personnelPrisonUserService.personIdCard(personnelPrisonUser.getIdCard()))){
			addMessage(redirectAttributes, "身份证号已存在,保存失败");
		}else{
			personnelPrisonUserService.save(personnelPrisonUser);
			addMessage(redirectAttributes, "保存在册安置帮教人员成功");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/personnelPrisonUser/?repage";
	}
	/**
	 * 导出在册安置帮教人员数据
	 * @param personnelPrisonUser
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:personnelPrisonUser:view")
    @RequestMapping(value = "export")
    public String exportFile(PersonnelPrisonUser personnelPrisonUser, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<PersonnelPrisonUser> list = null;
		try {
			if(personnelPrisonUser==null){
				personnelPrisonUser=new PersonnelPrisonUser();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="在册安置帮教人员数据模板.xlsx";
            	list = new ArrayList<PersonnelPrisonUser>();
            }else{
            	fileName = "在册安置帮教人员数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<PersonnelPrisonUser> page = personnelPrisonUserService.findPage(new Page<PersonnelPrisonUser>(request, response, -1), personnelPrisonUser);
        		list = page.getList();
            }
    		new ExportExcel("在册安置帮教人员数据", PersonnelPrisonUser.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/personnelPrisonUser/list?repage";
    }
	/**
	 * 导入在册安置帮教人员数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:personnelPrisonUser:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PersonnelPrisonUser> list = ei.getDataList(PersonnelPrisonUser.class);
			PersonnelPrisonUser personnelPrisonUser = null;
			 SimpleDateFormat sf = new SimpleDateFormat("yyyy");  
			for (int i=0;i<list.size();i++){
				personnelPrisonUser = list.get(i);
				try{
					BeanValidators.validateWithException(validator, personnelPrisonUser);
					/*if(personnelPrisonUser.getIdCard()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 安置人员身份证号不存在; ");
						failureNum++;
					}else{*/
						personnelPrisonUserService.save(personnelPrisonUser);
						successNum++;
					
				}catch(ConstraintViolationException ex){
					failureMsg.append("安置人员数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("安置人员数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入法援中心工作人员失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/personnelPrisonUser/list?repage";
    }
	@RequiresPermissions("info:personnelPrisonUser:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		String batchid = request.getParameter("batchid");
		personnelPrisonUserService.batchDelete(batchid);
		addMessage(redirectAttributes, "删除在册安置帮教人员成功");
		return "redirect:" + adminPath + "/info/personnelPrisonUser/list?repage";
	}

}