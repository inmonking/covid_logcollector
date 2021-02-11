package com.codivplus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codivplus.model.DataSourceConfig;

@Component
public class APIParser {
	@Autowired
	private DataSourceConfig dataSourceConfig;
	
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
		Map<String,Object> configMap = dataSourceConfig.getParsing_list().get(key);
		Map<String,String> paramMap = (Map<String, String>) configMap.get("param");
		setDateType(configMap);
		StringBuilder urlBuilder = new StringBuilder((String)configMap.get("ServiceURL")); /*URL*/
        try {
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + (String)configMap.get("ServiceKey"));
			for (String param : paramMap.keySet()) {
				urlBuilder.append("&" + URLEncoder.encode(param,"UTF-8") + "=" + URLEncoder.encode(paramMap.get(param), "UTF-8")); /*페이지번호*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return urlBuilder.toString();
	}
	
	public void setDateType(Map<String, Object> configMap){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		boolean dateBool = configMap.get("Today")!=null?(boolean)configMap.get("Today"):false;
		if(dateBool) {
			Map<String,String> dateParam = (Map<String,String>)configMap.get("DateParam");
			for (String key:dateParam.keySet()) {
				((Map<String,String>)configMap.get("param")).put(dateParam.get(key), sdf.format(date));
			}
		}
	}
}
