/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.google.common.collect.Lists;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的年数
	 * @param date
	 * @return
	 */
	public static Long pastYear(Date date) {
		return date==null?null:pastDays(date)/365;
	}
	
	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	/**
	 * 根据身份证算出年龄
	 */
	public static int getAge(String idCard){
		if(StringUtils.isNotBlank(idCard)){
			String str="";
			if(idCard.length()==15){
				str += "19"+idCard.substring(6, 12);
			}else if(idCard.length()==18){
				str += idCard.substring(6, 14);
			}
		    
		    return getTime(str);
		}
		return -1;
	}
	public static Date getBirthday(String idCard){
		
	  String str = "0000-00-00";
	  Date da = DateUtils.parseDate(str);
		StringBuffer b = new StringBuffer();
		if(StringUtils.isNotBlank(idCard)){
			if(idCard.length()==15){
				b.append("19");
				b.append(idCard.substring(6, 8));
				b.append("-");
				b.append(idCard.subSequence(8, 10));
				b.append("-");
				b.append(idCard.substring(10, 13));
			}else if(idCard.length()==18){
				b.append(idCard.substring(6, 10));
				b.append("-");
				b.append(idCard.substring(10, 12));
				b.append("-");
				b.append(idCard.substring(12, 14));
			}
			try {
				Date d = DateUtils.parseDate(b.toString(), parsePatterns[0]);
				return d;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		     return da;
		
	}
	
	public static int getTime(String str){
		if(StringUtils.isNotBlank(str)){
			Date date = new Date();
			long time1 = date.getTime();
			try {
				Date dd = DateUtils.parseDate(str.trim(), "yyyyMMdd");
				long time2 = dd.getTime();
				double day = (time1-time2)/(1000*60*60*24);
				int age = (int) (day/365);
				return age;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public static int getSubtraction(Date date){
		if(date!=null){
			    long time1 = date.getTime();
				long time2 = new Date().getTime();
				double day = (time2-time1)/(1000*60*60*24);
				int time = (int) (day/365);
				return time;
		}
		return 0;
	}
	/**
	 * 获取两个日期之间的所有日子
	 * 1.传入年份返回格式为["yyyy","yyyy","yyyy",...]
	 * 2.传入年月返回格式为["yyyy-MM","yyyy-MM","yyyy-MM",...]
	 * 3.传入年月日返回格式为["yyyy-MM-dd","yyyy-MM-dd","yyyy-MM-dd",...]
	 * @author 王鹏
	 * @version 2018-06-25 19:32:09
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getAllDayBetweenDate(String beginDate, String endDate) {
		List<String> allDays = Lists.newArrayList();
		if(StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
			throw new BusinessException("开始日期或结束日期为空");
		}
		//识别日期格式是否正确
		String[] datePattern = {"\\d{4}", "\\d{4}-\\d{2}", "\\d{4}-\\d{2}-\\d{2}"};
		Calendar sCal = Calendar.getInstance();//开始日期
		Calendar eCal = Calendar.getInstance();//结束日期
		//用于记录中间日期的变量
		Calendar mCal = Calendar.getInstance();
		if(match(datePattern[0], beginDate) && match(datePattern[0], endDate)) {
			sCal.setTime(parseDate(beginDate+"-01-01"));//开始日期
			eCal.setTime(parseDate(endDate+"-01-01"));//结束日期
			eCal.add(Calendar.YEAR, 1);//包含结束年份
			mCal.setTime(parseDate(beginDate+"-01-01"));//开始日期
			while(mCal.before(eCal)) {//循环补充年
				allDays.add(formatDate(mCal.getTime(), "yyyy"));
				mCal.add(Calendar.YEAR, 1);
			}
		}
		else if(match(datePattern[1], beginDate) && match(datePattern[1], endDate)) {
			sCal.setTime(parseDate(beginDate+"-01"));//开始日期
			eCal.setTime(parseDate(endDate+"-01"));//结束日期
			eCal.add(Calendar.MONTH, 1);//包含结束月份
			mCal.setTime(parseDate(beginDate+"-01"));//开始日期
			while(mCal.before(eCal)) {//补充月
				allDays.add(formatDate(mCal.getTime(), "yyyy-MM"));
				mCal.add(Calendar.MONTH, 1);
			}
		}
		else if(match(datePattern[2], beginDate) && match(datePattern[2], endDate)) {
			sCal.setTime(parseDate(beginDate));//开始日期
			eCal.setTime(parseDate(endDate));//结束日期
			eCal.add(Calendar.DATE, 1);//包含结束日期
			mCal.setTime(parseDate(beginDate));//开始日期
			while(mCal.before(eCal)) {//补充日
				allDays.add(formatDate(mCal.getTime()));
				mCal.add(Calendar.DATE, 1);
			}
		}
		else {
			throw new BusinessException("请确保日期格式正确且一致");
		}
		return allDays;
	}
	/**
	 * 验证字符串是否符合格式
	 * @author 王鹏
	 * @version 2018-06-25 20:08:38
	 * @return
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);//格式
		Matcher matcher = pattern.matcher(str);//带验证字符串
		return matcher.matches();
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(getAge("370983199206272330"));
		System.out.println(DateUtils.getBirAgeSex("370983199206272330"));
	}
	private static void testGetAllDayBetweenDate() {
		List<String> dates = Lists.newArrayList();
		dates = getAllDayBetweenDate("2012", "2018");
		System.out.println("dates="+dates.toString());
		dates = getAllDayBetweenDate("2016-05", "2018-03");
		System.out.println("dates="+dates.toString());
		dates = getAllDayBetweenDate("2017-12-20", "2018-03-01");
		System.out.println("dates="+dates.toString());
	}
	
	/**
	 * 获取当前时间的字符串形式（例如；"201806291135"）
	 * @return 年月日时分
	 */
	public static String getDateToString(){
		Calendar c = Calendar.getInstance();
		return getYear()+getMonth()+getDay()+c.get(Calendar.HOUR_OF_DAY)+c.get(Calendar.MINUTE);
	}
	/**
	 * 根据身份证计算出身日期
	 * 
	 */
	/**
     * 通过身份证号码获取出生日期、性别、年龄
     * @author 黄涛
     * @param certificateNo
     * @return 返回的出生日期格式：1990-01-01   性别格式：2-女，1-男
     */
    public static Map<String, String> getBirAgeSex(String certificateNo) {
    	System.out.println(certificateNo);
        String birthday = "";
        String age = "";
        String sexCode = "";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag) return new HashMap<String, String>();
                flag = Character.isDigit(number[x]);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag) return new HashMap<String, String>();
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-"
                    + certificateNo.substring(8, 10) + "-"
                    + certificateNo.substring(10, 12);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3, certificateNo.length())) % 2 == 0 ? "2" : "1";
            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        } else {
            birthday = certificateNo.substring(6, 10) + "-"
                    + certificateNo.substring(10, 12) + "-"
                    + certificateNo.substring(12, 14);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4, certificateNo.length() - 1)) % 2 == 0 ? "2" : "1";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("birthday", birthday);
        map.put("age", age);
        map.put("sexCode", sexCode);
        return map;
    }
	/**
	 * 根据身份证的号码算出当前身份证持有者的年龄 18位身份证
	 * @author 黄涛
	 * @return
	 * @throws Exception
	 */
	public static int getCarAge(String birthday){
		String year = birthday.substring(0, 4);//得到年份
		String yue = birthday.substring(4, 6);//得到月份
		//String day = birthday.substring(6, 8);//
		Date date = new Date();// 得到当前的系统时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String fyear = format.format(date).substring(0, 4);// 当前年份
		String fyue = format.format(date).substring(5, 7);// 月份
		// String fday=format.format(date).substring(8,10);
		int age = 0;
		if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
			age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
		} else {// 当前用户还没过生
			age = Integer.parseInt(fyear) - Integer.parseInt(year);
		}
		return age;
	}
	/**
	 * 
	 * 根据身份证获取性别
	 */
	public static String getCarSex(String CardCode){
		String sex;
		if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
			sex = "2";
		} else {
			sex = "1";
		}
		return sex;
	}

	/**
	 * 根据日期格式返回mysql日期格式
	 * 不处理时间
	 * @author 王鹏
	 * @version 2018年7月16日14:28:29
	 * @return
	 */
	public static String getDatePattern(String date) {
		String datePattern = "";
		if(StringUtils.isNotBlank(date)) {
			if(DateUtils.match("\\d{4}", date)) {
				datePattern = "%Y";
			}
			else if(DateUtils.match("\\d{4}-\\d{2}", date)) {
				datePattern = "%Y-%m";
			}
			else if(DateUtils.match("\\d{4}-\\d{2}-\\d{2}", date)) {
				datePattern = "%Y-%m-%d";
			}
		}
		return datePattern;
	}
}
