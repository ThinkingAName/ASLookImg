package zx.cn.com.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import zx.cn.com.myapplication.beans.Sister;
import zx.cn.com.myapplication.httpTools.SisterApi;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    private Button showBtn;
    private ImageView showImg;
    private ArrayList<String> urls;
    private int curPos = 0;
    private PictureLoader loader;
    private ArrayList<Sister> data;
    private int page = 1;
    private SisterApi sisterApi;
    private Button refreshBtn;
    private Button toSecondBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sisterApi = new SisterApi();
        loader = new PictureLoader();
        initData();
        initUI();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_second:
                Intent intent = new Intent(this,SecondActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_show:
                int a = 1/0;
                if (curPos > 9) {
                    curPos = 0;
                }
                loader.load(showImg, data.get(curPos).getUrl());
                curPos++;
                break;
            case R.id.btn_refresh:
                page++;
                new SisterTask(page).execute();
                curPos = 0;
                break;
        }
    }
    private void initUI() {
        showBtn = findViewById(R.id.btn_show);
        showImg = findViewById(R.id.img_show);
        toSecondBtn = findViewById(R.id.btn_to_second);
        refreshBtn = findViewById(R.id.btn_refresh);

        showBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        toSecondBtn.setOnClickListener(this);

    }

    private void initData() {
        //第一版本的请方式
//        urls = new ArrayList<>();
//        urls.add("http://ww4.sinaimg.cn/large/610dc034jw1f6ipaai7wgj20dw0kugp4.jpg");
//        urls.add("http://ww3.sinaimg.cn/large/610dc034jw1f6gcxc1t7vj20hs0hsgo1.jpg");
//        urls.add("http://ww4.sinaimg.cn/large/610dc034jw1f6f5ktcyk0j20u011hacg.jpg");
//        urls.add("http://ww1.sinaimg.cn/large/610dc034jw1f6e1f1qmg3j20u00u0djp.jpg");
//        urls.add("http://ww3.sinaimg.cn/large/610dc034jw1f6aipo68yvj20qo0qoaee.jpg");
//        urls.add("http://ww3.sinaimg.cn/large/610dc034jw1f69c9e22xjj20u011hjuu.jpg");
//        urls.add("http://ww3.sinaimg.cn/large/610dc034jw1f689lmaf7qj20u00u00v7.jpg");
//        urls.add("http://ww3.sinaimg.cn/large/c85e4a5cjw1f671i8gt1rj20vy0vydsz.jpg");
//        urls.add("http://ww2.sinaimg.cn/large/610dc034jw1f65f0oqodoj20qo0hntc9.jpg");
//        urls.add("http://ww2.sinaimg.cn/large/c85e4a5cgw1f62hzfvzwwj20hs0qogpo.jpg");
//      第二版本的请方式

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
