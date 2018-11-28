/**
 * ftp文件上传和下载
 * Maven配置
 * <dependency>
 *   <groupId>commons-net</groupId>
 *   <artifactId>commons-net</artifactId>
 *   <version>3.6</version>
 *   <classifier>ftp</classifier>
 * </dependency>
 */
package com.thinkgem.jeesite.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.thinkgem.jeesite.common.config.Global;

/**
 * ftp文件上传和下载
 * @author 王鹏
 * @version 2018-07-25 11:34:12
 */
public class FtpUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

	//ftp客户端配置
	private static final String IPADDR = Global.getConfig("ftp.ipaddr");//ip地址
	private static Integer PORT = Integer.parseInt(Global.getConfig("ftp.port"));//端口号
	private static final String USERNAME = Global.getConfig("ftp.username");//用户名
	private static final String PASSWORD = Global.getConfig("ftp.password");//密码
	private static final String PATH = Global.getConfig("ftp.path");//ftp根路径
	private static final String ENCODING = Global.getConfig("ftp.encoding");//ftp编码格式
	
	/**
	 * 初始化fpt客户端
	 * @author 王鹏
	 * @version 2018-07-27 15:12:46
	 * @return
	 */
	private static FTPClient getFtpClient(){//初始化客户端
		FTPClient ftp = null;
		try {
			ftp = new FTPClient();
			int reply;
			PORT = PORT==null?21:PORT;
			ftp.connect(IPADDR,21);
			ftp.login(USERNAME, PASSWORD);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			reply = ftp.getReplyCode();
			//设置连接模式
			ftp.enterLocalPassiveMode();
			//读取连接错误时断开连接
			if (!FTPReply.isPositiveCompletion(reply)) {  
				ftp.disconnect();
			}
			ftp.changeWorkingDirectory(PATH);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("初始化FTP客户端失败["+e.getMessage()+"]\n"+e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("初始化FTP客户端失败["+e.getMessage()+"]\n"+e);
		}
		return ftp;
	}
	
	/**
	 * 关闭ftp连接
	 */
	private static void closeFtp(FTPClient ftp){
		if (ftp!=null && ftp.isConnected()) {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				logger.debug("关闭FTP客户端失败["+e.getMessage()+"]\n"+e);
			}
		}
	}
	
	/**
	 * ftp上传文件
	 * @param file 本地文件或文件夹
	 * @param remotePath 远程目录
	 * @return 上传成功失败 上传目录时没有报错即为成功
	 */
	public static boolean upload(File file, String remotePath){
		boolean isSucc = false;//默认失败
		FTPClient ftp = null;
		FileInputStream input = null;//输入流
		try {//获取ftp客户端
			ftp = getFtpClient();
			//判断是否是目录
			if (file.isDirectory()) {
				String newRemotePath = remotePath+File.separator+file.getName();
				//新建一个目录文件夹，并进入此目录
				createDirecroty(ftp, newRemotePath);
				//循环所有文件
				String[] files = file.list();
				for(String fstr : files){
					//获取文件
					file = new File(file.getPath()+File.separator+fstr);
					if (file.isDirectory()) {//如果是目录则递归调用
						upload(file, remotePath+File.separator+file.getName());
						ftp.changeToParentDirectory();
					}else{//如果是文件则上传文件
						input=new FileInputStream(file);
						//处理文件的中文名，并上传文件
						ftp.storeFile(new String(file.getName().getBytes(ENCODING),"ISO-8859-1"), input);
						input.close();//关闭文件流
					}
				}
				isSucc = true;//只要没报错则为成功
			}else{//如果是文件则直接上传
				input = new FileInputStream(file);
				//进入相应目录(先新建在进入，以确保文件夹存在)
				//新建一个目录文件夹，并进入此目录
				createDirecroty(ftp, remotePath);
				//处理文件的中文名，并上传文件
				isSucc = ftp.storeFile(new String(file.getName().getBytes(ENCODING),"ISO-8859-1"), input);
				input.close();//关闭文件流
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("FTP上传文件到指定目录失败["+e.getMessage()+"]\n"+e);
		} finally {
			if(input != null) {
				try {//关闭输入流
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//关闭ftp客户端
			closeFtp(ftp);
		}
		return isSucc;
	}
	
	/**
	 * 下载链接配置
	 * @param localBaseDir 本地目录
	 * @param remoteFileName 远程文件名(从默认目录下载，路径必须正确)
	 * @throws Exception
	 * @return 是否下载成功
	 */
	public static boolean startDownFile(String localBaseDir, String remoteFileName){
		FTPClient ftp = null;
		try {//获取客户端
			ftp = getFtpClient();
			//设置编码
			ftp.setControlEncoding(ENCODING);
			return downloadFile(ftp, true, remoteFileName, localBaseDir, PATH);
		}finally {//关闭ftp客户端
			closeFtp(ftp);
		}		
	}

	/**
	 * 下载链接配置
	 * @param localBaseDir 本地目录
	 * @param remoteDir 远程目录名
	 * @throws Exception
	 * @return 是否下载成功
	 */
	public static boolean startDownDirectory(String localBaseDir, String remoteDir){
		FTPClient ftp = null;
		try {//获取客户端
			ftp = getFtpClient();
			//设置编码
			ftp.setControlEncoding(ENCODING);
			return downloadFile(ftp, false, remoteDir, localBaseDir, PATH);
		}finally {//关闭ftp客户端
			closeFtp(ftp);
		}		
	}
	
	/** 
	 * 下载FTP文件 
	 * 当你需要下载FTP文件的时候，调用此方法 
	 * 根据<b>获取的文件名，本地地址，远程地址</b>进行下载 
	 * * * 注意ftp客户端的创建和关闭
	 * 
	 * @param ftp ftp客户端
	 * @param isFile 是否是文件 true=文件;false=文件夹
	 * @param remoteFileName 服务器上的文件名 
	 * @param relativeLocalPath 本地目录
	 * @param relativeRemotePath 远程ftp目录
	 * @return 下载是否成功 ，下载目录时只要没有异常即为下载成功
	 */ 
	private static boolean downloadFile(FTPClient ftp, boolean isFile, String remoteFileName, String relativeLocalPath, String relativeRemotePath) { 
		OutputStream outputStream = null;
		boolean isSucc = false;//默认下载失败
		try {
			//处理路径分隔符
			String tempLocal = changeSeparator(remoteFileName);
			tempLocal = tempLocal.substring(tempLocal.lastIndexOf(File.separator));
			//创建本地文件
			File localFile= new File(relativeLocalPath + File.separator + tempLocal);
			if (isFile) {//下载文件
				File localPath = new File(localFile.getParent());
				if(!localPath.exists()) {//如果目录不存在则创建
					localPath.mkdirs();
				}
				outputStream = new FileOutputStream(localFile);
				//进入远程目录
				ftp.changeWorkingDirectory(relativeRemotePath);
				//处理文件名编码并下载文件
				isSucc = ftp.retrieveFile(new String(remoteFileName.getBytes(ENCODING),"ISO-8859-1"), outputStream);
				outputStream.flush();
				outputStream.close();
			}else {//如果是目录则下载目录中的全部文件
				if (!localFile.exists()) { 
					localFile.mkdirs();
				}
				//进入目录
				boolean changedir = ftp.changeWorkingDirectory(remoteFileName);
				if (changedir) {//进入成功则循环左右文件，并进行下载
					FTPFile[] files = null;
					//循环所有文件内容
					files = ftp.listFiles();
					for (int i = 0;i < files.length;i++) {
						//递归下载所有文件
						downloadFile(ftp, files[i].isFile(), files[i].getName(), localFile.getPath(), files[i].getName());
					}
				}
				if (changedir){//如果进入失败则返回上级目录
					ftp.changeToParentDirectory();
				}
				//如果没有异常则返回下载成功
				isSucc = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("FTP下载文件到指定目录失败["+e.getMessage()+"]\n"+e);
		}finally {
			try {//关闭输出流
				if (outputStream != null){
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}
		return isSucc;
	}
	
	/**
	 * 单独创建目录不做其他操作
	 * @author 王鹏
	 * @version 2018-08-16 15:40:36
	 * @param directory
	 * @return
	 */
	public static boolean createDirecroty(String directory) {
		FTPClient ftp = null;
		try {//获取客户端
			ftp = getFtpClient();
			//设置编码
			ftp.setControlEncoding(ENCODING);
			return createDirecroty(ftp, directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//关闭ftp客户端
			closeFtp(ftp);
		}
		return false;		
	}
	
	/**
	 * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param remote
	 * @return
	 * @throws IOException 
	 * @date 创建时间：2017年6月22日 上午11:51:33
	 */
	public static boolean createDirecroty(FTPClient ftp, String directory)
			throws IOException {
		boolean success = true;
		//将路径分隔符替换为系统分隔符(跨windows、linux)
		directory = changeSeparator(directory);
		// 如果远程目录不存在，则递归创建远程服务器目录
		if (!directory.equalsIgnoreCase(File.separator)) {
			int start = 0;
			int end = 0;
			if (directory.startsWith(File.separator)) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf(File.separator, start);
			String path = "";
			String paths = "";
			while (true) {
				String subDirectory = new String(directory.substring(start, end).getBytes(ENCODING), "iso-8859-1");
				path = path + File.separator + subDirectory;
				if(!changeWorkingDirectory(subDirectory, ftp)) {
					makeDirectory(subDirectory, ftp);
					changeWorkingDirectory(subDirectory, ftp);
				}
				paths = paths + File.separator + subDirectory;
				start = end + 1;
				end = directory.indexOf(File.separator, start);
				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}

	/**
	 * 将路径分隔符替换为系统分隔符(跨windows、linux)
	 * @author 王鹏
	 * @version 2018-08-17 10:06:08
	 * @param directory
	 * @return
	 */
	public static String changeSeparator(String directory) {
		//将diretory中的路径分隔符统一替换为系统分隔符
		directory = directory.replaceAll("/", File.separator+File.separator);
		directory = directory.replaceAll("\\\\\\\\", File.separator+File.separator);
		return directory;
	}
	/**
	 * 改变目录路径
	 * @param directory
	 * @param ftp
	 * @return 
	 * @date 创建时间：2017年6月22日 上午11:52:13
	 */
	public static boolean changeWorkingDirectory(String directory, FTPClient ftp) {
		boolean flag = true;
		try {
			flag = ftp.changeWorkingDirectory(directory);
			if (flag) {
				System.out.println("进入文件夹" + directory + " 成功！");
			} else {
				System.out.println("进入文件夹" + directory + " 失败！");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return flag;
	}
 
	/**
	 * 创建目录
	 * @param dir
	 * @param ftp
	 * @return 
	 * @date 创建时间：2017年6月22日 上午11:52:40
	 */
	public static boolean makeDirectory(String dir, FTPClient ftp) {
		boolean flag = true;
		try {
			flag = ftp.makeDirectory(dir);
			if (flag) {
				System.out.println("创建文件夹" + dir + " 成功！");
			} else {
				System.out.println("创建文件夹" + dir + " 失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
 
	/**
	 * 判断ftp服务器文件是否存在
	 * @param path
	 * @param ftp
	 * @return
	 * @throws IOException 
	 * @date 创建时间：2017年6月22日 上午11:52:52
	 */
	public static boolean existFile(String path, FTPClient ftp) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftp.listDirectories(path);
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * @author 王鹏
	 * @version 2018-07-25 11:34:13
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//测试上传下载文件
		File file = new File("D:/temp/律师事务所集合.txt");
		FtpUtil.upload(file, "/test/2018/08/");//把文件上传在ftp上
		FtpUtil.startDownFile("D:/temp/test/", "/test/2018/08/律师事务所集合.txt");//下载ftp文件测试
		System.out.println("ok");
		
		/*//测试上传下载文件夹
		File path = new File("D:/temp/test2");
		FtpUtil.upload(path, "");//把文件上传在ftp上
		FtpUtil.startDownDirectory("D:/temp/test2/test3", "/test2");//下载ftp文件测试
		System.out.println("ok");*/
		
		//测试创建多级目录
//		FtpUtil.createDirecroty("2018/08/16/");
	}

}
