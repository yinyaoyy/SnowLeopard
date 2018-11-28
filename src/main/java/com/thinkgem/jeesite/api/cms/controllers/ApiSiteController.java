package com.thinkgem.jeesite.api.cms.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinkgem.jeesite.api.cms.services.ApiSiteService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.form.SiteIdForm;
import com.thinkgem.jeesite.api.dto.vo.cms.ColumnVo;
import com.thinkgem.jeesite.api.dto.vo.cms.SiteVo;
import com.thinkgem.jeesite.api.dto.vo.cms.SysServiceVo;
import com.thinkgem.jeesite.api.dto.vo.sys.ServerAppVo;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.appmange.service.SysServiceService;
import com.thinkgem.jeesite.modules.cms.entity.Category;

/**
 * 栏目API接口
 * 此类接口介不需要登录
 * @author kakasun
 * @create 2018-04-17 上午9:55
 */
@RestController
@RequestMapping("/api/100/300")
public class ApiSiteController {

    @Autowired
    ApiSiteService apiSiteService;
    @Autowired
    SysServiceService sysServiceService;
    private static final Logger log = LoggerFactory.getLogger(ApiSiteController.class);

    /**
     * 获取所有站点
     * @return
     */
    @RequestMapping("/10")
    public ServerResponse<List<SiteVo>>  allSite(BaseForm<?> form){
        List<SiteVo> allSite = apiSiteService.getAllSite();
        ServerResponse<List<SiteVo>> result = ServerResponse.createBySuccess(allSite);
        log.debug("返回参数：{}",result);
        return result;
    }

    /**
     * 根据站点返回站点包含栏目
     * @return
     */
    @RequestMapping("/20")
    public ServerResponse allColumns(BaseForm<SiteIdForm> form){
        log.debug("接收到参数：{}",form);
        form.initQueryObj(new TypeReference<SiteIdForm>(){});
        List<ColumnVo> columnVoList = apiSiteService.getAllColumnsBySiteId(form.getQueryObj().getSiteId());
        ServerResponse<List<ColumnVo>> result = ServerResponse.createBySuccess(columnVoList);
        log.debug("返回参数：{}",result);
        return result;
    }
    /**
     * 获取所有web服务
     * @return
     */
    @RequestMapping("/30")
    public ServerResponse allAllService(BaseForm<SysServiceVo> form){
        log.debug("接收到参数：{}",form);
        form.initQueryObj(new TypeReference<SysServiceVo>(){});
        ServerResponse<List<SysServiceVo>> result = ServerResponse.createBySuccess(apiSiteService.allAllService());
        log.debug("返回参数：{}",result);
        return result;
    }
    /**
     * 通过人员机构id获取服务详细信息
     * @return
     */
    @RequestMapping("/40")
    public ServerResponse findByServiceId(BaseForm<SysServiceVo> form){
        log.debug("接收到参数：{}",form);
        form.initQueryObj(new TypeReference<SysServiceVo>(){});
        ServerResponse<SysServiceVo> result = ServerResponse.createBySuccess(apiSiteService.findByServiceId(form.getQueryObj().getId()));
        log.debug("返回参数：{}",result);
        return result;
    }
    /**
     * 通过栏目名获取服务列表
     * @return
     */
    @RequestMapping("/50")
    public ServerResponse findByCategoryName(BaseForm<Map<String,String>> form){
		JSONObject bject=(JSONObject) JSONObject.parse(form.getQuery());
		Category category=new Category();
		String categoryType=bject.getByte("categoryType").toString();
		switch (categoryType) {
		case "1":
			category.setName("政策发布");
			break;
		case "2":
			category.setName("服务动态");
			break;
		case "3":
			category.setName("法律法规");
			break;
		case "4":
			category.setName("服务指南");
			break;
		case "5":
			category.setName("文书表格");
			break;
			
		default:
			break;
		}
        log.debug("接收到参数：{}",form);
        //form.initQueryObj(new TypeReference<SysServiceVo>(){});
        ServerResponse<List<ServerAppVo>> result = ServerResponse.createBySuccess(apiSiteService.findByCategoryName(form,category));
        log.debug("返回参数：{}",result);
        return result;
    }
}
