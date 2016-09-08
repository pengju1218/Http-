package com.logic.http;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
public class RequestParams implements Serializable {
	public Map<String, String> map = new HashMap();
	public RequestParams() {
	}
	public RequestParams(Map<String, String> map2) {
		this.map = map2;
	}
	public void put(String key, String value) {
		this.map.put(key, value);
	}
	public void put(String key, Object value) {
		this.map.put(key, String.valueOf(value));
	}
	public void put(String key, int value) {
		this.map.put(key, value + "");
	}
	public void put(String key, float value) {
		this.map.put(key, String.valueOf(value));
	}
	public void put(String key, double value) {
		this.map.put(key, String.valueOf(value));
	}
	public void put(String key, boolean value) {
		this.map.put(key, String.valueOf(value));
	}
	public String getParams() {
		StringBuffer sb = new StringBuffer("?");
		if(this.map != null && this.map.size() != 0) {
			for(Iterator i$ = this.map.entrySet().iterator(); i$.hasNext(); sb.append("&")) {
				Entry entry = (Entry)i$.next();
				try {
					sb.append((String)entry.getKey()).append("=").append(URLEncoder.encode((String)entry.getValue(), "utf-8"));
				} catch (UnsupportedEncodingException var5) {
					var5.printStackTrace();
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public String getUpInfo() {
		StringBuffer sb = new StringBuffer("");
		if(this.map != null && this.map.size() != 0) {
			for(Iterator i$ = this.map.entrySet().iterator(); i$.hasNext(); sb.append(";")) {
				Entry entry = (Entry)i$.next();
				try {
					sb.append((String)entry.getKey()).append("=").append("\""+URLEncoder.encode((String)entry.getValue(), "utf-8")+"\"");
				} catch (UnsupportedEncodingException var5) {
					var5.printStackTrace();
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	public String getJson() {
		StringBuffer sb = new StringBuffer("{");
		if(this.map != null && this.map.size() != 0) {
			for (Iterator i$ = this.map.entrySet().iterator(); i$.hasNext(); ) {
				Entry entry = (Entry) i$.next();
				//   try {
				sb.append("\"" + (String) entry.getKey()).append("\":").append("\"" + entry.getValue()/*URLEncoder.encode((String) entry.getValue(), "utf-8") */+ "\",");
				//} /*catch (UnsupportedEncodingException var5) {
				//    var5.printStackTrace();
				// }
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
		}
		return sb.toString();
	}

	public String getStringJson() {
		StringBuffer sb = new StringBuffer("{");
		if(this.map != null && this.map.size() != 0) {
			for (Iterator i$ = this.map.entrySet().iterator(); i$.hasNext(); ) {
				Entry entry = (Entry) i$.next();
				//   try {
				sb.append("\\\"" + (String) entry.getKey()).append("\\\":").append("\\\"" + entry.getValue()/*URLEncoder.encode((String) entry.getValue(), "utf-8") */+ "\\\",");
				//} /*catch (UnsupportedEncodingException var5) {
				//    var5.printStackTrace();
				// }
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
		}
		return sb.toString();
	}
}
