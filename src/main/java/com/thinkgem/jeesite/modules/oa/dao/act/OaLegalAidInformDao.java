/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao.act;

import java.util.List;

import org.activiti.engine.impl.cmd.SaveAttachmentCmd;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidInform;

/**
 * 法援通知辩护DAO接口
 * @author suzz
 * @version 2018-07-11
 */
@MyBatisDao
public interface OaLegalAidInformDao extends CrudDao<OaLegalAidInform> {

	int updateSelective(OaLegalAidInform oaLegalAidInform);
	
	
	/**
	 * 接口查询草稿箱
	 * @author 王鹏
	 * @version 2018-06-09 17:32:52
	 * @param entity
	 * @return
	 */
	List<OaLegalAidInform> findDraftListForApi(OaLegalAidInform oaLegalAidInform);
	
	/**
	 * 保存到草稿箱
	 * @author 王鹏
	 * @version 2018-06-09 16:57:12
	 * @param oaLegalAid
	 * @return
	 */
	int insertDraft(OaLegalAidInform oaLegalAidInform);
	
	/**
	 * 修改草稿
	 * @author 王鹏
	 * @version 2018-06-09 16:57:22
	 * @param oaLegalAid
	 * @return
	 */
	int updateDraft(OaLegalAidInform oaLegalAidInform);
	
	/**
	 * 删除草稿箱内容(支持批量，英文逗号分割)
	 * @author 王鹏
	 * @version 2018-06-09 16:59:24
	 * @param draftId
	 * @return
	 */
	int deleteDraft(String[] draftIds);
	
}