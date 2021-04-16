package com.codivplus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codivplus.dao.DataDao;
import com.codivplus.model.DataSourceConfig;

@Component
public class APIParser {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/* application.yml - parsing-data */
	@Autowired
	private DataSourceConfig dataSourceConfig;

	/* application.yml - last2today-update */
	@Value("${last2today-update}")
	private Boolean last2today;
	
	@Autowired
	private DataDao dataDao;
	
	public String APIParsing(String key) throws IOException {
		URL url = new URL(URLPathMaking(key));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        logger.info("Response code: " + conn.getResponseCode());
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
		String resultURL = "";
		String info = (String) configMap.get("paringInfo");
		if(info != null) {	logger.info("info : "+info);	}
		
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
        resultURL = urlBuilder.toString();
        logger.info("Parsing URL : "+resultURL);
        return resultURL;
	}
	
	@SuppressWarnings("unchecked")
	public void setDateType(Map<String, Object> configMap){
		if(nullChkBoolean(configMap.get("Today"))) {								//	Today 값이 true일 경우 DateParam이 가지고 있는 시작, 마지막 날짜를 전부 오늘로 바꿈
			setlastToTodayUpdate(configMap);									//	false일 경우 startCreateDt, endCreateDt를 파싱함
		}
	}
	
	public void setlastToTodayUpdate(Map<String, Object> configMap) {	// key는 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		Map<String,String> dateParam = (Map<String,String>)configMap.get("DateParam");
		String[] dateParamKey = dateParam.keySet().toArray(new String[dateParam.size()]);
		String[] createDt = new String[dateParamKey.length];
		
		if(createDt.length==2) {							// 받는 날짜 파라미터가 두개일 경우
			if(nullChkBoolean(last2today)) {				// 마지막 날짜부터 시작해 오늘까지 파싱이 true
				createDt[0] = dataDao.selectLastDate((String) configMap.get("tableName"));
			}else {
				createDt[0] =  sdf.format(date);
			}
			createDt[1] =  sdf.format(date);
		}else {
			for (String dt : createDt) {
				dt = sdf.format(date);
			}
		}
		
		for (int idx = 0; idx<dateParamKey.length; idx++) {
			((Map<String,String>)configMap.get("param")).put(dateParam.get(dateParamKey[idx]), createDt[idx]);
		}
	}
	
	public boolean nullChkBoolean(Object boolObj) {
		return boolObj!=null?(boolean)boolObj:false;
	}
}