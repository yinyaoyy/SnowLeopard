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
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAgreement;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.dao.act.OaPeopleMediationAgreementDao;

/**
 * 人民调解协议书Service
 * @author zhangqiang
 * @version 2018-05-28
 */
@Service
@Transactional(readOnly = true)
public class OaPeopleMediationAgreementService extends CrudService<OaPeopleMediationAgreementDao, OaPeopleMediationAgreement> {
	
	public static final String[] PD_LEGAL_AID = new String[]{"people_mediation", "oa_people_mediation_agreement"};
	
	@Autowired
	private ActTaskService actTaskService;
	
	public OaPeopleMediationAgreement get(String id) {
		//返回数据的时候判断下协议书编号是不是空，是的话生成一个
		OaPeopleMediationAgreement oaPeopleMediationAgreement = super.get(id);
		if(oaPeopleMediationAgreement.getAgreementCode()==null){
			oaPeopleMediationAgreement.setAgreementCode(getAgreementCode());
		}
		if(oaPeopleMediationAgreement.getOaPeopleMediationApply()==null){
			oaPeopleMediationAgreement.setOaPeopleMediationApply(new OaPeopleMediationApply());
		}
		
		return oaPeopleMediationAgreement;
	}
	
	public List<OaPeopleMediationAgreement> findList(OaPeopleMediationAgreement oaPeopleMediationAgreement) {
		return super.findList(oaPeopleMediationAgreement);
	}
	
	public Page<OaPeopleMediationAgreement> findPage(Page<OaPeopleMediationAgreement> page, OaPeopleMediationAgreement oaPeopleMediationAgreement) {
		return super.findPage(page, oaPeopleMediationAgreement);
	}
	
	@Transactional(readOnly = false)
	public void save(OaPeopleMediationAgreement oaPeopleMediationAgreement) {
//		super.save(oaPeopleMediationAgreement);
		if( StringUtils.isBlank(oaPeopleMediationAgreement.getId())){
			oaPeopleMediationAgreement.preInsert();
			dao.insert(oaPeopleMediationAgreement);
		}else{
			super.save(oaPeopleMediationAgreement);
		}
	}
	
	/**
	 * 提交协议书，进入下一步
	 * @param oaPeopleMediationExamine
	 */
	@Transactional(readOnly = false)
	public void submit(OaPeopleMediationAgreement oaPeopleMediationAgreement) {
//		oaPeopleMediationAgreement.setAgreementCode(getAgreementCode());
		save(oaPeopleMediationAgreement);
		// 设置意见
//		oaPeopleMediationAgreement.getAct().setComment(("yes".equals(oaPeopleMediationAgreement.getAct().getFlag())?"[同意] ":"[驳回] ")+oaPeopleMediationAgreement.getAct().getComment());
		// 判断是否是调解环节
		String taskDefKey = oaPeopleMediationAgreement.getAct().getTaskDefKey();
		if("mediation_xieyi".equals(taskDefKey)){
			//领取任务
			claim(oaPeopleMediationAgreement.getAct());
		}
		// 提交流程任务
		actTaskService.complete(oaPeopleMediationAgreement.getAct().getTaskId(), oaPeopleMediationAgreement.getAct().getProcInsId(), oaPeopleMediationAgreement.getAct().getComment(),null);		
	
	}

	@Transactional(readOnly = false)
	public void delete(OaPeopleMediationAgreement oaPeopleMediationAgreement) {
		super.delete(oaPeopleMediationAgreement);
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
	 * 获取
	 * @return
	 */
	private String getAgreementCode(){
		int number = 0;
		synchronized(this){
			number = (int) (Math.random()*9000+1000);
		}
		String result = DateUtils.getDateToString()+number;
		return result;
	}
}