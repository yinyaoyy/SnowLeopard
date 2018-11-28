package com.thinkgem.jeesite.modules.cms.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author 如初
 * @Date 2018-06-24
 * 
 */
@SuppressWarnings("rawtypes")
public class Lexicon {
	private String ENCODING = "utf-8"; // 字符编码

	private Map sensitiveWordMap = null;
	public static int minMatchTYpe = 1; // 最小匹配规则
	public static int maxMatchType = 2; // 最大匹配规则

	/**
	 * @author 如初
	 * @Date 2018-06-24 初始化字库
	 */
	public Map initKeyWord() {
		try {
			// 读取敏感词库
			Set<String> keyWordSet = readSensitiveWordFile();
			// 将敏感词库加入到HashMap中
			addSensitiveWordToHashMap(keyWordSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	/**
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
	 * 中 = { isEnd = 0 国 = {<br>
	 * isEnd = 1 人 = {isEnd = 0 民 = {isEnd = 1} } 男 = { isEnd = 0 人 = { isEnd = 1 }
	 * } } } 五 = { isEnd = 0 星 = { isEnd = 0 红 = { isEnd = 0 旗 = { isEnd = 1 } } } }
	 * 此处是对字库构建模型，如果不使用此处的初始化方法，必须调用该方法处理， 其实内部就是将敏感字库的分拆为单个的字符放入map中设置词汇的结尾和开头
	 * 
	 * @author suzz
	 * @date
	 * @param keyWordSet
	 *            敏感词库
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap(keyWordSet.size()); // 初始化敏感词容器，减少扩容操作
		String key = null;
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		// 迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while (iterator.hasNext()) {
			key = iterator.next(); // 关键字
			nowMap = sensitiveWordMap;
			for (int i = 0; i < key.length(); i++) {
				char keyChar = key.charAt(i); // 转换成char型
				Object wordMap = nowMap.get(keyChar); // 获取

				if (wordMap != null) { // 如果存在该key，直接赋值
					nowMap = (Map) wordMap;
				} else { // 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String, String>();
					newWorMap.put("isEnd", "0"); // 不是最后一个
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				if (i == key.length() - 1) {
					nowMap.put("isEnd", "1"); // 最后一个
				}
			}
		}
	}

	/**
	 * 读取敏感词库中的内容，将内容添加到set集合中
	 * 
	 * @author suzz
	 * @date
	 * @return
	 * @version 1.0
	 * @throws Exception
	 */

	private Set<String> readSensitiveWordFile() throws Exception {
		Set<String> set = null;

		// File file = new File("E:\\SensitiveWord.txt"); //读取文件
		URL url = this.getClass().getClassLoader().getResource("SensitiveWord.properties");
		File file = new File(url.toURI());
		// InputStream
		// file=this.getClass().getResourceAsStream("SensitiveWord.properties");
		InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);
		BufferedReader bufferedReader = new BufferedReader(read);
		try {
			if (file.isFile() && file.exists()) { // 文件流是否存在
				set = new HashSet<String>();
				String txt = null;
				while ((txt = bufferedReader.readLine()) != null) { // 读取文件，将文件内容放入到set中
					set.add(txt);
				}
			} else { // 不存在抛出异常信息
				throw new Exception("敏感词库文件不存在");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			bufferedReader.close(); // 关闭文件流
			read.close(); // 关闭文件流
		}
		return set;
	}
	
}
