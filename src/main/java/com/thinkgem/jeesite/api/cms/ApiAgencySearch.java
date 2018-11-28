/**
 * 
 */
package com.thinkgem.jeesite.api.cms;

import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;

/**
 * @author 王鹏
 * @version 2018-04-19 09:17:50
 */
public interface ApiAgencySearch {

	/**
	 * 接口: 查询机构信息
	 * @author 王鹏
	 * @version 2018-04-19 09:19:15
	 * @param af
	 * @return
	 */
	PageVo<AgencyVo> searchAgency(AgencyForm af);
	
	/**
	 * 接口: 统计机构数量
	 * @author 王鹏
	 * @version 2018-04-19 09:22:58
	 * @param areaId 地区id
	 * @return
	 */
	int countAgency(String areaId);

	/**
	 * 接口: 根据id查询详细信息
	 * @author 王鹏
	 * @version 2018-4-23 15:10:03
	 * @param af
	 * @return
	 */
	Object searchAgencyById(AgencyForm af);
	
	/**
	 * 接口: 更新人员评价值
	 */
	void evaluationUpdate(String evaluation,String id,String remark);
	
}
