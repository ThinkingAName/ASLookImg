package zx.cn.com.myapplication.httpTools;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import zx.cn.com.myapplication.beans.Sister;

/**
 * Description: http://gank.io/api/data/福利/{请求个数}/{第几页}
 * 例如：每页显示10个，第一页：http://gank.io/api/data/福利/10/1
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/6/3 23:47
 */

public class SisterApi {
    private static final String TAG = "Network";
    //http://gank.io/api/data/福利/ 不能识别汉字，需要转码
    private static final String BASE_URL = "http://gank.io/api/data/";
    private static final String BASE_URL_CN ="福利";

        public ArrayList<Sister> fetchSister(int count,int page){
            String encodeUrl ="";
            try {
                 encodeUrl = URLEncoder.encode(BASE_URL_CN,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("转码后的汉字部分url为："+encodeUrl);
            String fetchUrl = BASE_URL + encodeUrl +"/" + count + "/" + page;
        ArrayList<Sister> sisters = new ArrayList<>();
        Log.e(TAG,"接口地址为："+ fetchUrl);
        try {
            URL url = new URL(fetchUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            //conn.setDoInput(true);
            // Post 请求不能使用缓存
            conn.setInstanceFollowRedirects(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("connection", "keep-alive");
            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//            conn.setRequestProperty("Content-type",
//                    "application/x-java-serialized-object");
//            conn.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded");
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

            conn.setRequestProperty("User-Agent","Mozilla/5.0");
            //conn.setRequestProperty("Charsert","UTF-8");
            //conn.connect();
            int code = conn.getResponseCode();
            Log.v(TAG, "Server response：" + code);
            if (code == 200){
                InputStream is = conn.getInputStream();
                //byte[] data = readFromStream(is);
                String data =  readFromStream(is);
                //System.out.println(data.length);
                System.out.println(data);
                //String result = new String(data,"UTF-8");
                //sisters = parseSister(result);
                sisters = parseSister(data);
            }else {
                Log.e(TAG,"请求失败：" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sisters;
    }

    /**
     * 解析返回Json数据的方法
     */
    public ArrayList<Sister> parseSister(String content) throws Exception {
        Log.e(TAG,"接口返回的数据为" + content);
        ArrayList<Sister> sisters = new ArrayList<>();
        JSONObject object = new JSONObject(content);
        JSONArray array = object.getJSONArray("results");
        System.out.println(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            Sister sister = new Sister();
            sister.set_id(results.getString("_id"));
            sister.setCreateAt(results.getString("createdAt"));
            sister.setDesc(results.getString("desc"));
            sister.setPublishedAt(results.getString("publishedAt"));
            sister.setSource(results.getString("source"));
            sister.setType(results.getString("type"));
            sister.setUrl(results.getString("url"));
            sister.setUsed(results.getBoolean("used"));
            sister.setWho(results.getString("who"));
            sisters.add(sister);
        }
        return sisters;
    }

    /**
     * 读取流中数据的方法
     */
//    public byte[] readFromStream(InputStream inputStream) throws Exception {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        byte[] buffer = new byte[4096];
//        int len ;
//        while ((len = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, len);
//        }
//        inputStream.close();
//        return outputStream.toByteArray();
//    }

    /**
     * 读取流中数据的方法
     */
    public String readFromStream(InputStream inputStream)  {
        StringBuffer sbf = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream,"UTF-8")
            );
            String string = null;

            while ((string=reader.readLine())!=null){
                sbf.append(string);
            }
            reader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return sbf.toString();
    }
}
