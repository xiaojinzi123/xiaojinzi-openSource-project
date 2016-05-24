package com.example.cxj.zhihu.common.skin;

/**
 * Created by cxj on 2016/3/14.
 * 菜单的皮肤
 */
public class MemuSkin {

    /**
     * 默认是蓝色
     */
    private String titleBarBgColor = "#00A2ED";
//    private String titleBarBgColor = "#222222";


    public int getTitleBarBgColor() {
        return ColorUtil.getColor(titleBarBgColor);
    }

    /**
     * 主页的条目的背景
     */
    private String homeItemBgColor = "#F0F0F0";
//    private String homeItemBgColor = "#2C2C2C";


    public int getHomeItemBgColor() {
        return ColorUtil.getColor(homeItemBgColor);
    }

    private String listBgColor = "#FFFFFF";
//    private String listBgColor = "#343434";


    public int getListBgColor() {
        return ColorUtil.getColor(listBgColor);
    }

    /**
     * listview内容的文字颜色
     */
    private String listTextForeColor = "#000000";
//    private String listTextForeColor = "#999999";


    public int getListTextForeColor() {
        return ColorUtil.getColor(listTextForeColor);
    }
}
