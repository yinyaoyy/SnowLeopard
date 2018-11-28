/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.appmange.entity.SysRoleAppMenu;
import com.thinkgem.jeesite.modules.sys.entity.Role;

import java.util.List;

/**
 * 角色与移动端应用的对应关系DAO接口
 * @author hejia
 * @version 2018-04-19
 */
@MyBatisDao
public interface SysRoleAppMenuDao extends CrudDao<SysRoleAppMenu> {

    /**
     * 根据应用id查找
     * @param appMenuId
     * @return
     */
    List<SysRoleAppMenu> findByAppMenuId(String appMenuId);

    /**
     * 根据角色的id查找对应的应用id
     * @param roles
     * @return
     */
    List<SysRoleAppMenu> findByRoleIdS(List<Role> roles);

    /**
     * 根据应用id和roleId查找
     * @param sysRoleAppMenu
     * @return
     */
    SysRoleAppMenu findByAppMenuIdAndRoleId(SysRoleAppMenu sysRoleAppMenu);
}