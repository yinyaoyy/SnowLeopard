/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.batik.gvt.filter.Mask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.api.act.entity.ProcessStateVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.oa.dao.OaProcessStateDao;
import com.thinkgem.jeesite.modules.oa.entity.OaProcessState;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 审批Service
 * @author thinkgem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OaProcessStateService extends CrudService<OaProcessStateDao, OaProcessState> {

	@Autowired
	private OaProcessStateDao dao;
	
	public OaProcessState get(String id) {
		return super.get(id);
	}
	
	/**
	 * 保存状态表
	 * @param id 业务ID
	 * @param procInsId 流程ID
	 * @param title 业务标题
	 * @param type 办理渠道
	 * @param state 状态
	 * @param createBy 创建人
	 * @param createDate 创建时间
	 * @param updateBy 更新者
	 * @param updateDate 更新时间
	 * @param types 0:创建、1:更新 
	 * @param caseType 案件类别
	 * @param caseArea 案发地区
	 */
	@Transactional(readOnly = false)
	public void save(String id,String procInsId,String title,String type,String state,User createBy,Date createDate,User updateBy,Date updateDate,String types,String caseType,String caseArea) {
		OaProcessState oaProcessState = new OaProcessState();
		oaProcessState.setId(id);
		oaProcessState.setProcInsId(procInsId);
		oaProcessState.setTitle(title);
		oaProcessState.setType(type);
		oaProcessState.setState(state);
		oaProcessState.setApplicantName(createBy.getLoginName());
		oaProcessState.setCreateBy(createBy);
		oaProcessState.setCreateDate(createDate);
		oaProcessState.setCaseType(caseType);
		oaProcessState.setCaseArea(caseArea);
		if(updateBy != null){
			oaProcessState.setUpdateBy(updateBy);
		}else{
			oaProcessState.setUpdateBy(createBy);
		}
		if(updateDate != null){
			oaProcessState.setUpdateDate(updateDate);
		}else{
			oaProcessState.setUpdateDate(createDate);
		}
		if(types.equals("0")){
			dao.insert(oaProcessState);
		}else{
			dao.update(oaProcessState);
		}
	}
	
	/**
	 * 改变状态为办理中/已退回
	 * @param procInsId 流程ID
	 * @param state 状态
	 * @param updateBy 更新者
	 * @param updateDate 更新时间
	 * @param comment 意见
	 * @param title 业务标题
	 * @param caseArea 案发地区
	 * @param handleDate 受理时间
	 * @param contractor 受理人
	 * @param severity 严重等级
	 */
	@Transactional(readOnly = false)
	public void save(String procInsId,String state,User updateBy,Date updateDate,String comment,String title ,String caseArea,Date handleDate,String contractor,String severity) {
		OaProcessState oaProcessState = new OaProcessState();
		oaProcessState.setProcInsId(procInsId);
		oaProcessState.setState(state);
		oaProcessState.setUpdateBy(updateBy);
		oaProcessState.setUpdateDate(updateDate);
		oaProcessState.setComment(comment);
		oaProcessState.setTitle(title);
		oaProcessState.setCaseArea(caseArea);
		oaProcessState.setHandleDate(handleDate);
		oaProcessState.setContractor(contractor);
		oaProcessState.setSeverity(severity);
		dao.update(oaProcessState);
	}
	
	/**
	 * 改变状态为已归档/作废/重新申请
	 * @param procInsId 流程ID
	 * @param state 状态
	 * @param updateBy 更新者 
	 * @param updateDate 更新时间
	 * @param comment 意见
	 *//*
	@Transactional(readOnly = false)
	public void save(String procInsId,String state,User updateBy,Date updateDate,String comment,String title ,String caseArea) {
		OaProcessState oaProcessState = new OaProcessState();
		oaProcessState.setProcInsId(procInsId);
		oaProcessState.setState(state);
		oaProcessState.setUpdateBy(updateBy);
		oaProcessState.setUpdateDate(updateDate);
		oaProcessState.setComment(comment);
		dao.update(oaProcessState);
	}*/
	
	/**
	 *更新流程中的名称和案发地区（申请中可以改动）
	 * @param procInsId 流程ID
	 * @param state 状态
	 * @param updateBy 更新者
	 * @param updateDate 更新时间
	 * @param comment 意见
	 * @param title 业务标题
	 * @param caseArea 案发地区
	 * @param handleDate 受理时间
	 * @param contractor 受理人
	 * @param severity 严重等级
	 *//*
	@Transactional(readOnly = false)
	public void save(String procInsId,String title ,String caseArea) {
		OaProcessState oaProcessState = new OaProcessState();
		oaProcessState.setProcInsId(procInsId);
		oaProcessState.setTitle(title);
		oaProcessState.setCaseArea(caseArea);
		dao.update(oaProcessState);
	}*/
	
	@Transactional(readOnly = false)
	public void delete(OaProcessState oaProcessState) {
		super.delete(oaProcessState);
	}
	
	/**
	 * 更新是否允许作废
	 * @param procInsId
	 */
	@Transactional(readOnly = false)
	public void end(String procInsId){
		dao.end(procInsId);
	};
	
	/**
	 * 查询是否允许作废
	 * @param procInsId
	 * @return
	 */
	public String getByIsDestroy(String procInsId){
		return dao.getByIsDestroy(procInsId);
	};
	
	/**
	 * 查询数据
	 * @param id
	 * @return
	 */
	public String getById(String id){
		return dao.getById(id);
	}
	
	/**
	 * 综合查询
	 * @param processStateVo
	 * @return
	 */
	public PageVo<ProcessStateVo> comprehensiveQueryForApi(ProcessStateVo processStateVo) {
		// TODO Auto-generated method stub
		
		List<ProcessStateVo> list = dao.comprehensiveQueryForApi(processStateVo);
		int count = dao.comprehensiveQueryCountForApi(processStateVo);
		processStateVo.getPage().setList(list);
		processStateVo.getPage().setCount(count);
		return new PageVo<ProcessStateVo>(processStateVo.getPage(), true);
	}

	/**
	 * 统计分析
	 */
	public List<Map<String, Object>> statisticsAnalysis(ProcessStateVo processStateVo) {
		// TODO Auto-generated method stub
		String[] colorList = {"#e6759b","#73c3ac","#6bbedf","#e77c72","#989df7","#ba5778","#b0c1ee","#c2f58f","#81dbe3","#dc8ff5","#f58fd7","#d8e773","#849fe5","#8575e6"};
		List<ProcessStateVo> list = dao.statisticsAnalysis(processStateVo);
		List<Map<String, Object>> resultList = Lists.newArrayList();
		Map<String, Object> resultMap =null;
		String type = processStateVo.getType();
		for(int i = 0 ; i< list.size() ; i++){
			resultMap = Maps.newHashMap();
			if("0".equals(type)){
				resultMap.put("count", list.get(i).getCount());
				resultMap.put("name", list.get(i).getCaseArea().getName());
				resultMap.put("color", colorList[i]);
				Map<String, Object> caseArea = Maps.newHashMap();
				caseArea.put("id", list.get(i).getCaseArea().getId());
				resultMap.put("caseArea", caseArea);
				resultMap.put("type", "");
				resultMap.put("caseType", "");
				resultMap.put("applyBeginDate",DateUtils.formatDate(processStateVo.getApplyBeginDate(), "yyyy-MM-dd"));
				resultMap.put("applyEndDate",DateUtils.formatDate(processStateVo.getApplyEndDate(), "yyyy-MM-dd"));
				
			}else if("1".equals(type)){
				resultMap.put("count", list.get(i).getCount());
				resultMap.put("name", DictUtils.getDictLabel(list.get(i).getType(), "oa_handle_channels"));
				resultMap.put("color", colorList[i]);
				resultMap.put("type",list.get(i).getType());
				resultMap.put("caseType", "");
				Map<String, Object> caseArea = Maps.newHashMap();
				caseArea.put("id", "");
				resultMap.put("caseArea", caseArea);
				resultMap.put("applyBeginDate",DateUtils.formatDate(processStateVo.getApplyBeginDate(), "yyyy-MM-dd"));
				resultMap.put("applyEndDate",DateUtils.formatDate(processStateVo.getApplyEndDate(), "yyyy-MM-dd"));
			}else if("2".equals(type)){
				resultMap.put("count", list.get(i).getCount());
				resultMap.put("name", DictUtils.getDictLabel(list.get(i).getCaseType(), "oa_handle_channels"));
				resultMap.put("color", colorList[i]);
				resultMap.put("caseType",list.get(i).getCaseType());
				resultMap.put("type", "");
				Map<String, Object> caseArea = Maps.newHashMap();
				caseArea.put("id", "");
				resultMap.put("caseArea", caseArea);
				resultMap.put("applyBeginDate",DateUtils.formatDate(processStateVo.getApplyBeginDate(), "yyyy-MM-dd"));
				resultMap.put("applyEndDate",DateUtils.formatDate(processStateVo.getApplyEndDate(), "yyyy-MM-dd"));
			}
			resultList.add(resultMap);
		}
		return resultList;
	}
	
	/**
		1.可以查询的机构现在有律师事务所,基层法律服务所,司法鉴定所,法援中心,公证处,人民调解委员会
		2.可以选择的地区加上全部一共有15个
		3.机构类型应该是字典,加上全部一共是7个
		4.如果没有参数,那就按照地区区分,把所有的找到
		5.如果只有地区参数,那么就按机构类型区分
		6.如果有地区,有机构,那就按具体的机构区分
		7.如果有地区,有机构,有具体的机构,就按机构下的人员区分
		8.如果有地区,有机构,有具体的机构,有机构的人员,那就按时间间隔区分
	 * @param processStateVo
	 * @return
	 */
	public List<Map<String, Object>> contractorQuery(ProcessStateVo processStateVo){
		List<Map<String, Object>> resultList = Lists.newArrayList();
		List<ProcessStateVo> list = dao.contractorQuery(processStateVo);
		
		for(int i = 0 ; i < list.size() ; i++ ){
			
		}
		return resultList;
	}
}
