package com.codivplus.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix="parsing-data")
public class DataSourceConfig {
    private Map<String,Map<String,Object>> parsing_list = new HashMap();
}
