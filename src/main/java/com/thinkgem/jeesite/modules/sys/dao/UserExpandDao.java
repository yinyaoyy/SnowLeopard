package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.UserExpand;

@MyBatisDao
public interface UserExpandDao {
	public UserExpand get(UserExpand userExpand);
	public int insert(UserExpand userExpand);
	public int update(UserExpand userExpand);
	public UserExpand getUserExpand(String id);
	public int updateQz(UserExpand userExpand);
}
