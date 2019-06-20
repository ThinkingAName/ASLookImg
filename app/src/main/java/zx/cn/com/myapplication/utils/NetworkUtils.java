package zx.cn.com.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Description:  网络相关工具类
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/6/18 23:40
 */

public class NetworkUtils {

    /** 获取网络信息 */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /** 判断网络是否可用 */
    public static boolean isAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable();
    }
}
