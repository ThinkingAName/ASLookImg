package zx.cn.com.myapplication.utils;

import android.util.Log;

import zx.cn.com.myapplication.BuildConfig;

/**
 * Description:
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/7/10 20:13
 */

public class LogUtils {
    private static final String TAG = "ASLookImg";

    public static void v(String msg){if (BuildConfig.DEBUG) Log.v(TAG,msg);}
    public static void v(String tag,String msg){if (BuildConfig.DEBUG) Log.v(tag,msg);}
    public static void d(String msg){if (BuildConfig.DEBUG) Log.d(TAG,msg);}
    public static void d(String tag,String msg){if (BuildConfig.DEBUG) Log.d(tag,msg);}
    public static void i(String msg){if (BuildConfig.DEBUG) Log.i(TAG,msg);}
    public static void i(String tag,String msg){if (BuildConfig.DEBUG) Log.i(tag,msg);}
    public static void w(String msg){if (BuildConfig.DEBUG) Log.w(TAG,msg);}
    public static void w(String tag,String msg){if (BuildConfig.DEBUG) Log.w(tag,msg);}
    public static void e(String msg){if (BuildConfig.DEBUG) Log.e(TAG,msg);}
    public static void e(String tag,String msg){if (BuildConfig.DEBUG) Log.e(tag,msg);}
}
