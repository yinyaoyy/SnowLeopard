/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.NodeManager;
import com.thinkgem.jeesite.modules.sys.service.NodeManagerService;

/**
 * 流程节点Controller
 * @author wanglin
 * @version 2018-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/nodeManager")
public class NodeManagerController extends BaseController {

	@Autowired
	private NodeManagerService nodeManagerService;
	
	@ModelAttribute
	public NodeManager get(@RequestParam(required=false) String id) {
		NodeManager entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = nodeManagerService.get(id);
		}
		if (entity == null){
			entity = new NodeManager();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:nodeManager:view")
	@RequestMapping(value = {"list", ""})
	public String list(NodeManager nodeManager, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NodeManager> page = nodeManagerService.findPage(new Page<NodeManager>(request, response), nodeManager); 
		model.addAttribute("page", page);
		return "modules/sys/nodeManagerList";
	}

	@RequiresPermissions("sys:nodeManager:view")
	@RequestMapping(value = "form")
	public String form(NodeManager nodeManager, Model model) {
		model.addAttribute("nodeManager", nodeManager);
		return "modules/sys/nodeManagerForm";
	}

	@RequiresPermissions("sys:nodeManager:edit")
	@RequestMapping(value = "save")
	public String save(NodeManager nodeManager, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nodeManager)){
			return form(nodeManager, model);
		}
		nodeManagerService.save(nodeManager);
		addMessage(redirectAttributes, "保存流程节点成功");
		return "redirect:"+Global.getAdminPath()+"/sys/nodeManager/?repage";
	}
	
	@RequiresPermissions("sys:nodeManager:edit")
	@RequestMapping(value = "delete")
	public String delete(NodeManager nodeManager, RedirectAttributes redirectAttributes) {
		nodeManagerService.delete(nodeManager);
		addMessage(redirectAttributes, "删除流程节点成功");
		return "redirect:"+Global.getAdminPath()+"/sys/nodeManager/?repage";
	}

}