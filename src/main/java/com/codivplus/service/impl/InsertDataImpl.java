package com.codivplus.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codivplus.dao.DataDao;
import com.codivplus.service.InsertData;

@Service
public class InsertDataImpl implements InsertData{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataDao dataDao;
	
	public void insertList(List<Map<String,Object>> list, String dataKey) {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "3");
		Method insertMethod;
		try {
			insertMethod = dataDao.getClass().getMethod("insert"+dataKey, Map.class);
			list.parallelStream().forEach(map ->{
				try {
					insertMethod.invoke(dataDao, map);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					logger.error(e.getMessage());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
