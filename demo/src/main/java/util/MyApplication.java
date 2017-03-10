package util;

import android.app.Application;
import android.content.Context;

/**
 * Created by szjdj on 2017-03-10.
 */
public class MyApplication extends Application{

    private static Context context;
    @Override
    public void onCreate() {
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
