/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserAndInfoData;

import groovy.lang.TracingInterceptor;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {

	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);
	/**
	 * 根据身份证号查询用户
	 * @param idCard
	 * @return
	 */
	public User getByIdCard(String idCard);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);
	
	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	
	/**
	 * 更新群众用户信息
	 * @param user
	 * @return
	 */
	public void updatequn(User user);
	
	/**
	 * 根据账号获取用户姓名
	 * @param user
	 * @return
	 */
	public User getName(String procInsId);
	/**
	 * 简单的注册（登录名和密码）
	 * @param user
	 * @return
	 */
     public int insertUser(User user);
 	/**
 	 * 初始化修改信息
 	 * @param user
 	 * @return
 	 */
      public int updateInfo(User user);
   	/**
   	 * 添加角色与人员关系表数据
   	 * @param user
   	 * @return
   	 */
    public int insertRoleGx(User user);
 	/**
 	 * 通讯录获取本部门全部人员
 	 * @param user
 	 * @return
 	 */
    public List<User> getAllList(User user);
    /**
     * 
     * 批量删除
     * @param batchid
     * @return
     */
    public int batchDelete(List<String> batchid);//批量删除
    public int  updatePhotoById(User user);

	/**
	 * 插入用户机构于业务数据对照
	 * @author 王鹏
	 * @version 2018-05-10 15:59:44
	 * @param uaid.id 系统账号id
	 * @param uaid.infoDataId 业务数据id
	 * @param uaid.type 分类:1=机构类;2=人员类;
	 * @param uaid.remark 备注(职业说明)
	 * @param uaid.isMain 是否是主要职位
	 * @return
	 */
	public int insertSysUserOfficeInfo(UserAndInfoData uaid);

	/**
	 * 根据业务id查询账号相关id
	 * @author 王鹏
	 * @version 2018-05-10 17:28:15
	 * @param infoId
	 * @param type 1=机构;2=人员
	 * @return
	 */
	public String selectSysUserOfficeInfo(@Param("infoId")String infoId, @Param("type")String type);
	
	/**
	 * 根据账号id或者是业务id查询用户全部信息(主职/兼职)
	 * 用户账号id和用户业务数据id有一个就好
	 * @author 王鹏
	 * @version 2018-06-05 21:14:09
	 * @param userId
	 * @param infoId
	 * @return
	 */
	public List<UserAndInfoData> selectByUserId(@Param("userId")String userId, @Param("infoId")String infoId);
	
	/**
	 * 根据业务数据主键删除对照关系
	 * @author 王鹏
	 * @version 2018-06-06 09:52:11
	 * @param infoDataId
	 * @return
	 */
	public int deleteByInfoDataId(String infoDataId);
	/**
	 * 人员表导入时根据生成的登录名来查询人员信息
	 * @param loginName
	 * @return
	 */
	public User getInfoByLoginName(String loginName);
	
	/**
	 * 查询人员表分类
	 * @param loginId
	 * @return
	 */
	public List<String> userTypeid(String loginId);
	
	/**
	 * 根据身份证号查询社会大众是否存在
	 * @param idCard
	 * @return
	 */
	public String identityType(String papernum);
	
	/***
	 * 根据sys_office的id去获取不同机构表的对应id
	 * @return
	 */
	public String selectInfoIdByLoginId(String loginId);
	
	
	/**
	 * 根据账号id或者是业务id查询用户全部信息(主职/兼职)
	 * 用户账号id和用户业务数据id有一个就好
	 * @author suzz
	 * @version 2018-08-01 21:14:09
	 * @param userId
	 * @param infoId
	 * @return
	 */
	public List<UserAndInfoData> selectUserInfor(@Param("userId")String userId, @Param("infoId")String infoId,@Param("officeId")String officeId);
	
	
	/**
	 * 根据不同数据维度查sys_user_role_office,为了查对应关系
	 * 在处理数据时一定要将此表的数据处理干净
	 * 后续此表作为用户角色对应的唯一表
	 * @author suzz
	 * @version 2018-08-01 21:14:09
	 * @param userId
	 * @param infoId
	 * @return
	 */
     public  List<UserAndInfoData>  selectUserRoleOffice(@Param("userId")String userId, @Param("roleId")String roleId,@Param("officeId")String officeid,@Param("isMain")String isMain);
	/**
	 * 
	 * 删除sys_user_role_office表对应
	 * @author suzz
	 * @version 2018-08-01 21:14:09
	 * @param userId 用户id
	 * @param officeId 对应机构id
	 * @param roleId 角色id
	 * @param isMain 兼职还是主职 (重要)
	 *
	 * @return
	 */
	public void deleteRoleOffice(Map<Object, String> param);
	
	
	/**
	 * 
	 * 删除sys_user_role表对应
	 * 特别是对兼职的情况
	 * @author suzz
	 * @version 2018-08-01 21:14:09
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public void deleteUserRoleOne(@Param("userId") String userId,@Param("roleId") String roleId );
	
	
	/**
	 * 
	 * 修改sys_user_role表对应
	 * 特别是对兼职的情况
	 * @author suzz
	 * @version 2018-08-01 21:14:09
	 * @param userId
	 * @param roleId
	 * @param isMain
	 * @return
	 */
	public void updateUserRoleOffice(@Param("userId")String userId,@Param("officeId") String officeId,@Param("isMain")String isMain);
	
	/**
	 * 
	 * 修改用户表机构id
	 * 特别是对兼职的情况
	 * @author suzz
	 * @version 2018-08-01 21:14:09
	 * @param userId
	 * @param officeId
	 * @param companyId
	 * @return
	 */
	public void updateUserOfficeId(@Param("userId")String userId,@Param("officeId") String officeId,@Param("companyId") String companyId);
	
	public String selectSysUserlogin(@Param("infoId")String infoId, @Param("remark")String remark);
	
	/**
	 * 修改sys_user_office_info 主兼职标记
	 * @author suzz
	 * @version 2018-08-04 10:38:09
	 * @param isMain 主兼职标记
	 * @param infoId 业务表id
	 * @return
	 */
	public void updateIsMain(@Param("infoId")String infoId,@Param("isMain") String isMain);
	
	
	/**
	 * 修改sys_user_office_info 表中普通用户权限对应机构id
	 * 要和主职保持一致
	 * @author suzz
	 * @version 2018-08-04 10:38:09
	 * @param isMain 主兼职标记
	 * @param infoId 业务表id
	 * @return
	 */
	public void updateOfficeIdPd(@Param("officeId")String officeId,@Param("userId") String userId);
	
	/**
	 * @author suzz
	 * @version 2018-08-04 10:38:09
	 * @param isMain 主兼职标记
	 * @param infoId 业务表id
	 * @return
	 */
	public List<UserAndInfoData> selectOfficeInfo(@Param("loginId") String loginId);
	
	public List<UserAndInfoData> selectOfficeUser(@Param("officeId") String officeId);
	public String getAreaByUser(@Param("userId")String id);
	public List<String> getUserListByAreaAndType(@Param("type")String type, @Param("areaId")String areaId);
	
}
