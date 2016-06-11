package com.example.cxj.zhihu.modular.collection.adapter;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.modular.collection.entity.DbStory;
import com.example.cxj.zhihu.modular.collection.entity.TmpImfo;
import com.example.cxj.zhihu.modular.collection.ui.MyCollectionActivity;

import java.util.List;

import xiaojinzi.EBus.EBus;
import xiaojinzi.base.android.adapter.listView.CommonAdapter;
import xiaojinzi.base.android.adapter.listView.CommonViewHolder;
import xiaojinzi.imageLoad.ImageLoad;
import xiaojinzi.viewAnimation.TranslateAnimationUtil;
import xiaojinzi.viewAnimation.adapter.AnimationListenerAdapter;


/**
 * Created by cxj on 2016/4/1.
 */
public class MyCollectionActAdapter extends CommonAdapter<DbStory> {

    /**
     * 构造函数
     *
     * @param context   上下文
     * @param data      显示的数据
     * @param layout_id listview使用的条目的布局文件id
     */
    public MyCollectionActAdapter(Context context, List<DbStory> data, int layout_id) {
        super(context, data, layout_id);
    }

    @Override
    public void convert(CommonViewHolder h, DbStory item, int position) {

        View convertView = h.getConvertView();

        TextView tv_title = h.getView(R.id.tv_story_item_title);
        ImageView iv_image = h.getView(R.id.iv_story_item_icon);
        ImageView iv_delete = h.getView(R.id.iv_horizontal_story_item_delete);

        iv_delete.setOnClickListener(new OnListenerAdapter(position, convertView));

        //设置标题
        tv_title.setText(item.getTitle());
        tv_title.setTextColor(MyApp.skin.myCollectionActSkin.getItemTextColor());
        //加载图片
        if (item.getImage() != null && !"".equals(item.getImage())) {
            ImageLoad.getInstance().asyncLoadImage(iv_image, item.getImage());
        }

    }

    private class OnListenerAdapter implements View.OnClickListener {

        private int position = -1;

        private View convertView = null;

        public OnListenerAdapter(int position, View convertView) {
            this.position = position;
            this.convertView = convertView;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.iv_horizontal_story_item_delete) {
                TranslateAnimationUtil.fillAfter = false;
                TranslateAnimation translateAnimation = TranslateAnimationUtil.translateSelf(0f, 1f, 0f, 0f);
                translateAnimation.setAnimationListener(new AnimationListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        EBus.postEvent(MyCollectionActivity.TAG, Constant.eBus.myCollectionAct.CANCELCOLLECT_FLAG, position);
                    }
                });
                convertView.startAnimation(translateAnimation);
            }
        }
    }

}
