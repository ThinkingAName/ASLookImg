package zx.cn.com.myapplication;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Description:    加载结果类
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/6/18 23:33
 */

public class LoaderResult {

    public ImageView img;
    public String uri;
    public Bitmap bitmap;
    public int reqWidth;
    public int reqHeight;

    public LoaderResult(ImageView img, String uri, Bitmap bitmap, int reqWidth, int reqHeight) {
        this.img = img;
        this.uri = uri;
        this.bitmap = bitmap;
        this.reqWidth = reqWidth;
        this.reqHeight = reqHeight;
    }
}
