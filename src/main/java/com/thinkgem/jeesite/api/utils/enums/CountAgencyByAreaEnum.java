/**
 * 
 */
package com.thinkgem.jeesite.api.utils.enums;

import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * 根据地区统计机构
 * @author 王鹏
 * @version 2018-06-10 15:27:00
 */
public enum CountAgencyByAreaEnum {

	/** *律师事务所数量* */
	LAW_OFFICE("2", "律师事务所", "info_low_office", "area_id"),
	
	/** *公证机构数量* */
	NOTARY_AGENCY("3", "公证机构", "info_notary_agency", "area_id"),
    
	/** *司法鉴定机构司法鉴定机构数量* */
	JUDICIAL_AUTHENTICATION("4", "司法鉴定机构", "info_judicial_authentication", "area"),
    
	/** *法援中心数量* */
	LAW_ASSISTANCE("5", "法援中心", "info_law_assistance", "area"),
    
	/** *基层法律服务所数量* */
	LEGAL_SERVICE_OFFICE("8", "基层法律服务所", "info_legal_service_office", "area_id"),
    
	/** *人民调解委员会数量* */
	PEOPLE_MEDIATION_COMMITTEE("10", "人民调解委员会", "info_people_mediation_committee", "area_id"),
    
	/** *司法所数量* */
	JUDICIARY("12", "司法所", "info_judiciary", "area_id");
	
	private String key;//统计标识，区分是统计哪个人员数据
	private String label;//显示图标标题
	private String tableName;//被统计人员所在数据库表名
	
	/*
	 * 被统计人员所在表的地区字段名(部分表没有统一)
	 * 如果出现#,#需要关联机构表进行统计，格式为: 机构表#,#人员表外键#,#机构表地区列名
	 */
	private String areaColumn;
	
	private CountAgencyByAreaEnum(String key, String label,
			String tableName, String areaColumn) {
		this.key = key;
		this.label = label;
		this.tableName = tableName;
		this.areaColumn = areaColumn;
	}
	
	/**
	 * 根据key获取Label
	 * @author 王鹏
	 * @version 2018-06-10 15:44:14
	 * @param key
	 * @return
	 */
	public static String getLabel(String key) {
        if(StringUtils.isBlank(key)){
            return "";
        }
        for (CountAgencyByAreaEnum cp : CountAgencyByAreaEnum.values()) {
            if(cp.key.equals(key)){
                return cp.getLabel();
            }
        }
        return "";
	}
	/**
	 * 根据key获取表名
	 * @author 王鹏
	 * @version 2018-06-10 15:44:14
	 * @param key
	 * @return
	 */
	public static String getTable(String key) {
        if(StringUtils.isBlank(key)){
            return "";
        }
        for (CountAgencyByAreaEnum cp : CountAgencyByAreaEnum.values()) {
            if(cp.key.equals(key)){
                return cp.getTableName();
            }
        }
        return "";
	}
	/**
	 * 根据key获取地区列名
	 * @author 王鹏
	 * @version 2018-06-10 15:44:14
	 * @param key
	 * @return
	 */
	public static String getAreaColumn(String key) {
        if(StringUtils.isBlank(key)){
            return "";
        }
        for (CountAgencyByAreaEnum cp : CountAgencyByAreaEnum.values()) {
            if(cp.key.equals(key)){
                return cp.getAreaColumn();
            }
        }
        return "";
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getAreaColumn() {
		return areaColumn;
	}
	public void setAreaColumn(String areaColumn) {
		this.areaColumn = areaColumn;
	}
}
