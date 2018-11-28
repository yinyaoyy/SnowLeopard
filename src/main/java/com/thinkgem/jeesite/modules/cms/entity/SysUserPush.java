/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.JGpush.PushMessage;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户未在线时的的消息保存Entity
 * @author 尹垚
 * @version 2018-07-12
 */
public class SysUserPush extends DataEntity<SysUserPush> {
	
	private static final long serialVersionUID = 1L;
	public static final String DEL_FLAG_SEND = "1";  //已发送状态
	private String receiveUserId;		// 接受人id
	private String sendUserId;		// 发送人id
	private String title;		// 通知标题
	private String content;		// 通知内容
	private Date sendTime;		// 发送时间
	private String status;		// 发送状态0失败1成功
	private String area;		// area
	private String url;		// 需要跳转的地址，为controller中的地址，需要有receiveUserId访问权限的才可以
    private String isRead;	    //是否已读
	private PushMessage pushMessage = new PushMessage();
	
	
	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public PushMessage getPushMessage() {
		return pushMessage;
	}

	public void setPushMessage(PushMessage pushMessage) {
		this.pushMessage = pushMessage;
	}

	public SysUserPush() {
		super();
	}

	public SysUserPush(String id){
		super(id);
	}

	@Length(min=1, max=50, message="接受人id长度必须介于 1 和 50 之间")
	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	
	@Length(min=1, max=50, message="发送人id长度必须介于 1 和 50 之间")
	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	
	@Length(min=0, max=100, message="通知标题长度必须介于 0 和 100 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=300, message="通知内容长度必须介于 0 和 300 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	@Length(min=0, max=2, message="发送状态0失败1成功长度必须介于 0 和 2 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=50, message="area长度必须介于 0 和 50 之间")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	
	@Length(min=0, max=1000, message="业务类型长度必须介于 0 和 100 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}