/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.chart.entity.LawyerVo;
import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.info.dao.LawyerDao;
import com.thinkgem.jeesite.modules.info.entity.Lawyer;
import com.thinkgem.jeesite.modules.info.entity.LegalServicePerson;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;

/**
 * 律师信息管理Service
 * @author 王鹏
 * @version 2018-04-22
 */
@Service
@Transactional(readOnly = true)
public class LawyerService extends CrudService<LawyerDao, Lawyer> implements ApiAgencySearch {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OfficeService officeService;
	

	public Lawyer get(String id) {
		return super.get(id);
	}
	
	public List<Lawyer> findList(Lawyer lawyer) {
		return super.findList(lawyer);
	}
	
	public Page<Lawyer> findPage(Page<Lawyer> page, Lawyer lawyer,User user) {
		String lawyerId = lawyer.getId();
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		        lawyer.getSqlMap().put("dsf", systemService.dataScopeFilter(user.getCurrentUser(), "o", "a",lawyerId));
				// 设置分页参数
				lawyer.setPage(page);
				// 执行分页查询
				page.setList(dao.findList(lawyer));
				return page;
	}
	 
	@Transactional(readOnly = false)
	public void save(Lawyer lawyer) {
		if(StringUtils.isBlank(lawyer.getId())){
			String imageUrl = lawyer.getImageUrl();
			if(StringUtils.isBlank(imageUrl)){
				imageUrl="/userfiles/default/user.png";
			}else{
				imageUrl="/userfiles/lawyer/"+lawyer.getLicenseNumber()+".jpg";
			}
			lawyer.setImageUrl(imageUrl);
			super.save(lawyer);
		   //自动向人员表插入一个记录，当做律师的账号
			String loginName=Chinese2pinyin.convert(lawyer.getName(),HanyuPinyinCaseType.LOWERCASE,false);
			User u=systemService.getUserByLoginName(loginName);
			if(u!=null){
				loginName=loginName+"ls"+this.countAgency(null);
			}
			//保存账号相关信息及账号与业务数据对照关系
			UserAndInfoData uaid = new UserAndInfoData();
			uaid.setName(lawyer.getName());//用户姓名
			uaid.setCompanyId(OfficeRoleConstant.OFFICE_LAWYER_OFFICE);//用户账号机构id
			uaid.setInfoDataOfficeId(lawyer.getLawOffice().getId());//业务数据机构主键
			uaid.setLoginName(loginName);//登录名
			if("1".equals(lawyer.getRole())) {
				uaid.setRoleId(OfficeRoleConstant.ROLE_LAWYER+","+OfficeRoleConstant.ROLE_LAWYER_ADMIN);//角色id	
			}else {
				uaid.setRoleId(OfficeRoleConstant.ROLE_LAWYER);//角色id
			}
			uaid.setInfoDataId(lawyer.getId());//业务数据主键
			uaid.setRemark("lawyer");//备注
			uaid.setIdCard(lawyer.getIdCard());//身份证号
			uaid.setPhone(lawyer.getPhone());
			systemService.saveUserByInfoData(uaid);
		}else{
			super.save(lawyer);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = false)
	public void delete(Lawyer lawyer) {
		//删除对应业务数据
		systemService.deletYeWu(lawyer.getId(),lawyer.getLawOffice().getId(),"lawyer");
		dao.delete(lawyer);	
	}

	/**
	 * 接口: 查询律师信息
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		// TODO Auto-generated method stub
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId(),af.getIsAidLawyer(),af.getIsMongolian(),af.getEvaluation());
		av.setOfficeId(af.getOfficeId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		if(StringUtils.isBlank(af.getIsEvaluate())||af.getIsEvaluate().equals("false")){//人员排序
			//page.setOrderBy("a.law_office_id,a.name");
			page.setOrderBy("area.code");
		}else{//满意度排序
			page.setOrderBy("a.evaluation DESC");
		}
		av.setPage(page);
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
	}

	/**
	 * 接口： 统计律师数量
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#countAgency(java.lang.String)
	 */
	@Override
	public int countAgency(String areaId) {
		// TODO Auto-generated method stub
		return dao.count(new Lawyer(null, null, areaId));
	}
	/**
	 * 接口： 按执业类别统计律师数量
	 */
	public int countAgency(String areaId, String licenseType) {
		// TODO Auto-generated method stub
		return dao.count(new Lawyer(null, licenseType, areaId));
	}

	/**
	 * 批量删除律师
	 * @author 王鹏
	 * @version 2018-4-22 16:28:44
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		String[] strings = batchid.split(",");
		for(String rr:strings) {
			Lawyer lawyer = dao.get(rr);
			systemService.deletYeWu(lawyer.getId(), lawyer.getLawOffice().getId(), "lawyer");
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
	 * 接口:大屏查询律师信息
	 * @author 王鹏
	 * @version 2018-6-11 21:01:21
	 * @param oaLegalAid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageVo<LawyerVo> findListForApiBigScreen(LawyerVo lawyerVo) {
		// TODO Auto-generated method stub
		if(lawyerVo.getPage()==null) {
			lawyerVo.setPage(new Page<LawyerVo>());
		}
		lawyerVo.getPage().setList(dao.findListForApiBigScreen(lawyerVo));
		return new PageVo<LawyerVo>(lawyerVo.getPage(), true);
	}

	public boolean getInfoByIdCard(String idCard) {
		Lawyer l = dao.getInfoByIdCard(idCard);
		if(l!=null){
			return true;
		}
		return false;
	}
}