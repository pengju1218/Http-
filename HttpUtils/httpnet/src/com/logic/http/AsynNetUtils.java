package com.logic.http;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
public class AsynNetUtils {
    static ExecutorService pool = Executors.newFixedThreadPool(100);
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
        httpTask.executeOnExecutor(pool, url);

    }


    public static void postUrl(final String url, final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "postUrl");
        httpTask.executeOnExecutor(pool, url);
    }

    public static void getHttp(final String url, final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "getHttp");
        httpTask.executeOnExecutor(pool, url);
    }

    public static void get(String url, AsynNetUtils.Callback callback) {
        AsynNetUtils.HttpTask httpTask = new AsynNetUtils.HttpTask((RequestParams) null, callback, "get");
        httpTask.executeOnExecutor(pool, url);
    }

    public static void postHttp(final String url, final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "postHttp");
        httpTask.executeOnExecutor(pool, url);
    }


    public static void postXml(final String url, final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "postXml");
        httpTask.executeOnExecutor(pool, url);
    }

    public static void postJsonUrl(final String url, final String json, final Callback callback) {
        JsonHttpTask httpTask = new JsonHttpTask(json, callback, "json");
        httpTask.executeOnExecutor(pool, url);
    }

    public static void postRJson(final String url, final RequestParams params, final Callback callback) {
        HttpTask httpTask = new HttpTask(params, callback, "postJson");
        httpTask.executeOnExecutor(pool, url);
    }

    public static void postImage(String url, RequestParams params, File file, AsynNetUtils.Callback callback) {

        UPTask htastk = new UPTask(file, params, callback);
        htastk.executeOnExecutor(pool, url);
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
            } else if ("get".equals(this.method)) {
                response = NetUtil.getInstance().get(url[0]);
            } else if ("postUrl".equals(method)) {
                response = NetUtil.getInstance().sendURLPOSTRequest(url[0], params);
            } else if ("getHttp".equals(method)) {
                response = NetUtil.getInstance().getHttpRequest(url[0], params);
            } else if ("postHttp".equals(method)) {
                response = NetUtil.getInstance().sendPOSTRequestHttpClient(url[0], params.map);
            } else if ("postXml".equals(method)) {
                response = NetUtil.getInstance().postXml(url[0], params.map);
            } else if ("postJson".equals(method)) {
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

    static class UPTask extends AsyncTask<String, Integer, String> {

        private File file;
        private AsynNetUtils.Callback callback;
        private RequestParams params;

        public UPTask(File file, RequestParams params, AsynNetUtils.Callback callback) {
            this.file = file;
            this.callback = callback;
            this.params = params;
        }

        protected String doInBackground(String... url) {
            String response = NetUtil.getInstance().uploadFile(url[0], params, file);
            return response;
        }

        protected void onPostExecute(String s) {
            this.callback.onResponse(s);
            super.onPostExecute(s);
        }
    }


}
