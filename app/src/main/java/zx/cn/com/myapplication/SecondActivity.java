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
import zx.cn.com.myapplication.httpTools.SisterApi;

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
private PictureLoader loader;
private SisterLoader mloader;
private ArrayList<Sister> data;
private int page = 1;
private SisterApi sisterApi;
private Button refreshBtn;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        sisterApi = new SisterApi();
        loader = new PictureLoader();
        mloader = SisterLoader.getInstance(SecondActivity.this);
        initData();
        initUI();
        }


@Override
public void onClick(View v) {
    switch (v.getId()) {
        case R.id.btn_show_before:
            if (curPos < 0) {
                curPos = 9;
            }
            //loader.load(showImg, data.get(curPos).getUrl());
            mloader.bindBitmap(data.get(curPos).getUrl(),showImg,400,400);
            curPos--;
            break;
        case R.id.btn_show_next:
        if (curPos > 9) {
        curPos = 0;
        }
        //loader.load(showImg, data.get(curPos).getUrl());
            mloader.bindBitmap(data.get(curPos).getUrl(),showImg,400,400);

            curPos++;
        break;
        case R.id.btn_refresh_sec:
        page++;
        new SisterTask(page).execute();
        curPos = 0;
        break;
        }
        }
private void initUI() {
    showBtnNext = findViewById(R.id.btn_show_next);
    showBtnBefore = findViewById(R.id.btn_show_before);
    showImg = findViewById(R.id.img_show_sec);
    refreshBtn = findViewById(R.id.btn_refresh_sec);

    showBtnNext.setOnClickListener(this);
    refreshBtn.setOnClickListener(this);
    showBtnBefore.setOnClickListener(this);

        }

private void initData() {

        data = new ArrayList<>();
        new SisterTask(page).execute();

        }



private class SisterTask extends AsyncTask<Void,Void,ArrayList<Sister>> {

    private int page;

    public SisterTask(int page) {
        this.page = page;
    }

    @Override
    protected ArrayList<Sister> doInBackground(Void... params) {
        return sisterApi.fetchSister(10,page);
    }



    @Override
    protected void onPostExecute(ArrayList<Sister> sisters) {
        super.onPostExecute(sisters);
        data.clear();
        data.addAll(sisters);
        System.out.println(data.size());

    }
}
}

