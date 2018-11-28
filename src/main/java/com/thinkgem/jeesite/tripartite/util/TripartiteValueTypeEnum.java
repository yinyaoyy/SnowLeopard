/**
 * 
 */
package com.thinkgem.jeesite.tripartite.util;

/**
 * 三方对接参数Enum
 * @author 王鹏
 * @version 2018-06-30 15:03:48
 */
public enum TripartiteValueTypeEnum {


	/** *字符串* */
    STRING("1", "string", ""),
	/** *数组,分隔符为英文逗号的字符串* */
    ARRAY("2", "array", "分隔符为英文逗号的字符串"),
	/** *方法，必须继承SchedulingService类，实现固定的getParam方法(如设置此方式，则该接口的其他参数设置将失效)* */
    METHOD("3", "method", "必须继承SchedulingService类，实现固定的getParam方法(如设置此方式，则该接口的其他参数设置将失效)");

    private String type;//value类型
    private String description;//类型说明
    private String remark;//类型备注
    
    private TripartiteValueTypeEnum(String type, String description, String remarks) {
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
        for (TripartiteValueTypeEnum o : TripartiteValueTypeEnum.values()) {
            if(o.type.equals(type)){
                return o.getDescription();
            }
        }
        return type;
    }
    /**
     * 根据参数类型查询参数备注
     * @param remark
     * @return
     */
    public static String getRemarkByType(String type){
        if(type == null){
            return "";
        }
        for (TripartiteValueTypeEnum o : TripartiteValueTypeEnum.values()) {
            if(o.type.equals(type)){
                return o.getRemark();
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
