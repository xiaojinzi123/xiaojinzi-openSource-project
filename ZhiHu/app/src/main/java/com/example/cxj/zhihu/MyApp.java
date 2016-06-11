package com.example.cxj.zhihu;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.cxj.zhihu.common.skin.Skin;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.db.json.JsonCache;
import com.example.cxj.zhihu.db.json.JsonCacheDao;
import com.example.cxj.zhihu.db.skin.SkinDb;
import com.example.cxj.zhihu.modular.skin.entity.JsonSkin;
import com.example.cxj.zhihu.service.AutoChangeSkinService;

import java.util.List;

import xiaojinzi.base.android.store.SPUtil;
import xiaojinzi.base.java.log.L;
import xiaojinzi.base.java.net.HttpRequest;
import xiaojinzi.base.java.net.ResultInfo;
import xiaojinzi.base.java.net.handler.ResponseHandler;
import xiaojinzi.net.AsyncHttp;

import xiaojinzi.net.filter.NetFilter;
import xiaojinzi.net.filter.PdHttpRequest;


/**
 * Created by cxj on 2016/1/19.
 */
public class MyApp extends Application {

    public static final String TAG = "MyApp";

    /**
     * 网络请求的框架
     */
    public static AsyncHttp ah = null;

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
        ah = new AsyncHttp();
        //初始化json缓存器
        initNetCache();
        initCacheJson(this);
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
//        AsyncHttp.initCacheJson(this);
//        ah.addNetFilter(new NetFilter() {
//            @Override
//            public boolean netTaskPrepare(NetTask<?> netTask) {
//                return false;
//            }
//
//            @Override
//            public boolean netTaskBegin(NetTask<?> netTask, ResultInfo<?> resultInfo) {
//                if (loadImageModeData == Constant.loadImageWithWifi && isWifiEable == false) {
//                    String url = netTask.url;
//                    if (url.toLowerCase().endsWith(".jpg") || url.toLowerCase().endsWith(".png")) {
//                        resultInfo.loadDataType = AsyncHttp.BaseDataHandler.ERRORDATA;
//                        return true;
//                    }
//                }
//                return false;
//            }
//
//            @Override
//            public boolean resultInfoReturn(ResultInfo<?> resultInfo) {
//                return false;
//            }
//        });
    }


    /**
     * 设置缓存的拦截器
     */
    public static void initCacheJson(Context context) {

        final boolean isLog = false;

        final JsonCacheDao cacheDao = new JsonCacheDao(context);
        // 添加一个json数据的拦截器

        ah.addNetFilter(new NetFilter() {
            @Override
            public boolean netTaskPrepare(PdHttpRequest<?> netTask) {
                return false;
            }

            @Override
            public boolean netTaskBegin(PdHttpRequest<?> netTask, ResultInfo<?> resultInfo) {
                return false;
            }

            @Override
            public boolean resultInfoReturn(ResultInfo<?> resultInfo) {
                // 获取请求的网址
                String url = resultInfo.httpRequest.getRequesutUrl();

                // 如果请求的数据是字符串
                if (resultInfo.httpRequest.getResponseDataStyle() == ResponseHandler.STRINGDATA && resultInfo.httpRequest.getLoadDataType() == HttpRequest.NET) {

                    // 根据这个url进行数据库的查询
                    List<JsonCache> list = cacheDao.selectByUrl(url);

                    // 如果有记录,就拿到json,塞到结果的对象中
                    if (list == null || list.size() == 0) { //网络访问成功之后,如果数据库中没有,那么就插入,如果有,那么更新一下
                        cacheDao.insert(new JsonCache(null, url, (String) resultInfo.result));
                        if (isLog)
                            L.s(TAG, "缓存成功:" + url);
                    } else {
                        JsonCache jsonCache = list.get(0);
                        jsonCache.setJson((String) resultInfo.result);
                        cacheDao.update(jsonCache);
                    }

                } else if (resultInfo.httpRequest.getResponseDataStyle() == ResponseHandler.ERRORDATA && resultInfo.httpRequest.getPreResponseDataStyle() == ResponseHandler.STRINGDATA) { // 如果是请求错误的情况,并且原来是希望请求字符串类型的数据的

                    // 根据这个url进行数据库的查询
                    List<JsonCache> list = cacheDao.selectByUrl(url);

                    // 如果有记录,就拿到json,塞到结果的对象中
                    if (list != null && list.size() != 0) {
                        resultInfo.result = list.get(0).getJson();
                        resultInfo.httpRequest.setResponseDataStyle(ResponseHandler.STRINGDATA);
                        if (isLog)
                            L.s(TAG, "访问网络失败,但是库中有缓存,使用了缓存数据:" + url);
                    }
                }

                return false;
            }
        });

//        ah.addNetFilter(new NetFilter() {
//            @Override
//            public boolean resultInfoReturn(ResultInfo<?> resultInfo) { // 数据返回,但是还没有处理的时候
//
//
//            }
//
//            @Override
//            public boolean netTaskPrepare(HttpRequest<?> netTask) { // 请求准备的时候
//                return false;
//            }
//
//            @Override
//            public boolean netTaskBegin(HttpRequest<?> netTask, ResultInfo<?> resultInfo) { // 结果对象创建,准备取请求数据,装载到结果对象里面之前

//                // 获取要请求的网址
//                String url = netTask.getRequesutUrl();
//
//                // 如果请求的数据是字符串,并且是使用缓存的
//                if (netTask.responseDataStyle == ResponseHandler.STRINGDATA && netTask.isUseJsonCache) {
//
//                    // 根据这个url进行数据库的查询
//                    List<JsonCache> list = cacheDao.selectByUrl(url);
//
//                    // 如果有记录,就拿到json,塞到结果的对象中
//                    if (list != null && list.size() > 0) {
//                        JsonCache jsonCache = list.get(0);
//                        resultInfo.result = jsonCache.getJson();
//                        resultInfo.loadDataType = ResultInfo.LOCAL;
//
//                        if (isLog)
//                            L.s(TAG, "拦截一次请求,数据库中存在:" + url);
//
//                        // 返回true,表示拦截这次的请求
//                        return true;
//                    }
//                }

//                return false;
//            }
//        });
    }

}
