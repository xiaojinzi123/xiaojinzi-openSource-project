package com.example.cxj.huaishi.modular.imageSelect.popupWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.cxj.huaishi.R;
import com.example.cxj.huaishi.modular.imageSelect.ui.SelectLocalImageAct;

import java.util.List;

import xiaojinzi.EBus.EBus;
import xiaojinzi.base.android.adapter.listView.CommonAdapter;
import xiaojinzi.base.android.adapter.listView.CommonViewHolder;
import xiaojinzi.base.android.image.localImage.LocalImageInfo;
import xiaojinzi.base.android.image.localImage.LocalImageManager;
import xiaojinzi.base.java.util.ArrayUtil;

/**
 * Created by cxj on 2016/5/5.
 */
public class ListImageDirPopupWindow extends PopupWindow implements View.OnTouchListener {

    /**
     * 布局文件的最外层View
     */
    protected View mContentView;

    /**
     * 上下文对象
     */
    protected Context context;

    /**
     * 本地图片的信息
     */
    private LocalImageInfo localImageInfo;


    /**
     * ListView
     */
    private ListView lv = null;

    /**
     * 适配器
     */
    private BaseAdapter adapter;

    /**
     * 显示的数据
     */
    private List<String> data;

    /**
     * 系统中的图片个数
     */
    private int systemImageNum = 0;

    public ListImageDirPopupWindow(View contentView, int width, int height,
                                   boolean focusable, LocalImageInfo localImageInfo) {
        super(contentView, width, height, focusable);

        this.localImageInfo = localImageInfo;


        data = ArrayUtil.setToList(localImageInfo.getImageFolders());
        data.add(0, "System");

        //根布局
        this.mContentView = contentView;
        //上下文
        context = contentView.getContext();

        //设置popupWindow个几个属性
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        setTouchable(true);
        setOutsideTouchable(true);
        //触摸的拦截事件
        setTouchInterceptor(this);

        //初始化控件
        initView();

        initData();

        initEvent();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //发送纤细让Activity加载指定的文件夹中的图片
                EBus.postEvent(SelectLocalImageAct.TAG, "loadImageByFolderPath", data.get(position));
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //创建适配器
        adapter = new CommonAdapter<String>(context, data, R.layout.list_dir_item) {
            @Override
            public void convert(CommonViewHolder h, String item, int position) {
                //设置目录名称
                h.setText(R.id.id_dir_item_name, item);
                if (position == 0) { //第一个条目不是目录,特殊考虑
                    h.setText(R.id.id_dir_item_count, systemImageNum + "张");
                } else {
                    List<String> list = LocalImageManager.queryImageByFolderPath(localImageInfo,
                            item);
                    h.setText(R.id.id_dir_item_count, list.size() + "张");
                }
            }
        };
        //设置适配器
        lv.setAdapter(adapter);
    }

    /**
     * 通知数据改变
     */
    public void notifyDataSetChanged() {
        List<String> list = ArrayUtil.setToList(localImageInfo.getImageFolders());
        if (systemImageNum == 0) {
            systemImageNum = localImageInfo.getImageFiles().size();
        }
        data.clear();
        data.add("System");
        data.addAll(list);
        list = null;
        adapter.notifyDataSetChanged();
    }

    /**
     * 查找控件封装
     *
     * @param id 控件的id
     * @return
     */
    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        lv = (ListView) findViewById(R.id.lv_list_dir);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            dismiss();
            return true;
        }
        return false;
    }
}
