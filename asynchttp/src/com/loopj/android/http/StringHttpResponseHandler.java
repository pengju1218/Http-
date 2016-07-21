package com.loopj.android.http;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/7/21.
 */
public class StringHttpResponseHandler extends TextHttpResponseHandler {
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        onFailure(responseString, throwable);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        onSuccess(responseString);
    }


    public void onSuccess(String content){

    }

    public void onFailure(String responseString, Throwable throwable){

    }
}
