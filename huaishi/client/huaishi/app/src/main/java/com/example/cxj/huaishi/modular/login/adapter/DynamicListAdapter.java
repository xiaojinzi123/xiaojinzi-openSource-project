package com.example.cxj.huaishi.modular.login.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.cxj.huaishi.R;
import com.example.cxj.huaishi.modular.postCard.entity.Card;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import xiaojinzi.base.android.adapter.listView.CommonAdapter;
import xiaojinzi.base.android.adapter.listView.CommonViewHolder;

/**
 * Created by cxj on 2016/6/14.
 */
public class DynamicListAdapter extends CommonAdapter<Card> {

    /**
     * 构造函数
     *
     * @param context   上下文
     * @param data      显示的数据
     * @param layout_id listview使用的条目的布局文件id
     */
    public DynamicListAdapter(Context context, List<Card> data, int layout_id) {
        super(context, data, layout_id);
    }

    @Override
    public void convert(CommonViewHolder h, Card item, int position) {
        //获取显示头像的控件
        SimpleDraweeView sdv = h.getView(R.id.iv_dynamic_icon);
        //设置显示的图片地址
        sdv.setImageURI(Uri.parse(item.getUser().getAvatarAddress()));

        //设置用户名
        h.setText(R.id.tv_dynamic_list_item_name,item.getUser().getName());

        //设置显示内容
        h.setText(R.id.tv_dynamic_list_item_content,item.getContent());

    }
}
