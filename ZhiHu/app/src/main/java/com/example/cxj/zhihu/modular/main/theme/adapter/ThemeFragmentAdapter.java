package com.example.cxj.zhihu.modular.main.theme.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.modular.main.home.entity.Story;

import java.util.List;

import xiaojinzi.base.android.adapter.listView.CommonAdapter;
import xiaojinzi.base.android.adapter.listView.CommonViewHolder;
import xiaojinzi.imageLoad.ImageLoad;


/**
 * Created by cxj on 2016/1/20.
 */
public class ThemeFragmentAdapter extends CommonAdapter<Story> {

    /**
     * 构造函数
     *
     * @param context
     * @param data
     * @param layout_id
     */
    public ThemeFragmentAdapter(Context context, List<Story> data, int layout_id) {
        super(context, data, layout_id);
    }

    @Override
    public void convert(CommonViewHolder h, Story item, int position) {
        TextView tv_title = h.getView(R.id.tv_story_item_title);
        ImageView icon = h.getView(R.id.iv_story_item_icon);

        icon.setImageResource(R.mipmap.loading);

        tv_title.setText(item.getTitle());
        tv_title.setTextColor(MyApp.skin.themeFragmentSkin.getListTextForeColor());

        if(item.getImages() == null || item.getImages().size() == 0){
            icon.setVisibility(View.GONE);
        }else{
            icon.setVisibility(View.VISIBLE);
            ImageLoad.getInstance().asyncLoadImage(icon, item.getImages().get(0));
        }
    }
}
