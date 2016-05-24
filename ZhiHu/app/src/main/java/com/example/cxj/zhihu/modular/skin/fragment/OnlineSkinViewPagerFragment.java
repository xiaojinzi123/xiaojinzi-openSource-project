package com.example.cxj.zhihu.modular.skin.fragment;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.modular.skin.entity.JsonSkin;
import com.example.cxj.zhihu.modular.skin.ui.OnlineSkinActivity;

import xiaojinzi.EBus.EBus;
import xiaojinzi.activity.fragment.BaseViewPagerFragment;
import xiaojinzi.annotation.Injection;
import xiaojinzi.imageLoad.ImageLoad;
import xiaojinzi.view.nineImage.NineImageView;


/**
 * Created by cxj on 2016/3/26.
 */
@SuppressLint("ValidFragment")
public class OnlineSkinViewPagerFragment extends BaseViewPagerFragment {

    /**
     * 要显示的数据
     */
    private JsonSkin jsonSkin = null;

    public OnlineSkinViewPagerFragment(JsonSkin jsonSkin) {
        this.jsonSkin = jsonSkin;
    }

    @Injection(R.id.nv_frag_online_skin)
    private NineImageView nineImageView = null;

    @Override
    public int getLayoutId() {
        return R.layout.frag_online_skin;
    }


    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        //设置加载网络的功能接口
        nineImageView.setOnLoadImageListener(new NineImageView.OnLoadImageListener() {
            @Override
            public void loadImage(ImageView imageView, String imageUrl) {
                ImageLoad.getInstance().asyncLoadImage(imageView, imageUrl);
            }
        });
        String imageUrls = jsonSkin.getImageUrl();
        String imageUrlArr[] = imageUrls.split(";");
        for (int i = 0; i < imageUrlArr.length; i++) {
            nineImageView.addImageUrl(Constant.Url.baseUrl + imageUrlArr[i], R.mipmap.loading);
        }
        //通知数据加载完毕
        notifyLoadDataComplete();
    }

    @Override
    public void setOnlistener() {
        EBus.postEvent(OnlineSkinActivity.TAG, Constant.eBus.myOnlineSkinAct.CHANGETITLE_FLAG, jsonSkin.getName());
    }

    @Override
    public void freshUI() {
    }
}
