package com.thinkgem.jeesite.modules.chart.peopleMediation;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationAcceptRegisterCount;
import com.thinkgem.jeesite.modules.oa.entity.act.OaPeopleMediationApplyCount;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationAcceptRegisterService;
import com.thinkgem.jeesite.modules.oa.service.act.OaPeopleMediationApplyService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

@Controller
@RequestMapping(value = "${adminPath}/chart/peopleMediation")
public class PeopleMediationChartController extends BaseController {

	//锡林郭勒盟  sys_area 中对应的id(为了只获取锡盟下属的地区)
    private static final String AREA_PARENT_ID = "5";
    
	@Autowired
	private OaPeopleMediationApplyService oaPeopleMediationApplyService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private OaPeopleMediationAcceptRegisterService oaPeopleMediationAcceptRegisterService;
	
	

//	@RequiresPermissions("chart:peopleMediation:view:countByYearArea")
	@RequestMapping(value="toCountByYearArea")
	public String toCountByYearArea(OaPeopleMediationApplyCount opmac, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID);
		//设置默认年度
		if(StringUtils.isBlank(opmac.getYear())) {
			opmac.setYear(DateUtils.getYear());
		}
		model.addAttribute("areaList", areaList);
		return "modules/chart/peopleMediation/countYearArea";
	}
//	@RequiresPermissions("chart:peopleMediation:view:countByYearArea")
	@RequestMapping(value="countByYearArea")
	@ResponseBody
	public List<OaPeopleMediationApplyCount> countByYearArea(OaPeopleMediationApplyCount opmac) {
		//获取相应数据
		List<OaPeopleMediationApplyCount> list = oaPeopleMediationApplyService.countByYearArea(opmac); 
		return list;
	}
	
//	@RequiresPermissions("chart:peopleMediation:view:countByYearCaseRank")
	@RequestMapping(value="toCountByYearCaseRank")
	public String toCountByYearCaseRank(OaPeopleMediationAcceptRegisterCount opmarc, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		List<Map<String, String>> list = Lists.newArrayList();
		Map<String, String> map = null;
		for (int i = 0; i < areaList.size(); i++) {
			map = Maps.newHashMap();
			map.put("id", areaList.get(i).getId());
			map.put("name", areaList.get(i).getName());
			list.add(map);
		}
		//加载严重等级字典
		List<Dict> dictList = DictUtils.getDictList("case_rank");
		List<Map<String, String>> rankList = Lists.newArrayList();
		Map<String, String> rankMap = null;
		for (int i = 0; i < dictList.size(); i++) {
			rankMap = Maps.newHashMap();
			rankMap.put("value", dictList.get(i).getValue());
			rankMap.put("label", dictList.get(i).getLabel());
			rankList.add(rankMap);
		}
		model.addAttribute("areaList", JSON.toJSONString(list));
		model.addAttribute("rankList", JSON.toJSONString(rankList));
		return "modules/chart/peopleMediation/countYearCaseRank";
	}
//	@RequiresPermissions("chart:peopleMediation:view:countByYearCaseRank")
	@RequestMapping(value="countByYearCaseRank")
	@ResponseBody
	public List<OaPeopleMediationAcceptRegisterCount> countByYearArea(OaPeopleMediationAcceptRegisterCount opmarc) {
		//获取相应数据
		List<OaPeopleMediationAcceptRegisterCount> list = oaPeopleMediationAcceptRegisterService.countByYearCaseRank(opmarc); 
		return list;
	}
	@RequestMapping(value="countByYearCaseRankDate")
	@ResponseBody
	public List<List<String>> countByYearCaseRankDate(OaPeopleMediationAcceptRegisterCount opmarc) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		//获取相应数据
		List<List<String>> list = oaPeopleMediationAcceptRegisterService.countByYearCaseRankDate(opmarc, areaList); 
		return list;
	}
}
