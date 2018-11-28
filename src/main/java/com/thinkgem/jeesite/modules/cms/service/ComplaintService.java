/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.entity.Complaint;
import com.thinkgem.jeesite.modules.cms.entity.ComplaintCount;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookComment;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookCommentRe;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.cms.dao.ComplaintDao;

/**
 * 投诉建议Service
 * @author wanglin
 * @version 2018-05-11
 */
@Service
@Transactional(readOnly = true)
public class ComplaintService extends CrudService<ComplaintDao, Complaint> {

	@Autowired
    private GuestbookCommentService guestbookCommentService;
	
	@Autowired
	private GuestbookCommentReService guestbookCommentReService;
	
	public Complaint get(String id) {
		return dao.get(id);
	}
	
	public List<Complaint> findList(Complaint complaint) {
		return super.findList(complaint);
	}
	
	public Page<Complaint> findPage(Page<Complaint> page, Complaint complaint) {
		return super.findPage(page, complaint);
	}
	//投诉建议保存及回复处理逻辑
	@Transactional(readOnly = false)
	public void save(Complaint complaint) {
		if(complaint.getIsNewRecord()) {
          complaint.preInsert();
          complaint.setIsComment("0");
          complaint.setIsInquiries("0");
          dao.insert(complaint);
		}else {
			complaint.setIsComment("1");;
			GuestbookComment guestbookComment=new GuestbookComment();
			guestbookComment.setContent(complaint.getReContent());
			guestbookComment.setGuestbookId(complaint.getId());
			guestbookComment.setCommentType("1");
			guestbookCommentService.save(guestbookComment);
			complaint.preUpdate();
			dao.update(complaint);
		}
		
	}
	
	@Transactional(readOnly = false)
	public void nameyuyu(String id,String content,Complaint complaint) {
		GuestbookCommentRe guestbookCommentre=new GuestbookCommentRe();
		guestbookCommentre.setCommentId(id);
		guestbookCommentre.setContent(content);
		guestbookCommentre.setGuestbookId(complaint.getId());
		guestbookCommentre.setCommentType("1");
		guestbookCommentre.setCreateBy(UserUtils.getUser());
		guestbookCommentReService.save(guestbookCommentre);
	}
	
	@Transactional(readOnly = false)
	public void SaveRe(String id) {
		
		GuestbookComment guestbookComment=new GuestbookComment();
		guestbookComment.setCommentType("0");
		guestbookComment.setId(id);
		guestbookCommentService.save(guestbookComment);
	
		
	};
	
	@Transactional(readOnly = false)
	public void delete(Complaint complaint) {
		super.delete(complaint);
	}
	public Page<Complaint> findUserComplaintList(Page<Complaint> page, Complaint complaint) {
		complaint.setPage(page);
		page.setList(dao.findUserComplaintList(complaint));
		return page;
	}
	public Page<Complaint> findUserIsInquiriesComplaintList(Page<Complaint> page, Complaint complaint) {
		complaint.setPage(page);
		page.setList(dao.findUserIsInquiriesComplaintList(complaint));
		return page;
	}
	
	/**
	 * 全盟各旗县法律援助中心接受投诉数量与处理投诉数量对比图
	 * @author 王鹏
	 * @version 2018-06-08 15:25:28
	 * @param cc
	 * @return
	 */
	public Map<String, Object> countComplaintCommentCompared(ComplaintCount cc){
		if(cc == null || StringUtils.isBlank(cc.getBeginDate())
				|| StringUtils.isBlank(cc.getEndDate())
				|| StringUtils.isBlank(cc.getType())
				|| cc.getCountNames() == null) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		//申请数量
		List<ComplaintCount> aidList = dao.countComplaintComment(cc);
		//已经受理(有回复)
		cc.setIsComment("1");
		List<ComplaintCount> commentList = dao.countComplaintComment(cc);
		List<String> xData = Lists.newArrayList();//横坐标数据
		//获取两个日期间的所有数据(年、年月、年月日)
		xData = DateUtils.getAllDayBetweenDate(cc.getBeginDate(), cc.getEndDate());
		resultMap.put("xData", xData);//存入横坐标
		//纵坐标数据
		List<Map<String, Object>> yData = Lists.newArrayList();
		Map<String, Object> aidMap = Maps.newHashMap();//申请集合
		aidMap.put("name", cc.getCountNames()[0]);
		aidMap.put("type", "line");//折线图
		Map<String, Object> commentMap = Maps.newHashMap();//受理集合
		commentMap.put("name", cc.getCountNames()[1]);
		commentMap.put("type", "line");//折线图
		yData.add(aidMap);//存入申请
		yData.add(commentMap);//存入受理;
		resultMap.put("yData", yData);//存入纵坐标(数据)
		Integer[] aidArr = new Integer[xData.size()];
		Integer[] commentArr = new Integer[xData.size()];
		for (int i = 0; i < xData.size(); i++) {
			aidArr[i] = 0;//放一个默认值
			commentArr[i] = 0;//放一个默认值
			//放入申请数量
			for (int j = 0; j < aidList.size(); j++) {
				if(xData.get(i).equals(aidList.get(j).getYear())) {
					//如果年度相符，就放入数量
					aidArr[i] = aidList.get(j).getCount();
					break;
				}
			}
			//放入受理数量
			for (int j = 0; j < commentList.size(); j++) {
				if(xData.get(i).equals(commentList.get(j).getYear())) {
					//如果年度相符，就放入数量
					commentArr[i] = commentList.get(j).getCount();
					break;
				}
			}
		}
		aidMap.put("data", Arrays.asList(aidArr));//放入相应数据
		commentMap.put("data", Arrays.asList(commentArr));//放入受理数据
		return resultMap;
	}
	/**
	 * 
	 * 全盟各旗县法律援助中心接受投诉数量,获取14个地区的所有数据
	 * @param cc
	 * @return
	 */
	public Map<String, Object> countComplaintCommentCompared1(ComplaintCount cc){
		if(cc == null || StringUtils.isBlank(cc.getBeginDate())
				|| StringUtils.isBlank(cc.getEndDate())
				|| StringUtils.isBlank(cc.getType())
				|| cc.getCountNames() == null) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		List<String> xData = Lists.newArrayList();//横坐标数据
		//获取两个日期间的所有数据(年、年月、年月日)
		xData = DateUtils.getAllDayBetweenDate(cc.getBeginDate(), cc.getEndDate());
		StringBuffer dsf = new StringBuffer();
		for(int i = 0 ; i < xData.size(); i++){
			if(i==xData.size()-1){
				dsf.append("SELECT '"+xData.get(i)+"'");
			}else{
				dsf.append("SELECT '"+xData.get(i)+"' YEAR UNION ALL ");
			}
		}
		
		cc.getSqlMap().put("dsf", dsf.toString());
		//申请数量
		List<ComplaintCount> aidList = dao.countComplaintComment1(cc);
		resultMap.put("xData", xData);//存入横坐标
		
		//纵坐标数据
		List<Map<String, Object>> yData = Lists.newArrayList();
		Map<String, Object> acceptMap = Maps.newHashMap();//受理集合

		int size = xData.size();
		int index = 0 ;
		Integer[] acceptArr = null;
				
		for (int i = 0 ; i<aidList.size();i++){
			if(i==0 || index ==0){
				acceptMap = Maps.newHashMap();//受理集合
				acceptMap.put("name", aidList.get(i).getArea().getName());
				acceptMap.put("type", "line");//折线图
				acceptMap.put("smooth", true);//折线图
				
				Map<String,Object> areaStyle = Maps.newHashMap();
				areaStyle.put("normal",  Maps.newHashMap());
				acceptMap.put("areaStyle",areaStyle );
				acceptArr = new Integer[xData.size()];
			}
			acceptArr[index] = aidList.get(i).getCount();
			if(index==size-1){
				acceptMap.put("data", Arrays.asList(acceptArr));
				yData.add(acceptMap);//存入受理;
				index=0;
			}else{
				index++;
			}
		}
		resultMap.put("yData", yData);//存入纵坐标(数据)
		
		return resultMap;
		
	}
	

	/**
	 * 大屏统计(折线图): 各旗县律师收到投诉数量
	 * @author 王鹏
	 * @version 2018-6-11 11:35:14
	 * @param cc
	 * @return
	 */
	public Map<String, Object> countComplaintLawOffice(ComplaintCount cc){
		if(cc == null || StringUtils.isBlank(cc.getBeginDate())
				|| StringUtils.isBlank(cc.getEndDate())
				|| StringUtils.isBlank(cc.getType())) {
			throw new BusinessException("参数不全");
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		List<ComplaintCount> list = dao.countComplaintLawOffice(cc);
		List<String> xData = Lists.newArrayList();//横坐标数据
		resultMap.put("xData", xData);//存入横坐标
		//纵坐标数据
		List<Integer> yData = Lists.newArrayList();
		resultMap.put("yData", yData);//存入纵坐标(数据)
		for (int i = 0; i < list.size(); i++) {
			xData.add(list.get(i).getArea().getName());
			yData.add(list.get(i).getCount());
		}
		return resultMap;
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