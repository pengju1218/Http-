package com.yolanda.nohttp.util;

import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by Administrator on 2016/7/23.
 */
public class StringHandler implements OnResponseListener<String> {
    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response<String> response) {
       /* if (what == NOHTTP_WHAT_TEST) {// 判断what是否是刚才指定的请求
            // 请求成功
            String result = response.get();// 响应结果
            // 响应头
            Headers headers = response.getHeaders();
            headers.getResponseCode();// 响应码
            response.getNetworkMillis();// 请求花费的时间
        }*/
        onSuccess(what,response.getHeaders().getResponseCode(),response.get());
    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

    }

    @Override
    public void onFinish(int what) {

    }

    public void onSuccess(int what,int code,String content){

    }
}
