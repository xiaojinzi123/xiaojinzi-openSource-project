package com.yoursecondworld.secondworld.modular.selectGame.fragment;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.yoursecondworld.secondworld.R;
import com.yoursecondworld.secondworld.modular.selectGame.adapter.SelectGameAdapter;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.activity.fragment.BaseViewPagerFragment;
import xiaojinzi.annotation.Injection;

/**
 * Created by cxj on 2016/7/5.
 */
public class SelectGameFragment extends BaseViewPagerFragment {

    /**
     * 显示多个格子的控件
     */
    @Injection(R.id.gv_frag_select_game)
    private GridView gridView = null;

    private List<String> data = new ArrayList<String>();

    @Override
    public int getLayoutId() {
        return R.layout.frag_select_game;
    }

    private BaseAdapter adapter = null;


    @Override
    public void initData() {
        data.clear();
        data.add("皇室战争");
        data.add("铁道游击队");
        data.add("第五小分队");
        data.add("北京");
        data.add("天天打玻璃");
        data.add("哈");
        data.add("哈哈哈");
        data.add("哈哈哈哈");
        data.add("hello");
        notifyLoadDataComplete();


    }

    @Override
    public void freshUI() {
        if (adapter == null) {
            adapter = new SelectGameAdapter(context, data, R.layout.flow_label_item);
            gridView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean isAutoSubscribeEvent() {
        return false;
    }
}
