/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web.act;

import java.util.List;
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
import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.info.entity.LowOffice;
import com.thinkgem.jeesite.modules.info.service.LowOfficeService;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidCount;
import com.thinkgem.jeesite.modules.oa.service.act.OaLegalAidService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 法援申请Controller
 * @author 王鹏
 * @version 2018-05-11
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/act/oaLegalAid")
public class OaLegalAidController extends BaseController {

    //锡林郭勒盟  sys_area 中对应的id(为了只获取锡盟下属的地区)
    private static final String AREA_PARENT_ID = "5";
    
	@Autowired
	private OaLegalAidService oaLegalAidService;
	@Autowired
	private LowOfficeService lawOfficeService;
	@Autowired
	private AreaService areaService;
	
	
	@ModelAttribute
	public OaLegalAid get(@RequestParam(required=false) String id) {
		OaLegalAid entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaLegalAidService.get(id);
		}
		if (entity == null){
			entity = new OaLegalAid();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:act:oaLegalAid:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaLegalAid oaLegalAid, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaLegalAid> page = oaLegalAidService.findPage(new Page<OaLegalAid>(request, response), oaLegalAid); 
		model.addAttribute("page", page);
		return "modules/oa/act/oaLegalAidList";
	}

	@RequiresPermissions("oa:act:oaLegalAid:view")
	@RequestMapping(value = "form")
	public String form(OaLegalAid oaLegalAid, Model model, HttpServletRequest request) {
		oaLegalAid.setLegalAidType("1");//从此方法进入的都是“申请”法律援助
		model.addAttribute("oaLegalAid", oaLegalAid);
		model.addAttribute("officeLawyerOffice", OfficeRoleConstant.OFFICE_LAWYER_OFFICE);
		model.addAttribute("officeLegalService", OfficeRoleConstant.OFFICE_LEGAL_SERVICE);
		String status = request.getParameter("status");
		if(oaLegalAid!=null&&StringUtils.isNotBlank(oaLegalAid.getId())){
			//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
			if("view".equals(oaLegalAid.getAct().getStatus()) || "1".equals(oaLegalAid.getArchiving()) || "view".equals(status)){
				model.addAttribute("zt", request.getParameter("zt"));
				return "modules/oa/act/oaLegalAidView";
			}else if("aid_update".equals(oaLegalAid.getAct().getTaskDefKey()) || "aid_apply_zhiding".equals(oaLegalAid.getAct().getTaskDefKey())) {
				return "modules/oa/act/oaLegalAidForm";
			}else{
				return "modules/oa/act/oaLegalAidTodo";
			}
		}
		else {
			return "modules/oa/act/oaLegalAidForm";
		}
	}
	@RequiresPermissions("oa:act:oaLegalAid:view1")
	@RequestMapping(value = "form1")
	public String form1(OaLegalAid oaLegalAid, Model model,HttpServletRequest request) {
		List<LowOffice> lawOfficeList = lawOfficeService.findList(new LowOffice());
		if(StringUtils.isBlank(oaLegalAid.getId())) {
			User user=UserUtils.getUser();
			oaLegalAid.setJgPerson(user.getName());
			oaLegalAid.setJgphone(user.getMobile());
		     oaLegalAid.setOfficeNamee(user.getOffice().getName());
		     //机构类型  后期加上效验其他机构的不能发起此流程
		     oaLegalAid.setOfficeType(user.getOffice().getType());
		}
		model.addAttribute("lawOfficeList", lawOfficeList);
		oaLegalAid.setLegalAidType("2");//从此方法进入的都是“指定”法律援助
		model.addAttribute("oaLegalAid", oaLegalAid);
		model.addAttribute("officeLawyerOffice", OfficeRoleConstant.OFFICE_LAWYER_OFFICE);
		model.addAttribute("officeLegalService", OfficeRoleConstant.OFFICE_LEGAL_SERVICE);
		if(oaLegalAid!=null&&StringUtils.isNotBlank(oaLegalAid.getId())){
		//如果流程是查看状态，或是已归档状态，那么只能查看，不能修改任何记录
		if("view".equals(oaLegalAid.getAct().getStatus()) || "1".equals(oaLegalAid.getArchiving())){
			model.addAttribute("zt", request.getParameter("zt"));
			return "modules/oa/act/oaLegalAidinformView";
		}else if("defense_update".equals(oaLegalAid.getAct().getTaskDefKey()) || "aid_apply_zhiding".equals(oaLegalAid.getAct().getTaskDefKey())) {
			return "modules/oa/act/oaLegalAidinformForm";
		}else{
			return "modules/oa/act/oaLegalAidinformTodo";
		}
		}else {
	
		return "modules/oa/act/oaLegalAidinformForm";
	}
		}
	

	
	@RequiresPermissions("oa:act:oaLegalAid:edit")
	@RequestMapping(value = "save")
	public String save(OaLegalAid oaLegalAid, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, oaLegalAid)){
			return form(oaLegalAid, model, request);
		}
		oaLegalAidService.save(oaLegalAid);
		addMessage(redirectAttributes, "保存法援申请成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}


	

	@RequiresPermissions("oa:act:oaLegalAid:edit")
	@RequestMapping(value = "toDo")
	public String toDo(OaLegalAid oaLegalAid, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		oaLegalAidService.toDo(oaLegalAid);
		if("no".equals(oaLegalAid.getAct().getFlag())){
			if("adi_approve".equals(oaLegalAid.getAct().getTaskDefKey())||"aid_zhuren".equals(oaLegalAid.getAct().getTaskDefKey())||"aid_chengbanren_shouli".equals(oaLegalAid.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援申请已驳回");
			}else if("aid_chengbanren_banli".equals(oaLegalAid.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "保存法援申请成功");
			}
		}else if("yes".equals(oaLegalAid.getAct().getFlag())){
			if("adi_approve".equals(oaLegalAid.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援申请审批通过");
			}else if("aid_ky_zhiding".equals(oaLegalAid.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援申请指定承办机构成功");
			}else if("aid_zhuren".equals(oaLegalAid.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援申请指定承办人成功");
			}else if("aid_chengbanren_shouli".equals(oaLegalAid.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "保存法援申请成功");
			}else if("aid_chengbanren_banli".equals(oaLegalAid.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援申请已办结");
			}else if("aid_pingjia".equals(oaLegalAid.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援申请已评价");
			}else if("aid_chengbanren_butie".equals(oaLegalAid.getAct().getTaskDefKey())){
				addMessage(redirectAttributes, "法援申请承办人申领补贴成功");
			}
		}
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	

	@RequiresPermissions("oa:act:oaLegalAid:edit")
	@RequestMapping(value = "delete")
	public String delete(OaLegalAid oaLegalAid, RedirectAttributes redirectAttributes) {
		oaLegalAidService.delete(oaLegalAid);
		addMessage(redirectAttributes, "删除法援申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/act/oaLegalAid/?repage";
	}

	@RequiresPermissions("oa:act:oaLegalAid:edit")
	@RequestMapping(value = "delete1")
	public String delete1(OaLegalAid oaLegalAid, RedirectAttributes redirectAttributes) {
		oaLegalAidService.delete(oaLegalAid);
		addMessage(redirectAttributes, "删除法援通知辩护申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/act/oaLegalAid/?repage";
	}
	@RequiresPermissions("oa:act:oaLegalAid:view:countYearArea")
	@RequestMapping(value="toCountYearArea")
	public String toCountYearArea(OaLegalAidCount oac, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID);
		//设置默认年度
		if(StringUtils.isBlank(oac.getYear())) {
			oac.setYear(DateUtils.getYear());
		}
		model.addAttribute("areaList", areaList);
		return "modules/chart/legalAid/countYearArea";
	}
	@RequiresPermissions("oa:act:oaLegalAid:view:countYearArea")
	@RequestMapping(value="countYearArea")
	@ResponseBody
	public List<OaLegalAidCount> countYearArea(OaLegalAidCount oac) {
		//获取相应数据
		List<OaLegalAidCount> list = oaLegalAidService.countYearArea(oac); 
		return list;
	}

	@RequiresPermissions("oa:act:oaLegalAid:view:countCaseType")
	@RequestMapping(value="toCountCaseType")
	public String toCountCaseType(OaLegalAidCount oac, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		model.addAttribute("areaList", areaList);
		//设置默认年度
		model.addAttribute("year", DateUtils.getYear());
		//设置案件类型
		model.addAttribute("caseTypes", JSON.toJSONString(DictUtils.getDictList("case_type")));
		return "modules/chart/legalAid/countCaseType";
	}
	@RequiresPermissions("oa:act:oaLegalAid:view:countCaseType")
	@RequestMapping(value="countCaseType")
	@ResponseBody
	public List<OaLegalAidCount> countCaseType(OaLegalAidCount oac) {
		//获取相应数据
		List<OaLegalAidCount> list = oaLegalAidService.countCaseType(oac); 
		return list;
	}

	@RequiresPermissions("oa:act:oaLegalAid:view:countLegalAidType")
	@RequestMapping(value = {"toCountLegalAidType", ""})
	public String toCountLegalAidType(OaLegalAidCount oac, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		model.addAttribute("areaList", areaList);
		//设置默认年度
		model.addAttribute("year", DateUtils.getYear());
		return "modules/chart/legalAid/countLegalAidType";
	}
	@RequiresPermissions("oa:act:oaLegalAid:view:countCaseType")
	@RequestMapping(value="countLegalAidType")
	@ResponseBody
	public List<OaLegalAidCount> countLegalAidType(OaLegalAidCount oac) {
		//获取相应数据
		List<OaLegalAidCount> list = oaLegalAidService.countLegalAidType(oac); 
		return list;
	}
}