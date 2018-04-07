package com.hc.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/6 12:23
 * Version 1.0
 * Params:
 * Description:  引擎的规范
*/
public interface IHttpEngine {
    // get请求
    void get(Context context , String url , Map<String , Object> params , EngineCallBack callBack) ;
    // post请求
    void post(Context context , String url , Map<String , Object> params , EngineCallBack callBack) ;


    // 上传文件
    // 下载文件
    // https 添加证书

}
