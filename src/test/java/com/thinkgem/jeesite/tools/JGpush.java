package com.thinkgem.jeesite.tools;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class JGpush {

	public static void main(String[] args) {

		String appKey = "002ee3f91a04ff95c27ff020";
		String masterSecret = "7091c1c3446d548b1460ee11";
		
		JPushClient jPushClient = new JPushClient(masterSecret, appKey);
		PushPayload pushPayload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.all()).setNotification(Notification.android("这是alert", "这是title", null)).build();
		try {
			PushResult result = jPushClient.sendPush(pushPayload);
			System.out.println(result);
			System.out.println(result.msg_id);
			System.out.println(result.sendno);
		} catch (APIConnectionException e) {
			
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
