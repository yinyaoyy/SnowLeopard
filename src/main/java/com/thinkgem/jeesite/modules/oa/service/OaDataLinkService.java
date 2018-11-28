/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.oa.entity.OaDataLink;
import com.thinkgem.jeesite.modules.oa.dao.OaDataLinkDao;

/**
 * 记录流程数据Service
 * @author zhangqiang
 * @version 2018-05-28
 */
@Service
@Transactional(readOnly = true)
public class OaDataLinkService extends CrudService<OaDataLinkDao, OaDataLink> {

	public OaDataLink get(String id) {
		return super.get(id);
	}
	
	public List<OaDataLink> findList(OaDataLink oaDataLink) {
		return super.findList(oaDataLink);
	}
	
	public Page<OaDataLink> findPage(Page<OaDataLink> page, OaDataLink oaDataLink) {
		return super.findPage(page, oaDataLink);
	}
	
	@Transactional(readOnly = false)
	public void save(OaDataLink oaDataLink) {
		super.save(oaDataLink);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaDataLink oaDataLink) {
		super.delete(oaDataLink);
	}

	public OaDataLink getDataId(String businessKey, String tableName) {
		// TODO Auto-generated method stub
		return dao.getDataId(businessKey, tableName);
	}
	
}