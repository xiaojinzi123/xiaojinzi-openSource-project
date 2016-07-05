package com.yoursecondworld.secondworld.modular.register.ui;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoursecondworld.secondworld.R;
import com.yoursecondworld.secondworld.common.BaseAct;
import com.yoursecondworld.secondworld.modular.userAgreement.UserAgreementAct;

import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.activity.ActivityUtil;

/**
 * 注册的页面
 */
public class RegisterAct extends BaseAct {

    @Injection(R.id.rl_act_titlebar)
    private RelativeLayout rl_titlebar = null;

    @Injection(value = R.id.iv_back, click = "clickView")
    private ImageView iv_back = null;

    @Injection(value = R.id.tv_act_register_user_agreement,click = "clickView")
    private TextView tv_userAgreement = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_register;
    }

    @Override
    public void initView() {
        super.initView();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_titlebar.getLayoutParams();
        lp.topMargin = statusHeight;

        //给用户协议几个字加粗
        tv_userAgreement.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        tv_userAgreement.getPaint().setFakeBoldText(true);//加粗
        tv_userAgreement.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        tv_userAgreement.getPaint().setAntiAlias(true);//抗锯齿

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
            case R.id.tv_act_register_user_agreement:

                ActivityUtil.startActivity(context, UserAgreementAct.class);

                break;
        }

    }

    @Override
    public boolean isRegisterEvent() {
        return false;
    }

}
