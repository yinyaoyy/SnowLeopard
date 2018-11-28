/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao.act;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.thinkgem.jeesite.api.chart.entity.LegalAidVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidCount;

/**
 * 法援申请DAO接口
 * @author 王鹏
 * @version 2018-05-11
 */
@MyBatisDao
public interface OaLegalAidDao extends CrudDao<OaLegalAid> {
	
	/**
	 * 根据当前年份获取一个最新的序号
	 * @author 王鹏
	 * @version 2018-05-14 09:30:22
	 * @param year
	 * @return
	 */
	int selectYearNo();
	
	/**
	 * 根据内容更新指定内容
	 * @author 王鹏
	 * @version 2018-05-15 11:52:07
	 * @param oaLegalAid
	 * @return
	 */
	int updateSelective(OaLegalAid oaLegalAid);
	
	/**
	 * 根据申请id归档申请
	 * @author 王鹏
	 * @version 2018-05-23 16:24:17
	 * @param id
	 * @return
	 */
	int archiving(String id);
	
	/**
	 * 接口查询草稿箱
	 * @author 王鹏
	 * @version 2018-06-09 17:32:52
	 * @param entity
	 * @return
	 */
	List<OaLegalAid> findDraftListForApi(OaLegalAid oaLegalAid);
	
	/**
	 * 保存到草稿箱
	 * @author 王鹏
	 * @version 2018-06-09 16:57:12
	 * @param oaLegalAid
	 * @return
	 */
	int insertDraft(OaLegalAid oaLegalAid);
	
	/**
	 * 修改草稿
	 * @author 王鹏
	 * @version 2018-06-09 16:57:22
	 * @param oaLegalAid
	 * @return
	 */
	int updateDraft(OaLegalAid oaLegalAid);
	
	/**
	 * 删除草稿箱内容(支持批量，英文逗号分割)
	 * @author 王鹏
	 * @version 2018-06-09 16:59:24
	 * @param draftId
	 * @return
	 */
	int deleteDraft(String[] draftIds);
	
	/**
	 * 按年份和日期统计法律援助申请数量
	 * @author 王鹏
	 * @version 2018-05-29 17:35:29
	 * @param oac
	 * @return
	 */
	List<OaLegalAidCount> countYearArea(OaLegalAidCount oac);
	
	/**
	 * 法律援助案件类型受理数量占比
	 * 1.根据年度地区案件类型进行统计
	 * 2.只统计已受理的
	 * @author 王鹏
	 * @version 2018-05-29 17:35:29
	 * @param oac
	 * @return
	 */
	List<OaLegalAidCount> countCaseType(OaLegalAidCount oac);
	
	/**
	 * 法律援助申请方式受理数量占比
	 * 1.根据年度地区申请方式进行统计
	 * 2.是要申请就统计
	 * @author 王鹏
	 * @version 2018-05-29 17:35:29
	 * @param oac
	 * @return
	 */
	List<OaLegalAidCount> countLegalAidType(OaLegalAidCount oac);
	
	/**
	 * 大屏统计: 按区域统计时间段内法援申请数量
	 * 全盟法律援助案件申请数量占比图、各旗县法律援助案件占比图
	 * @author 王鹏
	 * @version 2018-6-7 10:33:25
	 * @param oac
	 * @return
	 */
	List<OaLegalAidCount> countAreaByYearForBigScreen(OaLegalAidCount oac);
	
	/**
	 * 大屏统计: 按年度统计区域内的数量
	 * 全盟法律援助案件申请数量与受理数量走势对比图
	 * 全盟法律援助案件数量折线图
	 * @author 王鹏
	 * @version 2018-6-7 17:32:31
	 * @param oac
	 * @return
	 */
	List<OaLegalAidCount> countYearByAreaForBigScreen(OaLegalAidCount oac);

	/**
	 * 接口:查询法援申请
	 * @author 王鹏
	 * @version 2018-6-10 20:51:44
	 * @param entity
	 * @return
	 */
	List<LegalAidVo> findListForBigScreen(LegalAidVo legalAidVo);
	
	/**
	 * 获取月度申请案件数量
	 * @param 
	 * @return
	 */
	List<OaLegalAidCount> countMonthArea(OaLegalAidCount oac);
	
	/**
	 * 接口更新是否评价
	 * @param id
	 */
	void isEvaluate(String id);
	
	/**
	 * 查询法援评价信息
	 * @param id
	 * @return
	 */
	List<T> getApi(String id);
	
	
	List<OaLegalAidCount> countYearByAreaForBigScreen1(OaLegalAidCount oac);
	
	/**
	 * 查询得到总数
	 * @param legalAidVo
	 * @return
	 */
	int findCountListForBigScreen(LegalAidVo legalAidVo);
}