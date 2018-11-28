/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.util.ArrayList;
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
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 字典Controller
 * @author ThinkGem
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@ModelAttribute
	public Dict get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return dictService.get(id);
		}else{
			return new Dict();
		}
	}
	
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = {"list", ""})
	public String list(Dict dict, HttpServletRequest request, HttpServletResponse response, Model model) {
		dict.setParentId("");
		List<String> typeList = dictService.findTypeList();
		model.addAttribute("typeList", typeList);
        Page<Dict> page = dictService.findPage(new Page<Dict>(request, response), dict); 
        model.addAttribute("page", page);
		return "modules/sys/dictList";
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict dict, Model model) {
		model.addAttribute("dict", dict);
		return "modules/sys/dictForm";
	}

	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "save")//@Valid 
	public String save(Dict dict, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/dict/?repage&type="+dict.getType();
		}
		if (!beanValidator(model, dict)){
			return form(dict, model);
		}
		dictService.save(dict);
		addMessage(redirectAttributes, "保存字典'" + dict.getLabel() + "'成功");
		return "redirect:" + adminPath + "/sys/dict/?repage&type="+dict.getType();
	}
	
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	public String delete(Dict dict, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/dict/?repage";
		}
		dictService.delete(dict);
		addMessage(redirectAttributes, "删除字典成功");
		return "redirect:" + adminPath + "/sys/dict/?repage&type="+dict.getType();
	}
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		String batchid = request.getParameter("batchid");
		List<String> deList = new ArrayList<String>();
		String[] strs = batchid.split(",");
		for (String str : strs){
			deList.add(str);
		}
		dictService.batchDelete(deList);
		addMessage(redirectAttributes, "删除字典成功");
		return "redirect:"+Global.getAdminPath()+"/sys/dict/?repage";
		
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String type, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Dict dict = new Dict();
		dict.setType(type);
		List<Dict> list = dictService.findList(dict);
		for (int i=0; i<list.size(); i++){
			Dict e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public List<Dict> listData(@RequestParam(required=false) String type) {
		Dict dict = new Dict();
		dict.setType(type);
		return dictService.findList(dict);
	}
	@RequestMapping(value = "listDataByLanguage")
	public @ResponseBody List<Dict> listDataByLanguage(Dict dict) {
		return DictUtils.getDictListByLanguage(dict.getType(), dict.getLanguageType());
	}
	/**
	 * 导出字典数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:dict:view")
    @RequestMapping(value = "export")
    public String exportFile(Dict dict, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			if(dict==null){
				dict=new Dict();
			}
			if(dict.getLanguageType()==null||"".equals(dict.getLanguageType())){
				dict.setLanguageType((String)request.getSession().getAttribute("lang"));
			}
			 String type=request.getParameter("downType");
			 String fileName = "";
            if(type!=null&&"1".equals(type)){
            	dict.setLanguageType("CN");
            	fileName="字典数据-"+DictUtils.getDictLabels("CN", "act_langtype", dict.getLanguageType(),"")+"模板"+".xlsx";
            }else{
            	fileName = "字典数据-"+DictUtils.getDictLabels("CN", "act_langtype", dict.getLanguageType(),"")+"-"+DateUtils.getDate("yyyyMMddHHmm")+".xlsx";
            }
            Page<Dict> page = dictService.findListByLanguage(new Page<Dict>(request, response,-1), dict); 
    		new ExportExcel("字典数据", Dict.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/dict/list?repage";
    }
	/**
	 * 导入字典数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		String language = request.getParameter("languageTypes");
		if(language!=null&&!"".equals(language)){
			try {
				int successNum = 0;
				int failureNum = 0;
				StringBuilder failureMsg = new StringBuilder();
				ImportExcel ei = new ImportExcel(file, 1, 0);
				List<Dict> list = ei.getDataList(Dict.class);
				if(list.size()>0){
					String  langu="";
					for (Dict dict : list) {
						langu=dict.getLanguageType();
						dict.setLanguageType(DictUtils.getDictValue("CN", "act_langtype",langu,""));
						if(!"".equals(dict.getLanguageType())&&language.equals(dict.getLanguageType())){
							if(checkDict(dict)){
								dictService.save(dict);
								successNum++;
							}else{
								failureMsg.append("<br/>标签："+dict.getLabel()+"在"+langu+"已存在; ");
								failureNum++;
							}
						}else{
							failureMsg.append("<br/>语言:"+langu+"在当前语言中不存在; ");
							failureNum++;
						}
					}
				}
				if (failureNum>0){
					failureMsg.insert(0, "，失败 "+failureNum+" 条字典数据，导入信息如下：");
				}
				addMessage(redirectAttributes, "已成功导入 "+successNum+" 条字典数据"+failureMsg);
			} catch (Exception e) {
				addMessage(redirectAttributes, "导入字典数据失败！失败信息："+e.getMessage());
			}
		}else{
			addMessage(redirectAttributes, "请选择语言后在导入");
		}
		return "redirect:" + adminPath + "/sys/dict/list?repage";
    }
	private boolean  checkDict(Dict dict){
	   List<Dict> list=	dictService.getDictByLanAndTypeAndVal(dict);
	   if(list!=null&&list.size()>0){
		   return false;
	   }
	   return true;
	}
	/**
	 * 添加键值
	 * @param dict
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "addKey")
	public String addKey(Dict dict, Model model) {
		Dict d =new Dict();
		d.setDescription(dict.getDescription());
		d.setType(dict.getType());
		d.setSort(dict.getSort());
		d.setLanguageType(dict.getLanguageType());
		d.setRemarks(dict.getRemarks());
		d.setDelFlag("true");
		model.addAttribute("dict", d);
		return "modules/sys/dictForm";
	}

}
