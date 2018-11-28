/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service.act;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.zxing.WriterException;
import com.thinkgem.jeesite.common.utils.*;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.poi.ss.formula.functions.T;
import org.apache.xmlbeans.impl.jam.mutable.MPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.api.chart.entity.PeopleMediationVo;
import com.thinkgem.jeesite.api.chart.entity.PeopleMediatorVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
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
import com.thinkgem.jeesite.modules.info.dao.PeopleMediationDao;
import com.thinkgem.jeesite.modules.oa.dao.act.OaPeopleMediationApplyDao;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidInform;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApplyCount;
import com.thinkgem.jeesite.modules.oa.service.OaProcessStateService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 人民调解业务逻辑Service
 * @author zhangqiang
 * @version 2018-05-24
 */
@Service
@Transactional(readOnly = true)
public class OaPeopleMediationApplyService extends CrudService<OaPeopleMediationApplyDao, OaPeopleMediationApply> {
	
	/**
	 * 定义流程定义KEY，必须以“PD_”开头
	 * 组成结构：string[]{"流程标识","业务主表表名"}
	 */
	public static final String[] PD_LEGAL_AID = new String[]{"people_mediation", "oa_people_mediation_apply"};
	
	public static final String[] ROLE_FYKY = new String[]{"leader"};
	
	@Autowired
	private ActDao actDao;
	
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private PeopleMediationDao peopleMediationDao;
	@Autowired
	private OaProcessStateService oaProcessStateService;
	@Autowired
	private SysUserPushService sysUserPushService;
	public OaPeopleMediationApply get(String id) {
		return super.get(id);
	}
	
	public List<OaPeopleMediationApply> findList(OaPeopleMediationApply oaPeopleMediationApply) {
		return super.findList(oaPeopleMediationApply);
	}
	
	public Page<OaPeopleMediationApply> findPage(Page<OaPeopleMediationApply> page, OaPeopleMediationApply oaPeopleMediationApply) {
		return super.findPage(page, oaPeopleMediationApply);
	}
	/**
	 * 保存时不提交
	 */
	@Transactional(readOnly = false)
	public void save(OaPeopleMediationApply oaPeopleMediationApply) {
		String caseTitle = oaPeopleMediationApply.getCaseTitle();
		String caseTypeDesc = oaPeopleMediationApply.getCaseTypeDesc();
		String accuserName = UserUtils.getUser().getName();
		String caseCountyName = oaPeopleMediationApply.getCaseCounty().getName();
		
		if("".equals(caseTitle) && caseTypeDesc!="" && accuserName!="" && caseCountyName!="" ){
			oaPeopleMediationApply.setCaseTitle(caseCountyName+"-"+ accuserName+"-"+caseTypeDesc+"-"+DateUtils.getDateToString());
		}
		
		//保存的时候如果是司法所所长指定节点，那么需要判断是否签收，没签收则要签收
		Act act = oaPeopleMediationApply.getAct();
		if(act!=null){
			String taskDefKey = act.getTaskDefKey();
			if("mediation_zhiding".equals(taskDefKey)){
				//判断是否领取了任务
				if(!actTaskService.isClaim(oaPeopleMediationApply.getAct())){
					//领取任务
					claim(oaPeopleMediationApply.getAct());
				}
			}
		}
		
		super.save(oaPeopleMediationApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaPeopleMediationApply oaPeopleMediationApply) {
		super.delete(oaPeopleMediationApply);
	}
	/**
	 * 申请人提交启动流程
	 * @param oaPeopleMediationApply
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false)
	public ServerResponse submit(OaPeopleMediationApply oaPeopleMediationApply) {
		ServerResponse errorMsg = null;//错误信息
		
		try{
			User user=UserUtils.getUser();
			oaPeopleMediationApply.setCreateBy(user);
			checkUserRelated(oaPeopleMediationApply, user);//检查用户相关性
			// 申请发起
			Map<String, Object> vars = Maps.newHashMap();
			// 如果没有流程编号
			if (StringUtils.isBlank(oaPeopleMediationApply.getProcInsId())){
				String yearNo = getYearNo();
				oaPeopleMediationApply.setYearNo(yearNo);//指定年份序号
				oaPeopleMediationApply.setYear(DateUtils.getYear());//指定年份序号
				save(oaPeopleMediationApply);
				User userName = null;
				//获取人民调解员，放入参数中，通过判断为空来控制分支走向
				if("".equals(oaPeopleMediationApply.getMediator().getId())){
					//获取当前区域的司法所所长的name
					List<String> loginList = getSfsz(oaPeopleMediationApply);
					vars.put("sfsz",loginList);
					vars.put("tjy", null);
				}else{
					//获取当前所选的调节员
					userName = UserUtils.get(oaPeopleMediationApply.getMediator().getId());
					vars.put("tjy", userName.getLoginName());
				}
				// actTaskService.startProcess(PD_LEGAL_AID[0],PD_LEGAL_AID[1], oaPeopleMediationApply.getId(), vars);
				String procInsId = actTaskService.startProcess(PD_LEGAL_AID[0], PD_LEGAL_AID[1], oaPeopleMediationApply.getId(), oaPeopleMediationApply.getCaseTitle(), vars);
				//更新流程状态(受理中)
				oaProcessStateService.save(oaPeopleMediationApply.getId(), procInsId, oaPeopleMediationApply.getCaseTitle(), "1", "1",user,new Date(), oaPeopleMediationApply.getUpdateBy(), oaPeopleMediationApply.getUpdateDate(),"0","people_mediation",oaPeopleMediationApply.getCaseCounty().getId());
			    if(vars.get("tjy")!=null&&userName!=null&&vars.get("tjy")!=""){
			    	List<String> userIds = sysUserPushService.pushUser(null,userName.getLoginName());	
					List<List<String>> lists = JGpushUtils.averageAssign(userIds);
					for (int i = 0; i < lists.size(); i++) {
						SysUserPush push = new SysUserPush();
						push.getPushMessage().setTitle("");//如果没有则填空
						push.getPushMessage().setMsgContent("你有一个关于《案件标题:"+oaPeopleMediationApply.getCaseTitle()+"》的待办事项，请尽快处理。");
						push.setUrl("/act/task/todo/");//需要链接的地址，为controller中的地址。
					    sysUserPushService.pushNotificetion(push,lists.get(i));
					}
			    }else{
			    	List<String> userIds = sysUserPushService.pushUser(getSfsz(oaPeopleMediationApply),null);	
					List<List<String>> lists = JGpushUtils.averageAssign(userIds);
					for (int i = 0; i < lists.size(); i++) {
						SysUserPush push = new SysUserPush();
						push.getPushMessage().setTitle("");//如果没有则填空
						push.getPushMessage().setMsgContent("你有一个关于《案件标题:"+oaPeopleMediationApply.getCaseTitle()+"》的待办事项，请尽快处理。");
						push.setUrl("/act/task/todo/");//需要链接的地址，为controller中的地址。
					    sysUserPushService.pushNotificetion(push,lists.get(i));
					}
			    }
			}
		}catch(BusinessException e){
			errorMsg = ServerResponse.createByErrorCodeMessage(e.getCode(),
					e.getMessage());
		}
		return errorMsg;
	}

	@Transactional(readOnly = false)
	public String toDo(OaPeopleMediationApply oaPeopleMediationApply) {
		
		Map<String, Object> vars = Maps.newHashMap();
		String errorMsg = "";//错误信息
		// 设置意见
		// 
		try {
			oaPeopleMediationApply.getAct().setComment(("yes".equals(oaPeopleMediationApply.getAct().getFlag())?"[同意] ":"[驳回] ")+oaPeopleMediationApply.getAct().getComment());
			// 对不同环节的业务逻辑进行操作
			String taskDefKey = oaPeopleMediationApply.getAct().getTaskDefKey();
			//调解员审核
			if("mediation_shenhe".equals(taskDefKey)){
				//领取任务
				claim(oaPeopleMediationApply.getAct());
				
				vars.put("pass", "yes".equals(oaPeopleMediationApply.getAct().getFlag())? "1" : "0");
				//审批成功后跳转页面
				if ("yes".equals(oaPeopleMediationApply.getAct().getFlag())) {
					//科员同意受理后，需要自动生成字号
					oaPeopleMediationApply.setYearNo(getYearNo());
					oaPeopleMediationApply.setStatus("1");
					//同意后生成二维码
					try {
						oaPeopleMediationApply.setQrcode(QrCodeCreateUtil.createQrCode(null,oaPeopleMediationApply.getProcInsId(),900,"JPEG"));
					} catch (WriterException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					oaPeopleMediationApply.setStatus("2");
				}
			}else if("mediation_zhiding".equals(taskDefKey)){
				//司法所所长指定调解员
				//领取任务
				//判断是否领取了任务
				if(!actTaskService.isClaim(oaPeopleMediationApply.getAct())){
					//领取任务
					claim(oaPeopleMediationApply.getAct());
				}
				User userName = UserUtils.get(oaPeopleMediationApply.getMediator().getId());
				vars.put("tjy", userName.getLoginName());
				
			}else if("mediation_xiugai".equals(taskDefKey)){
				//申请人重新填写
				claim(oaPeopleMediationApply.getAct());
				//可能出现重新指定了申请人的情况
				User userName = UserUtils.get(oaPeopleMediationApply.getMediator().getId());
				vars.put("tjy", userName.getLoginName());
			}

			save(oaPeopleMediationApply);
			// 提交流程任务
			actTaskService.complete(oaPeopleMediationApply.getAct().getTaskId(), oaPeopleMediationApply.getAct().getProcInsId(), oaPeopleMediationApply.getAct().getComment(), vars);
			if("mediation_zhiding".equals(taskDefKey)){
				User userName = UserUtils.get(oaPeopleMediationApply.getMediator().getId());
				List<String> userIds = sysUserPushService.pushUser(null,userName.getLoginName());	
				List<List<String>> lists = JGpushUtils.averageAssign(userIds);
				for (int i = 0; i < lists.size(); i++) {
					SysUserPush push = new SysUserPush();
					push.getPushMessage().setTitle("");//如果没有则填空
					push.getPushMessage().setMsgContent("你有一个关于《案件标题:"+oaPeopleMediationApply.getCaseTitle()+"》的待办事项，请尽快处理。");
					push.setUrl("/act/task/todo/");//需要链接的地址，为controller中的地址。
				    sysUserPushService.pushNotificetion(push,lists.get(i));
				}
			}
			
			if("no".equals(oaPeopleMediationApply.getAct().getFlag())&&"mediation_shenhe".equals(oaPeopleMediationApply.getAct().getTaskDefKey())){
				//更新流程状态(已退回)
				oaProcessStateService.save(oaPeopleMediationApply.getAct().getProcInsId(),"3",oaPeopleMediationApply.getUpdateBy(),oaPeopleMediationApply.getUpdateDate(),oaPeopleMediationApply.getAct().getComment(),oaPeopleMediationApply.getCaseTitle(),oaPeopleMediationApply.getCaseCounty().getId(),null,null,null);
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
	public void   applyEndProcess(OaPeopleMediationApply oaPeopleMediationApply) {
		if (StringUtils.isBlank(oaPeopleMediationApply.getRemarks())) {
			throw new BusinessException("结束意见不能为空");
		}
		Task task= getCurrentTaskByProcInsId(oaPeopleMediationApply.getProcInsId());
		if(task!=null) {
			if("mediation_huifang".equals(task.getTaskDefinitionKey())||"mediation_juanzong".equals(task.getTaskDefinitionKey())||"mediation_end".equals(task.getTaskDefinitionKey())) {
				throw new BusinessException("当前任务节点不允许结束");
			}
		Map<String, Object> variables=new HashMap<>();
		actTaskService.jumpTask(oaPeopleMediationApply.getProcInsId(), "mediation_end", variables);	
		variables.put("actId", "mediation_end");
		variables.put("procInstId", oaPeopleMediationApply.getProcInsId());
		variables.put("assignee", UserUtils.getUser().getLoginName());
		String taskId=IdGen.uuid();
		variables.put("taskId", taskId);
         actDao.updateActinst(variables);
     	variables.put("id", IdGen.uuid());
     	variables.put("type", "comment");
     	variables.put("action", "AddComment");
     	variables.put("time", new Date());
     	variables.put("message", "[申请人结束任务]:"+oaPeopleMediationApply.getRemarks());
     	//动态加入一条结束意见
     	actDao.insertActhicomment(variables);
     	oaPeopleMediationApply.setUpdateBy(UserUtils.getUser());
     	dao.updateSelective(oaPeopleMediationApply);
		}else {
			throw new BusinessException("该任务已经结束");
		}
		}
	
	/**
	 * 当前任务处理人
	 * 结束任务
	 * 
	 */
	@Transactional(readOnly = false)
	public String   endProcess(OaPeopleMediationApply oaPeopleMediationApply) {
		if (StringUtils.isBlank(oaPeopleMediationApply.getAct().getComment())) {
			throw new BusinessException("结束意见不能为空");
		}
		oaPeopleMediationApply.setRemarks(oaPeopleMediationApply.getAct().getComment());
		dao.updateSelective(oaPeopleMediationApply);
		Act act=oaPeopleMediationApply.getAct();
		Task task= getCurrentTaskByProcInsId(act.getProcInsId());
		if(task!=null) {
		if("mediation_huifang".equals(task.getTaskDefinitionKey())||"mediation_juanzong".equals(task.getTaskDefinitionKey())||"mediation_end".equals(task.getTaskDefinitionKey())) {
			throw new BusinessException("当前任务节点不允许结束");
		}
		Map<String, Object> variables=new HashMap<>();
		// 处理前先签收操作等于进入个人池
	    actTaskService.claim(oaPeopleMediationApply.getAct().getTaskId(), UserUtils.getUser().getLoginName());
		taskService.addComment(act.getTaskId(),act.getProcInsId(), "[结束任务]:"+act.getComment());
		actTaskService.taskinst(act);
		actTaskService.jumpTask(oaPeopleMediationApply.getAct().getProcInsId(), "mediation_end", variables);

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
	 * 获取一个年度序号(暂定为5为序号)
	 * @author 王鹏
	 * @version 2018-05-14 09:47:11
	 * @return
	 */
	public String getYearNo() {
		return StringUtils.patchZero(dao.selectYearNo(), 5);
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
	 * [获取申请人所在地区的司法所所长]
	 * @param  oaPeopleMediationApply [description]
	 * @return                        [description]
	 */
	private List<String> getSfsz(OaPeopleMediationApply oaPeopleMediationApply) throws BusinessException{
		//获取区域id
		//通过区域id获取地区机构级别中最高的机构
		//在用户表中找该机构下的用户
		//通过用户-角色表查找用户中角色为领导的
/*		Area area = oaPeopleMediationApply.getCaseTown();
		if("".equals(area.getId())){
			area = oaPeopleMediationApply.getCaseCounty();
		}
		Office so = new Office();
		String type = "2";
		so.setArea(area);
		so.setType(type);//查询机构(此机构下有法援科员角色的即可，不强制要求是法援中心科室的)
		so.setName("司法所");
		List<Office> listOffice = officeService.findListByCondition(so);
		//再次查询
		if(listOffice==null||listOffice.size()==0) {
			type = "1";
			so.setArea(area);
			so.setType(type);//查询机构(此机构下有法援科员角色的即可，不强制要求是法援中心科室的)
			so.setName("司法");
			listOffice = officeService.findListByCondition(so);
		}*/
		Area caseCounty = oaPeopleMediationApply.getCaseCounty();
		Area caseTown = oaPeopleMediationApply.getCaseTown();
		String caseCountyId = "";
		String caseTownId = "";
		if(caseCounty!=null && !"".equals(caseTown.getId())){
			caseCountyId = caseCounty.getId();
		}
		if(caseTown!=null && !"".equals(caseTown.getId())){
			caseTownId = caseTown.getId();
		}
		List<Map<String, Object>> listOffice = systemService.getOfficeUser("12","0",caseCountyId,caseTownId,"");
		//如果当前乡镇没有对应的司法所,那么将任务交给所有的
		if(listOffice==null||listOffice.size()==0) {
			listOffice = systemService.getOfficeUser("12","0",caseCountyId,"","");
		}
		if(listOffice==null||listOffice.size()==0) {
			throw new BusinessException(ResponseCode.ERROR.getCode(),"当前区域没有对应的司法所！");
		}
		List<String> list=Lists.newArrayList();
		PartTimeJob  partTimeJob=null;
		Map<String, Object> map = null;
		for(int i = 0 ; i< listOffice.size(); i++){
			map = listOffice.get(i);
			String officeId = "";
			if(map != null){
				officeId = (String) map.get("id");
			}
			partTimeJob=new PartTimeJob();
			partTimeJob.setOfficeId(officeId);
			partTimeJob.setRoleId(OfficeRoleConstant.ROLE_JUDICIARY_MANAGER);
			list.addAll(systemService.findUserByOfficeRole(partTimeJob));
		}
		/*for (Office office : listOffice) {
			partTimeJob=new PartTimeJob();
			partTimeJob.setOfficeId(office.getId());
			partTimeJob.setRoleId(OfficeRoleConstant.ROLE_JUDICIARY_MANAGER);
			list.addAll(systemService.findUserByOfficeRole(partTimeJob));
		}*/
		if(list==null || list.size()==0){
			throw new BusinessException(ResponseCode.ERROR.getCode(),"当前区域没有对应的司法所所长！");
		}
		return list;
		/*User su = new User();//获取科员列表
		//查询机构
		if("2".equals(type)){
			su.setOffice(listOffice.get(0));
		}else if("1".equals(type)){
			su.setCompany(listOffice.get(0));
		}
		su.setOffice(listOffice.get(0));
		
		String roleEn = ROLE_FYKY[0];
		
		//查询角色
		su.setRole(systemService.getRoleByEnname(roleEn));
		//获取登录名(英文逗号分隔)
		return getLoginNamesByUserList(su);*/
	}

	/**
	 * 根据相应条件获得所有用户的登录名
	 * @author 张强
	 * @version 
	 * @param list
	 * @return
	 *//*
	public List<String> getLoginNamesByUserList(User searchUser) throws BusinessException{
		
		List<String> loginNames = null;
		//根据机构和角色获取人员列表
		List<User> userList = systemService.findUserByCondition(searchUser);
		for (int i = 0; i < userList.size(); i++) {
			if( i == 0){
				loginNames = Lists.newArrayList();
			}
			loginNames.add(userList.get(i).getLoginName());
		}
		if(loginNames==null) {
			throw new BusinessException(ResponseCode.MEDIATION_NOT_LEADER.getCode(),ResponseCode.MEDIATION_NOT_LEADER.getDesc());
		}
		return loginNames;
	}*/
	
/*	public String getLoginNameByMediatorId(String id){
		String loginName = "";
		loginName = peopleMediationDao.findUserLoginNameById(id);
		return loginName;
	}
	*/
	/**
	 * 根据年份区域统计
	 * 年度旗县调解案件占比
	 * @author 王鹏
	 * @version 2018-6-3 10:39:42
	 * @param opmac
	 * @return
	 */
	public List<OaPeopleMediationApplyCount> countByYearArea(OaPeopleMediationApplyCount opmac){
		if(opmac == null) {
			throw new BusinessException("参数不全");
		}
		return dao.countByYearArea(opmac);
	}

	/**
	 * 全盟各旗县人民调解案件申请数量占比图
	 * 各旗县人民调解案件数量占比
	 * 优化返回数据，减少字段数量
	 * @author 王鹏
	 * @version 2018-6-8 16:57:49
	 * @param opmac
	 * @return
	 */
	public Map<String, Object> countAreaPeopleMediationForBigScreen(OaPeopleMediationApplyCount opmac){
		if(opmac == null || StringUtils.isBlank(opmac.getBeginDate())
				|| StringUtils.isBlank(opmac.getEndDate())) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, Object>> dataList = Lists.newArrayList();
		Map<String, Object> map = null;
		Integer total = 0;//总数
		List<OaPeopleMediationApplyCount> list = dao.countAreaPeopleMediation(opmac);
		for (int i = 0; i < list.size(); i++) {
			map = Maps.newHashMap(); 
			map.put("name", list.get(i).getArea().getName());
			map.put("value", list.get(i).getCount());
			total += list.get(i).getCount();
			dataList.add(map);
		}
		resultMap.put("total", total);
		resultMap.put("data", dataList);
		return resultMap;
	}

	/**
	 * 大屏统计查询方法
	 * 全盟人民调解案件申请数量与受理数量走势对比图
	 * 优化返回数据，减少字段数量
	 * @author 王鹏
	 * @version 2018-6-9 11:16:12
	 * @param opmac
	 * @return
	 */
	public Map<String, Object> countPeopleMediationAidAcceptCompared(OaPeopleMediationApplyCount opmac){
		if(opmac == null || StringUtils.isBlank(opmac.getBeginDate())
				|| StringUtils.isBlank(opmac.getEndDate())) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		//申请数量
		List<OaPeopleMediationApplyCount> aidList = dao.countPeopleMediationAidAccept(opmac);
		//受理数量
		opmac.setIsAccept("1");//统计受理数量
		List<OaPeopleMediationApplyCount> acceptList = dao.countPeopleMediationAidAccept(opmac);
		List<String> xData = Lists.newArrayList();//横坐标数据
		//获取两个日期间的所有数据(年、年月、年月日)
		xData = DateUtils.getAllDayBetweenDate(opmac.getBeginDate(), opmac.getEndDate());
		resultMap.put("xData", xData);//存入横坐标
		//纵坐标数据
		List<Map<String, Object>> yData = Lists.newArrayList();
		Map<String, Object> aidMap = Maps.newHashMap();//申请集合
		aidMap.put("name", "人民调解案件申请数量");
		aidMap.put("type", "line");//折线图
		Map<String, Object> acceptMap = Maps.newHashMap();//受理集合
		acceptMap.put("name", "人民调解案件受理数量");
		acceptMap.put("type", "line");//折线图
		yData.add(aidMap);//存入申请
		yData.add(acceptMap);//存入受理;
		resultMap.put("yData", yData);//存入纵坐标(数据)
		Integer[] aidArr = new Integer[xData.size()];
		Integer[] acceptArr = new Integer[xData.size()];
		for (int i = 0; i < xData.size(); i++) {
			aidArr[i] = 0;//放一个默认值
			acceptArr[i] = 0;//放一个默认值
			//放入申请数量
			for (int j = 0; j < aidList.size(); j++) {
				if(xData.get(i).equals(aidList.get(j).getYear())) {
					//如果年度相符，就放入数量
					aidArr[i] = aidList.get(j).getCount();
					break;
				}
			}
			//放入受理数量
			for (int j = 0; j < acceptList.size(); j++) {
				if(xData.get(i).equals(acceptList.get(j).getYear())) {
					//如果年度相符，就放入数量
					acceptArr[i] = acceptList.get(j).getCount();
					break;
				}
			}
		}
		aidMap.put("data", Arrays.asList(aidArr));//放入相应数据
		acceptMap.put("data", Arrays.asList(acceptArr));//放入受理数据
		return resultMap;
	}

	/**
	 * 大屏统计查询方法
	 * 全盟及各旗县人民调解案件数量
	 * 优化返回数据，减少字段数量
	 * @author 王鹏
	 * @version 2018-6-9 11:16:12
	 * @param opmac
	 * @return
	 */
	public Map<String, Object> countPeopleMediationAccept(OaPeopleMediationApplyCount opmac){
		if(opmac == null || StringUtils.isBlank(opmac.getBeginDate())
				|| StringUtils.isBlank(opmac.getEndDate())) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		//受理数量
		opmac.setIsAccept("1");//统计受理数量
		List<OaPeopleMediationApplyCount> acceptList = dao.countPeopleMediationAidAccept(opmac);
		List<String> xData = Lists.newArrayList();//横坐标数据
		//获取两个日期间的所有数据(年、年月、年月日)
		xData = DateUtils.getAllDayBetweenDate(opmac.getBeginDate(), opmac.getEndDate());
		resultMap.put("xData", xData);//存入横坐标
		//纵坐标数据
		List<Map<String, Object>> yData = Lists.newArrayList();
		Map<String, Object> acceptMap = Maps.newHashMap();//受理集合
		acceptMap.put("name", "全盟及各旗县人民调解案件数量");
		acceptMap.put("type", "line");//折线图
		yData.add(acceptMap);//存入受理;
		resultMap.put("yData", yData);//存入纵坐标(数据)
		Integer[] acceptArr = new Integer[xData.size()];
		for (int i = 0; i < xData.size(); i++) {
			acceptArr[i] = 0;//放一个默认值
			//放入受理数量
			for (int j = 0; j < acceptList.size(); j++) {
				if(xData.get(i).equals(acceptList.get(j).getYear())) {
					//如果年度相符，就放入数量
					acceptArr[i] = acceptList.get(j).getCount();
					break;
				}
			}
		}
		acceptMap.put("data", Arrays.asList(acceptArr));//放入受理数据
		return resultMap;
	}
	
	/**
	 * 校验当前用户是否和申请有关(必须是申请人或者代理人)
	 * @author 王鹏
	 * @version 2018-05-24 20:52:26
	 * @param oaLegalAid
	 * @param user
	 */
	private void checkUserRelated(OaPeopleMediationApply oaPeopleMediationApply, User user) throws BusinessException {
		boolean userRelated = false;//校验当前用户是否和申请有关(必须是申请人或者代理人)
		if(user.getName().equals(oaPeopleMediationApply.getAccuserName())) {
			userRelated = true;
		}
		if(!userRelated) {//用户与此次援助申请没有关系，不能继续此流程
			throw new BusinessException(ResponseCode.MEDIATION_ID_PROVE.getCode(),ResponseCode.MEDIATION_ID_PROVE.getDesc());
		}
	}


	/**
	 * 接口: 查询草稿箱
	 * @author 张强
	 * @version 
	 * @param oaLegalAid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageVo<OaPeopleMediationApply> findDraftPageforApi(Integer pageNo, Integer pageSize, String caseTitle) {
		// TODO Auto-generated method stub
		OaPeopleMediationApply oaPeopleMediationApply = new OaPeopleMediationApply();
		Page<OaPeopleMediationApply> page = new Page<OaPeopleMediationApply>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		oaPeopleMediationApply.setCreateBy(UserUtils.getUser());
		oaPeopleMediationApply.setCaseTitle(caseTitle);
		oaPeopleMediationApply.setPage(page);
		//根据登录人和案件标题查询草稿
		page.setList(dao.findDraftListForApi(oaPeopleMediationApply));
		return new PageVo<OaPeopleMediationApply>(page, true);
	}

	/**
	 * 删除草稿，支持批量
	 * @author 张强
	 * @version 
	 * @param draftId
	 */
	@Transactional(readOnly = false)
	public void deleteDraft(String[] draftId) {
		dao.deleteDraft(draftId);
	}
	
	/**
	 * 接口: 查询草稿箱
	 * @author 张强
	 * @version 
	 * @param oaLegalAid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageVo<Act> findDraft(int pageNo, int pageSize, String beginDate, String endDate, String caseTitle) {
		// TODO Auto-generated method stub
		Page page = new Page<Act>();
		
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		
		User user = UserUtils.getUser();
		List<Act> list = dao.findDraft(page,beginDate,endDate,caseTitle,user);
		page.setList(list);
		page.setCount(list!=null?list.size():0);
		return new PageVo<Act>(page, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageVo<OaPeopleMediationApply> findDraftMediationById(String id) {
		
		Page page = new Page<OaPeopleMediationApply>();
		page.setList(dao.findDraftMediationById(id));
		return new PageVo<OaPeopleMediationApply>(page, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageVo<OaLegalAid> findDraftLegalById(String id) {
		// TODO Auto-generated method stub
		Page page = new Page<OaLegalAid>();
		page.setList(dao.findDraftLegalById(id));
		return new PageVo<OaLegalAid>(page, true);
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageVo<OaLegalAidInform> findDraftdefenseById(String id) {
		// TODO Auto-generated method stub
		Page page = new Page<OaLegalAid>();
		page.setList(dao.findDraftdefenseById(id));
		return new PageVo<OaLegalAidInform>(page, true);
	}
	/**
	 * 接口:大屏查询人民调解案件
	 * @author 王鹏
	 * @version 2018-6-13 11:48:58
	 * @param pmv
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageVo<PeopleMediationVo> findPageforApi(PeopleMediationVo pmv) {
		// TODO Auto-generated method stub
		List<PeopleMediationVo> list = dao.findListForBigScreen(pmv);
		int count = dao.findListCountForBigScreen(pmv);
		pmv.getPage().setList(list);
		pmv.getPage().setCount(count);
		return new PageVo<PeopleMediationVo>(pmv.getPage(), true);
	}
	
	/**
	 * 更新是否评价
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void isEvaluate(String id){
		dao.isEvaluate(id);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<T> getApi(String id) {
		return dao.getApi(id);
	}
	
	public Map<String, Object> countPeopleMediationAccept1(OaPeopleMediationApplyCount opmac){
		if(opmac == null || StringUtils.isBlank(opmac.getBeginDate())
				|| StringUtils.isBlank(opmac.getEndDate())) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		//受理数量
		opmac.setIsAccept("1");//统计受理数量
		List<String> xData = Lists.newArrayList();//横坐标数据
		//获取两个日期间的所有数据(年、年月、年月日)
		xData = DateUtils.getAllDayBetweenDate(opmac.getBeginDate(), opmac.getEndDate());
		
		StringBuffer dsf = new StringBuffer();
		for(int i = 0 ; i < xData.size(); i++){
			if(i==xData.size()-1){
				dsf.append("SELECT '"+xData.get(i)+"'");
			}else{
				dsf.append("SELECT '"+xData.get(i)+"' YEAR UNION ALL ");
			}
		}
		opmac.getSqlMap().put("dsf", dsf.toString());
		
		List<OaPeopleMediationApplyCount> acceptList = dao.countPeopleMediationAidAccept1(opmac);
		resultMap.put("xData", xData);//存入横坐标
		//纵坐标数据
		List<Map<String, Object>> yData = Lists.newArrayList();
		Map<String, Object> acceptMap = Maps.newHashMap();//受理集合
		

		int size = xData.size();
		int index = 0 ;
		Integer[] acceptArr = null;
		
		for (int i = 0 ; i<acceptList.size();i++){
			if(i==0 || index ==0){
				acceptMap = Maps.newHashMap();//受理集合
				acceptMap.put("name", acceptList.get(i).getArea().getName()+"人民调解案件受理数量");
				acceptMap.put("type", "line");
				acceptArr = new Integer[xData.size()];
			}
			acceptArr[index] = acceptList.get(i).getCount();
			if(index==size-1){
				acceptMap.put("data", Arrays.asList(acceptArr));
				yData.add(acceptMap);//存入受理;
				index=0;
			}else{
				index++;
			}
		}
		resultMap.put("yData", yData);//存入纵坐标(数据)
		return resultMap;
	}
	
	
	/**
	 * 获取人民调解和法律援助的所有地区的人民调解案件和法律援助案件
	 * @return
	 */
	public Map<String, Object> countLeaglAndMediation(){
		
		Map<String, Object> resultMap = Maps.newHashMap();
		List<OaPeopleMediationApplyCount> acceptList = dao.countLeaglAndMediation();
		//纵坐标数据
		List<Map<String, Object>> yData = Lists.newArrayList();
		Map<String, Object> acceptMap = Maps.newHashMap();//受理集合
		
		int size = 2;
		int index = 0 ;
		Integer[] acceptArr = null;
	
		int countAid = 0;
		int countMediation = 0 ;
		for (int i = 0 ; i<acceptList.size();i++){
			///统计全盟总数
			if("people_mediation".equals(acceptList.get(i).getProcDefKey())){
				countMediation+=acceptList.get(i).getCount();
			}else if("legal_aid".equals(acceptList.get(i).getProcDefKey())){
				countAid+=acceptList.get(i).getCount();
			}
			
			if(i==0 || index ==0){
				acceptMap = Maps.newHashMap();//受理集合
				acceptMap.put("name", acceptList.get(i).getArea().getName()+"案件受理数量");
				acceptMap.put("type", "bar");
				acceptArr = new Integer[size];
			}
			acceptArr[index] = acceptList.get(i).getCount();
			if(index==size-1){
				acceptMap.put("data", Arrays.asList(acceptArr));
				yData.add(acceptMap);//存入受理;
				index=0;
			}else{
				index++;
			}
		}
		//总数加入ydata
		acceptMap = Maps.newHashMap();//受理集合
		acceptArr = new Integer[size];
		acceptMap.put("name", "全盟案件受理数量");
		acceptMap.put("type", "bar");
		acceptArr[0] = countAid;
		acceptArr[1] = countMediation;	
		acceptMap.put("data", Arrays.asList(acceptArr));	
		yData.add(acceptMap);//存入受理;
		
		resultMap.put("yData", yData);//存入纵坐标(数据)
		return resultMap;
	}

	public Map<String, Object> countLeaglAndMediation1() {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = Maps.newHashMap();
		List<OaPeopleMediationApplyCount> acceptList = dao.countLeaglAndMediation();
		//纵坐标数据
		List<Integer> aidList = new ArrayList<Integer>();
		List<Integer> mediationList = new ArrayList<Integer>();
		List<Integer> countList = new ArrayList<Integer>();
		
		int countAid = 0;
		int countMediation = 0 ;
		for (int i = 0 ; i<acceptList.size();i++){
			///统计全盟总数
			if("people_mediation".equals(acceptList.get(i).getProcDefKey())){
				mediationList.add(acceptList.get(i).getCount());
				countMediation+=acceptList.get(i).getCount();
			}else if("legal_aid".equals(acceptList.get(i).getProcDefKey())){
				countAid+=acceptList.get(i).getCount();
				aidList.add(acceptList.get(i).getCount());
			}
		}
		countList.add(countAid);
		countList.add(countMediation);
		
		resultMap.put("aidList", aidList);//存入纵坐标(数据)
		resultMap.put("mediationList", mediationList);//存入纵坐标(数据)
		resultMap.put("countList", countList);//存入纵坐标(数据)
		Map<String,Object> map = Maps.newHashMap();
		map.put("map",resultMap);
		return map;
	}

	public Map<String, Object> countLegalAndMediationByAreaId(PeopleMediatorVo peopleMediatorVo) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = Maps.newHashMap();
		List<OaPeopleMediationApplyCount> acceptList = dao.countLegalAndMediationByAreaId(peopleMediatorVo);
		List<String> xData = Lists.newArrayList();
		List<Integer> yData = Lists.newArrayList();
		
		for(int i = 0 ; i < acceptList.size(); i ++){
			String procDefKey = acceptList.get(i).getProcDefKey();
			if("people_mediation".equals(procDefKey)){
				xData.add("人民调解");
				yData.add(acceptList.get(i).getCount());
			}
			if("legal_aid".equals(procDefKey)){
				xData.add("法律援助");
				yData.add(acceptList.get(i).getCount());
			}
		}
		resultMap.put("xData",xData );
		resultMap.put("yData",yData );
		resultMap.put("areaName",acceptList.get(0).getArea().getName() );
		return resultMap;
	}
}