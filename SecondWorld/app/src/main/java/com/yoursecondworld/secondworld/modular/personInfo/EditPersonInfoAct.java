package com.yoursecondworld.secondworld.modular.personInfo;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yoursecondworld.secondworld.R;
import com.yoursecondworld.secondworld.common.BaseAct;

import xiaojinzi.annotation.Injection;

/**
 * 编辑个人信息的界面
 */
public class EditPersonInfoAct extends BaseAct {

    @Injection(R.id.rl_act_edit_person_titlebar)
    private RelativeLayout rl_titlebar = null;

    @Injection(value = R.id.iv_back, click = "clickView")
    private ImageView iv_back = null;


    @Injection(R.id.iv_act_edit_person_info_icon)
    private SimpleDraweeView simpleDraweeView = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_edit_person_info;
    }

    @Override
    public void initView() {
        super.initView();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_titlebar.getLayoutParams();
        lp.topMargin = statusHeight;

        //设置图片的地址
        simpleDraweeView.setImageURI(Uri.parse("http://c.hiphotos.baidu.com/image/h%3D360/sign=93c0e40e6509c93d18f208f1af3cf" +
                "8bb/aa64034f78f0f736f514e2010855b319eac413c3.jpg"));

    }

    /**
     * 点击事件的集中处理
     *
     * @param v
     */
    public void clickView(View v) {

        //获取控件的id
        int id = v.getId();

        //对id的筛选然后做处理
        switch (id) {
            case R.id.iv_back:
                finish();
                break;
        }

    }

    @Override
    public boolean isRegisterEvent() {
        return false;
    }
}
