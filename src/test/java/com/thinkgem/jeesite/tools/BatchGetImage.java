/**
 * 
 */
package com.thinkgem.jeesite.tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.HttpUtil;

/**
 * 使用get方式批量获取图片地址下载到指定目录
 * @author 王鹏
 * @version 2018-07-02 20:12:23
 */
public class BatchGetImage {

	/**
	 * 图片统一域名信息
	 */
	private static final String REMOTE_URL = "http://59.196.11.40/uploadup/";
	/**
	 * 图片地址
	 * 保存名称=图片链接
	 */
	private static final String IMAGE_URL = "D:/temp/image/idt_persons_url.txt";
	/**
	 * 预备存放路径
	 */
	private static final String SAVE_PATH = "D:\\temp\\image\\idt_persons\\";
	
	public static void main(String[] args) {
		saveImage();
	}
	
	public static void saveImage() {
		//获取人员图片信息
		Map<String, String> map = getRealImageUrl();
		System.out.println(map.toString());
		for (String key : map.keySet()) {
			try {
				InputStream is = HttpUtil.sendRequestReturnStream(REMOTE_URL, map.get(key),
						"get", null, null);
				if(is == null) {
					System.out.println("error:文件保存失败，文件不存在[key="+key+"][jpg="+map.get(key)+"]");
					continue;
				}
				FileOutputStream fos = new FileOutputStream(SAVE_PATH+key+".jpg");
				byte[] b = new byte[1024];
				int n;
				while ((n = is.read(b)) != -1)
				{
					fos.write(b, 0, n);
				}
				fos.close();
				System.out.println("文件保存完毕[key="+key+"]");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("error:文件保存失败，文件不存在[key="+key+"][jpg="+map.get(key)+"]");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取真实完整的图片路径
	 * @author 王鹏
	 * @version 2018-07-02 20:22:43
	 * @return
	 */
	public static Map<String, String> getRealImageUrl(){
		Map<String, String> map = Maps.newHashMap();
		//读取所有文件内容
		String str = FileUtils.readFileToString(IMAGE_URL, "utf-8");
		//按照换行拆分为数组
		String[] arr = str.split("\r\n");
		//将数组放入map中
		String[] imgArr = null;
		for (int i = 0; i < arr.length; i++) {
			imgArr = arr[i].split("=");
			map.put(imgArr[0], imgArr[1]);
		}
		return map;
	}
}
