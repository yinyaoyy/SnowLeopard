/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.info.dao.PrisonUserDao;
import com.thinkgem.jeesite.modules.info.entity.PrisonUser;


/**
 * 在监服刑人员Service
 * @author liujiangling
 * @version 2018-06-21
 */
@Service
@Transactional(readOnly = true)
public class PrisonUserService extends CrudService<PrisonUserDao, PrisonUser> implements ApiAgencySearch{

	public PrisonUser get(String id) {
		return super.get(id);
	}
	
	public List<PrisonUser> findList(PrisonUser prisonUser) {
		return super.findList(prisonUser);
	}
	
	public Page<PrisonUser> findPage(Page<PrisonUser> page, PrisonUser prisonUser) {
		return super.findPage(page, prisonUser);
	}
	
	@Transactional(readOnly = false)
	public void save(PrisonUser prisonUser) {
		super.save(prisonUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(PrisonUser prisonUser) {
		super.delete(prisonUser);
	}
	/**
	 * 接口: 查询在监服刑人员信息
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	*/ 
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		av.setPage(page);
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
	}
	
	@Override
	public int countAgency(String areaId) {
		return dao.count(new PrisonUser(null, areaId));
	}
	@Override
	public Object searchAgencyById(AgencyForm af) {
		return dao.getById(af.getId());
	}
	@Override
	public void evaluationUpdate(String evaluation,String id,String remark) {}
	
	/**
	 * 批量删除在监服刑人员
	 * @author 
	 * @version 
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		return dao.batchDelete(batchid.split(","));
	}
	
	/**
	 * 在监服刑人员身份证验证
	 * @author 刘江陵
	 * @version 
	 * @param batchid
	 * @return
	 */
	public String personIdCard(String Card){
		String IdCard = dao.personIdCard(Card);
		return IdCard;
	}
	
}