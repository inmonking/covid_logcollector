package com.codivplus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class APIParser {
	@Autowired
	private Environment environment;
	
	String startCreateDt;
	String endCreateDt;
	
	public String APIParsing(String key) throws IOException {
		URL url = new URL(URLPathMaking(key));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
		
		return sb.toString();
	}
	
	public String URLPathMaking(String key) {
		setDateType(key);
		StringBuilder urlBuilder = new StringBuilder(environment.getProperty(key+".ServiceURL")); /*URL*/
        try {
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + environment.getProperty(key+".ServiceKey"));
			urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(environment.getProperty(key+".pageNo"), "UTF-8")); /*페이지번호*/
		    urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(environment.getProperty(key+".numOfRows"), "UTF-8")); /*한 페이지 결과 수*/
		    urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(startCreateDt, "UTF-8")); /*검색할 생성일 범위의 시작*/
		    urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(endCreateDt, "UTF-8")); /*검색할 생성일 범위의 종료*/
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return urlBuilder.toString();
	}
	
	public void setDateType(String key){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		if(environment.getProperty(key+".todayCreateDt").equals("true")) {
			startCreateDt = sdf.format(date);
			endCreateDt = sdf.format(date);
		}else {
			startCreateDt = environment.getProperty(key+".startCreateDt");
			endCreateDt = environment.getProperty(key+".endCreateDt");
		}
	}
}
