package com.thinkgem.jeesite.common.utils;

import java.util.ArrayList;
import java.util.List;



/**
 * <pre>
 * 	功能描述:分页辅助类
 * </pre>
 * @param <T>
 * @return
 */

public class PageHelper<T> {
	
	private int currentPage ;//构造参数
	private int pageSize ;//构造参数
	private List<T> allRecordList ;//构造参数(全部查询记录，分页前)
	//计算所得
	private int recordCount ;//总记录数
	private int pageCount ;//总页数
	private int starIndex ;//分页起点位置
	private int endIndex ;//分页终点位置
	
	
	public PageHelper(Integer currentPage, Integer pageSize, List<T> allRecordList) {
		if(currentPage==null||currentPage<0){
			this.currentPage = 1 ;
		}else{
			this.currentPage = currentPage;
		}
		if(pageSize == null ||pageSize<=0){
			this.pageSize = 10 ;
		}else{
			this.pageSize = pageSize;
		}
		
		
		this.allRecordList = allRecordList;
		if(this.allRecordList==null){
			this.allRecordList = new ArrayList<T>() ;
		}
		this.recordCount = this.allRecordList.size() ;
		//总页数
		this.pageCount = (this.recordCount+ this.pageSize - 1) / this.pageSize; // 总页数
		if(this.currentPage>this.pageCount){
			this.currentPage = this.pageCount ;
		}
		//起点，终点位置
		this.starIndex = (currentPage - 1)* this.pageSize ;
		if(this.starIndex<0){//总记录数为0时起点为0
			this.starIndex = 0 ;
		}
		if (this.currentPage * this.pageSize > this.recordCount) {
			this.endIndex = recordCount ;
		}else{
			this.endIndex = (this.currentPage * this.pageSize) ; 
		}
	}
	
	public List<T> getPageRecordList(){
		return this.allRecordList.subList(this.starIndex, this.endIndex) ;
	}
	
	
	
	
	


	public int getCurrentPage() {
		return currentPage;
	}



	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}



	public int getPageSize() {
		return pageSize;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public List<T> getAllRecordList() {
		return allRecordList;
	}
	public void setAllRecordList(List<T> allRecordList) {
		this.allRecordList = allRecordList;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getStarIndex() {
		return starIndex;
	}
	public void setStarIndex(int starIndex) {
		this.starIndex = starIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	
	

}
