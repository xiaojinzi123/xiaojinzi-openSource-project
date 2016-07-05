package com.yoursecondworld.secondworld.modular.userAgreement;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoursecondworld.secondworld.R;
import com.yoursecondworld.secondworld.common.BaseAct;

import java.io.IOException;
import java.io.InputStream;

import xiaojinzi.annotation.Injection;
import xiaojinzi.base.java.common.StringUtil;

/**
 * 协议界面
 */
public class UserAgreementAct extends BaseAct {

    @Injection(R.id.rl_act_titlebar)
    private RelativeLayout rl_titlebar = null;

    @Injection(value = R.id.iv_back, click = "clickView")
    private ImageView iv_back = null;

    @Injection(R.id.tv_act_user_agreement_content)
    private TextView tv_content = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_user_agreement;
    }

    @Override
    public void initView() {
        super.initView();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_titlebar.getLayoutParams();
        lp.topMargin = statusHeight;
    }

    @Override
    public void initData() {
        super.initData();
        InputStream is = getResources().openRawResource(R.raw.user_agreement);
        String content = "";
        try {
            content = StringUtil.isToStr(is,"UTF-8");
        } catch (IOException e) {
        }
        tv_content.setText(content);
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
