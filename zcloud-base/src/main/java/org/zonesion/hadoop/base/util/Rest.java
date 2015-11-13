package org.zonesion.hadoop.base.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.zonesion.hadoop.base.bean.HistoryURL;

public class Rest {
	
	public  static String  doRest(HttpURLConnection connection,HistoryURL historyURL) throws Exception {

		InputStreamReader in = null;
		URL url = new URL(historyURL.toString());
		connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(10000);
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("ContentType", "text/xml;charset=utf-8");
		if (historyURL.getKey() != null) {
			connection.setRequestProperty("X-ApiKey", historyURL.getKey());
		}
		in = new InputStreamReader(connection.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(in);
		StringBuffer strBuffer = new StringBuffer();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			strBuffer.append(line);
		}
		String result  =  strBuffer.toString();
		return result;
	}

}
