/**
 * 
 */
package com.thinkgem.jeesite.api.act.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thinkgem.jeesite.api.act.entity.BusiActDetailVo;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.OaDossier;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAid;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAcceptRegister;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.oa.service.OaDossierService;
import com.thinkgem.jeesite.modules.oa.service.act.OaLegalAidService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationAcceptRegisterService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationApplyService;

/**
 * 大屏业务流程处理
 * @author 王鹏
 * @version 2018-07-14 15:06:56
 */
@Service
public class BusiActService {

    @Autowired
    private OaLegalAidService legalService;
    @Autowired
    private OaPeopleMediationApplyService mediationApplyService;
    @Autowired
    private OaPeopleMediationAcceptRegisterService mediationRegisterService;
	@Autowired
	private OaDossierService oaDossierService;
    
    /**
     * 根据业务主键和类型查询相关信息展示到大屏
     * @author 王鹏
     * @version 2018-07-14 15:10:52
     * @param badv
     * @return
     */
    public BusiActDetailVo getDetailByBusiId(BusiActDetailVo badv) {
    	if("fy".equals(badv.getBusiType())){
    		//查询法援详细信息
    		OaLegalAid ola = legalService.get(badv.getBusiId());
    		//案件标题
    		badv.setCaseTitle(ola.getCaseTitle());
    		//案件内容
    		badv.setCaseContent(ola.getCaseReason());
    		//申请时间(精确到秒)
    		badv.setApplyTime(DateUtils.formatDate(ola.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
    		//案件类型
    		badv.setCaseType(ola.getCaseTypeDesc());
    		//所属旗县
    		badv.setAreaName(ola.getArea().getName());
    		//严重等级
    		badv.setCaseRank("");
    		//申请人
    		badv.setApplyUserName(ola.getName());
    		//申请人电话
    		badv.setApplyUserPhone(ola.getPhone());
    		//办理进度(受理中、办理中、已办结)
    		if(StringUtils.isBlank(ola.getYearNo())) {
    			badv.setHandleProgress("受理中");
    		}
    		else if("0".equals(ola.getArchiving())) {
    			badv.setHandleProgress("办理中");
    		}
    		else if("1".equals(ola.getArchiving())) {
    			badv.setHandleProgress("已办结");
    		}
    		else {//其他暂时显示空
    			badv.setHandleProgress("");
    		}
    	}
    	else if("tj".equals(badv.getBusiType())) {
    		//查询人民调解详细信息
    		OaPeopleMediationApply opma = mediationApplyService.get(badv.getBusiId());
    		OaPeopleMediationAcceptRegister opmar = null;
    		OaDossier od = null;
    		if("1".equals(opma.getStatus())) {
    			//办理中才可能有后续数据
    			opmar = mediationRegisterService.get(badv.getBusiId());
    			od = oaDossierService.get(badv.getBusiId());
    		}
    		//案件标题
    		badv.setCaseTitle(opma.getCaseTitle());
    		//案件内容
    		badv.setCaseContent(opma.getCaseSituation());
    		//申请时间(精确到秒)
    		badv.setApplyTime(DateUtils.formatDate(opma.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
    		//案件类型
    		badv.setCaseType(opma.getCaseTypeDesc());
    		//所属旗县
    		badv.setAreaName(opma.getCaseCounty().getName());
    		//严重等级
    		badv.setCaseRank(opmar==null?"":opmar.getCaseRankDesc());
    		//申请人
    		badv.setApplyUserName(opma.getAccuserName());
    		//申请人电话
    		badv.setApplyUserPhone(opma.getAccuserPhone());
    		//办理进度(受理中、办理中、已办结)
    		if("0".equals(opma.getStatus())) {
    			badv.setHandleProgress("受理中");
    		}
    		else if(od!=null && "1".equals(od.getStatus())) {
    			badv.setHandleProgress("已办结");
    		}
    		else if("1".equals(opma.getStatus())) {
    			badv.setHandleProgress("办理中");
    		}
    		else {//其他暂时显示空
    			badv.setHandleProgress("");
    		}
    	}
    	return badv;
    }
}
