package com.example.cxj.huaishi.modular.main.main.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.cxj.huaishi.R;
import com.example.cxj.huaishi.modular.main.home.fragment.HomeFragment;

import java.util.List;

import xiaojinzi.activity.BaseActivity;
import xiaojinzi.activity.BaseFragmentActivity;
import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.os.T;
import xiaojinzi.view.tabHost.MTabHost;

public class MainAct extends BaseFragmentActivity {

    @Injection(R.id.act_main_rg)
    private RadioGroup rg = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initView() {
    }

    @Override
    public void setOnlistener() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_act_main_mtabhost_home:
                        changeFragment(new HomeFragment());
                        break;
                }
            }
        });
    }

    /**
     * 更换fragment
     *
     * @param fragment
     */
    public void changeFragment(Fragment fragment) {
        //fl_act_main_body
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_act_main, fragment);
        ft.commit();
    }

}
