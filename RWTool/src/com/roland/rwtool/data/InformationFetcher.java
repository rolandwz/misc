package com.roland.rwtool.data;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roland.rwtool.Utils;

public class InformationFetcher {

	public static List<String> informationList = null;

	public static List<String> getInformationList() {
		informationList = new ArrayList<String>();
    	
    	try {
    		informationList.add(fetchWeather("101010200", "万柳"));
    		
    	} catch(Exception e) {
			e.printStackTrace();
			informationList.add("Exception:" + e.getMessage());
			return informationList;
		}
    	
    	return informationList;
    }
	

	private static String fetchWeather(String city, String station) {
		try {
			StringBuffer weather = new StringBuffer();
			String html = Utils.fetchUrl("http://ext.weather.com.cn/" + city + ".json", "http://ext.weather.com.cn/p.html");
			JSONObject climate = new JSONObject(html);
			
			html = Utils.fetchUrl("http://mobile.weather.com.cn/data/forecast/" + city + ".html?_=1386498530227",
					"http://mobile.weather.com.cn/weather/" + city + ".html");
			JSONObject forcast = new JSONObject(html);
			JSONObject today = forcast.getJSONObject("f").getJSONArray("f1").getJSONObject(0);
			JSONObject tomorrow = forcast.getJSONObject("f").getJSONArray("f1").getJSONObject(1);

			String pm25Value = "";
			String pm25Quality = "";
			if (station != null && station.length() > 0) {
				html = Utils.fetchUrl("http://zx.bjmemc.com.cn/ashx/Data.ashx?Action=GetAQIClose1h", null);
				JSONArray pm25s = new JSONArray(html);
				
				for (int i = 0; i < pm25s.length(); i ++) {
					JSONObject pm25 = pm25s.getJSONObject(i);
					if (station.equals(pm25.getString("StationName"))) {
						pm25Value = pm25.getString("AQIValue");
						pm25Quality = pm25.getString("Quality");
					} // if
				}
			}
			
			SimpleDateFormat dtf = new SimpleDateFormat("HH:mm", Locale.US);
			String time = dtf.format(new Date());
			weather.append(climate.getString("n") + "," + time + "," + climate.getString("s") + ",");
			weather.append(climate.getString("t") + "℃," + climate.getString("w"));
			weather.append(",湿度" + climate.getString("h") + "\n");

			if (station != null && station.length() > 0) {
				weather.append("空气指数:" + pm25Value + "," + pm25Quality + "\n");
			} // if
			if (today.getString("fa").length() > 0) {
				weather.append("今天白天:" + WEATHER.get(today.getString("fa")));
				weather.append(",最高" + today.getString("fc") + "℃,");
				weather.append(WIND.get(today.getString("fe")) + WINDPOWER.get(today.getString("fg")) + "\n");
			} // if
			weather.append("今天晚间:" + WEATHER.get(today.getString("fb")));
			weather.append(",最低" + today.getString("fd") + "℃,");
			weather.append(WIND.get(today.getString("ff")) + WINDPOWER.get(today.getString("fh")) + "\n");
			weather.append("明天白天:" + WEATHER.get(tomorrow.getString("fa")));
			weather.append(",最高" + tomorrow.getString("fc") + "℃,");
			weather.append(WIND.get(tomorrow.getString("fe")) + WINDPOWER.get(tomorrow.getString("fg")) + "\n");
			
			return weather.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "Weather: ClientProtocolException";
		} catch (IOException e) {
			e.printStackTrace();
			return "Weather: IOException";
		} catch (JSONException e) {
			e.printStackTrace();
			return "Weather: JSONException";
		}
	} // fetchWeather

	private static Map<String, String> WEATHER = new HashMap<String, String>();
	private static Map<String, String> WIND = new HashMap<String, String>();
	private static Map<String, String> WINDPOWER = new HashMap<String, String>();
	
	static {
		WEATHER.put("00", "晴");WEATHER.put("001", "晴");WEATHER.put("01", "多云");WEATHER.put("011", "多云");
		WEATHER.put("02", "阴");WEATHER.put("03", "阵雨");WEATHER.put("04", "雷阵雨");WEATHER.put("05", "雷阵雨伴有冰雹");
		WEATHER.put("06", "雨夹雪");WEATHER.put("07", "小雨");WEATHER.put("08", "中雨");WEATHER.put("09", "大雨");
		WEATHER.put("10", "暴雨");WEATHER.put("11", "大暴雨");WEATHER.put("12", "特大暴雨");WEATHER.put("13", "阵雪");
		WEATHER.put("14", "小雪");WEATHER.put("15", "中雪");WEATHER.put("16", "大雪");WEATHER.put("17", "暴雪");
		WEATHER.put("18", "雾");WEATHER.put("19", "冻雨");WEATHER.put("20", "沙尘暴");WEATHER.put("21", "小到中雨");
		WEATHER.put("22", "中到大雨");WEATHER.put("23", "大到暴雨");WEATHER.put("24", "暴雨到大暴雨");WEATHER.put("25", "大暴雨到特大暴雨");
		WEATHER.put("26", "小到中雪");WEATHER.put("27", "中到大雪");WEATHER.put("28", "大到暴雪");WEATHER.put("29", "浮尘");
		WEATHER.put("30", "扬沙");WEATHER.put("31", "强沙尘暴");WEATHER.put("53", "霾");WEATHER.put("99", "未知");

		WIND.put("0", "");WIND.put("1", "东北风");WIND.put("2", "东风");WIND.put("3", "东南风");WIND.put("4", "南风");
		WIND.put("5", "西南风");WIND.put("6", "西风");WIND.put("7", "西北风");WIND.put("8", "北风");WIND.put("9", "旋转风");

		WINDPOWER.put("0", "微风");WINDPOWER.put("1", "3-4级");WINDPOWER.put("2", "4-5级");WINDPOWER.put("3", "5-6级");
		WINDPOWER.put("4", "6-7级");WINDPOWER.put("5", "7-8级");WINDPOWER.put("6", "8-9级");WINDPOWER.put("7", "9-10级");
		WINDPOWER.put("8", "10-11级");WINDPOWER.put("9", "11-12级");WINDPOWER.put("99", "未知");
	} // static
	
}
