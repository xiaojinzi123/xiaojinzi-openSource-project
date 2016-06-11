package com.example.cxj.zhihu.modular.detail.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.modular.collection.db.MyCollectionDb;
import com.example.cxj.zhihu.modular.collection.entity.DbStory;
import com.example.cxj.zhihu.modular.collection.ui.MyCollectionActivity;
import com.example.cxj.zhihu.modular.comment.ui.CommentAct;
import com.example.cxj.zhihu.modular.detail.entity.StoryExtraInfo;
import com.example.cxj.zhihu.modular.detail.fragment.DetailFragment;
import com.example.cxj.zhihu.modular.main.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.EBus.EBus;
import xiaojinzi.activity.BaseFragmentActivity;

import xiaojinzi.base.android.activity.ActivityUtil;
import xiaojinzi.base.android.log.L;
import xiaojinzi.base.android.os.T;
import xiaojinzi.base.android.store.SPUtil;
import xiaojinzi.viewAnnotation.Injection;


/**
 * 故事详情页,要实现像知乎一样左右还能滑动,
 * 所以这里使用viewpager+fragment的方法
 * 但是都是异步加载数据,所以应该写一个BaseFragment来组织viewpager的预加载
 * 还有就是跳转过来的时候需要带过来需要显示的故事的id的数组,
 * 可以是所有的id用一个标识符连接的形式,如:5&6&8 这样子
 * 也可以传递数组过来,总之就是需要拿到要显示的故事详情的id的集合
 */
public class DetailActivity extends BaseFragmentActivity {

    /**
     * 类的标识
     */
    public static final String TAG = "DetailActivity";

    /**
     * 标题栏
     */
    @Injection(R.id.rl_act_detail_titlebar)
    private RelativeLayout rl_titlebar = null;

    /**
     * 返回的图标
     */
    @Injection(value = R.id.iv_act_detail_back, click = "viewClick")
    private ImageView iv_back = null;

    @Injection(value = R.id.iv_act_detail_collection, click = "viewClick")
    private ImageView iv_collection = null;

    @Injection(value = R.id.iv_act_detail_share, click = "viewClick")
    private ImageView iv_share = null;

    /**
     * viewpager用来显示不同故事详情
     */
    @Injection(R.id.vp_act_detail)
    private ViewPager vp = null;

    @Injection(value = R.id.iv_act_detail_message, click = "viewClick")
    private ImageView iv_comment = null;

    /**
     * 显示评论总数的TextView
     */
    @Injection(R.id.tv_act_detail_message)
    private TextView tv_comment = null;

    /**
     * 显示赞的总数的TextView
     */
    @Injection(R.id.tv_act_detail_zan)
    private TextView tv_zan = null;

    /**
     * viewpager使用的fragment的集合
     */
    private List<Fragment> fragments = new ArrayList<Fragment>();

    /**
     * 所有故事的id
     */
    private String[] ids = null;

    /**
     * 调过来之前用户点的是哪一个故事
     */
    private int position = 0;

    /**
     * 跳转到这个详情界面现在有三个地方:
     * 1.收藏界面
     * 2.homeFragment也就是主页
     * 3.themeFragment也就是主题日报的页面
     * 如果是收藏界面跳过来的时候,取消收藏是可以的,但是收藏就是不可以的了
     */
    private String from = null;

    /**
     * 额外的信息
     */
    private StoryExtraInfo storyExtraInfo;


    @Override
    public int getLayoutId() {
        return R.layout.act_detail;
    }

    /**
     * 标题栏的高度
     */
    private static int titleBarHeight = 0;

    @Override
    public void initView() {
        //如果是第一次使用,弹出提示的界面
        boolean bb = SPUtil.get(context, Constant.SP.detailAct.isShowDetailTip, false);
        if (!bb) {
            ActivityUtil.startActivity(this, DetailTipActivity.class);
        }
    }

    @Override
    public void initData() {
        loadStyle();
        //让标题栏去测量自己的宽和高
        rl_titlebar.measure(-1, -2);
        //获取测量后的高度
        titleBarHeight = rl_titlebar.getMeasuredHeight();

        //获取故事列表点击条目的时候带过来的数据
        position = getIntent().getIntExtra("position", 0);
        ids = getIntent().getStringExtra("ids").split("&");
        from = getIntent().getStringExtra("from");

        for (int i = 0; i < ids.length; i++) {
            String storyId = ids[i];
            fragments.add(new DetailFragment(storyId));
        }
        //设置viewpager的适配器
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        vp.setCurrentItem(position);
    }

    @Override
    public void setOnlistener() {
        //监听viewpager的选中状态
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                onEventChangeTitleBarOpacity(0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * view控件的点击事件
     *
     * @param v
     */
    public void viewClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_act_detail_back) {
            finish();
        } else if (id == R.id.iv_act_detail_collection) {
            try {
                int storyId = Integer.parseInt(ids[vp.getCurrentItem()]);
                DbStory dbStory = MyCollectionDb.getInstance(context).queryByStoryId(storyId);
                if (dbStory == null) {
                    if (MyCollectionActivity.TAG.equals(from)) {
                        T.showShort(context, "请在主页和日报界面收藏文章!");
                    } else {
                        EBus.postEvent(Constant.REQUESTCOLLECTSTORY_FLAG, storyId);
                    }
                } else {
                    MyCollectionDb.getInstance(context).delete(dbStory);
                    EBus.postEvent(DetailActivity.TAG, Constant.CHANGECOLLECTICON_FLAG, false);
                    T.showShort(context, "取消收藏成功");
                }
                //叫收藏界面更新数据
                EBus.postEvent(MyCollectionActivity.TAG, Constant.eBus.myCollectionAct.FRESHCOLLECTDATA_FLAG);
            } catch (Exception e) {
                T.showShort(context, "操作失败~~");
            }
        } else if (id == R.id.iv_act_detail_message) {
            //需要跳转到显示评论的界面
            Intent i = new Intent(this, CommentAct.class);
            i.putExtra(Constant.STORY_FLAG, ids[vp.getCurrentItem()]);
            i.putExtra("comments", tv_comment.getText().toString());
            startActivity(i);
        } else if (id == R.id.iv_act_detail_share) { //分享的按钮
            //发送通知,让fragment分享一下
            EBus.postEvent(DetailFragment.tag, Constant.eBus.detailFragment.SHAERURL_FLAG, ids[vp.getCurrentItem()]);
        }
    }

    /**
     * 设置故事详情的额外信息,采用Ebus跨区调用
     *
     * @param storyExtraInfo 故事的额外信息
     */
    public void onEventSetStoryExtraInfo(StoryExtraInfo storyExtraInfo) {
        this.storyExtraInfo = storyExtraInfo;
        tv_comment.setText(storyExtraInfo.getComments());
        tv_zan.setText(storyExtraInfo.getPopularity());
    }

    /**
     * 加载样式
     */
    private void loadStyle() {
        rl_titlebar.setBackgroundColor(MyApp.skin.detailActSkin.getTitleBarBgColor());
        vp.setBackgroundColor(MyApp.skin.detailActSkin.getViewPagerBgColor());
    }

    /**
     * 改变透明度,由EBus跨区域调用
     *
     * @param top
     */
    public void onEventChangeTitleBarOpacity(Integer top) {
        float percent = top / new Float(2 * titleBarHeight);
        if (percent > 1) {
            rl_titlebar.setAlpha(0);
            if (rl_titlebar.getVisibility() != View.GONE) {
                rl_titlebar.setVisibility(View.GONE);
            }
        } else {
            if (rl_titlebar.getVisibility() != View.VISIBLE) {
                rl_titlebar.setVisibility(View.VISIBLE);
            }
            rl_titlebar.setAlpha(1 - percent);
        }

    }

    /**
     * 根据是否已经收藏显示收藏的图片样式
     *
     * @param isCollect
     */
    public void onEventChangeCollectIcon(Boolean isCollect) {
        if (isCollect) {
            iv_collection.setImageResource(R.mipmap.act_detail_collectioned);
        } else {
            iv_collection.setImageResource(R.mipmap.act_detail_no_collection);
        }
    }

    /**
     * 获取标题栏的高度
     *
     * @return
     */
    public static int getTitleBarHeight() {
        return titleBarHeight;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
