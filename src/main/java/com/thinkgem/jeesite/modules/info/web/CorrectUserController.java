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
import com.thinkgem.jeesite.modules.info.entity.CorrectUser;
import com.thinkgem.jeesite.modules.info.service.CorrectUserService;

/**
 * 社区矫正人员Controller
 * @author liujiangling
 * @version 2018-06-25
 */
@Controller
@RequestMapping(value = "${adminPath}/info/correctUser")
public class CorrectUserController extends BaseController {

	@Autowired
	private CorrectUserService correctUserService;
	
	@ModelAttribute
	public CorrectUser get(@RequestParam(required=false) String id) {
		CorrectUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = correctUserService.get(id);
		}
		if (entity == null){
			entity = new CorrectUser();
		}
		return entity;
	}
	
	@RequiresPermissions("info:correctUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(CorrectUser correctUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CorrectUser> page = correctUserService.findPage(new Page<CorrectUser>(request, response), correctUser); 
		model.addAttribute("page", page);
		return "modules/info/correctUserList";
	}

	@RequiresPermissions("info:correctUser:view")
	@RequestMapping(value = "form")
	public String form(CorrectUser correctUser, Model model) {
		model.addAttribute("correctUser", correctUser);
		return "modules/info/correctUserForm";
	}

	@RequiresPermissions("info:correctUser:edit")
	@RequestMapping(value = "save")
	public String save(CorrectUser correctUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, correctUser)){
			return form(correctUser, model);
		}
		correctUserService.save(correctUser);
		addMessage(redirectAttributes, "保存社区矫正人员成功");
		return "redirect:"+Global.getAdminPath()+"/info/correctUser/?repage";
	}
	
	@RequiresPermissions("info:correctUser:edit")
	@RequestMapping(value = "delete")
	public String delete(CorrectUser correctUser, RedirectAttributes redirectAttributes) {
		correctUserService.delete(correctUser);
		addMessage(redirectAttributes, "删除社区矫正人员成功");
		return "redirect:"+Global.getAdminPath()+"/info/correctUser/?repage";
	}
	
	/**
	 * 导出社区矫正人员数据
	 * @param correctUser
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:correctUser:view")
    @RequestMapping(value = "export")
    public String exportFile(CorrectUser correctUser, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<CorrectUser> list = null;
		try {
			if(correctUser==null){
				correctUser=new CorrectUser();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="社区矫正人员数据模板.xlsx";
            	list = new ArrayList<CorrectUser>();
            }else{
            	fileName = "社区矫正人员数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<CorrectUser> page = correctUserService.findPage(new Page<CorrectUser>(request, response, -1), correctUser);
        		list = page.getList();
            }
    		new ExportExcel("社区矫正人员数据", CorrectUser.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/correctUser/?repage";
    }

	/**
	 * 导入社区矫正人员数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:correctUser:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CorrectUser> list = ei.getDataList(CorrectUser.class);
			CorrectUser correctUser = null;
			for (int i=0;i<list.size();i++){
				correctUser = list.get(i);
				try{
					BeanValidators.validateWithException(validator, correctUser);
					correctUserService.save(correctUser);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureMsg.append("社区矫正人员数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("社区矫正人员数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入社区矫正人员失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/correctUser/?repage";
    }
	
	@RequiresPermissions("info:correctUser:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		String batchid = request.getParameter("batchid");
		correctUserService.batchDelete(batchid);
		addMessage(redirectAttributes, "删除社区矫正人员成功");
		return "redirect:" + adminPath + "/info/correctUser/?repage";
	}

}