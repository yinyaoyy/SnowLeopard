/**
 * 
 */
package com.thinkgem.jeesite.tools;

import java.io.File;

/**
 * 批量替换文件名
 * @author 王鹏
 * @version 2018-06-10 18:09:46
 */
public class SimpleReplaceFileName {

	private static final String FILE_PATH = "D:\\temp\\LegalServicePerson";
	
	/**
	 * @author 王鹏
	 * @version 2018-06-10 18:09:46
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		System.out.println("苏勒德巴特尔15252319800407161xjpg.png".split("\\.")[0].replaceAll("[^0-9|X|x]", "").replaceAll("x", "X")+".jpg");
		replaceFileName();
//		printFileName();
		System.out.println("处理完成.");
	}

	/**
	 * 替换文件名
	 * @author 王鹏
	 * @version 2018-06-10 18:11:02
	 */
	public static void replaceFileName() {
		File filePath = new File(FILE_PATH);
		String fileName = null;//文件名
		for(File file : filePath.listFiles()) {
			fileName = file.getName();
			fileName = fileName.split("\\.")[0].replaceAll("[^0-9|X|x]", "").replaceAll("x", "X")+".jpg";
			file.renameTo(new File(FILE_PATH+File.separator+fileName));
			System.out.println(fileName);
		}
	}
	/**
	 * 查看文件名
	 * @author 王鹏
	 * @version 2018-06-10 18:11:02
	 */
	public static void printFileName() {
		File filePath = new File(FILE_PATH);
		for(File file : filePath.listFiles()) {
			System.out.println(file.getName());
		}
	}
}
