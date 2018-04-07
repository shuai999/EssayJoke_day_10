package com.example.administrator.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/6 19:41
 * Version 1.0
 * Params:
 * Description:  接口
*/

public interface IDaoSupport<T> {


    /**
     * 创建表
     */
    void init(SQLiteDatabase sqLiteDatabase , Class<T> clazz) ;
    /**
     * 插入数据
     */
    public long insert(T t) ;

    /**
     * 批量插入，用于检测性能
     */
    public void insert(List<T> datas) ;

    /**
     * 查询所有
     */
    public List<T> query() ;

    /**
     * 按照语句查询
     */



}
