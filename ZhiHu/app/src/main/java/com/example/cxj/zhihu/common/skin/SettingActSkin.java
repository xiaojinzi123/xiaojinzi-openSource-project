package com.example.cxj.zhihu.common.skin;

/**
 * Created by cxj on 2016/3/24.
 */
public class SettingActSkin {

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
     * 设置界面的小条目的提示头的文字颜色
     */
    private String itemTagTextColor = "#39ADA3";
//    private String itemTagTextColor = "#DEDEDE";

    public int getItemTagTextColor() {
        return ColorUtil.getColor(itemTagTextColor);
    }

    /**
     * 设置界面的条目的文字颜色
     */
    private String itemTextColor = "#000000";
//    private String itemTextColor = "#AAAAAA";


    public int getItemTextColor() {
        return ColorUtil.getColor(itemTextColor);
    }



}
