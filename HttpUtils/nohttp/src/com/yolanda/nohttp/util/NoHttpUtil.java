package com.yolanda.nohttp.util;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by Administrator on 2016/7/23.
 */
public class NoHttpUtil {
   RequestQueue requestQueue = NoHttp.newRequestQueue();
    public void add(String url,int what){
        /**
         * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
         */
     //   private static final int NOHTTP_WHAT_TEST = 0x001;
        Request request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.addHeader("AppVersioin", "2.0");
        request.add("userName", "yolanda");
        //request.add("file", new FileBinary(file));
        requestQueue.add(what, request, new StringHandler(){

            @Override
            public void onSuccess(int what, int code, String content) {
                super.onSuccess(what, code, content);
            }
        });
    }
}
