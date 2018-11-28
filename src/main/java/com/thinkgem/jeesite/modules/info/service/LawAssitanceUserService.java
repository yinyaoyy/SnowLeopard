/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.info.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

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
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.modules.info.dao.LawAssitanceUserDao;
import com.thinkgem.jeesite.modules.info.entity.LawAssitanceUser;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.Chinese2pinyin;

/**
 * 法援中心工作人员Service
 * @author wanglin
 * @version 2018-04-22
 */
@Service
@Transactional(readOnly = true)
public class LawAssitanceUserService extends CrudService<LawAssitanceUserDao, LawAssitanceUser> implements ApiAgencySearch {
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;

	public LawAssitanceUser get(String id) {
		return super.get(id);
	}
	
	public List<LawAssitanceUser> findList(LawAssitanceUser lawAssitanceUser) {
		return super.findList(lawAssitanceUser);
	}
	
	public Page<LawAssitanceUser> findPage(Page<LawAssitanceUser> page, LawAssitanceUser lawAssitanceUser,User user) {
		String laId = lawAssitanceUser.getId();
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		lawAssitanceUser.getSqlMap().put("dsf", systemService.dataScopeFilter(user.getCurrentUser(), "o", "a",laId));
		// 设置分页参数
		lawAssitanceUser.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(lawAssitanceUser));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(LawAssitanceUser lawAssitanceUser) {
			if(StringUtils.isBlank(lawAssitanceUser.getId())){
				/*String imageUrl=lawAssitanceUser.getImage();
				if(StringUtils.isBlank(imageUrl)){
					imageUrl="/userfiles/default/user.png";
				}else{
					imageUrl="/userfiles/lawAssitanceUser/"+imageUrl;
				}*/
			//lawAssitanceUser.setImage(imageUrl);
			//通过身份证号判断性别
			if(StringUtils.isNoneBlank(lawAssitanceUser.getIdCard())){
			  Map<String,String>	map=DateUtils.getBirAgeSex(lawAssitanceUser.getIdCard());
			  if(StringUtils.isBlank(lawAssitanceUser.getSex())) {
				  lawAssitanceUser.setSex(map.get("sexCode")); 
			  }
			  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");  
			  try {
				lawAssitanceUser.setBirthday(sf.parse(map.get("birthday")));
			} catch (ParseException e) {
				e.printStackTrace();
			 }
			}
			super.save(lawAssitanceUser);
			//自动向机构表写入一个记录，方便后续为法援中心工作人员创建账户
		    Office parentOffice=new Office();
		    parentOffice.setArea(lawAssitanceUser.getArea());
		    parentOffice=officeService.findListByCondition(parentOffice).get(0);
		   //自动向人员表插入一个记录，当做律师的账号
			String loginName=Chinese2pinyin.convert(lawAssitanceUser.getName(),HanyuPinyinCaseType.LOWERCASE,false);
			User u=systemService.getUserByLoginName(loginName);
			if(u!=null){
				loginName=loginName+"fygzry"+this.countAgency(null);
			}
			lawAssitanceUser.setOffice(officeService.get(lawAssitanceUser.getOffice().getId()));
			String ids=lawAssitanceUser.getOffice().getParentIds();
			String[]id=ids.split(",");
			//保存账号相关信息及账号与业务数据对照关系
			UserAndInfoData uaid = new UserAndInfoData();
			uaid.setName(lawAssitanceUser.getName());//用户姓名
			uaid.setCompanyId(id[id.length-2]);//用户账号机构id
			uaid.setInfoDataOfficeId(lawAssitanceUser.getOffice().getId());//业务数据机构主键
			uaid.setLoginName(loginName);//登录名
			if("1".equals(lawAssitanceUser.getRole())) {
				uaid.setRoleId(OfficeRoleConstant.ROLE_LAW_ASSITANCE_USER+","+OfficeRoleConstant.ROLE_LAW_ASSITANCE_MANAGER_USER);//角色id	
			}else {
				uaid.setRoleId(OfficeRoleConstant.ROLE_LAW_ASSITANCE_USER);//角色id
			}
			uaid.setInfoDataId(lawAssitanceUser.getId());//业务数据主键
			uaid.setRemark("lawAssitanceUser");//备注法援中心工作人员
			uaid.setIdCard(lawAssitanceUser.getIdCard());//身份证号
			uaid.setPhone(lawAssitanceUser.getPhone());
			//uaid.setUserSourceType("5");
			systemService.saveUserByInfoData(uaid);
		}else{
			super.save(lawAssitanceUser);
		}
	  
	}
	@Transactional(readOnly = false)
	public void delete(LawAssitanceUser lawAssitanceUser) {
		super.delete(lawAssitanceUser);
		//根据业务数据Id删除对照关系
		systemService.deletYeWu(lawAssitanceUser.getId(), lawAssitanceUser.getOffice().getId(), "lawAssitanceUser");
	}

	/* (non-Javadoc)
	 * @see com.thinkgem.jeesite.api.cms.ApiAgencySearch#searchAgency(com.thinkgem.jeesite.api.dto.form.AgencyForm)
	 */
	@Override
	public PageVo<AgencyVo> searchAgency(AgencyForm af) {
		// TODO Auto-generated method stub
		AgencyVo av = new AgencyVo(af.getName(), af.getAreaId());
		av.setIsMongolian(af.getIsMongolian());
		av.setEvaluation(af.getEvaluation());
		av.setOfficeId(af.getOfficeId());
		av.setIsAidLawyer(af.getIsAidLawyer());
		Page<AgencyVo> page = new Page<AgencyVo>();
		page.setPageNo(af.getPageNo());
		page.setPageSize(af.getPageSize());
		if(StringUtils.isBlank(af.getIsEvaluate())||af.getIsEvaluate().equals("false")){//人员排序
			page.setOrderBy("a.role,a7.code");
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
		return dao.count(new LawAssitanceUser());
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
    	dao.evaluationUpdate(evaluation, id ,remark);
    }
	/**
	 * 批量删除法援中心工作人员
	 * @author 王鹏
	 * @version 2018-4-22 16:28:44
	 * @param batchid
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchDelete(String batchid) {
		String [] hh=batchid.split(",");
		for(String jj:hh) {
			LawAssitanceUser lawAssitanceUser = dao.get(jj);
	     systemService.deletYeWu(lawAssitanceUser.getId(), lawAssitanceUser.getOffice().getId(), "lawAssitanceUser");
		}
		return dao.batchDelete(batchid.split(","));
	}

	/**
	 * 法援中心工作者身份证验证
	 * @author 黄涛
	 * @version 2018-6-12 
	 * @param batchid
	 * @return
	 */
	public String personIdCard(String Card){
		
		return dao.personIdCard(Card);
	}
}