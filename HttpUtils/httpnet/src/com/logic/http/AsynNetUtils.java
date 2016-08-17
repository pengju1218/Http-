package com.logic.http;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
public class AsynNetUtils {

    public static AsynNetUtils asynNetUtils;


    public static AsynNetUtils getT() {
        if (asynNetUtils == null) {
            asynNetUtils = new AsynNetUtils();
        }
        return asynNetUtils;
    }


    public interface Callback {
        void onResponse(String response);
    }


    public static void getUrl(final String url, RequestParams params, final Callback callback) {

        HttpTask httpTask = new HttpTask(params, callback, "getUrl");
        httpTask.execute(url);

    }


    public static void postUrl(final String url, final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "postUrl");
        httpTask.execute(url);
    }

    public static void getHttp(final String url, final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "getHttp");
        httpTask.execute(url);
    }


    public static void postHttp(final String url, final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "postHttp");
        httpTask.execute(url);
    }


    public static void postXml(final String url, final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "postXml");
        httpTask.execute(url);
    }

    public static void postJsonUrl(final String url, final String json, final Callback callback) {
        JsonHttpTask httpTask = new JsonHttpTask(json, callback, "json");
        httpTask.execute(url);
    }

    public static void postRJson(final String url,final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "postJson");
        httpTask.execute(url);
    }

    static class HttpTask extends AsyncTask<String, Integer, String> {

        public RequestParams params;
        private Callback callback;
        private String method;

        public HttpTask(RequestParams params, Callback callback, String method) {
            this.params = params;
            this.callback = callback;
            this.method = method;
        }

        @Override
        protected String doInBackground(String... url) {

            String response = "";


            if ("getUrl".equals(method)) {
                response = NetUtil.getInstance().sendURLGETRequest(url[0], params);
            } else if ("postUrl".equals(method)) {
                response = NetUtil.getInstance().sendURLPOSTRequest(url[0], params);
            } else if ("getHttp".equals(method)) {
                response = NetUtil.getInstance().getHttpRequest(url[0], params);
            } else if ("postHttp".equals(method)) {
                response = NetUtil.getInstance().sendPOSTRequestHttpClient(url[0], params.map);
            } else if ("postXml".equals(method)) {
                response = NetUtil.getInstance().postXml(url[0], params.map);
            }else if ("postJson".equals(method)) {
                response = NetUtil.getInstance().sendURLPOSTRJson(url[0], params);
            }




            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            callback.onResponse(s);
            super.onPostExecute(s);
        }
    }

    static class JsonHttpTask extends AsyncTask<String, Integer, String> {

        public String params;
        private Callback callback;
        private String method;

        public JsonHttpTask(String params, Callback callback, String method) {
            this.params = params;
            this.callback = callback;
            this.method = method;
        }

        @Override
        protected String doInBackground(String... url) {

            String response = "";

            response = NetUtil.getInstance().sendURLPOSTJson(url[0], params);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            callback.onResponse(s);
            super.onPostExecute(s);
        }
    }

}
