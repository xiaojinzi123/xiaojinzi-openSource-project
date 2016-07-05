package com.yoursecondworld.secondworld;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by cxj on 2016/7/4.
 * 项目的app类
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

    }
}
