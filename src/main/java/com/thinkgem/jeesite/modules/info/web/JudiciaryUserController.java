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
import com.thinkgem.jeesite.modules.info.entity.JudiciaryUser;
import com.thinkgem.jeesite.modules.info.entity.LawAssitanceUser;
import com.thinkgem.jeesite.modules.info.service.JudiciaryUserService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 司法所工作人员Controller
 * @author wanglin
 * @version 2018-06-10
 */
@Controller
@RequestMapping(value = "${adminPath}/info/judiciaryUser")
public class JudiciaryUserController extends BaseController {

	@Autowired
	private JudiciaryUserService judiciaryUserService;
	
	@ModelAttribute
	public JudiciaryUser get(@RequestParam(required=false) String id) {
		JudiciaryUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = judiciaryUserService.get(id);
		}
		if (entity == null){
			entity = new JudiciaryUser();
		}
		return entity;
	}
	
	@RequiresPermissions("info:judiciaryUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(JudiciaryUser judiciaryUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u =UserUtils.getUser();
		Page<JudiciaryUser> page = judiciaryUserService.findPage(new Page<JudiciaryUser>(request, response), judiciaryUser,u); 
		model.addAttribute("page", page);
		return "modules/info/judiciaryUserList";
	}

	@RequiresPermissions("info:judiciaryUser:view")
	@RequestMapping(value = "form")
	public String form(JudiciaryUser judiciaryUser, Model model) {
		model.addAttribute("judiciaryUser", judiciaryUser);
		return "modules/info/judiciaryUserForm";
	}

	@RequiresPermissions("info:judiciaryUser:edit")
	@RequestMapping(value = "save")
	public String save(JudiciaryUser judiciaryUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, judiciaryUser)){
			return form(judiciaryUser, model);
		}
		judiciaryUserService.save(judiciaryUser);
		addMessage(redirectAttributes, "保存司法所工作人员成功");
		return "redirect:"+Global.getAdminPath()+"/info/judiciaryUser/?repage";
	}
	
	@RequiresPermissions("info:judiciaryUser:edit")
	@RequestMapping(value = "delete")
	public String delete(JudiciaryUser judiciaryUser, RedirectAttributes redirectAttributes) {
		try {
			judiciaryUserService.delete(judiciaryUser);
			addMessage(redirectAttributes, "删除司法所工作人员成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		return "redirect:"+Global.getAdminPath()+"/info/judiciaryUser/?repage";
	}
	/**
	 * 导出司法所工作人员数据
	 * @param judiciaryUser
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:judiciaryUser:view")
    @RequestMapping(value = "export")
    public String exportFile(JudiciaryUser judiciaryUser, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<JudiciaryUser> list = null;
		try {
			if(judiciaryUser==null){
				judiciaryUser=new JudiciaryUser();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="司法所工作人员数据模板.xlsx";
            	list = new ArrayList<JudiciaryUser>();
            }else{
            	fileName = "司法所工作人员数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<JudiciaryUser> page = judiciaryUserService.findPage(new Page<JudiciaryUser>(request, response, -1), judiciaryUser);
        		list = page.getList();
            }
    		new ExportExcel("司法所工作人员数据", JudiciaryUser.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/judiciaryUser/list?repage";
    }
	/**
	 * 导入司法所工作人员数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:judiciary:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<JudiciaryUser> list = ei.getDataList(JudiciaryUser.class);
			JudiciaryUser lo = null;
			for (int i=0;i<list.size();i++){
				lo = list.get(i);
				try{
					BeanValidators.validateWithException(validator, lo);
					if(lo.getArea()==null || StringUtils.isBlank(lo.getArea().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 司法所工作人员所在旗县不存在; ");
						failureNum++;
					}else if(lo.getTown()==null || StringUtils.isBlank(lo.getTown().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 司法所工作人员所在乡镇不存在; ");
						failureNum++;
					}else if(lo.getOffice()==null || StringUtils.isBlank(lo.getOffice().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 司法所工作人员所在司法所不存在; ");
						failureNum++;
					}else if(judiciaryUserService.findList(lo).size()>0){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 司法所工作人员已存在; ");
						failureNum++;
					}else{
						judiciaryUserService.save(lo);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("司法所工作人员数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("司法所工作人员数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入司法所工作人员失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/judiciaryUser/list?repage";
    }
}