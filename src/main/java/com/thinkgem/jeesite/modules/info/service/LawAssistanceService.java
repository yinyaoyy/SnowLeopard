/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.info.entity.LawAssistance;
import com.thinkgem.jeesite.modules.info.dao.LawAssistanceDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 法援中心Service
 * @author wanglin
 * @version 2018-04-22
 */
@Service
@Transactional(readOnly = true)
public class LawAssistanceService extends CrudService<LawAssistanceDao, LawAssistance> implements ApiAgencySearch {
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;

	public LawAssistance get(String id) {
		return super.get(id);
	}
	
	public List<LawAssistance> findList(LawAssistance lawAssistance) {
		return super.findList(lawAssistance);
	}
	
	public Page<LawAssistance> findPage(Page<LawAssistance> page, LawAssistance lawAssistance,User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		lawAssistance.getSqlMap().put("dsf", systemService.dataFilter(user.getCurrentUser(),"o"));
				// 设置分页参数
		lawAssistance.setPage(page);
				// 执行分页查询
				page.setList(dao.findList(lawAssistance));
				return page;
	}
	
	@Transactional(readOnly = false)
	public void save(LawAssistance lawAssistance) {
	if(StringUtils.isBlank(lawAssistance.getId())){
			//自动向机构表写入一个记录，方便后续为法援中心工作人员创建账户
		    Office parentOffice=new Office();
		    parentOffice.setName("法律援助中心");
		    parentOffice.setArea(lawAssistance.getArea());
		    parentOffice=officeService.findListByCondition(parentOffice).get(0);
			Office newOffice=new Office();
			newOffice.setArea(lawAssistance.getArea());
			newOffice.setType("5");//机构类型为法援中心
			newOffice.setGrade("2");
			newOffice.setUseable("1");
			newOffice.setName(lawAssistance.getName());
			newOffice.setParent(parentOffice);
			newOffice.setParentIds(parentOffice.getParentIds()+parentOffice.getId()+",");
			officeService.save(newOffice);
			super.save(lawAssistance);
			systemService.saveSysUserOfficeInfo(newOffice.getId(),lawAssistance.getId(),"1","lawAssistance");
		}else{
			super.save(lawAssistance);
		}
	}
	@Transactional(readOnly = false)
	public void delete(LawAssistance lawAssistance) {
		officeService.deleteProfessionOffice(lawAssistance.getId(), "5");
		super.delete(lawAssistance);
	}

	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		page.setOrderBy("a7.code");
		av.setPage(page);
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
	}

	@Override
	public int countAgency(String areaId) {
		return dao.count(new LawAssistance("", areaId));
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgencyById(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public Object searchAgencyById(AgencyForm af) {
		// TODO Auto-generated method stub
		return dao.getById(af.getId());
	}
	@Override
	public void evaluationUpdate(String evaluation,String id,String remark) {}
	/**
	 * 法援中心区域验证
	 * @author 黄涛
	 * @version 2018-06-12
	 * @param batchid
	 * @return
	 */
	public String areaName(String name) {
		
		return dao.areaName(name);
	}

	
	
}