/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.cms.dao.CommentDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Comment;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApply;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 评论Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class CommentService extends CrudService<CommentDao, Comment> {

	public static final String[] PD_COMMENT = new String[]{"comment", "cms_comment"};
	
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private SystemService systemService;
	
	public Comment get(String id) {
		return super.get(id);
	}
	
	public Page<Comment> findPage(Page<Comment> page, Comment comment) {
		comment.getSqlMap().put("dsf", dataScopeFilter(comment.getCurrentUser(), "of", "a"));
		return super.findPage(page, comment);
	}
	
	@Transactional(readOnly = false)
	public void delete(Comment entity, Boolean isRe) {
		super.delete(entity);
	}
	
	@Transactional(readOnly = false)
	public void save(Comment comment) {
		super.save(comment);
	}
	
/*	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false)
	public ServerResponse submit(Comment comment) {
		ServerResponse errorMsg = null;//错误信息
		
		try {
			User user=UserUtils.getUser();
			comment.setCreateBy(user);
			// 申请发起
			Map<String, Object> vars = Maps.newHashMap();
			// 如果没有流程编号
			if (StringUtils.isBlank(comment.getId())){
				save(comment);
				//获取当前区域的司法所所长的name
				vars.put("spy", getSpy(comment));
				
				actTaskService.startProcess(PD_COMMENT[0], PD_COMMENT[1], comment.getId(), comment.getTitle(), vars);
			}
		} catch (BusinessException e) {
			//获取错误信息
			errorMsg = ServerResponse.createByErrorCodeMessage(e.getCode(),
					e.getMessage());
		}
		return errorMsg;
	
	}
	*//**
	 * 获取审批人列表，通过文章发布所属的，查找所在的机构组织和部门，分发给该部门下的所有人
	 * @param comment
	 * @return
	 *//*
	private List<String> getSpy(Comment comment) {
		// TODO Auto-generated method stub
		//先获取文章的相关信息
		String articleId = comment.getContentId();
		Article article = articleService.get(articleId);
		Office office = article.getOffice();
		Office company = article.getCompany();
		
		User user = new User();
		user.setCompany(company);
		user.setOffice(office);
		
		return getLoginNamesByUserList(user);
	}
	
	*//**
	 * 根据相应条件获得所有用户的登录名
	 * @author 张强
	 * @version 
	 * @param list
	 * @return
	 *//*
	public List<String> getLoginNamesByUserList(User searchUser) throws BusinessException{
		
		List<String> loginNames = null;
		//根据机构和角色获取人员列表
		List<User> userList = systemService.findUserByCondition(searchUser);
		for (int i = 0; i < userList.size(); i++) {
			if( i == 0){
				loginNames = Lists.newArrayList();
			}
			loginNames.add(userList.get(i).getLoginName());
		}
		if(loginNames==null) {
			throw new BusinessException(ResponseCode.ARTICLE_COMMENT_NOT_STAFF.getCode(),ResponseCode.ARTICLE_COMMENT_NOT_STAFF.getDesc());
		}
		return loginNames;
	}
	
	*//**
	 * 获取当前登录人的文章待审批列表
	 * @return
	 *//*
	public List<Article> getToDoCommentListByRole(Page page){
		
		List<Article> list = null;
		User user = UserUtils.getUser();
//		list = dao.getToDoCommentList();
		return null;
	}*/
}
