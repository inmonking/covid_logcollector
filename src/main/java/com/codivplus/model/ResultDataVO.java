package com.codivplus.model;

import java.lang.reflect.InvocationTargetException;

import lombok.Data;

@Data
public class ResultDataVO {
	private String create_dt;
	private String gubun;
	private String conf_case;
	private String conf_case_rate;
	private String death;
	private String death_rate;
	private String critical_rate;
	
	public void set(String fieldName, String param) {
		String name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
		try {
			this.getClass().getMethod("set"+name, String.class).invoke(this, param);
		} catch (NoSuchMethodException 
				| SecurityException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public boolean find(String fieldName) {
		try {
			this.getClass().getDeclaredField(fieldName);
			return true;
		} catch (NoSuchFieldException | SecurityException e) {
			return false;
		}
	}
}
