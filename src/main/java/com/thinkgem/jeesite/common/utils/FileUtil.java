package com.thinkgem.jeesite.common.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 文件上传下载工具类
 */
public class FileUtil {

	/**
	 * 上传文件到本地服务器并返回路径
	 * @param uploadFile
	 * @param request
	 * @param map 
	 * @param filename 
	 * @throws IOException
	 */
	public static String uploadImgFileReturnPath(MultipartFile uploadFile,String fineName) throws IOException {
		//String oldName = uploadFile.getOriginalFilename();
		//生成新文件名
		String userId=UserUtils.getUser().getId();
		//新文件名
		//oldName =  
		//创建目录
		String filePath="/userfiles/"+userId+"/images/";
		//String realPath = System.getProperty("catalina.home")+"/webapps"+filePath;
		String realPath =	Global.getUserfilesBaseDir()+filePath;
		File file =new File(realPath);
		if(!file.exists()){
			file.mkdirs();
		}
		//最终的文件路径
		String finalPath=realPath+fineName;
		//文件上传
		InputStream inputStream = uploadFile.getInputStream();
		byte[] buffer=new byte[1024];
		int len=-1;
		FileOutputStream fos=new FileOutputStream(new File(finalPath));
		while((len=inputStream.read(buffer))!=-1){
			fos.write(buffer, 0, len);
			fos.flush();
		}
		inputStream.close();
		fos.close();
		return filePath+fineName;
	}
	/**
	 * 上传文件到本地服务器并返回路径
	 * @param uploadFile
	 * @param request
	 * @param map 
	 * @param filename 
	 * @throws IOException
	 */
	public static String uploadFileFileReturnPath(MultipartFile uploadFile,String fineName) throws IOException {
		//String oldName = uploadFile.getOriginalFilename();
		//生成新文件名
		String userId=UserUtils.getUser().getId();
		//新文件名
		//oldName =  
		//创建目录
		String filePath="/userfiles/"+userId+"/file/";
		//String realPath = System.getProperty("catalina.home")+"/webapps"+filePath;
		String realPath =	Global.getUserfilesBaseDir()+filePath;
		File file =new File(realPath);
		if(!file.exists()){
			file.mkdirs();
		}
		//最终的文件路径
		String finalPath=realPath+fineName;
		//文件上传
		InputStream inputStream = uploadFile.getInputStream();
		byte[] buffer=new byte[1024];
		int len=-1;
		FileOutputStream fos=new FileOutputStream(new File(finalPath));
		while((len=inputStream.read(buffer))!=-1){
			fos.write(buffer, 0, len);
			fos.flush();
		}
		inputStream.close();
		fos.close();
		return filePath+fineName;
	}
	
	/**
	 * 上传文本时响应文本大小
	 * @param url
	 * @return
	 */
	public static long getFileLength(String url){
		String realPath = Global.getUserfilesBaseDir()+url;
		File file =new File(realPath);
		long f=0;
		try{
		  if(file.exists() && file.isFile()){
			  f = file.length();
		  }
		}catch(Exception e){
		    e.printStackTrace();
		}
		return  f; 
	}
}
