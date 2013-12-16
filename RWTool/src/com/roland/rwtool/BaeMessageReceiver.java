package com.roland.rwtool;

import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;

public class BaeMessageReceiver extends FrontiaPushMessageReceiver {

	@Override
	public void onMessage(Context context, String message, String customContentString) {
		MessageMgt.addMessage(context, "", message);
		String messageString = "透传消息 message=\"" + message + "\" customContentString="
				+ customContentString;
		
		Utils.updateLog(messageString);
	}
	
	@Override
	public void onNotificationClicked(Context context, String title, 
			String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;

		MessageMgt.addMessage(context, title, description);
		Utils.updateLog(notifyString);
		
		Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
	}
	
	@Override
	public void onBind(Context context, int errorCode, String appid, 
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		if (errorCode == 0) {
			Utils.setBind(context, true);
		}
		Utils.updateLog(responseString);
	}

	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			Utils.setBind(context, false);
		}
		Utils.updateLog(responseString);
	}

	@Override
	public void onDelTags(Context context, int errorCode, 
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode + " sucessTags="
				+ sucessTags + " failTags=" + failTags + " requestId="
				+ requestId;
		Utils.updateLog(responseString);
	}

	@Override
	public void onListTags(Context context, int errorCode, 
			List<String> tags, String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags=" + tags;
		
		Utils.updateLog(responseString);
	}

	@Override
	public void onSetTags(Context context, int errorCode, 
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode + " sucessTags="
				+ sucessTags + " failTags=" + failTags + " requestId="
				+ requestId;
		
		Utils.updateLog(responseString);
	}

}
