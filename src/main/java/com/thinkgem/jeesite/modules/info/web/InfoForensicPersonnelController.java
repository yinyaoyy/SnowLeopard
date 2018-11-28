/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.thinkgem.jeesite.api.dto.vo.info.ForensicPersonnelExcellExportVo;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.info.dao.InfoJudicialAuthenticationDao;
import com.thinkgem.jeesite.modules.info.entity.InfoJudicialAuthentication;
import com.thinkgem.jeesite.modules.info.entity.Lawyer;
import com.thinkgem.jeesite.modules.info.service.InfoJudicialAuthenticationService;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.info.entity.InfoForensicPersonnel;
import com.thinkgem.jeesite.modules.info.service.InfoForensicPersonnelService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鉴定人员信息管理Controller
 * @author hejia
 * @version 2018-04-24
 */
@Controller
@RequestMapping(value = "${adminPath}/info/infoForensicPersonnel")
public class InfoForensicPersonnelController extends BaseController {

	@Autowired
	private InfoForensicPersonnelService infoForensicPersonnelService;
	@Autowired
	InfoJudicialAuthenticationService judicialAuthenticationService;
	@Autowired
	InfoJudicialAuthenticationDao judicialAuthenticationDao;
	@Autowired
	AreaService areaService;
	@Autowired
	AreaDao areaDao;

	@ModelAttribute
	public InfoForensicPersonnel get(@RequestParam(required=false) String id) {
		InfoForensicPersonnel entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = infoForensicPersonnelService.get(id);
		}
		if (entity == null){
			entity = new InfoForensicPersonnel();
		}
		return entity;
	}
	
	@RequiresPermissions("info:infoForensicPersonnel:view")
	@RequestMapping(value = {"list", ""})
	public String list(InfoForensicPersonnel infoForensicPersonnel, HttpServletRequest request, HttpServletResponse response, Model model) {
        if(infoForensicPersonnel.getArea() != null && StringUtils.isNotEmpty(infoForensicPersonnel.getArea().getId())){
            infoForensicPersonnel.setArea(areaService.get(infoForensicPersonnel.getArea().getId()));
        }
        User u =UserUtils.getUser();
		Page<InfoForensicPersonnel> page = infoForensicPersonnelService.findPage(new Page<InfoForensicPersonnel>(request, response), infoForensicPersonnel,u); 
		model.addAttribute("page", page);
		return "modules/info/infoForensicPersonnelList";
	}

	@RequiresPermissions("info:infoForensicPersonnel:view")
	@RequestMapping(value = "form")
	public String form(InfoForensicPersonnel infoForensicPersonnel, Model model) {
		model.addAttribute("infoForensicPersonnel", infoForensicPersonnel);
		return "modules/info/infoForensicPersonnelForm";
	}

	@RequiresPermissions("info:infoForensicPersonnel:edit")
	@RequestMapping(value = "save")
	public String save(InfoForensicPersonnel infoForensicPersonnel, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, infoForensicPersonnel)){
			return form(infoForensicPersonnel, model);
		}
		infoForensicPersonnelService.save(infoForensicPersonnel);
		addMessage(redirectAttributes, "保存鉴定人员信息管理成功");
		return "redirect:"+Global.getAdminPath()+"/info/infoForensicPersonnel/?repage";
	}
	
	@RequiresPermissions("info:infoForensicPersonnel:edit")
	@RequestMapping(value = "delete")
	public String delete(InfoForensicPersonnel infoForensicPersonnel, RedirectAttributes redirectAttributes) {
		try {
			infoForensicPersonnelService.delete(infoForensicPersonnel);
			addMessage(redirectAttributes, "删除鉴定人员信息管理成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/infoForensicPersonnel/?repage";
	}

/**
     * 导入司法鉴定人员
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("info:infoJudicialAuthentication:edit")
    @RequestMapping(value = "inport", method=RequestMethod.POST)
    public String inport(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<InfoForensicPersonnel> list = ei.getDataList(InfoForensicPersonnel.class);
            InfoForensicPersonnel lo = null;
            Map<String,InfoJudicialAuthentication> judicialAuthenticationBuffer = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i=0;i<list.size();i++){
                lo = list.get(i);
                try{
                    BeanValidators.validateWithException(validator, lo);
                    if(lo.getArea()==null || StringUtils.isEmpty(lo.getArea().getId())){
                        failureMsg.append("<br/>第"+(i+1)+"条导入失败: 司法鉴定人员在区域不存在; ");
                        failureNum++;
                    }else{
                        infoForensicPersonnelService.save(lo);
                        successNum++;
                    }
                }catch(ConstraintViolationException ex){
                    failureMsg.append("司法鉴定人员数据导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList){
                        failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
                        failureNum++;
                    }
                }catch (Exception ex) {
                    failureMsg.append("司法鉴定人员数据导入失败："+ex.getLocalizedMessage());
                }
            }
            if (failureNum>0){
                failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入司法鉴定人员失败！失败信息："+e.getLocalizedMessage());
        }
        return "redirect:"+Global.getAdminPath()+"/info/infoForensicPersonnel/?repage";
    }
    /**
     * 导出司法鉴定所数据
     * @param lo
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("info:lowOffice:view")
    @RequestMapping(value = "export")
    public String exportFile(InfoForensicPersonnel lo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<InfoForensicPersonnel> list = null;
		try {
			if(lo==null){
				lo=new InfoForensicPersonnel();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="司法鉴定人员数据模板.xlsx";
            	list = new ArrayList<InfoForensicPersonnel>();
            }else{
            	fileName = "司法鉴定人员数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<InfoForensicPersonnel> page = infoForensicPersonnelService.findPage(new Page<InfoForensicPersonnel>(request, response, -1), lo);
        		list = page.getList();
            }
    		new ExportExcel("司法鉴定人员数据", InfoForensicPersonnel.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/infoForensicPersonnel/list?repage";
    }

}