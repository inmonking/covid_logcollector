package com.codivplus.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {
	public static <T> T map2voConvert(Map<String, Object> aMap, Class<T> t) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.convertValue(aMap, objectMapper.getTypeFactory().constructType(t));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 public static String camel2potholeConvert(String input) {

	        int inputSize = input.length();

	        for (int i = 0 ; i < inputSize ; i++) {
	            if (Character.isUpperCase(input.charAt(i))) {
	                String str = String.valueOf(input.charAt(i));
	                input = input.replace(str, "_" + str.toLowerCase()); 
	            }
	        }

	        Pattern p = Pattern.compile("\\d");
	        Matcher m = p.matcher(input);

	        while (m.find()) 
	            input = input.replace(m.group(0), "_" + m.group(0));

	        return input;
	    }
}
