package com.thinkgem.jeesite.api.sys;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.api.dto.bo.MobileAndCode;
import com.thinkgem.jeesite.api.dto.form.*;
import com.thinkgem.jeesite.api.dto.vo.UserInfoVo;
import com.thinkgem.jeesite.api.utils.CheckIdcd;
import com.thinkgem.jeesite.common.JGpush.JGpushUtils;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.cms.entity.SysUserPush;
import com.thinkgem.jeesite.modules.cms.service.SysUserPushService;
import com.thinkgem.jeesite.modules.platform.utils.PlatformUtils;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm;
import com.thinkgem.jeesite.modules.sys.security.UsernamePasswordToken;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 登录注册相关接口
 *
 * @author kakasun
 * @create 2018-04-18 上午8:54
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/100/400")
public class ApiLoginOutController {

    @Autowired
    SystemService systemService;

    @Autowired
    OfficeService officeService;
    
    @Autowired
    SystemAuthorizingRealm systemAuthorizingRealm;
    @Autowired
    SysUserPushService sysUserPushService;
    /**
     * 获取短信验证码
     *
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse getMobleCode(BaseForm<MobileForm> form) {
        form.initQueryObj(MobileForm.class);
        StringBuffer code = new StringBuffer();
        code.append("1234");
        String str = code.toString();
        String mobile = form.getQueryObj().getMobile();
        SMSUtils.sendSMS(mobile, str);
        String smsToken = IdGen.uuid();
        SMSCodeCacheUtils.cacheKeyValue(smsToken, MobileAndCode.build(mobile, str));
        ServerResponse<String> result = ServerResponse.createBySuccess(smsToken);
        return result;
    }

    /**
     * 注册
     *
     * @return
     */
    @RequestMapping("/20")
    public ServerResponse register(HttpServletRequest request,HttpServletResponse response,BaseForm<PublicRegisterForm> form) throws ExecutionException {
        form.initQueryObj(PublicRegisterForm.class);
        PublicRegisterForm queryObj = form.getQueryObj();
        ResponseCode responseCode = null;
        if(!CheckIdcd.checkId(queryObj.getPapernum())) {
        	responseCode = ResponseCode.INVALID_PARAMETER;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), "身份证号码不合法，请检查身份证号");
        }
        if (StringUtils.isEmpty(queryObj.getPwd())) {
            responseCode = ResponseCode.INVALID_PARAMETER;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), responseCode.getDesc());
        }
        String smsToken = queryObj.getSmsToken();
        if (null == smsToken) {
            responseCode = ResponseCode.INCORRECT_SIGNATURE;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), responseCode.getDesc());
        }
        MobileAndCode mac = SMSCodeCacheUtils.getValueByKey(smsToken);
        if (null == mac) {
            responseCode = ResponseCode.CODE_INVALID;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), responseCode.getDesc());
        }
        try {
            if (null != systemService.getUserByLoginName(mac.getMobile())) {
                responseCode = ResponseCode.INVALID_PARAMETER;
                return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), "手机号已注册");
            }
        } catch (Exception e) {
            responseCode = ResponseCode.INVALID_PARAMETER;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), "手机号已注册");
        }

        String code = mac.getCode();
        if (!code.equalsIgnoreCase(queryObj.getCode())) {
            responseCode = ResponseCode.CODE_INVALID;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), responseCode.getDesc());
        }
        if(StringUtils.isNotBlank(systemService.identityType(queryObj.getPapernum()))){
        	responseCode = ResponseCode.INVALID_PARAMETER;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), "该身份证已注册过账号");
        }
        ;
       
        User u = new User();
//        u.preInsert();
        u.setName(queryObj.getName());
        u.setPapernum(queryObj.getPapernum());
        u.setLoginName(mac.getMobile());
        u.setMobile(mac.getMobile());
       // u.setPassword(SystemService.entryptPassword(queryObj.getPwd()));
       // u.setPassword(Digests.getMD5(queryObj.getPwd())+mac.getMobile());
        u.setPassword(queryObj.getPwd());
        u.setCreateBy(u);
        u.setUpdateBy(u);
        u.setIsPerfect("1");
//        u.setPhoto("/userfiles/default/userProfile_default.png");
        String companyId = OfficeRoleConstant.OFFICE_ROOT;
        Office company = officeService.get(companyId);
        u.setCompany(company);
        String officeId = OfficeRoleConstant.OFFICE_DEFAULT_NORMAL;
        Office office = officeService.get(officeId);
        u.setOffice(office);
        List<Role> roles =  Lists.newArrayList();
        roles.add(systemService.findRoleById(OfficeRoleConstant.ROLE_DEFAULT_NORMAL));
        u.setRoleList(roles);
       // u.setUserSourceType("0");
        u.setIdentityType("0");
        systemService.saveUser(u,"0");
        
        //注册之后自动登陆
        BaseForm<LoginForm> loginForm = new BaseForm<LoginForm>();
        Map<String, Object> map = Maps.newHashMap();
        map.put("username",mac.getMobile() );
        map.put("password", queryObj.getPwd());
        String string = JSON.toJSONString(map);
        loginForm.setQuery(string);
        
        return login(request,response,loginForm );
        
    }

    /**
     * 校验身份证号
     *
     * @param papernum
     * @return
     */
    private boolean checkPapernum(String papernum) {
        return true;
    }

    /**
     * 忘记密码重置密码
     *
     * @return
     */
    @RequestMapping("/30")
    public ServerResponse changePwdForget(BaseForm<ChangePwdForgetForm> form) throws ExecutionException {
        form.initQueryObj(ChangePwdForgetForm.class);
        ChangePwdForgetForm queryObj = form.getQueryObj();
        ResponseCode responseCode = null;
        if (StringUtils.isEmpty(queryObj.getPwd())) {
            responseCode = ResponseCode.INVALID_PARAMETER;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), responseCode.getDesc());
        }
        String smsToken = queryObj.getSmsToken();
        if (null == smsToken) {
            responseCode = ResponseCode.INCORRECT_SIGNATURE;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), responseCode.getDesc());
        }
        MobileAndCode mac = SMSCodeCacheUtils.getValueByKey(smsToken);
        if (null == mac) {
            responseCode = ResponseCode.CODE_INVALID;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), responseCode.getDesc());
        }
        String code = mac.getCode();
        if (!code.equalsIgnoreCase(queryObj.getCode())) {
            responseCode = ResponseCode.CODE_INVALID;
            return ServerResponse.createByErrorCodeMessage(responseCode.getCode(), responseCode.getDesc());
        }
        User u = systemService.getUserByLoginName(mac.getMobile());
        systemService.updatePasswordById(u.getId(), u.getLoginName(), queryObj.getPwd());
        return ServerResponse.createBySuccess();
    }


    /**
     * api登录接口
     *
     * @return
     */
    @RequestMapping("/40")
    public ServerResponse login(HttpServletRequest request, HttpServletResponse response,
                                BaseForm<LoginForm> form) {
        form.initQueryObj(LoginForm.class);
        String tag = request.getHeader("tag");
        UserUtils.putCache(Constants.SOURCE_DEVICE, tag);
        LoginForm queryObj = form.getQueryObj();
        User user=new User();
        user.setLoginName(queryObj.getUsername());
        user.setPassword(queryObj.getPassword());
        ResponseCode rc = systemService.getByLoginNameAndPassword(user);
        if(rc.getCode() != 0) {//登录出现错误
        	return ServerResponse.createByErrorCodeMessage(rc);
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String lang = queryObj.getLangCode();
        session.setAttribute("lang", lang);
        if (null == queryObj.getPassword()) {
            queryObj.setPassword("");
        }
        String host = StringUtils.getRemoteAddr(request);
        //加密密码
//        queryObj.setPassword(SystemService.entryptPassword(queryObj.getPassword()));
        UsernamePasswordToken upt = new UsernamePasswordToken(queryObj.getUsername(),
                queryObj.getPassword().toCharArray(), queryObj.getRememberMe(), host,
                queryObj.getValidateCode(), false);
        //登录
        subject.login(upt);
        String sessionId = session.getId().toString();
        response.setHeader(Constants.HEADER_SESSION_ID, sessionId);
        //首先清除session中的缓存(同一浏览器换账号登录)
        UserUtils.clearCache();
        User u = UserUtils.getUser();
        UserInfoVo userInfoVo= new UserInfoVo(u);
        boolean isNormal = false;//判断是否是普通用户
    	List<Role> roleList=u.getRoleList();
    	userInfoVo.setUserType("1");
    	userInfoVo.setUserTypeDesc("工作人员");
    	//'区分大众和服务人员
    	if(roleList!=null&&roleList.size()>0&&OfficeRoleConstant.ROLE_DEFAULT_NORMAL.equals(roleList.get(0).getId())){
    		isNormal = true;
    		userInfoVo.setUserType("0");
        	userInfoVo.setUserTypeDesc("普通人员");
    	}
    	if(!isNormal&&"100".equals(tag)){//提示使用工作人员登录窗口登录
    		return ServerResponse.createByErrorCodeMessage(ResponseCode.IDENTITY_ERROR_WORKER);	
    	}
        ServerResponse<UserInfoVo> result = ServerResponse.createBySuccess(userInfoVo);
        result.msg(sessionId);
        SecurityUtils.getSubject().getSession().setTimeout(-100000);
        systemService.updateUserLoginInfo(UserUtils.getUser());
        LogUtils.saveLog(Servlets.getRequest(), "接口登陆");
        return result;
    }

    /**
     * 工作人员登录窗口
     * 不返回用户信息
     * 只返回后台链接地址
     *
     * @return
     */
    @RequestMapping("/41")
    public ServerResponse loginByWorker(HttpServletRequest request, HttpServletResponse response,
                                BaseForm<LoginForm> form) {
        form.initQueryObj(LoginForm.class);
        UserUtils.putCache(Constants.SOURCE_DEVICE,form.getTag());
        LoginForm queryObj = form.getQueryObj();
        User user=new User();
        user.setLoginName(queryObj.getUsername());
        user.setPassword(queryObj.getPassword());
        ResponseCode rc = systemService.getByLoginNameAndPassword(user);
        if(rc.getCode() != 0) {//登录出现错误
        	return ServerResponse.createByErrorCodeMessage(rc);
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String lang = queryObj.getLangCode();
        session.setAttribute("lang", lang);
        if (null == queryObj.getPassword()) {
            queryObj.setPassword("");
        }
        String host = StringUtils.getRemoteAddr(request);
        //加密密码
//        queryObj.setPassword(SystemService.entryptPassword(queryObj.getPassword()));
        UsernamePasswordToken upt = new UsernamePasswordToken(queryObj.getUsername(),
                queryObj.getPassword().toCharArray(), queryObj.getRememberMe(), host,
                queryObj.getValidateCode(), false);
        //登录
        subject.login(upt);
        String sessionId = session.getId().toString();
        response.setHeader(Constants.HEADER_SESSION_ID, sessionId);
        //首先清除session中的缓存(同一浏览器换账号登录)
        UserUtils.clearCache();
        //获取用户所有的角色列表
        User u = UserUtils.getUser();
        UserInfoVo userInfoVo= new UserInfoVo(u);
        boolean isWorker = false;//判断是否是普通用户
    	List<Role> roleList=UserUtils.getUser().getRoleList();
    	//'区分大众和服务人员
    	if(roleList!=null&&roleList.size()==0&&OfficeRoleConstant.ROLE_DEFAULT_NORMAL.equals(roleList.get(0).getId())){
    		isWorker = true;
    	}
    	if(isWorker){//提示使用工作人员登录窗口登录
    		return ServerResponse.createByErrorCodeMessage(ResponseCode.IDENTITY_ERROR_NORMAL);
    	}
        //放入用户认证缓存
        systemService.getSessionDao().delete(session);
        UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO);
    	UserUtils.putCache(UserUtils.CACHE_AUTH_INFO, systemAuthorizingRealm.setUserAuthorizatioinInfo(UserUtils.getUser()));
		// 更新登录IP和时间
		systemService.updateUserLoginInfo(UserUtils.getUser());
		// 记录登录日志
		LogUtils.saveLog(Servlets.getRequest(), "系统登录");
        String workPlatform = PlatformUtils.getPrimaryKey("server_url")+"/"+PlatformUtils.getPrimaryKey("project_name");
        ServerResponse<String> result = ServerResponse.createBySuccess(workPlatform);
        result.msg(sessionId);
        SecurityUtils.getSubject().getSession().setTimeout(-100000);
        return result;
    }

    /**
     * api获取当前登录用户信息
     *
     * @return
     */
    @RequestMapping("/50")
    public ServerResponse userInfo() {
        User u = UserUtils.getUser();
    	List<Role> roleList=u.getRoleList();
    	u.setUserType("1");
    	u.setUserTypeDesc("工作人员");
    	//'区分大众和服务人员
    	if(roleList!=null&&roleList.size()>0&&OfficeRoleConstant.ROLE_DEFAULT_NORMAL.equals(roleList.get(0).getId())){
    		u.setUserType("0");
        	u.setUserTypeDesc("普通人员");
    	}
        ServerResponse<UserInfoVo> result = null;
        if(null == u|| null == u.getId()||"".equals(u.getId())){
            ResponseCode code = ResponseCode.NEED_LOGIN;
            result = ServerResponse.createByErrorCodeMessage(code.getCode(),code.getDesc());
            return result;
        }
        result = ServerResponse.createBySuccess(new UserInfoVo(u));
        return result;
    }
    /**
     * 在api绑定别名之后调用，推送通知
     * @return
     */
    @RequestMapping("/45")
    public ServerResponse sendPush(){
    	User u = UserUtils.getUser();
    	String userId = u.getId();
    	SysUserPush push = new SysUserPush();
    	push.setReceiveUserId(userId);
    	List<SysUserPush> uu = sysUserPushService.findList(push);
    	for (int i = 0; i < uu.size(); i++) {
    		JPushClient jPushClient = JGpushUtils.newJpush();
    		String id = uu.get(i).getReceiveUserId();
			PushResult result2;
			try {
				result2 = jPushClient.sendPush(JGpushUtils.buildPushObject_android_and_ios(uu.get(i).getPushMessage().getTitle(), uu.get(i).getPushMessage().getMsgContent(),id));
				int status = result2.getResponseCode();//状态，200成功
				if(status==200){
					sysUserPushService.updateStatusById(uu.get(i).getId());//推送成功后改变表中数据
				}
			} catch (APIConnectionException e) {
				e.printStackTrace();
			} catch (APIRequestException e) {
				e.printStackTrace();
			}
    	}
        return ServerResponse.createBySuccess();
    	
    }
    /**
     * 退出登录
     * @return
     */
    @RequestMapping("/60")
    public ServerResponse loginOut(){
        UserUtils.getSubject().logout();
        return ServerResponse.createBySuccess();
    }

}