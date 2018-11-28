package com.thinkgem.jeesite.modules.platform.web;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.utils.SensitiveCache;
import com.thinkgem.jeesite.modules.language.entity.SysMunlLang;
import com.thinkgem.jeesite.modules.language.service.SysMunlLangService;
import com.thinkgem.jeesite.modules.platform.utils.PlatformUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;

@Controller
@RequestMapping(value = "${adminPath}/platform")
public class PlatformController extends BaseController {
	@Autowired
	private SysMunlLangService sysMunlLangService;

	@RequestMapping(value = {"list", ""})
	public String platformSetting(HttpServletRequest request, HttpServletResponse response, Model model) {
		String [] param=Global.getConfig("static_url").split("\\|");//静态图片路径
		double params= Math.random();
		model.addAttribute("params", params);
		model.addAttribute("imgList", param);
		model.addAttribute("languageList",DictUtils.getDictListByLanguage("act_langtype","CN"));
		return "modules/sys/platformSetting";
	}
	/**
	 * 根据key修改jeesite配置文件的值
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "version")
	public @ResponseBody String version( HttpServletRequest request, HttpServletResponse response, Model model) {
		String key=request.getParameter("key");
		String value=request.getParameter("value");
		String type=request.getParameter("type");
		String logMessage=request.getParameter("logMessage");
		boolean falg=false;
		if("1".equals(type)){//修改backgroundType第一个参数
			String [] param=Global.getConfig(key).split("\\|");
			if("canvas".equals(value)){
				value=value+"|true|"+param[2]+"|"+param[3];
			}else{
				value=value+"|false|"+param[2]+"|"+param[3];
			}
		}else if("2".equals(type)){//修改backgroundType第二个参数
			String [] param=Global.getConfig(key).split("\\|");
			value=param[0]+"|"+value+"|"+param[2]+"|"+param[3];
		}else if("3".equals(type)){//修改backgroundType第三个参数
			String [] param=Global.getConfig(key).split("\\|");
			value=param[0]+"|"+param[1]+"|"+value+"|"+param[3];
		}else if("4".equals(type)){//平台语言修改
		    if(!"CN".equals(value)){
				SysMunlLang lang=new SysMunlLang();
				lang.setOperationType("1");
				lang.setLangCode("CN");
				int countM=sysMunlLangService.getCountByLanguage(lang);
				lang.setLangCode(value);
				int countP=sysMunlLangService.getCountByLanguage(lang);
				if(countM>countP){
					falg=true;
				}
		    }
		}
		PlatformUtils.saveLastKey(key, value);
		LogUtils.saveLog(request, logMessage);
		if(falg){
			return "-1";
		}
		return "";
	}

	/**
	 * 平台logo修改
	 * @param request
	 */
	@RequestMapping(value = "logoUplod")
	public @ResponseBody double  logoUplod(HttpServletRequest request) {
		String param=request.getParameter("param");
		String fileName ="logo.png";
		String realPath ="/userfiles/platform/logo";
		FileUtils.GenerateImage(param, realPath, fileName);
		LogUtils.saveLog(request, "修改平台logo");
		double params= Math.random();
		return params;
	}
	/**
	 * 删除图片
	 * @param request
	 */
	@RequestMapping(value = "delImg")
	public void  delImg(HttpServletRequest request) {
		String param=request.getParameter("param");
		int start=param.indexOf("userfiles");
		String filePath=param.substring(start-1);
		//配置文件中删除
		String [] urlArr=Global.getConfig("static_url").split("\\|");
		String value="";
		String urlValue="";
		int count=0;
		if(urlArr.length>1){
			for (int i = 0; i < urlArr.length; i++) {
				urlValue=urlArr[i];
				if(!filePath.equals(urlValue)&&count==0){
					value+=urlValue;
					count++;
				}else if(!filePath.equals(urlValue)&&count>0){
					value+="|"+urlValue;
					count++;
				}
			}
		}
		PlatformUtils.saveLastKey("static_url", value);
		//文件夹中删除
		File file=new File(Global.getUserfilesBaseDir()+filePath);
		if(file.exists()){
			file.delete();
		}
		LogUtils.saveLog(request, "删除平台静态背景图片");
	}
	/**
	 * 背景图片上传
	 * @param request
	 */
	@RequestMapping(value = "upalodBackImg")
	public @ResponseBody String  upalodBackImg(HttpServletRequest request) {
		String param=request.getParameter("param");
		String fileName =UUID.randomUUID().toString()+".png";
		String realPath ="/userfiles/platform/back";
		//修改配置文件
		String value=Global.getConfig("static_url");
		if(value!=null&&!"".equals(value)){
			value=value+"|/userfiles/platform/back/"+fileName;
		}else{
			value="/userfiles/platform/back/"+fileName;
		}
		PlatformUtils.saveLastKey("static_url",value);
		//上传到文件夹
		FileUtils.GenerateImage(param, realPath, fileName);
		LogUtils.saveLog(request, "上传平台静态背景图片");
		return "/userfiles/platform/back/"+fileName;
	}
	
	   @RequestMapping(value = "Initialization")
       public String  Initialization(HttpServletRequest request,RedirectAttributes redirectAttributes) {
		SensitiveCache.remove();
		LogUtils.saveLog(request, "初始化敏感字库");
		addMessage(redirectAttributes,"初始化字库成功");
		return "redirect:" + adminPath + "/platform";
	}
}
