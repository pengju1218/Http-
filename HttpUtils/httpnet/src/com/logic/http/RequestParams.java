package com.logic.http;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestParams implements Serializable {
	public  Map<String, String> map = new HashMap<String, String>();

	public RequestParams() {

	}

	public RequestParams(Map<String, String> map2) {
		this.map = map2;
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public void put(String key, Object value) {
		map.put(key, String.valueOf(value));
	}

	public void put(String key, int value) {
		map.put(key, value + "");
	}

	public void put(String key, float value) {
		map.put(key, String.valueOf(value));
	}

	public void put(String key, double value) {
		map.put(key, String.valueOf(value));
	}

	public void put(String key, boolean value) {
		map.put(key, String.valueOf(value));
	}


	public String getParams(){
		StringBuffer sb=new StringBuffer("?");
		if (map != null && map.size() != 0) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				// 如果请求参数中有中文，需要进行URLEncoder编码 gbk/utf8
				try {
					sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

}
