/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.Complaint;
import com.thinkgem.jeesite.modules.cms.entity.ComplaintCount;

/**
 * 投诉建议DAO接口
 * @author wanglin
 * @version 2018-05-11
 */
@MyBatisDao
public interface ComplaintDao extends CrudDao<Complaint> {
	List<Complaint> findUserComplaintList(Complaint complaint);
	List<Complaint> findUserIsInquiriesComplaintList(Complaint complaint);
	
	/**
	 * 全盟各旗县法律援助中心接受投诉数量与处理投诉数量对比图
	 * @author 王鹏
	 * @version 2018-06-08 15:23:10
	 * @param cc
	 * @return
	 */
	List<ComplaintCount> countComplaintComment(ComplaintCount cc);
	
	/**
	 * 大屏统计(折线图): 各旗县律师收到投诉数量
	 * @author 王鹏
	 * @version 2018-6-11 20:06:09
	 * @param cc
	 * @return
	 */
	List<ComplaintCount> countComplaintLawOffice(ComplaintCount cc);
	/**
	 * 接口访问统计量
	 * @param id
	 */
	public void statisticNum(String id);
	
	/**
	 * 全盟各旗县法律援助中心接受投诉数量
	 * @param cc
	 * @return
	 */
	List<ComplaintCount> countComplaintComment1(ComplaintCount cc);
}