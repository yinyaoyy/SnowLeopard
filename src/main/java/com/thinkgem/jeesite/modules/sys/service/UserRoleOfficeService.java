/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserRoleOffice;
import com.thinkgem.jeesite.modules.sys.dao.UserRoleOfficeDao;

/**
 * 162599Service
 * @author wanglin
 * @version 2018-07-23
 */
@Service
@Transactional(readOnly = true)
public class UserRoleOfficeService extends CrudService<UserRoleOfficeDao, UserRoleOffice> {

	public UserRoleOffice get(String id) {
		return super.get(id);
	}
	
	public List<UserRoleOffice> findList(UserRoleOffice userRoleOffice) {
		return super.findList(userRoleOffice);
	}
	
	public Page<UserRoleOffice> findPage(Page<UserRoleOffice> page, UserRoleOffice userRoleOffice) {
		return super.findPage(page, userRoleOffice);
	}
	
	@Transactional(readOnly = false)
	public void save(UserRoleOffice userRoleOffice) {
		super.save(userRoleOffice);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserRoleOffice userRoleOffice) {
		super.delete(userRoleOffice);
	}
	@Transactional(readOnly = false)
	public void delete(String userId) {
		dao.delete(userId);
	}
	@Transactional(readOnly = false)
	public int insertBatch(List<PartTimeJob> list) {
		 return dao.insertBatch(list);
	 }
	public List<String>  findUserByOfficeRole(PartTimeJob  partTimeJob){
		return dao.findUserByOfficeRole(partTimeJob);
	}

	public String getOfficeIdByUidAndRid(String uId, String rId) {
		String oId = dao.getOfficeIdByUidAndRid(uId,rId);
		return oId;
	}
}