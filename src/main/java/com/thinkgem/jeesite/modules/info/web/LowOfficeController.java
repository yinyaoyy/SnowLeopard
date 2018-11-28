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
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.info.entity.LowOffice;
import com.thinkgem.jeesite.modules.info.service.LowOfficeService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 律师事务所Controller
 * @author 王鹏
 * @version 2018-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/info/lowOffice")
public class LowOfficeController extends BaseController {

	@Autowired
	private LowOfficeService lowOfficeService;
	
	@ModelAttribute
	public LowOffice get(@RequestParam(required=false) String id) {
		LowOffice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = lowOfficeService.get(id);
		}
		if (entity == null){
			entity = new LowOffice();
		}
		return entity;
	}
	
	@RequiresPermissions("info:lowOffice:view")
	@RequestMapping(value = {"list", ""})
	public String list(LowOffice lowOffice, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u = UserUtils.getUser();
		Page<LowOffice> page = lowOfficeService.findPage(new Page<LowOffice>(request, response), lowOffice,u); 
		model.addAttribute("page", page);
		return "modules/info/lowOfficeList";
	}

	@RequiresPermissions("info:lowOffice:view")
	@RequestMapping(value = "form")
	public String form(LowOffice lowOffice, Model model) {
		model.addAttribute("lowOffice", lowOffice);
		return "modules/info/lowOfficeForm";
	}

	@RequiresPermissions("info:lowOffice:edit")
	@RequestMapping(value = "save")
	public String save(LowOffice lowOffice, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, lowOffice)){
			return form(lowOffice, model);
		}
		lowOfficeService.save(lowOffice);
		addMessage(redirectAttributes, "保存律师事务所成功");
		return "redirect:"+Global.getAdminPath()+"/info/lowOffice/?repage";
	}
	
	@RequiresPermissions("info:lowOffice:edit")
	@RequestMapping(value = "delete")
	public String delete(LowOffice lowOffice, RedirectAttributes redirectAttributes) {
		try {
			lowOfficeService.delete(lowOffice);
			addMessage(redirectAttributes, "删除律师事务所成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		return "redirect:"+Global.getAdminPath()+"/info/lowOffice/?repage";
	}

	/**
	 * 导出律师事务所数据
	 * @param lo
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:lowOffice:view")
    @RequestMapping(value = "export")
    public String exportFile(LowOffice lo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<LowOffice> list = null;
		try {
			if(lo==null){
				lo=new LowOffice();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="律师事务所数据模板.xlsx";
            	list = new ArrayList<LowOffice>();
            	//做一个模板数据，仅供参考使用
            	LowOffice lo_template = new LowOffice();
            	lo_template.setName("模板数据");
            	Area a = new Area();
            	a.setName("锡林浩特市");
            	lo_template.setArea(a);
            	lo_template.setAddress("内蒙古自治区锡林郭勒盟锡林浩特市那达慕街1号");
            	lo_template.setPhone("0479-88889999");
            	lo_template.setLicenseNumber("2342JHH235253L25352L");
            	lo_template.setIntroduction("模板样例数据，仅供参考，上传时请删除此记录！！！");
            	lo_template.setCoordinate("116.093297,43.932355");
            	list.add(lo_template);
            }else{
            	fileName = "律师事务所数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<LowOffice> page = lowOfficeService.findPage(new Page<LowOffice>(request, response, -1), lo);
        		list = page.getList();
            }
    		new ExportExcel("律师事务所数据", LowOffice.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/lowOffice/list?repage";
    }

	/**
	 * 导入律师事务所数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:lowOffice:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LowOffice> list = ei.getDataList(LowOffice.class);
			LowOffice lo = null;
			for (int i=0;i<list.size();i++){
				lo = list.get(i);
				try{
					BeanValidators.validateWithException(validator, lo);
					if(lo.getArea()==null || StringUtils.isBlank(lo.getArea().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 律师事务所坐在区域不存在; ");
						failureNum++;
					}else if(lo.getOffice()==null||StringUtils.isBlank(lo.getOffice().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 律师事务所主营机关不存在; ");
						failureNum++;
					}else if(lowOfficeService.getLawOfficeByName(lo.getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 律师事务所已经存在; ");
						failureNum++;
					}else{
						lowOfficeService.save(lo);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("律师事务所数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("律师事务所数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入律师事务所失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/lowOffice/list?repage";
    }

	@RequiresPermissions("info:lowOffice:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String batchid = request.getParameter("batchid");
			lowOfficeService.batchDelete(batchid);
			addMessage(redirectAttributes, "删除律师事务所成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
	
		return "redirect:" + adminPath + "/info/lowOffice/list?repage";
	}
}