package com.example.cxj.zhihu.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.common.skin.Skin;
import com.example.cxj.zhihu.config.Constant;

import java.util.Calendar;

import xiaojinzi.base.android.os.T;


/**
 * Created by cxj on 2016/3/31.
 * 这个服务用于自动更换皮肤
 */
public class AutoChangeSkinService extends Service implements Runnable {

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int what = msg.what;
            if (what == 1) {
                MyApp.skin.loadOwnDayStyle(AutoChangeSkinService.this);
                T.showLong(AutoChangeSkinService.this, "开启日间模式");
            } else {
                MyApp.skin.loadOwnNightStyle(AutoChangeSkinService.this);
                T.showLong(AutoChangeSkinService.this, "开启夜间模式");
            }

        }
    };

    private boolean flag = true;

    @Override
    public void onCreate() {
        new Thread(this).start();
    }

    @Override
    public void onDestroy() {
        flag = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void run() {
        while (flag) {
            int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hours >= 6 && hours <= 18) {
//            if (hours < 6 || hours > 18) {
                if (MyApp.skin.getEnvironment() == Skin.NIGHT_ENVIRONMENT) {
                    h.sendEmptyMessage(1);
                }
            } else {
                if (MyApp.skin.getEnvironment() == Skin.DAY_ENVIRONMENT) {
                    h.sendEmptyMessage(2);
                }
            }
            try {
                Thread.sleep(6000); //休息一分钟
            } catch (InterruptedException e) {
            }
        }
    }
}
