/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.web;

import com.thinkgem.jeesite.api.dto.vo.info.JudicialAuthenticationExcellExportVo;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.info.entity.InfoJudicialAuthentication;
import com.thinkgem.jeesite.modules.info.service.InfoJudicialAuthenticationService;
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
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鉴定所管理Controller
 * @author hejia
 * @version 2018-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/info/infoJudicialAuthentication")
public class InfoJudicialAuthenticationController extends BaseController {

	@Autowired
	private InfoJudicialAuthenticationService infoJudicialAuthenticationService;
	@Autowired
	AreaDao areaDao;
	
	@ModelAttribute
	public InfoJudicialAuthentication get(@RequestParam(required=false) String id) {
		InfoJudicialAuthentication entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = infoJudicialAuthenticationService.get(id);
		}
		if (entity == null){
			entity = new InfoJudicialAuthentication();
		}
		return entity;
	}
	
	@RequiresPermissions("info:infoJudicialAuthentication:view")
	@RequestMapping(value = {"list", ""})
	public String list(InfoJudicialAuthentication infoJudicialAuthentication, HttpServletRequest request, HttpServletResponse response, Model model) {
		User u = UserUtils.getUser();
		Page<InfoJudicialAuthentication> page = infoJudicialAuthenticationService.findPage(new Page<InfoJudicialAuthentication>(request, response), infoJudicialAuthentication,u); 
		model.addAttribute("page", page);
		return "modules/info/infoJudicialAuthenticationList";
	}

	@RequiresPermissions("info:infoJudicialAuthentication:view")
	@RequestMapping(value = "form")
	public String form(InfoJudicialAuthentication infoJudicialAuthentication, Model model) {
		model.addAttribute("infoJudicialAuthentication", infoJudicialAuthentication);
		return "modules/info/infoJudicialAuthenticationForm";
	}

	@RequiresPermissions("info:infoJudicialAuthentication:edit")
	@RequestMapping(value = "save")
	public String save(InfoJudicialAuthentication infoJudicialAuthentication, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, infoJudicialAuthentication)){
			return form(infoJudicialAuthentication, model);
		}
		infoJudicialAuthenticationService.save(infoJudicialAuthentication);
		addMessage(redirectAttributes, "保存司法所信息管理成功");
		return "redirect:"+Global.getAdminPath()+"/info/infoJudicialAuthentication/?repage";
	}
	
	@RequiresPermissions("info:infoJudicialAuthentication:edit")
	@RequestMapping(value = "delete")
	public String delete(InfoJudicialAuthentication infoJudicialAuthentication, RedirectAttributes redirectAttributes) {
	
		try {
			infoJudicialAuthenticationService.delete(infoJudicialAuthentication);
			addMessage(redirectAttributes, "删除司法所信息管理成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		}catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
		
		return "redirect:"+Global.getAdminPath()+"/info/infoJudicialAuthentication/?repage";
	}

    /**
     * 导入司法鉴定所
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
            List<JudicialAuthenticationExcellExportVo> list = ei.getDataList(JudicialAuthenticationExcellExportVo.class);
            JudicialAuthenticationExcellExportVo lo = null;
            Map<String,Area> areaBuffer = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i=0;i<list.size();i++){
                lo = list.get(i);
                try{
                    BeanValidators.validateWithException(validator, lo);
                    if(lo.getArea()==null || StringUtils.isEmpty(lo.getArea())){
                        failureMsg.append("<br/>第"+(i+1)+"条导入失败: 司法鉴定所在区域不存在; ");
                        failureNum++;
                    }else{
                        infoJudicialAuthenticationService.save(excellTemplateToInfoJudicialAuthentication(lo,areaBuffer,sdf));
                        successNum++;
                    }
                }catch(ConstraintViolationException ex){
                    failureMsg.append("司法鉴定所数据导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList){
                        failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
                        failureNum++;
                    }
                }catch (Exception ex) {
                    failureMsg.append("司法鉴定所数据导入失败："+ex.getLocalizedMessage());
                }
            }
            if (failureNum>0){
                failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入司法鉴定所失败！失败信息："+e.getLocalizedMessage());
        }
		return "redirect:"+Global.getAdminPath()+"/info/infoJudicialAuthentication/?repage";
	}

    /**
     * 将excel模板转换为po
     * @param lo
     * @param areaBuffer
     * @return
     */
    private InfoJudicialAuthentication excellTemplateToInfoJudicialAuthentication(JudicialAuthenticationExcellExportVo lo, Map<String,Area> areaBuffer,SimpleDateFormat sdf) throws ParseException {
        InfoJudicialAuthentication judicialAuthentication = new InfoJudicialAuthentication();
        Area area = areaBuffer.get(lo.getArea());
        if(null == area){
            List<Area> areaList = areaDao.findByName(lo.getArea());
            if(null != areaList && areaList.size() > 0) {
                area = areaList.get(0);
                areaBuffer.put(lo.getArea(),area);
            }
        }
        judicialAuthentication.setArea(area);
        judicialAuthentication.setName(lo.getName());
        judicialAuthentication.setAddress(lo.getAddress());
        judicialAuthentication.setPhone(lo.getPhone());
        judicialAuthentication.setIntroduction(lo.getIntroduction());
        judicialAuthentication.setCoordinate(lo.getCoordinate());
        judicialAuthentication.setFoundingTime(sdf.parse(lo.getFoundingTime()));
        judicialAuthentication.setBusinessExpertise(lo.getBusinessExpertise());
        judicialAuthentication.setPersonInCharge(lo.getPersonInCharge());
        judicialAuthentication.setTeamSize(lo.getTeamSize());
        judicialAuthentication.setPracticeLicenseNum(lo.getPracticeLicenseNum());
        judicialAuthentication.setScopeOfBusiness(lo.getScopeOfBusiness());
        judicialAuthentication.setOfficialWebAddress(lo.getOfficialWebAddress());
        return judicialAuthentication;
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
    public void exportFile(InfoJudicialAuthentication lo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<InfoJudicialAuthentication> list = null;
        OutputStream outputStream = null;
        String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		
        try {
            if(lo==null){
                lo=new InfoJudicialAuthentication();
            }
            String fileName = "司法鉴定所数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            //不分页，获取全部数据
            Page<InfoJudicialAuthentication> page = infoJudicialAuthenticationService.findPage(new Page<InfoJudicialAuthentication>(request, response), lo);
            list = page.getList();
            List<JudicialAuthenticationExcellExportVo> excellExportVoList = new ArrayList<>();
            if(isTemplate) {
            	 fileName = "司法鉴定所数据模板.xlsx";
            }else {
            JudicialAuthenticationExcellExportVo excellExportVo = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(InfoJudicialAuthentication judicialAuthentication:list){
                excellExportVo = new JudicialAuthenticationExcellExportVo();
                excellExportVo.setName(judicialAuthentication.getName());
                excellExportVo.setAddress(judicialAuthentication.getAddress());
                excellExportVo.setPhone(judicialAuthentication.getPhone());
                excellExportVo.setIntroduction(judicialAuthentication.getIntroduction());
                excellExportVo.setArea(judicialAuthentication.getArea().getName());
                excellExportVo.setCoordinate(judicialAuthentication.getCoordinate());
                excellExportVo.setFoundingTime(sdf.format(judicialAuthentication.getFoundingTime()));
                excellExportVo.setBusinessExpertise(judicialAuthentication.getBusinessExpertise());
                excellExportVo.setPersonInCharge(judicialAuthentication.getPersonInCharge());
                excellExportVo.setTeamSize(judicialAuthentication.getTeamSize());
                excellExportVo.setPracticeLicenseNum(judicialAuthentication.getPracticeLicenseNum());
                excellExportVo.setScopeOfBusiness(judicialAuthentication.getScopeOfBusiness());
                excellExportVo.setOfficialWebAddress(judicialAuthentication.getOfficialWebAddress());
                excellExportVoList.add(excellExportVo);
            }
            }
           new ExportExcel("司法鉴定所数据", JudicialAuthenticationExcellExportVo.class).setDataList(excellExportVoList).write(response, fileName).dispose();
           /* response.reset();
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/octet-stream");
            outputStream = response.getOutputStream();
            exportExcel.write(outputStream);*/
        }catch (Exception e){
            e.printStackTrace();
            response.sendRedirect("/info/infoJudicialAuthentication/list");
        }finally {
            if(null != outputStream){
                outputStream.close();
            }
        }
    }
    
	

}