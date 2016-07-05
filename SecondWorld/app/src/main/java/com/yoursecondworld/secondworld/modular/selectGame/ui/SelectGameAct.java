package com.yoursecondworld.secondworld.modular.selectGame.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.yoursecondworld.secondworld.R;
import com.yoursecondworld.secondworld.common.BaseAct;
import com.yoursecondworld.secondworld.common.BaseFragmentAct;
import com.yoursecondworld.secondworld.modular.selectGame.adapter.SelectGameAdapter;
import com.yoursecondworld.secondworld.modular.selectGame.fragment.SelectGameFragment;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.annotation.Injection;

/**
 * 选择游戏标签的界面
 */
public class SelectGameAct extends BaseFragmentAct {

    @Injection(R.id.vp_act_select_game)
    private ViewPager vp = null;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public int getLayoutId() {
        return R.layout.act_select_game;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        fragments.add(new SelectGameFragment());
        fragments.add(new SelectGameFragment());
        fragments.add(new SelectGameFragment());
        fragments.add(new SelectGameFragment());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return false;
            }
        };

    }

    @Override
    public boolean isRegisterEvent() {
        return false;
    }
}
