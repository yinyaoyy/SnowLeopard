/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.thinkgem.jeesite.modules.info.entity.NotaryAgency;
import com.thinkgem.jeesite.modules.info.service.NotaryAgencyService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 公证机构Controller
 * @author 王鹏
 * @version 2018-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/info/notaryAgency")
public class NotaryAgencyController extends BaseController {

	@Autowired
	private NotaryAgencyService notaryAgencyService;
	
	@ModelAttribute
	public NotaryAgency get(@RequestParam(required=false) String id) {
		NotaryAgency entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = notaryAgencyService.get(id);
		}
		if (entity == null){
			entity = new NotaryAgency();
		}
		return entity;
	}
	
	@RequiresPermissions("info:notaryAgency:view")
	@RequestMapping(value = {"list", ""})
	public String list(NotaryAgency notaryAgency, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u = UserUtils.getUser();
		Page<NotaryAgency> page = notaryAgencyService.findPage(new Page<NotaryAgency>(request, response), notaryAgency,u); 
		model.addAttribute("page", page);
		return "modules/info/notaryAgencyList";
	}

	@RequiresPermissions("info:notaryAgency:view")
	@RequestMapping(value = "form")
	public String form(NotaryAgency notaryAgency, Model model) {
		model.addAttribute("notaryAgency", notaryAgency);
		return "modules/info/notaryAgencyForm";
	}

	@RequiresPermissions("info:notaryAgency:edit")
	@RequestMapping(value = "save")
	public String save(NotaryAgency notaryAgency, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, notaryAgency)){
			return form(notaryAgency, model);
		}
		if(StringUtils.isBlank(notaryAgency.getId())){
			if(notaryAgency.getArea()==null || StringUtils.isBlank(notaryAgency.getArea().getName())){
				addMessage(redirectAttributes, "保存失败，公证机构 所在区域不存在");
			}else if(StringUtils.isNotBlank(notaryAgencyService.personInstitution(notaryAgency.getLicenseNumber(),notaryAgency.getArea().getId()))){
				addMessage(redirectAttributes, "保存失败，公证机构已存在");
			}else{
				notaryAgencyService.save(notaryAgency);
				addMessage(redirectAttributes, "保存公证机构成功");
			}
		}else{
			notaryAgencyService.save(notaryAgency);
			addMessage(redirectAttributes, "保存公证机构成功");
		}
		return "redirect:"+Global.getAdminPath()+"/info/notaryAgency/?repage";
	}
	
	@RequiresPermissions("info:notaryAgency:edit")
	@RequestMapping(value = "delete")
	public String delete(NotaryAgency notaryAgency, RedirectAttributes redirectAttributes) {
		try {
			notaryAgencyService.delete(notaryAgency);
			addMessage(redirectAttributes, "删除公证机构成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/notaryAgency/?repage";
	}
	
	/**
	 * 导入公证处数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:notaryAgency:edit")
	@RequestMapping(value = "import", method=RequestMethod.POST)
	public String inport(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<NotaryAgency> list = ei.getDataList(NotaryAgency.class);
			NotaryAgency lo = null;
			Map<String,Area> areaBuffer = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (int i=0;i<list.size();i++){
				lo = list.get(i);
				try{
					BeanValidators.validateWithException(validator, lo);
					if(lo.getName()==null||StringUtils.isBlank(lo.getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 公证处 名称不存在; ");
						failureNum++;
					}else if(lo.getArea()==null || StringUtils.isBlank(lo.getArea().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 公证处 所在区域不存在; ");
						failureNum++;
					}else if(StringUtils.isNotBlank(notaryAgencyService.personInstitution(lo.getLicenseNumber(),lo.getArea().getId()))){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 公证处已存在; ");
						failureNum++;
					}else{
						notaryAgencyService.save(lo);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("公证处导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败 ;");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("公证处导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入公证处数据失败！失败信息："+e.getLocalizedMessage());
		}
		return "redirect:"+adminPath+"/info/notaryAgency/?repage";
	}
	
	/**
	 * 导出公证处数据
	 * @param lo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("info:notaryAgency:view")
	@RequestMapping(value = "export")
	public String exportFile(NotaryAgency lo, HttpServletRequest request, HttpServletResponse response , RedirectAttributes redirectAttributes) throws IOException {
		try {
			if(lo==null){
				lo=new NotaryAgency();
			}
			String fileName = "公证处数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
			//不分页，获取全部数据
			Page<NotaryAgency> page = notaryAgencyService.findPage(new Page<NotaryAgency>(request, response), lo);
			List<NotaryAgency> list = page.getList();
			new ExportExcel("公证处数据", NotaryAgency.class).setDataList(list).write(response, fileName).dispose();
			return null;
		}catch (Exception e){
			addMessage(redirectAttributes, "导出公证处失败！失败信息："+e.getMessage());
		}
		return "redirect:"+adminPath+"/info/notaryAgency/?repage";
	}

}