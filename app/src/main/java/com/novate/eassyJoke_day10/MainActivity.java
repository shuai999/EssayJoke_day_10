package com.novate.eassyJoke_day10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.framelibrary.BaseSkinActivity;
import com.example.administrator.framelibrary.HttpCallBack;
import com.hc.baselibrary.http.HttpUtils;
import com.hc.baselibrary.http.OkHttpEngine;
import com.novate.eassyJoke_day10.mode.DiscoverListResult;

public class MainActivity extends BaseSkinActivity implements View.OnClickListener{

    @Override
    protected void initData() {
        HttpUtils.with(this).url("http://is.snssdk.com/2/essay/discovery/v3/")    // 这里的url地址、参数都需要放到jni中，不能让别人反编译，否则你的url地址及参数就让别人知道了
                .addParam("iid", "6152551759")
                .addParam("aid", "7")
                .exchangeEnigne(new OkHttpEngine())  // 这里可以切换网络引擎
                .execute(new HttpCallBack<DiscoverListResult>() {
                    @Override
                    public void onSuccess(DiscoverListResult result) {

                        // TAG: name -> 发现
                        Log.e("TAG" , "name -> " + result.getData().getCategories().getName()) ;  // 这里请求网络成功，且已经打印出数据
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

    }
}
