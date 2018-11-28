/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.thinkgem.jeesite.common.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import org.apache.commons.lang3.Validate;

import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.Exceptions;

/**
 * 支持SHA-1/MD5消息摘要的工具类.
 * 
 * 返回ByteSource，可进一步被编码为Hex, Base64或UrlSafeBase64
 * 
 * @author calvin
 */
public class Digests {

	private static final String SHA1 = "SHA-1";
	private static final String MD5 = "MD5";

	private static SecureRandom random = new SecureRandom();

	/**
	 * 对输入字符串进行md5散列.
	 */
	public static byte[] md5(byte[] input) {
		return digest(input, MD5, null, 1);
	}
	public static byte[] md5(byte[] input, int iterations) {
		return digest(input, MD5, null, iterations);
	}
	
	/**
	 * 对输入字符串进行sha1散列.
	 */
	public static byte[] sha1(byte[] input) {
		return digest(input, SHA1, null, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt) {
		return digest(input, SHA1, salt, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
		return digest(input, SHA1, salt, iterations);
	}

	/**
	 * 对字符串进行散列, 支持md5与sha1算法.
	 */
	private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				digest.update(salt);
			}

			byte[] result = digest.digest(input);

			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		} catch (GeneralSecurityException e) {
			throw Exceptions.unchecked(e);
		}
	}
	 public  static  String getMD5(String message){  
         String md5Result="";  
         try{  
             //1.创建一个提供信息摘要算法的对象，初始化为MD5算法对象  
             MessageDigest md=MessageDigest.getInstance("MD5");  
             //2.将消息变为byte数组  
             byte[] input=message.getBytes();  
             //3.计算后获得字节数组，128位长度的MD5加密  
             byte[] buff=md.digest(input);  
             //4.把数组每一个字节（一个字节占8位）换成16进制的md5字符串  
            md5Result=bytesHex(buff);  
         }catch (Exception e){  
             e.printStackTrace();  
         }  
         return md5Result;  
    }  
	    public static String bytesHex(byte[]bytes){  
            StringBuffer md5Result =new StringBuffer();  
            //把数组每一字节换成换成16进制连成md5字符串  
            int digital;  
            for (int i=0;i<bytes.length;i++){  
                digital=bytes[i];  
                if (digital<0){  
                    digital+=256;  
                }  
                if (digital<16){  
                    md5Result.append("0");  
                }  
                md5Result.append(Integer.toHexString(digital));  
            }  
          return md5Result.toString();  
        }  
	/**
	 * 生成随机的Byte[]作为salt.
	 * 
	 * @param numBytes byte数组的大小
	 */
	public static byte[] generateSalt(int numBytes) {
		Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}

	/**
	 * 对文件进行md5散列.
	 */
	public static byte[] md5(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	/**
	 * 对文件进行sha1散列.
	 */
	public static byte[] sha1(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	private static byte[] digest(InputStream input, String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int bufferLength = 8 * 1024;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}

			return messageDigest.digest();
		} catch (GeneralSecurityException e) {
			throw Exceptions.unchecked(e);
		}
	}
	
}
