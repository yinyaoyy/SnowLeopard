/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.api.chart.entity.SupervisorVo;
import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.info.entity.Supervisor;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;

import com.thinkgem.jeesite.modules.info.dao.SupervisorDao;

/**
 * 人民监督员Service
 * @author suzz
 * @version 2018-06-08
 */
@Service
@Transactional(readOnly = true)
public class SupervisorService extends CrudService<SupervisorDao, Supervisor>implements ApiAgencySearch {

	@Autowired
	private SystemService systemService;
	
	public Supervisor get(String id) {
		return super.get(id);
	}
	
	public List<Supervisor> findList(Supervisor supervisor) {
		return super.findList(supervisor);
	}
	
	public Page<Supervisor> findPage(Page<Supervisor> page, Supervisor supervisor) {
		return super.findPage(page, supervisor);
	}
	
	@Transactional(readOnly = false)
	public void save(Supervisor supervisor) {
		if(StringUtils.isBlank(supervisor.getId())) {
			String photograph=supervisor.getPhotograph();
			if(StringUtils.isBlank(photograph)){
				photograph="/userfiles/default/user.png";
			}else{
				photograph="/userfiles/judiciaryUser/"+photograph;
			}
			supervisor.setPhotograph(photograph);
			supervisor.setCreateDate(new Date());
			super.save(supervisor);
			String loginName=Chinese2pinyin.convert(supervisor.getXrName(),HanyuPinyinCaseType.LOWERCASE,false);
			User u=systemService.getUserByLoginName(loginName);
			if(u!=null){
				loginName=loginName+"ls"+this.countAgency(null);
			}
			//保存账号相关信息及账号与业务数据对照关系
			UserAndInfoData uaid = new UserAndInfoData();
			uaid.setName(supervisor.getXrName());//用户姓名
			uaid.setCompanyId(OfficeRoleConstant.OFFICE_SFJ_OFFICE);//锡盟司法局
			uaid.setInfoDataOfficeId(OfficeRoleConstant.OFFICE_SUPERVISOR_SERVICE);// 人民监督科
			uaid.setLoginName(loginName);//登录名
			uaid.setRoleId(OfficeRoleConstant.ROLE_SUPERVISOR_USER);//角色id人名监督员是普通用户角色
			uaid.setInfoDataId(supervisor.getId());//业务数据主键
			uaid.setRemark("supervisor");//备注
			uaid.setIdCard(supervisor.getIdno());//身份证号
			uaid.setPhone(supervisor.getPhone());
			systemService.saveUserByInfoData(uaid);
		}else{
			supervisor.setCreateDate(new Date());
			super.save(supervisor);
		}
		
	}

	/**
	 * 接口： 按执业类别统计人民监督员数量
	 */
	public int countAgency(String areaId, String licenseType) {
		Supervisor supervisor=new Supervisor(null,areaId);
		supervisor.setJob(licenseType);
		return dao.count(supervisor);
	}

	
	@Transactional(readOnly = false)
	public void delete(Supervisor supervisor) {
		systemService.deletYeWu(supervisor.getId(), OfficeRoleConstant.OFFICE_SUPERVISOR_SERVICE, "supervisor");
		super.delete(supervisor);
	}

	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId(),af.getTownId());
		av.setIsMongolian(af.getIsMongolian());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		if(StringUtils.isBlank(af.getIsEvaluate())||af.getIsEvaluate().equals("false")){//人员排序
			page.setOrderBy("a13.code");
		}
		av.setPage(page);
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
	}

	@Override
	public Object searchAgencyById(AgencyForm af) {
		return dao.getById(af.getId());
	}

	@Override
	public int countAgency(String areaId) {
		return dao.count(new Supervisor(null, areaId));
	}
	
	@Override
	@Transactional(readOnly = false)
	public void evaluationUpdate(String evaluation,String id,String remark) {
    	dao.evaluationUpdate(evaluation, id, remark);
    }

	public PageVo<SupervisorVo> findListForApiBigScreen(SupervisorVo supervisorVo) {
		// TODO Auto-generated method stub
		if(supervisorVo.getPage()==null) {
			supervisorVo.setPage(new Page<SupervisorVo>());
		}
		supervisorVo.getPage().setList(dao.findListForApiBigScreen(supervisorVo));
		return new PageVo<SupervisorVo>(supervisorVo.getPage(), true);
	}
	
}