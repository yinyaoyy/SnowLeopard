/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.List;

import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.info.entity.InfoJudicialAuthentication;
import com.thinkgem.jeesite.modules.info.dao.InfoJudicialAuthenticationDao;

/**
 * 鉴定所管理Service
 * @author hejia
 * @version 2018-04-23
 */
@Service
@Transactional(readOnly = true)
public class InfoJudicialAuthenticationService extends CrudService<InfoJudicialAuthenticationDao, InfoJudicialAuthentication> implements ApiAgencySearch {
	@Autowired
	private SystemService systemService;

	@Autowired
	InfoJudicialAuthenticationDao judicialAuthenticationDao;
	
	@Autowired
	private OfficeService officeService;

	public InfoJudicialAuthentication get(String id) {
		return super.get(id);
	}
	
	public List<InfoJudicialAuthentication> findList(InfoJudicialAuthentication infoJudicialAuthentication) {
		return super.findList(infoJudicialAuthentication);
	}
	
	public Page<InfoJudicialAuthentication> findPage(Page<InfoJudicialAuthentication> page, InfoJudicialAuthentication infoJudicialAuthentication,User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		infoJudicialAuthentication.getSqlMap().put("dsf", systemService.dataFilter(user.getCurrentUser(),"o"));
				// 设置分页参数
		infoJudicialAuthentication.setPage(page);
				// 执行分页查询
				page.setList(dao.findList(infoJudicialAuthentication));
				return page;
	}
	
	@Transactional(readOnly = false)
	public void save(InfoJudicialAuthentication infoJudicialAuthentication) {
		if(StringUtils.isBlank(infoJudicialAuthentication.getId())){
			super.save(infoJudicialAuthentication);
			//自动向机构表写入一个记录，方便后续为公证处人员创建账户
			systemService.saveSysUserOfficeInfo(infoJudicialAuthentication.getId(), infoJudicialAuthentication.getName(), 
					OfficeRoleConstant.OFFICE_INFO_JUDICIAL_AUTHENTICATION, infoJudicialAuthentication.getArea(), 
					null, "info_judicial_authentication");
		}else{
			super.save(infoJudicialAuthentication);
		}

	}
	
	@Transactional(readOnly = false)
	public void delete(InfoJudicialAuthentication infoJudicialAuthentication) {
		officeService.deleteProfessionOffice(infoJudicialAuthentication.getId(),"2");
		super.delete(infoJudicialAuthentication);
	}

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
        InfoJudicialAuthentication infoJudicialAuthentication = new InfoJudicialAuthentication();
        Area area = new Area();
        area.setId(areaId);
        infoJudicialAuthentication.setArea(area);
        return dao.countByAreaId(infoJudicialAuthentication);
	}

	@Override
	public Object searchAgencyById(AgencyForm af) {
		return judicialAuthenticationDao.getInfo(af.getId());
	}
	@Override
	public void evaluationUpdate(String evaluation,String id,String remark) {}
}