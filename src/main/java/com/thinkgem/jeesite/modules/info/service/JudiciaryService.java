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
import com.thinkgem.jeesite.modules.info.entity.Judiciary;
import com.thinkgem.jeesite.modules.info.dao.JudiciaryDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 司法所Service
 * @author wanglin
 * @version 2018-06-08
 */
@Service
@Transactional(readOnly = true)
public class JudiciaryService extends CrudService<JudiciaryDao, Judiciary> implements ApiAgencySearch {
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;

	public Judiciary get(String id) {
		return super.get(id);
	}
	
	public List<Judiciary> findList(Judiciary judiciary) {
		return super.findList(judiciary);
	}
	
	public Page<Judiciary> findPage(Page<Judiciary> page, Judiciary judiciary,User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		judiciary.getSqlMap().put("dsf", systemService.dataFilter(user.getCurrentUser(),"o"));
				// 设置分页参数
		judiciary.setPage(page);
				// 执行分页查询
				page.setList(dao.findList(judiciary));
				return page;
		
	}
	
	@Transactional(readOnly = false)
	public void save(Judiciary judiciary) {
		if(StringUtils.isBlank(judiciary.getId())){
			Office query=new Office();
			query.setArea(judiciary.getArea());
			query.setName("司法局");
			Office offices=officeService.findListByCondition(query).get(0);
			Office newOffice=new Office();
			newOffice.setArea(judiciary.getTown());
			newOffice.setType("2");
			newOffice.setGrade("2");
			newOffice.setUseable("1");
			newOffice.setName(judiciary.getName());
			newOffice.setParent(offices);
			newOffice.setParentIds(offices.getParentIds()+offices.getId()+",");
			officeService.save(newOffice);
			super.save(judiciary);
			systemService.saveSysUserOfficeInfo(newOffice.getId(),judiciary.getId(),"1","judiciary");
		}else{
			super.save(judiciary);
		}
	}
	@Transactional(readOnly = false)
	public void delete(Judiciary judiciary) {
		officeService.deleteProfessionOffice(judiciary.getId(), "2");
		super.delete(judiciary);
	}

	/**
	 * 接口: 查询司法所工作人员信息
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

	/**
	 * 接口： 按地区统计司法所工作人员数量
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#countAgency(java.lang.String)
	 */
	@Override
	public int countAgency(String areaId) {
		return dao.count(new Judiciary(null, areaId));
	}
	@Override
	public Object searchAgencyById(AgencyForm af) {
		return dao.getById(af.getId());
	}
	@Override
	public void evaluationUpdate(String evaluation,String id,String remark) {}
}