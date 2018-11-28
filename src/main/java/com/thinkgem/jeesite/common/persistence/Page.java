/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.CookieUtils;

/**
 * 分页类
 * @author ThinkGem
 * @version 2013-7-2
 * @param <T>
 */
public class Page<T> {
	
	private int pageNo = 1; // 当前页码
	private int pageSize = Integer.valueOf(Global.getConfig("page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）
	
	private long count;// 总记录数，设置为“-1”表示不查询总数
	
	private int first;// 首页索引
	private int last;// 尾页索引
	private int prev;// 上一页索引
	private int next;// 下一页索引
	
	private boolean firstPage;//是否是第一页
	private boolean lastPage;//是否是最后一页

	private int length = 8;// 显示页面长度
	private int slider = 1;// 前后显示页面长度
	
	private List<T> list = new ArrayList<T>();
	
	private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc

	private String funcName = "page"; // 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	
	private String funcParam = ""; // 函数的附加参数，第三个参数值。
	
	private String message = ""; // 设置提示消息，显示在“共n条”之后

	public Page() {
		this.pageSize = -1;
	}
	
	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 */
	public Page(HttpServletRequest request, HttpServletResponse response){
		this(request, response, -2);
	}

	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 * @param defaultPageSize 默认分页大小，如果传递 -1 则为不分页，返回所有数据
	 */
	public Page(HttpServletRequest request, HttpServletResponse response, int defaultPageSize){
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("pageNo");
		if (StringUtils.isNumeric(no)){
			CookieUtils.setCookie(response, "pageNo", no);
			this.setPageNo(Integer.parseInt(no));
		}else if (request.getParameter("repage")!=null){
			 no = CookieUtils.getCookie(request, "pageNo");
			if (StringUtils.isNumeric(no)){
				this.setPageNo(Integer.parseInt(no));
			}
		}
		if(defaultPageSize==-1){
			this.pageSize = defaultPageSize;
		}else{
			// 设置页面大小参数（传递repage参数，来记住页码大小）
			String size = request.getParameter("pageSize");
			if (StringUtils.isNumeric(size)){
				CookieUtils.setCookie(response, "pageSize", size);
				this.setPageSize(Integer.parseInt(size));
			}else if (request.getParameter("repage")!=null){
				size = CookieUtils.getCookie(request, "pageSize");
				if (StringUtils.isNumeric(size)){
					this.setPageSize(Integer.parseInt(size));
				}
			}else if (defaultPageSize != -2){
				this.pageSize = defaultPageSize;
			}
		}

		// 设置页面分页函数
        String funcName = request.getParameter("funcName");
        if (StringUtils.isNotBlank(funcName)){
            CookieUtils.setCookie(response, "funcName", funcName);
            this.setFuncName(funcName);
        }else if (request.getParameter("repage")!=null){
            funcName = CookieUtils.getCookie(request, "funcName");
            if (StringUtils.isNotBlank(funcName)){
                this.setFuncName(funcName);
            }
        }
		// 设置排序参数
		String orderBy = request.getParameter("orderBy");
		if (StringUtils.isNotBlank(orderBy)){
			this.setOrderBy(orderBy);
		}
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 */
	public Page(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0);
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 */
	public Page(int pageNo, int pageSize, long count) {
		this(pageNo, pageSize, count, new ArrayList<T>());
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 * @param list 本页数据对象列表
	 */
	public Page(int pageNo, int pageSize, long count, List<T> list) {
		this.setCount(count);
		this.setPageNo(pageNo);
		this.pageSize = pageSize;
		this.list = list;
	}
	
	/**
	 * 初始化参数
	 */
	public void initialize(){
				
		//1
		this.first = 1;
		
		this.last = (int)(count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);
		
		if (this.count % this.pageSize != 0 || this.last == 0) {
			this.last++;
		}

		if (this.last < this.first) {
			this.last = this.first;
		}
		
		if (this.pageNo <= 1) {
			this.pageNo = this.first;
			this.firstPage=true;
		}

		if (this.pageNo >= this.last) {
			this.pageNo = this.last;
			this.lastPage=true;
		}

		if (this.pageNo < this.last - 1) {
			this.next = this.pageNo + 1;
		} else {
			this.next = this.last;
		}

		if (this.pageNo > 1) {
			this.prev = this.pageNo - 1;
		} else {
			this.prev = this.first;
		}
		
		//2
		if (this.pageNo < this.first) {// 如果当前页小于首页
			this.pageNo = this.first;
		}

		if (this.pageNo > this.last) {// 如果当前页大于尾页
			this.pageNo = this.last;
		}
		
	}
	
	/**
	 * 默认输出当前分页标签 
	 * <div class="page">${page}</div>
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		if(count==0){
			sb.append("<div id=\"dataNull\" style=\"width: 100%;height: 200px;text-align: center;\">"+
            "<svg class=\"icon\" style=\"width: 1.5302734375em;margin-top: 51px;/* height: 3em; */vertical-align: middle;fill: currentColor;overflow: hidden;font-size: 54px;\" viewBox=\"0 0 1567 1024\""+
            "version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"888\">"+
            "<path d=\"M156.662278 699.758173h21.097186A10.444152 10.444152 0 0 1 187.994733 710.202325c0 5.765172-4.490985 10.444152-10.235269 10.444152H156.662278v21.097186A10.444152 10.444152 0 0 1 146.218126 751.978932a10.277045 10.277045 0 0 1-10.444152-10.235269V720.646477H114.676787A10.444152 10.444152 0 0 1 104.441518 710.202325c0-5.765172 4.490985-10.444152 10.235269-10.444152H135.773974v-21.097187A10.444152 10.444152 0 0 1 146.218126 668.425717c5.765172 0 10.444152 4.490985 10.444152 10.235269v21.097187z m1378.628042-83.553215v-21.097186A10.277045 10.277045 0 0 0 1524.846168 584.872503a10.444152 10.444152 0 0 0-10.444152 10.235269v21.097186h-21.097186a10.277045 10.277045 0 0 0-10.235269 10.444152c0 5.598065 4.595427 10.444152 10.235269 10.444152h21.097186v21.097187c0 5.744284 4.67898 10.235269 10.444152 10.235268a10.444152 10.444152 0 0 0 10.444152-10.235268V637.093262h21.097187c5.744284 0 10.235269-4.67898 10.235268-10.444152a10.444152 10.444152 0 0 0-10.235268-10.444152H1535.29032zM776.460024 960.861969H250.596979A20.80475 20.80475 0 0 1 229.77134 939.973665c0-11.530344 9.462402-20.888304 20.825639-20.888303h94.728457A83.010119 83.010119 0 0 1 334.212859 877.413196v-605.96969A83.49055 83.49055 0 0 1 417.849627 187.994733H480.430984V167.001988A83.49055 83.49055 0 0 1 564.067752 83.553215h501.152182A83.448773 83.448773 0 0 1 1148.856702 167.001988v605.969689c0 15.185797-4.052331 29.410732-11.133466 41.672166h115.554096c11.551232 0 20.909192 9.274407 20.909192 20.888304 0 11.530344-9.295295 20.888304-20.888304 20.888304H1002.638576v20.992745c0 15.185797-4.052331 29.410732-11.133466 41.672166h11.196131c11.488567 0 20.825639 9.274407 20.825639 20.888303 0 11.530344-9.462402 20.888304-20.825639 20.888304h-109.893365c9.545955 16.000441 7.478013 36.972297-6.41271 50.863019a41.672166 41.672166 0 0 1-59.072122 0L776.460024 960.861969z m76.367638-41.776607h66.424806c22.977134 0 41.609501-18.59059 41.609501-41.881049V270.461756c0-22.559368-18.047494-40.690416-40.314426-40.690416H416.303892c-22.266932 0-40.314426 18.214601-40.314426 40.690416v606.742557c0 23.123352 18.799473 41.881049 41.588613 41.881049h317.084449l-10.736588-10.757477a41.693054 41.693054 0 0 1-10.861918-40.377091l-19.718558-19.739447A146.259902 146.259902 0 0 1 502.363703 627.693525a146.218126 146.218126 0 0 1 220.517822 190.981761l19.739447 19.739447a41.630389 41.630389 0 0 1 40.377091 10.841029L852.827662 919.085362zM1002.638576 814.643843h62.852906A41.797496 41.797496 0 0 0 1107.080095 772.867236V167.106429c0-23.14424-18.632367-41.776607-41.588613-41.776607H563.775316A41.797496 41.797496 0 0 0 522.207592 167.106429v20.888304h396.794216A83.448773 83.448773 0 0 1 1002.638576 271.443506V814.643843zM266.325872 46.998683h31.123572c8.773088 0 15.875111 6.955805 15.875111 15.666228 0 8.647758-7.102023 15.666228-15.875111 15.666228h-31.123572v31.123572c0 8.773088-6.955805 15.875111-15.666228 15.875111a15.770669 15.770669 0 0 1-15.666228-15.875111V78.331139H203.869844A15.728893 15.728893 0 0 1 187.994733 62.664911c0-8.647758 7.102023-15.666228 15.875111-15.666228h31.123572V15.875111c0-8.773088 6.955805-15.875111 15.666228-15.875111 8.647758 0 15.666228 7.102023 15.666228 15.875111v31.123572zM20.888304 939.973665c0-11.530344 9.462402-20.888304 20.825638-20.888303h125.455152c11.488567 0 20.825639 9.274407 20.825639 20.888303 0 11.530344-9.462402 20.888304-20.825639 20.888304H41.713942A20.80475 20.80475 0 0 1 20.888304 939.973665z m658.733544-135.021995a104.441518 104.441518 0 1 0-147.722083-147.722083 104.441518 104.441518 0 0 0 147.722083 147.722083zM459.542681 313.324555a20.888304 20.888304 0 0 1 20.867415-20.888304H710.202325a20.888304 20.888304 0 1 1 0 41.776608H480.430984A20.825639 20.825639 0 0 1 459.542681 313.324555z m0 104.441518c0-11.530344 9.295295-20.888304 20.742085-20.888303h334.505295c11.44679 0 20.742086 9.274407 20.742086 20.888303 0 11.530344-9.295295 20.888304-20.742086 20.888304H480.284766A20.762974 20.762974 0 0 1 459.542681 417.766073z m0 104.441519c0-11.530344 9.316183-20.888304 20.846527-20.888304h146.301679c11.509455 0 20.846527 9.274407 20.846527 20.888304 0 11.530344-9.316183 20.888304-20.846527 20.888303h-146.301679A20.80475 20.80475 0 0 1 459.542681 522.207592zM62.664911 396.87777a62.664911 62.664911 0 1 1 0-125.329822 62.664911 62.664911 0 0 1 0 125.329822z m0-31.332456a31.332456 31.332456 0 1 0 0-62.664911 31.332456 31.332456 0 0 0 0 62.664911zM1357.739739 271.547948a62.664911 62.664911 0 1 1 0-125.329822 62.664911 62.664911 0 0 1 0 125.329822z m0-31.332456a31.332456 31.332456 0 1 0 0-62.664911 31.332456 31.332456 0 0 0 0 62.664911z\""+
            "fill=\"#8A96A3\" p-id=\"889\"></path></svg><p style=\"margin-top: 10px;color: #999;\">暂无数据</p></div>");
		}else{
			if (pageNo == first) {// 如果是首页
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; 上一页</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+prev+","+pageSize+",'"+funcParam+"');\">&#171; 上一页</a></li>\n");
			}

			int begin = pageNo - (length / 2);

			if (begin < first) {
				begin = first;
			}

			int end = begin + length - 1;

			if (end >= last) {
				end = last;
				begin = end - length + 1;
				if (begin < first) {
					begin = first;
				}
			}

			if (begin > first) {
				int i = 0;
				for (i = first; i < first + slider && i < begin; i++) {
					sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+",'"+funcParam+"');\">"
							+ (i + 1 - first) + "</a></li>\n");
				}
				if (i < begin) {
					sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
				}
			}

			for (int i = begin; i <= end; i++) {
				if (i == pageNo) {
					sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
							+ "</a></li>\n");
				} else {
					sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+",'"+funcParam+"');\">"
							+ (i + 1 - first) + "</a></li>\n");
				}
			}

			if (last - end > slider) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
				end = last - slider;
			}

			for (int i = end + 1; i <= last; i++) {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+",'"+funcParam+"');\">"
						+ (i + 1 - first) + "</a></li>\n");
			}

			if (pageNo == last) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">下一页 &#187;</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+next+","+pageSize+",'"+funcParam+"');\">"
						+ "下一页 &#187;</a></li>\n");
			}

			sb.append("<li class=\"disabled controls\"><a href=\"javascript:\">当前 ");
			sb.append("<input ondragover=\"return false\" type=\"text\" value=\""+pageNo+"\" onkeypress=\"var e=window.event||event;var c=e.keyCode||e.which;if(c==13)");
			sb.append(funcName+"(this.value,"+pageSize+",'"+funcParam+"');\" onclick=\"this.select();\"/> / ");
			sb.append("<input ondragover=\"return false\" type=\"text\" value=\""+pageSize+"\" onkeypress=\"var e=window.event||event;var c=e.keyCode||e.which;if(c==13)");
			sb.append(funcName+"("+pageNo+",this.value,'"+funcParam+"');\" onclick=\"this.select();\"/> 页，");
			sb.append("共 " + count + " 条"+(message!=null?message:"")+"</a></li>\n");

			sb.insert(0,"<ul>\n").append("</ul>\n");
			
			sb.append("<div style=\"clear:both;\"></div>");
		}
		
		return sb.toString();
	}
	
	/**
	 * 获取分页HTML代码
	 * @return
	 */
	public String getHtml(){
		return toString();
	}

	/**
	 * 获取设置总数
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 设置数据总数
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
		if (pageSize >= count){
			pageNo = 1;
		}
	}
	
	/**
	 * 获取当前页码
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}
	
	/**
	 * 设置当前页码
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	/**
	 * 获取页面大小
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置页面大小（最大500）
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize;// > 500 ? 500 : pageSize;
	}

	/**
	 * 首页索引
	 * @return
	 */
	@JsonIgnore
	public int getFirst() {
		return first;
	}

	/**
	 * 尾页索引
	 * @return
	 */
	@JsonIgnore
	public int getLast() {
		return last;
	}
	
	/**
	 * 获取页面总数
	 * @return getLast();
	 */
	@JsonIgnore
	public int getTotalPage() {
		return getLast();
	}

	/**
	 * 是否为第一页
	 * @return
	 */
	@JsonIgnore
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * 是否为最后一页
	 * @return
	 */
	@JsonIgnore
	public boolean isLastPage() {
		return lastPage;
	}
	
	/**
	 * 上一页索引值
	 * @return
	 */
	@JsonIgnore
	public int getPrev() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * 下一页索引值
	 * @return
	 */
	@JsonIgnore
	public int getNext() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}
	
	/**
	 * 获取本页数据对象列表
	 * @return List<T>
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设置本页数据对象列表
	 * @param list
	 */
	public Page<T> setList(List<T> list) {
		this.list = list;
		if(list==null){
			list=new ArrayList<>();
		}
		initialize();
		return this;
	}

	/**
	 * 获取查询排序字符串
	 * @return
	 */
	@JsonIgnore
	public String getOrderBy() {
		// SQL过滤，防止注入 
		String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
					+ "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
		Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		if (sqlPattern.matcher(orderBy).find()) {
			return "";
		}
		return orderBy;
	}

	/**
	 * 设置查询排序，标准查询有效， 实例： updatedate desc, name asc
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 获取点击页码调用的js函数名称
	 * function ${page.funcName}(pageNo){location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+i;}
	 * @return
	 */
	@JsonIgnore
	public String getFuncName() {
		return funcName;
	}

	/**
	 * 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	 * @param funcName 默认为page
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * 获取分页函数的附加参数
	 * @return
	 */
	@JsonIgnore
	public String getFuncParam() {
		return funcParam;
	}

	/**
	 * 设置分页函数的附加参数
	 * @return
	 */
	public void setFuncParam(String funcParam) {
		this.funcParam = funcParam;
	}

	/**
	 * 设置提示消息，显示在“共n条”之后
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 分页是否有效
	 * @return this.pageSize==-1
	 */
	@JsonIgnore
	public boolean isDisabled() {
		return this.pageSize==-1;
	}
	
	/**
	 * 是否进行总数统计
	 * @return this.count==-1
	 */
	@JsonIgnore
	public boolean isNotCount() {
		return this.count==-1;
	}
	
	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getCount()) {
			firstResult = 0;
		}
		return firstResult;
	}
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
	}

//	/**
//	 * 获取 Spring data JPA 分页对象
//	 */
//	public Pageable getSpringPage(){
//		List<Order> orders = new ArrayList<Order>();
//		if (orderBy!=null){
//			for (String order : StringUtils.split(orderBy, ",")){
//				String[] o = StringUtils.split(order, " ");
//				if (o.length==1){
//					orders.add(new Order(Direction.ASC, o[0]));
//				}else if (o.length==2){
//					if ("DESC".equals(o[1].toUpperCase())){
//						orders.add(new Order(Direction.DESC, o[0]));
//					}else{
//						orders.add(new Order(Direction.ASC, o[0]));
//					}
//				}
//			}
//		}
//		return new PageRequest(this.pageNo - 1, this.pageSize, new Sort(orders));
//	}
//	
//	/**
//	 * 设置 Spring data JPA 分页对象，转换为本系统分页对象
//	 */
//	public void setSpringPage(org.springframework.data.domain.Page<T> page){
//		this.pageNo = page.getNumber();
//		this.pageSize = page.getSize();
//		this.count = page.getTotalElements();
//		this.list = page.getContent();
//	}
	
}
