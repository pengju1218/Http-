package com.logic.http.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class SaxService {

	public SaxService() {
	}

	public static String readXml(String strxml, String nodeName) {

		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		// ����һ������XML��������
		SAXParserFactory spf = SAXParserFactory.newInstance();
		MyHandler myHandler = new MyHandler(nodeName);
		try {

			InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
			SAXParser parser = spf.newSAXParser();
			parser.parse(in, myHandler);
			in.close();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myHandler.getJson();
	}

}
