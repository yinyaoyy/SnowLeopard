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
import com.thinkgem.jeesite.modules.info.entity.NotaryAgency;
import com.thinkgem.jeesite.modules.info.dao.NotaryAgencyDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 公证机构Service
 * @author 王鹏
 * @version 2018-04-23
 */
@Service
@Transactional(readOnly = true)
public class NotaryAgencyService extends CrudService<NotaryAgencyDao, NotaryAgency> implements ApiAgencySearch{
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	
	public NotaryAgency get(String id) {
		return super.get(id);
	}
	
	public List<NotaryAgency> findList(NotaryAgency notaryAgency) {
		return super.findList(notaryAgency);
	}
	
	public Page<NotaryAgency> findPage(Page<NotaryAgency> page, NotaryAgency notaryAgency,User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		notaryAgency.getSqlMap().put("dsf", systemService.dataFilter(user.getCurrentUser(),"o14"));
				// 设置分页参数
		notaryAgency.setPage(page);
				// 执行分页查询
				page.setList(dao.findList(notaryAgency));
				return page;
	}
	
	@Transactional(readOnly = false)
	public void save(NotaryAgency notaryAgency) {
		if(StringUtils.isBlank(notaryAgency.getId())){
			super.save(notaryAgency);
			//自动向机构表写入一个记录，方便后续为公证处人员创建账户
			systemService.saveSysUserOfficeInfo(notaryAgency.getId(), notaryAgency.getName(), 
					OfficeRoleConstant.OFFICE_NOTARY_SERVICE, notaryAgency.getArea(), 
					null, "notary_agency_office");
		}else{
			super.save(notaryAgency);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(NotaryAgency notaryAgency) {
		officeService.deleteProfessionOffice(notaryAgency.getId(), "2");
		super.delete(notaryAgency);
	}
	

	/**
	 * 接口: 查询机构信息
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

	/**
	 * 接口： 统计机构数量
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#countAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public int countAgency(String areaId) {
		// TODO Auto-generated method stub
		return dao.count(new NotaryAgency("", areaId));
	}

	/**
	 * 批量删除律师事务所
	 * @author 王鹏
	 * @version 2018-5-3 17:57:48
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
		return dao.getInfo(af.getId());
	}
	@Override
	public void evaluationUpdate(String evaluation,String id,String remark) {}
	
	/**
	 * 机构验证
	 * @author 刘江陵
	 * @version 2018-6-10 17:50:25
	 * @param batchid
	 * @return
	 */
	public String personInstitution(String licenseNumber,String areaId){
		String name = dao.personInstitution(licenseNumber,areaId);
		return name;
	}
}