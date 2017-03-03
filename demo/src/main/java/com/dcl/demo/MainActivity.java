package com.dcl.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private Button mLoginView;
    private Button mCookieView;
    private Button dopostString;
    private Button mdopostfile;
    private TextView mCookieTextView;
    private EditText musername;
    private EditText mpassword;
    private String name;
    private String password;


    OkHttpClient okHttpClient = new OkHttpClient();

    private String mBaseUrl="http://192.168.1.118:8080/Demo_okhttp/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        mContext = MainActivity.this;

    }
    private void initview() {
        mLoginView = (Button) findViewById(R.id.login_view);
        mCookieView = (Button) findViewById(R.id.get_cookie_view);
        dopostString= (Button) findViewById(R.id.do_post);
        mdopostfile= (Button) findViewById(R.id.do_postfile);

        mCookieTextView = (TextView) findViewById(R.id.cookie_show_view);

        musername= (EditText) findViewById(R.id.username);
        mpassword= (EditText) findViewById(R.id.password);

        mLoginView.setOnClickListener(this);
        mCookieView.setOnClickListener(this);
        dopostString.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_view:
                getRequest();
                break;

            case R.id.get_cookie_view:
                postRequest();
                break;

            case R.id.do_post:
                doPostString();
                break;
            case R.id.do_postfile:
                doPostFile();
                break;
        }
    }
    /**
     * 发送一个get请求
     */
    private void getRequest() {
        Request.Builder builder = new Request.Builder();
        name=musername.getText().toString();
        password=mpassword.getText().toString();
        //2.构造Request
        Request request = builder.get().url(mBaseUrl+"login?username="+name+"&password="+password).build();
        Log.e("name",name);
        Log.e("password",password);
        //3.将Request封装为Call
        executeRequest(request);
        Log.v("sms", "发送get请求");

    }
    private void executeRequest(Request request) {
        Call call = okHttpClient.newCall(request);
        //4.执行calll
        //    Response response=call.execute();      直接调用
        /**
         * 使用异步的方法
         */
        call.enqueue(new Callback() {
            //失败的时候调用
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure:" + e.getMessage());
                e.printStackTrace();
            }

            //成功的时候调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("onResponse:");
                final String res = response.body().string();
                L.e(res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCookieTextView.setText(res);
                    }
                });
            }
        });
    }

    /**
     * 发送一个post请求
     */
    private void postRequest() {
        name=musername.getText().toString();
        password=mpassword.getText().toString();
        /**
         * 1:拿到httpclient对象
         */
        FormBody body=new FormBody.Builder()
                .add("username", name)
                .add("password", password)
                .build();
        Log.v("sms","发送post请求");
        /**
         * 2:构造request对象
         */
        Request.Builder builder = new Request.Builder();
        Request request=  builder.url(mBaseUrl + "login").post(body).build();
        executeRequest(request);
    }

    /**
     * dopostString,发送一个String到服务器端
     */
    private void doPostString() {
        RequestBody requestbody= RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), "{username:小龙,password:123456}");
        Request.Builder builder = new Request.Builder();
        Request request= builder.url(mBaseUrl + "postString").post(requestbody).build();
        executeRequest(request);
    }
    /**
     * dopostfile，发送一个文件到服务器端
     */
    private void doPostFile() {
        File file=new File(Environment.getExternalStorageDirectory(),"ic_launcher.png");
        if (!file.exists()){
            L.e(file.getAbsolutePath()+"没有图片");
            return;
        }
        RequestBody requestbody= RequestBody.create(MediaType.parse("application/octet-stream"), file);
        Request.Builder builder = new Request.Builder();
        Request request= builder.url(mBaseUrl + "postString").post(requestbody).build();
        executeRequest(request);
    }

}
