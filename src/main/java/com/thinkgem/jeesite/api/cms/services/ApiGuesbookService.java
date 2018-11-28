package com.thinkgem.jeesite.api.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.cms.dao.ComplaintDao;
import com.thinkgem.jeesite.modules.cms.dao.GuestbookCommentThumbsUpDao;
import com.thinkgem.jeesite.modules.cms.dao.GuestbookDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Complaint;
import com.thinkgem.jeesite.modules.cms.entity.Guestbook;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookComment;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentThumbsUp;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookEvaluate;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentReService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentThumbsUpService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookEvaluateService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
/**
 * api站点service层
 * @author wanglin
 * @create 2018-04-18 上午10:00
 */
@Service
@Transactional(readOnly = true)
public class ApiGuesbookService {
	@Autowired
	private GuestbookDao guestbookDao;
	@Autowired
	private GuestbookCommentService guestbookCommentService;
	@Autowired
	private GuestbookCommentReService  guestbookCommentReService;
	@Autowired
	private GuestbookCommentThumbsUpService guestbookCommentThumbsUpService;
	@Autowired
    private GuestbookEvaluateService guestbookEvaluateService;
	@Autowired
	private ComplaintDao complaintDao;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private GuestbookCommentThumbsUpDao guestbookCommentThumbsUpDao;
	public Page<Guestbook> findPage(Page<Guestbook> page, Guestbook guestbook) {
		guestbook.setPage(page);
		page.setList(guestbookDao.findList(guestbook));
		return page;
	}
	@Transactional(readOnly = false)
	public void addGustbookComment(GuestbookCommentRe guestbookCommentRe){
		guestbookCommentReService.save(guestbookCommentRe);
		Guestbook guestbook=new Guestbook();
		guestbook.setId(guestbookCommentRe.getGuestbookId());
		guestbook.setIsInquiries("1");
		guestbookDao.update(guestbook); 
	}
	/**
	 * 点赞
	 * @param ct
	 * @return
	 */
	@Transactional(readOnly = false)
	public int addGustbookThumbsUp(GuestbookCommentThumbsUp ct){
		GuestbookCommentThumbsUp gct=guestbookCommentThumbsUpService.get(ct);
		if(gct==null){
			guestbookCommentThumbsUpService.save(ct);
			GuestbookComment gc=guestbookCommentService.get(ct.getCommentId());
			if( gc != null){
				if(ct.isThumbsUp()){
					gc.setThumbsUpTrue(gc.getThumbsUpTrue()+1);
				}else{
					gc.setThumbsUpFalse(gc.getThumbsUpFalse()+1);
				}
				guestbookCommentService.updateThumbsUp(gc);
			}
			return 1;
		}
		//如果不是留言的点赞，那么查询是否是文章的点赞
		Article article = articleService.get(ct.getCommentId());
		if(article!= null){
			if(ct.isThumbsUp()){
				return 0;
			}else{
				
			}
			ct.setUserId(UserUtils.getUser().getId());
			guestbookCommentThumbsUpService.deleteByCommentIdAndUserId(ct);
			return 0;	
		}
		
		//已点赞
		return 0;
	}
	@Transactional(readOnly = false)
	public void addGustbookEvaluate(GuestbookEvaluate guestbookEvaluate){
		guestbookEvaluateService.save(guestbookEvaluate);
	}
	public Page<Guestbook> findUserList(Page<Guestbook> page, Guestbook guestbook) {
		guestbook.setPage(page);
		page.setList(guestbookDao.findUserList(guestbook));
		return page;
	}
	public Page<Guestbook> findUserIsInquiriesList(Page<Guestbook> page, Guestbook guestbook) {
		guestbook.setPage(page);
		page.setList(guestbookDao.findUserIsInquiriesList(guestbook));
		return page;
	}
	public Page<Complaint> findUserComplaintList(Page<Complaint> page, Complaint complaint) {
		complaint.setPage(page);
		page.setList(complaintDao.findUserComplaintList(complaint));
		return page;
	}
	public Page<Complaint> findUserIsInquiriesComplaintList(Page<Complaint> page, Complaint complaint) {
		complaint.setPage(page);
		page.setList(complaintDao.findUserIsInquiriesComplaintList(complaint));
		return page;
	}
	public Page<GuestbookEvaluate> getUserEvaluate(Page<GuestbookEvaluate> page, GuestbookEvaluate guestbookEvaluate){
		return guestbookEvaluateService.findPage(page, guestbookEvaluate);
	}
}
