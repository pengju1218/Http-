package com.logic.http;

import android.os.Handler;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
public class AsynNetUtils {
    public interface Callback {
        void onResponse(String response);
    }

    public static void get(final String url, final Callback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = NetUtil.get(url);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        });
    }

    public static void post(final String url, final String content, final Callback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = NetUtil.post(url, content);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        });
    }

}
