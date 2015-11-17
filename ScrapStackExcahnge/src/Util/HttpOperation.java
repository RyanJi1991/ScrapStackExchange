package Util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.json.*;

public class HttpOperation {
	
	//perform Http GET, uncompress the Gzip response and return the json file
	public static JSONObject httpGet(String urlStr) throws IOException, InterruptedException{
		//Perform Http GET
		URL url = new URL(urlStr);
		HttpURLConnection conn =(HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
        conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
        conn.setRequestProperty("Accept-Encoding", "gzip");
		if(conn.getResponseCode()!=200){return null;}
		
		//Uncompress the Gzip file	
		Reader rd =  new InputStreamReader(new GZIPInputStream(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		int ch;
		while ((ch=rd.read())!=-1) {
	         sb.append((char)ch);
	    }
		
		//make sure less than 30 requests per second
		Thread.sleep(50);
		
		try{
			JSONObject rawResponse = new JSONObject(sb.toString());
			//if available daily request is lower than 1000, alert user
			if(rawResponse.getInt("quota_remaining")==1000){
				System.out.println("Only 1000 requests remaining");
			}
		
			if(rawResponse.has("backoff")){
				int backOff = rawResponse.getInt("backoff");
				System.out.println("Stack Exchange is unhappy and told you to back off for " + backOff + " seconds");
				Thread.sleep(1000*backOff);
			}
		
			return rawResponse;
		}
		catch (Exception e) {
			return null;
		}
	}
}
