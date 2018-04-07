package com.novate.eassyJoke_day10;

import org.litepal.crud.DataSupport;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/6 19:58
 * Version 1.0
 * Params:
 * Description:  测试数据 --> 用于测试自己写的数据库
*/
public class Person extends DataSupport{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    // 默认的构造方法
    public Person(){

    }


    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }


}
