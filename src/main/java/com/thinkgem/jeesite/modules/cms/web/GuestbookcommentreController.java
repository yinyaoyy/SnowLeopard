package com.thinkgem.jeesite.modules.cms.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookComment;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentReService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;


@Controller
@RequestMapping(value = "${adminPath}/cms/guestbookcommentre")
public class GuestbookcommentreController extends BaseController{
	@Autowired
	private GuestbookCommentReService guestbookCommentReService;
	@Autowired
	private GuestbookCommentService guestbookCommentService;

		
			
			
	    @RequestMapping(value ="save")
	    @ResponseBody
		public Map<String, Object> savere(@RequestParam(required=false) String id,String conttent, RedirectAttributes redirectAttributes) {

			GuestbookCommentRe guestbookCommentRe=new GuestbookCommentRe();
			Map<String, Object> map = new HashMap<>();
			GuestbookComment guestbookComment=guestbookCommentService.get(id);
			//System.out.println(guestbookComment);
			guestbookCommentRe.setCommentId(id);
			guestbookCommentRe.setContent(conttent);
			guestbookCommentRe.setGuestbookId(guestbookComment.getGuestbookId());
			guestbookCommentRe.setCommentType("1");
			try {
				guestbookCommentReService.save(guestbookCommentRe);
			} catch (Exception e) {
				map.put("data", "erro");
			}
			  map.put("data", "success");
			return map;
	    }
	
	    
	     //修改回复
	@RequestMapping(value = "updateContent")
	@ResponseBody
	public Map<String, Object> updateContent(@RequestParam(required = false) String id, String conttent,
			RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<>();
		GuestbookComment guestbookComment = guestbookCommentService.get(id);

		map.put("id", guestbookComment.getGuestbookId());
		User user = UserUtils.getUser();
		User user1 = guestbookComment.getCreateBy();
		if (user.getId().equals(user1.getId())) {
			guestbookComment.setContent(conttent);
			guestbookCommentService.updateContent(guestbookComment);
			map.put("data", "success");
		} else {
			map.put("data", "error");
			addMessage(redirectAttributes, "该回复您只有查看权限，不能修改");
		}
		return map;
	}

		//删除流言对应的回复
		@RequestMapping(value = "delete")
		public String delete(@RequestParam(required=false) Boolean isRe,String id,String type, RedirectAttributes redirectAttributes) {
		GuestbookComment guestbookComment = guestbookCommentService.get(id);
		String guestbookid = guestbookComment.getGuestbookId();
		User user=UserUtils.getUser();
		User user1=guestbookComment.getCreateBy();
		//判断是不是同一个用户不是同一个用户不允许删除
		if(user.getId().equals(user1.getId())) {
		String hh = guestbookCommentService.delete(id);
		if ("".equals(hh)) {
			addMessage(redirectAttributes, "删除回复成功");
		} else {
			addMessage(redirectAttributes, "该回复已有追问不允许删除");
		}
		
		}else {
			addMessage(redirectAttributes, "该回复您不允许删除,如需要请联系回复人本人删除");
		}
		//根据不同类型的回复返回到不同的页面，其实根本不用返回直接将jsp页面的值擦除就行不愿意改
		if(("complaint").equals(type)) {
		        return "redirect:" + adminPath + "/cms/complaint/form?id=" + guestbookid;
		}else {
			return "redirect:" + adminPath + "/cms/guestbook/form?id=" + guestbookid;
		}
	}
		
		//修改追答内容
		@RequestMapping(value = "updateguestbookcommentre")
		@ResponseBody
		public  Map<String, Object> updateguestbookcommentre(@RequestParam(required=false) String id,String conttent, RedirectAttributes redirectAttributes) {
			Map<String, Object> map = new HashMap<>();
			GuestbookCommentRe guestbookCommentRe=guestbookCommentReService.get(id);
			map.put("id", guestbookCommentRe.getGuestbookId());
			guestbookCommentRe.setContent(conttent);
			try {
				guestbookCommentReService.save(guestbookCommentRe);
			} catch (Exception e) {
				map.put("data", "erro");
			}
			  map.put("data", "success");
			return map;
		}
	

		//删除流言对应的追答
		@RequestMapping(value = "deleteguestbookcommentre")
		public String deleteGuestbooker(@RequestParam(required=false) Boolean isRe,String id,String type, RedirectAttributes redirectAttributes) {
		GuestbookCommentRe guestbookCommentRe = guestbookCommentReService.get(id);
		String guestbookid = guestbookCommentRe.getGuestbookId();
		User user=UserUtils.getUser();
		User user1=guestbookCommentRe.getCreateBy();
		//判断是不是同一个用户不是同一个用户不允许删除
		if(user.getId().equals(user1.getId())) {
	       guestbookCommentReService.deletecommentre(id);
			addMessage(redirectAttributes, "删除追答成功");
		} else {
			addMessage(redirectAttributes, "该追答不允许您删除,如需要请联系追答人本人删除");
		}
		//根据不同类型的回复返回到不同的页面，其实根本不用返回直接将jsp页面的值擦除就行不愿意改
		if("complaint".equals(type)) {
		        return "redirect:" + adminPath + "/cms/complaint/form?id=" + guestbookid;
		}else {
			return "redirect:" + adminPath + "/cms/guestbook/form?id=" + guestbookid;
		}
	}
}
