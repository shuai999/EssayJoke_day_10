package com.example.administrator.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/6 19:44
 * Version 1.0
 * Params:
 * Description:   这是一个工厂
*/

public class DaoSupportFactory {


    private SQLiteDatabase mSqliteDatabase ;

    private static volatile DaoSupportFactory mFactory ;

    /**
     * 持有外部数据库的引用
     */
    private DaoSupportFactory(){
        // 打开或者创建数据库，这个地方是把数据库放到内存卡中
        // 这里需要判断：1. 是否有内存卡 2. 6.0需要动态申请权限（可以直接在app刚运行就申请权限）
        File dbRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "nhdz"
                + File.separator + "database") ;

        // 如果目录不存在，就去创建目录
        if (!dbRoot.exists()){
            dbRoot.mkdirs() ;
        }

        File dbFile = new File(dbRoot  ,"nhdz.db") ;

        // 打开或者创建一个数据库
        mSqliteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile , null) ;
    }


    /**
     * 这里是双重校验，为了防止多个线程同时操作同一个资源
     */
    public static DaoSupportFactory getFactory(){
        if (mFactory == null){
            synchronized (DaoSupportFactory.class){
                if (mFactory == null){
                    mFactory = new DaoSupportFactory() ;
                }
            }

        }
        return mFactory ;
    }


    /**
     * 这里是使用 自己写的创建表的init()方法，如果以后使用第三方方便切换
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> IDaoSupport<T> getDao(Class<T> clazz){
        IDaoSupport<T> daoSupport = new DaoSupport() ;
        daoSupport.init(mSqliteDatabase  ,clazz);
      return daoSupport ;
    }

}
