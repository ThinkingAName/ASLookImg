package zx.cn.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Description:
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/5/30 23:39
 */

public class PictureLoader {
    private ImageView loadImg;
    private String imgUrl;
    private byte[] picByte;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0x123){
                if (picByte!=null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(
                            picByte,0,picByte.length);
                    loadImg.setImageBitmap(bitmap);
                }
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                URL url = new URL(imgUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000);
                if (conn.getResponseCode()==200){
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int lenght = -1;
                    while ((lenght=is.read(bytes))!=-1){
                        os.write(bytes,0,lenght);
                    }
                    picByte = os.toByteArray();
                    is.close();
                    os.close();
                    handler.sendEmptyMessage(0x123);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void load(ImageView loadImg, String imgUrl) {
        this.loadImg = loadImg;
        this.imgUrl = imgUrl;
        Drawable drawable = loadImg.getDrawable();
        if(drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            if(bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        new Thread(runnable).start();
    }
    
}
