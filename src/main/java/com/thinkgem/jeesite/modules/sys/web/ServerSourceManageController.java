/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.ServerSourceManage;
import com.thinkgem.jeesite.modules.sys.service.ServerSourceManageService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import java.util.*;

/**
 * 服务数据资源的对应管理Controller
 * @author hejia
 * @version 2018-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/serverSourceManage")
public class ServerSourceManageController extends BaseController {

	@Autowired
	private ServerSourceManageService serverSourceManageService;
	
	@ModelAttribute
	public ServerSourceManage get(@RequestParam(required=false) String id) {
		ServerSourceManage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = serverSourceManageService.get(id);
		}
		if (entity == null){
			entity = new ServerSourceManage();
		}
		return entity;
	}
	
	/*@RequiresPermissions("sys:serverSourceManage:view")
	@RequestMapping(value = {"list", ""})
	public String list(ServerSourceManage serverSourceManage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ServerSourceManage> page = serverSourceManageService.findPage(new Page<ServerSourceManage>(request, response), serverSourceManage); 
		model.addAttribute("page", page);
		return "modules/sys/serverSourceManageList";
	}*/

	@RequiresPermissions("sys:serverSourceManage:view")
	@RequestMapping(value = {"list", ""})
	public String list(ServerSourceManage serverSourceManage, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ServerSourceManage> list = Lists.newArrayList();
		List<ServerSourceManage> sourcelist = serverSourceManageService.findList(serverSourceManage);
		ServerSourceManage.sortList(list, sourcelist, ServerSourceManage.getRootId(), true);
        model.addAttribute("list", list);
		return "modules/sys/serverSourceManageTreeList";
	}

	@RequiresPermissions("sys:serverSourceManage:view")
	@RequestMapping(value = "form")
	public String form(ServerSourceManage serverSourceManage, Model model) {
		if (serverSourceManage.getPid()==null){
			serverSourceManage.setPid(ServerSourceManage.getRootId());
		}
		ServerSourceManage pserver = serverSourceManageService.get(serverSourceManage.getPid());
		serverSourceManage.setPname(pserver==null?"":pserver.getName());
		model.addAttribute("serverSourceManage", serverSourceManage);
		model.addAttribute("allRoles", UserUtils.getRoleList());
		return "modules/sys/serverSourceManageForm";
	}

	@RequiresPermissions("sys:serverSourceManage:edit")
	@RequestMapping(value = "save")
	public String save(ServerSourceManage serverSourceManage, Model model, RedirectAttributes redirectAttributes) {
	    if(StringUtils.isEmpty(serverSourceManage.getPid())){
	        serverSourceManage.setLeve(1);
        }else {
	        serverSourceManage.setLeve(serverSourceManageService.get(serverSourceManage.getPid()).getLeve() + 1);
        }
		if (!beanValidator(model, serverSourceManage)){
			return form(serverSourceManage, model);
		}
		serverSourceManageService.save(serverSourceManage);
		addMessage(redirectAttributes, "保存服务管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/serverSourceManage/?repage";
	}
	
	@RequiresPermissions("sys:serverSourceManage:edit")
	@RequestMapping(value = "delete")
	public String delete(ServerSourceManage serverSourceManage, RedirectAttributes redirectAttributes) {
		serverSourceManageService.delete(serverSourceManage);
		addMessage(redirectAttributes, "删除服务管理成功");
		return "redirect:"+Global.getAdminPath()+"/sys/serverSourceManage/?repage";
	}
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<ServerSourceManage> list = serverSourceManageService.findList(new ServerSourceManage());
//		ServerSourceManage e = null;
        Map<String, Object> map = null;
        Queue<String> pid = new LinkedList<>();
        pid.offer(extId);
        ServerSourceManage serverSourceManage;
        Iterator<ServerSourceManage> it = null;
        while (pid.size() > 0){
            extId = pid.poll();
            for (it = list.iterator();it.hasNext();){
                serverSourceManage = it.next();
                map = Maps.newHashMap();
                map.put("id", serverSourceManage.getId());
                map.put("pId", serverSourceManage.getPid());
                map.put("name", serverSourceManage.getName());
                mapList.add(map);
                pid.offer(serverSourceManage.getId());
                it.remove();
                /*if((StringUtils.isBlank(extId) && StringUtils.isBlank(serverSourceManage.getPid()))
                 || (StringUtils.isNotBlank(extId) && extId.equals(serverSourceManage.getPid()))){
                    map = Maps.newHashMap();
                    map.put("id", serverSourceManage.getId());
                    map.put("pId", serverSourceManage.getPid());
                    map.put("name", serverSourceManage.getName());
                    mapList.add(map);
                    pid.offer(serverSourceManage.getId());
                    it.remove();
                }*/
            }
        }
		return mapList;
	}
}