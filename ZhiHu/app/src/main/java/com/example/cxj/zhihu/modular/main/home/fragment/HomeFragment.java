package com.example.cxj.zhihu.modular.main.home.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.modular.collection.db.MyCollectionDb;
import com.example.cxj.zhihu.modular.collection.entity.DbStory;
import com.example.cxj.zhihu.modular.detail.ui.DetailActivity;
import com.example.cxj.zhihu.modular.main.home.adapter.HomeFragmentAdapter;
import com.example.cxj.zhihu.modular.main.home.entity.LatestStories;
import com.example.cxj.zhihu.modular.main.home.entity.Story;
import com.example.cxj.zhihu.modular.setting.SettingActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xiaojinzi.EBus.EBus;

import xiaojinzi.base.android.activity.ActivityUtil;
import xiaojinzi.base.android.os.ProgressDialogUtil;
import xiaojinzi.base.android.os.T;

import xiaojinzi.base.java.common.DateUtil;
import xiaojinzi.imageLoad.ImageLoad;
import xiaojinzi.json.android.JsonUtil;

import xiaojinzi.net.adapter.ResponseHandlerAdapter;
import xiaojinzi.net.filter.PdHttpRequest;
import xiaojinzi.view.advView.AdvView;
import xiaojinzi.viewAnimation.RotateAnimationUtil;
import xiaojinzi.viewAnnotation.Injection;
import xiaojinzi.viewAnnotation.ViewInjectionUtil;


/**
 * Created by cxj on 2016/1/20.
 * 主页的fragment
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 类的标识
     */
    private String tag = "HomeFragment";

    private android.os.Handler h = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
        }
    };

    @Injection(R.id.rl_frag_home_container)
    private RelativeLayout rl_container = null;

    /**
     * 控件的点击事件触发的方法名称是一个叫"onClick"的方法,方法必须修饰为public
     */
    @Injection(value = R.id.iv_frag_home_menu, click = "clickView")
    private ImageView iv_menu = null;

    @Injection(R.id.rl_frag_home_titlebar)
    private RelativeLayout rl_titleBar = null;

    @Injection(R.id.tv_frag_home_title)
    private TextView tv_title = null;

    //android原生的下拉刷新控件
    @Injection(R.id.sr_frag_home)
    private SwipeRefreshLayout sr = null;

    //显示内容的ListView
    @Injection(R.id.lv_frag_home)
    private ListView lv = null;

    @Injection(value = R.id.iv_frag_home_setting, click = "clickView")
    private ImageView iv_setting = null;

    /**
     * 进度条对话框
     */
    private ProgressDialog dialog = null;

    /**
     * 要展示的数据
     */
    private LatestStories latestStories = null;

    /**
     * 显示数据的适配器
     */
    private BaseAdapter adapter = null;

    /**
     * 日期格式化的工具类
     */
    private DateUtil d = new DateUtil("yyyyMMdd");
    private DateUtil dd = new DateUtil("MM月dd日 E");

    /**
     * 主页fragment显示的试图
     */
    private View viewContent = null;

    /**
     * 轮播图控制
     */
    private AdvView av = null;

    /**
     * 在轮播图上面的文字
     */
    private TextView tv_advContent = null;

    /**
     * 是否切换了fragment,这个数值指的是是不是进行了HomeFragment和ThemeFragment之间的切换
     */
    private boolean isChangeFragment = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (viewContent == null) {
            viewContent = inflater.inflate(R.layout.frag_home, null);
            //让注解起作用
            ViewInjectionUtil.injectView(this, viewContent);
        }

        //注册自己
        EBus.register(this);

        if (latestStories == null) {
            initData();
        }

        setOnListener();

        return viewContent;
    }

    /**
     * 设置各种监听
     */
    private void setOnListener() {
        //设置下拉刷新的监听
        sr.setOnRefreshListener(this);
        //设置下拉刷新的一个颜色的资源
        sr.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        //设置listview的滚动事件
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //弹出进度条对话框
                        popupDialog();
                        getBeforNews();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (latestStories == null) {
                    return;
                }
                Story story = latestStories.getStories().get(firstVisibleItem);
                if (story.isTag()) {
                    tv_title.setText(story.getDate());
                }
            }
        });
        //设置listview的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - lv.getHeaderViewsCount();
                if (position < 0) {
                    return;
                }
                Story story = latestStories.getStories().get(position);
                if (!story.isTag()) {

                    //记录这个条目已经点击
                    story.setIsClick(true);
                    adapter.notifyDataSetChanged();

                    //跳转到详情的界面
                    String ids = getAllStoryId(false);
                    Intent i = new Intent(getActivity(), DetailActivity.class);
                    i.putExtra("ids", ids);
                    i.putExtra("position", getRealPosition(position));
                    getActivity().startActivity(i);
                }
            }
        });

    }

    /**
     * 弹出对话框
     */
    private void popupDialog() {
        if (dialog == null) {
            dialog = ProgressDialogUtil.show(getActivity(), ProgressDialog.STYLE_SPINNER, false);
        } else {
            dialog.show();
        }
    }

    /**
     * 关闭对话框,延迟关闭,效果更佳
     */
    private void closeDialog() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                h.sendEmptyMessage(666);
            }
        }.start();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        //加载不同模式下的样式
        loadStyle();

        //加载最新的数据
        loadLastData();


    }

    /**
     * 加载最新的数据
     */
    private void loadLastData() {

        //弹出对话框
        popupDialog();

        //获取最新的数据
        PdHttpRequest httpRequest = new PdHttpRequest(Constant.Url.MainAct.latestUrl);

        httpRequest.setResponseHandler(new ResponseHandlerAdapter() {
            @Override
            public void error(Exception e, Object[] p) {
                //让进度条对话框消失
                closeDialog();
                sr.setRefreshing(false);
            }

            @Override
            public void handler(String data, Object[] p) throws Exception {
                //转化json数据为实体对象
                latestStories = JsonUtil.createObjectFromJson(LatestStories.class, data);
                Story s = new Story();
                s.setIsTag(true);
                s.setDate("今日热闻");
                //添加一个头
                latestStories.getStories().add(0, s);
                //创建适配器
                adapter = new HomeFragmentAdapter(getActivity(), latestStories.getStories(), R.layout.story_item);

                //加载轮播图
                loadAdvTag();

                //设置listview的适配器
                lv.setAdapter(adapter);

                getBeforNewsTimes = 0;

                //让进度条对话框消失
                closeDialog();
                sr.setRefreshing(false);
            }
        });

        MyApp.ah.send(httpRequest);
    }

    /**
     * 轮番图使用的fragment集合
     */
    private List<Fragment> advFragments = new ArrayList<Fragment>();

    /**
     * 加载轮播图的试图,添加到listview的头部
     */
    private void loadAdvTag() {
        //拿到listview头部要显示的视图
        View view_adv = View.inflate(getActivity(), R.layout.frag_home_adv, null);
        //找到广告控件
        av = (AdvView) view_adv.findViewById(R.id.vp_frag_home_adv);
        //设置广告控件的监听
        addAdvViewListener();
        //设置指示器的圆点半径
        av.setIndicatorRadius(10);
        //找到显示标题的TextView
        tv_advContent = (TextView) view_adv.findViewById(R.id.tv_frag_home_adv_content);
        //拿到故事
        final List<Story> top_stories = latestStories.getTop_stories();
        //设置显示的标题
        tv_advContent.setText(top_stories.size() == 0 ? "" : top_stories.get(0).getTitle());
        //添加故事中的每个故事的图片到广告控件中
        for (int i = 0; i < top_stories.size(); i++) {
            Story story = top_stories.get(i);
            av.addImageUrl(story.getImage(), R.mipmap.loading);
        }
        lv.addHeaderView(view_adv);
        av.startAutoChange();
    }

    /**
     * 设置有光广告控件的监听
     */
    private void addAdvViewListener() {
        final List<Story> top_stories = latestStories.getTop_stories();
        //设置广告控件的加载图片监听
        av.setOnLoadImageListener(new AdvView.OnLoadImageListener() {
            @Override
            public void loadImage(ImageView imageView, String imageUrl) {
                ImageLoad.getInstance().asyncLoadImage(imageView, imageUrl);
            }
        });
        //设置广告控件中图片选择的监听器
        av.setOnSelectionListener(new AdvView.OnSelectionListener() {
            @Override
            public void onSelect(int index) {
                tv_advContent.setText(top_stories.get(index).getTitle());
            }
        });
        //设置广告控件中的图片的点击事件
        av.setOnImageClickListener(new AdvView.OnImageClickListener() {
            @Override
            public void imageClick(int index) {
                //跳转到详情的界面
                String ids = getAllStoryId(true);
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra("ids", ids);
                i.putExtra("position", index);
                getActivity().startActivity(i);
            }
        });
        //监听广告控件的滑动状态
        av.setOnMoveStateListener(new AdvView.OnMoveStateListener() {
            @Override
            public void onMoveState(int state) {
                if (state == AdvView.MOVING) {
                    sr.setEnabled(false);
                } else {
                    sr.setEnabled(true);
                }
            }
        });
    }

    /**
     * 控件的点击事件的处理的方法
     *
     * @param v
     */
    public void clickView(View v) {
        int id = v.getId();
        if (id == R.id.iv_frag_home_menu) {
            //弹出菜单
            EBus.postEvent(Constant.POPUPMENU_FLAG);
        } else if (id == R.id.iv_frag_home_setting) {
            ActivityUtil.startActivity(getActivity(), SettingActivity.class);
        }
    }

    @Override
    public void onRefresh() {

        if (latestStories == null) { //表示数据没有加载过,或者数据加载失败了
            loadLastData();
            return;
        }

        //弹出对话框
        popupDialog();

        //获取最新的数据
        PdHttpRequest httpRequest = new PdHttpRequest(Constant.Url.MainAct.latestUrl);

        httpRequest.setResponseHandler(new ResponseHandlerAdapter() {
            @Override
            public void handler(String data, Object[] p) throws Exception {
                //转化json数据为实体对象
                LatestStories tmpLatestStories = JsonUtil.createObjectFromJson(LatestStories.class, data);
                //更换原有对象中的数据
                latestStories.getStories().clear();
                Story s = new Story();
                s.setIsTag(true);
                s.setDate("今日热闻");
                //添加一个头
                latestStories.getStories().add(s);
                latestStories.getStories().addAll(tmpLatestStories.getStories());
                adapter.notifyDataSetChanged();
                getBeforNewsTimes = 0;
                //让进度条对话框消失
                closeDialog();
                sr.setRefreshing(false);
                T.showLong(getActivity(), "刷新成功");
            }

            @Override
            public void error(Exception e, Object[] p) {
                //让进度条对话框消失
                closeDialog();
                sr.setRefreshing(false);
            }
        });

        MyApp.ah.send(httpRequest);

    }

    /**
     * 获取过往消息的次数,下拉刷新之后这个数值会被重置
     */
    private int getBeforNewsTimes = 0;

    /**
     * 获取过往消息内容添加到现在的集合中,实现上拉加载更多的功能
     */
    private void getBeforNews() {
        //得到20160122这样子格式的日期
        String date = d.formatDate(new Date(System.currentTimeMillis() - getBeforNewsTimes * DateUtil.DAYTIMEMILLIS));
        //然后请求网络家在更多
        PdHttpRequest httpRequest = new PdHttpRequest(Constant.Url.MainAct.beforeNewsUrl + date);
        httpRequest.setResponseHandler(new ResponseHandlerAdapter() {
            @Override
            public void handler(String data, Object[] p) throws Exception {
                //转化为实体对象
                LatestStories tmpLatestStories = JsonUtil.createObjectFromJson(LatestStories.class, data);
                Story s = new Story();
                s.setIsTag(true);
                Date date = d.parse(tmpLatestStories.getDate());
                s.setDate(dd.formatDate(date));
                //添加一个头
                latestStories.getStories().add(s);
                latestStories.getStories().addAll(tmpLatestStories.getStories());
                adapter.notifyDataSetChanged();
                getBeforNewsTimes++;
                closeDialog();
            }

            @Override
            public void error(Exception e, Object[] p) {
                closeDialog();
                getBeforNewsTimes++;
                T.showLong(getActivity(), "获取过往数据失败");
            }
        });
        MyApp.ah.send(httpRequest);
    }

    /**
     * 获取所有的id,用&标识符连接起来
     *
     * @param isTopStories 是不是广告推荐中的故事
     * @return
     */
    private String getAllStoryId(boolean isTopStories) {
        StringBuffer sb = new StringBuffer();
        List<Story> stories = null;
        if (isTopStories) {
            stories = latestStories.getTop_stories();
        } else {
            stories = latestStories.getStories();
        }
        for (int i = 0; i < stories.size(); i++) {
            Story story = stories.get(i);
            if (!story.isTag()) {
                sb.append(story.getId() + "&");
            }
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    /**
     * 获取用户点击的真正的故事下标,除去用户点的故事之前的头
     *
     * @param position
     * @return
     */
    private int getRealPosition(int position) {
        int tagNumber = 0;
        List<Story> stories = latestStories.getStories();
        for (int i = 0; i < position; i++) {
            Story story = stories.get(i);
            if (story.isTag()) {
                tagNumber++;
            }
        }
        return position - tagNumber;
    }

    /**
     * 关闭菜单的动画的执行,通过EBus跨区域调用
     */
    public void onEventMenuClosed() {
        if (isChangeFragment) {
            return;
        }
        RotateAnimationUtil.fillAfter = true;
        RotateAnimationUtil.defaultDuration = 500;
        RotateAnimationUtil.rotateSelf(iv_menu, -90, 0);
        if (av != null) {
            av.setCurrentImage(av.getCurrIndex());
        }
    }

    /**
     * 抽屉式菜单收回的时候调用这个方法,通过EBus跨区域调用
     */
    public void onEventMenuOpened() {
        RotateAnimationUtil.fillAfter = true;
        RotateAnimationUtil.defaultDuration = 500;
        RotateAnimationUtil.rotateSelf(iv_menu, 0, -90);
        if (av != null) {
            av.setCurrentImage(av.getCurrIndex());
        }
        isChangeFragment = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStyle();
        if (av != null) {
            av.startAutoChange();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EBus.unRegister(this);
        if (av != null) {
            av.stopAutoChange();
            av.exit();
        }
    }

    /**
     * 通过EventBus跨区域调用,达到切换的效果
     */
    public void onEventChangeMode() {
        loadStyle();
    }

    /**
     * 加载显示的模式:
     * 1.白天模式
     * 2.夜晚模式
     */
    private void loadStyle() {
        rl_container.setBackgroundColor(MyApp.skin.homeFragmentSkin.getContainerBgColor());
        rl_titleBar.setBackgroundColor(MyApp.skin.homeFragmentSkin.getTitleBarBgColor());
        lv.setBackgroundColor(MyApp.skin.homeFragmentSkin.getListBgColor());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * listview滚动套最上面
     */
    public void onEventGoToTop() {
        lv.smoothScrollToPosition(0);
    }

    /**
     * 详情页请求缓存一个故事
     *
     * @param storyId
     */
    public void onEventRequestCollectStory(Integer storyId) {
        List<Story> stories = latestStories.getStories();
        for (int i = 0; i < stories.size(); i++) {
            Story story = stories.get(i);
            if (!story.isTag() && storyId.equals(story.getId())) {
                DbStory dbStory = MyCollectionDb.getInstance(getActivity()).queryByStoryId(storyId);
                if (dbStory == null) {
                    dbStory = new DbStory();
                    dbStory.setId(story.getId());
                    dbStory.setDate(story.getDate());
                    dbStory.setGa_prefix(story.getGa_prefix());
                    List<String> images = story.getImages();
                    dbStory.setImage(images.size() == 0 ? null : images.get(0));
                    dbStory.setTitle(story.getTitle());
                    dbStory.setType(story.getType());
                    MyCollectionDb.getInstance(getActivity()).insert(dbStory);
                    EBus.postEvent(DetailActivity.TAG, Constant.CHANGECOLLECTICON_FLAG, true);
                    T.showShort(getActivity(), "收藏成功");
                } else {
                    T.showShort(getActivity(), "已收藏");
                }
                break;
            }
        }
    }

    public void setIsChangeFragment(boolean isChangeFragment) {
        this.isChangeFragment = isChangeFragment;
    }
}
