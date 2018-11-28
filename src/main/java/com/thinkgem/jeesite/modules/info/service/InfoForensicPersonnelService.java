/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.util.List;

import com.thinkgem.jeesite.api.chart.entity.ForensicPersonnelVo;
import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.info.entity.InfoForensicPersonnel;
import com.thinkgem.jeesite.modules.info.dao.InfoForensicPersonnelDao;

/**
 * 鉴定人员信息管理Service
 * @author hejia
 * @version 2018-04-24
 */
@Service
@Transactional(readOnly = true)
public class InfoForensicPersonnelService extends CrudService<InfoForensicPersonnelDao, InfoForensicPersonnel> implements ApiAgencySearch {

    /**
     * 鉴定人员dao
     */
    @Autowired
    InfoForensicPersonnelDao forensicPersonnelDao;
    @Autowired
    InfoJudicialAuthenticationService judicialAuthenticationService;
    @Autowired
    AreaService areaService;
    @Autowired
	private SystemService systemService;

	public InfoForensicPersonnel get(String id) {
		return super.get(id);
	}
	
	public List<InfoForensicPersonnel> findList(InfoForensicPersonnel infoForensicPersonnel) {
		return super.findList(infoForensicPersonnel);
	}
	
	public Page<InfoForensicPersonnel> findPage(Page<InfoForensicPersonnel> page, InfoForensicPersonnel infoForensicPersonnel,User user) {
		String jId = infoForensicPersonnel.getId();
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		infoForensicPersonnel.getSqlMap().put("dsf", systemService.dataScopeFilter(user.getCurrentUser(), "o", "a",jId));
		// 设置分页参数
		infoForensicPersonnel.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(infoForensicPersonnel));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(InfoForensicPersonnel infoForensicPersonnel) {
		if(StringUtils.isBlank(infoForensicPersonnel.getId())) {
			//照片路径是空值
			/*if(StringUtils.isBlank(infoForensicPersonnel.getImageUrl())){
				infoForensicPersonnel.setImageUrl("/userfiles/default/user.png");
			}else{//照片url不存在路径文件夹
				infoForensicPersonnel.setImageUrl("/userfiles/infoForensicPersonnel/"+infoForensicPersonnel.getImageUrl());
			}*/
			infoForensicPersonnel.setImageUrl("/userfiles/idt_persons/"+infoForensicPersonnel.getLicenseNumber()+".jpg");
			super.save(infoForensicPersonnel);
			//自动向人员表插入一个记录，当做基层法律服务工作者的账号
			String loginName=Chinese2pinyin.convert(infoForensicPersonnel.getName(),HanyuPinyinCaseType.LOWERCASE,false);
			User u=systemService.getUserByLoginName(loginName);
			if(u!=null){
				loginName=loginName+"jd"+this.countAgency(null);
			}
			//保存账号相关信息及账号与业务数据对照关系
			UserAndInfoData uaid = new UserAndInfoData();
			uaid.setName(infoForensicPersonnel.getName());//用户姓名
			uaid.setCompanyId(OfficeRoleConstant.OFFICE_INFO_JUDICIAL_AUTHENTICATION);//鉴定所
			uaid.setInfoDataOfficeId(infoForensicPersonnel.getJudicialAuthentication().getId());// 所在鉴定所
			uaid.setLoginName(loginName);//登录名
			if("1".equals(infoForensicPersonnel.getRole())) {
				uaid.setRoleId(OfficeRoleConstant.ROLE_INFORENSIC_PERSONNEL+","+OfficeRoleConstant.ROLE_INFORENSIC_MANAGER_PERSONNEL);//角色id	
			}else {
				uaid.setRoleId(OfficeRoleConstant.ROLE_INFORENSIC_PERSONNEL);//角色id
			}
			uaid.setInfoDataId(infoForensicPersonnel.getId());//业务数据主键
			uaid.setRemark("infoForensicPersonnel");//备注
			uaid.setIdCard(infoForensicPersonnel.getIdCard());//身份证号
			uaid.setPhone(infoForensicPersonnel.getPhone());
			systemService.saveUserByInfoData(uaid);
		}else{
			super.save(infoForensicPersonnel);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(InfoForensicPersonnel infoForensicPersonnel) {
		systemService.deletYeWu(infoForensicPersonnel.getId(), infoForensicPersonnel.getJudicialAuthentication().getId(), "infoForensicPersonnel");
		super.delete(infoForensicPersonnel);
	}

    @Override
    public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId(),af.getTownId(),af.getIsMongolian(),af.getEvaluation(),af.getOfficeId());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		if(StringUtils.isBlank(af.getIsEvaluate())||af.getIsEvaluate().equals("false")){//人员排序
			page.setOrderBy("a16.code");
		}else{//满意度排序
			page.setOrderBy("a.evaluation DESC");
		}
		av.setPage(page);
		page.setList(dao.findListForApi(av));
		return new PageVo<AgencyVo>(page, true);
    }

    @Override
    public int countAgency(String areaId) {
        return forensicPersonnelDao.countByAreaId(areaId);
    }

    @Override
    public Object searchAgencyById(AgencyForm af) {
        return forensicPersonnelDao.getInfo(af.getId());
    }
    @Override
    @Transactional(readOnly = false)
	public void evaluationUpdate(String evaluation,String id,String remark) {
    	forensicPersonnelDao.evaluationUpdate(evaluation, id,remark);
    }

	public PageVo<ForensicPersonnelVo> findListForApiBigScreen(ForensicPersonnelVo forensicPersonnelVo) {
		if(forensicPersonnelVo.getPage()==null) {
			forensicPersonnelVo.setPage(new Page<ForensicPersonnelVo>());
		}
		forensicPersonnelVo.getPage().setList(dao.findListForApiBigScreen(forensicPersonnelVo));
		return new PageVo<ForensicPersonnelVo>(forensicPersonnelVo.getPage(), true);
	}
}