package com.thinkgem.jeesite.api.dto.vo.common;

import java.util.ArrayList;
import java.util.List;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;

public class PageVo<T> {
	private int pageNo = 1; // 当前页码
	private int pageSize = Integer.valueOf(Global.getConfig("page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）
	private long count;// 总记录数，设置为“-1”表示不查询总数
	private List<T> list = new ArrayList<T>();
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public PageVo() {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	public PageVo(Page page) {
		super();
		this.pageNo = page.getPageNo();
		this.pageSize = page.getPageSize();
		this.count = page.getCount();
	}
	public PageVo(Page<T> page, boolean isT) {
		super();
		this.pageNo = page.getPageNo();
		this.pageSize = page.getPageSize();
		this.count = page.getCount();
		this.list = page.getList();
	}

}
