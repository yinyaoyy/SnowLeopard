/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.JGpush.JGpushUtils;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.cms.entity.SysUserPush;
import com.thinkgem.jeesite.modules.cms.service.SysUserPushService;
import com.thinkgem.jeesite.modules.oa.dao.OaAgreementDao;
import com.thinkgem.jeesite.modules.oa.entity.OaAgreement;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 三定方案Service
 * @author liujiangling
 * @version 2018-06-22
 */
@Service
@Transactional(readOnly = true)
public class OaAgreementService extends CrudService<OaAgreementDao, OaAgreement> {
	
	/**
	 * 定义流程定义KEY，必须以“PD_”开头
	 * 组成结构：string[]{"流程标识","业务主表表名"}
	 */
	public static final String[] PD_AGREEMENT = new String[]{"oa_agreement", "oa_agreement"};
	/**
	 * 政治部科员
	 */
	public static final String[] ROLE_FYKY = new String[]{"aaaaa", "qxaaaaa"};
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeDao officedao;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private OaProcessStateService oaProcessStateService;
	@Autowired
	private SysUserPushService sysUserPushService;
	public OaAgreement get(String id) {
		return super.get(id);
	}
	
	public List<OaAgreement> findList(OaAgreement oaAgreement) {
		return super.findList(oaAgreement);
	}
	
	public Page<OaAgreement> findPage(Page<OaAgreement> page, OaAgreement oaAgreement,Page<OaAgreement> apage) {
		Page<OaAgreement> pages = super.findPage(page, oaAgreement);
		List<OaAgreement> list = pages.getList();
		List<OaAgreement> lists = new ArrayList<OaAgreement>();		
		for(int i=0;i<list.size();i++){
			OaAgreement agreement = list.get(i);
			//查询三定方案状态
			List<HistoricActivityInstance> hlist = historyService.createHistoricActivityInstanceQuery().processInstanceId(agreement.getProcInsId())
					.orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().list();
			for(int j=hlist.size()-2;j<hlist.size();j++){
				if(!hlist.get(j).getActivityName().equals("审核是否通过")){
					agreement.setActname(hlist.get(j).getActivityName());
				}
			}
			lists.add(agreement);
		}
		apage.setCount(list.size());
		apage.setList(lists);
		return apage;
	}
	
	@Transactional(readOnly = false)
	public void save(OaAgreement oaAgreement) {
		User user=UserUtils.getUser();
		oaAgreement.setCreateBy(user);
		
		// 申请发起
		Map<String, Object> vars = Maps.newHashMap();
		if (StringUtils.isBlank(oaAgreement.getId())){
			oaAgreement.preInsert();
			OaAgreement agreement = getUser(oaAgreement);
			dao.insert(agreement);
			//获取科员处理
			List<String> list = getFykyUserList(oaAgreement);
			vars.put("aaaaa",list);
			// 启动流程
			String procInsId = actTaskService.startProcess(PD_AGREEMENT[0], PD_AGREEMENT[1], oaAgreement.getId(), oaAgreement.getTitle(), vars);
			//更新流程状态(受理中)
			oaProcessStateService.save(oaAgreement.getId(), procInsId, oaAgreement.getTitle(), "3", "1", oaAgreement.getCreateBy(), oaAgreement.getCreateDate(), oaAgreement.getUpdateBy(), oaAgreement.getUpdateDate(),"0","oa_agreement",oaAgreement.getArea().getId());
			List<String> userIds = sysUserPushService.pushUser(list,null);	
			List<List<String>> lists = JGpushUtils.averageAssign(userIds);
			for (int i = 0; i < lists.size(); i++) {
				SysUserPush push = new SysUserPush();
				push.getPushMessage().setTitle("");//如果没有则填空
				push.getPushMessage().setMsgContent("你有一个关于《案件标题:"+oaAgreement.getTitle()+"》的待办事项，请尽快处理。");
				push.setUrl("/act/task/todo/");//需要链接的地址，为controller中的地址。
			    sysUserPushService.pushNotificetion(push,lists.get(i));
			}
		}
		else{// 重新编辑申请
			oaAgreement.preUpdate();
			if("yes".equals(oaAgreement.getAct().getFlag())){
				oaAgreement.getAct().setComment("[重申] "+oaAgreement.getAct().getComment());
				vars.put("pass","1");
				//更新流程状态(受理中)
				oaProcessStateService.save(oaAgreement.getAct().getProcInsId(),"1",oaAgreement.getUpdateBy(),oaAgreement.getUpdateDate(),oaAgreement.getAct().getComment(),oaAgreement.getTitle(),oaAgreement.getArea().getId(),null,null,null);
			}else{
				oaAgreement.getAct().setComment("[删除] "+oaAgreement.getAct().getComment());
				vars.put("pass","0");
				//更新流程状态(作废)
				oaProcessStateService.save(oaAgreement.getAct().getProcInsId(),"5",oaAgreement.getUpdateBy(),oaAgreement.getUpdateDate(),oaAgreement.getAct().getComment(),oaAgreement.getTitle(),oaAgreement.getArea().getId(),null,null,null);
			}
			OaAgreement agreement = getUser(oaAgreement);
			dao.update(agreement);
			actTaskService.complete(oaAgreement.getAct().getTaskId(), oaAgreement.getAct().getProcInsId(), oaAgreement.getAct().getComment(),oaAgreement.getTitle(), vars);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(OaAgreement oaAgreement) {
		super.delete(oaAgreement);
	}
	
	@Transactional(readOnly = false)
	public String toDo(OaAgreement oaAgreement) {
		String errorMsg = "";//错误信息
		Map<String, Object> vars = Maps.newHashMap();
		String taskDefKey = oaAgreement.getAct().getTaskDefKey();
		if("no".equals(oaAgreement.getAct().getFlag()) && StringUtils.isBlank(oaAgreement.getAct().getComment())) {
			//如果是退回，则必须填写退回意见
			errorMsg = "未填写退回意见";
			return errorMsg;
		}
		//处理前先签收操作
		actTaskService.claim(oaAgreement.getAct().getTaskId(), UserUtils.getUser().getLoginName());
		// 设置意见
		oaAgreement.getAct().setComment(("yes".equals(oaAgreement.getAct().getFlag())?"[通过] ":"[退回] ")+oaAgreement.getAct().getComment());
		// 对不同环节的业务逻辑进行操作
		try {
			if("agreement_approve".equals(taskDefKey)){
				if("yes".equals(oaAgreement.getAct().getFlag())) {
					//科员同意受理后，需要自动生成字号
					OaAgreement agreement = getUser(oaAgreement);
					dao.update(agreement);
				}
				vars.put("pass", "yes".equals(oaAgreement.getAct().getFlag())? "1" : "0");
			}else{
				vars.put("pass", "yes".equals(oaAgreement.getAct().getFlag())? "1" : "0");
			}
		} catch (BusinessException e) {
			// TODO: handle exception
			errorMsg = e.getMessage();
		}
		if(StringUtils.isBlank(errorMsg)) {// 提交流程任务
			actTaskService.complete(oaAgreement.getAct().getTaskId(), oaAgreement.getAct().getProcInsId(), oaAgreement.getAct().getComment(), vars);
		}
		if("no".equals(oaAgreement.getAct().getFlag()) && "agreement_approve".equals(taskDefKey)){
			//更新流程状态(已退回)
			oaProcessStateService.save(oaAgreement.getAct().getProcInsId(),"3",oaAgreement.getUpdateBy(),oaAgreement.getUpdateDate(),oaAgreement.getAct().getComment(),null,null,null,null,null);
		}else{
			//更新流程状态(已办结)
			oaProcessStateService.save(oaAgreement.getAct().getProcInsId(),"2",oaAgreement.getUpdateBy(),oaAgreement.getUpdateDate(),oaAgreement.getAct().getComment(),null,null,null,null,null);
		}
		return errorMsg;
	}
	
	/**获取机构、地区信息
	 * 
	 */
	public OaAgreement getUser(OaAgreement oaAgreement){
		User user = userDao.get(oaAgreement.getCreateBy().getId());
		oaAgreement.setOffice(user.getOffice());
		oaAgreement.setArea(user.getArea());
		return oaAgreement;
	}
	
	/**
	 * 根据区域角色等信息获取相应的政治部人员列表
	 * 住:只要登录名集合就可
	 * @param oaAgreement
	 * @return
	 */
	public List<String> getFykyUserList(OaAgreement oaAgreement){
		PartTimeJob  partTimeJob=new   PartTimeJob();
		partTimeJob.setOfficeId(OfficeRoleConstant.OFFICE_XIZHB_SERVICE);
		return systemService.findUserByOfficeRole(partTimeJob);
	}
}