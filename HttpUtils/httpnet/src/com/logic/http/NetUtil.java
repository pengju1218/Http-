package com.logic.http;

import android.accounts.NetworkErrorException;
import android.net.ParseException;
import android.util.Log;


import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRouteParams;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import sun.net.www.http.HttpClient;


public class NetUtil {
    public static String post(String url, String content) {
        HttpURLConnection conn = null;
        try {
            //创建一个URL对象
            URL mURL = new URL(url);
            //调用URL的openConnection()方法,获取HttpURLConnection对象
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("POST");//设置请求方法为post
            conn.setReadTimeout(5000);//设置读取超时为5秒
            conn.setConnectTimeout(10000);//设置连接网络超时为10秒
            conn.setDoOutput(true);//设置此方法,允许向服务器输出内容

            //post请求的参数
            String data = content;
            //获得一个输出流,向服务器写数据,默认情况下,系统不允许向服务器输出内容
            OutputStream out = conn.getOutputStream();//获得一个输出流,向服务器写数据
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();//调用此方法就不必再使用conn.connect()方法
            if (responseCode == 200) {

                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            } else {
                throw new NetworkErrorException("responsestatusis" + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();//关闭连接
            }
        }

        return null;
    }

    public static String get(String url) {
        HttpURLConnection conn = null;
        try {
            //利用stringurl构建URL对象
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {

                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            } else {
                throw new NetworkErrorException("responsestatusis" + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //模板代码必须熟练
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();//把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;
    }


    public static String sendURLGETRequest(String path, RequestParams params) {

        try {




            URL url = new URL(path+params.getParams());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            }
            if (conn != null)
                conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String sendURLGETRequest1(String path, RequestParams requestParams) {

        try {

            URL url = new URL(path+requestParams.getParams());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            }
            if (conn != null)
                conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String sendURLPOSTRequest(String path, RequestParams params) {
        try {
            boolean success = false;
            //StringBuilder是用来组拼请求参数
            String sb=params.getParams().substring(1,params.getParams().length());

            //entity为请求体部分内容
            //如果有中文则以UTF-8编码为username=%E4%B8%AD%E5%9B%BD&password=123
            byte[] entity = sb.getBytes();

            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            // 设置以POST方式
            conn.setRequestMethod("POST");
            // Post 请求不能使用缓存
            //  urlConn.setUseCaches(false);
            //要向外输出数据，要设置这个
            conn.setDoOutput(true);
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded
            //设置content－type获得输出流，便于想服务器发送信息。
            //POST请求这个一定要设置
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", entity.length + "");
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            OutputStream out = conn.getOutputStream();
            //写入参数值
            out.write(entity);
            //刷新、关闭
            out.flush();
            out.close();

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;

            }
            if (conn != null)
                conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    public static String getHttpRequest(String UrlPath, RequestParams requestParams) {

        String  content="";

        org.apache.http.client.HttpClient httpClient = getHttpClient();
        HttpGet getMethod = new HttpGet(UrlPath+requestParams.getParams());

        HttpResponse response = null;
        try {
            response = httpClient.execute(getMethod);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            try {
                content =   EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return content;
    }


    public static String sendPOSTRequestHttpClient(String path, Map<String, String> params) {
        try {
            boolean success = false;
            // 封装请求参数
            List<NameValuePair> pair = new ArrayList<NameValuePair>();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    pair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            // 把请求参数变成请求体部分
            UrlEncodedFormEntity uee = new UrlEncodedFormEntity(pair, "utf-8");
            // 使用HttpPost对象设置发送的URL路径
            HttpPost post = new HttpPost(path);
            // 发送请求体
            post.setEntity(uee);
            // 创建一个浏览器对象，以把POST对象向服务器发送，并返回响应消息
            org.apache.http.client.HttpClient dhc = getHttpClient();
            HttpResponse response = dhc.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {

                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static DefaultHttpClient getHttpClient() {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        DefaultHttpClient client = new DefaultHttpClient(httpParams);
        return client;
    }


}
