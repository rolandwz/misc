package com.roland.rwtool.data;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.roland.rwtool.Utils;

public class MarketFetcher {
	public static List<String> marketDataList = null;
	public static DecimalFormat defaultFormat = new DecimalFormat("#######0.0##");

	public static List<String> getMarketDataList() {
		marketDataList = new ArrayList<String>();
    	
    	try {
    		marketDataList.add(fetchAgg());
    		marketDataList.add(fetchAgtd());
    		marketDataList.add(fetchStock("sh000001", "SHDX"));
    		marketDataList.add(fetchStock("sh600050", "UNICOM"));
    		
    	} catch(Exception e) {
			e.printStackTrace();
			marketDataList.add("Exception:" + e.getMessage());
			return marketDataList;
		}
    	
    	return marketDataList;
    }
	
	private static String fetchAgg() {
		try {
			String xagStr = Utils.fetchUrl("http://hq.sinajs.cn/?_=1386077085140/&list=hf_XAG", null);
			xagStr = xagStr.substring(19, xagStr.length() - 3);
			// Utils.updateLog("xagStr:" + xagStr);
			String[] xagArr = xagStr.split(",");
			String dt = xagArr[12] + " " + xagArr[6];
			double xag = Double.parseDouble(xagArr[0]);
			double xag0 = Double.parseDouble(xagArr[7]);
			
			String usdStr = Utils.fetchUrl("http://hq.sinajs.cn/rn=13860770561347070422433316708&list=USDCNY", null);
			usdStr = usdStr.substring(19, usdStr.length() - 3);
			double usd = Double.parseDouble(usdStr.split(",")[1]);

			double agg = usd * xag / 31.1035;
			double agg0 = usd * xag0 / 31.1035;
			//double per = Double.parseDouble(xagArr[1]);
			
			String ret = dt + " AGG\n" + defaultFormat.format(agg) + "     "
						+ xagArr[1] + "%    " + defaultFormat.format(agg0) + "\n";
			return ret;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "AGG: ClientProtocolException";
		} catch (IOException e) {
			e.printStackTrace();
			return "AGG: IOException";
		}
	} // fetchAgg
	

	private static String fetchAgtd() {
		try {
			String agtdStr = Utils.fetchUrl("http://hq.sinajs.cn/list=hf_AGTD", null);
			agtdStr = agtdStr.substring(20, agtdStr.length() - 3);
			String[] agtdArr = agtdStr.split(",");

			String dt = agtdArr[12] + " " + agtdArr[6];
			double agtd = Double.parseDouble(agtdArr[0]);
			double agtd0 = Double.parseDouble(agtdArr[7]);
			double per = (agtd - agtd0) * 100 / agtd0;
			

			String ret = dt + " AGTD\n" + defaultFormat.format(agtd) + "     "
					+ defaultFormat.format(per) + "%     " + defaultFormat.format(agtd0) + "\n";
			return ret;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "AGTD: ClientProtocolException";
		} catch (IOException e) {
			e.printStackTrace();
			return "AGTD: IOException";
		}
	} // fetchAgtd
	
	private static String fetchStock(String stockId, String stockName) {
		try {
			String str = Utils.fetchUrl("http://hq.sinajs.cn/rn=1386417950746&list=" + stockId, null);
			str = str.substring(21, str.length() - 3);
			String[] arr = str.split(",");

			String dt = arr[30].substring(5) + " " + arr[31];
			double price = Double.parseDouble(arr[3]);
			double price0 = Double.parseDouble(arr[2]);
			double per = (price - price0) * 100 / price0;

			String ret = dt + " " + stockName + "\n" + defaultFormat.format(price) + "     "
					+ defaultFormat.format(per) + "%     " + defaultFormat.format(price0) + "\n";
			return ret;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "AGTD: ClientProtocolException";
		} catch (IOException e) {
			e.printStackTrace();
			return "AGTD: IOException";
		}
	} // fetchStock
	

}
