/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import com.thinkgem.jeesite.modules.language.service.SysMunlLangService;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.ReturnMassage;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.FormAuthenticationFilter;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private SessionDAO sessionDAO;
	@Autowired
	private SystemService systemService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private SysMunlLangService sysMunlLangService;
	@Autowired
    OfficeService officeService;

	public static final String SESSION_IP = "session_ip";
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, Model model) {
		String tag=request.getHeader("tag");
		if(tag!=null&&("100".equals(tag)||"200".equals(tag)||"300".equals(tag))){
			response.setContentType("application/json; charset=utf-8");
			ModelAndView mv = new ModelAndView();
			/*  使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常   */
			FastJsonJsonView view = new FastJsonJsonView();
			ResponseCode responseCode = ResponseCode.NEED_LOGIN;
			Map<String,Object> attrbutes = new HashMap<>();
			attrbutes.put("status",responseCode.getCode());
			attrbutes.put("msg",responseCode.getDesc());
			view.setAttributesMap(attrbutes);
			mv.setView(view);
			return mv;
		}
		Principal principal = UserUtils.getPrincipal();
		if (logger.isDebugEnabled()){
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			CookieUtils.setCookie(response, "LOGINED", "false");
		}
		// 如果已经登录，则跳转到管理首页
		if(principal != null && !principal.isMobileLogin()){
			return new ModelAndView("redirect:" + "newIndex");
		}
		if(principal != null && principal.isMobileLogin()){
			response.setContentType("application/json; charset=utf-8");
			ModelAndView mv = new ModelAndView();
			/*  使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常   */
			FastJsonJsonView view = new FastJsonJsonView();
			ResponseCode responseCode = ResponseCode.SUCCESS;
			Map<String,Object> attrbutes = new HashMap<>();
			attrbutes.put("status",responseCode.getCode());
			attrbutes.put("msg",responseCode.getDesc());
			view.setAttributesMap(attrbutes);
			mv.setView(view);
			return mv;

		}
		model.addAttribute("random", Math.random());
		return new ModelAndView("modules/sys/sysLogin");
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "/${adminPath}/login", method = RequestMethod.POST)
	public @ResponseBody String  loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		ReturnMassage rm=new ReturnMassage();
		// 如果已经登录，则跳转到管理首页
		if(principal != null){
		    response.setHeader(Constants.HEADER_SESSION_ID,principal.getSessionid());
			rm.setCode("0");
			rm.setMassage("登录成功");
			return JSON.toJSONString(rm);
		}
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
		
		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
			message = "-1";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
		
		if (logger.isDebugEnabled()){
			logger.debug("login fail, active session size: {}, message: {}, exception: {}", 
					sessionDAO.getActiveSessions(false).size(), message, exception);
		}
		
		// 非授权异常，登录失败，验证码加1。
		/*if (!UnauthorizedException.class.getName().equals(exception)){
			rm.setValidateCodeLogin(isValidateCodeLogin(username, true, false));
		}*/
		rm.setCode("-1");
		rm.setMassage(message);
		request.getSession().removeAttribute("lang");
		return JSON.toJSONString(rm);
	}
	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "newIndex")
	public String newIndex(HttpServletRequest request, HttpServletResponse response,Model model) {
		String isLock= (String) UserUtils.getSession().getAttribute("isLock");
		if(isLock!=null&&"true".equals(isLock)){
			return "modules/sys/lockscreen";
		}else{
			Principal principal = UserUtils.getPrincipal();
			UserUtils.getSession().setAttribute(SESSION_IP, StringUtils.getRemoteAddr((HttpServletRequest)request));
			// 登录成功后，验证码计算器清零
			//isValidateCodeLogin(principal.getLoginName(), false, true);
			
			if (logger.isDebugEnabled()){
				logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
			}
			// 如果已登录，再次访问主页，则退出原账号。
			if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
				String logined = CookieUtils.getCookie(request, "LOGINED");
				if (StringUtils.isBlank(logined) || "false".equals(logined)){
					CookieUtils.setCookie(response, "LOGINED", "true");
				}else if (StringUtils.equals(logined, "true")){
					UserUtils.getSubject().logout();
					return "redirect:" + adminPath + "/login";
				}
			}
			// 如果是手机登录，则返回JSON字符串
			if (principal.isMobileLogin()){
				if (request.getParameter("login") != null){
					return renderString(response, principal);
				}
				if (request.getParameter("index") != null){
					return "modules/sys/sysIndex";
				}
				return "redirect:" + adminPath + "/login";
			}
			String isPerfect = UserUtils.getUser().getIsPerfect();
			model.addAttribute("isPerfect", isPerfect);
			//获取该页国际化数据
			model.addAttribute("language",sysMunlLangService.getCurrentPageList("/index"));
			double params= Math.random();
			model.addAttribute("params",params);
			CmsUtils.setDefaultSite(response, UserUtils.getUser().getId());
			return "modules/sys/sysIndex";	
		}
		
	}
	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "index")
	public @ResponseBody String index(HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		ReturnMassage rm=null;
		// 登录成功后，验证码计算器清零
		//isValidateCodeLogin(principal.getLoginName(), false, true);
		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
			}else if (StringUtils.equals(logined, "true")){
				UserUtils.getSubject().logout();
				rm=new ReturnMassage("-1","失败");
				return JSON.toJSONString(rm);
			}
		}
		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()){
			if (request.getParameter("login") != null){
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null){
				return "modules/sys/sysIndex";
			}
			rm=new ReturnMassage("-1","失败");
			return JSON.toJSONString(rm);
		}
		rm=new ReturnMassage("0","登录成功！");
		return JSON.toJSONString(rm);
	}
	/**
	 * 刷新页面并保留在原页面
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}/recIndex")
	public String index2(String menuId,HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		model.addAttribute("menuId",menuId);
		// 登录成功后，验证码计算器清零
		//isValidateCodeLogin(principal.getLoginName(), false, true);
		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
			}else if (StringUtils.equals(logined, "true")){
				UserUtils.getSubject().logout();
				return "redirect:" + adminPath + "/login";
			}
		}
		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()){
			if (request.getParameter("login") != null){
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null){
				return "modules/sys/sysIndex";
			}
			return "redirect:" + adminPath + "/login";
		}
		return "modules/sys/sysIndex3";
	}
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}

    /**
     * 忘记密码修改密码
     * @return
     */
    @RequestMapping("/changepwd_forget")
	public String changePwdForget(HttpServletRequest request,Model model){
        String token = (String)UserUtils.getCache(Constants.CHANGE_PWD_FORGET_TOKEN);
        String changePwdToken = request.getParameter("forgettoken");
        if(token.equalsIgnoreCase(changePwdToken)){
            String mobile = (String)UserUtils.getCache(Constants.MOBLE_KEY);
            String newPwd = request.getParameter("pwd");
            User u = systemService.getUserByLoginName(mobile);
            if(null == u){
                model.addAttribute("msg","密码重置失败");
                return "modules/sys/verifymobile";
            }
            systemService.updatePasswordById(u.getId(),u.getLoginName(),newPwd);
            return "redirect:/login";
        }
        model.addAttribute("msg","密码重置失败");
        return "modules/sys/verifymobile";
    }
    /**
     * 忘记密码校验短信验证码
     * @return
     */
    @RequestMapping("/checkmobilecode_forget")
	public String checkMobleCodeForget(HttpServletRequest request,Model model){
        String code = (String)UserUtils.getCache(Constants.MOBLE_CODE_KEY);
        String checkmobilecode = request.getParameter("checkmobilecode");
//        if(null != code && code.equalsIgnoreCase(checkmobilecode)){
        if(true){
            String forgettoke = IdGen.uuid();
            model.addAttribute("forgettoken",forgettoke);
            UserUtils.putCache(Constants.CHANGE_PWD_FORGET_TOKEN,forgettoke);
            return "modules/sys/changepwdforget";
        }
        model.addAttribute("msg","短信验证码不正确");
        return "modules/sys/verifymobile";
    }
    /**
     * 忘记密码
     * @return
     */
    @RequestMapping("/forgetpwd")
	public String forgetPwdPage(HttpServletRequest request,Model model){
        return "modules/sys/verifymobile";
    }

    /**
     * 获取短信验证码
     * @return
     */
    @RequestMapping("/mobilecheckcode")
    @ResponseBody
	public String getMobleCode(HttpServletRequest request){
        String mobile = request.getParameter("mobile");
        if(StringUtils.isEmpty(mobile)){
            //无手机号
            return "10";
        }
        UserUtils.putCache(Constants.MOBLE_KEY,mobile);
        Random random = new Random(System.currentTimeMillis());
        StringBuffer code = new StringBuffer();
        code.append("1234");
//        for(int i = 0;i < 4;i++){
//            code.append(random.nextInt(10)-1);
//        }
        String str = code.toString();
        SMSUtils.sendSMS(mobile,str);
        UserUtils.putCache(Constants.MOBLE_CODE_KEY,str);
        return "0";
    }
	/**
	 *注册
	 */
	@RequestMapping(value = "${adminPath}/register")
	public @ResponseBody String register(HttpServletRequest request, HttpServletResponse response, Model model) {
		//短信验证码
	    String checkcode=request.getParameter("checkcode");
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String currentcode = (String)UserUtils.getCache(Constants.MOBLE_CODE_KEY);
/*        if(!currentcode.equalsIgnoreCase(checkcode)){
            //短信验证码错误
            return "-2";
        }*/
	    //身份证号
	    String papernum=request.getParameter("papernum");
        if(!checkPapernum(papernum)){
            //身份证号码不合法
            return "-3";
        }
	    String realname=request.getParameter("realname");
	    String newUserName= (String)UserUtils.getCache(Constants.MOBLE_KEY);
	    String newPasswd=request.getParameter("newPasswd");
	    if(UserUtils.getByLoginName(newUserName)!=null){
	    	//用户名已存在
	    	return "-1";
	    }else{
	    	User u=new User();
	    	u.preInsert();
	    	u.setName(realname);
            u.setPapernum(papernum);
	    	u.setLoginName(newUserName);
	    	u.setMobile(newUserName);
	    	u.setPassword(Digests.getMD5(newPasswd+newUserName));
	    	u.setCreateBy(u);
	    	u.setUpdateBy(u);
	    	u.setIsPerfect("1");
	    	u.setPhoto("/userfiles/default/userProfile_default.png");
	    	String companyId = "a2f73bc0c80b4a5e9191cb7b2f846c08";
            Office company = officeService.get(companyId);
            u.setCompany(company);
	    	String officeId = "190d3bc7673544ffa0d18dc0ae7c7602";
	    	Office office = officeService.get(officeId);
	    	u.setOffice(office);
            List<Role> roles = systemService.findRoleByRoleId(OfficeRoleConstant.ROLE_DEFAULT_NORMAL);
            u.setRoleList(roles);
	    	systemService.insertUser(u);
	    	return "0";
	    }
	}

    /**
     * 校验身份证号的合法性
     * @param papernum
     * @return
     */
    private boolean checkPapernum(String papernum) {
        if(null == papernum){
            return false;
        }
	    if(papernum.length() == 15){
	        return true;
        }else if(papernum.length() == 18){
	        return true;
        }else {
	        return false;
        }
    }

    @RequestMapping(value = "isOtherUser")
	public @ResponseBody boolean isOtherUser(HttpServletRequest request){
		boolean falg=false;
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        String userId=UserUtils.getUser().getId();
       // String  sessionIp=StringUtils.getRemoteAddr((HttpServletRequest)request);
        int count=0;
        for(Session session:sessions){
            Object sourceDevice = session.getAttribute(Constants.SOURCE_DEVICE);
            if(null != sourceDevice){
                String str = (String)sourceDevice;
                if("200".equalsIgnoreCase(str) || "300".equalsIgnoreCase(str)){
                    continue;
                }
            }
        	if(userId.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
        		count++;
        		if(count>1){
            		falg=true;
                	break;
        		}
            }
       }
       return falg;
	}
	@RequestMapping(value = "clearnuser")
	public @ResponseBody String clearnuser(HttpServletRequest request){
		/*DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        String userId=UserUtils.getUser().getId();
        for(Session session:sessions){
            Object sourceDevice = session.getAttribute(Constants.SOURCE_DEVICE);
            if(null != sourceDevice){
                String str = (String)sourceDevice;
                if("200".equalsIgnoreCase(str) || "300".equalsIgnoreCase(str)){
                    continue;
                }
            }
            if(userId.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))&&!request.getSession().getId().equals(session.getId())) {
            	session.setTimeout(0);
            	sessionManager.getSessionDAO().delete(session);
            }
         }*/
        return "1";
	}
}
