/**
 * 
 */
package com.thinkgem.jeesite.api;

/**
 * @author 王鹏
 * @version 2018-05-24 09:57:17
 */
public enum ActTaskBusiEnum {

	
	/** *法律援助申请* */
    LEGAL_AID("legal_aid", "oaLegalAidService"),
    /** 人民调解，通过流程图中不同的节点Id来获取不同的逻辑层 */
    MEDIATION_START("mediation_start","oaPeopleMediationApplyService"),
    MEDIATION_SHENHE("mediation_shenhe","oaPeopleMediationApplyService"),
    MEDIATION_XIUGAI("mediation_xiugai","oaPeopleMediationApplyService"),
    MEDIATION_ZHIDING("mediation_zhiding","oaPeopleMediationApplyService"),
    MEDIATION_DENGJI("mediation_dengji","oaPeopleMediationAcceptRegisterService"),
    MEDIATION_QIANZI_1("mediation_qianzi_1","oaPeopleMediationAcceptRegisterService"),
    MEDIATION_QIANZI_2("mediation_qianzi_2","oaPeopleMediationAcceptRegisterService"),
    MEDIATION_DIAOCHA("mediation_diaocha","oaPeopleMediationExamineService"),
    MEDIATION_TIAOJIE("mediation_tiaojie","oaPeopleMediationRecordService"),
    MEDIATION_XIEYI("mediation_xieyi","oaPeopleMediationAgreementService"),
    MEDIATION_HUIFANG("mediation_huifang","oaPeopleMediationVisitService"),
    MEDIATION_JUANZONG("mediation_juanzong","oaDossierService"),
    FAST_LEGAL("fast_legal","oaFastLegalService"),

    /** *法律通知申请* */
    LEGAL_NO_AID("notification_defense", "oaLegalAidInformService");
    private String procDefKey;//流程key
    private String busiService;//业务服务层
    
    private ActTaskBusiEnum(String procDefKey, String busiService) {
        this.procDefKey = procDefKey;
        this.busiService = busiService;
    }
    
    /**
     * 根据流程key查找对应的业务服务层
     * @param code
     * @return
     */
    public static String getBusiByKey(String procDefKey){
        if(procDefKey == null){
            return "";
        }
        for (ActTaskBusiEnum o : ActTaskBusiEnum.values()) {
            if(o.procDefKey.equals(procDefKey)){
                return o.getBusiService();
            }
        }
        return procDefKey;
    }
    
	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

	public String getBusiService() {
		return busiService;
	}

	public void setBusiService(String busiService) {
		this.busiService = busiService;
	}
	
}
