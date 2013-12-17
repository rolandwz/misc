package com.roland.rwtool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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
			String xagStr = fetchUrl("http://hq.sinajs.cn/?_=1386077085140/&list=hf_XAG");
			xagStr = xagStr.substring(19, xagStr.length() - 3);
			// Utils.updateLog("xagStr:" + xagStr);
			String[] xagArr = xagStr.split(",");
			String dt = xagArr[12] + " " + xagArr[6];
			double xag = Double.parseDouble(xagArr[0]);
			double xag0 = Double.parseDouble(xagArr[7]);
			
			String usdStr = fetchUrl("http://hq.sinajs.cn/rn=13860770561347070422433316708&list=USDCNY");
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
			String agtdStr = fetchUrl("http://hq.sinajs.cn/list=hf_AGTD");
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
			String str = fetchUrl("http://hq.sinajs.cn/rn=1386417950746&list=" + stockId);
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
	}
	
	private static String fetchUrl(String url) throws ClientProtocolException, IOException {
		StringBuilder sb = new StringBuilder();
		
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSoTimeout(params, 5000);
		HttpResponse resp = client.execute(new HttpGet(url));
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
