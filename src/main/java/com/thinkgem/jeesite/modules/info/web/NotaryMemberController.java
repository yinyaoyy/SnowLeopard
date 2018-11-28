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
import com.thinkgem.jeesite.modules.info.entity.NotaryAgency;
import com.thinkgem.jeesite.modules.info.entity.NotaryMember;
import com.thinkgem.jeesite.modules.info.service.NotaryAgencyService;
import com.thinkgem.jeesite.modules.info.service.NotaryMemberService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 公证员Controller
 * @author 王鹏
 * @version 2018-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/info/notaryMember")
public class NotaryMemberController extends BaseController {

	@Autowired
	private NotaryMemberService notaryMemberService;
	
	@ModelAttribute
	public NotaryMember get(@RequestParam(required=false) String id) {
		NotaryMember entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = notaryMemberService.get(id);
		}
		if (entity == null){
			entity = new NotaryMember();
		}
		return entity;
	}
	
	@RequiresPermissions("info:notaryMember:view")
	@RequestMapping(value = {"list", ""})
	public String list(NotaryMember notaryMember, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u =UserUtils.getUser();
		Page<NotaryMember> page = notaryMemberService.findPage(new Page<NotaryMember>(request, response), notaryMember,u); 
		//List<NotaryAgency> notaryAgencyList = notaryAgencyService.findList(new NotaryAgency());
		//model.addAttribute("notaryAgencyList", notaryAgencyList);
		model.addAttribute("page", page);
		return "modules/info/notaryMemberList";
	}

	@RequiresPermissions("info:notaryMember:view")
	@RequestMapping(value = "form")
	public String form(NotaryMember notaryMember, Model model) {
		//List<NotaryAgency> notaryAgencyList = notaryAgencyService.findList(new NotaryAgency());
		//model.addAttribute("notaryAgencyList", notaryAgencyList);
		model.addAttribute("notaryMember", notaryMember);
		return "modules/info/notaryMemberForm";
	}

	@RequiresPermissions("info:notaryMember:edit")
	@RequestMapping(value = "save")
	public String save(NotaryMember notaryMember, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, notaryMember)){
			return form(notaryMember, model);
		}
		if(StringUtils.isBlank(notaryMember.getId())){
			if(StringUtils.isNotBlank(notaryMemberService.personLicenseNumber(notaryMember.getLicenseNumber()))){
				addMessage(redirectAttributes, "保存失败，公证员已存在");
			}else{
				notaryMemberService.save(notaryMember);
				addMessage(redirectAttributes, "保存公证员成功");
			}
		}else{
			notaryMemberService.save(notaryMember);
			addMessage(redirectAttributes, "保存公证员成功");
		}
		return "redirect:"+Global.getAdminPath()+"/info/notaryMember/?repage";
	}
	
	@RequiresPermissions("info:notaryMember:edit")
	@RequestMapping(value = "delete")
	public String delete(NotaryMember notaryMember, RedirectAttributes redirectAttributes) {
		try {
			notaryMemberService.delete(notaryMember);
			addMessage(redirectAttributes, "删除公证员成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		} catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/notaryMember/?repage";
	}
	/**
	 * 导出公证员数据
	 * @param notaryMember
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:notaryMember:view")
    @RequestMapping(value = "export")
    public String exportFile(NotaryMember notaryMember, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<NotaryMember> list = null;
		try {
			if(notaryMember==null){
				notaryMember=new NotaryMember();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="公证员数据模板.xlsx";
            	list = new ArrayList<NotaryMember>();
            }else{
            	fileName = "公证员数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<NotaryMember> page = notaryMemberService.findPage(new Page<NotaryMember>(request, response, -1), notaryMember);
        		list = page.getList();
            }
    		new ExportExcel("公证员数据", NotaryMember.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/notaryMember/list?repage";
    }

	/**
	 * 导入公证员数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:notaryMember:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<NotaryMember> list = ei.getDataList(NotaryMember.class);
			NotaryMember notaryMember = null;
			for (int i=0;i<list.size();i++){
				notaryMember = list.get(i);
				try{
					BeanValidators.validateWithException(validator, notaryMember);
					if(notaryMember.getNotaryAgency()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 公证员所在公证处不存在; ");
						failureNum++;
					}/*else if(notaryMember.getOffice()==null){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 公证员所在主管机关不存在; ");
						failureNum++;
					}*/else if(StringUtils.isNotBlank(notaryMemberService.personLicenseNumber(notaryMember.getLicenseNumber()))){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 公证员已存在; ");
						failureNum++;
					}else{
						notaryMemberService.save(notaryMember);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("公证员数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("公证员数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入公证员失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/notaryMember/list?repage";
    }

	@RequiresPermissions("info:notaryMember:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String batchid = request.getParameter("batchid");
			notaryMemberService.batchDelete(batchid);
			addMessage(redirectAttributes, "删除公证员成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
	
		return "redirect:" + adminPath + "/info/notaryMember/list?repage";
	}
}