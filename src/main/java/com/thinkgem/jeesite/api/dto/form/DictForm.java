/**
 * 
 */
package com.thinkgem.jeesite.api.dto.form;

/**
 * 字典请求
 * @author 王鹏
 * @version 2018-04-18 15:24:20
 */
public class DictForm {

	private String key;//字典key值
    private String parentId;//父级id
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("key="+key+"\n");
		sb.append("parentId="+parentId+"\n");
		return sb.toString();
	}
}
