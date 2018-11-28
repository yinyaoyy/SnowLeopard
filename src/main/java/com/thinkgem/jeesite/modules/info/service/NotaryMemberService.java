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
import com.thinkgem.jeesite.modules.info.entity.NotaryMember;
import com.thinkgem.jeesite.modules.info.dao.NotaryMemberDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;

/**
 * 公证员Service
 * @author 王鹏
 * @version 2018-04-23
 */
@Service
@Transactional(readOnly = true)
public class NotaryMemberService extends CrudService<NotaryMemberDao, NotaryMember> implements ApiAgencySearch {
	
	@Autowired
	private SystemService systemService;
	
	public NotaryMember get(String id) {
		return super.get(id);
	}
	
	public List<NotaryMember> findList(NotaryMember notaryMember) {
		return super.findList(notaryMember);
	}
	
	public Page<NotaryMember> findPage(Page<NotaryMember> page, NotaryMember notaryMember,User user) {
		String gzId = notaryMember.getId();
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		notaryMember.getSqlMap().put("dsf", systemService.dataScopeFilter(user.getCurrentUser(), "o", "a",gzId));
				// 设置分页参数
		notaryMember.setPage(page);
				// 执行分页查询
				page.setList(dao.findList(notaryMember));
				return page;
	}
	
	@Transactional(readOnly = false)
	public void save(NotaryMember notaryMember) {
		if(StringUtils.isBlank(notaryMember.getId())) {
			//照片路径是空值
			/*if(StringUtils.isBlank(notaryMember.getImageUrl())){
				notaryMember.setImageUrl("/userfiles/default/user.png");
			}else{//照片url不存在路径文件夹
				notaryMember.setImageUrl("/userfiles/NotaryMember/"+notaryMember.getImageUrl());
			}*/
			notaryMember.setImageUrl("/userfiles/NotaryMember/"+notaryMember.getIdCard()+".jpg");
			super.save(notaryMember);
			//自动向人员表插入一个记录，当做基层法律服务工作者的账号
			String loginName=Chinese2pinyin.convert(notaryMember.getName(),HanyuPinyinCaseType.LOWERCASE,false);
			User u=systemService.getUserByLoginName(loginName);
			if(u!=null){
				loginName=loginName+"ls"+this.countAgency(null);
			}
			//保存账号相关信息及账号与业务数据对照关系
			UserAndInfoData uaid = new UserAndInfoData();
			uaid.setName(notaryMember.getName());//用户姓名
			uaid.setCompanyId(OfficeRoleConstant.OFFICE_NOTARY_SERVICE);//公证处
			uaid.setInfoDataOfficeId(notaryMember.getNotaryAgency().getId());// 所在公证处
			uaid.setLoginName(loginName);//登录名
			if("1".equals(notaryMember.getRole())){
				uaid.setRoleId(OfficeRoleConstant.ROLE_NOTARY_MEMBER_USER);//角色id
			}else {
				uaid.setRoleId(OfficeRoleConstant.ROLE_NOTARY_MEMBER_USER+","+OfficeRoleConstant.ROLE_NOTARY_MEMBER_MANAGER_USER);//角色id
			}
			uaid.setInfoDataId(notaryMember.getId());//业务数据主键
			uaid.setRemark("notaryMember");//备注
			uaid.setIdCard(notaryMember.getIdCard());//身份证号
			uaid.setPhone(notaryMember.getPhone());
			systemService.saveUserByInfoData(uaid);
		}else{
			super.save(notaryMember);
		}
		
	}
	
	@Transactional(readOnly = false)
	public void delete(NotaryMember notaryMember) {
		systemService.deletYeWu(notaryMember.getId(), notaryMember.getNotaryAgency().getId(), "notaryMember");
		super.delete(notaryMember);
	}
	
	/**
	 * 接口: 查询公证员信息
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		// TODO Auto-generated method stub
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId(),af.getTownId(),af.getIsMongolian(),af.getEvaluation(),af.getOfficeId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		av.setPage(page);
		if(StringUtils.isBlank(af.getIsEvaluate())||af.getIsEvaluate().equals("false")){//人员排序
			//page.setOrderBy("a.notary_agency_id,a.name");
			page.setOrderBy("b.code");
		}
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
	}

	/**
	 * 接口： 统计公证员数量
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#countAgency(java.lang.String)
	 */
	@Override
	public int countAgency(String areaId) {
		// TODO Auto-generated method stub
		return dao.count(new NotaryMember(null, areaId));
	}

	/**
	 * 批量删除公证员
	 * @author 王鹏
	 * @version 2018-4-22 16:28:44
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		String[] strings = batchid.split(",");
	    for(String hh:strings) {
	    	NotaryMember notaryMember = dao.get(hh);
	    	systemService.deletYeWu(notaryMember.getId(), notaryMember.getNotaryAgency().getId(), "notaryMember");
	    }
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
	@Transactional(readOnly = false)
	public void evaluationUpdate(String evaluation,String id,String remark) {
    	dao.evaluationUpdate(evaluation, id, remark);
    }
	
	/**
	 * 公证员执业证号验证
	 * @author 刘江陵
	 * @version 2018-6-10 17:50:25
	 * @param batchid
	 * @return
	 */
	public String personLicenseNumber(String licenseNumber){
		String LicenseNumber = dao.personLicenseNumber(licenseNumber);
		return LicenseNumber;
	}
}