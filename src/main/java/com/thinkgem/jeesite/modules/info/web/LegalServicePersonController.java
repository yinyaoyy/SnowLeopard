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
import com.thinkgem.jeesite.modules.info.dao.LegalServicePersonDao;
import com.thinkgem.jeesite.modules.info.entity.InfoLegalServiceOffice;
import com.thinkgem.jeesite.modules.info.entity.LegalServicePerson;
import com.thinkgem.jeesite.modules.info.service.InfoLegalServiceOfficeService;
import com.thinkgem.jeesite.modules.info.service.LegalServicePersonService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 基层法律服务工作者Controller
 * @author 王鹏
 * @version 2018-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/info/legalServicePerson")
public class LegalServicePersonController extends BaseController {

	@Autowired
	private LegalServicePersonService legalServicePersonService;
	@Autowired
	private InfoLegalServiceOfficeService legalOfficeService;
	@Autowired
	private LegalServicePersonDao dao;
	
	@ModelAttribute
	public LegalServicePerson get(@RequestParam(required=false) String id) {
		LegalServicePerson entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = legalServicePersonService.get(id);
		}
		if (entity == null){
			entity = new LegalServicePerson();
		}
		return entity;
	}
	
	@RequiresPermissions("info:legalServicePerson:view")
	@RequestMapping(value = {"list", ""})
	public String list(LegalServicePerson legalServicePerson, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u =UserUtils.getUser();
		Page<LegalServicePerson> page = legalServicePersonService.findPage(new Page<LegalServicePerson>(request, response), legalServicePerson,u); 
		model.addAttribute("page", page);
		return "modules/info/legalServicePersonList";
	}

	@RequiresPermissions("info:legalServicePerson:view")
	@RequestMapping(value = "form")
	public String form(LegalServicePerson legalServicePerson, Model model) {
		List<InfoLegalServiceOffice> legalOfficeList = legalOfficeService.findList(new InfoLegalServiceOffice());
		model.addAttribute("legalOfficeList", legalOfficeList);
		model.addAttribute("legalServicePerson", legalServicePerson);
		return "modules/info/legalServicePersonForm";
	}

	@RequiresPermissions("info:legalServicePerson:edit")
	@RequestMapping(value = "save")
	public String save(LegalServicePerson legalServicePerson, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, legalServicePerson)){
			return form(legalServicePerson, model);
		}
		if(StringUtils.isBlank(legalServicePerson.getId())){
			if(StringUtils.isNotBlank(legalServicePersonService.personIdCard(legalServicePerson.getIdCard()))){
				addMessage(redirectAttributes, "保存失败，基层法律服务工作者已存在");
			}else{
				legalServicePersonService.save(legalServicePerson);
				addMessage(redirectAttributes, "保存基层法律服务工作者成功");
			}
		}else{
			legalServicePersonService.save(legalServicePerson);
			addMessage(redirectAttributes, "保存基层法律服务工作者成功");
		}
		return "redirect:"+Global.getAdminPath()+"/info/legalServicePerson/?repage";
	}
	
	@RequiresPermissions("info:legalServicePerson:edit")
	@RequestMapping(value = "delete")
	public String delete(LegalServicePerson legalServicePerson, RedirectAttributes redirectAttributes) {
		try {
			legalServicePersonService.delete(legalServicePerson);
			addMessage(redirectAttributes, "删除基层法律服务工作者成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/legalServicePerson/?repage";
	}

	@RequiresPermissions("info:legalServicePerson:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String batchid = request.getParameter("batchid");
			legalServicePersonService.batchDelete(batchid);
			addMessage(redirectAttributes, "删除基层法律服务工作者成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:" + adminPath + "/info/legalServicePerson/list?repage";
	}
	

	/**
	 * 导出基层法律服务工作者数据
	 * @param legalServicePerson
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:legalServicePerson:view")
    @RequestMapping(value = "export")
    public String exportFile(LegalServicePerson legalServicePerson, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<LegalServicePerson> list = null;
		try {
			if(legalServicePerson==null){
				legalServicePerson=new LegalServicePerson();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="基层法律服务工作者数据模板.xlsx";
            	list = new ArrayList<LegalServicePerson>();
            }else{
            	fileName = "基层法律服务工作者数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<LegalServicePerson> page = legalServicePersonService.findPage(new Page<LegalServicePerson>(request, response, -1), legalServicePerson);
        		list = page.getList();
            }
    		new ExportExcel("基层法律服务工作者数据", LegalServicePerson.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/legalServicePerson/list?repage";
    }

	/**
	 * 导入基层法律服务工作者数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:legalServicePerson:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LegalServicePerson> list = ei.getDataList(LegalServicePerson.class);
			LegalServicePerson legalServicePerson = null;
			for (int i=0;i<list.size();i++){
				legalServicePerson = list.get(i);
				try{					
					BeanValidators.validateWithException(validator, legalServicePerson);
					if(legalServicePerson==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 基层法律服务工作者坐在区域不存在; ");
						failureNum++;
					}else if(StringUtils.isNotBlank(legalServicePersonService.personIdCard(legalServicePerson.getIdCard()))){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 基层法律服务工作者已存在; ");
						failureNum++;
					}else{
						legalServicePersonService.save(legalServicePerson);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("基层法律服务工作者数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("基层法律服务工作者数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入基层法律服务工作者失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/legalServicePerson/list?repage";
    }

}