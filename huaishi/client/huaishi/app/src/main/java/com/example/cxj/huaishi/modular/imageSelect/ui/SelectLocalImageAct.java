package com.example.cxj.huaishi.modular.imageSelect.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.cxj.huaishi.R;
import com.example.cxj.huaishi.modular.imageSelect.adapter.ImageAdapter;
import com.example.cxj.huaishi.modular.imageSelect.entity.MessageDataHolder;
import com.example.cxj.huaishi.modular.imageSelect.popupWindow.ListImageDirPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xiaojinzi.EBus.EBus;
import xiaojinzi.base.android.adapter.recyclerView.CommonRecyclerViewAdapter;
import xiaojinzi.base.android.image.localImage.LocalImageInfo;
import xiaojinzi.base.android.image.localImage.LocalImageManager;
import xiaojinzi.base.android.os.ScreenUtils;
import xiaojinzi.base.java.util.ThreadPool;


/**
 * 选择本地图片的activity
 */
public class SelectLocalImageAct extends Activity implements View.OnClickListener, CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener, Runnable {

    /**
     * 类的标识
     */
    public static final String TAG = "SelectLocalImageAct";

    /**
     * activity带回去的数据的标识
     */
    public static final String RETURN_DATA_FLAG = "data";

    /**
     * 结果码
     */
    public static final int RESULT_CODE = 666;


    private RecyclerView rv = null;

    /**
     * PopupWindow
     */
    private ListImageDirPopupWindow listImageDirPopupWindow;

    /**
     * 返回图标
     */
    private ImageView iv_back;

    /**
     * 确定
     */
    private TextView tv_ok;

    /**
     * 底部的控件
     */
    private LinearLayout ll_info = null;


    /**
     * 显示文件夹名称的控件
     */
    private TextView tv_folderName;


    /**
     * 显示文件夹中图片文件的个数
     */
    private TextView tv_imageNumber;

    /**
     * 适配器
     */
    private CommonRecyclerViewAdapter<String> adapter;

    /**
     * 本地图片的信息
     */
    private LocalImageInfo localImageInfo = new LocalImageInfo(new String[]{LocalImageManager.PNG_MIME_TYPE,
            LocalImageManager.JPEG_MIME_TYPE,
            LocalImageManager.JPG_MIME_TYPE});


    /**
     * 显示的数据u
     */
    private List<String> data = new ArrayList<String>();

    /**
     * 记录图片是不是被选中,利用下标进行关联
     */
    private List<Boolean> imageStates = new ArrayList<Boolean>();

    /**
     * 上下文
     */
    private Context context;


    private Handler h = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            MessageDataHolder m = (MessageDataHolder) msg.obj;

            //设置底部的信息
            tv_folderName.setText(m.folderName.length() > 12 ? m.folderName.substring(0, 12) + "..." : m.folderName);
            tv_imageNumber.setText(m.imageNum + "张");

            //初始化选中状态的记录集合
            imageStates.clear();
            for (int i = 0; i < localImageInfo.getImageFiles().size(); i++) {
                imageStates.add(false);
            }

            data.clear();
            data.addAll(localImageInfo.getImageFiles());

            //关闭弹出窗口
            listImageDirPopupWindow.dismiss();
            setBackAlpha(false);

            //通知数据改变
            adapter.notifyDataSetChanged();
            listImageDirPopupWindow.notifyDataSetChanged();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_image);

        EBus.register(TAG, this);

        //初始化控件
        initView();

        //初始化事件
        initEvent();

        //线程池执行任务
        ThreadPool.getInstance().invoke(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EBus.unRegister(TAG);
    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {

        iv_back.setOnClickListener(this);
        tv_ok.setOnClickListener(this);

        //设置底部菜单的监听
        ll_info.setOnClickListener(this);

        //设置ReCyclerView的条目监听
        adapter.setOnRecyclerViewItemClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        context = this;

        //寻找一些控件
        iv_back = (ImageView) findViewById(R.id.iv_act_main_back);
        tv_ok = (TextView) findViewById(R.id.tv_act_main_ok);

        ll_info = (LinearLayout) findViewById(R.id.ll_act_main_info);
        tv_folderName = (TextView) findViewById(R.id.tv_act_main_image_folder_name);
        tv_imageNumber = (TextView) findViewById(R.id.tv_act_main_image_number);

        rv = (RecyclerView) findViewById(R.id.rv);
        //创建适配器
        adapter = new ImageAdapter(context, data, imageStates);
        //设置适配器
        rv.setAdapter(adapter);

        rv.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));

        //初始化弹出窗口
        initPopuWindow();

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_act_main_info:
                if (listImageDirPopupWindow != null) {
                    listImageDirPopupWindow
                            .setAnimationStyle(R.style.anim_popup_dir);
                    listImageDirPopupWindow.showAsDropDown(ll_info, 0, 0);
                }
                // 设置背景颜色变暗
                setBackAlpha(true);
                break;
            case R.id.iv_act_main_back:
                finish();
                break;
            case R.id.tv_act_main_ok:
                Intent i = new Intent();
                i.putExtra(RETURN_DATA_FLAG, getSelectImages());
                setResult(RESULT_CODE, i);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        imageStates.set(position, !imageStates.get(position));
        adapter.notifyDataSetChanged();
    }

    /**
     * 根据文件夹的路径,进行加载图片
     *
     * @param folderPath
     */
    public void onEventLoadImageByFolderPath(final String folderPath) {
        if ("System".equals(folderPath)) {
            //线程池执行任务
            ThreadPool.getInstance().invoke(this);
            return;
        }
        File folder = new File(folderPath);
        //文件夹存在并且是一个目录
        if (folder.exists() && folder.isDirectory()) {
            ThreadPool.getInstance().invoke(new Runnable() {
                @Override
                public void run() {
                    List<String> tmpData = LocalImageManager.queryImageByFolderPath(localImageInfo, folderPath);
                    localImageInfo.getImageFiles().clear();
                    localImageInfo.getImageFiles().addAll(tmpData);
                    //发送消息
                    h.sendMessage(MessageDataHolder.obtain(folderPath, localImageInfo.getImageFiles().size()));
                }
            });

        }
    }

    @Override
    public void run() {

        //初始化本地图片的管理者
        LocalImageManager.init(context);

        //获取本地系统图片的信息,并且整理文件夹
        LocalImageManager.
                queryImageWithFolder(localImageInfo);

        //发送消息
        h.sendMessage(MessageDataHolder.obtain("所有文件", localImageInfo.getImageFiles().size()));
    }

    /**
     * 初始化弹出的窗口
     */
    private void initPopuWindow() {
        //初始化弹出框
        View contentView = View.inflate(context, R.layout.list_dir, null);
        //创建要弹出的popupWindow
        listImageDirPopupWindow = new ListImageDirPopupWindow(contentView,
                ScreenUtils.getScreenWidthPixels(context),
                ScreenUtils.getScreenHeightPixels(context) * 2 / 3,
                true, localImageInfo);

        //消失的时候监听
        listImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackAlpha(false);
            }
        });
    }

    /**
     * 设置窗体的透明度,根据PopupWindow是否打开
     *
     * @param isOpen
     */
    private void setBackAlpha(boolean isOpen) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (isOpen) {
            lp.alpha = .3f;
        } else {
            lp.alpha = 1.0f;
        }
        getWindow().setAttributes(lp);
    }

    /**
     * 获取被选中的图片的数组
     *
     * @return
     */
    private String[] getSelectImages() {
        List<String> tmp = new ArrayList<String>();
        for (int i = 0; i < imageStates.size(); i++) {
            if (imageStates.get(i)) {
                tmp.add(localImageInfo.getImageFiles().get(i));
            }
        }
        String[] arr = new String[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            arr[i] = tmp.get(i);
        }
        tmp = null;
        return arr;
    }

}
