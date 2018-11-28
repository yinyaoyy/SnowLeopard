/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service.act;


import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.WriterException;
import com.thinkgem.jeesite.common.utils.*;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.JGpush.JGpushUtils;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.act.dao.ActDao;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.cms.entity.SysUserPush;
import com.thinkgem.jeesite.modules.cms.service.SysUserPushService;
import com.thinkgem.jeesite.modules.info.service.InfoForensicPersonnelService;
import com.thinkgem.jeesite.modules.info.service.InfoJudicialAuthenticationService;
import com.thinkgem.jeesite.modules.info.service.LawAssistanceService;
import com.thinkgem.jeesite.modules.info.service.LawAssitanceUserService;
import com.thinkgem.jeesite.modules.info.service.LawyerService;
import com.thinkgem.jeesite.modules.info.service.LowOfficeService;
import com.thinkgem.jeesite.modules.info.service.NotaryAgencyService;
import com.thinkgem.jeesite.modules.info.service.NotaryMemberService;
import com.thinkgem.jeesite.modules.info.service.PeopleMediationCommitteeService;
import com.thinkgem.jeesite.modules.info.service.PeopleMediationService;
import com.thinkgem.jeesite.modules.oa.dao.act.OaFastLegalDao;
import com.thinkgem.jeesite.modules.oa.entity.act.OaFastLegal;
import com.thinkgem.jeesite.modules.oa.service.OaProcessStateService;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 法律服务直通车Service
 * @author zq
 * @version 2018-07-07
 */
@Service
@Transactional(readOnly = true)
public class OaFastLegalService extends CrudService<OaFastLegalDao, OaFastLegal> {
	
	
	public static final String[] PD_FAST_LEGAL = new String[]{"fast_legal", "oa_fast_legal"};
	
	public static final String[] ROLE_FYKY = new String[]{"12348personnel"};
	
	@Autowired
	private ActDao actDao;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SysUserPushService sysUserPushService;
	/*@Autowired
	private PeopleMediationCommitteeService peopleMediationCommitteeService;
	@Autowired
	private PeopleMediationService peopleMediationService;
	@Autowired
	private LawAssistanceService lawAssistanceService;
	@Autowired
	private LawAssitanceUserService lawAssitanceUserService;
	@Autowired
	private LowOfficeService lowOfficeService;
	@Autowired
	private LawyerService lawyerService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private InfoJudicialAuthenticationService infoJudicialAuthenticationService;
	@Autowired
	private InfoForensicPersonnelService infoForensicPersonnelService; 
	@Autowired
	private NotaryAgencyService notaryAgencyService;
	@Autowired
	private NotaryMemberService notaryMemberService;*/
	@Autowired
	private OaProcessStateService oaProcessStateService;
	@Autowired
	private AreaService areaService;
	
	public OaFastLegal get(String id) {
		
		OaFastLegal oaFastLegal = super.get(id);
		User acceptMan = oaFastLegal.getAcceptMan();
		if((acceptMan==null||"".equals(acceptMan.getId()))){
			User user = UserUtils.getUser();
			if(user!=null){
				oaFastLegal.setAcceptMan(user);
				oaFastLegal.setAcceptManCode(user.getNo());
			}
		}
		String idCard = oaFastLegal.getAccuserIdCard();
		String accuserSex = oaFastLegal.getAccuserSex();
		Date accuserBirthday = oaFastLegal.getAccuserBirthday();
		if(idCard!=null && idCard!="" && !"".equals(idCard)){
			if(accuserSex==null || accuserSex=="" || "".equals(accuserSex)){
				if(Integer.parseInt(idCard.substring(16, 17))%2!=0){
					oaFastLegal.setAccuserSex("1");
				}else{
					oaFastLegal.setAccuserSex("2");
				}
			}
			if(accuserBirthday==null){
				try {
					String birthday = idCard.substring(6, 10)+"-"+idCard.substring(10, 12)+"-"+idCard.substring(12, 14);
					
					oaFastLegal.setAccuserBirthday(DateUtils.parseDate(birthday, "yy-MM-dd"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return oaFastLegal;
	}
	
	public List<OaFastLegal> findList(OaFastLegal oaFastLegal) {
		return super.findList(oaFastLegal);
	}
	
	public Page<OaFastLegal> findPage(Page<OaFastLegal> page, OaFastLegal oaFastLegal) {
		return super.findPage(page, oaFastLegal);
	}
	
	/**
	 * 当12348工作人员点击保存的时候，签收任务，通知用户工作人员已经受理你自己的任务，等待工作人员将任务下发至业务部门领导，当工作人员再次点击保存的时候，判断是否已经签收，是，只保存，否，重复上一步
	 */
	@Transactional(readOnly = false)
	public void save(OaFastLegal oaFastLegal) {
		
		Act act = oaFastLegal.getAct();
		if(act!=null){
			String taskDefKey = act.getTaskDefKey();
			if("fast_shouli".equals(taskDefKey)){
				//判断是否领取了任务
				if(!actTaskService.isClaim(oaFastLegal.getAct())){
					//领取任务
					claim(oaFastLegal.getAct());
				}
				User acceptMan = oaFastLegal.getAcceptMan();
				if(acceptMan==null || "".equals(acceptMan.getId()) || null == acceptMan.getId()){
					oaFastLegal.setAcceptMan(UserUtils.getUser());
					oaFastLegal.setAcceptManCode(UserUtils.getUser().getNo());
				}
			}
		}
		
		
		super.save(oaFastLegal);
	}
	
	/**
	 * 社会公众填写登记表
	 * @param oaFastLegal
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false)
	public ServerResponse submit(OaFastLegal oaFastLegal) {
		ServerResponse errorMsg = null;//错误信息
		
		User user = new User();
		try{
			user.setPhone(oaFastLegal.getAccuserPhone());
			oaFastLegal.setCreateBy(user);
			// 申请发起
			Map<String, Object> vars = Maps.newHashMap();
			
			if (StringUtils.isBlank(oaFastLegal.getId())){
				//自动生成案件登记编号
				String code = createCode();
				oaFastLegal.setCaseAcceptCode(code);
				
				//自动生成标题
				createTitle(oaFastLegal);
				
				//提交给盟12348所有工作人员受理
				List<String> list = findGZRYList();
				vars.put("gzry",list);
				save(oaFastLegal);
				String procInsId = actTaskService.startProcess(PD_FAST_LEGAL[0], PD_FAST_LEGAL[1], oaFastLegal.getId(), oaFastLegal.getCaseTitle(), vars);
				//更新流程状态(受理中)
				oaProcessStateService.save(oaFastLegal.getId(), procInsId, oaFastLegal.getCaseTitle(), "2", "1", oaFastLegal.getCreateBy(), oaFastLegal.getCreateDate(), oaFastLegal.getUpdateBy(), oaFastLegal.getUpdateDate(),"0",oaFastLegal.getCaseClassify(),oaFastLegal.getCaseCounty().getId());
				List<String> userIds = sysUserPushService.pushUser(list,null);	
				List<List<String>> lists = JGpushUtils.averageAssign(userIds);
				for (int i = 0; i < lists.size(); i++) {
					SysUserPush push = new SysUserPush();
					push.getPushMessage().setTitle("");//如果没有则填空
					push.getPushMessage().setMsgContent("你有一个关于《案件标题:"+oaFastLegal.getCaseTitle()+"》的待办事项，请尽快处理。");
					push.setUrl("/act/task/todo/");//需要链接的地址，为controller中的地址。
				    sysUserPushService.pushNotificetion(push,lists.get(i));
				}
			}
		}catch(BusinessException e){
			errorMsg = ServerResponse.createByErrorCodeMessage(e.getCode(),
					e.getMessage());
		}
		return errorMsg;
	}
	
	private void createTitle(OaFastLegal oaFastLegal) {
		// TODO Auto-generated method stub
		if(oaFastLegal.getCaseTitle()==null||"".equals(oaFastLegal.getCaseTitle())){
			String caseCountyName = "";
			Area caseCounty = areaService.get(oaFastLegal.getCaseCounty().getId());
			if (caseCounty!=null) {
				caseCountyName = caseCounty.getName();
			}
			oaFastLegal.setCaseTitle(caseCountyName+oaFastLegal.getAccuserName()+"-"+oaFastLegal.getCaseClassifyDesc()+"-"+oaFastLegal.getCaseTypeDesc()+"-"+DateUtils.getDateToString());
		}
	}

	/**
	 * 当工作人员点击保存的时候，签收任务，通知用户工作人员已经受理你自己的任务，等待工作人员将任务下发至业务部门领导，当工作人员再次点击保存的时候，判断是否已经签收，是，只保存，否，重复上一步
	 * 当工作人员点击提交的时候，给用户发信息通知已下发至业务部门领导
	 * 当工作人员直接点击提交的时候，签收任务，通知用户工作人员已受理，任务已下发至业务部门
	 * 当领导已提交，通知用户已分配承办人
	 * 
	 * 当承办人点击保存的时候，签收任务，通知用户承办人已经受理，承办人提交，任务已完成
	 * 
	 * @param oaFastLegal
	 * @return
	 */
	@Transactional(readOnly = false)
	public String toDo(OaFastLegal oaFastLegal) {
		
		Map<String, Object> vars = Maps.newHashMap();
		String errorMsg = "";//错误信息
		// 设置意见
		// 
		try {
			// 对不同环节的业务逻辑进行操作
			String taskDefKey = oaFastLegal.getAct().getTaskDefKey();
			//调解员审核
			if("fast_shouli".equals(taskDefKey)){
				
				//判断是否领取了任务
				if(!actTaskService.isClaim(oaFastLegal.getAct())){
					//领取任务
					claim(oaFastLegal.getAct());
				}
				vars.put("pass", "yes".equals(oaFastLegal.getAct().getFlag())? "1" : "0");
				if ("yes".equals(oaFastLegal.getAct().getFlag())) {
					oaFastLegal.setStatus("1");
					List<String> list = findLinder(oaFastLegal);
					vars.put("leader",list);    //获取业务部门领导
					//同意之后生成二维码
					try {
						oaFastLegal.setQrcode(QrCodeCreateUtil.createQrCode(null,oaFastLegal.getProcInsId(),900,"JPEG"));
					} catch (WriterException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					vars.put("leader", findLinder(oaFastLegal));    //获取业务部门领导
				}else{
					oaFastLegal.setStatus("4");
				}
			}else if("fast_zhiding".equals(taskDefKey)){
				claim(oaFastLegal.getAct());
				String transactor = oaFastLegal.getTransactor().getId();
				User user = UserUtils.get(transactor);
				if(user!=null){
					vars.put("cbr", user.getLoginName());    //指定承办人
				}
				oaFastLegal.setStatus("2");
			}else if("fast_banli".equals(taskDefKey)){
				claim(oaFastLegal.getAct());
				oaFastLegal.setStatus("3");
			}
			save(oaFastLegal);
			// 提交流程任务
			actTaskService.complete(oaFastLegal.getAct().getTaskId(), oaFastLegal.getAct().getProcInsId(), oaFastLegal.getAct().getComment(), vars);
			if("fast_shouli".equals(taskDefKey)){
				List<String> list = findLinder(oaFastLegal);
				List<String> userIds = sysUserPushService.pushUser(list,null);	
				List<List<String>> lists = JGpushUtils.averageAssign(userIds);
				for (int i = 0; i < lists.size(); i++) {
					SysUserPush push = new SysUserPush();
					push.getPushMessage().setTitle("");//如果没有则填空
					push.getPushMessage().setMsgContent("你有一个关于《案件标题:"+oaFastLegal.getCaseTitle()+"》的待办事项，请尽快处理。");
					push.setUrl("/act/task/todo/");//需要链接的地址，为controller中的地址。
				    sysUserPushService.pushNotificetion(push,lists.get(i));
				}
			}
			if("fast_zhiding".equals(taskDefKey)){
				String transactor = oaFastLegal.getTransactor().getId();
				User user = UserUtils.get(transactor);
				List<String> userIds = sysUserPushService.pushUser(null,user.getLoginName());	
				List<List<String>> lists = JGpushUtils.averageAssign(userIds);
				for (int i = 0; i < lists.size(); i++) {
					SysUserPush push = new SysUserPush();
					push.getPushMessage().setTitle("");//如果没有则填空
					push.getPushMessage().setMsgContent("你有一个关于《案件标题:"+oaFastLegal.getCaseTitle()+"》的待办事项，请尽快处理。");
					push.setUrl("/act/task/todo/");//需要链接的地址，为controller中的地址。
				    sysUserPushService.pushNotificetion(push,lists.get(i));
				}
			}
			if("yes".equals(oaFastLegal.getAct().getFlag())&&"fast_shouli".equals(taskDefKey)){
				//更新流程状态(办理中)
				oaProcessStateService.save(oaFastLegal.getAct().getProcInsId(),"4",oaFastLegal.getUpdateBy(),oaFastLegal.getUpdateDate(),null,oaFastLegal.getCaseTitle(),oaFastLegal.getCaseCounty().getId(),new Date(),"",oaFastLegal.getCaseRank());
			}else if("fast_zhiding".equals(taskDefKey)){
				//更新流程状态(已办结)
				oaProcessStateService.save(oaFastLegal.getAct().getProcInsId(),"4",oaFastLegal.getUpdateBy(),oaFastLegal.getUpdateDate(),null,null,null,null,oaFastLegal.getTransactor().getId(),null);
			}else if("fast_banli".equals(taskDefKey)){
				//更新流程状态(已办结)
				oaProcessStateService.save(oaFastLegal.getAct().getProcInsId(),"2",oaFastLegal.getUpdateBy(),oaFastLegal.getUpdateDate(),null,null,null,null,null,null);
			}
		}catch(BusinessException e){
			errorMsg = e.getMessage();
		}
		
		return errorMsg;
	}
	
	
	/**
	 * 申请人结束任务
	 * 
	 */
	@Transactional(readOnly = false)
	public void   applyEndProcess(OaFastLegal oaFastLegal) {
		if (StringUtils.isBlank(oaFastLegal.getRemarks())) {
			throw new BusinessException("结束意见不能为空");
		}
		Task task= getCurrentTaskByProcInsId(oaFastLegal.getProcInsId());
		if(task!=null) {
		Map<String, Object> variables=new HashMap<>();
		actTaskService.jumpTask(oaFastLegal.getProcInsId(), "fast_end", variables);	
		variables.put("actId", "fast_end");
		variables.put("procInstId", oaFastLegal.getProcInsId());
		variables.put("assignee", UserUtils.getUser().getLoginName());
		String taskId=IdGen.uuid();
		variables.put("taskId", taskId);
         actDao.updateActinst(variables);
     	variables.put("id", IdGen.uuid());
     	variables.put("type", "comment");
     	variables.put("action", "AddComment");
     	variables.put("time", new Date());
     	variables.put("message", "[申请人结束任务]:"+oaFastLegal.getRemarks());
     	//动态加入一条结束意见
     	actDao.insertActhicomment(variables);
		}else {
			throw new BusinessException("该任务已经结束");
		}
		}
	
	/**
	 * 当前任务处理人
	 * 结束任务
	 * 快速办理暂时不在业务标准增加
	 * 结束原因
	 */
	@Transactional(readOnly = false)
	public String   endProcess(OaFastLegal oaFastLegal) {
		if (StringUtils.isBlank(oaFastLegal.getAct().getComment())) {
			throw new BusinessException("结束意见不能为空");
		}
		Act act=oaFastLegal.getAct();
		Task task= getCurrentTaskByProcInsId(act.getProcInsId());
		if(task!=null) {
		Map<String, Object> variables=new HashMap<>();
		// 处理前先签收操作等于进入个人池
	    actTaskService.claim(oaFastLegal.getAct().getTaskId(), UserUtils.getUser().getLoginName());
		taskService.addComment(act.getTaskId(),act.getProcInsId(), "[结束任务]:"+act.getComment());
		actTaskService.taskinst(act);
		actTaskService.jumpTask(oaFastLegal.getAct().getProcInsId(), "fast_end", variables);	

		}else {
			throw new BusinessException("该任务已经结束");
		}
		return "";
		}
	
	
	/**
	 * 获取任务
	 * @param taskId 任务ID
	 */
	public Task getCurrentTaskByProcInsId(String procInsId){
		List<Task> list = taskService.createTaskQuery().processInstanceId(procInsId).active().list();
		
		if(list==null || list.size()==0) {
			return null;
		}
		else {
			return list.get(0);
		}
	}
	
	
	/**
	 * 签收任务
	 * @param act
	 */
	private void claim(Act act){
		String userId = UserUtils.getUser().getLoginName();
		actTaskService.claim(act.getTaskId(), userId);
	}
	/**
	 * 自动生成案件登记编号
	 * 生成规则
	 * 年+月+每月的流水号，例如：2018070001
	 * @return
	 */
	private String createCode() {
		String result = "";
		int number = 0;
		synchronized(this){
			number = (int) (Math.random()*9000+1000);
		}
		result = DateUtils.getDateToString()+number;
		return result;
	}

	@Transactional(readOnly = false)
	public void delete(OaFastLegal oaFastLegal) {
		super.delete(oaFastLegal);
	}
	
	/**
	 * 查找角色为盟12348工作人员的人员列表
	 * @return
	 */
	private List<String> findGZRYList(){
		
		PartTimeJob partTimeJob=new PartTimeJob();
		//partTimeJob.setOfficeId(oaLegalAid.getLegalOffice().getId());
		partTimeJob.setRoleId(systemService.getRoleByEnname( ROLE_FYKY[0]).getId());
		return systemService.findUserByOfficeRole(partTimeJob);
		
		/*
		User user = new User();
		String roleEn = ROLE_FYKY[0];
		user.setRole(systemService.getRoleByEnname(roleEn));
		List<User> userList = systemService.findUserByCondition(user);
		List<String> loginNames = null;
		for (int i = 0; i < userList.size(); i++) {
			if( i == 0){
				loginNames = Lists.newArrayList();
			}
			loginNames.add(userList.get(i).getLoginName());
		}
		if(loginNames==null) {
//			throw new BusinessException(ResponseCode.MEDIATION_NOT_LEADER.getCode(),ResponseCode.MEDIATION_NOT_LEADER.getDesc());
		}
		return loginNames;*/
	}
	
	/**
	 * 查找该机构下的领导
	 * @param office
	 * @return
	 */
	private List<String> findLinder(OaFastLegal oaFastLegal) throws BusinessException{
		//根据不同的机构id 去不同的表中找不同角色的人
		Office office = oaFastLegal.getOffice();
		String caseClassify = oaFastLegal.getCaseClassify();
		List<String> loginName =Lists.newArrayList();
		if(caseClassify!=null && office!=null && !"".equals(oaFastLegal.getOffice().getId())){
			//String jigouid =  userDao.selectInfoIdByLoginId(office.getId());
			PartTimeJob partTimeJob=new PartTimeJob();
			partTimeJob.setOfficeId(office.getId());
			if("people_mediation".equals(caseClassify)){
				partTimeJob.setRoleId(OfficeRoleConstant.ROLE_PROPLE_MANAGER_MEDIATION);
			}else if("legal_aid".equals(caseClassify)){
				partTimeJob.setRoleId(OfficeRoleConstant.ROLE_LAW_ASSITANCE_MANAGER_USER);//法援
			}else if("apply_lawyer".equals(caseClassify)){//律师
				partTimeJob.setRoleId(OfficeRoleConstant.ROLE_LAWYER_ADMIN);
			}else if("apply_appraise".equals(caseClassify)){//鉴定
				partTimeJob.setRoleId(OfficeRoleConstant.ROLE_INFORENSIC_MANAGER_PERSONNEL);
			}else if("apply_notarization".equals(caseClassify)){//公正
				partTimeJob.setRoleId(OfficeRoleConstant.ROLE_NOTARY_MEMBER_MANAGER_USER);
			}
			loginName=systemService.findUserByOfficeRole(partTimeJob);
			if(loginName.size()==0){
				throw new BusinessException("该机构中未找到该负责人信息！！！");
			}
			//Office officeLogin = systemService.getSysUserOfficeInfo(infoId,"2");
			//loginName = UserUtils.get(officeLogin.getId()).getLoginName();
		}
		return loginName;
	}
	
	/**
	 * 
	 * @param commentId
	 */
	@Transactional(readOnly = false)
	public void isEvaluate(String id) {
		// TODO Auto-generated method stub
		dao.isEvaluate(id);
	}

	public List<T> getApi(String id) {
		// TODO Auto-generated method stub
		return dao.getApi(id);
	}
	
	
}