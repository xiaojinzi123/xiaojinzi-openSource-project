package com.example.cxj.zhihu.modular.main.theme.fragment;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.modular.collection.db.MyCollectionDb;
import com.example.cxj.zhihu.modular.collection.entity.DbStory;
import com.example.cxj.zhihu.modular.detail.ui.DetailActivity;
import com.example.cxj.zhihu.modular.main.entity.Theme;
import com.example.cxj.zhihu.modular.main.home.entity.Story;
import com.example.cxj.zhihu.modular.main.theme.adapter.ThemeFragmentAdapter;
import com.example.cxj.zhihu.modular.main.theme.entity.ThemeContent;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.EBus.EBus;
import xiaojinzi.animation.RotateAnimationUtil;
import xiaojinzi.annotation.Injection;
import xiaojinzi.annotation.ViewInjectionUtil;
import xiaojinzi.base.android.log.L;
import xiaojinzi.base.android.os.ProgressDialogUtil;
import xiaojinzi.base.android.os.T;
import xiaojinzi.imageLoad.ImageLoad;
import xiaojinzi.json.android.JsonUtil;
import xiaojinzi.net.adapter.BaseDataHandlerAdapter;


/**
 * Created by cxj on 2016/1/26.
 * 显示主题日报的fragment,
 * 这个fragment挂在在主界面上
 */
public class ThemeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 创建这个fragment的时候需要传递一个主题的id
     *
     * @param theme
     */
    public ThemeFragment(Theme theme) {
        this.theme = theme;
    }

    /**
     * 类的标识
     */
    private String tag = "ThemeFragment";

    private android.os.Handler h = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
        }
    };

    @Injection(R.id.ll_frag_theme_container)
    private LinearLayout ll_container = null;

    //android原生的下拉刷新控件
    @Injection(R.id.sr_frag_theme)
    private SwipeRefreshLayout sr = null;

    @Injection(value = R.id.iv_frag_theme_menu, click = "popupMenu")
    private ImageView iv_menu = null;

    @Injection(R.id.rl_frag_theme_titlebar)
    private RelativeLayout rl_titleBar = null;

    @Injection(R.id.tv_frag_theme_title)
    private TextView tv_title = null;

    /**
     * 显示主题下面的故事的ListView
     */
    @Injection(R.id.lv_frag_theme)
    private ListView lv = null;

    /**
     * 进度条对话框
     */
    private ProgressDialog dialog = null;

    /**
     * 显示数据的adapter
     */
    private BaseAdapter adapter = null;

    /**
     * 这个fragment要显示的主题的id
     */
//    private Integer themeId;
    private Theme theme = null;


    /**
     * 要显示的数据
     */
    private ThemeContent themeContent;

    /**
     * 是否切换了fragment,这个数值指的是是不是进行了HomeFragment和ThemeFragment之间的切换
     */
    private boolean isChangeFragment = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_theme, null);
        //让注解起作用
        ViewInjectionUtil.injectView(this, v);

        //注册自己可以接受消息
        EBus.register(this);

        initData();

        setOnListener();

        return v;
    }

    /**
     * 设置各种监听
     */
    private void setOnListener() {
        //设置下拉刷新控件的监听
        sr.setOnRefreshListener(this);
        //设置listview的条目点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String allIds = getAllStoryId();
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra("ids", allIds);
                i.putExtra("position", position - lv.getHeaderViewsCount());
                getActivity().startActivity(i);
            }
        });

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
                        getBeforThemes();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }

    /**
     * 初始化数据
     */
    private void initData() {

        //弹出对话框
        popupDialog();

        //加载样式
        loadStyle();

        //设置标题栏的文本
        tv_title.setText(theme.getName());

        //加载数据
        MyApp.ah.getWithoutJsonCache(Constant.Url.MainAct.themesUrl + theme.getId(), new BaseDataHandlerAdapter() {
            @Override
            public void handler(String data) throws Exception {
                //转化json数据为实体对象
                themeContent = JsonUtil.createObjectFromJson(ThemeContent.class, data);
                //创建显示数据的适配器
                adapter = new ThemeFragmentAdapter(getActivity(), themeContent.getStories(), R.layout.story_item);
                //添加listview的头部
                loadAdvTag();
                //挂在适配器到listview上面
                lv.setAdapter(adapter);
                closeDialog();
            }

            @Override
            public void error(Exception e) {
                closeDialog();
                L.s(tag, "根据主题id获取主题失败");
            }
        });
    }

    /**
     * 添加listview的头部
     */
    private void loadAdvTag() {
        //拿到listview头部要显示的视图
        View view_adv = View.inflate(getActivity(), R.layout.frag_theme_adv, null);
        //找到大图控件
        ImageView iv_image = (ImageView) view_adv.findViewById(R.id.iv_frag_theme_adv_image);
        ImageLoad.getInstance().asyncLoadImage(iv_image,themeContent.getBackground());
        //找到显示标题的TextView
        TextView tv_advContent = (TextView) view_adv.findViewById(R.id.tv_frag_theme_adv_content);
        //拿到描述
        String description = themeContent.getDescription();
        tv_advContent.setText(description);
        lv.addHeaderView(view_adv);
    }

    /**
     * 获取过往的主题日报
     */
    private void getBeforThemes() {
        //弹出对话框
        popupDialog();
        List<Story> stories = themeContent.getStories();
        Story story = stories.get(stories.size() - 1);
        //加载数据
        MyApp.ah.getWithoutJsonCache(Constant.Url.MainAct.themesUrl + theme.getId() + "/before/" + story.getId(), new BaseDataHandlerAdapter() {
            @Override
            public void handler(String data) throws Exception {
                ThemeContent tmpThemeContent = JsonUtil.createObjectFromJson(ThemeContent.class, data);
                themeContent.getStories().addAll(tmpThemeContent.getStories());
                adapter.notifyDataSetChanged();
                //关闭对话框
                closeDialog();
            }

            @Override
            public void error(Exception e) {
                closeDialog();
                L.s(tag, "加载更多主题日报失败");
            }
        });
    }

    /**
     * 弹出菜单
     *
     * @param v
     */
    public void popupMenu(View v) {
        int id = v.getId();
        if (id == R.id.iv_frag_theme_menu) {
            //弹出菜单
            EBus.postEvent("popupMenu");
        }
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
    }

    /**
     * 抽屉式菜单收回的时候调用这个方法,通过EBus跨区域调用
     */
    public void onEventMenuOpened() {
        RotateAnimationUtil.fillAfter = true;
        RotateAnimationUtil.defaultDuration = 500;
        RotateAnimationUtil.rotateSelf(iv_menu, 0, -90);
        isChangeFragment = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //当销毁的时候反注册自己,在EBus中取消注册
        EBus.unRegister(this);
    }


    @Override
    public void onRefresh() {

        popupDialog();

        //加载数据
        MyApp.ah.getWithoutJsonCache(Constant.Url.MainAct.themesUrl + theme.getId(), new BaseDataHandlerAdapter() {
            @Override
            public void handler(String data) throws Exception {
                //转化json数据为实体对象
                ThemeContent tmpThemeContent = JsonUtil.createObjectFromJson(ThemeContent.class, data);
                if (themeContent == null) {
                    //说明第一次进来的时候是失败的
                    themeContent = tmpThemeContent;
                    //创建显示数据的适配器
                    adapter = new ThemeFragmentAdapter(getActivity(), themeContent.getStories(), R.layout.story_item);
                    //加载listview头部
                    loadAdvTag();
                    //挂在适配器到listview上面
                    lv.setAdapter(adapter);
                } else {
                    //清空原来的集合
                    themeContent.getStories().clear();
                    //添加新的数据到原来的集合
                    themeContent.getStories().addAll(tmpThemeContent.getStories());
                    //通知listview数据发生改变
                    adapter.notifyDataSetChanged();
                }

                closeDialog();
                sr.setRefreshing(false);
            }

            @Override
            public void error(Exception e) {
                closeDialog();
                sr.setRefreshing(false);
                L.s(tag, "根据主题id获取主题失败");
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
     * 获取所有的id,用&标识符连接起来
     *
     * @return
     */
    private String getAllStoryId() {
        StringBuffer sb = new StringBuffer();
        List<Story> stories = themeContent.getStories();
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
     * 加载样式
     */
    private void loadStyle() {
        ll_container.setBackgroundColor(MyApp.skin.themeFragmentSkin.getContainerBgColor());
        rl_titleBar.setBackgroundColor(MyApp.skin.themeFragmentSkin.getTitleBarBgColor());
        lv.setBackgroundColor(MyApp.skin.themeFragmentSkin.getListBgColor());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 通过EventBus跨区域调用,达到切换的效果
     */
    public void onEventChangeMode() {
        loadStyle();
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
//        T.showShort(getActivity(), "收到:" + storyId);
        //循环所有的故事找到故事id符合的那个
        List<Story> stories = themeContent.getStories();
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
                    dbStory.setImage(images == null || images.size() == 0 ? story.getImage() : images.get(0));
                    dbStory.setTitle(story.getTitle());
                    dbStory.setType(story.getType());
                    MyCollectionDb.getInstance(getActivity()).insert(dbStory);
                    EBus.postEvent(DetailActivity.TAG, Constant.CHANGECOLLECTICON_FLAG, true);
                    T.showShort(getActivity(), "在日报界面收藏成功");
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
