package com.loopj.android.http.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.StringHttpResponseHandler;

/**
 * Created by Administrator on 2016/7/23.
 */
public class HttpUtil {

    public void get() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("AppointmentStatusDID", "");
        client.get("", params, new StringHttpResponseHandler() {

            @Override
            public void onSuccess(String content) {

                super.onSuccess(content);
            }

        });
    }


    public void post() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("AppointmentStatusDID", "");
        client.post("", params, new StringHttpResponseHandler() {

            @Override
            public void onSuccess(String content) {

                super.onSuccess(content);
            }

        });
    }
}
