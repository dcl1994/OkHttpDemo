package com.dcl.demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import util.App;
import util.MyApplication;
import util.OkHttpUtil;
import util.UrlUtil;

public class RequestActivity extends Activity implements View.OnClickListener {
    private Button button;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String responseData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        init();
    }
    private void init() {
        button = (Button) findViewById(R.id.login);
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        //判断是否联网
        if (UrlUtil.isConnected(MyApplication.getContext())) {
            String phone = "13148700419";
            String passWord = "be7557be0c67b9abdaf91fab44b30717";
            switch (v.getId()) {
                case R.id.login:
                    OkHttpUtil.sendOkHttpRequest(UrlUtil.login + "?phone=" +
                            phone + "&password=" + passWord, new okhttp3.Callback() {
                        //得到服务器返回的数据
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            responseData = response.body().string();
                            L.e(responseData);
                            LoginAsyncTask loginasynctask = new LoginAsyncTask();
                            loginasynctask.execute();
                        }
                        //在这里对异常情况进行处理
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }
                    });
                    break;
            }
        } else {
            Toast.makeText(MyApplication.getContext(), "没有联网，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 登录的异步处理
     */
    public class LoginAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                jsonObject = new JSONObject(responseData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
        /**
         * 解析json更新UI
         *
         * @param jsonObject
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                jsonArray = jsonObject.getJSONArray("JsonArry");
                Gson gson=new Gson();
                List<App> applist=gson.fromJson(String.valueOf(jsonArray),new TypeToken<List<App>>(){}.getType());
//                List<App> applist = jsonArray.getJSONArray()
                L.e(applist.get(0).getUsername());
                for (App app:applist){
                    L.e(app.getUsername());
                    L.e(app.getMessage());
                    L.e(app.getDescription());
                    L.e(app.getHeadUrl());

                    if (app.getMessage().equals("success")){
                        Toast.makeText(MyApplication.getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    }else {
                        //登录失败给出提示
                    String hint = jsonArray.getJSONObject(0).opt("description").toString();
                    Toast.makeText(MyApplication.getContext(), hint, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


//            try {
//                jsonObject = new JSONObject(responseData);
//                jsonArray = jsonObject.getJSONArray("JsonArry");
//                L.e(jsonArray + "");
//                if (jsonArray.getJSONObject(0).opt("message").equals("success")) {
//                    Toast.makeText(MyApplication.getContext(), "登录成功", Toast.LENGTH_SHORT).show();
//                    //进行跳转等操作
//                } else {
//                    //登录失败给出提示
//                    String hint = jsonArray.getJSONObject(0).opt("description").toString();
//                    Toast.makeText(MyApplication.getContext(), hint, Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

}



