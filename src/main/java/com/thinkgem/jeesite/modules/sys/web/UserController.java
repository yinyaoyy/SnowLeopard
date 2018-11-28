/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.language.service.SysMunlLangService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.ReturnMassage;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.ServerSourceManage;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserExpand;
import com.thinkgem.jeesite.modules.sys.entity.UserExport;
import com.thinkgem.jeesite.modules.sys.entity.UserRoleOffice;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.ServerSourceManageService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.service.UserRoleOfficeService;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;


/**
 * 用户Controller
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService  officeService;	
	@Autowired
	private SysMunlLangService sysMunlLangService;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private ServerSourceManageService serverService;
	@Autowired
	private UserRoleOfficeService userRoleOfficeService;

	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			User u =  systemService.getUser(id);
			return u;
		}else{
			return new User();
		}
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"index"})
	public String index(User user, Model model) {
		return "modules/sys/userIndex";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"list", ""})
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        if(user.getOffice()!=null&&user.getOffice().getId()!=null&&!"".equals(user.getOffice().getId())){
        	user.getOffice().setName(officeService.get(user.getOffice().getId()).getName());
        }
		model.addAttribute("page", page);
		return "modules/sys/userList";
	}
	
	@ResponseBody
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"listData"})
	public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
		return page;
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findRoleByRoleId(UserUtils.getUser().getCompany().getId()));
		return "modules/sys/userForm";
	}
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "formxg")
	public String formxg(User user, Model model,HttpServletRequest request, HttpServletResponse response ) {
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("allRoles", systemService.findAllRole());
		return "modules/campus/userForm";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		//user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(Digests.getMD5(user.getNewPassword()+user.getLoginName()));
		}
		if (!beanValidator(model, user)){
			return form(user, model);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		/*List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();//传过来的是角色id
		for (Role r : systemService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);*/
		//user.setUserSourceType("1");
		// 保存用户信息
        	systemService.save(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
		}
		addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(User user, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		if (UserUtils.getUser().getId().equals(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		}else if (User.isAdmin(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
		}else{
			try {
				systemService.deleteUser(user);
				addMessage(redirectAttributes, "删除用户成功");
			} catch (BusinessException e) {
				addMessage(redirectAttributes, e.getMessage());
			} catch (Exception e) {
				addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
			}
			
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value="batchDelete")
	public String batchDelete(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		String batchid = request.getParameter("batchid");
		List<String> deList = new ArrayList<String>();
		String[] strs = batchid.split(",");
		boolean  flag=true;
		for (String str : strs){
			if(UserUtils.getUser().getId().equals(str)){
				addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
				flag=false;
				break;
			}else if(User.isAdmin(str)){
				addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
				flag=false;
				break;
			}
			deList.add(str);
		}
		if(flag){
			try {
				systemService.batchDelete(deList);
				addMessage(redirectAttributes, "删除用户成功");
			} catch (BusinessException e) {
				addMessage(redirectAttributes, e.getMessage());
			} catch (Exception e) {
				addMessage(redirectAttributes, "失败!未知错误请联系管理员处理");
			}
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
    		List<User> list=page.getList();
    		List<UserExport> exList=Lists.newArrayList();
    		UserExpand expand=null;
    		String currentLanguage=DictUtils.getCurrentLanguage();
    		for (User user2 : list) {
    			expand=user2.getUserExpand();
    			if(expand==null){
    				expand=new UserExpand();
    			}
    			UserExport exUser=new UserExport(user2,user2.getUserExpand());
    			exList.add(exUser);
			}
            new ExportExcel("用户数据", UserExport.class).setDataList(exList).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * 导入用户数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserExport> list = ei.getDataList(UserExport.class);
			for (UserExport userExport : list){
				boolean ue = systemService.getUserInfoByIdCard(userExport.getPapernum());
				try{
					if (!ue){
						userExport.setPwd("00000000");
						BeanValidators.validateWithException(validator, userExport);
						if(officeService.get(userExport.getOffice().getId())==null){
							failureMsg.append("<br/>用户名 "+userExport.getName()+"的部门不存在; ");
							failureNum++;
						}else
						if(officeService.get(userExport.getOffice().getId())==null){
							failureMsg.append("<br/>用户名 "+userExport.getName()+"的科室不存在; ");
							failureNum++;
						}else{
							//userExport.setUserSourceType("1");
							systemService.saveUserExport(userExport);
							successNum++;
						}
					}else{
						failureMsg.append("<br/>用户名 "+userExport.getName()+" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>用户名 "+userExport.getName()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>用户名 "+userExport.getName()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }
	
	/**
	 * 下载导入用户数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据导入模板.xlsx";
    		List<UserExport> list = Lists.newArrayList(); 
    		list.add(new UserExport());
    		new ExportExcel("用户数据", UserExport.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 用户信息显示及保存
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "info")
	public String info(User user, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			User id = UserUtils.get(user.getId());
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			currentUser.setAddress(user.getAddress());
			currentUser.setBirthday(user.getBirthday());
			currentUser.setPapernum(user.getPapernum());
			currentUser.setName(user.getName());
			currentUser.setUserType(user.getUserType());
			currentUser.setArea(user.getArea());
			currentUser.setIsShow(user.getIsShow());
			currentUser.getUserExpand().setId(user.getId());
			currentUser.getUserExpand().setMajor(user.getUserExpand().getMajor());
			currentUser.getUserExpand().setOtherLinks(user.getUserExpand().getOtherLinks());
			currentUser.getUserExpand().setQq(user.getUserExpand().getQq());
			currentUser.getUserExpand().setSchoolInfo(user.getUserExpand().getSchoolInfo());
			currentUser.getUserExpand().setWeChat(user.getUserExpand().getWeChat());
			currentUser.getUserExpand().setEducation(user.getUserExpand().getEducation());
			currentUser.getUserExpand().setEthnic(user.getUserExpand().getEthnic());
			if(StringUtils.isNotBlank(user.getUserExpand().getSex())){
				currentUser.getUserExpand().setSex(user.getUserExpand().getSex());
			}else{
				currentUser.getUserExpand().setIdCard(user.getPapernum());
			}
			currentUser.getUserExpand().setNativePlace(user.getUserExpand().getNativePlace());
			systemService.updateUserInfo(currentUser);
			model.addAttribute("message", "修改用户信息成功");
		}
		if(StringUtils.isBlank(currentUser.getUserExpand().getSex())){
			currentUser.getUserExpand().setIdCard(currentUser.getPapernum());
		}
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/userInfo";
	}

	/**
	 * 返回用户信息
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "infoData")
	public User infoData() {
		return UserUtils.getUser();
	}
	
	/**
	 * 修改个人用户密码
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	
	@RequiresPermissions("user")
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		/*if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userModifyPwd";
			}
			oldPassword=Digests.getMD5(oldPassword+user.getLoginName());
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
				model.addAttribute("server_url",Global.getConfig("server_url"));
			
				return model;
			}else{
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		*/
		model.addAttribute("user", user);
		return "modules/sys/userModifyPwd";
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "changePwd")
	public Object changePwd(HttpServletRequest req, HttpServletResponse res) {
		String oldPassword = req.getParameter("oldPassword");
		String newPassword = req.getParameter("newPassword");
		User user = UserUtils.getUser();
		Map<String,Object> map = new HashMap<String,Object>();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if(Global.isDemoMode()){
				map.put("message", "演示模式，不允许操作！");
				return "modules/sys/userModifyPwd";
			}
			oldPassword=Digests.getMD5(oldPassword+user.getLoginName());
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				HttpSession s = req.getSession();
				s.invalidate();
				map.put("message", "修改密码成功");
				map.put("status", "1");
				map.put("server_url",Global.getConfig("server_url"));
			}else{
				map.put("message", "修改密码失败，旧密码错误");
				map.put("ststua", "0");
			}
		}
		return map;
	}
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUserByOfficeId(officeId);
		for (int i=0; i<list.size(); i++){
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+e.getId());
			map.put("pId", officeId);
			map.put("type", "3");
			map.put("name", StringUtils.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
	@ResponseBody
	@RequestMapping(value = {"userByCommanId"})
	public List<User> userByCommanId(User user, HttpServletRequest request, HttpServletResponse response) {
		String commanId=request.getParameter("commanId");
		Office o=new Office();
		o.setId(commanId);
		user.setOffice(o);
		List<User> list = systemService.findUser( user);
		return list;
	}
	@RequestMapping(value = "deleteById")
	public String deleteById(User user, RedirectAttributes redirectAttributes) {
		if (UserUtils.getUser().getId().equals(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		}else if (User.isAdmin(user.getId())){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
		}else{
			systemService.deleteUser(user);
			addMessage(redirectAttributes, "删除用户成功");
		}
		return "redirect:" + adminPath + "/campus/campus/baseUserDgs/list?repage";
		
	}
	@RequestMapping(value = "savee")
	public String save(User user, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/campus/campus/baseUserDgs/list?repage";
		}
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(Digests.getMD5(user.getNewPassword()+user.getLoginName()));
		}
		
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		// 保存用户信息
		systemService.saveUserReturnId(user);	
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
		}
		addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		return "redirect:" + adminPath + "/campus/campus/baseUserDgs/list?repage";
	}
	@RequestMapping(value = "upload")
	public String upload( HttpServletRequest request, HttpServletResponse response, Model model) {
		User user=UserUtils.getUser();
		model.addAttribute("userImgHidden", Global.getConfig("server_url")+user.getPhoto());
		return "modules/sys/userImg";
	}
	@RequestMapping(value = "imgUpload")
	public @ResponseBody String  imgUpload(HttpServletRequest request) {
		String param=request.getParameter("param");
		User user=UserUtils.getUser();
		double params= Math.random();
		String fileName = user.getId() + ".jpeg";
		String realPath ="/userfiles/"+user.getId()+"/_thumbs/images";
		FileUtils.GenerateImage(param, realPath, fileName);
		FileUtils.GenerateImage(param, "/userfiles/"+user.getId()+"/images", fileName);
		user.setPhoto("/userfiles/"+user.getId()+"/_thumbs/images/"+fileName+"?params="+params );
		systemService.updateUserInfo(user);
		return user.getPhoto();
	}
	@RequestMapping(value = "lockscreen")
	public String lockscreen(User user, Model model) {
		UserUtils.getSession().setAttribute("isLock", "true");
		model.addAttribute("language", sysMunlLangService.getCurrentPageList("/lockscreen"));
		return "modules/sys/lockscreen";
	}
	@RequestMapping(value = "unlock")
	public @ResponseBody String unlock(User user, Model model) {
		ReturnMassage rm=null;
	  	User u=UserUtils.getUser();
	  	user.setPassword(Digests.getMD5(user.getPassword()+u.getLoginName()));
	  	if(u==null){
	  		rm=new ReturnMassage("-1","用户登录状态失效");
	  	}else if(!SystemService.validatePassword(user.getPassword(), u.getPassword())){
	  		rm=new ReturnMassage("-2","密码错误！");
	  	}else{
	  		UserUtils.getSession().setAttribute("isLock", "false");
	  		rm=new ReturnMassage("0","成功！");
	  	}
	  	return  JSON.toJSONString(rm);
	}
	@RequestMapping(value = "isFirstLogin")
	public @ResponseBody boolean isFirstLogin(HttpServletRequest request) {
		User user=UserUtils.getUser();
		if(user.getLoginCount()>1){
			return false;
		}
		return true;
	}
	@RequestMapping(value = "update")
	public @ResponseBody int update(HttpServletRequest request,User user) {
		String job = request.getParameter("job");//角色
		String jgId = request.getParameter("jgId");//科室
		String userImg=request.getParameter("userImg");
		Principal principal = UserUtils.getPrincipal();
		String userId = principal.getId();
		Office office=officeService.get(jgId);
		Office company=new Office(jgId);//部门
		user.setOffice(office);
		String []companyArr=office.getParentIds().split(",");
		if(companyArr.length>1){
			company.setId(companyArr[1]);
		}else{
			company.setId(jgId);
		}
		user.setCompany(company);
		Role r=new Role(job);
		List<Role> roleList=new ArrayList<Role>();
		roleList.add(r);
		user.setRoleList(roleList);
		user.setId(userId);
		//修改基本信息
		systemService.userInfo(user);
		//修改头像
		String fileName = user.getId() + ".jpeg";
		String realPath ="/userfiles/"+user.getId()+"/_thumbs/images";
		FileUtils.GenerateImage(userImg, realPath, fileName);
		FileUtils.GenerateImage(userImg, "/userfiles/"+user.getId()+"/images", fileName);
		user.setPhoto("/userfiles/"+user.getId()+"/_thumbs/images/"+fileName+"?params="+Math.random() );
		UserUtils.getUser().setIsPerfect(null);
		systemService.userInfo(user);
		return 1;
	}
	@RequestMapping(value = "getTree")
	public @ResponseBody List<Map<String, Object>> getTree(HttpServletRequest request,User user) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(true);
		for (int i=0; i<list.size(); i++){
			Office o = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", o.getId());
			map.put("pId", o.getParent()!=null?o.getParent().getId():0);
			map.put("pIds", o.getParentIds()!=null?o.getParentIds():"");
			map.put("name", o.getName());
			mapList.add(map);
		}
		return mapList;
	}
	//通讯录
	@RequestMapping(value = "addBook")
	public String addBook(Model model,HttpServletRequest request,HttpServletResponse response) {
		User u=UserUtils.getUser();
		User user = new User();
		user.setIsPerfect("0");
		String companyId = u.getCompany().getId();
		Office company = new Office(companyId);
		user.setCompany(company);
		user.setId(u.getId());
		List<User> userList = systemService.getAllList(user);
		for(User users : userList){
			String realPath =Global.getUserfilesBaseDir()+users.getPhoto();
			if(realPath.indexOf("?")!=-1){
				realPath = realPath.substring(0,realPath.indexOf("?"));
			}
			if(!"".equals(users.getPhoto())){
				if(!isImg(realPath)){
					users.setPhoto("/userfiles/default/userProfile_default.png");
				}
			}else{
				users.setPhoto("/userfiles/default/userProfile_default.png");
			}
		}
		model.addAttribute("userList",userList);
		return "modules/sys/contacts";
	}
	//通讯录
	@RequestMapping(value = "addBooks")
	public @ResponseBody List<User> addBooks(Model model,HttpServletRequest request,HttpServletResponse response) {
		User u=UserUtils.getUser();
		User user = new User();
		user.setIsPerfect("0");
		String companyId = u.getCompany().getId();
		Office company = new Office(companyId);
		user.setCompany(company);
		user.setId(u.getId());
		List<User> userList = systemService.getAllList(user);
		for(User users : userList){
			String userType=users.getUserType();
			if(userType==null||"".equals(userType)){
				users.setUserType("未填写");
			}
			String phone =users.getPhone();
			if(phone==null||"".equals(phone)){
				users.setPhone("未填写");
			}
			String address =users.getAddress();
			if(address==null||"".equals(address)){
				users.setAddress("未填写");
			}
			String email = users.getEmail();
			if(email==null||"".equals(email)){
				users.setEmail("未填写");
			}
			String realPath = Global.getUserfilesBaseDir()+users.getPhoto();
			if(realPath.indexOf("?")!=-1){
				realPath = realPath.substring(0,realPath.indexOf("?"));
			}
			if(!"".equals(users.getPhoto())){
				if(!isImg(realPath)){
					users.setPhoto("/userfiles/default/userProfile_default.png");
				}
			}else{
				users.setPhoto("/userfiles/default/userProfile_default.png");
			}
		}
		return userList;
	}
	//通讯录查询
	@RequestMapping(value = "selectAddBook")
	public @ResponseBody List<User> selectAddBook(HttpServletRequest request,HttpServletResponse response,Model model) {
		User u=UserUtils.getUser();
		User user = new User();
		user.setIsPerfect("0");
		String companyId = u.getCompany().getId();
		Office company = new Office(companyId);
		user.setCompany(company);
		user.setId(u.getId());
		String firstName = request.getParameter("firstName");
		List<User> userList = systemService.getAllList(user);
		for(User users : userList){
			String userType=users.getUserType();
			if(userType==null||"".equals(userType)){
				users.setUserType("未填写");
			}
			String phone =users.getPhone();
			if(phone==null||"".equals(phone)){
				users.setPhone("未填写");
			}
			String address =users.getAddress();
			if(address==null||"".equals(address)){
				users.setAddress("未填写");
			}
			String email = users.getEmail();
			if(email==null||"".equals(email)){
				users.setEmail("未填写");
			}
			String realPath =Global.getUserfilesBaseDir()+users.getPhoto();
			if(realPath.indexOf("?")!=-1){
				realPath = realPath.substring(0,realPath.indexOf("?"));
			}
			if(!"".equals(users.getPhoto())){
				if(!isImg(realPath)){
					users.setPhoto("/userfiles/default/userProfile_default.png");
				}
			}else{
				users.setPhoto("/userfiles/default/userProfile_default.png");
			}
		}
		List<User> newUserList = new ArrayList<User>();
		if(!firstName.equals("全部")){
			for(int i=0;i<userList.size();i++){
				String namePy = Chinese2pinyin.res(userList.get(i).getName());
				if(namePy.length()>0){
					if(namePy.substring(0,1).equalsIgnoreCase(firstName)){
						newUserList.add(userList.get(i));
					}
				}
			}
		}else{
			newUserList = userList;
		}
		return newUserList;
	}
	@RequestMapping(value = "toDrag")
	public String toDarg(HttpServletResponse response, Model model) {
		//获取代办数量
		Long waitCount = actTaskService.todoListCount(new Page<Act>(), new Act());
		//获取已办数量
		Long alreadyCount = actTaskService.historicListCount(new Page<Act>(), new Act());
		//获取全部数量
		//Long allCount = actTaskService.todoListsCount(new Page<Act>(), new Act());
		Long allCount =waitCount+alreadyCount;
		model.addAttribute("waitCount", waitCount);
		model.addAttribute("alreadyCount", alreadyCount);
		model.addAttribute("allCount", allCount);
		
		//获取对应服务资源
		ServerSourceManage ssm = new ServerSourceManage();
		ssm.setCloudShow("1");//云平台首页展示
		ssm.setRoleList(UserUtils.getUser().getRoleList());//当前用户角色
		//云平台首页展示
		List<ServerSourceManage> serverList = serverService.findList(ssm);
		model.addAttribute("serverList", serverList);
		return "modules/sys/drag";
	}
	@RequestMapping(value = "userToPartTime")
	public String userToPartTime(User user, Model model) {
		if(user!=null&&StringUtils.isNoneBlank(user.getId())){
			UserRoleOffice  userRoleOffice=new UserRoleOffice();
			userRoleOffice.setUser(user);
			model.addAttribute("roleList", userRoleOfficeService.findList(userRoleOffice));//拥有的兼职以其主职
		}else {
			
		}
		model.addAttribute("allRoles", systemService.findRoleByRoleId(UserUtils.getUser().getCompany().getId()));//所有角色
		return "modules/sys/selectUserOfficeRole";
	}
	public boolean isImg(String imgUrl){
		File file=new File(imgUrl);    
		if(!file.exists()){
			return false;
		}else{
			return true;
		}
	}
}
