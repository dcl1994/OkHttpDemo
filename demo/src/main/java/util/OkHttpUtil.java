package util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 封装okhttputil的请求工具类
 */
public class OkHttpUtil {
        public static void  sendOkHttpRequest(String address,okhttp3.Callback callback){
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder()
                    .url(address)
                    .build();
            client.newCall(request).enqueue(callback);
        }
}
