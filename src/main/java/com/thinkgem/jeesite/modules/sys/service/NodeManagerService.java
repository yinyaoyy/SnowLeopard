/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.NodeManager;
import com.thinkgem.jeesite.modules.sys.dao.NodeManagerDao;

/**
 * 流程节点Service
 * @author wanglin
 * @version 2018-06-06
 */
@Service
@Transactional(readOnly = true)
public class NodeManagerService extends CrudService<NodeManagerDao, NodeManager> {

	public NodeManager get(String id) {
		return super.get(id);
	}
	
	public List<NodeManager> findList(NodeManager nodeManager) {
		
		return super.findList(nodeManager);
	}
	
	public Page<NodeManager> findPage(Page<NodeManager> page, NodeManager nodeManager) {
		return super.findPage(page, nodeManager);
	}
	
	@Transactional(readOnly = false)
	public void save(NodeManager nodeManager) {
		super.save(nodeManager);
	}
	
	@Transactional(readOnly = false)
	public void delete(NodeManager nodeManager) {
		super.delete(nodeManager);
	}
	public String findNewVersion(NodeManager nodeManager){
		return dao.findNewVersion(nodeManager);
	}
}