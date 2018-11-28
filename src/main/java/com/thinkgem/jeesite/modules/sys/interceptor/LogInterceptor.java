/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.interceptor;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 日志拦截器
 * @author ThinkGem
 * @version 2014-8-19
 */
public class LogInterceptor extends BaseService implements HandlerInterceptor {

    @Autowired
    MenuDao menuDao;

	private static final ThreadLocal<Long> startTimeThreadLocal =
			new NamedThreadLocal<Long>("ThreadLocal StartTime");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		if (logger.isDebugEnabled()){
			long beginTime = System.currentTimeMillis();//1、开始时间  
	        startTimeThreadLocal.set(beginTime);		//线程绑定变量（该数据只有当前请求的线程可见）  
	        logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS")
	        	.format(beginTime), request.getRequestURI());
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
        String query = request.getParameter("query");
        if(StringUtils.isNotBlank(query)){
            logger.debug("API接口接收到表单数据：{}",query);
            if (modelAndView != null) {
                logger.info("相应body: " + modelAndView.getView());
            }
        }else if (modelAndView != null){
            logger.info("ViewName: " + modelAndView.getViewName());
        }
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
//		String title = getTitleByUri(request.getRequestURI());
		LogUtils.saveLog(request,handler,ex,null);
		// 打印JVM信息。
		if (logger.isDebugEnabled()){
			long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）  
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
	        logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
	        		new SimpleDateFormat("hh:mm:ss.SSS").format(endTime), DateUtils.formatDateTime(endTime - beginTime),
					request.getRequestURI(), Runtime.getRuntime().maxMemory()/1024/1024, Runtime.getRuntime().totalMemory()/1024/1024, Runtime.getRuntime().freeMemory()/1024/1024, 
					(Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024); 
	        //删除线程变量中的数据，防止内存泄漏
	        startTimeThreadLocal.remove();
		}
		
	}

	/**
	 * 根据请求uri获取对应的title
	 * @param requestURI
	 * @return
	 */
	private String getTitleByUri(String requestURI) {
        Object o = CacheUtils.get(requestURI);
        if(null == o){
            List<String> menus = menuDao.distinctFindNameByUri(requestURI);
            if(null != menus && menus.size() > 0){
                String title = menus.get(0);
                CacheUtils.put(requestURI,title);
                return title;
            }
            return "";
        }
        return o.toString();
	}

}
