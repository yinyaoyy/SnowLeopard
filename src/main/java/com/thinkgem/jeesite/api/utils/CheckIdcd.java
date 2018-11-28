package com.thinkgem.jeesite.api.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验身份证号
 *
 * @param papernum
 * @return
 */
public class CheckIdcd {
	
		
	
	@SuppressWarnings("unused")
	public static  boolean checkId(String papernum) {

		String Ai = "";

		if (null == papernum || papernum.trim().isEmpty()) {
			return false;
		}
		// 判断号码的长度 15位或18位
		if (papernum.length() != 15 && papernum.length() != 18) {
			return false;
		}
		// 18位身份证前17位位数字，如果是15位的身份证则所有号码都为数字
		if (papernum.length() == 18) {
			Ai = papernum.substring(0, 17);
		} else if (papernum.length() == 15) {
			Ai = papernum.substring(0, 6) + "19" + papernum.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			return false;
		}
		// 判断出生年月是否有效
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 日期
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			return false;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {

				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {

			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {

			return false;
		}

		// 判断第18位校验码是否正确
		if (isVarifyCode(Ai, papernum) == false) {
			return false;
		}

		return true;
	}
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern.compile(
				"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isVarifyCode(String Ai, String IDStr) {
		String[] VarifyCode = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			sum = sum + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
		}
		int modValue = sum % 11;
		String strVerifyCode = VarifyCode[modValue];
		Ai = Ai + strVerifyCode;
		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				return false;
			}
		}
		return true;
	}
	private static boolean isNumeric(String strnum) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(strnum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}
	public static void main(String[] args) {
	}
}
