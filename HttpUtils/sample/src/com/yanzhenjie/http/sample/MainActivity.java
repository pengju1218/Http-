package com.yanzhenjie.http.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.logic.http.AsynNetUtils;
import com.logic.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/23.
 */
public class MainActivity extends Activity {
    private android.widget.Button bu;
    private android.widget.LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.container = (LinearLayout) findViewById(R.id.container);
        this.bu = (Button) findViewById(R.id.bu);
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params =new RequestParams();
                params.pub("name","111");
                params.pub("pass","222");
                AsynNetUtils.getHttp("http://192.168.0.101:8080/test/ok",params,new AsynNetUtils.Callback(){

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response+"",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
