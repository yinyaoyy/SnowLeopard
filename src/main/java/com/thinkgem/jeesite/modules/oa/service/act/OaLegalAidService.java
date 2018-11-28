/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service.act;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.zxing.WriterException;
import com.thinkgem.jeesite.api.chart.entity.LegalAidVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.JGpush.JGpushUtils;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.modules.act.dao.ActDao;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.cms.entity.SysUserPush;
import com.thinkgem.jeesite.modules.cms.service.SysUserPushService;
import com.thinkgem.jeesite.modules.oa.dao.act.OaLegalAidDao;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidCount;
import com.thinkgem.jeesite.modules.oa.service.OaProcessStateService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * 法援申请Service
 * @author 王鹏
 * @version 2018-05-11
 */
@Service
@Transactional(readOnly = true)
public class OaLegalAidService extends CrudService<OaLegalAidDao, OaLegalAid> implements JavaDelegate {

	/**
	 * 定义流程定义KEY，必须以“PD_”开头
	 * 组成结构：string[]{"流程标识","业务主表表名"}
	 */
	public static final String[] PD_LEGAL_AID = new String[]{"legal_aid", "oa_legal_aid"};
	
	
	/**
	 * 定义流程定义KEY，必须以“PD_”开头
	 * 组成结构：string[]{"流程标识","业务主表表名"}
	 */
	public static final String[] PD_LEGALIFROM_AID = new String[]{"notification_defense", "oa_legal_aid"};
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
	/**
	 * 法援主任的角色识别码
	 */
	public static final String[] ROLE_LAW = new String[]{"qxfygly"};
	
	@Autowired
	private ActDao actDao;
	@Autowired
	
	private ActTaskService actTaskService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private SysUserPushService sysUserPushService;

	@Autowired
	private AreaService areaService;
	@Autowired
	private OaProcessStateService oaProcessStateService;
	
	
	public OaLegalAid get(String id) {
		return super.get(id);
	}
	
	public List<OaLegalAid> findList(OaLegalAid oaLegalAid) {
		return super.findList(oaLegalAid);
	}
	
	public Page<OaLegalAid> findPage(Page<OaLegalAid> page, OaLegalAid oaLegalAid) {
		return super.findPage(page, oaLegalAid);
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
	public PageVo<OaLegalAid> findDraftPageforApi(Integer pageNo, Integer pageSize, String caseTitle,String legalAidType ) {
		// TODO Auto-generated method stub
		OaLegalAid oaLegalAid = new OaLegalAid();
		Page<OaLegalAid> page = new Page<OaLegalAid>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		oaLegalAid.setCreateBy(UserUtils.getUser());
		oaLegalAid.setCaseTitle(caseTitle);
		oaLegalAid.setPage(page);
		oaLegalAid.setLegalAidType(legalAidType);
		//根据登录人和案件标题查询草稿
		page.setList(dao.findDraftListForApi(oaLegalAid));
		return new PageVo<OaLegalAid>(page, true);
	}
	
	@Transactional(readOnly = false)
	public void save(OaLegalAid oaLegalAid) {
		User user=UserUtils.getUser();
		oaLegalAid.setCreateBy(user);
		//自动生成标题
		setCaseTitle(oaLegalAid,user);
		if(StringUtils.isBlank(oaLegalAid.getSource())) {
			oaLegalAid.setSource("100");//默认网页申请
		}
		// 申请发起
		Map<String, Object> vars = Maps.newHashMap();
		if (StringUtils.isBlank(oaLegalAid.getId())){
			checkUserRelated(oaLegalAid, user);//检查用户相关性
			oaLegalAid.preInsert();
			dao.insert(oaLegalAid);
			List<String> list = getFykyUserList(oaLegalAid);
			//获取科员处理
			vars.put("fyky",list);
			
			// 启动流程
			String procInsId = actTaskService.startProcess(PD_LEGAL_AID[0], PD_LEGAL_AID[1], oaLegalAid.getId(), oaLegalAid.getCaseTitle(), vars);
			//更新流程状态(受理中)
			oaProcessStateService.save(oaLegalAid.getId(), procInsId, oaLegalAid.getCaseTitle(), "1", "1", oaLegalAid.getCreateBy(), oaLegalAid.getCreateDate(), oaLegalAid.getUpdateBy(), oaLegalAid.getUpdateDate(),"0","legal_aid",oaLegalAid.getArea().getId());
			//开始推送   服务人员的推送
			//pushUser方法,list为登录名的集合，第二个参数为单个的登录名，如果没有可以传空，返回值为id集合,根据id推送
			List<String> userIds = sysUserPushService.pushUser(list,null);	
			List<List<String>> lists = JGpushUtils.averageAssign(userIds);
			for (int i = 0; i < lists.size(); i++) {
				SysUserPush push = new SysUserPush();
				push.getPushMessage().setTitle("");//如果没有则填空
				push.getPushMessage().setMsgContent("你有一个关于《案件标题:"+oaLegalAid.getCaseTitle()+"》的待办事项，请尽快处理。");
				push.setUrl("/act/task/todo/");//需要链接的地址，为controller中的地址。
			    sysUserPushService.pushNotificetion(push,lists.get(i));
			}
		}
		else if("aid_apply_zhiding".equals(oaLegalAid.getAct().getTaskDefKey())){
			//当用户选择时，需要继续处理流程
			//根据律师或者是法律服务工作者信息需要补充机构信息
			//如果没有选择，则不用处理
			if(StringUtils.isNotBlank(oaLegalAid.getLawOffice().getId())
					|| StringUtils.isNotBlank(oaLegalAid.getLegalOffice().getId()) || StringUtils.isNoneBlank(oaLegalAid.getLawAssistanceOffice().getId())) {
		/*		if(StringUtils.isNotBlank(oaLegalAid.getLawyer().getId())){
					oaLegalAid.setLawOffice(systemService.getUser(oaLegalAid.getLawyer().getId()).getOffice());
				}
				else {
					oaLegalAid.setLegalOffice(systemService.getUser(oaLegalAid.getLegalPerson().getId()).getOffice());
				}*/
				vars.put("pass", "1");//用户指定了承办人
				//指定该律所的主任进行审核
				if(StringUtils.isNotBlank(oaLegalAid.getLawOffice().getId())) {
					vars.put("zhuren", getLawyerUserList(oaLegalAid));
					oaLegalAid.setLegalPerson(null);
				}
				else if(StringUtils.isNotBlank(oaLegalAid.getLegalOffice().getId())){
					vars.put("zhuren", getLegalServiceUserList(oaLegalAid));
					oaLegalAid.setLawyer(null);
				}else if(StringUtils.isNotBlank(oaLegalAid.getLawAssistanceOffice().getId())){
					vars.put("zhuren", getLegalUserList(oaLegalAid));
				}
			}
			else {//未指定（交由法援科员指定）
				vars.put("pass", "0");
			}
			dao.updateSelective(oaLegalAid);
			actTaskService.complete(oaLegalAid.getAct().getTaskId(), oaLegalAid.getAct().getProcInsId(), oaLegalAid.getAct().getComment(), vars);
		}
		else{// 重新编辑申请
			checkUserRelated(oaLegalAid, user);//检查用户相关性
			oaLegalAid.preUpdate();
			if("yes".equals(oaLegalAid.getAct().getFlag())){
				oaLegalAid.getAct().setComment("[重申] "+oaLegalAid.getAct().getComment());
				vars.put("pass","1");
				//更新流程状态(受理中)
				oaProcessStateService.save(oaLegalAid.getAct().getProcInsId(),"1",oaLegalAid.getUpdateBy(),oaLegalAid.getUpdateDate(),oaLegalAid.getAct().getComment(),oaLegalAid.getCaseTitle(),oaLegalAid.getArea().getId(),null,null,null);
			}else{
				oaLegalAid.getAct().setComment("[销毁] "+oaLegalAid.getAct().getComment());
				vars.put("pass",  "0");
				//更新流程状态(作废)
				oaProcessStateService.save(oaLegalAid.getAct().getProcInsId(),"5",oaLegalAid.getUpdateBy(),oaLegalAid.getUpdateDate(),oaLegalAid.getAct().getComment(),oaLegalAid.getCaseTitle(),oaLegalAid.getArea().getId(),null,null,null);
			}
			dao.update(oaLegalAid);
			actTaskService.complete(oaLegalAid.getAct().getTaskId(), oaLegalAid.getAct().getProcInsId(), oaLegalAid.getAct().getComment(),oaLegalAid.getCaseTitle(), vars);
		}
	}

	
	/**
	 * 保存法援申请草稿
	 * @author 王鹏
	 * @version 2018-06-09 16:54:19
	 * @param oaLegalAid
	 */
	@Transactional(readOnly = false)
	public void saveDraft(OaLegalAid oaLegalAid) {
//		checkUserRelated(oaLegalAid, UserUtils.getUser());//检查用户相关性
		//自动生成标题
		setCaseTitle(oaLegalAid,UserUtils.getUser());
		if (oaLegalAid.getIsNewRecord()){
			oaLegalAid.preInsert();
			dao.insertDraft(oaLegalAid);
		}else{
			oaLegalAid.preUpdate();
			dao.updateDraft(oaLegalAid);
		}
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
	 * 校验当前用户是否和申请有关(必须是申请人或者代理人)
	 * @author 王鹏
	 * @version 2018-05-24 20:52:26
	 * @param oaLegalAid
	 * @param user
	 */
	private void checkUserRelated(OaLegalAid oaLegalAid, User user) {
		boolean userRelated = false;//校验当前用户是否和申请有关(必须是申请人或者代理人)
		/*if(user.getName().equals(oaLegalAid.getName()) && user.getPapernum().equals(oaLegalAid.getIdCard())) {
			userRelated = true;
		}
		if(user.getName().equals(oaLegalAid.getProxyName()) && user.getPapernum().equals(oaLegalAid.getProxyIdCard())) {
			userRelated = true;
		}*/
		if(user.getName().equals(oaLegalAid.getName())) {
			userRelated = true;
		}
		if(user.getName().equals(oaLegalAid.getProxyName())) {
			userRelated = true;
		}
		if(!userRelated) {//用户与此次援助申请没有关系，不能继续此流程
			throw new BusinessException("登录用户与此次法律援助申请无关！");
		}
	}

	@Transactional(readOnly = false)
	public String toDo(OaLegalAid oaLegalAid) {
		String errorMsg = "";//错误信息
		Map<String, Object> vars = Maps.newHashMap();
		String taskDefKey = oaLegalAid.getAct().getTaskDefKey();
		if("aid_chengbanren_banli".equals(taskDefKey) && "no".equals(oaLegalAid.getAct().getFlag())) {
			//如果是承办人办理，且没有办结，则不需要提交到下一步流程
			dao.updateSelective(oaLegalAid);
			return errorMsg;
		}
		if("no".equals(oaLegalAid.getAct().getFlag()) && StringUtils.isBlank(oaLegalAid.getAct().getComment())) {
			//如果是驳回，则必须填写驳回意见
			errorMsg = "未填写驳回意见";
			return errorMsg;
		}
		//处理前先签收操作
		actTaskService.claim(oaLegalAid.getAct().getTaskId(), UserUtils.getUser().getLoginName());
		// 设置意见
		oaLegalAid.getAct().setComment(("yes".equals(oaLegalAid.getAct().getFlag())?"[同意] ":"[驳回] ")+oaLegalAid.getAct().getComment());
		// 对不同环节的业务逻辑进行操作
		try {
			if("adi_approve".equals(taskDefKey)){
				if("yes".equals(oaLegalAid.getAct().getFlag())) {
					//科员同意受理后，需要自动生成字号
					oaLegalAid.setYearNo(getYearNo());
					//保存二维码
					try {
						oaLegalAid.setQrcode(QrCodeCreateUtil.createQrCode(null,oaLegalAid.getProcInsId(),900,"JPEG"));
					} catch (WriterException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					dao.updateSelective(oaLegalAid);
				}
				vars.put("pass", "yes".equals(oaLegalAid.getAct().getFlag())? "1" : "0");
			}else if("aid_ky_zhiding".equals(taskDefKey)){
				if(StringUtils.isBlank(oaLegalAid.getLawOffice().getId()) && StringUtils.isBlank(oaLegalAid.getLegalOffice().getId()) && StringUtils.isBlank(oaLegalAid.getLawAssistanceOffice().getId())) {
					throw new BusinessException("未指定律师事务所或基层法律服务所");
				}
				//保存律师事务所或者是基层法律服务所的信息
				dao.updateSelective(oaLegalAid);
				//指定该律所的主任进行审核
				if(StringUtils.isNotBlank(oaLegalAid.getLawOffice().getId())) {
					vars.put("zhuren", getLawyerUserList(oaLegalAid));
				}
				else if(StringUtils.isNotBlank(oaLegalAid.getLegalOffice().getId())){
					vars.put("zhuren", getLegalServiceUserList(oaLegalAid));
				}else if (StringUtils.isNotBlank(oaLegalAid.getLawAssistanceOffice().getId())) {
					vars.put("zhuren", getLegalUserList(oaLegalAid));
 				}
			}else if("aid_zhuren".equals(taskDefKey)){
				if(StringUtils.isNotBlank(oaLegalAid.getLawOffice().getId()) && StringUtils.isBlank(oaLegalAid.getLawyer().getId())) {
					throw new BusinessException("未指定律师");
				}
				if(StringUtils.isNotBlank(oaLegalAid.getLegalOffice().getId()) && StringUtils.isBlank(oaLegalAid.getLegalPerson().getId())) {
					throw new BusinessException("未指定法律服务工作者");
				}
				//选择具体律师或是法律服务工作者
				dao.updateSelective(oaLegalAid);
				vars.put("pass", "yes".equals(oaLegalAid.getAct().getFlag())? "1" : "0");
				//交由指定承办人来确认(根据id获取登录名)
				if(StringUtils.isNotBlank(oaLegalAid.getLawyer().getId())) {
					vars.put("chengbanren", systemService.getUser(oaLegalAid.getLawyer().getId()).getLoginName());
				}
				else if(StringUtils.isNotBlank(oaLegalAid.getLegalPerson().getId())) {
					vars.put("chengbanren", systemService.getUser(oaLegalAid.getLegalPerson().getId()).getLoginName());
				}else{
					vars.put("chengbanren", systemService.getUser(oaLegalAid.getLawAssistUser().getId()).getLoginName());
				}
			}else if("aid_chengbanren_banli".equals(taskDefKey)){
				//将附件信息存放到律师附件中
				dao.updateSelective(oaLegalAid);
				vars.put("pass", "yes".equals(oaLegalAid.getAct().getFlag())? "1" : "0");
				//律师或是法律工作者具体处理
			}else if("aid_pingjia".equals(taskDefKey)){
				if(StringUtils.isBlank(oaLegalAid.getThirdPartyScore()) || StringUtils.isBlank(oaLegalAid.getThirdPartyEvaluation())) {
					throw new BusinessException("未填写第三方评分或第三方评价");
				}
				//第三方参与评价
				dao.updateSelective(oaLegalAid);
				//传入申请id
				vars.put("aid_id", oaLegalAid.getId());
			}else if("aid_chengbanren_butie".equals(taskDefKey)){
				if(StringUtils.isBlank(oaLegalAid.getReceiveSubsidy())) {
					throw new BusinessException("未填写补贴信息");
				}
				//承办人填写申请补贴信息
				dao.updateSelective(oaLegalAid);
				//传入申请id
				vars.put("aid_id", oaLegalAid.getId());
			}else{
				/*
				 * 1.承办人是否同意
				 */
				vars.put("pass", "yes".equals(oaLegalAid.getAct().getFlag())? "1" : "0");
			}
		} catch (BusinessException e) {
			// TODO: handle exception
			errorMsg = e.getMessage();
			e.printStackTrace();
		}
		if(StringUtils.isBlank(errorMsg)) {// 提交流程任务
			actTaskService.complete(oaLegalAid.getAct().getTaskId(), oaLegalAid.getAct().getProcInsId(), oaLegalAid.getAct().getComment(), vars);
		}
		if("no".equals(oaLegalAid.getAct().getFlag()) && "adi_approve".equals(taskDefKey)){
			//更新流程状态(已退回)
			oaProcessStateService.save(oaLegalAid.getAct().getProcInsId(),"3",oaLegalAid.getUpdateBy(),oaLegalAid.getUpdateDate(),oaLegalAid.getAct().getComment(),oaLegalAid.getCaseTitle(),oaLegalAid.getArea().getId(),null,null,null);
		}else if("yes".equals(oaLegalAid.getAct().getFlag()) && "adi_approve".equals(taskDefKey)){
			//更新流程状态(办理中)
			oaProcessStateService.save(oaLegalAid.getAct().getProcInsId(),"4",oaLegalAid.getUpdateBy(),oaLegalAid.getUpdateDate(),oaLegalAid.getAct().getComment(),oaLegalAid.getCaseTitle(),oaLegalAid.getArea().getId(),new Date(),null,null);
		}else if("aid_chengbanren_butie".equals(taskDefKey)){
			//更新流程状态(已办结)
			oaProcessStateService.save(oaLegalAid.getAct().getProcInsId(),"2",oaLegalAid.getUpdateBy(),oaLegalAid.getUpdateDate(),oaLegalAid.getAct().getComment(),null,null,null,null,null);
		}else if("yes".equals(oaLegalAid.getAct().getFlag()) && "aid_chengbanren_shouli".equals(taskDefKey)){
			//更新流程状态(办理中)
			oaProcessStateService.save(oaLegalAid.getAct().getProcInsId(),"4",oaLegalAid.getUpdateBy(),oaLegalAid.getUpdateDate(),oaLegalAid.getAct().getComment(),null,null,null,oaLegalAid.getLawyer().getId(),null);
		}
		return errorMsg;
	}


	@Transactional(readOnly = false)
	public void delete(OaLegalAid oaLegalAid) {
		super.delete(oaLegalAid);
	}
	
	/**
	 * 获取一个年度序号
	 * 注：只要编号即可，前面不用补零
	 * @author 王鹏
	 * @version 2018-05-14 09:47:11
	 * @return
	 */
	public String getYearNo() {
		return StringUtils.patchZero(dao.selectYearNo(), 0);
	}
	/**
	 * 根据区域角色等信息获取相应的法律科员人员列表
	 * 住:只要登录名集合就可
	 * @author 王鹏
	 * @version 2018-05-16 11:44:33
	 * @param oaLegalAid
	 * @return
	 */
	public List<String> getFykyUserList(OaLegalAid oaLegalAid){
		//根据区域获取处理机构
		
		Area area = oaLegalAid.getArea();
		String areaId = "";
		if(area!=null && !"".equals(area.getId())){
			areaId = area.getId();
		}
		List<Map<String, Object>> listOffice = systemService.getOfficeUser("5", "0", areaId, "", "");
		
		
		/*Office so = new Office();
		so.setArea(oaLegalAid.getArea());
		so.setType("1");//查询机构(此机构下有法援科员角色的即可，不强制要求是法援中心科室的)
		List<Office> listOffice = officeService.findListByCondition(so);*/
		if(listOffice==null||listOffice.size()==0) {
			throw new BusinessException("当前区域没有对应的机构！");
		}
		//User su = new User();//获取科员列表
		//查询机构
		//su.setCompany(listOffice.get(0));
		String roleEn = ROLE_FYKY[1];
		//机构是盟的则用法援科员
		/*if(!"2".equals(listOffice.get(0).getGrade())&&!"S0000".equals(listOffice.get(0).getCode())) {
			roleEn = ROLE_FYKY[1];//如果是其他级别的，都用旗县科员角色
		}*/
		//查询角色
		//su.setRole(systemService.getRoleByEnname(roleEn));
		//获取登录名(英文逗号分隔)
		PartTimeJob partTimeJob=new PartTimeJob();
		partTimeJob.setOfficeId((String) listOffice.get(0).get("id"));
		partTimeJob.setRoleId(systemService.getRoleByEnname(roleEn).getId());
		return systemService.findUserByOfficeRole(partTimeJob);
		//return getLoginNamesByUserList(su);
	}
	

	


	/**
	 * 根据区域角色等信息获取相应的法援*主任
	 * 住:只要登录名集合就可，英文逗号分隔
	 * @author 如初
	 * @version 2018-07-07 17:19:30
	 * @param oaLegalAid
	 * @return
	 */
	public List<String> getFyUserList(OaLegalAid oaLegalAid){
	/*	User su = new User();//获取科员主任角色列表
		//查询机构
		su.setOffice(new Office(oaLegalAid.getLegalOffice().getId()));
		//查询角色
		su.setRole(systemService.getRoleByEnname(ROLE_FYZRKY[0]));
		//获取登录名(英文逗号分隔)
		return getLoginNamesByUserList(su);*/
		PartTimeJob partTimeJob=new PartTimeJob();
		partTimeJob.setOfficeId(oaLegalAid.getLegalOffice().getId());
		partTimeJob.setRoleId(systemService.getRoleByEnname(ROLE_FYZRKY[0]).getId());
		return systemService.findUserByOfficeRole(partTimeJob);
	}
	
	/**
	 * 根据区域角色等信息获取相应的法援科员
	 * 住:只要登录名集合就可，英文逗号分隔
	 * @author 如初
	 * @version 2018-07-07 17:19:30
	 * @param oaLegalAid
	 * @return
	 */
	public List<String> getFykUserList(OaLegalAid oaLegalAid){
/*		List<String> loginNames = Lists.newArrayList();
		List<String> loginNames1 = Lists.newArrayList();
		User su = new User();//获取法援科员角色列表
		//查询机构
		su.setOffice(new Office(oaLegalAid.getLegalOffice().getId()));
		//查询角色
		su.setRole(systemService.getRoleByEnname(ROLE_FYKY[1]));
		loginNames=getLoginNamesUserList(su);
		su.setRole(systemService.getRoleByEnname(ROLE_FYKY[0]));
		loginNames1=getLoginNamesUserList(su);
		if(loginNames1.size()!=0) {
		loginNames.addAll(getLoginNamesUserList(su));
		}
		if(loginNames.size()==0) {
			throw new BusinessException("没有找到任务接收人。");
		}
		//获取登录名(英文逗号分隔)
		return loginNames;*/
		List<String> loginNames = Lists.newArrayList();
		List<String> loginNames1 = Lists.newArrayList();
		PartTimeJob partTimeJob=new PartTimeJob();
		partTimeJob.setOfficeId(oaLegalAid.getLegalOffice().getId());
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
	public List<String> getLawyerUserList(OaLegalAid oaLegalAid){
		/*User su = new User();//获取律师主任角色列表
		//查询机构
		su.setOffice(new Office(oaLegalAid.getLawOffice().getId()));
		//查询角色
		su.setRole(systemService.getRoleByEnname(ROLE_LAWYER[0]));
		//获取登录名(英文逗号分隔)
		return getLoginNamesByUserList(su);*/
		List<String> loginNames1 = Lists.newArrayList();
		PartTimeJob partTimeJob=new PartTimeJob();
		partTimeJob.setOfficeId(oaLegalAid.getLawOffice().getId());
		partTimeJob.setRoleId(systemService.getRoleByEnname(ROLE_LAWYER[0]).getId());
		loginNames1 =systemService.findUserByOfficeRole(partTimeJob);
		if(loginNames1.size()==0) {
			throw new BusinessException("没有找到对应律师机构的主任请核实。");
		}
	return loginNames1;
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
		/*User su = new User();//获取基层法律服务所主任角色列表
		//查询机构
		su.setOffice(new Office(oaLegalAid.getLegalOffice().getId()));
		//查询角色
		su.setRole(systemService.getRoleByEnname(ROLE_LEGAL_SERVICE[0]));
		//获取登录名(英文逗号分隔)
		return getLoginNamesByUserList(su);*/
		List<String> loginNames1 = Lists.newArrayList();
		PartTimeJob partTimeJob=new PartTimeJob();
		partTimeJob.setOfficeId(oaLegalAid.getLegalOffice().getId());
		partTimeJob.setRoleId(systemService.getRoleByEnname(ROLE_LEGAL_SERVICE[0]).getId());
		loginNames1 =systemService.findUserByOfficeRole(partTimeJob);
			if(loginNames1.size()==0) {
				throw new BusinessException("没有找到对应法援机构的主任请核实。");
			}
		return loginNames1;
	}
	
	/**
	 * 根据区域角色等信息获取相应的基层法律服务所*主任
	 * 住:只要登录名集合就可，英文逗号分隔
	 * @author 王鹏
	 * @version 2018-5-17 17:19:30
	 * @param oaLegalAid
	 * @return
	 */
	public List<String> getLegalUserList(OaLegalAid oaLegalAid){
		/*User su = new User();//获取基层法律服务所主任角色列表
		//查询机构
		su.setOffice(new Office(oaLegalAid.getLegalOffice().getId()));
		//查询角色
		su.setRole(systemService.getRoleByEnname(ROLE_LEGAL_SERVICE[0]));
		//获取登录名(英文逗号分隔)
		return getLoginNamesByUserList(su);*/
		List<String> loginNames1 = Lists.newArrayList();
		PartTimeJob partTimeJob=new PartTimeJob();
		partTimeJob.setOfficeId(oaLegalAid.getLawAssistanceOffice().getId());
		partTimeJob.setRoleId(systemService.getRoleByEnname(ROLE_LAW[0]).getId());
		loginNames1 =systemService.findUserByOfficeRole(partTimeJob);
			if(loginNames1.size()==0) {
				throw new BusinessException("没有找到对应法援机构的主任请核实。");
			}
		return loginNames1;
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
	 * 处理监听任务
	 * 1.当流转到此节点时，自动将任务进行归档
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String aidId = String.valueOf(execution.getVariable("aid_id"));
		dao = SpringContextHolder.getBean("oaLegalAidDao");
		dao.archiving(aidId);//设为归档，所有人均不可看
	}
	
	/**
	 * 按年份和日期统计法律援助申请数量
	 * @author 王鹏
	 * @version 2018-05-29 17:37:04
	 * @param oac
	 * @return
	 */
	public List<OaLegalAidCount> countYearArea(OaLegalAidCount oac){
		if(oac == null) {
			throw new BusinessException("参数不全");
		}
		return dao.countYearArea(oac);
	}

	/**
	 * 法律援助案件类型受理数量占比
	 * 1.根据年度地区案件类型进行统计
	 * 2.只统计已受理的
	 * @author 王鹏
	 * @version 2018-05-29 17:37:04
	 * @param oac
	 * @return
	 */
	public List<OaLegalAidCount> countCaseType(OaLegalAidCount oac){
		if(oac == null || StringUtils.isBlank(oac.getArea().getId())) {
			throw new BusinessException("参数不全");
		}
		return dao.countCaseType(oac);
	}

	/**
	 * 法律援助申请方式受理数量占比
	 * 1.根据年度地区申请方式进行统计
	 * 2.是要申请就统计
	 * @author 王鹏
	 * @version 2018-05-29 17:37:04
	 * @param oac
	 * @return
	 */
	public List<OaLegalAidCount> countLegalAidType(OaLegalAidCount oac){
		if(oac == null || StringUtils.isBlank(oac.getArea().getId())) {
			throw new BusinessException("参数不全");
		}
		return dao.countLegalAidType(oac);
	}

	/**
	 * 大屏统计: 按区域统计时间段内法援申请数量
	 * 全盟法律援助案件申请数量占比图、各旗县法律援助案件占比图
	 * 优化返回数据，减少字段数量
	 * @author 王鹏
	 * @version 2018-6-7 10:41:53
	 * @param oac
	 * @return
	 */
	public Map<String, Object> countAreaByYearForBigScreen(OaLegalAidCount oac){
		if(oac == null || StringUtils.isBlank(oac.getBeginDate()) || StringUtils.isBlank(oac.getEndDate())) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, Object>> dataList = Lists.newArrayList();
		Map<String, Object> map = null;
		Integer total = 0;//总数
		List<OaLegalAidCount> list = dao.countAreaByYearForBigScreen(oac);
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
	 * 大屏统计: 按年度区域统计时间段内法援申请/受理数量对比
	 * 全盟法律援助案件申请数量与受理数量走势对比图
	 * 申请和受理查两边的数量对比
	 * 优化返回数据，减少字段数量
	 * @author 王鹏
	 * @version 2018-6-7 10:41:53
	 * @param oac
	 * @return
	 */
	public Map<String, Object> countYearByAreaComparedForBigScreen(OaLegalAidCount oac){
		if(oac == null || StringUtils.isBlank(oac.getBeginDate()) || StringUtils.isBlank(oac.getEndDate())) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		//申请数量
		List<OaLegalAidCount> aidList = dao.countYearByAreaForBigScreen(oac);
		//受理数量
		oac.setIsAccept("1");//统计受理数量
		List<OaLegalAidCount> acceptList = dao.countYearByAreaForBigScreen(oac);
		List<String> xData = Lists.newArrayList();//横坐标数据
		//获取两个日期间的所有数据(年、年月、年月日)
		xData = DateUtils.getAllDayBetweenDate(oac.getBeginDate(), oac.getEndDate());
		resultMap.put("xData", xData);//存入横坐标
		
		String areaName = "";
		if(oac.getArea()==null || "".equals(oac.getArea()) || oac.getArea().getId() == null || "".equals(oac.getArea().getId())){
			areaName = "全盟";
		}else{
			areaName = areaService.get(oac.getArea().getId()).getName();
		}
		
		//纵坐标数据
		List<Map<String, Object>> yData = Lists.newArrayList();
		Map<String, Object> aidMap = Maps.newHashMap();//申请集合
		aidMap.put("name", areaName+"法律援助案件申请数量");
		aidMap.put("type", "line");//折线图
		aidMap.put("smooth", true);
		Map<String, Object> acceptMap = Maps.newHashMap();//受理集合
		acceptMap.put("name", areaName+"法律援助案件受理数量");
		acceptMap.put("type", "line");//折线图
		acceptMap.put("smooth", true);
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
	 * 大屏统计: 按年度区域统计时间段内法援申请/受理数量对比
	 * 全盟法律援助案件数量折线图
	 * 优化返回数据，减少字段数量
	 * @author 王鹏
	 * @version 2018-6-7 10:41:53
	 * @param oac
	 * @return
	 */
	public Map<String, Object> countLegalAidAcceptForBigScreen(OaLegalAidCount oac){
		if(oac == null || StringUtils.isBlank(oac.getBeginDate()) || StringUtils.isBlank(oac.getEndDate())) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		//受理数量
		oac.setIsAccept("1");//统计受理数量
		List<OaLegalAidCount> acceptList = dao.countYearByAreaForBigScreen(oac);
		List<String> xData = Lists.newArrayList();//横坐标数据
		//获取两个日期间的所有数据(年、年月、年月日)
		xData = DateUtils.getAllDayBetweenDate(oac.getBeginDate(), oac.getEndDate());
		resultMap.put("xData", xData);//存入横坐标
		//纵坐标数据
		List<Map<String, Object>> yData = Lists.newArrayList();
		Map<String, Object> acceptMap = Maps.newHashMap();//受理集合
		acceptMap.put("name", "全盟法律援助案件受理数量");
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
	 * 接口: 大屏查询法援申请
	 * @author 王鹏
	 * @version 2018-06-09 17:25:24
	 * @param oaLegalAid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageVo<LegalAidVo> findPageforApi(LegalAidVo legalAidVo) {
		// TODO Auto-generated method stub
		List<LegalAidVo> list = dao.findListForBigScreen(legalAidVo);
		int count = dao.findCountListForBigScreen(legalAidVo);
		legalAidVo.getPage().setList(list);
		legalAidVo.getPage().setCount(count);
		return new PageVo<LegalAidVo>(legalAidVo.getPage(), true);
	}
	

	/**
	 * 按月份和日期统计法律援助申请数量
	 * @author zq
	 * @version
	 * @param oac
	 * @return
	 */
	public List<List<String>> countMonthArea(OaLegalAidCount oac, List<Area> areaList){
		if(oac == null) {
			throw new BusinessException("参数不全");
		}
		List<List<String>> list = Lists.newArrayList();
		//统计数据
		List<OaLegalAidCount> oacList = dao.countYearArea(oac);
		//获取所有日期
		List<String> dateList = DateUtils.getAllDayBetweenDate(oac.getBeginDate(), oac.getEndDate());
		//初始化表头数据
		List<String> tr = Lists.newArrayList();
		tr.add("日期");
		for (int i = 0; i < areaList.size(); i++) {
			tr.add(areaList.get(i).getName());
		}
		list.add(tr);//添加表头数据
		//添加统计数据
		OaLegalAidCount result_oac = null;
		List<String> td = null;
		boolean areaHasData = false;
		for (int i = 0; i < dateList.size(); i++) {
			td = Lists.newArrayList();//单个日期的数据
			td.add(dateList.get(i));
			//判断该地区是否有此日期的数据（默认没有）
			for (int j = 0; j < areaList.size(); j++) {
				areaHasData = false;
				for (int k = 0; k < oacList.size(); k++) {
					result_oac = oacList.get(k);
					//如果日期和地区均相符
					if(dateList.get(i).equals(result_oac.getYear())
							&& areaList.get(j).getId().equals(result_oac.getArea().getId())) {
						//添加数据
						td.add(String.valueOf(result_oac.getCount()));
						areaHasData = true;
						break;
					}
				}
				if(!areaHasData) {//如果此地区没有数据，则补零
					td.add("0");
				}
			}
			list.add(td);//添加一行数据
		}
		return list;
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
	 * 查询法援评价信息
	 * @param id
	 * @return
	 */
	public List<T> getApi(String id) {
		return dao.getApi(id);
	}
	
	/**
	 * 大屏上法律援助的数量执行图，支持多个地区选择,与第一种数据格式不同
	 * @param oac
	 * @return
	 */
	public Map<String, Object> countLegalAidAcceptForBigScreen1(OaLegalAidCount oac){
		if(oac == null || StringUtils.isBlank(oac.getBeginDate()) || StringUtils.isBlank(oac.getEndDate())) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		//受理数量
		oac.setIsAccept("1");//统计受理数量
		List<String> xData = Lists.newArrayList();//横坐标数据
		//获取两个日期间的所有数据(年、年月、年月日)
		xData = DateUtils.getAllDayBetweenDate(oac.getBeginDate(), oac.getEndDate());
		
		StringBuffer dsf = new StringBuffer();
		for(int i = 0 ; i < xData.size(); i++){
			if(i==xData.size()-1){
				dsf.append("SELECT '"+xData.get(i)+"'");
			}else{
				dsf.append("SELECT '"+xData.get(i)+"' YEAR UNION ALL ");
			}
		}
		
		oac.getSqlMap().put("dsf", dsf.toString());
		List<OaLegalAidCount> acceptList = dao.countYearByAreaForBigScreen1(oac);
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
				acceptMap.put("name", acceptList.get(i).getArea().getName()+"法律援助案件受理数量");
				acceptMap.put("type", "line");//折线图
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
	 * 申请人结束任务
	 * 
	 */
	@Transactional(readOnly = false)
	public void   applyEndProcess(OaLegalAid oaLegalAid) {
		if (StringUtils.isBlank(oaLegalAid.getRemarks())) {
			throw new BusinessException("结束意见不能为空");
		}
		Task task= getCurrentTaskByProcInsId(oaLegalAid.getProcInsId());
		if(task!=null) {
			if("aid_chengbanren_banli".equals(task.getTaskDefinitionKey())||"aid_pingjia".equals(task.getTaskDefinitionKey())||"aid_chengbanren_butie".equals(task.getTaskDefinitionKey())) {
				throw new BusinessException("当前任务节点不允许结束");
			}
		Map<String, Object> variables=new HashMap<>();
		actTaskService.jumpTask(oaLegalAid.getProcInsId(), "aid_end", variables);	
		variables.put("actId", "aid_end");
		variables.put("procInstId", oaLegalAid.getProcInsId());
		variables.put("assignee", UserUtils.getUser().getLoginName());
		String taskId=IdGen.uuid();
		variables.put("taskId", taskId);
         actDao.updateActinst(variables);
     	variables.put("id", IdGen.uuid());
     	variables.put("type", "comment");
     	variables.put("action", "AddComment");
     	variables.put("time", new Date());
     	variables.put("message", "[申请人结束任务]:"+oaLegalAid.getRemarks());
     //动态加入一条结束意见
     	actDao.insertActhicomment(variables);
     	oaLegalAid.setUpdateBy(UserUtils.getUser());
     	oaLegalAid.setUpdateDate(new Date());
     	dao.updateSelective(oaLegalAid);
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
	public String   endProcess(OaLegalAid oaLegalAid) {
		if (StringUtils.isBlank(oaLegalAid.getAct().getComment())) {
			throw new BusinessException("结束意见不能为空");
		}
		oaLegalAid.setRemarks(oaLegalAid.getAct().getComment());
		oaLegalAid.setUpdateBy(UserUtils.getUser());
		oaLegalAid.setUpdateDate(new Date());
		dao.updateSelective(oaLegalAid);
		Act act=oaLegalAid.getAct();
		Task task= getCurrentTaskByProcInsId(act.getProcInsId());
		if(task!=null) {
		if("aid_pingjia".equals(task.getTaskDefinitionKey())||"aid_chengbanren_butie".equals(task.getTaskDefinitionKey())) {
			throw new BusinessException("当前任务节点不允许结束");
		}
		Map<String, Object> variables=new HashMap<>();
		// 处理前先签收操作等于进入个人池
	    actTaskService.claim(oaLegalAid.getAct().getTaskId(), UserUtils.getUser().getLoginName());
		taskService.addComment(act.getTaskId(),act.getProcInsId(), "[结束任务]:"+act.getComment());
		actTaskService.taskinst(act);
		actTaskService.jumpTask(oaLegalAid.getAct().getProcInsId(), "aid_end", variables);	

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
	 * 没有标题自动生成标题的
	 * @param oaLegalAid
	 * @param user
	 */
	private void setCaseTitle(OaLegalAid oaLegalAid, User user) {
		String caseTypeDesc = oaLegalAid.getCaseTypeDesc();
		String area = oaLegalAid.getArea().getName();
		String name = user.getName();
		String title = oaLegalAid.getCaseTitle();
		if("".equals(title) && caseTypeDesc != "" & area!="" && name!=""){
			title = area+"-"+name+"-"+caseTypeDesc+"-"+DateUtils.getDateToString();
		}
		oaLegalAid.setCaseTitle(title);
	}

}