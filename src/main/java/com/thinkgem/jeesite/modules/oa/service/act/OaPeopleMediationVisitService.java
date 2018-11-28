/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service.act;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAgreement;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationVisit;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.dao.act.OaPeopleMediationVisitDao;

/**
 * 人民调解回访记录Service
 * @author zhangqiang
 * @version 2018-05-28
 */
@Service
@Transactional(readOnly = true)
public class OaPeopleMediationVisitService extends CrudService<OaPeopleMediationVisitDao, OaPeopleMediationVisit> {
	
	public static final String[] PD_LEGAL_AID = new String[]{"people_mediation", "oa_people_mediation_visit"};

	@Autowired
	private ActTaskService actTaskService;
	
	public OaPeopleMediationVisit get(String id) {
		OaPeopleMediationVisit oaPeopleMediationVisit = super.get(id);
		if(oaPeopleMediationVisit.getOaPeopleMediationAgreement()==null){
			oaPeopleMediationVisit.setOaPeopleMediationAgreement(new OaPeopleMediationAgreement());
		}
		if(oaPeopleMediationVisit.getOaPeopleMediationApply()==null){
			oaPeopleMediationVisit.setOaPeopleMediationApply(new OaPeopleMediationApply());
		}
		
		return oaPeopleMediationVisit;
	}
	
	public List<OaPeopleMediationVisit> findList(OaPeopleMediationVisit oaPeopleMediationVisit) {
		return super.findList(oaPeopleMediationVisit);
	}
	
	public Page<OaPeopleMediationVisit> findPage(Page<OaPeopleMediationVisit> page, OaPeopleMediationVisit oaPeopleMediationVisit) {
		return super.findPage(page, oaPeopleMediationVisit);
	}
	
	@Transactional(readOnly = false)
	public void save(OaPeopleMediationVisit oaPeopleMediationVisit) {
//		super.save(oaPeopleMediationVisit);
		if( StringUtils.isBlank(oaPeopleMediationVisit.getId())){
			oaPeopleMediationVisit.preInsert();
			dao.insert(oaPeopleMediationVisit);
		}else{
			super.save(oaPeopleMediationVisit);
		}
	}
	
	/**
	 * 提交回访记录，进入下一步
	 * @param oaPeopleMediationExamine
	 */
	@Transactional(readOnly = false)
	public void submit(OaPeopleMediationVisit oaPeopleMediationVisit) {
		save(oaPeopleMediationVisit);
		Map<String, Object> vars = Maps.newHashMap();
		// 设置意见
//		oaPeopleMediationVisit.getAct().setComment(("yes".equals(oaPeopleMediationVisit.getAct().getFlag())?"[同意] ":"[驳回] ")+oaPeopleMediationVisit.getAct().getComment());
		// 判断是否是调解环节
		String taskDefKey = oaPeopleMediationVisit.getAct().getTaskDefKey();
		if("mediation_huifang".equals(taskDefKey)){
			//领取任务
			claim(oaPeopleMediationVisit.getAct());
			
		}
		// 提交流程任务
		actTaskService.complete(oaPeopleMediationVisit.getAct().getTaskId(), oaPeopleMediationVisit.getAct().getProcInsId(), oaPeopleMediationVisit.getAct().getComment(),vars);		
	
	}

	@Transactional(readOnly = false)
	public void delete(OaPeopleMediationVisit oaPeopleMediationVisit) {
		super.delete(oaPeopleMediationVisit);
	}
	
	/**
	 * 签收任务
	 * @param act
	 */
	private void claim(Act act){
		String userId = UserUtils.getUser().getLoginName();
		actTaskService.claim(act.getTaskId(), userId);
	}
}