/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.modules.info.dao.InfoJudicialAuthenticationDao;
import com.thinkgem.jeesite.modules.info.dao.InfoLegalServiceOfficeDao;
import com.thinkgem.jeesite.modules.info.dao.JudiciaryDao;
import com.thinkgem.jeesite.modules.info.dao.LawAssistanceDao;
import com.thinkgem.jeesite.modules.info.dao.LowOfficeDao;
import com.thinkgem.jeesite.modules.info.dao.NotaryAgencyDao;
import com.thinkgem.jeesite.modules.info.dao.PeopleMediationCommitteeDao;
import com.thinkgem.jeesite.modules.info.entity.InfoJudicialAuthentication;
import com.thinkgem.jeesite.modules.info.entity.InfoLegalServiceOffice;
import com.thinkgem.jeesite.modules.info.entity.Judiciary;
import com.thinkgem.jeesite.modules.info.entity.LawAssistance;
import com.thinkgem.jeesite.modules.info.entity.LowOffice;
import com.thinkgem.jeesite.modules.info.entity.NotaryAgency;
import com.thinkgem.jeesite.modules.info.entity.PeopleMediation;
import com.thinkgem.jeesite.modules.info.entity.PeopleMediationCommittee;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {
	@Autowired
	private UserDao userDao;
	@Autowired
	private LowOfficeDao lowOfficeDao;
	@Autowired
	private InfoLegalServiceOfficeDao infoLegalServiceOfficeDao;
	@Autowired
	private InfoJudicialAuthenticationDao infoJudicialAuthenticationDao;
	@Autowired
	private LawAssistanceDao lawAssistanceDao;
	@Autowired
	private JudiciaryDao judiciaryDao;
	@Autowired
	private PeopleMediationCommitteeDao peopleMediationCommitteeDao;
	@Autowired
	private NotaryAgencyDao notaryAgencyDao;
	
 	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}
	public List<Office> findOffice(String companyId){
		return dao.findOffice(companyId);
	}
	
	@Transactional(readOnly = true)
	public List<Office> findList(Office office){
		if(office != null&&!"".equals(office.getId())){
			office.setParentIds(office.getParentIds()+office.getId()+",%");
			return dao.findByParentIdsLike(office);
		}else {
			return UserUtils.getOfficeList();
		}
		//return  new ArrayList<Office>();
	}
	
	@Transactional(readOnly = false)
	public void save(Office office) {
			super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = false)
	public void delete(Office office) {
		List<UserAndInfoData> selectOfficeUser = userDao.selectOfficeUser(office.getId());
		List<Office> selectParent = dao.selectParent(office);
		if(selectOfficeUser.size()>0||selectParent.size()>0) {
			//也有可能是系统中的垃圾数据
			throw new BusinessException("删除失败!该律师事务所下有对应律师,或者对应机构下有对应下属机构，不允许删除");
		}else {
			
			List<UserAndInfoData> selectOfficeInfo = userDao.selectOfficeInfo(office.getId());
			for(UserAndInfoData userAndInfoData:selectOfficeInfo) {
				if("law_office".equals(userAndInfoData.getRemark())) {
					LowOffice lowOffice = new  LowOffice();
					lowOffice.setId(userAndInfoData.getInfoDataId());
					lowOfficeDao.delete(lowOffice);
				}else if("judiciary".equals(userAndInfoData.getRemark())) {
					Judiciary judiciary = new Judiciary();
					judiciary.setId(userAndInfoData.getInfoDataId());
					judiciaryDao.delete(judiciary);
				}else if("peopleMediationCommittee".equals(userAndInfoData.getRemark())) {
					PeopleMediationCommittee peopleMediationCommittee = new PeopleMediationCommittee();
					peopleMediationCommittee.setId(userAndInfoData.getInfoDataId());
					peopleMediationCommitteeDao.delete(peopleMediationCommittee);
				}else if("legal_service_office".equals(userAndInfoData.getRemark())) {
					InfoLegalServiceOffice infoLegalServiceOffice = new InfoLegalServiceOffice();
					infoLegalServiceOffice.setId(userAndInfoData.getInfoDataId());
					infoLegalServiceOfficeDao.delete(infoLegalServiceOffice);
				}else if("info_judicial_authentication".equals(userAndInfoData.getRemark())) {
					InfoJudicialAuthentication infoJudicialAuthentication = new InfoJudicialAuthentication();
					infoJudicialAuthentication.setId(userAndInfoData.getInfoDataId());
					infoJudicialAuthenticationDao.delete(infoJudicialAuthentication);
				}else if("lawAssistance".equals(userAndInfoData.getRemark())) {
					LawAssistance lawAssistance = new LawAssistance();
					lawAssistance.setId(userAndInfoData.getInfoDataId());
					lawAssistanceDao.delete(lawAssistance);
				}else if("notary_agency_office".equals(userAndInfoData.getRemark())) {
					NotaryAgency notaryAgency = new NotaryAgency();
					notaryAgency.setId(userAndInfoData.getInfoDataId());
					notaryAgencyDao.delete(notaryAgency);
				}
				//删除机构对应业务主键
				userDao.deleteByInfoDataId(userAndInfoData.getInfoDataId());
			}
			super.delete(office);
			UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);	
		}
			
	}
	public Office get(String id){
		return dao.get(id);
	}
	public List<Office> getOfficeByCompanyId(String companyId){
		Office office = new Office();
		office.setParentIds(companyId);
		List<Office> list = dao.findAllList(office);
		List<Office> lis = new ArrayList<Office>();
		for(int i=0;i<list.size();i++){
			office=list.get(i);
			String[] array=office.getParentIds().split(",");
			for(int j=0;j<array.length;j++){
				if((array[j].equals(companyId)&& Global.YES.equals(office.getUseable()))||(companyId.equals(office.getId()))){
					lis.add(office);
					break;
				}
			}
		}
		return lis;

	}
	/**
	 * 根据指定条件查询相应机构
	 * @author suzz
	 * @version 2018-08-07 10:37:00
	 * @param ProfessionId 机构对应业务部id
	 * @param type 机构的类型  科室。。。等
	 * @return
	 */
	public String deleteProfessionOffice(String ProfessionId,String type) {
		String id= userDao.selectSysUserOfficeInfo(ProfessionId,null);
	    List<UserAndInfoData> selectOfficeUser = userDao.selectOfficeUser(id);
		Office office2 = new Office();
		office2.setId(id);
		office2.setType(type);
		List<Office> selectParent = dao.selectParent(office2);
		if(selectOfficeUser.size()>0||selectParent.size()>0) {
			//也有可能是系统中的垃圾数据
			throw new BusinessException("删除失败!该机构下有对应人员,或者对应机构下有对应下属机构，不允许删除");
		}else {
			//删除机构对应业务主键
			userDao.deleteByInfoDataId(ProfessionId);
			//删除机构表数据
			dao.delete(office2);	
			UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);	
		}
		return "";
		
	}
	
	/**
	 * 根据指定条件查询相应机构
	 * @author 王鹏
	 * @version 2018-05-17 10:37:00
	 * @param office
	 * @return
	 */
	public  List<Office> findListByCondition(Office office){
		if(office != null){
			return dao.findListByCondition(office);
		}
		return  new ArrayList<Office>();
	}
	public Office getByParentAndName(Office office){
		return dao.getByParentAndName(office);
	}
	
	public List<Office> findIdName (Office office){
		return dao.findIdName(office);
	}
}
