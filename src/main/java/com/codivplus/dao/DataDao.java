package com.codivplus.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataDao {
	public void insertGenAgeCase(Map<String,Object> map);
	public void insertTotalCase(Map<String,Object> map);
	public void insertSiDoCase(Map<String,Object> map);
	public void insertNatCase(Map<String,Object> map);
	public void insertHospListA0(Map<String,Object> map);
	public void insertHospList97(Map<String,Object> map);
	public void insertHospList99(Map<String,Object> map);
	
	public String selectLastDate(String key);
}
