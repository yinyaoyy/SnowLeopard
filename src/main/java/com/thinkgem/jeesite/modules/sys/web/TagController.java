/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;


import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ActivitiException;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 标签Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/tag")
public class TagController extends BaseController {
	
	/**
	 * 树结构选择标签（treeselect.tag）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getParameter("url")); 	// 树结构数据URL
		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		model.addAttribute("isAll", request.getParameter("isAll")); 	// 是否读取全部数据，不进行权限过滤
		model.addAttribute("module", request.getParameter("module"));	// 过滤栏目模型（仅针对CMS的Category树）
		return "modules/sys/tagTreeselect";
	}
	/**
	 * 自定义标签（treeselectt.tag）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeselectt")
	public String treeselectt(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getParameter("url")+"&isUser="+request.getParameter("isUser")); 	// 树结构数据URL
		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		model.addAttribute("isAll", request.getParameter("isAll")); 	// 是否读取全部数据，不进行权限过滤
		model.addAttribute("module", request.getParameter("module"));	// 过滤栏目模型（仅针对CMS的Category树）
		return "modules/sys/tagTreeselectt";
	}
	
	/**
	 * 图标选择标签（iconselect.tag）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "iconselect")
	public String iconselect(HttpServletRequest request, Model model) {
		model.addAttribute("value", request.getParameter("value"));
		return "modules/sys/tagIconselect";
	}
	/**
	 * 图标选择标签（faselect.tag）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "faselect")
	@ResponseBody
	public String faselect(HttpServletRequest request, Model model) {
		model.addAttribute("value", request.getParameter("value"));
		String path = request.getParameter("Path");
		InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream(path);
		try {
			return IOUtils.toString(stencilsetStream, "utf-8");
		} catch (Exception e) {
			throw new ActivitiException("json解析出现错误");
		}
	}
}
