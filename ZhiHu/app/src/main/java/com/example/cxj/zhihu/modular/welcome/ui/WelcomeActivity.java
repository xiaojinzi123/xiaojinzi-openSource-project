package com.example.cxj.zhihu.modular.welcome.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.modular.main.ui.MainActivity;
import com.example.cxj.zhihu.modular.welcome.entity.StartImage;

import java.util.Random;

import xiaojinzi.activity.BaseActivity;
import xiaojinzi.animation.AlphaAnimationUtil;
import xiaojinzi.animation.ScaleAnimationUtil;
import xiaojinzi.animation.adapter.AnimationListenerAdapter;
import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.activity.ActivityUtil;
import xiaojinzi.imageLoad.ImageLoad;
import xiaojinzi.json.android.JsonUtil;
import xiaojinzi.net.adapter.BaseDataHandlerAdapter;


/**
 * 欢迎的界面
 */
public class WelcomeActivity extends BaseActivity {

    /**
     * 类的标识
     */
    private String tag = "WelcomeActivity";

    /**
     * 友情提醒在欢迎界面的文字,随机显示其中的一个
     */
    private String[] tipText = {"一个人的快乐,不是因为他拥有的多,而是因为他计较的少",
            "择善人而交,择善书而读,择善言而听,择善行而从",
            "生气,就是拿别人的过错来惩罚自己.原谅别人,就是善待自己",
            "未必钱多乐便多,财多累己招烦恼.清贫乐道真自在，无牵无挂乐逍遥",
            "处事不必求功,无过便是功.为人不必感德,无怨便是德",
            "平安是幸,知足是福,清心是禄,寡欲是寿",
            "人之心胸,多欲则窄,寡欲则宽",
            "宁可清贫自乐,不可浊富多忧",
            "受思深处宜先退,得意浓时便可休",
            "势不可使尽,福不可享尽,便宜不可占尽,聪明不可用尽",
            "滴水穿石,不是力量大,而是功夫深",
            "平生不做皱眉事,世上应无切齿人",
            "须交有道之人,莫结无义之友.饮清静之茶,莫贪花色之酒.开方便之门,闲是非之口",
            "多门之室生风,多言之人生祸",
            "世事忙忙如水流，休将名利挂心头。粗茶淡饭随缘过，富贵荣华莫强求",
            "\"我欲\"是贫穷的标志,事能常足,心常惬,人到无求品自高",
            "人生至恶是善谈人过；人生至愚恶闻己过"};

    @Injection(R.id.iv_act_welcome)
    private ImageView iv_startImage = null;

    @Injection(R.id.tv_act_welcome_tip)
    private TextView tv_tip = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_welcome;
    }

    @Override
    public void initView() {
        Random r = new Random();
        int index = r.nextInt(tipText.length);
        tv_tip.setText(tipText[index]);
    }

    @Override
    public void initData() {

        //获取启动图片的信息
        MyApp.ah.getWithoutJsonCache(Constant.Url.WelcomeAct.startImage, new BaseDataHandlerAdapter() {
            @Override
            public void handler(String data) throws Exception {
                //完成json数据和实体对象的转化
                StartImage startImage = JsonUtil.createObjectFromJson(StartImage.class, data);
                //加载启动图片,并监听
                ImageLoad.getInstance().asyncLoadImage(iv_startImage, startImage.getImg(), null, true, true, new ImageLoad.OnResultListener() {
                    @Override
                    public void onResult(boolean isSuccess) {
                        scale();
                    }
                });
            }

            @Override
            public void error(Exception e) {
                scale();
            }
        });
    }

    /**
     * 实现图片的缩放,并完成跳转
     */
    private void scale() {
        ScaleAnimationUtil.fillAfter = true;
        ScaleAnimationUtil.defaultDuration = 3000;
        ScaleAnimation scaleAnimation = ScaleAnimationUtil.scaleSelf(1f, 1.2f, 1f, 1.2f);
        scaleAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                iv_startImage.setScaleX(1.2f);
                iv_startImage.setScaleY(1.2f);
                //开始透明动画
                alpha();
            }
        });
        iv_startImage.startAnimation(scaleAnimation);
    }

    /**
     * 透明化
     */
    private void alpha() {
        AlphaAnimationUtil.fillAfter = true;
        AlphaAnimationUtil.defaultDuration = 1200;
        AlphaAnimation alphaAnimation = AlphaAnimationUtil.alphaToHide();
        alphaAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                //跳转到主界面
                ActivityUtil.startActivity(context, MainActivity.class);
                //结束欢迎界面
                finish();
            }
        });
        iv_startImage.startAnimation(alphaAnimation);
    }


    @Override
    public void setOnlistener() {
        super.setOnlistener();
    }
}
