package com.yoursecondworld.secondworld.modular.guide.ui;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yoursecondworld.secondworld.R;
import com.yoursecondworld.secondworld.common.BaseAct;
import com.yoursecondworld.secondworld.modular.login.ui.LoginAct;
import com.yoursecondworld.secondworld.modular.register.ui.RegisterAct;

import java.io.IOException;
import java.io.InputStream;

import xiaojinzi.annotation.Injection;
import xiaojinzi.annotation.ViewInjectionUtil;
import xiaojinzi.base.android.activity.ActivityUtil;

/**
 * 引导界面
 */
public class GuideAct extends BaseAct {

    @Injection(R.id.tv_act_guide_tip)
    private TextView tv_logo = null;

    @Injection(value = R.id.bt_act_guide_register, click = "clickView")
    private Button bt_register = null;

    @Injection(value = R.id.bt_act_guide_login, click = "clickView")
    private Button bt_login = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_guide;
    }

    @Override
    public void initView() {
        super.initView();

        //设置特殊的字体
        tv_logo.setTypeface(Typeface.createFromAsset(getAssets(), "txchj.TTF"));

    }

    public void clickView(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_act_guide_register: //去注册的界面

                ActivityUtil.startActivity(context, RegisterAct.class);

                finish();
                break;
            case R.id.bt_act_guide_login: //去登陆的界面

                ActivityUtil.startActivity(context, LoginAct.class);

                finish();
                break;
        }
    }

    @Override
    public boolean isRegisterEvent() {
        return false;
    }

    @Override
    public boolean isTranslucentNavigation() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

}
