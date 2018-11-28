/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.info.dao.CorrectUserDao;
import com.thinkgem.jeesite.modules.info.entity.CorrectUser;
import com.thinkgem.jeesite.modules.info.entity.PrisonUser;

/**
 * 社区矫正人员Service
 * @author liujiangling
 * @version 2018-06-25
 */
@Service
@Transactional(readOnly = true)
public class CorrectUserService extends CrudService<CorrectUserDao, CorrectUser> implements ApiAgencySearch{

	public CorrectUser get(String id) {
		return super.get(id);
	}
	
	public List<CorrectUser> findList(CorrectUser correctUser) {
		return super.findList(correctUser);
	}
	
	public Page<CorrectUser> findPage(Page<CorrectUser> page, CorrectUser correctUser) {
		return super.findPage(page, correctUser);
	}
	
	@Transactional(readOnly = false)
	public void save(CorrectUser correctUser) {
		super.save(correctUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(CorrectUser correctUser) {
		super.delete(correctUser);
	}
	
	/**
	 * 接口: 查询社区矫正人员信息
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	*/ 
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		AgencyVo av = new AgencyVo();
		av.setAgencyName(af.getName());
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
	 * 批量删除社区矫正人员
	 * @author 
	 * @version 
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		return dao.batchDelete(batchid.split(","));
	}
	public CorrectUser getByIdCard(String idCard) {
		return dao.getByIdCard(idCard);
	}
}