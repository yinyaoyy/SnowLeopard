/**
 * 
 */
package com.thinkgem.jeesite.modules.chart.article;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.thinkgem.jeesite.modules.cms.entity.ArticleCount;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.oa.entity.act.OaLegalAidCount;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.AreaService;

/**
 * 各旗县普法宣传文章统计图
 * @author 王鹏
 * @version 2018-07-12 10:50:38
 */
@Controller
@RequestMapping(value = "${adminPath}/chart/article")
public class ArticleChartController {

	//锡林郭勒盟  sys_area 中对应的id(为了只获取锡盟下属的地区)
	private static final String AREA_PARENT_ID = "5";
   
	@Autowired
	private ArticleService articleService;
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="toCountArticle")
	public String toCountYearArea(OaLegalAidCount oac, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取地区列表
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID);
		model.addAttribute("areaList", areaList);
		return "modules/chart/article/countArticle";
	}
	
	/**
	 * 各旗县普法宣传文章统计图
	 * @author 王鹏
	 * @version 2018-07-12 15:59:20
	 * @param ac
	 * @return
	 */
	@RequestMapping(value="countArticle")
	@ResponseBody
	public Map<String, List<String>> countArticle(ArticleCount ac) {
		return articleService.countArticle(ac);
	}

	/**
	 * 各旗县普法宣传文章统计图
	 * @author 王鹏
	 * @version 2018-07-12 15:59:20
	 * @param ac
	 * @return
	 */
	@RequestMapping(value="countArticleForTable")
	@ResponseBody
	public List<List<String>> countArticleForTable(ArticleCount ac) {
		List<Area> areaList = areaService.findByParent(AREA_PARENT_ID);
		return articleService.countArticleForTable(ac, areaList);
	}
}
