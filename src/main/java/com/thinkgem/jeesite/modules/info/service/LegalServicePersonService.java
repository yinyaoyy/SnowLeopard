/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.List;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;

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
import com.thinkgem.jeesite.modules.info.dao.LegalServicePersonDao;
import com.thinkgem.jeesite.modules.info.entity.LegalServicePerson;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;

/**
 * 基层法律服务工作者Service
 * @author 王鹏
 * @version 2018-05-09
 */
@Service
@Transactional(readOnly = true)
public class LegalServicePersonService extends CrudService<LegalServicePersonDao, LegalServicePerson>
	implements ApiAgencySearch {
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private LegalServicePersonDao dao;
	
	public LegalServicePerson get(String id) {
		return super.get(id);
	}
	
	public List<LegalServicePerson> findList(LegalServicePerson legalServicePerson) {
		return super.findList(legalServicePerson);
	}
	
	public Page<LegalServicePerson> findPage(Page<LegalServicePerson> page, LegalServicePerson legalServicePerson,User user) {
		String jId = legalServicePerson.getId();
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		legalServicePerson.getSqlMap().put("dsf", systemService.dataScopeFilter(user.getCurrentUser(), "o", "a",jId));
		// 设置分页参数
		legalServicePerson.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(legalServicePerson));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(LegalServicePerson legalServicePerson) {
		if(StringUtils.isBlank(legalServicePerson.getId())){
			//照片路径是空值
			if(StringUtils.isBlank(legalServicePerson.getImageUrl())){
				legalServicePerson.setImageUrl("/userfiles/default/user.png");
			}else{//照片url不存在路径文件夹
				legalServicePerson.setImageUrl("/userfiles/LegalServicePerson/"+legalServicePerson.getImageUrl());
			}
			super.save(legalServicePerson);
		   //自动向人员表插入一个记录，当做基层法律服务工作者的账号
			String loginName=Chinese2pinyin.convert(legalServicePerson.getName(),HanyuPinyinCaseType.LOWERCASE,false);
			User u=systemService.getUserByLoginName(loginName);
			if(u!=null){
				loginName=loginName+"ls"+this.countAgency(null);
			}
			//保存账号相关信息及账号与业务数据对照关系
			UserAndInfoData uaid = new UserAndInfoData();
			uaid.setName(legalServicePerson.getName());//用户姓名
			uaid.setCompanyId(OfficeRoleConstant.OFFICE_LEGAL_SERVICE);//用户账号机构id
			uaid.setInfoDataOfficeId(legalServicePerson.getLegalOffice().getId());//业务数据机构主键
			uaid.setLoginName(loginName);//登录名
			if("1".equals(legalServicePerson.getRole())) {
				uaid.setRoleId(OfficeRoleConstant.ROLE_LEGAL_SERVICE_PERSON+","+OfficeRoleConstant.ROLE_LEGAL_SERVICE_ADMIN);//角色id
			}else {
				uaid.setRoleId(OfficeRoleConstant.ROLE_LEGAL_SERVICE_PERSON);//角色id
			}
			uaid.setInfoDataId(legalServicePerson.getId());//业务数据主键
			uaid.setRemark("legal_service_person");//备注
			uaid.setIdCard(legalServicePerson.getIdCard());//身份证号
			uaid.setPhone(legalServicePerson.getPhone());
			//uaid.setUserSourceType("3");
			systemService.saveUserByInfoData(uaid);
		}else{
			super.save(legalServicePerson);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(LegalServicePerson legalServicePerson) {
		systemService.deletYeWu(legalServicePerson.getId(), legalServicePerson.getLegalOffice().getId(), "legal_service_person");
		super.delete(legalServicePerson);
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		// TODO Auto-generated method stub
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId(), af.getTownId(), af.getIsMongolian(),af.getEvaluation());
		av.setOfficeId(af.getOfficeId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		if(StringUtils.isBlank(af.getIsEvaluate())||af.getIsEvaluate().equals("false")){//人员排序
			//page.setOrderBy("a.legal_office_id,a.name");
			page.setOrderBy("area.code");
		}else{//满意度排序
			page.setOrderBy("a.evaluation DESC");
		}
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
		return dao.count(new LegalServicePerson(null, areaId));
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgencyById(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public Object searchAgencyById(AgencyForm af) {
		// TODO Auto-generated method stub
		return dao.getById(af.getId());
	}
	@Override
	@Transactional(readOnly = false)
	public void evaluationUpdate(String evaluation,String id,String remark) {
    	dao.evaluationUpdate(evaluation, id, remark);
    }

	/**
	 * 批量删除基层法律服务者
	 * @author 王鹏
	 * @version 2018-5-9 11:50:25
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		String[] strings = batchid.split(",");
		for(String rr:strings) {
			LegalServicePerson legalServicePerson = dao.get(rr);
			systemService.deletYeWu(legalServicePerson.getId(), legalServicePerson.getLegalOffice().getId(), "legal_service_person");
		}
		return dao.batchDelete(batchid.split(","));
	}
	
	/**
	 * 基层法律服务者身份证验证
	 * @author 刘江陵
	 * @version 2018-6-10 17:50:25
	 * @param batchid
	 * @return
	 */
	public String personIdCard(String Card){
		String IdCard = dao.personIdCard(Card);
		return IdCard;
	}
}