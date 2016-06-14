package com.example.cxj.huaishi.modular.imageSelect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.example.cxj.huaishi.R;

import java.util.List;

import xiaojinzi.base.android.adapter.recyclerView.CommonRecyclerViewAdapter;
import xiaojinzi.base.android.adapter.recyclerView.CommonRecyclerViewHolder;
import xiaojinzi.base.android.image.localImage.LocalImageLoader;
import xiaojinzi.base.android.os.ScreenUtils;

/**
 * Created by cxj on 2016/5/5.
 * 这个适配器是显示图片的
 */
public class ImageAdapter extends CommonRecyclerViewAdapter<String> {

    /**
     * 图片的宽度
     */
    private int imageWidth = 0;

    /**
     * 本地图片的信息
     */
    private List<String> imageFiles;

    /**
     * 记录每一张图是不是被选中
     */
    private List<Boolean> imageStates;

    /**
     * 选中的时候的覆盖的颜色
     */
    private int colorFilter = Color.parseColor("#88AAAAAA");

    /**
     * 构造函数
     *
     * @param context     上下文
     * @param imageStates
     */
    public ImageAdapter(Context context, List<String> imageFiles, List<Boolean> imageStates) {
        super(context, imageFiles);
        this.imageStates = imageStates;
        this.imageFiles = imageFiles;
        //初始化选中的集合
        initImageStates();
        //获取屏幕宽度
        int screenWidth = ScreenUtils.getScreenWidthPixels(context);
        //屏幕宽度的三分之一
        imageWidth = screenWidth / 3;
    }

    /**
     * 初始化选中的集合
     */
    private void initImageStates() {
        imageStates.clear();
        for (int i = 0; i < imageFiles.size(); i++) {
            imageStates.add(false);
        }
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, String entity, int position) {

        //获取控件
        ImageView iv = h.getView(R.id.iv);
        ImageButton ib = h.getView(R.id.item_select);
        //拿到布局参数对象
        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        //设置宽和高
        lp.width = imageWidth - h.itemView.getPaddingLeft() - h.itemView.getPaddingRight();
        lp.height = lp.width * 3 / 2;
        //设置默认图片
        iv.setImageResource(R.drawable.pictures_no);
        //如果是选中的状态
        if (imageStates.get(position)) {
            iv.setColorFilter(colorFilter);
            ib.setImageResource(R.drawable.picture_selected);
        } else {
            iv.setColorFilter(null);
            //ib.setImageResource(R.drawable.picture_unselected);
            ib.setImageDrawable(null);
        }

        //加载本地图片,自带压缩功能
        LocalImageLoader.getInstance().loadImage(entity, iv);
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item;
    }
}
