package com.example.cxj.huaishi;

import android.app.Application;
import android.app.ProgressDialog;


import com.example.cxj.huaishi.common.entity.User;
import com.example.cxj.huaishi.service.NetWorkService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xiaojinzi.base.android.os.ProgressDialogUtil;


/**
 * Created by cxj on 2016/6/10.
 */
public class MyApp extends Application {

    /**
     * 声明请求的接口
     */
    public static NetWorkService netWorkService;

    /**
     * 网络请求框架
     */
    public static Retrofit retrofit;

    public static Gson gson;

    /**
     * 进度对话框,需要在activity中初始化,第一个activity中最适合
     */
    public static ProgressDialog dialog;

    /**
     * 整个项目的维护的User对象,用户对象
     */
    public static User u = new User();

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.102:8080/huaishi/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        //让框架自动实现我们的请求接口,让我们的请求接口可以被调用
        netWorkService = retrofit.create(NetWorkService.class);

        gson = new GsonBuilder().create();

        Fresco.initialize(this);

    }
}
