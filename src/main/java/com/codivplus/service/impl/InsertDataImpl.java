package com.codivplus.service.impl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codivplus.dao.DataDao;
import com.codivplus.service.InsertData;

@Service
public class InsertDataImpl implements InsertData{
	
	@Autowired
	private DataDao dataDao;
	
	public void insertList(List<Map<String,Object>> list, String dataKey) {
		Method insertMethod;
		try {
			insertMethod = dataDao.getClass().getMethod("insert"+dataKey, Map.class);
			for (Map<String, Object> map : list) {
				insertMethod.invoke(dataDao, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
