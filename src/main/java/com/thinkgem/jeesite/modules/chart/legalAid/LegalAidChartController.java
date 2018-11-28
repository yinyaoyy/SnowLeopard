package com.thinkgem.jeesite.modules.chart.legalAid;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidCount;
import com.thinkgem.jeesite.modules.oa.service.act.OaLegalAidService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

@Controller
@RequestMapping(value = "${adminPath}/chart/legalAid")
public class LegalAidChartController extends BaseController {

	 //锡林郭勒盟  sys_area 中对应的id(为了只获取锡盟下属的地区)
    private static final String AREA_PARENT_ID = "5";
    
	@Autowired
	private OaLegalAidService oaLegalAidService;
	@Autowired
	private AreaService areaService;
	
//	@RequiresPermissions("chart:oaLegalAid:view:countYearArea")
	@RequestMapping(value="toCountYearArea")
	public String toCountYearArea(OaLegalAidCount oac, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID);
		/*//设置默认年度
		if(StringUtils.isBlank(oac.getYear())) {
			oac.setYear(DateUtils.getYear());
		}*/
		model.addAttribute("areaList", areaList);
		return "modules/chart/legalAid/countYearArea";
	}
//	@RequiresPermissions("chart:oaLegalAid:view:countYearArea")
	@RequestMapping(value="countYearArea")
	@ResponseBody
	public List<OaLegalAidCount> countYearArea(OaLegalAidCount oac) {
		//获取相应数据
		List<OaLegalAidCount> list = oaLegalAidService.countYearArea(oac); 
		return list;
	}
	
	@RequestMapping(value="countMonthArea")
	@ResponseBody
	public List<List<String>> countMonthrArea(OaLegalAidCount oac) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID);
		//获取相应数据
		oac.setForTable("true");//查询表格
		List<List<String>> list = oaLegalAidService.countMonthArea(oac, areaList); 
		return list;
	}

//	@RequiresPermissions("chart:oaLegalAid:view:countCaseType")
	@RequestMapping(value="toCountCaseType")
	public String toCountCaseType(OaLegalAidCount oac, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		model.addAttribute("areaList", areaList);
		//设置默认年度
		model.addAttribute("year", DateUtils.getYear());
		//设置案件类型
		model.addAttribute("caseTypes", JSON.toJSONString(DictUtils.getDictList("case_type")));
		return "modules/chart/legalAid/countCaseType";
	}
//	@RequiresPermissions("chart:oaLegalAid:view:countCaseType")
	@RequestMapping(value="countCaseType")
	@ResponseBody
	public List<OaLegalAidCount> countCaseType(OaLegalAidCount oac) {
		//获取相应数据
		List<OaLegalAidCount> list = oaLegalAidService.countCaseType(oac); 
		return list;
	}

//	@RequiresPermissions("chart:oaLegalAid:view:countLegalAidType")
	@RequestMapping(value = {"toCountLegalAidType", ""})
	public String toCountLegalAidType(OaLegalAidCount oac, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID, false);
		model.addAttribute("areaList", areaList);
		//设置默认年度
		model.addAttribute("year", DateUtils.getYear());
		return "modules/chart/legalAid/countLegalAidType";
	}
//	@RequiresPermissions("chart:oaLegalAid:view:countCaseType")
	@RequestMapping(value="countLegalAidType")
	@ResponseBody
	public List<OaLegalAidCount> countLegalAidType(OaLegalAidCount oac) {
		//获取相应数据
		List<OaLegalAidCount> list = oaLegalAidService.countLegalAidType(oac); 
		return list;
	}
}
