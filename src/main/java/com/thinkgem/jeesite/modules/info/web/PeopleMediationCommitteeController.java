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
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.info.entity.PeopleMediationCommittee;
import com.thinkgem.jeesite.modules.info.service.PeopleMediationCommitteeService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 人民调解委员会Controller
 * @author wanglin
 * @version 2018-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/info/peopleMediationCommittee")
public class PeopleMediationCommitteeController extends BaseController {

	@Autowired
	private PeopleMediationCommitteeService peopleMediationCommitteeService;
	
	@ModelAttribute
	public PeopleMediationCommittee get(@RequestParam(required=false) String id) {
		PeopleMediationCommittee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = peopleMediationCommitteeService.get(id);
		}
		if (entity == null){
			entity = new PeopleMediationCommittee();
		}
		return entity;
	}
	
	@RequiresPermissions("info:peopleMediationCommittee:view")
	@RequestMapping(value = {"list", ""})
	public String list(PeopleMediationCommittee peopleMediationCommittee, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u = UserUtils.getUser();
		Page<PeopleMediationCommittee> page = peopleMediationCommitteeService.findPage(new Page<PeopleMediationCommittee>(request, response), peopleMediationCommittee,u); 
		model.addAttribute("page", page);
		return "modules/info/peopleMediationCommitteeList";
	}

	@RequiresPermissions("info:peopleMediationCommittee:view")
	@RequestMapping(value = "form")
	public String form(PeopleMediationCommittee peopleMediationCommittee, Model model) {
		model.addAttribute("peopleMediationCommittee", peopleMediationCommittee);
		return "modules/info/peopleMediationCommitteeForm";
	}

	@RequiresPermissions("info:peopleMediationCommittee:edit")
	@RequestMapping(value = "save")
	public String save(PeopleMediationCommittee peopleMediationCommittee, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, peopleMediationCommittee)){
			return form(peopleMediationCommittee, model);
		}
		peopleMediationCommitteeService.save(peopleMediationCommittee);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:"+Global.getAdminPath()+"/info/peopleMediationCommittee/?repage";
	}
	
	@RequiresPermissions("info:peopleMediationCommittee:edit")
	@RequestMapping(value = "delete")
	public String delete(PeopleMediationCommittee peopleMediationCommittee, RedirectAttributes redirectAttributes) {
		try {
			peopleMediationCommitteeService.delete(peopleMediationCommittee);
			addMessage(redirectAttributes, "删除人民调解委员会成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
	
		return "redirect:"+Global.getAdminPath()+"/info/peopleMediationCommittee/?repage";
	}
  /**
	 * 导出人民调解委员会数据
	 * @param lo
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:peopleMediationCommittee:view")
    @RequestMapping(value = "export")
    public String exportFile(PeopleMediationCommittee lo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<PeopleMediationCommittee> list = null;
		try {
			if(lo==null){
				lo=new PeopleMediationCommittee();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="人民调解委员会数据模板.xlsx";
            	list = new ArrayList<PeopleMediationCommittee>();
            	//做一个模板数据，仅供参考使用
            	PeopleMediationCommittee lo_template = new PeopleMediationCommittee();
            	lo_template.setOrganizationForm("01");
            	lo_template.setChargeUser("张三");
            	lo_template.setName("***人民监督委员会");
            	lo_template.setAbbreviation("**调委会");
            	lo_template.setAddress("内蒙古自治区锡林郭勒盟锡林浩特市那达慕街1号");
            	Office office=new Office();
            	office.setName("锡林浩特市司法局");
            	lo_template.setOffice(office);
            	lo_template.setZipCode("011100");
            	lo_template.setPhone("0479-88889999");
            	lo_template.setFaxNumber("04797522548");
            	lo_template.setIntroduction("模板样例数据！！！");
            	Area a = new Area();
            	a.setName("c");
            	lo_template.setArea(a);
            	lo_template.setEstablishArea("锡林浩特市");
            	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");  
            	lo_template.setEstablishTime(sf.parse("2018-12-12"));
            	lo_template.setAdministrativeDivision("152501");
            	lo_template.setCoordinate("116.093297,43.932355");
            	list.add(lo_template);
            }else{
            	fileName = "人民调解委员会数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<PeopleMediationCommittee> page = peopleMediationCommitteeService.findPage(new Page<PeopleMediationCommittee>(request, response, -1), lo);
        		list = page.getList();
            }
    		new ExportExcel("人民调解委员会数据", PeopleMediationCommittee.class).setDataList(list).write(response, fileName).dispose();
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
	@RequiresPermissions("info:peopleMediationCommittee:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PeopleMediationCommittee> list = ei.getDataList(PeopleMediationCommittee.class);
			PeopleMediationCommittee lo = null;
			for (int i=0;i<list.size();i++){
				lo = list.get(i);
				try{
					BeanValidators.validateWithException(validator, lo);
					if(lo.getArea()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 人民调解委员会所在区域不存在; ");
						failureNum++;
					}else if(peopleMediationCommitteeService.findListInfo(lo)){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 人民调解委员会已存在; ");
						failureNum++;
					}else if(lo.getOffice()==null || StringUtils.isBlank(lo.getOffice().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 人民调解委员会主管机关不存在; ");
						failureNum++;
					}else{
						peopleMediationCommitteeService.save(lo);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("人民调解委员会数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("人民调解委员会数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入人民调解委员会失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/peopleMediationCommittee/list?repage";
    }

	@RequiresPermissions("info:peopleMediationCommittee:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String batchid = request.getParameter("batchid");
			peopleMediationCommitteeService.batchDelete(batchid);
			addMessage(redirectAttributes, "删除人民调解委员会成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		return "redirect:" + adminPath + "/info/peopleMediationCommittee/list?repage";
	}
	
}