/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import java.util.List;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 服务数据资源的对应管理Entity
 * @author hejia
 * @version 2018-04-25
 */
public class ServerSourceManage extends DataEntity<ServerSourceManage> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String serverType;		// 服务类别
	private String pid;		// 父服务id
	private String pids;    // 所有父id集合
	private String pname;		// 父服务名称

	private String sourceId;		// 服务对应资源id
	//private String sourceIdHide;		// 服务对应资源id
	private String pcHerf;		// pc端服务外部链接
	private String mobileHerf;		// 移动端服务外部链接
	private String logo;		// logo图片链接
	private String homeShow;		// 首页显示
	private String pcShow;		// pc端显示
	private String mobileShow;		// 移动端显示
	private String mobileHomeShow;		// 移动首页显示
	private String bigdataShow;		// 大屏显示
	private String cloudShow;//云平台展示
	private Integer leve;		// 层级
	private Integer sort;		// 排序
	private String mobileLogo;//移动端logo图片链接
	

	private List<Role> roleList=Lists.newArrayList();//拥有的角色列表
	
	public ServerSourceManage() {
		super();
	}

	public ServerSourceManage(String id){
		super(id);
	}

	public static void sortList(List<ServerSourceManage> list, List<ServerSourceManage> sourcelist, String parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			ServerSourceManage e = sourcelist.get(i);
			if (e.getPid()!=null && e.getPid().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						ServerSourceManage child = sourcelist.get(j);
						if (child.getPid()!=null && child.getPid().equals(e.getId())){
							sortList(list, sourcelist, e.getId(), true);
							break;
						}
					}
				}
			}
		}
	}

	@JsonIgnore
	public static String getRootId(){
		return "";
	}
	
	@Length(min=1, max=225, message="名称长度必须介于 1 和 225 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=25, message="服务类别长度必须介于 1 和 25 之间")
	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	@Length(min=0, max=225, message="pc端服务外部链接长度必须介于 0 和 225 之间")
	public String getPcHerf() {
		return pcHerf;
	}

	public void setPcHerf(String pcHerf) {
		this.pcHerf = pcHerf;
	}
	
	@Length(min=0, max=225, message="移动端服务外部链接长度必须介于 0 和 225 之间")
	public String getMobileHerf() {
		return mobileHerf;
	}

	public void setMobileHerf(String mobileHerf) {
		this.mobileHerf = mobileHerf;
	}
	
	@Length(min=0, max=225, message="logo图片链接长度必须介于 0 和 225 之间")
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@Length(min=0, max=2, message="首页显示长度必须介于 0 和 2 之间")
	public String getHomeShow() {
		return homeShow;
	}

	public void setHomeShow(String homeShow) {
		this.homeShow = homeShow;
	}
	
	@Length(min=0, max=2, message="pc端显示长度必须介于 0 和 2 之间")
	public String getPcShow() {
		return pcShow;
	}

	public void setPcShow(String pcShow) {
		this.pcShow = pcShow;
	}
	
	@Length(min=0, max=2, message="移动端显示长度必须介于 0 和 2 之间")
	public String getMobileShow() {
		return mobileShow;
	}

	public void setMobileShow(String mobileShow) {
		this.mobileShow = mobileShow;
	}
	
	@Length(min=0, max=2, message="大屏显示长度必须介于 0 和 2 之间")
	public String getBigdataShow() {
		return bigdataShow;
	}

	public void setBigdataShow(String bigdataShow) {
		this.bigdataShow = bigdataShow;
	}
	
	@NotNull(message="层级不能为空")
	public Integer getLeve() {
		return leve;
	}

	public void setLeve(Integer leve) {
		this.leve = leve;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public String getMobileHomeShow() {
		return mobileHomeShow;
	}

	public void setMobileHomeShow(String mobileHomeShow) {
		this.mobileHomeShow = mobileHomeShow;
	}

	public String getCloudShow() {
		return cloudShow;
	}

	public void setCloudShow(String cloudShow) {
		this.cloudShow = cloudShow;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	public String getMobileLogo() {
		return mobileLogo;
	}

	public void setMobileLogo(String mobileLogo) {
		this.mobileLogo = mobileLogo;
	}
}