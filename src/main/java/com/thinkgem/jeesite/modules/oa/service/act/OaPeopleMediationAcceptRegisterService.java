/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service.act;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.oa.dao.act.OaPeopleMediationAcceptRegisterDao;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAcceptRegister;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAcceptRegisterCount;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.service.OaProcessStateService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人民调解受理登记Service
 * @author zhangqiang
 * @version 2018-05-24
 */
@Service
@Transactional(readOnly = true)
public class OaPeopleMediationAcceptRegisterService extends CrudService<OaPeopleMediationAcceptRegisterDao, OaPeopleMediationAcceptRegister> {

	public static final String[] PD_LEGAL_AID = new String[]{"people_mediation", "oa_people_mediation_accept_register"};
	
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private OaProcessStateService oaProcessStateService;
	
	public OaPeopleMediationAcceptRegister get(String id) {

		OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister = super.get(id);
		if(oaPeopleMediationAcceptRegister.getOaPeopleMediationApply()==null){
			oaPeopleMediationAcceptRegister.setOaPeopleMediationApply(new OaPeopleMediationApply());
		}
		return oaPeopleMediationAcceptRegister;
	}
	
	public List<OaPeopleMediationAcceptRegister> findList(OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister) {
		return super.findList(oaPeopleMediationAcceptRegister);
	}
	
	public Page<OaPeopleMediationAcceptRegister> findPage(Page<OaPeopleMediationAcceptRegister> page, OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister) {
		return super.findPage(page, oaPeopleMediationAcceptRegister);
	}
	
	@Transactional(readOnly = false)
	public void save(OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister) {
//		super.save(oaPeopleMediationAcceptRegister);
		if( StringUtils.isBlank(oaPeopleMediationAcceptRegister.getId())){
			oaPeopleMediationAcceptRegister.preInsert();
			dao.insert(oaPeopleMediationAcceptRegister);
		}else{
			super.save(oaPeopleMediationAcceptRegister);
		}
	}

	@Transactional(readOnly = false)
	public void submit(OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister) {
		
		Map<String, Object> vars = Maps.newHashMap();
		// 设置意见
//		oaPeopleMediationAcceptRegister.getAct().setComment(("yes".equals(oaPeopleMediationAcceptRegister.getAct().getFlag())?"[同意] ":"[驳回] ")+oaPeopleMediationAcceptRegister.getAct().getComment());
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaPeopleMediationAcceptRegister.getAct().getTaskDefKey();
		if("mediation_dengji".equals(taskDefKey)){
			//领取任务
			claim(oaPeopleMediationAcceptRegister.getAct());
			//涉及人员是否超过安全阈值
			if(!"".equals(oaPeopleMediationAcceptRegister.getCaseInvolveCount())){
				int overThresholdNumber = Integer.parseInt(Global.getConfig("threshold"));
				int CaseInvolveCount = Integer.parseInt(oaPeopleMediationAcceptRegister.getCaseInvolveCount());
				if(CaseInvolveCount>=overThresholdNumber){
					oaPeopleMediationAcceptRegister.setOverThreshold("1");
				}
			}
		}

		save(oaPeopleMediationAcceptRegister);
		actTaskService.complete(oaPeopleMediationAcceptRegister.getAct().getTaskId(), oaPeopleMediationAcceptRegister.getAct().getProcInsId(), oaPeopleMediationAcceptRegister.getAct().getComment(), vars);
		if("mediation_dengji".equals(oaPeopleMediationAcceptRegister.getAct().getTaskDefKey())){
			//更新流程状态(已退回)
			oaProcessStateService.save(oaPeopleMediationAcceptRegister.getAct().getProcInsId(),"4",oaPeopleMediationAcceptRegister.getUpdateBy(),oaPeopleMediationAcceptRegister.getUpdateDate(),oaPeopleMediationAcceptRegister.getAct().getComment(),null,null,new Date(),UserUtils.getUser().getId(),oaPeopleMediationAcceptRegister.getCaseRank());
		}

	}
	@Transactional(readOnly = false)
	public void delete(OaPeopleMediationAcceptRegister oaPeopleMediationAcceptRegister) {
		super.delete(oaPeopleMediationAcceptRegister);
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
	 * 根据年度旗县严重等级统计
	 * 年度旗县严重等级数量(单柱状)
	 * @author 王鹏
	 * @version 2018-6-4 16:56:15
	 * @param oac
	 * @return
	 */
	public List<OaPeopleMediationAcceptRegisterCount> countByYearCaseRank(OaPeopleMediationAcceptRegisterCount opmarc){
		if(opmarc == null) {
			throw new BusinessException("参数不全");
		}
		return dao.countByYearCaseRank(opmarc);
	}
	
	/**
	 * 年度旗县严重等级数量 按地区和日期显示一个表格
	 * @author 王鹏
	 * @version 2018-07-17 20:08:50
	 * @param opmarc
	 * @return
	 */
	public List<List<String>> countByYearCaseRankDate(OaPeopleMediationAcceptRegisterCount opmarc, List<Area> areaList){
		List<List<String>> resultList = Lists.newArrayList();
		opmarc.setGroupByDate("true");//增加按日期统计
		List<OaPeopleMediationAcceptRegisterCount> countList = dao.countByYearCaseRank(opmarc);
		//拿到两个日期之间的全部结果
		List<String> dateList = DateUtils.getAllDayBetweenDate(opmarc.getBeginDate(), opmarc.getEndDate());
		//循环数据
		List<String> td = null;//数据集合
		OaPeopleMediationAcceptRegisterCount countOpmarc = null;//统计数据
		boolean isArea = false;//比对是否与当前日期和地区匹配,默认没有匹配
		for (int i = 0; i < dateList.size(); i++) {
			td = Lists.newArrayList();
			td.add(dateList.get(i));//放入时间
			//保存地区对应的数据
			for (int j = 0; j < areaList.size(); j++) {
				//是否匹配到了地区
				//比对是否与当前日期和地区匹配
				isArea = false;//默认没有匹配
				for (int k = 0; k < countList.size(); k++) {
					countOpmarc = countList.get(k);//拿到统计数据
					if(dateList.get(i).equals(countOpmarc.getCountDate())
							&& areaList.get(j).getId().equals(countOpmarc.getArea().getId())) {
						td.add(String.valueOf(countOpmarc.getCount()));
						isArea = true;//匹配到了，就退出此次循环
						break;
					}
				}
				if(!isArea) {//如果没有匹配到，则补0
					td.add("0");
				}
			}
			resultList.add(td);//一条数据添加完成
		}
		return resultList;
	}
}