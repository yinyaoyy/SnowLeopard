/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.xpath.internal.operations.And;
import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.info.dao.InfoForensicPersonnelDao;
import com.thinkgem.jeesite.modules.info.dao.JudiciaryUserDao;
import com.thinkgem.jeesite.modules.info.dao.LawAssitanceUserDao;
import com.thinkgem.jeesite.modules.info.dao.LawyerDao;
import com.thinkgem.jeesite.modules.info.dao.LegalServicePersonDao;
import com.thinkgem.jeesite.modules.info.dao.NotaryMemberDao;
import com.thinkgem.jeesite.modules.info.dao.PeopleMediationDao;
import com.thinkgem.jeesite.modules.info.dao.SupervisorDao;
import com.thinkgem.jeesite.modules.info.entity.InfoForensicPersonnel;
import com.thinkgem.jeesite.modules.info.entity.JudiciaryUser;
import com.thinkgem.jeesite.modules.info.entity.LawAssitanceUser;
import com.thinkgem.jeesite.modules.info.entity.Lawyer;
import com.thinkgem.jeesite.modules.info.entity.LegalServicePerson;
import com.thinkgem.jeesite.modules.info.entity.NotaryMember;
import com.thinkgem.jeesite.modules.info.entity.PeopleMediation;
import com.thinkgem.jeesite.modules.info.entity.Supervisor;
import com.thinkgem.jeesite.modules.language.dao.SysMunlLangDao;
import com.thinkgem.jeesite.modules.language.entity.SysMunlLang;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.dao.UserExpandDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.entity.UserExpand;
import com.thinkgem.jeesite.modules.sys.entity.UserExport;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SessionDAO sessionDao;
	@Autowired
	private UserExpandDao userExpandDao;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private  SysMunlLangDao sysMunlLangDao;
	@Autowired
	private UserRoleOfficeService userRoleOfficeService;
	@Autowired
	private JudiciaryUserDao judiciaryUserDao;
	@Autowired
	private LawyerDao lawyerDao;
	@Autowired
	private LegalServicePersonDao legalServicePersonDao;
	@Autowired
	private InfoForensicPersonnelDao infoForensicPersonnelDao;
	@Autowired
	private LawAssitanceUserDao lawAssitanceUserDao;
	@Autowired
	private NotaryMemberDao notaryMemberDao;
	@Autowired
	private PeopleMediationDao peopleMediationDao;
	@Autowired
	private SupervisorDao supervisorDao;
	private ApiAgencySearch apiAgencySearch;
	private static final String DICT_AGENCY_CAGETORY = "sys_office_category";

	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	@Autowired
	private IdentityService identityService;
	

	
	/**
	 * 获取用户
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return UserUtils.get(id);
	}

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}
	
	public Page<User> findUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(userDao.findList(user));
		return page;
	}
	
	/**
	 * 无分页查询人员列表
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		List<User> list = userDao.findList(user);
		return list;
	}

	/**
	 * 通过科室ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>)CacheUtils.get(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null){
			User user = new User();
			user.setOffice(new Office(officeId));
			list = userDao.findUserByOfficeId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}
	
	@Transactional(readOnly = false)
	public void saveUser(User user,String isMain) {
		//校验登录名是否存在
		User checkUser = getUserByLoginName(user.getLoginName());
		if (StringUtils.isBlank(user.getId())){
			if(checkUser!=null) {
				throw new BusinessException("登录名已存在！");
			}
			boolean b = getUserInfoByIdCard(user.getPapernum());
			if(b){
				throw new BusinessException("身份证已存在！");
			}
			user.preInsert();
			String userImg = user.getPhoto();
			if(userImg!=null&&!"".equals(userImg)){
				String fileName = user.getId() + ".jpeg";
				String realPath ="/userfiles/"+user.getId()+"/_thumbs/images";
				FileUtils.GenerateImage(userImg, realPath, fileName);
				FileUtils.GenerateImage(userImg, "/userfiles/"+user.getId()+"/images", fileName);
				user.setPhoto("/userfiles/"+user.getId()+"/_thumbs/images/"+fileName+"?params="+Math.random() );
			}else{
				user.setPhoto("/userfiles/default/userProfile_default.png");
			}
			boolean  isUser=true;//是否具有普通用户角色
			for (Role role : user.getRoleList()) {
				if(OfficeRoleConstant.ROLE_DEFAULT_NORMAL.equals(role.getId())){
					isUser=false;
				}
			}
			if(isUser){
				List<Role> roleList=user.getRoleList();
				roleList.add(new Role(OfficeRoleConstant.ROLE_DEFAULT_NORMAL));
			}
			userDao.insert(user);
			if(user.getUserExpand()!=null){
				user.getUserExpand().setId(user.getId());
				userExpandDao.insert(user.getUserExpand());
			}
		}else{
			User oldUser=UserUtils.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null){
				CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			String userImg = user.getPhoto();
			if(userImg.indexOf("/userfiles")==-1){
				String fileName = user.getId() + ".jpeg";
				String realPath ="/userfiles/"+user.getId()+"/_thumbs/images";
				FileUtils.GenerateImage(userImg, realPath, fileName);
				FileUtils.GenerateImage(userImg, "/userfiles/"+user.getId()+"/images", fileName);
				userImg = "/userfiles/"+user.getId()+"/_thumbs/images/"+fileName+"?params="+Math.random();
			}
			List<Role> list=roleDao.findList(new Role(user));
			if(user.getRole()!=null){
				list.add(user.getRole());
			}
			user.setRoleList(list);
			user.setPhoto(userImg);
			userDao.update(user);
			user.getUserExpand().setId(user.getId());
			UserExpand userExpand = userExpandDao.get(user.getUserExpand());
			if(userExpand!=null){
				userExpandDao.update(user.getUserExpand());
			}else{
				userExpandDao.insert(user.getUserExpand());
			}
		}
		if (StringUtils.isNotBlank(user.getId())){
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0){
				userDao.insertUserRole(user);
			}else{
				throw new BusinessException(user.getLoginName() + "没有设置角色！");
			}
			// 将当前用户同步到Activiti
			saveActivitiUser(user);
			// 清除用户缓存
			UserUtils.clearCache(user);
		}
		List<PartTimeJob> partTimeJobList= updatepartList(user.getId(),user.getRoleList(),user.getOffice().getId(),isMain);
		try {
			userRoleOfficeService.insertBatch(partTimeJobList);;
		} catch (Exception e) {
			System.out.println("--------------------"+user.getName()+"-----------");
		}
	}
	/**
	 * 处理从业务表增加兼职的情况
	 * @param user 不同的业务表id
	 * @param isMain 直接传兼职的标记就行， 
	 * @param uaid 兼职对应的信息
	 * 业务表数据在对应业务的业务逻辑层删除
	 * @author suzz
	 */
	@Transactional(readOnly = false)
	public void saveUserZh(User user,String isMain,UserAndInfoData uaid) {
	   ArrayList<Role> arrayList = new ArrayList<>();
	    String[] roleArr=uaid.getRoleId().split(",");
	    for (String string : roleArr) {
	    	Role role = new Role();
	  	   role.setId(string);
	  	   arrayList.add(role);
		}
		List<PartTimeJob> partTimeJobList= updatepartList(user.getId(),arrayList,uaid.getInfoDataOfficeId(),isMain);
		try {
			userRoleOfficeService.insertBatch(partTimeJobList);
		} catch (Exception e) {
			System.out.println("--------------------"+user.getName());
		}
		
	}
	
	@Transactional(readOnly = false)
	public void save(User user) {
		//校验登录名是否存在
		User checkUser = getUserByLoginName(user.getLoginName());
		if (StringUtils.isBlank(user.getId())){
			if(checkUser!=null) {
				throw new BusinessException("登录名已存在！");
			}
			boolean b = getUserInfoByIdCard(user.getPapernum());
			if(b){
				throw new BusinessException("身份证已存在！");
			}
			user.preInsert();
			String userImg = user.getPhoto();
			if(userImg!=null&&!"".equals(userImg)){
				String fileName = user.getId() + ".jpeg";
				String realPath ="/userfiles/"+user.getId()+"/_thumbs/images";
				FileUtils.GenerateImage(userImg, realPath, fileName);
				FileUtils.GenerateImage(userImg, "/userfiles/"+user.getId()+"/images", fileName);
				user.setPhoto("/userfiles/"+user.getId()+"/_thumbs/images/"+fileName+"?params="+Math.random() );
			}else{
				user.setPhoto("/userfiles/default/userProfile_default.png");
			}
			userDao.insert(user);
			if(user.getUserExpand()!=null){
				user.getUserExpand().setId(user.getId());
				userExpandDao.insert(user.getUserExpand());
			}
			List<PartTimeJob> partTimeJobList= updatepartList(user.getId(),user.getPartTimeList(),user.getRoleList(),user.getOffice().getId());
			userRoleOfficeService.insertBatch(partTimeJobList);
			//userRoleOfficeService.delete(user.getId());
		}else{
			/*//登录名已存在且不是自己的
			if(checkUser!=null && !checkUser.getId().equals(user.getId())) {
				throw new BusinessException("登录名已存在！");
			}*/
			// 清除原用户机构用户缓存
			//User oldUser = userDao.get(c);
			User oldUser=UserUtils.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null){
				CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			String userImg = user.getPhoto();
			if(userImg.indexOf("/userfiles")==-1){
				String fileName = user.getId() + ".jpeg";
				String realPath ="/userfiles/"+user.getId()+"/_thumbs/images";
				FileUtils.GenerateImage(userImg, realPath, fileName);
				FileUtils.GenerateImage(userImg, "/userfiles/"+user.getId()+"/images", fileName);
				userImg = "/userfiles/"+user.getId()+"/_thumbs/images/"+fileName+"?params="+Math.random();
			}
			//user.setRoleList(roleDao.findList(new Role(user)));
			user.setPhoto(userImg);
			userDao.update(user);
			user.getUserExpand().setId(user.getId());
			UserExpand userExpand = userExpandDao.get(user.getUserExpand());
			if(userExpand!=null){
				userExpandDao.update(user.getUserExpand());
			}else{
				userExpandDao.insert(user.getUserExpand());
			}
		}
		if (StringUtils.isNotBlank(user.getId())){
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0){
				userDao.insertUserRole(user);
			}else{
				throw new BusinessException(user.getLoginName() + "没有设置角色！");
			}
			// 将当前用户同步到Activiti
			saveActivitiUser(user);
			// 清除用户缓存
			UserUtils.clearCache(user);
		}
		List<PartTimeJob> partTimeJobList= updatepartList(user.getId(),user.getPartTimeList(),user.getRoleList(),user.getOffice().getId());
		userRoleOfficeService.delete(user.getId());
		userRoleOfficeService.insertBatch(partTimeJobList);
	}
	@Transactional(readOnly = false)
	public String saveUserReturnId(User user) {
		String userId="";
		if (StringUtils.isBlank(user.getId())){
			//System.out.println(user.getId());
			user.preInsert();
			userId=user.getId();
			userDao.insert(user);
		}else{
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			//System.out.println(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null){
				CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			userId=user.getId();
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())){
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0){
				userDao.insertUserRole(user);
			}else{
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			// 将当前用户同步到Activiti
			saveActivitiUser(user);
			// 清除用户缓存
			UserUtils.clearCache(user);
		}
		return userId;
	}
	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		if(user.getUserExpand()==null){
			UserExpand ue=new UserExpand();
			ue.setId(user.getId());
			user.setUserExpand(ue);
		}else{
			user.getUserExpand().setId(user.getId());
		}
		UserExpand userExpand = userExpandDao.get(user.getUserExpand());
		if(userExpand!=null){
			userExpandDao.update(user.getUserExpand());
		}else{
			userExpandDao.insert(user.getUserExpand());
		}
		// 清除用户缓存
		UserUtils.clearCache(user);
	}
	
	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.delete(user);
		deletYeWuDate(user);
		// 同步到Activiti
		deleteActivitiUser(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
	}
	@Transactional(readOnly = false)
	public int batchDelete(List<String>  batchid) {
		for(int i=0;i<batchid.size();i++){
			User user = new User();
			user.setId(batchid.get(i)+"");
			deletYeWuDate(user);
			deleteActivitiUser(user);
			UserUtils.clearCache(user);
		}
		 return userDao.batchDelete(batchid);
	}
	
	
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(Digests.getMD5(newPassword+loginName));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		user.setLoginName(loginName);
		UserUtils.clearCache(user);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(StringUtils.getRemoteAddr(Servlets.getRequest()));
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}

	/**
	 * 修改个人用户信息，目前只有接口在用 如初
	 */
	@Transactional(readOnly = false)
	public void UpdateUserExpand(User userExpand) {
		User user = UserUtils.getUser();
		user.setArea(userExpand.getArea());
		user.setTownarea(userExpand.getTownarea());
		user.setBirthday(userExpand.getBirthday());
		user.setMobile(userExpand.getMobile());
		user.setUserSourceType(userExpand.getUserSourceType());
		user.setPapernum(userExpand.getPapernum());
		user.setUpdateDate(new Date());
		// System.out.println(user.getId());
		if((null !=userExpand.getUserExpand().getEducation()||null !=userExpand.getUserExpand().getSex())&&(!"".equals(userExpand.getUserExpand().getEducation())||!"".equals(userExpand.getUserExpand().getSex()))) {
			UserExpand userExpandee = userExpandDao.getUserExpand(user.getId());
			if (null==userExpandee) {
				userExpandee = new UserExpand(user.getId());
				userExpandee.setSex(userExpand.getUserExpand().getSex());
				userExpandee.setEducation(userExpand.getUserExpand().getEducation());
				userExpandee.setCreateDate(new Date());
				userExpandDao.insert(userExpandee);
			} else {
				userExpandee.setSex(userExpand.getUserExpand().getSex());
				userExpandee.setEducation(userExpand.getUserExpand().getEducation());
				userExpandee.setUpdateDate(new Date());
				userExpandDao.updateQz(userExpandee);
				
		}
		}
		userDao.updatequn(user);
		UserUtils.clearCache(user);
	}
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword,String loginName) {
		return Digests.getMD5(plainPassword+loginName);
		/*String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);*/
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		/*String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));*/
		return plainPassword.equals(password);
	}
	
	/**
	 * 获得活动会话
	 * @return
	 */
	public Collection<Session> getActiveSessions(){
		return sessionDao.getActiveSessions(false);
	}
	
	//-- Role Service --//
	
	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return roleDao.getByName(r);
	}
	
	public Role getRoleByEnname(String enname) {
		Role r = new Role();
		r.setEnname(enname);
		return roleDao.getByEnname(r);
	}
	
	public List<Role> findRole(Role role){
		return roleDao.findList(role);
	}
	
	public List<Role> findAllRole(){
		return UserUtils.getRoleList();
	}
	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (StringUtils.isBlank(role.getId())){
			role.preInsert();
			roleDao.insert(role);
			// 同步到Activiti
			saveActivitiGroup(role);
		}else{
			role.preUpdate();
			roleDao.update(role);
		}
		// 更新角色与菜单关联
		roleDao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0){
			roleDao.insertRoleMenu(role);
		}
		// 更新角色与科室关联
		roleDao.deleteRoleOffice(role);
		if (role.getOfficeList().size() > 0){
			roleDao.insertRoleOffice(role);
		}
		// 同步到Activiti
		saveActivitiGroup(role);
		// 清除用户角色缓存
		List<User> userList = this.findUser(new User(new Role(role.getId())));
		for (User user : userList) {
			UserUtils.clearCache(user);
		}
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);
		// 同步到Activiti
		deleteActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles){
			if (e.getId().equals(role.getId())){
				roles.remove(e);
				save(user);
				return true;
			}
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) {
		if (user == null){
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		save(user);
		return user;
	}

	
	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));
		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds(); 
		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		// 保存或更新实体
		if (StringUtils.isBlank(menu.getId())){
			menu.preInsert();
			menuDao.insert(menu);
			String langCode=menu.getLanguageKey();
			if(langCode!=null&&!"".equals(langCode)){
				addManuToLanguage(menu);
			}
 		}else{
			//如果菜单名修改，修改当前登录语言下国际化语言中的菜单名
 			Menu  oldMenu=menuDao.get(menu.getId());
 			SysMunlLang sysMunlLang=new SysMunlLang();
 			if(!oldMenu.getLanguageKey().equals(menu.getLanguageKey())){//语言key修改了
 				sysMunlLangDao.updateLangKeyByLangKey(oldMenu.getLanguageKey(), menu.getLanguageKey());
 			}
			if(!oldMenu.getName().equals(menu.getName())){////菜单名修改了
 				//菜单名修改了
 	 				sysMunlLang.setLangKey(oldMenu.getLanguageKey());
 	 				sysMunlLang.setLangCode(DictUtils.getCurrentLanguage());
 	 				sysMunlLang =sysMunlLangDao.findList(sysMunlLang).get(0);
 	 				sysMunlLang.setLangContext(menu.getName());
 	 			}
 			sysMunlLang.preUpdate();
 			sysMunlLangDao.update(sysMunlLang);
			menu.preUpdate();
			menuDao.update(menu);
		}
		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%,"+menu.getId()+",%");
		List<Menu> list = menuDao.findByParentIdsLike(m);
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}
    private void addManuToLanguage(Menu menu){
    	try {
    	 	String langCode=menu.getLanguageKey();
    		String menuName=menu.getName();
        	//添加国际化数据
    		List<Dict> langList =DictUtils.getDictListByLanguage("act_langtype", "CN");
    		List<SysMunlLang> sysMunlLangList=Lists.newArrayList();
    		for (Dict dict : langList) {
    			SysMunlLang s=new SysMunlLang();
    			s.preInsert();
    			s.setLangKey(langCode);
    			s.setLangContext(menuName);
    			s.setLangCode(dict.getValue());
    			User user = UserUtils.getUser();
    			Date now = new Date();
    			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			String hehe = df.format(now); 
    			Date date = df.parse(hehe);  
    			s.setCreateDate(date);
    			s.setCreateName(user.getName());
    			s.setOperationType("1");
    			s.setAttributeType("1");
    			s.setLanguageAscription("/sys");
    			String lang = dict.getValue();
    			if(lang==null){
    				lang="";
    			}
    			@SuppressWarnings("unchecked")
    			Map<String,String> object = (Map<String, String>) CacheUtils.get(langCode+"_"+"/sys");
    			if(object!=null){
    				object.put(langCode, menuName);
    			}
    			CacheUtils.put(langCode+"_"+"/sys", object);
    			sysMunlLangList.add(s);
    		}
    		sysMunlLangDao.batchAdd(sysMunlLangList);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		menuDao.updateSort(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu,SysMunlLang sysMunlLang) {
		menuDao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
		sysMunlLangDao.deleteBylangKey(sysMunlLang);
	}
	
	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 "+Global.getConfig("productName")+"  - Powered By \r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}
	
	

	/**
	 * 是需要同步Activiti数据，如果从未同步过，则同步数据。
	 */
	private static boolean isSynActivitiIndetity = true;
	public void afterPropertiesSet() throws Exception {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		if (isSynActivitiIndetity){
			isSynActivitiIndetity = false;
	        // 同步角色数据
			List<Group> groupList = identityService.createGroupQuery().list();
			if (groupList.size() == 0){
			 	Iterator<Role> roles = roleDao.findAllList(new Role()).iterator();
			 	while(roles.hasNext()) {
			 		Role role = roles.next();
			 		saveActivitiGroup(role);
			 	}
			}
		 	// 同步用户数据
			List<org.activiti.engine.identity.User> userList = identityService.createUserQuery().list();
			if (userList.size() == 0){
			 	Iterator<User> users = userDao.findAllList(new User()).iterator();
			 	while(users.hasNext()) {
			 		saveActivitiUser(users.next());
			 	}
			}
		}
	}
	
	private void saveActivitiGroup(Role role) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		String groupId = role.getEnname();
		
		// 如果修改了英文名，则删除原Activiti角色
		if (StringUtils.isNotBlank(role.getOldEnname()) && !role.getOldEnname().equals(role.getEnname())){
			identityService.deleteGroup(role.getOldEnname());
		}
		
		Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
		if (group == null) {
			group = identityService.newGroup(groupId);
		}
		group.setName(role.getName());
		group.setType(role.getRoleType());
		identityService.saveGroup(group);
		
		// 删除用户与用户组关系
		List<org.activiti.engine.identity.User> activitiUserList = identityService.createUserQuery().memberOfGroup(groupId).list();
		for (org.activiti.engine.identity.User activitiUser : activitiUserList){
			identityService.deleteMembership(activitiUser.getId(), groupId);
		}

		// 创建用户与用户组关系
		List<User> userList = findUser(new User(new Role(role.getId())));
		for (User e : userList){
			String userId = e.getLoginName();//ObjectUtils.toString(user.getId());
			// 如果该用户不存在，则创建一个
			org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
			if (activitiUser == null){
				activitiUser = identityService.newUser(userId);
				activitiUser.setFirstName(e.getName());
				activitiUser.setLastName(StringUtils.EMPTY);
				activitiUser.setEmail(e.getEmail());
				activitiUser.setPassword(StringUtils.EMPTY);
				identityService.saveUser(activitiUser);
			}
			identityService.createMembership(userId, groupId);
		}
	}

	public void deleteActivitiGroup(Role role) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		if(role!=null) {
			String groupId = role.getEnname();
			identityService.deleteGroup(groupId);
		}
	}

	private void saveActivitiUser(User user) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
		org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
		if (activitiUser == null) {
			activitiUser = identityService.newUser(userId);
		}
		activitiUser.setFirstName(user.getName());
		activitiUser.setLastName(StringUtils.EMPTY);
		activitiUser.setEmail(user.getEmail());
		activitiUser.setPassword(StringUtils.EMPTY);
		identityService.saveUser(activitiUser);
		
		// 删除用户与用户组关系
		List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
		for (Group group : activitiGroups) {
			identityService.deleteMembership(userId, group.getId());
		}
		// 创建用户与用户组关系
		for (Role role : user.getRoleList()) {
	 		String groupId = role.getEnname();
	 		// 如果该用户组不存在，则创建一个
		 	Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
            if(group == null) {
	            group = identityService.newGroup(groupId);
	            group.setName(role.getName());
	            group.setType(role.getRoleType());
	            identityService.saveGroup(group);
            }
			identityService.createMembership(userId, role.getEnname());
		}
	}

	private void deleteActivitiUser(User user) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		if(user!=null) {
			String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
			identityService.deleteUser(userId);
		}
	}
	public  String getUserArea(){
		User currentUser = UserUtils.getUser();
		if(currentUser.getOffice()!=null){
			return currentUser.getCompany().getId();
		}else{
			return "0";
		}
	}
	@Transactional(readOnly = false)
	public int insertUser(User user){
		return  userDao.insert(user);
	}
	//完善用户信息
	@Transactional(readOnly = false)
	public void userInfo(User user){
			// 清除原用户机构用户缓存
		User oldUser = userDao.get(user.getId());
		if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null){
			CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
		}
		// 更新用户数据
		user.preUpdate();
		userDao.updateInfo(user);
		if (StringUtils.isNotBlank(user.getId())){
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0){
				userDao.insertUserRole(user);
			}else{
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			// 将当前用户同步到Activiti
			saveActivitiUser(user);
			// 清除用户缓存
			UserUtils.clearCache();
		}
	}
	//获取用户信息
	public User getUserInfo(String userId){
		User user = userDao.get(userId);
		return user;
	}
	public List<Role> findRoleByRoleId(String id){
		List<Office> oList= officeService.getOfficeByCompanyId(id);
		if(oList!=null&&oList.size()>0){
			return roleDao.findRoleByRoleId(oList);
		}else{
			return Lists.newArrayList();
		}
		
	}
	//获取全部用户信息
	public List<User> getAllList(User user){
		return userDao.getAllList(user);
	}
	public List<Role> findRoleListBySiteId(String siteId){
		return roleDao.findRoleListBySiteId(siteId);
	}
	@Transactional(readOnly = false)
	public int  updatePhotoById(User user){
		return userDao.updatePhotoById(user);
	}
	public List<Role> findRoleListByServerId(String serverId){
		return roleDao.findRoleListByServerId(serverId);
	}
	
	/**
	 * 根据业务id查询账号相关id
	 * @author 王鹏
	 * @version 2018-05-17 17:21:37
	 * @param infoId
	 * @param type 1=机构;2=人员
	 * @return
	 */
	public Office getSysUserOfficeInfo(String infoId, String type) {
		//删除人员的时候用了这个方法，修改时请注意
		return new Office(userDao.selectSysUserOfficeInfo(infoId, type));
	}

	/**
	 * 自动为业务数据中的机构在账号体系中创建对应记录，方便对业务数据人员账号的创建和管理
	 * @author 王鹏
	 * @version 2018-05-11 09:26:56
	 * @param infoDataId 业务数据机构id
	 * @param infoDataName 业务数据机构名称
	 * @param officeType OfficeRoleConstant业务数据分类
	 * @param area 所在区域
	 * @param code 机构编码  可空
	 * @param remark 备注
	 * @throws BusinessException
	 */
	@Transactional
	public void saveSysUserOfficeInfo(String infoDataId, String infoDataName, String officeType,
			Area area, String code, String remark) {
		Office office = new Office(infoDataName, officeType, area, code);
		String officeId = userDao.selectSysUserOfficeInfo(infoDataId, "1");
		office.setId(officeId);
		officeService.save(office);
		if(StringUtils.isBlank(officeId)) {//若是新增则保存一下对照关系
			UserAndInfoData uaid = new UserAndInfoData();
			uaid.setId(office.getId());
			uaid.setInfoDataId(infoDataId);
			uaid.setType("1");//机构类
			uaid.setRemark(remark);
			userDao.insertSysUserOfficeInfo(uaid);
		}
	}
	
	@Transactional
	public void saveSysUserOfficeInfo(String officeId, String infoDataId, 
			String type, String remark) {
		UserAndInfoData uaid = new UserAndInfoData();
		uaid.setId(officeId);
		uaid.setInfoDataId(infoDataId);
		uaid.setType(type);
		uaid.setRemark(remark);
		userDao.insertSysUserOfficeInfo(uaid);
	}
	
	/**
	 * 自动为业务数据中的人员创建账号
	 * @author 王鹏
	 * @version 2018-05-11 09:05:11
	 * @param name 人员姓名
	 * @param companyId OfficeRoleConstant中机构的分类id
	 * @param infoDataOfficeId 具体的业务数据机构id
	 * @param loginName 登录账号
	 * @param roleId OfficeRoleConstant对应的角色id
	 * @param infoDataId 业务数据id
	 * @param remark 备注
	 * @throws BusinessException 
	 */
	@Transactional
	public void saveUserByInfoData(UserAndInfoData uaid) {
		Office company = new Office(uaid.getCompanyId());
		Office office = new Office(uaid.getInfoDataOfficeId());
		//根据身份证号查询是否有其他 职位
		User user=null;
		if(StringUtils.isNotBlank(uaid.getIdCard())){
			user= userDao.getByIdCard(uaid.getIdCard());
		}
		String userId = userDao.selectSysUserOfficeInfo(uaid.getInfoDataId(), "2");
		if(user == null) {//如果没有创建过账号
			user = new User(uaid.getName(), company, office, uaid.getLoginName(),
					uaid.getRoleId(), uaid.getIdCard());
			user.setId(userId);
			uaid.setIsMain("0");//是主要职位
			user.setMobile(uaid.getPhone());
			user.setName(uaid.getName());
			user.setUserSourceType(uaid.getUserSourceType());
			saveUser(user,"0");
		}
		else {
			uaid.setIsMain("0");//是兼职
			if(!user.getName().equals(uaid.getName())) {
				//如果改名了
				user.setName(uaid.getName());
				user.setUserSourceType(uaid.getUserSourceType());
				//CacheUtils.remove(key);
				
			}
			saveUserZh(user,"1",uaid);
		}
		uaid.setId(user.getId());
		uaid.setType("2");//人员处理
		if(StringUtils.isBlank(userId)) {
			//保存账号和业务数据的对照关系
			userDao.insertSysUserOfficeInfo(uaid);
		}
		//User u=new User(user.getId());
		//u.setRole(new Role(uaid.getRoleId()));
	}
	
	
	public ResponseCode getByLoginNameAndPassword(User user){
		User u=	userDao.getByLoginName(user);
		if(u==null||StringUtils.isBlank(u.getId())) {
//			   return false;//没有找到用户
			return ResponseCode.LOGINNAME_NOT_EXISTS;
		}
		if(validatePassword(user.getPassword(),u.getPassword())) {
			return ResponseCode.SUCCESS;
		}
		return ResponseCode.PASSWORD_WRONG;
	}
	

	/**
	 * 根据条件查询用户列表(不含权限)
	 * @author 王鹏
	 * @version 2018-05-22 09:36:05
	 * @param user
	 * @return
	 */
	public List<User> findUserByCondition(User user){
		List<User> list = userDao.findList(user);
		return list;
	}
	
	/**
	 * 根据业务数据主键删除对照关系
	 * @author 王鹏
	 * @version 2018-06-06 09:58:09
	 * @param infoDataId
	 * @return
	 */
	public int deleteUserInfoByInfoDataId(String infoDataId) {
		//更新时提示下目前删除机构对应业务主键也在用请综合考虑 suzz
		return userDao.deleteByInfoDataId(infoDataId);
	}
	
	/**
	 * 通过roleId获取role
	 * @param id
	 * @return
	 */
	public Role findRoleById(String id){
		return roleDao.get(id);
	}
	/**
	 * 更新角色排序
	 * @param role
	 */
	@Transactional(readOnly = false)
	public void updateRoleSort(Role role) {
		roleDao.updateRoleSort(role);
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
	}

	public boolean getUserInfoByIdCard(String papernum) {
		User u = userDao.getByIdCard(papernum);
		if(u!=null){
			return true;
		}
		return false; 
		
	}
	@Transactional(readOnly = false)
	public void saveUserExport(UserExport userExport) {
		// TODO Auto-generated method stub
		//校验登录名是否存在
				User checkUser = userDao.getByIdCard(userExport.getPapernum());
				if (StringUtils.isBlank(userExport.getU().getId())){
					if(checkUser!=null) {
						throw new BusinessException("用户已存在！");
					}
					userExport.preInsert();
					String loginName=Chinese2pinyin.convert(userExport.getName(),HanyuPinyinCaseType.LOWERCASE,false).replace(" ", "");
					
					User u = userDao.getInfoByLoginName(loginName.trim());
					if(u!=null){
						loginName=loginName+"yh"+userDao.findAllCount(new User());
					}
					userExport.setLoginName(loginName);//设置登录
					userExport.setPwd(Digests.getMD5(userExport.getPwd()+loginName));
					String userImg = userExport.getPhoto();
					userExport.setLoginFlag("0");
					if(userImg!=null&&!"".equals(userImg)){
						String fileName = userExport.getId() + ".jpeg";
						String realPath ="/userfiles/"+userExport.getId()+"/_thumbs/images";
						FileUtils.GenerateImage(userImg, realPath, fileName);
						FileUtils.GenerateImage(userImg, "/userfiles/"+userExport.getId()+"/images", fileName);
						userExport.setPhoto("/userfiles/"+userExport.getId()+"/_thumbs/images/"+fileName+"?params="+Math.random() );
					}else{
						userExport.setPhoto("/userfiles/default/userProfile_default.png");
					}
					userExport.getU().setUserExpand(userExport.getUe());
					userDao.insert(userExport.getU());
					if(userExport.getUe()!=null){
						userExpandDao.insert(userExport.getUe());
					}
				}else{
					/*//登录名已存在且不是自己的
					if(checkUser!=null && !checkUser.getId().equals(userExport.getId())) {
						throw new BusinessException("登录名已存在！");
					}*/
					// 清除原用户机构用户缓存
					User oldUser = userDao.get(userExport.getU().getId());
					if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null){
						CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
					}
					// 更新用户数据
					userExport.getU().preUpdate();
					String userImg = userExport.getPhoto();
					if(userImg.indexOf("/userfiles")==-1){
						String fileName = userExport.getId() + ".jpeg";
						String realPath ="/userfiles/"+userExport.getId()+"/_thumbs/images";
						FileUtils.GenerateImage(userImg, realPath, fileName);
						FileUtils.GenerateImage(userImg, "/userfiles/"+userExport.getId()+"/images", fileName);
						userImg = "/userfiles/"+userExport.getId()+"/_thumbs/images/"+fileName+"?params="+Math.random();
					}
					userExport.setPhoto(userImg);
					userDao.update(userExport.getU());
					userExport.getUe().setId(userExport.getU().getId());
					UserExpand userExpand = userExpandDao.get(userExport.getUe());
					if(userExpand!=null){
						userExpandDao.update(userExport.getUe());
					}else{
						userExpandDao.insert(userExport.getUe());
					}
				}
				if (StringUtils.isNotBlank(userExport.getU().getId())){
					// 更新用户与角色关联
					userDao.deleteUserRole(userExport.getU());
					if (userExport.getU().getRoleList() != null && userExport.getU().getRoleList().size() > 0){
						/*String ttt=OfficeRoleConstant.ROLE_DEFAULT_NORMAL;
						List<String> roleIdList = userExport.getU().getRoleIdList();
						roleIdList.add(ttt);
						userExport.getU().setRoleIdList(roleIdList);
						List<PartTimeJob> partTimeJobList= updatepartList(userExport.getU().getId(),userExport.getU().getPartTimeList(),userExport.getU().getRoleList(),userExport.getU().getOffice().getId());
						userRoleOfficeService.insertBatch(partTimeJobList);*/
						userDao.insertUserRole(userExport.getU());
						
					}else{
						throw new BusinessException(userExport.getU().getLoginName() + "没有设置角色！");
					}
					// 将当前用户同步到Activiti
					saveActivitiUser(userExport.getU());
					// 清除用户缓存
					UserUtils.clearCache(userExport.getU());
				}
	}
	
	/**
	 * 获取机构人员功能(云平台专用)
	 * type  机构人员类型 2.律师事务所3.公证处 4.司法鉴定所8.基层法律服务所5.法援中心10.人民调委会12.司法所
	 * isUser 0或者空不查询人员 1查询人员
	 * areaId  地区旗县或者乡镇
	 * @author 王兆林
	 * @version 2018-05-24 21:15:37
	 * @return
	 */
	public List<Map<String, Object>> getOfficeUser(String type, String isUser,String areaId,String townId,String officeId){
		AgencyForm agencyForm=new AgencyForm();
		agencyForm.setTownId(townId);
		agencyForm.setPageNo(1);
		agencyForm.setPageSize(5000);
    	if(StringUtils.isNotBlank(type)){
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY,type);
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		if(StringUtils.isNotBlank(areaId)&&!"5".equals(areaId)){
			agencyForm.setAreaId(areaId);
		}
		PageVo<AgencyVo> page = apiAgencySearch.searchAgency(agencyForm);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<AgencyVo> list=page.getList();
		switch (type) {
		case "2":
			agencyForm.setCategoryId("1");
			break;
		case "3":
			agencyForm.setCategoryId("6");
			break;
		case "4":
			agencyForm.setCategoryId("7");
			break;
		case "5":
			agencyForm.setCategoryId("9");
			break;
		case "10":
			agencyForm.setCategoryId("11");
			break;
		case "8":
			agencyForm.setCategoryId("13");
			break;
		case "12":
			agencyForm.setCategoryId("15");
			break;
		default:
			break;
		}
		if(StringUtils.isNotBlank(type)){
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY,agencyForm.getCategoryId());
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		for (int i = 0; i < list.size(); i++) {
			AgencyVo agencyVo=list.get(i);
			if(StringUtils.isBlank(officeId)||"null".equals(officeId)||agencyVo.getId().equals(officeId)){
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", agencyVo.getId());
			map.put("pId","0");
			map.put("pIds","0,");
			map.put("name", agencyVo.getAgencyName());
			map.put("isParent", true);
			agencyForm.setOfficeId(agencyVo.getId());
			/*
			 * by 王鹏
			 * 2018年7月10日17:03:54
			 * 改为异步加载(tagTreeselectt.jsp)处理
			 * 
			 * */if("1".equals(isUser)){
				agencyForm.setOfficeId(agencyVo.getId());
				agencyForm.setAreaId("");
				agencyForm.setTownId("");
				page = apiAgencySearch.searchAgency(agencyForm);
				List<AgencyVo> ulist=page.getList();
				//List<Map<String, Object>> users = Lists.newArrayList();
				for (int y = 0; y<ulist.size();y++ ){
					Map<String, Object> map3 = Maps.newHashMap();
					AgencyVo agencyVou=ulist.get(y);
					map3.put("id", agencyVou.getId());
					map3.put("name", agencyVou.getPersonName());
					map3.put("pId", agencyVo.getId());
					map3.put("pIds",map.get("pIds")+agencyVo.getId());
					mapList.add(map3);
				}
			}
			mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 获取机构人员功能(云平台专用)
	 * type  机构人员类型 2.律师事务所3.公证处 4.司法鉴定所8.基层法律服务所5.法援中心10.人民调委会12.司法所
	 * isUser 0或者空不查询人员 1查询人员
	 * areaId  地区旗县或者乡镇
	 * @author 王兆林
	 * @version 2018-05-24 21:15:37
	 * @return
	 */
	public List<Map<String, Object>> getOfficeUser(String type, String isUser,String areaId,String townId,String officeId,String flag){
		AgencyForm agencyForm=new AgencyForm();
		agencyForm.setTownId(townId);
		agencyForm.setPageNo(1);
		agencyForm.setPageSize(5000);
    	if(StringUtils.isNotBlank(type)){
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY,type);
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		if(StringUtils.isNotBlank(areaId)&&!"5".equals(areaId)){
			agencyForm.setAreaId(areaId);
		}
		PageVo<AgencyVo> page = apiAgencySearch.searchAgency(agencyForm);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<AgencyVo> list=page.getList();
		switch (type) {
		case "2":
			agencyForm.setCategoryId("1");
			break;
		case "3":
			agencyForm.setCategoryId("6");
			break;
		case "4":
			agencyForm.setCategoryId("7");
			break;
		case "5":
			agencyForm.setCategoryId("9");
			break;
		case "10":
			agencyForm.setCategoryId("11");
			break;
		case "8":
			agencyForm.setCategoryId("13");
			break;
		case "12":
			agencyForm.setCategoryId("15");
			break;
		default:
			break;
		}
		if(StringUtils.isNotBlank(type)){
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY,agencyForm.getCategoryId());
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		for (int i = 0; i < list.size(); i++) {
			AgencyVo agencyVo=list.get(i);
			if(StringUtils.isBlank(officeId)||"null".equals(officeId)||agencyVo.getId().equals(officeId)){
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", agencyVo.getId());
			map.put("pId","0");
			map.put("pIds","0,");
			map.put("name", agencyVo.getAgencyName());
			map.put("isParent", true);
			agencyForm.setOfficeId(agencyVo.getId());
			/*
			 * by 王鹏
			 * 2018年7月10日17:03:54
			 * 改为异步加载(tagTreeselectt.jsp)处理
			 * 
			 * */if("1".equals(isUser)){
				agencyForm.setOfficeId(agencyVo.getId());
				agencyForm.setAreaId("");
				agencyForm.setTownId("");
				if(!"".equals(flag)){
					agencyForm.setIsAidLawyer("1");
				}
				page = apiAgencySearch.searchAgency(agencyForm);
				List<AgencyVo> ulist=page.getList();
				//List<Map<String, Object>> users = Lists.newArrayList();
				for (int y = 0; y<ulist.size();y++ ){
					Map<String, Object> map3 = Maps.newHashMap();
					AgencyVo agencyVou=ulist.get(y);
					map3.put("id", agencyVou.getId());
					map3.put("name", agencyVou.getPersonName());
					map3.put("pId", agencyVo.getId());
					map3.put("pIds",map.get("pIds")+agencyVo.getId());
					mapList.add(map3);
				}
			}
			mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 获取机构人员功能（接口专用）
	 * type  机构人员类型 2.律师事务所8.基层法律服务所5.法援中心10.人民调委会12.司法所
	 * isUser 0或者空不查询人员 1查询人员
	 * areaId  地区旗县或者乡镇
	 * @author 王兆林
	 * @version 2018-05-24 21:15:37
	 * @return
	 */
	public List<Map<String, Object>> getOfficeUserApi(String type, String isUser,String areaId,String townId,String officeId){
		AgencyForm agencyForm=new AgencyForm();
		agencyForm.setPageNo(1);
		agencyForm.setTownId(townId);
		agencyForm.setPageSize(5000);
    	if(StringUtils.isNotBlank(type)){
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY,type);
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		if(StringUtils.isNotBlank(areaId)&&!"5".equals(areaId)&&!"null".equals(areaId)){
			agencyForm.setAreaId(areaId);
		}
		PageVo<AgencyVo> page = apiAgencySearch.searchAgency(agencyForm);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<AgencyVo> list=page.getList();
		switch (type) {
		case "2":
			agencyForm.setCategoryId("1");
			break;
		case "5":
			agencyForm.setCategoryId("9");
			break;
		case "10":
			agencyForm.setCategoryId("11");
			break;
		case "8":
			agencyForm.setCategoryId("13");
			break;
		case "12":
			agencyForm.setCategoryId("15");
			break;
		case "4":
			agencyForm.setCategoryId("7");
			break;
		case "3":
			agencyForm.setCategoryId("6");
			break;
		default:
			break;
		}
		if(StringUtils.isNotBlank(type)){
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY,agencyForm.getCategoryId());
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		for (int i = 0; i < list.size(); i++) {
			AgencyVo agencyVo=list.get(i);
            if(StringUtils.isBlank(officeId)||"null".equals(officeId)||agencyVo.getId().equals(officeId)){
    			Map<String, Object> map = Maps.newHashMap();
    			map.put("id", agencyVo.getId());
    			map.put("pId","0");
    			map.put("pIds","0,");
    			map.put("name", agencyVo.getAgencyName());
    			map.put("isParent", true);
    			agencyForm.setOfficeId(agencyVo.getId());
    			if("1".equals(isUser)){
    				agencyForm.setOfficeId(agencyVo.getId());
    				agencyForm.setAreaId("");
    				agencyForm.setTownId("");
    				page = apiAgencySearch.searchAgency(agencyForm);
    				List<AgencyVo> ulist=page.getList();
    				List<Map<String, Object>> users = Lists.newArrayList();
    				for (int y = 0; y<ulist.size();y++ ){
    					Map<String, Object> map3 = Maps.newHashMap();
    					AgencyVo agencyVou=ulist.get(y);
    					map3.put("id", agencyVou.getId());
    					map3.put("name", agencyVou.getPersonName());
    					map3.put("pId", agencyVo.getId());
    					map3.put("pIds",map.get("pIds")+agencyVo.getId());
    					users.add(map3);
    				}
    				map.put("users", users);
    			}
    			mapList.add(map);
    		  }
			}
		return mapList;
	}
	
	/**
	 * 获取机构人员功能（接口专用）
	 * type  机构人员类型 2.律师事务所8.基层法律服务所5.法援中心10.人民调委会12.司法所
	 * isUser 0或者空不查询人员 1查询人员
	 * areaId  地区旗县或者乡镇
	 * @author 王兆林
	 * @version 2018-05-24 21:15:37
	 * @return
	 */
	public List<Map<String, Object>> getOfficeUserApi(String type, String isUser,String areaId,String townId,String officeId,String flag){
		AgencyForm agencyForm=new AgencyForm();
		agencyForm.setPageNo(1);
		agencyForm.setTownId(townId);
		agencyForm.setPageSize(5000);
    	if(StringUtils.isNotBlank(type)){
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY,type);
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		if(StringUtils.isNotBlank(areaId)&&!"5".equals(areaId)&&!"null".equals(areaId)){
			agencyForm.setAreaId(areaId);
		}
		PageVo<AgencyVo> page = apiAgencySearch.searchAgency(agencyForm);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<AgencyVo> list=page.getList();
		switch (type) {
		case "2":
			agencyForm.setCategoryId("1");
			break;
		case "5":
			agencyForm.setCategoryId("9");
			break;
		case "10":
			agencyForm.setCategoryId("11");
			break;
		case "8":
			agencyForm.setCategoryId("13");
			break;
		case "12":
			agencyForm.setCategoryId("15");
			break;
		case "4":
			agencyForm.setCategoryId("7");
			break;
		case "3":
			agencyForm.setCategoryId("6");
			break;
		default:
			break;
		}
		if(StringUtils.isNotBlank(type)){
			Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY,agencyForm.getCategoryId());
			apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    	}
		for (int i = 0; i < list.size(); i++) {
			AgencyVo agencyVo=list.get(i);
            if(StringUtils.isBlank(officeId)||"null".equals(officeId)||agencyVo.getId().equals(officeId)){
    			Map<String, Object> map = Maps.newHashMap();
    			map.put("id", agencyVo.getId());
    			map.put("pId","0");
    			map.put("pIds","0,");
    			map.put("name", agencyVo.getAgencyName());
    			map.put("isParent", true);
    			agencyForm.setOfficeId(agencyVo.getId());
    			if("1".equals(isUser)){
    				agencyForm.setOfficeId(agencyVo.getId());
    				agencyForm.setAreaId("");
    				agencyForm.setTownId("");
    				if(!"".equals(flag)){
    					agencyForm.setIsAidLawyer("1");
    				}
    				page = apiAgencySearch.searchAgency(agencyForm);
    				List<AgencyVo> ulist=page.getList();
    				List<Map<String, Object>> users = Lists.newArrayList();
    				for (int y = 0; y<ulist.size();y++ ){
    					Map<String, Object> map3 = Maps.newHashMap();
    					AgencyVo agencyVou=ulist.get(y);
    					map3.put("id", agencyVou.getId());
    					map3.put("name", agencyVou.getPersonName());
    					map3.put("pId", agencyVo.getId());
    					map3.put("pIds",map.get("pIds")+agencyVo.getId());
    					users.add(map3);
    				}
    				map.put("users", users);
    			}
    			mapList.add(map);
    		  }
			}
		return mapList;
	}
	
	/**
	 * 查询人员表分类
	 * @param loginId
	 * @return
	 */
	public List<String> userTypeid(String loginId){
		return userDao.userTypeid(loginId);
	};
	
    public static boolean checkObjFieldIsNull(Object obj) throws IllegalAccessException {
        
        boolean flag = false;
        for(Field f : obj.getClass().getDeclaredFields()){
            f.setAccessible(true);
            if(f.get(obj) == null || f.get(obj).equals("")){
                flag = true;
                return flag;
            }
        }
        return flag;
    }
    
    public String identityType(String papernum){
    	return userDao.identityType(papernum);
    }
    /**
     * 更新用户兼职表
     * @param userId 用户id
     * @param list 用户兼职list
     * @param roleidList 用户主职角色id
     * @param officeId   用户主职机构id
     * @return
     */
    private List<PartTimeJob> updatepartList(String userId,List<PartTimeJob> list,List<Role> roleidList,String officeId){
    	for (PartTimeJob partTimeJob : list) {
    		partTimeJob.setUserId(userId);
		}
        if(roleidList!=null&&roleidList.size()>0&&officeId!=null) {
            for (Role role : roleidList) {
            	PartTimeJob partTimeJob =new PartTimeJob(userId,role.getId(),officeId,"0");
            	list.add(partTimeJob);
			}        	
        }
    	return list;
    }
    /**
     * 更新用户兼职表
     * @param userId 用户id
     * @param roleidList 用户角色id
     * @param officeId   用户机构id
     * *@param isMain   在该机构下是主职还是兼职
     * @return
     */
    private List<PartTimeJob> updatepartList(String userId,List<Role> roleidList,String officeId,String isMain){
    	List<PartTimeJob> list=Lists.newArrayList();
        if(roleidList!=null&&roleidList.size()>0&&officeId!=null) {
            for (Role role : roleidList) {
            	PartTimeJob partTimeJob =new PartTimeJob(userId,role.getId(),officeId,isMain);
            	list.add(partTimeJob);
			}        	
        }
    	return list;
    }
    /**
     * 通过机构id和角色id获取用户id姓名，登录名
     * @param partTimeJob
     * @return
     */
   public  List<String>  findUserByOfficeRole(PartTimeJob  partTimeJob){
		return userRoleOfficeService.findUserByOfficeRole(partTimeJob);
	}

   /** 数据范围过滤
	 * @param user 当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param officeAlias 机构表别名，多个用“,”逗号隔开。
	 * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public  String dataScopeFilter(User user, String officeAlias, String userAlias,String lawyerId) {

		StringBuilder sqlString = new StringBuilder();
		
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		
		// 超级管理员，跳过权限过滤
		if (!user.isAdmin()){
			boolean isDataScopeAll = false;
			for (Role r : user.getRoleList()){
				String oId = userRoleOfficeService.getOfficeIdByUidAndRid(user.getId(),r.getId());
				for (String oa : StringUtils.split(officeAlias, ",")){
					if (StringUtils.isNotBlank(oa)){
						if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
							isDataScopeAll = true;
						}
						else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(r.getDataScope())){
							sqlString.append(" OR " + oa + ".id = '" + user.getCompany().getId() + "'");
							sqlString.append(" OR " + oa + ".parent_ids LIKE '" + user.getCompany().getParentIds() + user.getCompany().getId() + ",%'");
						}
						else if (Role.DATA_SCOPE_COMPANY.equals(r.getDataScope())){
							sqlString.append(" OR " + oa + ".id = '" + user.getCompany().getId() + "'");
							// 包括本部门下的科室 （type=1:部门；type=2：科室）
							sqlString.append(" OR (" + oa + ".parent_id = '" + user.getCompany().getId() + "' AND " + oa + ".type = '2')");
						}
						else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(r.getDataScope())){
							sqlString.append(" OR " + oa + ".id = '" + user.getOffice().getId() + "'");
							sqlString.append(" OR " + oa + ".parent_ids LIKE '" + user.getOffice().getParentIds() + user.getOffice().getId() + ",%'");
						}
						else if (Role.DATA_SCOPE_OFFICE.equals(r.getDataScope())){
							sqlString.append(" OR " + oa + ".id = '" + oId + "'");
						}
						else if (Role.DATA_SCOPE_CUSTOM.equals(r.getDataScope())){
//							String officeIds =  StringUtils.join(r.getOfficeIdList(), "','");
//							if (StringUtils.isNotEmpty(officeIds)){
//								sqlString.append(" OR " + oa + ".id IN ('" + officeIds + "')");
//							}
							sqlString.append(" OR EXISTS (SELECT 1 FROM sys_role_office WHERE role_id = '" + r.getId() + "'");
							sqlString.append(" AND office_id = " + oa +".id)");
						}
						//else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())){
						dataScope.add(oId);
					}
				}
			}
			// 如果没有全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
			if (!isDataScopeAll){
				if (StringUtils.isNotBlank(userAlias)){
					for (String ua : StringUtils.split(userAlias, ",")){
						sqlString.append(" OR " + ua + ".id = '" +lawyerId+ "'");
					}
				}else {
					for (String oa : StringUtils.split(officeAlias, ",")){
						//sqlString.append(" OR " + oa + ".id  = " + user.getOffice().getId());
						sqlString.append(" OR " + oa + ".id IS NULL");
					}
				}
			}else{
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}
	/** 数据范围过滤
	 * @param user 当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param officeAlias 机构表别名，多个用“,”逗号隔开。
	 * @return 标准连接条件对象
	 */
	public  String dataFilter(User user, String officeAlias) {

		StringBuilder sqlString = new StringBuilder();
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		// 超级管理员，跳过权限过滤
		if (!user.isAdmin()){
			boolean isDataScopeAll = false;
			for (Role r : user.getRoleList()){
				String oId = userRoleOfficeService.getOfficeIdByUidAndRid(user.getId(),r.getId());
				for (String oa : StringUtils.split(officeAlias, ",")){
					if (StringUtils.isNotBlank(oa)){
						if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
							isDataScopeAll = true;
						}
						else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(r.getDataScope())){
							sqlString.append(" OR " + oa + ".id = '" + user.getCompany().getId() + "'");
							sqlString.append(" OR " + oa + ".parent_ids LIKE '" + user.getCompany().getParentIds() + user.getCompany().getId() + ",%'");
						}
						else if (Role.DATA_SCOPE_COMPANY.equals(r.getDataScope())){
							sqlString.append(" OR " + oa + ".id = '" + user.getCompany().getId() + "'");
							// 包括本部门下的科室 （type=1:部门；type=2：科室）
							sqlString.append(" OR (" + oa + ".parent_id = '" + user.getCompany().getId() + "' AND " + oa + ".type = '2')");
						}
						else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(r.getDataScope())){
							sqlString.append(" OR " + oa + ".id = '" + user.getOffice().getId() + "'");
							sqlString.append(" OR " + oa + ".parent_ids LIKE '" + user.getOffice().getParentIds() + user.getOffice().getId() + ",%'");
						}
						else if (Role.DATA_SCOPE_OFFICE.equals(r.getDataScope())){
							sqlString.append(" OR " + oa + ".id = '" + oId + "'");
						}
						else if (Role.DATA_SCOPE_CUSTOM.equals(r.getDataScope())){
//							String officeIds =  StringUtils.join(r.getOfficeIdList(), "','");
//							if (StringUtils.isNotEmpty(officeIds)){
//								sqlString.append(" OR " + oa + ".id IN ('" + officeIds + "')");
//							}
							sqlString.append(" OR EXISTS (SELECT 1 FROM sys_role_office WHERE role_id = '" + r.getId() + "'");
							sqlString.append(" AND office_id = " + oa +".id)");
						}
						//else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())){
						dataScope.add(oId);
					}
				}
			}
			// 如果没有全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
			if (isDataScopeAll){
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}
	/**
	 * 根据用户信息表id或者用户表id获取用户信息
	 * @param loginName
	 * @return
	 */
	public List<UserAndInfoData> selectByUserId(String userId,String infoId) {
		List<UserAndInfoData> loginid=userDao.selectByUserId(userId,infoId);
		return loginid;	
	};
	 
	/**
	 * 对人员数据进行删除时使用
	 * @param id 不同的业务表id
	 * @param remark 类信息描述对应sys_user_office_info
	 * 
	 * @return
	 * 业务表数据在对应业务的业务逻辑层删除
	 * @author suzz
	 */
	public String deletYeWu(String id,String officeId,String remark) {
		 String loginid = userDao.selectSysUserlogin(id, remark);
		 if(loginid.length()<=0||null ==loginid) {
			 throw new BusinessException("删除失败!此业务信息异常，请联系管理员处理");
		 }
			User user = userDao.get(loginid);
		 Map<Object, String> param=new HashMap<>();
		 //查找对应机构对应用户唯一的一条，但当这个为对应机构主任的时候会是两条
		 List<UserAndInfoData>  userAndInfoData = userDao.selectUserInfor(loginid, id,officeId);
		 //为了处理之前业务逻辑不对导致的垃圾历史问题     想说草
		 if(userAndInfoData.size()<=0) {
			 throw new BusinessException("删除失败!此业务信息异常，请联系管理员处理");
		 }
		 //之所以不遍历是因为在一个机构两个职位都是主任和科员的角色不考虑在内
		   UserAndInfoData userAndInfoData1=userAndInfoData.get(0);
		//看他有没有在其他机构有兼职
		 List<UserAndInfoData> selectUserRoleOffice = userDao.selectUserRoleOffice(loginid, "", "", "1");
		//主职且没有兼职的情况取的sys_user_role_office的is_main 0是主职，1是兼职 目前这样写不稳妥，应该避免是垃圾数据误删正确数据，增加一些数据为空的效验，
		//有问题了请注意看这些数据是否找的完整
			if ("0".equals(userAndInfoData1.getIsMain())&&selectUserRoleOffice.size()<=0) {
				//dao.delete(lawyer);
				// 删除对应角色联同普通用户一并删除
				//删除所有对应角色包含普通用户权限角色
				userDao.deleteUserRole(user);
				// 根据业务数据Id删除对照关系
				userDao.delete(user);
				// 删除对照关系
				deleteUserInfoByInfoDataId(id);
				param.put("userId", loginid);
				param.put("officeId", officeId);
				//为了删除普通用户的那一条
				//param.put("roleId", userAndInfoData.getRoleId());
				param.put("isMain", userAndInfoData1.getIsMain());
				// 删除兼职对应角色
				userDao.deleteRoleOffice(param);
				// 清除用户缓存
				UserUtils.clearCache(user);
	        //删除兼职的情况 兼职时不能删除用户
			} else if ("1".equals(userAndInfoData1.getIsMain())) {
				//dao.delete(lawyer);
				// 删除对照关系
				deleteUserInfoByInfoDataId(id);
				param.put("userId", loginid);
				param.put("officeId",officeId);
				//param.put("roleId", userAndInfoData1.getRoleId());
				param.put("isMain", "1");
				// 删除兼职对应角色
				userDao.deleteRoleOffice(param);
				// 删除兼职对应的角色
				userDao.deleteUserRoleOne(loginid, userAndInfoData1.getRoleId());
				// 清除用户缓存
				UserUtils.clearCache(user);
				//数据同步到activity
				saveActivitiUser(user);
	        //删除主职下面有兼职的情况
			} else if ("0".equals(userAndInfoData1.getIsMain())&&selectUserRoleOffice.size()>=1) {
				
				UserAndInfoData serAndInfoData=selectUserRoleOffice.get(0);
				//将任意一个兼职修改为主职
				userDao.updateUserRoleOffice(serAndInfoData.getId(),serAndInfoData.getInfoDataOfficeId(), "0");
				//将用户普通用户权限对应的机构和主职保持一致
				userDao.updateOfficeIdPd(serAndInfoData.getInfoDataOfficeId(), serAndInfoData.getId());
				//修改对应表主职
				userDao.updateIsMain(serAndInfoData.getInfoDataId(), "1");
				Office office = officeService.get(serAndInfoData.getInfoDataOfficeId());
				//修改用户表的机构
				userDao.updateUserOfficeId(serAndInfoData.getId(), office.getId(), office.getParentId());
				//dao.delete(lawyer);
				// 删除对照关系
			     deleteUserInfoByInfoDataId(id);
				param.put("userId", loginid);
				param.put("officeId", officeId);
				//param.put("roleId", userAndInfoData1.getRoleId());
				param.put("isMain", "0");
				// 删除主职对应角色
				userDao.deleteRoleOffice(param);
				// 删除兼职对应的角色
				userDao.deleteUserRoleOne(loginid, userAndInfoData1.getRoleId());
				// 清除用户缓存
				UserUtils.clearCache(user);
				//数据同步到activity
				saveActivitiUser(user);
			}
		return "";
		
	}
	
	/**
	 * 删除用户时使用
	 * 删除所有的关联业务表
	 * @param user 不同的业务表id
	 * @return
	 * @author suzz
	 */
	
	@SuppressWarnings("deprecation")
	public String deletYeWuDate(User user) {
		 List<UserAndInfoData>  userAndInfoDataList = userDao.selectOfficeInfo(user.getId());
		 Map<Object, String> param=new HashMap<>();
		//循环删除对应业务表数据
		 for(UserAndInfoData userAndInfoData:userAndInfoDataList) {
			// 删除对照关系
			 userDao.deleteByInfoDataId(userAndInfoData.getInfoDataId());
			 if("judiciaryUser".equals(userAndInfoData.getRemark())) {
				 JudiciaryUser judiciaryUser = new JudiciaryUser();
				 judiciaryUser.setId(userAndInfoData.getInfoDataId());
				 judiciaryUserDao.delete(judiciaryUser);
			 }else if("lawyer".equals(userAndInfoData.getRemark())) {
				 Lawyer lawyer = new Lawyer();
				 lawyer.setId(userAndInfoData.getInfoDataId());
				 lawyerDao.delete(lawyer);
			 }else if("legal_service_person".equals(userAndInfoData.getRemark())) {
				 LegalServicePerson legalServicePerson = new LegalServicePerson();
				 legalServicePerson.setId(userAndInfoData.getInfoDataId());
				 legalServicePersonDao.delete(legalServicePerson);
			 }else if("infoForensicPersonnel".equals(userAndInfoData.getRemark())){
				 InfoForensicPersonnel infoForensicPersonnel = new InfoForensicPersonnel();
				 infoForensicPersonnel.setId(userAndInfoData.getInfoDataId());
				 infoForensicPersonnelDao.delete(infoForensicPersonnel);
			}else if("lawAssitanceUser".equals(userAndInfoData.getRemark())) {
				LawAssitanceUser lawAssitanceUser = new LawAssitanceUser();
				lawAssitanceUser.setId(userAndInfoData.getInfoDataId());
				lawAssitanceUserDao.delete(lawAssitanceUser);
			}else if("notaryMember".equals(userAndInfoData.getRemark())) {
				NotaryMember notaryMember = new NotaryMember();
				notaryMember.setId(userAndInfoData.getInfoDataId());
				notaryMemberDao.delete(notaryMember);
			}else if("peopleMediation".equals(userAndInfoData.getRemark())) {
				PeopleMediation peopleMediation = new PeopleMediation();
				peopleMediation.setId(userAndInfoData.getInfoDataId());
				peopleMediationDao.delete(peopleMediation);
			}else if("supervisor".equals(userAndInfoData.getRemark())){
				Supervisor supervisor = new Supervisor();
				supervisor.setId(userAndInfoData.getInfoDataId());
				supervisorDao.delete(supervisor);
			}
			 param.put("userId", userAndInfoData.getId());
			 userDao.deleteRoleOffice(param);
		 }
		 
		return "";
		
	}

	public String getAreaByUser(User user) {
		String areaId = userDao.getAreaByUser(user.getId());
		return areaId;
	}

	public List<String> getUserListByAreaAndType(String str, String areaId) {

		List<String> list = userDao.getUserListByAreaAndType(str,areaId);
		return list;
	}


}
