package com.example.cxj.zhihu.modular.detail.fragment;

import android.view.View;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.modular.collection.db.MyCollectionDb;
import com.example.cxj.zhihu.modular.collection.entity.DbStory;
import com.example.cxj.zhihu.modular.detail.entity.DetailStory;
import com.example.cxj.zhihu.modular.detail.entity.StoryExtraInfo;
import com.example.cxj.zhihu.modular.detail.ui.DetailActivity;
import com.example.cxj.zhihu.modular.detail.view.MyScrollView;

import xiaojinzi.EBus.EBus;
import xiaojinzi.activity.fragment.BaseViewPagerFragment;
import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.os.ScreenUtils;
import xiaojinzi.base.android.os.SystemUtil;
import xiaojinzi.imageLoad.ImageLoad;
import xiaojinzi.json.android.JsonUtil;
import xiaojinzi.net.adapter.BaseDataHandlerAdapter;


/**
 * Created by cxj on 2016/1/22.
 * 显示一个故事详情的fragment
 */
public class DetailFragment extends BaseViewPagerFragment {

    /**
     * 类的标识
     */
    public static final String tag = "DetailFragment";

    /**
     * 离线加载的网页内容要加上的一些网页源码
     */
    private String codePrefixOne = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" +
            "<html>" +
            "<head>" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\">" +
            "<style type=\"text/css\">";

    //这个和上个变量之间可以写css代码
    private String codePrefixTwo = "</style><script type=\"text/javascript\">";

    //这个和上个变量之间可以写js代码
    private String codePrefixThree = "</script></head><body>";
    private String codeSubfix = "</body></html>";

    /**
     * 屏幕的宽度
     */
    private int screenWidth = 0;

    /**
     * 第一段js脚本
     */
    private String jsCodeOne = "";

    /**
     * 故事的id
     */
    private String storyId = null;

    @Injection(R.id.sv_frag_detail_container)
    private MyScrollView sv = null;

    @Injection(R.id.iv_frag_detail_image)
    private ImageView iv_image = null;

    @Injection(R.id.wv_frag_detail)
    private WebView myWebView = null;

    /**
     * 显示的数据实体
     */
    private DetailStory detailStory;

    /**
     * 故事的额外的信息
     */
    private StoryExtraInfo storyExtraInfo;


    public DetailFragment() {
    }

    /**
     * 构造函数
     *
     * @param storyId 传进来一个故事的id
     */
    public DetailFragment(String storyId) {
        this.storyId = storyId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_detail;
    }

    @Override
    public void initView() {
        //得到屏幕的宽度
        screenWidth = ScreenUtils.getScreenWidth(getActivity());
        jsCodeOne = "window.onload = function (){" +
                "var imgs = document.getElementsByTagName(\"img\");" +
                "for(var i = 0; i < imgs.length; i++){" +
                "var img = imgs[i];" +
                "if(img.width > " + screenWidth + "){" +
                "img.width = " + screenWidth + ";" +
                "}" +
                "}" +
                "}";
    }

    /**
     * 加载数据
     */
    @Override
    public void initData() {

        loadStyle();

        //根据故事id加载故事的详情
        MyApp.ah.get(Constant.Url.detailAct.detailStoryUrl + storyId, new BaseDataHandlerAdapter() {
            @Override
            public void handler(String data) throws Exception {
                //转化json数据为故事详情的实体对象
                detailStory = JsonUtil.createObjectFromJson(DetailStory.class, data);
                if (detailStory.getImage() != null && !"".equals(detailStory.getImage())) {
                    iv_image.setVisibility(View.VISIBLE);
                    //异步加载上面的大图
                    ImageLoad.getInstance().asyncLoadImage(iv_image, detailStory.getImage());
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv_image.getLayoutParams();
                    lp.topMargin = DetailActivity.getTitleBarHeight();
                } else {
                    //有些故事是没有大图的,所以直接隐藏就可以了
                    iv_image.setVisibility(View.GONE);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) myWebView.getLayoutParams();
                    lp.topMargin = DetailActivity.getTitleBarHeight();
                }
                WebSettings settings = myWebView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                String webData = codePrefixOne +
                        "*{margin:0px;padding:0px;color:" + MyApp.skin.detailFragmentSkin.getWebViewTextColor() +
                        ";}body{word-wrap:break-word;font-family:Arial;width:" + ScreenUtils.getScreenWidth(context) + "px;}" +
                        codePrefixTwo + jsCodeOne
                        + codePrefixThree + detailStory.getBody() + codeSubfix;
                myWebView.loadDataWithBaseURL(null, webData, "text/html", "UTF-8", null);
                //初始化额外的信息
                initExtraInfo();
            }

            @Override
            public void error(Exception e) {
                closeDialog();
            }
        });


    }

    private int count = 0;

    private boolean flag = false;

    @Override
    public void setOnlistener() {
        sv.setOnScrollChange(new MyScrollView.OnScrollChange() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                int dt = Math.abs(t - oldt);
                //有些详情界面是没有上面的大图显示的,所以需要判断
                if (iv_image.getVisibility() == View.VISIBLE) {
                    android.widget.LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) myWebView.getLayoutParams();
                    lp.topMargin = -t / 2;
                    ViewParent parent = myWebView.getParent();
                    if (parent != null) {
                        parent.requestLayout();
                    }
                }
                //发送改变标题栏透明度的通知,并且携带top过去
                EBus.postEvent(DetailActivity.TAG, Constant.eBus.detailFragment.CHANGETITLEBAROPACITY_FLAG, t);
                int height = iv_image.getHeight();
                if (t == height + DetailActivity.getTitleBarHeight() && oldt == 0) {
                    sv.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * 加载样式
     */
    private void loadStyle() {
        myWebView.setBackgroundColor(MyApp.skin.detailFragmentSkin.getWebViewBgColor());
    }

    /**
     * 获取额外的信息
     */
    private void initExtraInfo() {
        //获取详情页额外的信息,比如点赞数目,评论数量
        MyApp.ah.getWithoutJsonCache(Constant.Url.detailAct.extraStoryInfoUrl + storyId, new BaseDataHandlerAdapter() {
            @Override
            public void handler(String data) throws Exception {
                storyExtraInfo = JsonUtil.createObjectFromJson(StoryExtraInfo.class, data);
                //通知数据加载完毕
                notifyLoadDataComplete();
            }

            @Override
            public void error(Exception e) {
                closeDialog();
            }
        });
    }


    @Override
    public void freshUI() {
        EBus.register(this);
        //让试图滚到最上面
        sv.smoothScrollTo(0, 0);
        //如果故事的额外信息已经获取
        if (storyExtraInfo != null) {
            //发送一个事件
            EBus.postEvent(storyExtraInfo);
        }
        DbStory dbStory = MyCollectionDb.getInstance(context).queryByStoryId(Integer.parseInt(storyId));
        if (dbStory == null) {
            EBus.postEvent(DetailActivity.TAG, Constant.CHANGECOLLECTICON_FLAG, false);
        } else {
            EBus.postEvent(DetailActivity.TAG, Constant.CHANGECOLLECTICON_FLAG, true);
        }
    }

    @Override
    public boolean isPopupDialog() {
        return false;
    }

    @Override
    public boolean isAutoSubscribeEvent() {
        return false;
    }

    /**
     * 接受别的地方发送的通知
     */
    public void onEventShareUrl(String storyId) {
        if (this.storyId.equals(storyId)) {
            //使用系统自带的分享功能
            SystemUtil.shareMsg(context,
                    "分享", "提示", "我在阅读知乎日报,发现一个有趣的故事,特意跑来与您分享\n" + detailStory.getShare_url(), null);
        }
    }

}
