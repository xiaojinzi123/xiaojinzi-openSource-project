package com.yoursecondworld.secondworld.common;

import android.os.Bundle;
import android.view.WindowManager;

import xiaojinzi.activity.BaseActivity;
import xiaojinzi.base.android.os.ScreenUtils;


/**
 * Created by cxj on 2016/7/4.
 */
public abstract class BaseAct extends BaseActivity {

    /**
     * 状态栏的高度
     */
    protected int statusHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isTranslucentNavigation()){
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    /**
     * 是否使用沉浸式状态栏
     *
     * @return
     */
    public boolean isTranslucentNavigation(){
        return true;
    }

    @Override
    public void initView() {
        statusHeight = ScreenUtils.getStatusHeight(context);
    }
}
