package com.dcl.demo;

import android.util.Log;

/**
 * Created by szjdj on 2016-12-26.
 * log类，打印输出日志文件
 */
public class L {


    private static boolean debug=true;
    private static String TAG="ok_Http";

    public static void  e(String msg){
        if (debug){
            Log.e(TAG,msg);
        }
    }
}
