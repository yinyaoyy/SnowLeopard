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

import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.info.entity.PeopleMediation;
import com.thinkgem.jeesite.modules.info.service.PeopleMediationService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 人民调解员Controller
 * @author wanglin
 * @version 2018-05-25
 */
@Controller
@RequestMapping(value = "${adminPath}/info/peopleMediation")
public class PeopleMediationController extends BaseController {

	@Autowired
	private PeopleMediationService peopleMediationService;
	
	@ModelAttribute
	public PeopleMediation get(@RequestParam(required=false) String id) {
		PeopleMediation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = peopleMediationService.get(id);
		}
		if (entity == null){
			entity = new PeopleMediation();
		}
		return entity;
	}
	
	@RequiresPermissions("info:peopleMediation:view")
	@RequestMapping(value = {"list", ""})
	public String list(PeopleMediation peopleMediation, HttpServletRequest request, HttpServletResponse response, Model model) {
		//System.out.println("地区:"+peopleMediation.getArea().getId());
		//System.out.println("机构:"+peopleMediation.getAgencyId());
		User u =UserUtils.getUser();
		Page<PeopleMediation> page = peopleMediationService.findPage(new Page<PeopleMediation>(request, response), peopleMediation,u); 
		model.addAttribute("page", page);
		return "modules/info/peopleMediationList";
	}

	@RequiresPermissions("info:peopleMediation:view")
	@RequestMapping(value = "form")
	public String form(PeopleMediation peopleMediation, Model model) {
		model.addAttribute("peopleMediation", peopleMediation);
		return "modules/info/peopleMediationForm";
	}

	@RequiresPermissions("info:peopleMediation:edit")
	@RequestMapping(value = "save")
	public String save(PeopleMediation peopleMediation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, peopleMediation)){
			return form(peopleMediation, model);
		}
		
		String id = peopleMediation.getId();
		if(StringUtils.isBlank(id)){
			String p = peopleMediationService.findInfoByCardNo(peopleMediation.getIdCard());
			if(StringUtils.isBlank(p)){
				peopleMediationService.save(peopleMediation);
			}else{
				addMessage(redirectAttributes,"保存失败！人民调解员已存在");
			}
		}else{
			peopleMediationService.save(peopleMediation);
			addMessage(redirectAttributes, "保存人民调解员成功");
		}
		
		
		return "redirect:"+Global.getAdminPath()+"/info/peopleMediation/?repage";
	}
	
	@RequiresPermissions("info:peopleMediation:edit")
	@RequestMapping(value = "delete")
	public String delete(PeopleMediation peopleMediation, RedirectAttributes redirectAttributes) {
		try {
			peopleMediationService.delete(peopleMediation);
			addMessage(redirectAttributes, "删除人民调解员成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		} catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
	
		return "redirect:"+Global.getAdminPath()+"/info/peopleMediation/?repage";
	}
	/**
	 * 导出调解员数据
	 * @param peopleMediation
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:peopleMediation:view")
    @RequestMapping(value = "export")
    public String exportFile(PeopleMediation peopleMediation, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<PeopleMediation> list = null;
		try {
			if(peopleMediation==null){
				peopleMediation=new PeopleMediation();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="调解员数据模板.xlsx";
            	list = new ArrayList<PeopleMediation>();
            }else{
            	fileName = "调解员数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<PeopleMediation> page = peopleMediationService.findPage(new Page<PeopleMediation>(request, response, -1), peopleMediation);
        		list = page.getList();
            }
    		new ExportExcel("调解员数据", PeopleMediation.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/peopleMediation/list?repage";
    }

	/**
	 * 导入调解员数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:peopleMediation:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PeopleMediation> list = ei.getDataList(PeopleMediation.class);
			PeopleMediation peopleMediation = null;
			for (int i=0;i<list.size();i++){
				peopleMediation = list.get(i);
				try{
					BeanValidators.validateWithException(validator, peopleMediation);
					if(peopleMediation.getArea()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 调解员所在区域不存在; ");
						failureNum++;
					}else if(peopleMediation.getOffice()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 调解员所在调委会不存在; ");
						failureNum++;
					} else if(peopleMediationService.getPeopleMediationInfo(peopleMediation)){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 调解员已存在; ");
						failureNum++;
					}else{
						peopleMediationService.save(peopleMediation);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("调解员数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("调解员数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入调解员失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/peopleMediation/list?repage";
    }

	@RequiresPermissions("info:peopleMediation:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String batchid = request.getParameter("batchid");
			peopleMediationService.batchDelete(batchid);
			addMessage(redirectAttributes, "删除调解员成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		} catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:" + adminPath + "/info/peopleMediation/list?repage";
	}
}