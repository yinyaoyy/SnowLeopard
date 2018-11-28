/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 机构Controller
 * @author ThinkGem
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private SystemService systemService;
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return officeService.get(id);
		}else{
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {""})
	public String index(Office office, Model model) {
		return "modules/sys/officeIndex";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {"list"})
	public String list(Office office, Model model) {
        model.addAttribute("list", officeService.findList(office));
		return "modules/sys/officeList";
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		User user = UserUtils.getUser();
		if (office.getParent()==null || office.getParent().getId()==null){
			office.setParent(user.getOffice());
		}
		office.setParent(officeService.get(office.getParent().getId()));
		if (office.getArea()==null){
			office.setArea(user.getOffice().getArea());
		}
		// 自动获取排序号
		if (StringUtils.isBlank(office.getId())&&office.getParent()!=null){
			int size = 0;
			List<Office> list = officeService.findAll();
			for (int i=0; i<list.size(); i++){
				Office e = list.get(i);
				if (e.getParent()!=null && e.getParent().getId()!=null
						&& e.getParent().getId().equals(office.getParent().getId())){
					size++;
				}
			}
			office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size+1 : 1), 3, "0"));
		}
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/office/";
		}
		if (!beanValidator(model, office)){
			return form(office, model);
		}
		officeService.save(office);
		
		if(office.getChildDeptList()!=null){
			Office childOffice = null;
			for(String id : office.getChildDeptList()){
				childOffice = new Office();
				childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
				childOffice.setParent(office);
				childOffice.setArea(office.getArea());
				childOffice.setType("2");
				childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade())+1));
				childOffice.setUseable(Global.YES);
				officeService.save(childOffice);
			}
		}
		
		addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
		String id = "0".equals(office.getParentId()) ? "" : office.getParentId();
		return "redirect:" + adminPath + "/sys/office/list?id="+id+"&parentIds="+office.getParentIds();
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(Office office, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/office/list";
		}
		try {
			officeService.delete(office);
			addMessage(redirectAttributes, "删除机构成功");
		} catch (BusinessException e) {
			addMessage(redirectAttributes, e.getMessage());
		} catch (Exception e) {
			addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
		}
			
//		}
		return "redirect:" + adminPath + "/sys/office/list?id="+office.getParentId()+"&parentIds="+office.getParentIds();
	}

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：部门；2：科室/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeDataAll")
	public List<Map<String, Object>> treeDataAll(@RequestParam(required=false) String userid,@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				if(e.getType()!=null&&"1".equals(e.getType())){
				   map.put("type", "1");	
				}else{
				  map.put("type", "2");
				}
				/*
				 * by 王鹏
				 * 2018年7月10日17:03:54
				 * 改为异步加载(tagTreeselect.jsp)处理
				 * 
				 * if("3".equals(type)){
					//通过 e.getId()获取用户list    list循环所有的user放在  mapList
						List<User> liste = systemService.findUserByOfficeId(e.getId());
					//List<Map<String, Object>> map2 = Lists.newArrayList();
					if(liste.size()>0){
					for (int y = 0; y<liste.size();y++ ){
						if(userid!=null&&!userid.equals(liste.get(y).getId())){
						Map<String, Object> map3 = Maps.newHashMap();
						map3.put("id", liste.get(y).getId()+","+e.getId()+","+e.getName());
						map3.put("name", liste.get(y).getName());
						map3.put("pId",e.getId());
						map3.put("pIds", e.getParentIds()+e.getId());
						map3.put("type","3");
						mapList.add(map3);
					   }
					   }
					}
				}*/
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：部门；2：科室/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			 @RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				if(e.getType()!=null&&"1".equals(e.getType())){
					   map.put("type", "1");	
					}else{
					  map.put("type", "2");
					}
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：部门；2：科室/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeUser")
	public List<Map<String, Object>> treeUser(@RequestParam(required=false) String userid, @RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			 @RequestParam(required=false) String companyId,@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = new ArrayList<Office>();
		if(companyId!=null){
			list = officeService.getOfficeByCompanyId(companyId);
		}else{
			list = officeService.findList(isAll);
		}
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				/*
				 * by 王鹏
				 * 2018年7月10日17:03:54
				 * 改为异步加载(tagTreeselect.jsp)处理
				 * 
				 * if("3".equals(type)){
					List<User> liste = systemService.findUserByOfficeId(e.getId());
					if(liste.size()>0){
						for (int y = 0; y<liste.size();y++ ){
							User u=liste.get(y);
							if(userid!=null&&!userid.equals(liste.get(y).getId())){
								Map<String, Object> map3 = Maps.newHashMap();
								map3.put("id", u.getId());
								map3.put("name", u.getName());
								map3.put("pId",e.getId());
								map3.put("pIds", e.getParentIds()+e.getId());
								map3.put("type","3");
								mapList.add(map3);
							}
						}
					}
				}*/
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	@ResponseBody
	@RequestMapping(value = "getOffice")
	public List<Map<String,String>> getOffice(HttpServletRequest request,HttpServletResponse response, Model model) {
		String companyId = request.getParameter("companyId");
		List<Office> list = officeService.findAll();
		List<Map<String,String>> lis = new ArrayList<Map<String,String>>();
		officeService.get(companyId);
		Office  office=new Office();
		for(int i=0;i<list.size();i++){
			office=list.get(i);
			String[] array=office.getParentIds().split(",");
			for(int j=0;j<array.length;j++){
				if(array[j].equals(companyId)&& Global.YES.equals(office.getUseable())){
					Map<String,String> map = new HashMap<String,String>();
					map.put("name",office.getName());
					map.put("id",office.getId());
					map.put("pId",office.getParentId());
					map.put("pIds",office.getParentIds());
					lis.add(map);
					break;
				}
			}
		}
		return lis;
	}
	@ResponseBody
	@RequestMapping(value = "getRole")
	public Office getRole(HttpServletRequest request,HttpServletResponse response, Model model) {
		String officeId = request.getParameter("officeId");
		Office officeList = officeService.get(officeId);
		return officeList;
	}
	/**
	 * 获取机构人员功能
	 * type   机构人员类型 2.律师事务所3.公证处 4.司法鉴定所8.基层法律服务所5.法援中心10.人民调委会12.司法所
	 * isUser 0或者空不查询人员 1查询人员
	 * areaId  地区旗县或者乡镇
	 * @author 王兆林
	 * @version 2018-05-24 21:15:37
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getOfficeUser")
	public List<Map<String, Object>>  getOfficeUser(HttpServletRequest request,HttpServletResponse response, Model model) {
		String type = request.getParameter("type");
		String isUser = request.getParameter("isUser");
		String areaId = request.getParameter("areaId");
		String officeId = request.getParameter("officeId");
		String townId = request.getParameter("townId");
		String flag = request.getParameter("flag");
		String atype = request.getParameter("atype");
		if(StringUtils.isNotBlank(atype)) {
			type = atype;
		}
		List<Map<String, Object>>  lis = null;
		if("".equals(flag)){
			lis =systemService.getOfficeUser(type, isUser, areaId,townId,officeId);
		}else{
			lis =systemService.getOfficeUser(type, isUser, areaId,townId,officeId,flag);
		}
		return lis;
	}
}
