package okhttp3.internal.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by Administrator on 2016/7/23.
 */
public class OkHttpUtil {


    /**
     * 用来处理提交POST提交Json数据
     *
     * @param url  提交的链接
     * @param json json参数
     * @return
     * @throws IOException
     */
    public String post(String url, String json) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    /**
     * POST提交键值对
     *
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public String post(String url, HashMap<String, String> map) throws IOException {
        OkHttpClient client = new OkHttpClient();
        if (map == null) {
            return "";
        }
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody formBody = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder = builder.add(entry.getKey(), entry.getValue());
            // System.out.println(entry.getKey()+"--->"+entry.getValue());
        }
        formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    /**
     * 步get
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String getUrl(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
        }
        return response.body().string();
    }


    /**
     * 异步get
     *
     * @param url
     * @throws Exception
     */
    public void getCall(String url) throws Exception {
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                System.out.println(response.body().string());
            }
        });
    }


    /**
     * Post方式提交String
     *
     * @param url
     * @param postBody
     * @return
     * @throws IOException
     */
    public String postString(String url, String postBody) throws IOException {
        MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
        // System.out.println(response.body().string());
    }


    /**
     * Post方式提交流
     *
     * @param url
     * @throws Exception
     */
    public void postStream(String url) throws Exception {
        final MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        System.out.println(response.body().string());
    }


    /**
     * 提交文件
     *
     * @param url
     * @throws Exception
     */
    public void postFile(String url) throws Exception {
        final MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");

        final OkHttpClient client = new OkHttpClient();
        File file = new File("README.md");

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        System.out.println(response.body().string());
    }


    /**
     * Post方式提交表单
     * @param url
     * @param map
     * @throws Exception
     */
    public void postForm(String url, HashMap<String, String> map) throws Exception {
        final OkHttpClient client = new OkHttpClient();
        if (map == null) {
            return;
        }
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody formBody = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder = builder.add(entry.getKey(), entry.getValue());
            // System.out.println(entry.getKey()+"--->"+entry.getValue());
        }
        formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }

}


