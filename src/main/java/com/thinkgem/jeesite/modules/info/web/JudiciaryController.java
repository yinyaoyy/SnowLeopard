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
import com.thinkgem.jeesite.modules.info.entity.Judiciary;
import com.thinkgem.jeesite.modules.info.service.JudiciaryService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 司法所Controller
 * @author wanglin
 * @version 2018-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/info/judiciary")
public class JudiciaryController extends BaseController {

	@Autowired
	private JudiciaryService judiciaryService;
	
	@ModelAttribute
	public Judiciary get(@RequestParam(required=false) String id) {
		Judiciary entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = judiciaryService.get(id);
		}
		if (entity == null){
			entity = new Judiciary();
		}
		return entity;
	}
	
	@RequiresPermissions("info:judiciary:view")
	@RequestMapping(value = {"list", ""})
	public String list(Judiciary judiciary, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u = UserUtils.getUser();
		Page<Judiciary> page = judiciaryService.findPage(new Page<Judiciary>(request, response), judiciary,u); 
		model.addAttribute("page", page);
		return "modules/info/judiciaryList";
	}

	@RequiresPermissions("info:judiciary:view")
	@RequestMapping(value = "form")
	public String form(Judiciary judiciary, Model model) {
		model.addAttribute("judiciary", judiciary);
		return "modules/info/judiciaryForm";
	}

	@RequiresPermissions("info:judiciary:edit")
	@RequestMapping(value = "save")
	public String save(Judiciary judiciary, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, judiciary)){
			return form(judiciary, model);
		}
		judiciaryService.save(judiciary);
		addMessage(redirectAttributes, "保存司法所成功");
		return "redirect:"+Global.getAdminPath()+"/info/judiciary/?repage";
	}
	
	@RequiresPermissions("info:judiciary:edit")
	@RequestMapping(value = "delete")
	public String delete(Judiciary judiciary, RedirectAttributes redirectAttributes) {
		try {
			judiciaryService.delete(judiciary);
			addMessage(redirectAttributes, "删除司法所成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/judiciary/?repage";
	}
	/**
	 * 导出司法所数据
	 * @param lo
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:judiciary:view")
    @RequestMapping(value = "export")
    public String exportFile(Judiciary lo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<Judiciary> list = null;
		try {
			if(lo==null){
				lo=new Judiciary();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="司法所数据模板.xlsx";
            	list = new ArrayList<Judiciary>();
            	//做一个模板数据，仅供参考使用
            	Judiciary lo_template = new Judiciary();
            	lo_template.setName("模板数据");
            	Area a = new Area();
            	a.setName("锡林浩特市");
            	lo_template.setAddress("内蒙古自治区锡林郭勒盟锡林浩特市那达慕街1号");
            	lo_template.setPhone("0479-88889999");
            	lo_template.setIntroduction("模板样例数据，仅供参考，上传时请删除此记录！！！");
            	lo_template.setCoordinate("116.093297,43.932355");
            	list.add(lo_template);
            }else{
            	fileName = "司法所数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<Judiciary> page = judiciaryService.findPage(new Page<Judiciary>(request, response, -1), lo);
        		list = page.getList();
            }
    		new ExportExcel("司法所数据", Judiciary.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/judiciary/list?repage";
    }
	/**
	 * 导入司法所数据
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
			List<Judiciary> list = ei.getDataList(Judiciary.class);
			Judiciary lo = null;
			for (int i=0;i<list.size();i++){
				lo = list.get(i);
				try{
					BeanValidators.validateWithException(validator, lo);
					if(lo.getArea()==null || StringUtils.isBlank(lo.getArea().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 司法所所在旗县不存在; ");
						failureNum++;
					}else if(lo.getTown()==null || StringUtils.isBlank(lo.getTown().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 司法所所在乡镇不存在; ");
						failureNum++;
					}else if(judiciaryService.findList(lo).size()>0){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 司法所已存在; ");
						failureNum++;
					}else{
						judiciaryService.save(lo);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("司法所数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("司法所数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入司法所失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/judiciary/list?repage";
    }
}