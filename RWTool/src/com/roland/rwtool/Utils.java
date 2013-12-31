package com.roland.rwtool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Utils {
	public static String logStringCache = "";

    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static boolean hasBind(Context context) {
    	SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		String flag = sp.getString("bind_flag", "");
		if ("ok".equalsIgnoreCase(flag)) {
			return true;
		}
		return false;
    }
    
    public static void setBind(Context context, boolean flag) {
    	String flagStr = "not";
    	if (flag) {
    		flagStr = "ok";
    	}
    	SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("bind_flag", flagStr);
		editor.commit();
    }
	
	public static void updateLog(String content) {
		String logText = "" + logStringCache;
		
		System.out.println(content);
		
		if (!logText.equals("")) {
			logText += "\n";
		}
		
		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
		logText += sDateFormat.format(new Date()) + " - ";
		logText += content;
		
		logStringCache = logText;
	}
	
	public static String fetchUrl(String url, String referer) throws ClientProtocolException, IOException {
		StringBuilder sb = new StringBuilder();
		
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSoTimeout(params, 5000);
		HttpGet get = new HttpGet(url);
		if (referer != null)
			get.addHeader("Referer", referer);
		HttpResponse resp = client.execute(get);
		HttpEntity entity = resp.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}
}
