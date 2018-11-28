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
import com.thinkgem.jeesite.modules.info.dao.PeopleMediationCommitteeDao;
import com.thinkgem.jeesite.modules.info.entity.PeopleMediationCommittee;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 人民调解委员会Service
 * @author wanglin
 * @version 2018-05-23
 */
@Service
@Transactional(readOnly = true)
public class PeopleMediationCommitteeService extends CrudService<PeopleMediationCommitteeDao, PeopleMediationCommittee> implements ApiAgencySearch{
   //字符补0
	public static String addZeroForNum(String str, int strLength) {  
        int strLen = str.length();  
        if (strLen < strLength) {  
            while (strLen < strLength) {  
                StringBuffer sb = new StringBuffer();  
                sb.append("0").append(str);// 左补0   
                str = sb.toString();  
                strLen = str.length();  
            }  
        }  
      
        return str;  
    }  
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	public PeopleMediationCommittee get(String id) {
		return super.get(id);
	}
	
	public List<PeopleMediationCommittee> findList(PeopleMediationCommittee peopleMediationCommittee) {
		return super.findList(peopleMediationCommittee);
	}
	
	public Page<PeopleMediationCommittee> findPage(Page<PeopleMediationCommittee> page, PeopleMediationCommittee peopleMediationCommittee,User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		peopleMediationCommittee.getSqlMap().put("dsf", systemService.dataFilter(user.getCurrentUser(),"o7"));
				// 设置分页参数
		peopleMediationCommittee.setPage(page);
				// 执行分页查询
				page.setList(dao.findList(peopleMediationCommittee));
				return page;
	}
	
	@Transactional(readOnly = false)
	public void save(PeopleMediationCommittee peopleMediationCommittee) {
		if(StringUtils.isBlank(peopleMediationCommittee.getId())){
			//自动向机构表写入一个记录
			Office office=new Office();
			office.setName("调委会");
			Office parent=new Office(peopleMediationCommittee.getOffice().getId());
			office.setParent(parent);
			Office offices= officeService.getByParentAndName(office);
			if(offices==null){
				offices=new Office();
				offices.setArea(peopleMediationCommittee.getArea());
				offices.setType("4");
				offices.setGrade("2");
				offices.setUseable("1");
				offices.setName("调委会");
				offices.setParent(peopleMediationCommittee.getOffice());
				offices.setParentIds(peopleMediationCommittee.getOffice().getParentIds()+peopleMediationCommittee.getOffice().getId()+",");
				officeService.save(offices);
			}
			Office newOffice=new Office();
			newOffice.setArea(peopleMediationCommittee.getArea());
			newOffice.setType("2");
			newOffice.setGrade("2");
			newOffice.setUseable("1");
			newOffice.setName(peopleMediationCommittee.getName());
			newOffice.setParent(offices);
			newOffice.setParentIds(offices.getParentIds()+offices.getId()+",");
			officeService.save(newOffice);
			String licenseNumber=peopleMediationCommittee.getLicenseNumber();
			//生成处理6位执业证号方法
			if(licenseNumber == null || licenseNumber.length() <= 0) {
				int Number=dao.counte("0");
				++Number ;
				//System.out.println(Number);
				String str= String.valueOf(Number);
				licenseNumber=addZeroForNum(str,6);
				peopleMediationCommittee.setLicenseNumber(licenseNumber);
			}
			
			super.save(peopleMediationCommittee);
			systemService.saveSysUserOfficeInfo(newOffice.getId(),peopleMediationCommittee.getId(),"1","peopleMediationCommittee");
		}else{
			String licenseNumber=peopleMediationCommittee.getLicenseNumber();
			if(licenseNumber == null || licenseNumber.length() <= 0) {
				int Number=dao.counte("0");
				String str= String.valueOf(Number);
				licenseNumber=addZeroForNum(str,6);
			}
			peopleMediationCommittee.setLicenseNumber(licenseNumber);
			super.save(peopleMediationCommittee);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(PeopleMediationCommittee peopleMediationCommittee) {
		officeService.deleteProfessionOffice(peopleMediationCommittee.getId(), "2");
		super.delete(peopleMediationCommittee);
	}

	/**
	 * 批量删除人民调解委员会
	 * @author 
	 * @version 2018-04-20 11:32:26
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		String[] strings = batchid.split(",");
		for(String hh:strings) {
			officeService.deleteProfessionOffice(hh, "2");
		}
		return dao.batchDelete(batchid.split(","));
	}
	public List<PeopleMediationCommittee> getByNameAdnArea(PeopleMediationCommittee peopleMediationCommittee){
		return dao.getByNameAdnArea(peopleMediationCommittee);
	}

	/**
	 * 多条件查询人民调委会
	 */
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		//如果查询的乡镇是锡林郭勒盟,那么把townId置空,查询锡林郭勒盟下所有的人民调解委员会
		if("5".equals(af.getTownId())){
			af.setAreaId("5");
			af.setTownId("");
		}
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId(),af.getTownId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		av.setPage(page);
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
	}

	@Override
	public int countAgency(String areaId) {
		return dao.count(new PeopleMediationCommittee("", areaId));
	}

	@Override
	public Object searchAgencyById(AgencyForm af) {
		return dao.getById(af.getId());
	}
	@Override
	public void evaluationUpdate(String evaluation,String id,String remark) {}

	public boolean getMediationInfo(String name, String areaId,String belongTowns) {
        List<PeopleMediationCommittee> l = dao.getMediationInfo(name,areaId,belongTowns);
		if(l!=null||l.size()>0){
        	  return true;
          }
		return false;
	}

	public boolean findListInfo(
			PeopleMediationCommittee lo) {
		List<PeopleMediationCommittee> p = dao.findListInfo(lo);
		   if(p.size()>0){
			   return true;
		   }
		return false;
	}
}