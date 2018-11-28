/**
 * 
 */
package com.thinkgem.jeesite.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * 去除字符串中的转义及html编码
 * @author 王鹏
 * @version 2018-06-29 11:33:11
 */
public class RemoveStringHtml {

	/**
	 * 正则规则: 转义字符、空格、制表符、换行
	 */
	private static final String REGEX_ESCAPE_RULE = "\\\\|\\s*|\t|\r|\n";
	
	
	/**
	 * @author 王鹏
	 * @version 2018-06-29 11:33:12
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = FileUtils.readFileToString("D:/temp/律师事务所集合.txt", "UTF-8");
		str = removeStrHtml(str);
		System.out.println(str);
	}

	/**
	 * 去除html标记
	 * @author 王鹏
	 * @version 2018-06-29 11:35:05
	 * @param htmlStr
	 * @return
	 */
	public static String removeStrHtml(String htmlStr) {
		String result = null;
		if(StringUtils.isNotBlank(htmlStr)) {
			Pattern p = Pattern.compile(REGEX_ESCAPE_RULE);
			Matcher m = p.matcher(htmlStr);
			result = m.replaceAll("");
			result = StringUtils.replaceHtml(result);
		}
		return result;
	}
}
