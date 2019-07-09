package zx.cn.com.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import zx.cn.com.myapplication.beans.Sister;
import zx.cn.com.myapplication.controller.SisterLoader;
import zx.cn.com.myapplication.helper.SisterDBOperateHelper;
import zx.cn.com.myapplication.httpTools.SisterApi;
import zx.cn.com.myapplication.utils.NetworkUtils;

/**
 * Description:
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/6/19 0:01
 */

public class SecondActivity extends AppCompatActivity implements
        View.OnClickListener{

private Button showBtnNext;
private Button showBtnBefore;
private ImageView showImg;
private ArrayList<String> urls;
private int curPos = 0;
private int page = 1;
private PictureLoader loader;
private SisterLoader mloader;
private SisterTask sisterTask;
private SisterDBOperateHelper mDbHelper;
private ArrayList<Sister> data;
private SisterApi sisterApi;
private Button refreshBtn;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        sisterApi = new SisterApi();
        loader = new PictureLoader();
        mloader = SisterLoader.getInstance(SecondActivity.this);
        mDbHelper = SisterDBOperateHelper.getInstance();
        initUI();
        initData();

        }
@Override
public void onClick(View v) {
    //优化只能显示两页的数据
    int curPos = this.curPos%10;

    switch (v.getId()) {
        case R.id.btn_show_before:
            --this.curPos;
            curPos = this.curPos%10;
            if (this.curPos == 0) {
                showBtnBefore.setVisibility(View.INVISIBLE);
            }
            if (curPos==data.size()-1&&this.curPos>0){
                --page;

                sisterTask = new SisterTask();
                sisterTask.execute();
            }else if (curPos<data.size()){
                mloader.bindBitmap(data.get(curPos).getUrl(),showImg,400,400);
            }
            break;
        case R.id.btn_show_next:
            showBtnBefore.setVisibility(View.VISIBLE);
        if (curPos < data.size()) {
            ++this.curPos;
            curPos = this.curPos%10;
        }
        if (curPos==0&&this.curPos>data.size()-1){//优化curPos>data.size()-1只能显示两页的数据
            ++page;
            sisterTask = new SisterTask();
            sisterTask.execute();
        } else if (curPos < data.size()){
            //loader.load(showImg, data.get(curPos).getUrl());
            mloader.bindBitmap(data.get(curPos).getUrl(),showImg,400,400);
            System.out.println("当前的curPos为："+this.curPos);
        }
        break;
        case R.id.btn_cleardb_sec:
            mDbHelper.deleteAllSisters();
            break;
    }
}
private void initUI() {
    showBtnNext = findViewById(R.id.btn_show_next);
    showBtnBefore = findViewById(R.id.btn_show_before);
    showImg = findViewById(R.id.img_show_sec);
    refreshBtn = findViewById(R.id.btn_cleardb_sec);

    showBtnBefore.setVisibility(View.INVISIBLE);
    showBtnNext.setOnClickListener(this);
    refreshBtn.setOnClickListener(this);
    showBtnBefore.setOnClickListener(this);

        }
private void initData() {

        data = new ArrayList<>();
        sisterTask = new SisterTask();
        sisterTask.execute();
        }



private class SisterTask extends AsyncTask<Void,Void,ArrayList<Sister>> {

    //private int page;

    public SisterTask() {
        System.out.println("当前的page为："+page);
    }

    @Override
    protected ArrayList<Sister> doInBackground(Void... params) {
        ArrayList<Sister> result = new ArrayList<>();

        //判断网络是否可以访问
        if (NetworkUtils.isAvailable(getApplicationContext())){
            result = sisterApi.fetchSister(10,page);
            //查询数据库里有多少个妹子，避免重复插入
            if(mDbHelper.getSistersCount()/10 < page) {
                mDbHelper.insertSisters(result);
            }
        }else {
            result.clear();
            result.addAll(mDbHelper.getSistersLimit(page-1,10));
        }
        return result;
    }



    @Override
    protected void onPostExecute(ArrayList<Sister> sisters) {
        super.onPostExecute(sisters);
        data.clear();
        data.addAll(sisters);
        System.out.println(data.size());
        //curPos=0;
        if (data.size() > 0 && curPos%10 < data.size()) {
            mloader.bindBitmap(data.get(curPos%10).getUrl(), showImg, 400, 400);
        }

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        sisterTask = null;
    }
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sisterTask != null) {
            sisterTask.cancel(true);
        }
    }

}

