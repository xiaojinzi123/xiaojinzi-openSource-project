package com.example.cxj.zhihu.modular.detail.ui;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;

import xiaojinzi.activity.BaseActivity;

import xiaojinzi.base.android.store.SPUtil;
import xiaojinzi.viewAnimation.AlphaAnimationUtil;
import xiaojinzi.viewAnimation.adapter.AnimationListenerAdapter;
import xiaojinzi.viewAnnotation.Injection;


public class DetailTipActivity extends BaseActivity {

    @Injection(value = R.id.rl_act_detail_tip_container, click = "clickView")
    private RelativeLayout rl_container = null;

    /**
     * 是否点击过了
     */
    private boolean isClick = false;

    @Override
    public int getLayoutId() {
        return R.layout.act_detail_tip;
    }

    /**
     * 点击事件的集中处理
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
                rl_container.clearAnimation();
                finish();
            }
        });
        rl_container.startAnimation(alphaAnimation);
    }

    @Override
    public void finish() {
        super.finish();
        //保存已经提示过的标识,下次不再提示
        SPUtil.put(context, Constant.SP.detailAct.isShowDetailTip, true);
    }
}
