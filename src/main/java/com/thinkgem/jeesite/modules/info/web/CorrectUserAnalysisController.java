/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.info.entity.CorrectUser;
import com.thinkgem.jeesite.modules.info.entity.CorrectUserAnalysis;
import com.thinkgem.jeesite.modules.info.entity.CorrectUserAnalysisVo;
import com.thinkgem.jeesite.modules.info.service.CorrectUserAnalysisService;
import com.thinkgem.jeesite.modules.info.service.CorrectUserService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 社区矫正心理生理分析Controller
 * @author wanglin
 * @version 2018-07-28
 */
@Controller
@RequestMapping(value = "${adminPath}/info/correctUserAnalysis")
public class CorrectUserAnalysisController extends BaseController {

	@Autowired
	private CorrectUserAnalysisService correctUserAnalysisService;
	@Autowired
	private CorrectUserService correctUserService;
	@ModelAttribute
	public CorrectUserAnalysis get(@RequestParam(required=false) String id) {
		CorrectUserAnalysis entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = correctUserAnalysisService.get(id);
		}
		if (entity == null){
			entity = new CorrectUserAnalysis();
		}
		return entity;
	}
	@RequiresPermissions("info:correctUserAnalysis:view")
	@RequestMapping(value = {"list", ""})
	public String list(CorrectUserAnalysis correctUserAnalysis, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(correctUserAnalysis.getIdCard()==null) {
			correctUserAnalysis.setIdCard("");	
		}
		Page<CorrectUserAnalysisVo> page = correctUserAnalysisService.findPageVo(new Page<CorrectUserAnalysis>(request, response), correctUserAnalysis); 
		model.addAttribute("page", page);
		CorrectUser  correctUser=correctUserService.getByIdCard(correctUserAnalysis.getIdCard());
		model.addAttribute("correctUser", correctUser);
		return "modules/info/correctUserAnalysisList";
	}

	@RequiresPermissions("info:correctUserAnalysis:view")
	@RequestMapping(value = "form")
	public String form(CorrectUserAnalysis correctUserAnalysis, Model model) {
		model.addAttribute("correctUserAnalysis", correctUserAnalysis);
		return "modules/info/correctUserAnalysisForm";
	}

	@RequiresPermissions("info:correctUserAnalysis:edit")
	@RequestMapping(value = "save")
	public String save(CorrectUserAnalysis correctUserAnalysis, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, correctUserAnalysis)){
			return form(correctUserAnalysis, model);
		}
		correctUserAnalysisService.save(correctUserAnalysis);
		addMessage(redirectAttributes, "保存社区矫正心理生理分析成功");
		return "redirect:"+Global.getAdminPath()+"/info/correctUserAnalysis/?repage";
	}
	
	@RequiresPermissions("info:correctUserAnalysis:edit")
	@RequestMapping(value = "delete")
	public String delete(CorrectUserAnalysis correctUserAnalysis, RedirectAttributes redirectAttributes) {
		correctUserAnalysisService.delete(correctUserAnalysis);
		addMessage(redirectAttributes, "删除社区矫正心理生理分析成功");
		return "redirect:"+Global.getAdminPath()+"/info/correctUserAnalysis/?repage";
	}
	/**
	 * 导出社区矫正心理生理分析数据
	 * @param lo
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:correctUserAnalysis:view")
    @RequestMapping(value = "export")
    public String exportFile(CorrectUserAnalysis lo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String type=request.getParameter("downType");
		boolean isTemplate = type!=null&&"1".equals(type);
		List<CorrectUserAnalysis> list = null;
		try {
			if(lo==null){
				lo=new CorrectUserAnalysis();
			}
			String fileName = "";
            if(isTemplate){
            	fileName="社区矫正心理生理分析数据模板.xlsx";
            	list = new ArrayList<CorrectUserAnalysis>();
            	//做一个模板数据，仅供参考使用
            }else{
            	fileName = "社区矫正心理生理分析数据-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            	//不分页，获取全部数据
        		Page<CorrectUserAnalysis> page = correctUserAnalysisService.findPage(new Page<CorrectUserAnalysis>(request, response, -1), lo);
        		list = page.getList();
            }
    		new ExportExcel("社区矫正心理生理分析数据", CorrectUserAnalysis.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, (isTemplate?"下载模板":"导出数据")+"失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/correctUserAnalysis/list?repage";
    }
	/**
	 * 导入社区矫正心理生理分析数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("info:correctUserAnalysis:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		String idCard="";
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CorrectUserAnalysis> list = ei.getDataList(CorrectUserAnalysis.class);
			CorrectUserAnalysis correctUserAnalysis = null;
			for (int i=0;i<list.size();i++){
				correctUserAnalysis = list.get(i);
				try{
					if(StringUtils.isBlank(correctUserAnalysis.getIdCard())){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: 身份证号不能为空; ");
						failureNum++;
					} else {
						CorrectUser correctUser=correctUserService.getByIdCard(correctUserAnalysis.getIdCard());
						if(correctUser==null||correctUser.getIdCard()==null) {
							failureMsg.append("<br/>第"+(i+1)+"条导入失败: 改社区矫正人员不存在; ");
							failureNum++;	
						}else {
							correctUserAnalysisService.save(correctUserAnalysis);
							idCard=correctUserAnalysis.getIdCard();
							successNum++;
						}
					}
					
				}catch(ConstraintViolationException ex){
					failureMsg.append("社区矫正心理生理分析数据导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append("<br/>第"+(i+1)+"条导入失败: "+message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("社区矫正心理生理分析数据导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入社区矫正心理生理分析失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/info/correctUserAnalysis/list?idCard="+idCard+"&repage";
    }
	@RequiresPermissions("info:correctUserAnalysis:view")
	@RequestMapping(value = "analysis")
	public String analysis(CorrectUserAnalysis correctUserAnalysis, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("correctUserAnalysis", correctUserAnalysis);
		return "modules/chart/info/correctUserAnalysis";
	}
	@RequiresPermissions("info:correctUserAnalysis:view")
	@RequestMapping(value = "analysisList")
	@ResponseBody
	public Map<String,List<CorrectUserAnalysis>> analysisList(CorrectUserAnalysis correctUserAnalysis, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(correctUserAnalysis.getIdCard()==null) {
			correctUserAnalysis.setIdCard("");	
		}
		List<Dict> dictList=DictUtils.getDictList("info_correct_analysis_type");
		Map<String,List<CorrectUserAnalysis>> map=new HashMap();
		for (Dict dict : dictList) {
			correctUserAnalysis.setType(dict.getValue());
			map.put('"'+dict.getLabel()+'"', correctUserAnalysisService.findList(correctUserAnalysis));
		}
		return  map;
	}
	@RequiresPermissions("info:correctUserAnalysis:view")
	@RequestMapping(value = "analysisOneDayPage")
	public String analysisOneDayPage(CorrectUserAnalysis correctUserAnalysis, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("correctUserAnalysis", correctUserAnalysis);
		return "modules/chart/info/correctUserAnalysisOneDay";
	}
	@RequiresPermissions("info:correctUserAnalysis:view")
	@RequestMapping(value = "analysisOneDay")
	@ResponseBody
	public List<CorrectUserAnalysis> analysisOneDay(CorrectUserAnalysis correctUserAnalysis, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(correctUserAnalysis.getIdCard()==null) {
			correctUserAnalysis.setIdCard("");	
		}
		Date begin=correctUserAnalysis.getAnalysisDate();
		Calendar c = Calendar.getInstance();  
        c.setTime(begin);  
        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天 
        correctUserAnalysis.setBeginDate(begin);
        correctUserAnalysis.setEndDate(c.getTime());
		List<CorrectUserAnalysis> list=correctUserAnalysisService.findList(correctUserAnalysis);
		return list;
	} 
}