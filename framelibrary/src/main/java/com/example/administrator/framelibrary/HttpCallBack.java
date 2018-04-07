package com.example.administrator.framelibrary;

import android.content.Context;

import com.google.gson.Gson;
import com.hc.baselibrary.http.EngineCallBack;
import com.hc.baselibrary.http.HttpUtils;

import java.util.Map;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/6 17:43
 * Version 1.0
 * Params:
 * Description:   处理公共参数的问题
*/
public abstract class HttpCallBack<T> implements EngineCallBack {
    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        // 这里是调用每一个接口都必须带有的公共参数，直接在这里处理公共参数就ok
        params.put("app_name","joke_essay");
        params.put("version_name","5.7.0");
        params.put("ac","wifi");
        params.put("device_id","30036118478");
        params.put("device_brand","Xiaomi");
        params.put("update_version_code","5701");
        params.put("manifest_version_code","570");
        params.put("longitude","113.000366");
        params.put("latitude","28.171377");
        params.put("device_platform","android");


        onPreExecute();

    }


    /**
     * 这个方法就是：在把这些公共参数添加之后，告诉这个onPreExecute()方法，我要开始执行了
     */
    public void onPreExecute() {

    }


    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson() ;
        T objResult = (T) gson.fromJson(result , HttpUtils.analysisClazzInfo(this));
        onSuccess(objResult);

    }


    /**
     * 返回可以直接操作的对象
     */
    public abstract void onSuccess(T result) ;
}
