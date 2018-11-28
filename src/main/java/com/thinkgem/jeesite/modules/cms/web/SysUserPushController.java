/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.web;


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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.entity.SysUserPush;
import com.thinkgem.jeesite.modules.cms.service.SysUserPushService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 用户未在线时的的消息保存Controller
 * @author 尹垚
 * @version 2018-07-12
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysUserPush")
public class SysUserPushController extends BaseController {

	@Autowired
	private SysUserPushService sysUserPushService;
	
	@ModelAttribute
	public SysUserPush get(@RequestParam(required=false) String id) {
		SysUserPush entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysUserPushService.get(id);
		}
		if (entity == null){
			entity = new SysUserPush();
		}
		return entity;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(SysUserPush sysUserPush, HttpServletRequest request, HttpServletResponse response, Model model) {
		String userId = UserUtils.getUser().getId();
		sysUserPush.setReceiveUserId(userId);
		Page<SysUserPush> page = sysUserPushService.findPage(new Page<SysUserPush>(request, response), sysUserPush); 
		model.addAttribute("page", page);
		return "modules/sys/personPushList";
	}
		
	@RequestMapping(value = {"mlist"})
	public String listInfo(SysUserPush sysUserPush, HttpServletRequest request, HttpServletResponse response, Model model) {
		String userId = UserUtils.getUser().getId();
		sysUserPush.setReceiveUserId(userId);
		Page<SysUserPush> page = sysUserPushService.findMessgaePage(new Page<SysUserPush>(request, response), sysUserPush); 
		model.addAttribute("page", page);
		return "modules/sys/personPushList";
	}
	
	@RequestMapping(value = {"mesList"})
	@ResponseBody
	public Page<SysUserPush> messageList(SysUserPush sysUserPush, HttpServletRequest request, HttpServletResponse response, Model model) {
		String userId = UserUtils.getUser().getId();
		sysUserPush.setReceiveUserId(userId);
		String no = request.getParameter("no_read");
		sysUserPush.setIsRead(no);
		Page<SysUserPush> page = sysUserPushService.findMessgaePage(new Page<SysUserPush>(request, response), sysUserPush); 
		return page;
	}
	
	@RequestMapping(value = "form")
	public String form(SysUserPush sysUserPush, Model model) {
		sysUserPushService.changeReadStatusById(sysUserPush.getId());
		model.addAttribute("sysUserPush", sysUserPush);
		return "modules/sys/sysUserPushForm";
	}

	@RequiresPermissions("sys:sysUserPush:edit")
	@RequestMapping(value = "save")
	public String save(SysUserPush sysUserPush, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysUserPush)){
			return form(sysUserPush, model);
		}
		sysUserPushService.save(sysUserPush);
		addMessage(redirectAttributes, "保存消息推送成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysUserPush/?repage";
	}
	
	@RequiresPermissions("sys:sysUserPush:edit")
	@RequestMapping(value = "delete")
	public String delete(SysUserPush sysUserPush, RedirectAttributes redirectAttributes) {
		sysUserPushService.delete(sysUserPush);
		addMessage(redirectAttributes, "删除消息推送成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysUserPush/?repage";
	}
	/*@RequiresPermissions("sys:sysUserPush:view")*/
	@RequestMapping(value = "no_read")
	@ResponseBody
	public int findNoRead(RedirectAttributes redirectAttributes) {
		User u = UserUtils.getUser();
		String userId = u.getId();
		int a  = sysUserPushService.findNoReadList(userId);
		return a;
	}
	@RequestMapping(value = "qbyd")
	@ResponseBody
	public String changeReadToAll(RedirectAttributes redirectAttributes) {
		User u = UserUtils.getUser();
		String userId = u.getId();
		sysUserPushService.changeReadToAll(userId);
		return "success";
	}
	
	@RequestMapping(value = "changeStatus")
	@ResponseBody
	public String changeStatus(RedirectAttributes redirectAttributes,HttpServletRequest req) {
		String id = req.getParameter("id");
		sysUserPushService.changeReadStatusById(id);
		return "success";
	}
}