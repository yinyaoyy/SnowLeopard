package com.thinkgem.jeesite.api.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.cms.dao.ComplaintDao;
import com.thinkgem.jeesite.modules.cms.entity.Complaint;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;
import com.thinkgem.jeesite.modules.cms.service.ComplaintService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentReService;
import com.thinkgem.jeesite.modules.cms.service.GuestbookCommentService;
/**
 * api站点service层
 * @author wanglin
 * @create 2018-04-18 上午10:00
 */
@Service
@Transactional(readOnly = true)
public class ApiComplaintService {
	@Autowired
	private ComplaintService complaintService;
	@Autowired
	private ComplaintDao complaintDao;
	@Autowired
    private GuestbookCommentService guestbookCommentService;
	@Autowired
	private GuestbookCommentReService  guestbookCommentReService;


	public Page<Complaint> findUserComplaintList(Page<Complaint> page, Complaint complaint) {
		return complaintService.findUserComplaintList(page,complaint);
	}
	public Page<Complaint> findUserIsInquiriesComplaintList(Page<Complaint> page, Complaint complaint) {
		return complaintService.findUserIsInquiriesComplaintList(page, complaint);
	}
	@Transactional(readOnly = false)
    public void addComplaint(Complaint complaint){
    	complaintService.save(complaint);
    }
	@Transactional(readOnly = false)
	public void addComplaintComment(GuestbookCommentRe guestbookCommentRe){
		guestbookCommentReService.save(guestbookCommentRe);
		Complaint complaint=new Complaint();
		complaint.setId(guestbookCommentRe.getGuestbookId());
		complaint.setIsInquiries("1");
		complaintDao.update(complaint); 
	}

}
