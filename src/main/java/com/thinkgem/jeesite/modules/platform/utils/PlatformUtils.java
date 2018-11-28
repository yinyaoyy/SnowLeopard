package com.thinkgem.jeesite.modules.platform.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.thinkgem.jeesite.common.config.Global;

public class PlatformUtils {
private static Properties properties= new Properties();
    
    /*properties文件名*/
    private static final String PROPERTIES_FILE_NAME="jeesite.properties";
    
    
    /**
     * 初始化properties，即载入数据
     */
    private static void initProperties(){
        try {
            InputStream ips = PlatformUtils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
            properties.load(ips);
            ips.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**将数据载入properties，
     * @return
     */
    public static String getPrimaryKey(String key){
        if(properties.isEmpty())//如果properties为空，则初始化一次。
            initProperties();
        return properties.getProperty(key); //properties返回的值为String
    }
    
    /**修改key的值，并保存
     * @param id
     */
    public static void saveLastKey(String key,String value){
        if(properties.isEmpty())
            initProperties();
        //修改值
        properties.setProperty(key,value);
        //保存文件
        try {
            URL fileUrl = PlatformUtils.class.getClassLoader().getResource(PROPERTIES_FILE_NAME);//得到文件路径
            FileOutputStream fos = new FileOutputStream(new File(fileUrl.toURI()));
            properties.store(fos, "the primary key of article table");
            fos.close();
            Global.updateValueFromMap(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
}
