/**
 * 
 */
package com.thinkgem.jeesite.tripartite.util;

/**
 * 三方对接参数Enum
 * @author 王鹏
 * @version 2018-06-30 15:03:48
 */
public enum TripartiteTypeEnum {


	/** *系统地址* */
    SYSTEM_URL("1", "系统地址"),
	/** *接口名称* */
    API_URL("2", "接口名称"),
	/** *状态码* */
    STATUS_CODE("3", "状态码"),
	/** *请求参数(单个接口)* */
    API_PARAM("4", "请求参数(单个接口)"),
	/** *请求头设置* */
    SYSTEM_HEADER("5", "请求头设置"),
	/** *通用参数* */
    SYSTEM_PARAM("6", "通用参数");

    private String type;//param类型
    private String description;//类型说明
    
    private TripartiteTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * 根据参数类型查询参数说明
     * @param type
     * @return
     */
    public static String getDescByType(String type){
        if(type == null){
            return "";
        }
        for (TripartiteTypeEnum o : TripartiteTypeEnum.values()) {
            if(o.type.equals(type)){
                return o.getDescription();
            }
        }
        return type;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
