package com.example.cxj.zhihu;

import android.app.Application;
import android.content.Intent;

import com.example.cxj.zhihu.common.skin.Skin;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.db.skin.SkinDb;
import com.example.cxj.zhihu.modular.skin.entity.JsonSkin;
import com.example.cxj.zhihu.service.AutoChangeSkinService;

import xiaojinzi.base.android.store.SPUtil;
import xiaojinzi.net.AsyncHttp;
import xiaojinzi.net.NetTask;
import xiaojinzi.net.ResultInfo;
import xiaojinzi.net.filter.NetFilter;


/**
 * Created by cxj on 2016/1/19.
 */
public class MyApp extends Application {

    /**
     * 网络请求的框架
     */
    public static AsyncHttp<Void> ah = null;

    /**
     * 应用的皮肤
     */
    public static Skin skin = null;

    /**
     * 是否wifi有用包含两点:
     * 1.连上了wifi
     * 2.有网络连接
     */
    public static boolean isWifiEable = false;

    /**
     * 是否开启自动换肤
     */
    public static boolean isAutoChangeSkin = false;

    /**
     * 加载图片的模式
     */
    public static int loadImageModeData = Constant.loadImageWithAll;

    @Override
    public void onCreate() {
        super.onCreate();
        //加载轻量级存储里面的数据
        initSpData();
        //初始化对象
        ah = new AsyncHttp<Void>();
        //初始化json缓存器
        initNetCache();
    }

    /**
     * 加载轻量级存储里面的数据
     */
    private void initSpData() {
        //加载图片的模式
        int loadImageMode = SPUtil.get(this, Constant.SP.settingAct.loadImageMode, Constant.loadImageWithAll);
        if (loadImageMode == (Constant.loadImageWithWifi)) {
            loadImageModeData = Constant.loadImageWithWifi;
        } else {
            loadImageModeData = Constant.loadImageWithAll;
        }
        //获取wifi是可用
        isWifiEable = SPUtil.get(this, Constant.SP.common.isWifiEable, false);
        //获取是否要自动更换皮肤
        isAutoChangeSkin = SPUtil.get(this, Constant.SP.settingAct.isAutoChangeSkin, false);
        if (isAutoChangeSkin) {
            //开启服务
            startService(new Intent(this, AutoChangeSkinService.class));
        }

        //拿到皮肤控制器
        skin = Skin.getInstance();
        //根据存储的数值加载相应的样式
        int mode = SPUtil.get(this, Constant.SP.common.showMode, 0);
        if (mode == Skin.DAY_ENVIRONMENT) { //如果使用的是白天的皮肤
            skin.loadOwnDayStyle(this);
        } else {
            skin.loadOwnNightStyle(this);
        }
    }

    /**
     * 初始化网络缓存
     */
    private void initNetCache() {
        //添加网络访问过滤器
        AsyncHttp.initCacheJson(this);
        ah.addNetFilter(new NetFilter() {
            @Override
            public boolean netTaskPrepare(NetTask<?> netTask) {
                return false;
            }

            @Override
            public boolean netTaskBegin(NetTask<?> netTask, ResultInfo<?> resultInfo) {
                if (loadImageModeData == Constant.loadImageWithWifi && isWifiEable == false) {
                    String url = netTask.url;
                    if (url.toLowerCase().endsWith(".jpg") || url.toLowerCase().endsWith(".png")) {
                        resultInfo.loadDataType = AsyncHttp.BaseDataHandler.ERRORDATA;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean resultInfoReturn(ResultInfo<?> resultInfo) {
                return false;
            }
        });
    }

}
