package com.litesuits.http.utils;

import android.content.Context;

import com.litesuits.http.LiteHttp;
import com.litesuits.http.data.GsonImpl;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.impl.huc.HttpUrlClient;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.UrlEncodedFormBody;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/23.
 */
public class HttpUtils {
    protected static LiteHttp liteHttp;
    public static final String baseUrl = "http://litesuits.com";

    /**
     * 单例 keep an singleton instance of litehttp
     */
    private LiteHttp initLiteHttp(Context context) {
        if (liteHttp == null) {
            liteHttp = LiteHttp.build(context)
                    .setHttpClient(new HttpUrlClient())       // http client
                    .setJsonConvertor(new GsonImpl())        // json convertor
                    .setBaseUrl(baseUrl)                    // set base url
                    .setDebugged(true)                     // log output when debugged
                    .setDoStatistics(true)                // statistics of time and traffic
                    .setDetectNetwork(true)              // detect network before connect
                    .setUserAgent("Mozilla/5.0 (...)")  // set custom User-Agent
                    .setSocketTimeout(10000)           // socket timeout: 10s
                    .setConnectTimeout(10000)         // connect timeout: 10s
                    .create();
        } else {

            liteHttp.getConfig()                   // configuration directly
                    .setSocketTimeout(5000)       // socket timeout: 5s
                    .setConnectTimeout(5000);    // connect timeout: 5s
        }
        return liteHttp;
    }




    public void login(Map<String,String> map){
        UrlEncodedFormBody urlEncodedFormBody=new UrlEncodedFormBody(map);
        StringRequest login = new StringRequest("https://10.218.128.111/router/rest?")
                .setMethod(HttpMethods.Post)
                .setHttpListener(new HttpListener<String>() {
                    @Override
                    public void onSuccess(String s, Response<String> response) {
                        response.printInfo();
                    }

                    @Override
                    public void onFailure(HttpException e, Response<String> response) {
                        response.printInfo();
                    }
                }).setHttpBody(urlEncodedFormBody);
              //  .setHttpBody(new UrlEncodedFormBody("app_key=4272&cp_code=D_DBKD&format=json&login_account=123456&method=cainiao.yima.app.password.login&os_type=2&password=14e1b600b1fd579f47433b88e8d85291&sign=667037188C082C22C111689EFE06320E&sign_method=md5&timestamp=2016-05-06&v=2.0"));
        liteHttp.executeAsync(login);

    }












}
