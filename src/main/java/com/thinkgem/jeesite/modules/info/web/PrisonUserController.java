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
import com.thinkgem.jeesite.modules.info.entity.PrisonUser;
import com.thinkgem.jeesite.modules.info.service.PrisonUserService;

/**
 * 在监服刑人员Controller
 * @author liujiangling
 * @version 2018-06-21
 */
@Controller
@RequestMapping(value = "${adminPath}/info/prisonUser")
public class PrisonUserController extends BaseController {

	@Autowired
	private PrisonUserService prisonUserService;
	
	@ModelAttribute
	public PrisonUser get(@RequestParam(required=false) String id) {
		PrisonUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = prisonUserService.get(id);
		}
		if (entity == null){
			entity = new PrisonUser();
		}
		return entity;
	}
	
	@RequiresPermissions("info:prisonUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(PrisonUser prisonUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PrisonUser> page = prisonUserService.findPage(new Page<PrisonUser>(request, response), prisonUser); 
		model.addAttribute("page", page);
		return "modules/info/prisonUserList";
	}

	@RequiresPermissions("info:prisonUser:view")
	@RequestMapping(value = "form")
	public String form(PrisonUser prisonUser, Model model) {
		model.addAttribute("prisonUser", prisonUser);
		return "modules/info/prisonUserForm";
	}

	@RequiresPermissions("info:prisonUser:edit")
	@RequestMapping(value = "save")
	public String save(PrisonUser prisonUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, prisonUser)){
			return form(prisonUser, model);
		}
		if(StringUtils.isBlank(prisonUser.getId())){
			if(StringUtils.isNotBlank(prisonUserService.personIdCard(prisonUser.getIdCard()))){
				addMessage(redirectAttributes, "保存失败，在监服刑人员已存在");
			}else{
				prisonUserService.save(prisonUser);
				addMessage(redirectAttributes, "保存在监服刑人员成功");
			}
		}else{
			prisonUserService.save(prisonUser);
			addMessage(redirectAttributes, "保存在监服刑人员成功");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/prisonUser/?repage";
	}
	
	@RequiresPermissions("info:prisonUser:edit")
	@RequestMapping(value = "delete")
	public String delete(PrisonUser prisonUser, RedirectAttributes redirectAttributes) {
		prisonUserService.delete(prisonUser);
		addMessage(redirectAttributes, "删除在监服刑人员成功");
		return "redirect:"+Global.getAdminPath()+"/info/prisonUser/?repage";
	}
	
	/**
	 * 导出在监服刑人员数据
	 * @param prisonUser
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:prisonUser:view")
    @RequestMapping(value = "export")
    public String exportFile(PrisonUser prisonUser, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<PrisonUser> list = null;
		try {
			if(prisonUser==null){
				prisonUser=new PrisonUser();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="在监服刑人员数据模板.xlsx";
            	list = new ArrayList<PrisonUser>();
            }else{
            	fileName = "在监服刑人员数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<PrisonUser> page = prisonUserService.findPage(new Page<PrisonUser>(request, response, -1), prisonUser);
        		list = page.getList();
            }
    		new ExportExcel("在监服刑人员数据", PrisonUser.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/prisonUser/?repage";
    }

	/**
	 * 导入公证员数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:prisonUser:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PrisonUser> list = ei.getDataList(PrisonUser.class);
			PrisonUser prisonUser = null;
			for (int i=0;i<list.size();i++){
				prisonUser = list.get(i);
				try{
					BeanValidators.validateWithException(validator, prisonUser);
					prisonUserService.save(prisonUser);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureMsg.append("在监服刑人员数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("在监服刑人员数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入在监服刑人员失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/prisonUser/?repage";
    }
	
	@RequiresPermissions("info:prisonUser:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		String batchid = request.getParameter("batchid");
		prisonUserService.batchDelete(batchid);
		addMessage(redirectAttributes, "删除在监服刑人员成功");
		return "redirect:" + adminPath + "/info/prisonUser/?repage";
	}

}