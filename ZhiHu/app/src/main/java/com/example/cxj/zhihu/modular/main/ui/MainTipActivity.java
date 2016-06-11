package com.example.cxj.zhihu.modular.main.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;

import xiaojinzi.activity.BaseActivity;

import xiaojinzi.base.android.store.SPUtil;
import xiaojinzi.viewAnimation.AlphaAnimationUtil;
import xiaojinzi.viewAnimation.TranslateAnimationUtil;
import xiaojinzi.viewAnimation.adapter.AnimationListenerAdapter;
import xiaojinzi.viewAnnotation.Injection;


/**
 * 主界面的提示页面
 */
public class MainTipActivity extends BaseActivity {

    @Injection(value = R.id.rl_act_main_tip_container, click = "clickView")
    private RelativeLayout rl_container = null;

    @Injection(R.id.iv_act_main_tip_point)
    private ImageView iv = null;

    @Injection(R.id.tv_act_main_tip_text)
    private TextView tv_tip = null;

    /**
     * 是否点击过了
     */
    private boolean isClick = false;

    @Override
    public int getLayoutId() {
        return R.layout.act_main_tip;
    }

    @Override
    public void initView() {
        TranslateAnimation animation = TranslateAnimationUtil.translateSelf(0f, 0.5f, 0, 0,3000);
        animation.setRepeatMode(TranslateAnimation.RESTART);
        animation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(animation);
        tv_tip.startAnimation(animation);
    }

    /**
     * 点击时间的集中处理
     *
     * @param view
     */
    public void clickView(View view) {
        if (isClick) {
            return;
        }
        isClick = true;
        AlphaAnimationUtil.fillAfter = true;
        AlphaAnimation alphaAnimation = AlphaAnimationUtil.alphaToHide();
        alphaAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                rl_container.setAlpha(0f);
                finish();
                iv.clearAnimation();
                tv_tip.clearAnimation();
                rl_container.clearAnimation();
            }
        });
        rl_container.startAnimation(alphaAnimation);
    }

    @Override
    public void finish() {
        super.finish();
        //保存已经提示过的标识
        SPUtil.put(context, Constant.SP.mainAct.isShowMainTip, true);
    }
}
