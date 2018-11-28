/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.info.entity.PersonnelPrisonUser;
import com.thinkgem.jeesite.modules.info.dao.PersonnelPrisonUserDao;

/**
 * 在册安置帮教人员Service
 * @author huangtao
 * @version 2018-06-22
 */
@Service
@Transactional(readOnly = true)
public class PersonnelPrisonUserService extends CrudService<PersonnelPrisonUserDao, PersonnelPrisonUser> {

	public PersonnelPrisonUser get(String id) {
		return super.get(id);
	}
	
	public List<PersonnelPrisonUser> findList(PersonnelPrisonUser personnelPrisonUser) {
		return super.findList(personnelPrisonUser);
	}
	
	public Page<PersonnelPrisonUser> findPage(Page<PersonnelPrisonUser> page, PersonnelPrisonUser personnelPrisonUser) {
		return super.findPage(page, personnelPrisonUser);
	}
	
	@Transactional(readOnly = false)
	public void save(PersonnelPrisonUser personnelPrisonUser) {
		super.save(personnelPrisonUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(PersonnelPrisonUser personnelPrisonUser) {
		super.delete(personnelPrisonUser);
	}
	/**
	 * 接口: 查询安置人员信息
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	*/ 
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		// TODO Auto-generated method stub
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		av.setPage(page);
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
	}
	public Object searchAgencyById(AgencyForm af) {
		// TODO Auto-generated method stub
		return dao.getById(af.getId());
	}
    /**
     * 添加安置人员工作者
	 * @author huangtao
	 * @version 2018-6-12 
     * @param idCard
     * @return
     */
    public String personIdCard(String IdCard){
		
		return dao.personIdCard(IdCard);
	}
    
	/**
	 * 批量删除法援中心工作人员
	 * @author huangtao
	 * @version 2018-6-23
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		return dao.batchDelete(batchid.split(","));
	}

	
	
}