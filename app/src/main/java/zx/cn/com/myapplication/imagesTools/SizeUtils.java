package zx.cn.com.myapplication.imagesTools;

import android.content.Context;

/**
 * Description:
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/6/18 22:15
 */

public class SizeUtils {

    /** dp转px */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /** px转dp */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
