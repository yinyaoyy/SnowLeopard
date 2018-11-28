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

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Guestbook;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookComment;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 留言Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/guestbook")
public class GuestbookController extends BaseController {

	@Autowired
	private GuestbookService guestbookService;
	
	@ModelAttribute
	public Guestbook get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return guestbookService.get(id);
		}else{
			return new Guestbook();
		}
	}
	
	@RequiresPermissions("cms:guestbook:view")
	@RequestMapping(value = {"list", ""})
	public String list(Guestbook guestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Guestbook> page = guestbookService.findPage(new Page<Guestbook>(request, response), guestbook); 
        model.addAttribute("page", page);
		return "modules/cms/guestbookList";
	}

	@RequiresPermissions("cms:guestbook:view")
	@RequestMapping(value = "form")
	public String form(Guestbook guestbook, Model model) {
		guestbook=guestbookService.get(guestbook.getId());
		model.addAttribute("guestbook", guestbook);
		return "modules/cms/guestbookForm";
	}

	@RequiresPermissions("cms:guestbook:edit")
	@RequestMapping(value = "save")
	public String save(Guestbook guestbook, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, guestbook)){
			return form(guestbook, model);
		}
		if(StringUtils.isNotBlank(guestbook.getReContent())){
			guestbook.setReContent("您好！欢迎关注12348锡林郭勒法网。根据您的描述，我们给予建议如下："+guestbook.getReContent());
		}
		guestbookService.save(guestbook);
		addMessage(redirectAttributes, DictUtils.getDictLabel(guestbook.getDelFlag(), "cms_del_flag", "保存")
				+"回复'" + guestbook.getName() + "'成功");
		guestbook=guestbookService.get(guestbook.getId());
		model.addAttribute("guestbook", guestbook);
		
		return "modules/cms/guestbookForm";
	}
	
	@RequiresPermissions("cms:guestbook:edit")
	@RequestMapping(value = "delete")
	public String delete(Guestbook guestbook, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		if(isRe!=null&&isRe){
			guestbook.setDelFlag("0");
		}else{
			guestbook.setDelFlag("1");
		}
		guestbookService.delete(guestbook, isRe);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"恢复审核":"删除")+"留言成功");
		return "redirect:" + adminPath + "/cms/guestbook/?repage&status=2";
	}

	//删除流言对应的回复
	@RequiresPermissions("cms:guestbook:edit")
	@RequestMapping(value = "deleteGuestbookComment")
	public String delete(@RequestParam(required=false) Boolean isRe,String id, RedirectAttributes redirectAttributes) {
		GuestbookCommentService guestbookCommentService	=new GuestbookCommentService();
		GuestbookComment guestbookComment=guestbookCommentService.get(id);
		if(isRe!=null&&isRe){
			guestbookComment.setDelFlag("0");
		}else{
			guestbookComment.setDelFlag("1");
		}
		GuestbookCommentService guestbookCommentServic	=new GuestbookCommentService();
		guestbookCommentServic.delete(guestbookComment);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"恢复审核":"删除")+"回复成功");
		return "redirect:" + adminPath + "/cms/guestbook/?repage&status=2";
	}
	
	
}
