package com.yyw.okhttpdemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.yyw.okhttpdemo.progressbar.OkHttpHelper;
import com.yyw.okhttpdemo.progressbar.SafeOkHttp;
import com.yyw.okhttpdemo.progressbar.UIProgressRequestListener;
import com.yyw.okhttpdemo.progressbar.UIProgressResponseListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0106/2275.html#goodfb1113
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0904/3416.html
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        File cacheDirectory = new File(Environment.getExternalStorageDirectory(), "/cache");

        client = OkHttpHelper.getCacheClient(new OkHttpClient(),cacheDirectory,cacheSize);
    }

    public void onClick(View v) {
        HashMap<String, String> params = null;
        String baseUrl = "http://192.168.0.166:8082/LearnNew/servlet/FirstServlet";
        switch (v.getId()) {
            case R.id.ok_get_string:
                params = new HashMap<>();
                params.put("price", "5");
                String getUrl = getUrl(params, baseUrl);
                httpGet1(getUrl);
                break;
            case R.id.ok_get_string2:
                params = new HashMap<>();
                params.put("price", "5");
                httpGet2(getUrl(params, baseUrl));
                break;
            case R.id.ok_post_json:
                postJson(baseUrl);
                break;
            case R.id.ok_post_params:
                postParams(baseUrl);
                break;
            case R.id.ok_get_picture:
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                baseUrl = "http://192.168.0.166:8082/download/a.png";
                getFile(baseUrl, path);
                break;
            case R.id.ok_post_file:
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/a.png";
                baseUrl = "http://192.168.0.166:8082/LearnNew/servlet/UploadHandleServlet";
                upFile(baseUrl, new File(filePath));
                break;
            case R.id.ok_get_https_one:
                params = new HashMap<>();
                params.put("price", "5");
                baseUrl = "https://192.168.0.166:8443/LearnNew/servlet/FirstServlet";
                httpGet3(getUrl(params, baseUrl));
                break;
            case R.id.ok_get_https_two:
                params = new HashMap<>();
                params.put("price", "5");
                baseUrl = "https://192.168.0.166:8443/LearnNew/servlet/FirstServlet";
                httpGet4(getUrl(params, baseUrl));
                break;
            case R.id.ok_get_http_cache:
                params = new HashMap<>();
                params.put("price", "5");
                baseUrl = "https://192.168.0.166:8443/LearnNew/servlet/FirstServlet";
                httpGet_cache(getUrl(params, baseUrl));
                break;
            default:
                break;
        }
    }

    private String getUrl(HashMap<String, String> params, String baseUrl) {
        StringBuilder builder = new StringBuilder(baseUrl);
        if (params.isEmpty()) {
            return baseUrl;
        } else {
            builder.append("?");
            Set<Map.Entry<String, String>> set = params.entrySet();
            for (Map.Entry<String, String> entry : set) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                builder.append("&");
            }
            builder = builder.replace(builder.lastIndexOf("&"), builder.length() + 1, "");
        }
        return builder.toString();
    }

    /**
     * OkHttp的get请求
     * 需要加线程
     */
    private void httpGet1(final String getUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = OkHttpHelper.getCacheRequest_NOT_STORE(getUrl);
                    Response response = client.newCall(request).execute();
                    String r = response.body().string();
                    if (response.isSuccessful()) {
                        Log.i(TAG, "httpGet1 OK: " + r);
                    } else {
                        Log.i(TAG, "httpGet1 error: " + r);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    /**
     * Https的get请求
     * 需要加线程
     */
    private void httpGet3(final String getUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = SafeOkHttp.setCertificates(new URL(getUrl),getAssets().open("yyw_servlet.cer"));
                    Request request = OkHttpHelper.getCacheRequest_NOT_STORE(getUrl);
                    Response response = client.newCall(request).execute();
                    String r = response.body().string();
                    if (response.isSuccessful()) {
                        Log.i(TAG, "httpGet1 OK: " + r);
                    } else {
                        Log.i(TAG, "httpGet1 error: " + r);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    /**
     * Https的get请求
     * 双方验证
     * 不需要加线程
     */
    private void httpGet4(final String getUrl) {
        try {
//            int cacheSize = 2 * 1024; // 10 MiB
//            Cache cache = new Cache(cacheDirectory, cacheSize);
            OkHttpClient client = SafeOkHttp.setCertificatesTwo(new URL(getUrl),getAssets().open("client.p12"),"123456",getAssets().open("server.cer"));
            Request request = new Request.Builder().url(getUrl).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (e != null){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String r = response.body().string();
                    //不是在UI线程中运行的。
                    if (response.isSuccessful()) {
                        Log.i(TAG, "httpGet1 OK: " + r);
                    } else {
                        Log.i(TAG, "httpGet1 error: " + r);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Http的get请求缓存
     * 不需要加线程
     */
    private void httpGet_cache(final String getUrl) {
        try {
//            int cacheSize = 2 * 1024; // 10 MiB
//            Cache cache = new Cache(cacheDirectory, cacheSize);
//            OkHttpClient client = new OkHttpClient.Builder().cache(cache).build();
            Request request = new Request.Builder().url(getUrl).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //不是在UI线程中运行的。
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String r = response.body().string();
                    //不是在UI线程中运行的。
                    if (response.isSuccessful()) {
                        Log.i(TAG, "httpGet1 OK: " + r);
                    } else {
                        Log.i(TAG, "httpGet1 error: " + r);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * OkHttp的get请求
     * 不需要加线程
     */
    private void httpGet2(final String getUrl) {
        try {

            OkHttpClient client = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().url(getUrl).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //不是在UI线程中运行的。
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String r = response.body().string();
                    //不是在UI线程中运行的。
                    if (response.isSuccessful()) {
                        Log.i(TAG, "httpGet1 OK: " + r);
                    } else {
                        Log.i(TAG, "httpGet1 error: " + r);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * POST提交Json数据
     *
     * @param url
     */
    private void postJson(String url) {
        OkHttpClient client = new OkHttpClient();
        String json = "{\"desc\":\"OK\",\"status\":1000,\"data\":{\"wendu\":\"-6\",\"ganmao\":\"天冷空气湿度大，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。\",\"forecast\":[{\"fengxiang\":\"无持续风向\",\"fengli\":\"微风级\",\"high\":\"高温 -2℃\",\"type\":\"多云\",\"low\":\"低温 -7℃\",\"date\":\"20日星期三\"},{\"fengxiang\":\"无持续风向\",\"fengli\":\"微风级\",\"high\":\"高温 -2℃\",\"type\":\"阴\",\"low\":\"低温 -8℃\",\"date\":\"21日星期四\"},{\"fengxiang\":\"北风\",\"fengli\":\"4-5级\",\"high\":\"高温 -6℃\",\"type\":\"晴\",\"low\":\"低温 -16℃\",\"date\":\"22日星期五\"},{\"fengxiang\":\"北风\",\"fengli\":\"5-6级\",\"high\":\"高温 -9℃\",\"type\":\"晴\",\"low\":\"低温 -15℃\",\"date\":\"23日星期六\"},{\"fengxiang\":\"北风\",\"fengli\":\"4-5级\",\"high\":\"高温 -3℃\",\"type\":\"晴\",\"low\":\"低温 -12℃\",\"date\":\"24日星期天\"}],\"yesterday\":{\"fl\":\"微风\",\"fx\":\"无持续风向\",\"high\":\"高温 -2℃\",\"type\":\"晴\",\"low\":\"低温 -9℃\",\"date\":\"19日星期二\"},\"aqi\":\"127\",\"city\":\"北京\"}}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();
                if (response.isSuccessful()) {
                    Log.i(TAG, "httpGet1 OK: " + r);
                } else {
                    Log.i(TAG, "httpGet1 error: " + r);
                }
            }
        });
    }

    /**
     * 上传键值对
     *
     * @param url
     */
    private void postParams(String url) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("price", "25").add("历史", "张三").build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();
                if (response.isSuccessful()) {
                    Log.i(TAG, "httpGet1 OK: " + r);
                } else {
                    Log.i(TAG, "httpGet1 error: " + r);
                }
            }
        });
    }

    /**
     * 获取文件。
     *
     * @param url  地址
     * @param path 下载的文件地址
     */
    private void getFile(final String url, final String path) {
        final String fileName = url.substring(url.lastIndexOf("/") + 1);
        OkHttpClient client = new OkHttpClient();
        client = OkHttpHelper.getOkClient(client, new UIProgressResponseListener() {
            @Override
            public void onUIProgressRequest(long allBytes, long currentBytes, boolean done) {
                float progress = currentBytes * 100f / allBytes;
                Log.i(TAG, "onUIProgressRequest: 总长度：" + allBytes + " 当前下载的长度：" + currentBytes + "是否下载完成：" + done + "下载进度：" + progress);
            }
        });
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream is = response.body().byteStream();
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File downLoad = new File(file, fileName);
                    FileOutputStream fos = new FileOutputStream(downLoad);
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                }
            }
        });
    }

    /**
     * 上传文件
     *
     * @param url
     * @param file
     */
    private void upFile(String url, File file) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename =\"" + file.getName() + "\""), RequestBody.create(null, file)).build();
        body = OkHttpHelper.getRequestBody(new UIProgressRequestListener() {
            @Override
            public void onUIProgressRequest(long allBytes, long currentBytes, boolean done) {
                float progress = currentBytes * 100f / allBytes;
                Log.i(TAG, "onUIProgressRequest: 总长度：" + allBytes + " 当前下载的长度：" + currentBytes + "是否下载完成：" + done + "下载进度：" + progress);
            }
        }, body);
        Request request = new Request.Builder().post(body).url(url).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();
                if (response.isSuccessful()) {
                    Log.i(TAG, "httpGet1 OK: " + r);
                } else {
                    Log.i(TAG, "httpGet1 error: " + r);
                }
            }
        });
    }
}
