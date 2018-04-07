package com.hc.baselibrary.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/5 18:43
 * Version 1.0
 * Params:
 * Description:   头部的基类
 *                可能在整个项目中像这样的布局用的地方不是很多，但是必须要有，
 *                所以这里为了考虑头部可能出现的所有情况，也把基本的布局考虑进去
*/

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;

    private View mNavigationView;

    public AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }


    public P getParams() {
        return mParams;
    }


    /**
     * 设置文本
     * @param viewId
     * @param text
     */
    protected void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if(!TextUtils.isEmpty(text)){
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    /**
     * 设置点击
     * @param viewId
     * @param listener
     */
    protected void setOnClickListener(int viewId,View.OnClickListener listener){
        findViewById(viewId).setOnClickListener(listener);
    }


    public <T extends View> T findViewById(int viewId){
        return (T)mNavigationView.findViewById(viewId);
    }

    /**
     * 绑定和创建View
     */
    private void createAndBindView() {
        // 1. 创建View

        if(mParams.mParent == null){
            // 获取activity的根布局，View源码
            // 方法1：android.R.id.content就是 android.R.layout.screen_simple中的一个 FrameLayout的一个id
//            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext))
//                    .findViewById(android.R.id.content);

            // 方法2：.getWindow().getDecorView() 表示直接加载的就是 android.R.layout.screen_simple的根布局，而根布局就是一个 LinearLayout
            // 所以不管 activity_main中的根布局是RelativeLayout、LinearLayout都可以
            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext))
                    .getWindow().getDecorView() ;


            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
            Log.e("TAG",mParams.mParent+"");
        }

        // 处理Activity的源码，后面再去看


        if(mParams.mParent == null){
            return;
        }

        mNavigationView = LayoutInflater.from(mParams.mContext).
                inflate(bindLayoutId(), mParams.mParent, false);// 插件换肤

        // 2.添加
        mParams.mParent.addView(mNavigationView, 0);

        applyView();
    }

    // Builder  仿照系统写的， 套路，活  AbsNavigationBar  Builder  参数Params
    public abstract static class Builder {

        public Builder(Context context, ViewGroup parent) {

        }

        public abstract AbsNavigationBar builder();


        public static class AbsNavigationParams {
            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}

//public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar{
//
//    // 这里传递的是泛型
//    private P mParams ;
//    private View mNavigationView ;
//    public AbsNavigationBar(P params){
//        this.mParams = params ;
//        createAndBindView() ;
//    }
//
//
//    public P getParams() {
//        return mParams;
//    }
//
//
//    public void setText(int viewId, String text) {
//        TextView tv = findViewById(viewId);
//        if (TextUtils.isEmpty(text)){
//            tv.setVisibility(View.VISIBLE);
//            tv.setText(text);
//        }
//    }
//
//    public void setOnClicListener(int viewId , View.OnClickListener listener){
//        findViewById(viewId).setOnClickListener(listener);
//    }
//
//
//    public <T extends View> T findViewById(int viewId){
//        return (T) mNavigationView.findViewById(viewId);
//    }
//    /**
//     * 创建和绑定 头部的View
//     */
//    private void createAndBindView() {
//        // 1. 创建头部的View
//        if (mParams.mParent == null){
//            // 获取Activity的根布局
//            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext))
//                    .findViewById(android.R.id.content);
//            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
//        }
//
//        if (mParams.mParent == null){
//            return;
//        }
//        mNavigationView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId() , mParams.mParent , false) ;
//        // 2. 添加头部到父布局，注意需要添加到第0个位置  参数1：要添加的头部的View，参数2：添加的位置
//        mParams.mParent.addView(mNavigationView , 0);
//        // 3. 绑定参数
//        applyView();
//    }
//
//
//    // Builder设计模式  仿照系统写的  其实设计模式都是一些套路，一定要把它用到项目中，
//    // 不要记死，如果学习了但是不在项目中使用，相当于没学，你可能永远都不会在项目中去用
//    // 这里涉及到3个部分，分别是  AbsNavigationBar、Builder、参数Params
//    public abstract static class Builder{
//
//        AbsNavigationParams p ;
//
//        /**
//         * 构造方法
//         * @param context
//         * @param parent ： 父布局，你要把布局文件添加到哪个父布局
//         */
//        public Builder(Context context , ViewGroup parent){
//            // 创建 p
//
//
//        }
//        public abstract AbsNavigationBar builder();
//
//        public static class AbsNavigationParams{
//            public Context mContext ;
//            public ViewGroup mParent ;
//            public AbsNavigationParams(Context context , ViewGroup parent){
//                this.mContext = context ;
//                this.mParent = parent ;
//            }
//        }
//    }
//}
