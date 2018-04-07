package com.novate.eassyJoke_day10;

import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.administrator.framelibrary.BaseSkinActivity;
import com.example.administrator.framelibrary.db.DaoSupportFactory;
import com.example.administrator.framelibrary.db.IDaoSupport;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/6 18:16
 * Version 1.0
 * Params:
 * Description:
*/
public class SecondActivity extends BaseSkinActivity {
    @Override
    protected void initData() {

        /**
         * 思路就是：
         *     1>：程序一启动，就会调用工厂DaoSupportFactory，然后调用自己的构造方法，然后创建目录和 nhdz.db数据库 到内存卡中，然后创建 mSliteDataBase数据库；
         *     2>：然后调用 init()方法，该方法是在 DaoSupport中实现 init()方法
         *
         * 目的就是：
         *     1>：利用工厂 getFactory()，别人在调用的时候只需要传递 一个 需要存储的 对象就ok，达到最少知识原则的思想
         *     2>：别人需要添加、删除、修改、查询都非常方便
         *
         *
         */
        // Person是要 存储的数据
        IDaoSupport<Person> daoSupport = DaoSupportFactory.getFactory().getDao(Person.class) ;

        // 最少知识原则

        // 这里可以直接插入数据到数据库，就不用  Person person = new Person() ;
//        daoSupport.insert(new Person("novate" , 26)) ;
        /*List<Person> persons = new ArrayList<>() ;
        for (int i = 0; i < 5000; i++) {
            persons.add(new Person("Novate" , 26 + i)) ;
        }

        long startTime = System.currentTimeMillis() ;
        // 自己的批量插入
        daoSupport.insert(persons);
        // litepal的批量插入
//        DataSupport.saveAll(persons);
        long endTime = System.currentTimeMillis() ;

        // 这里批量插入1000条数据：目的是为了测试自己写的数据库框架所需要时间和第三方litepal数据库比较

        // 统一批量插入5000条：
        // 自己的：48426ms   优化后：2285ms  768ms
        // litepal：4831ms   优化后：1127ms 1120ms
        Log.e("TAG" , "时间差 --> " + (endTime - startTime)) ;*/


        List<Person> personSize = daoSupport.query();
        Log.e("TAG" , "personSize --> " + personSize.size()) ;    // personSize --> 37010

    }

    @Override
    protected void initView() {
        //初始化权限
        initPermission();
    }

    @Override
    protected void initTitle() {

    }


    /**
     * 初始化权限事件
     */
    private void initPermission() {
        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            //权限都申请了
            //是否登录
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_second);
    }
}
