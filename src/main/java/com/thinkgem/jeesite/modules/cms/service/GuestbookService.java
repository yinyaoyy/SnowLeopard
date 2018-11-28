/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.JGpush.JGpushUtils;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.cms.dao.GuestbookDao;
import com.thinkgem.jeesite.modules.cms.entity.Guestbook;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookComment;
import com.thinkgem.jeesite.modules.cms.entity.SysUserPush;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 留言Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class GuestbookService extends CrudService<GuestbookDao, Guestbook> {
	@Autowired
    private GuestbookCommentService guestbookCommentService;
	@Autowired
    private SystemService systemService;
	@Autowired
	private SysUserPushService sysUserPushService;
	public Guestbook get(String id) {
		return dao.get(id);
	}
	
	public Page<Guestbook> findPage(Page<Guestbook> page, Guestbook guestbook) {
		//guestbook.getSqlMap().put("dsf", dataScopeFilter(guestbook.getCurrentUser(), "o", "u"));
	    String areaId = systemService.getAreaByUser(UserUtils.getUser());
	    guestbook.getArea().setId(areaId);
		guestbook.setUser(UserUtils.getUser());
		guestbook.setPage(page);
		page.setList(dao.findList(guestbook));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void delete(Guestbook guestbook, Boolean isRe) {
		dao.delete(guestbook);
	}
	
	/**
	 * 更新索引
	 */
	public void createIndex(){
	}
	
	/**
	 * 全文检索
	 */
	// 暂不提供
	public Page<Guestbook> search(Page<Guestbook> page, String q, String beginDate, String endDate){
		
		return page;
	}
	@Transactional(readOnly = false)
	public void save(Guestbook guestbook){
		 if (guestbook.getIsNewRecord()){
			guestbook.preInsert();
			dao.insert(guestbook);
				String str = null;
				String type = guestbook.getType();
				if("1".equals(type)){
					str = "lawyer";
				}else if ("2".equals(type)) {
					str = "notaryMember";
				}else if ("3".equals(type)) {
					str = "lawAssitanceUser";
				}else if ("4".equals(type)) {
					str = "peopleMediation";
				}else if ("5".equals(type)) {
					str = "infoForensicPersonnel";
				}else if ("6".equals(type)) { //仲裁咨询如果未指定人所有都可看
					str = "";
				}
				List<String> userIds = null;
				if(guestbook.getUser() == null){
				//根据所选地区和类型找出相应类型的人员登录名
				List<String> list = systemService.getUserListByAreaAndType(str,guestbook.getArea().getId());
				userIds = sysUserPushService.pushUser(list,null);
				}
				if(guestbook.getUser()!=null&&!"".equals(guestbook.getUser().getId())){
					userIds = Lists.newArrayList();
					userIds.add(guestbook.getUser().getId());
				}
				List<List<String>> lists = JGpushUtils.averageAssign(userIds);
				for (int i = 0; i < lists.size(); i++) {
					SysUserPush push = new SysUserPush();
					push.getPushMessage().setTitle("");//如果没有则填空
					push.getPushMessage().setMsgContent("你有一条关于《留言标题："+guestbook.getTitle()+"》的留言咨询，请给予回复。");
					push.setUrl("/cms/guestbook/");//需要链接的地址，为controller中的地址。
				    sysUserPushService.pushNotificetion(push,lists.get(i));
				}
			
			
		 }else{
			guestbook.setIsComment("1");
			GuestbookComment guestbookComment=new GuestbookComment();
			guestbookComment.setContent(guestbook.getReContent());
			guestbookComment.setGuestbookId(guestbook.getId());
			guestbookComment.setCommentType("1");
			guestbookCommentService.save(guestbookComment);
			guestbook.setIsInquiries("1");
			guestbook.preUpdate();
			dao.update(guestbook);
		} 
	}
	public List<Guestbook> findUserList(Guestbook guestbook){
		return dao.findUserList(guestbook);
	}
	
	/**
	 * 查询法援评价信息
	 * @param id
	 * @return
	 */
	public List<T> getApi(String id) {
		return dao.getApi(id);
	}
	
	/**
	 * 接口访问统计量
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void statisticNum(String id){
		dao.statisticNum(id);
	};
}
