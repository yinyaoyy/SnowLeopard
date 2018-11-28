/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.ServerSourceManage;
import com.thinkgem.jeesite.modules.sys.dao.ServerSourceManageDao;

/**
 * 服务数据资源的对应管理Service
 * @author hejia
 * @version 2018-04-25
 */
@Service
@Transactional(readOnly = true)
public class ServerSourceManageService extends CrudService<ServerSourceManageDao, ServerSourceManage> {

	@Autowired
	private ServerRoleService serverRoleService;
	@Autowired
	private SystemService systemService;
	
	public ServerSourceManage get(String id) {
		ServerSourceManage ssm = super.get(id);
		if(ssm!=null) {
			ssm.setRoleList(systemService.findRoleListByServerId(id));
		}
		return ssm;
	}
	
	public List<ServerSourceManage> findList(ServerSourceManage serverSourceManage) {
		return super.findList(serverSourceManage);
	}
	
	public Page<ServerSourceManage> findPage(Page<ServerSourceManage> page, ServerSourceManage serverSourceManage) {
		return super.findPage(page, serverSourceManage);
	}
	
	@Transactional(readOnly = false)
	public void save(ServerSourceManage serverSourceManage) {
		ServerSourceManage pServer = super.get(serverSourceManage.getPid());
		String pids = pServer==null?"":pServer.getPids()+pServer.getId()+",";
		String oldPids = serverSourceManage.getPids();//用于替换下级服务的全部上级服务id
		serverSourceManage.setPids(pids);
		// 保存或更新实体
		if (StringUtils.isBlank(serverSourceManage.getId())){
			serverSourceManage.preInsert();
			dao.insert(serverSourceManage);
 		}else{
 			serverSourceManage.preUpdate();
 			dao.update(serverSourceManage);
 			ServerSourceManage ssm = new ServerSourceManage();
 			ssm.setPids("%"+serverSourceManage.getId()+",%");
 			List<ServerSourceManage> subList = dao.findByPids(ssm);
 			for(ServerSourceManage sub : subList) {
 				if(StringUtils.isBlank(oldPids)) {
 					sub.setPids(serverSourceManage.getPids()+serverSourceManage.getId()+",");
 				}
 				else {
 					sub.setPids(sub.getPids().replace(oldPids, serverSourceManage.getPids()+""));
 				}
 				dao.updatePids(sub);
 			}
 		}
		serverRoleService.delete(serverSourceManage.getId());
		dao.insertServerRole(serverSourceManage);
	}
	
	@Transactional(readOnly = false)
	public void delete(ServerSourceManage serverSourceManage) {
		super.delete(serverSourceManage);
	}
	
}