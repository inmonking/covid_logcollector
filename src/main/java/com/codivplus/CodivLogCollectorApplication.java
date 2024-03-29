package com.codivplus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.codivplus.service.InsertData;
import com.codivplus.util.APIParser;
import com.codivplus.util.XMLParser;

@SpringBootApplication
@PropertySource(value = { "classpath:parsingConfig.properties" })
public class CodivLogCollectorApplication implements CommandLineRunner{
	
	@Autowired
	private APIParser apiParser;
	
	@Autowired 
	private XMLParser xmlParser;

	@Autowired
	private InsertData insertData;
	
	@Value("${ParsingDataKey}")
	private String Datakey;
	
	public static void main(String[] args) {
		SpringApplication.run(CodivLogCollectorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for (String keyStr : Datakey.split(",")) {
			insertData.insertList(xmlParser.XMLParsing(apiParser.APIParsing(keyStr)),keyStr);
		}
	}
}
