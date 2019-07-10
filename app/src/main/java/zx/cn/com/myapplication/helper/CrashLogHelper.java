package zx.cn.com.myapplication.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zx.cn.com.myapplication.MainActivity;
import zx.cn.com.myapplication.utils.LogUtils;

/**
 * Description: 奔溃日志帮助类
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/7/10 20:31
 */

public class CrashLogHelper implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashLogHelper";

    private static CrashLogHelper instance;
    //用于存储设备信息与异常信息
    private Map<String,String> logInfo = new HashMap<>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;    //系统默认UncaughtExceptionHandler

    private CrashLogHelper() { }

    public static CrashLogHelper getInstance() {
        if(instance == null) {
            instance = new CrashLogHelper();
        }
        return instance;
    }
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    
    
    /**
     *  1.一个文件夹专门放日志文件：需要判断是存储卡是否可用，然后判断文件 夹是否存在，不存在则新建文件夹；
     *  2.需要一个把字符串写入文件的方法
     *  3.崩溃日志的内容组成：当前的时间，应用版本，设备信息，奔溃日志
     *  4.获取系统默认的UncaughtException处理器，然后判断是否为null，不为空 设置为自定义的UncaughtException，这里我们用单例
     *  5.最后是应用的重启，设置1s后重新启动应用；
     */
    
    
    
    //获取本地日志存储路径
    private static String getLogPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Crash" + File.separator;
    }
    //将日志字符串写入日志文件，返回文件名称
    private String writeFile(String str) throws Exception {
         String date = formatter.format(new Date());
         String fileName = "crash-" + date + ".log";
         //判断存储卡是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            String path = getLogPath();
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }
            //写入字符串
            FileOutputStream fos = new FileOutputStream(path+fileName,true);
            fos.write(str.getBytes());
            fos.flush();
            fos.close();
        }

        return fileName;
    }

    //采集应用程序和设备信息
    private void getDeviceInfo(Context context) {
        //获取app版本

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);
            if (info!=null){
                logInfo.put("VersionName",info.versionName);
                logInfo.put("VersionCode",info.versionCode+"");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(TAG,"an error occured when collect package info");
        }

        //获取系统相关信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            try {
                logInfo.put(field.getName(),field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                LogUtils.e(TAG,"an error occured when collect package info");
            }
        }
    }
    //组合crash信心并写入文件
    private String saveCrashInfoToFile(Throwable throwable) throws Exception {
        StringBuilder sb = new StringBuilder();
        //拼接当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        sb.append("\r\n").append(date).append("\n");
        //拼接版本信息和折本信息
        for (Map.Entry<String,String> entry:logInfo.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("==").append(value).append("\n");
        }
        //获取Crash日志信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        printWriter.flush();
        printWriter.close();
        String result = writer.toString();
        //拼接日志奔溃信息
        sb.append(result);
        //写入到文件中
        try {
            LogUtils.i(TAG,sb.toString());
            return writeFile(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "an error occured while writing file...", e);
            sb.append("an error occured while writing file...\r\n");
            writeFile(sb.toString());
        }
        return null;

    }
    //实现  uncaughtException方法
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if(!handleException(throwable) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {

            AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("crash", true);
            PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            System.gc();

        }
    }

    /* 自定义错误处理，错误信息采集，日志文件保存，如果处理了返回True，否则返回False */
    private boolean handleException(Throwable throwable) {
        if(throwable == null) return false;
        try {
            new Thread(){
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext,"程序出现异常，即将重启.",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }.start();
            getDeviceInfo(mContext);
            saveCrashInfoToFile(throwable);
            SystemClock.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
