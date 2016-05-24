package com.example.cxj.zhihu.common.skin;

/**
 * Created by cxj on 2016/3/25.
 */
public class MySkinActSkin {

    /**
     * 默认是蓝色
     */
    private String titleBarBgColor = "#00A2ED";
//    private String titleBarBgColor = "#222222";

    public int getTitleBarBgColor() {
        return ColorUtil.getColor(titleBarBgColor);
    }

    /**
     * 容器的背景
     */
    private String containerBgColor = "#FFFFFF";
//    private String containerBgColor = "#303030";


    public int getContainerBgColor() {
        return ColorUtil.getColor(containerBgColor);
    }

    /**
     * listview的背景颜色
     */
    private String listBgColor = "#FFFFFF";
//    private String listBgColor = "#303030";


    public int getListBgColor() {
        return ColorUtil.getColor(listBgColor);
    }

    /**
     * 我的皮肤的界面的item选中的时候的背景颜色
     */
    private String selectSkinItemBgColor = "#EAEAEA";
//    private String selectSkinItemBgColor = "#404040";


    public int getSelectSkinItemBgColor() {
        return ColorUtil.getColor(selectSkinItemBgColor);
    }
}
