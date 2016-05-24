package com.example.cxj.zhihu.common.skin;

/**
 * Created by cxj on 2016/3/14.
 */
public class ThemeFragmentSkin {
    /**
     * 默认是蓝色
     */
    private String titleBarBgColor = "#00A2ED";
//    private String titleBarBgColor = "#222222";


    public int getTitleBarBgColor() {
        return ColorUtil.getColor(titleBarBgColor);
    }

    /**
     * 容器背景颜色
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
     * listview内容的文字颜色
     */
    private String listTextForeColor = "#000000";
//    private String listTextForeColor = "#999999";


    public int getListTextForeColor() {
        return ColorUtil.getColor(listTextForeColor);
    }
}
