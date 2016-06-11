package com.example.cxj.zhihu.modular.main.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.common.adapter.MyDrawerListener;
import com.example.cxj.zhihu.common.skin.Skin;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.modular.collection.ui.MyCollectionActivity;
import com.example.cxj.zhihu.modular.main.adapter.MenuAdapter;
import com.example.cxj.zhihu.modular.main.entity.Theme;
import com.example.cxj.zhihu.modular.main.entity.ThemeList;
import com.example.cxj.zhihu.modular.main.home.fragment.HomeFragment;
import com.example.cxj.zhihu.modular.main.theme.fragment.ThemeFragment;
import com.example.cxj.zhihu.modular.setting.SettingActivity;
import com.example.cxj.zhihu.service.AutoChangeSkinService;

import java.util.List;

import xiaojinzi.EBus.EBus;
import xiaojinzi.activity.BaseFragmentActivity;

import xiaojinzi.base.android.activity.ActivityUtil;
import xiaojinzi.base.android.os.T;
import xiaojinzi.base.android.store.SPUtil;
import xiaojinzi.json.android.JsonUtil;

import xiaojinzi.net.adapter.ResponseHandlerAdapter;
import xiaojinzi.net.filter.PdHttpRequest;
import xiaojinzi.view.popupMenu.SmartMenu;
import xiaojinzi.view.scaleSlideMenu.ScaleSlideMenu;
import xiaojinzi.viewAnnotation.Injection;


/**
 * 主界面,同时也负责加载左边的菜单中的信息
 */
public class MainActivity extends BaseFragmentActivity implements SmartMenu.OnMenuCloseListener, View.OnClickListener {

    /**
     * 类的标识
     */
    public static final String TAG = "MainActivity";

    /**
     * 自定义侧滑菜单
     */
    @Injection(R.id.ssm_act_main)
    private ScaleSlideMenu ssm = null;

    //=======================菜单中的部分================================
    @Injection(R.id.lv_act_main_menu)
    private ListView lv_themes = null;

    /**
     * 菜单的适配器
     */
    private MenuAdapter adapter;

    /**
     * 菜单部分的容器
     */
    @Injection(R.id.ll_act_main_menu)
    private LinearLayout ll_menu = null;

    /**
     * 右下角的弹出式小控件
     */
    @Injection(R.id.sm_act_main_body_menu)
    private SmartMenu sm = null;

    /**
     * 显示现在皮肤模式的文本
     */
    @Injection(R.id.tv_act_main_body_mode_textcontent)
    private TextView tv_mode = null;

    /**
     * 显示现在的皮肤模式的图标
     */
    @Injection(R.id.iv_act_main_body_mode_icon)
    private ImageView iv_mode = null;

    /**
     * 主页的fragment
     */
    private HomeFragment homeFragment = null;

    /**
     * 左边菜单的数据
     */
    private ThemeList themeList = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initView() {
        loadStyle();
    }

    @Override
    public void initData() {
        //切换至主页
        changeToHomeFragment();
        //加载提示页面
        boolean isShowMainTip = SPUtil.get(context, Constant.SP.mainAct.isShowMainTip, false);
        if (!isShowMainTip) {
            ActivityUtil.startActivity(context, MainTipActivity.class);
        }
        //根据皮肤的模式改变对应的文本和图标
        changeShowTextAndIcon();
        //加载左边的菜单数据
        loadLeftMenuData();
    }

    /**
     * 加载左边的菜单数据
     */
    private void loadLeftMenuData() {

        //获取菜单中的主题日报的列表
        PdHttpRequest httpRequest = new PdHttpRequest(Constant.Url.MainAct.themesListUrl);
        httpRequest.setResponseHandler(new ResponseHandlerAdapter(){
            @Override
            public void handler(String data, Object[] p) throws Exception {
                //转化json数据为实体对象
                themeList = JsonUtil.createObjectFromJson(ThemeList.class, data);
                //创建菜单的适配器
                adapter = new MenuAdapter(context, themeList.getOthers(), R.layout.act_main_menu_item);
                //把头布局文件转化成View对象
                addMenuListHeaderView();
                //设置左边listview的适配器
                lv_themes.setAdapter(adapter);
            }
        });
        MyApp.ah.send(httpRequest);
    }

    /**
     * 添加左边菜单的头,这个不是异步加载的,所以可以先显示出来的,在没有网络的情况下
     */
    private void addMenuListHeaderView() {
        //菜单listview的头
        View tagHead = View.inflate(context, R.layout.act_main_menu_tag, null);
        //设置listview背景
//        lv_themes.setBackgroundColor(MyApp.skin.memuSkin.getListBgColor());
        //获取头部的几个能点的控件的容器,包括:头像图标和请登录文字,收藏图标和我的收藏文字,下载图标和离线下载文字
        RelativeLayout rl_tag_option = (RelativeLayout) tagHead.findViewById(R.id.rl_act_main_menu_tag_option);
        //设置头部背景
//        rl_tag_option.setBackgroundColor(MyApp.skin.memuSkin.getTitleBarBgColor());
        //获取首页条目的布局
        LinearLayout ll_home = (LinearLayout) tagHead.findViewById(R.id.ll_act_main_menu_tag_home);
        //设置首页独特的背景
//        ll_home.setBackgroundColor(MyApp.skin.memuSkin.getHomeItemBgColor());
        //设置点击主页条目的点击事件
        ll_home.setOnClickListener(this);
        //获取我的收藏图标和文字控件
        ImageView iv_collection = (ImageView) tagHead.findViewById(R.id.iv_act_main_menu_collect_tag_icon);
        TextView tv_collection = (TextView) tagHead.findViewById(R.id.tv_act_main_menu_collect_tag_text);
        //设置收藏的点击事件
        iv_collection.setOnClickListener(this);
        tv_collection.setOnClickListener(this);
        //把整个头部的试图加入到listview上面去
        lv_themes.addHeaderView(tagHead);
    }

    @Override
    public void setOnlistener() {

        //设置侧滑菜单的状态
        ssm.setOnMenuStateListener(new ScaleSlideMenu.OnMenuStateListener() {
            @Override
            public void onMenuState(boolean state) {
                if (state) {
                    if (themeList == null) { //菜单完全打开的时候,如果菜单的数据没有加载,那么就去加载一下
                        loadLeftMenuData();
                    }
                    EBus.postEvent(Constant.MENUOPENED_FLAG);
                }else{
                    EBus.postEvent(Constant.MENUCLOSED_FLAG);
                }
            }
        });

        /**
         * 设置左边菜单的条目点击事件
         */
        lv_themes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || themeList == null || themeList.getOthers() == null || themeList.getOthers().size() < 1) {
                    return;
                }
                Theme theme = themeList.getOthers().get(position - lv_themes.getHeaderViewsCount());
                changeFragment(new ThemeFragment(theme));
                ssm.closeMenu();
            }
        });

        //自定义弹出菜单关闭的时候监听
        sm.setOnMenuCloseListener(this);

    }

    /**
     * 切换到主页
     */
    private void changeToHomeFragment() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        changeFragment(homeFragment);
    }

    /**
     * 根据皮肤的模式,对此界面加载样式
     */
    private void loadStyle() {
        //获取现在皮肤的模式,然后更换背景
        int environment = MyApp.skin.getEnvironment();
        if (environment == Skin.DAY_ENVIRONMENT) {
            ssm.setBackgroundResource(R.drawable.main_bg_day);
        }else{
            ssm.setBackgroundResource(R.drawable.main_bg_night);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 切换fragment来显示内容
     *
     * @param fragment 要显示的fragment
     */
    public void changeFragment(Fragment fragment) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            Fragment f = fragments.get(0);
            if (f instanceof HomeFragment && fragment instanceof HomeFragment) {
                return;
            }
        }
        if (fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).setIsChangeFragment(true);
        }
        if (fragment instanceof ThemeFragment) {
            ((ThemeFragment) fragment).setIsChangeFragment(true);
        }
        //fl_act_main_body
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_act_main_body, fragment);
        ft.commit();
    }

    /**
     * 其他界面要求弹出菜单弹出菜单
     */
    public void onEventPopupMenu() {
//        drawerLayout.openDrawer(Gravity.LEFT);
        ssm.openMenu();
    }

    /**
     * 通过EventBus跨区域调用,达到切换的效果
     */
    public void onEventChangeMode() {
        loadStyle();
        changeShowTextAndIcon();
    }

    /**
     * 改变弹出菜单中的图标和文字
     */
    private void changeShowTextAndIcon() {
        if (MyApp.skin.getEnvironment() == Skin.DAY_ENVIRONMENT) {
            tv_mode.setText("夜晚模式");
            iv_mode.setImageResource(R.mipmap.popup_menu_item_night);
        } else {
            tv_mode.setText("日间模式");
            iv_mode.setImageResource(R.mipmap.popup_menu_item_day);
        }
    }

    /**
     *  界面中右下角的弹出小控件的回调方法
     *
     * @param v        被点击的小菜单,如果不是点击小菜单引起的收缩,值为null
     * @param position 点击的是第几个菜单,如果不是点击小菜单引起的收缩,值为null
     */
    @Override
    public void handle(View v, Integer position) {
        if (v == null || position == null) {
            return;
        }
        //是切换模式
        if (position == 3) {
            if (MyApp.isAutoChangeSkin) { //如果已经开启了自动换肤功能
                stopService(new Intent(this, AutoChangeSkinService.class));
                MyApp.isAutoChangeSkin = false;
                SPUtil.put(context, Constant.SP.settingAct.isAutoChangeSkin, false);
                T.showShort(context, "关闭自动更换皮肤功能");
            }
            //获取显示的模式
            if (MyApp.skin.getEnvironment() == Skin.DAY_ENVIRONMENT) {
                //当前是日间模式,要切换成夜晚模式
                MyApp.skin.loadOwnNightStyle(this);
            } else {
                //当前是夜晚模式,要切换成日间模式
                MyApp.skin.loadOwnDayStyle(this);
            }

        } else if (position == 1) {
            EBus.postEvent("goToTop");
        } else if (position == 2) {
            ActivityUtil.startActivity(this, SettingActivity.class);
        }
    }

    /**
     * 点击返回键的次数
     */
    private int pressBackButtonCount = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean b1 = super.onKeyDown(keyCode, event);
        boolean b2 = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pressBackButtonCount++;
            if (ssm.isMenuOpen()) {
                ssm.closeMenu();
//                drawerLayout.closeDrawer(Gravity.LEFT);
                b2 = false;
                pressBackButtonCount = 0;
            } else if (sm.isMenuOpen()) {
                sm.closeMenu();
                b2 = false;
                pressBackButtonCount = 0;
            } else if (pressBackButtonCount == 1) {
                T.showShort(this, "再按一次退出应用");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pressBackButtonCount = 0;
                    }
                }).start();
                b2 = false;
            }
        }else if(keyCode == KeyEvent.KEYCODE_MENU){
            sm.openMenu();
        }
        return b2;
    }

    /**
     * 处理点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_act_main_menu_tag_home) {
            //切换至主页
            changeToHomeFragment();
//            drawerLayout.closeDrawer(Gravity.LEFT);
            ssm.closeMenu();
        } else if (id == R.id.iv_act_main_menu_collect_tag_icon || id == R.id.tv_act_main_menu_collect_tag_text) {
            ssm.closeMenu();
            ActivityUtil.startActivity(context, MyCollectionActivity.class);
            //关闭菜单
//            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }
}
