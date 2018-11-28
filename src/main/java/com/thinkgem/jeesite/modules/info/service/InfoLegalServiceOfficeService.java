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
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.info.dao.InfoLegalServiceOfficeDao;
import com.thinkgem.jeesite.modules.info.entity.InfoLegalServiceOffice;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 基层法律服务所Service
 * @author hejia
 * @version 2018-04-24
 */
@Service
@Transactional(readOnly = true)
public class InfoLegalServiceOfficeService extends CrudService<InfoLegalServiceOfficeDao, InfoLegalServiceOffice>
	
implements ApiAgencySearch{

	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;

	public InfoLegalServiceOffice get(String id) {
		return super.get(id);
	}
	
	public List<InfoLegalServiceOffice> findList(InfoLegalServiceOffice infoLegalServiceOffice) {
		return super.findList(infoLegalServiceOffice);
	}
	
	public Page<InfoLegalServiceOffice> findPage(Page<InfoLegalServiceOffice> page, InfoLegalServiceOffice infoLegalServiceOffice,User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		infoLegalServiceOffice.getSqlMap().put("dsf", systemService.dataFilter(user.getCurrentUser(),"o"));
				// 设置分页参数
		infoLegalServiceOffice.setPage(page);
				// 执行分页查询
				page.setList(dao.findList(infoLegalServiceOffice));
				return page;
	}
	
	@Transactional(readOnly = false)
	public void save(InfoLegalServiceOffice infoLegalServiceOffice) {
//		super.save(infoLegalServiceOffice);
		if(StringUtils.isBlank(infoLegalServiceOffice.getId())){
			super.save(infoLegalServiceOffice);
			//自动向机构表写入一个记录，方便后续为基层法律服务人员创建账户
			systemService.saveSysUserOfficeInfo(infoLegalServiceOffice.getId(), infoLegalServiceOffice.getName(), 
					OfficeRoleConstant.OFFICE_LEGAL_SERVICE, infoLegalServiceOffice.getArea(), 
					null, "legal_service_office");
		}else{
			super.save(infoLegalServiceOffice);
		}

	}
	
	@Transactional(readOnly = false)
	public void delete(InfoLegalServiceOffice infoLegalServiceOffice) {
		officeService.deleteProfessionOffice(infoLegalServiceOffice.getId(), "2");
		super.delete(infoLegalServiceOffice);
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId(),af.getTownId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		av.setPage(page);
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#countAgency(java.lang.String)
	 */
	@Override
	public int countAgency(String areaId) {
		// TODO Auto-generated method stub
		return dao.count(new InfoLegalServiceOffice("", areaId));
	}

	/**
	 * 批量删除律师事务所
	 * @author 王鹏
	 * @version 2018-04-20 11:32:26
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		return dao.batchDelete(batchid.split(","));
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgencyById(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public Object searchAgencyById(AgencyForm af) {
		// TODO Auto-generated method stub
		return dao.getById(af.getId());
	}
	
	/**
	 * 机构验证
	 * @author 刘江陵
	 * @version 2018-6-10 17:50:25
	 * @param batchid
	 * @return
	 */
	public String personInstitution(String no,String areaId,String townId){
		String name = dao.personInstitution(no,areaId,townId);
		return name;
	}
	@Override
	public void evaluationUpdate(String evaluation,String id,String remark) {}
	
}