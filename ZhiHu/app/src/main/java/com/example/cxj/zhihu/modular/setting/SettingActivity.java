package com.example.cxj.zhihu.modular.setting;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.common.skin.Skin;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.modular.skin.ui.MySkinActivity;
import com.example.cxj.zhihu.modular.skin.ui.OnlineSkinActivity;
import com.example.cxj.zhihu.service.AutoChangeSkinService;

import xiaojinzi.activity.BaseActivity;
import xiaojinzi.animation.TranslateAnimationUtil;
import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.activity.ActivityUtil;
import xiaojinzi.base.android.store.SPUtil;


/**
 * 设置的界面,最好弄一个通用的设置界面,供以后使用
 */
public class SettingActivity extends BaseActivity {

    @Injection(R.id.cb_act_setting_divider_load_image_mode)
    private CheckBox cb_loadImageMode = null;

    @Injection(R.id.rl_act_setting_titlebar)
    private RelativeLayout rl_titleBar = null;

    @Injection(R.id.sv_act_setting_container)
    private ScrollView sv_container = null;

    @Injection(R.id.tv_act_setting_routine)
    private TextView tv_routine = null;

    @Injection(R.id.tv_act_setting_skin)
    private TextView tv_skin = null;

    @Injection(R.id.tv_act_setting_aboat_app)
    private TextView tv_aboatApp = null;

    @Injection(R.id.tv_act_setting_aboat_author)
    private TextView tv_aboatAuthor = null;

    @Injection(R.id.tv_act_setting_app_version)
    private TextView tv_appVersion = null;

    @Injection(R.id.tv_act_setting_author_qq)
    private TextView tv_authorQQ = null;

    @Injection(R.id.tv_act_setting_author_email)
    private TextView tv_authorEmail = null;

    @Injection(R.id.tv_act_setting_str_one)
    private TextView tv_strOne = null;

    @Injection(R.id.tv_act_setting_str_two)
    private TextView tv_strTwo = null;

    @Injection(R.id.tv_act_setting_str_three)
    private TextView tv_strThree = null;

    @Injection(R.id.tv_act_setting_str_four)
    private TextView tv_strFour = null;

    @Injection(R.id.tv_act_setting_str_six)
    private TextView tv_strFive = null;

    @Injection(R.id.tv_act_setting_str_serven)
    private TextView tv_strSix = null;

    @Injection(R.id.tv_act_setting_str_eight)
    private TextView tv_strServen = null;

    @Injection(R.id.tv_act_setting_str_nine)
    private TextView tv_strEight = null;

    @Injection(R.id.tv_act_setting_str_five)
    private TextView tv_strNine = null;

    @Injection(R.id.iv_act_setting_online_skin)
    private ImageView iv_onlineSkin = null;

    @Injection(R.id.iv_act_setting_my_skin)
    private ImageView iv_mySkin = null;

    @Injection(R.id.iv_act_setting_author_blog)
    private ImageView iv_authorBlog = null;

    @Injection(R.id.ll_act_setting_content)
    private LinearLayout ll_content = null;

    @Injection(value = R.id.iv_act_setting_back, click = "clickView")
    private ImageView iv_back = null;

    @Injection(value = R.id.rl_act_setting_online_skin, click = "clickView")
    private RelativeLayout rl_onlineSkin = null;

    @Injection(value = R.id.rl_act_setting_myskin, click = "clickView")
    private RelativeLayout rl_mySkin = null;

    @Injection(value = R.id.rl_act_setting_aboat_author_blog, click = "clickView")
    private RelativeLayout rl_blog = null;

    @Injection(R.id.sw_act_setting_autochange_skin)
    private Switch aSwitch = null;

    private Intent service;

    @Override
    public int getLayoutId() {
        return R.layout.act_setting;
    }

    @Override
    public void initView() {
        service = new Intent(context, AutoChangeSkinService.class);
        loadStyle();
        startAnimation();
    }

    @Override
    public void initData() {
        loadSpData();
    }

    @Override
    public void setOnlistener() {

        cb_loadImageMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SPUtil.put(context, Constant.SP.settingAct.loadImageMode, Constant.loadImageWithWifi);
                    MyApp.loadImageModeData = Constant.loadImageWithWifi;
                } else {
                    SPUtil.put(context, Constant.SP.settingAct.loadImageMode, Constant.loadImageWithAll);
                    MyApp.loadImageModeData = Constant.loadImageWithAll;
                }
            }
        });

        //设置开关按钮的状态接口
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //把用户选择的数值保存起来
                SPUtil.put(context, Constant.SP.settingAct.isAutoChangeSkin, isChecked);
                if (isChecked) {
                    context.startService(service);
                    MyApp.isAutoChangeSkin = true;
                } else {
                    context.stopService(service);
                    MyApp.isAutoChangeSkin = false;
                }
            }
        });
    }

    public void clickView(View v) {
        int id = v.getId();
        if (id == R.id.iv_act_setting_back) {
            finish();
        } else if (id == R.id.rl_act_setting_online_skin) {
            ActivityUtil.startActivity(this, OnlineSkinActivity.class);
        } else if (id == R.id.rl_act_setting_myskin) {
            ActivityUtil.startActivity(this, MySkinActivity.class);
        } else if (id == R.id.rl_act_setting_aboat_author_blog) {
            String url = "http://blog.csdn.net/u011692041"; // web address
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    /**
     * 加载样式
     */
    private void loadStyle() {
        sv_container.setBackgroundColor(MyApp.skin.settingActSkin.getContainerBgColor());
        rl_titleBar.setBackgroundColor(MyApp.skin.settingActSkin.getTitleBarBgColor());
        tv_routine.setTextColor(MyApp.skin.settingActSkin.getItemTagTextColor());
        tv_skin.setTextColor(MyApp.skin.settingActSkin.getItemTagTextColor());
        tv_aboatApp.setTextColor(MyApp.skin.settingActSkin.getItemTagTextColor());
        tv_aboatAuthor.setTextColor(MyApp.skin.settingActSkin.getItemTagTextColor());
        tv_appVersion.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_authorQQ.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_authorEmail.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_strOne.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_strTwo.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_strThree.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_strFour.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_strFive.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_strSix.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_strServen.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_strEight.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        tv_strNine.setTextColor(MyApp.skin.settingActSkin.getItemTextColor());
        if (MyApp.skin.getEnvironment() == Skin.DAY_ENVIRONMENT) {
            iv_onlineSkin.setImageResource(R.mipmap.act_settting_arrow_right_day);
            iv_mySkin.setImageResource(R.mipmap.act_settting_arrow_right_day);
            iv_authorBlog.setImageResource(R.mipmap.act_settting_arrow_right_day);
        } else if (MyApp.skin.getEnvironment() == Skin.NIGHT_ENVIRONMENT) {
            iv_onlineSkin.setImageResource(R.mipmap.act_settting_arrow_right_night);
            iv_mySkin.setImageResource(R.mipmap.act_settting_arrow_right_night);
            iv_authorBlog.setImageResource(R.mipmap.act_settting_arrow_right_night);
        }

    }

    /**
     * 开始视觉动画
     */
    private void startAnimation() {
        for (int i = 0; i < ll_content.getChildCount(); i++) {
            View view = ll_content.getChildAt(i);
            TranslateAnimation translateAnimation = TranslateAnimationUtil.translateSelf(i % 2 == 0 ? 1 : -1, 0, 0, 0);
            translateAnimation.setStartOffset(i * 20);
            view.startAnimation(translateAnimation);
        }
    }

    /**
     * 加载轻量级存储中的数据回显
     */
    private void loadSpData() {
        boolean isAutoChangeSkin = SPUtil.get(context, Constant.SP.settingAct.isAutoChangeSkin, false);
        aSwitch.setChecked(isAutoChangeSkin);
        int loadImageMode = SPUtil.get(context, Constant.SP.settingAct.loadImageMode, Constant.loadImageWithAll);
        if (loadImageMode == (Constant.loadImageWithWifi)) {
            cb_loadImageMode.setChecked(true);
        }
    }

    /**
     * 更换皮肤
     */
    public void onEventChangeMode() {
        loadStyle();
    }

}
