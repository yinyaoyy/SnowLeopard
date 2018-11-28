package com.thinkgem.jeesite.common.JGpush;

import java.util.List;
import java.util.Map;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;

public class JGpushUtils {
	private static String appKey = Global.getConfig("appKey");
	private static String masterSecret = Global.getConfig("masterSecret");
	
	public static  JPushClient  newJpush(){
		JPushClient jPushClient = new JPushClient(masterSecret, appKey);
		return jPushClient;
	}
	/**
	 * 全部信息的推送，下载app的用户都可以收到通知
	 * 
	 */
	public static PushPayload getAllPushPayLoad(String title,String content){
		PushPayload payload = PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.all())
				.setNotification(Notification.android(title,content, null)).build();
		return payload;
	}
	
	/* public static PushPayload buildPushObject_all_alias_alert(String alias,String ALERT,String title) {
	        return PushPayload.newBuilder()
	                .setPlatform(Platform.all())
	                .setAudience(Audience.alias(alias))
	                .setNotification(Notification.newBuilder()
	                        //指定当前推送的android通知
	                        .addPlatformNotification(AndroidNotification.newBuilder()
	                                .setAlert(ALERT)
	                                .setTitle(title)
	                                .build())
	                        .build()
	                )
	                .setOptions(Options.newBuilder().setTimeToLive(864000).build())
	                .build();
	    }
	 */
	 public static PushPayload buildPushObject_all_alias_alert(String alias,Object alert) {
	        return PushPayload.newBuilder()
	                .setPlatform(Platform.all())
	                .setAudience(Audience.registrationId(alias))
	                .setNotification(Notification.alert(alert)).setOptions(Options.newBuilder().setTimeToLive(864000).build())
	                .build();
	    }
	 
	/**
	 * 给所有的安卓和ios推送通知
	 * @param notification_title  标题,不能为null，可以不传，数据库默认为空字符
	 * @param msg_content  推送通知内容,不能为null
	 * @param alias   别名，以userId作为别名推送
	 * @return
	 */
	 public static PushPayload buildPushObject_android_and_ios(String notification_title, String msg_content,String alias) {
	        return PushPayload.newBuilder()
	                .setPlatform(Platform.android_ios())
	                .setAudience(Audience.alias(alias))
	                .setNotification(Notification.newBuilder()
	                        .setAlert(msg_content)
	                        .addPlatformNotification(AndroidNotification.newBuilder()
	                                .setAlert(msg_content)
	                                .setTitle(notification_title)
	                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                                .build()
	                        )
	                        .addPlatformNotification(IosNotification.newBuilder()
	                                //传一个IosAlert对象，指定apns title、title、subtitle等
	                                .setAlert(msg_content)
	                                //直接传alert
	                                //此项是指定此推送的badge自动加1
	                                .incrBadge(1)
	                                /*//此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
	                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
	                                .setSound("sound.caf")
	                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                                .addExtra("iosNotification extras key",extrasparam)*/
	                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
	                                // .setContentAvailable(true)
	                                .build()
	                        )
	                        .build()
	                )
	               /* .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .addExtra("from", "JPush")
                        .build())*/
	               /* //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
	                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
	                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
	                .setMessage(Message.newBuilder()
	                        .setMsgContent(msg_content)
	                        .setTitle(msg_title)
	                        .addExtra("message extras key",extrasparam)
	                        .build())*/
	                .setOptions(Options.newBuilder()
	                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
	                        .setApnsProduction(false)
	                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
	                        .setSendno(1)
	                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
	                        .setTimeToLive(86400*10)
	                        .build()
	                )
	                .build();
	    }
	 /**
	  * 给所有的安卓和ios推送消息
	 * @param notification_title  标题，可为null，如果为null则标题显示app名字
	 * @param msg_content  推送通知内容
	 * @param alias   别名，以userId作为别名推送.此参数为list集合，别名推送一次最多1000条。
	  * @return
	  */
	 public static PushPayload buildPushObject_android_and_ios(String notification_title, String msg_content,List<String> alias) {
	        return PushPayload.newBuilder()
	                .setPlatform(Platform.android_ios())
	                .setAudience(Audience.alias(alias))
	                .setNotification(Notification.newBuilder()
	                        .setAlert(msg_content)
	                        .addPlatformNotification(AndroidNotification.newBuilder()
	                                .setAlert(msg_content)
	                                .setTitle(notification_title)
	                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                                .build()
	                        )
	                         
	                        .addPlatformNotification(IosNotification.newBuilder()
	                                //传一个IosAlert对象，指定apns title、title、subtitle等
	                                .setAlert(msg_content)
	                                //直接传alert
	                                //此项是指定此推送的badge自动加1
	                                .incrBadge(1)
	                                /*//此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
	                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
	                                .setSound("sound.caf")
	                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                                .addExtra("iosNotification extras key",extrasparam)*/
	                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
	                                // .setContentAvailable(true)
	                                .build()
	                        )
	                        .build()
	                )
	                .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .addExtra("from", "JPush")
                        .build())
	               /* //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
	                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
	                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
	                .setMessage(Message.newBuilder()
	                        .setMsgContent(msg_content)
	                        .setTitle(msg_title)
	                        .addExtra("message extras key",extrasparam)
	                        .build())*/
	                .setOptions(Options.newBuilder()
	                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
	                        .setApnsProduction(false)
	                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
	                        .setSendno(1)
	                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
	                        .setTimeToLive(86400*10)
	                        .build()
	                )
	                .build();
	    }
	 
	 /**
	  * 安卓手机的推送
	  * @param notification_title
	  * @param msg_title
	  * @param msg_content
	  * @param extrasparam
	  * @return
	  */
	 
	 private static PushPayload buildPushObject_android_all_alertWithTitle(String notification_title, String msg_title, String msg_content) {
	        System.out.println("----------buildPushObject_android_registrationId_alertWithTitle");
	        return PushPayload.newBuilder()
	                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
	                .setPlatform(Platform.android())
	                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
	                .setAudience(Audience.all())
	                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
	                .setNotification(Notification.newBuilder()
	                        //指定当前推送的android通知
	                        .addPlatformNotification(AndroidNotification.newBuilder()
	                                .setAlert(notification_title)
	                                .setTitle(notification_title)
	                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                                /*.addExtra("androidNotification extras key",extrasparam)*/
	                                .build())
	                        .build()
	                )
	                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
	                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
	                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
	               /* .setMessage(Message.newBuilder()
	                        .setMsgContent(msg_content)
	                        .setTitle(msg_title)
	                        .addExtra("message extras key",extrasparam)
	                        .build())*/
	 
	                .setOptions(Options.newBuilder()
	                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
	                        .setApnsProduction(false)
	                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
	                        .setSendno(1)
	                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
	                        .setTimeToLive(86400*10)
	                        .build())
	                .build();
	    }
	 
	 /**
	  * ios的信息推送
	  * @param notification_title
	  * @param msg_title
	  * @param msg_content
	  * @param extrasparam
	  * @return
	  */
	 
	 private static PushPayload buildPushObject_ios_all_alertWithTitle( String notification_title, String msg_title, String msg_content) {
	        System.out.println("----------buildPushObject_ios_registrationId_alertWithTitle");
	        return PushPayload.newBuilder()
	                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
	                .setPlatform(Platform.ios())
	                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
	                .setAudience(Audience.all())
	                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
	                .setNotification(Notification.newBuilder()
	                        //指定当前推送的android通知
	                        .addPlatformNotification(IosNotification.newBuilder()
	                                //传一个IosAlert对象，指定apns title、title、subtitle等
	                                .setAlert(notification_title)
	                                //直接传alert
	                                //此项是指定此推送的badge自动加1
	                                .incrBadge(1)
	                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
	                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
	                                .setSound("sound.caf")
	                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                                /*.addExtra("iosNotification extras key",extrasparam)*/
	                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
	                               // .setContentAvailable(true)
	 
	                                .build())
	                        .build()
	                )
	                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
	                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
	                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
	               /* .setMessage(Message.newBuilder()
	                        .setMsgContent(msg_content)
	                        .setTitle(msg_title)
	                        .addExtra("message extras key",extrasparam)
	                        .build())*/
	 
	                .setOptions(Options.newBuilder()
	                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
	                        .setApnsProduction(false)
	                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
	                        .setSendno(1)
	                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
	                        .setTimeToLive(86400)
	                        .build())
	                .build();
	    //}
	 
//	    public static void main(String[] args){
//	        if(JpushClientUtil.sendToAllIos("testIos","testIos","this is a ios Dev test","")==1){
//	            System.out.println("success");
//	        }
//	    }
	}
	 
	 

     /** 推送通知:根据alias同时推送给多个用户
     * @param aliases
     * @param notification_title
     * @param msg_title
     * @param extrasparam
     * @return
     */
    public static PushPayload buildPushObject_all_aliases_alertWithTitle(List<String> aliases,String notification_title, String msg_title, Map<String,String> extrasparam){
        String iosAlert = notification_title+":"+msg_title; 
        //需要最新sdk版本
//        IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();
        //IosAlert.newBuilder().setTitleAndBody(notification_title, null, msg_title).setActionLocKey("PLAY").build()
        return PushPayload.newBuilder()
                //推送平台
                .setPlatform(Platform.all())
                //推送目标：all、tag、tag_and、tag_not、alias、registration_id等
                .setAudience(Audience.alias(aliases))
                //通知
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()//指定不同平台的推送内容
                                .setTitle(notification_title)//标题
                                .setAlert(msg_title)//内容
                                //透传，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                //另一种方式： addExtra(key,value)
                                .addExtras(extrasparam)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(iosAlert)//传一个IosAlert对象，指定apns title、title、subtitle等
                                .incrBadge(1)//此项是指定此推送的badge自动加1
                                //.setSound("sound.caf")//设置声音
                                //透传，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                //另一种方式： addExtra(key,value)
                                .addExtras(extrasparam)
                                .setContentAvailable(true)//是否可以在锁屏状态下接收
                                .build())    
                    .build())
                //可选参数
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)//APNs是否为生产环境，false为开发环境
                        .setSendno(1)//推送编号
                        .setTimeToLive(86400)//指定本推送的离线保存时长(单位：秒)，如果不传此字段则默认保存一天，最多指定保留十天
                        .build())
                .build();
    }
    
    
    
    /**
     * 推送通知和自定义消息：根据alias推给个人
     * @param alias
     * @param notification_title
     * @param msg_title
     * @param msg_content
     * @param extrasparam
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alertAndmessage(String alias,String notification_title,String msg_title,String msg_content,Map<String, String> extrasparam ){
        String iosAlert = notification_title+":"+msg_title;
        //IosAlert.newBuilder().setTitleAndBody(notification_title, null, msg_title).setActionLocKey("PLAY").build()
        
        return PushPayload.newBuilder()
                //推送平台
                .setPlatform(Platform.all())
                //推送目标：all、tag、tag_and、tag_not、alias、registration_id等
                .setAudience(Audience.alias(alias))
                //通知
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()//指定不同平台的推送内容
                                .setTitle(notification_title)//标题
                                .setAlert(msg_title)//内容
                                //透传，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                //另一种方式： addExtra(key,value)
                                .addExtras(extrasparam)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(iosAlert)//传一个IosAlert对象，指定apns title、body、subtitle等
                                .incrBadge(1)//此项是指定此推送的badge自动加1
                                //.setSound("sound.caf")//设置声音
                                //透传，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                //另一种方式： addExtra(key,value)
                                .addExtras(extrasparam)
                                //.setContentAvailable(true)//是否可以在锁屏状态下接收
                                .build())    
                    .build())
                //自定义消息
                /*.setMessage(Message.newBuilder()
                        .setTitle(msg_title)//消息标题
                        .setMsgContent(msg_content)//消息内容本身
                        //.setContentType("json")//消息内容类型
                        .addExtras(extrasparam)//json格式的可选参数
                        .build())*/
                //可选参数
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)//APNs是否为生产环境，false为开发环境
                        .setSendno(1)//推送编号
                        .setTimeToLive(86400)//指定本推送的离线保存时长(单位：秒)，如果不传此字段则默认保存一天，最多指定保留十天
                        .build())
                .build();
    }
	/**
	 * 将一个list分成n个list,别名推送每次最多1000条，如果数据量大可以分为几个list,分批推送
	 * @param source 传入的list集合
	 * @return
	 */
	public static  List<List<String>> averageAssign(List<String> source){
		List<List<String>> result= Lists.newArrayList();
		//别名推送最多是每次推送1000条，所以如果大于1000条，则进行分组。
		if(source.size()>990){
			int n = (source.size()/990)+1;
			for (int i=0 ; i < n ; i++ ){
		            int number=source.size()/n;//每组的个数
		            int remainder=source.size()%n;//余数
		            int offset=0;//偏移量
                List<String> pp = null;
                if (remainder > 0){
                    pp=source.subList(i*number+offset,(i+1)*number+offset+1);
                    remainder--;
                    offset++;
                }else {
                    pp=source.subList(i*number+offset, (i+1)*number+offset);
                }
                result.add(pp);
            }
		}else{
			result.add(source);
		}
		       return result;
	}

	public static void main(String[] args) {
		JPushClient jPushClient = JGpushUtils.newJpush();
		try {
			//PushResult result = jPushClient.sendPush(JGpushUtils.buildPushObject_all_alias_alert("c5372d09e74143e0affbf9c663a49d97", "这是alter", "这是state"));
			//PushResult result1 = jPushClient.sendPush(JGpushUtils.buildPushObject_all_alias_alert("c5372d09e74143e0affbf9c663a49d97","这是coutent","这是传入的title"));
			PushResult result2 = jPushClient.sendPush(JGpushUtils.buildPushObject_android_and_ios(null, "这是coutent", "c5372d09e74143e0affbf9c663a49d97"));

			//PushResult result = jPushClient.sendPush(JGpushUtils.getAllPushPayLoad("这是title主题", "这是发布的内容"));
			//PushResult result = jPushClient.sendPush(JGpushUtils.buildPushObject_all_alias_alert("c5372d09e74143e0affbf9c663a49d97", "这是alert"));
			System.out.println(result2.msg_id);
			System.out.println(result2.sendno);
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
	}
}
