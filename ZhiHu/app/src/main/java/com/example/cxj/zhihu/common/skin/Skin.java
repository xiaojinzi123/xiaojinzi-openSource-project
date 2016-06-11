package com.example.cxj.zhihu.common.skin;

import android.content.Context;

import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.db.skin.SkinDb;
import com.example.cxj.zhihu.modular.skin.entity.JsonSkin;

import org.json.JSONException;

import java.io.InputStream;

import xiaojinzi.EBus.EBus;
import xiaojinzi.base.android.os.T;
import xiaojinzi.base.android.store.SPUtil;
import xiaojinzi.base.java.common.StringUtil;
import xiaojinzi.json.android.JsonUtil;


/**
 * Created by cxj on 2016/3/14.
 * 应用的皮肤
 */
public class Skin {

    /**
     * 构造函数私有化
     */
    private Skin() {
    }

    private static Skin skin = null;

    /**
     * 白天使用的环境
     */
    public static final int DAY_ENVIRONMENT = 0;

    /**
     * 晚上使用的环境
     */
    public static final int NIGHT_ENVIRONMENT = 1;

    /**
     * 环境
     */
    private int environment = DAY_ENVIRONMENT;

    /**
     * 菜单的皮肤
     */
    public MemuSkin memuSkin = new MemuSkin();

    /**
     * 主页的fragment的皮肤
     */
    public HomeFragmentSkin homeFragmentSkin = new HomeFragmentSkin();

    /**
     * 主题日报的fragment的皮肤
     */
    public ThemeFragmentSkin themeFragmentSkin = new ThemeFragmentSkin();

    /**
     * 详情界面的act皮肤
     */
    public DetailActSkin detailActSkin = new DetailActSkin();

    /**
     * 详情界面的fragment皮肤
     */
    public DetailFragmentSkin detailFragmentSkin = new DetailFragmentSkin();

    /**
     * 评论界面的皮肤
     */
    public CommentActSkin commentActSkin = new CommentActSkin();

    /**
     * 设置界面的皮肤
     */
    public SettingActSkin settingActSkin = new SettingActSkin();

    /**
     * 我的皮肤的界面的皮肤
     */
    public MySkinActSkin mySkinActSkin = new MySkinActSkin();

    /**
     * 在线皮肤界面的皮肤
     */
    public OnLineSkinActSkin onLineSkinActSkin = new OnLineSkinActSkin();

    /**
     * 我的收藏的界面
     */
    public MyCollectionActSkin myCollectionActSkin = new MyCollectionActSkin();

    /**
     * 获取实例对象
     *
     * @return
     */
    public static Skin getInstance() {
        if (skin == null) {
            skin = new Skin();
        }
        return skin;
    }

    /**
     * 加载自带的白天的样式
     *
     * @param context 上下文对象
     */
    public void loadOwnDayStyle(Context context) {
        try {
            if (!loadDownLoadDayStyle(context)) {
                InputStream is = context.getResources().openRawResource(R.raw.day);
                String str = StringUtil.isToStr(is, "UTF-8");
                JsonUtil.parseObjectFromJson(skin, str);
            }
            environment = DAY_ENVIRONMENT;
            SPUtil.put(context, Constant.SP.common.showMode, 0);
            EBus.postEvent(Constant.CHANGEMODE_FLAG);
        } catch (Exception e) {
            T.showShort(context, "更换皮肤失败");
        }
    }

    /**
     * 加载下载的白天皮肤
     *
     * @param context 上下文对象
     * @return
     */
    public boolean loadDownLoadDayStyle(Context context) {
        //获取是否使用了下载的皮肤
        int skinId = SPUtil.get(context, Constant.SP.mySkinAct.usedDaySkinId, -1);
        if (skinId == -1) {
            return false;
        } else {
            JsonSkin jsonSkin = SkinDb.getInstance(context).queryBySkinId(skinId);
            if (jsonSkin == null) {
                //废除使用下载的皮肤
                SPUtil.put(context, Constant.SP.mySkinAct.usedDaySkinId, -1);
                return false;
            } else {
                //获取使用的环境
                int useEnvironment = jsonSkin.getUseEnvironment();
                if (useEnvironment != 0) { //是夜晚使用的
                    return false;
                }
                try {
                    //解析json数据到skin皮肤对象中
                    JsonUtil.parseObjectFromJson(skin, jsonSkin.getSkinJsonData());
                    return true;
                } catch (JSONException e) {
                    return false;
                }
            }
        }
    }

    /**
     * 加载自带的晚上的样式
     *
     * @param context 上下文对象
     */
    public void loadOwnNightStyle(Context context) {
        try {
            if (!loadDownLoadNightStyle(context)) {
                InputStream is = context.getResources().openRawResource(R.raw.night);
                String str = StringUtil.isToStr(is, "UTF-8");
                JsonUtil.parseObjectFromJson(skin, str);
            }
            environment = NIGHT_ENVIRONMENT;
            SPUtil.put(context, Constant.SP.common.showMode, 1);
            EBus.postEvent(Constant.CHANGEMODE_FLAG);
        } catch (Exception e) {
            T.showShort(context, "更换皮肤失败");
        }
    }

    /**
     * 加载下载的白天皮肤
     *
     * @param context 上下文对象
     * @return
     */
    public boolean loadDownLoadNightStyle(Context context) {
        //获取是否使用了下载的皮肤
        int skinId = SPUtil.get(context, Constant.SP.mySkinAct.usedNightSkinId, -1);
        if (skinId == -1) {
            return false;
        } else {
            JsonSkin jsonSkin = SkinDb.getInstance(context).queryBySkinId(skinId);
            if (jsonSkin == null) {
                //废除使用下载的皮肤
                SPUtil.put(context, Constant.SP.mySkinAct.usedNightSkinId, -1);
                return false;
            } else {
                //获取使用的环境
                int useEnvironment = jsonSkin.getUseEnvironment();
                if (useEnvironment == 0) { //是白天使用的
                    return false;
                }
                try {
                    //解析json数据到skin皮肤对象中
                    JsonUtil.parseObjectFromJson(skin, jsonSkin.getSkinJsonData());
                    environment = NIGHT_ENVIRONMENT;
                    return true;
                } catch (JSONException e) {
                    return false;
                }
            }
        }
    }

    /**
     * 设置皮肤的环境
     *
     * @param environment
     */
    public void setEnvironment(int environment) {
        this.environment = environment;
    }

    /**
     * 获取皮肤的环境
     *
     * @return
     */
    public int getEnvironment() {
        return environment;
    }
}
