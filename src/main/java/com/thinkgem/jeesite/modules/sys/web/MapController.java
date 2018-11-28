/**
 * 
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 地图处理类
 * @author 王鹏
 * @version 2018-05-02 13:53:51
 */
@Controller
@RequestMapping(value = "${adminPath}/map")
public class MapController {

	/**
	 * 百度地图选择标签
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "bmap")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAttribute("coordinate", request.getParameter("coordinate")); 	// 坐标
		return "modules/sys/bmap";
	}
}
