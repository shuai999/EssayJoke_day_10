package com.novate.eassyJoke_day10;

import android.app.Application;

import com.hc.baselibrary.http.HttpUtils;
import com.hc.baselibrary.http.OkHttpEngine;

import org.litepal.LitePalApplication;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/6 18:32
 * Version 1.0
 * Params:
 * Description:
*/

public class BaseApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        HttpUtils.init(new OkHttpEngine());
    }
}
