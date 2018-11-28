package com.thinkgem.jeesite.modules.language.listener;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.thinkgem.jeesite.modules.language.service.MutiLangServiceI;

public class InitListener implements javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		WebApplicationContext webApplicationContext =WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		MutiLangServiceI mutiLangService = (MutiLangServiceI) webApplicationContext.getBean("mutiLangService");
		mutiLangService.initAllMutiLang(null);
		
	}

}
