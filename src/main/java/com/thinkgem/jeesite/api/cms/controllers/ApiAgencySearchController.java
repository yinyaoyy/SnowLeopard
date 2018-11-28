package com.thinkgem.jeesite.api.cms.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.api.cms.ApiAgencySearch;
import com.thinkgem.jeesite.api.cms.services.ApiSiteService;
import com.thinkgem.jeesite.api.dto.form.AgencyForm;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.AgencyCategoryVo;
import com.thinkgem.jeesite.api.dto.vo.AgencyVo;
import com.thinkgem.jeesite.api.dto.vo.common.PageVo;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.cms.entity.GuestbookEvaluate;
import com.thinkgem.jeesite.modules.cms.service.GuestbookEvaluateService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 文章搜索API接口
 * 此类接口介不需要登录
 * @author 王鹏
 * @version 2018-04-18 09:40:02
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/100/500")
public class ApiAgencySearchController {

    @Autowired
    private ApiSiteService apiSiteService;
    private ApiAgencySearch apiAgencySearch;
    @Autowired
    private AreaService areaService;
//    @Autowired
//    private LawyerService lawyerService;
    //机构分类字典识别码
    private static final String DICT_AGENCY_CAGETORY = "sys_office_category";
    private static final Logger log = LoggerFactory.getLogger(ApiAgencySearchController.class);
    //锡林郭勒盟  sys_area 中对应的id(为了只获取锡盟下属的地区)
    private static final String AREA_PARENT_ID = "5";
//    //律师执业类别
//    private static final String LAWYER_LICENSE_TYPE = "lawyer_license_type";

    /**
     * 根据提交的信息初始化form内容
     * 并反射出相应的apiAgencySearch服务
     * @author 王鹏
     * @version 2018-04-23 15:15:46
     * @param form
     */
    private void getApiAgencySearchByAgencyForm(BaseForm<AgencyForm> form) {
		form.initQueryObj(AgencyForm.class);
		Dict dict = DictUtils.getDictByTypeValue(DICT_AGENCY_CAGETORY, form.getQueryObj().getCategoryId());
		apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
    }
    
    /**
     * 查询所有机构类型
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse search(BaseForm<AgencyForm> form){
    	ServerResponse result = null;
    	try {
    		//获取字典表中的所有机构分类
    		List<Dict> dicts = DictUtils.getDictList(DICT_AGENCY_CAGETORY);
    		//将字典内容转换为分类实体内容
    		List<AgencyCategoryVo> list = new ArrayList<AgencyCategoryVo>();
    		AgencyCategoryVo acv = null;
    		for (int i = 0; i < dicts.size(); i++) {
    			acv = new AgencyCategoryVo();
    			acv.setCategoryId(dicts.get(i).getValue());
    			acv.setCategoryName(dicts.get(i).getLabel());
    			acv.setServiceVo(apiSiteService.findByOfficeId(acv.getCategoryId()));
    			list.add(acv);
    		}
    		result = ServerResponse.createBySuccess(list);
    		log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }

    /**
     * 根据机构类型、名称和地区查询机构列表信息
     * @return
     */
	@RequestMapping("/20")
    public ServerResponse searchByCategory(BaseForm<AgencyForm> form){
    	ServerResponse result = null;
    	try {
    		//获取字典表对应的机构信息(备注中包含表名)
    		getApiAgencySearchByAgencyForm(form);
    		//将字典内容转换为分类实体内容
    		AgencyForm agencyForm=form.getQueryObj();
    		PageVo<AgencyVo> list = apiAgencySearch.searchAgency(agencyForm);
    		//String categoryId=agencyForm.getCategoryId();
    		/*
    		if(StringUtils.isNotEmpty(agencyForm.getIsEvaluate())&&"true".equals(agencyForm.getIsEvaluate())){
        		if("1".equals(categoryId)||"7".equals(categoryId)||"6".equals(categoryId)||"9".equals(categoryId)||"11".equals(categoryId)){
        			List<AgencyVo> fomeList=list.getList();
        			User user=new User();
        			GuestbookEvaluate guestbookEvaluate=new GuestbookEvaluate();
        			for (AgencyVo agencyVo : fomeList) {
        				agencyVo.setType(DictUtils.getDictLabel(categoryId, "sys_office_category"));
        				if(agencyVo.getId()!=null){
            				user.setId(agencyVo.getId());
            				guestbookEvaluate.setBeEvaluatedUser(user);
            				agencyVo.setEvaluate(guestbookEvaluateService.getEvaluatedByUser(guestbookEvaluate));
        				}    
    				}
        		}
    		}
    		*/
    		result = ServerResponse.createBySuccess(list);
    		log.debug("返回参数：{}",result);
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.toString(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }

    /**
     * 根据机构类型统计机构数量
     * @return
     */
	@RequestMapping("/30")
    public ServerResponse countAgencyForm(BaseForm<Area> form){
    	ServerResponse result = null;
    	try {
    		Area area = form.initQueryObj(Area.class);
    		//获取字典表中的所有机构分类
    		List<Dict> dicts = DictUtils.getDictList(DICT_AGENCY_CAGETORY);
    		List<Map<String, Object>> resultList = Lists.newArrayList();
    		Dict dict = null;//字典
    		Map<String, Object> map = null;
    		//机构数量统计
    		for (int i = 0; i < dicts.size(); i++) {
    			dict = dicts.get(i);
    			if("人员分类".equals(dict.getDescription())) {
    				continue;//目前只统计机构的
    			}
    			map = Maps.newHashMap();
    			map.put("name", dict.getLabel());
    			map.put("count", 0);//默认0个
    			resultList.add(map);
				if(StringUtils.isBlank(dict.getRemarks())) {
					continue;//如果没有配置，则不能统计数量
				}
				//如果有相应配置(必须确保正确),则调用服务层统计实际数量
				apiAgencySearch = SpringContextHolder.getBean(dict.getRemarks());
				//放入统计数量
				map.put("count", apiAgencySearch.countAgency(area.getId()));
			}
    		result = ServerResponse.createBySuccess(resultList);
    		log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数{},详细信息:\n{}",e.getMessage(),form.getQuery(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }

    /**
     * 查询所有区域
     * @return
     */
	@RequestMapping("/40")
    public ServerResponse searchArea(BaseForm<Map> form){
    	ServerResponse result = null;
    	try {
    		form.initQueryObj(Map.class);
    		String parentId=AREA_PARENT_ID;
    		if(form!=null&&form.getQueryObj()!=null&&form.getQueryObj().get("parentId")!=null){
    			parentId=String.valueOf(form.getQueryObj().get("parentId"));
    			if(StringUtils.isBlank(parentId)){
    				parentId=AREA_PARENT_ID;
    			}
    		}
    		result = ServerResponse.createBySuccess(areaService.findByParent(parentId));
    		log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	
    /**
     * 查询各个机构的详细信息(如律所详细信息、律师详细信息等)
     * @return
     */
	@RequestMapping("/50")
    public ServerResponse searchById(BaseForm<AgencyForm> form){
    	ServerResponse result = null;
    	try {
    		getApiAgencySearchByAgencyForm(form);
    		result = ServerResponse.createBySuccess(apiAgencySearch.searchAgencyById(form.getQueryObj()));
    		log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	
}
