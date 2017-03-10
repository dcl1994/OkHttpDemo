package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 存放所有接口地址的公共类
 */
public class UrlUtil {
    public static final String ProcessUrl = "http://120.76.22.150:8080/CellBank";
    /**
     * 登录注册
     */
    public static final String login = ProcessUrl+"/login.action";

    /**
     * 查找
     */
    public static final String searchUrl=ProcessUrl+"/searchByKey.action";

    /**
     * 评论
     */
    public static  final  String TopicUrl=ProcessUrl+"/getComRep.action";

    /**
     * 获取专家列表
     */
    public static final String getExperts=ProcessUrl+"/getExperts.action";


    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected())
            {
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
