/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.JGpush.JGpushUtils;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.dao.SysUserPushDao;
import com.thinkgem.jeesite.modules.cms.entity.SysUserPush;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;


/**
 * 用户未在线时的的消息保存Service
 * @author 尹垚
 * @version 2018-07-12
 */
@Service
@Transactional(readOnly = true)
public class SysUserPushService extends CrudService<SysUserPushDao, SysUserPush> {
	
	@Autowired
	private SystemService systemService;
	protected static final Logger LOG = LoggerFactory.getLogger(SysUserPushService.class);
	public SysUserPush get(String id) {
		return super.get(id);
	}
	
	public List<SysUserPush> findList(SysUserPush sysUserPush) {
		return super.findList(sysUserPush);
	}
	
	public Page<SysUserPush> findPage(Page<SysUserPush> page, SysUserPush sysUserPush) {
		return super.findPage(page, sysUserPush);
	}
	
	@Transactional(readOnly = false)
	public void save(SysUserPush sysUserPush) {
		super.save(sysUserPush);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysUserPush sysUserPush) {
		super.delete(sysUserPush);
	}

	public void insertList(List<SysUserPush> pushs) {
        		dao.insertList(pushs);
	}
	/**
	 * 可以传单个的登录名获取单个的userId,也可以传入登录名集合，获取userIds集合
	 * @param loginNames 登录名集合
	 * @param loginName  单个的登录名
	 * @return 人员的userId集合
	 */
	public List<String> pushUser(List<String> loginNames,String loginName){
		List<String> list = Lists.newArrayList();
		if(loginNames!=null&&!loginNames.isEmpty()&&!"".equals(loginNames)){
			for (int i = 0; i < loginNames.size(); i++) {
				User u = systemService.getUserByLoginName(loginNames.get(i));
				list.add(u.getId());
			}
		}
		if(loginName!=null&&StringUtils.isNotBlank(loginName)&&!"".equals(loginName)){
			User u = systemService.getUserByLoginName(loginName);
			list.add(u.getId());
		}
		return list;
	}

	public void pushNotificetion(SysUserPush push, String userId) {

		JPushClient jPushClient = JGpushUtils.newJpush();
		
		try {
			PushResult result2 = jPushClient.sendPush(JGpushUtils.buildPushObject_android_and_ios(push.getPushMessage().getTitle(),push.getPushMessage().getMsgContent(),userId));
			LOG.info("通知推送成功：msg_id="+result2.msg_id);
		} catch (APIConnectionException e1) {
			push.preInsert();
			push.setSendTime(new Date());
			push.setReceiveUserId(userId);
			List<SysUserPush> s = dao.getByReceivedId(userId);
			if(s!=null&&!s.isEmpty()){
				for (int j = 0; j < s.size(); j++) {
					if(!s.get(j).getPushMessage().getMsgContent().equals(push.getPushMessage().getMsgContent())||!s.get(j).getPushMessage().getTitle().equals(push.getPushMessage().getTitle())){
						dao.insert(push);
					}else{
						dao.updateByReceiveId(push);
					}
				}
			}else{
				dao.insert(push);
			}
			LOG.error("Connection error. Should retry later. ", e1);
		} catch (APIRequestException e) {
			push.preInsert();
			push.setSendTime(new Date());
			push.setReceiveUserId(userId);
			List<SysUserPush> s = dao.getByReceivedId(userId);
			if(s!=null&&!s.isEmpty()){
				for (int j = 0; j < s.size(); j++) {
					if(!s.get(j).getPushMessage().getMsgContent().equals(push.getPushMessage().getMsgContent())||!s.get(j).getPushMessage().getTitle().equals(push.getPushMessage().getTitle())){
						dao.insert(push);
					}else{
						dao.updateByReceiveId(push);
					}
				}
			}else{
				dao.insert(push);
			}
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
		}
	}
	/**
	 * 
	 * @param push
	 * @param userId
	 */
	public void pushNotificetion(SysUserPush push, List<String> userId) {
		JPushClient jPushClient = JGpushUtils.newJpush();
		for (int i = 0; i < userId.size(); i++) {
			try {
				PushResult result2 = jPushClient.sendPush(JGpushUtils.buildPushObject_android_and_ios(push.getPushMessage().getTitle(),push.getPushMessage().getMsgContent(),userId.get(i)));
                if(result2.getResponseCode()==200){
                	push.preInsert();
    				push.setSendTime(new Date());
    				push.setReceiveUserId(userId.get(i));
    				push.setStatus("1");
    				dao.insert(push);
                }
				LOG.info("律师通知推送成功：msg_id="+result2.msg_id);
			} catch (APIConnectionException e1) {
				push.preInsert();
				push.setSendTime(new Date());
				push.setReceiveUserId(userId.get(i));
				push.setStatus("0");
				List<SysUserPush> s = dao.getByReceivedId(userId.get(i));
				if(s!=null&&!s.isEmpty()){
					for (int j = 0; j < s.size(); j++) {
						if(!s.get(j).getPushMessage().getMsgContent().equals(push.getPushMessage().getMsgContent())||!s.get(j).getPushMessage().getTitle().equals(push.getPushMessage().getTitle())){
							dao.insert(push);
						}else{
							dao.updateByReceiveId(push);
						}
					}
				}else{
					dao.insert(push);
				}
				LOG.error("Connection error. Should retry later. ", "推送连接失败");
			} catch (APIRequestException e) {
				push.preInsert();
				push.setSendTime(new Date());
				push.setReceiveUserId(userId.get(i));
				push.setStatus("0");
				List<SysUserPush> s = dao.getByReceivedId(userId.get(i));
				if(s!=null&&!s.isEmpty()){
					for (int j = 0; j < s.size(); j++) {
						if(!s.get(j).getPushMessage().getMsgContent().equals(push.getPushMessage().getMsgContent())||!s.get(j).getPushMessage().getTitle().equals(push.getPushMessage().getTitle())){
							dao.insert(push);
						}else{
							dao.updateByReceiveId(push);
						}
					}
					
				}else{
					dao.insert(push);
				}
				LOG.error("Error response from JPush server. Should review and fix it. ", "当前用户不在线，存到数据库中");
	            LOG.info("HTTP Status: " + e.getStatus());
	            LOG.info("Error Code: " + e.getErrorCode());
	            LOG.info("Error Message: " + e.getErrorMessage());
	            LOG.info("Msg ID: " + e.getMsgId());
			}
		}
		
	}
	@Transactional(readOnly = false)
	public void deleteById(String id){
		dao.deleteById(id);
	}
	@Transactional(readOnly = false)
	public void updateStatusById(String id) {
		dao.updateStatusById(id);
	}

	/**
	 * 查询未读的消息数量。
	 * @param userId 用户id
	 * @return
	 */
	public int findNoReadList(String userId) {
		int a  = dao.findNoReadList(userId);
		return a;
	}

	public Page<SysUserPush> findMessgaePage(Page<SysUserPush> page,
			SysUserPush sysUserPush) {
		sysUserPush.setPage(page);
		page.setList(dao.findMesList(sysUserPush));
		return page;
	}
	@Transactional(readOnly = false)
	public void changeReadToAll(String userId) {
		dao.changeReadToAll(userId);
		
	}
	@Transactional(readOnly = false)
	public void changeReadStatusById(String id) {

		dao.changeReadStatusById(id);
	}
}