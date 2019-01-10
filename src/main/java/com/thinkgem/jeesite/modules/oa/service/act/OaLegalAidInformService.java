/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service.act;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidInform;
import com.thinkgem.jeesite.modules.oa.service.OaProcessStateService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.act.dao.ActDao;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.dao.act.OaLegalAidInformDao;

/**
 * 法援通知辩护Service
 * @author suzz
 * @version 2018-07-11
 */
@Service
@Transactional(readOnly = true)
public class OaLegalAidInformService extends CrudService<OaLegalAidInformDao, OaLegalAidInform> implements JavaDelegate {

	

	/**
	 * 定义流程定义KEY，必须以“PD_”开头
	 * 组成结构：string[]{"流程标识","业务主表表名"}
	 */
	public static final String[] PD_LEGALIFROM_AID = new String[]{"notification_defense", "oa_legal_aid_inform"};
	/**
	 * 法援科员于旗县法援科员的角色识别码
	 */
	public static final String[] ROLE_FYKY = new String[]{"fyky", "qxfyky"};
	
	/**
	 * 法援科员于旗县法援科员的角色识别码
	 */
	public static final String[] ROLE_FYZRKY = new String[]{"fygly", "fyky"};
	/**
	 * 律师主任和律师的角色识别码
	 */
	public static final String[] ROLE_LAWYER = new String[]{"lawOfficeAdmin", "lawyer"};
	/**
	 * 基层法律服务所主任和律师的角色识别码
	 */
	public static final String[] ROLE_LEGAL_SERVICE = new String[]{"legalServiceAdmin", "legalServicePerson"};
	
	@Autowired
	private ActDao actDao;
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OaProcessStateService oaProcessStateService;
	@Autowired
	private OfficeService officeService;
	public OaLegalAidInform get(String id) {
		return super.get(id);
	}
	
	public List<OaLegalAidInform> findList(OaLegalAidInform oaLegalAidInform) {
		return super.findList(oaLegalAidInform);
	}
	
	public Page<OaLegalAidInform> findPage(Page<OaLegalAidInform> page, OaLegalAidInform oaLegalAidInform) {
		return super.findPage(page, oaLegalAidInform);
	}
	
	@Transactional(readOnly = false)
	public void save(OaLegalAidInform oaLegalAidInform) {
		User user=UserUtils.getUser();
		oaLegalAidInform.setCreateBy(user);
		if(StringUtils.isBlank(oaLegalAidInform.getSource())) {
			oaLegalAidInform.setSource("100");//默认网页申请
		}
		Map<String, Object> vars = Maps.newHashMap();
		if (StringUtils.isBlank(oaLegalAidInform.getId())){

			if(StringUtils.isNotBlank(oaLegalAidInform.getLegalOffice().getId())) {
				//根据填写人指定的法援机构找到对应的机构下的法援工作人员
				vars.put("fyky", getFykUserList(oaLegalAidInform));
				oaLegalAidInform.preInsert();
				dao.insert(oaLegalAidInform);
				// 启动流程
				String procInsId = actTaskService.startProcess(PD_LEGALIFROM_AID[0], PD_LEGALIFROM_AID[1], oaLegalAidInform.getId(), oaLegalAidInform.getCaseTitle(), vars);
				//更新流程状态(受理中)
				String areaId = "";
				Office office = officeService.get(oaLegalAidInform.getLegalOffice().getId());
				if(office != null && office.getArea()!=null && office.getArea().getId()!=""){
					areaId = office.getArea().getId();
				}
				oaProcessStateService.save(oaLegalAidInform.getId(), procInsId, oaLegalAidInform.getCaseTitle(), "3", "1", oaLegalAidInform.getCreateBy(), oaLegalAidInform.getCreateDate(), oaLegalAidInform.getUpdateBy(), oaLegalAidInform.getUpdateDate(),"0","notification_defense",areaId);

			}else {
				throw new BusinessException("未指认法援机构");}
			}
		else{// 重新编辑申请
			oaLegalAidInform.preUpdate();
			if("yes".equals(oaLegalAidInform.getAct().getFlag())){
				oaLegalAidInform.getAct().setComment("[重申] "+oaLegalAidInform.getAct().getComment());
				vars.put("pass","1");
				//更新流程状态(受理中)
				oaProcessStateService.save(oaLegalAidInform.getAct().getProcInsId(),"1",oaLegalAidInform.getUpdateBy(),oaLegalAidInform.getUpdateDate(),oaLegalAidInform.getAct().getComment(),oaLegalAidInform.getCaseTitle(),null,null,null,null);
			}else{
				oaLegalAidInform.getAct().setComment("[销毁] "+oaLegalAidInform.getAct().getComment());
				vars.put("pass",  "0");
				//更新流程状态(作废)
				oaProcessStateService.save(oaLegalAidInform.getAct().getProcInsId(),"5",oaLegalAidInform.getUpdateBy(),oaLegalAidInform.getUpdateDate(),oaLegalAidInform.getAct().getComment(),null,null,null,null,null);
			}
			dao.update(oaLegalAidInform);
			actTaskService.complete(oaLegalAidInform.getAct().getTaskId(), oaLegalAidInform.getAct().getProcInsId(), oaLegalAidInform.getAct().getComment(),oaLegalAidInform.getCaseTitle(), vars);
		}
	}

	@Transactional(readOnly = false)
	public String toDo(OaLegalAidInform oaLegalAidInform) {
		String errorMsg = "";// 错误信息
		Map<String, Object> vars = Maps.newHashMap();
		String taskDefKey = oaLegalAidInform.getAct().getTaskDefKey();
		if("end".equals(oaLegalAidInform.getRemarks())) {
			endProcess(oaLegalAidInform);
		}else {
		if ("no".equals(oaLegalAidInform.getAct().getFlag())
				&& StringUtils.isBlank(oaLegalAidInform.getAct().getComment())) {
			// 如果是驳回，则必须填写驳回意见
			errorMsg = "未填写驳回意见";
			return errorMsg;
		}
		// 处理前先签收操作等于进入个人池，其他同级人员不能再处理该结果
		actTaskService.claim(oaLegalAidInform.getAct().getTaskId(), UserUtils.getUser().getLoginName());
		// 设置意见
		oaLegalAidInform.getAct().setComment(("yes".equals(oaLegalAidInform.getAct().getFlag()) ? "[同意] " : "[驳回] ")
				+ oaLegalAidInform.getAct().getComment());

		// 对不同环节的业务逻辑进行操作
		// 法援人员审查
		if ("defense_approve".equals(taskDefKey)) {
			if ("yes".equals(oaLegalAidInform.getAct().getFlag())) {
				if (StringUtils.isBlank(oaLegalAidInform.getAct().getComment())) {
					errorMsg = "未填写意见";
					return errorMsg;
				}
				dao.updateSelective(oaLegalAidInform);
				// 根据法援中心机构的id获取对应的法援主任集合
				vars.put("fyzhuren", getFyUserList(oaLegalAidInform));
			}
			vars.put("pass", "yes".equals(oaLegalAidInform.getAct().getFlag()) ? "1" : "0");
			// 法援主任审批
		} else if ("defense_fyzhuren".equals(taskDefKey)) {
			if (StringUtils.isBlank(oaLegalAidInform.getAct().getComment())) {
				errorMsg = "未填写意见";
				return errorMsg;
			}
			dao.updateSelective(oaLegalAidInform);
			vars.put("pass", "yes".equals(oaLegalAidInform.getAct().getFlag()) ? "1" : "0");
			if ("yes".equals(oaLegalAidInform.getAct().getFlag())) {
				if (StringUtils.isBlank(oaLegalAidInform.getLawOffice().getId())) {
					throw new BusinessException("未指认律师事务所");
				}
				// 获取指定律师事务所主任
				vars.put("lszhuren", getLawyerUserList(oaLegalAidInform));
			} 
		}
			else if ("defense_lszhuren".equals(taskDefKey)) {
				dao.updateSelective(oaLegalAidInform);
				vars.put("pass", "yes".equals(oaLegalAidInform.getAct().getFlag()) ? "1" : "0");
				if (StringUtils.isBlank(oaLegalAidInform.getLawyer().getId())) {
					throw new BusinessException("请指定一个律师");
				}

				vars.put("fyky", getFykUserList(oaLegalAidInform));
			}
			
			// 法援中心确认
		 else if ("defense_confirm".equals(taskDefKey)) {
			dao.updateSelective(oaLegalAidInform);
			vars.put("pass", "yes".equals(oaLegalAidInform.getAct().getFlag()) ? "1" : "0");
			if ("yes".equals(oaLegalAidInform.getAct().getFlag())) {
				if (StringUtils.isNotBlank(oaLegalAidInform.getLawyer().getId())) {
					vars.put("chengbanren", systemService.getUser(oaLegalAidInform.getLawyer().getId()).getLoginName());
				}
			}
		} else if ("defense_chengbanren_banli".equals(taskDefKey)) {
			dao.updateSelective(oaLegalAidInform);
			vars.put("pass", "yes".equals(oaLegalAidInform.getAct().getFlag()) ? "1" : "0");
		} else if ("defense_pingjia".equals(taskDefKey)) {
			if (StringUtils.isBlank(oaLegalAidInform.getThirdPartyScore())
					|| StringUtils.isBlank(oaLegalAidInform.getThirdPartyEvaluation())) {
				throw new BusinessException("未填写第三方评分或第三方评价");
			}
			// 第三方参与评价
			dao.updateSelective(oaLegalAidInform);
			// 传入申请id
			vars.put("aid_id", oaLegalAidInform.getId());
		} else if ("defense_chengbanren_butie".equals(taskDefKey)) {
			if (StringUtils.isBlank(oaLegalAidInform.getReceiveSubsidy())) {
				throw new BusinessException("未填写补贴信息");
			}
			// 承办人填写申请补贴信息
			dao.updateSelective(oaLegalAidInform);
			// 传入申请id
			vars.put("aid_id", oaLegalAidInform.getId());
		}
		if (!"0".equals(oaLegalAidInform.getIsSubmit())) {
			actTaskService.complete(oaLegalAidInform.getAct().getTaskId(), oaLegalAidInform.getAct().getProcInsId(),
					oaLegalAidInform.getAct().getComment(), vars);
		}

		   if("no".equals(oaLegalAidInform.getAct().getFlag()) && "defense_approve".equals(taskDefKey)){
				//更新流程状态(已退回)
				oaProcessStateService.save(oaLegalAidInform.getAct().getProcInsId(),"3",oaLegalAidInform.getUpdateBy(),oaLegalAidInform.getUpdateDate(),oaLegalAidInform.getAct().getComment(),oaLegalAidInform.getCaseTitle(),null,null,null,null);
			}else if("yes".equals(oaLegalAidInform.getAct().getFlag()) && "defense_approve".equals(taskDefKey)){
				//更新流程状态(办理中)
				oaProcessStateService.save(oaLegalAidInform.getAct().getProcInsId(),"4",oaLegalAidInform.getUpdateBy(),oaLegalAidInform.getUpdateDate(),oaLegalAidInform.getAct().getComment(),oaLegalAidInform.getCaseTitle(),null,new Date(),null,"");
				oaProcessStateService.save(oaLegalAidInform.getAct().getProcInsId(),"4",oaLegalAidInform.getUpdateBy(),oaLegalAidInform.getUpdateDate(),oaLegalAidInform.getAct().getComment(),oaLegalAidInform.getCaseTitle(),null,new Date(),null,null);

			}else if("defense_chengbanren_butie".equals(taskDefKey)){
				//更新流程状态(已办结)
				oaProcessStateService.save(oaLegalAidInform.getAct().getProcInsId(),"2",oaLegalAidInform.getUpdateBy(),oaLegalAidInform.getUpdateDate(),oaLegalAidInform.getAct().getComment(),oaLegalAidInform.getCaseTitle(),null,null,null,null);
			}
		}
		   return errorMsg;
	}
	@Transactional(readOnly = false)
	public void delete(OaLegalAidInform oaLegalAidInform) {
		super.delete(oaLegalAidInform);
	}

	/**
	 * 当前工作人结束任务
	 * 
	 */
	@Transactional(readOnly = false)
	public String   endProcess(OaLegalAidInform oaLegalAidInform) {
		String errorMsg="";
		oaLegalAidInform.setRemarks(oaLegalAidInform.getAct().getComment());
		if (StringUtils.isBlank(oaLegalAidInform.getAct().getComment())) {
			throw new BusinessException("结束意见为空");
			
		}
		dao.updateSelective(oaLegalAidInform);
		Act act=oaLegalAidInform.getAct();
		Task task= getCurrentTaskByProcInsId(act.getProcInsId());
		if(task!=null) {
		if("defense_pingjia".equals(task.getTaskDefinitionKey())||"defense_chengbanren_butie".equals(task.getTaskDefinitionKey())||"defense_end".equals(task.getTaskDefinitionKey())) {
			throw new BusinessException("当前任务节点不允许结束");
		}
		Map<String, Object> variables=new HashMap<>();
		// 处理前先签收操作等于进入个人池
	   actTaskService.claim(oaLegalAidInform.getAct().getTaskId(), UserUtils.getUser().getLoginName());
		taskService.addComment(act.getTaskId(),act.getProcInsId(), "[结束任务]:"+act.getComment());
		actTaskService.taskinst(act);
		actTaskService.jumpTask(oaLegalAidInform.getAct().getProcInsId(), "defense_end", variables);	

		}else {
			throw new BusinessException("该任务已经结束");
		}
		return errorMsg;
		}
	
	
	/**
	 * 申请人结束任务
	 * 
	 */
	@Transactional(readOnly = false)
	public String   applyEndProcess(OaLegalAidInform oaLegalAidInform) {
		String errorMsg="";
		if (StringUtils.isBlank(oaLegalAidInform.getRemarks())) {
			throw new BusinessException("结束意见不能为空");
		}
		Task task= getCurrentTaskByProcInsId(oaLegalAidInform.getProcInsId());
		if(task!=null) {
			if("defense_pingjia".equals(task.getTaskDefinitionKey())||"defense_chengbanren_butie".equals(task.getTaskDefinitionKey())||"defense_end".equals(task.getTaskDefinitionKey())) {
				throw new BusinessException("当前任务节点不允许结束");
			}
		Map<String, Object> variables=new HashMap<>();
		actTaskService.jumpTask(oaLegalAidInform.getProcInsId(), "defense_end", variables);	
		variables.put("actId", "defense_end");
		variables.put("procInstId", oaLegalAidInform.getProcInsId());
		variables.put("assignee", UserUtils.getUser().getLoginName());
		String taskId=IdGen.uuid();
		variables.put("taskId", taskId);
         actDao.updateActinst(variables);
     	variables.put("id", IdGen.uuid());
     	variables.put("type", "comment");
     	variables.put("action", "AddComment");
     	variables.put("time", new Date());
     	variables.put("message", "[申请人结束任务]:"+oaLegalAidInform.getRemarks());
     	//动态加入一条结束意见
     	actDao.insertActhicomment(variables);
     	oaLegalAidInform.setUpdateBy(UserUtils.getUser());
     	dao.updateSelective(oaLegalAidInform);
		}else {
			throw new BusinessException("该任务已经结束");
		}
		return errorMsg;
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
	 * 根据区域角色等信息获取相应的法援*主任
	 * 住:只要登录名集合就可，英文逗号分隔
	 * @author 如初
	 * @version 2018-07-07 17:19:30
	 * @param oaLegalAid
	 * @return
	 */
	public List<String> getFyUserList(OaLegalAidInform oaLegalAidInform){
		//User su = new User();//获取科员主任角色列表
		//查询机构
		//su.setOffice(new Office(oaLegalAidInform.getLegalOffice().getId()));
		//查询角色
		//su.setRole(systemService.getRoleByEnname(ROLE_FYZRKY[0]));
		List<String> loginNames = Lists.newArrayList();
		//获取登录名(英文逗号分隔)
		PartTimeJob partTimeJob=new PartTimeJob();
		partTimeJob.setOfficeId(oaLegalAidInform.getLegalOffice().getId());
		partTimeJob.setRoleId(systemService.getRoleByEnname(ROLE_FYZRKY[0]).getId());
		loginNames=systemService.findUserByOfficeRole(partTimeJob);
		if(loginNames.size()==0) {
			throw new BusinessException("未找到对应机构的法援中心主任,请核实");
		}
		return loginNames;
		//return getLoginNamesByUserList(su);
	}
	
	/**
	 * 根据区域角色等信息获取相应的法援科员
	 * 住:只要登录名集合就可，英文逗号分隔
	 * @author 如初
	 * @version 2018-07-07 17:19:30
	 * @param oaLegalAid
	 * @return
	 */
	public List<String> getFykUserList(OaLegalAidInform aLegalAidInform){
		List<String> loginNames = Lists.newArrayList();
		List<String> loginNames1 = Lists.newArrayList();
		PartTimeJob partTimeJob=new PartTimeJob();
		partTimeJob.setOfficeId(aLegalAidInform.getLegalOffice().getId());
		
		//User su = new User();//获取法援科员角色列表
		//查询机构
		//su.setOffice(new Office(partTimeJob.getOfficeId()));
		//查询角色
		//su.setRole(systemService.getRoleByEnname(ROLE_FYKY[1]));
		partTimeJob.setRoleId(systemService.getRoleByEnname(ROLE_FYKY[1]).getId());
		//loginNames= getLoginNamesUserList(su);
		loginNames=systemService.findUserByOfficeRole(partTimeJob);
		//su.setRole(systemService.getRoleByEnname(ROLE_FYKY[0]));
		partTimeJob.setRoleId(systemService.getRoleByEnname(ROLE_FYKY[0]).getId());
		loginNames1=systemService.findUserByOfficeRole(partTimeJob);
		//loginNames1=getLoginNamesUserList(su);
		if(loginNames1.size()!=0) {
		//loginNames.addAll(getLoginNamesUserList(su));
			loginNames.addAll(loginNames1);
		}
		if(loginNames.size()==0) {
			throw new BusinessException("没有找到任务接收人。");
		}
		//获取登录名(英文逗号分隔)
		return loginNames;
	}
	
	
	/**
	 * 根据相应机构和获得所有用户的登录名集合
	 * @author 如初
	 * @version 2018-05-17 17:27:06
	 * @param list
	 * @return
	 */
	public List<String> getLoginNamesUserList(User searchUser) {
		List<String> loginNames = Lists.newArrayList();
		//根据机构和角色获取人员列表
		List<User> userList = systemService.getAllList(searchUser);
		for (int i = 0; i < userList.size(); i++) {
			loginNames.add(userList.get(i).getLoginName());
		}
		
		return loginNames;
	}
	/**
	 * 根据区域角色等信息获取相应的律师事务所*主任
	 * 住:只要登录名集合就可，英文逗号分隔
	 * @author 王鹏
	 * @version 2018-5-17 17:19:30
	 * @param oaLegalAid
	 * @return
	 */
	public List<String> getLawyerUserList(OaLegalAidInform oaLegalAidInform){
	/*	User su = new User();//获取律师主任角色列表
		//查询机构
		su.setOffice(new Office(oaLegalAidInform.getLawOffice().getId()));
		//查询角色
		su.setRole(systemService.getRoleByEnname(ROLE_LAWYER[0]));
		//获取登录名(英文逗号分隔)
		return getLoginNamesByUserList(su);*/
		List<String> ListLoginNames= Lists.newArrayList();
		PartTimeJob partTimeJob=new PartTimeJob();
		partTimeJob.setOfficeId(oaLegalAidInform.getLawOffice().getId());
		partTimeJob.setRoleId(systemService.getRoleByEnname(ROLE_LAWYER[0]).getId());
		ListLoginNames=systemService.findUserByOfficeRole(partTimeJob);
		System.out.println(ListLoginNames.toString());
		if(ListLoginNames.size()<=0) {
			throw new BusinessException("没有找到对应律所的主任，请核实");
		}
		return ListLoginNames;

	}
	/**
	 * 根据区域角色等信息获取相应的基层法律服务所*主任
	 * 住:只要登录名集合就可，英文逗号分隔
	 * @author 王鹏
	 * @version 2018-5-17 17:19:30
	 * @param oaLegalAid
	 * @return
	 */
	public List<String> getLegalServiceUserList(OaLegalAid oaLegalAid){
		User su = new User();//获取基层法律服务所主任角色列表
		//查询机构
		su.setOffice(new Office(oaLegalAid.getLegalOffice().getId()));
		//查询角色
		su.setRole(systemService.getRoleByEnname(ROLE_LEGAL_SERVICE[0]));
		//获取登录名(英文逗号分隔)
		return getLoginNamesByUserList(su);
	}
	/**
	 * 根据相应条件获得所有用户的登录名集合
	 * @author 王鹏
	 * @version 2018-05-17 17:27:06
	 * @param list
	 * @return
	 */
	public List<String> getLoginNamesByUserList(User searchUser) {
		List<String> loginNames = Lists.newArrayList();
		//根据机构和角色获取人员列表
		List<User> userList = systemService.findUserByCondition(searchUser);
		for (int i = 0; i < userList.size(); i++) {
			loginNames.add(userList.get(i).getLoginName());
		}
		//抛出没有任务接收人异常
		if(loginNames.size()==0) {
			throw new BusinessException("没有找到任务接收人。");
		}
		return loginNames;
	}

	
	
	/**
	 * 保存法援申请草稿
	 * @author 王鹏
	 * @version 2018-06-09 16:54:19
	 * @param oaLegalAidInform
	 */
	@Transactional(readOnly = false)
	public void saveDraft(OaLegalAidInform oaLegalAidInform) {
//		checkUserRelated(oaLegalAid, UserUtils.getUser());//检查用户相关性
		setCaseTitle(oaLegalAidInform);
		if (oaLegalAidInform.getIsNewRecord()){
			oaLegalAidInform.preInsert();
			dao.insertDraft(oaLegalAidInform);
		}else{
			oaLegalAidInform.preUpdate();
			dao.updateDraft(oaLegalAidInform);
		}
	}
	
	/**
	 * 没有标题自动生成标题的
	 * 草稿箱用，正式提交必须有一个案件名
	 * @param oaLegalAid
	 * 
	 */
	private void setCaseTitle(OaLegalAidInform oaLegalAidInform) {
		
		String name = oaLegalAidInform.getName();
		String title = oaLegalAidInform.getCaseTitle();
		if("".equals(title) && name!=""){
			title = name+"-"+"通知辩护"+"-"+DateUtils.getDateToString();
		}
		oaLegalAidInform.setCaseTitle(title);
	}
	
	/**
	 * 删除草稿，支持批量
	 * @author 王鹏
	 * @version 2018-06-09 17:10:37
	 * @param draftId
	 */
	@Transactional(readOnly = false)
	public void deleteDraft(String[] draftId) {
		dao.deleteDraft(draftId);
	}

	/**
	 * 接口: 查询草稿箱
	 * @author 王鹏
	 * @version 2018-06-09 17:25:24
	 * @param oaLegalAid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageVo<OaLegalAidInform> findDraftPageforApi(Integer pageNo, Integer pageSize, String caseTitle,String legalAidType ) {
		// TODO Auto-generated method stub
		OaLegalAidInform oaLegalAidInform = new OaLegalAidInform();
		Page<OaLegalAidInform> page = new Page<OaLegalAidInform>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		oaLegalAidInform.setCreateBy(UserUtils.getUser());
		oaLegalAidInform.setCaseTitle(caseTitle);
		oaLegalAidInform.setPage(page);
		oaLegalAidInform.setLegalAidType(legalAidType);
		//根据登录人和案件标题查询草稿
		page.setList(dao.findDraftListForApi(oaLegalAidInform));
		return new PageVo<OaLegalAidInform>(page, true);
	}
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
	}

}