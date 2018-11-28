/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.OaDossier;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAgreement;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.dao.OaDossierDao;

/**
 * 卷宗说明Service
 * @author zhangqiang
 * @version 2018-05-28
 */
@Service
@Transactional(readOnly = true)
public class OaDossierService extends CrudService<OaDossierDao, OaDossier> {

	public static final String[] PD_LEGAL_AID = new String[]{"people_mediation", "oa_dossier"};
	
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private OaProcessStateService oaProcessStateService;
	
	public OaDossier get(String id) {
		OaDossier oaDossier = super.get(id);
		if(oaDossier.getOaPeopleMediationAgreement()==null){
			oaDossier.setOaPeopleMediationAgreement(new OaPeopleMediationAgreement());
		}
		if(oaDossier.getOaPeopleMediationApply()==null){
			oaDossier.setOaPeopleMediationApply(new OaPeopleMediationApply());
		}
		
		return oaDossier;
	}
	
	public List<OaDossier> findList(OaDossier oaDossier) {
		return super.findList(oaDossier);
	}
	
	public Page<OaDossier> findPage(Page<OaDossier> page, OaDossier oaDossier) {
		return super.findPage(page, oaDossier);
	}
	
	@Transactional(readOnly = false)
	public void save(OaDossier oaDossier) {
//		super.save(oaDossier);
		if( StringUtils.isBlank(oaDossier.getId())){
			oaDossier.preInsert();
			dao.insert(oaDossier);
		}else{
			super.save(oaDossier);
		}
	}
	
	@Transactional(readOnly = false)
	public void submit(OaDossier oaDossier) {
		
		// 设置意见
//		oaDossier.getAct().setComment(("yes".equals(oaDossier.getAct().getFlag())?"[同意] ":"[驳回] ")+oaDossier.getAct().getComment());
		
		String taskDefKey = oaDossier.getAct().getTaskDefKey();
		if("mediation_juanzong".equals(taskDefKey)){
			//领取任务
			claim(oaDossier.getAct());
		}
		oaDossier.setStatus("1");
		save(oaDossier);
		// 提交流程任务
		actTaskService.complete(oaDossier.getAct().getTaskId(), oaDossier.getAct().getProcInsId(), oaDossier.getAct().getComment(),null);
		//更新流程状态(已办结)
		oaProcessStateService.save(oaDossier.getAct().getProcInsId(),"2",oaDossier.getUpdateBy(),oaDossier.getUpdateDate(),null,null,null,null,null,null);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaDossier oaDossier) {
		super.delete(oaDossier);
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