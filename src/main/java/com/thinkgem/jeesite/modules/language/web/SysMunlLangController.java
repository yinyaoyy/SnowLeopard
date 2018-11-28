/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.language.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.language.entity.SysMunlLang;
import com.thinkgem.jeesite.modules.language.service.SysMunlLangService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * languageController
 * @author language
 * @version 2017-09-01
 */
@Controller
@RequestMapping(value = "${adminPath}/language/sysMunlLang")
public class SysMunlLangController extends BaseController {

	@Autowired
	private SysMunlLangService sysMunlLangService;
	
	@ModelAttribute
	public SysMunlLang get(@RequestParam(required=false) String id) {
		SysMunlLang entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysMunlLangService.get(id);
		}
		if (entity == null){
			entity = new SysMunlLang();
		}
		return entity;
	}
	
	@RequiresPermissions("language:sysMunlLang:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysMunlLang sysMunlLang, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysMunlLang> page= new Page<SysMunlLang>(request, response);
		page.setOrderBy("a.lang_key and a.update_date DESC");
		page = sysMunlLangService.findPage(page, sysMunlLang); 
		model.addAttribute("page", page);
		if(!model.containsAttribute("message")){
			List<Dict> dList=DictUtils.getDictListByLanguage("act_langtype","CN");
			SysMunlLang lang=new SysMunlLang();
			lang.setOperationType("1");
			lang.setLangCode("CN");
			String message="";
			int countM=sysMunlLangService.getCountByLanguage(lang);
			int countP=0;
			boolean  flag=false;
			for (Dict dict : dList) {
				if(!"CN".equals(dict.getValue())){
					lang.setLangCode(dict.getValue());
					countP=sysMunlLangService.getCountByLanguage(lang);
					if(countM>countP){
						message+="\""+dict.getLabel()+"\" 语言 比 \"中文\" 语言 在 语言归属为 \"平台\" 的分类上少"+(countM-countP)+"个语言数据，请及时完善语言信息！";
						flag=true;
					}
				}
			}
			if(flag){
				message+="请及时完善信息";
				model.addAttribute("message", message);
			}
		}
		return "modules/language/sysMunlLangList";
	}

	@RequiresPermissions("language:sysMunlLang:view")
	@RequestMapping(value = "form")
	public String form(SysMunlLang sysMunlLang, Model model) {
		List<SysMunlLang> list= sysMunlLangService.getLanguageAscription();
		model.addAttribute("sysMunlLang", sysMunlLang);
		model.addAttribute("sysOnlylist", list);
		return "modules/language/sysMunlLangForm";
	}

	@RequiresPermissions("language:sysMunlLang:edit")
	@RequestMapping(value = "save")
	public String save(HttpServletRequest request,SysMunlLang sysMunlLang, Model model, RedirectAttributes redirectAttributes) throws ParseException {
		if (!beanValidator(model, sysMunlLang)){
			return form(sysMunlLang, model);
		}
		User user = UserUtils.getUser();
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String hehe = df.format(now); 
		Date date = df.parse(hehe);  
		sysMunlLang.setCreateDate(date);
		sysMunlLang.setCreateName(user.getName());
		String lang =  DictUtils.getCurrentLanguage();
		if(lang==null){
			lang="";
		}
		@SuppressWarnings("unchecked")
		Map<String,String> object = (Map<String, String>) CacheUtils.get(sysMunlLang.getLangCode()+"_"+sysMunlLang.getLanguageAscription());
		if(object!=null){
			object.put(sysMunlLang.getLangKey(), sysMunlLang.getLangContext());
		}
		CacheUtils.put(sysMunlLang.getLangCode()+"_"+sysMunlLang.getLanguageAscription(), object);
		sysMunlLangService.save(sysMunlLang);
		addMessage(redirectAttributes, "保存语言成功");
		return "redirect:"+Global.getAdminPath()+"/language/sysMunlLang/?repage";
	}
	
	@RequiresPermissions("language:sysMunlLang:edit")
	@RequestMapping(value = "delete")
	public String delete(SysMunlLang sysMunlLang, RedirectAttributes redirectAttributes) {
		sysMunlLangService.delete(sysMunlLang);
		addMessage(redirectAttributes, "删除语言成功");
		return "redirect:"+Global.getAdminPath()+"/language/sysMunlLang/?repage";
	}
	@RequiresPermissions("language:sysMunlLang:view")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		String batchid = request.getParameter("batchid");
		List<String> deList = new ArrayList<String>();
		String[] strs = batchid.split(",");
		for (String str : strs){
			deList.add(str);
		}
		sysMunlLangService.batchDelete(deList);
		addMessage(redirectAttributes, "删除语言成功");
		return "redirect:"+Global.getAdminPath()+"/language/sysMunlLang/?repage";
		
	}
	@RequiresPermissions("language:sysMunlLang:view")
	@RequestMapping(value = "changeLanguage")
	public void changeLanguage(SysMunlLang sysMunlLang, HttpServletRequest request, HttpServletResponse response, Model model) {
		UserUtils.getSession().setAttribute("lang", sysMunlLang.getLangCode());
	}
	@RequiresPermissions("language:sysMunlLang:view")
	@RequestMapping(value = "changeLanguageOut")
	public void changeLanguageOut(SysMunlLang sysMunlLang, HttpServletRequest request, HttpServletResponse response, Model model) {
		UserUtils.getSubject().logout();
	}
	@RequiresPermissions("language:sysMunlLang:view")
	@RequestMapping(value = "isExist")
	@ResponseBody
	public String isExist( HttpServletRequest request, HttpServletResponse response, Model model) {
		String  key =request.getParameter("key");
		String yy = request.getParameter("yy");
		SysMunlLang lang = new SysMunlLang();
		lang.setLangCode(yy);
		lang.setLangKey(key);
		String mes="";
		Page<SysMunlLang> page = sysMunlLangService.findPage(new Page<SysMunlLang>(request, response), lang); 
		if(page.getList().size()>0){
			mes="0";
		}else{
			mes="-1";
		}
		return mes;
	}
	@RequiresPermissions("language:sysMunlLang:view")
	@RequestMapping(value = "getValueByLanguageAndKey")
	public @ResponseBody String getValueByLanguageAndKey(SysMunlLang sysMunlLang,HttpServletRequest request, HttpServletResponse response, Model model) {
		return  Global.getValueByLanguageAndKey(sysMunlLang.getLangCode(), sysMunlLang.getLangKey());
	}
	@RequestMapping(value = "getCurrentPageList")
	public @ResponseBody Map<String,String> getCurrentPageList(SysMunlLang sysMunlLang,HttpServletRequest request, HttpServletResponse response, Model model) {
		return  sysMunlLangService.getCurrentPageList(sysMunlLang.getLanguageAscription());
	}

	@RequestMapping(value = "getCurrentPageListByLan")
	public @ResponseBody Map<String,String> getCurrentPageListByLan(SysMunlLang sysMunlLang,HttpServletRequest request, HttpServletResponse response, Model model) {
		return  sysMunlLangService.getCurrentPageListByLan(sysMunlLang);
	}
	/**
	 * 导出语言数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("language:sysMunlLang:edit")
    @RequestMapping(value = "export")
    public String exportFile(SysMunlLang sysMunlLang, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String language= DictUtils.getCurrentLanguage();
			if(sysMunlLang.getLangCode()==null||"".equals(sysMunlLang.getLangCode())){
				sysMunlLang.setLangCode(language);
			}
			String type=request.getParameter("downType");
			String fileName ="";
            if(type!=null&&"1".equals(type)){
            	sysMunlLang.setLangCode("CN");
            	language="CN";
            	fileName="语言数据-"+DictUtils.getDictLabels(sysMunlLang.getLangCode(), "act_langtype", sysMunlLang.getLangCode(),"")+"模板"+".xlsx";
            }else{
            	fileName = "语言数据-"+DictUtils.getDictLabels("CN", "act_langtype", sysMunlLang.getLangCode(),"")+"-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            }
            Page<SysMunlLang> page = sysMunlLangService.findPage(new Page<SysMunlLang>(request, response,-1), sysMunlLang); 
    		List<SysMunlLang> list=page.getList();
    		List<SysMunlLang> newList=Lists.newArrayList();
    		for (SysMunlLang sysMunlLang2 : list) {
    			sysMunlLang2.setAttributeType(DictUtils.getDictLabels(language, "language_attribute_type", sysMunlLang2.getAttributeType(),""));
    			sysMunlLang2.setOperationType(DictUtils.getDictLabels(language, "language_operation_type", sysMunlLang2.getOperationType(),""));
    			sysMunlLang2.setLangCode(DictUtils.getDictLabels("CN", "act_langtype", sysMunlLang2.getLangCode(),""));
    			newList.add(sysMunlLang2);
    		}
            new ExportExcel("语言数据", SysMunlLang.class).setDataList(newList).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/language/sysMunlLang/list?repage";
    }
	/**
	 * 导入语言数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("language:sysMunlLang:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		String language = request.getParameter("languageType");
		if(language!=null&&!"".equals(language)){
			try {
				int successNum = 0;
				int failureNum = 0;
				StringBuilder failureMsg = new StringBuilder();
				ImportExcel ei = new ImportExcel(file, 1, 0);
				List<SysMunlLang> list = ei.getDataList(SysMunlLang.class);
				if(list.size()>0){
					User user = UserUtils.getUser();
					Date now = new Date();
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String hehe = df.format(now); 
					Date date = df.parse(hehe); 
					String langu="";
					for (SysMunlLang sysMunlLang2 : list) {
						langu=sysMunlLang2.getLangCode();
						sysMunlLang2.setLangCode(DictUtils.getDictValue("CN", "act_langtype", langu,""));
						if(!"".equals(sysMunlLang2.getLangCode())&&language.equals(sysMunlLang2.getLangCode())){
							if(checkDict(sysMunlLang2)){
								sysMunlLang2.setCreateDate(date);
								sysMunlLang2.setCreateName(user.getName());
				    			sysMunlLang2.setAttributeType(DictUtils.getDictValue(language, "language_attribute_type", sysMunlLang2.getAttributeType(),""));
				    			sysMunlLang2.setOperationType(DictUtils.getDictValue(language, "language_operation_type", sysMunlLang2.getOperationType(),""));
				    			@SuppressWarnings("unchecked")
								Map<String,String> object = (Map<String, String>) CacheUtils.get(sysMunlLang2.getLangCode()+"_"+sysMunlLang2.getLanguageAscription());
								if(object!=null){
									object.put(sysMunlLang2.getLangKey(), sysMunlLang2.getLangContext());
								}
								sysMunlLangService.save(sysMunlLang2);
								successNum++;
							}else{
								failureMsg.append("<br/>语言key："+sysMunlLang2.getLangKey()+"在语言："+langu+"中已存在; ");
								failureNum++;
							}
						}else{
							failureMsg.append("<br/>语言："+langu+"在当前语言中不存在; ");
							failureNum++;
						}
					}
				}
				if (failureNum>0){
					failureMsg.insert(0, "，失败 "+failureNum+" 条语言数据，导入信息如下：");
				}
				addMessage(redirectAttributes, "已成功导入 "+successNum+" 条字典数据"+failureMsg);
			} catch (Exception e) {
				addMessage(redirectAttributes, "导入语言数据失败！失败信息："+e.getMessage());
			}
		}else{
			addMessage(redirectAttributes, "请选择语言后在导入");
		}
		return "redirect:" + adminPath + "/language/sysMunlLang/list?repage";
    }
	private boolean  checkDict(SysMunlLang sysMunlLang){
		SysMunlLang s=new SysMunlLang();
		s.setLangCode(sysMunlLang.getLangCode());
		s.setLangKey(sysMunlLang.getLangKey());
		List<SysMunlLang> list=sysMunlLangService.findList(s);
		if(list.size()>0){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 添加语言
	 * @param dict
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addLanguage")
	public String addLanguage(SysMunlLang sysMunlLang, Model model) {
		SysMunlLang language =new SysMunlLang();
		language.setLangKey(sysMunlLang.getLangKey());
		language.setOperationType(sysMunlLang.getOperationType());
		language.setAttributeType(sysMunlLang.getAttributeType());
		language.setLanguageAscription(sysMunlLang.getLanguageAscription());
		model.addAttribute("sysMunlLang", language);
		model.addAttribute("flag",true);
		return "modules/language/sysMunlLangForm";
	}

}