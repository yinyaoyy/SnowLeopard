/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.api.chart.entity.PeopleMediatorVo;
import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.info.dao.PeopleMediationDao;
import com.thinkgem.jeesite.modules.info.entity.PeopleMediation;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;

/**
 * 人民调解员Service
 * @author wanglin
 * @version 2018-05-25
 */
@Service
@Transactional(readOnly = true)
public class PeopleMediationService extends CrudService<PeopleMediationDao, PeopleMediation> implements ApiAgencySearch {
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;

	public PeopleMediation get(String id) {
		return super.get(id);
	}
	
	public List<PeopleMediation> findList(PeopleMediation peopleMediation) {
		return super.findList(peopleMediation);
	}
	
	public Page<PeopleMediation> findPage(Page<PeopleMediation> page, PeopleMediation peopleMediation,User user) {
		String pmId = peopleMediation.getId();
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		peopleMediation.getSqlMap().put("dsf", systemService.dataScopeFilter(user.getCurrentUser(), "o", "a",pmId));
				// 设置分页参数
		peopleMediation.setPage(page);
				// 执行分页查询
				page.setList(dao.findList(peopleMediation));
				return page;
	}
	
	
	@Transactional(readOnly = false)
	public void save(PeopleMediation peopleMediation) {
		if(StringUtils.isBlank(peopleMediation.getId())){
			String imageUrl=peopleMediation.getImageUrl();
			if(StringUtils.isBlank(imageUrl)){
				imageUrl="/userfiles/default/user.png";
			}else{
				imageUrl="/userfiles/peopleMediation/"+imageUrl;
			}
			peopleMediation.setImageUrl(imageUrl);
//			String cardNo = dao.findInfoByCardNo(peopleMediation.getIdCard());
//			if(cardNo.isEmpty()){
//				//插入调解员的表中
				super.save(peopleMediation);
//			}
			
		   //自动向人员表插入一个记录，当做人名调解员的账号
			String loginName=Chinese2pinyin.convert(peopleMediation.getName(),HanyuPinyinCaseType.LOWERCASE,false);
			//根据用户名查找sys_user中的用户信息。
			User u=systemService.getUserByLoginName(loginName);
			if(u!=null){
				loginName=loginName+"rmtjy"+this.countAgency(null);
			}
			peopleMediation.setOffice(officeService.get(peopleMediation.getOffice().getId()));
			String ids=peopleMediation.getOffice().getParentIds();
			String[]id=ids.split(",");

			//保存账号相关信息及账号与业务数据对照关系
			UserAndInfoData uaid = new UserAndInfoData();
			uaid.setName(peopleMediation.getName());//用户姓名
			uaid.setCompanyId(id[id.length-2]);//用户账号机构id
			uaid.setInfoDataOfficeId(peopleMediation.getOffice().getId());//业务数据机构主键
			uaid.setLoginName(loginName);//登录名
			if("主任".equals(peopleMediation.getRoleId())||"副主任".equals(peopleMediation.getRoleId())||"负责人".equals(peopleMediation.getRoleId())) {
				uaid.setRoleId(OfficeRoleConstant.ROLE_PROPLE_MEDIATION+","+OfficeRoleConstant.ROLE_PROPLE_MANAGER_MEDIATION);//角色id
			}else {
				uaid.setRoleId(OfficeRoleConstant.ROLE_PROPLE_MEDIATION);//角色id
			}
			uaid.setInfoDataId(peopleMediation.getId());//业务数据主键
			uaid.setRemark("peopleMediation");//备注
			uaid.setIdCard(peopleMediation.getIdCard());//身份证号
			uaid.setPhone(peopleMediation.getPhone());
			//uaid.setUserSourceType("7");
			systemService.saveUserByInfoData(uaid);
		}else{
			super.save(peopleMediation);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(PeopleMediation peopleMediation) {
		systemService.deletYeWu(peopleMediation.getId(), peopleMediation.getOffice().getId(), "peopleMediation");
		super.delete(peopleMediation);
		
	}
	/**
	 * 批量删除调解员
	 * @author
	 * @version 2018-4-22 16:28:44
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		String[] strings = batchid.split(",");
		for(String jj:strings) {
			PeopleMediation peopleMediation = dao.get(jj);
			systemService.deletYeWu(peopleMediation.getId(), peopleMediation.getOffice().getId(), "peopleMediation");
		}
		return dao.batchDelete(batchid.split(","));
	}
	/**
	 * 接口: 查询调解员信息List
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId(),af.getTownId(),af.getIsMongolian(),af.getEvaluation());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		av.setOfficeId(af.getOfficeId());
		page.setPageSize(af.getPageSize());
		if(StringUtils.isBlank(af.getIsEvaluate())||af.getIsEvaluate().equals("false")){//人员排序
			page.setOrderBy("a.role_id,d5.code");
		}else{//满意度排序
			page.setOrderBy("a.evaluation DESC");
		}
		av.setPage(page);
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
	}
	/**
	 * 查询调解员个人详细信息
	 */
	@Override
	public Object searchAgencyById(AgencyForm af) {
		return dao.getInfo(af.getId());
	}
	/**
	 * 接口： 按地区统计调解员数量
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#countAgency(java.lang.String)
	 */
	@Override
	public int countAgency(String areaId) {
		return dao.count(new PeopleMediation(null, areaId));
	}
	@Override
	@Transactional(readOnly = false)
	public void evaluationUpdate(String evaluation,String id,String remark) {
    	dao.evaluationUpdate(evaluation, id,remark);
    }
	

	public String findInfoByCardNo(String idCard) {
		String card = dao.findInfoByCardNo(idCard);
		return card;
	}

	/**
	 * 接口:大屏查询人民调解员信息
	 * @author 王鹏
	 * @version 2018-6-13 21:07:40
	 * @param pmv
	 * @return
	 */
	public PageVo<PeopleMediatorVo> findListForBigScreen(PeopleMediatorVo pmv) {
		// TODO Auto-generated method stub
		if(pmv.getPage()==null) {
			pmv.setPage(new Page<PeopleMediatorVo>());
		}
		pmv.getPage().setList(dao.findListForBigScreen(pmv));
		return new PageVo<PeopleMediatorVo>(pmv.getPage(), true);
	}

	public boolean getPeopleMediationInfo(PeopleMediation peopleMediation) {
		List<PeopleMediation> p = dao.getPeopleMediationInfo(peopleMediation);
		if(p!=null&&p.size()>0){
		return true;	
		}
		return false;
	}
}