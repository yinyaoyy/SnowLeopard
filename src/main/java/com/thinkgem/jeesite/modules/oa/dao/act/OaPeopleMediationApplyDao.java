/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao.act;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import com.thinkgem.jeesite.api.chart.entity.PeopleMediationVo;
import com.thinkgem.jeesite.api.chart.entity.PeopleMediatorVo;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidInform;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApplyCount;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 人民调解业务逻辑DAO接口
 * @author zhangqiang
 * @version 2018-05-24
 */
@MyBatisDao
public interface OaPeopleMediationApplyDao extends CrudDao<OaPeopleMediationApply> {
	
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
	int updateSelective(OaPeopleMediationApply oaPeopleMediationApply);
	
	/**
	 * 根据年份区域统计
	 * 年度旗县调解案件占比
	 * @author 王鹏
	 * @version 2018-06-03 10:37:04
	 * @param opmac
	 * @return
	 */
	List<OaPeopleMediationApplyCount> countByYearArea(OaPeopleMediationApplyCount opmac);

	/**
	 * 全盟各旗县人民调解案件申请数量占比图
	 * 各旗县人民调解案件数量占比
	 * @author 王鹏
	 * @version 2018-06-08 16:55:19
	 * @param opmac
	 * @return
	 */
	List<OaPeopleMediationApplyCount> countAreaPeopleMediation(OaPeopleMediationApplyCount opmac);
	
	/**
	 * 大屏统计查询方法
	 * 全盟人民调解案件申请数量与受理数量走势对比图
	 * 全盟及各旗县人民调解案件数量
	 * @author 王鹏
	 * @version 2018-06-09 11:14:20
	 * @param opmac
	 * @return
	 */
	List<OaPeopleMediationApplyCount> countPeopleMediationAidAccept(OaPeopleMediationApplyCount opmac);

	/**
	 * 接口查询草稿箱
	 * @author 张强
	 * @version 
	 * @param entity
	 * @return
	 */
	List<OaPeopleMediationApply> findDraftListForApi(OaPeopleMediationApply oaPeopleMediationApply);

	/**
	 * 删除草稿箱内容(支持批量，英文逗号分割)
	 * @author 张强
	 * @version 
	 * @param draftId
	 * @return
	 */
	int deleteDraft(String[] draftIds);
	/**
	 * 接口查询人民调解和法律援助（暂时用）
	 * @author 张强
	 * @version 
	 * @param entity
	 * @return
	 */
	List<Act> findDraft(@Param("page")Page<Act> page, @Param("beginDate")String beginDate, @Param("endDate")String endDate, @Param("caseTitle")String caseTitle,@Param("user")User user);
	/**
	 * 接口查询人民调解和法律援助根据id（暂时用）
	 * @author 张强
	 * @version 
	 * @param entity
	 * @return
	 */
	List<OaPeopleMediationApply> findDraftMediationById(@Param("id")String id);
	/**
	 * 接口查询人民调解和法律援助根据id（暂时用）
	 * @author 张强
	 * @version 
	 * @param entity
	 * @return
	 */
	List<OaLegalAid> findDraftLegalById(@Param("id")String id);
	
	
	/**
	 * 接口查询法律援申请助根据id（暂时用）
	 * @author suzz
	 * @version 
	 * @param entity
	 * @return
	 */
	List<OaLegalAidInform> findDraftdefenseById(@Param("id")String id);
	
	/**
	 * 接口:大屏查询人民调解案件
	 * @author 王鹏
	 * @version 2018-06-13 10:53:58
	 * @param pmv
	 * @return
	 */
	List<PeopleMediationVo> findListForBigScreen(PeopleMediationVo pmv);
	
	/**
	 * 接口更新是否评价
	 * @param id
	 */
	void isEvaluate(String id);
	
	/**
	 * 查询调解员评价信息
	 * @param id
	 * @return
	 */
	List<T> getApi(String id);

	/**
	 * 大屏折线图，获取人民调解委托数量
	 * @param opmac
	 * @return
	 */
	List<OaPeopleMediationApplyCount> countPeopleMediationAidAccept1(OaPeopleMediationApplyCount opmac);
	
	/**
	 * 获取各个地区的人民调解数量和法律援助数量
	 * @return
	 */
	List<OaPeopleMediationApplyCount> countLeaglAndMediation();
	
	
	/**
	 * 获取表格数据总条数
	 * @param pmv
	 * @return
	 */
	int findListCountForBigScreen(PeopleMediationVo pmv);
	
	/**
	 * 
	 * @param peopleMediatorVo
	 * @return
	 */
	List<OaPeopleMediationApplyCount> countLegalAndMediationByAreaId(PeopleMediatorVo peopleMediatorVo);
	
}