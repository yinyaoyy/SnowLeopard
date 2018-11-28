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
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationRecord;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.dao.act.OaPeopleMediationRecordDao;

/**
 * 人民调解调解记录Service
 * @author zhangqiang
 * @version 2018-05-28
 */
@Service
@Transactional(readOnly = true)
public class OaPeopleMediationRecordService extends CrudService<OaPeopleMediationRecordDao, OaPeopleMediationRecord> {
	
	@Autowired
	private ActTaskService actTaskService;
	
	public static final String[] PD_LEGAL_AID = new String[]{"people_mediation", "oa_people_mediation_record"};

	public OaPeopleMediationRecord get(String id) {
		OaPeopleMediationRecord oaPeopleMediationRecord = super.get(id);
		if(oaPeopleMediationRecord.getOaPeopleMediationApply()==null){
			oaPeopleMediationRecord.setOaPeopleMediationApply(new OaPeopleMediationApply());
		}
		if(oaPeopleMediationRecord.getOaPeopleMediationExamine()==null){
			oaPeopleMediationRecord.setOaPeopleMediationExamine(new OaPeopleMediationExamine());
		}
		return oaPeopleMediationRecord;
	}
	
	public List<OaPeopleMediationRecord> findList(OaPeopleMediationRecord oaPeopleMediationRecord) {
		return super.findList(oaPeopleMediationRecord);
	}
	
	public Page<OaPeopleMediationRecord> findPage(Page<OaPeopleMediationRecord> page, OaPeopleMediationRecord oaPeopleMediationRecord) {
		return super.findPage(page, oaPeopleMediationRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(OaPeopleMediationRecord oaPeopleMediationRecord) {
//		super.save(oaPeopleMediationRecord);
		if( StringUtils.isBlank(oaPeopleMediationRecord.getId())){
			oaPeopleMediationRecord.preInsert();
			dao.insert(oaPeopleMediationRecord);
		}else{
			super.save(oaPeopleMediationRecord);
		}
	}
	
	/**
	 * 提交调解记录，进入下一步
	 * @param oaPeopleMediationRecord
	 */
	@Transactional(readOnly = false)
	public void submit(OaPeopleMediationRecord oaPeopleMediationRecord) {
		save(oaPeopleMediationRecord);
		// 设置意见
//		oaPeopleMediationRecord.getAct().setComment(("yes".equals(oaPeopleMediationRecord.getAct().getFlag())?"[同意] ":"[驳回] ")+oaPeopleMediationRecord.getAct().getComment());
		// 判断是否是调解环节
		String taskDefKey = oaPeopleMediationRecord.getAct().getTaskDefKey();
		if("mediation_tiaojie".equals(taskDefKey)){
			//领取任务
			claim(oaPeopleMediationRecord.getAct());
		}
		// 提交流程任务
		actTaskService.complete(oaPeopleMediationRecord.getAct().getTaskId(), oaPeopleMediationRecord.getAct().getProcInsId(), oaPeopleMediationRecord.getAct().getComment(),null);		
	
	}

	@Transactional(readOnly = false)
	public void delete(OaPeopleMediationRecord oaPeopleMediationRecord) {
		super.delete(oaPeopleMediationRecord);
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