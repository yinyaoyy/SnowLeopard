/**
 * 
 */
package com.thinkgem.jeesite.api.utils.enums;

import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * @author 王鹏
 * @version 2018-06-10 15:27:00
 */
public enum CountPeopleByAreaEnum {

	/** *目前各旗县社区矫正人员数量占比* */
	COMMUNITY_CORRECTION("1", "目前各旗县社区矫正人员数量占比", "info_correct_user", 
			"(select o.id, o.del_flag,a.parent_id area_id from sys_office o, sys_area a where o.area_id = a.id)#,#office_id#,#area_id",
			"create_date"),
	
	/** *目前各旗县律师人员数量占比* */
    LAWYER("2", "目前各旗县律师人员数量占比", "info_lawyer", "sys_office#,#law_office_id#,#area_id","create_date"),
    
	/** *目前各旗县公证人员数量占比* */
    NOTARY_PEOPLE("3", "目前各旗县公证人员数量占比", "info_notary_member", "sys_office#,#notary_agency_id#,#area_id","create_date"),
    
	/** *目前各旗县安置帮教人员数量占比* */
    PLACEMENT_TEACH("4", "目前各旗县安置帮教人员数量占比", "info_personnel_prison_user", "area_id","release_time"),
    
	/** *目前三个旗县司法鉴定所人员数量占比* */
    FORENSIC_PERSONNEL("5", "目前三个旗县司法鉴定所人员数量占比", "info_forensic_personnel", "area_id","create_date"),
    
	/** *目前各旗县人民调解员人员数量占比* */
    PEOPLE_MEDIATION("6", "目前各旗县人民调解员人员数量占比", "info_people_mediation", "area_id","create_date"),
    
	/** *目前各旗县人民监督员人员数量占比* */
    PEOPLE_SUPERVISION("7", "目前各旗县人民监督员人员数量占比", "info_supervisor", "native_county","create_date"),
    
	/** *目前各旗县基层法律服务者数量占比* */
    LEGAL_SERVICES("8", "目前各旗县基层法律服务者数量占比", "info_legal_service_person", "sys_office#,#legal_office_id#,#area_id","create_date"),
    
	/** *各旗县法律援助中心工作人员数量* */
    LAW_ASSITANCE_USER("9", "各旗县法律援助中心工作人员数量", "info_law_assitance_user", "area","create_date"),
    
	/** *各旗县司法所工作人员人员数量占比* */
    JUDICIAL_IDENTIFICATION("10", "各旗县司法所工作人员人员数量占比", "info_judiciary_user", "area_id","create_date"),
    
	/** *全盟各旗县在监服刑人员数量对比* */
    PRISON_USER("11", "全盟各旗县在监服刑人员数量对比", "info_prison_user", "area_id","create_date");
	
	private String key;//统计标识，区分是统计哪个人员数据
	private String label;//显示图标标题
	private String tableName;//被统计人员所在数据库表名
	/*
	 * 被统计人员所在表的地区字段名(部分表没有统一)
	 * 如果出现#,#需要关联机构表进行统计，格式为: 机构表#,#人员表外键#,#机构表地区列名
	 */
	private String areaColumn;
	/*
	 * 被统计人员所在表的日期字段(比如安置帮教的释放时间release_time)
	 * 默认使用系统记录时间create_date
	 */
	private String dateColumn;
	
	private CountPeopleByAreaEnum(String key, String label,
			String tableName, String areaColumn, String dateColumn) {
		this.key = key;
		this.label = label;
		this.tableName = tableName;
		this.areaColumn = areaColumn;
		this.dateColumn = dateColumn;
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
        for (CountPeopleByAreaEnum cp : CountPeopleByAreaEnum.values()) {
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
        for (CountPeopleByAreaEnum cp : CountPeopleByAreaEnum.values()) {
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
        for (CountPeopleByAreaEnum cp : CountPeopleByAreaEnum.values()) {
            if(cp.key.equals(key)){
                return cp.getAreaColumn();
            }
        }
        return "";
	}
	/**
	 * 根据key获取日期列名
	 * @author 王鹏
	 * @version 2018-06-10 15:44:14
	 * @param key
	 * @return
	 */
	public static String getDateColumn(String key) {
        if(StringUtils.isBlank(key)){
            return "";
        }
        for (CountPeopleByAreaEnum cp : CountPeopleByAreaEnum.values()) {
            if(cp.key.equals(key)){
                return cp.getDateColumn();
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
	public String getDateColumn() {
		return dateColumn;
	}
	public void setDateColumn(String dateColumn) {
		this.dateColumn = dateColumn;
	}
}
