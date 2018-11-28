/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service.act;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationExamine;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.dao.act.OaPeopleMediationExamineDao;

/**
 * 人民调解调查记录Service
 * @author zhangqiang
 * @version 2018-05-28
 */
@Service
@Transactional(readOnly = true)
public class OaPeopleMediationExamineService extends CrudService<OaPeopleMediationExamineDao, OaPeopleMediationExamine> {
	
	@Autowired
	private ActTaskService actTaskService;
	
	public static final String[] PD_LEGAL_AID = new String[]{"people_mediation", "oa_people_mediation_examine"};
	
	public OaPeopleMediationExamine get(String id) {
		OaPeopleMediationExamine oaPeopleMediationExamine = super.get(id);
		if(oaPeopleMediationExamine.getOaPeopleMediationApply()==null){
			oaPeopleMediationExamine.setOaPeopleMediationApply(new OaPeopleMediationApply());
		}
		return oaPeopleMediationExamine;
	}
	
	public List<OaPeopleMediationExamine> findList(OaPeopleMediationExamine oaPeopleMediationExamine) {
		return super.findList(oaPeopleMediationExamine);
	}
	
	public Page<OaPeopleMediationExamine> findPage(Page<OaPeopleMediationExamine> page, OaPeopleMediationExamine oaPeopleMediationExamine) {
		return super.findPage(page, oaPeopleMediationExamine);
	}
	
	@Transactional(readOnly = false)
	public void save(OaPeopleMediationExamine oaPeopleMediationExamine) {
//		super.save(oaPeopleMediationExamine);
		if( StringUtils.isBlank(oaPeopleMediationExamine.getId())){
			oaPeopleMediationExamine.preInsert();
			dao.insert(oaPeopleMediationExamine);
		}else{
			super.save(oaPeopleMediationExamine);
		}
	}
	

	/**
	 * 提交调查记录表，进入下一步
	 * @param oaPeopleMediationExamine
	 */
	@Transactional(readOnly = false)
	public void submit(OaPeopleMediationExamine oaPeopleMediationExamine) {
		save(oaPeopleMediationExamine);
		// 设置意见
//		oaPeopleMediationExamine.getAct().setComment(("yes".equals(oaPeopleMediationExamine.getAct().getFlag())?"[同意] ":"[驳回] ")+oaPeopleMediationExamine.getAct().getComment());
		// 判断是否是调查环节
		String taskDefKey = oaPeopleMediationExamine.getAct().getTaskDefKey();
		if("mediation_diaocha".equals(taskDefKey)){
			//领取任务
			claim(oaPeopleMediationExamine.getAct());
			// 提交流程任务
		}
		actTaskService.complete(oaPeopleMediationExamine.getAct().getTaskId(), oaPeopleMediationExamine.getAct().getProcInsId(), oaPeopleMediationExamine.getAct().getComment(),null);		
	}
	@Transactional(readOnly = false)
	public void delete(OaPeopleMediationExamine oaPeopleMediationExamine) {
		super.delete(oaPeopleMediationExamine);
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