/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.dao.UserRoleOfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 用户工具类
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static UserRoleOfficeDao userRoleOfficeDao = SpringContextHolder.getBean(UserRoleOfficeDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static SystemService systemService = SpringContextHolder.getBean(SystemService.class);
	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_ID_CARD_ = "idcard_";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";
	
	public static final String CACHE_AUTH_INFO = "authInfo";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user ==  null){
			user = userDao.get(id);
			if (user == null){
				return null;
			}
			if(StringUtils.isNoneBlank(user.getPapernum())) {
				Map<String,String> map = DateUtils.getBirAgeSex(user.getPapernum());
				Date birthday = DateUtils.getBirthday(user.getPapernum());
				user.setBirthday(birthday);
				user.getUserExpand().setSex(map.get("sexCode"));
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			//查询兼职
			//user.setCareerList(userDao.selectByUserId(id, null));
			user.setPartTimeList(userRoleOfficeDao.findPartTimeJobList(new PartTimeJob(user.getId(),"1")));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_CARD_ + user.getPapernum(), user);
		}
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null){
			user = userDao.getByLoginName(new User(null, loginName));
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			//查询兼职
			//user.setCareerList(userDao.selectByUserId(user.getId(), null));
			user.setPartTimeList(userRoleOfficeDao.findPartTimeJobList(new PartTimeJob(user.getId(),"1")));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_CARD_ + user.getPapernum(), user);
		}
		return user;
	}

	/**
	 * 根据身份证号获取用户
	 * @param idCard
	 * @return 取不到返回null
	 */
	public static User getByIdCard(String idCard){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_ID_CARD_ + idCard);
		if (user == null){
			user = userDao.getByIdCard(idCard);
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			//查询兼职
			//user.setCareerList(userDao.selectByUserId(user.getId(), null));
			user.setPartTimeList(userRoleOfficeDao.findPartTimeJobList(new PartTimeJob(user.getId(),"1")));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_CARD_ + user.getPapernum(), user);
		}
		return user;
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(CACHE_AUTH_INFO);
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		UserUtils.clearCache(getUser());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_CARD_ + user.getPapernum());
		if (user.getOffice() != null && user.getOffice().getId() != null){
			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
		}
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser(){
		Principal principal = getPrincipal();
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}

	/**
	 * 获取当前用户所处机构能看到的角色列表
	 * @return
	 */
	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (roleList == null){
			User user = getUser();
			if(null == user.getId()){
				return roleList;
			}
			if (user.isAdmin()){
				roleList = roleDao.findAllList(new Role());
			}else{
				//roleList = roleDao.findList(new Role(user));
				roleList = systemService.findRoleByRoleId(user.getCompany().getId());
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		//获取session语言选择 
		Subject subject = SecurityUtils.getSubject();
	     Session session = subject.getSession();
		String lang = (String)session.getAttribute("lang");
		if(lang==null){
			 lang=Global.getConfig("langeageType");
		}
		//获取缓存语言
		@SuppressWarnings("unchecked")
		Map<String,String> object = (Map<String, String>) CacheUtils.get(lang+"_"+"/sys");
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
					menuList=menuDao.findAllList(new Menu());
			}else{
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao.findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		for (int i = 0; i < menuList.size(); i++) {
			for(String key :object.keySet()){
				if(menuList.get(i).getLanguageKey().equals(key)){
					menuList.get(i).setName(object.get(key));
				}
			}
		}
		return menuList;
	}
	
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 获取当前用户有权限访问的科室
	 * @return
	 */
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){
			User user = getUser();
			if (user.isAdmin()){
				officeList = officeDao.findAllList(new Office());
			}else{
				Office office = new Office();
				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
				officeList = officeDao.findList(office);
			}
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
		
	}

	/**
	 * 获取当前用户有权限访问的科室
	 * @return
	 */
	public static List<Office> getOfficeAllList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);
		if (officeList == null){
			officeList = officeDao.findAllList(new Office());
		}
		return officeList;
	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}

	/**
	 * 根据上级id查询该id下的区域
	 * @author 王鹏
	 * @version 2018-04-22 17:49:29
	 * @param parentId
	 * @return
	 */
	public static List<Area> findByParent(String parentId){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		List<Area> subAreas = new ArrayList<Area>();
		for (int i = 0; i < areaList.size(); i++) {
			if(areaList.get(i).getParentId().equals(parentId)) {
				subAreas.add(areaList.get(i));
			}
		}
		return subAreas;
	}

}
