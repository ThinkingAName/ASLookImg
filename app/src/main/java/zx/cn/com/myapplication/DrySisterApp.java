package zx.cn.com.myapplication;

import android.app.Application;
import android.content.Context;

import zx.cn.com.myapplication.helper.CrashLogHelper;

/**
 * Description:  Application类
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/6/20 21:57
 */

public class DrySisterApp extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        CrashLogHelper.getInstance().init(this);
    }

    public static DrySisterApp getContext() {
        return (DrySisterApp) context;
    }
}
