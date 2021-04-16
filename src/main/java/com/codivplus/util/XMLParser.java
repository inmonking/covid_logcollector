package com.codivplus.util;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.codivplus.model.ResultDataVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Component
public class XMLParser {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<Map<String,Object>> XMLParsing(String XMLData) throws IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		HashMap<String, Object> map;
		
		List<Map<String,Object>> returnList = new ArrayList<>();
		
		try {
			String result = XMLData;
			InputSource is = new InputSource(new StringReader(result));
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("//items/item");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				NodeList child = nodeList.item(i).getChildNodes();
				map = new HashMap<String, Object>();
				for (int j = 0; j < child.getLength(); j++) {
					Node node = child.item(j);
					map.put(Converter.camel2potholeConvert(node.getNodeName()), node.getTextContent());
				}
				returnList.add(map);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		logger.info("Parsing Count : "+returnList.size());
		return returnList;
	}
}
