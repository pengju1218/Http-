package com.logic.http.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyHandler extends DefaultHandler {
	private StringBuffer json = new StringBuffer("{");
	private List<HashMap<String, String>> list = null; // �洢���н��������Ķ���
	private String currentTag = null; // ���ڽ�����Ԫ�صı�ǩ
	private String currentValue = null; // ��ǰ������Ԫ�ص�ֵ

	// private String nodeName; //��ǰ�����Ľ�����

	public MyHandler(String nodeName) {
		// this.nodeName = nodeName;
		System.out.println("111111111111111111111111111");
	}

	public String getJson() {
		json = json.deleteCharAt(json.length() - 1);
		json.append("}");
		return json.toString();
	}

	// ����ȡ����һ����ʼ��ǩʱ�򴥷����¼�
	@Override
	public void startDocument() throws SAXException {
		list = new ArrayList<HashMap<String, String>>();
	}

	// ���Ķ�ȡ��һ����ʼԪ��ʱ�������¼�
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentTag = qName;

	}

	// ����ȡԪ�ص�ֵʱ�򴥷�����
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue = new String(ch, start, length);

		if (currentTag != null && json != null) {
			System.out.println(currentTag + "-----------" + currentValue);

			json.append("\"" + currentTag + "\":\"" + currentValue + "\",");

		}

		// ��ȡ�������ֵ
		currentTag = null;
		currentValue = null;
	}

	// ���������ǵ�ʱ������������
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

	}

}
