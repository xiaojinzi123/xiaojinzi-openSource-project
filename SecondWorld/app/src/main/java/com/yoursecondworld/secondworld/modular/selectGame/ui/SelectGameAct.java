package com.yoursecondworld.secondworld.modular.selectGame.ui;

import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.yoursecondworld.secondworld.R;
import com.yoursecondworld.secondworld.common.BaseFragmentAct;
import com.yoursecondworld.secondworld.modular.selectGame.adapter.SelectGameActAdapter;
import com.yoursecondworld.secondworld.modular.selectGame.entity.SelectedLabel;
import com.yoursecondworld.secondworld.modular.selectGame.fragment.SelectGameFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.log.L;

/**
 * 选择游戏标签的界面
 */
public class SelectGameAct extends BaseFragmentAct {

    @Injection(R.id.vp_act_select_game)
    private ViewPager vp = null;

    @Injection(R.id.gv_act_select_game_selected)
    private GridView gv_selected = null;

    /**
     * 显示已经选择的标签的适配器
     */
    private BaseAdapter adapter = null;

    /**
     * 已经选择的标签的集合
     */
    private List<String> selectedLabels = new ArrayList<String>();

    /**
     * 显示所有标签的fragment
     */
    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public int getLayoutId() {
        return R.layout.act_select_game;
    }

    @Override
    public void initView() {
        super.initView();
        adapter = new SelectGameActAdapter(context, selectedLabels, R.layout.flow_label_item);
        gv_selected.setAdapter(adapter);
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
    public void setOnlistener() {
        super.setOnlistener();
        gv_selected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLabels.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe
    public void addSelectLabel(SelectedLabel selectedLabel) {
        selectedLabels.add(selectedLabel.name);
        adapter.notifyDataSetChanged();
    }
}
