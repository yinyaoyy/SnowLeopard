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
import com.thinkgem.jeesite.modules.info.entity.LowOffice;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.info.dao.LowOfficeDao;

/**
 * 律师事务所Service
 * @author 王鹏
 * @version 2018-04-18
 */
@Service
@Transactional(readOnly = true)
public class LowOfficeService extends CrudService<LowOfficeDao, LowOffice> implements ApiAgencySearch  {

	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	
	public LowOffice get(String id) {
		return super.get(id);
	}
	
	public List<LowOffice> findList(LowOffice lowOffice) {
		return super.findList(lowOffice);
	}
	
	public Page<LowOffice> findPage(Page<LowOffice> page, LowOffice lowOffice,User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		lowOffice.getSqlMap().put("dsf", systemService.dataFilter(user.getCurrentUser(),"o"));
		// 设置分页参数
		lowOffice.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(lowOffice));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(LowOffice lowOffice) {
		if(StringUtils.isBlank(lowOffice.getId())){
			String imageUrl = lowOffice.getImageUrl();
			if(StringUtils.isNotBlank(imageUrl)){
				imageUrl="/userfiles/law_office/"+lowOffice.getCreditCode()+".jpg";
			}
			lowOffice.setImageUrl(imageUrl);
			super.save(lowOffice);
			//自动向机构表写入一个记录，方便后续为律师创建账户
			systemService.saveSysUserOfficeInfo(lowOffice.getId(), lowOffice.getName(), 
					OfficeRoleConstant.OFFICE_LAWYER_OFFICE, lowOffice.getArea(), 
					null, "law_office");
		}else{
			super.save(lowOffice);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(LowOffice lowOffice) {
		//删除对应机构并且效验
		officeService.deleteProfessionOffice(lowOffice.getId(), "2");
			//删除律所表里的数据
			dao.delete(lowOffice);
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
		return dao.count(new LowOffice("", areaId));
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
		String[] strings = batchid.split(",");
		for(String jj:strings) {
			//删除对应机构并且效验
			officeService.deleteProfessionOffice(jj, "2");
		}
		return dao.batchDelete(batchid.split(","));
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgencyById(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public Object searchAgencyById(AgencyForm af) {
		// TODO Auto-g才enerated method stub
		return dao.getInfo(af.getId());
	}
	@Override
	public void evaluationUpdate(String evaluation,String id,String remark) {}

	public boolean getLawOfficeByName(String name) {
		LowOffice  o = dao.getLawOfficeByName(name);
		if(o!=null){
			return true;
		}
		return false;
	}
}