package com.thinkgem.jeesite.modules.cms.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.util.SSCellRange;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

public class FormatStyle {
    
    //匹配数组中定义的
	public static String  reaplance(String str) {
		String [] arr= {"position: absolute;","position: fixed;","float: right;","float: left;"};
		for(String s:arr){
            if(str.contains(s)){ 
            	str=str.replace(s, "");
            	}	
		}
		
				return str;
	}
	//匹配正则类型的html标签width属性
	public static String reaplance1(String str) {
		/*String gg= "width:\\s*\\d*px;";
		 Pattern p = Pattern.compile(gg);
	       // get a matcher object
	       Matcher m = p.matcher(str); 
	       str = m.replaceAll("");*/
		//将图片的width替换为特殊字符
		//str = str.replaceAll("style=\"width:","style=\"ljl:");
		String re="(width)(:)( )(\\d+)(.)(\\d+)(px)(;)";	
	    Pattern p = Pattern.compile(re,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    Matcher m = p.matcher(str);
	    str = m.replaceAll("width: 100%;");
	    //替换回图片的原width
	    //str = str.replaceAll("style=\"ljl:","style=\"width:");
		return str;
	}
	 public static void main(String[] args) {
		 String str="<p>\r\n" + 
		    		"	<span style=\"font-family:宋体;\"><span style=\"color: rgb(0, 0, 0); font-size: 12px; line-height: normal; white-space: normal;\">&nbsp; &nbsp;</span><span style=\"margin: 0px; padding: 0px; border: 0px; outline: 0px; color: rgb(0, 0, 0); line-height: normal; white-space: normal; text-align: justify; text-indent: 28px; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;\">《人民调解委员会组织条例》对调解工作应当遵守的基本原则作了具体规定：</span></span></p>"
		            + "<p class=\"MsoNormal\">\r\n" + 
		            "	<span style=\"font-family:宋体;\">&nbsp;&nbsp; ①依据法律、法规、规章和政策进行调解，法律、法规、规章和政策没有明确定的，依据社会公德进行调解；</span></p>\r\n" + 
		            "<p class=\"MsoNormal\">";
		 
		 String hh="<p class=\"p0\" style=\"width:  2018px; margin: 0px 0px 24px; padding: 0px; border: 0px; width: 948.005px; white-space: normal; widows: 1; font-size: 12px; line-height: 24px; text-indent: 31.5pt; float: left; font-family: 宋体;\">\r\n" + 
		 		"	<span style=\"margin: 0px; padding: 0px; font-size: 16pt; border: 0px; font-family: 仿宋_GB2312;\">为进一步提高新任科级干部依法决策、依法行政、依法管理的能力，近日、苏尼特左旗对全旗</span><span style=\"margin: 0px; padding: 0px; font-size: 16pt; border: 0px; font-family: 仿宋_GB2312;\">74</span><span style=\"margin: 0px; padding: 0px; font-size: 16pt; border: 0px; font-family: 仿宋_GB2312;\">名非人大任命科级干部（含非领导职务）进行了任前法律知识考试。考试的目的，是促进大家对法律知识的学习；通过学习，提高领导干部的法律意识和法治观念；通过运用法律，提高公职人员实际解决问题的本领，真正做到依法用权、谨慎用权。</span></p>\r\n" + 
		 		"<p class=\"p0\" style=\"margin: 0px 0px 24px; padding: 0px; border: 0px; width: 948.005px; white-space: normal; widows: 1; font-size: 12px; line-height: 24px; text-indent: 28pt; float: left; font-family: 宋体;\">\r\n" + 
		 		"	<span style=\"margin: 0px; padding: 0px; font-size: 16pt; border: 0px; font-family: 仿宋_GB2312;\">为组织好本次法律知识考试，旗委组织部、旗司法局精心安排，专人负责，并制定了具体方案。参加本次考试的</span><span style=\"margin: 0px; padding: 0px; font-size: 16pt; border: 0px; font-family: 仿宋_GB2312;\">74</span><span style=\"margin: 0px; padding: 0px; font-size: 16pt; border: 0px; font-family: 仿宋_GB2312;\">名非人大任命科级干部都能独立思考，认真答卷。本次考试的主要内容为《宪法》、《治安管理处罚法》《公务员法》等基本法律知识以及十八届四中全会通过的《中共中央关于全面推进依法治国若干重大问题的决定》和群众路线教育实践活动等相关内容，考题包括单选、多选、判断、简答、论述等形式，全方面考察领导干部的法律知识。</span></p>\r\n" + 
		 		"<p class=\"p0\" style=\"margin: 0px 0px 24px; padding: 0px; border: 0px; width: 948.005px; white-space: normal; widows: 1; font-size: 12px; line-height: 24px; text-indent: 28pt; float: left; font-family: 宋体;\">\r\n" + 
		 		"	<span style=\"margin: 0px; padding: 0px; font-size: 16pt; border: 0px; font-family: 仿宋_GB2312;\">举办干部任前法律知识考试，是学习贯彻落实十八届四中全会精神的具体措施，是实施依法治国基本方略、推进依法治旗的重要保证，是提高领导干部法律素质和依法执政能力的重要途径。</span></p>";
		/* String ff;
		 if(str.contains("font")) {
				 ff=str.replaceAll("[font]","");
				 System.out.println(ff);
			};*/
		 
		 FormatStyle formatStyle	= new FormatStyle();
		 hh= formatStyle.reaplance1(hh);
		System.out.println(hh);
	        
}
}