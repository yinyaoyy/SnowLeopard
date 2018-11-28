/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web.act;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.act.OaFastLegal;
import com.thinkgem.jeesite.modules.oa.service.act.OaFastLegalService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 法律服务直通车Controller
 * @author zq
 * @version 2018-07-07
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/act/oaFastLegal")
public class OaFastLegalController extends BaseController {

	@Autowired
	private OaFastLegalService oaFastLegalService;
	@Autowired
	private SystemService systemService;
	@ModelAttribute
	public OaFastLegal get(@RequestParam(required=false) String id) {
		OaFastLegal entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaFastLegalService.get(id);
		}
		if (entity == null){
			entity = new OaFastLegal();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:act:oaFastLegal:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaFastLegal oaFastLegal, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaFastLegal> page = oaFastLegalService.findPage(new Page<OaFastLegal>(request, response), oaFastLegal); 
		model.addAttribute("page", page);
		return "modules/oa/act/oaFastLegalList";
	}

	@RequiresPermissions("oa:act:oaFastLegal:view")
	@RequestMapping(value = "form")
	public String form(OaFastLegal oaFastLegal, Model model,HttpServletRequest request) {
		model.addAttribute("oaFastLegal", oaFastLegal);
		String status = request.getParameter("status");
		if(oaFastLegal!=null&&StringUtils.isNotBlank(oaFastLegal.getId())){
			//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
			if("view".equals(oaFastLegal.getAct().getStatus()) || "view".equals(status)){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/act/oaFastLegalView";
			}else if(!"fast_shouli".equals(oaFastLegal.getAct().getTaskDefKey())){
				return "modules/oa/act/oaFastLegalTodo";
			}else{
				return "modules/oa/act/oaFastLegalForm";
			}
		}
		else {
			return "modules/oa/act/oaFastLegalForm";
		}
	}

	@RequiresPermissions("oa:act:oaFastLegal:edit")
	@RequestMapping(value = "save")
	public String save(OaFastLegal oaFastLegal, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, oaFastLegal)){
			return form(oaFastLegal, model,request);
		}
		oaFastLegalService.save(oaFastLegal);
		addMessage(redirectAttributes, "保存法律服务直通车成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:act:oaFastLegal:edit")
	@RequestMapping(value = "submit")
	public String submit(OaFastLegal oaFastLegal, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, oaFastLegal)){
			return form(oaFastLegal, model,request);
		}
		oaFastLegalService.submit(oaFastLegal);
		addMessage(redirectAttributes, "提交法律服务直通车成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:act:oaFastLegal:edit")
	@RequestMapping(value = "toDo")
	public String toDo(OaFastLegal oaFastLegal, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaFastLegal)){
			return form(oaFastLegal, model,request);
		}
		String errorMsg = oaFastLegalService.toDo(oaFastLegal);
		if(!"".equals(errorMsg)){
			addMessage(redirectAttributes, "提交法律服务直通车失败");
		}else{
			addMessage(redirectAttributes, "提交法律服务直通车成功");
		}
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:act:oaFastLegal:edit")
	@RequestMapping(value = "delete")
	public String delete(OaFastLegal oaFastLegal, RedirectAttributes redirectAttributes) {
		oaFastLegalService.delete(oaFastLegal);
		addMessage(redirectAttributes, "删除法律服务直通车成功");
		return "redirect:"+Global.getAdminPath()+"/oa/act/oaFastLegal/?repage";
	}
	
	@ResponseBody
	@RequestMapping(value = "getCaseType")
	public Map<String, Object> getCaseType(@RequestParam(required = false)String type,@RequestParam(required = false)String parentId) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Dict> list = null;
		try {
			list = DictUtils.getChildrenDictList(type, parentId);
			resultMap.put("result", "success");
			resultMap.put("list", list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultMap.put("result", "error");
		}
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "getOfficeTree")
	public List<Map<String, Object>> officeTreeData(HttpServletRequest request, HttpServletResponse response) {
		String type =request.getParameter("fastCasetype");
		String isUser = request.getParameter("isUser");
		String areaId = request.getParameter("fastAreaId");
		String townId = request.getParameter("fastTownId");
		String id = request.getParameter("fastOfficeId");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		switch (type) {
		case "legal_aid":
			type="5";
			break;
		case "people_mediation":
			type="10";
			break;
		case "apply_lawyer":
			type="2";
			break;
		case "apply_appraise":
			type="4";
			break;
		case "apply_notarization":
			type="3";
			break;
		default:
			break;
		}
		if("0".equals(isUser)){
			id="";
		}
		mapList=systemService.getOfficeUserApi(type,isUser, areaId, townId,id);
		if("1".equals(isUser)){
			mapList = (List<Map<String, Object>>) mapList.get(0).get("users");
		}
		return mapList;
	}
	
}