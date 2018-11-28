/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.appmange.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.appmange.entity.AppMenu;
import com.thinkgem.jeesite.modules.appmange.entity.SysRoleAppMenu;

import java.util.List;

/**
 * app应用DAO接口
 * @author hejia
 * @version 2018-04-19
 */
@MyBatisDao
public interface AppMenuDao extends CrudDao<AppMenu> {

    /**
     * 根据应用id查找显示，未删除应用
     * @param sram
     * @return
     */
    List<AppMenu> findByIdsAndShow(List<SysRoleAppMenu> sram);
}