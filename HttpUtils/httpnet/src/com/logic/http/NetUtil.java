package com.logic.http;

import android.accounts.NetworkErrorException;
import android.net.ParseException;
import android.util.Log;
import android.util.Xml;


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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLHandshakeException;

import sun.net.www.http.HttpClient;


public class NetUtil {
    private static String sessionid = null;
    public static NetUtil netUtil;

    public static NetUtil getInstance() {
        if (netUtil == null) {
            netUtil = new NetUtil();
        }
        return netUtil;
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


    public String getHttpRequest(String UrlPath, RequestParams requestParams) {

        String content = "";

        org.apache.http.client.HttpClient httpClient = getHttpClient();
        HttpGet getMethod = new HttpGet(UrlPath + requestParams.getParams());

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
                content = EntityUtils.toString(response.getEntity(), "UTF-8");

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return content;
    }


    public String sendPOSTRequestHttpClient(String path, Map<String, String> params) {
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

    public DefaultHttpClient getHttpClient() {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        DefaultHttpClient client = new DefaultHttpClient(httpParams);
        return client;
    }

    public String postXml(String path, Map<String, String> map) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            // 如果请求参数中有中文，需要进行URLEncoder编码 gbk/utf8
            // xml.append("<" + entry.getKey() + ">" + URLEncoder.encode(entry.getValue(), "utf-8") + "</" + entry.getKey() + ">");
            xml.append("<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">");
        }
        xml.append("</xml>");

        try {
            byte[] xmlbyte = xml.toString().getBytes("UTF-8");
            Log.i("testxml", xml + "");
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);// 允许输出
            conn.setDoInput(true);
            conn.setUseCaches(false);// 不使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("Content-Length", String.valueOf(xmlbyte.length));
            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            // conn.setRequestProperty("X-ClientType", "2");//发送自定义的头信息
            conn.getOutputStream().write(xmlbyte);
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                Log.i("testxml", response + "");
                return getXmlJson(response);
            }

            if (conn != null)
                conn.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e);
        }
        return "";
    }


    public String getXmlJson(String s) {

        InputStream in_withcode;
        StringBuffer json = new StringBuffer();
        s = s.replaceAll("<!\\[CDATA\\[", " ");
        s = s.replaceAll("\\]\\]>", " ");
        s = s.trim();

        //s="<?xml version=\"1.0\" encoding=\"UTF-8\"?><return_code>SUCCESS</return_code><return_msg>OK</return_msg><appid>wx92d44a070a670616</appid><mch_id>1361960602</mch_id><nonce_str>BLkb2FxguHeOL3zw</nonce_str><sign>9946200715A1FB86149B3D2B331B149A</sign><result_code>SUCCESS</result_code><prepay_id>wx20160727171534ed1da19ef70194308071</prepay_id><trade_type>APP</trade_type></xml>";
        //s = s.replaceAll("]]", "");
        Log.i("eeeeeeeeeeese", s + "");
        final XmlPullParser parser;
        try {
            parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
            in_withcode = new ByteArrayInputStream(s.getBytes("UTF-8"));
            parser.setInput(in_withcode, "UTF-8");

            // 产生第一个事件
            int eventType = parser.getEventType();
            json.append("{");
            Log.i("eeeeeeeeeeese", eventType + "");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                Log.i("eeeeeeeeeeese", "22222222222" + "");
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        Log.i("eeeeeeeeeeese", parser.getName());
                        if (parser.getName() != null) {
                            json.append("\"" + parser.getName() + "\"" + ":");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        json.append("\""
                                + parser.getText() + "\",");
                        break;
                    case XmlPullParser.COMMENT:

                    case XmlPullParser.END_TAG:

                        break;

                }

                eventType = parser.next();

            }
            json.deleteCharAt(json.length() - 1);

            json.append("}");

            Log.i("eeeeeeeeeeese", json.toString());
            in_withcode.close();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json.toString();
    }


    public static String post(String url, String content) {
        HttpURLConnection conn = null;

        try {
            URL e = new URL(url);
            conn = (HttpURLConnection) e.openConnection();

            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            if (sessionid != null) {
                conn.setRequestProperty("Cookie", sessionid);
            }
            OutputStream out = conn.getOutputStream();
            out.write(content.getBytes());
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new NetworkErrorException("responsestatusis" + responseCode);
            }
       if(sessionid == null || sessionid.equals("")) {
                    sessionid = getSessionid(conn);
                }
            InputStream is = conn.getInputStream();
            String response = getStringFromInputStream(is);
            String var9 = response;
            return var9;
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

        return null;
    }

    public static String get(String url) {
        HttpURLConnection conn = null;

        String var6;
        try {
            URL e = new URL(url);
            conn = (HttpURLConnection) e.openConnection();
            if (sessionid != null) {
                conn.setRequestProperty("Cookie", sessionid);
            }
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new NetworkErrorException("responsestatusis" + responseCode);
            }
        if(sessionid == null || sessionid.equals("")) {
                    sessionid = getSessionid(conn);
                }
            InputStream is = conn.getInputStream();
            String response = getStringFromInputStream(is);
            var6 = response;
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

        return var6;
    }


    public static String sendURLGETRequest(String path, RequestParams params) {
        try {
            String e = path + params.getParams();
            Log.i("urlsget", e + "");
            URL url = new URL(e);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (sessionid != null) {
                conn.setRequestProperty("Cookie", sessionid);
            }
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                //  sessionid=getSessionid(conn);
               if(sessionid == null || sessionid.equals("")) {
                    sessionid = getSessionid(conn);
                }
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            }

            if (conn != null) {
                conn.disconnect();
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return "";
    }

    public static String sendURLGETRequest1(String path, RequestParams requestParams) {
        try {
            URL e = new URL(path + requestParams.getParams());
            HttpURLConnection conn = (HttpURLConnection) e.openConnection();
            if (sessionid != null) {
                conn.setRequestProperty("Cookie", sessionid);
            }
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
         if(sessionid == null || sessionid.equals("")) {
                    sessionid = getSessionid(conn);
                }
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            }

            if (conn != null) {
                conn.disconnect();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return "";
    }

    public static String sendURLPOSTRequest(String path, RequestParams params) {
        try {
            boolean e = false;
            String sb = params.getParams().substring(1, params.getParams().length());
            byte[] entity = sb.getBytes();
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            if (sessionid != null) {
                conn.setRequestProperty("Cookie", sessionid);
            }
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", entity.length + "");

            OutputStream out = conn.getOutputStream();

            out.write(entity);
            out.flush();
            out.close();
            //获取Cookie：从返回的消息头里的Set-Cookie的相应的值
            if (conn.getResponseCode() == 200) {
             if(sessionid == null || sessionid.equals("")) {
                    sessionid = getSessionid(conn);
                }
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            }

            if (conn != null) {
                conn.disconnect();
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return "";
    }

    public static String sendURLPOSTJson(String path, String json) {
        try {
            boolean e = false;
            byte[] entity = json.getBytes();
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            if (sessionid != null) {
                conn.setRequestProperty("Cookie", sessionid);
            }
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", entity.length + "");
            OutputStream out = conn.getOutputStream();
            out.write(entity);
            out.flush();
            out.close();
            if (conn.getResponseCode() == 200) {
                if(sessionid == null || sessionid.equals("")) {
                    sessionid = getSessionid(conn);
                }
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            }

            if (conn != null) {
                conn.disconnect();
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return "";
    }


    public static String sendURLPOSTRJson(String path, RequestParams params) {
        try {
            boolean e = false;
            byte[] entity = params.getJson().getBytes();
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            if (sessionid != null) {
                conn.setRequestProperty("Cookie", sessionid);
            }
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", entity.length + "");
            OutputStream out = conn.getOutputStream();
            out.write(entity);
            out.flush();
            out.close();
            if (conn.getResponseCode() == 200) {
              if(sessionid == null || sessionid.equals("")) {
                    sessionid = getSessionid(conn);
                }
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            }

            if (conn != null) {
                conn.disconnect();
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return "";
    }


    public static String getSessionid(HttpURLConnection conn) {
        String cookieval = conn.getHeaderField("Set-Cookie");
        String sessionid = null;
        if (cookieval != null) {
            sessionid = cookieval.substring(0, cookieval.indexOf(";"));
        }
        Log.i("urlsget", sessionid + "");
        return sessionid;
    }

    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 10000000; //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    public static String uploadFile(String RequestURL, RequestParams params, File file) {
        //  String sbp = params.getParams().substring(1, params.getParams().length());
        String sses = params.getUpInfo();
        Log.i("eeesaes", sses);
        //byte[] entity=sbp.getBytes();
        String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; //内容类型


        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false); //不允许使用缓存
            conn.setRequestMethod("POST"); //请求方式
            if (sessionid != null) {
                conn.setRequestProperty("Cookie", sessionid);
            }
            conn.setRequestProperty("Charset", CHARSET);
            //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.setRequestProperty("Content-Disposition", sses);
            if (file != null) {
                /** * 当文件不为空，把文件包装并且上传 */
                OutputStream outputSteam = conn.getOutputStream();

                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; ok=\"11111111111111111111\";name=\"img\";" + sses + ";filename=\"" + file.getName() + "\"" + LINE_END);

                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);

                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

             /*   outputSteam.write(entity);
                outputSteam.flush();
                outputSteam.close();*/
                /**
                 * 获取响应码 200=成功
                 * 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                Log.e(TAG, "response code:" + res);
                if (conn.getResponseCode() == 200) {
                     if(sessionid == null || sessionid.equals("")) {
                    sessionid = getSessionid(conn);
                }
                    InputStream is2 = conn.getInputStream();
                    String response = getStringFromInputStream(is2);
                    return response;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FAILURE;
    }
}
