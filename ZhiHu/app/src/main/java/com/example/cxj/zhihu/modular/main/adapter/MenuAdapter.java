package com.example.cxj.zhihu.modular.main.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.modular.main.entity.Theme;

import java.util.List;

import xiaojinzi.base.android.adapter.listView.CommonAdapter;
import xiaojinzi.base.android.adapter.listView.CommonViewHolder;


/**
 * Created by cxj on 2016/1/19.
 * 左边菜单使用的adapter
 */
public class MenuAdapter extends CommonAdapter<Theme> {
    /**
     * 构造函数
     *
     * @param context
     * @param data
     * @param layout_id
     */
    public MenuAdapter(Context context, List<Theme> data, int layout_id) {
        super(context, data, layout_id);
    }

    @Override
    public void convert(CommonViewHolder h, Theme item, int position) {
        TextView tv_name = h.getView(R.id.tv_menu_item_name);
        tv_name.setText(item.getName());
        tv_name.setTextColor(MyApp.skin.memuSkin.getListTextForeColor());
    }
}
