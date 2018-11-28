package com.thinkgem.jeesite.modules.sys.utils;
import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper; 
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType; 
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat; 
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType; 
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType; 
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
public class Chinese2pinyin { 
	public static String res(String name){
		String namePy = convertUpper(name);
		return namePy;
	}
	public static String convertUpper(String text){
        return convert(text, HanyuPinyinCaseType.UPPERCASE, false);
    }
	public static String convert(String text, HanyuPinyinCaseType caseType, boolean isCapitalize) {
        if(StringUtils.isBlank(text)){
            return "";
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        if(caseType != null) {
            format.setCaseType(caseType);
            isCapitalize = false;
        }
         
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = StringUtils.trimToEmpty(text).toCharArray();
        StringBuilder builder = new StringBuilder();
        try {
            for (char c: input) {
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if(isCapitalize) {
                        builder.append(StringUtils.capitalize(temp[0]));
                    }else {
                        builder.append(temp[0]);
                    }
                } else {
                    if(isCapitalize) {
                        builder.append(StringUtils.capitalize(Character.toString(c)));
                    }else {
                         builder.append(Character.toString(c));
                    }
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination ex) {
            ex.printStackTrace();
        }
 
        return builder.toString();
   }
	/*public static void main(String[] args) {
		System.out.println(Chinese2pinyin.convert("王琳",HanyuPinyinCaseType.LOWERCASE,false));
	}*/
}
