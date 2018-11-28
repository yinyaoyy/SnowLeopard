/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.web;

import com.thinkgem.jeesite.api.dto.vo.info.LegalServiceOfficeExcelExportVo;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.info.entity.InfoLegalServiceOffice;
import com.thinkgem.jeesite.modules.info.service.InfoLegalServiceOfficeService;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基层法律服务所Controller
 * @author hejia
 * @version 2018-04-24
 */
@Controller
@RequestMapping(value = "${adminPath}/info/infoLegalServiceOffice")
public class InfoLegalServiceOfficeController extends BaseController {

	@Autowired
	private InfoLegalServiceOfficeService infoLegalServiceOfficeService;
	@Autowired
	AreaDao areaDao;
	
	@ModelAttribute
	public InfoLegalServiceOffice get(@RequestParam(required=false) String id) {
		InfoLegalServiceOffice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = infoLegalServiceOfficeService.get(id);
		}
		if (entity == null){
			entity = new InfoLegalServiceOffice();
		}
		return entity;
	}
	
	@RequiresPermissions("info:infoLegalServiceOffice:view")
	@RequestMapping(value = {"list", ""})
	public String list(InfoLegalServiceOffice infoLegalServiceOffice, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u = UserUtils.getUser();
		Page<InfoLegalServiceOffice> page = infoLegalServiceOfficeService.findPage(new Page<InfoLegalServiceOffice>(request, response), infoLegalServiceOffice,u); 
		model.addAttribute("page", page);
		return "modules/info/infoLegalServiceOfficeList";
	}

	@RequiresPermissions("info:infoLegalServiceOffice:view")
	@RequestMapping(value = "form")
	public String form(InfoLegalServiceOffice infoLegalServiceOffice, Model model) {
		model.addAttribute("infoLegalServiceOffice", infoLegalServiceOffice);
		return "modules/info/infoLegalServiceOfficeForm";
	}

	@RequiresPermissions("info:infoLegalServiceOffice:edit")
	@RequestMapping(value = "save")
	public String save(InfoLegalServiceOffice infoLegalServiceOffice, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, infoLegalServiceOffice)){
			return form(infoLegalServiceOffice, model);
		}
		if(StringUtils.isBlank(infoLegalServiceOffice.getId())){
			if(infoLegalServiceOffice.getArea()==null || StringUtils.isBlank(infoLegalServiceOffice.getArea().getName())){
				addMessage(redirectAttributes, "保存失败，基层法律服务所 所在区域不存在");
			}else if(StringUtils.isNotBlank(infoLegalServiceOfficeService.personInstitution(infoLegalServiceOffice.getNo(),infoLegalServiceOffice.getArea().getId(),infoLegalServiceOffice.getTown().getId()))){
				addMessage(redirectAttributes, "保存失败，基层法律服务所已存在");
			}else{
				infoLegalServiceOfficeService.save(infoLegalServiceOffice);
				addMessage(redirectAttributes, "保存基层法律服务所管理成功");
			}
		}else{
			infoLegalServiceOfficeService.save(infoLegalServiceOffice);
			addMessage(redirectAttributes, "保存基层法律服务所管理成功");
		}
		return "redirect:"+Global.getAdminPath()+"/info/infoLegalServiceOffice/?repage";
	}
	
	@RequiresPermissions("info:infoLegalServiceOffice:edit")
	@RequestMapping(value = "delete")
	public String delete(InfoLegalServiceOffice infoLegalServiceOffice, RedirectAttributes redirectAttributes) {
		try {
			infoLegalServiceOfficeService.delete(infoLegalServiceOffice);
			addMessage(redirectAttributes, "删除基层法律服务所管理成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/infoLegalServiceOffice/?repage";
	}

    /**
	 * 导入基层法律服务所数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:infoJudicialAuthentication:edit")
	@RequestMapping(value = "import", method=RequestMethod.POST)
	public String inport(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<InfoLegalServiceOffice> list = ei.getDataList(InfoLegalServiceOffice.class);
			InfoLegalServiceOffice lo = null;
			Map<String,Area> areaBuffer = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (int i=0;i<list.size();i++){
				lo = list.get(i);
				try{
					BeanValidators.validateWithException(validator, lo);
					if(lo.getArea()==null || StringUtils.isBlank(lo.getArea().getName())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 基层法律服务所 所在区域不存在; ");
						failureNum++;
					}else{
                        infoLegalServiceOfficeService.save(lo);
						successNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("基层法律服务所数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败 ;");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("基层法律服务所数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入基层法律服务所数据失败！失败信息："+e.getLocalizedMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/info/infoLegalServiceOffice/?repage";
	}

	/**
	 * 将excel模板转换为po
	 * @param lo
	 * @param areaBuffer
	 * @return
	 */
    private InfoLegalServiceOffice excellTemplateToInfoLegalServiceOffice(LegalServiceOfficeExcelExportVo lo,
                                                                          Map<String, Area> areaBuffer,
                                                                          SimpleDateFormat sdf) throws ParseException {
        InfoLegalServiceOffice legalServiceOffice = new InfoLegalServiceOffice();
        Area area = areaBuffer.get(lo.getArea());
        if (null == area) {
            List<Area> areaList = areaDao.findByName(lo.getArea());
            if (null != areaList && areaList.size() > 0) {
                area = areaList.get(0);
                areaBuffer.put(lo.getArea(), area);
            }
        }
        legalServiceOffice.setName(lo.getName());
        legalServiceOffice.setPersonInCharge(lo.getPersonInCharge());
        legalServiceOffice.setPracticeLicenseNum(lo.getPracticeLicenseNum());
        if (StringUtils.isNotEmpty(lo.getPracticeLicenseExpiryTime())) {
            legalServiceOffice.setPracticeLicenseExpiryTime(sdf.parse(lo.getPracticeLicenseExpiryTime()));
        }
        legalServiceOffice.setOccupationalState("normal");
        legalServiceOffice.setLicenseNumber(lo.getLicenseNumber());
        legalServiceOffice.setApprovalNumber(lo.getApprovalNumber());
        if (StringUtils.isNotEmpty(lo.getApprovalDate())) {
            legalServiceOffice.setApprovalDate(sdf.parse(lo.getApprovalDate()));
        }
        legalServiceOffice.setCompetentAuthoritiesName(lo.getCompetentAuthoritiesName());
        legalServiceOffice.setAddress(lo.getAddress());
        legalServiceOffice.setPhone(lo.getPhone());
        legalServiceOffice.setFax(lo.getFax());
        legalServiceOffice.setOfficialWebAddress(lo.getOfficialWebAddress());
        legalServiceOffice.setIntroduction(lo.getIntroduction());
        legalServiceOffice.setArea(area);
        legalServiceOffice.setCoordinate(lo.getCoordinate());
        return legalServiceOffice;
    }

	/**
	 * 导出基层法律服务所数据
	 * @param lo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("info:lowOffice:view")
	@RequestMapping(value = "export")
	public void exportFile(InfoLegalServiceOffice lo, HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<InfoLegalServiceOffice> list = null;
		try {
			if(lo==null){
				lo=new InfoLegalServiceOffice();
			}
			String fileName = "基层法律服务所数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
			//不分页，获取全部数据
			Page<InfoLegalServiceOffice> page = infoLegalServiceOfficeService.findPage(new Page<InfoLegalServiceOffice>(request, response), lo);
			list = page.getList();
			/*
			List<InfoLegalServiceOffice> excellExportVoList = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for(InfoLegalServiceOffice ilso:list){
				excellExportVo = new LegalServiceOfficeExcelExportVo(ilso,sdf);				
				excellExportVoList.add(excellExportVo);
			}*/
			new ExportExcel("基层法律服务所数据", InfoLegalServiceOffice.class).setDataList(list).write(response, fileName).dispose();
           /* response.reset();
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/octet-stream");
            outputStream = response.getOutputStream();
            exportExcel.write(outputStream);*/
		}catch (Exception e){
			e.printStackTrace();
			response.sendRedirect("/info/infoLegalServiceOffice/list");
		}
	}

}