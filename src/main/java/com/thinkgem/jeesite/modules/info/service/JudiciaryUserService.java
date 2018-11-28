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
import com.thinkgem.jeesite.modules.info.entity.JudiciaryUser;
import com.thinkgem.jeesite.modules.info.dao.JudiciaryUserDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;

/**
 * 司法所工作人员Service
 * @author wanglin
 * @version 2018-06-10
 */
@Service
@Transactional(readOnly = true)
public class JudiciaryUserService extends CrudService<JudiciaryUserDao, JudiciaryUser>  implements ApiAgencySearch{
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;

	public JudiciaryUser get(String id) {
		return super.get(id);
	}
	
	public List<JudiciaryUser> findList(JudiciaryUser judiciaryUser) {
		return super.findList(judiciaryUser);
	}
	
	public Page<JudiciaryUser> findPage(Page<JudiciaryUser> page, JudiciaryUser judiciaryUser,User user) {
		String jId = judiciaryUser.getId();
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		judiciaryUser.getSqlMap().put("dsf", systemService.dataScopeFilter(user.getCurrentUser(), "o", "a",jId));
		// 设置分页参数
		judiciaryUser.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(judiciaryUser));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(JudiciaryUser judiciaryUser) {
		if(StringUtils.isBlank(judiciaryUser.getId())){
			String imageUrl=judiciaryUser.getImageUrl();
			if(StringUtils.isBlank(imageUrl)){
				imageUrl="/userfiles/default/user.png";
			}else{
				imageUrl="/userfiles/judiciaryUser/"+imageUrl;
			}
			judiciaryUser.setImageUrl(imageUrl);
			/*if(judiciaryUser.getRoleList()!=null&&judiciaryUser.getRoleList().size()>0){
				judiciaryUser.setRole(judiciaryUser.getRoleList().get(0));
			}*/
			super.save(judiciaryUser);
		   //自动向人员表插入一个记录，当做人名司法所工作人员的账号
			String loginName=Chinese2pinyin.convert(judiciaryUser.getName(),HanyuPinyinCaseType.LOWERCASE,false);
			User u=systemService.getUserByLoginName(loginName);
			if(u!=null){
				loginName=loginName+"sfsgzry"+this.countAgency(null);
			}
			judiciaryUser.setOffice(officeService.get(judiciaryUser.getOffice().getId()));
			String ids=judiciaryUser.getOffice().getParentIds();
			String[]id=ids.split(",");

			//保存账号相关信息及账号与业务数据对照关系
			UserAndInfoData uaid = new UserAndInfoData();
			uaid.setName(judiciaryUser.getName());//用户姓名
			uaid.setCompanyId(id[id.length-2]);//用户账号机构id
			uaid.setInfoDataOfficeId(judiciaryUser.getOffice().getId());//业务数据机构主键
			uaid.setLoginName(loginName);//登录名
			if("1".equals(judiciaryUser.getRoleId())) {
				uaid.setRoleId(OfficeRoleConstant.ROLE_JUDICIARY_USER+","+OfficeRoleConstant.ROLE_JUDICIARY_MANAGER);//角色id
			}else {
				uaid.setRoleId(OfficeRoleConstant.ROLE_JUDICIARY_USER);//角色id
			}
			uaid.setInfoDataId(judiciaryUser.getId());//业务数据主键
			uaid.setRemark("judiciaryUser");//备注
			uaid.setIdCard(judiciaryUser.getIdcard());//身份证号
			//uaid.setUserSourceType("8");
			systemService.saveUserByInfoData(uaid);
		}else{
			super.save(judiciaryUser);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(JudiciaryUser judiciaryUser) {
		systemService.deletYeWu(judiciaryUser.getId(), judiciaryUser.getOffice().getId(), "judiciaryUser");
	  super.delete(judiciaryUser);
	}
	/**
	 * 接口: 查询司法所工作人员信息
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId(),af.getTownId(),af.getEvaluation());
		av.setIsMongolian(af.getIsMongolian());
		av.setOfficeId(af.getOfficeId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		if(StringUtils.isBlank(af.getIsEvaluate())||af.getIsEvaluate().equals("false")){//人员排序
			page.setOrderBy("a.role_id desc,a13.code");
		}else{//满意度排序
			page.setOrderBy("a.evaluation DESC");
		}
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
		return dao.count(new JudiciaryUser(null, areaId));
	}
	@Override
	public Object searchAgencyById(AgencyForm af) {
		return dao.getById(af.getId());
	}
	@Override
	@Transactional(readOnly = false)
	public void evaluationUpdate(String evaluation,String id,String remark) {
    	dao.evaluationUpdate(evaluation, id, remark);
    }
	
}