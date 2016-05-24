package com.example.cxj.zhihu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.config.Constant;

import xiaojinzi.base.android.store.SPUtil;


public class NetBroadCastReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //判断wifi是打开还是关闭
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) { //此处无实际作用，只是看开关是否开启
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;

                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }

        //此处是主要代码，
        //如果是在开启wifi连接和有网络状态下
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && NetworkInfo.State.CONNECTED == info.getState()) { //如果有网络连接,说明是wifi下面的
                MyApp.isWifiEable = true;
                SPUtil.put(context, Constant.SP.common.isWifiEable, true);
            } else { //如果没有网络连接
                MyApp.isWifiEable = false;
                SPUtil.put(context, Constant.SP.common.isWifiEable, false);
            }
        }


    }

}
