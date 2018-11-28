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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApplyCount;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationApplyService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 人民调解业务逻辑Controller
 * @author zhangqiang
 * @version 2018-05-24
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/act/oaPeopleMediationApply")
public class OaPeopleMediationApplyController extends BaseController {

    //锡林郭勒盟  sys_area 中对应的id(为了只获取锡盟下属的地区)
    private static final String AREA_PARENT_ID = "5";
    
	@Autowired
	private OaPeopleMediationApplyService oaPeopleMediationApplyService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private SystemService systemService;
	
	public OaPeopleMediationApply get(@RequestParam(required=false) String id) {
		OaPeopleMediationApply entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaPeopleMediationApplyService.get(id);
		}
		if (entity == null){
			entity = new OaPeopleMediationApply();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaPeopleMediationApply oaPeopleMediationApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaPeopleMediationApply> page = oaPeopleMediationApplyService.findPage(new Page<OaPeopleMediationApply>(request, response), oaPeopleMediationApply); 
		model.addAttribute("page", page);
		return "modules/oa/act/oaPeopleMediationApplyList";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationApply:view")
	@RequestMapping(value = "form")
	public String form(OaPeopleMediationApply oaPeopleMediationApply, Model model,HttpServletRequest request) {
		
		Act act = oaPeopleMediationApply.getAct();
		oaPeopleMediationApply = get(request.getParameter("id"));
		oaPeopleMediationApply.setAct(act);
		model.addAttribute("oaPeopleMediationApply", oaPeopleMediationApply);
		
		//当还没有流程Id的时候，说明是申请状态
		if(oaPeopleMediationApply!=null&&StringUtils.isNotBlank(oaPeopleMediationApply.getProcInsId())){
			//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
			if("view".equals(oaPeopleMediationApply.getAct().getStatus())){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/act/oaPeopleMediationApplyView";
			}else if("mediation_zhiding".equals(act.getTaskDefKey())){
				return "modules/oa/act/oaPeopleMediationApplyAppoint";
			}else{
				User user = UserUtils.getUser();
				model.addAttribute("user", user.getId());
				
				return "modules/oa/act/oaPeopleMediationApplyTodo";
			}
		}
		else {
			return "modules/oa/act/oaPeopleMediationApplyForm";
		}
	}

	@RequiresPermissions("oa:act:oaPeopleMediationApply:edit")
	@RequestMapping(value = "save")
	public String save(OaPeopleMediationApply oaPeopleMediationApply, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationApply)){
			return form(oaPeopleMediationApply, model, request);
		}
		oaPeopleMediationApplyService.save(oaPeopleMediationApply);
		addMessage(redirectAttributes, "保存人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationApply:edit")
	@RequestMapping(value = "submit")
	public String submit(OaPeopleMediationApply oaPeopleMediationApply, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaPeopleMediationApply)){
			return form(oaPeopleMediationApply, model, request);
		}
		oaPeopleMediationApplyService.submit(oaPeopleMediationApply);
		addMessage(redirectAttributes, "保存人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationApply:edit")
	@RequestMapping(value = "toDo")
	public String toDo(OaPeopleMediationApply oaPeopleMediationApply, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		oaPeopleMediationApplyService.toDo(oaPeopleMediationApply);
		if("no".equals(oaPeopleMediationApply.getAct().getFlag())){
			if("mediation_shenhe".equals(oaPeopleMediationApply.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "人民调解申请已驳回");
			}
		}else if("yes".equals(oaPeopleMediationApply.getAct().getFlag())){
			if("mediation_shenhe".equals(oaPeopleMediationApply.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "人民调解申请已受理");
			}else if("mediation_zhiding".equals(oaPeopleMediationApply.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "人民调解员已成功指定");
			}else if("mediation_xiugai".equals(oaPeopleMediationApply.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "人民调解已修改完成");
			}
		}
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
		//审核成功后直接跳转到填写调解受理登记表
//		if (resultFlag) {
//			// return "modules/oa/act/oaPeopleMediationAcceptRegisterForm";
//			addMessage(redirectAttributes, "保存人民调解成功");
//			return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
//		}else{
//			addMessage(redirectAttributes, "保存人民调解成功");
//			return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
//		}

	}
	
	@RequiresPermissions("oa:act:oaPeopleMediationApply:edit")
	@RequestMapping(value = "delete")
	public String delete(OaPeopleMediationApply oaPeopleMediationApply, RedirectAttributes redirectAttributes) {
		oaPeopleMediationApplyService.delete(oaPeopleMediationApply);
		addMessage(redirectAttributes, "删除人民调解成功");
		return "redirect:"+Global.getAdminPath()+"/oa/act/oaPeopleMediationApply/?repage";
	}

	@RequiresPermissions("oa:act:oaPeopleMediationApply:view:countByYearArea")
	@RequestMapping(value="toCountByYearArea")
	public String toCountByYearArea(OaPeopleMediationApplyCount opmac, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID);
		//设置默认年度
		if(StringUtils.isBlank(opmac.getYear())) {
			opmac.setYear(DateUtils.getYear());
		}
		model.addAttribute("areaList", areaList);
		return "modules/chart/peopleMediation/countYearArea";
	}
	@RequiresPermissions("oa:act:oaPeopleMediationApply:view:countByYearArea")
	@RequestMapping(value="countByYearArea")
	@ResponseBody
	public List<OaPeopleMediationApplyCount> countByYearArea(OaPeopleMediationApplyCount opmac) {
		//获取相应数据
		List<OaPeopleMediationApplyCount> list = oaPeopleMediationApplyService.countByYearArea(opmac); 
		return list;
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "comTreeData")
	public List<Map<String, Object>> comTreeData(@RequestParam(required=false) String extId, HttpServletResponse response, HttpServletRequest request) {
		/*List<Map<String, Object>> mapList = Lists.newArrayList();
		PeopleMediationCommittee peopleMediationCommittee = new PeopleMediationCommittee();
		
		peopleMediationCommittee.setArea(new Area(request.getParameter("caseTown")));
		List<PeopleMediationCommittee> list = peopleMediationCommitteeService.findList(peopleMediationCommittee);
		if(list==null || list.size()==0){
			peopleMediationCommittee.setArea(new Area(request.getParameter("caseCounty")));
			list = peopleMediationCommitteeService.findList(peopleMediationCommittee);
		}
		for (int i=0; i<list.size(); i++){
			PeopleMediationCommittee e = list.get(i);
			
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("name", e.getName());
				mapList.add(map);
			
		}
		return mapList;*/
		String countyId = request.getParameter("caseCounty");
		String townId = request.getParameter("caseTown");
		List<Map<String, Object>> resultList = Lists.newArrayList();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		mapList=systemService.getOfficeUserApi("10","0", countyId, townId,"");
		for (int i = 0; i < mapList.size(); i++) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", mapList.get(i).get("id"));
			map.put("name",mapList.get(i).get("name"));
			resultList.add(map);
		}
		
		return resultList;
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "manTreeData")
	public List<Map<String, Object>> manTreeData(@RequestParam(required=false) String extId, HttpServletResponse response, HttpServletRequest request) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		Office office=	systemService.getSysUserOfficeInfo(request.getParameter("peopleMediationCommittee"),"1");
		List<User> list = systemService.findUserByOfficeId(request.getParameter("peopleMediationCommittee"));
		for (int i=0; i<list.size(); i++){
				User e = list.get(i);
			
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("name", e.getName());
				mapList.add(map);
			
		}
		return mapList;
	}
}