package com.thinkgem.jeesite.api.dto.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 登录成功返回用户信息
 *
 * @author kakasun
 * @create 2018-04-18 下午4:07
 */
@JsonInclude
public class UserInfoVo {



	/**
     * 用户id
     */
    String id;
    /**
     * 头像
     */
    String photo;
    /**
     * 真实姓名
     */
    String realname;
     String loginName;// 登录名
    /**
     * 手机号
     */
    String mobile;
    
    /**
     * 性别
     */
    String sex;
    /**
     * 学历
     */
    String education;
    
    /**
     * 出生日期
     */
    Date birthday;
    
    /**
     * 职业类型
     */
    String userType;
    /**
     * 职业类型
     */
    String userTypeDesc;
    /**
     * 用户类型
     */
    String userSourceTypeDesc;
    /**
     * 用户类型
     */
    String userSourceType;
    /**
     * 所在乡镇id
     */
    Area townarea=new Area();
    
    /**
     * 所在县id
     */
    Area  area=new Area();
    
    /**
     * 拥有角色列表
     */
    List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
    /**
     * 身份证号
     */
    private String papernum;//身份证号
    
	public UserInfoVo() {
    }

    public UserInfoVo(User u) {
        if(null == u){
            return;
        }
        this.id = u.getId();
        this.photo = u.getPhoto();
        this.realname = u.getName();
        this.loginName=u.getLoginName();
        this.mobile = u.getMobile();
        this.sex=u.getUserExpand().getSex();
        this.birthday=u.getBirthday();
        this.education=u.getUserExpand().getEducation();
        this.area=u.getArea();
        this.townarea=u.getTownarea();
        this.userType=u.getUserType();
        this.userTypeDesc=u.getUserTypeDesc();
        this.roleList=u.getRoleList();
        this.userSourceTypeDesc=u.getUserSourceTypeDesc();
        this.userSourceType=u.getUserSourceType();
        this.papernum = u.getPapernum();
    }

    public Area getTownarea() {
		return townarea;
	}

	public void setTownarea(Area townarea) {
		this.townarea = townarea;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

    public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserSourceTypeDesc() {
		return userSourceTypeDesc;
	}

	public void setUserSourceTypeDesc(String userSourceTypeDesc) {
		this.userSourceTypeDesc = userSourceTypeDesc;
	}

	public String getUserTypeDesc() {
		return userTypeDesc;
	}

	public void setUserTypeDesc(String userTypeDesc) {
		this.userTypeDesc = userTypeDesc;
	}

	public String getUserSourceType() {
		return userSourceType;
	}

	public void setUserSourceType(String userSourceType) {
		this.userSourceType = userSourceType;
	}

	public String getPapernum() {
		/*if(!"".equals(papernum) && papernum.length()==18){
			this.papernum = papernum.substring(0, 3)+"***********"+papernum.substring(14);
		}*/
		return papernum;
	}

	public void setPapernum(String papernum) {
		this.papernum = papernum;
	}

	@Override
	public String toString() {
		return "UserInfoVo [id=" + id + ", photo=" + photo + ", realname=" + realname + ", mobile=" + mobile + ", sex="
				+ sex + ", education=" + education + ", birthday=" + birthday + ", userType=" + userType + ", userSourceTypeDesc=" + userSourceTypeDesc + userType + ", userSourceType=" + userSourceType+ ", townarea="
				+ townarea + ", area=" + area + "]";
	}
}
