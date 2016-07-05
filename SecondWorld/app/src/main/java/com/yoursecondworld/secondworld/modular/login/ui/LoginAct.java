package com.yoursecondworld.secondworld.modular.login.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoursecondworld.secondworld.R;
import com.yoursecondworld.secondworld.common.BaseAct;
import com.yoursecondworld.secondworld.modular.forgetPassword.ForgetPasswordAct;

import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.activity.ActivityUtil;

/**
 * 登陆界面
 */
public class LoginAct extends BaseAct {

    @Injection(R.id.rl_act_titlebar)
    private RelativeLayout rl_titlebar = null;

    @Injection(value = R.id.iv_back, click = "clickView")
    private ImageView iv_back = null;

    @Injection(value = R.id.tv_act_login_forget_password,click = "clickView")
    private TextView tv_forgetPassword = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    public void initView() {
        super.initView();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_titlebar.getLayoutParams();
        lp.topMargin = statusHeight;
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
            case R.id.tv_act_login_forget_password:

                ActivityUtil.startActivity(context, ForgetPasswordAct.class);

                break;
        }

    }

    @Override
    public boolean isRegisterEvent() {
        return false;
    }
}
