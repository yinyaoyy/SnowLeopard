/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.PartTimeJob;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserRoleOffice;

/**
 * 162599DAO接口
 * @author wanglin
 * @version 2018-07-23
 */
@MyBatisDao
public interface UserRoleOfficeDao extends CrudDao<UserRoleOffice> {
   public int delete(String userId);
   int insertBatch(List<PartTimeJob> list);
   List<PartTimeJob>  findPartTimeJobList(PartTimeJob  partTimeJob);
   List<String>  findUserByOfficeRole(PartTimeJob  partTimeJob);
   public String getOfficeIdByUidAndRid(@Param("uId")String uId, @Param("rId")String rId);
}
