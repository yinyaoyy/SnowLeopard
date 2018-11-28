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
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.info.entity.LawAssistance;
import com.thinkgem.jeesite.modules.info.entity.LawAssitanceUser;
import com.thinkgem.jeesite.modules.info.service.LawAssistanceService;
import com.thinkgem.jeesite.modules.info.service.LawAssitanceUserService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 法援中心工作人员Controller
 * @author wanglin
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/info/lawAssitanceUser")
public class LawAssitanceUserController extends BaseController {

	@Autowired
	private LawAssitanceUserService lawAssitanceUserService;
	@Autowired
	private LawAssistanceService lawAssistanceService;
	
	@ModelAttribute
	public LawAssitanceUser get(@RequestParam(required=false) String id) {
		LawAssitanceUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = lawAssitanceUserService.get(id);
		}
		if (entity == null){
			entity = new LawAssitanceUser();
		}
		return entity;
	}
	
	@RequiresPermissions("info:lawAssitanceUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(LawAssitanceUser lawAssitanceUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u =UserUtils.getUser();
		Page<LawAssitanceUser> page = lawAssitanceUserService.findPage(new Page<LawAssitanceUser>(request, response), lawAssitanceUser,u); 
		model.addAttribute("page", page);
		return "modules/info/lawAssitanceUserList";
	}

	@RequiresPermissions("info:lawAssitanceUser:view")
	@RequestMapping(value = "form")
	public String form(LawAssitanceUser lawAssitanceUser, Model model) {
		model.addAttribute("lawAssitanceUser", lawAssitanceUser);
		model.addAttribute("lawAssitanceList", lawAssistanceService.findList(new LawAssistance()));
		return "modules/info/lawAssitanceUserForm";
	}

	@RequiresPermissions("info:lawAssitanceUser:edit")
	@RequestMapping(value = "save")
	public String save(LawAssitanceUser lawAssitanceUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, lawAssitanceUser)){
			return form(lawAssitanceUser, model);
		}
		if(StringUtils.isBlank(lawAssitanceUser.getId())&&StringUtils.isNotBlank(lawAssitanceUserService.personIdCard(lawAssitanceUser.getIdCard()))){
			addMessage(redirectAttributes, "身份证号已存在,保存失败");
		}else{
			lawAssitanceUserService.save(lawAssitanceUser);
			addMessage(redirectAttributes, "保存法援中心工作者成功");
		}
		return "redirect:"+Global.getAdminPath()+"/info/lawAssitanceUser/?repage";
	}
	
	@RequiresPermissions("info:lawAssitanceUser:edit")
	@RequestMapping(value = "delete")
	public String delete(LawAssitanceUser lawAssitanceUser, RedirectAttributes redirectAttributes) {
		lawAssitanceUserService.delete(lawAssitanceUser);
		addMessage(redirectAttributes, "删除法援中心工作人员成功");
		return "redirect:"+Global.getAdminPath()+"/info/lawAssitanceUser/?repage";
	}
	/**
	 * 导出法援中心工作人员数据
	 * @param lawAssitanceUser
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:LawAssitanceUser:view")
    @RequestMapping(value = "export")
    public String exportFile(LawAssitanceUser lawAssitanceUser, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<LawAssitanceUser> list = null;
		try {
			if(lawAssitanceUser==null){
				lawAssitanceUser=new LawAssitanceUser();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="法援中心工作人员数据模板.xlsx";
            	list = new ArrayList<LawAssitanceUser>();
            }else{
            	fileName = "法援中心工作人员数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<LawAssitanceUser> page = lawAssitanceUserService.findPage(new Page<LawAssitanceUser>(request, response, -1), lawAssitanceUser);
        		list = page.getList();
            }
    		new ExportExcel("法援中心工作人员数据", LawAssitanceUser.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/lawAssitanceUser/list?repage";
    }

	/**
	 * 导入法援中心工作人员数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:lawAssitanceUser:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LawAssitanceUser> list = ei.getDataList(LawAssitanceUser.class);
			LawAssitanceUser lawAssitanceUser = null;
			for (int i=0;i<list.size();i++){
				lawAssitanceUser = list.get(i);
				try{
					BeanValidators.validateWithException(validator, lawAssitanceUser);
					if(StringUtils.isNotBlank(lawAssitanceUserService.personIdCard(lawAssitanceUser.getIdCard()))){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 法援中心工作人员已存在; ");
						failureNum++;
					}else if(lawAssitanceUser.getArea()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 法援中心工作人员所在区域不存在; ");
						failureNum++;
					}else if(lawAssitanceUser.getOffice()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 法援中心工作人员归属法援中心不存在; ");
						failureNum++;
					}
					else{
						lawAssitanceUserService.save(lawAssitanceUser);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("法援中心工作人员数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					ex.printStackTrace();
					failureMsg.append("法援中心工作人员数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入法援中心工作人员失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/lawAssitanceUser/list?repage";
    }

	@RequiresPermissions("info:lawAssitanceUser:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		String batchid = request.getParameter("batchid");
		lawAssitanceUserService.batchDelete(batchid);
		addMessage(redirectAttributes, "删除法援中心工作人员成功");
		return "redirect:" + adminPath + "/info/lawAssitanceUser/list?repage";
	}
}